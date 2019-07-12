package com.yucheng.cmis.biz01line.lmt.op.lmtagrjointcoop;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryLmtAgrJointPopOp extends CMISOperation {

	private final String modelId = "LmtAgrJointCoop";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			KeyedCollection queryData = null;
			String flag = "";
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false) ;
			if("".equals(conditionStr)){
				conditionStr = "WHERE 1=1 ";
			}
			/*if(context.containsKey("condition")){
				conditionStr += context.getDataValue("condition");
			}*/
			if(context.containsKey("flag")){
				flag = (String)context.getDataValue("flag");
			}
			if("1".equals(flag)){
				conditionStr += " AND AGR_STATUS='002' AND COOP_TYPE='010' AND ADD_MONTHS(TO_DATE(END_DATE, 'yyyy-mm-dd'),6) >= (SELECT TO_DATE(OPENDAY,'yyyy-mm-dd') FROM PUB_SYS_INFO) ";
			}else if("2".equals(flag)){
				conditionStr += " AND AGR_STATUS in('002','004') AND COOP_TYPE='010' AND ADD_MONTHS(TO_DATE(END_DATE, 'yyyy-mm-dd'),6) >= (SELECT TO_DATE(OPENDAY,'yyyy-mm-dd') FROM PUB_SYS_INFO) ";
			}else if("3".equals(flag)){
				conditionStr += " AND AGR_STATUS='002' AND COOP_TYPE!='010' AND ADD_MONTHS(TO_DATE(END_DATE, 'yyyy-mm-dd'),6) >= (SELECT TO_DATE(OPENDAY,'yyyy-mm-dd') FROM PUB_SYS_INFO) ";
			}
			//COOP_TYPE=010为联保小组协议 其他都为合作方
			//conditionStr += " AND AGR_STATUS='002' AND COOP_TYPE='010' "; 
			//conditionStr += " AND ADD_MONTHS(TO_DATE(END_DATE, 'yyyy-mm-dd'),6) >= (SELECT TO_DATE(OPENDAY,'yyyy-mm-dd') FROM PUB_SYS_INFO)  ";   //顺延六个月
			conditionStr += "order by agr_no desc";
			
			int size = 15;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr, pageInfo,connection);

			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用	
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
			SInfoUtils.addUSerName(iColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(iColl, new String[]{"input_br_id"});
			
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
