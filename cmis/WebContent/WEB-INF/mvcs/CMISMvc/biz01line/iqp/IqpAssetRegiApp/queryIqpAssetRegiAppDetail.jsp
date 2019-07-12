<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String flag = "";
	if(context.containsKey("flag")){
		flag = (String)context.getDataValue("flag");
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
		var url = '<emp:url action="queryIqpAssetRegiAppList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpAssetRegiAppGroup" title="信贷资产登记" maxColumn="2">
			<emp:select id="IqpAssetRegiApp.regi_type" label="业务类别" required="true" dictname="STD_ZB_REGI_TYPE"/>
			<emp:pop id="IqpAssetRegiApp.bill_no" label="借据编号" url="AccViewPop.do?cusTypCondition=table_model in('AccLoan','AccPad') and status='1'&returnMethod=returnAcc" required="true" />
			<emp:text id="IqpAssetRegiApp.cont_no" label="合同编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="IqpAssetRegiApp.cus_id" label="客户编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="IqpAssetRegiApp.cus_id_displayname" label="客户名称"  required="false" readonly="true"/>
			<emp:select id="IqpAssetRegiApp.cur_type" label="币种" required="false" readonly="true" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpAssetRegiApp.loan_amt" label="贷款金额" maxlength="16" required="false" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpAssetRegiApp.loan_balance" label="贷款余额" maxlength="16" required="false" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpAssetRegiApp.inner_owe_int" label="表内欠息" maxlength="16" required="false" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpAssetRegiApp.out_owe_int" label="表外欠息" maxlength="16" required="false" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="IqpAssetRegiApp.five_class" label="五级分类" required="false" readonly="true" dictname="STD_ZB_FIVE_SORT"/>
			<emp:select id="IqpAssetRegiApp.twelve_cls_flg" label="十二级分类" required="false" readonly="true" dictname="STD_ZB_TWELVE_CLASS"/>
			
			<emp:pop id="IqpAssetRegiApp.manager_br_id_displayname" label="管理机构"  required="true" buttonLabel="选择" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" reqParams="restrictUsed=false" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no" readonly="true" />
			<emp:pop id="IqpAssetRegiApp.fina_br_id_displayname" label="账务机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getAcctOrgID" reqParams="restrictUsed=false" required="true" readonly="true"/>
		    <emp:text id="IqpAssetRegiApp.manager_br_id" label="管理机构" hidden="true" />
			<emp:text id="IqpAssetRegiApp.fina_br_id" label="账务机构" hidden="true" />
			
			<emp:textarea id="IqpAssetRegiApp.remarks" label="备注" maxlength="250" required="false" colSpan="2"/>
		</emp:gridLayout>
		<emp:gridLayout id="1" title="登记信息" maxColumn="2">
			<emp:text id="IqpAssetRegiApp.input_id_displayname" label="登记人"  required="false" defvalue="${context.currentUserName}" readonly="true"/>
			<emp:text id="IqpAssetRegiApp.input_br_id_displayname" label="登记机构"  required="false" defvalue="${context.organName}" readonly="true"/>
			<emp:text id="IqpAssetRegiApp.input_date" label="登记日期" maxlength="10" required="false" defvalue="${context.OPENDAY}"  readonly="true"/>
			<emp:text id="IqpAssetRegiApp.input_id" label="登记人" maxlength="40" required="false" defvalue="${context.currentUserId}" hidden="true" readonly="true"/>
			<emp:text id="IqpAssetRegiApp.input_br_id" label="登记机构" maxlength="20" required="false" defvalue="${context.organNo}" hidden="true" readonly="true"/>
			<emp:select id="IqpAssetRegiApp.approve_status" label="审批状态" required="false" defvalue="000" readonly="true" hidden="true" dictname="WF_APP_STATUS" />
		    <emp:text id="IqpAssetRegiApp.serno" label="业务流水号" maxlength="40" required="false" hidden="true"/>
		</emp:gridLayout>
	
	<div align="center">
		<br>
		<%if(!"noButton".equals(flag)){ %>
		   <emp:button id="return" label="返回到列表页面"/>
		<%}%>
	</div>
</body>
</html>
</emp:page>
