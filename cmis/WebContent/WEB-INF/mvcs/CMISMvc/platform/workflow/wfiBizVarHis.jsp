<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.yucheng.cmis.platform.workflow.domain.WfiBizVarRecordVO"%>
<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ page import="java.util.*" %>
<%
boolean pageDispart =  false;
int pageSize = 5;  //默认8条记录每页
int begin, end, currentPage = 1;
String instanceid = (String)request.getParameter("instanceId");
String nodeid = (String)request.getParameter("nodeId");
Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
List<WfiBizVarRecordVO> list = (List<WfiBizVarRecordVO>)context.getDataValue("wfiBizVarList");
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
<title>流程业务审批变更意见信息</title>
<link href="<%=request.getContextPath()%>/styles/workflow/default2.css" rel="stylesheet" type="text/css" />

<style type="text/css">
.A2{color: blue; text-decoration: none;}
.A2:link {color: blue; text-decoration: none;}
.A2:visited {color: blue; text-decoration: none;}
.A2:hover {font-size:10pt; color: blue; text-decoration: none;}
</style>

<script type="text/javascript">
window.onload = function() {
	//调整iframe高度
	parent.document.getElementById("wfibizvar_wf").style.height = document.body.scrollHeight;
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
	var url = contextURL+'/getWfiBizVarHis.do?instanceId=<%=instanceid %>&EMP_SID=<%=request.getParameter("EMP_SID")%>';
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

function showtDetail(instanceId,nodeId,userId,opTime) {
	var url = 'getWfiBizVarDetail.do?instanceId='+instanceId+'&nodeId='+nodeId+'&userId='+userId+'&opTime='+opTime+'&EMP_SID=<%=request.getParameter("EMP_SID")%>';
	window.open(url,'varDetailPage','height:450px;width:700px;location:no');
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
<td>审批变更详细</td>
</tr>
<%
if(list==null||list.isEmpty()){
%>
<tr class=trclass><td colspan='6'>没有审批变更信息</td></tr>
<%
}else{
	WfiBizVarRecordVO cvo;
	boolean tr = true;
	int k=0;
	int a=0;
	for(int i=begin;i<end;i++){
		cvo=(WfiBizVarRecordVO)list.get(i);
	    if(tr){
			out.print("<tr class=trclass onmouseout='resumemenu()' onmouseover='invertmenu()'>");
		}else{
			out.print("<tr class=trclass2 onmouseout='resumemenu()' onmouseover='invertmenu()'>");
		}
	    k++;
%>
<td><%=i+1%></td>
<td><%=cvo.getNodename()%></td>
<td><%=cvo.getOpTime()%></td>
<td><%=cvo.getInputName()%></td>
<td><a href="#" onclick="showtDetail('<%=cvo.getInstanceid() %>','<%=cvo.getNodeid()%>','<%=cvo.getInputId()%>','<%=cvo.getOpTime()%>')">点击查看</a></td>
</tr>
<%
		tr=!tr;
	}
}
%>
</table>
<%if(pageDispart) { %>
	<div align="right">
	<font color=#999999><a href="javascript:linkpage('first')" class="A2">第一页</a></font>&nbsp;
	<font color=#999999><a href="javascript:linkpage('forward')" class="A2">上页</a></font>&nbsp;
	<font color=#999999><a href="javascript:linkpage('behind')" class="A2">下页</a></font>&nbsp;
	<font color=#999999><a href="javascript:linkpage('last')" class="A2">最后页</a></font>&nbsp;&nbsp;
	<input type="text" id="goPage" name="goPage" maxlength="5" size="3" onkeydown="doKeyDownMy();"/>
	<font color=#999999><a href="javascript:linkpage('go')" class="A2">跳转</a></font>&nbsp;&nbsp;
	<font color=#000>当前第[<%=currentPage %>]页&nbsp;共[<%=amountPage %>页]&nbsp;[<%=size %>条记录]</font>
	</div>
<%} %>

</body></html>