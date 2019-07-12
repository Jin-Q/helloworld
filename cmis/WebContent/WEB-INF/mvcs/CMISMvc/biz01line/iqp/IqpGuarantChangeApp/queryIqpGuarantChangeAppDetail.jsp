<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String flag = "";
	String isShowButton = "";
	if(context.containsKey("flag")){
		flag = (String)context.getDataValue("flag");
	}
	if(context.containsKey("isShowButton")){
		isShowButton = (String)context.getDataValue("isShowButton");
	}
%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="jsGuarantChange.jsp" flush="true" />
<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn(){
		if('iqpGuarantChangeApp' == '<%=flag%>'){
			var url = '<emp:url action="queryIqpGuarantChangeAppList.do"/>';
		}else{
			var url = '<emp:url action="queryIqpGuarantChangeAppHistory.do"/>';     
		}
		url = EMPTools.encodeURI(url);   
		window.location=url; 
	};    
	function onload(){
		IqpGuarantChangeApp.cont_no._obj.addOneButton("cont_no","查看",getCont);
		/** modified by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
		//getHLByCurr();
		//getHLByCurr4Security();
		changeSecRate();
		//getHLByCurr4Old();
        //getHLByCurr4Security4Old();
		changeRmbAmt();
		changeRmbAmt4Security();
		/** modified by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
	};     
	function getCont(){
		var cont_no = IqpGuarantChangeApp.cont_no._getValue();
		url = '<emp:url action="getCtrLoanContViewPage.do"/>?op=view&cont=cont&cont_no='+cont_no+"&flag=ctrLoanCont&menuIdTab=queryCtrLoanContHistoryList&pvp=pvp";
		url=EMPTools.encodeURI(url);  
      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	}; 
	
	/*--user code begin--*/
		 	
	/*--user code end--*/
	 
</script>
</head>
<body class="page_content" onload="onload()">  
  <emp:tabGroup mainTab="base_tab" id="mainTab" >         
	   <emp:tab label="信用证修改申请基本信息" id="base_tab" needFlush="true" initial="true" >
	     <emp:gridLayout id="IqpGuarantChangeAppGroup" title="保函修改申请表" maxColumn="2">
			<emp:text id="IqpGuarantChangeApp.serno" label="业务编号" maxlength="40" required="true" readonly="true" />
			<emp:date id="IqpGuarantChangeApp.apply_date" label="申请日期" required="false" readonly="true"/>
			<emp:text id="IqpGuarantChangeApp.bill_no" label="借据编号" maxlength="40" required="false" readonly="true" />
			<emp:text id="IqpGuarantChangeApp.old_serno" label="原业务编号" maxlength="40" required="false" readonly="true" />
			<emp:text id="IqpGuarantChangeApp.cont_no" label="合同编号" maxlength="40" required="false" readonly="true" colSpan="2"/>    
			<emp:text id="IqpGuarantChangeApp.prd_id" label="产品编号" maxlength="6" required="false" readonly="true" />
			<emp:text id="IqpGuarantChangeApp.prd_id_displayname" label="产品名称" required="false" readonly="true"/>
			<emp:text id="IqpGuarantChangeApp.cus_id" label="客户码" maxlength="40" required="false" readonly="true" colSpan="2"/>   
			<emp:text id="IqpGuarantChangeApp.cus_id_displayname" label="客户名称" required="false" readonly="true" colSpan="2" />
			<emp:select id="IqpGuarantChangeApp.assure_main" label="担保方式" required="false" dictname="STD_ZB_ASSURE_MEANS" readonly="true" />
			<emp:select id="IqpGuarantChangeApp.assure_main_details" label="担保方式细分" required="false" dictname="STD_ZB_ASSUREDET_TYPE" readonly="true" />
			<emp:select id="IqpGuarantChangeApp.cont_cur_type" label="币种" required="false" dictname="STD_ZX_CUR_TYPE" readonly="true" /> 
			<emp:text id="IqpGuarantChangeApp.exchange_rate" label="汇率" maxlength="16" required="false" readonly="true" />
			<emp:text id="IqpGuarantChangeApp.cont_amt" label="保函金额" maxlength="16" required="false" dataType="Currency" readonly="true" colSpan="2" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpGuarantChangeApp.apply_rmb_amount" label="折合成人民币金额" maxlength="18" readonly="true" required="false" dataType="Currency" colSpan="2" cssElementClass="emp_currency_text_readonly"/>
			
			<emp:select id="IqpGuarantChangeApp.security_cur_type" label="保证金币种" defvalue="CNY" required="false" readonly="true" dictname="STD_ZX_CUR_TYPE" />
		   	<emp:text id="IqpGuarantChangeApp.security_exchange_rate" label="保证金汇率" defvalue="1" maxlength="16" readonly="true" required="false" />
		    <emp:text id="IqpGuarantChangeApp.security_rate" label="保证金比例" maxlength="16" required="false" dataType="Rate" readonly="true" cssElementClass="emp_currency_text_readonly"/>
		   	<emp:text id="IqpGuarantChangeApp.security_amt" label="保证金金额" maxlength="16" defvalue="0"  required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpGuarantChangeApp.security_rmb_rate" label="保证金折算人民币金额" maxlength="18" readonly="true" required="false" dataType="Currency" colSpan="2" cssElementClass="emp_currency_text_readonly"/>
			
			<emp:text id="IqpGuarantChangeApp.risk_open_amt" label="风险敞口金额" maxlength="16" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpGuarantChangeApp.risk_open_rate" label="敞口比例" maxlength="10" required="false" dataType="Percent" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:date id="IqpGuarantChangeApp.cont_start_date" label="起始日期" required="false" readonly="true" />
			<emp:date id="IqpGuarantChangeApp.cont_end_date" label="到期日期" required="false" readonly="true" />
			<emp:select id="IqpGuarantChangeApp.guarant_type" label="保函种类" required="false" dictname="STD_ZB_GUARANT_MODEL" readonly="true" />
			<emp:select id="IqpGuarantChangeApp.guarant_mode" label="保函类型" required="false" readonly="true" dictname="STD_ZB_GUARANT_TYPE"/>
			<emp:date id="IqpGuarantChangeApp.end_date" label="保函到期日" required="false" readonly="true" />
			<emp:select id="IqpGuarantChangeApp.open_type" label="开立类型" required="false" dictname="STD_ZB_OPEN_TYPE" readonly="true" />
			<emp:select id="IqpGuarantChangeApp.is_bank_format" label="是否我行标准格式" required="false" dictname="STD_ZX_YES_NO" readonly="true" />
			<emp:text id="IqpGuarantChangeApp.ben_name" label="受益人名称" maxlength="80" required="false" readonly="true" />
			<emp:select id="IqpGuarantChangeApp.guarant_pay_type" label="保函付款方式" required="false" dictname="STD_GUARANT_PAY_TYPE" readonly="true" />
			
			<emp:text id="IqpGuarantChangeApp.same_security_amt" label="视同保证金" maxlength="16" hidden="true" required="false" dataType="Currency" readonly="true" />
			<emp:text id="IqpGuarantChangeApp.chrg_rate" label="手续费率" maxlength="10" required="false" dataType="Rate" hidden="true" readonly="true"/>
			<emp:select id="IqpGuarantChangeApp.is_agt_guarant" label="是否转开代理行保函" required="false" dictname="STD_ZX_YES_NO" hidden="true" readonly="true" />
			<emp:text id="IqpGuarantChangeApp.agt_bank_no" label="代理行行号" maxlength="20" required="false" readonly="true" hidden="true"/>
			<emp:text id="IqpGuarantChangeApp.agt_bank_name" label="代理行名称" maxlength="100" required="false" readonly="true" hidden="true"/>
			<emp:text id="IqpGuarantChangeApp.item_name" label="项目名称" maxlength="80" required="false" readonly="true" hidden="true"/>
			<emp:text id="IqpGuarantChangeApp.guarant_cont_no" label="商务合同编号" maxlength="40" required="false" readonly="true" hidden="true"/>
			<emp:text id="IqpGuarantChangeApp.cont_name" label="商务合同名称" maxlength="100" required="false" readonly="true" hidden="true"/>
			<emp:text id="IqpGuarantChangeApp.item_amt" label="商务合同金额" maxlength="16" required="false" readonly="true" hidden="true" />
			<emp:text id="IqpGuarantChangeApp.guarant_cur_type" label="商务合同币种" dictname="STD_ZX_CUR_TYPE" maxlength="3" required="false" readonly="true" hidden="true"/>
		    <emp:text id="IqpGuarantChangeApp.ben_addr" label="受益人地址" maxlength="150" required="false" readonly="true" hidden="true"/>
			<emp:text id="IqpGuarantChangeApp.ben_acct_org_no" label="受益人开户行行号" maxlength="20" required="false" readonly="true" hidden="true"/>
			<emp:text id="IqpGuarantChangeApp.ben_acct_no" label="受益人账号" maxlength="40" required="false" readonly="true" hidden="true"/>
			<emp:text id="IqpGuarantChangeApp.corre_busnes_cont_amt" label="相关贸易合同金额" maxlength="16" required="false" dataType="Currency" readonly="true" hidden="true"/>
		</emp:gridLayout> 
		<emp:gridLayout id="IqpGuarantChangeAppGroup" title="保函修改信息" maxColumn="2">	
			<emp:select id="IqpGuarantChangeApp.new_cur_type" label="修改后申请币种" required="true" onblur="getHLByCurr()" defvalue="CNY" dictname="STD_ZX_CUR_TYPE" />
		    <emp:text id="IqpGuarantChangeApp.new_exchange_rate" label="汇率" maxlength="16" readonly="true" defvalue="1" required="true"/>
			<emp:text id="IqpGuarantChangeApp.new_cont_amt" label="修改后保函金额" maxlength="16" onblur="changeRmbAmt();changeRmbAmt4Security()"  required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" />
			<emp:text id="IqpGuarantChangeApp.new_rmb_amount" label="修改后折合成人民币金额" maxlength="16" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly" />
			
			<emp:select id="IqpGuarantChangeApp.new_security_cur_type" label="修改后保证金币种" defvalue='${context.IqpGuarantChangeApp.security_cur_type}' onchange="getHLByCurr4Security()" readonly="true" required="true" dictname="STD_ZX_CUR_TYPE" />
		   	<emp:text id="IqpGuarantChangeApp.new_security_exchange_rate" label="保证金汇率" defvalue='${context.IqpGuarantChangeApp.security_exchange_rate}' maxlength="16" readonly="true" required="true" />
		   	<emp:text id="IqpGuarantChangeApp.new_security_rate" label="修改后保证金比例" maxlength="16" required="true" defvalue='${context.IqpGuarantChangeApp.security_rate}' readonly="true" cssElementClass="emp_currency_text_readonly" dataType="Rate" />
		   	<emp:text id="IqpGuarantChangeApp.new_security_amt" label="修改后保证金金额" maxlength="18" defvalue="0"  required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
		    <emp:text id="IqpGuarantChangeApp.new_security_rmb_rate" label="保证金折算人民币金额" maxlength="18" readonly="true" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		    <emp:text id="IqpGuarantChangeApp.new_add_security_rmb_rate" label="修改后追加保证金金额" maxlength="18" readonly="true" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		    <emp:text id="IqpGuarantChangeApp.new_risk_open_amt" label="风险敞口金额（元）" maxlength="18" onchange="riskOpenAmtChange()" readonly="true" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		    <emp:text id="IqpGuarantChangeApp.new_risk_open_rate" label="敞口比率" maxlength="10" readonly="true" required="true" dataType="Percent" cssElementClass="emp_currency_text_readonly"/>
		    
			<emp:select id="IqpGuarantChangeApp.new_assure_main" label="修改后担保方式" required="true" onchange="assure_mainChange()" dictname="STD_ZB_ASSURE_MEANS" />
			<emp:select id="IqpGuarantChangeApp.new_assure_main_details" label="修改后担保方式细分" required="true" dictname="STD_ZB_ASSUREDET_TYPE" />
			<emp:select id="IqpGuarantChangeApp.new_guarant_type" label="保函种类" required="true" dictname="STD_ZB_GUARANT_MODEL" onchange="doChange()"/>
			<emp:select id="IqpGuarantChangeApp.new_guarant_mode" label="保函类型" required="true" dictname="STD_ZB_GUARANT_TYPE" readonly="true"/>  
			<emp:select id="IqpGuarantChangeApp.new_is_bank_format" label="是否我行标准格式" required="true"  dictname="STD_ZX_YES_NO" />
            <emp:date id="IqpGuarantChangeApp.new_cont_end_date" label="修改后保函到期日" required="true"/>
            <emp:text id="IqpGuarantChangeApp.new_ben_name" label="受益人名称" maxlength="80" required="true" /> 			
			<emp:textarea id="IqpGuarantChangeApp.remarks" label="备注" maxlength="250" required="false" colSpan="2"/>  
		</emp:gridLayout>
		<emp:gridLayout id="IqpGuarantChangeAppGroup" title="登记信息" maxColumn="3">
			<emp:pop id="IqpGuarantChangeApp.manager_br_id_displayname" label="管理机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID"/>
			<emp:text id="IqpGuarantChangeApp.input_id_displayname" label="登记人" required="true" readonly="true"/>      
			<emp:text id="IqpGuarantChangeApp.input_br_id_displayname" label="登记机构" required="true" readonly="true"/>   
			<emp:text id="IqpGuarantChangeApp.input_date" label="登记日期" maxlength="10" required="true" readonly="true"/>     
			<emp:select id="IqpGuarantChangeApp.approve_status" label="申请状态" required="false" dictname="WF_APP_STATUS" defvalue="000" hidden="true"/>     
			
			<emp:text id="IqpGuarantChangeApp.manager_br_id" label="管理机构" maxlength="20" required="false" hidden="true"/>
			<emp:text id="IqpGuarantChangeApp.input_id" label="登记人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="IqpGuarantChangeApp.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true"/>  
	</emp:gridLayout>
	</emp:tab>    
		<emp:ExtActTab></emp:ExtActTab>  
		</emp:tabGroup>   
	<div align="center">    
		<br>
		<%if(!"no".equals(isShowButton)){ %>
		<emp:button id="return" label="返回到列表页面"/>
		<%} %>
	</div>
</body>
</html>
</emp:page>
