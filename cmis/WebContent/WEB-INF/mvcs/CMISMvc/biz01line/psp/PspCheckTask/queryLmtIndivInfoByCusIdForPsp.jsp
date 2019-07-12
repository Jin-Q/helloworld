<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
	}
	String psp_cus_type = "";
	if(context.containsKey("psp_cus_type")){
		psp_cus_type = (String)context.getDataValue("psp_cus_type");
	}
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateIqpBksyndicRecord.do" method="POST">
		<emp:gridLayout id="IqpBksyndicGroup" title="授信额度" maxColumn="2">
			<emp:text id="LmtIndivInfo.cir_crd_amt" label="循环授信额度（元）" defvalue="0" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtIndivInfo.once_crd_amt" label="一次性授信额度（元）" defvalue="0" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtIndivInfo.loan_amt" label="贷款金额(元)" defvalue="0" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtIndivInfo.loan_balance" label="贷款余额(元)" defvalue="0" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtIndivInfo.owe_int" label="欠息累计(元)" defvalue="0" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		</emp:gridLayout>
	</emp:form>
	<emp:tabGroup id="IqpBksyndic_tabs" mainTab="IqpBksyndicInfo_tab">
	<%if("002".equals(psp_cus_type)){//消费性 %>
		<%if(op.equals("view")){ %>
			<emp:tab id="IqpBksyndicInfo_tab" label=" " url="queryPspCheckList.do" reqParams="scheme_id=FFFA276A01540B408DDF86D4BD01757E&task_id=${context.task_id}&cus_id=${context.cus_id}&op=view" initial="true"/>
		<%}else{ %>
			<emp:tab id="IqpBksyndicInfo_tab" label=" " url="savePspCheckList.do" reqParams="scheme_id=FFFA276A01540B408DDF86D4BD01757E&task_id=${context.task_id}&cus_id=${context.cus_id}&op=update" initial="true"/>
		<%} %>
	<%}else{ %>
		<%if(op.equals("view")){ %>
			<emp:tab id="IqpBksyndicInfo_tab" label=" " url="queryPspCheckList.do" reqParams="scheme_id=FFFA27870186AC649B81C1BD49E92F3C&task_id=${context.task_id}&cus_id=${context.cus_id}&op=view" initial="true"/>
		<%}else{ %>
			<emp:tab id="IqpBksyndicInfo_tab" label=" " url="savePspCheckList.do" reqParams="scheme_id=FFFA27870186AC649B81C1BD49E92F3C&task_id=${context.task_id}&cus_id=${context.cus_id}&op=update" initial="true"/>
		<%} %>
	<%} %>
	</emp:tabGroup>
</body>
</html>
</emp:page>
