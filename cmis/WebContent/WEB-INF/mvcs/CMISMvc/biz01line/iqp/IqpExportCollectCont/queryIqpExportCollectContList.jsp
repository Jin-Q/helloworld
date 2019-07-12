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
		IqpExportCollectCont._toForm(form);
		IqpExportCollectContList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpExportCollectContPage() {
		var paramStr = IqpExportCollectContList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpExportCollectContUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpExportCollectCont() {
		var paramStr = IqpExportCollectContList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpExportCollectContViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpExportCollectContPage() {
		var url = '<emp:url action="getIqpExportCollectContAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpExportCollectCont() {
		var paramStr = IqpExportCollectContList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpExportCollectContRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpExportCollectContGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpExportCollectContGroup" title="输入查询条件" maxColumn="2">
			<emp:select id="IqpExportCollectCont.is_limit_cont_pay" label="是否额度合同项下支用" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpExportCollectCont.limit_cont_no" label="额度合同编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpExportCollectContPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpExportCollectContPage" label="修改" op="update"/>
		<emp:button id="deleteIqpExportCollectCont" label="删除" op="remove"/>
		<emp:button id="viewIqpExportCollectCont" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpExportCollectContList" pageMode="true" url="pageIqpExportCollectContQuery.do">
		<emp:text id="is_limit_cont_pay" label="是否额度合同项下支用" dictname="STD_ZX_YES_NO" />
		<emp:text id="limit_cont_no" label="额度合同编号" />
		<emp:text id="is_replace" label="是否置换" />
		<emp:text id="receipt_cur_amt" label="单据金额" />
		<emp:text id="serno" label="业务编号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    