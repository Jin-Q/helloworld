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
	<emp:table icollName="CcrWarnDetail" pageMode="true" url="pageDthCcrWarnDetail.do?type=${context.type}&value=${context.value}">
	
		<%
		if("one".equals(type)){%>
		<emp:text id="belg_line" label="所属条线" dictname="STD_ZB_BUSILINE"/>
		<%}%>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="cus_crd_grade" label="评级等级" dictname="STD_ZB_CREDIT_GRADE"/>
		<emp:text id="cus_crd_dt" label="到期日期" />
		<emp:text id="cust_mgr_displayname" label="客户经理" />
		<emp:text id="main_br_id_displayname" label="管理机构" />
		<%if("two".equals(type)){%>
		<emp:text id="over_status" label="是否已到期" />
		<%}%>		
	</emp:table>
	
</body>
</html>
</emp:page>