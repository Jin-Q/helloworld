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
		AccpDetail._toForm(form);
		AccpDetailList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateAccpDetailPage() {
		var paramStr = AccpDetailList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getAccpDetailUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewAccpDetail() {
		var paramStr = AccpDetailList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getAccpDetailViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddAccpDetailPage() {
		var url = '<emp:url action="getAccpDetailAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteAccpDetail() {
		var paramStr = AccpDetailList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteAccpDetailRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.AccpDetailGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="AccpDetailGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="AccpDetail.serno" label="业务编号" />
			<emp:text id="AccpDetail.cont_no" label="合同编号" />
			<emp:text id="AccpDetail.pyee" label="收款人" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddAccpDetailPage" label="新增" op="add"/>
		<emp:button id="getUpdateAccpDetailPage" label="修改" op="update"/>
		<emp:button id="deleteAccpDetail" label="删除" op="remove"/>
		<emp:button id="viewAccpDetail" label="查看" op="view"/>
	</div>

	<emp:table icollName="AccpDetailList" pageMode="true" url="pageAccpDetailQuery.do">
		<emp:text id="pyee" label="收款人" />
		<emp:text id="pyee _acct_no" label="收款人账号" />
		<emp:text id="pyee_acctsvcr_no" label="收款人开户行行号" />
		<emp:text id="pyee_acctsvcr_name" label="收款人开户行行名" />
		<emp:text id="drft_amt" label="票面金额" />
		<emp:text id="serno" label="业务编号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    