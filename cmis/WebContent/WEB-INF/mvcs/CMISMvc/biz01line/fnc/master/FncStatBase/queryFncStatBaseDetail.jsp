<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>
<jsp:include page="/include.jsp" />

<script type="text/javascript">

	var page = new EMP.util.Page();
	function doOnLoad() {
		page.renderEmpObjects();
		checkStatChange(FncStatBase.stat_is_audit._getValue());
		checkAdjtChange(FncStatBase.stat_is_adjt._getValue());
	}
	
	function doReturn() {
		var paramStr="FncStatBase.cus_id="+FncStatBase.cus_id._obj.element.value;
		var url = '<emp:url action="queryFncStatBaseList.do"/>&'+paramStr;
		url = EMP.util.Tools.encodeURI(url);
		window.location=url;
	};
	function checkStatChange(value){	
		if(value==1){
			FncStatBase.stat_adt_entr._obj._renderReadonly(true);
			FncStatBase.stat_adt_entr._obj._renderRequired(true);
			FncStatBase.stat_adt_conc._obj._renderReadonly(true);
			FncStatBase.stat_adt_conc._obj._renderRequired(true);
		}else{
			FncStatBase.stat_adt_entr._setValue("");
			FncStatBase.stat_adt_entr._obj._renderRequired(false);
			FncStatBase.stat_adt_entr._obj._renderReadonly(true);
			FncStatBase.stat_adt_conc._setValue("");
			FncStatBase.stat_adt_conc._obj._renderRequired(false);
			FncStatBase.stat_adt_conc._obj._renderReadonly(true);
		}
	} 
	function checkAdjtChange(value){
		
		if(value==1){
			FncStatBase.stat_adj_rsn._obj._renderReadonly(true);
			FncStatBase.stat_adj_rsn._obj._renderRequired(true);
		}else{
			FncStatBase.stat_adj_rsn._setValue("");			
			FncStatBase.stat_adj_rsn._obj._renderRequired(false);
			FncStatBase.stat_adj_rsn._obj._renderReadonly(true);
		}
	}
</script>
<style type="text/css">
.emp_field_text_input_0 {
width:300px;
}
</style>
</head>
<body class="page_content" onload="doOnLoad()">
	<form id="submitForm" action="updateFncStatBaseRecord.do" method="POST">
	</form>
	
	<div id="FncStatBaseGroup" class="emp_group_div">
		<emp:gridLayout id="FncStatBaseGroup" maxColumn="2" title="公司客户报表">
			<emp:text id="FncStatBase.cus_id" label="客户码" maxlength="30" required="true" readonly="true" hidden="true"/>
			<emp:select id="FncStatBase.stat_prd_style" label="报表周期类型" required="true" dictname="STD_ZB_FNC_STAT" readonly="true"/>
			<emp:text id="FncStatBase.stat_prd" label="报表期间" maxlength="6" required="true" readonly="true"/>
			<emp:text id="FncStatBase.stat_bs_style_id" label="资产样式编号" maxlength="6" required="false" hidden="true"/>
			<emp:text id="FncStatBase.stat_pl_style_id" label="损益表编号" maxlength="6" required="false" hidden="true" />
			<emp:text id="FncStatBase.stat_cf_style_id" label="现金流量表编号" maxlength="6" required="false" hidden="true"/>
			<emp:text id="FncStatBase.stat_fi_style_id" label="财务指标表编号" maxlength="6" required="false" hidden="true"/>
			<emp:text id="FncStatBase.stat_soe_style_id" label="所有者权益变动表编号" maxlength="6" required="false" hidden="true"/>
			<emp:text id="FncStatBase.stat_sl_style_id" label="财务简表编号" maxlength="6" required="false" hidden="true"/>
			<emp:text id="FncStatBase.style_id1" label="保留" maxlength="6" required="false" hidden="true"/>
			<emp:text id="FncStatBase.style_id2" label="保留1" maxlength="6" required="false" hidden="true"/>
			<emp:text id="FncStatBase.state_flg" label="状态" maxlength="6" required="true" readonly="true"/>
			<emp:select id="FncStatBase.stat_is_nrpt" label="是否新准则报表" required="true" dictname="STD_ZX_YES_NO" readonly="true"/>
			<emp:select id="FncStatBase.stat_style" label="报表口径" required="true" dictname="STD_ZB_FNC_STYLE" readonly="true"/>
			<emp:select id="FncStatBase.stat_is_audit" label="是否经过审计" required="true" dictname="STD_ZX_YES_NO" readonly="true"/>
			<emp:select id="FncStatBase.stat_is_adjt" label="是否经过调整" required="true" dictname="STD_ZX_YES_NO" readonly="true"/>
			<emp:text id="FncStatBase.stat_adt_entr" label="审计单位" maxlength="60" required="false" readonly="true" cssElementClass="emp_field_text_input_0"/>
			<emp:text id="FncStatBase.last_upd_date" label="更新时间" maxlength="10" required="false" hidden="true" />
			<emp:textarea id="FncStatBase.stat_adt_conc" label="审计结论" maxlength="200" required="false" readonly="true"/>
			<emp:text id="FncStatBase.input_date" label="登记日期" maxlength="10" required="false" hidden="true" />
			<emp:textarea id="FncStatBase.stat_adj_rsn" label="财务报表调整原因" maxlength="200" required="false" readonly="true"/>
			<emp:text id="FncStatBase.last_upd_id" label="更新人" maxlength="16" required="false" hidden="true" />
			<emp:text id="FncStatBase.input_br_id" label="登记机构" maxlength="10" required="false" hidden="true"/>
			<emp:text id="FncStatBase.input_id" label="登记人" maxlength="16" required="false" hidden="true" />
			
			
		</emp:gridLayout>
	</div>
		
	<div align="center">
		<br>
		<emp:button id="return" label="返回"/>
	</div>
	<script type="text/javascript">
	
	</script>
</body>
</html>
</emp:page>
