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
		IqpPackLoan._toForm(form);
		IqpPackLoanList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpPackLoanPage() {
		var paramStr = IqpPackLoanList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpPackLoanUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpPackLoan() {
		var paramStr = IqpPackLoanList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpPackLoanViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpPackLoanPage() {
		var url = '<emp:url action="getIqpPackLoanAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpPackLoan() {
		var paramStr = IqpPackLoanList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpPackLoanRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpPackLoanGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpPackLoanGroup" title="输入查询条件" maxColumn="2">
			<emp:select id="IqpPackLoan.is_limit_cont_pay" label="是否额度合同项下支用" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpPackLoan.limit_cont_no" label="额度合同编号" />
			<emp:text id="IqpPackLoan.cdt_cert_no" label="信用证编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpPackLoanPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpPackLoanPage" label="修改" op="update"/>
		<emp:button id="deleteIqpPackLoan" label="删除" op="remove"/>
		<emp:button id="viewIqpPackLoan" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpPackLoanList" pageMode="true" url="pageIqpPackLoanQuery.do">
		<emp:text id="is_limit_cont_pay" label="是否额度合同项下支用" dictname="STD_ZX_YES_NO" />
		<emp:text id="limit_cont_no" label="额度合同编号" />
		<emp:text id="cdt_cert_no" label="信用证编号" />
		<emp:text id="cdt_cert_cur_type" label="信用证币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="cdt_cert_amt" label="信用证金额" />
		<emp:text id="cdt_cert_bal" label="信用证余额" />
		<emp:text id="is_internal_cert" label="是否国内证" dictname="STD_ZX_YES_NO" />
		<emp:text id="serno" label="业务编号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    