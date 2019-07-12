package com.yucheng.cmis.biz01line.acc.op.accdrft;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;
/**
 * 票据明细的excel导出
 * @author Administrator
 *
 */
public class AccDrftGroupExpBatchToExcel extends CMISOperation {


	private final String modelId = "AccDrft";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			String porder_no = "";
			try {
				porder_no = (String)context.getDataValue("porder_no"); 
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			String conditionStr ="";
		    if(queryData!=null){
		         conditionStr = TableModelUtil.getQueryCondition_bak(this.modelId, queryData, context, false, true, false);
				                       
		    }
		    if(porder_no != null && !"".equals(porder_no)){
		    	if(conditionStr.equals("")){
			    	conditionStr = " where porder_no='"+porder_no+"'";
			    }else{
			    	conditionStr = conditionStr + " and porder_no='"+porder_no+"'";
			    }
		    }
		    conditionStr = conditionStr+"order by dscnt_date desc";
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,connection);
			for(Object obj:iColl){
				KeyedCollection kColl = (KeyedCollection)obj;
				KeyedCollection kColl4d = dao.queryDetail("IqpBillDetail", (String)kColl.getDataValue("porder_no"), connection);
				kColl.put("drft_amt", kColl4d.getDataValue("drft_amt"));
			}
			iColl.setName(iColl.getName()+"List");
			String[] args=new String[] {"prd_id" };
			String[] modelIds=new String[]{"PrdBasicinfo"};
			String[]modelForeign=new String[]{"prdid"};
			String[] fieldName=new String[]{"prdname"}; 
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			
		    this.putDataElement2Context(iColl, context);
			
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
