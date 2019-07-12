package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.domain.WFIUserVO;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 * <p>根据节点获取下一办理人</p>
 * @author liuhw
 */
public class GetNodeUserListOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		
		int totalCount = 0;
		IndexedCollection icoll = new IndexedCollection("topics");
		Connection connection = null;
		try {
			String instanceid = (String) context.get("instanceId");
			String nodeid = (String) context.get("nodeId");
			String currentuserid=(String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			String orgid = (String) context.getDataValue(CMISConstance.ATTR_ORGID);
			String toNodeId = (String) context.get("toNodeId");
			//初始化时为空
			if(toNodeId == null || "".equals(toNodeId)){
				return null;
			}
			
			WorkflowServiceInterface wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
			connection = this.getConnection(context);
			List<WFIUserVO> list = wfi.getNodeUserList(instanceid, currentuserid, toNodeId, connection);
			if(list!=null && list.size()>0) {
				//查询值以及对查询值做处理
				String query = (String) context.get("query");
				if(query!=null){ 
					query = query.indexOf(";")<0?query:query.substring(query.lastIndexOf(";")+1);
				}
				String s_start = (String) context.get("start");
				String s_limit = (String) context.get("limit");
				//开始行数
				int start = s_start==null?0:Integer.parseInt(s_start);
				//显示行数
				int limit = s_limit==null?10:Integer.parseInt(s_limit);
				List<WFIUserVO> su = new ArrayList<WFIUserVO>();
				//首先找出符合条件的记录
				for(int i=0;i<list.size();i++){
					//判断有没有过滤条件
					if(query!=null && !"".equals(query)){ //有过滤条件
						try{
							//名字过滤判断
							if(Pattern.matches(".*"+query+".*", list.get(i).getUserName())){
								su.add(list.get(i));
							}else		
							//汉语拼音过滤判断
							if(Pattern.matches(".*"+query+".*", list.get(i).getChineseFull())){
								su.add(list.get(i));
							}else						
							if(Pattern.matches(".*"+query+".*", list.get(i).getChineseHead())){
								su.add(list.get(i));
							}						
						}catch(PatternSyntaxException e){
							//如果抛出正则表达式语法错误,则继续下一条判断
							continue;
						}
					}else{//无过滤条件
						su.add(list.get(i));
					}
				}
				List<WFIUserVO> suPage = new ArrayList<WFIUserVO>();
				//其次找出分页记录
				for(int i = 0;i<limit;i++){
					if(i+start>=su.size()){
						break;
					}else{
						suPage.add(su.get(i+start));
					}
				}
				if(su.size() > 0){
					totalCount = su.size();
					for(WFIUserVO vo : suPage) {
						KeyedCollection kcoll = new KeyedCollection();
						kcoll.put("userId", vo.getUserId());
						kcoll.put("userName", vo.getUserName());
						kcoll.put("orgId", vo.getOrgId());
						kcoll.put("orgName", vo.getOrgName());
						kcoll.put("chineseFull", vo.getChineseFull());
						kcoll.put("chineseHead", vo.getChineseHead());
						kcoll.put("userIsmu", vo.getUserIsmu());
						icoll.add(kcoll);
					}
				}
			}
			context.put("totalCount", totalCount);
			this.putDataElement2Context(icoll, context);
		} catch (Exception e) {
			EMPLog.log("GetNodeUserListOp", EMPLog.ERROR, EMPLog.ERROR, "获取节点办理人出错！异常信息为："+e.getMessage(), e);
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		
		return "0";
	}

}
