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
		var url = '<emp:url action="queryIqpAssetOrgRegiList.do"/>?serno='+IqpAssetOrgRegi.serno._getValue();
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpAssetOrgRegiGroup" maxColumn="2" title="机构登记">
			<emp:text id="IqpAssetOrgRegi.serno" label="业务编号" maxlength="40" required="true" readonly="true" defvalue="${context.serno}"/>
			<emp:pop id="IqpAssetOrgRegi.asset_org_id" label="机构代码" required="true" buttonLabel="选择" url="queryIqpAssetOrgPop.do?returnMethod=returnOrg&restrictUsed=false"/>
			<emp:text id="IqpAssetOrgRegi.asset_org_id_displayname" label="机构名称"  required="false" readonly="true"/>
			<emp:select id="IqpAssetOrgRegi.asset_org_type" label="机构类型" required="true" dictname="STD_ZB_ASSET_ORG_TYPE"/>
			<emp:text id="IqpAssetOrgRegi.acct_no" label="账号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="IqpAssetOrgRegi.acct_name" label="账户名" maxlength="80" required="false" readonly="true"/>
			<emp:text id="IqpAssetOrgRegi.acctsvcr_no" label="开户行行号" maxlength="20" required="false" readonly="true"/>
			<emp:text id="IqpAssetOrgRegi.acctsvcr_name" label="开户行行名" maxlength="100" required="false" readonly="true"/>
		</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
