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
		var paramStr="CusObisAssure.cus_id="+CusObisAssure.cus_id._obj.element.value;
		var EditFlag  ='${context.EditFlag}';
		var url = '<emp:url action="queryCusObisAssureList.do"/>&'+paramStr+"&EditFlag="+EditFlag;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="CusObisAssureGroup" title="他行交易－他行担保" maxColumn="2">
			<emp:text id="CusObisAssure.cus_id" label="客户码" maxlength="30" required="true" />
			<emp:text id="CusObisAssure.seq" label="序号" maxlength="38" required="true" hidden="true"/>
			<emp:text id="CusObisAssure.warrantee_name" label="被担保人名称" maxlength="80" required="true" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:select id="CusObisAssure.gty_typ" label="担保方式" required="true" dictname="STD_ZB_ASSURE_MEANS" colSpan="2"/>
			
			<emp:text id="CusObisAssure.gty_amt" label="担保金额(元)" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="CusObisAssure.gty_blc" label="担保余额(元)" maxlength="18" required="true" dataType="Currency" />
			<emp:date id="CusObisAssure.gty_str_dt" label="起始日期" required="false" />
			<emp:date id="CusObisAssure.gty_end_dt" label="到期日期" required="false" />
			<emp:text id="CusObisAssure.gty_bus_bch_dec" label="发生行详细名称" maxlength="60" required="false" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:select id="CusObisAssure.valid_flg" label="有效标志" required="true" dictname="STD_ZB_STATUS" />
			<emp:textarea id="CusObisAssure.remark" label="备注" maxlength="250" required="true"  colSpan="2"/>
			<emp:text id="CusObisAssure.input_id" label="登记人" maxlength="20" required="true" hidden="true"/>
			<emp:text id="CusObisAssure.input_br_id" label="登记机构" maxlength="20" required="true"  hidden="true"/>
			<emp:date id="CusObisAssure.input_date" label="登记日期" required="true" hidden="true"/>
			<emp:text id="CusObisAssure.last_upd_id" label="更新人" maxlength="20" required="true" hidden="true"/>
			<emp:text id="CusObisAssure.last_upd_date" label="更新日期" maxlength="10" required="true" hidden="true"/>
			<emp:textarea id="CusObisAssure.guar_detail" label="担保业务描述" maxlength="250" required="false" colSpan="2" hidden="false"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回"/>
	</div>
</body>
</html>
</emp:page>
