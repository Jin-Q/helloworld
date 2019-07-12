package com.yucheng.cmis.biz01line.iqp.op.iqpaverageassetapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckIqpAverageAssetAppOp extends CMISOperation {

	private final String modelId = "IqpAverageAssetApp";//资产卖出登记申请表
	private final String modelIdAverageAsset = "IqpAverageAsset";//资产卖出表
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String bill_no = null;
			try {
				bill_no = (String)context.getDataValue("bill_no");
			} catch (Exception e) {}
			if(bill_no == null || bill_no.length() == 0)
				throw new EMPJDBCException("The value bill_no cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			String condition = "where bill_no = '"+bill_no+"' and approve_status!='998' ";
			IndexedCollection iColl = dao.queryList(modelId, condition, connection);
			if(iColl.size()==0){
				String condiditonStr = "where bill_no='"+bill_no+"'";
				KeyedCollection kColl = dao.queryFirst("AccView", null, condiditonStr, connection);
				String prd_id = (String) kColl.getDataValue("prd_id");
				if(prd_id.equals("600021")||prd_id.equals("600022")||prd_id.equals("600020")){
					context.addDataField("flag", "error");
					context.addDataField("msg", "资产借据不能再做资产业务!");
				}else if(prd_id.equals("100063")||prd_id.equals("100065")){
					context.addDataField("flag", "error");
					context.addDataField("msg", "委托贷款不能做资产业务!");
				}else{
					context.addDataField("flag", "success");
					context.addDataField("msg", "");
				}
			}else{
				String conditionStr = "where bill_no = '"+bill_no+"' and average_status='2'";
				IndexedCollection iCollAverageAsset = dao.queryList(modelIdAverageAsset, conditionStr, connection);
				if(iCollAverageAsset.size()>0){
					context.addDataField("flag", "success");
					context.addDataField("msg", "");
				}else{
					context.addDataField("flag", "error");
					context.addDataField("msg", "该笔借据已做过卖出资产登记!");
				}
			}
		}catch (EMPException ee) {
			context.addDataField("flag", "error");
			context.addDataField("msg", "该笔借据已做过卖出资产登记，失败原因："+ee.getMessage());
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
