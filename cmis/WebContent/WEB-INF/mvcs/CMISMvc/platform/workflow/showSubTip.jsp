<%@page import="com.yucheng.cmis.platform.workflow.domain.WFICommentVO"%>
<%@page import="com.yucheng.cmis.platform.workflow.domain.WFIGatherCommentVO"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ page import="java.util.*" %>
<html>
<head>
<%
	String subType = request.getParameter("subType");
	String isShut=request.getParameter("isShut");//是否显示关闭  0不显示 1显示
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	//会办流程意见
	List<WFIGatherCommentVO> gatherWfComment = null;
	//会办意见
	List<WFIGatherCommentVO> gatherComment = null;
	//流程跟踪意见
	List listGz = null;
	//流程审批意见
	List<WFICommentVO> listYj = null;
	if(subType.equals("subGather")) {
		gatherWfComment = (List<WFIGatherCommentVO>)context.getDataValue("GatherWfCommentList");
		gatherComment = (List<WFIGatherCommentVO>)context.getDataValue("GatherCommentList");	
		
	} else if(subType.equals("subFlow")||subType.equals("mainFlow")) {
		listGz = (List)context.getDataValue("WorkFlowHisList");
		listYj = (List<WFICommentVO>)context.getDataValue("WFICommentList");
	}
%>

<title>查看子流程或会办相关意见信息</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/workflow/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/workflow/ext/ext-all.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/workflow/ext/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/styles/workflow/default.css" />

<script type="text/javascript">
var originClassName;
function invertmenu(){
	if (event.srcElement.tagName == 'TD'){
		originClassName=event.srcElement.parentElement.className;
		event.srcElement.parentElement.className = 'trclass3';
	}
	else{
		originClassName=event.srcElement.parentElement.parentElement.className;
		event.srcElement.parentElement.parentElement.className = 'trclass3';
	}
}
function resumemenu(){
	if (event.srcElement.tagName == 'TD')
		event.srcElement.parentElement.className=originClassName;
	else
		event.srcElement.parentElement.parentElement.className =originClassName;
}
	function showta(id){
		if(typeof win!='undefined')
		win.close();
		win=new Ext.Window({title:'<div style="height:15px;padding-top:8px">意见查看</div>',width:400,height:250,html:document.getElementById(id).value});
		win.show();
	}
	
