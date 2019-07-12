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
		IqpAccumulationFundInfo._toForm(form);
		IqpAccumulationFundInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpAccumulationFundInfoPage() {
		var paramStr = IqpAccumulationFundInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAccumulationFundInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAccumulationFundInfo() {
		var paramStr = IqpAccumulationFundInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAccumulationFundInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpAccumulationFundInfoPage() {
		var url = '<emp:url action="getIqpAccumulationFundInfoAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpAccumulationFundInfo() {
		var paramStr = IqpAccumulationFundInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpAccumulationFundInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpAccumulationFundInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpAccumulationFundInfoGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpAccumulationFundInfo.serno" label="业务流水号" />
			<emp:select id="IqpAccumulationFundInfo.ir_accord_type" label="利率依据方式" dictname="STD_ZB_IR_ACCORD_TYPE" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpAccumulationFundInfoPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpAccumulationFundInfoPage" label="修改" op="update"/>
		<emp:button id="deleteIqpAccumulationFundInfo" label="删除" op="remove"/>
		<emp:button id="viewIqpAccumulationFundInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpAccumulationFundInfoList" pageMode="true" url="pageIqpAccumulationFundInfoQuery.do">
		<emp:text id="serno" label="业务流水号" />
		<emp:text id="apply_amount" label="申请金额" />
		<emp:text id="apply_cur_type" label="申请币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="ir_accord_type" label="利率依据方式" dictname="STD_ZB_IR_ACCORD_TYPE" />
		<emp:text id="ir_type" label="利率种类" dictname="STD_ZB_RATE_TYPE" />
	</emp:table>
	
</body>
</html>
</emp:page>
    