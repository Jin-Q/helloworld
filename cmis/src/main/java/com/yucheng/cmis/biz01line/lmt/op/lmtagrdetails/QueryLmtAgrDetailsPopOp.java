package com.yucheng.cmis.biz01line.lmt.op.lmtagrdetails;
 
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryLmtAgrDetailsPopOp extends CMISOperation {

	private final String modelId = "LmtAgrDetails";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			if("".equals(conditionStr)){
				conditionStr = "WHERE 1=1 ";
			}
			/*if(context.containsKey("condition")){
				conditionStr += context.getDataValue("condition");
			}*/
			String fg = "";
			if(context.containsKey("flag")){
				fg = (String) context.getDataValue("flag");
			}else{
				context.put("flag", fg);
			}
			String cusId = "";
			String openDay = "";
			if(context.containsKey("cus_id")){
				cusId = (String) context.getDataValue("cus_id");
			}else{
				context.put("cus_id", cusId);
			}
			if(context.containsKey("openDay")){
				openDay = (String) context.getDataValue("openDay");
			}else{
				context.put("openDay", openDay);
			}
			if("1".equals(fg)){
				conditionStr += " and lmt_status='10' and cus_id ='"+cusId+"' ";
			}else if("2".equals(fg)){
				conditionStr += " and cus_id ='"+cusId+"' ";
			}else if("3".equals(fg)){
				conditionStr += " AND SUB_TYPE IN('01','05') AND AGR_NO IN(SELECT AGR_NO FROM LMT_AGR_INFO WHERE GRP_AGR_NO IS NULL UNION ALL SELECT AGR_NO FROM LMT_AGR_INDIV) ";
			}else if("4".equals(fg)){
				conditionStr += " AND lmt_status='10' AND SUB_TYPE IN('01','05') AND end_date>'"+openDay+"' AND cus_id='"+cusId+"' ";
			}
			
			//condition+=" AND SUB_TYPE IN('01','05') AND AGR_NO IN(SELECT AGR_NO FROM LMT_AGR_INFO WHERE GRP_AGR_NO IS NULL UNION ALL SELECT AGR_NO FROM LMT_AGR_INDIV)";
			
			conditionStr += " order by limit_code";
			int size = 15;
		   
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			/**modified by lisj 2015-2-10 需求编号【HS141110017】保理业务改造 begin**/
			List<String> list = new ArrayList<String>();
			list.add("agr_no");
			list.add("cus_id");
			list.add("sub_type");
			list.add("limit_name");
			list.add("term");
			list.add("crd_amt");
			list.add("guar_type");
			list.add("limit_code");
			list.add("start_date");
			list.add("end_date");
			list.add("limit_type");  //额度类型
			list.add("core_corp_cus_id");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			
			String[] args=new String[] { "cus_id","core_corp_cus_id" };
			String[] modelIds=new String[]{"CusBase","CusBase"};
			String[] modelForeign=new String[]{"cus_id","cus_id"};
			String[] fieldName=new String[]{"cus_name","cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			/**modified by lisj 2015-2-10 需求编号【HS141110017】保理业务改造 end**/
			/**翻译额度名称   2013-11-29 唐顺岩**/
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
