<%@ page language="java" contentType="text/html; charset=gb2312"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<style type='text/css'>
.title{FONT-WEIGHT:bold;FONT-SIZE:22px;COLOR: #000066;LINE-HEIGHT:200%;FONT-FAMILY: '宋体';text-align: center;}
</style>
<title>Shuffle规则引擎管理平台</title>
</head>
<body>
<form action="<%=request.getContextPath()%>/ShuffleServlet">
<input type="hidden" name="method" value="shufflemanage">
<br><br><br>
<center>
<img src="<%=request.getContextPath()%>/shuffle/shufflemanage/icon_success.gif"><br><br>
操作成功!
</center>
</form>
</body>
</html>
