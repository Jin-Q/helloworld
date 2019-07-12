package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpoverseeagr;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryMortInfo4AccOverseeRemindListOp extends CMISOperation {


	private final String modelId = "MortGuarantyBaseInfo";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		IndexedCollection iColl = null;
		try{
			connection = this.getConnection(context);
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));

			KeyedCollection queryData = null;
			String oversee_agr_no = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			try {
				oversee_agr_no = (String)context.getDataValue("oversee_agr_no");
			} catch (Exception e) {}
			
			String conditionStr = TableModelUtil.getQueryCondition_bak(this.modelId, queryData, context, false, true, false);
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			List<String> list = new ArrayList<String>();
			list.add("guaranty_no");
			list.add("guaranty_name");
			list.add("cus_id");
			list.add("guaranty_cls");
			list.add("guaranty_type");
			list.add("guaranty_info_status");
			list.add("manager_id");
			list.add("manager_br_id");
			list.add("input_date");
			if(conditionStr.equals("")){
				conditionStr += "where guaranty_no in (select guaranty_no from iqp_cargo_oversee_re where agr_no = '"+oversee_agr_no+"') and guaranty_info_status not in('1','4') order by guaranty_no desc";
			}else{
				conditionStr += "and guaranty_no in (select guaranty_no from iqp_cargo_oversee_re where agr_no = '"+oversee_agr_no+"') and guaranty_info_status not in('1','4') order by guaranty_no desc";
			}
			iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			
			//用来区分是一般情况下的押品新增还是供应链时的押品新增
			Map<String,String> map = new HashMap<String,String>();
			map.put("guaranty_type","MORT_TYPE");
			//树形菜单服务
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
	    	SInfoUtils.addPopName(iColl, map, service);
			iColl.setName(iColl.getName()+"List");
			//客户名称翻译
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iColl,args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			SInfoUtils.addSOrgName(iColl, new String[] { "manager_br_id","input_br_id" });
			SInfoUtils.addUSerName(iColl, new String[] { "manager_id","input_id"});
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
