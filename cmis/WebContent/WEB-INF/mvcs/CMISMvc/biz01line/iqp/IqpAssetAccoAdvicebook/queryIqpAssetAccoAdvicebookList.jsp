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
		IqpAssetAccoAdvicebook._toForm(form);
		IqpAssetAccoAdvicebookList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpAssetAccoAdvicebookPage() {
		var paramStr = IqpAssetAccoAdvicebookList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetAccoAdvicebookUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAssetAccoAdvicebook() {
		var paramStr = IqpAssetAccoAdvicebookList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetAccoAdvicebookViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpAssetAccoAdvicebookPage() {
		var url = '<emp:url action="getIqpAssetAccoAdvicebookAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpAssetAccoAdvicebook() {
		var paramStr = IqpAssetAccoAdvicebookList._obj.getParamStr(['serno']);
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
							var url = '<emp:url action="queryIqpAssetAccoAdvicebookList.do"/>';
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
				var url = '<emp:url action="deleteIqpAssetAccoAdvicebookRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpAssetAccoAdvicebookGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddIqpAssetAccoAdvicebookPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpAssetAccoAdvicebookPage" label="修改" op="update"/>
		<emp:button id="deleteIqpAssetAccoAdvicebook" label="删除" op="remove"/>
		<emp:button id="viewIqpAssetAccoAdvicebook" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpAssetAccoAdvicebookList" pageMode="true" url="pageIqpAssetAccoAdvicebookQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="acco_advice" label="会计出表意见" />
		<emp:text id="advice_date" label="意见提供日期" />
		<emp:text id="advice_memo" label="会计意见摘要" />
	</emp:table>
	
</body>
</html>
</emp:page>
    