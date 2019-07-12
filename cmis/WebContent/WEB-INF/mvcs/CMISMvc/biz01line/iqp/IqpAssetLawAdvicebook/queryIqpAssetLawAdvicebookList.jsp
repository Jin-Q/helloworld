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
		IqpAssetLawAdvicebook._toForm(form);
		IqpAssetLawAdvicebookList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpAssetLawAdvicebookPage() {
		var paramStr = IqpAssetLawAdvicebookList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetLawAdvicebookUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAssetLawAdvicebook() {
		var paramStr = IqpAssetLawAdvicebookList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetLawAdvicebookViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpAssetLawAdvicebookPage() {
		var url = '<emp:url action="getIqpAssetLawAdvicebookAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpAssetLawAdvicebook() {
		var paramStr = IqpAssetLawAdvicebookList._obj.getParamStr(['serno']);
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
							var url = '<emp:url action="queryIqpAssetLawAdvicebookList.do"/>';
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
				var url = '<emp:url action="deleteIqpAssetLawAdvicebookRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpAssetLawAdvicebookGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddIqpAssetLawAdvicebookPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpAssetLawAdvicebookPage" label="修改" op="update"/>
		<emp:button id="deleteIqpAssetLawAdvicebook" label="删除" op="remove"/>
		<emp:button id="viewIqpAssetLawAdvicebook" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpAssetLawAdvicebookList" pageMode="true" url="pageIqpAssetLawAdvicebookQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="law_office" label="律师事务所" />
		<emp:text id="lawer" label="律师" />
		<emp:text id="advice_date" label="意见提供日期" />
		<emp:text id="advice_memo" label="意见摘要" />
	</emp:table>
	
</body>
</html>
</emp:page>
    