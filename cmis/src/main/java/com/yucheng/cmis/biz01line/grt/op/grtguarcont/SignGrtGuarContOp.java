package com.yucheng.cmis.biz01line.grt.op.grtguarcont;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class SignGrtGuarContOp extends CMISOperation {
	

	private final String modelId = "GrtGuarCont";
//	private final String modelIdLmt = "RLmtGuar";
	private final String modelIdLmt = "RLmtGuarCont";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			KeyedCollection kColl = null;
			KeyedCollection kCollLmt = null;
			String type = "";//不同授信时更改启动金额标识，（01--其他，02--联保）
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
				String guar_model = (String) kColl.getDataValue("guar_model");
				if("05".equals(guar_model)){//联保时
					type="02";
				}else{
					type="01";
				}
//				kCollLmt = (KeyedCollection)context.getDataElement(modelIdLmt);
				//设置担保合同签订日期
				kColl.setDataValue("sign_date", context.getDataValue("OPENDAY"));
				//将合同状态改为已签订
				kColl.setDataValue("guar_cont_state", "01");
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");				
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.update(kColl, connection);
			//是否追加担保
//			String is_add_guar = (String) kCollLmt.getDataValue("is_add_guar");
			if(kColl.getDataValue("lmt_grt_flag").equals("1")){//如果是授信项下时签订，则须要更新授信担保合同关联信息
				//调用授信模块接口
				try{
					CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
					LmtServiceInterface service = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
					String guar_cont_no=(String) kColl.getDataValue("guar_cont_no");
					String guar_amt=(String) kColl.getDataValue("guar_amt");
					kCollLmt = new KeyedCollection(modelIdLmt);
					kCollLmt.put("guar_cont_no", guar_cont_no);
					kCollLmt.put("guar_amt", guar_amt);
					kCollLmt.put("type","grt");
					int rst = service.addRLmtAppGuarCont(kCollLmt,(String)kColl.getDataValue("guar_cont_type"),context, connection);
				}catch(Exception ee){
					
				}
			}
		/*	if(is_add_guar.equals("2")){//否的时候需要更新授信启用金额
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				LmtServiceInterface service = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
				BigDecimal amt = new BigDecimal((String) kCollLmt.getDataValue("guar_amt"));
				service.updateLmtEnableamt((String)kCollLmt.getDataValue("limit_code"),amt,type,connection);
			}
		*/
			if(count!=1){
				throw new EMPException("Update Failed! Record Count: " + count);
			}
			//异步标志
			context.addDataField("flag", "success");
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
