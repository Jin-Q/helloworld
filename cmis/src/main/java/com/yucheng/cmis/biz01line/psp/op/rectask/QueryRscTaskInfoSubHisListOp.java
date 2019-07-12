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
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.exception.AsynException;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryRscTaskInfoSubHisListOp  extends CMISOperation {
	private final String modelId = "RscTaskInfoSubHis";

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String conditionStr = "";
		TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		try {
			connection = this.getConnection(context);
			// 页面查询类别，前台参数键名为"searchType"，参数值为"quickquery"表示搜索框快速查询 
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			//定义查询参数map
			Map paramMap = new HashMap();
			
		
			//取查询关键字为in的查询条件,这里没有用到，注释
			//EUIUtil.assembleSearchParamerter(context, "in", EUIUtil.QueryParamMatchType_Array, paramMap);
			
            int size = 15; 
			
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));

			conditionStr = TableModelUtil.getQueryCondition_bak(modelId, queryData, context, false, true, false);
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			IndexedCollection list = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			list.setName(list.getName()+"List");
		 
			for(int i=0 ;list.size()>i;i++){
				KeyedCollection r = (KeyedCollection)list.get(i);
				KeyedCollection duty = null;
				if(r.getDataValue("identy_duty").equals("Q0000")){
					r.put("identy_duty_cname","已完成");
				}else{
					duty = dao.queryDetail("SDuty", (String)r.getDataValue("identy_duty"),connection);
					if(duty!=null){
						r.put("identy_duty_cname",duty.getDataValue("dutyname"));
					}
				} 
				list.set(i, r);
			}
			//将查询结构放入Context中以便前端获查询结结
			this.putDataElement2Context(list, context); 
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
