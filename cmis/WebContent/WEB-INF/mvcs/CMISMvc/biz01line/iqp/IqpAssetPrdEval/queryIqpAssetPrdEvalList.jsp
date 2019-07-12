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
		IqpAssetPrdEval._toForm(form);
		IqpAssetPrdEvalList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpAssetPrdEvalPage() {
		var paramStr = IqpAssetPrdEvalList._obj.getParamStr(['prd_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetPrdEvalUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAssetPrdEval() {
		var paramStr = IqpAssetPrdEvalList._obj.getParamStr(['prd_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetPrdEvalViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpAssetPrdEvalPage() {
		var url = '<emp:url action="getIqpAssetPrdEvalAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpAssetPrdEval() {
		var paramStr = IqpAssetPrdEvalList._obj.getParamStr(['prd_id']);
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
							var url = '<emp:url action="queryIqpAssetPrdEvalList.do"/>';
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
				var url = '<emp:url action="deleteIqpAssetPrdEvalRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpAssetPrdEvalGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddIqpAssetPrdEvalPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpAssetPrdEvalPage" label="修改" op="update"/>
		<emp:button id="deleteIqpAssetPrdEval" label="删除" op="remove"/>
		<emp:button id="viewIqpAssetPrdEval" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpAssetPrdEvalList" pageMode="true" url="pageIqpAssetPrdEvalQuery.do">
		<emp:text id="prd_id" label="产品代码" />
		<emp:text id="serno" label="业务编号" />
		<emp:text id="eval_org" label="评级机构" />
		<emp:text id="cdt_eval" label="信用评级" />
		<emp:text id="eval_date" label="评级日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    