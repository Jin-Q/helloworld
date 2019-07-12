<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryMortBailDelivList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	//质押物清单下查看按钮方法
	function doViewMortCargoPledge() {
		var data = MortCargoPledgeNewList._obj.getSelectedData();
		if (data.length ==1) {
			var cargo_id = data[0].cargo_id._getValue();
			var tally_date = MortBailDeliv.tally_date._getValue();
			var url = '<emp:url action="getMortCargoPledgeViewPage.do"/>?flag=tab&cargo_id='+cargo_id+'&exware_date='+tally_date;//flag标志非押品维护时的货物操作（tab）
			url = EMPTools.encodeURI(url);
	      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};
	
</script>
</head>
<body class="page_content">
		<emp:tabGroup id="MortCargoReplTab" mainTab="appinf">
   		<emp:tab label="基本信息" id="appinf"  needFlush="true" initial="true" >
		
		<emp:gridLayout id="MortBailDelivGroup" title="保证金提货" maxColumn="2">
			<emp:text id="MortBailDeliv.serno" label="提货流水号" maxlength="60" required="true" readonly="true"/>
			<emp:text id="MortBailDeliv.guaranty_no" label="押品编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="MortBailDeliv.cus_id" label="出质人客户码" maxlength="40" required="true" readonly="true"/>
			<emp:text id="MortBailDeliv.oversee_agr_no" label="监管协议编号" maxlength="40" required="true" readonly="true"/>
			<emp:text id="MortBailDeliv.cus_id_displayname" label="出质人客户名称"  required="true" cssElementClass="emp_field_text_readonly" colSpan="2"/>
			<emp:select id="MortBailDeliv.repay_receipt_type" label="还款凭证类型" required="true" dictname="STD_REPAY_LIST_TYPE" />
			<emp:text id="MortBailDeliv.receipt_serno" label="凭证流水号" maxlength="40" required="true" />
			<emp:text id="MortBailDeliv.repay_amt" label="还款金额" maxlength="20" required="true" dataType="Double" onchange="doChange()"/>
			<emp:text id="MortBailDeliv.storage_total" label="库存总价值" maxlength="18" required="false" dataType="Currency" readonly="true"/>
			<emp:text id="MortBailDeliv.deliv_total" label="提货总价值" maxlength="18" required="false" dataType="Currency" readonly="true" defvalue="0"/>
			<emp:text id="MortBailDeliv.surplus_total" label="剩余总价值" maxlength="18" required="false" dataType="Currency" readonly="true"/>
			<emp:date id="MortBailDeliv.tally_date" label="记账日期" required="false" readonly="true"/>
			<emp:select id="MortBailDeliv.status" label="状态" required="false" dictname="STD_ZB_TALLY_STATUS" readonly="true" defvalue="00"/>
			<emp:textarea id="MortBailDeliv.memo" label="备注" maxlength="200" required="false" colSpan="2" />	
		</emp:gridLayout>
		<emp:gridLayout id="MortBailDelivGroup" title="登记信息" maxColumn="2">		
			<emp:text id="MortBailDeliv.input_id" label="登记人" maxlength="20" required="false" readonly="true" defvalue="$currentUserId" hidden="true"/>
			<emp:text id="MortBailDeliv.input_br_id" label="登记机构" maxlength="20" required="false" readonly="true" defvalue="$organNo" hidden="true"/>
			<emp:text id="MortBailDeliv.input_id_displayname" label="登记人" required="false" readonly="true"/>
			<emp:text id="MortBailDeliv.input_br_id_displayname" label="登记机构" required="false" readonly="true"/>
			<emp:date id="MortBailDeliv.input_date" label="登记日期" required="false" readonly="true" defvalue="$OPENDAY"/>
		</emp:gridLayout>
	<div class='emp_gridlayout_title'>货物清单</div>
		<div align="left">
			<emp:button id="viewMortCargoPledge" label="查看" op="view"/>
		</div>
	<emp:table icollName="MortCargoPledgeNewList" pageMode="false" url="" selectType="2">
		<emp:text id="cargo_id" label="货物编号" readonly="true"/>
		<emp:text id="guaranty_catalog" label="押品所处目录" readonly="true"/>
		<emp:text id="guaranty_catalog_displayname" label="押品所处目录名称" readonly="true"/>
		<emp:text id="qnt" label="在库数量" readonly="true"/>
		<emp:text id="identy_unit_price" label="单价" readonly="true" dataType="Currency"/>
		<emp:text id="identy_total" label="在库总价" readonly="true" dataType="Currency"/>
		<emp:text id="deliv_qnt" label="提货数量" dataType="Double" cssElementClass="emp_text3" onchange="doCacul()" defvalue="0" required="true" flat="true"/>
		<emp:text id="deliv_value" label="提货价值" readonly="true" dataType="Currency" defvalue="0"/>
		<emp:text id="surplus_qnt" label="剩余数量" readonly="true" dataType="Currency" defvalue="0"/>
		<emp:text id="surplus_value" label="剩余价值" readonly="true" dataType="Currency" defvalue="0"/>
		<emp:text id="cargo_status" label="状态" dictname="STD_CARGO_STATUS" />
	</emp:table>
	</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
