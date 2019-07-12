<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpBillIncome._toForm(form);
		IqpBillIncomeList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpBillIncomePage() {
		var paramStr = IqpBillIncomeList._obj.getParamStr(['batch_no','porder_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpBillIncomeUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpBillIncome() {
		var paramStr = IqpBillIncomeList._obj.getParamStr(['batch_no','porder_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpBillIncomeViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpBillIncomePage() {
		var url = '<emp:url action="getIqpBillIncomeAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpBillIncome() {
		var paramStr = IqpBillIncomeList._obj.getParamStr(['batch_no','porder_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpBillIncomeRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpBillIncomeGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpBillIncomeGroup" title="输入查询条件" maxColumn="2">
			<emp:select id="IqpBillIncome.biz_type" label="业务类型" />
			<emp:text id="IqpBillIncome.batch_no" label="批次号" />
			<emp:text id="IqpBillIncome.porder_no" label="汇票号码" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpBillIncomePage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpBillIncomePage" label="修改" op="update"/>
		<emp:button id="deleteIqpBillIncome" label="删除" op="remove"/>
		<emp:button id="viewIqpBillIncome" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpBillIncomeList" pageMode="true" url="pageIqpBillIncomeQuery.do">
		<emp:text id="biz_type" label="业务类型" />
		<emp:text id="batch_no" label="批次号" />
		<emp:text id="porder_no" label="汇票号码" />
		<emp:text id="fore_disc_date" label="转/贴现日期" />
		<emp:text id="drft_amt" label="票面金额" />
		<emp:text id="disc_days" label="贴现天数" />
		<emp:text id="adj_days" label="调整天数" />
		<emp:text id="disc_rate" label="转/贴现利率" />
	</emp:table>
	
</body>
</html>
</emp:page>
    