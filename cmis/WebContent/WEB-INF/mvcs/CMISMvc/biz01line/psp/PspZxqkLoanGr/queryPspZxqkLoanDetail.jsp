<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	String task_id = request.getParameter("task_id"); 
	String dataFrom = request.getParameter("dataFrom");
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">

	/*--user code begin--*/
	function doload(){
		var task_id = '<%=task_id%>';
		PspZxqkLoan.task_id._setValue(task_id);
		
		convert_jqgr();
		convert_dwrz();
		convert_dwdb();
	}	

	function doReturn(){
		history.go(-1);
	}

	function convert_jqgr(){
		if(PspZxqkLoan.jqgr_sfyqb._getValue()=="1"){
			PspZxqkLoan.jqgr_sfyqb_sm._obj._renderHidden(false);
			PspZxqkLoan.jqgr_sfyqb_sm._obj._renderRequired(true);
		}else{
			PspZxqkLoan.jqgr_sfyqb_sm._obj._renderHidden(true);
			PspZxqkLoan.jqgr_sfyqb_sm._obj._renderRequired(false);
			PspZxqkLoan.jqgr_sfyqb_sm._setValue("");
		}
	}
	function convert_dwrz(){
		if(PspZxqkLoan.dwrz_sfyqb._getValue()=="1"){
			PspZxqkLoan.dwrz_sfyqb_sm._obj._renderHidden(false);
			PspZxqkLoan.dwrz_sfyqb_sm._obj._renderRequired(true);
		}else{
			PspZxqkLoan.dwrz_sfyqb_sm._obj._renderHidden(true);
			PspZxqkLoan.dwrz_sfyqb_sm._obj._renderRequired(false);
			PspZxqkLoan.dwrz_sfyqb_sm._setValue("");
		}
	}
	function convert_dwdb(){
		if(PspZxqkLoan.dwdb_sfyqb._getValue()=="1"){
			PspZxqkLoan.dwdb_sfyqb_sm._obj._renderHidden(false);
			PspZxqkLoan.dwdb_sfyqb_sm._obj._renderRequired(true);
		}else{
			PspZxqkLoan.dwdb_sfyqb_sm._obj._renderHidden(true);
			PspZxqkLoan.dwdb_sfyqb_sm._obj._renderRequired(false);
			PspZxqkLoan.dwdb_sfyqb_sm._setValue("");
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	
	<emp:form id="submitForm" action="addPspZxqkLoanRecord.do" method="POST">
		<emp:text id="PspZxqkLoan.pk_id" label="主键" required="true" hidden="true"/>
		<emp:text id="PspZxqkLoan.task_id" label="任务编码" hidden="true" defvalue="${context.task_id}" />
			<emp:text id="PspZxqkLoan.cus_id" label="客户编码" hidden="true" defvalue="${context.cus_id}" />
		
		<emp:gridLayout id="PspSitecheckComGroup" title="保证人" maxColumn="2"  >
			<emp:text id="PspZxqkLoan.bzr_name" label="保证人名称" required="true" />
			<emp:text id="PspZxqkLoan.dwrz_amt" label="对外融资总额（万元）"  required="true" dataType="Currency" />	
			<emp:radio id="PspZxqkLoan.dwrz_sfyqb" label="是否逾期、欠息或不良" required="true" dictname="STD_ZX_YES_NO" disabled="true" layout="false" colSpan="2"/>
			<emp:textarea id="PspZxqkLoan.dwrz_sfyqb_sm" label="如有请说明原因：" required="true" colSpan="2"/>
			<emp:text id="PspZxqkLoan.credit_bank_num" label="授信合作银行数量" required="true"  dataType="Int"/>
			<emp:text id="PspZxqkLoan.dwdb_amt" label="对外担保总额（万元）" required="true" dataType="Currency" />
			<emp:text id="PspZxqkLoan.dwdb_jsqzje" label="较上期增加（或减少）额"  required="true" dataType="Currency" />
			<emp:radio id="PspZxqkLoan.dwdb_sfyqb" label="是否逾期、欠息或不良" required="true" dictname="STD_ZX_YES_NO" disabled="true" layout="false" colSpan="2"/>
			<emp:textarea id="PspZxqkLoan.dwdb_sfyqb_sm" label="如有请说明原因：" required="true" colSpan="2"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="return" label="返回到列表页面" />
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

