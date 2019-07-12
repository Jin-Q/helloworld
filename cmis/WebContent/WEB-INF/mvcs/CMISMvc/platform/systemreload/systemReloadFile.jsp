<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<HTML>
<HEAD>
<TITLE>ECC IDE Jsp file</TITLE>

<jsp:include page="/include.jsp" />
</HEAD>
<BODY>
<script>
function doSubmitDic() {
    var form = document.getElementById("addDic");
	form.submit();

}
function doSubmitDate() {
    var form = document.getElementById("addDate");
	form.submit();

}
function doSubmitSeq() {
    var form = document.getElementById("addSeq");
	form.submit();

}
function doSubmitComponent() {
    var form = document.getElementById("addCompnt");
	form.submit();

}
function doSubmitTableModel() {
    var form = document.getElementById("addTableModel");
	form.submit();

}
function doSubmitOrg() {
    var form = document.getElementById("addOrg");
	form.submit();

}
function doSubmitAll() {
    var form = document.getElementById("addAll");
	form.submit();

}

function doSubmitWfrole(){
	 var form = document.getElementById("addWfrole");
		form.submit();
	
}
/**重载流程字典    2014-08-11  唐顺岩 */
function doSubmitFlowDic(){
	var form = document.getElementById("addFlowDic");
	form.submit();
}
/**重载流程字典  END */

function doSubmitTableRight() {
    var form = document.getElementById("addTableRight");
	form.submit();

}
function doSubmitRecord(){
	var form = document.getElementById("reloadRecord");
	form.submit();
}

function doSubmitSql(){
	var form = document.getElementById("reloadSql");
	form.submit();
}
</script>

<emp:form id="messgaebox" >
	<emp:label text="点击相应的按钮，可以重载对应的缓存内容" />
</emp:form>
<emp:form id="reloadRecord" action="beginReloadSystemInfo.do?type=record" method="POST">
	<table class="QZ_tableMsg"><tr><td><emp:button id="submitRecord" label="重载记录集权限" mousedownCss="button100" mouseoutCss="button100" mouseoverCss="button100" mouseupCss="button100"/></td>
	<td><font color="red">重载记录集权限</font></td></tr></table>
</emp:form>
<emp:form id="reloadSql" action="beginReloadSystemInfo.do?type=sql" method="POST">
	<table class="QZ_tableMsg"><tr><td><emp:button id="submitSql" label="重载命名SQL" mousedownCss="button100" mouseoutCss="button100" mouseoverCss="button100" mouseupCss="button100" />
	<td><font color="red">重新加载命名SQL</font></td></tr></table>
</emp:form>
<emp:form id="addDic" action="beginReloadSystemInfo.do?type=dic" method="POST">
    
    <table class="QZ_tableMsg"><tr><td>
	<emp:button id="submitDic" label="重载字典/树形字典" mousedownCss="button100" mouseoutCss="button100" mouseoverCss="button100" mouseupCss="button100"/>
	<td><font color="red">重新加载service中的字典/树形字典服务</font></td></tr></table>
</emp:form>


<emp:form id="addDate" action="beginReloadSystemInfo.do?type=date" method="POST">
	<table class="QZ_tableMsg"><tr><td><emp:button id="submitDate" label="重载系统日期" mousedownCss="button100" mouseoutCss="button100" mouseoverCss="button100" mouseupCss="button100"/>
	<td><font color="red">重新加载营业日期</font></td></tr></table>
</emp:form>
<!-- 
<emp:form id="addSeq" action="beginReloadSystemInfo.do?type=seq" method="POST">
	<emp:button id="submitSeq" label="重新加载序列" mousedownCss="button100" mouseoutCss="button100" mouseoverCss="button100" mouseupCss="button100"/>&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">重新加载序列</font>
</emp:form>
 -->
 
<emp:form id="addCompnt" action="beginReloadSystemInfo.do?type=compnt" method="POST">
	<table class="QZ_tableMsg"><tr><td><emp:button id="submitComponent" label="重新加载组件" mousedownCss="button100" mouseoutCss="button100" mouseoverCss="button100" mouseupCss="button100"/>
	<td><font color="red">重新加载组件</font></td></tr></table>
</emp:form>
<emp:form id="addTableModel" action="beginReloadSystemInfo.do?type=tabmode" method="POST">
	<table class="QZ_tableMsg"><tr><td><emp:button id="submitTableModel" label="重新加载表模型" mousedownCss="button100" mouseoutCss="button100" mouseoverCss="button100" mouseupCss="button100"/>
	<td><font color="red">重新加载表模型</font></td></tr></table>
</emp:form>
<emp:form id="addOrg" action="beginReloadSystemInfo.do?type=org" method="POST">
	<table class="QZ_tableMsg"><tr><td><emp:button id="submitOrg" label="重载机构用户岗位" mousedownCss="button100" mouseoutCss="button100" mouseoverCss="button100" mouseupCss="button100"/>
	<td><font color="red">重新加载用户、机构、岗位缓存</font></td></tr></table>
</emp:form>
<emp:form id="addFncConf" action="beginReloadSystemInfo.do?type=fnc" method="POST">
	<table class="QZ_tableMsg"><tr><td><emp:button id="submitAll" label="重载财务报表样式" mousedownCss="button100" mouseoutCss="button100" mouseoverCss="button100" mouseupCss="button100"/>
	<td><font color="red">重载财务报表配置样式</font></td></tr></table>
</emp:form>

<emp:form id="addWfrole" action="beginReloadSystemInfo.do?type=wfRole" method="POST">
	<table class="QZ_tableMsg"><tr><td><emp:button id="submitWfrole" label="重载流程相关角色" mousedownCss="button100" mouseoutCss="button100" mouseoverCss="button100" mouseupCss="button100"/>
	<td><font color="red">重载流程相关角色岗位机构配置</font></td></tr></table>
</emp:form>

<emp:form id="addFlowDic" action="beginReloadSystemInfo.do?type=flowDic" method="POST">
	<table class="QZ_tableMsg"><tr><td><emp:button id="submitFlowDic" label="重载流程字典" mousedownCss="button100" mouseoutCss="button100" mouseoverCss="button100" mouseupCss="button100"/>
	<td><font color="red">重载流程字典</font></td></tr></table>
</emp:form>

<emp:form id="addAll" action="beginReloadSystemInfo.do?type=all" method="POST">
	<table class="QZ_tableMsg"><tr><td><emp:button id="submitAll" label="重载以上所有" mousedownCss="button100" mouseoutCss="button100" mouseoverCss="button100" mouseupCss="button100"/>
	<td><font color="red">重新加载上面的所有</font></td></tr></table>
</emp:form>

</BODY></HTML>
</emp:page>
