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
		var url = '<emp:url action="queryIqpAssetOrgList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpAssetOrgGroup" title="参与机构信息" maxColumn="2">
			<emp:text id="IqpAssetOrg.asset_org_id" label="机构代码" maxlength="40" required="true" readonly="true"/>
			<emp:text id="IqpAssetOrg.asset_org_name" label="机构名称" maxlength="80" required="true" />
			<emp:select id="IqpAssetOrg.org_nature" label="机构性质" required="true" dictname="STD_ORG_QLTY"/>
			<emp:text id="IqpAssetOrg.org_address" label="注册地址" maxlength="200" required="true" />
			<emp:text id="IqpAssetOrg.org_repr" label="法人代表" maxlength="40" required="true" />
			<emp:text id="IqpAssetOrg.pcode" label="邮政编码" maxlength="20" required="true" />
			<emp:text id="IqpAssetOrg.phone" label="联系电话" maxlength="20" required="true" dataType="Phone"/>
			<emp:text id="IqpAssetOrg.fax" label="传真" maxlength="20" required="true" dataType="Phone"/>
			<emp:text id="IqpAssetOrg.acct_no" label="账号" maxlength="40" required="true" />
			<emp:text id="IqpAssetOrg.acct_name" label="账户名" maxlength="80" required="true" />
			<emp:text id="IqpAssetOrg.acctsvcr_no" label="开户行行号" maxlength="20" required="true" />
			<emp:text id="IqpAssetOrg.acctsvcr_name" label="开户行行名" maxlength="100" required="true" />
		</emp:gridLayout>
		<emp:gridLayout id="1" title="登记信息" maxColumn="2">
			<emp:text id="IqpAssetOrg.input_id_displayname" label="登记人"  required="false" defvalue="${context.currentUserName}" readonly="true"/>
			<emp:text id="IqpAssetOrg.input_br_id_displayname" label="登记机构"  required="false" defvalue="${context.organName}" readonly="true"/>
			<emp:text id="IqpAssetOrg.input_date" label="登记日期" maxlength="10" required="false" defvalue="${context.OPENDAY}"  readonly="true"/>
			<emp:text id="IqpAssetOrg.input_id" label="登记人" maxlength="40" required="false" defvalue="${context.currentUserId}" hidden="true" readonly="true"/>
			<emp:text id="IqpAssetOrg.input_br_id" label="登记机构" maxlength="20" required="false" defvalue="${context.organNo}" hidden="true" readonly="true"/>
		</emp:gridLayout>
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
