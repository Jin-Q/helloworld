<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String menuId = "";
	if(context.containsKey("menuId")){
		menuId = (String)context.getDataValue("menuId");
	}
%>
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
		var url = '<emp:url action="queryLmtQuotaManagerList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/

	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:gridLayout id="LmtQuotaManagerGroup" title="限额管理" maxColumn="2">
			<emp:text id="LmtQuotaManager.serno" label="业务编号" maxlength="40" required="false" hidden="true"  colSpan="2" />
		<%if("OrgLmtQuota".equals(menuId)){%>
			<emp:text id="LmtQuotaManager.code_id" label="机构代码" readonly="true"  required="true" />
			<emp:text id="LmtQuotaManager.code_id_displayname" label="机构名称" colSpan="2" required="true" 
			cssElementClass="emp_field_text_cusname" readonly="true"/>
		<%} else{%>
			<emp:text id="LmtQuotaManager.code_id" label="客户经理编号" readonly="true" required="true" />
			<emp:text id="LmtQuotaManager.code_id_displayname" label="客户经理名称" colSpan="2" required="true" 
			readonly="true" cssElementClass="emp_field_text_cusname"/>
		<%} %>
			<emp:pop id="LmtQuotaManager.prd_id" label="产品代码" url='showPrdCheckTreeDetails.do?bizline=BL300' returnMethod="setProds"
			 colSpan="2" cssElementClass="emp_field_text_readonly" required="true" />
			<emp:textarea id="LmtQuotaManager.prd_id_displayname" label="产品名称" required="true" readonly="true" colSpan="2"/>
			<emp:text id="LmtQuotaManager.single_amt_quota" label="授信限额" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="LmtQuotaManager.sig_amt_quota" label="单户授信限额" maxlength="18" required="true" dataType="Currency" />			
			<emp:text id="LmtQuotaManager.sig_loan_quota" label="单笔贷款限额" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="LmtQuotaManager.sig_use_quota" label="单笔支用限额" maxlength="18" required="true" dataType="Currency" />
			<emp:date id="LmtQuotaManager.start_date" label="起始日期" required="true" readonly="true"/>
			<emp:date id="LmtQuotaManager.end_date" label="到期日期" required="true" />
			<emp:text id="LmtQuotaManager.manager_id_displayname" label="经办人" required="true" readonly="true" />
			<emp:text id="LmtQuotaManager.manager_br_id_displayname" label="经办机构"  required="true" readonly="true" />
			<emp:text id="LmtQuotaManager.manager_br_id" label="经办机构" maxlength="20" required="false" hidden="true" />
			<emp:text id="LmtQuotaManager.manager_id" label="经办人" maxlength="32" required="false"  hidden="true" />
			<emp:select id="LmtQuotaManager.quota_type" label="限额类型" required="false" dictname="STD_ZB_QUOTA_MAG" hidden="true"/>
			<emp:select id="LmtQuotaManager.approve_status" label="审批状态" dictname="WF_APP_STATUS" hidden="true" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表"/>
	</div>
</body>
</html>
</emp:page>
