<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
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
		var url = '<emp:url action="queryArpLawFeeInfoList.do"/>?case_no='+ArpLawFeeInfo.case_no._getValue();
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
		<emp:gridLayout id="ArpLawFeeInfoGroup" title="费用信息" maxColumn="2">
			<emp:text id="ArpLawFeeInfo.serno" label="流水号" maxlength="40" hidden="true" />
			<emp:text id="ArpLawFeeInfo.case_no" label="案件编号" maxlength="40" required="true" hidden="true"/>
			<emp:select id="ArpLawFeeInfo.fee_phase" label="费用发生阶段" required="true" dictname="STD_ZB_FEE_PHASE" />
			<emp:select id="ArpLawFeeInfo.fee_type" label="费用类型" required="true" dictname="STD_ZB_FEE_TYPE" />
			<emp:text id="ArpLawFeeInfo.fee_amt" label="费用金额" maxlength="16" required="true" dataType="Currency" />
			<emp:date id="ArpLawFeeInfo.fee_date" label="费用发生日期" required="true" />
			<emp:textarea id="ArpLawFeeInfo.memo" label="备注" maxlength="250" required="false" colSpan="2" />
		</emp:gridLayout>
		
		<emp:gridLayout id="ArpLawFeeInfoGroup" maxColumn="2" title="登记信息">
			<emp:text id="ArpLawFeeInfo.input_id_displayname" label="登记人" readonly="true" required="true" />
			<emp:text id="ArpLawFeeInfo.input_br_id_displayname" label="登记机构" readonly="true" required="true"  />
			<emp:text id="ArpLawFeeInfo.input_id" label="登记人" required="true"   hidden="true"/>
			<emp:text id="ArpLawFeeInfo.input_br_id" label="登记机构" required="true" hidden="true" />
			<emp:date id="ArpLawFeeInfo.input_date" label="登记日期" required="true"  readonly="true" />
		</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表"/>
	</div>
</body>
</html>
</emp:page>
