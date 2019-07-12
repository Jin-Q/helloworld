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
		PspCheckItem._toForm(form);
		PspCheckItemList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePspCheckItemPage() {
		var paramStr = PspCheckItemList._obj.getParamStr(['item_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspCheckItemUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPspCheckItem() {
		var paramStr = PspCheckItemList._obj.getParamStr(['item_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspCheckItemViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPspCheckItemPage() {
		var url = '<emp:url action="getPspCheckItemAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePspCheckItem() {
		var paramStr = PspCheckItemList._obj.getParamStr(['item_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deletePspCheckItemRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PspCheckItemGroup.reset();
	};

	function doTest(){
		
		var url = '<emp:url action="queryPspCheckList.do"/>?scheme_id=FFFA278001451FB5F2CBE3D890DA4F4B&task_id=00000000';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	function doTest1(){
		
		var url = '<emp:url action="savePspCheckList.do"/>?scheme_id=FFFA278001451FB5F2CBE3D890DA4F4B&task_id=00000000';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PspCheckItemGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PspCheckItem.item_id" label="项目编号" />
			<emp:text id="PspCheckItem.item_name" label="项目名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddPspCheckItemPage" label="新增" op="add"/>
		<emp:button id="getUpdatePspCheckItemPage" label="修改" op="update"/>
		<emp:button id="deletePspCheckItem" label="删除" op="remove"/>
		<emp:button id="viewPspCheckItem" label="查看" op="view"/>
		<emp:button id="test" label="测试" op="" />
		<emp:button id="test1" label="示例" />
	</div>

	<emp:table icollName="PspCheckItemList" pageMode="true" url="pagePspCheckItemQuery.do">
		<emp:text id="item_id" label="项目编号" />
		<emp:text id="item_name" label="项目名称" />
		<emp:text id="default_value" label="缺省值" hidden="true"/>
		<emp:text id="tag_type" label="标签类型" dictname="STD_ZB_TAB_TYPE"/> 
		<emp:text id="tag_attr" label="标签属性" hidden="true"/>
		<emp:text id="msg" label="提示信息" hidden="true"/>
		<emp:text id="url" label="URL" hidden="true"/>
		<emp:text id="url_desc" label="URL说明" hidden="true"/>
		<emp:text id="is_null" label="是否不为空" dictname="STD_ZX_YES_NO" />
		<emp:text id="is_judge" label="是否自动判断" dictname="STD_ZX_YES_NO" />
		<emp:text id="rule" label="业务规则" hidden="true"/>
		<emp:text id="memo" label="备注" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_id" hidden="true" label="登记人" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_br_id" hidden="true" label="登记机构" />
	</emp:table>
	
</body>
</html>
</emp:page>
    