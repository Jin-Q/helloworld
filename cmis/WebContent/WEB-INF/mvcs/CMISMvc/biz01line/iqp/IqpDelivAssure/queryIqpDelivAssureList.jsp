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
		IqpDelivAssure._toForm(form);
		IqpDelivAssureList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpDelivAssurePage() {
		var paramStr = IqpDelivAssureList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpDelivAssureUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpDelivAssure() {
		var paramStr = IqpDelivAssureList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpDelivAssureViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpDelivAssurePage() {
		var url = '<emp:url action="getIqpDelivAssureAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpDelivAssure() {
		var paramStr = IqpDelivAssureList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpDelivAssureRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpDelivAssureGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpDelivAssureGroup" title="输入查询条件" maxColumn="2">
			<emp:select id="IqpDelivAssure.is_limit_cont_pay" label="是否额度合同项下支用" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpDelivAssure.limit_cont_no" label="额度合同编号" />
			<emp:text id="IqpDelivAssure.cdt_cert_no" label="信用证编号" />
			<emp:text id="IqpDelivAssure.reorder_no" label="提单号码" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpDelivAssurePage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpDelivAssurePage" label="修改" op="update"/>
		<emp:button id="deleteIqpDelivAssure" label="删除" op="remove"/>
		<emp:button id="viewIqpDelivAssure" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpDelivAssureList" pageMode="true" url="pageIqpDelivAssureQuery.do">
		<emp:text id="is_limit_cont_pay" label="是否额度合同项下支用" dictname="STD_ZX_YES_NO" />
		<emp:text id="limit_cont_no" label="额度合同编号" />
		<emp:text id="is_replace" label="是否置换" />
		<emp:text id="cdt_cert_no" label="信用证编号" />
		<emp:text id="reorder_no" label="提单号码" />
		<emp:text id="serno" label="业务编号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    