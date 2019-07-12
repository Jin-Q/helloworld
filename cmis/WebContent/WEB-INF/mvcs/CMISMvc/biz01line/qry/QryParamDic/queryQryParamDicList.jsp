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
		QryParamDic._toForm(form);
		QryParamDicList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateQryParamDicPage() {
		var paramStr = QryParamDicList._obj.getParamStr(['param_dic_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getQryParamDicUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewQryParamDic() {
		var paramStr = QryParamDicList._obj.getParamStr(['param_dic_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getQryParamDicViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddQryParamDicPage() {
		var url = '<emp:url action="getQryParamDicAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteQryParamDic() {
		var paramStr = QryParamDicList._obj.getParamStr(['param_dic_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteQryParamDicRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.QryParamDicGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="QryParamDicGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="QryParamDic.name" label="字典名称"/>
			<emp:text id="QryParamDic.opttype" label="参数选项字典" />
			<emp:text id="QryParamDic.param_dic_no" label="参数选项字典编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />

	
	<div align="left">
		<emp:button id="getAddQryParamDicPage" label="新增" op="add"/>
		<emp:button id="getUpdateQryParamDicPage" label="修改" op="update"/>
		<emp:button id="deleteQryParamDic" label="删除" op="remove"/>
		<emp:button id="viewQryParamDic" label="查看" op="view"/>
	</div>

	<emp:table icollName="QryParamDicList" pageMode="true" url="pageQryParamDicQuery.do">
		<emp:text id="param_dic_no" label="参数选项字典编号" />
		<emp:text id="name" label="字典名称" />
		<emp:text id="par_dic_type" label="参数类型" dictname="STD_ZB_PAR_DIC_TYPE" />
		<emp:text id="opttype" label="参数选项字典" />
		<emp:text id="query_sql" label="查询SQL语句"  hidden="true"/>
		<emp:text id="popname" label="Pop框信息" />
	</emp:table>
	
</body>
</html>
</emp:page>
    