package com.yucheng.cmis.biz01line.iqp.op.iqpassettranslist;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIqpAssetTransListListOp extends CMISOperation {


	private final String modelId = "IqpAssetTransList";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			String serno = (String) context.getDataValue("serno");
			
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			if(conditionStr.equals("")){
				conditionStr = " where serno = '"+serno+"'";
			}else{
				conditionStr = conditionStr + " and serno = '"+serno+"'";
			}
			conditionStr = conditionStr+" order by serno desc,bill_no desc";
			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,pageInfo,connection);
			IndexedCollection iColl4Show = new IndexedCollection();
			
			iColl4Show.setName(iColl.getName()+"List");
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				String bill_no = (String)kColl.getDataValue("bill_no");
				
				/**  客户码字段取值从贷款台账获取   2014-08-01  唐顺岩   */
				//String condition = "where bill_no='"+bill_no+"' and approve_status='997'";
				//KeyedCollection kCollIqpAsset = dao.queryFirst("IqpAssetRegiApp", null, condition, connection);
				String condition = "WHERE BILL_NO='"+bill_no+"'";
				KeyedCollection kCollIqpAsset = dao.queryFirst("AccLoan", null, condition, connection);
				/** END */
				String cus_id = (String)kCollIqpAsset.getDataValue("cus_id");
				kColl.put("cus_id", cus_id);
				iColl4Show.addDataElement(kColl);
			}
			String[] args=new String[] {"cus_id"};
			String[] modelIds=new String[]{"CusBase"};
			String[]modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
		    SInfoUtils.addSOrgName(iColl, new String[]{"manager_br_id","fina_br_id"});
			
			this.putDataElement2Context(iColl4Show, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
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
