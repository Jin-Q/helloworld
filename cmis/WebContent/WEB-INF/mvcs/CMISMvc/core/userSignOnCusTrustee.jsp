<%@page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="java.util.Map"%>
<%@page import="com.ecc.emp.data.KeyedCollection"%>
<%@page import="com.ecc.emp.data.IndexedCollection"%>
<%@page import="java.util.List"%><html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>信贷管理信息系统(CMIS)</title>
<link href="styles/login.css" rel="stylesheet" type="text/css" />
</head>
<%
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	KeyedCollection kColl = (KeyedCollection)context.getParent().getDataElement();
	
	IndexedCollection consignor = (IndexedCollection)kColl.getDataValue("consignor");
	String loginUserName = (String)kColl.getDataValue("loginusername");
	String loginuserid = (String)kColl.getDataValue("loginuserid");
	String loginorgid = (String)kColl.getDataValue("loginorgid");
	String loginorgname = (String)kColl.getDataValue("loginorgname");
	String loginRoleNameList = (String)kColl.getDataValue("loginRoleNameList");
%>
<style type="text/css">

</style>

<body id="QZ_mainbody">
<div id="main" class="QZ_main">

<div id="login">
    <div id="info" class="info">请选择用户登录</div>
    <table class="QZ_loginTable">
    <tr>
    <td>
	<button class=btn1_mouseout onmouseover="this.className='btn1_mouseover'" 
	onmouseout="this.className='btn1_mouseout'" onclick="signOnSelf('<%=loginuserid%>')" style="width: 200px;height: 35px;"><%=loginUserName%></button>
	<button class=btn1_mouseout onmouseover="this.className='btn1_mouseover'" onmouseout="this.className='btn1_mouseout'" style="width: 200px;height: 35px;" DISABLED>本用户</button>
     </td>
     </tr>
    <% 
    if(consignor !=null || consignor.size()>0){
    	for(int i =0;i<consignor.size();i++){
    		KeyedCollection consignorValue = (KeyedCollection)consignor.get(i);
			String actorno = consignorValue.getDataValue("consignor_id").toString();
			String actorname = consignorValue.getDataValue("consignor_name").toString();
		%>
			<tr><td>
		    <button class=btn1_mouseout_other onmouseover="this.className='btn1_mouseover_other'" 
	onmouseout="this.className='btn1_mouseout_other'" onclick="signOnOther('<%=actorno%>')" style="width: 200px;height:35px;"><%=actorname%></button>
	<button class=btn1_mouseout_other onmouseover="this.className='btn1_mouseover_other'" onmouseout="this.className='btn1_mouseout_other'" style="width: 200px;height:35px;" DISABLED >托管给我的用户</button>
			</td></tr>
		<% 
		}
    }	
    %>
    </table>
        <emp:form action="#" id="submitForm">
</emp:form>
</div>
</div>
</div>
<script language='javascript'>

   function signOnOther(actorno) {
	    var loginuserid = '${context.loginuserid}';
	    var loginusername = '${context.loginusername}';
	    var loginorgid = '${context.loginorgid}';
	    var loginorgname = '${context.loginorgname}';
	    var loginRoleNameList = '${context.loginRoleNameList}';
	    var submitForm=document.getElementById("submitForm");
	    var emp_sid = "";
	    if(document.getElementById("EMP_SID")){
	    	emp_sid=document.getElementById("EMP_SID").value;
	    }else{
	    	emp_sid=document.getElementsByName("EMP_SID")[0].value;
	    }
	    submitForm.action="userSignOn.do?EMP_SID="+emp_sid+"&currentUserId="+actorno+"&loginuserid="+loginuserid;
	    submitForm.submit();
	}

	function signOnSelf(loginuserid) { 
		var loginuserid = '${context.loginuserid}';
	    var loginusername = '${context.loginusername}';
	    var loginorgid = '${context.loginorgid}';
	    var loginorgname = '${context.loginorgname}';
	    var loginRoleNameList = '${context.loginRoleNameList}';
	    var submitForm=document.getElementById("submitForm");
	    var emp_sid = "";
	    if(document.getElementById("EMP_SID")){
	    	emp_sid=document.getElementById("EMP_SID").value;
	    }else{
	    	emp_sid=document.getElementsByName("EMP_SID")[0].value;
	    }
	    submitForm.action="userSignOn.do?EMP_SID="+emp_sid+"&currentUserId="+loginuserid+"&loginuserid="+loginuserid;
	    submitForm.submit();
	}
</script>
</body>
</html>
