<%@ page contentType="text/html;charset=gb2312" language="java"%>
<%
String userid=(String)request.getSession().getAttribute("userid");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>�ޱ����ĵ�</title>

</head>
<body topmargin=0px leftmargin=0px>
<table bgcolor=#000000 cellspacing=1 cellpadding=3 width="100%">
<tr><td bgcolor=#e3e4e3 style="font-size:12px">
&nbsp;��ǰ��¼�û���<%=userid%>
</td></tr></table>
<center><br>
<a href="<%=request.getContextPath()%>/shufflejnlpservlet">������</a><br><br>
<a href="./constantList.jsp" target="mainFrame">ȫ�ֳ���</a><br><br>
<a href="./varList.jsp" target="mainFrame">ȫ�ֱ���</a><br><br>
<a href="./rules.jsp" target="mainFrame">�����б�</a><br><br>
<a href="./testRule.jsp" target="mainFrame">�������</a><br><br>
<a href="./userManagerPage.jsp" target="mainFrame">��½�û�����</a><br><br>
<a href="<%=request.getContextPath()%>/ShuffleServlet?method=shufflemanage&actionType=logoff" target="_top">[&nbsp;��&nbsp;��&nbsp;]</a><br><br>
</center>
</body>
</html>
