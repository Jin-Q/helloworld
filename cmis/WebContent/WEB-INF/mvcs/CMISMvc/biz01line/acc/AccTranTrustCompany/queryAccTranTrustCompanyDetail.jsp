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
	/**add by lisj 2015-1-4 需求编号：【XD141204082】关于信托台账改造需求调整  begin **/
	function doOnLoad(){
		checkReclaimMode();
	};

	function checkReclaimMode(){
		var list_type = AccTranTrustCompany.list_type._getValue();
    	if(list_type == "1"){//1:回收
    	     AccTranTrustCompany.reclaim_mode._obj._renderHidden(false);
    	     AccTranTrustCompany.reclaim_mode._obj._renderRequired(false);
    	}else{//0:发放 
    	     AccTranTrustCompany.reclaim_mode._obj._renderHidden(true);
    	     AccTranTrustCompany.reclaim_mode._obj._renderRequired(false);
    	     AccTranTrustCompany.reclaim_mode._setValue("");
   	    }
	};	
	
	function doReturn() {
		var url = '<emp:url action="queryAccTranTrustCompanyList.do"/>?bill_no=${context.bill_no}&cont_no=${context.cont_no}&loan_balance=${context.loan_balance}';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/**add by lisj 2015-1-4 需求编号：【XD141204082】关于信托台账改造需求调整  begin **/
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<emp:gridLayout id="AccTranTrustCompanyGroup" title="信托公司贷款台账交易流水" maxColumn="2">
			<emp:text id="AccTranTrustCompany.bill_no" label="借据号" maxlength="40" required="false" />
			<emp:text id="AccTranTrustCompany.cont_no" label="合同号" maxlength="40" required="false" />
			<emp:select id="AccTranTrustCompany.list_type" label="明细类型" required="false" dictname="STD_ACC_DETAIL_TYPE"/>
			<emp:select id="AccTranTrustCompany.cur_type" label="币种" required="false" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="AccTranTrustCompany.tran_amt" label="交易金额" maxlength="16" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="AccTranTrustCompany.reclaim_mode" label="款项明细" required="false" dictname="STD_ZB_RECYCLE_TYPE"/>
			<emp:date id="AccTranTrustCompany.tran_date" label="付款日" required="false" />
			<emp:text id="AccTranTrustCompany.last_pay_date" label="上一付款日" required="false" readonly="true"/>
			<emp:text id="AccTranTrustCompany.input_id_displayname" label="登记人" required="false" readonly="true" hidden="false"/>
			<emp:text id="AccTranTrustCompany.input_br_id_displayname" label="登记机构" required="false" readonly="true" hidden="false" />
			<emp:text id="AccTranTrustCompany.input_date" label="登记日期" required="false" readonly="true"  hidden="false"/>
	        <emp:text id="AccTranTrustCompany.serno" label="交易明细编号" maxlength="40" required="false" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
