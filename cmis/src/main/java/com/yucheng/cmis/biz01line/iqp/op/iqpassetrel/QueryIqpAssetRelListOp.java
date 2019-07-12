package com.yucheng.cmis.biz01line.iqp.op.iqpassetrel;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIqpAssetRelListOp extends CMISOperation {


	private final String modelId = "IqpAssetRel";
	private final String asModelId = "IqpAsset";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
									
			String asset_no = "";
			if(context.containsKey("asset_no")){
				asset_no = (String)context.getDataValue("asset_no");
			}
			if(conditionStr.trim().length() == 0 || conditionStr == null){
				conditionStr = " where asset_no = '"+asset_no+"'";
			}else {
				conditionStr = conditionStr +" and asset_no = '"+asset_no+"'";
			}
			int size = 10;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List<String> list = new ArrayList<String>();
			list.add("cus_id");
			list.add("pk_id");
			//list.add("cus_name");
			list.add("cont_no");
			list.add("bill_no");
			list.add("guar_type");
			list.add("loan_start_date");
			list.add("loan_end_date");
			list.add("asset_no");
			list.add("loan_bal");
			list.add("takeover_amt");
			/**add by lisj 2014-11-27 资产包管理，资产清单列表增加币种字段 begin**/
			list.add("cur_type");
			/**add by lisj 2014-11-27 资产包管理，资产清单列表增加币种字段 end**/
			
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			
			/** 客户名称通过客户码翻译   2014-08-01  */
			String[] args=new String[] {"cus_id"};
			String[] modelIds=new String[]{"CusBase"};
			String[]modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
		    //客户码加客户名称翻译		
		    SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
		    /** END */
		    
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);

			KeyedCollection asKColl = dao.queryDetail(asModelId, asset_no, connection);
			if(context.containsKey("asset_qnt")){
				context.setDataValue("asset_qnt", asKColl.getDataValue("asset_qnt"));
			}else{
				context.addDataField("asset_qnt", asKColl.getDataValue("asset_qnt"));
			}
			if(context.containsKey("asset_total_amt")){
				context.setDataValue("asset_total_amt", asKColl.getDataValue("asset_total_amt"));
			}else{
				context.addDataField("asset_total_amt", asKColl.getDataValue("asset_total_amt"));
			}
			if(context.containsKey("takeover_total_amt")){
				context.setDataValue("takeover_total_amt", asKColl.getDataValue("takeover_total_amt"));
			}else{
				context.addDataField("takeover_total_amt", asKColl.getDataValue("takeover_total_amt"));
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
