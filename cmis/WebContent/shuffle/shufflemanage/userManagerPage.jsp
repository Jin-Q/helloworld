<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.ecc.shuffleserver.manager.AccessManager"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.ecc.shufflestudio.permission.UserInfo"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ShuffleUserManager</title>
<style type='text/css'>
A{font-size:9pt}
BODY{font-size:9pt}
.tablemain {background-color: #000000;width:100%;}
.trtitle{FONT-WEIGHT: bold;FONT-SIZE: 12px;COLOR: #000066;LINE-HEIGHT: 150%;FONT-FAMILY: '宋体';BACKGROUND-COLOR: #e3e4e3;text-align: center;}
.trtitle2{BACKGROUND-COLOR: #e3e3e3;text-align: center;}
.trclass {background-color: #ffffff;FONT-WEIGHT: 500;FONT-SIZE: 12px;COLOR: #000066;FONT-FAMILY: 宋体;;height:20px}
td{padding-top:3px;padding-left:5px;padding-right:2px;padding-bottom:2px;}
.button
{
    font-size: 9pt;
    color: #000000;
    padding: 2px 0px 0px 0px;
	background-color:#F4F9FF;    
    height: 19px;
	BORDER: #B7BAC1 1pt solid;
	cursor:hand;
}
</style>
</head>
<%
	AccessManager accessManager = AccessManager.getInstance();
	String actionType = request.getParameter("actionType");
	String isChecked = request.getParameter("isChecked");
	String refreshTime = (String)request.getParameter("refreshTime");
	
	if(isChecked==null){
		isChecked = "unchecked";
	}
	if(refreshTime==null){
		refreshTime="10";
	}
	
	
	if(actionType==null){
		
	}else if(actionType.equalsIgnoreCase("del")){
		String userId = request.getParameter("userId");	
		String sessionId = request.getParameter("sessionId");
		accessManager.unregisterUser(userId,sessionId);
	}
	List userList = accessManager.getAllUserInfo();
	
%>
<body>
<table class=tablemain cellspacing=1 cellpadding=0>
	<tr class=trtitle><td colspan="4" align="center">用户登录信息表</td></tr>
	<tr class=trtitle2>
		<td>用户名</td>
		<td>SessionId</td>
		<td>上次活动时间</td>
		<td>操作</td>
	</tr>
<%
	if(userList!=null&&userList.size()!=0){
		Iterator userIt = userList.iterator();
		while(userIt.hasNext()){
			UserInfo userInfo = (UserInfo)userIt.next();
			Long timerLong = Long.valueOf(userInfo.getTimestamp());
			Date logDate = new Date(timerLong);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String longTime = dateFormat.format(logDate);
%>
	<tr class=trclass>
		<td><%=userInfo.getUserName() %></td>
		<td><%=userInfo.getSessionId() %></td>
		<td><%=longTime %></td>
		<td><input type="button"  class=button value="签退" onClick="unRegisterUser('<%=userInfo.getUserName() %>','<%=userInfo.getSessionId() %>')"/></td>
	</tr>
<%
			
		}
	}else{
%>
	<tr class=trclass><td colspan="4" align="center">系统暂时无人登录！！！</td></tr>
<% 
	}
%>
</table>
<div align="center">
	是否定时刷新：<input id="isRefresh" type="checkbox" <%=isChecked %> onclick="onChangeRefresh()"/>
	间隔时间：<input id="refreshTime" type="text" value="<%=refreshTime %>"/>秒
	<input type="button" class=button value="设置" onClick="refreshUserPage()"/> 
</div>
</body>
<script type="text/javascript">

function refreshUserPage(){
	var url = getSelfUrl();
	window.location.href = url;
}
function onChangeRefresh(){
	var isCheckedObj = document.getElementById('isRefresh');
	var refreshTime = document.getElementById('refreshTime');
	if(isCheckedObj.checked){
		refreshTime.disabled = false;
	}else{
		refreshTime.disabled = true;
	}
}
function getSelfUrl(){
	var checkState;
	var isCheckedObj = document.getElementById('isRefresh');
	var refreshTime = document.getElementById('refreshTime').value;
	if(isCheckedObj.checked)
		checkState = 'checked';
	else
		checkState = 'unchecked';
	var url= './userManagerPage.jsp?isChecked='+checkState+'&refreshTime='+refreshTime;
	return url;
}

function unRegisterUser(userId,sessionId){
	var url= getSelfUrl()+'&actionType=del&userId='+userId+'&sessionId='+sessionId;
	window.location.href = url;
}
function initUserPage(){
	var isCheckedObj = document.getElementById('isRefresh');
	var refreshTime = document.getElementById('refreshTime');
	if(isCheckedObj.checked){	
	try{	
		refreshTime.disabled = false;
		setTimeout(refreshUserPage,refreshTime.value*1000);
	}catch(e){
		alert(e);
	}
	}else{
		refreshTime.disabled = true;
	}
}
initUserPage();
</script>
</html>