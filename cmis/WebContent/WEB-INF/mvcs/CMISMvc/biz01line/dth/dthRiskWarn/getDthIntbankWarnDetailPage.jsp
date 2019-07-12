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
	<emp:table icollName="IntbankWarnDetail" pageMode="true" url="pageDthIntbankWarnDetail.do?value=${context.value}">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="same_org_cnname" label="同业机构（行）名称" />
		<emp:text id="crd_grade" label="信用等级" dictname="STD_ZB_FINA_GRADE"/>
		<emp:text id="limit_type" label="额度类型" dictname="STD_ZB_LIMIT_TYPE"/>
		<emp:text id="lmt_amt" label="授信总额" dataType="Currency" />
		<emp:text id="lmt_cost" label="已用额度" dataType="Currency" />
		<emp:text id="lmt_balance" label="剩余额度" dataType="Currency" />
		<emp:text id="start_date" label="授信起始日期" />
		<emp:text id="end_date" label="授信到期日期" />
	</emp:table>
	
</body>
</html>
</emp:page>