package com.yucheng.cmis.biz01line.grt.op.grtguarantee;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.grt.component.GrtGuarContComponet;
import com.yucheng.cmis.biz01line.grt.domain.grtguarantee.GrtGuartee;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryGrtGuaranteeAllListOp extends CMISOperation {


	private final String modelId = "GrtGuarantee";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
	   // String guarContNo=null;
		try{
 			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
				
			} catch (Exception e) {}
			
		    //拼接where条件对担保人信息进行过滤
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			int end = conditionStr.length()-2;
			if(!"".equals(conditionStr)){
			 conditionStr = " and "+conditionStr.substring(8,end);
			}
			String guarty_cus_id = "";
			if(context.containsKey("GrtGuarantee.guarty_cus_id") && !"".equals(context.getDataValue("GrtGuarantee.guarty_cus_id"))){
				guarty_cus_id = context.getDataValue("GrtGuarantee.guarty_cus_id").toString();
				conditionStr += " AND t1.CUS_ID='"+guarty_cus_id+"'";
			}
			String guarContNo = "";
			if(context.containsKey("GrtGuarantee.guar_cont_no") && !"".equals(context.getDataValue("GrtGuarantee.guar_cont_no"))){
				guarContNo = context.getDataValue("GrtGuarantee.guar_cont_no").toString();
				conditionStr += " AND t.guar_cont_no='"+guarContNo+"'";
			}
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			conditionStr = " and "+conditionStr.substring(6, conditionStr.length());
			
			GrtGuartee ggt = new GrtGuartee();
			ggt.setConditionStr(conditionStr);
			//获取查询SQL
			String cond = ggt.doGetSql();
			
		    PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "10");
		    //分页处理
			IndexedCollection iColl = TableModelUtil.buildPageData(pageInfo, this.getDataSource(context),cond);
			iColl.setName("GrtGuaranteeList");
			/**翻译客户名称、登记人、登记机构*/
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			/*翻译客户名称、登记人、登记机构*/
			String[] args1=new String[] {"guarty_cus_id" };
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iColl, args1, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			SInfoUtils.addUSerName(iColl, new String[] { "input_id" });
			SInfoUtils.addSOrgName(iColl, new String[] { "manager_br_id" });
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
