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
		RBusLmtcreditInfo._toForm(form);
		RBusLmtcreditInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateRBusLmtcreditInfoPage() {
		var paramStr = RBusLmtcreditInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getRBusLmtcreditInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewRBusLmtcreditInfo() {
		var paramStr = RBusLmtcreditInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getRBusLmtcreditInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddRBusLmtcreditInfoPage() {
		var url = '<emp:url action="getRBusLmtcreditInfoAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteRBusLmtcreditInfo() {
		var paramStr = RBusLmtcreditInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteRBusLmtcreditInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.RBusLmtcreditInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="RBusLmtcreditInfoGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="RBusLmtcreditInfo.agr_no" label="授信协议编号" />
			<emp:text id="RBusLmtcreditInfo.serno" label="业务编号" />
			<emp:text id="RBusLmtcreditInfo.cont_no" label="合同编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddRBusLmtcreditInfoPage" label="新增" op="add"/>
		<emp:button id="getUpdateRBusLmtcreditInfoPage" label="修改" op="update"/>
		<emp:button id="deleteRBusLmtcreditInfo" label="删除" op="remove"/>
		<emp:button id="viewRBusLmtcreditInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="RBusLmtcreditInfoList" pageMode="true" url="pageRBusLmtcreditInfoQuery.do">
		<emp:text id="agr_no" label="授信协议编号" />
		<emp:text id="lmt_type" label="授信类别" />
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cont_no" label="合同编号" />
	</emp:table>
	
</body>
</html>
</emp:page>
    