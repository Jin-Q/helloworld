package com.yucheng.cmis.biz01line.lmt.op.intbank.lmtbatchlmt;

import java.sql.Connection;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
public class SaveSignCusLmtOp extends CMISOperation {
	
	private final String modelId = "LmtSigLmt";
	@Override
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		KeyedCollection kColl =null;
		KeyedCollection kColl_temp =null;
		try{
			connection = this.getConnection(context);			
			TableModelDAO dao = this.getTableModelDAO(context);
			kColl = (KeyedCollection)context.getDataElement(modelId);
			if(kColl == null || kColl.size() == 0){
				throw new EMPException("获取表单数据失败！");
			}
			
			String serno_value= (String)kColl.getDataValue("serno");
			
			IndexedCollection iColl_batch = dao.queryList("LmtSigLmt", "where serno='"+serno_value+"'", connection);
			//如果在已经进行批量授信，那么进行修改授信信息。否则，新增一条记录。
			if(iColl_batch.size()>0){
				dao.update(kColl, connection);
			}else{
				dao.insert(kColl, connection);
			}		
			
			String lmt_amt = (String)kColl.getDataValue("lmt_amt");
			IndexedCollection iColl_temp = dao.queryList("LmtSubApp", "where serno='"+serno_value+"'", connection);
			if(iColl_temp != null && iColl_temp.size() > 0){
				for(int i=0;i<iColl_temp.size();i++){
					kColl_temp = (KeyedCollection) iColl_temp.get(i);
					kColl_temp.setDataValue("lmt_amt", lmt_amt);
					dao.update(kColl_temp, connection);
				}
			}
			
			//dao.insert(kColl, connection);
			context.addDataField("flag", "success");				
		}catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}
