<%@ page contentType="text/html;charset=gb2312" language="java"%>
<%
String userid=(String)request.getSession().getAttribute("userid");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>无标题文档</title>

</head>
<body topmargin=0px leftmargin=0px>
<table bgcolor=#000000 cellspacing=1 cellpadding=3 width="100%">
<tr><td bgcolor=#e3e4e3 style="font-size:12px">
&nbsp;当前登录用户：<%=userid%>
</td></tr></table>
<center><br>
<a href="<%=request.getContextPath()%>/shufflejnlpservlet">规则定制</a><br><br>
<a href="./constantList.jsp" target="mainFrame">全局常量</a><br><br>
<a href="./varList.jsp" target="mainFrame">全局变量</a><br><br>
<a href="./rules.jsp" target="mainFrame">规则列表</a><br><br>
<a href="./testRule.jsp" target="mainFrame">规则仿真</a><br><br>
<a href="./userManagerPage.jsp" target="mainFrame">登陆用户管理</a><br><br>
<a href="<%=request.getContextPath()%>/ShuffleServlet?method=shufflemanage&actionType=logoff" target="_top">[&nbsp;退&nbsp;出&nbsp;]</a><br><br>
</center>
</body>
</html>
