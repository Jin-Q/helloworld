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
		EsbConfigImple._toForm(form);
		EsbConfigImpleList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateEsbConfigImplePage() {
		var paramStr = EsbConfigImpleList._obj.getParamStr(['esb_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getEsbConfigImpleUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewEsbConfigImple() {
		var paramStr = EsbConfigImpleList._obj.getParamStr(['esb_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getEsbConfigImpleViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddEsbConfigImplePage() {
		var url = '<emp:url action="getEsbConfigImpleAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteEsbConfigImple() {
		var paramStr = EsbConfigImpleList._obj.getParamStr(['esb_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteEsbConfigImpleRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.EsbConfigImpleGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="EsbConfigImpleGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="EsbConfigImple.esb_id" label="主键" />
			<emp:text id="EsbConfigImple.service_code" label="交易码" />
			<emp:text id="EsbConfigImple.service_sence" label="交易场景" />
			<emp:select id="EsbConfigImple.status" label="状态" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddEsbConfigImplePage" label="新增" op="add"/>
		<emp:button id="getUpdateEsbConfigImplePage" label="修改" op="update"/>
		<emp:button id="deleteEsbConfigImple" label="删除" op="remove"/>
		<emp:button id="viewEsbConfigImple" label="查看" op="view"/>
	</div>

	<emp:table icollName="EsbConfigImpleList" pageMode="true" url="pageEsbConfigImpleQuery.do">
		<emp:text id="esb_id" label="主键" hidden="true"/>
		<emp:text id="service_code" label="交易码" />
		<emp:text id="service_sence" label="交易场景" />
		<emp:text id="memo" label="备注" />
		<emp:text id="imple_class" label="交易实现类" />
		<emp:text id="status" label="状态" dictname="STD_PRD_STATE" />
	</emp:table>
	
</body>
</html>
</emp:page>
    