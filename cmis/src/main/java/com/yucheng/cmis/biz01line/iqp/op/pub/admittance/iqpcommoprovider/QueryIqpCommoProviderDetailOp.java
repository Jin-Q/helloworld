package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.iqpcommoprovider;

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
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryIqpCommoProviderDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpCommoProvider";
	

	private final String mort_catalog_no_name = "mort_catalog_no";
	
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			
			String mort_catalog_no_value = null;
			try {
				mort_catalog_no_value = (String)context.getDataValue(mort_catalog_no_name);
			} catch (Exception e) {}
			if(mort_catalog_no_value == null || mort_catalog_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+mort_catalog_no_name+"] cannot be null!");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, mort_catalog_no_value, connection);

			String[] args=new String[] { "provider_no" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用
			SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id"});
			SInfoUtils.addUSerName(kColl, new String[] { "input_id"});
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
			Map<String,String> map = new HashMap<String, String>();
			map.put("link_addr", "STD_GB_AREA_ALL");
			//树形菜单服务
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			SInfoUtils.addPopName(kColl, map, service);
			
			this.putDataElement2Context(kColl, context);
			
			String provider_no = kColl.getDataValue("provider_no").toString();
			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			CusServiceInterface service1 = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
			CusBase cusbase = service1.getCusBaseByCusId(provider_no, context, connection);
			String CertCode = cusbase.getCertCode();
			String CertType = cusbase.getCertType();
			kColl.addDataField("cert_code", CertCode);
			kColl.addDataField("cert_type", CertType);
			
			this.putDataElement2Context(kColl, context);
			context.addDataField("operate", "updateIqpCommoProviderRecord.do");
			
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
