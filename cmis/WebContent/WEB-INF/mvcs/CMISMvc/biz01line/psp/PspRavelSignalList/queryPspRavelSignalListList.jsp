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
		PspRavelSignalList._toForm(form);
		PspRavelSignalListList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePspRavelSignalListPage() {
		var paramStr = PspRavelSignalListList._obj.getParamStr(['serno','pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspRavelSignalListUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPspRavelSignalList() {
		var paramStr = PspRavelSignalListList._obj.getParamStr(['serno','pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspRavelSignalListViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspRavelSignalListPage() {
		var url = '<emp:url action="getPspRavelSignalListAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePspRavelSignalList() {
		var paramStr = PspRavelSignalListList._obj.getParamStr(['serno','pk_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deletePspRavelSignalListRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PspRavelSignalListGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddPspRavelSignalListPage" label="新增" op="add"/>
		<emp:button id="getUpdatePspRavelSignalListPage" label="修改" op="update"/>
		<emp:button id="deletePspRavelSignalList" label="删除" op="remove"/>
		<emp:button id="viewPspRavelSignalList" label="查看" op="view"/>
	</div>

	<emp:table icollName="PspRavelSignalListList" pageMode="false" url="pagePspRavelSignalListQuery.do">
		<emp:text id="serno" label="申请编号" />
		<emp:text id="pk_id" label="预警信号ID" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="signal_info" label="风险预警信息内容及影响" />
		<emp:text id="signal_type" label="类型" dictname="STD_ZB_ALT_SIGNAL_TYPE" />
		<emp:text id="last_date" label="预计持续时间（天）" />
		<emp:text id="disp_mode" label="处置措施及进展情况" />
		<emp:text id="input_id" label="登记人" />
		<emp:text id="input_br_id" label="登记机构" />
		<emp:text id="input_date" label="登记日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    