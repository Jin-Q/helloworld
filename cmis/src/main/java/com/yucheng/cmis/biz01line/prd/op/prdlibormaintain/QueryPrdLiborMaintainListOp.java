package com.yucheng.cmis.biz01line.prd.op.prdlibormaintain;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.iqp.appPub.DateUtils;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryPrdLiborMaintainListOp extends CMISOperation {


	private final String modelId = "PrdLiborMaintain";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false)
									+"order by pk_id desc,libor_date desc";
			
		
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			int size = 10;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			SInfoUtils.addUSerName(iColl, new String[] { "maintain_id" });
			SInfoUtils.addUSerName(iColl, new String[] { "check_id" });
			SInfoUtils.addSOrgName(iColl, new String[] { "organno" });
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			String openDay = (String)context.getDataValue("OPENDAY");
			String forwardDay = DateUtils.getEndDate("003", openDay, -1);
			context.put("forwardDay",forwardDay);
			
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
