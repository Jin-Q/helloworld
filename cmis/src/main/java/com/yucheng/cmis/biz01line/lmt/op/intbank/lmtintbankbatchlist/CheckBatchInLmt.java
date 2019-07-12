package com.yucheng.cmis.biz01line.lmt.op.intbank.lmtintbankbatchlist;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;

public class CheckBatchInLmt extends CMISOperation {
	
	/**
	 * 检查符合条件的批量包是否已经在授信
	 * 批量授信时过滤已在授信的批量包
	 * */
	private final String modelId = "LmtBatchLmt";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			String status = "";
			connection = this.getConnection(context);
			String batch_no_value = context.getDataValue("batch_cus_no").toString();					
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			String condition = "where batch_cus_no='"+batch_no_value+"'";		
			IndexedCollection iColl = dao.queryList(modelId,condition,connection);
			//判断是否有已发起的记录
			if(iColl.size()>0){
				KeyedCollection kColl = (KeyedCollection)iColl.get(0);
				status = kColl.getDataValue("approve_status").toString();
				if(!"997".equals(status)){
					context.addDataField("flag", PUBConstant.SUCCESS);
					if("000".equals(status)){
						context.addDataField("message", "该批量客户包存在状态为'待发起'的授信，不能重复发起！");
					}
					if("111".equals(status)){
						context.addDataField("message", "该批量客户包已授信，不能重复发起！");
					}
				}else{
					context.addDataField("flag", PUBConstant.FAIL);
					context.addDataField("message", "该批量客户包已授信，不能重复发起！");
				}
			}else{
				IndexedCollection correiColl=dao.queryList("LmtBatchCorre", "where batch_cus_no='"+batch_no_value+"'", connection);
				if(correiColl.isEmpty()){
					context.addDataField("flag", PUBConstant.FAIL);
					context.addDataField("message", "该批量客户包没有客户,不能进行有效的授信!请重新选择批量包！");
				}else{
					context.addDataField("flag", "true");
					context.addDataField("message", "");
				}
				
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
