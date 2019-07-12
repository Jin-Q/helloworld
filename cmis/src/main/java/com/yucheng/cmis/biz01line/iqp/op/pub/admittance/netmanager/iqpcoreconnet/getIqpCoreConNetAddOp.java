package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpcoreconnet;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class getIqpCoreConNetAddOp extends CMISOperation {

	 private final String modelId="IqpCoreConNet";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		KeyedCollection kColl = new KeyedCollection(modelId);
		try
		{
			connection = this.getConnection(context);	
			String cus_id = null;
			try{
				cus_id = (String)context.getDataValue("cus_id");
			}catch(Exception e){
				throw new Exception("数据为空,请检查!");
			}
			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
			LmtServiceInterface lmtservice = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");//获取授信接口
			KeyedCollection LmtkColl = lmtservice.getLmtAgrInfoMsg(cus_id,"05",connection);
			
			CusBase cusbase = service.getCusBaseByCusId(cus_id, context, connection);//客户基本信息
			String cus_id_displayname = cusbase.getCusName();//获取客户名称
			String cus_crd_grade =cusbase.getCusCrdGrade();//获取信用等级
			
			CusCom cuscom = service.getCusComByCusId(cus_id, context, connection);//对公客户信息，获取行业分类
			ComponentHelper helper = new ComponentHelper();//获取工具类
			KeyedCollection ComkColl = new KeyedCollection("CusCom");
			ComkColl = helper.domain2kcol(cuscom, "CusCom");
			Map<String,String> map = new HashMap<String,String>();
			map.put("com_cll_type", "STD_GB_4754-2011");//行业分类
			CMISTreeDicService cmisservice = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			SInfoUtils.addPopName(ComkColl, map, cmisservice);
			String com_cll_type_displayname = (String)ComkColl.getDataValue("com_cll_type_displayname");
			
			
			if(LmtkColl != null && LmtkColl.size()>0){
				kColl.addDataField("lmt_amt", LmtkColl.getDataValue("crd_totl_amt"));
			}else{
				kColl.addDataField("lmt_amt", "0");
			}	
			
			
			String orgId=context.getDataValue("organNo").toString();
			String userId= context.getDataValue("currentUserId").toString();			
			kColl.addDataField("input_id", userId);
			kColl.addDataField("input_br_id", orgId);
			kColl.addDataField("cus_id", cus_id);
			kColl.addDataField("cus_id_displayname", cus_id_displayname);
			kColl.addDataField("cdt_lvl", cus_crd_grade);
			kColl.addDataField("trade_type_displayname", com_cll_type_displayname);
			SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(kColl, new String[]{"input_br_id"});
			this.putDataElement2Context(kColl, context);
			
		}catch(EMPException ee) {
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
