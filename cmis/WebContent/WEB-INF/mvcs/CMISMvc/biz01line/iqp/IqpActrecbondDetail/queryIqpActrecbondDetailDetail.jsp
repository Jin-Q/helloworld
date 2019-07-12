<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>
<style type="text/css">
.emp_select{
width:628px;
}
</style>
<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">

	/*--user code begin--*/
	function doReturn() {
		window.close();
	};

	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:gridLayout id="IqpActrecbondDetailGroup" title="应收账款明细" maxColumn="2">
		<emp:text id="IqpActrecbondDetail.po_no" label="池编号" maxlength="30" required="true" colSpan="2" readonly="true" />
		<emp:text id="IqpActrecbondDetail.buy_cus_name" label="买方客户名称"   colSpan="2" maxlength="40" required="true" cssElementClass="emp_field_text_long_readonly"/>
		<emp:text id="IqpActrecbondDetail.sel_cus_name" label="卖方客户名称"  colSpan="2"  required="true" cssElementClass="emp_field_text_long_readonly"/>
		<emp:select id="IqpActrecbondDetail.bond_mode" label="债权类型" cssFakeInputClass="emp_field_text_long_readonly" dictname="STD_ACTRECPO_BOND_TYPE" required="true" colSpan="2"/>
		<emp:text id="IqpActrecbondDetail.invc_no" label="权证号" maxlength="40" required="true" />
		
		<emp:text id="IqpActrecbondDetail.invc_amt" label="权证金额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		<emp:text id="IqpActrecbondDetail.cont_no" label="贸易合同编号" maxlength="40" required="true" />
		<emp:text id="IqpActrecbondDetail.bond_amt" label="债权金额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		<emp:date id="IqpActrecbondDetail.invc_date" label="权证日期"  required="true" />
		<emp:date id="IqpActrecbondDetail.bond_pay_date" label="付款日期"   required="true" />
		<emp:text id="IqpActrecbondDetail.status" label="状态" maxlength="5" required="false" hidden="true"/>
		<emp:text id="IqpActrecbondDetail.input_id" label="登记人" maxlength="30" required="false" hidden="true"/>
		<emp:text id="IqpActrecbondDetail.input_br_id" label="登记机构" maxlength="30" required="false" hidden="true"/>
		<emp:text id="IqpActrecbondDetail.input_date" label="登记日期" maxlength="10" required="false" hidden="true"/>
		<emp:text id="IqpActrecbondDetail.invc_ccy" label="发票币种" maxlength="3" required="false"  hidden="true"/>
		<!-- add by lisj 2015-1-30 需求编号【HS141110017】保理业务改造 begin -->
		<emp:text id="IqpActrecbondDetail.buy_cus_id" label="买方客户编号" maxlength="40" required="false"  hidden="true"/>
		<emp:text id="IqpActrecbondDetail.sel_cus_id" label="卖方客户编号" maxlength="40" required="false"  hidden="true"/>
		<!-- add by lisj 2015-1-30 需求编号【HS141110017】保理业务改造 end -->
	</emp:gridLayout>
	
  	<emp:tabGroup mainTab="repaytab" id="main_tabs">
		<emp:tab label="回款信息" id="repaytab" needFlush="true" initial="true" url="queryRIqpActrecRepayList.do?invc_no=${context.IqpActrecbondDetail.invc_no}&cont_no=${context.IqpActrecbondDetail.cont_no}&po_no=${context.IqpActrecbondDetail.po_no}&buy_cus_id=${context.IqpActrecbondDetail.buy_cus_id}&type=view"></emp:tab>
   	</emp:tabGroup>
   <div align="center">
	<br>
	<emp:button id="return" label="关闭"/>
  </div>	
</body>
</html>
</emp:page>
