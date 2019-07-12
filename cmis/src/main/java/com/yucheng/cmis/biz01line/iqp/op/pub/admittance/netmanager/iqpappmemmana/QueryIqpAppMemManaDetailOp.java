package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpappmemmana;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryIqpAppMemManaDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpAppMemMana";
	private final String modelIdNet = "IqpCoreConNet";
	private final String serno_name = "serno";
	private final String mem_cus_id_name = "mem_cus_id";
	
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");

			String mem_cus_id_value = null;
			try {
				mem_cus_id_value = (String)context.getDataValue(mem_cus_id_name);
			} catch (Exception e) {}
			if(mem_cus_id_value == null || mem_cus_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+mem_cus_id_name+"] cannot be null!");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String,String> pkMap = new HashMap<String,String>();
			pkMap.put("serno",serno_value);
			pkMap.put("mem_cus_id",mem_cus_id_value);
			KeyedCollection kColl = dao.queryDetail(modelId, pkMap, connection);
			
			//查询核心企业客户码
			KeyedCollection kCollNet = dao.queryDetail(modelIdNet, serno_value, connection);
			String cus_id = (String)kCollNet.getDataValue("cus_id");
			String dealer_lmt_type = (String)kCollNet.getDataValue("dealer_lmt_type");//经销商授信业务种类
			String provider_lmt_type = (String)kCollNet.getDataValue("provider_lmt_type");//供应商授信业务种类
			
			//得到授信接口
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			LmtServiceInterface lmtservice = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");//获取授信接口
			//通过授信接口查询客户所有供应链授信
			IndexedCollection lmt_iColl = lmtservice.searchLmtAgrDetailsList(mem_cus_id_value, "05", cus_id, connection);
			lmt_iColl.setName("LmtAgrDetailsList");
			
			
			//详细信息翻译时调用	
			String[] args=new String[] {"mem_cus_id"};
		    String[] modelIds=new String[]{"CusBase"};
		    String[] modelForeign=new String[]{"cus_id"};
		    String[] fieldName=new String[]{"cus_name"};
            SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			this.putDataElement2Context(kColl, context);			
			
			/**翻译额度名称**/
			args = new String[] { "limit_name" };
			modelIds = new String[]{"PrdBasicinfo"};
			modelForeign = new String[]{"prdid"};
			fieldName = new String[]{"prdname"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(lmt_iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			this.putDataElement2Context(lmt_iColl, context);
			
			context.addDataField("cus_id", cus_id);
			context.addDataField("dealer_lmt_type", dealer_lmt_type);
			context.addDataField("provider_lmt_type", provider_lmt_type);
			
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
