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
		MortCargoExware._toForm(form);
		MortCargoExwareList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateMortCargoExwarePage() {
		var paramStr = MortCargoExwareList._obj.getParamStr(['guaranty_no','cus_id','serno']);
		if (paramStr != null) {
			var status = MortCargoExwareList._obj.getParamStr(['status']);
			if(status=="status=01"){
				alert("此笔业务已完成记账出库操作，不能对其进行修改！");
			}else{
				var url = '<emp:url action="getMortCargoExwareAddPage.do"/>?op=update&'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		}else{
			alert('请先选择一条记录！');
		}
	};
	
	function doViewMortCargoExware() {
		var paramStr = MortCargoExwareList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getMortCargoExwareViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddMortCargoExwarePage() {
		var url = '<emp:url action="getMortCargoExwareAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteMortCargoExwarePage() {
		var paramStr = MortCargoExwareList._obj.getParamStr(['serno','guaranty_no']);
		var status = MortCargoExwareList._obj.getParamStr(['status']);
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
					var url = '<emp:url action="deleteMortCargoExwareRecord.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);
			 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
				}
			}else{
					alert("非登记状态的出库记录，不可以对其进行删除操作！");
		    }
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.MortCargoExwareGroup.reset();
	};
	
	function doOutStorage(){
		var paramStr = MortCargoExwareList._obj.getParamStr(['guaranty_no','cus_id','serno']);
		if (paramStr != null) {
			var status = MortCargoExwareList._obj.getParamStr(['status']);
			if(status=="status=01"){
				alert("此笔业务已完成出库操作不能进行再次出库！");
			}else{
				var url = '<emp:url action="getMortCargoExwareAddPage.do"/>?op=out_storage&'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		}else{
			alert('请先选择一条记录！');
		}
	}
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="MortCargoExwareGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="MortCargoExware.serno" label="业务编号" />
			<emp:text id="MortCargoExware.cus_id" label="客户码" />
			<emp:text id="MortCargoExware.guaranty_no" label="押品编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="outStorage" label="出库" op="out_storage"/>
		<emp:button id="getUpdateMortCargoExwarePage" label="修改" op="update"/>
		<emp:button id="deleteMortCargoExwarePage" label="删除" op="remove"/>
		<emp:button id="viewMortCargoExware" label="查看" op="view"/>
	</div>

	<emp:table icollName="MortCargoExwareList" pageMode="true" url="pageMortCargoExwareQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="oversee_agr_no" label="监管协议编号" />
		<emp:text id="guaranty_no" label="押品编号" />
		<emp:text id="storage_total" label="库存总价值" dataType="Currency"/>
		<emp:text id="status" label="状态" dictname="STD_ZB_TALLY_STATUS" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_date" label="登记日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    