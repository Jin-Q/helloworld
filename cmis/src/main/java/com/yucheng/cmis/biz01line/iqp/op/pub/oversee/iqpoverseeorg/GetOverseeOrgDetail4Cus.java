package com.yucheng.cmis.biz01line.iqp.op.pub.oversee.iqpoverseeorg;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class GetOverseeOrgDetail4Cus extends CMISOperation {
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String oversee_org_id = context.getDataValue("oversee_org_id").toString();
			
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
			String com_scale = (String)cuscom.getComScale();//企业规模
			String con_trade_stats = (String)cuscom.getComHdEnterprise();//企业行业地位
			
			KeyedCollection kColl = new KeyedCollection();
			kColl.addDataField("oversee_org_addr",oversee_org_addr );
			kColl.addDataField("oversee_org_street",oversee_org_street );
			kColl.addDataField("orgmodal",orgmodal );
			kColl.addDataField("belg_grp",belg_grp );
			kColl.addDataField("regi_cap",regi_cap );
			kColl.addDataField("build_date",build_date );
			kColl.addDataField("com_scale",com_scale );
			kColl.addDataField("con_trade_stats",con_trade_stats );
			kColl.addDataField("legal",cusComManager );
			
			Map<String,String> map = new HashMap<String, String>();
			map.put("oversee_org_addr", "STD_GB_AREA_ALL");
			//树形菜单服务
			CMISTreeDicService service1 = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			SInfoUtils.addPopName(kColl, map, service1);
			
			String[] args=new String[] { "legal" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
			IndexedCollection iColl=new IndexedCollection("cusList"); 
			iColl.addDataElement(kColl);
			this.putDataElement2Context(iColl, context);
			
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
