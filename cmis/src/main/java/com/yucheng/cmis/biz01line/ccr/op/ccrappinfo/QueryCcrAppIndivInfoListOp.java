package com.yucheng.cmis.biz01line.ccr.op.ccrappinfo;

import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.ccr.msi.CcrServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryCcrAppIndivInfoListOp extends CMISOperation {


	private final String modelId = "CcrAppInfo";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			String lmt_serno = (String)context.getDataValue("serno");
			if(lmt_serno==null||"".equals(lmt_serno)){
				throw new Exception("额度申请编号[lmt_serno]不能为空！");
			}
			
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, true, false);
//			conditionStr = StringUtil.transConditionStr(conditionStr, "cus_name");
			if(conditionStr==null||"".equals(conditionStr.trim())){
				conditionStr = " where lmt_serno='"+lmt_serno+"'";
			}else{
				conditionStr += " and lmt_serno='"+lmt_serno+"'";
			}
//			conditionStr=recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			int size = 15;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iCollApp = dao.queryList(modelId,conditionStr,connection);
			
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[] { "cus_id" };
			String[] fieldName=new String[]{"cus_name"};
			SystemTransUtils.dealName(iCollApp, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			
			SInfoUtils.addUSerName(iCollApp, new String[] { "input_id"});
			SInfoUtils.addSOrgName(iCollApp, new String[] { "input_br_id"});
			iCollApp.setName(iCollApp.getName()+"List");
			this.putDataElement2Context(iCollApp, context);
			
			//增加历史信用评级历史查询
			DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			CcrServiceInterface service = (CcrServiceInterface)serviceJndi.getModualServiceById("ccrServices", "ccr");
			IndexedCollection CusIdic = new IndexedCollection();
			CusIdic = SqlClient.queryList4IColl("queryCusIdForIndivByLmtSerno", lmt_serno, connection);
			String condition = " where lmt_serno != '"+lmt_serno+"'";
			
			//根据客户码串获得其评级信息
			IndexedCollection inColl = service.getCcrAppInfoByCusIdStr(CusIdic,pageInfo,dataSource,condition);
			inColl.setName("CcrAppInfoHisList");
			String[] args1=new String[] { "cus_id" };
			String[] modelIds1=new String[]{"CusBase"};
			String[] modelForeign1=new String[] { "cus_id" };
			String[] fieldName1=new String[]{"cus_name"};
			SystemTransUtils.dealName(inColl, args1, SystemTransUtils.ADD, context, modelIds1,modelForeign1, fieldName1);
			
			SInfoUtils.addUSerName(inColl, new String[] { "manager_id"});
			SInfoUtils.addSOrgName(inColl, new String[] { "manager_br_id"});
			
			this.putDataElement2Context(inColl, context);
			
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
