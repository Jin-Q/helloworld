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
		IqpGuarantInfo._toForm(form);
		IqpGuarantInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpGuarantInfoPage() {
		var paramStr = IqpGuarantInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpGuarantInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpGuarantInfo() {
		var paramStr = IqpGuarantInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpGuarantInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpGuarantInfoPage() {
		var url = '<emp:url action="getIqpGuarantInfoAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpGuarantInfo() {
		var paramStr = IqpGuarantInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpGuarantInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpGuarantInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpGuarantInfoGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpGuarantInfo.serno" label="业务编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpGuarantInfoPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpGuarantInfoPage" label="修改" op="update"/>
		<emp:button id="deleteIqpGuarantInfo" label="删除" op="remove"/>
		<emp:button id="viewIqpGuarantInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpGuarantInfoList" pageMode="true" url="pageIqpGuarantInfoQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="guarant_type" label="保函种类" />
		<emp:text id="guarant_mode" label="保函类型" />
		<emp:text id="open_type" label="开立类型" />
		<emp:text id="is_bank_format" label="是否我行标准格式" dictname="STD_ZX_YES_NO" />
		<emp:text id="is_agt_guarant" label="是否转开代理行保函" dictname="STD_ZX_YES_NO" />
		<emp:text id="agt_bank_no" label="代理行行号" />
		<emp:text id="agt_bank_name" label="代理行名称" />
		<emp:text id="item_name" label="项目名称" />
		<emp:text id="item_amt" label="项目金额" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="cont_name" label="合同名称" />
		<emp:text id="ben_name" label="受益人名称" />
	</emp:table>
	
</body>
</html>
</emp:page>
    