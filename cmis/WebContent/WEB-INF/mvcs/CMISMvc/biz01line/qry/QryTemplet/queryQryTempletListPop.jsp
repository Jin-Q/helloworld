<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doGetAddQryTempletPage(){
		var url = '<emp:url action="getQryTempletAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doViewQryTemplet(){
		var paramStr = QryTempletList._obj.getParamStr(['temp_no']);
		if (paramStr != null) {
			var url = '<emp:url action="queryQryTempletDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doQuery(){
		var form = document.getElementById('queryForm');
		QryTemplet._toForm(form);
		QryTempletList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.QryTempletGroup.reset();
	};

	function doSelect(){
		var data = QryTempletList._obj.getSelectedData();	
		if (data != null && data.length !=0) {
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin.${context.popReturnMethod}(data[0])");
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};
	
</script>
</head>

<body class="page_content">
	<form action="#" id="queryForm">
	</form>
	<emp:gridLayout id="QryTempletGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="QryTemplet.temp_name" label="查询名称"/>
			<emp:select id="QryTemplet.templet_type" label="查询类型" dictname="STD_ZB_TEMPLET_TYPE" readonly="false" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />


	<div align="left">
		<button onclick="doSelect()">选取返回</button>
	</div>
	<emp:table icollName="QryTempletList" pageMode="true" url="pageQryTempletQuery.do">
		<emp:text id="temp_no" label="查询模板编号" hidden="false"/>
		<emp:text id="temp_name" label="查询名称"/>
		<emp:text id="organlevel_displayname" label="适用机构" />
		<emp:text id="templet_type" label="查询类型" dictname="STD_ZB_TEMPLET_TYPE" />
		<emp:text id="temp_pattern" label="查询模式" dictname="STD_ZB_TEMP_PATTERN" hidden="true"/>
		<emp:text id="classpath" label="扩展类路径" hidden="true"/>
		<emp:text id="temp_enable" label="是否启用" dictname="STD_ZX_YES_NO"   />
		<emp:text id="query_sql" label="查询SQL语句" hidden="true" />
		<emp:text id="jsp_file_name" label="查询页面文件夹名" hidden="true"/>
		<emp:text id="order_id" label="排序字段" />
	</emp:table>
	<div align="left">
		<button onclick="doSelect()">选取返回</button>
	</div>
</body>
</html>
</emp:page>