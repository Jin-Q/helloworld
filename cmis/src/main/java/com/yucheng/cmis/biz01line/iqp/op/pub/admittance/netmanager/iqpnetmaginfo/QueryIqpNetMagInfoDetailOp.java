package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpnetmaginfo;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
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

public class QueryIqpNetMagInfoDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpNetMagInfo";	
	private final String net_agr_no_name = "net_agr_no";		
	private boolean updateCheck = false;
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}		
			
			String net_agr_no_value = null;
			try {
				net_agr_no_value = (String)context.getDataValue(net_agr_no_name);
			} catch (Exception e) {}
			if(net_agr_no_value == null || net_agr_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+net_agr_no_name+"] cannot be null!");		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, net_agr_no_value, connection);
			//获取信用等级和客户名称
			String cusId = kColl.getDataValue("cus_id").toString();
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
			LmtServiceInterface lmtservice = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");//获取授信接口
			CusBase cusbase = service.getCusBaseByCusId(cusId, context, connection);//客户基本信息
			String cus_name=cusbase.getCusName();//获取客户名称
			String cus_crd_grade=cusbase.getCusCrdGrade();//显示信用等级
			kColl.addDataField("cus_id_displayname", cus_name);//用于显示客户名称
		    kColl.addDataField("cdt_lvl", cus_crd_grade);//显示信用等级
			CusCom cuscom = service.getCusComByCusId(cusId, context, connection);//对公客户信息，获取行业分类
			ComponentHelper helper= new ComponentHelper();
			KeyedCollection ComkColl = new KeyedCollection("CusCom");
			ComkColl = helper.domain2kcol(cuscom, "CusCom");
			
			Map<String,String> map = new HashMap<String,String>();
			map.put("com_cll_type", "STD_GB_4754-2011");//行业分类
			CMISTreeDicService cmisservice = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			SInfoUtils.addPopName(ComkColl, map, cmisservice);//翻译行业分类
			String com_cll_type_displayname = (String)ComkColl.getDataValue("com_cll_type_displayname");    
			kColl.addDataField("trade_type_displayname", com_cll_type_displayname);//用于显示行业分类名称
			KeyedCollection LmtkColl = lmtservice.getLmtAgrInfoMsg(cusId,"05",connection);
			if(LmtkColl.isEmpty()){
				kColl.addDataField("lmt_amt", "0");//设定直接额度
			}
			else{
				kColl.addDataField("lmt_amt",LmtkColl.getDataValue("crd_totl_amt"));
			}
			if(context.containsKey("app_type")){
				kColl.addDataField("app_type", context.getDataValue("app_type"));	
			}  
		    SInfoUtils.addSOrgName(kColl, new String[]{"manager_br_id","input_br_id"});
			SInfoUtils.addUSerName(kColl, new String[]{"manager_id","input_id"});
			this.putDataElement2Context(kColl, context);
			
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
