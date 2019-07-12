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
		IqpAssetTransApp._toForm(form);
		IqpAssetTransAppList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpAssetTransAppPage() {
		var paramStr = IqpAssetTransAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetTransAppUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAssetTransApp() {
		var paramStr = IqpAssetTransAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetTransAppViewPage.do"/>?op=view&isHistory=history&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpAssetTransAppPage() {
		var url = '<emp:url action="getIqpAssetTransAppAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpAssetTransApp() {
		var paramStr = IqpAssetTransAppList._obj.getParamStr(['serno']);
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
							var url = '<emp:url action="queryIqpAssetTransAppList.do"/>';
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
				var url = '<emp:url action="deleteIqpAssetTransAppRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpAssetTransAppGroup.reset();
	};
	
	function returnCusId(data){
		IqpAssetTransApp.toorg_no._setValue(data.same_org_no._getValue());
		IqpAssetTransApp.toorg_name._setValue(data.same_org_cnname._getValue());
	}	
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpAssetTransAppGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpAssetTransApp.serno" label="业务编号" />
			<emp:pop id="IqpAssetTransApp.toorg_name" label="交易对手行名" url="queryCusSameOrgForPopList.do?restrictUsed=false" returnMethod="returnCusId"/>
			<emp:select id="IqpAssetTransApp.approve_status" label="审批状态" dictname="WF_APP_STATUS"/>
			<emp:text id="IqpAssetTransApp.toorg_no" label="交易对手行号" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewIqpAssetTransApp" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpAssetTransAppList" pageMode="true" url="pageIqpAssetTransAppQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="toorg_no" label="交易对手行号" />
		<emp:text id="toorg_no_displayname" label="交易对手行名" />
		<emp:text id="trans_type" label="业务类型" dictname="STD_ZB_TRANS_TYPE"/>
		<emp:text id="trans_amt" label="转让金额" dataType="Currency"/>
		<emp:text id="trans_date" label="转让日期" />
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="manager_br_id" label="管理机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="approve_status" label="审批状态" dictname="WF_APP_STATUS"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    