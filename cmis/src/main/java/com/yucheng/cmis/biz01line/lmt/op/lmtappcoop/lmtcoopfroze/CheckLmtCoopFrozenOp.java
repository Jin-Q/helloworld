package com.yucheng.cmis.biz01line.lmt.op.lmtappcoop.lmtcoopfroze;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckLmtCoopFrozenOp extends CMISOperation {
	
	private final String modelId = "LmtAppJointCoop";
	private final String modelAgr = "LmtAgrJointCoop";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String agrNo = "";
//		String serno="";
//		String approveStatus="";
		String app_type="";
		try{
			connection = this.getConnection(context);
			
			try {
				agrNo = (String)context.getDataValue("agr_no");	//协议编号
				app_type = (String)context.getDataValue("app_type");	//申请类型
			} catch (Exception e) {}
			if(agrNo == null || "".equals(agrNo))
				throw new EMPJDBCException("The values [agrNo] cannot be empty!");
			
			//判断是否有在途的冻结申请
			String condition = " where agr_no='"+agrNo+"' and approve_status not in('997','998') ";
			TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId, condition, connection);
			if(iColl.size()>0){
				KeyedCollection kColl = (KeyedCollection) iColl.get(0);
//				serno = (String) kColl.getDataValue("serno");	//冻结申请流水
//				approveStatus = (String) kColl.getDataValue("approve_status");
				String appType = (String) kColl.getDataValue("app_type");
				if(app_type.equals(appType)){	//若申请类型一致，则返回流水号方便跳转
//					context.addDataField("serno", serno);
//					context.addDataField("appStatus", approveStatus);
					context.put("flag", "same");
				}else{	//有在途的变更或解冻申请
//					context.addDataField("serno", "other");
//					context.addDataField("appStatus", approveStatus);
					context.put("flag", "noSame");
				}
			}else{
				if("04".equals(app_type)){	//额度解冻前先查询冻结金额是否大于0
					KeyedCollection kCollAgr = dao.queryDetail(modelAgr, agrNo, connection);
					String frozeAmt = (String)kCollAgr.getDataValue("froze_amt");//冻结金额
					if(frozeAmt==null||"".equals(frozeAmt)){
						frozeAmt = "0";
					}
					if(Double.parseDouble(frozeAmt)>0){//冻结金额>0，则可以进行解冻	
//						context.addDataField("serno", "");
//						context.addDataField("appStatus", approveStatus);
						context.put("flag", "success");
					}else{
//						context.addDataField("serno", "");
//						context.addDataField("appStatus", "nofrozen");
						context.put("flag", "nofrozen");
					}
				}else{
//					context.addDataField("serno", "");
//					context.addDataField("appStatus", approveStatus);
					context.put("flag", "success");
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
