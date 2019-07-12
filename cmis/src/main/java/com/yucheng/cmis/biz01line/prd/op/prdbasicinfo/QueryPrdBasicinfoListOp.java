package com.yucheng.cmis.biz01line.prd.op.prdbasicinfo;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryPrdBasicinfoListOp extends CMISOperation {


	private final String modelId = "PrdBasicinfo";
	

	public String doExecute(Context context) throws EMPException {
		String conditionStr ="";
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
		
			conditionStr = TableModelUtil.getQueryCondition_bak(modelId, queryData, context, false, true, false);
		
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			int size = 15;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List list = new ArrayList();
			list.add("prdname");
			list.add("prdowner");
			list.add("supcatalog");
			list.add("prdid");
			list.add("prddescribe");
			list.add("prdversion");
			list.add("prdmanager");
			list.add("startdate");
			list.add("enddate");
			list.add("prdstatus");
			list.add("guarway");
			list.add("currency");
			list.add("preventtactics");
			list.add("loanform");
			list.add("contform");
			list.add("pvpform");
			list.add("contmapping");
			list.add("pvpmapping");
			list.add("loanflow");
			list.add("pvpway");
			list.add("payflow");
			list.add("repayway");
			list.add("costset");
			list.add("businessrule");
			list.add("policytactics");
			list.add("datacollection");
			list.add("comments");
			list.add("inputid");
			list.add("inputdate");
			list.add("orgid");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(iColl, new String[]{"inputid"});
			SInfoUtils.addSOrgName(iColl, new String[]{"orgid"});
			
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
