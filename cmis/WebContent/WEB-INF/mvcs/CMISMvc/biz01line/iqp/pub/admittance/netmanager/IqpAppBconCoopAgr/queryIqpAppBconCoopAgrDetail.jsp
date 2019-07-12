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
	border: 1px solid #b7b7b7;
	background-color:#eee;
	text-align:left;
	width:450px;
}
</style>
<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryIqpAppBconCoopAgrList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
    function doClose(){
      window.close();
    };
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="IqpAppBconCoopAgrGroup" title="银企商合作协议信息" maxColumn="2">
			<emp:text id="IqpAppBconCoopAgr.coop_agr_no" label="银企商协议号" maxlength="32" hidden="false" required="true" colSpan="2"/>
		    <emp:text id="IqpAppBconCoopAgr.borrow_cus_id" label="借款人客户码" maxlength="30" required="true" readonly="true" />
			<emp:text id="IqpAppBconCoopAgr.borrow_cus_id_displayname" label="借款人客户名称"   required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:text id="IqpAppBconCoopAgr.manuf_cus_id" label="核心企业客户码" maxlength="30" required="true" readonly="true" />
			<emp:text id="IqpAppBconCoopAgr.manuf_cus_id_displayname" label="核心企业客户名称"   required="true" colSpan="2" cssElementClass="emp_field_text_input2" readonly="true"/>
			<emp:pop id="IqpAppBconCoopAgr.psale_cont" label="购销合同" url="queryIqpPsaleContPop.do?serno=${context.serno}" returnMethod="setPsale" required="true"/>
			<emp:text id="IqpAppPsaleCont.cont_amt" label="合同金额" maxlength="18" required="true" dataType="Currency" readonly="true"/>
			<emp:date id="IqpAppPsaleCont.start_date" label="合同起始日" required="true" readonly="true"/>
			<emp:date id="IqpAppPsaleCont.end_date" label="合同到期日"  required="true" readonly="true"/>
			
			<emp:pop id="IqpAppBconCoopAgr.desgoods_plan_no" label="订货计划" url="queryIqpDesbuyPlanPop.do?serno=${context.serno}" returnMethod="setDesgood" required="false" />
			<emp:text id="IqpAppBconCoopAgr.low_bail_perc" label="最低保证金比例" maxlength="16" required="false" dataType="Rate" />
			<emp:text id="IqpAppBconCoopAgr.vigi_line" label="警戒线" maxlength="10" required="true" dataType="Percent" onblur="checkLine()" />
			<emp:text id="IqpAppBconCoopAgr.stor_line" label="平仓线" maxlength="10" required="true" dataType="Percent" onblur="checkLine()" />
			<emp:text id="IqpAppBconCoopAgr.froze_line" label="冻结线" maxlength="10" required="true" dataType="Percent"  />
			
			<emp:pop id="IqpAppBconCoopAgr.consign_cus_id" label="收货人客户码" url="queryAllCusPop.do?cusTypCondition=&returnMethod=getCusInfo4consign" required="false" colSpan="2"/>
			<emp:text id="IqpAppBconCoopAgr.consign_cus_id_displayname" label="收货人" required="false" colSpan="2" cssElementClass="emp_field_text_input2" readonly="true" />
			<emp:pop id="IqpAppBconCoopAgr.consign_addr_displayname" label="收货地点" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" required="false" 
			          returnMethod="onReturnRegStateCode" colSpan="2"  cssElementClass="emp_field_text_input2"/>		
			
			
			<emp:text id="IqpAppBconCoopAgr.refndmt_acct" label="退款账户账号" maxlength="40" required="false" />
			<emp:text id="IqpAppBconCoopAgr.refndmt_acct_name" label="退款账户名称" maxlength="80" required="false" />
			
			<emp:textarea id="IqpAppBconCoopAgr.memo" label="备注" maxlength="500" required="false" colSpan="2"/>
			<emp:text id="IqpAppBconCoopAgr.input_id_displayname" label="登记人"  required="true" readonly="true"/>
			<emp:text id="IqpAppBconCoopAgr.input_br_id_displayname" label="登记机构"  required="true" readonly="true"/>	
			<emp:text id="IqpAppBconCoopAgr.input_date" label="登记日期" maxlength="10" required="true" defvalue="$OPENDAY" readonly="true" />
			<emp:text id="IqpAppBconCoopAgr.input_id" label="登记人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="IqpAppBconCoopAgr.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true"/>
			
			 
			<emp:text id="IqpAppBconCoopAgr.serno" label="业务编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="IqpAppBconCoopAgr.consign_addr" label="收货地点" maxlength="100" required="false" hidden="true"/>
			<emp:text id="IqpAppBconCoopAgr.start_date" label="协议起始日期" maxlength="10" required="false" hidden="true"/>
			<emp:text id="IqpAppBconCoopAgr.end_date" label="协议到期日期" maxlength="10" required="false" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="close" label="关闭"/>
	</div>
</body>
</html>
</emp:page>
