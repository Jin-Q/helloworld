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
<script>
function doSubmitAll() {
    var form = document.getElementById("addAll");
	form.submit();

}
function doSubmitOrg() {
    var form = document.getElementById("addOrg");
    //var orgno = orgno._getValue();
    orgno._toForm(form);
	form.submit();

}
function doSubmitOne() {
    var form = document.getElementById("addOne");
    var actorno = actorno._getValue();
    actorno._toForm(form);
	form.submit();

}
</script>
<emp:form id="addAll" action="userPermissionFileAll.do" method="POST">
<!-- <emp:button id="submitAll" label="生成所有权限文件"/>   -->
<input type="button" class="button100" onclick="doSubmitAll()" value="生成所有权限文件">
</emp:form>


<emp:form id="addOrg" action="userPermissionFileOrg.do" method="POST">
<table cellpadding="0" cellspacing="0" class="QZ_tableMsg">
<tr><td>
<emp:label text="机构码"/><emp:text id="orgno" label="机构码" /></td>
<!--  <emp:button id="submitOrg" label="按机构生成权限文件"/>   -->
<td>
<input type="button" class="button120" onclick="doSubmitOrg()" value="按机构生成权限文件" required="ture">
</td>
</tr>
</table>
</emp:form>


<emp:form id="submitForm" action="userPermissionFile.do" method="POST">
<table  cellpadding="0" cellspacing="0" class="QZ_tableMsg"><tr><td>
<emp:label text="用户名"/><emp:text id="actorno" label="actorno" required="ture"/>
</td>
<td>
<!--  <emp:button id="submit" label="生成权限文件"/>    -->
<input type="button" class="button80" onclick="doSubmit()" value="生成权限文件">
</td></tr>
</emp:form>
</table>



</BODY></HTML>
</emp:page>