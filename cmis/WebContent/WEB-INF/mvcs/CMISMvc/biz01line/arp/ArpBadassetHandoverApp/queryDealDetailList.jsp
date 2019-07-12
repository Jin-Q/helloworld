<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<html>
<head>
<title>列表查询页面</title>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	/*--user code begin--*/
	function doload(){
		flag = '${context.flag}';
		if(flag != 'success'){
			alert(flag);
		}
	};
	/*--user code end--*/
</script>
</head>
<body class="page_content" onload="doload()">
	<emp:table icollName="AccLoanList" pageMode="true" url="pageQueryDealDetailList.do" reqParams="bill_no=${context.bill_no}">
		<%-- <emp:text id="BillNo" label="借据编号" /> --%>
		<emp:text id="AcctNoCrdNo" label="账号/卡号" hidden="true"/>
		<%-- <emp:text id="AmtTp" label="金额类型" dataType="Currency"/> --%>
		<emp:text id="RecDtlAmt" label="回收明细金额" dataType="Currency"/>
		<emp:text id="AcctTxnTm" label="账户交易时间" />
		<emp:text id="AcctAccgSt" label="账户核算状态" />
	</emp:table>

	<%-- <emp:table icollName="AccLoanList" pageMode="true" url="pageQueryDealDetailList.do" reqParams="bill_no=${context.bill_no}">
		<emp:text id="CONTRACT_NO" label="合同编号" />
		<emp:text id="DUEBILL_NO" label="借据编号" />
		<emp:text id="LOAN_CARD_NO" label="贷款卡号" hidden="true"/>
		<emp:text id="DR_CR_FLAG" label="借贷标志" dictname="STD_ZB_DC_FLAG" />
		<emp:text id="DETAIL_TYPE" label="明细类型" dictname="STD_ZB_DETAIL_TYPE" />
		<emp:text id="CCY" label="币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="TRANT_AMT" label="交易金额" dataType="Currency"/>
		<emp:text id="RECYCLE_TYPE" label="回收方式" dictname="STD_ZB_RECOVER_TYPE" />
		<emp:text id="TRAN_DATE" label="交易日期" />
	</emp:table> --%>
</body>
</html>
</emp:page>