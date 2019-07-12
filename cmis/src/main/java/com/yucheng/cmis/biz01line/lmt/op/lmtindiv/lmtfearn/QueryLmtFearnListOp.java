package com.yucheng.cmis.biz01line.lmt.op.lmtindiv.lmtfearn;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryLmtFearnListOp extends CMISOperation {


	private final String modelId = "LmtFearn";
	
	private final String modelIdpay = "LmtFpayout";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			KeyedCollection queryDatapay = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
				queryDatapay = (KeyedCollection)context.getDataElement(this.modelIdpay);
			} catch (Exception e) {}
			
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
									
			String serno = (String)context.getDataValue("serno");
			if(conditionStr == null || "".equals(conditionStr)){
				conditionStr="where serno ='"+serno+"'order by serno desc";
			}else{
				conditionStr +="and serno ='"+serno+"'order by serno desc";
			}
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			List<String> list = new ArrayList<String>();
			list.add("cus_id");
			list.add("cus_attr");
			list.add("earning_sour");
			list.add("mearn_score");
			list.add("ibank_mearn");
			list.add("yearn_score");
			list.add("ibank_yearn");
			IndexedCollection iColl = dao.queryList(modelId,list,conditionStr,connection);
			iColl.setName(iColl.getName()+"List");
			
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
			this.putDataElement2Context(iColl, context);
			
			String conditionStr1 = TableModelUtil.getQueryCondition(this.modelId, queryDatapay, context, false, false, false);

			if(conditionStr1 == null || "".equals(conditionStr1)){
				conditionStr1="where serno ='"+serno+"'order by serno desc";
			}else{
				conditionStr1 +="and serno ='"+serno+"'order by serno desc";
			}
			
			List<String> listpay = new ArrayList<String>();
			listpay.add("cus_id");
			listpay.add("cus_attr");
			listpay.add("fpayout_type");
			listpay.add("mpayout");
			listpay.add("ypayout");
			IndexedCollection ic = dao.queryList(modelIdpay,listpay,conditionStr1,connection);
			ic.setName(ic.getName()+"List");
			
			String[] args1=new String[] { "cus_id" };
			String[] modelIds1=new String[]{"CusBase"};
			String[] modelForeign1=new String[]{"cus_id"};
			String[] fieldName1=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(ic, args1, SystemTransUtils.ADD, context, modelIds1, modelForeign1, fieldName1);
			
			this.putDataElement2Context(ic, context);
			
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
