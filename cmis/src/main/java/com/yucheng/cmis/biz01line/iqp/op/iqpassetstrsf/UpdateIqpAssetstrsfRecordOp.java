package com.yucheng.cmis.biz01line.iqp.op.iqpassetstrsf;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpBatchComponent;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class UpdateIqpAssetstrsfRecordOp extends CMISOperation {
	

	private final String modelId = "IqpAssetstrsf";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			
			KeyedCollection kColl = null;
			String serno = "";
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
				serno = (String) kColl.getDataValue("serno");
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			TableModelDAO dao = this.getTableModelDAO(context);
			
			kColl.put("topp_acct_no", kColl.getDataValue("topp_acct_no").toString().trim());
			
			KeyedCollection oldKColl = dao.queryDetail(modelId, serno, connection);
			String old_asset_no = (String) oldKColl.getDataValue("asset_no");
			
			//修改业务与批次关联
			String asset_no = (String) kColl.getDataValue("asset_no");
			if(!asset_no.equals(old_asset_no)){
				KeyedCollection kc1 = dao.queryDetail("IqpAsset", old_asset_no, connection);
				kc1.setDataValue("status", "01");//原批次改为登记状态
				KeyedCollection kc2 = dao.queryDetail("IqpAsset", asset_no, connection);
				kc2.setDataValue("status", "02");//新批次改为已引用状态
			}
			
			int count=dao.update(kColl, connection);
			if(count!=1){
				throw new EMPException("Update Failed! Record Count: " + count);
			}
			
			IqpBatchComponent batchComponent = (IqpBatchComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPBATCHCOMPONENT, context, connection);
			KeyedCollection kCollBatchMng = batchComponent.getBatchMngBySerno(serno);
			if(kCollBatchMng!=null){
				if(kCollBatchMng.containsKey("input_id")&&"".equals(kCollBatchMng.getDataValue("input_id"))){
					kCollBatchMng.put("input_id", kColl.getDataValue("input_id"));
					kCollBatchMng.put("input_br_id", kColl.getDataValue("input_br_id"));
				}
				kCollBatchMng.setName("IqpBatchMng");
				dao.update(kCollBatchMng, connection);
			}
			context.addDataField("flag", "success");
			
			//添加授信关系
			IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
			String limit_ind = (String) kColl.getDataValue("limit_ind");
			if(limit_ind!=null&&!limit_ind.equals("")){
				if(limit_ind.equals("2")||limit_ind.equals("3")){//循环，一次性额度
					String agr_no = (String) kColl.getDataValue("limit_acc_no");
					iqpLoanAppComponent.doLmtRelation("01", serno, agr_no);
				}else if(limit_ind.equals("4")){//第三方额度
					String agr_no = (String) kColl.getDataValue("limit_acc_no");
					iqpLoanAppComponent.doLmtRelation("02", serno, agr_no);
				}else{
					iqpLoanAppComponent.deleteLmtRelation(serno);
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
