<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
function doReturn() {
	var url = '<emp:url action="queryFncOtherPayableList.do"/>';
	url = EMPTools.encodeURI(url);
	window.location=url;
};	
function checkM(data){
	var val= FncOtherPayable.fnc_sum_amt._getValue();
	var changeValue = data._getValue();
	if(changeValue == null || changeValue == ''){
         return;
	}	
	var a = parseFloat(val);
	var b = parseFloat(changeValue);
	if(b>a){
		alert("金额不能大于合计金额");
		data._setValue("");
	}
}
function checkT(){
	var sum = FncOtherPayable.fnc_sum_amt._getValue();
	var imm = FncOtherPayable.fnc_imm_trm_amt._getValue();
	var shortm = FncOtherPayable.fnc_short_trm_amt._getValue();
	var inter = FncOtherPayable.fnc_inter_trm_amt._getValue();
	var longm = FncOtherPayable.fnc_long_trm_amt._getValue();
	if(sum<imm || sum<shortm || sum<inter || sum<longm){
		alert("应付款余额合计不应小于其他金额");
		FncOtherPayable.fnc_sum_amt._setValue("");
	}
}
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateFncOtherPayableRecord.do" method="POST">
		<emp:gridLayout id="FncOtherPayableGroup" maxColumn="2" title="其他应付款项明细">
			<emp:pop id="FncOtherPayable.cus_id" label="客户码" url="selectCusInfoPop.do?type=com" returnMethod="returnCus"  required="true" />
			<emp:text id="FncOtherPayable.cus_name" label="客户姓名" maxlength="80" required="true" />
			<emp:text id="FncOtherPayable.fnc_con_cus_id" label="对方客户编码" maxlength="30" required="false" />				
			<emp:text id="FncOtherPayable.fnc_con_cus_name" label="对方客户名称" maxlength="60" required="false" />
			<emp:text id="FncOtherPayable.fnc_ym" label="年月" maxlength="6" required="false" onchange="checkZero(this.value)"/>	
			<emp:select id="FncOtherPayable.fnc_cur_typ" label="货币种类" dictname="STD_ZX_CUR_TYPE" required="false" />
			<emp:text id="FncOtherPayable.fnc_sum_amt" label="应付款余额合计" maxlength="18" required="false" dataType="Currency" onchange="checkT()"/>
			<emp:text id="FncOtherPayable.fnc_imm_trm_amt" label="即期应付款" onchange="checkM(FncOtherPayable.fnc_imm_trm_amt)" maxlength="18" required="false" dataType="Currency"/>
			<emp:text id="FncOtherPayable.fnc_short_trm_amt" label="短期应付款" onchange="checkM(FncOtherPayable.fnc_short_trm_amt)" maxlength="18" required="false" dataType="Currency"/>
			<emp:text id="FncOtherPayable.fnc_inter_trm_amt" label="中期应付款" onchange="checkM(FncOtherPayable.fnc_inter_trm_amt)" maxlength="18" required="false" dataType="Currency"/>
			<emp:text id="FncOtherPayable.fnc_long_trm_amt" label="长期应付款" onchange="checkM(FncOtherPayable.fnc_long_trm_amt)" maxlength="18" required="false" dataType="Currency"/>
			<emp:select id="FncOtherPayable.rel_flg" label="是否关联企业" dictname="STD_ZX_YES_NO" required="false" />
			<emp:textarea id="FncOtherPayable.remark" label="备注" maxlength="100" required="false" colSpan="2"/>
			<emp:text id="FncOtherPayable.input_id_displayname" label="登记人" required="true" readonly="true" defvalue="${context.actorname}" />
			<emp:text id="FncOtherPayable.input_br_id_displayname" label="登记机构" required="true" readonly="true" defvalue="${context.organName}" />
			<emp:text id="FncOtherPayable.input_date" label="登记日期" required="true" readonly="true" defvalue="${context.OPENDAY}"/>
			<emp:text id="FncOtherPayable.input_id" label="登记人" hidden="true" defvalue="${context.currentUserId}"/>
			<emp:text id="FncOtherPayable.input_br_id" label="登记机构" hidden="true" defvalue="${context.organNo}"/>
			<emp:text id="FncOtherPayable.last_upd_id" label="更新人" hidden="true" defvalue="${context.currentUserId}"/>
			<emp:text id="FncOtherPayable.last_upd_date" label="更新日期" hidden="true" defvalue="${context.OPENDAY}"  />
			<emp:text id="FncOtherPayable.pk_id" label="主键" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="保存" op="update"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
