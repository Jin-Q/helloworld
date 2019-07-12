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
		ArpDispReclaimInfo._toForm(form);
		ArpDispReclaimInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateArpDispReclaimInfoPage() {
		var paramStr = ArpDispReclaimInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpDispReclaimInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewArpDispReclaimInfo() {
		var paramStr = ArpDispReclaimInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpDispReclaimInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddArpDispReclaimInfoPage() {
		var url = '<emp:url action="getArpDispReclaimInfoAddPage.do"/>?asset_disp_no=${context.asset_disp_no}&guaranty_no=${context.guaranty_no}';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteArpDispReclaimInfo() {
		var paramStr = ArpDispReclaimInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o) {
					if (o.responseText !== undefined) {
						try {
							var jsonstr = eval("(" + o.responseText + ")");
						} catch (e) {
							alert("Parse jsonstr define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						if("success" == flag){
							alert("已成功删除！");
							window.location.reload();
						}else{
							alert("删除失败！");
						}
					}
				};
				var handleFailure = function(o) {
				};
				var callback = {
					success :handleSuccess,
					failure :handleFailure
				};
				var url = '<emp:url action="deleteArpDispReclaimInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.ArpDispReclaimInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<div align="left">
		<emp:actButton id="getAddArpDispReclaimInfoPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateArpDispReclaimInfoPage" label="修改" op="update"/>
		<emp:actButton id="deleteArpDispReclaimInfo" label="删除" op="remove"/>
		<emp:actButton id="viewArpDispReclaimInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="ArpDispReclaimInfoList" pageMode="true" url="pageArpDispReclaimInfoQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="guaranty_no" label="抵债资产编号" />
		<emp:text id="is_cash" label="是否现金" dictname="STD_ZX_YES_NO" />
		<emp:text id="disp_amt" label="处置金额" dataType="Currency"/>		
		<emp:text id="disp_date" label="处置日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    