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
		CusTrusteeLog._toForm(form);
		CusTrusteeLogList._obj.ajaxQuery(null,form);
	};

	function doGetUpdateCusTrusteeLogPage() {
		var paramStr = CusTrusteeLogList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusTrusteeLogUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doViewCusTrusteeLog() {
		var paramStr = CusTrusteeLogList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusTrusteeLogViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doGetAddCusTrusteeLogPage() {
		var url = '<emp:url action="getCusTrusteeLogAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

	function doDeleteCusTrusteeLog() {
		var paramStr = CusTrusteeLogList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusTrusteeLogRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doReset(){
		page.dataGroups.CusTrusteeLogGroup.reset();
	};

	/*--user code begin--*/

	/*--user code end--*/

</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="CusTrusteeLogGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CusTrusteeLog.consignor_id" label="委托人" />
			<emp:text id="CusTrusteeLog.consignor_br_id" label="委托机构" />
			<emp:text id="CusTrusteeLog.trustee_id" label="托管人" />
			<emp:text id="CusTrusteeLog.trustee_br_id" label="托管机构" />
			<emp:datespace id="CusTrusteeLog.trustee_date" label="托管日期" colSpan="2"/>
	</emp:gridLayout>

	<jsp:include page="/queryInclude.jsp" flush="true" />

	<div align="left">
		<emp:button id="getAddCusTrusteeLogPage" label="新增" op="add"/>
		<emp:button id="getUpdateCusTrusteeLogPage" label="修改" op="update"/>
		<emp:button id="deleteCusTrusteeLog" label="删除" op="remove"/>
		<emp:button id="viewCusTrusteeLog" label="查看" op="view"/>
	</div>

	<emp:table icollName="CusTrusteeLogList" pageMode="true" url="pageCusTrusteeLogQuery.do">
		<emp:text id="trustee_date" label="托管日期" />
		<emp:text id="trustee_scope" label="托管范围" dictname="STD_ZB_HAND_SCOPE" />

		<emp:text id="consignor_br_id_displayname" label="委托机构" />
		<emp:text id="consignor_id_displayname" label="委托人" />
		<emp:text id="trustee_br_id_displayname" label="托管机构" />
		<emp:text id="trustee_id_displayname" label="托管人" />
		<emp:text id="supervise_br_id_displayname" label="监交机构" />
		<emp:text id="supervise_id_displayname" label="监交人" />

		<emp:text id="consignor_br_id" label="委托机构" hidden="true"/>
		<emp:text id="consignor_id" label="委托人" hidden="true"/>
		<emp:text id="trustee_br_id" label="托管机构" hidden="true"/>
		<emp:text id="trustee_id" label="托管人" hidden="true"/>
		<emp:text id="supervise_br_id" label="监交机构" hidden="true"/>
		<emp:text id="supervise_id" label="监交人" hidden="true"/>

		<emp:text id="retract_date" label="收回日期" />
		<emp:text id="serno" label="申请流水号" hidden="true"/>
	</emp:table>

</body>
</html>
</emp:page>
