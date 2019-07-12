package com.yucheng.cmis.biz01line.lmt.op.lmtagrjointcoop;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.dao.SqlClient;

/**
 * 解散联保小组：
 * 1.检查授信与担保关系是否解除
 * 2.设置协议状态为注销 003
 * 3.设置台账状态为终止 30
 * 4.解除授信与担保关系
 * @author QZCB
 * 
 */
public class DisbandLmtAgrJointCoopOp  extends CMISOperation {
	
	private final String modelId = "LmtAgrJointCoop";
	private final String modelIdDet = "LmtAgrDetails";

	private final String agr_no_name = "agr_no";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String agr_no_value = null;
			String flagInfo = null;
			try {
				agr_no_value = (String)context.getDataValue(agr_no_name);
			} catch (Exception e) {}
			if(agr_no_value == null || agr_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+agr_no_name+"] cannot be null!");

			/**检查授信与担保关系是否已经解除*/
			Object cc = (Object)SqlClient.queryFirst("checkRLmtGuarContJont", agr_no_value, null, connection);
//			String limitCodes = "";
			TableModelDAO dao = this.getTableModelDAO(context);
//			String condition = " where agr_no='"+agr_no_value+"'";
//			IndexedCollection iColl = dao.queryList(modelIdDet, condition, connection);
//			for(int i=0;i<iColl.size();i++){
//				KeyedCollection kCollTmp = (KeyedCollection)iColl.get(i);
//				String limitCode = (String)kCollTmp.getDataValue("limit_code");
//				if(i==(iColl.size()-1)){
//					limitCodes += "'"+limitCode+"'";
//				}else{
//					limitCodes += "'"+limitCode+"',";
//				}
//			}
			//调用业务接口检查授信项下业务是否都已结清
//			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
//			IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
//			boolean b = service.checkIqpInfo4Lmt(limitCodes, context, connection);
			if(Integer.parseInt(cc.toString())==0){
				KeyedCollection kColl = dao.queryDetail(modelId, agr_no_value, connection);
				/**设置协议状态为注销 003*/
				kColl.setDataValue("agr_status", "003");
				dao.update(kColl, connection);
				
				/**设置名单状态为无效 2*/
				SqlClient.executeUpd("updateLmtNameListStatusJont", agr_no_value, "2", null, connection);
				
				/**设置台账状态为终止 30*/
				SqlClient.executeUpd("updateLmtAgrDetailsStatusJont", agr_no_value, "30", null, connection);
				
				/**解除授信与担保关系*/
//				SqlClient.executeUpd("unchainRLmtGuarContJont", agr_no_value, null, null, connection);
				flagInfo = PUBConstant.SUCCESS;
			}else{
				flagInfo = PUBConstant.EXISTS;
			}
			
			context.addDataField("flag", flagInfo);
		}catch (EMPException ee) {
			context.addDataField("flag", PUBConstant.FAIL);
			throw ee;
		} catch(Exception e){
			context.addDataField("flag", PUBConstant.FAIL);
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
