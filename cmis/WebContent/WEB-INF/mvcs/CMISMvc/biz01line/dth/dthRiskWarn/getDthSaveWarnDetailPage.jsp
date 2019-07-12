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
	<emp:table icollName="SaveWarnDetail" pageMode="true" url="pageDthSaveWarnDetail.do?value=${context.value}">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="lmt_type" label="授信类型" />
		<emp:text id="crd_totl_amt" label="授信总额" dataType="Currency" />
		<emp:text id="crd_cir_amt" label="循环授信敞口额度" dataType="Currency" />
		<emp:text id="crd_one_amt" label="一次性授信敞口额度" dataType="Currency" />
		<emp:text id="cust_mgr_displayname" label="客户经理" />
		<emp:text id="manager_br_id_displayname" label="管理机构" />
	</emp:table>
</body>
</html>
</emp:page>