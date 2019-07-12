<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>风险预警信息</title>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">

</script>
</head>

<body class="page_content">
	<emp:table icollName="CostWarnDetail" pageMode="true" url="pageDthCostWarnDetail.do?BL=${context.BL}&type=${context.type}&value=${context.value}">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE"/>
		<emp:text id="loan_amt" label="借据金额" dataType="Currency" />
		<emp:text id="overdue_balance" label="逾期金额" dataType="Currency" />
		<emp:text id="owe_int" label="欠息" dataType="Currency" />
		<emp:text id="overdue_date" label="逾期日期" />
		<emp:text id="manager_id_displayname" label="客户经理" />
		<emp:text id="manager_br_id_displayname" label="管理机构" />
	</emp:table>
	
</body>
</html>
</emp:page>