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
		var url = '<emp:url action="queryMortCargoReplList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	//货物记录查看事件
	function doViewMortCargoPledge() {
		var data = MortCargoPledgeNewList._obj.getSelectedData();
		if (data.length ==1) {
			var cargo_id = data[0].cargo_id._getValue();
			var url = '<emp:url action="getMortCargoPledgeViewPage.do"/>?flag=tab&action=zh&cargo_id='+cargo_id;//flag标志非押品维护时的货物操作（tab）
			url = EMPTools.encodeURI(url);
	      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};
	//货物记录查看事件
	function doViewMortCargoPledge1() {
		var data = MortCargoPledgeList._obj.getSelectedData();
		if (data.length ==1) {
			var cargo_id = data[0].cargo_id._getValue();
			var url = '<emp:url action="getMortCargoPledgeViewPage.do"/>?flag=tab&action=zh&cargo_id='+cargo_id;//flag标志非押品维护时的货物操作（tab）
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
	<emp:gridLayout id="MortCargoReplGroup" title="货物置换" maxColumn="2">
			<emp:text id="MortCargoRepl.serno" label="置换流水号" maxlength="60" required="true" readonly="true"/>
			<emp:text id="MortCargoRepl.guaranty_no" label="押品编号" required="true" readonly="true"/>			
			<emp:text id="MortCargoRepl.cus_id" label="客户码" maxlength="40" required="true" readonly="true"/>
			<emp:text id="MortCargoRepl.oversee_agr_no" label="监管协议编号" maxlength="40" required="true" readonly="true"/>			
			<emp:text id="MortCargoRepl.cus_id_displayname" label="出质人客户名称" required="false" cssElementClass="emp_field_text_readonly" colSpan="2"/>
			<emp:text id="MortCargoRepl.storage_total" label="库存总价值" maxlength="18" required="false" dataType="Currency" readonly="true"/>
			<emp:text id="MortCargoRepl.this_repl_total" label="此次置换总价值" maxlength="18" required="false" dataType="Currency" readonly="true"/>
			<emp:text id="MortCargoRepl.not_out_total" label="出库待记账货物总价值" maxlength="18" required="false" dataType="Currency" hidden="true"/>
			<emp:text id="MortCargoRepl.not_to_total" label="入库待记账货物总价值" maxlength="18" required="false" dataType="Currency" hidden="true"/>
			<emp:text id="MortCargoRepl.after_repl_total" label="置换后总价值" maxlength="18" required="false" dataType="Currency" readonly="true" colSpan="2"/>
			<emp:date id="MortCargoRepl.tally_date" label="记账日期" required="false" readonly="true"/>
			<emp:select id="MortCargoRepl.status" label="状态" required="false" dictname="STD_ZB_TALLY_STATUS" defvalue="00" readonly="true"/>
			<emp:textarea id="MortCargoRepl.memo" label="备注" maxlength="200" required="false" colSpan="2" />
		</emp:gridLayout>
		<emp:gridLayout id="MortCargoReplGroup" title="登记信息" maxColumn="2">	
			<emp:text id="MortCargoRepl.input_id" label="登记人" maxlength="20" required="true" defvalue="$currentUserId" hidden="true"/>
			<emp:text id="MortCargoRepl.input_br_id" label="登记机构" maxlength="20" required="true" defvalue="$organNo" hidden="true" />
			<emp:text id="MortCargoRepl.input_id_displayname" label="登记人" required="true" readonly="true" defvalue="$currentUserName"/>
			<emp:text id="MortCargoRepl.input_br_id_displayname" label="登记机构" required="true" readonly="true" defvalue="$organName"/>
			<emp:date id="MortCargoRepl.input_date" label="登记日期" required="false" defvalue="$OPENDAY" readonly="true"/>
		</emp:gridLayout>
	<div class='emp_gridlayout_title'>在库货物信息</div>
		<div align="left">
			<emp:button id="viewMortCargoPledge1" label="查看" op="view"/>
		</div>
		<emp:table icollName="MortCargoPledgeList" pageMode="false" url="pageMortCargoPledgeQuery.do?status=ck&guarantyNo=${context.MortCargoRepl.guaranty_no}" selectType="2">
			<emp:text id="cargo_id" label="货物编号" readonly="true"/>
			<emp:text id="guaranty_catalog" label="押品所处目录" readonly="true" hidden="true" />
			<emp:text id="guaranty_catalog_displayname" label="押品所处目录名称" readonly="true" />
			<emp:text id="qnt" label="在库数量" readonly="true" />
			<emp:text id="identy_unit_price" label="单价" readonly="true" dataType="Currency" />
			<emp:text id="identy_total" label="在库总价" readonly="true" dataType="Currency"/>
			<emp:text id="deliv_qnt" label="置换数量" dataType="Double" onchange="doCacul()" defvalue="0" required="false"/>
			<emp:text id="deliv_value" label="置换价值" readonly="true" dataType="Currency"  defvalue="0"/>
			<emp:text id="surplus_qnt" label="剩余数量" readonly="true" dataType="Currency"  defvalue="0"/>
			<emp:text id="surplus_value" label="剩余价值" readonly="true" dataType="Currency"  defvalue="0"/>
			<emp:text id="cargo_status" label="状态" dictname="STD_CARGO_STATUS" hidden="true"/>
		</emp:table>
		<div class='emp_gridlayout_title'>置换货物信息</div>
		<div align="left">
			<emp:button id="viewMortCargoPledge" label="查看" op="view"/>
		</div>
		
		<emp:table icollName="MortCargoPledgeNewList" pageMode="false" url="pageMortCargoPledgeNewQuery.do?status=zh&guarantyNo=${context.MortCargoRepl.guaranty_no}" selectType="2">
			<emp:text id="cargo_id" label="货物编号" />
			<emp:text id="guaranty_catalog" label="押品所处目录" hidden="true" />
			<emp:text id="guaranty_catalog_displayname" label="押品所处目录" />
			<emp:text id="cargo_name" label="货物名称" />
			<emp:text id="qnt" label="数量" dataType="Double"/>
			<emp:text id="identy_unit_price" label="银行认定单价" dataType="Currency"/>
			<emp:text id="identy_total" label="银行认定总价" dataType="Currency"/>
			<emp:text id="cargo_status" label="状态" dictname="STD_CARGO_STATUS" hidden="true"/>
			<emp:text id="reg_date" label="登记日期" />
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
