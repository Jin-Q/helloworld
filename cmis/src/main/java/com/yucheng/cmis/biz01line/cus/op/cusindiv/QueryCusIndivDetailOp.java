package com.yucheng.cmis.biz01line.cus.op.cusindiv;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryCusIndivDetailOp  extends CMISOperation {
	
	private final String modelId = "CusIndiv";
	private final String modelIdBase = "CusBase";
	
	private final String cus_id_name = "cus_id";
	
//	private boolean updateCheck = false;
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
//			if(this.updateCheck){
//				RecordRestrict recordRestrict = this.getRecordRestrict(context);
//				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
//			}
			
			String cus_id_value = null;
			try {
				cus_id_value = (String)context.getDataValue(cus_id_name);
			} catch (Exception e) {}
			if(cus_id_value == null || cus_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cus_id_name+"] cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, cus_id_value, connection);
			KeyedCollection kCollBase = dao.queryDetail(modelIdBase, cus_id_value, connection);
			if(null==kCollBase||kCollBase.size()<1||kColl==null||kColl.size()<1){
				throw new ComponentException("未查询到客户基本信息表信息");
			}
			
			/*
			 * 从CUS_BANK_RELATION表取出的"客户与我行关系"信息
			 *  added by tzb   2009-12-24
			 */
			
//			  String cusBankRelat = null;
//			  String certType = null;
//			  String certCode = null;
//			  
//			  try {
//				  certType = (String) kColl.getDataValue("cert_type");
//				  certCode = (String) kColl.getDataValue("cert_code");
//				  } catch (Exception e) {
//					e.printStackTrace();
//				  }
//				  if((null!=certType&&!"".equals(certType))&&(null!=certCode&&!"".equals(certCode))) {
//					  						
//						CusIndivComponent cusIndivComponent = (CusIndivComponent) CMISComponentFactory
//						.getComponentFactoryInstance().getComponentInstance(
//								PUBConstant.CUSINDIV, context, connection);
//
//				         cusBankRelat = cusIndivComponent.getIndivCusBankRelat(certType,certCode);												
//						
//						kColl.addDataField("cus_bank_relat",cusBankRelat);// 客户与我行关系						
//				  }

             /*
			 * end
			 */

//			SInfoUtils.addSOrgName(kColl, new String[]{"main_br_id", "input_br_id"});
//            SInfoUtils.addUSerName(kColl, new String[]{"cust_mgr", "input_id", "last_upd_id"});
            SInfoUtils.addSOrgName(kCollBase, new String[]{"main_br_id", "input_br_id"});
            SInfoUtils.addUSerName(kCollBase, new String[]{"cust_mgr", "input_id"});
            Map<String,String> map = new HashMap<String,String>();
            map.put("indiv_com_fld", "STD_GB_4754-2011");
            map.put("indiv_com_addr", "STD_GB_AREA_ALL");
            map.put("area_code", "STD_GB_AREA_ALL");
            map.put("indiv_houh_reg_add", "STD_GB_AREA_ALL");
            map.put("post_addr", "STD_GB_AREA_ALL");
            map.put("indiv_rsd_addr", "STD_GB_AREA_ALL");
            map.put("indiv_brt_place", "STD_GB_AREA_ALL");
            //树形菜单服务
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			SInfoUtils.addPopName(kColl, map, service);
			this.putDataElement2Context(kColl, context);
			this.putDataElement2Context(kCollBase, context);
			
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
