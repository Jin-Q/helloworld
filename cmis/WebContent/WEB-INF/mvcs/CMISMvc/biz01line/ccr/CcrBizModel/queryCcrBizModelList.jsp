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
		CcrBizModel._toForm(form);
		CcrBizModelList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCcrBizModelPage() {
		var paramStr = CcrBizModelList._obj.getParamStr(['com_cll_typ','com_opt_scale','index_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getCcrBizModelUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCcrBizModel() {
		alert(CcrBizModelList._obj.getSelectedData());

		var paramStr = CcrBizModelList._obj.getParamStr(['com_cll_typ','com_opt_scale','index_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getCcrBizModelViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCcrBizModelPage() {
		var url = '<emp:url action="getCcrBizModelAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCcrBizModel() {
		var paramStr = CcrBizModelList._obj.getParamStr(['com_cll_typ','com_opt_scale','index_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCcrBizModelRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CcrBizModelGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="CcrBizModelGroup" title="输入查询条件" maxColumn="2">
			<emp:select id="CcrBizModel.com_cll_typ" label="行业类型" dictname="STD_ZB_ASS_CREDIT"/>
			<emp:text id="CcrBizModel.index_no" label="指标编号" />
			<emp:text id="CcrBizModel.index_name" label="指标名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddCcrBizModelPage" label="新增" op="add"/>
		<emp:button id="getUpdateCcrBizModelPage" label="修改" op="update"/>
		<emp:button id="deleteCcrBizModel" label="删除" op="remove"/>
		<emp:button id="viewCcrBizModel" label="查看" op="view"/>
	</div>

	<emp:table icollName="CcrBizModelList" pageMode="true" url="pageCcrBizModelQuery.do">
		<emp:select id="com_cll_typ" label="行业类型" dictname="STD_ZB_ASS_CREDIT"/>
		<emp:select id="com_opt_scale" label="企业规模"  dictname="STD_ZB_ENTERPRISE"/>
		<emp:text id="index_no" label="指标编号" />
		<emp:text id="index_name" label="指标名称" />
		<emp:text id="excellent_score" label="优秀值" />
		<emp:text id="good_score" label="良好值" />
		<emp:text id="average_score" label="平均值" />
		<emp:text id="lower_score" label="较低值" />
		<emp:text id="worse_score" label="较差值" />
		<emp:text id="worst_score" label="最差值" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    