<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String type = context.getDataValue("type").toString();
%>
<emp:page>

<html>
<head>
<title>贷款指标分析</title>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">

</script>
</head>

<body class="page_content">
	<emp:table icollName="IndivBadAnalyDetail" pageMode="true" url="pageDthIndivBadAnalyDetail.do?type=${context.type}&value=${context.value}">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="prd_type" label="业务品种" />
		<%
			if (type.equals("prd")||type.equals("grt")) {
		%>
		<emp:text id="assure_main" label="担保方式" dictname="STD_ZB_ASSURE_MEANS"/>
		<%
			}
		%>
		<emp:text id="loan_amt" label="贷款金额" dataType="Currency" />
		<emp:text id="loan_balance" label="贷款余额" dataType="Currency" />
		<emp:text id="distr_date" label="起贷日期" />
		<emp:text id="end_date" label="止贷日期" />
		<emp:text id="five_class" label="五级分类" dictname="STD_ZB_FIVE_SORT"/>
		<emp:text id="cust_mgr_displayname" label="客户经理" />
		<emp:text id="manager_br_id_displayname" label="管理机构" />
	</emp:table>
	
</body>
</html>
</emp:page>