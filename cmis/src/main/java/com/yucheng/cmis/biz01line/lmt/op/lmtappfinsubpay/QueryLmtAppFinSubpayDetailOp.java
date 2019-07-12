package com.yucheng.cmis.biz01line.lmt.op.lmtappfinsubpay;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryLmtAppFinSubpayDetailOp  extends CMISOperation {
	
	private final String modelId = "LmtAppFinSubpay";
	
	private final String serno_name = "serno";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
//			RecordRestrict recordRestrict = this.getRecordRestrict(context);
//			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {
				throw new Exception("业务流水号为空!");
			}
		
			TableModelDAO dao = this.getTableModelDAO(context);
			
//			if(serno_value == null || "".equals("serno_value")){
//				String cus_id = (String) context.getDataValue("cus_id");
//				IndexedCollection ic = dao.queryList(modelId, " where cus_id = '" + cus_id + "'", connection);
//				KeyedCollection kc = null;
//				if (ic != null && ic.size() > 0) {
//					kc = (KeyedCollection) ic.get(0);
//					kc.setName(modelId);
//					String[] args = new String[] { "cus_id" };
//					String[] modelIds = new String[] { "CusBase" };
//					String[] modelForeign = new String[] { "cus_id" };
//					String[] fieldName = new String[] { "cus_name" };
//					// 详细信息翻译时调用
//					SystemTransUtils.dealName(kc, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
//
//					kc.getDataValue("input_id");
//					kc.getDataValue("input_br_id");
//					SInfoUtils.addSOrgName(kc, new String[] { "input_br_id", "manager_br_id" });
//					SInfoUtils.addUSerName(kc, new String[] { "input_id", "manager_id" });
//					serno_value=kc.getDataValue(serno_name).toString();
//					IndexedCollection iColl4List = dao.queryList("LmtSubpayList", "where serno ='"+serno_value+"'", connection);
//					this.putDataElement2Context(iColl4List, context);
//				} else {
//					kc = new KeyedCollection(modelId);
//				}
//				this.putDataElement2Context(kc, context);
//			}else{
//				IndexedCollection iColl4List = dao.queryList("LmtSubpayList", "where serno ='"+serno_value+"'", connection);
//				this.putDataElement2Context(iColl4List, context);
				
				IndexedCollection iColl4List = SqlClient.queryList4IColl("getSubpayBillInfoBySerno", serno_value, connection);
				iColl4List.setName("LmtSubpayList");
				String[] args=new String[] { "prd_id" };
				String[] modelIds=new String[]{"PrdBasicinfo"};
				String[] modelForeign=new String[]{"prdid"};
				String[] fieldName=new String[]{"prdname"};
				SystemTransUtils.dealName(iColl4List, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
				this.putDataElement2Context(iColl4List, context);
				
				KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
				args=new String[] { "cus_id" };
				modelIds=new String[]{"CusBase"};
				modelForeign=new String[]{"cus_id"};
				fieldName=new String[]{"cus_name"};
				SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
				kColl.getDataValue("input_id");
				kColl.getDataValue("input_br_id");
				SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id", "manager_br_id" });
				SInfoUtils.addUSerName(kColl, new String[] { "input_id", "manager_id" });
				
				this.putDataElement2Context(kColl, context);
//			}
			
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
