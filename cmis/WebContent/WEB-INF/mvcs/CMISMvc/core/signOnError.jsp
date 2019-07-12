<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>信贷管理信息系统(CMIS)</title>
<link href="styles/login.css" rel="stylesheet" type="text/css" />
<style type="text/css">
#login {width:530px; height:130px; margin:250px 0px 0px 340px;}
</style>
</head>
<body>
<form name="FORM_SIGNON" method="POST">
<div id="main">
<div id="content">
<div id="login">
<div id="txtput">
<li><img src="images/ico_18.gif" />用户名：
  <label>
  <input type="text" name="currentUserId" value='' onkeydown="if(event.keyCode==13){event.keyCode=9;}" style="width:150px"/>
  </label>
</li>
<li><img src="images/ico_19.gif" />密　码：
  <label>
  <input type="password" name="password" value='' onkeydown="if(event.keyCode==13){ doSubmit();}" style="width:150px"/>
  </label></li>
  <li><img  src="images/default/stop_button.gif" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="#ff0000">账号或密码错误，登录失败！</font></li>
</div>
<div id="login_bt"><a href="javascript:doSubmit()"><img src="images/loging_bt.gif"/></a></div>
</div>
</div>
</div>
<script language='javascript'>
   function doSubmit(){
	   FORM_SIGNON.action="checkUserSignOn.do";
	   FORM_SIGNON.submit();
   }
</script>
</form>
</body>
</html>

