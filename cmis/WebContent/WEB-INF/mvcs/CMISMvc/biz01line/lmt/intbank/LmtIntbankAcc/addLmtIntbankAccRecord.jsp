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
	
	<emp:form id="submitForm" action="addLmtIntbankAccRecord.do" method="POST">
		<emp:gridLayout id="LmtIntbankAccGroup" title="同业客户授信台帐" maxColumn="2">
			<emp:text id="LmtIntbankAcc.serno" label="业务编号" maxlength="32" required="true" />
			<emp:text id="LmtIntbankAcc.batch_cus_no" label="批量客户编号" maxlength="32" required="false" />
			<emp:text id="LmtIntbankAcc.cus_id" label="客户码" maxlength="32" required="false" />
			<emp:select id="LmtIntbankAcc.cur_type" label="授信币种" required="false" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="LmtIntbankAcc.lmt_amt" label="授信总额" maxlength="16" required="false" />
			<emp:text id="LmtIntbankAcc.froze_amt" label="冻结额度" maxlength="16" required="false" />
			<emp:text id="LmtIntbankAcc.unfroze_amt" label="解冻额度" maxlength="16" required="false" />
			<emp:date id="LmtIntbankAcc.start_date" label="授信起始日期" required="false" />
			<emp:date id="LmtIntbankAcc.end_date" label="授信到期日期" required="false" />
			<emp:text id="LmtIntbankAcc.manager_id" label="责任人" maxlength="20" required="false" />
			<emp:pop id="LmtIntbankAcc.manager_br_id" label="管理机构" url="null" required="false" />
			<emp:select id="LmtIntbankAcc.lmt_status" label="额度状态" required="false" dictname="STD_LMT_STATUS" />
			<emp:date id="LmtIntbankAcc.break_date" label="终止日期" required="false" />
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
