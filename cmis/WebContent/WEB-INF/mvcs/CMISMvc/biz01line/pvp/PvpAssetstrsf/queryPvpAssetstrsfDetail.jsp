<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<jsp:include page="jsPvpComm.jsp" flush="true" /> 
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<%
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
    String flag = "";
    if(context.containsKey("flag")){
    	flag = (String)context.getDataValue("flag");
    }

    request.setAttribute("canwrite","");
%>
<emp:page> 
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<style type="text/css">
.emp_input{
border:1px solid #b7b7b7;
width:180px;
}

.emp_input2{
border:1px solid #b7b7b7;
width:430px;
}
</style>
<script type="text/javascript">
	function doReturn1() { 
		if('<%=flag%>'=="assetstrs"){
	        var url = '<emp:url action="queryPvpAssetstrsfList.do"/>';
	    }else if('<%=flag%>'=="assetstrsHis"){
	        var url = '<emp:url action="queryPvpAssetstrsfHistoryList.do"/>';
	    }else if('<%=flag%>'=="pvpLoan"){
        	var url = '<emp:url action="queryPvpRpddscantHistoryList.do"/>';
        }else{
        	var url = '<emp:url action="queryPvpRpddscantList.do"/>';
        }
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	function doReturn() {
		window.close();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
		  <emp:tabGroup mainTab="base_tab" id="mainTab" >
		 <emp:tab label="出账信息" id="base_tab" needFlush="true" initial="true" >
          <emp:gridLayout id="CtrLoanContGroup" maxColumn="2" title="出账基本信息">
			<emp:text id="PvpLoanApp.serno" label="业务编号" maxlength="40" required="true" readonly="true" hidden="false" colSpan="2"/>	
			<emp:text id="PvpLoanApp.prd_id" label="产品编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="PvpLoanApp.prd_id_displayname" label="产品名称"   required="false" readonly="true"/>
			<emp:text id="PvpLoanApp.cus_id" label="交易对手行号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="PvpLoanApp.toorg_name" label="交易对手行名" maxlength="80" required="false" colSpan="2" readonly="true" cssElementClass="emp_input2"/>
			<emp:text id="PvpLoanApp.cont_no" label="合同编号" maxlength="40" required="false" readonly="true" cssElementClass="emp_input" colSpan="2"/> 
			<emp:text id="PvpLoanApp.bill_no" label="借据编号" maxlength="40" required="false" cssElementClass="emp_input" hidden="true"/> 	
		  </emp:gridLayout>        
          
          <emp:gridLayout id="" maxColumn="2" title="金额信息">
		    <emp:text id="PvpLoanApp.cont_amt" label="合同金额" maxlength="18" required="false" dataType="Currency" readonly="true"/>
		    <emp:select id="PvpLoanApp.cur_type" label="币种"  required="false" dictname="STD_ZX_CUR_TYPE" readonly="true"/>
		    <emp:text id="PvpLoanApp.pvp_amt" label="出账金额" maxlength="18" required="true"  dataType="Currency"/>
		    <emp:text id="PvpLoanApp.cont_balance" label="合同余额" maxlength="18" required="false" dataType="Currency" readonly="true" hidden="true"/>	
		</emp:gridLayout>
		<emp:gridLayout id="" maxColumn="3" title="登记信息">   
		  	<emp:text id="PvpLoanApp.manager_br_id_displayname" label="管理机构"   required="false" readonly="true"/>
		    <emp:pop id="PvpLoanApp.in_acct_br_id_displayname" label="入账机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" buttonLabel="选择" required="true"/>
		    <emp:select id="PvpLoanApp.flow_type" label="流程类型" dictname="STD_ZB_FLOW_TYPE" defvalue="01" required="false"/>    
		    <emp:text id="PvpLoanApp.input_id_displayname" label="登记人"   required="false" readonly="true"/>
			<emp:text id="PvpLoanApp.input_br_id_displayname" label="登记机构"   required="false" readonly="true"/>
			<emp:date id="PvpLoanApp.input_date" label="登记日期" required="false" readonly="true"/>
			<emp:select id="PvpLoanApp.approve_status" label="审批状态" required="false" hidden="true" dictname="WF_APP_STATUS"/>
		    <emp:text id="PvpLoanApp.manager_br_id" label="管理机构" maxlength="20" hidden="true" required="false" readonly="true"/>
		    <emp:pop id="PvpLoanApp.in_acct_br_id" label="入账机构" hidden="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" buttonLabel="选择" required="true"/>
		     <emp:text id="PvpLoanApp.input_id" label="登记人" hidden="true" maxlength="20" required="false" readonly="true"/>
			<emp:text id="PvpLoanApp.input_br_id" label="登记机构" hidden="true" maxlength="20" required="false" readonly="true"/>
		 </emp:gridLayout>
		 
		</emp:tab>
		<emp:tab label="资产转受让合同信息" id="subTab" url="getCtrAssetstrsfContViewPage.do?cont_no=${context.PvpLoanApp.cont_no}&menuIdTab=queryCtrAssetstrsfHistoryList&op=view&pvp=pvp" initial="false" needFlush="true"/>
	</emp:tabGroup>
	<div align="center">
	<br>
		<emp:button id="return" label="返回列表"/>
	</div>
</body>
</html>
</emp:page>
