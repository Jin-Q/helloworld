package com.yucheng.cmis.platform.organization.suser.op;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryAllSUserPopListOp extends CMISOperation {
	
	//所要操作的表模型
	private final String modelId = "SUserOrg";
	
	/**
	 * 业务逻辑执行的具体实现方法
	 */
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			//获得查询的过滤数据
			KeyedCollection queryData = null;
			String cusQueryCondition = "";
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			try {
            	cusQueryCondition = (String) context.getDataValue("cusTypCondition");
            } catch (Exception e) {
            	EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "没有配置查询条件", null);}


			//获得查询条件，交集、精确查询，忽略空值
			String conditionStr = TableModelUtil.getQueryCondition_bak(modelId, queryData, context, false, true, false);
			
			if(cusQueryCondition==null || "".equals(cusQueryCondition)){
				cusQueryCondition = " 1=1 ";
			}
			
			if(conditionStr!=null && !"".equals(conditionStr)){
				conditionStr = conditionStr + " and " + cusQueryCondition ;
			}else{
				conditionStr = " where " + cusQueryCondition;
			}
			
			String fg = "";
			String receiveOrg = "";
			if(context.containsKey("receiveOrg")){
				receiveOrg = (String)context.getDataValue("receiveOrg");
			}else{
				context.put("receiveOrg", receiveOrg);
			}
			if(context.containsKey("flag")){
				fg = (String)context.getDataValue("flag");
			}else{
				context.put("flag", fg);
			}
			if("receive".equals(fg)){
				if(StringUtils.isNotEmpty(receiveOrg)){
					conditionStr = conditionStr + " and orgid = '"+ receiveOrg +"' ";
				}
			}
			
			int size = 20;
			//设置只在第一次查询总记录数
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			//获取可以分页的OracleDao对象
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List<String> list = new ArrayList<String>();
			list.add("actorno");
			list.add("actorname");
			list.add("telnum");
			list.add("idcardno");
			list.add("orgid");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName("SUserList");
			
			SInfoUtils.addSOrgName(iColl, new String[] { "orgid" });
			
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
