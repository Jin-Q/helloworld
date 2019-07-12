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
		IqpCredit._toForm(form);
		IqpCreditList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpCreditPage() {
		var paramStr = IqpCreditList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpCreditUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpCredit() {
		var paramStr = IqpCreditList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpCreditViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpCreditPage() {
		var url = '<emp:url action="getIqpCreditAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpCredit() {
		var paramStr = IqpCreditList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpCreditRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpCreditGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpCreditGroup" title="输入查询条件" maxColumn="2">
			<emp:select id="IqpCredit.is_limit_cont_pay" label="是否额度合同项下支用" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpCredit.limit_cont_no" label="额度合同编号" />
			<emp:text id="IqpCredit.limit_cont_no" label="额度合同编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpCreditPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpCreditPage" label="修改" op="update"/>
		<emp:button id="deleteIqpCredit" label="删除" op="remove"/>
		<emp:button id="viewIqpCredit" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpCreditList" pageMode="true" url="pageIqpCreditQuery.do">
		<emp:text id="is_limit_cont_pay" label="是否额度合同项下支用" dictname="STD_ZX_YES_NO" />
		<emp:text id="limit_cont_no" label="额度合同编号" />
		<emp:text id="end_date" label="到期日" />
		<emp:text id="end_date" label="到期日" />
		<emp:text id="credit_term_type" label="信用证期限类型" />
		<emp:text id="fast_day" label="远期天数" />
		<emp:text id="floodact_perc" label="溢装比例" />
		<emp:text id="shortact_perc" label="短装比例" />
		<emp:text id="serno" label="业务编号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    