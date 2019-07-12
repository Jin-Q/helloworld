package com.yucheng.cmis.biz01line.grt.op.grtguarcont;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class DestroyGrtGuarContRecord extends CMISOperation {
	

	private final String modelId = "GrtGuarCont";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String guarContNo  = "";
			KeyedCollection kColl =null;
			String type = "";//不同授信时更改启动金额标识，（01--其他，02--联保）
			try {
				guarContNo = (String)context.getDataValue("guar_cont_no");
				
			} catch (Exception e) {}
			/**modified by lisj 2014-12-12 修改当合同状态为【登记】，该笔担保合同未关联授信、业务信息时可做注销操作，于 2014-12-18上线 begin**/
			//调用授信模块接口
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			LmtServiceInterface serviceLmt = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
			IqpServiceInterface serviceIqp = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			IndexedCollection iCollLmt = serviceLmt.getLmtGuarReByGuarContNo(guarContNo, connection);
			IndexedCollection iCollIqp = serviceIqp.getIqpGuarContReByGuarContNo(guarContNo, connection);
			String guarContState ="";
			guarContState = (String)context.getDataValue("guar_cont_state");
			//校验当合同状态为登记时，查询担保合同是否存在授信、业务信息
			/**modified by yangzy 2014/12/19 修改当合同状态为【登记】，该笔担保合同未关联授信、业务信息时可做注销操作 begin**/
			if(guarContState.equals("00") && ((iCollLmt!=null && iCollLmt.size()>0) || (iCollIqp!=null && iCollIqp.size()>0))){
			/**modified by yangzy 2014/12/19 修改当合同状态为【登记】，该笔担保合同未关联授信、业务信息时可做注销操作 end**/
				context.addDataField("flag", "undone");
			}else{
				TableModelDAO dao = this.getTableModelDAO(context);
				kColl = dao.queryDetail(modelId, guarContNo, connection);
				String guar_model = (String) kColl.getDataValue("guar_model");
				if("05".equals(guar_model)){//联保时
					type="02";
				}else{
					type="01";
					}
				//设置担保合同状态为注销状态
				kColl.setDataValue("guar_cont_state", "03");
	
				/**KeyedCollection resKc = service.queryRLmtGuarContInfo(guarContNo,"grt",connection);
				String is_add_guar = (String) resKc.getDataValue("is_add_guar");
				//不是追加担保时，需要同时更新授信启用金额
				if("2".equals(is_add_guar)){
					service.updateLmtEnableamtOff((String) kColl.getDataValue("limit_code"),new BigDecimal( (String)kColl.getDataValue("guar_amt")),type, connection);
				}**/   
				serviceLmt.updateLmtGuarStatus(guarContNo, connection);
				int count=dao.update(kColl, connection);
				if(count!=1){
					throw new EMPException("Update Failed! Record Count: " + count);
				}
				//添加异步返回信息
				context.addDataField("flag", "success");
			}
			/**modified by lisj 2014-12-12 修改当合同状态为【登记】，该笔担保合同未关联授信、业务信息时可做注销操作，于 2014-12-18上线 end**/
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
