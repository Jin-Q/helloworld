package com.yucheng.cmis.biz01line.mort.mortguarantybaseinfo;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.mort.component.MortCommenOwnerComponent;
import com.yucheng.cmis.biz01line.mort.morttool.MORTConstant;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryMortGuarantyBaseInfoListPopOp extends CMISOperation {


	private final String modelId = "MortGuarantyBaseInfo";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		IndexedCollection iColl = null;
		String cus_id="";
		try{
			connection = this.getConnection(context);
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, true,true, false)
			+"";
			if(!"".equals(conditionStr)){
				conditionStr = conditionStr.substring(6, conditionStr.length()-1);				
			}

			//构建组件类
			MortCommenOwnerComponent mortRe = (MortCommenOwnerComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(
					MORTConstant.MORTCOMMENOWNERCOMPONENT, context, connection);
			iColl = mortRe.getGuarantyInfoList(conditionStr, pageInfo,this.getDataSource(context));
			Map<String,String> map = new HashMap<String,String>();
			map.put("guaranty_type","MORT_TYPE");
			//树形菜单服务
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
	    	SInfoUtils.addPopName(iColl, map, service);
			iColl.setName("MortGuarantyBaseInfoList");
			
			//客户名称翻译
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iColl,args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
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
