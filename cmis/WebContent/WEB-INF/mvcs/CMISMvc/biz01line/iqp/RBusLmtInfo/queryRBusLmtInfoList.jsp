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
		RBusLmtInfo._toForm(form);
		RBusLmtInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateRBusLmtInfoPage() {
		var paramStr = RBusLmtInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getRBusLmtInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewRBusLmtInfo() {
		var paramStr = RBusLmtInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getRBusLmtInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddRBusLmtInfoPage() {
		var url = '<emp:url action="getRBusLmtInfoAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteRBusLmtInfo() {
		var paramStr = RBusLmtInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteRBusLmtInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.RBusLmtInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="RBusLmtInfoGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="RBusLmtInfo.agr_no" label="授信协议编号" />
			<emp:text id="RBusLmtInfo.serno" label="业务编号" />
			<emp:text id="RBusLmtInfo.cont_no" label="合同编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddRBusLmtInfoPage" label="新增" op="add"/>
		<emp:button id="getUpdateRBusLmtInfoPage" label="修改" op="update"/>
		<emp:button id="deleteRBusLmtInfo" label="删除" op="remove"/>
		<emp:button id="viewRBusLmtInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="RBusLmtInfoList" pageMode="true" url="pageRBusLmtInfoQuery.do">
		<emp:text id="agr_no" label="授信协议编号" />
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cont_no" label="合同编号" />
	</emp:table>
	
</body>
</html>
</emp:page>
    