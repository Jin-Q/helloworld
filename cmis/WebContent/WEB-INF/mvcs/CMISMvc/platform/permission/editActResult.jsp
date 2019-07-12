<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<html>
<head>
<title>ECC IDE Jsp file</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<!-- ECC IDE required comment, please don't delete it! -->
<!-- toBeLayoutContent="true" mvcFile="designFiles/mvcs/CMISMvc/permissionDefine/resourceDefine.mvc" -->

</head>
<body>


<emp:text id="resourceid" label="resourceid" hidden="true"/>

<script>

		var resid=document.getElementById("resourceid").value;
		var url = "<emp:url action='resourceActList.do'/>&s_resourceaction.resourceid="+resid;
		window.opener.location = url;
		window.close();
</script>

</body>
</html>
