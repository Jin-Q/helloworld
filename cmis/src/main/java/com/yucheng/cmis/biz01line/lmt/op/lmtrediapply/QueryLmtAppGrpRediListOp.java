package com.yucheng.cmis.biz01line.lmt.op.lmtrediapply;

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
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryLmtAppGrpRediListOp extends CMISOperation {

	private final String modelId = "LmtAppGrpRedi";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String returnStr = "";
		try{
			connection = this.getConnection(context);
		
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			
			int size = 15;
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId,conditionStr, context, connection);
			conditionStr += " order by serno desc";
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			List<String> list = new ArrayList<String>();
			list.add("grp_no");
			list.add("serno");
			list.add("app_type");
			list.add("crd_totl_amt");
			list.add("manager_id");
			list.add("manager_br_id");
			list.add("approve_status");
			list.add("cur_type");
			list.add("app_date");
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			IndexedCollection iColl = dao.queryList(modelId,list,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			
			String[] args=new String[] { "grp_no" };
			String[] modelIds=new String[]{"CusGrpInfo"};
			String[] modelForeign=new String[]{"grp_no"};
			String[] fieldName=new String[]{"grp_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			SInfoUtils.addSOrgName(iColl, new String[] { "manager_br_id" });
			SInfoUtils.addUSerName(iColl, new String[] { "manager_id" });
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
		return returnStr;
	}

}
