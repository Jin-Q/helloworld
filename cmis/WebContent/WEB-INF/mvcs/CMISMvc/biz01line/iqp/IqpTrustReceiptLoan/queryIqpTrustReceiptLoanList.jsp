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
		IqpTrustReceiptLoan._toForm(form);
		IqpTrustReceiptLoanList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpTrustReceiptLoanPage() {
		var paramStr = IqpTrustReceiptLoanList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpTrustReceiptLoanUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpTrustReceiptLoan() {
		var paramStr = IqpTrustReceiptLoanList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpTrustReceiptLoanViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpTrustReceiptLoanPage() {
		var url = '<emp:url action="getIqpTrustReceiptLoanAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpTrustReceiptLoan() {
		var paramStr = IqpTrustReceiptLoanList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpTrustReceiptLoanRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpTrustReceiptLoanGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpTrustReceiptLoanGroup" title="输入查询条件" maxColumn="2">
			<emp:select id="IqpTrustReceiptLoan.is_limit_cont_pay" label="是否额度合同项下支用" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpTrustReceiptLoan.limit_cont_no" label="额度合同编号" />
			<emp:text id="IqpTrustReceiptLoan.order_no" label="来单号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpTrustReceiptLoanPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpTrustReceiptLoanPage" label="修改" op="update"/>
		<emp:button id="deleteIqpTrustReceiptLoan" label="删除" op="remove"/>
		<emp:button id="viewIqpTrustReceiptLoan" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpTrustReceiptLoanList" pageMode="true" url="pageIqpTrustReceiptLoanQuery.do">
		<emp:text id="is_limit_cont_pay" label="是否额度合同项下支用" dictname="STD_ZX_YES_NO" />
		<emp:text id="limit_cont_no" label="额度合同编号" />
		<emp:text id="order_no" label="来单号" />
		<emp:text id="receipt_cur_type" label="单据币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="curt_order_amt" label="本次到单金额" />
		<emp:text id="serno" label="业务编号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    