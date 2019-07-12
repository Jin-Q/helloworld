<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String type = (String)context.getDataValue("type");
%> 
<emp:page>

<html>
<head>
<title>风险预警信息</title>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">

</script>
</head>

<body class="page_content">
	<emp:table icollName="LmtWarnDetail" pageMode="true" url="pageDthLmtWarnDetail.do?type=${context.type}&value=${context.value}">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<%
			if (type.equals("Onetwo")) {
		%>
		<emp:text id="guar_type" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" />
		<%
			}
		%>
		<emp:text id="belg_line" label="条线" dictname="STD_ZB_BIZ_BELG" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="crd_amt" label="授信金额" dataType="Currency" />
		<%
			if (type.equals("Five")) {
		%>
		<emp:text id="guar_amt" label="担保金额" dataType="Currency" />
		<%
			}
		%>		
		<emp:text id="cust_mgr_displayname" label="客户经理" />
		<emp:text id="main_br_id_displayname" label="管理机构" />
	</emp:table>
	
</body>
</html>
</emp:page>