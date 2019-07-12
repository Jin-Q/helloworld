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
		IqpInterFact._toForm(form);
		IqpInterFactList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpInterFactPage() {
		var paramStr = IqpInterFactList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpInterFactUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpInterFact() {
		var paramStr = IqpInterFactList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpInterFactViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpInterFactPage() {
		var url = '<emp:url action="getIqpInterFactAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpInterFact() {
		var paramStr = IqpInterFactList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpInterFactRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpInterFactGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpInterFactGroup" title="输入查询条件" maxColumn="2">
			<emp:select id="IqpInterFact.is_limit_cont_pay" label="是否额度合同项下支用" dictname="STD_ZX_YES_NO" />
			<emp:text id="IqpInterFact.limit_cont_no" label="额度合同编号" />
			<emp:select id="IqpInterFact.fin_type" label="融资类型" />
			<emp:select id="IqpInterFact.fact_type" label="保理类型" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpInterFactPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpInterFactPage" label="修改" op="update"/>
		<emp:button id="deleteIqpInterFact" label="删除" op="remove"/>
		<emp:button id="viewIqpInterFact" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpInterFactList" pageMode="true" url="pageIqpInterFactQuery.do">
		<emp:text id="is_limit_cont_pay" label="是否额度合同项下支用" dictname="STD_ZX_YES_NO" />
		<emp:text id="limit_cont_no" label="额度合同编号" />
		<emp:text id="fin_type" label="融资类型" />
		<emp:text id="fact_type" label="保理类型" />
		<emp:text id="invc_cur_type" label="发票币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="invc_totl_amt" label="发票总金额" />
		<emp:text id="busnes_cont_no" label="贸易合同号" />
		<emp:text id="serno" label="业务编号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    