package com.yucheng.cmis.biz01line.lmt.op.lmtappfinguar;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;
/**
 * 通过客户码从CusCom表中取担保类别和担保放大倍数
 * @author ZYF
 */
public class GetGuarByCusComOp extends CMISOperation{
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String cus_id = (String)context.getDataValue("cus_id");
			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();			
			CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");//获取客户接口	
			CusCom cuscom = service.getCusComByCusId(cus_id, context, connection);
			ComponentHelper helper = new ComponentHelper();
			KeyedCollection ComkColl = new KeyedCollection("CusCom");
			ComkColl = helper.domain2kcol(cuscom, "CusCom");
			if(ComkColl.containsKey("guar_cls") && ComkColl.containsKey("guar_bail_multiple") && !"04".equals(ComkColl.containsKey("guar_cls"))){
				String guar_cls = (String)ComkColl.getDataValue("guar_cls");//担保类别
				String guar_bail_multiple = (String)ComkColl.getDataValue("guar_bail_multiple");//担保放大倍数
				context.addDataField("guar_cls", guar_cls);
				context.addDataField("guar_bail_multiple", guar_bail_multiple);
				context.addDataField("flag", PUBConstant.SUCCESS);
			}else{
				context.addDataField("guar_cls", "");
				context.addDataField("guar_bail_multiple", "");
				context.addDataField("flag", PUBConstant.FAIL);
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
