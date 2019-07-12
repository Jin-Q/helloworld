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
		MortBailDeliv._toForm(form);
		MortBailDelivList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateMortBailDelivPage() {
		var paramStr = MortBailDelivList._obj.getParamStr(['serno','guaranty_no','cus_id']);
		var status = MortBailDelivList._obj.getParamStr(['status']);
		if (paramStr != null) {
			if(status=="status=00"){
				var url = '<emp:url action="getMortBailDelivAddPage.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else{
				alert("已记账状态的记录不能进行修改操作！");
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewMortBailDeliv() {
		var paramStr = MortBailDelivList._obj.getParamStr(['serno','guaranty_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getMortBailDelivViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddMortBailDelivPage() {
		var paramStr = MortBailDelivList._obj.getParamStr(['serno','guaranty_no','cus_id']);
		var status = MortBailDelivList._obj.getParamStr(['status']);
		if (paramStr != null) {
			if(status=="status=00"){
				var url = '<emp:url action="getMortBailDelivAddPage.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else{
				alert("已记账状态的记录不能再次进行记账操作");
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doDeleteMortBailDeliv() {
		var paramStr = MortBailDelivList._obj.getParamStr(['serno','guaranty_no']);
		var status = MortBailDelivList._obj.getParamStr(['status']);
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
					var url = '<emp:url action="deleteMortBailDelivRecord.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);
			 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
			 		}
			    }else{
					alert("非登记状态的提货记录，不可以对其进行删除操作！");
				}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.MortBailDelivGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="MortBailDelivGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="MortBailDeliv.cus_id" label="出质人客户码" />
			<emp:text id="MortBailDeliv.guaranty_no" label="押品编号" />
			<emp:text id="MortBailDeliv.oversee_agr_no" label="监管协议编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddMortBailDelivPage" label="提货记账" op="add"/>
		<emp:button id="getUpdateMortBailDelivPage" label="修改" op="update"/>
		<emp:button id="deleteMortBailDeliv" label="删除" op="remove"/>
		<emp:button id="viewMortBailDeliv" label="查看" op="view"/>
	</div>

	<emp:table icollName="MortBailDelivList" pageMode="true" url="pageMortBailDelivQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="出质人客户码" />
		<emp:text id="cus_id_displayname" label="出质人客户名称" />
		<emp:text id="guaranty_no" label="押品编号" />
		<emp:text id="oversee_agr_no" label="监管协议编号" />
		<emp:text id="storage_total" label="库存总价值" dataType="Currency"/>
		<emp:text id="status" label="状态" dictname="STD_ZB_TALLY_STATUS" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="serno" label="业务编号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    