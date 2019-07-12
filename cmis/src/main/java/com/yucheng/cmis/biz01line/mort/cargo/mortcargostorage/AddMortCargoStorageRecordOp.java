package com.yucheng.cmis.biz01line.mort.cargo.mortcargostorage;

import java.sql.Connection;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.mort.component.MortCommenOwnerComponent;
import com.yucheng.cmis.biz01line.mort.morttool.MORTConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddMortCargoStorageRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "MortCargoStorage";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String serno = "";
			String flg ="";//标志位，2--记账入库
			KeyedCollection kColl = null;
			//add a record
			TableModelDAO dao = this.getTableModelDAO(context);
			//构建组件
			MortCommenOwnerComponent mortCom = (MortCommenOwnerComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(
					MORTConstant.MORTCOMMENOWNERCOMPONENT,context,connection);
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
				if(context.containsKey("serno")){
					serno = (String) context.getDataValue("serno");
					//设置更新操作时主键
					kColl.setDataValue("serno",serno);
					Map<String, String> map = new HashedMap();
					map.put("serno", serno);
					mortCom.deleteByField("MortCargoReplList",map);
				}else{
					serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
					//设置主键
					kColl.setDataValue("serno",serno);
				}
				
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			//入库方式
			String storage_mode = (String) kColl.getDataValue("storage_mode");
			//首次入库时
			if("00".equals(storage_mode)){
			//存入货物清单历史数据，查看列表数据时显示。
				//mortCom.insertMortDelivListByStatus(serno,"1","01",(String) context.getDataValue("guaranty_no"));
				mortCom.insertMortDelivListByStatus(serno,"1","04",(String) context.getDataValue("guaranty_no"));
			}else if("01".equals(storage_mode)){//补货入库
				mortCom.insertMortDelivListByStatus(serno,"2","04",(String) context.getDataValue("guaranty_no"));
				//mortCom.insertMortDelivListByStatus(serno,"2","01",(String) context.getDataValue("guaranty_no"));
			}
			if(context.containsKey("flg")){//入库记账时，批量更改货物的状态，改为入库状态。（入库记账时，所需要做的操作）
				if("2".equals(context.getDataValue("flg").toString().trim())){
					String guaranty_no = (String) context.getDataValue("guaranty_no");
					mortCom.updateStatusBatch("04","02",guaranty_no,context);//将货物批量设置为入库状态
					kColl.setDataValue("status","01");//设置记账状态为已记账
					kColl.setDataValue("tally_date", context.getDataValue("OPENDAY"));//设置记账日期为当前日期
					//入库流水状态改为入库
					mortCom.updateStatusBatchCargoRepl("04","02", guaranty_no, serno);
				}
				
			}
			
			int count = 0;
			try{
				if(context.containsKey("serno")){
				 count = dao.update(kColl, connection);
				}else{	
				 count = dao.insert(kColl, connection);
				}
			}catch(Exception e){
				EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.DEBUG, 0, "入库操作失败！");
				throw new ComponentException(CMISMessage.MESSAGEDEFAULT,"入库操作失败！请重新操作！");
			}
			if(count==1){
				context.addDataField("flag","success");		
			}else {
				context.addDataField("flag","fail");		
			}
				
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
