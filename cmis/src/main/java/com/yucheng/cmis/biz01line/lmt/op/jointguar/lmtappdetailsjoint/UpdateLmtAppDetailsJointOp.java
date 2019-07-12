package com.yucheng.cmis.biz01line.lmt.op.jointguar.lmtappdetailsjoint;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class UpdateLmtAppDetailsJointOp extends CMISOperation {
	
	private final String modelId = "LmtAppDetails";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			KeyedCollection kColl = null;
//			String belgLine = "";//所属条线
//			String app_type = "";//申请类型
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.update(kColl, connection);
			if(count!=1){
				throw new EMPException("Update Failed! Record Count: " + count);
			}
//			if(kColl.containsKey("app_type")){
//				app_type = (String) kColl.getDataValue("app_type");
//			}
			
			//冻结解冻时不需要更新基表数据
//			if(!"03".equals(app_type)&&!"04".equals(app_type)){
//				//所属条线
//				belgLine = kColl.getDataValue("belg_line").toString();
//				//更新授信申请中的金额
//				LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
//				if(belgLine!=null&&"BL300".equals(belgLine)){
//					lmtComponent.updateLmtAppIndivAmt(kColl.getDataValue("serno").toString());//根据流水号更新个人授信申请基表数据
//				}else{
//					lmtComponent.updateLmtApplyAmt(kColl.getDataValue("serno").toString());  //根据流水号更新授信申请基表数据
//				}
//			}
			context.addDataField("flag","success");

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
