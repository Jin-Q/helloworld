package com.yucheng.cmis.biz01line.cus.op.cuscommanager;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class QueryCusComManagerDetailOp extends CMISOperation {
	
	private final String modelId = "CusComManager";
	
	private final String cus_id_name = "cus_id";
	private final String cus_id_rel_name = "cus_id_rel";
	private final String com_mrg_typ_name = "com_mrg_typ";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String cus_id_value = null;
			try {
				cus_id_value = (String)context.getDataValue(cus_id_name);
			} catch (Exception e) {}
			if(cus_id_value == null || cus_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cus_id_name+"] cannot be null!");
			String cus_id_rel_value = null;
			try {
				cus_id_rel_value = (String)context.getDataValue(cus_id_rel_name);
			} catch (Exception e) {}
			if(cus_id_rel_value == null || cus_id_rel_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cus_id_rel_value+"] cannot be null!");
			String com_mrg_typ_value = null;
			try {
				com_mrg_typ_value = (String)context.getDataValue(com_mrg_typ_name);
			} catch (Exception e) {}
			if(com_mrg_typ_value == null || com_mrg_typ_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+com_mrg_typ_value+"] cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String, String> pkMap = new HashMap<String, String>();
			pkMap.put("cus_id",cus_id_value);
			pkMap.put("cus_id_rel",cus_id_rel_value);
			pkMap.put("com_mrg_typ",com_mrg_typ_value);
			KeyedCollection kColl = dao.queryAllDetail(modelId, pkMap, connection);
			
			//加载component 如果前面已经有实例从工厂中加载，请使用改实例的getComponent(comId) 如cusBaseComponent.getComponent(comId)，以保证事务一致
			CusBaseComponent cusBaseComponent = (CusBaseComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSBASE,context,connection);
			
			//取客户基表中数据
			Map<String, String> baseMaps=new HashMap<String, String>();
			baseMaps.put("com_mrg_name", "cus_name");
			baseMaps.put("com_mrg_cert_typ", "cert_type");
			baseMaps.put("com_mrg_cert_code", "cert_code");
			baseMaps.put("cus_country", "cus_country");
			
			//需要从客户信息中获取的字段mapping关系map
			Map<String, String> colMap=new HashMap<String, String>();
			colMap.put("com_mrg_bday", "indiv_dt_of_birth");
			colMap.put("com_mrg_sex", "indiv_sex");
			colMap.put("com_mrg_occ", "indiv_occ");
			colMap.put("com_mrg_duty", "indiv_com_job_ttl");
			colMap.put("com_mrg_crtf", "indiv_crtfctn");
			colMap.put("com_mrg_edt", "indiv_edt");
			colMap.put("com_mrg_dgr", "indiv_dgr");
			colMap.put("com_relate_phone", "mobile");//INDIV_OCC
			colMap.put("com_mrg_occ", "indiv_occ");
			
			cusBaseComponent.getKCollCusById(kColl, baseMaps, colMap, "cus_id_rel");
			
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
