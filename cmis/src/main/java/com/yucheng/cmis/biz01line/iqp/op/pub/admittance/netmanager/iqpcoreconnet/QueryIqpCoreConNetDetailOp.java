package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpcoreconnet;

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

public class QueryIqpCoreConNetDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpCoreConNet";
	

	private final String serno_name = "serno";
	
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		double totl_amt_01 = 0;
		double totl_amt_02 = 0;
		try{
			connection = this.getConnection(context);					
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}			
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
			//获取信用等级和客户名称
			String cusId = kColl.getDataValue("cus_id").toString();
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
			LmtServiceInterface lmtservice = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");//获取授信接口
			KeyedCollection LmtkColl = lmtservice.getLmtAgrInfoMsg(cusId,"05",connection);//供应链授信
			KeyedCollection Lmtkc = lmtservice.getLmtAgrInfoMsg(cusId,"01",connection);//一般授信
			
			CusBase cusbase = service.getCusBaseByCusId(cusId, context, connection);//客户基本信息
			String cus_name = cusbase.getCusName();//获取客户名称
			String cus_crd_grade =cusbase.getCusCrdGrade();//获取信用等级
			CusCom cuscom = service.getCusComByCusId(cusId, context, connection);//对公客户信息，获取行业分类
			ComponentHelper helper = new ComponentHelper();//获取工具类
			KeyedCollection ComkColl = new KeyedCollection("CusCom");
			ComkColl = helper.domain2kcol(cuscom, "CusCom");
			Map<String,String> map = new HashMap<String,String>();
			map.put("com_cll_type", "STD_GB_4754-2011");//行业分类
			CMISTreeDicService cmisservice = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			SInfoUtils.addPopName(ComkColl, map, cmisservice);
			String com_cll_type_displayname = (String)ComkColl.getDataValue("com_cll_type_displayname");
			
		    kColl.addDataField("cus_id_displayname", cus_name);
		    kColl.addDataField("cdt_lvl", cus_crd_grade);
			kColl.addDataField("trade_type_displayname", com_cll_type_displayname);
			
			if(LmtkColl.isEmpty()&&Lmtkc.isEmpty()){
				kColl.addDataField("lmt_amt", "0");
			}else if(LmtkColl.isEmpty()&& !Lmtkc.isEmpty()){
				totl_amt_02 = (Double)Lmtkc.getDataValue("crd_totl_amt");
				kColl.addDataField("lmt_amt", totl_amt_02);
			}else if(!LmtkColl.isEmpty()&& Lmtkc.isEmpty()){
				totl_amt_01 = (Double)LmtkColl.getDataValue("crd_totl_amt");
				kColl.addDataField("lmt_amt", totl_amt_01);
			}else{
				totl_amt_01 = (Double)LmtkColl.getDataValue("crd_totl_amt");
				totl_amt_02 = (Double)Lmtkc.getDataValue("crd_totl_amt");
				kColl.addDataField("lmt_amt", totl_amt_01+totl_amt_02);
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
