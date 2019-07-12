<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<%
	request.setAttribute("canwrite","");
%>
<script type="text/javascript">

</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="" method="POST">	
		<emp:gridLayout id="PspCheckAnalyGroup" title="集团信息情况" maxColumn="2">
			<emp:textarea id="PspCheckAnaly.grp_member_infos" label="集团客户整体及各成员企业的基本情况、成员构成与股权结构的检查" maxlength="2000" required="true" colSpan="2"/>
			<emp:textarea id="PspCheckAnaly.grp_intra_grouptrans" label="报告期内该集团的内部关联交易情况" maxlength="2000" required="true" colSpan="2"/>
			<emp:textarea id="PspCheckAnaly.grp_major_events" label="报告期内集团客户的发展及内部重大事项" maxlength="2000" required="true" colSpan="2"/>
			<emp:textarea id="PspCheckAnaly.grp_credit_actualsituation" label="报告期内我行对该集团客户提供的各类授信及实际发生情况" maxlength="2000" required="true" colSpan="2"/>
			<emp:textarea id="PspCheckAnaly.grp_biz_circumstance" label="集团整体经营情况、集团整体财务情况" maxlength="2000" required="true" colSpan="2"/>
			<emp:textarea id="PspCheckAnaly.grp_compre_assessment" label="对该集团的综合评价及今后授信策略的建议" maxlength="2000" required="true" />
			<emp:text id="PspCheckAnaly.task_id" label="集团任务号" hidden="true"/>
		</emp:gridLayout>
	</emp:form>
</body>
</html>
</emp:page>
