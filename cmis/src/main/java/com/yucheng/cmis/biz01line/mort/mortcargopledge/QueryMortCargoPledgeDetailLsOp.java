package com.yucheng.cmis.biz01line.mort.mortcargopledge;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

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

public class QueryMortCargoPledgeDetailLsOp  extends CMISOperation {
	
	private final String modelId = "MortCargoPledge";
	

	private final String cargo_id_name = "cargo_id";
	
	
	private boolean updateCheck = true;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				
				if(context.containsKey("action")){
					String action = (String) context.getDataValue("action");
					if("zh".equals(action)){
					//	recordRestrict.judgeUpdateRestrict("MortCargoReplList", context, connection);
					}
				}else{
					if(context.containsKey("op")&&"view".equals(context.getDataValue("op"))){
						recordRestrict.judgeQueryRestrict(this.modelId,null, context, connection);
					}else{
						recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
					}
				}
			}
			String cargo_id_value = null;
			try {
				cargo_id_value = (String)context.getDataValue(cargo_id_name);
			} catch (Exception e) {}
			if(cargo_id_value == null || cargo_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cargo_id_name+"] cannot be null!");

		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = null;
			if(context.containsKey("action")){
				String action = (String) context.getDataValue("action");
				if("zh".equals(action)){
					//kColl = dao.queryDetail("MortCargoReplList", cargo_id_value, connection);
					kColl = dao.queryDetail(modelId, cargo_id_value, connection);
				}
			}else{
				kColl = dao.queryDetail(modelId, cargo_id_value, connection);
			}
			if(context.containsKey("serno")){
				String serno = (String) context.getDataValue("serno");
				Map<String,String> map = new HashedMap();
				map.put("serno",serno);
				map.put("cargo_id",cargo_id_value);
				kColl = dao.queryDetail("MortCargoReplList", map, connection);
			}
			//翻译产地、销售区域、押品所属目录
			Map<String, String> map = new HashMap<String, String>();
			map.put("produce_area", "STD_GB_AREA_ALL");
			map.put("sale_area", "STD_GB_AREA_ALL");
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
	    	SInfoUtils.addPopName(kColl, map, service);
	    	kColl.setName(modelId);
			SInfoUtils.addPrdPopName("IqpMortCatalogMana",kColl, "guaranty_catalog", "catalog_no", "catalog_name", "->", connection, dao);  //翻译目录路径
			String[] argsvalue=new String[] { "value_no" };
			String[] modelIdsvalue=new String[]{"IqpMortValueMana"};
			String[] modelForeignvalue=new String[]{"value_no"};
			String[] fieldNamevalue=new String[]{"market_value"};
			String[] resultNamevalue = new String[] { "market_value"};
			//详细信息翻译时调用			
			SystemTransUtils.dealPointName(kColl, argsvalue, SystemTransUtils.ADD, context, modelIdsvalue,modelForeignvalue, fieldNamevalue,resultNamevalue);
			SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id" });
			SInfoUtils.addUSerName(kColl, new String[] { "input_id" });
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
