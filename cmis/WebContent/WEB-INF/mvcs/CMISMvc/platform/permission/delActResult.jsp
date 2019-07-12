<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<HTML>
<HEAD>
<TITLE>ECC IDE Jsp file</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<!-- ECC IDE required comment, please don't delete it! -->
<!-- toBeLayoutContent="true" mvcFile="${mvcfile}" -->

</HEAD>
<BODY>

<emp:input dataName="resourceid" type="hidden"/>

<script>

		var resid=document.getElementById("resourceid").value;
		var url = "<emp:url action='resourceActList.do'/>&s_resourceaction.resourceid="+resid;
		window.location = url;
		
</script>
</BODY></HTML>