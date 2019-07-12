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
.emp_field_text_input2 {
	border:1px solid #CEC7BD;
	text-align:left;
	width:450px;
	background-color: #e3e3e3;
}
</style>
<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryCusIndivBondList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doReturnCusIndivBond(){
		goback();
	}
	function goback(){
		var editFlag = '${context.EditFlag}';
		var paramStr="CusIndivBond.cus_id="+CusIndivBond.cus_id._obj.element.value+"&EditFlag="+editFlag;
		var stockURL = '<emp:url action="queryCusIndivBondList.do"/>&'+paramStr;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	}			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:gridLayout id="CusIndivBondGroup" title="持有资本证券信息" maxColumn="2">
		<emp:text id="CusIndivBond.indiv_bond_id" label="证券编号" maxlength="40" required="true" hidden="true"/>
		<emp:select id="CusIndivBond.indiv_bond_typ" label="证券类别" required="false" dictname="STD_ZB_INV_BON_TYP"/>
		<emp:text id="CusIndivBond.indiv_bond_name" label="证券名称" maxlength="80" required="true" colSpan="2" cssElementClass="emp_field_text_input2"/>
		<emp:text id="CusIndivBond.indiv_bond_eva_amt" label="证券估价总额(元)" maxlength="18" required="false" dataType="Currency" />
		<emp:text id="CusIndivBond.indiv_bond_pub" label="发行商" maxlength="80" required="false" />
		<emp:date id="CusIndivBond.indiv_bond_str_dt" label="持有起始日期" required="false" />
		<emp:date id="CusIndivBond.indiv_bond_end_dt" label="持有到期日期" required="false" />
		<emp:select id="CusIndivBond.indiv_bond_status" label="抵押状况" required="false" dictname="STD_ZX_YES_NO" hidden="true"/>
		<emp:textarea id="CusIndivBond.remark" label="备注" maxlength="250" required="false" colSpan="2" />
		<emp:text id="CusIndivBond.input_id_displayname" label="登记人"  hidden="true"/>
		<emp:text id="CusIndivBond.input_br_id_displayname" label="登记机构"  hidden="true" />
		<emp:date id="CusIndivBond.input_date" label="登记日期" hidden="true" />
		<emp:text id="CusIndivBond.last_upd_id_displayname" label="更新人"  hidden="true" />
		<emp:date id="CusIndivBond.last_upd_date" label="更新日期" hidden="true" />
		<emp:text id="CusIndivBond.cus_id" label="客户码" maxlength="30" required="true" hidden="true"/>
		<emp:text id="CusIndivBond.input_id" label="登记人" maxlength="20" required="false" hidden="true" />
        <emp:text id="CusIndivBond.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" />
        <emp:text id="CusIndivBond.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true" />
	</emp:gridLayout>
	<div align="center">
		<br>
		<emp:button id="returnCusIndivBond" label="返回"/>
	</div>
</body>
</html>
</emp:page>