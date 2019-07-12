<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ page import="java.util.*" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%
Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
List list = (List)context.getDataValue("WorkFlowHisList");


String instanceId = (String)request.getParameter("instanceId");
String nodeId = (String)request.getParameter("nodeId");
String intransfer=request.getParameter("intransfer");

boolean pageDispart =  false;
int pageSize = 5;  //默认5条记录每页
if(intransfer!=null&&intransfer.equals("true"))//当内部调用时每页8条
	pageSize=8;
int begin, end, currentPage = 1;

int size =0;
if(list!=null)
size=list.size();
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
<title>流程跟踪</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/styles/workflow/default2.css" />
<script type="text/javascript" src="<%=request.getContextPath() %>/scripts/workflow/jquery-1.4.4.js"></script>
<style type="text/css">
.A2{color: blue; text-decoration: none;}
.A2:link {color: blue; text-decoration: none;}
.A2:visited {color: blue; text-decoration: none;}
.A2:hover {font-size:10pt; color: blue; text-decoration: none;}
</style>
<script type="text/javascript">
window.onload = function() {
		//调整iframe高度
		var parentObj=parent.document.getElementById("advice_wf_lcgz");
		if(typeof parentObj != "undefined" && parentObj != null)
		parentObj.style.height = document.body.scrollHeight;
}

function linkpage(tag) {
	var amount = parseInt("<%=amountPage%>");
	if(amount < 1)
		return;
	var curPage = parseInt("<%=currentPage%>");
	var pageTmp;
	var contextURL = "<%=request.getContextPath()%>";
	var url = contextURL+'/getWorkFlowHistory.do?&instanceId=<%=instanceId %>&nodeId=<%=nodeId%>&intransfer=<%=intransfer %>&EMP_SID=<%=request.getParameter("EMP_SID")%>';
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
	window.location=url;
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

//查看子流程/会办相关信息
function showSubTip(subType,subID){
	var contextPath="<%=request.getContextPath()%>";
	var url = contextPath+"/showSubTip.do?subType="+subType+"&subID="+subID+'&EMP_SID=<%=request.getParameter("EMP_SID")%>';
	window.showModalDialog(url,'selectPage','dialogHeight:500px;dialogWidth:900px;help:yes;resizable:yes;status:no;');
}

function exExcel(){
document.getElementById("exExcel_div").src="<%=request.getContextPath()%>/ccServlet?instanceid=<%=instanceId%>&actionType=exExcel";
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
<%-- <img alt="导出excel" src="<%=request.getContextPath()%>/images/workflow/xls.gif"  onclick="exExcel();" onmouseover="this.style.cursor='hand'"> --%>
<br>
<table class=tablemain cellspacing=0 cellpadding=0>
<tr class=trtitle>
<td>序号</td>
<td>处理步骤</td>
<td>办理人</td>
<!-- <td width="12%">办理机构</td> -->
<td>办理时间</td>
<td>下一处理步骤</td>
<td>下一办理人</td>
<td>备注</td></tr>
<%
if(list==null||list.isEmpty()){
%>
<tr class=trclass><td colspan='10'>没有流程办理信息</td></tr>
<% 
}else{
	Map obj;
	boolean tr = true;
	for(int i=begin;i<end;i++){
		obj=(Map)list.get(i);
	    if(tr){
			out.print("<tr class=trclass onmouseout='resumemenu()' onmouseover='invertmenu()'>");
		}else{
			out.print("<tr class=trclass2 onmouseout='resumemenu()' onmouseover='invertmenu()'>");
		}
%>
<td><%=(i+1)%></td>
<td><%=(String)obj.get("nodename")%></td>
<td><%=(String)obj.get("username")%></td>
<!-- <td><%=obj.get("orgname")==null?"-":(String)obj.get("orgname")%></td> -->
<td><%=(String)obj.get("nodestarttime")%></td>
<%	
	//如果是发起会办或是发起子流程，则可以链节点相关信息查看页面
	if(obj.get("nextnodename")!=null && (((String)obj.get("nextnodename")).startsWith("发起会办-") || ((String)obj.get("nextnodename")).startsWith("发起子流程-"))){
		String subType = ((String)obj.get("nextnodename")).startsWith("发起会办-")?"subGather":"subFlow";
		String subID = subType.equals("subGather")?((String)obj.get("nextnodename")).substring("发起会办-".length()):((String)obj.get("nextnodename")).substring("发起子流程-".length());
		String tip = subType.equals("subGather")?"会办操作痕迹及意见":"子流程操作痕迹及意见";
%>
		<td><img alt="点击查看<%=tip %>" style="cursor:hand" src="<%=request.getContextPath()%>/images/workflow/title.gif" onclick="showSubTip('<%=subType %>','<%=subID %>')">
				<%=obj.get("nextnodename")==null?"-":(String)obj.get("nextnodename")%>
		</td>
<%	} else{ %>
		<td><%=obj.get("nextnodename")==null?"-":(String)obj.get("nextnodename")%></td>
<%	} %>
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
<%
if (pageDispart) {
%>
	<div align="right">
	<font color=#999999><a href="javascript:linkpage('first')" class="A2">第一页</a></font>&nbsp;
	<font color=#999999><a href="javascript:linkpage('forward')" class="A2">上页</a></font>&nbsp;
	<font color=#999999><a href="javascript:linkpage('behind')" class="A2">下页</a></font>&nbsp;
	<font color=#999999><a href="javascript:linkpage('last')" class="A2">最后页</a></font>&nbsp;&nbsp;
	<input type="text" id="goPage" name="goPage" maxlength="5" size="3" onkeydown="doKeyDownMy();"/>
	<font color=#999999><a href="javascript:linkpage('go')" class="A2">跳转</a></font>&nbsp;&nbsp;
	<font color=#000>当前第[<%=currentPage%>]页&nbsp;共[<%=amountPage%>页]&nbsp;[<%=size%>条记录]</font>
	</div>
<%
}
%>
<!-- <iframe id="exExcel_div" name="exExcel_div" style="display: none"
			width="10" height="10"></iframe> -->
<div align="center">	

<!-- input name="Submit52" type="button"  value="图形跟踪" class="button" onClick="window.open('<%=request.getContextPath()%>/echain/studio/eChainMonitor.jsp?instanceid=<%=instanceId%>','name','left=0,top=0,width=1024,height=768,menubar=no,toolbar=no,location=no,directories=no,status=no,scrollbars=yes,resizable=yes');"-->		
</div>	  

</body>
</html>
