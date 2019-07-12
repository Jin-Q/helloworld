package com.yucheng.cmis.biz01line.arp.op.arplawmembermana;

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
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryArpLawDefendantManaDetailOp  extends CMISOperation {
	
	private final String modelId = "ArpLawMemberMana";
	

	private final String pk_serno_name = "pk_serno";
	
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			
			String pk_serno_value = null;
			try {
				pk_serno_value = (String)context.getDataValue(pk_serno_name);
			} catch (Exception e) {}
			if(pk_serno_value == null || pk_serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+pk_serno_name+"] cannot be null!");

			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, pk_serno_value, connection);
			
			String cus_type = context.getDataValue("cus_type").toString();
			Map<String,String> map = new HashMap<String, String>();
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			String[] args,modelIds,modelForeign,fieldName,resultName ;
			if(cus_type.equals("indiv")){	//个人客户处理
				args=new String[]{"cus_id","cus_id","cus_id","cus_id","cus_id","cus_id","cus_id","cus_id"};
				modelIds=new String[]{"CusBase","CusBase","CusBase","CusIndiv","CusIndiv","CusIndiv","CusIndiv","CusIndiv"};
				modelForeign=new String[]{"cus_id","cus_id","cus_id","cus_id","cus_id","cus_id","cus_id","cus_id"};
				fieldName=new String[]{"cus_name","cert_type","cert_code","indiv_sex","indiv_ntn","indiv_rsd_addr","indiv_dt_of_birth","street3"};
				resultName=new String[]{"cus_name","cert_type","cert_code","indiv_sex","indiv_ntn","indiv_rsd_addr","indiv_dt_of_birth","street3"};
				SystemTransUtils.dealPointName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
				
				map.put("indiv_rsd_addr", "STD_GB_AREA_ALL");
				SInfoUtils.addPopName(kColl, map, service);
			}else{	//对公客户处理
				args=new String[]{"cus_id","cus_id","cus_id","cus_id","cus_id"};
				modelIds=new String[]{"CusBase","CusBase","CusBase","CusCom","CusCom"};
				modelForeign=new String[]{"cus_id","cus_id","cus_id","cus_id","cus_id"};
				fieldName=new String[]{"cus_name","cert_type","cert_code","acu_addr","street"};
				resultName=new String[]{"cus_name","cert_type","cert_code","acu_addr","street"};
				SystemTransUtils.dealPointName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);

				map.put("acu_addr", "STD_GB_AREA_ALL");
				SInfoUtils.addPopName(kColl, map, service);
			}
			
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
