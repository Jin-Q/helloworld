package com.yucheng.cmis.biz01line.iqp.op.iqpbailsubdis;

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
public class IqpCheckBailExistOp extends CMISOperation {

	private final String modelId = "IqpBailSubDis";
	private final String modelIdPubBail = "PubBailInfo";
	

	private final String cont_no = "cont_no";
	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String cont_no = null;
			try {
				cont_no = (String)context.getDataValue("cont_no");
			} catch (Exception e) {
				EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "Kcoll["+modelId+"] can't find in context");				
			}
			if(cont_no == null || cont_no.length() == 0)
				throw new EMPJDBCException("The value of pk["+cont_no+"] cannot be null!");
			TableModelDAO dao = this.getTableModelDAO(context);

			IndexedCollection iColl = dao.queryList(modelId, "where cont_no='"+cont_no+"'", connection);
			if(!(iColl.size()>0))
			{
				context.addDataField("result", "false");
			}else{   
				KeyedCollection kc=null;
				int count = 0;
				for(int i=0;i<iColl.size();i++){
					kc = (KeyedCollection) iColl.get(i);
					String status = (String) kc.getDataValue("approve_status");
					if(status.equals("997")||status.equals("998")){//办结状态
						count++;
					}
				}
				if(count==iColl.size()){//遍历完所有的记录，全是已经办结
					context.addDataField("result", "false");
				}else{//遍历完所有的记录，存在在途
					context.addDataField("result", "true");
				}
			}
			//判断该笔业务是否存在保证金，如果没有，则不让做保证金提取操作
			String conditon = "where cont_no='"+cont_no+"'";
			IndexedCollection iCollPubBail = dao.queryList(modelIdPubBail, conditon, connection);
			String flag = (String)context.getDataValue("flag");
			if(iCollPubBail.size()<=0 && "2".equals(flag)){
				context.put("result", "canNot");
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
