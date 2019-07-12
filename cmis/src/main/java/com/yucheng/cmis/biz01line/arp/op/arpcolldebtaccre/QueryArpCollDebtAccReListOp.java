package com.yucheng.cmis.biz01line.arp.op.arpcolldebtaccre;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryArpCollDebtAccReListOp extends CMISOperation {

	private final String modelId = "ArpCollDebtAccRe";	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);

			String serno = context.getDataValue("serno").toString();
			String conditionStr = " where serno = '"+serno+"' order by serno desc";			
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));			
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			
			// 详细信息翻译时调用
			String[] args = new String[] { "guaranty_no","guaranty_no","guaranty_no"};
			String[] modelIds = new String[] { "MortGuarantyBaseInfo","MortGuarantyBaseInfo","MortGuarantyBaseInfo"};
			String[] modelForeign = new String[] { "guaranty_no","guaranty_no","guaranty_no"};
			String[] fieldName = new String[] { "guaranty_name","guaranty_type","guaranty_info_status"};
			String[] resultName = new String[] { "guaranty_name","guaranty_type","guaranty_info_status"};
			SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			
			//树形菜单服务
			Map<String,String> map = new HashMap<String,String>();
			map.put("guaranty_type","MORT_TYPE");
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			SInfoUtils.addPopName(iColl, map, service);
			
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
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