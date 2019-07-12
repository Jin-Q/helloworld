package com.yucheng.cmis.biz01line.iqp.op.iqpasset;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class DeleteIqpAssetRecordOp extends CMISOperation {

	private final String modelId = "IqpAsset";
	private final String modelIdTab = "IqpAssetRel";
	

	private final String asset_no_name = "asset_no";

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

			String asset_no_value = null;
			try {
				asset_no_value = (String)context.getDataValue(asset_no_name);
			} catch (Exception e) {}
			if(asset_no_value == null || asset_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+asset_no_name+"] cannot be null!");
            //删除资产包关系表记录
			IqpLoanAppComponent cmisComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
                                                .getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);				
            int countTab = cmisComponent.deleteTabByAssetno(asset_no_value);
            if(countTab!=1 && countTab!=0){
				throw new EMPException("Remove Failed! Records :"+countTab);
			}
            //删除资产包记录
            TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, asset_no_value, connection);
			
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			context.addDataField("flag", "success");
		}catch (EMPException ee) {
			context.addDataField("flag", "error");
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
