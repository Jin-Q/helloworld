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
		WfiCallBackDisc._toForm(form);
		WfiCallBackDiscList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateWfiCallBackDiscPage() {
		var paramStr = WfiCallBackDiscList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getWfiCallBackDiscUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewWfiCallBackDisc() {
		var paramStr = WfiCallBackDiscList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getWfiCallBackDiscViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddWfiCallBackDiscPage() {
		var url = '<emp:url action="getWfiCallBackDiscAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	
	function doReset(){
		page.dataGroups.WfiCallBackDiscGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="WfiCallBackDiscGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="WfiCallBackDisc.cb_enname" label="打回标识号"  />
		<emp:text id="WfiCallBackDisc.cb_cnname" label="打回标识"  />
		<emp:select id="WfiCallBackDisc.is_inuse" label="是否有效" dictname="STD_ZX_YES_NO"  />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="getAddWfiCallBackDiscPage" label="新增" op="add"/>
		<emp:button id="getUpdateWfiCallBackDiscPage" label="修改" op="update"/>
		<emp:button id="viewWfiCallBackDisc" label="查看" op="view"/>
	</div>

	<emp:table icollName="WfiCallBackDiscList" pageMode="true" url="pageWfiCallBackDiscQuery.do">
		<emp:text id="pk_id" label="主键" hidden="true"  />
		<emp:text id="cb_enname" label="打回标识号" />
		<emp:text id="cb_cnname" label="打回标识" />
		<emp:text id="cb_memo" label="内容说明" />
		<emp:text id="attr1" label="attr1" hidden="true" />
		<emp:text id="attr2" label="attr2" hidden="true"/>
		<emp:text id="attr3" label="attr3" hidden="true"/>
		<emp:text id="or_no" label="排序号" />
		<emp:text id="is_inuse" label="是否有效" dictname="STD_ZX_YES_NO" />
	</emp:table>
	
</body>
</html>
</emp:page>
    