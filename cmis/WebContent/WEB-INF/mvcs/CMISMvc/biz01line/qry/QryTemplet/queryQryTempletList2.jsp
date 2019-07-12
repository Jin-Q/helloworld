<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>

<%
	String tempType = request.getParameter("QryTemplet.templet_type");
%>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doGetAddQryTempletPage(){
		var url = '<emp:url action="getQryTempletAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteQryTemplet(){		
		var paramStr = QryTempletList._obj.getParamStr(['temp_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteQryTempletRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetUpdateQryTempletPage(){
		var paramStr = QryTempletList._obj.getParamStr(['temp_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getQryTempletUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
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
	function doGenQryPage(){
		var paramStr = QryTempletList._obj.getParamStr(['temp_no']);
		if (paramStr != null) {
			var url = '<emp:url action="genQryPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doGetQueryPage(){
		var paramStr = QryTempletList._obj.getParamStr(['temp_no','jsp_file_name','templet_type','temp_name']);
		if (paramStr != null) {
			var url = '<emp:url action="getQueryPage.do"/>?'+paramStr;
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

	function doOnload() {
		//QryTemplet.temp_type._obj.element.value = "<%=tempType%>";
		QryTemplet.templet_type._setValue(<%=tempType%>);
		//alert(<%=tempType%>);
	}
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>

<body class="page_content" onload="doOnload()">
	<form action="#" id="queryForm" >
	</form>
	<emp:gridLayout id="QryTempletGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="QryTemplet.temp_name" label="查询名称"/>
			<emp:text id="QryTemplet.temp_enable" label="是否启用" defvalue="1" hidden="true"/>
			<emp:select id="QryTemplet.templet_type" label="查询类型" dictname="STD_ZB_TEMPLET_TYPE" readonly="true" hidden="true" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />


	<div align="left">
		<emp:button id="getAddQryTempletPage" label="新增" op="add"/>
		<emp:button id="getUpdateQryTempletPage" label="修改" op="update"/>
		<emp:button id="deleteQryTemplet" label="删除" op="remove"/>
		<emp:button id="genQryPage" label="生成查询页面" op="update"/>
		<emp:button id="getQueryPage" label="综合查询"/>
	</div>
	<emp:table icollName="QryTempletList" pageMode="true" url="pageQryTempletQuery.do" reqParams="QryTemplet.temp_enable=$QryTemplet.temp_enable;">
		<emp:text id="temp_no" label="查询模板编号" hidden="true"/>
		<emp:text id="temp_name" label="查询名称"/>
		<emp:text id="organlevel" label="适用机构"  hidden="true"/>
		<emp:text id="templet_type" label="查询类型" dictname="STD_ZB_TEMPLET_TYPE" hidden="true"/>
		<emp:text id="temp_pattern" label="查询模式" dictname="STD_ZB_TEMP_PATTERN" hidden="true"/>
		<emp:text id="classpath" label="扩展类路径" hidden="true"/>
		<emp:text id="temp_enable" label="是否启用" dictname="STD_ZX_YES_NO" hidden="true"/>
		<emp:text id="query_sql" label="查询SQL语句" hidden="true" />
		<emp:text id="jsp_file_name" label="查询页面文件夹名" hidden="true"/>
		<emp:text id="order_id" label="排序字段" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>