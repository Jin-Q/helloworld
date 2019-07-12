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
		MortCargoStorage._toForm(form);
		MortCargoStorageList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateMortCargoStoragePage() {
		var paramStr = MortCargoStorageList._obj.getParamStr(['guaranty_no','cus_id','serno']);
		if (paramStr != null) {
			var status = MortCargoStorageList._obj.getParamStr(['status']);
			if(status=="status=01"){
				alert("此笔业务已记账入库不能进行修改操作！");
			}else{
				var url = '<emp:url action="getMortCargoStorageAddPage.do"/>?op=update&'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewMortCargoStorage() {
		var paramStr = MortCargoStorageList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getMortCargoStorageViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddMortCargoStoragePage() {
		
		var paramStr = MortCargoStorageList._obj.getParamStr(['guaranty_no','cus_id','serno']);
		if (paramStr != null) {
			var status = MortCargoStorageList._obj.getParamStr(['status']);
			if(status=="status=01"){
				alert("此笔业务已完成入库操作不能进行再次入库！");
			}else{
				var url = '<emp:url action="getMortCargoStorageAddPage.do"/>?op=to_storage&'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doDeleteMortCargoStorage() {
		var paramStr = MortCargoStorageList._obj.getParamStr(['serno','guaranty_no']);
		var status = MortCargoStorageList._obj.getParamStr(['status']);
		if (paramStr != null) {
			if(status=="status=00"){
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
							alert("记录已删除！")
							window.location.reload();
						}else{
							alert("记录删除失败！");
						}
					}
				};
				var handleFailure = function(o) {
				};
				var callback = {
					success :handleSuccess,
					failure :handleFailure
				};
				var url = '<emp:url action="deleteMortCargoStorageRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
		 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
			  }
			}else{
				alert("非登记状态下的入库记录，不可以对其进行删除操作！");
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.MortCargoStorageGroup.reset();
	};
	function doPrint(){
		var paramStr = MortCargoStorageList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var serno  = MortCargoStorageList._obj.getParamValue(['serno']);
			var url = '<emp:url action="getReport2ShowPage.do"/>&reportId=MortStor/yprkzywqd.raq&serno='+serno;

			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		}else{
			alert('请先选择一条记录！');
		}
	}
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="MortCargoStorageGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="MortCargoStorage.serno" label="业务编号" />
			<emp:text id="MortCargoStorage.cus_id" label="客户码" />
			<emp:text id="MortCargoStorage.guaranty_no" label="押品编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddMortCargoStoragePage" label="入库" op="to_storage"/>
		<emp:button id="getUpdateMortCargoStoragePage" label="修改" op="update"/>
        <emp:button id="deleteMortCargoStorage" label="删除" op="remove"/>
		<emp:button id="viewMortCargoStorage" label="查看" op="view"/>
		<emp:button id="print" label="打印质押物清单" op="print" mousedownCss="button100" mouseoutCss="button100" mouseoverCss="button100" mouseupCss="button100"/>
	</div>

	<emp:table icollName="MortCargoStorageList" pageMode="true" url="pageMortCargoStorageQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="guaranty_no" label="押品编号" />
		<emp:text id="oversee_agr_no" label="监管协议编号" />
		<emp:text id="storage_total" label="库存总价值" />
		<emp:text id="status" label="状态" dictname="STD_ZB_TALLY_STATUS" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_date" label="登记日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    