<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
    function doload(){
	    var flag = '${context.flag}';
	    if(flag != 'success'){
		    alert(flag);
	    }
    };
	function doQuery(){
		var form = document.getElementById('queryForm');
		AccLoan._toForm(form);
		RepayPlayList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.AccLoanGroup.reset();
	};
	function returnCus(data){
		AccLoan.cus_id._setValue(data.cus_id._getValue());
		AccLoan.cus_name._setValue(data.cus_name._getValue());
	};

	function returnPrdId(data){
		AccLoan.prd_id._setValue(data.id);
		AccLoan.prd_id_displayname._setValue(data.label); 
	};
	/*--user code begin--*/
	
	/*--user code end----*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:table icollName="RepayPlayList" pageMode="true" url="PageSelectRepayPlay.do" reqParams="bill_no=${context.bill_no}">
		<emp:text id="BillNo" label="借据编号" />
		<emp:text id="TrmTms"  label="期数" />
		<emp:text id="AmtTp" label="金额类型" dictname="STD_ZB_AIM_TYPE"/>
		<emp:text id="BegDt" label="起始日期"/>
		<emp:text id="EndDt" label="终止日期"/>
		<emp:text id="PlanAmt" label="计划金额" dataType="Currency"/>
		<emp:text id="BnPayAmt" label="已支付金额" dataType="Currency" hidden="true"/>
		<emp:text id="RcvblPnpAmt" label="本金余额" dataType="Currency"/>		
	</emp:table>

	<%-- <emp:table icollName="RepayPlayList" pageMode="true" url="PageSelectRepayPlay.do" reqParams="bill_no=${context.bill_no}">
		<emp:text id="CONTRACT_NO" label="合同号" hidden="true"/>   
		<emp:text id="DUEBILL_NO" label="借据编号" />
		<emp:text id="TERMS"  label="期数" />
		<emp:text id="AGREE_REPAY_DATE" label="约定还款日期" />
		<emp:text id="TERM_AMT" label="分期金额" dataType="Currency"/>
		<emp:text id="TERM_CORPUS" label="分期本金" dataType="Currency"/>
		<emp:text id="TERM_INTEREST" label="分期利息" dataType="Currency"/>
		<emp:text id="REMAIN_CORPUS" label="剩余本金" dataType="Currency"/>
		<emp:text id="ACT_INT_RATE" label="执行利率" dataType="Rate"/>		
		<emp:text id="REPAY_CORPUS" label="实还本金" dataType="Currency"/>
		<emp:text id="REPAY_INTEREST" label="实还利息" dataType="Currency"/>
		<emp:text id="ACTUAL_REPAY_DATE" label="实际还款日期" />
	</emp:table> --%>
</body>
</html>
</emp:page>
    