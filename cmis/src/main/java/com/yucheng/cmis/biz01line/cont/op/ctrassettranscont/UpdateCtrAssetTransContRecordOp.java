package com.yucheng.cmis.biz01line.cont.op.ctrassettranscont;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.appPub.DateUtils;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class UpdateCtrAssetTransContRecordOp extends CMISOperation {
	
	private final String modelId = "CtrAssetTransCont";
	
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = "";
		String msg = "";
		try{
			connection = this.getConnection(context);
           
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			
			kColl.setDataValue("cont_status", 200);
			kColl.put("ser_date", context.getDataValue("OPENDAY").toString());
			kColl.put("inure_date", context.getDataValue("OPENDAY").toString());
			
			String serno = (String)kColl.getDataValue("serno");
			IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
			
			String date = iqpLoanAppComponent.getMaxDate4CtrAssetTrans(serno);
			if(!"".equals(date) && date != null){
				String openday = (String)context.getDataValue("OPENDAY");
				String int_start_date = (String)kColl.getDataValue("int_start_date");//起息日
				Boolean res1 = DateUtils.isBigBetweenDate(date, openday);
				Boolean res2 = DateUtils.isBigBetweenDate(date, int_start_date);
				if(!res1 || !res2){
					if(!res1){
						flag = "error";
				       	msg = "【到期日期】必须大于当前营业日期";
					}
					if(!res2){
						flag = "error";
						msg = "【到期日期】必须大于【起息日】";	
					}
				}else{
					kColl.put("end_date", date);
					int count=dao.update(kColl, connection);
					if(count!=1){
						throw new EMPException("Update Failed! Record Count: " + count);
					}
					flag = "success";
				}
			}else{
				flag = "error";
				msg = "该资产流清单下无最大到期日,合同签订失败";
			}
			context.addDataField("flag", flag);
			context.addDataField("msg", msg);
		}catch (EMPException ee) {
			context.addDataField("flag", "error");
			context.addDataField("msg", msg);
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
