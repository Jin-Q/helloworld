package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpcoreconnet;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class GetValuefromCusOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection =null;
		try{
			connection = this.getConnection(context);
			String cus_id = (String)context.getDataValue("cus_id");
			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();			
			CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");//获取客户接口		
			LmtServiceInterface lmtservice = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");//获取授信接口
			//调用客户接口，实现方法getCusComByCusId
			CusCom cuscom = service.getCusComByCusId(cus_id, context, connection);
			ComponentHelper helper = new ComponentHelper();
			KeyedCollection ComkColl = new KeyedCollection("CusCom");
			ComkColl = helper.domain2kcol(cuscom, "CusCom");
			Map<String,String> map = new HashMap<String,String>();
			map.put("com_cll_type", "STD_GB_4754-2011");//行业分类
			CMISTreeDicService cmisservice = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			SInfoUtils.addPopName(ComkColl, map, cmisservice);
			String com_cll_type_displayname = (String)ComkColl.getDataValue("com_cll_type_displayname");//获取行业分类
			//调用授信接口，实现方法getLmtAgrInfoMsg 获取额度  05为供应链额度
			KeyedCollection LmtkColl = lmtservice.getLmtAgrInfoMsg(cus_id,"05",connection);
			if(LmtkColl.isEmpty()){
				context.addDataField("amt", "0");
			}else{
				String lmt_amt = LmtkColl.getDataValue("crd_totl_amt")+"";
				context.addDataField("amt", lmt_amt);
			}			
			context.addDataField("type", com_cll_type_displayname);
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}

		
		return null;
	}

}
