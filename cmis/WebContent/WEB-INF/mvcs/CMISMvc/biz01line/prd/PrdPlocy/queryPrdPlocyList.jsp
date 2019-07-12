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
		PrdPlocy._toForm(form);
		PrdPlocyList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePrdPlocyPage() {
		var paramStr = PrdPlocyList._obj.getParamStr(['schemecode']);
		if (paramStr != null) {
			var url = '<emp:url action="getPrdPlocyUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPrdPlocy() {
		var paramStr = PrdPlocyList._obj.getParamStr(['schemecode']);
		if (paramStr != null) {
			var url = '<emp:url action="getPrdPlocyViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPrdPlocyPage() {
		var url = '<emp:url action="getPrdPlocyAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePrdPlocy() {
		var paramStr = PrdPlocyList._obj.getParamStr(['schemecode']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deletePrdPlocyRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PrdPlocyGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PrdPlocyGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PrdPlocy.schemecode" label="政策资料代码" />
			<emp:select id="PrdPlocy.ifwarrant" label="是否权证类" dictname="STD_ZX_YES_NO" />
			<emp:select id="PrdPlocy.schemetype" label="政策资料类型" dictname="STD_ZB_INFO_TYPE" />
			<emp:text id="PrdPlocy.inputid" label="登记人员" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddPrdPlocyPage" label="新增" op="add"/>
		<emp:button id="getUpdatePrdPlocyPage" label="修改" op="update"/>
		<emp:button id="deletePrdPlocy" label="删除" op="remove"/>
		<emp:button id="viewPrdPlocy" label="查看" op="view"/>
	</div>

	<emp:table icollName="PrdPlocyList" pageMode="true" url="pagePrdPlocyQuery.do">
		<emp:text id="schemecode" label="政策资料代码" />
		<emp:text id="schemedesc" label="政策资料描述" />
		<emp:text id="ifwarrant" label="是否权证类" dictname="STD_ZX_YES_NO" />
		<emp:text id="schemetype" label="政策资料类型" dictname="STD_ZB_INFO_TYPE"/>
		<emp:text id="outline" label="外部链接" />
		<emp:text id="videocode" label="影像关联代码" />
		<emp:text id="comments" label="备注" />
		<emp:text id="inputid" label="登记人员" hidden="true"/>
		<emp:text id="inputid_displayname" label="登记人员" />
		<emp:text id="inputdate" label="登记日期" />
		<emp:text id="orgid" label="登记机构" hidden="true"/>
		<emp:text id="orgid_displayname" label="登记机构" />
	</emp:table>
	
</body>
</html>
</emp:page>
    