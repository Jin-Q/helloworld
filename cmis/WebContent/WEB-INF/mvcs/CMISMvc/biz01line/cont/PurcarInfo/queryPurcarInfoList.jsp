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
		PurcarInfo._toForm(form);
		PurcarInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePurcarInfoPage() {
		var paramStr = PurcarInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getPurcarInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPurcarInfo() {
		var paramStr = PurcarInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getPurcarInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPurcarInfoPage() {
		var url = '<emp:url action="getPurcarInfoAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePurcarInfo() {
		var paramStr = PurcarInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deletePurcarInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PurcarInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PurcarInfoGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PurcarInfo.serno" label="业务编号" />
			<emp:text id="PurcarInfo.cont_no" label="合同编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddPurcarInfoPage" label="新增" op="add"/>
		<emp:button id="getUpdatePurcarInfoPage" label="修改" op="update"/>
		<emp:button id="deletePurcarInfo" label="删除" op="remove"/>
		<emp:button id="viewPurcarInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="PurcarInfoList" pageMode="true" url="pagePurcarInfoQuery.do">
		<emp:text id="car_brand" label="汽车品牌" />
		<emp:text id="car_name" label="汽车名称" />
		<emp:text id="car_model" label="汽车型号" />
		<emp:text id="car_type" label="汽车种类" />
		<emp:text id="pur_amt" label="购买金额" />
		<emp:text id="loan_perc" label="贷款比例" />
		<emp:text id="fst_pyr_perc" label="首付款比率" />
		<emp:text id="serno" label="业务编号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    