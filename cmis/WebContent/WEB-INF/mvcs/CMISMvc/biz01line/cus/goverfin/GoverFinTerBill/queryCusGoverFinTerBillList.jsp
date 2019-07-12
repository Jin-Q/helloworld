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
		CusGoverFinTerBill._toForm(form);
		CusGoverFinTerBillList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusGoverFinTerBillPage() {
		var paramStr = CusGoverFinTerBillList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			paramStr = paramStr + '&cus_id='+cus_id+'&manager_id='+manager_id+'&manager_br_id='+manager_br_id;
			var url = '<emp:url action="getCusGoverFinTerBillUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusGoverFinTerBill() {
		var paramStr = CusGoverFinTerBillList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			paramStr = paramStr + '&cus_id='+cus_id+'&manager_id='+manager_id+'&manager_br_id='
			+manager_br_id+'&ops='+ops;
			var url = '<emp:url action="getCusGoverFinTerBillViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusGoverFinTerBillPage() {
		var url = '<emp:url action="getCusGoverFinTerBillAddPage.do"/>?cus_id='+cus_id+
		'&manager_id='+manager_id+'&manager_br_id='+manager_br_id;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusGoverFinTerBill() {
		var paramStr = CusGoverFinTerBillList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusGoverFinTerBillRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				doDeleteRecord(url);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CusGoverFinTerBillGroup.reset();
	};
	
	/*--user code begin--*/
	function doOnload(){
		cus_id = "${context.cus_id}";
		manager_id = "${context.manager_id}";
		manager_br_id = "${context.manager_br_id}";
		ops = "${context.ops}";
		if(ops == 'view'){
			document.getElementById('button_getAddCusGoverFinTerBillPage').style.display = 'none';
			document.getElementById('button_getUpdateCusGoverFinTerBillPage').style.display = 'none';
			document.getElementById('button_deleteCusGoverFinTerBill').style.display = 'none';
		}
	};
	function doDeleteRecord(url){
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var operMsg = jsonstr.operMsg;
				if(operMsg=='1'){
		            alert('删除成功!');
		            window.location.reload();
				}else if(operMsg=='2'){
					alert('删除失败!');
				}
			}
		};
		var handleFailure = function(o) {
			alert("保存失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};

		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnload()">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddCusGoverFinTerBillPage" label="新增" op="add"/>
		<emp:button id="getUpdateCusGoverFinTerBillPage" label="修改" op="update"/>
		<emp:button id="deleteCusGoverFinTerBill" label="删除" op="remove"/>
		<emp:button id="viewCusGoverFinTerBill" label="查看" op="view"/>
	</div>

	<emp:table icollName="CusGoverFinTerBillList" pageMode="true" url="pageCusGoverFinTerBillQuery.do?cus_id=${context.cus_id}">
		<emp:text id="serno" label="申请流水号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="loan_amount" label="借款金额(元)" dataType="Currency"/>
		<emp:text id="repayment_mode_displayname" label="还款方式" dictname="STD_ZB_REPAYMENT_MODE" />
		<emp:text id="rfn_ori" label="还款来源" dictname="STD_ZB_RFN_ORI" />
	</emp:table>
	
</body>
</html>
</emp:page>
    