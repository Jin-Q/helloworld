<%@ page language="java" contentType="text/html; charset=gb2312"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<style type='text/css'>
.title{FONT-WEIGHT:bold;FONT-SIZE:22px;COLOR: #000066;LINE-HEIGHT:200%;FONT-FAMILY: '����';text-align: center;}
.button
{
    font-size: 9pt;
    color: #000000;
    padding: 2px 0px 0px 0px;
	background-color:#F4F9FF;    
    height: 19px;
	BORDER: #B7BAC1 1pt solid;
}
</style>
<title>Shuffle�����������ƽ̨</title>
</head>
<body>
<form action="<%=request.getContextPath()%>/ShuffleServlet">
<input type="hidden" name="method" value="shufflemanage">
<input type="hidden" name="actionType" value="login">
<center>
<font class=title>Shuffle�����������</font>
<hr>
<br>
�û�����<input type="text" name="userid" value="admin"><br>
&nbsp;&nbsp;���룺<input type="password" name="password" value=""><br><br>
<input type="submit" class="button" value="&nbsp;��&nbsp;¼&nbsp;"><br><br>
<%
String errormsg=(String)request.getAttribute("errormsg");
if(errormsg!=null&&!errormsg.equals("")){
	out.print("<font color=red><b>"+errormsg+"</b></font>");
}
%>
</center>
</form>
</body>
</html>
