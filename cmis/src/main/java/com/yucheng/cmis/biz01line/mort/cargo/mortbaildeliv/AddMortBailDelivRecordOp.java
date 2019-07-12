package com.yucheng.cmis.biz01line.mort.cargo.mortbaildeliv;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.mort.component.MortCommenOwnerComponent;
import com.yucheng.cmis.biz01line.mort.morttool.MORTConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddMortBailDelivRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "MortBailDeliv";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String serno = "";
		try{
			connection = this.getConnection(context);
			KeyedCollection kColl = null;
			//构建组件
			MortCommenOwnerComponent mortCom = (MortCommenOwnerComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(MORTConstant.MORTCOMMENOWNERCOMPONENT,context,connection);
			kColl = (KeyedCollection)context.getDataElement(modelId);
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			serno = (String) kColl.getDataValue("serno");
			TableModelDAO dao = this.getTableModelDAO(context);
			if(serno==null||"".equals(serno)){//新增
				serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
				kColl.setDataValue("serno",serno);
				dao.insert(kColl, connection);
			}else{//修改
				dao.update(kColl, connection);
				Map<String, String> map = new HashMap<String, String>();
				map.put("serno", serno);
				//mortCom.deleteByField("MortCargoReplList",map);
			}
			//6--提货
			//mortCom.insertMortDelivListByStatus(serno,"6","05",(String) kColl.getDataValue("guaranty_no"));
			//mortCom.insertMortDelivListByStatus(serno,"6","03",(String) kColl.getDataValue("guaranty_no"));
			//给提货清单表中关联业务流水号设值。
			mortCom.updateMortDelivListOper(serno,(String)kColl.getDataValue("guaranty_no"),"2");
			context.addDataField("flag","success");
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
