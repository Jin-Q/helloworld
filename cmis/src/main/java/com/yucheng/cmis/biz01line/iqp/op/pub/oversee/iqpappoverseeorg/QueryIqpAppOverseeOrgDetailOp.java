package com.yucheng.cmis.biz01line.iqp.op.pub.oversee.iqpappoverseeorg;

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
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryIqpAppOverseeOrgDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpAppOverseeOrg";
	
	private final String serno_name = "serno";
	
	private boolean updateCheck = true;
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
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
			
			String oversee_org_id = (String)kColl.getDataValue("oversee_org_id");
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();			
			CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");//获取客户接口		
			CusCom cuscom = service.getCusComByCusId(oversee_org_id, context, connection);//调用客户接口，实现方法getCusComByCusId
			
			String cusComManager = service.getManagerByCusId(oversee_org_id, context, connection);//调用客户接口，实现方法getManagerByCusId得到法人客户号
			
			String oversee_org_addr = (String)cuscom.getAcuAddr();//地址
			String oversee_org_street = (String)cuscom.getStreet();//街道
			String orgmodal = (String)cuscom.getComHoldType();//组织形式
			String belg_grp = (String)cuscom.getParentCusName();//所属集团名称
			double regi_cap = (double)cuscom.getRegCapAmt();//注册资金
			String build_date = (String)cuscom.getComStrDate();//成立日期
			//String com_scale = (String)cuscom.getComScale();//企业规模
			String con_trade_stats = (String)cuscom.getComHdEnterprise();//企业行业地位
			
			kColl.addDataField("oversee_org_addr",oversee_org_addr );
			kColl.addDataField("oversee_org_street",oversee_org_street );
			kColl.addDataField("orgmodal",orgmodal );
			kColl.addDataField("belg_grp",belg_grp );
			kColl.addDataField("regi_cap",regi_cap );
			kColl.addDataField("build_date",build_date );
		//	kColl.addDataField("com_scale",com_scale );
			kColl.addDataField("con_trade_stats",con_trade_stats );
			kColl.addDataField("legal",cusComManager );
			
			
			String[] args=new String[] { "oversee_org_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
			Map<String,String> map = new HashMap<String, String>();
			map.put("oversee_org_addr", "STD_GB_AREA_ALL");
			//树形菜单服务
			CMISTreeDicService service1 = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			SInfoUtils.addPopName(kColl, map, service1);
			
			String[] args1=new String[] { "legal" };
			String[] modelIds1=new String[]{"CusBase"};
			String[] modelForeign1=new String[]{"cus_id"};
			String[] fieldName1=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args1, SystemTransUtils.ADD, context, modelIds1, modelForeign1, fieldName1);
			
			SInfoUtils.addUSerName(kColl, new String[] {"manager_id"});
			SInfoUtils.addSOrgName(kColl, new String[] {"manager_br_id"});
			
			kColl.addDataField("reportId","iqp/overseeOrg.raq$iqp/pjscb.raq");
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
