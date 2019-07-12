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
	
	<emp:form id="submitForm" action="updateAccPadRecord.do" method="POST">
		<emp:gridLayout id="AccPadGroup" maxColumn="2" title="垫款台帐">
			<emp:text id="AccPad.serno" label="业务编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="AccPad.acc_day" label="日期" maxlength="10" required="false" />
			<emp:text id="AccPad.acc_year" label="年份" maxlength="5" required="false" />
			<emp:text id="AccPad.acc_mon" label="月份" maxlength="5" required="false" />
			<emp:text id="AccPad.prd_id" label="产品编号" maxlength="40" required="false" />
			<emp:text id="AccPad.cont_no" label="合同编号" maxlength="40" required="false" />
			<emp:text id="AccPad.bill_no" label="借据编号" maxlength="40" required="false" />
			<emp:text id="AccPad.cus_id" label="客户码" maxlength="40" required="false" />
			<emp:select id="AccPad.pad_type" label="垫款种类" required="false" />
			<emp:text id="AccPad.pad_bill_no" label="垫款业务借据编号" maxlength="40" required="false" />
			<emp:select id="AccPad.pad_cur_type" label="垫款币种" required="false" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="AccPad.pad_amt" label="垫款金额" maxlength="18" required="false" dataType="Currency" />
			<emp:date id="AccPad.pad_date" label="垫款日期" required="false" />
			<emp:text id="AccPad.pad_bal" label="垫款余额" maxlength="18" required="false" dataType="Currency" />
			<emp:date id="AccPad.separate_date" label="清分日期" required="false" hidden="true"/>
			<emp:date id="AccPad.writeoff_date" label="核销日期" required="false" />
			<emp:select id="AccPad.five_class" label="五级分类" required="false" dictname="STD_ZB_FIVE_SORT" />
			<emp:text id="AccPad.twelve_cls_flg" label="十二级分类标志" maxlength="5" required="false" />
			<emp:text id="AccPad.manager_br_id" label="管理机构" maxlength="20" required="false" />
			<emp:text id="AccPad.fina_br_id" label="账务机构" maxlength="20" required="false" />
			<emp:select id="AccPad.accp_status" label="台账状态" required="false" />
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
