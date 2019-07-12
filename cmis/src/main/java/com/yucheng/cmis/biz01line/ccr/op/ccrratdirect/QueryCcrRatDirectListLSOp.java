package com.yucheng.cmis.biz01line.ccr.op.ccrratdirect;

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

/**
 *@Classname	QueryCcrRatDirectListLSOp.java
 *@Version 1.0	
 *@Since   1.0 	Mar 1, 2010 
 *@Copyright 	yuchengtech
 *@Author 		eric
 *@Description：
 *@Lastmodified 
 *@Author	    
 */
public class QueryCcrRatDirectListLSOp  extends CMISOperation {

	private final String modelId = "CcrRatDirect";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, true, false)
									;
			if(conditionStr==null||"".equals(conditionStr.trim())){
				conditionStr += " where APPROVE_STATUS in('997','998','990','111') order by serno desc";
			}else{
				
				conditionStr += " and APPROVE_STATUS in('997','998','990','111') order by serno desc";
			}

			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			//modify by chenBQ 20190322 设置每页15行
			int size = 15;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		

			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
//			SInfoUtils.addUSerName(iColl, new String[] { "manager_id","input_id" });
//			SInfoUtils.addSOrgName(iColl, new String[] { "manager_br_id","input_br_id" });
			
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
