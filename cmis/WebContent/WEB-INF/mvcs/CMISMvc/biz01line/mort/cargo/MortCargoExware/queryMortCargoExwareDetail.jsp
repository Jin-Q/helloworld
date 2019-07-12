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
		var url = '<emp:url action="queryMortCargoExwareList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	//货物记录查看事件
	function doViewMortCargoPledge() {
		var data = MortDelivListHisList._obj.getSelectedData();
		if (data.length==1) {
			var cargo_id = data[0].cargo_id._getValue();
			var tally_date = MortCargoExware.tally_date._getValue();
			var url = '<emp:url action="getMortCargoPledgeViewPage.do"/>?flag=tab&cargo_id='+cargo_id+'&exware_date='+tally_date;//flag标志非押品维护时的货物操作（tab）
			url = EMPTools.encodeURI(url);
	      	window.open(url,'newwindow','height=538,width=1024,top=70,left=50,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
			} else {
			alert('请先选择一条记录！');
		}
	};
	/*--user code begin--*/
	function doLoad(){
		var agr_type = MortCargoExware.agr_type._getValue();
		var labelName = '';
		if(agr_type=='00'){
			labelName = '保兑仓协议编号';
		}else if(agr_type=='01'){
			labelName = '银企商合作协议编号';
		}else{
			labelName = '监管协议编号';
		}
		$(document).ready(function(){
			$(".emp_field_label:eq(2)").text(labelName);
		});
	}	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:gridLayout id="MortCargoExwareGroup" title="货物出库管理" maxColumn="2">
			<emp:text id="MortCargoExware.serno" label="业务编号" maxlength="60" required="false" hidden="true"/>
			<emp:text id="MortCargoExware.guaranty_no" label="押品编号" maxlength="40" required="true" readonly="true"/>
			<emp:text id="MortCargoExware.oversee_agr_no" label="监管协议编号" maxlength="40" required="true" readonly="true"/>
			<emp:text id="MortCargoExware.cus_id" label="客户码" maxlength="40" required="true" readonly="true"/>
			<emp:text id="MortCargoExware.cus_id_displayname" label="出质人客户名称" required="true" cssElementClass="emp_field_text_readonly" colSpan="2"/>
			<emp:text id="MortCargoExware.storage_total" label="库存总价值" maxlength="18" required="false" dataType="Currency" readonly="true"/>
			<emp:text id="MortCargoExware.this_exware_total" label="此次出库总价值" maxlength="18" required="false" dataType="Currency" readonly="true"/>
			<emp:text id="MortCargoExware.exware_total" label="出库后总价值" maxlength="18" required="false" dataType="Currency" readonly="true" colSpan="2"/>
			<emp:date id="MortCargoExware.tally_date" label="记账日期" required="false" readonly="true"/>
			<emp:select id="MortCargoExware.status" label="状态" required="false" dictname="STD_ZB_TALLY_STATUS" readonly="true" defvalue="00"/>
			<emp:textarea id="MortCargoExware.memo" label="备注" maxlength="200" required="false" colSpan="2" />
		</emp:gridLayout>
		<emp:gridLayout id="MortCargoExwareGroup" title="登记信息" maxColumn="2">
			<emp:text id="MortCargoExware.input_id" label="登记人" maxlength="20" required="false" readonly="true" hidden="true"/>
			<emp:text id="MortCargoExware.input_br_id" label="登记机构" maxlength="20" required="false" readonly="true" hidden="true"/>
			<emp:text id="MortCargoExware.input_id_displayname" label="登记人" required="false" readonly="true"/>
			<emp:text id="MortCargoExware.input_br_id_displayname" label="登记机构"  required="false" readonly="true"/>
			<emp:date id="MortCargoExware.input_date" label="登记日期" required="false" defvalue="$OPENDAY" readonly="true"/>
			<emp:text id="MortCargoExware.agr_type" label="协议类型" required="false" readonly="true" hidden="true"/>
		</emp:gridLayout>
		<emp:button id="viewMortCargoPledge" label="查看" op="view"/>
		<emp:table icollName="MortDelivListHisList" pageMode="false" url="" selectType="2">
			<emp:text id="cargo_id" label="货物编号" />
			<emp:text id="guaranty_catalog" label="押品所处目录" hidden="true"/>
			<emp:text id="guaranty_catalog_displayname" label="押品所处目录" />
			<emp:text id="cargo_name" label="货物名称" />
			<emp:text id="storage_qnt" label="在库数量" readonly="true" dataType="Currency"/>
			<emp:text id="storage_value" label="银行认定总价" dataType="Currency"/>
			
			<emp:text id="deliv_qnt" label="出库数量" dataType="Double" onblur="doCacul()" defvalue="0" required="false" />
			<emp:text id="deliv_value" label="出库价值" readonly="true" dataType="Currency"  defvalue="0"/>
			<emp:text id="surplus_qnt" label="剩余数量" readonly="true" dataType="Currency"  defvalue="0"/>
			<emp:text id="surplus_value" label="剩余价值" readonly="true" dataType="Currency"  defvalue="0"/>
			<emp:text id="storage_date" label="入库日期" hidden="true"/>
			<emp:text id="exware_date" label="出库日期" hidden="true"/>
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
