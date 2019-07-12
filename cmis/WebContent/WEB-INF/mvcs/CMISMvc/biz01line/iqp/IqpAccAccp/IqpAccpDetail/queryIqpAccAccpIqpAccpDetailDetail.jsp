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
	
	function doReturn(){
		window.close();
	};

	function doOnLoad(){
		var porder_no = IqpAccpDetail.porder_no._getValue();
		if(porder_no == null || porder_no == ''){
			IqpAccpDetail.batch_no._obj._renderHidden(true);
			IqpAccpDetail.porder_no._obj._renderHidden(true);
			IqpAccpDetail.bill_isse_date._obj._renderHidden(true);
			IqpAccpDetail.bill_expiry_date._obj._renderHidden(true);

		}
	}
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
<style type="text/css">
.emp_field_input {
	   border: 1px solid #b7b7b7;
	  text-align:left;
	  width:200px;
};
.emp_input2{
border: 1px solid #b7b7b7;
width:600px;
}
</style>
</head>
<body class="page_content" onload="doOnLoad();">
	<emp:gridLayout id="IqpAccpDetailGroup" title="承兑汇票申请明细" maxColumn="2">
		<emp:text id="IqpAccpDetail.clt_person" label="收款人" maxlength="40" required="true" colSpan="2" cssElementClass="emp_input2"/>
		<emp:text id="IqpAccpDetail.clt_acct_no" label="收款人账号" maxlength="40" required="true" dataType="Acct" cssElementClass="emp_field_input" colSpan="2"/>
		<emp:text id="IqpAccpDetail.paorg_no" label="收款人开户行行号" maxlength="20" required="true" />
		<emp:text id="IqpAccpDetail.paorg_name" label="收款人开户行行名" maxlength="100" required="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
		<emp:text id="IqpAccpDetail.batch_no" label="票据批次" maxlength="40" required="true" />
		<emp:text id="IqpAccpDetail.porder_no" label="汇票号码" maxlength="40" required="true" />
		<emp:text id="IqpAccpDetail.bill_isse_date" label="出票日期" maxlength="40" required="true" />
		<emp:text id="IqpAccpDetail.bill_expiry_date" label="到期日期" maxlength="40" required="true" />
		<emp:text id="IqpAccpDetail.drft_amt" label="票面金额" maxlength="18" required="true" dataType="Currency" colSpan="2"/>
		<emp:select id="IqpAccpDetail.term_type" label="期限类型" required="true" dictname="STD_ZB_TERM_TYPE" />
		<emp:text id="IqpAccpDetail.term" label="期限" maxlength="38" required="true" />
		<emp:text id="IqpAccpDetail.serno" label="业务编号" maxlength="40" required="true" hidden="true"/>
		<emp:text id="IqpAccpDetail.pk1" label="承兑汇票申请明细流水号" maxlength="40" required="true" hidden="true"/>
	</emp:gridLayout> 
		
	<div align="center">
		<br>
		<emp:button id="return" label="关闭"/>
	</div>
</body>
</html>
</emp:page>
