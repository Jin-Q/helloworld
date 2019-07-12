package com.yucheng.cmis.biz01line.grt.op.grtguarcont;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryGrtGuarContListOp extends CMISOperation {

	private final String modelId = "GrtGuarCont";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String action = null;
	//	String conditionStr = null;
		try{
			connection = this.getConnection(context);
		
            
		KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			action = (String) context.getDataValue("action");
			String guarId = null;
			if(queryData!=null&&queryData.containsKey("guar_id")&&queryData.getDataValue("guar_id")!=null&&!"".equals(queryData.getDataValue("guar_id"))){
				guarId = queryData.getDataValue("guar_id").toString();
				queryData.removeDataElement("guar_id");
				queryData.removeDataElement("guar_id_displayname");
			}
			
			String conditionStr = TableModelUtil.getQueryCondition_bak(this.modelId, queryData, context, false, true, false);
			if("".equals(conditionStr)){
				conditionStr = "WHERE 1=1 ";
			}
			if(guarId!=null&&!"".equals(guarId)){
				conditionStr += " and guar_cont_no in (select a.guar_cont_no from grt_guaranty_re a, (select guar_id as guaranty_no, cus_id from grt_guarantee union all select guaranty_no, cus_id from mort_guaranty_base_info) b where a.guaranty_id = b.guaranty_no and b.cus_id = '"+guarId+"') ";
			}
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId,conditionStr, context, connection);
			
			int size = 15;
			//查询合同类型为一般担保合同的记录
			if("yb".equals(action)){
				conditionStr += "AND guar_cont_type='00'";
			}
			//查询合同类型为最高额担保合同的记录
			if("zg".equals(action)){
			conditionStr += "AND guar_cont_type='01'";
			}
		    conditionStr +="order by reg_date desc";

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
			TableModelUtil.parsePageInfo(context, pageInfo);
			this.putDataElement2Context(iColl, context);

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
