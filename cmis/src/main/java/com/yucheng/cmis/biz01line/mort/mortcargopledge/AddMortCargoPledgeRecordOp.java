package com.yucheng.cmis.biz01line.mort.mortcargopledge;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class AddMortCargoPledgeRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "MortCargoPledge";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			if(context.containsKey("action")){
				String action = (String) context.getDataValue("action");
				if("zh".equals(action)){
				//	kColl.setName("MortCargoReplList");
				}
			}
			//2014-01-09注释
			/*if(context.containsKey("storage_mode")){
				if("01".equals(context.getDataValue("storage_mode"))){//补货入库
					kColl.addDataField("serno","2");//临时的serno（为了拼接主键，保存时，会被有意义的业务流水号替代）
					kColl.addDataField("oper","2");
					kColl.setName("MortCargoReplList");
				}
			}
			*/
			//add a record
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(kColl, connection);
			context.addDataField("flag","success");
			context.addDataField("msg","");
/*			String guaranty_no =(String) kColl.getDataValue("guaranty_no");
			String guaranty_catalog =(String) kColl.getDataValue("guaranty_catalog");
			String produce_vender =(String) kColl.getDataValue("produce_vender");//生产厂家
			String produce_area =(String) kColl.getDataValue("produce_area");//产地
			String sale_area =(String) kColl.getDataValue("sale_area");//销售区域

			String condStr = "where cargo_id in (select cargo_id from Mort_Cargo_Pledge where guaranty_no ='"+guaranty_no+"')";
			//遍历该押品编号下存在的所有货物信息
			IndexedCollection ic = dao.queryList(modelId, condStr, connection);
			//不存在货物记录时，可以新增任何押品所属目录下的货物
			if(null==ic){
				dao.insert(kColl, connection);
				context.addDataField("flag","success");
				context.addDataField("msg","");
			}else{
				int count = 0;//用来记录是否已存在将要新增的所属押品目录下的记录
				for(int i=0;i<ic.size();i++){
					KeyedCollection kc = (KeyedCollection) ic.get(i);
					if(guaranty_catalog.trim().equals(kc.getDataValue("guaranty_catalog"))&&
							(produce_vender.trim().equals(kc.getDataValue("produce_vender")))&&
							(produce_area.trim().equals(kc.getDataValue("produce_area")))&&
							(sale_area.trim().equals(kc.getDataValue("sale_area"))))
					{
						context.addDataField("flag","fail");
						context.addDataField("msg","已存在此押品目录下，相同生产厂家、产地、销售区域的货物，不可以重复进行新增！");
						break;
					}else{
						count++;
					}
				}
				if(count==ic.size()){//遍历结束后不存在
					dao.insert(kColl, connection);
					context.addDataField("flag","success");
					context.addDataField("msg","");
				}
			}
	*/
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
