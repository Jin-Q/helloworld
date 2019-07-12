<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="../pubAction/arpPubAction.jsp" flush="true"/>
<style type="text/css">
.emp_field_textarea_textarea {
	width: 99%;
};
</style>
<script type="text/javascript">
	
	/*--user code begin--*/
	function doSubmits(){
		doPubUpdate(ArpLawExecuteRecover);
	};
	function doReturn() {
		var url = '<emp:url action="queryArpLawExecuteRecoverList.do"/>?case_no='+ArpLawExecuteRecover.case_no._getValue();
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateArpLawExecuteRecoverRecord.do" method="POST">	
		<emp:gridLayout id="ArpLawExecuteRecoverGroup" title="执行回收情况" maxColumn="2">
			<emp:text id="ArpLawExecuteRecover.serno" label="流水号" maxlength="40" hidden="true" />
			<emp:text id="ArpLawExecuteRecover.case_no" label="案件编号" maxlength="40" required="true" hidden="true"/>
			<emp:select id="ArpLawExecuteRecover.reclaim_type" label="回收类型" required="true" dictname="STD_ZB_RECLAIM_TYPE" />
			<emp:select id="ArpLawExecuteRecover.phase" label="阶段" required="true" dictname="STD_ZB_LAWSUIT_PHASE" />
			<emp:text id="ArpLawExecuteRecover.pay_amt" label="支出金额" maxlength="16" required="false" dataType="Currency" />
			<emp:date id="ArpLawExecuteRecover.fee_hpp_date" label="费用发生日期" required="false" />
			<emp:text id="ArpLawExecuteRecover.reclaim_amt" label="回收金额" maxlength="16" required="true" dataType="Currency" />			
			<emp:date id="ArpLawExecuteRecover.reclaim_date" label="回收日期" required="true" />
			<emp:textarea id="ArpLawExecuteRecover.memo" label="备注" maxlength="250" required="false" colSpan="2" />
		</emp:gridLayout>
		
		<emp:gridLayout id="ArpLawExecuteRecoverGroup" maxColumn="2" title="登记信息">
			<emp:text id="ArpLawExecuteRecover.input_id_displayname" label="登记人" readonly="true" required="true" />
			<emp:text id="ArpLawExecuteRecover.input_br_id_displayname" label="登记机构" readonly="true" required="true"  />
			<emp:text id="ArpLawExecuteRecover.input_id" label="登记人" required="true"   hidden="true"/>
			<emp:text id="ArpLawExecuteRecover.input_br_id" label="登记机构" required="true"  hidden="true" />
			<emp:date id="ArpLawExecuteRecover.input_date" label="登记日期" required="true"   readonly="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submits" label="保存" />
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
