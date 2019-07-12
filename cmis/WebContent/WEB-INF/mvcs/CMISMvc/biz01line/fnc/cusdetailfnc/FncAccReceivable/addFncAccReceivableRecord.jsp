<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

function returnCus(data){
	FncAccReceivable.cus_id._setValue(data.cus_id._getValue());
	FncAccReceivable.cus_name._setValue(data.cus_name._getValue());
}
function checkZero(data){
	var x=data.length;
	if(x !=6){
 		alert("年月的位数必须为6位,格式为YYYYMM！");
 		FncAccReceivable.fnc_ym._setValue("");
	}
	else{
		var nn;
		nn=new Date();
		var m = data.substring(4,6);
		var y = Number(data.substring(0,4));
		var ny = nn.getFullYear();
		if(y<=ny){
				if(m>'12'||m=='00'){
				alert("月份应该在1-12月之间！");
				FncAccReceivable.fnc_ym._setValue("");
				}
		}else {		
			alert("年份不应该大于当前年份");
			FncAccReceivable.fnc_ym._setValue("");
		}
		
	}
}	
function doReturn() {
	var url = '<emp:url action="queryFncAccReceivableList.do"/>';
	url = EMPTools.encodeURI(url);
	window.location=url;
}
function checkM(data){
	var val= FncAccReceivable.fnc_sum_amt._getValue();
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
	var sum = FncAccReceivable.fnc_sum_amt._getValue();
	var imm = FncAccReceivable.fnc_imm_trm_amt._getValue();
	var shortm = FncAccReceivable.fnc_short_trm_amt._getValue();
	var inter = FncAccReceivable.fnc_inter_trm_amt._getValue();
	var longm = FncAccReceivable.fnc_long_trm_amt._getValue();
	if(sum<imm || sum<shortm || sum<inter || sum<longm){
		alert("应付款余额合计不应小于其他金额");
		FncAccReceivable.fnc_sum_amt._setValue("");
	}
}
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addFncAccReceivableRecord.do" method="POST">
		
		<emp:gridLayout id="FncAccReceivableGroup" title="应收账款及帐龄分析表" maxColumn="2">
			<emp:pop id="FncAccReceivable.cus_id" label="客户码" url="selectCusInfoPop.do?type=com" returnMethod="returnCus" required="true" />
			<emp:text id="FncAccReceivable.cus_name" label="客户名称" maxlength="80" required="false" />
			<emp:text id="FncAccReceivable.fnc_con_cus_id" label="对方客户编码" maxlength="30" required="true" />		
			<emp:text id="FncAccReceivable.fnc_con_cus_name" label="对方客户名称" maxlength="60" required="true" />
			<emp:text id="FncAccReceivable.fnc_ym" label="年月" maxlength="6" required="true" onchange="checkZero(this.value)"/>
			<emp:select id="FncAccReceivable.fnc_cur_typ" label="货币种类" dictname="STD_ZX_CUR_TYPE" required="true" />
			<emp:select id="FncAccReceivable.guar_st" label="低质押情况" dictname="STD_ZB_GUAR_ST" required="true" />
			<emp:text id="FncAccReceivable.fnc_sum_amt" label="应付款余额合计" maxlength="18" required="true" dataType="Currency" onchange="checkT()"/>
			<emp:text id="FncAccReceivable.fnc_imm_trm_amt" label="即期应付款" maxlength="18" required="true" dataType="Currency" onchange="checkM(FncAccReceivable.fnc_imm_trm_amt)"/>
			<emp:text id="FncAccReceivable.fnc_short_trm_amt" label="短期应付款" maxlength="18" required="true" dataType="Currency" onchange="checkM(FncAccReceivable.fnc_short_trm_amt)"/>
			<emp:text id="FncAccReceivable.fnc_inter_trm_amt" label="中期应付款" maxlength="18" required="true" dataType="Currency" onchange="checkM(FncAccReceivable.fnc_inter_trm_amt)"/>
			<emp:text id="FncAccReceivable.fnc_long_trm_amt" label="长期应付款" maxlength="18" required="true" dataType="Currency" onchange="checkM(FncAccReceivable.fnc_long_trm_amt)"/>
			<emp:select id="FncAccReceivable.rel_flg" label="是否关联企业" dictname="STD_ZX_YES_NO" required="false" colSpan="2"/>
			<emp:textarea id="FncAccReceivable.remark" label="备注" maxlength="100" required="false" colSpan="2"/>
			<emp:text id="FncAccReceivable.input_id_displayname" label="登记人" required="true" readonly="true" defvalue="${context.actorname}" />
			<emp:text id="FncAccReceivable.input_br_id_displayname" label="登记机构" required="true" readonly="true" defvalue="${context.organName}" />
			<emp:text id="FncAccReceivable.input_date" label="登记日期" required="true" readonly="true" defvalue="${context.OPENDAY}"/>
			<emp:text id="FncAccReceivable.input_id" label="登记人" hidden="true" defvalue="${context.currentUserId}"/>
			<emp:text id="FncAccReceivable.input_br_id" label="登记机构" hidden="true" defvalue="${context.organNo}"/>
			<emp:text id="FncAccReceivable.last_upd_id" label="更新人" hidden="true" defvalue="${context.currentUserId}"/>
			<emp:text id="FncAccReceivable.last_upd_date" label="更新日期" hidden="true" defvalue="${context.OPENDAY}"  />
			<emp:text id="FncAccReceivable.pk_id" label="主键" hidden="true" />
			
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="保存" op="add"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

