<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<HTML>
<HEAD>
<TITLE>ECC IDE Jsp file</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<!-- ECC IDE required comment, please don't delete it! -->
<!-- toBeLayoutContent="true" mvcFile="designFiles/mvcs/CMISMvc/permissionDefine/userPermissionFile.mvc" -->
<jsp:include page="/include.jsp" />
</HEAD>
<BODY>
<script type="text/javascript">
function doReturnMethod(){
	var url = '<emp:url action="reloadCashe.do"/>';
	url = EMPTools.encodeURI(url);
	window.location=url;
}
</script>
<emp:form id="submitForm">
	<emp:label text="提示：重载成功！" />
</emp:form>
<div align="center">
<emp:button id="returnMethod" label="返回"/>
</div>
<script type="text/javascript">
try{window.parent.unmask();}catch(e){}
</script>
</body>
</html>
</emp:page>