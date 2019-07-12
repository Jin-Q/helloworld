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
		var url = '<emp:url action="queryIqpAssetIssueInfoList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpAssetIssueInfoGroup" title="封包/发行管理" maxColumn="2">
			<emp:text id="IqpAssetIssueInfo.serno" label="业务编号" maxlength="40" required="true" readonly="true" defvalue="${context.serno}" />
			<emp:text id="IqpAssetIssueInfo.cont_no" label="合同编号" maxlength="40" required="true" readonly="true" defvalue="${context.cont_no}"/>
			<emp:select id="IqpAssetIssueInfo.act_issue_type" label="业务类型" required="true" dictname="STD_ZB_ACT_ISSUE_TYPE"/>
			<emp:date id="IqpAssetIssueInfo.act_issue_date" label="实际发行日期" required="true" defvalue="${context.issue_date}"/>
			<emp:text id="IqpAssetIssueInfo.act_issue_amt" label="实际发行总量（万元）" maxlength="16" required="true" dataType="Currency"/>
			<emp:date id="IqpAssetIssueInfo.base_date" label="基准日（起息日）" required="true" />
			<emp:date id="IqpAssetIssueInfo.end_date" label="法定到期日期" required="true" />
			<emp:text id="IqpAssetIssueInfo.fee_cal_mode" label="服务费计算方式" maxlength="10" required="true" />
			<emp:text id="IqpAssetIssueInfo.fee_rate" label="服务费率" maxlength="16" required="true" dataType="Percent"/>
			<emp:text id="IqpAssetIssueInfo.fee_min" label="服务费下限（元）" maxlength="16" required="true" dataType="Currency"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
