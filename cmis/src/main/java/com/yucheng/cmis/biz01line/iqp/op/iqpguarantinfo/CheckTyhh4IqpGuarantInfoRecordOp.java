package com.yucheng.cmis.biz01line.iqp.op.iqpguarantinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
/**
 * 福州分行、漳州分行办理委托代开银承时，
 * 系统限制受益行行号
 * @author FChengLiang
 * XD150209008
 */
public class CheckTyhh4IqpGuarantInfoRecordOp extends CMISOperation {
	

	private final String modelId = "IqpLoanApp";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String ben_acct_org_no = "";
			String serno = "";
			String guarantType = "";
			if(context.containsKey("ben_acct_org_no")){
				 ben_acct_org_no =(String)context.getDataValue("ben_acct_org_no");
				 if(ben_acct_org_no==null){
		            	ben_acct_org_no="";
		            }
			}
			if(context.containsKey("serno")){
				serno =(String)context.getDataValue("serno");
				 if(serno==null){
					 serno="";
		            }
			}
			if(context.containsKey("guarantType")){
				guarantType =(String)context.getDataValue("guarantType");
				if(guarantType==null){
					guarantType="";
				}
			}
			
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kc = dao.queryDetail(modelId, serno, connection);
			String mgrBrId = (String)kc.getDataValue("manager_br_id");
			//福州分行下支行，办理委托代开银承时，限制同业机构号 
			//中国建设银行福州城南支行（支付行号：105391005013）、中国银行福州市仓山支行（支付行号：104391011304）
			if(mgrBrId!=null&&mgrBrId.contains("93501")&&"20".equals(guarantType)){
				if(!("105391005013".equals(ben_acct_org_no))&&!("104391011304".equals(ben_acct_org_no))){
					context.addDataField("flag", "failed");
				}else{
					context.addDataField("flag", "success");
				}
			}else if(mgrBrId!=null&&mgrBrId.contains("93506")&&"20".equals(guarantType)){//漳州分行下设机构办理委托代开银承时，限制 中国银行漳州分行（支付行号104399050006）
				if(!"104399050006".equals(ben_acct_org_no)){
					context.addDataField("flag", "failed");
				}else{
					context.addDataField("flag", "success");
				}
			}else{
				context.addDataField("flag", "success");
			}
		}catch (EMPException ee) {
			context.addDataField("flag", "failed");   
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
