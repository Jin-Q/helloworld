package com.yucheng.cmis.biz01line.iqp.op.iqpchkstoretask;

import java.sql.Connection;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckIqpChkStoreTaskOp extends CMISOperation {

	private final String modelId = "IqpChkStoreTask";
	private final String modelId1 = "IqpChkStoreSet";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = "success";
		String msg = "";
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			String cus_id = (String)context.getDataValue("cus_id");//客户号
			String task_set_id = (String)context.getDataValue("task_set_id");//流水号
			
			//获取批次信息
			String condition = " where cus_id = '"+cus_id+"' and input_date = '"+context.getDataValue("OPENDAY")+"'";
			IndexedCollection iColl = dao.queryList(modelId,condition, connection);
			KeyedCollection kcoll = dao.queryDetail(modelId1, task_set_id, connection);
			if(iColl.size()>0){
				flag = "failure";
				msg = "该客户已存在当天任务！";
			}else{
				if(kcoll!=null&&kcoll.size()>0){
					if(kcoll.containsKey("task_request_time")&&kcoll.getDataValue("task_request_time")!=null&&!"".equals("task_request_time")){
						return "0";
					}else{
						flag = "failure";
						msg = "请先保存，再生成任务！";
					}
				}else{
					flag = "failure";
					msg = "请先保存，再生成任务！";
				}
			}
		}catch (EMPException ee) {
			flag = "failure";
			msg = "校验失败！";
			throw ee;
		}  catch(Exception e){
			throw new EMPException(e);
		} finally {
			context.addDataField("flag", flag);
			context.addDataField("msg", msg);
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}