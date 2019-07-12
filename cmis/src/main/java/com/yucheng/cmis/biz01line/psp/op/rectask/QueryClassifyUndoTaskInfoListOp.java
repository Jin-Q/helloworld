package com.yucheng.cmis.biz01line.psp.op.rectask;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.AsynException;
import com.yucheng.cmis.pub.util.NewStringUtils;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryClassifyUndoTaskInfoListOp extends CMISOperation {
	private final String modelId = "RscTaskInfo";

	@Override
	public String doExecute(Context context) throws EMPException {
		TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		Connection connection = null;
		String conditionStr = "";
		try {
			connection = this.getConnection(context); 
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			//定义查询参数map
			Map paramMap = new HashMap();
			if(queryData!=null&&queryData.containsKey("input_br_id")&&NewStringUtils.isNotBlank((String)queryData.getDataValue("input_br_id"))){
				paramMap.put("input_br_id", (String)queryData.getDataValue("input_br_id"));
			}
			
			//解析页面排序字段
		 
			//声明分页信息，默认读取10条记录
			int size = 15; 
				
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
	
			IndexedCollection iColl  = new IndexedCollection();
            iColl = SqlClient.queryList4IColl("queryClassifyUndoTaskInfoLike",paramMap ,new String[]{"condition1"},pageInfo.beginIdx,pageInfo.endIdx, connection);
	
		   
		   iColl.setName("RscTaskInfoList");
		   pageInfo.setRecordSize(String.valueOf(iColl.size()));
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(iColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(iColl, new String[]{"input_br_id"});
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
		} catch (EMPException ee) {
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
