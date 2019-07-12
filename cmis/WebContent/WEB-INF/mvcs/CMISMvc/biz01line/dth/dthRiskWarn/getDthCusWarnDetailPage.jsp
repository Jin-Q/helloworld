<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String type = "";
	if(context.containsKey("type")){
		type = (String)context.getDataValue("type");
	}
%>

<html>
<head>
<title>风险预警信息</title>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">

</script>
</head>

<body class="page_content">
	<emp:table icollName="CusWarnDetail" pageMode="true" url="pageDthCusWarnDetail.do?type=${context.type}&value=${context.value}">
		<%if("one".equals(type)){%>
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="prd_type" label="业务品种" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="loan_amt" label="贷款金额" dataType="Currency" />
		<emp:text id="loan_balance" label="贷款余额" dataType="Currency" />
		<emp:text id="distr_date" label="起贷日期" />
		<emp:text id="end_date" label="止贷日期" />
		<emp:text id="five_class" label="五级分类" dictname="STD_ZB_FIVE_SORT"/>
		<emp:text id="cust_mgr_displayname" label="客户经理" />
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<%}else{%>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cust_name" label="客户名称" />
		<emp:text id="month_real_avg" label="存款日均(月)" dataType="Currency"/>
		<emp:text id="manager_br_id" label="机构码" hidden="true"/>
		<emp:text id="org_name" label="机构名称" hidden="true"/>
		<%}%>
	</emp:table>	
</body>
</html>
</emp:page>