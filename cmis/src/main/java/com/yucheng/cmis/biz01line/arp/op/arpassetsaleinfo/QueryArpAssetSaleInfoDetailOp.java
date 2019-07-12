package com.yucheng.cmis.biz01line.arp.op.arpassetsaleinfo;

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

public class QueryArpAssetSaleInfoDetailOp  extends CMISOperation {
	
	private final String modelId = "ArpAssetSaleInfo";
	

	private final String serno_name = "serno";
	private final String guaranty_no_name = "guaranty_no";
	
	
	private boolean updateCheck = false;
	

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

			String guaranty_no_value = null;
			try {
				guaranty_no_value = (String)context.getDataValue(guaranty_no_name);
			} catch (Exception e) {}
			if(guaranty_no_value == null || guaranty_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+guaranty_no_name+"] cannot be null!");

			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			Map pkMap = new HashMap();
			pkMap.put("serno",serno_value);
			pkMap.put("guaranty_no",guaranty_no_value);
			KeyedCollection kColl = dao.queryDetail(modelId, pkMap, connection);
			String[] args = new String[] {"guaranty_no","guaranty_no"};
			String[] modelIds = new String[] {"MortGuarantyBaseInfo","MortGuarantyBaseInfo"};
			String[] modelForeign = new String[] {"guaranty_no","guaranty_no"};
			String[] fieldName = new String[] { "guaranty_name","guaranty_type"};
			String[] resultName = new String[] { "guaranty_name","guaranty_type"};
			// 详细信息翻译时调用
			SystemTransUtils.dealPointName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			Map<String,String> map = new HashMap<String,String>();
			map.put("guaranty_type","MORT_TYPE");
			//树形菜单服务
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			SInfoUtils.addPopName(kColl, map, service);
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
