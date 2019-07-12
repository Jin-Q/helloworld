<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addCtrLimitContRecord.do" method="POST">
		
		<emp:gridLayout id="CtrLimitContGroup" title="额度合同表" maxColumn="2">
			<emp:text id="CtrLimitCont.cont_no" label="合同编号" maxlength="40" required="true" />
			<emp:text id="CtrLimitCont.serno" label="业务编号" maxlength="40" required="false" />
			<emp:text id="CtrLimitCont.cont_cn" label="中文合同编号" maxlength="200" required="false" />
			<emp:text id="CtrLimitCont.cus_id" label="客户码" maxlength="32" required="false" />
			<emp:text id="CtrLimitCont.cur_type" label="币种" maxlength="3" required="false" />
			<emp:text id="CtrLimitCont.cont_amt" label="合同金额" maxlength="16" required="false" />
			<emp:text id="CtrLimitCont.start_date" label="起始日期" maxlength="10" required="false" />
			<emp:text id="CtrLimitCont.end_date" label="到期日期" maxlength="10" required="false" />
			<emp:text id="CtrLimitCont.cont_status" label="合同状态" maxlength="5" required="false" />
			<emp:text id="CtrLimitCont.memo" label="备注" maxlength="200" required="false" />
			<emp:text id="CtrLimitCont.manager_br_id" label="管理机构" maxlength="20" required="false" />
			<emp:text id="CtrLimitCont.input_id" label="登记人" maxlength="32" required="false" />
			<emp:text id="CtrLimitCont.input_br_id" label="登记机构" maxlength="20" required="false" />
			<emp:text id="CtrLimitCont.input_date" label="登记日期" maxlength="10" required="false" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

