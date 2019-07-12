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
<style type="text/css">
.emp_field_text_onerow {
	border: 1px solid #b7b7b7;
	text-align:left;
	width:395px;
}
.emp_field_text_onepop {
	border: 1px solid #b7b7b7;
	text-align:left;
	width:365px;
}
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	background-color:#eee;
	text-align:left;
	width:450px;
}
</style>
<script type="text/javascript">
	function doReturn() {
		var cus_id  =CusComRelInvest.cus_id._obj.element.value;
		var EditFlag  ='${context.EditFlag}';
		var paramStr="CusComRelInvest.cus_id="+cus_id;
		var url = '<emp:url action="queryCusComRelInvestList.do"/>&'+paramStr+"&EditFlag="+EditFlag;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/*--user code begin--*/
	function doLoad(){
		CusComRelInvest.cus_id_rel._obj.addOneButton('view12','查看',viewCusInfo);
	}

	//查看客户信息
	function viewCusInfo(){
		var cus_id_rel = CusComRelInvest.cus_id_rel._getValue();
		if(cus_id_rel==null||cus_id_rel==''){
			alert('客户码为空！');
		}else {
			var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id_rel;
	      	url=encodeURI(url); 
	      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		}
	}
	/*--user code end--*/
</script>
</head>
<body class="page_content" onload="doLoad()">
	<emp:gridLayout id="CusComRelInvestGroup" title="对外投资信息" maxColumn="2">
		<emp:select id="CusComRelInvest.com_inv_typ" label="投资性质"  required="true"  dictname="STD_ZB_INVT_NATURE" colSpan="2"/>
		<emp:text id="CusComRelInvest.com_inv_inst_code" label="组织机构代码" maxlength="10" required="true" colSpan="2" />
		<emp:text id="CusComRelInvest.cus_id_rel" label="被投资人客户码" maxlength="30" required="true" readonly="true" />
		<emp:text id="CusComRelInvest.reg_code" label="登记注册号" maxlength="30" required="false" readonly="true" hidden="true"/>
		<emp:text id="CusComRelInvest.cus_id" label="客户码" maxlength="30" required="true" hidden="true"/>
		<emp:text id="CusComRelInvest.com_inv_name" label="被投资企业名称(全称)" maxlength="80" required="true" cssElementClass="emp_field_text_input2"/>
		<emp:text id="CusComRelInvest.com_inv_loan_card" label="贷款卡号" maxlength="16" required="false" readonly="true" colSpan="2"/>
		<emp:select id="CusComRelInvest.com_inv_cur_typ" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="CusComRelInvest.com_inv_amt" label="投资金额(万元)" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		<emp:select id="CusComRelInvest.com_inv_app" label="出资方式" required="true" dictname="STD_ZB_INVT_TYPE" />
		<emp:date id="CusComRelInvest.com_inv_dt" label="投资时间" required="true" />
		<emp:text id="CusComRelInvest.com_inv_perc" label="所占比例" maxlength="10"  dataType="Percent" required="true" cssElementClass="emp_currency_text_readonly"/>
		<emp:textarea id="CusComRelInvest.com_inv_desc" label="投资说明" maxlength="200" required="true" colSpan="2" />
		<emp:textarea id="CusComRelInvest.remark" label="备注" maxlength="250" required="false" colSpan="2" />
		<emp:text id="CusComRelInvest.input_id" label="登记人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId"/>
		<emp:text id="CusComRelInvest.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="$organNo"/>
		<emp:date id="CusComRelInvest.input_date" label="登记日期" required="false" hidden="true" defvalue="$OPENDAY"/>
		<emp:text id="CusComRelInvest.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId"/>
		<emp:date id="CusComRelInvest.last_upd_date" label="更新日期" required="false" hidden="true" defvalue="$OPENDAY"/>
	</emp:gridLayout>
	<div align="center">
		<br>
		<emp:button id="return" label="返回"/>
	</div>
</body>
</html>
</emp:page>