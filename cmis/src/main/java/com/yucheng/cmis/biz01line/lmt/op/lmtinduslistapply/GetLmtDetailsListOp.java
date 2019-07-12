package com.yucheng.cmis.biz01line.lmt.op.lmtinduslistapply;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class GetLmtDetailsListOp extends CMISOperation {
	
	private final String modelIdAppDet = "LmtAppDetails";//授信分项表
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			String serno = "";
			
			if(context.containsKey("serno")){
				serno = (String) context.getDataValue("serno");
			}else{
				serno = (String) context.getDataValue("agr_no");
				KeyedCollection agrKcoll = dao.queryDetail("LmtIndusAgr", serno, connection); //行业授信协议
				serno = agrKcoll.getDataValue("serno").toString();
			}
			
			String cus_id = (String) context.getDataValue("cus_id");
			
			//查询额度分项信息
			String condition = " where serno='"+serno+"' and cus_id='"+cus_id+"' order by limit_code desc"; 
			IndexedCollection iCollAppDet = dao.queryList(modelIdAppDet, condition, connection);
			iCollAppDet.setName(modelIdAppDet+"List");
			this.putDataElement2Context(iCollAppDet, context);
			
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