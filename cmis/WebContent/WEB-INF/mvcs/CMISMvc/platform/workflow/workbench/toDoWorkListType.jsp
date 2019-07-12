<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="java.util.*"%>
<%
Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
Map applTypeMap = (Map)context.getDataValue("applTypeMap");
%>
<html>
<head>
<title>待办事项流程申请类型</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<style type="text/css">
.emp_table_ext {
	width: 96%;
	border-collapse: collapse;
	border-spacing: 0;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
}

a {
	border-color: #b7b7b7;
	border-width: 0px;
	border-style: solid;
	padding: 4px 7px;
	text-decoration: none;
	color: #336699;
	font-size: 13;
	font-weight: bold;
}

</style>

<script type="text/javascript">

function doLink(applType) {
	var baseUrl = 'getToDoWorkListByType.do';
	var url = baseUrl+'?WfiWorklistTodo.appl_type='+applType+'&EMP_SID=<%=request.getParameter("EMP_SID")%>';
	document.getElementById("rightframeTable").src = url;
}
 </script>
 
</head>
<body>
	<div>
	<table class="emp_table_ext">
	<%
	Iterator iterator = applTypeMap.keySet().iterator();
	int i=1;
	while(iterator.hasNext()) {
		String applType = (String)iterator.next();
		String applTypeName = (String)applTypeMap.get(applType);
		if(i==1 || (i-1)%6 == 0) {
			out.println("<tr><td height='25'>");
		} else {
			out.println("<td height='25'>");
		}
	%>
	<a href="javascript:doLink('<%=applType%>')"><%=applTypeName %></a>
	<%
		if(i%6 == 0) {
			out.println("</td></tr>");
		} else {
			out.println("</td>");	
		}
		i++;
	}
	%>
	</table>
	</div>
	<iframe id="rightframeTable" 
		name="rightframeTable" 
		src='getToDoWorkList.do?EMP_SID=<%=request.getParameter("EMP_SID")%>' frameborder="0" scrolling="auto" height="100%" width="100%" >
	</iframe>

</body>

</html>
