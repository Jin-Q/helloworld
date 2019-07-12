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
		IqpBailSubDisDetail._toForm(form);
		IqpBailSubDisDetailList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpBailSubDisDetailPage() {
		var paramStr = IqpBailSubDisDetailList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpBailSubDisDetailUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpBailSubDisDetail() {
		var paramStr = IqpBailSubDisDetailList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpBailSubDisDetailViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpBailSubDisDetailPage() {
		var url = '<emp:url action="getIqpBailSubDisDetailAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpBailSubDisDetail() {
		var paramStr = IqpBailSubDisDetailList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpBailSubDisDetailRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpBailSubDisDetailGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<div align="left">
		<emp:button id="getAddIqpBailSubDisDetailPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpBailSubDisDetailPage" label="修改" op="update"/>
		<emp:button id="deleteIqpBailSubDisDetail" label="删除" op="remove"/>
		<emp:button id="viewIqpBailSubDisDetail" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpBailSubDisDetailList" pageMode="true" url="pageIqpBailSubDisDetailQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="bail_acct_no" label="保证金账号" />
		<emp:text id="origi_bail_bal" label="原保证金余额" />
		<emp:text id="adjust_amt" label="追加/提取金额" />
	</emp:table>
	
</body>
</html>
</emp:page>
    