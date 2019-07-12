package com.yucheng.cmis.biz01line.lmt.op.jointguar.lmtnamelist_joint;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;

public class ChkBeforeAddJointCoopNameRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "LmtAppNameList";
	private final String modelIdNl = "LmtNameList";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String cus_id = null;//客户码
			String serno = null;//联保申请编号
			try {
				cus_id = (String)context.getDataValue("cus_id");
			} catch (Exception e) {}
			if(cus_id == null || "".equals(cus_id))
				throw new EMPJDBCException("客户码为空无法校验!");
			
			try {
				serno = (String)context.getDataValue("serno");
			} catch (Exception e) {}
//			if(serno == null || "".equals(serno))
//				throw new EMPJDBCException("联保申请编号为空无法校验!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			//校验该客户是否已存在该申请中
			if(serno!=null&&!"".equals(serno)){
				String condition = " where serno='"+serno+"' and cus_id='"+cus_id+"'"; 
				IndexedCollection iCollThis = dao.queryList(modelId, condition, connection);
				if(iCollThis.size()>0){
					context.addDataField("flag", "existThis");
					context.addDataField("returnMsg", "该客户已存在该笔申请中，不能重复新增！");
					return "0";
				}
			}
			//判断是否存在有效联保小组名单中
			String condition0 = " where cus_id='"+cus_id+"' and cus_status='1' and agr_no in (select agr_no from lmt_agr_joint_coop)";
			IndexedCollection iCollNl = dao.queryList(modelIdNl, condition0, connection);
			if(iCollNl.size()>0){
				String strTmp = "";//联保协议编号
				for(int i=0;i<iCollNl.size();i++){
					KeyedCollection kCollNl = (KeyedCollection)iCollNl.get(i);
					strTmp += "["+(String)kCollNl.getDataValue("agr_no")+"]";
				}
				context.addDataField("flag", "exist");
				context.addDataField("returnMsg", "该客户已经在联保小组"+strTmp+"名单中，是否确认新增？");
				return "0";
			}
			//判断是否存在在途的联保申请中
			String condition1 = " where cus_id='"+cus_id+"' and serno in(select serno from lmt_app_joint_coop where approve_status not in('997','998'))";
			IndexedCollection iCollAppNl = dao.queryList(modelId, condition1, connection);
			if(iCollAppNl.size()>0){
				String strTmp = "";//联保申请编号
				for(int i=0;i<iCollAppNl.size();i++){
					KeyedCollection kCollAppNl = (KeyedCollection)iCollAppNl.get(i);
					strTmp += "["+(String)kCollAppNl.getDataValue("serno")+"]";
				}
				context.addDataField("flag", "exist");
				context.addDataField("returnMsg", "该客户存在在途的联保申请"+strTmp+"，是否确认新增？");
				return "0";
			}
			
			context.addDataField("flag", PUBConstant.SUCCESS);
			context.addDataField("returnMsg", "");
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
