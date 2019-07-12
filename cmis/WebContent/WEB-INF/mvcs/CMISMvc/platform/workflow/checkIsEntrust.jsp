<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ page import="java.lang.*"%>
<%@ page import="java.util.*"%>
<%
Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
String isEntrust = (String)context.getDataValue("isEntrust");
%>

<HTML>
<HEAD>
<TITLE>校验所选用户是否有代办</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/styles/workflow/default.css" />

<script type="text/javascript">

	function doSubmit(){
		retObj = [];
		var entrustModel = "";
		var entrust = document.getElementsByName("entrustModel");
		for(var i=0; i<entrust.length; i++){
			if(entrust[i].checked)
				entrustModel = entrust[i].value;
		}

		if(entrustModel==""){
			alert("请选择代办模式");
			return;
		}

		retObj[0] = "<%=isEntrust%>" ;
		retObj[1] = entrustModel;
		window.returnValue = retObj;
		window.close();
	}

	
	function doReturn(){
		retObj = [];
		var isEntrust = "<%=isEntrust%>" ;
		if(isEntrust=="false"){
			retObj[0] = isEntrust;
			window.returnValue = retObj;
			window.close();
		}
	}
</script>
</HEAD>
<BODY onload="doReturn()">
<div class="selectNextNodeStyle">
<table border="0" width="550px" cellspacing="1" cellpadding="0" bgcolor="000000">
	<tr>
		<td class="tdtitle">由于当前选择的审批人设置了工作委托，请指定代办模式</td>
	</tr>
	<tr>
		<td class="td">
		<input type="radio" name="entrustModel" value="0" checked="checked">代办人办理<br>
		<input type="radio" name="entrustModel" value="1">原办理人代人都可以办理<br>
		<input type="radio" name="entrustModel" value="2">原办理人办理<br>
		</td>
	</tr>
</table>
<br></div>
<div align="center">
	<input type="button" class="button" value="确    定" onclick="doSubmit()">
	<input type="button" class="button" value="取    消" onclick="window.close()">
</div>
</BODY>
</HTML>
