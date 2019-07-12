<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input2 {
	border:1px solid #CEC7BD;
	text-align:left;
	width:450px;
	background: #e3e3e3;
}
</style>
<%
	request.setAttribute("canwrite","");
%>
<script type="text/javascript">
	function doReturn() {
		var url = '<emp:url action="queryCusIndivAssList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doReturnCusIndivAss(){
		goback();
	}
	
	function goback(){
		var paramStr="CusIndivAss.cus_id="+CusIndivAss.cus_id._obj.element.value;
		var EditFlag  ='${context.EditFlag}';
		var stockURL = '<emp:url action="queryCusIndivAssList.do"/>&'+paramStr+"&EditFlag="+EditFlag;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	}			
	/*--user code end--*/
</script>
</head>
<body class="page_content">
	<emp:gridLayout id="CusIndivAssGroup" title="家庭资产信息" maxColumn="2">
		<emp:text id="CusIndivAss.indiv_ass_id" label="资产编号" maxlength="40" required="true" hidden="true"/>
		<emp:select id="CusIndivAss.indiv_ass_type" label="资产类别" required="false" dictname="STD_ZB_INV_ASS_TPY"/>
		<emp:text id="CusIndivAss.indiv_ass_name" label="资产名称" maxlength="80" required="true" colSpan="2" cssElementClass="emp_field_text_input2"/>
		<emp:text id="CusIndivAss.indiv_ass_plr" label="资产单位" maxlength="80" required="true" />
		<emp:text id="CusIndivAss.indiv_ass_num" label="资产数量" maxlength="38" required="true" dataType="Int" cssElementClass="emp_currency_text_readonly"/>
		<emp:date id="CusIndivAss.com_ass_buy_date" label="资产购置日期" required="false" hidden="true"/>
		<emp:text id="CusIndivAss.com_ass_ori_amt" label="资产购置原价(元)" maxlength="18" required="false" hidden="true" dataType="Currency" onblur="cheakAmt(CusIndivAss.com_ass_ori_amt)"/>
		<emp:text id="CusIndivAss.indiv_ass" label="资产估价(元)" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		<emp:select id="CusIndivAss.com_ass_status" label="抵押状况" required="true" dictname="STD_ZB_ASS_COLL_INFO" colSpan="2"/>
		<emp:textarea id="CusIndivAss.indiv_ass_des" label="资产描述" maxlength="250" required="false" colSpan="2"/>
		<emp:textarea id="CusIndivAss.remark" label="备注" maxlength="250" required="false" colSpan="2" />
		<emp:text id="CusIndivAss.input_id_displayname" label="登记人"  required="false" />
		<emp:text id="CusIndivAss.input_br_id_displayname" label="登记机构"  required="false" />
		<emp:date id="CusIndivAss.input_date" label="登记日期" required="false" colSpan="2"/>
		<emp:text id="CusIndivAss.last_upd_id_displayname" label="更新人"  required="false" />
		<emp:date id="CusIndivAss.last_upd_date" label="更新日期" required="false" />
		<emp:text id="CusIndivAss.cus_id" label="客户码" maxlength="30" required="true" hidden="true"/>
	</emp:gridLayout>
	<div align="center">
		<br>
		<emp:button id="returnCusIndivAss" label="返回"/>
	</div>
</body>
</html>
</emp:page>