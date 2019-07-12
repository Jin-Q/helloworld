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
		IqpDiscApp._toForm(form);
		IqpDiscAppList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpDiscAppPage() {
		var paramStr = IqpDiscAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpDiscAppUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpDiscApp() {
		var paramStr = IqpDiscAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpDiscAppViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpDiscAppPage() {
		var url = '<emp:url action="getIqpDiscAppAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpDiscApp() {
		var paramStr = IqpDiscAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpDiscAppRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpDiscAppGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpDiscAppGroup" title="输入查询条件" maxColumn="2">
			<emp:select id="IqpDiscApp.bill_type" label="票据种类" dictname="STD_BIZ_TYPE" />
			<emp:select id="IqpDiscApp.five_class" label="五级分类" dictname="STD_ZB_FIVE_SORT" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpDiscAppPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpDiscAppPage" label="修改" op="update"/>
		<emp:button id="deleteIqpDiscApp" label="删除" op="remove"/>
		<emp:button id="viewIqpDiscApp" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpDiscAppList" pageMode="true" url="pageIqpDiscAppQuery.do">
		<emp:text id="bill_type" label="票据种类" dictname="STD_BIZ_TYPE" />
		<emp:text id="is_elec_bill" label="是否电子票据" dictname="STD_ZX_YES_NO" />
		<emp:text id="disc_type" label="贴现类型" />
		<emp:text id="disc_rate" label="贴现利息" />
		<emp:text id="net_pay_amt" label="实付总金额" />
		<emp:text id="serno" label="业务编号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    