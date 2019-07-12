<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%  
    //added by yangzy 2015/01/28  需求:XD141230092,零售、首次贷后检查改造 start	
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String check_type = "";//检查类型
	if(context.containsKey("check_type")){
		check_type = (String)context.getDataValue("check_type");
	}
	//added by yangzy 2015/01/28  需求:XD141230092,零售、首次贷后检查改造 end	
%>
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
		//var task_id = PspCapUseMonitor.task_id._getValue();
		//var url = '<emp:url action="queryPspCapUseMonitorList.do"/>?task_id='+task_id;
		//url = EMPTools.encodeURI(url);
		//window.location=url;
		history.go(-1);
	};
	/*--user code begin--*/
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" >
	
	<emp:gridLayout id="PspCapUseMonitorGroup" title="资金用途监控" maxColumn="2">
			<emp:pop id="PspCapUseMonitor.bill_no" label="借据编号" readonly="true" url="queryAccLoanPop.do?returnMethod=selAccInfo" colSpan="2" />
			<emp:date id="PspCapUseMonitor.disb_date" label="用款日期" required="true" />
			<emp:select id="PspCapUseMonitor.cur_type" label="放款币种"  dictname="STD_ZX_CUR_TYPE" readonly="true"/>
			<emp:select id="PspCapUseMonitor.is_exchang_setl" label="是否结汇"  dictname="STD_ZX_YES_NO" readonly="true"/>
			<emp:text id="PspCapUseMonitor.exchang_setl_amt" label="结汇金额" maxlength="16" required="true" dataType="Currency" readonly="true"/>
			<emp:text id="PspCapUseMonitor.disb_amt" label="用款金额" maxlength="16"  dataType="Currency" readonly="true"/>
			<emp:text id="PspCapUseMonitor.pyee_name" label="收款人名称" maxlength="100" required="true" />
			<emp:text id="PspCapUseMonitor.paorg_acct_no" label="收款人账号" maxlength="40" required="true" />
			<emp:pop id="PspCapUseMonitor.paorg_no" label="收款人开户行行号" url="getPrdBankInfoPopList.do" returnMethod="getToorgNo" required="true" buttonLabel="选择" />
			<emp:text id="PspCapUseMonitor.paorg_name" label="收款人开户行名称" maxlength="100" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_cusname_readonly"/>
			<emp:select id="PspCapUseMonitor.use_type" label="实际用途" required="true" dictname="STD_CAP_USE_TYPE"/>
			<emp:select id="PspCapUseMonitor.is_cash" label="支取方式" required="true" dictname="STD_ZB_RENT_COLL_TYPE"/>
			<emp:text id="PspCapUseMonitor.proof_type" label="证明材料" maxlength="250" required="true" cssElementClass="emp_field_text_cusname" colSpan="2" />
			<emp:select id="PspCapUseMonitor.is_cap_back" label="是否短期内资金回流借款人" required="true" dictname="STD_ZX_YES_NO"/>
			<emp:select id="PspCapUseMonitor.debit_percn_rela" label="收款人是否上游往来客户" required="true" dictname="STD_ZX_YES_NO"/>
			<emp:select id="PspCapUseMonitor.is_move_loan" label="是否符合计划及约定用途" required="true" dictname="STD_ZX_YES_NO"/>
			<%if("01".equals(check_type)){ 
			//modified by yangzy 2015/01/28  需求:XD141230092,零售、首次贷后检查改造 start	
			%>
			<emp:textarea id="PspCapUseMonitor.memo" label="补充说明" maxlength="250" required="false" colSpan="2"/>
			<%}else{ 
			%>
			<emp:textarea id="PspCapUseMonitor.memo" label="首次检查结论" maxlength="250" required="false" colSpan="2"/>
			<%}
			//modified by yangzy 2015/01/28  需求:XD141230092,零售、首次贷后检查改造 end	
			%>
			<emp:text id="PspCapUseMonitor.pk_id" label="主键" maxlength="32" readonly="true" hidden="true" />
			<emp:text id="PspCapUseMonitor.task_id" label="任务编号" hidden="true" />
			<emp:text id="PspCapUseMonitor.cus_id" label="客户码" maxlength="40" hidden="true" />
		</emp:gridLayout>
		
		<emp:gridLayout id="" title="登记信息" maxColumn="2">
			<emp:text id="PspCapUseMonitor.input_id_displayname" label="登记人" required="false" readonly="true"/>
			<emp:text id="PspCapUseMonitor.input_br_id_displayname" label="登记机构" required="false" readonly="true"/>
			<emp:date id="PspCapUseMonitor.input_date" label="登记日期" required="false" readonly="true"/>
			
			<emp:text id="PspCapUseMonitor.input_id" label="登记人" maxlength="40" required="false" hidden="true" />
			<emp:text id="PspCapUseMonitor.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" />
		</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表"/>
	</div>
</body>
</html>
</emp:page>
