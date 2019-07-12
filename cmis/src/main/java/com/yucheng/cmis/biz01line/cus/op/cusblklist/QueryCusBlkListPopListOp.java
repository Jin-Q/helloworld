package com.yucheng.cmis.biz01line.cus.op.cusblklist;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.StringUtil;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryCusBlkListPopListOp extends CMISOperation {

	private final String modelId = "CusBlkList";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			conditionStr = StringUtil.transConditionStr(conditionStr, "cus_name");
			if (conditionStr == null || conditionStr.trim().equals(""))
				conditionStr = " where 1=1 ";
			
			//过滤掉在途的申请
//			StringBuffer sb = new StringBuffer("");
//			sb.append(" and not exists (select 1 from cus_blk_list a, cus_blk_logoutapp b");
//			sb.append(" where a.cert_type = b.cert_type ");
//			sb.append(" and a.cert_code = b.cert_code ");
//			sb.append("and b.approve_status not in ('998', '997'))");
			
//			conditionStr = conditionStr + sb.toString();
			//added by yangzy 2015/04/16 需求：XD150325024，集中作业扫描岗权限改造 start
			String cusQueryCondition = "";
			if(context.containsKey("cusTypCondition")&&context.getDataValue("cusTypCondition")!=null&&!"".equals(context.getDataValue("cusTypCondition"))){
				cusQueryCondition = (String) context.getDataValue("cusTypCondition");
				conditionStr += " and "+cusQueryCondition;
			}
			//added by yangzy 2015/04/16 需求：XD150325024，集中作业扫描岗权限改造 end
			int size = 15;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List<String> list = new ArrayList<String>();
			list.add("black_date");
//			list.add("serno");
			list.add("cus_id");
			list.add("cus_name");
			list.add("cert_type");
			list.add("cert_code");
			list.add("black_type");
			list.add("black_level");
			list.add("legal_phone");
			list.add("legal_name");
			list.add("legal_addr");
			list.add("black_reason");
			list.add("manager_id");
			list.add("manager_br_id");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			
			Map<String,String> map = new HashMap<String, String>();
			map.put("legal_addr", "STD_GB_AREA_ALL");
			//树形菜单服务
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			SInfoUtils.addPopName(iColl, map, service);
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
