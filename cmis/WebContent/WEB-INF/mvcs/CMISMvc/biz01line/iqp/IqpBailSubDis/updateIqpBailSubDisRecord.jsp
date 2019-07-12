<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateIqpBailSubDisRecord.do" method="POST">
		<emp:gridLayout id="IqpBailSubDisGroup" maxColumn="2" title="保证金追加/提取申请">
			<emp:text id="IqpBailSubDis.serno" label="业务编号" maxlength="60" required="true" readonly="true" />
			<emp:text id="IqpBailSubDis.cont_no" label="合同编号" maxlength="60" required="true" />
			<emp:text id="IqpBailSubDis.cus_id" label="客户码" maxlength="60" required="false" />
			<emp:text id="IqpBailSubDis.ori_bail_amt" label="原保证金金额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="IqpBailSubDis.ori_bail_perc" label="原保证金比例" maxlength="20" required="false" dataType="Rate" />
			<emp:text id="IqpBailSubDis.adjusted_bail_perc" label="追加/提取后保证金比率" maxlength="20" required="false" dataType="Rate" />
			<emp:text id="IqpBailSubDis.adjust_bail_amt" label="追加/提取保证金金额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="IqpBailSubDis.adjusted_bail_amt" label="追加/提取后保证金金额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="IqpBailSubDis.manager_id" label="责任人" maxlength="30" required="false" />
			<emp:text id="IqpBailSubDis.manager_br_id" label="责任机构" maxlength="30" required="false" />
			<emp:text id="IqpBailSubDis.input_id" label="登记人" maxlength="30" required="false" />
			<emp:text id="IqpBailSubDis.input_br_id" label="登记机构" maxlength="30" required="false" />
			<emp:date id="IqpBailSubDis.input_date" label="登记日期" required="false" />
			<emp:select id="IqpBailSubDis.approve_status" label="申请状态" required="false" dictname="WF_APP_STATUS" />
			<emp:text id="IqpBailSubDis.flag" label="申请类型（1--追加，2--提取）" maxlength="6" required="false" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="修改" op="update"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
