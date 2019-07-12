<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<html>
<head>
<title>ECC IDE Jsp file</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<!-- ECC IDE required comment, please don't delete it! -->
<!-- toBeLayoutContent="true" mvcFile="designFiles/mvcs/CMISMvc/permissionDefine/roleActDefine.mvc" -->
</head>
<body>
<script type="text/javascript">
	window.opener.location="<emp:url action="rolePermissionDefine__s_role.do"/>";
	window.close();
</script>

</body>
</html>
