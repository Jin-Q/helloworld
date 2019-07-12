<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>

<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		AccLoan._toForm(form);
		AccLoanList._obj.ajaxQuery(null,form);
	};
	
	function doViewAccLoan() {
		var paramStr = AccLoanList._obj.getParamStr(['bill_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getAccLoanViewPage.do"/>?'+paramStr+'&menu_type=bad';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.AccLoanGroup.reset();
	};
	function returnCus(data){
		AccLoan.cus_id._setValue(data.cus_id._getValue());
	};
	/*--user code begin--*/
	function doDebitAccLoan(){
		var url = '<emp:url action="getReportShowPage.do"/>&reportId=arp/arpbadrecive.raq&bill_no=';
		checkCusBelong(url);
	};
	function doGuarAccLoan(){
		var url = '<emp:url action="getReportShowPage.do"/>&reportId=arp/arpbadguartee.raq&bill_no=';
		checkCusBelong(url);
	};
	function checkCusBelong(url){
		var bill_no = AccLoanList._obj.getParamValue('bill_no');
		url = url+bill_no;
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9
				+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	};
	function doHandoverAccLoan(){
		var paramStr = AccLoanList._obj.getParamStr(['bill_no']);
		if (paramStr != null) {
			check(paramStr);
		} else {
			alert('请先选择一条记录！');
		}
	};
	function check(paramStr){
		var bill_no = AccLoanList._obj.getParamValue('bill_no');
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;	
				if(flag == "success"){
					var url = '<emp:url action="getArpBadassetHandoverAppAddPage.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else {
					alert("此借据已做过不良资产移交!");
					obj.value = '';
					return false;
				}
			}
		};
		var handleFailure = function(o){
			alert("异步请求出错！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var url="<emp:url action='checkAssetPreserve.do'/>&type=checkBadasset&value="+bill_no;
		var postData = YAHOO.util.Connect.setForm();
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData)
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="AccLoanGroup" title="输入查询条件" maxColumn="2">
	        <emp:text id="AccLoan.bill_no" label="借据编号" />
			<emp:pop id="AccLoan.cus_id" label="客户码" url="queryAllCusPop.do?returnMethod=returnCus" buttonLabel="选择" />
			<emp:date id="AccLoan.distr_date" label="发放日期" />
			<emp:date id="AccLoan.end_date" label="到期日期" /> 
	</emp:gridLayout> 	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewAccLoan" label="查看" op="view"/>
		<emp:button id="HandoverAccLoan" label="不良移交" op="Handover"/>
		<!-- <emp:button id="DebitAccLoan" label="借款人催收" op="Debit" />
		<emp:button id="GuarAccLoan" label="担保人催收" op="Guar" />	-->
	</div>

	<emp:table icollName="AccLoanList" pageMode="true" url="pageArpBadassetAccQuery.do" >
		<emp:text id="serno" label="业务编号" hidden="true"/>   
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cont_no" hidden="true" label="合同编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="prd_id_displayname" label="产品编号" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="loan_balance" label="贷款余额" dataType="Currency"/>
		<emp:text id="loan_amt" label="贷款金额" dataType="Currency"/>
		<emp:text id="distr_date" label="发放日期" />
		<emp:text id="end_date" label="到期日期" />
		<emp:text id="fina_br_id_displayname" label="账务机构" />
		<emp:text id="acc_status" label="台账状态" dictname="STD_ZB_ACC_TYPE"/>
		<emp:text id="five_class" label="五级分类" dictname="STD_ZB_FIVE_SORT"/>
		<emp:text id="handover_status" label="移交情况" dictname="WF_APP_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>