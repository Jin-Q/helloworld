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
<script src="<emp:file fileName='scripts/jquery/jquery-1.4.4.min.js'/>" type="text/javascript" language="javascript"></script>
<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryMortCargoStorageList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function doLoad(){
		doChange();

		var agr_type = MortCargoStorage.agr_type._getValue();
		var labelName = '';
		if(agr_type=='00'){
			labelName = '保兑仓协议编号';
		}else if(agr_type=='01'){
			labelName = '银企商合作协议编号';
		}else{
			labelName = '监管协议编号';
		}
		$(document).ready(function(){
			$(".emp_field_label:eq(1)").text(labelName);
		});
	}
	//根据入库方式的不同，页面显示不同的字段
	function doChange(){
		var flag =MortCargoStorage.storage_mode._getValue();
		if(flag=="00"){
			MortCargoStorage.need_reple_total._obj._renderHidden(true);	
			MortCargoStorage.act_reple_total._obj._renderHidden(true);	
			MortCargoStorage.need_reple_total._setValue("");
			MortCargoStorage.act_reple_total._setValue("");
		}else if(flag=="01"){
			MortCargoStorage.need_reple_total._obj._renderHidden(false);	
			MortCargoStorage.act_reple_total._obj._renderHidden(false);	
		}
	}
	//货物记录查看事件
	function doViewMortCargoPledge() {
		var data = MortDelivListHisList._obj.getSelectedData();
		if (data.length ==1) {
			var serno = data[0].serno._getValue();
			var cargo_id = data[0].cargo_id._getValue();
			var url = '<emp:url action="getMortCargoPledgeViewPage.do"/>?flag=tab&cargo_id='+cargo_id+'&serno='+serno;//flag标志非押品维护时的货物操作（tab）
			url = EMPTools.encodeURI(url);
	      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:gridLayout id="MortCargoStorageGroup" title="货物入库管理" maxColumn="2">
			<emp:text id="MortCargoStorage.guaranty_no" label="押品编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="MortCargoStorage.oversee_agr_no" label="监管协议编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="MortCargoStorage.serno" label="业务编号" maxlength="60" required="false" hidden="true"/>
			<emp:text id="MortCargoStorage.cus_id" label="客户码" maxlength="40" required="true" readonly="true"/>
			<emp:text id="MortCargoStorage.cus_id_displayname" label="出质人客户名称" required="true" readonly="true" cssElementClass="emp_field_text_readonly" colSpan="2"/> 
			<emp:select id="MortCargoStorage.storage_mode" label="入库方式" required="true" dictname="STD_ZB_STORAGE_MODE" onblur="doChange()" defvalue="00" colSpan="2"/>
			<emp:text id="MortCargoStorage.storage_total" label="库存总价值" maxlength="18" required="false" dataType="Currency" readonly="true" defvalue="0" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="MortCargoStorage.need_reple_total" label="需补货总价值" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="MortCargoStorage.act_reple_total" label="实际补货总价值" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="MortCargoStorage.after_storage_total" label="入库后总价值" maxlength="18" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			
			<emp:date id="MortCargoStorage.tally_date" label="记账日期" required="false" readonly="true"/>
			<emp:select id="MortCargoStorage.status" label="状态" required="false" dictname="STD_ZB_TALLY_STATUS" readonly="true" defvalue="00"/>
			<emp:textarea id="MortCargoStorage.memo" label="备注" maxlength="200" required="false" colSpan="2" />
		</emp:gridLayout>
		<emp:gridLayout id="MortCargoStorageGroup" title="登记信息" maxColumn="2">
			<emp:text id="MortCargoStorage.input_id" label="登记人" maxlength="20" required="false" readonly="true" hidden="true"/>
			<emp:text id="MortCargoStorage.input_br_id" label="登记机构" required="false" readonly="true" hidden="true"/>
			<emp:text id="MortCargoStorage.input_id_displayname" label="登记人"  required="false" readonly="true"/>
			<emp:text id="MortCargoStorage.input_br_id_displayname" label="登记机构" required="false" readonly="true"/>
			<emp:date id="MortCargoStorage.input_date" label="登记日期" required="false" defvalue="$OPENDAY" readonly="true"/>
			<emp:text id="MortCargoStorage.agr_type" label="协议类型" required="false" readonly="true" hidden="true"/>
		</emp:gridLayout>
		
		<emp:button id="viewMortCargoPledge" label="查看" op="view"/>
		<emp:table icollName="MortDelivListHisList" pageMode="true" url="pageMortCargoPledgeQuery.do?status=rk&guarantyNo=${context.MortCargoStorage.guaranty_no}" selectType="2">
			<emp:text id="serno" label="业务编号" hidden="true"/>
			<emp:text id="cargo_id" label="货物编号" />
			<emp:text id="guaranty_catalog" label="押品所处目录" hidden="true"/>
			<emp:text id="guaranty_catalog_displayname" label="押品所处目录" />
			<emp:text id="cargo_name" label="货物名称" />
			<emp:text id="identy_total" label="银行认定总价" dataType="Currency"/>
			<emp:text id="cargo_status" label="状态" dictname="STD_CARGO_STATUS" />
			<emp:text id="reg_date" label="登记日期" />
		</emp:table>
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
	
</body>
</html>
</emp:page>
