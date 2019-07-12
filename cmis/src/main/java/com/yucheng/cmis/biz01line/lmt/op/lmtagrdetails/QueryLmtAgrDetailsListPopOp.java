package com.yucheng.cmis.biz01line.lmt.op.lmtagrdetails;


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
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryLmtAgrDetailsListPopOp extends CMISOperation {

	private final String modelId = "LmtAgrDetails";
	private final String modelIdApp = "LmtAppFrozeUnfroze";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			KeyedCollection queryData = null;
			
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			
			//1.判断传入参数subConndition中是否有值，有值则代表集团授信协议、联保授信协议、行业授信协议下查看台账信息；则只能查询当前
			//授信协议下的分项额度台账subConndition=lmt_status='10'
			if(context.containsKey("subConndition") && !"".equals(context.getDataValue("subConndition"))){
				if(null == conditionStr || "".equals(conditionStr)){
					conditionStr = " WHERE 1=1 ";
				}
				conditionStr = conditionStr + " AND "+ context.getDataValue("subConndition");
			}			
			
			//2.否则为授信台账列表，则使用记录集权限进行过滤，加载所有有权限查看的授信台账
			else{
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			}
			conditionStr += " ORDER BY AGR_NO DESC, LIMIT_CODE DESC";
			int size = 10;
		   
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);

			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			/**翻译额度名称**/
			args=new String[] { "limit_name" };
			modelIds=new String[]{"PrdBasicinfo"};
			modelForeign=new String[]{"prdid"};
			fieldName=new String[]{"prdname"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
			
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
