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
		var serno = IqpBailInfo.serno._getValue();
		var url = '<emp:url action="queryIqpBailInfoList.do"/>?serno='+serno;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpBailInfoGroup" title="保证金信息表" maxColumn="2">
			<emp:text id="IqpBailInfo.serno" label="业务编号" colSpan="2" maxlength="40" hidden="true" required="false"/>
			<emp:text id="IqpBailInfo.bail_acct_no" label="保证金账号"  required="true" />
			<emp:text id="IqpBailInfo.bail_acct_name" label="保证金账号名称" maxlength="80" required="true" />
			<emp:select id="IqpBailInfo.cur_type" label="币种" required="true" defvalue="CNY" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpBailInfo.rate" label="利率" maxlength="10" required="true" dataType="Rate" />
			<emp:text id="IqpBailInfo.up_rate" label="上浮比例" maxlength="10" required="true" dataType="Percent" />
			<emp:select id="IqpBailInfo.bail_type" label="保证金类型" required="true" dictname="STD_ZB_BAIL_STATUS" />
			<emp:text id="IqpBailInfo.dep_term" label="存期" maxlength="10" required="true" dataType="Int" />
			<emp:pop id="IqpBailInfo.open_org_displayname" label="开户机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" required="true" />
			<emp:text id="IqpBailInfo.open_org" label="开户机构" required="false" hidden="true"/>
			<emp:text id="IqpBailInfo.cus_id" label="客户码" maxlength="40" hidden="true" required="false" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
