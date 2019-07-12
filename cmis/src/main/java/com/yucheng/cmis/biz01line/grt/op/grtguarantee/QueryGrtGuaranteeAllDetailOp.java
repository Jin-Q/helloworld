package com.yucheng.cmis.biz01line.grt.op.grtguarantee;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.yucheng.cmis.biz01line.grt.component.GrtGuarContComponet;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryGrtGuaranteeAllDetailOp  extends CMISOperation {
	
	private final String modelId = "GrtGuarantee";
	private final String modelId1 = "GrtGuarCont";
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String guarContNo = null;
		String cusId = null;
		KeyedCollection kColl = null;
		try{
			connection = this.getConnection(context);
			
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			guarContNo = (String)context.getDataValue("guar_cont_no");
			cusId = (String)context.getDataValue("guarty_cus_id");
			
			GrtGuarContComponet ggc = (GrtGuarContComponet) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("GrtGuarCont", context, connection);
			kColl = ggc.queryGrtGuaranteeAllDetail(guarContNo,cusId);
			kColl.setName("GrtGuarantee");
			/**翻译客户名称、登记人、登记机构*/
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			/*翻译客户名称、登记人、登记机构*/
			String[] args1=new String[] {"guarty_cus_id" };
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args1, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			SInfoUtils.addUSerName(kColl, new String[] {"input_id"});
			SInfoUtils.addSOrgName(kColl, new String[] {"input_br_id"});
			SInfoUtils.addUSerName(kColl, new String[] {"manager_id"});
			SInfoUtils.addSOrgName(kColl, new String[] {"manager_br_id"});
			this.putDataElement2Context(kColl, context);
		//	this.putDataElement2Context(grtGuarCont, context);
			
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
