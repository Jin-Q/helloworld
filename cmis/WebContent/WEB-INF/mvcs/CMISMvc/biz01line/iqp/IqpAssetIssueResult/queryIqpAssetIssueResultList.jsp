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
		IqpAssetIssueResult._toForm(form);
		IqpAssetIssueResultList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpAssetIssueResultPage() {
		var paramStr = IqpAssetIssueResultList._obj.getParamStr(['serno','prd_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetIssueResultUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAssetIssueResult() {
		var paramStr = IqpAssetIssueResultList._obj.getParamStr(['serno','prd_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetIssueResultViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpAssetIssueResultPage() {
		var url = '<emp:url action="getIqpAssetIssueResultAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpAssetIssueResult() {
		var paramStr = IqpAssetIssueResultList._obj.getParamStr(['serno','prd_id']);
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
							var url = '<emp:url action="queryIqpAssetIssueResultList.do"/>';
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
				var url = '<emp:url action="deleteIqpAssetIssueResultRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpAssetIssueResultGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:actButton id="getUpdateIqpAssetIssueResultPage" label="结果登记" op="update"/>
		<emp:actButton id="viewIqpAssetIssueResult" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpAssetIssueResultList" pageMode="true" url="pageIqpAssetIssueResultQuery.do">
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="prd_id" label="产品代码" />
		<emp:text id="prd_id_displayname" label="产品名称" />
		<emp:text id="bidding_dest" label="招标标的" />
		<emp:text id="bidding_totl_amt" label="招标总量（万元）" dataType="Currency"/>
		<emp:text id="act_issue_date" label="实际发行首日" />
		<emp:text id="end_date" label="法定到期日" />
		<emp:text id="base_date" label="基准日（起息日）" />
	</emp:table>
	
</body>
</html>
</emp:page>
    