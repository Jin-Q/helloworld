package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sourceforge.pinyin4j.PinyinHelper;

import com.ecc.echain.db.DbControl;
import com.ecc.echain.workflow.engine.EVO;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.component.WFIComponent;
import com.yucheng.cmis.platform.workflow.domain.WFIUserVO;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.ComponentHelper;

/**
 * <p>根据节点获取下一办理人信息</p>
 * @author liuhw
 */
public class GetNodeUserListOp0712 extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		

		class QueryWFIUser{
			public List<WFIUserVO> query(String isMu,List<WFIUserVO> users) throws Exception{
				//查询数据库,查找出用户的组织机构号,组织机构名称等相关信息
				String whereCon = "(";
				for(int i=0;i<users.size();i++){
					if(i==users.size()-1){
						whereCon+="'"+users.get(i).getUserId().substring(2)+"'";
						break;
					}
					whereCon+="'"+users.get(i).getUserId().substring(2)+"',";
				}
				whereCon+=")";
				
				List<WFIUserVO> ret = new ArrayList<WFIUserVO>();
				Connection c = DbControl.getInstance().getConnection();
				java.sql.Statement st = c.createStatement();
				String sql = "select st.actorno,st.actorname,st.orgid,u.organname from s_user st,s_org u where st.orgid = u.organno ";
				if(users.size()>0)
					sql=sql+" and st.actorno in "+whereCon;
				ResultSet rs = st.executeQuery(sql);
				while(rs.next()){
					WFIUserVO su = new WFIUserVO();
					su.setUserId("U."+rs.getString(1));
					su.setUserName(rs.getString(2));
					su.setOrgId(rs.getString(3));
					su.setOrgName(rs.getString(4));
					su.setUserIsmu(isMu);
					String[] chinese = toChinese(su.getUserName());
					su.setChineseFull(chinese[0]);
					su.setChineseHead(chinese[1]);
					ret.add(su);
				}
				
				if(st!=null){
					st.close();
				}
				if(c!=null){
					c.close();
				}
				return ret;
			}
			
			public String[] toChinese(String orgi){
				StringBuffer sb = new StringBuffer();
				StringBuffer sb2 = new StringBuffer();
				if(orgi==null)
					orgi="";
				char[] orgiChar = orgi.toCharArray();
				String[] ret = new String[2];
				for(int i=0;i<orgiChar.length;i++){
					String[] newStr = PinyinHelper.toHanyuPinyinStringArray(orgiChar[i]);
					if(newStr!=null){
						sb.append(newStr[0].substring(0,newStr[0].length()-1));
						sb2.append(newStr[0].substring(0,1));
					}
				}
				ret[0] = sb.toString();
				ret[1] = sb2.toString();
				return ret;
			}
		}
		
		String instanceid = (String) context.get("instanceId");
		String nodeid = (String) context.get("nodeId");
		String currentUserId=(String) context.get("currentUserId");
		String s_orgid = (String) context.get("organNo");
		
		
		String toNodeid = (String) context.get("toNodeid");

		//初始化时为空
		if(toNodeid == null || "".equals(toNodeid)){
			return null;
		}
		
		EVO evo=new EVO();
		evo.setInstanceID(instanceid);
		evo.setNodeID(nodeid);
		evo.setCurrentUserID(currentUserId);
		evo.setOrgid(s_orgid);
		//evo.setConnection(null);
		
		

		try {
			long starttime1 = System.currentTimeMillis();
			
			//选择指定节点ID的办理人员
			WorkflowServiceInterface wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
			List<WFIUserVO> list = wfi.getNodeUserList(instanceid, currentUserId, toNodeid, this.getConnection(context));
			
			if(list == null || list.size()== 0){
				context.put("totalCount", 0);
				context.put("topics", "[]");
				return null;
			}
			
			long starttime = System.currentTimeMillis();
			
			
			
			boolean ismu = Boolean.parseBoolean(list.get(0).getUserIsmu());

			list = new QueryWFIUser().query(ismu?"true":"false",list);
			
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
			
			Map map = new HashMap();
			
			if(su.size()<=0){
				context.put("totalCount", 0);
				context.put("topics", "[]");
			}else{
				map.put("topics", suPage);//记录集
				map.put("totalCount", su.size());//总记录数
				/*ComponentHelper helper = new ComponentHelper();
				helper.d*/
				//转换成JSON
				JSONObject obj = JSONObject.fromObject(map);
//				response.getWriter().write(new String(obj.toString()));
				
				context.put("totalCount", 0);
				Object[] topics = {};
				context.put("topics", topics);
				
			}
			context.put("totalCount", 0);
			Object[] topics = {};
			context.put("topics", null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException(e);
		}
		
		return "0";
	}

}
