package com.yucheng.cmis.biz01line.lmt.op.lmtquotaadjust;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryLmtQuotaAdjustAppListOp extends CMISOperation {


	private final String modelId = "LmtQuotaAdjustApp";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			String fin_serno = "";
			if(context.containsKey("serno")){
				fin_serno = (String) context.getDataValue("serno");
			}
			context.put("serno", fin_serno);
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			
			if(conditionStr==null||"".equals(conditionStr)){
				conditionStr = "where fin_serno='"+fin_serno+"'order by inure_date asc,end_date asc";
			}else{
				conditionStr +=conditionStr+"and fin_serno='"+fin_serno+"'order by inure_date asc,end_date asc";
			}
	    	int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
	    	IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			/* added by yangzy 2014/12/30 融资性担保公司限额调整追回等操作展示申请信息为非审批中 start */
			KeyedCollection kColl = dao.queryDetail("LmtAppFinGuar", fin_serno,connection);
			if(kColl!=null&&kColl.containsKey("serno")&&!"".equals(kColl.getDataValue("serno"))){
				for(Object obj:iColl){
					KeyedCollection kColl4Upd = (KeyedCollection)obj;
					kColl4Upd.put("approve_status", kColl.getDataValue("approve_status"));
				}
			}
			/* added by yangzy 2014/12/30 融资性担保公司限额调整追回等操作展示申请信息为非审批中 end */
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
