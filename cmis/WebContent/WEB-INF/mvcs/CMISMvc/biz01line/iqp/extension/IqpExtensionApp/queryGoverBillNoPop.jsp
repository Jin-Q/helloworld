<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表POP页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		AccLoanPop._toForm(form);
		resultSet._obj.ajaxQuery(null,form);
	};
	
	
	function doReset(){
		page.dataGroups.AccLoanPopGroup.reset();
	};
	
	/*--user code begin--*/
	function doReturnMethod(){
		var data = resultSet._obj.getSelectedData();
		if (data != null && data.length !=0) {
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin.${context.returnMethod}(data[0])");
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doSelect(){
		doReturnMethod();
	}
	function doCancel(){
		window.close();
	};

	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="AccLoanPopGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="AccLoanPop.bill_no" label="借据编号"  />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<emp:returnButton id="s1" label="选择返回"/>
	<emp:table icollName="resultSet" pageMode="true" url="pageOutBillNoPop.do?cus_id=${context.cus_id}">
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="loan_amt" label="借款金额(元)" dataType="Currency"/>
		<emp:text id="loan_balance" label="借款余额(元)" dataType="Currency"/>
		<emp:text id="start_date" label="起贷日期" />
		<emp:text id="end_date" label="止贷日期" />
	</emp:table>
	<div align="left">
			<br>
 			<emp:returnButton id="s2" label="选择返回"/>
			<emp:button id="cancel" label="关闭" />
	</div>
</body>
</html>
</emp:page>
