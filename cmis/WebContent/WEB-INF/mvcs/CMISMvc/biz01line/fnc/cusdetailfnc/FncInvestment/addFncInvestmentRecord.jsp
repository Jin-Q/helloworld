<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
function returnCus(data){
	FncInvestment.cus_id._setValue(data.cus_id._getValue());
	FncInvestment.cus_name._setValue(data.cus_name._getValue());
}
function checkZero(data){
	var x=data.length;
	if(x !=6){
 		alert("年月的位数必须为6位,格式为YYYYMM！");
 		FncInvestment.fnc_ym._setValue("");
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
				FncInvestment.fnc_ym._setValue("");
				}
		}else {		
			alert("年份不应该大于当前年份");
			FncInvestment.fnc_ym._setValue("");
		}
		
	}
}
function doReturn() {
	var url = '<emp:url action="queryFncInvestmentList.do"/>';
	url = EMPTools.encodeURI(url);
	window.location=url;
};	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addFncInvestmentRecord.do" method="POST">
		
		<emp:gridLayout id="FncInvestmentGroup" title="主要长期投资明细表" maxColumn="2">
			<emp:pop id="FncInvestment.cus_id"  label="客户码"  url="selectCusInfoPop.do?type=com" returnMethod="returnCus"  required="true" />
			<emp:text id="FncInvestment.cus_name" label="客户名称" maxlength="80" required="true" />
			<emp:text id="FncInvestment.fnc_ym" label="年月" maxlength="6" required="true" onblur="checkZero(this.value)"/>
			<emp:text id="FncInvestment.fnc_invt_toward" label="投资资产名称" maxlength="200" required="true" />
			<emp:text id="FncInvestment.fnc_invt_amt" label="投资金额" maxlength="18" dataType="Currency" required="true" />
			<emp:text id="FncInvestment.fnc_cur_val" label="现价值" maxlength="18" required="true" dataType="Currency"/>
			<emp:text id="FncInvestment.fnc_last_invt_incm" label="上年投资收益" maxlength="18" required="true" dataType="Currency"/>
			<emp:text id="FncInvestment.fnc_avg_invt_incm" label="平均投资收益" maxlength="18" required="true" dataType="Currency"/>
			<emp:select id="FncInvestment.fnc_invt_type" label="投资性质" dictname="STD_ZB_INVT_NATURE" required="true" />
			<emp:select id="FncInvestment.fnc_price_typ" label="计价方法" dictname="STD_ZB_FNC_PRIC_TYP" required="true" />
			<emp:date id="FncInvestment.fnc_invt_dt" label="投资日期"  required="true" colSpan="2"/>
			<emp:textarea id="FncInvestment.remark" label="备注" maxlength="100" required="false" colSpan="2"/>
			<emp:text id="FncInvestment.input_id_displayname" label="登记人" required="true" readonly="true" defvalue="${context.actorname}" />
			<emp:text id="FncInvestment.input_br_id_displayname" label="登记机构" required="true" readonly="true" defvalue="${context.organName}" />
			<emp:text id="FncInvestment.input_date" label="登记日期" required="true" readonly="true" defvalue="${context.OPENDAY}"/>
			<emp:text id="FncInvestment.input_id" label="登记人" hidden="true" defvalue="${context.currentUserId}"/>
			<emp:text id="FncInvestment.input_br_id" label="登记机构" hidden="true" defvalue="${context.organNo}"/>
			<emp:text id="FncInvestment.last_upd_id" label="更新人" hidden="true" defvalue="${context.currentUserId}"/>
			<emp:text id="FncInvestment.last_upd_date" label="更新日期" hidden="true" defvalue="${context.OPENDAY}"  />
			<emp:text id="FncInvestment.pk_id" label="主键" hidden="true"/>
			
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

