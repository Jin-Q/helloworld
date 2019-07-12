package com.yucheng.cmis.biz01line.lmt.op.bizarea.lmtappnamelist;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;

public class AddNameToAppListOp extends CMISOperation {
	
	private final String modelId = "LmtAppNameList";
	private final String modelIdNameList = "LmtNameList";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String returnFlag = "";
			String serno = "";//入/退圈申请编号
			String cus_id = "";
			String agr_no = "";//LmtNameList表中协议编号
			try {
				serno = (String)context.getDataValue("serno");
				cus_id = (String)context.getDataValue("cus_id");
				agr_no = (String)context.getDataValue("agr_no");
			} catch (Exception e) {}
			if(serno == null||"".equals(serno))
				throw new EMPJDBCException("The values [serno] cannot be empty!");
			
			if(cus_id == null||"".equals(cus_id))
				throw new EMPJDBCException("The values [cus_id] cannot be empty!");
		
			if(agr_no == null||"".equals(agr_no))
				throw new EMPJDBCException("The values [agr_no] cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			String condition = " where serno='"+serno+"' and cus_id='"+cus_id+"'";
			IndexedCollection iColl = dao.queryList(modelId, condition, connection);
			if(iColl.size()>0){
				returnFlag = "该客户已存在该笔申请中！";
			}else {
				//查询名单信息并放入退圈申请名单中
				String condition1 = " where agr_no='"+agr_no+"' and cus_id='"+cus_id+"'";
				IndexedCollection iCollNameList = dao.queryList(modelIdNameList, condition1, connection);
				if(iCollNameList.size()>0){
					KeyedCollection kCollNameList = (KeyedCollection)iCollNameList.get(0);
					kCollNameList.addDataField("serno", serno);
					kCollNameList.remove("agr_no");
					kCollNameList.setName(modelId);
					dao.insert(kCollNameList, connection);
					returnFlag = PUBConstant.SUCCESS;
				}else {
					throw new Exception("客户["+cus_id+"]不存在于该商圈中！");
				}
			}
			context.addDataField("flag", returnFlag);
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
