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

public class QueryLmtQuotaAdjustListOp extends CMISOperation {


	private final String modelId = "LmtQuotaAdjust";
	private final String modelId_app = "LmtQuotaAdjustApp";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			String agr_no = (String) context.getDataValue("agr_no");
			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			
		    if(context.containsKey("op")&&"update".equals(context.getDataValue("op"))){
		    	if(conditionStr==null||"".equals(conditionStr)){
					conditionStr = "where fin_agr_no='"+agr_no+"' and approve_status in ('000','111','992','993') order by inure_date asc,end_date asc";
				}else{
					conditionStr +=conditionStr+"and fin_agr_no='"+agr_no+"' and approve_status in ('000','111','992','993') order by inure_date asc,end_date asc";
				}
				/* added by yangzy 2014/12/30 融资性担保公司限额调整追回等操作展示申请信息为非审批中 start */
		    	String fin_serno = "";
		    	/* added by yangzy 2014/12/30 融资性担保公司限额调整追回等操作展示申请信息为非审批中 end */
		    	IndexedCollection iColl1 = dao.queryList(modelId_app,null ,conditionStr,connection);
		    	if(iColl1==null || iColl1.size()== 0){
		    		IndexedCollection iColl2 = dao.queryList(modelId," where fin_agr_no='"+agr_no+"' order by inure_date asc,end_date asc ", connection);
		    		if(iColl2!=null&&iColl2.size()>0){
		    			for(int j=0;j<iColl2.size();j++){
		    				KeyedCollection kColl2 = (KeyedCollection) iColl2.get(j);
		    				kColl2.setName("LmtQuotaAdjustApp");
		    				/* modified by yangzy 2014/12/30 融资性担保公司限额调整追回等操作展示申请信息为非审批中 start */
		    				kColl2.remove("serno");
		    				/* modified by yangzy 2014/12/30 融资性担保公司限额调整追回等操作展示申请信息为非审批中 end */
		    				kColl2.put("approve_status","000");
				    		dao.insert(kColl2, connection);
		    			}
		    		}
		    	}else{
		    		/* added by yangzy 2014/12/30 融资性担保公司限额调整追回等操作展示申请信息为非审批中 start */
		    		KeyedCollection kColl = (KeyedCollection) iColl1.get(0);
		    		if(kColl!=null&&kColl.containsKey("fin_serno")&&kColl.getDataValue("fin_serno")!=null&&!"".equals(kColl.getDataValue("fin_serno"))){
		    			fin_serno = (String) kColl.getDataValue("fin_serno");
		    		}
		    		/* added by yangzy 2014/12/30 融资性担保公司限额调整追回等操作展示申请信息为非审批中 end */
		    	}
		    	int size = 10;
				PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
				IndexedCollection iColl = dao.queryList(modelId_app,null ,conditionStr,pageInfo,connection);
				iColl.setName("LmtQuotaAdjustList");
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
		    }else{
		    	if(conditionStr==null||"".equals(conditionStr)){
					conditionStr = "where fin_agr_no='"+agr_no+"'order by inure_date asc,end_date asc";
				}else{
					conditionStr +=conditionStr+"and fin_agr_no='"+agr_no+"'order by inure_date asc,end_date asc";
				}
		    	int size = 10;
				PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		    	IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
				iColl.setName(iColl.getName()+"List");
				this.putDataElement2Context(iColl, context);
				TableModelUtil.parsePageInfo(context, pageInfo);
		    }
//			RecordRestrict recordRestrict = this.getRecordRestrict(context);
//			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
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
