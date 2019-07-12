package com.yucheng.cmis.biz01line.arp.op.arpcolldebtre;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryArpCollDebtReListOp extends CMISOperation {

	private final String modelId = "ArpCollDebtRe";

	public String doExecute(Context context) throws EMPException {		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String serno = null;
			try {
				serno = (String) context.getDataValue("serno");
			} catch (Exception e) {}
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			//引入的担保
			IndexedCollection iColl = dao.queryList(modelId, null,"where rel='1' and serno ='"+serno+"'",connection);
			iColl.setName("ArpCollDebtReList");
			//新增的担保
			IndexedCollection iCollnew = dao.queryList(modelId, null,"where rel='2' and serno ='"+serno+"'",connection);
			iCollnew.setName("MortGuarantyBaseInfoList");
			
			String[] args = new String[] { "guaranty_no","guaranty_no","guaranty_no"};
			String[] modelIds = new String[] { "MortGuarantyBaseInfo","MortGuarantyBaseInfo","MortGuarantyBaseInfo"};
			String[] modelForeign = new String[] { "guaranty_no","guaranty_no","guaranty_no"};
			String[] fieldName = new String[] { "guaranty_name","guaranty_type","guaranty_info_status"};
			String[] resultName = new String[] { "guaranty_name","guaranty_type","guaranty_info_status"};
			// 详细信息翻译时调用
			SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			// 详细信息翻译时调用
			SystemTransUtils.dealPointName(iCollnew, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			
			Map<String,String> map = new HashMap<String,String>();
			map.put("guaranty_type","MORT_TYPE");
			//树形菜单服务
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			SInfoUtils.addPopName(iColl, map, service);
			SInfoUtils.addPopName(iCollnew, map, service);
			this.putDataElement2Context(iColl, context);
			this.putDataElement2Context(iCollnew, context);
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