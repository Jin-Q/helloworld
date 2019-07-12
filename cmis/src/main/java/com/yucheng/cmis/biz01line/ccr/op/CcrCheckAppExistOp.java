package com.yucheng.cmis.biz01line.ccr.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
/**
 * 异步查询客户信息是否在明细表中存在OP类
 * @author Administrator
 *
 */
public class CcrCheckAppExistOp extends CMISOperation {

	private final String modelId = "CcrAppInfo";
	

	private final String serno_name = "cus_id";
	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String cusId = null;
			try {
				cusId = (String)context.getDataValue("cus_id");
			} catch (Exception e) {
				EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "Kcoll["+modelId+"] can't find in context");				
			}
			if(cusId == null || cusId.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			TableModelDAO dao = this.getTableModelDAO(context);

			IndexedCollection iColl = dao.queryList(modelId, "where cus_id='"+cusId+"'", connection);
			if(!(iColl.size()>0))
			{
				context.addDataField("ccr_result", "false");
				context.addDataField("ccr_message","");
			}else{   
				
				KeyedCollection kc=null;
				int count = 0;
				for(int i=0;i<iColl.size();i++){
					kc = (KeyedCollection) iColl.get(i);
					String status = (String) kc.getDataValue("approve_status");
					if(status.equals("997")||status.equals("998")){//办结状态的评级
						count++;
					}
				}
				if(count==iColl.size()){//遍历完所有的记录，全是已经办结完成的评级
					context.addDataField("ccr_result", "false");
					context.addDataField("ccr_message","");
				}else{//遍历完所有的记录，存在在途的评级
					context.addDataField("ccr_result", "true");
					context.addDataField("ccr_message","该客户存在在途的信用评级,在评级结束之前不允许重复评级");
				}
			}						
			
			return null;
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}

	}

}
