<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	function doReturn() {
		var url = '<emp:url action="queryCusFixAuthorizeList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
</script>
</head>
<body class="page_content">
	<emp:gridLayout id="CusFixAuthorizeGroup" title="客户修改授权" maxColumn="2">
			<emp:pop id="CusFixAuthorize.auth_id_displayname" label="授权人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="CusFixAuthorize.manager_br_id_displayname" label="管理机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID"/>
			<emp:password id="CusFixAuthorize.checkcode" label="授权码" maxlength="32" required="true" />
			<emp:password id="CusFixAuthorize.checkcodechk" label="确认授权码" maxlength="32" required="true" defvalue="$CusFixAuthorize.checkcode"/>
			<emp:date id="CusFixAuthorize.startdate" label="开始日期" required="true" />
			<emp:date id="CusFixAuthorize.enddate" label="结束日期" required="true" />
			<emp:text id="CusFixAuthorize.input_id_displayname" label="登记人"  required="true" />
			<emp:text id="CusFixAuthorize.input_br_id_displayname" label="登记机构"  required="true" />
			<emp:text id="CusFixAuthorize.input_id" label="登记人" maxlength="20" required="true" hidden="true"/>
			<emp:text id="CusFixAuthorize.input_br_id" label="登记机构" maxlength="20" required="true" hidden="true"/>
			<emp:text id="CusFixAuthorize.serno" label="流水号" maxlength="32" readonly="true" required="false" hidden="true" />
	</emp:gridLayout>
	<div align="center">
		<br>
		<input type="button" class="button100" onclick="doReturn(this)" value="返回到列表页面">
	</div>
</body>
</html>
</emp:page>