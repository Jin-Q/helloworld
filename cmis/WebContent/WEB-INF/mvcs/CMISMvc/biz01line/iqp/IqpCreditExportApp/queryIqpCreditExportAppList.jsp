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
		IqpCreditExportApp._toForm(form);
		IqpCreditExportAppList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpCreditExportAppPage() {
		var paramStr = IqpCreditExportAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpCreditExportAppUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpCreditExportApp() {
		var paramStr = IqpCreditExportAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpCreditExportAppViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpCreditExportAppPage() {
		var url = '<emp:url action="getIqpCreditExportAppAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpCreditExportApp() {
		var paramStr = IqpCreditExportAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpCreditExportAppRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpCreditExportAppGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpCreditExportAppGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpCreditExportApp.serno" label="业务编号" />
			<emp:select id="IqpCreditExportApp.is_limit_cont_pay" label="是否额度合同项下支用" dictname="STD_ZX_YES_NO" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpCreditExportAppPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpCreditExportAppPage" label="修改" op="update"/>
		<emp:button id="deleteIqpCreditExportApp" label="删除" op="remove"/>
		<emp:button id="viewIqpCreditExportApp" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpCreditExportAppList" pageMode="true" url="pageIqpCreditExportAppQuery.do">
		<emp:text id="is_limit_cont_pay" label="是否额度合同项下支用" dictname="STD_ZX_YES_NO" />
		<emp:text id="limit_cont_no" label="额度合同编号" />
		<emp:text id="is_replace" label="是否置换" />
		<emp:text id="bank_bp_no" label="我行bp号" />
		<emp:text id="is_internal_cert" label="是否国内证项下" />
		<emp:text id="receipt_cur_type" label="单据币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="receipt_cur_amt" label="单据金额" />
		<emp:text id="serno" label="业务编号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    