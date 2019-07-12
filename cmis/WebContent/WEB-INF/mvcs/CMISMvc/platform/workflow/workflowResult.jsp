<%@page import="com.yucheng.cmis.platform.workflow.WorkFlowConstance"%>
<%@page import="com.yucheng.cmis.platform.workflow.domain.WFIVO"%>
<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@ page import="com.ecc.emp.core.Context"%>
<%@ page import="java.util.*"%>
<%@ page import="com.ecc.emp.core.EMPConstance"%>

<HTML>
<HEAD>
<TITLE></TITLE>
<link id="skin" href="styles/info.css" rel="stylesheet" type="text/css" />
<link href="<emp:file fileName='styles/emp/relatedTab.css'/>" rel="stylesheet" type="text/css" />
</HEAD>
<BODY>
<%
String from = request.getParameter("from");
if("self".equals(from)) {
String message = request.getParameter("message");	
%>
<br>
   <table id='dialogMainTable' height="200" align='center'  class="emp_table" style="width:70%" >
    <tr height='16' class="emp_table_title">
      <td width='5%' onclick=''   align='center'>
        <img src="images/tree/question.gif"></img></td>    
      <td id='dialogTitle' height='16' width='95%' >
        &nbsp;<B>处理结束</B></td>
    </tr>
    <tr  height='6'><td colspan='2'></td></tr>
    <tr id='dialogMainTr' height='178' >
      <td id ='dialogMainTd' height='178'  colspan='2' align='center'>
       <center><%=message %></center>
      </td>
    </tr>
   </table>

<%
} else {
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	WFIVO wfiVO = (WFIVO)context.getDataValue(WorkFlowConstance.WFVO_RET_NAME);
	String message = null;
	if(wfiVO.getSign() == wfiVO.SIGN_SUCCESS && wfiVO.getNextNodeName()!=null) {
		message = wfiVO.getMessage() + "<br/>下一办理节点："+wfiVO.getNextNodeName()+"&nbsp;&nbsp;下一办理人："+wfiVO.getNextNodeUserName();
	}else if(context.containsKey("errorMsg")){
		message = (String)context.getDataValue("errorMsg");
	} else {
		message = wfiVO.getMessage();
	}
%>

	<form id="resultForm" action="showWorkflowResult.do?EMP_SID=${context.EMP_SID}" method="post">
	<input type="hidden" id="from" name="from" value="self">
	<input type="hidden" id="message" name="message" value="<%=message%>">
	</form>
	
	<script type="text/javascript">
	window.onload = function() {
		document.getElementById("resultForm").submit();
	}
	</script>

<%	
}
%>

</BODY>
</HTML>