package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.organization.msi.OrganizationCacheServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
/**
 * 根据关键字搜索用户,ext方式,所有用户选择页面使用
 * @author liuhw
 *
 */
public class GetSearchUsersOp extends CMISOperation {

	private final static String ACTIONTYPE_INIT = "init";  //combobox初级初始化
	private final static String ACTIONTYPE_SEARCH = "search"; //用户搜索
	@Override
	public String doExecute(Context context) throws EMPException {

		String actionType = null;
		Connection connection = null;
		String retValue = null;
		try {
			int totalCount = 0;
			IndexedCollection icoll = new IndexedCollection("topics");
			actionType = (String) context.getDataValue("actionType");
			if(!ACTIONTYPE_INIT.equals(actionType) && !ACTIONTYPE_SEARCH.equals(actionType)) {
				throw new EMPException("参数[actionType]不合法!");
			}
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			List fields = new ArrayList();
			fields.add("actorno");
			fields.add("actorname");
			fields.add("orgid");
			OrganizationCacheServiceInterface orgCacheMsi = (OrganizationCacheServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById("organizationCacheServices", "organization");
			if(ACTIONTYPE_INIT.equals(actionType)) {
				//查询值以及对查询值做处理
				String query = (String) context.get("query");
				String s_start = (String) context.get("start");
				String s_limit = (String) context.get("limit");
				//开始行数
				int start = s_start==null?0:Integer.parseInt(s_start);
				//显示行数
				int limit = s_limit==null?10:Integer.parseInt(s_limit);
				String condition = null;
				if(query!=null && !"".equals(query.trim())) {
					condition = " WHERE ACTORNAME LIKE '%"+query+"%'";
				}
				IndexedCollection icollTmp = dao.queryList("SUser", condition, connection);
				if(icollTmp!=null && icollTmp.size()>0) {
					totalCount = icollTmp.size();
					//找出分页记录
					for(int i=0; i<limit; i++){
						if(i+start >= totalCount){
							break;
						}else{
							KeyedCollection kcoll = (KeyedCollection) icollTmp.get(i+start);
							icoll.add(kcoll);
						}
					}
				}
				icoll = orgCacheMsi.addOrgName(icoll, new String[]{"orgid"});
				context.put("totalCount", totalCount);
				this.putDataElement2Context(icoll, context);
				retValue = "init";
			} else {
				boolean isMatch = false;
				String userName = (String) context.get("userName");
				StringBuffer sb = new StringBuffer();
				sb.append("<table>");
				if(userName==null || userName.trim().equals("")) {
					isMatch = false;
				} else {
					String condition = " WHERE ACTORNAME LIKE '%"+userName+"%'";
					IndexedCollection icollTmp = dao.queryList("SUser", condition, connection);
					if(icollTmp!=null && icollTmp.size()>0) {
						isMatch = true;
						icoll = orgCacheMsi.addOrgName(icollTmp, new String[]{"orgid"});
						for(int i=0; i<icoll.size(); i++) {
							KeyedCollection kcoll = (KeyedCollection) icoll.get(i);
//							String retStr = "<tr><td>"+kcoll.getDataValue("orgid_displayname")+ "/" +kcoll.getDataValue("actorname")+"_"+kcoll.getDataValue("actorno")+"</td></tr>";
							sb.append("<tr><td>").append(kcoll.getDataValue("orgid_displayname")).append("/").append(kcoll.getDataValue("actorname")).append("_").append(kcoll.getDataValue("actorno")).append("</td></tr>");
						}
					}
				}
				sb.append("</table>");
				if(!isMatch) sb = new StringBuffer("");
				String retStr = sb.toString();
				context.put("resultStr", retStr);
				retValue = "search";
			}
			
		} catch (Exception e) {
			EMPLog.log("GetSearchUsersOp", EMPLog.ERROR, EMPLog.ERROR, "根据关键字搜索用户处理出错！异常信息为："+e.getMessage(), e);
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		return retValue;
	}
	
}
