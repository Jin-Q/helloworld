<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doGetAddIndLibPage(){
		var url = '<emp:url action="getIndLibAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIndLib(){		
		var paramStr = IndLibList._obj.getParamStr(['index_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIndLibRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetUpdateIndLibPage(){
		var paramStr = IndLibList._obj.getParamStr(['index_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIndLibUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIndLib(){
		var paramStr = IndLibList._obj.getParamStr(['index_no']);
		if (paramStr != null) {
			var url = '<emp:url action="queryIndLibDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IndLib._toForm(form);
		IndLibList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.IndLibGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>

<body class="page_content">
	<form action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IndLibGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IndLib.index_no" label="指标编号" />
			<emp:text id="IndLib.index_name" label="指标名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="getAddIndLibPage" label="新增" op="add"/>
		<emp:button id="getUpdateIndLibPage" label="修改" op="update"/>
		<emp:button id="deleteIndLib" label="删除" op="remove"/>
		<emp:button id="viewIndLib" label="查看" op="view"/>
	</div>
	<emp:table icollName="IndLibList" pageMode="true" url="pageIndLibQuery.do">
		<emp:text id="index_no" label="指标编号" />
		<emp:text id="index_name" label="指标名称" />
		<emp:text id="fnc_index_rpt" label="财务指标编号" maxlength="16" />
		<emp:text id="par_index_no" label="上级指标编号" />
		<emp:select id="index_property" label="指标性质" dictname="STD_ZB_PARA_PROP"/>
		<emp:select id="index_type" label="指标类别" dictname="STD_ZB_IND_TYPE"/>
		<emp:select id="input_type" label="指标取值方式" dictname="STD_ZB_PARA_VAL_TYP"/>
		<emp:text id="input_classpath" label="指标取值实现类" />
		<emp:select id="exe_cycle" label="执行周期" dictname="STD_ZB_RUN_FREQ"/>
		<emp:select id="index_level" label="指标级别" dictname="STD_ZB_PARA_LEVEL"/>
	</emp:table>
</body>
</html>
</emp:page>