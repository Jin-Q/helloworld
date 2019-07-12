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
		CtrAssetTransCont._toForm(form);
		CtrAssetTransContList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCtrAssetTransContPage() {
		var paramStr = CtrAssetTransContList._obj.getParamStr(['cont_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getCtrAssetTransContUpdatePage.do"/>?op=update&cont=cont&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCtrAssetTransCont() {
		var paramStr = CtrAssetTransContList._obj.getParamStr(['cont_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getCtrAssetTransContViewPage.do"/>?op=view&isHistory=history&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCtrAssetTransContPage() {
		var url = '<emp:url action="getCtrAssetTransContAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCtrAssetTransCont() {
		var paramStr = CtrAssetTransContList._obj.getParamStr(['cont_no']);
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
							var url = '<emp:url action="queryCtrAssetTransContList.do"/>';
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
				var url = '<emp:url action="deleteCtrAssetTransContRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doDeliverAsset() {
		var paramStr = CtrAssetTransContList._obj.getParamStr(['cont_no']);
		if (paramStr != null) {
			if(confirm("是否确认要资产交割？")){
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
							alert("资产交割成功!");
							var url = '<emp:url action="queryCtrAssetTransContHistoryList.do"/>';
							url = EMPTools.encodeURI(url);
							window.location = url;
						}else {
							alert("资产交割异常!");
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
				var url = '<emp:url action="deliverAssetRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CtrAssetTransContGroup.reset();
	};
	
	function returnCusId(data){
		CtrAssetTransCont.toorg_no._setValue(data.same_org_no._getValue());
		CtrAssetTransCont.toorg_name._setValue(data.same_org_cnname._getValue());
	}
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="CtrAssetTransContGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CtrAssetTransCont.cont_no" label="合同编号" />
			<emp:text id="CtrAssetTransCont.serno" label="业务编号" />
			<emp:pop id="CtrAssetTransCont.toorg_name" label="交易对手行名" url="queryCusSameOrgForPopList.do?restrictUsed=false" returnMethod="returnCusId"/>
			<emp:select id="CtrAssetTransCont.cont_status" label="项目状态" dictname="ZB_BIZ_CONT_STATUS"/>
			<emp:text id="CtrAssetTransCont.toorg_no" label="交易对手行号" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="deliverAsset" label="资产交割" op="deliver"/>
		<emp:button id="viewCtrAssetTransCont" label="查看" op="view"/>
	</div>

	<emp:table icollName="CtrAssetTransContList" pageMode="true" url="pageCtrAssetTransContQuery.do">
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="serno" label="业务编号" />
		<emp:text id="toorg_no" label="交易对手" />
		<emp:text id="toorg_no_displayname" label="交易对手行名" />
		<emp:text id="trans_type" label="业务类型" dictname="STD_ZB_TRANS_TYPE"/>
		<emp:text id="trans_amt" label="转让金额" dataType="Currency"/>
		<emp:text id="trans_date" label="转让日期" />
		<emp:text id="deliver_date" label="交割日" />
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="manager_br_id" label="管理机构" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id" label="登记机构" />
		<emp:text id="cont_status" label="项目状态" dictname="ZB_BIZ_CONT_STATUS"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    