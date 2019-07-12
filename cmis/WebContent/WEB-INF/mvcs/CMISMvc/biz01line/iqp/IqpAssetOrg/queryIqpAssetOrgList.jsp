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
		IqpAssetOrg._toForm(form);
		IqpAssetOrgList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpAssetOrgPage() {
		var paramStr = IqpAssetOrgList._obj.getParamStr(['asset_org_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetOrgUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAssetOrg() {
		var paramStr = IqpAssetOrgList._obj.getParamStr(['asset_org_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetOrgViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpAssetOrgPage() {
		var url = '<emp:url action="getIqpAssetOrgAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpAssetOrg() {
		var paramStr = IqpAssetOrgList._obj.getParamStr(['asset_org_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr1 define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						if(flag == "success"){
							alert("删除成功!");
							var url = '<emp:url action="queryIqpAssetOrgList.do"/>';
							url = EMPTools.encodeURI(url);
							window.location = url;
						}else {
							alert("删除异常!");
						}
					}
				};
				var handleFailure = function(o){
					alert("异步请求出错！");
				};
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				};
				var url = '<emp:url action="deleteIqpAssetOrgRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpAssetOrgGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpAssetOrgGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpAssetOrg.asset_org_id" label="机构代码" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpAssetOrgPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpAssetOrgPage" label="修改" op="update"/>
		<emp:button id="deleteIqpAssetOrg" label="删除" op="remove"/>
		<emp:button id="viewIqpAssetOrg" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpAssetOrgList" pageMode="true" url="pageIqpAssetOrgQuery.do">
		<emp:text id="asset_org_id" label="机构代码" />
		<emp:text id="asset_org_name" label="机构名称" />
		<emp:text id="org_nature" label="机构性质" dictname="STD_ORG_QLTY"/>
		<emp:text id="org_repr" label="法人代表" />
		<emp:text id="phone" label="联系电话" />
	</emp:table>
	
</body>
</html>
</emp:page>
    