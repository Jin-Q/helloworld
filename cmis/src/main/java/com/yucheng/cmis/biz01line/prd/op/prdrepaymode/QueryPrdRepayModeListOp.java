package com.yucheng.cmis.biz01line.prd.op.prdrepaymode;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryPrdRepayModeListOp extends CMISOperation {


	private final String modelId = "PrdRepayMode";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			/*RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);*/

			KeyedCollection queryData = null;
			String prd_id = "";
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
		    
		    
		    String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			
		    if(context.containsKey("prd_id")){
				try{
			    	prd_id = (String)context.getDataValue("prd_id");
			    }catch(Exception e){
			    	throw new Exception("产品编号获取失败!");
			    }
			    if(!"none".equals(prd_id)){
			    	if("".equals(conditionStr) || conditionStr == null){
				    	conditionStr = "where repay_mode_id in (select repay_mode_id from R_Prd_Repaymode where prdid='"+prd_id+"')  order by repay_mode_id desc";
				    }else{
				    	conditionStr += " and repay_mode_id in (select repay_mode_id from R_Prd_Repaymode where prdid='"+prd_id+"')  order by repay_mode_id desc";
				    }
			    }
			}
		    
		    int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List<String> list = new ArrayList<String>(); 
			list.add("repay_mode_id");
			list.add("repay_mode_dec");
			list.add("repay_mode_type");
			list.add("min_term");
			list.add("max_term");
			list.add("repay_interval");
			list.add("firstpay_perc");
			list.add("lastpay_perc");
			list.add("is_instm");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
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
