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

	/*--user code begin--*/
	function doReturn() {
		var editFlag = '${context.EditFlag}';
		var cus_id  =CusComFinaBond.cus_id._obj.element.value;
		var paramStr="CusComFinaBond.cus_id="+cus_id+"&EditFlag="+editFlag;
		var url = '<emp:url action="queryCusComFinaBondList.do"/>&'+paramStr;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="CusComFinaBondGroup" title="发行债券信息" maxColumn="2">
			<emp:text id="CusComFinaBond.cus_id" label="客户码" maxlength="30" required="true" hidden="true"/>
			<emp:text id="CusComFinaBond.seq" label="序号" maxlength="38" required="true" hidden="true"/>
			<emp:text id="CusComFinaBond.com_bond_no" label="债券编号" maxlength="60" required="true" colSpan="2"/>
			<emp:text id="CusComFinaBond.com_bond_name" label="债券名称" maxlength="60" required="true" />
			<emp:text id="CusComFinaBond.com_bond_trm" label="债券期限(月)" maxlength="38" required="true" dataType="Long" cssElementClass="emp_currency_text_readonly"/>
			<emp:date id="CusComFinaBond.com_bond_pub_dt" label="债券发行日期" required="true"/>
			<emp:date id="CusComFinaBond.com_bond_end_dt" label="债券到期日期" required="true"/>
			<emp:select id="CusComFinaBond.com_bond_typ" label="债券类型" required="true" dictname="STD_ZB_COM_BOND_TYP"/>
			<emp:select id="CusComFinaBond.com_bond_cls" label="债券分类" required="true" dictname="STD_ZB_COM_BOND_CLS"/>
			<emp:select id="CusComFinaBond.com_bond_cur_typ" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="CusComFinaBond.com_bond_amt" label="总金额(元)" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusComFinaBond.com_bond_rate" label="年利率" maxlength="16" required="true" dataType="Rate" colSpan="2" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="CusComFinaBond.com_bond_mrk_flg" label="是否上市" required="true" dictname="STD_ZX_YES_NO" />
			<emp:select id="CusComFinaBond.com_bond_mrk_place" label="上市地" required="true" dictname="STD_ZX_LISTED" />
			<emp:text id="CusComFinaBond.com_bond_mrk_brs" label="交易所名称" maxlength="60" required="false" colSpan="2"/>
			<emp:select id="CusComFinaBond.com_acct_typ" label="账户类型" required="true" colSpan="2" dictname="STD_ZB_COM_ACC_TYP" />
			<emp:text id="CusComFinaBond.com_bond_crt_info" label="债券等级" maxlength="60" required="false" colSpan="2"/>
			<emp:text id="CusComFinaBond.com_bond_crt_org" label="评级机构" maxlength="80" required="false" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:text id="CusComFinaBond.com_bond_eva_amt" label="债券评估价(元)" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="CusComFinaBond.com_bond_assure_means" label="债券的担保方式" required="false" dictname="STD_ZB_ASSURE_MEANS"/>
			<emp:textarea id="CusComFinaBond.remark" label="备注" maxlength="250" required="false" colSpan="2" />
		
			<emp:text id="CusComFinaBond.input_id" label="登记人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="CusComFinaBond.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true"/>
			<emp:text id="CusComFinaBond.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true"/>
			<emp:date id="CusComFinaBond.input_date" label="登记日期" required="false" hidden="true"/>
			<emp:date id="CusComFinaBond.last_upd_date" label="更新日期" required="false" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回"/>
	</div>
</body>
</html>
</emp:page>