</script>
</head>
<body onload="">
<form action="<%=request.getContextPath()%>/echaincommonservlet">
	<input type="hidden" id="method" name="method" value="echainflowdemo">
	<input type="hidden" id="actionType" name="actionType" />
	
	
	
	<%
	if(subType.equals("subGather")){
	%>
		<br><br>
		
	  	<fieldset><legend>流程意见列表</legend><br>
		<table class=tablemain cellspacing=1 cellpadding=0>
			<tr class=trtitle>
				<td width="5%">序号</td>
				<td width="8%">办理人</td>
				<td width="15%">办理时间</td>
				<td width="30%">执行操作</td>
				<td >意见内容</td>
			</tr>
			
			<%
				WFIGatherCommentVO actionVO = null;
				boolean tr = true;
				int k=1;
				if(gatherWfComment==null || gatherWfComment.isEmpty()){
					out.print("<tr class=trclass><td colspan='5'>没有流程意见信息</td></tr>");
				}
				
				for(int i=0; i<gatherWfComment.size(); i++){
					actionVO = (WFIGatherCommentVO)gatherWfComment.get(i);
					if(tr){
						out.print("<tr class=trclass onmouseout='resumemenu()' onmouseover='invertmenu()'>");
					}else{
						out.print("<tr class=trclass2 onmouseout='resumemenu()' onmouseover='invertmenu()'>");
					}
			%>
				<td><%=k %></td>
				<td><%=actionVO.getTransActorName() %></td>
				<td><%=actionVO.getActTime() %></td>
				<td><%=actionVO.getActionName() %></td>
				<td><a href="#" onclick="showta('wf_<%=k%>')"><%=actionVO.getSuggest()==null?"":actionVO.getSuggest().length()>30?actionVO.getSuggest().substring(0,30)+"...":actionVO.getSuggest() %></a></td>
				<input type="hidden" id="wf_<%=k%>" value='<%=actionVO.getSuggest()==null?"":actionVO.getSuggest()%>'>
			</tr>	
			<%
					tr = !tr;
					k++;
				}	
			%>
			
		</table>
		<br>
	</fieldset>
	<br>
	
	<fieldset><legend>会办意见列表</legend><br>
		<table class=tablemain cellspacing=1 cellpadding=0>
			<tr class=trtitle>
				<td width="5%">序号</td>
				<td width="8%">办理人</td>
				<td width="15%">办理时间</td>
				<td width="30%">执行操作</td>
				<td >意见内容</td>
			</tr>
			
			<%
				actionVO = null;
				tr = true;
				k=1;
				
				if(gatherComment==null || gatherComment.isEmpty()){
					out.print("<tr class=trclass><td colspan='5'>没有会办意见信息</td></tr>");
				}
				
				for(int i=0; i<gatherComment.size(); i++){
					actionVO = (WFIGatherCommentVO)gatherComment.get(i);
					if(tr){
						out.print("<tr class=trclass onmouseout='resumemenu()' onmouseover='invertmenu()'>");
					}else{
						out.print("<tr class=trclass2 onmouseout='resumemenu()' onmouseover='invertmenu()'>");
					}
			%>
				<td><%=k %></td>
				<td><%=actionVO.getTransActorName() %></td>
				<td><%=actionVO.getActTime() %></td>
				<td><%=actionVO.getActionName() %></td>
				<td><a href="#" onclick="showta('wf2_<%=k%>')"><%=actionVO.getSuggest()==null?"":actionVO.getSuggest().length()>30?actionVO.getSuggest().substring(0,30)+"...":actionVO.getSuggest() %></a></td>
				<input type="hidden" id="wf2_<%=k%>" value='<%=actionVO.getSuggest()==null?"":actionVO.getSuggest()%>'>
			</tr>	
			<%
					tr = !tr;
					k++;
				}	
			%>
			
		</table>
		<br>
	</fieldset>
		<br><br>
	<%
	}else if(subType.equals("subFlow")||subType.equals("mainFlow")){
	%>
		<br><br>
		<fieldset><legend><%=subType.equals("subFlow")?"子":"主" %>流程流程意见信息</legend>
			<br>
			<table class=tablemain cellspacing=1 cellpadding=0>
			<tr class=trtitle>
			<td width="5%">序号</td>
			<td width="12%">审批节点</td>
			<td width="15%">意见时间</td>
			<td width="10%">审批人</td>
			<td width="10%">意见标识</td>
			<td>流程意见</td>
			</tr>
			<%
			if(listYj==null||listYj.isEmpty()){
			%>
			<tr class=trclass><td colspan='6'>没有流程意见信息</td></tr>
			<%
			}else{
				WFICommentVO cvo;
				boolean tr = true;
				int k=0;
				for(int i=0;i<listYj.size();i++){
					cvo=(WFICommentVO)listYj.get(i);
				    if(tr){
						out.print("<tr class=trclass onmouseout='resumemenu()' onmouseover='invertmenu()'>");
					}else{
						out.print("<tr class=trclass2 onmouseout='resumemenu()' onmouseover='invertmenu()'>");
					}
				    k++;
			%>
				<td><%=i+1%></td>
				<td><%=cvo.getNodeName()%></td>
				<td><%=cvo.getCommentTime()%></td>
				<td><%=cvo.getUserName()%></td>
				<td><%=cvo.getCommentSignName()==null?"":cvo.getCommentSignName()%></td>
				<td><a href="#" onclick="showta('wf_<%=k%>')"><%=cvo.getCommentContent()==null?"-":cvo.getCommentContent().length()>30?cvo.getCommentContent().substring(0,30)+"......":cvo.getCommentContent()%></a></td>
				<input type="hidden" id="wf_<%=k%>" value='<%=cvo.getCommentContent()==null?"":cvo.getCommentContent()%>'>
				</tr>
			<%
					tr=!tr;
				}
			}
			%>
			</table><br>
		</fieldset>
		
		<br><br>
		<fieldset><legend><%=subType.equals("subFlow")?"子":"主" %>流程操作痕迹</legend><br>	
			<table class=tablemain cellspacing=1 cellpadding=0>
				<tr class=trtitle>
				<td width="5%">序号</td>
				<td width="10%">当前审批步骤</td>
				<td width="8%">当前办理人</td>
				<td width="15%">办理时间</td>
				<td width="10%">下一审批步骤</td>
				<td width="8%">下一办理人</td>
				<td>办理描述</td></tr>
				<%
				if(listGz==null||listGz.isEmpty()){
				%>
				<tr class=trclass><td colspan='7'>没有流程办理信息</td></tr>
				<%
				}else{
					Map obj;
					boolean tr = true;
					for(int i=0;i<listGz.size();i++){
						obj=(Map)listGz.get(i);
					    if(tr){
							out.print("<tr class=trclass onmouseout='resumemenu()' onmouseover='invertmenu()'>");
						}else{
							out.print("<tr class=trclass2 onmouseout='resumemenu()' onmouseover='invertmenu()'>");
						}
				%>
				<td><%=(i+1)%></td>
				<td><%=(String)obj.get("nodename")%></td>
				<td><%=(String)obj.get("username")%></td>
				<%-- <td><%=obj.get("orgname")==null?"-":(String)obj.get("orgname")%></td> --%>
				<td><%=(String)obj.get("nodestarttime")%></td>
				<td><%=obj.get("nextnodename")==null?"-":(String)obj.get("nextnodename")%></td>
				<td><%=obj.get("nextnodeuser")==null?"-":(String)obj.get("nextnodeuser")%></td>
				<td><%=(String)obj.get("methods")%></td>
				</tr>
				<%
						tr=!tr;
					}
				}
				%>
			</table>
			<br>
		</fieldset>	
			<br><br>
	<%
	}
	if(isShut!=null&&isShut.equals("0")){}
	else{
	%>
		
	 <div align="center">
	 	<input name="Submit43" type="button" class="button" value="关闭" onClick="javascript:window.close();">	
	 </div>
	<%} %>
</form>
</body></html>
