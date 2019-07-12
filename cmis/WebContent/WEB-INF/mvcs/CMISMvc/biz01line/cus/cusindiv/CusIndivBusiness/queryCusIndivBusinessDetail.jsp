<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
	String flag = request.getParameter("EditFlag");
%>

<script type="text/javascript">
	
	function doReturn() {
		var paramStr="cus_id="+CusIndivBusiness.cus_id._obj.element.value;
		var flag = '<%=flag%>';
		var stockURL = '<emp:url action="queryCusIndivBusinessList.do"/>&'+paramStr+"&EditFlag="+flag;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	};
	
	/*--user code begin--*/
	function doLoad(){
		checkOpac();
	}

	function checkOpac(){
		var ourBank = CusIndivBusiness.is_ourbank_opac._getValue();
		if(ourBank == "2"){
			CusIndivBusiness.acct_type._obj._renderRequired(false);
		}else{
			CusIndivBusiness.acct_type._obj._renderRequired(true);
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:gridLayout id="CusIndivBusinessGroup" title="经营信息" maxColumn="2">
			<emp:text id="CusIndivBusiness.serno" label="SERNO" maxlength="40" hidden="true"/>
			<emp:text id="CusIndivBusiness.cus_id" label="客户码" maxlength="40" hidden="true"/>
			<emp:text id="CusIndivBusiness.biz_lice_id" label="营业执照号码" maxlength="40" required="true" />
			<emp:text id="CusIndivBusiness.com_name" label="企业名称" maxlength="80" required="true" />
			<emp:text id="CusIndivBusiness.major_biz" label="主营业务" maxlength="40" required="true" />
			<emp:select id="CusIndivBusiness.indus_type" label="行业类别" dictname="STD_GB_COM_TYPE" required="true" />
			<emp:select id="CusIndivBusiness.org_qlty" label="机构性质" dictname="STD_ORG_QLTY" required="true" />
			<emp:text id="CusIndivBusiness.biz_lice_exp" label="营业执照有效期" maxlength="10" required="true" />
			<emp:text id="CusIndivBusiness.emp_num" label="雇佣人数" maxlength="10" required="true" />
			<emp:text id="CusIndivBusiness.reg_addr" label="注册地址" maxlength="80" required="true" />
			<emp:text id="CusIndivBusiness.opera_field" label="经营场所" maxlength="50" required="true" />
			<emp:select id="CusIndivBusiness.prop_qlty" label="产权性质" dictname="STD_PROP_QLTY" required="true" />
			<emp:text id="CusIndivBusiness.opt_aera" label="经营场地面积(平方米)"  required="true" onblur="cheakAera()"/>
			<emp:text id="CusIndivBusiness.reg_fund" label="注册资金" maxlength="18" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusIndivBusiness.paid_in_capt" label="实收资本金" maxlength="18" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusIndivBusiness.real_controller" label="实际控制人" maxlength="40" required="true" />
			<emp:text id="CusIndivBusiness.debit_percn" label="借款人股权占比" maxlength="10" required="true" dataType="Rate" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="CusIndivBusiness.ent_model" label="企业规模" dictname="STD_ZB_ENTERPRISE" required="true" />
			<emp:select id="CusIndivBusiness.is_ourbank_opac" label="是否在我行开户" dictname="STD_ZX_YES_NO" required="true" onchange="checkOpac()"/>
			<emp:select id="CusIndivBusiness.acct_type" label="账户类型" dictname="STD_IND_ACCT_TYPE" required="true" />
			<emp:select id="CusIndivBusiness.is_ourbank_authorize" label="是否在我行授信客户" dictname="STD_ZX_YES_NO" required="true" />
			<emp:text id="CusIndivBusiness.org_code" label="组织机构代码" maxlength="40" />
			<emp:text id="CusIndivBusiness.tax_reg_no_c" label="税务登记证号（国税）" maxlength="80" required="false" />
			<emp:text id="CusIndivBusiness.tax_reg_no_a" label="税务登记证号（地税）" maxlength="80" required="false" />
			<emp:text id="CusIndivBusiness.ln_card_no" label="贷款卡号" maxlength="40" required="false" />
			<emp:text id="CusIndivBusiness.phone" label="电话" maxlength="20" required="false" />
			<emp:text id="CusIndivBusiness.linkman" label="联系人" maxlength="40" required="false" />
			<emp:text id="CusIndivBusiness.other" label="其他" maxlength="80" required="false" />
		</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回页面"/>
	</div>
</body>
</html>
</emp:page>
