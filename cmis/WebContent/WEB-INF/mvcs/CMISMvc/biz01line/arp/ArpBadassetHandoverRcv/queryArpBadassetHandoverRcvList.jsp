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
		ArpBadassetHandoverRcv._toForm(form);
		ArpBadassetHandoverRcvList._obj.ajaxQuery(null,form);
	};
	
	function doViewArpBadassetHandoverRcv() {
		var paramStr = ArpBadassetHandoverRcvList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getArpBadassetHandoverRcvViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.ArpBadassetHandoverRcvGroup.reset();
	};
	
	/*--user code begin--*/
	function doReceiveArpBadassetHandoverRcv() {
		var paramStr = ArpBadassetHandoverRcvList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var rcv_status = ArpBadassetHandoverRcvList._obj.getParamValue('rcv_status');
			if(rcv_status != "001"){
				alert("只能接收未接收的任务!");
				return;
			}
			var rcv_person = ArpBadassetHandoverRcvList._obj.getParamValue('rcv_person');
			var input_id = ArpBadassetHandoverRcvList._obj.getParamValue('input_id');
			if(rcv_person != input_id){
				alert("当前用户不是指定接收人员!");
				return;
			}
			var url = '<emp:url action="getArpBadassetHandoverRcvUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	function returnCus(data){
		ArpBadassetHandoverRcv.cus_id._setValue(data.cus_id._getValue());
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="ArpBadassetHandoverRcvGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="ArpBadassetHandoverRcv.bill_no" label="借据编号" />
			<emp:pop id="ArpBadassetHandoverRcv.cus_id" label="客户码" url="queryAllCusPop.do?returnMethod=returnCus" buttonLabel="选择" />
			<emp:select id="ArpBadassetHandoverRcv.handover_resn" label="移交原因" dictname="STD_ZB_HANDOVER_RESN" />
			<emp:select id="ArpBadassetHandoverRcv.rcv_status" label="接收状态" dictname="STD_ZB_RCV_STATUS" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewArpBadassetHandoverRcv" label="查看" op="view"/>
		<emp:button id="receiveArpBadassetHandoverRcv" label="接收" op="receive"/>
	</div>

	<emp:table icollName="ArpBadassetHandoverRcvList" pageMode="true" url="pageArpBadassetHandoverRcvQuery.do">
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="prd_type" label="产品类型" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="loan_amt" label="借据金额" dataType="Currency"/>
		<emp:text id="loan_balance" label="借据余额" dataType="Currency"/>
		<emp:text id="five_class" label="五级分类" dictname="STD_ZB_FIVE_SORT"/>
		<emp:text id="handover_resn" label="移交原因" dictname="STD_ZB_HANDOVER_RESN" />
		<emp:text id="fount_manager_id_displayname" label="原主管客户经理" />
		<emp:text id="rcv_person_displayname" label="接收人员" />
		<emp:text id="rcv_status" label="接收状态" dictname="STD_ZB_RCV_STATUS" />
		<emp:text id="rcv_person" label="接收人员" hidden="true"/>
		<emp:text id="input_id" label="登录人员" defvalue="$currentUserId" hidden="true" />
	</emp:table>
	
</body>
</html>
</emp:page>