<%@page import="com.yucheng.cmis.platform.workflow.domain.WFICommentVO"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ page import="java.util.*" %>
<%
boolean pageDispart =  false;
int pageSize = 5;  //默认8条记录每页
int begin, end, currentPage = 1;
String instanceid = (String)request.getParameter("instanceId");
String nodeid = (String)request.getParameter("nodeId");
Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
List<WFICommentVO> list = (List<WFICommentVO>)context.getDataValue("WFICommentList");
int size = list.size();
int amountPage = (size+pageSize-1)/pageSize;
if(size > pageSize) {
	pageDispart = true;
	try {
		currentPage = Integer.valueOf((String)request.getParameter("currentPage"));
	} catch(Exception e) {
	}
	begin = (currentPage-1)*pageSize;
	end = currentPage*pageSize<=size? currentPage*pageSize:size;

} else {
	begin = 0;
	end = list.size();
}
%>
<html>
<head>
<title>流程意见信息</title>
<link href="<%=request.getContextPath()%>/styles/workflow/default2.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">
window.onload = function() {
	//调整iframe高度
	parent.document.getElementById("advice_wf").style.height = document.body.scrollHeight;
}

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

function linkpage(tag) {
	var amount = parseInt("<%=amountPage%>");
	if(amount < 1)
		return;
	var curPage = parseInt("<%=currentPage%>");
	var pageTmp;
	var contextURL = "<%=request.getContextPath()%>";
	var url = contextURL+'/getWFComment.do?instanceId=<%=instanceid %>&nodeId=<%=nodeid%>&EMP_SID=<%=request.getParameter("EMP_SID")%>';
	if(tag == "first") {
		if(curPage == 1)
			return;
		url = url + "&currentPage=1";
	} else if(tag == "forward") {
		if(curPage == 1)
			return;
		else
			pageTmp = curPage-1;
		url =  url + "&currentPage="+pageTmp;
	} else if(tag == "behind") {
		if(curPage == amount)
			return;
		else
			pageTmp = parseInt(curPage) + 1;
		url =  url + "&currentPage="+pageTmp;	
	} else if(tag == "last") {
		if(curPage == amount)
			return;
		url =  url + "&currentPage="+amount;
	} else if(tag == "go") {
		var goPage = document.getElementById("goPage").value;
		if(!isNaN(goPage)) {
			if(goPage < 1) {
				goPage = 1;
			} else if(goPage > amount) {
				goPage = amount;
			} else if(goPage == curPage) {
				document.getElementById("goPage").value = "";
				return;
			}
			url =  url + "&currentPage="+goPage;
		} else if(goPage == "") {
			return;
		} else{
			alert("输入的页码非法，请重新输入！");
			document.getElementById("goPage").value = "";
			document.getElementById("goPage").focus();
			return;
		}
	}
	window.location = url;
}
function showta(id){
parent.showta(document.getElementById(id).value);
}
function doKeyDownMy(){
if(event.keyCode==13){
linkpage('go');
return false;
}
}
</script>
</head>
<body>

<table id=table_advice class="tablemain" cellspacing="0" cellpadding="0">
<tr class=trtitle>
<td>序号</td>
<td>处理节点</td>
<td>意见时间</td>
<td>处理人</td>
<td>意见标识</td>
<td>流程意见</td>
</tr>
<%
if(list==null||list.isEmpty()){
%>
<tr class=trclass><td colspan='6'>没有流程意见信息</td></tr>
<%
}else{
	WFICommentVO cvo;
	boolean tr = true;
	int k=0;
	int a=0;
	for(int i=begin;i<end;i++){
		cvo=(WFICommentVO)list.get(i);
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
<%-- <td><%=cvo.getCommentSign()==null?"":cvo.getCommentSign()%></td> --%>
<td><%=cvo.getCommentSignName()==null?"&nbsp;":cvo.getCommentSignName()%></td>
<td><a href="#" onclick="showta('wf_<%=k%>')"><%=cvo.getCommentContent()==null?"-":cvo.getCommentContent().length()>30?cvo.getCommentContent().substring(0,30)+"......":cvo.getCommentContent()%></a></td>
<input type="hidden" id="wf_<%=k%>" value='<%=cvo.getCommentContent()==null?"":cvo.getCommentContent()%>'>
</tr>
<%
		tr=!tr;
	}
}
%>
</table>
<%if(pageDispart) { %>
	<div class="QZ_pages">
	<font><a href="javascript:linkpage('first')" class="A2_first">首页</a></font>&nbsp;
	<font><a href="javascript:linkpage('forward')" class="A2_pre">上一页</a></font>&nbsp;
	<font><a href="javascript:linkpage('behind')" class="A2_next">下一页</a></font>&nbsp;
	<font><a href="javascript:linkpage('last')" class="A2_last">尾页</a></font>&nbsp;&nbsp;
	<input type="text" id="goPage" name="goPage" maxlength="5" size="3" onkeydown="doKeyDownMy();"/>
	<font><a href="javascript:linkpage('go')" class="A2_change">跳转</a></font>&nbsp;&nbsp;
	<font>当前第[<%=currentPage %>]页&nbsp;共[<%=amountPage %>页]&nbsp;[<%=size %>条记录]</font>
	</div>
<%} %>

</body></html>
