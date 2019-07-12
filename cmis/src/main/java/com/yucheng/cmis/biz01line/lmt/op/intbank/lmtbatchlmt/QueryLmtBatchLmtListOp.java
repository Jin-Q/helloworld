package com.yucheng.cmis.biz01line.lmt.op.intbank.lmtbatchlmt;

import java.sql.Connection;

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

public class QueryLmtBatchLmtListOp extends CMISOperation {


	private final String modelId = "LmtBatchLmt";
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);		
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
		    //展示状态为待发起、通过、退回的数据
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false)
								+"order by serno desc";
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			int size = 10;
		    //分页信息
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);		
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			//对责任人、管理机构信息进行翻译
			SInfoUtils.addSOrgName(iColl, new String[]{"manager_br_id"});
			SInfoUtils.addUSerName(iColl, new String[]{"manager_id"});
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
