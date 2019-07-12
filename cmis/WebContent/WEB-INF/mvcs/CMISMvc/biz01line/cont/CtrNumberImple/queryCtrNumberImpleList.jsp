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
		CtrNumberImple._toForm(form);
		CtrNumberImpleList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCtrNumberImplePage() {
		var paramStr = CtrNumberImpleList._obj.getParamStr(['score_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getCtrNumberImpleUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCtrNumberImple() {
		var paramStr = CtrNumberImpleList._obj.getParamStr(['score_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getCtrNumberImpleViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCtrNumberImplePage() {
		var url = '<emp:url action="getCtrNumberImpleAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCtrNumberImple() {
		var paramStr = CtrNumberImpleList._obj.getParamStr(['score_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCtrNumberImpleRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CtrNumberImpleGroup.reset();
	};
		
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="CtrNumberImpleGroup" title="输入查询条件" maxColumn="2">
			<emp:select id="CtrNumberImple.score_type" label="评分类型" dictname="STD_CTR_SCORE_TYPE"/>
			<emp:text id="CtrNumberImple.score_name" label="评分字段名称" />
			<emp:date id="CtrNumberImple.update_date" label="修改日期" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddCtrNumberImplePage" label="新增" op="add"/>
		<emp:button id="getUpdateCtrNumberImplePage" label="修改" op="update"/>
		<emp:button id="deleteCtrNumberImple" label="删除" op="remove"/>
		<emp:button id="viewCtrNumberImple" label="查看" op="view"/>
	</div>

	<emp:table icollName="CtrNumberImpleList" pageMode="true" url="pageCtrNumberImpleQuery.do">
		<emp:text id="score_type" label="评分类型"  dictname="STD_CTR_SCORE_TYPE"/>
		<emp:text id="score_code" label="评分字段" />
		<emp:text id="score_name" label="评分字段名称" />
		<emp:text id="auto_score" label="得分" />
		<emp:date id="input_date" label="登记日期" />
		<emp:text id="update_date" label="修改日期" />
	    <emp:text id="score_id" label="主键"  hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    