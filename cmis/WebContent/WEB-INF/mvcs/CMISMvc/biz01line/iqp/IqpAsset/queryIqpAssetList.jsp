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
		IqpAsset._toForm(form);
		IqpAssetList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpAssetPage() {
		var paramStr = IqpAssetList._obj.getParamStr(['asset_no']);
		if (paramStr != null) {
			var status = IqpAssetList._obj.getParamValue(['status']);
			if(status!='01'){
				alert('资产包已被引用，不能进行修改！');
				return;
			}
			var url = '<emp:url action="getIqpAssetUpdatePage.do"/>?op=update&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url; 
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAsset() {
		var paramStr = IqpAssetList._obj.getParamStr(['asset_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAssetViewPage.do"/>?op=view&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpAssetPage() {
		var url = '<emp:url action="getIqpAssetAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpAsset() {
		var paramStr = IqpAssetList._obj.getParamStr(['asset_no']);
		if (paramStr != null) {
			var status = IqpAssetList._obj.getParamValue(['status']);
			if(status!='01'){
				alert('资产包已被引用，不能进行删除！');
				return;
			}
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
							alert("删除成功！");
							window.location.reload();
						}else {
							alert("删除失败！");
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
				var url = '<emp:url action="deleteIqpAssetRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null) 
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpAssetGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpAssetGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpAsset.asset_no" label="资产包编号" />
			<emp:text id="IqpAsset.asset_name" label="资产包名称" />
			<emp:select id="IqpAsset.asset_type" label="资产类型" required="false" dictname="STD_ZB_ASSET_TYPE"/>
			<emp:select id="IqpAsset.status" label="资产包状态" dictname="STD_ZB_ASSET_STATUS"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpAssetPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpAssetPage" label="修改" op="update"/>
		<emp:button id="deleteIqpAsset" label="删除" op="remove"/>
		<emp:button id="viewIqpAsset" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpAssetList" pageMode="true" url="pageIqpAssetQuery.do">
		<emp:text id="asset_no" label="资产包编号" />
		<emp:text id="asset_name" label="资产包名称" />
		<emp:text id="asset_type" label="资产类型" required="false" dictname="STD_ZB_ASSET_TYPE"/>
		<emp:text id="takeover_type" label="转让方式" required="false" dictname="STD_ZB_TAKEOVER_MODE"/>
		<emp:text id="asset_qnt" label="资产数量" dataType="Int"/>
		<emp:text id="asset_total_amt" label="资产总额" dataType="Currency"/>
		<emp:text id="takeover_total_amt" label="转让总额" dataType="Currency"/>
		<emp:text id="status" label="资产包状态" dictname="STD_ZB_ASSET_STATUS"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    