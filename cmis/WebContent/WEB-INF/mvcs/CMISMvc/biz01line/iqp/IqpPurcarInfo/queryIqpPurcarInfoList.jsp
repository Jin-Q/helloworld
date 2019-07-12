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
		IqpPurcarInfo._toForm(form);
		IqpPurcarInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpPurcarInfoPage() {
		var paramStr = IqpPurcarInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpPurcarInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpPurcarInfo() {
		var paramStr = IqpPurcarInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpPurcarInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpPurcarInfoPage() {
		var url = '<emp:url action="getIqpPurcarInfoAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpPurcarInfo() {
		var paramStr = IqpPurcarInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpPurcarInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpPurcarInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpPurcarInfoGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpPurcarInfo.serno" label="业务编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpPurcarInfoPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpPurcarInfoPage" label="修改" op="update"/>
		<emp:button id="deleteIqpPurcarInfo" label="删除" op="remove"/>
		<emp:button id="viewIqpPurcarInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpPurcarInfoList" pageMode="true" url="pageIqpPurcarInfoQuery.do">
		<emp:text id="car_sign" label="汽车品牌" />
		<emp:text id="car_name" label="汽车名称" />
		<emp:text id="car_model" label="汽车型号" />
		<emp:text id="buy_amt" label="购买金额" />
		<emp:text id="loan_rate" label="贷款比例" />
		<emp:text id="first_pay_rate" label="首付款比率" />
		<emp:text id="serno" label="业务编号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    