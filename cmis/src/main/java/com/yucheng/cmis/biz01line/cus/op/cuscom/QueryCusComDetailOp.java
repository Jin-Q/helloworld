package com.yucheng.cmis.biz01line.cus.op.cuscom;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryCusComDetailOp  extends CMISOperation {
	
	private final String modelId = "CusBase";
	private final String modelIdCom = "CusCom";
	private final String cus_id_name = "cus_id";
	private final String modelIdRel = "CusCliRel";
	
//	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flagInfo = null;
		try{
			connection = this.getConnection(context);
			
//			if(this.updateCheck){
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
//			RecordRestrict recordRestrict = this.getRecordRestrict(context);
//			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
//			}
			String rel_flag;
			String cus_id_value = null;
			try {
				cus_id_value = (String)context.getDataValue(cus_id_name);
			} catch (Exception e) {}
			if(cus_id_value == null || cus_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cus_id_name+"] cannot be null!");
			String condition = " where cus_id = '"+cus_id_value+"'";
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, cus_id_value, connection);
			KeyedCollection kCollCom = dao.queryDetail(modelIdCom, cus_id_value, connection);
			IndexedCollection detIColl = dao.queryList(modelIdRel, condition, connection);
			if(null==kColl||kColl.size()<1||kCollCom==null||kCollCom.size()<1){
				throw new ComponentException("未查询到客户基本信息表信息");
			}
			
			if(detIColl.size()<1){
				rel_flag = "2";
			}else{
				rel_flag = "1";
			}
			kCollCom.put("rel_flag", rel_flag);
			
			flagInfo = "com";

			SInfoUtils.addSOrgName(kColl, new String[]{"main_br_id","input_br_id"});
			SInfoUtils.addUSerName(kColl, new String[]{"cust_mgr","input_id"});
			
			Map<String,String> map = new HashMap<String,String>();
			map.put("com_cll_type", "STD_GB_4754-2011");//行业名称
			map.put("reg_state_code", "STD_GB_AREA_ALL");//行政区划名称
			map.put("corp_qlty", "STD_GB_CORP_QLTY");//企业性质
			map.put("econ_dep", "STD_GB_ECON_DEPT");//国民经济部门
			map.put("reg_addr", "STD_GB_AREA_ALL");//注册登记地址
			map.put("acu_addr", "STD_GB_AREA_ALL");//实际经营地址
			map.put("post_addr", "STD_GB_AREA_ALL");//实际经营地址
//			SInfoUtils.addPopName(kCollCom, map,connection);
			//树形菜单服务
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			SInfoUtils.addPopName(kCollCom, map, service);
			
			String[] args=new String[] { "bas_acc_bank" };
			String[] modelIds=new String[]{"PrdBankInfo"};
			String[] modelForeign=new String[]{"bank_no"};
			String[] fieldName=new String[]{"bank_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kCollCom, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
			this.putDataElement2Context(kColl, context);
			this.putDataElement2Context(kCollCom, context);
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return flagInfo;
	}
}
