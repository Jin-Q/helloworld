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
<jsp:include page="jsCreditChange.jsp" flush="true" />
<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		if("iqpCreditChangeApp" == '<%=flag%>'){
			var url = '<emp:url action="queryIqpCreditChangeAppList.do"/>';
	    }else{
	    	var url = '<emp:url action="queryIqpCreditChangeHisList.do"/>';
		} 
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
  	
	/*--user code begin--*/
	function load(){ 
		 IqpCreditChangeApp.cont_no._obj.addOneButton("cont_no","查看",getCont);
         var credit_term_type = IqpCreditChangeApp.credit_term_type._getValue();
         if(credit_term_type == '02'){      
        	 IqpCreditChangeApp.fast_day._obj._renderHidden(false);
        	 IqpCreditChangeApp.fast_day._obj._renderRequired(true);
 		}else {
 			IqpCreditChangeApp.fast_day._setValue("");
 			IqpCreditChangeApp.fast_day._obj._renderHidden(true);
 			IqpCreditChangeApp.fast_day._obj._renderRequired(false);
 		} 
 		/** modified by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
        changeTermType(); 
        //getHLByCurr();   
        //getHLByCurr4Security();
        //getHLByCurr4Old();
        //getHLByCurr4Security4Old();
        changeRmbAmt();
		changeRmbAmt4Security();
		/** modified by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
	}; 
	function getCont(){
		var cont_no = IqpCreditChangeApp.cont_no._getValue();
		url = '<emp:url action="getCtrLoanContViewPage.do"/>?op=view&cont=cont&cont_no='+cont_no+"&flag=ctrLoanCont&menuIdTab=queryCtrLoanContHistoryList&pvp=pvp";
		url=EMPTools.encodeURI(url);  
      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	}; 
	 
	function changeTermType(){
		var credit_term_type = IqpCreditChangeApp.new_credit_term_type._getValue();
        if(credit_term_type == '02'){
        	IqpCreditChangeApp.new_fast_day._obj._renderHidden(false);
        	IqpCreditChangeApp.new_fast_day._obj._renderRequired(true);
		}else {
			IqpCreditChangeApp.new_fast_day._setValue("");
			IqpCreditChangeApp.new_fast_day._obj._renderHidden(true);
			IqpCreditChangeApp.new_fast_day._obj._renderRequired(false);
		}   
	};		
	/*--user code end--*/
	
</script>
</head> 
<body class="page_content" onload="load()">
	 <emp:tabGroup mainTab="base_tab" id="mainTab" >  
	   <emp:tab label="信用证修改申请基本信息" id="base_tab" needFlush="true" initial="true" >
	      <emp:gridLayout id="IqpCreditChangeAppGroup" title="原信用证信息" maxColumn="2" > 
			<emp:text id="IqpCreditChangeApp.serno" label="业务编号" maxlength="40" required="true" readonly="true"/>
			<emp:date id="IqpCreditChangeApp.apply_date" label="申请日期" required="false" readonly="true"/>
			<emp:text id="IqpCreditChangeApp.bill_no" label="借据编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="IqpCreditChangeApp.old_serno" label="原业务编号" maxlength="40" required="false" readonly="true"/>
			<emp:text id="IqpCreditChangeApp.cont_no" label="合同编号" maxlength="40" required="false" readonly="true" colSpan="2"/>   
			<emp:text id="IqpCreditChangeApp.prd_id" label="产品编号" maxlength="6" required="false" readonly="true"/>      
			<emp:text id="IqpCreditChangeApp.prd_id_displayname" label="产品名称" required="false" readonly="true"/>   
			<emp:text id="IqpCreditChangeApp.cus_id" label="客户码" maxlength="40" required="false" readonly="true" colSpan="2"/>   
			<emp:text id="IqpCreditChangeApp.cus_id_displayname" label="客户名称" required="false" readonly="true" colSpan="2"/>
			<emp:select id="IqpCreditChangeApp.assure_main" label="担保方式" required="false" dictname="STD_ZB_ASSURE_MEANS" readonly="true"/>  
			<emp:select id="IqpCreditChangeApp.assure_main_details" label="担保方式细分" required="false" dictname="STD_ZB_ASSUREDET_TYPE" readonly="true"/>
			<emp:select id="IqpCreditChangeApp.cont_cur_type" label="币种" required="false" dictname="STD_ZX_CUR_TYPE" readonly="true"/>   
			<emp:text id="IqpCreditChangeApp.exchange_rate" label="汇率" maxlength="16" required="false" readonly="true"/>
			<emp:text id="IqpCreditChangeApp.cont_amt" label="开证金额" maxlength="16" colSpan="2" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			
            <emp:select id="IqpCreditChangeApp.security_cur_type" label="保证金币种" defvalue="CNY" required="false" readonly="true" dictname="STD_ZX_CUR_TYPE" />
		   	<emp:text id="IqpCreditChangeApp.security_exchange_rate" label="保证金汇率" defvalue="1" maxlength="16" readonly="true" required="false" />
		    <emp:text id="IqpCreditChangeApp.security_rate" label="保证金比例" maxlength="16" required="false" dataType="Rate" readonly="true" cssElementClass="emp_currency_text_readonly"/>
		   	<emp:text id="IqpCreditChangeApp.security_amt" label="保证金金额" maxlength="16" defvalue="0"  required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
		   	
			
			<emp:text id="IqpCreditChangeApp.risk_open_amt" label="风险敞口金额" maxlength="16" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpCreditChangeApp.risk_open_rate" label="敞口比例" maxlength="10" required="false" dataType="Percent" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpCreditChangeApp.cdt_cert_no" label="信用证编号" maxlength="40" required="false" readonly="true"/>
			<emp:date id="IqpCreditChangeApp.cont_start_date" label="开证日期" required="false" readonly="true"/>
			<emp:date id="IqpCreditChangeApp.end_date" label="信用证效期" required="false" readonly="true"/>
			<emp:select id="IqpCreditChangeApp.credit_type" label="信用证类型" required="false" dictname="STD_ZB_CREDIT_TYPE" readonly="true"/>
			<emp:select id="IqpCreditChangeApp.credit_term_type" label="信用证期限类型" required="false" dictname="STD_ZB_CREDIT_DATETYPE" readonly="true"/>
			<emp:text id="IqpCreditChangeApp.fast_day" label="远期天数" maxlength="38" required="false" dataType="Int" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpCreditChangeApp.floodact_perc" label="溢装比例" maxlength="10" required="false" dataType="Percent" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpCreditChangeApp.shortact_perc" label="短装比例" maxlength="10" required="false" dataType="Percent" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="IqpCreditChangeApp.is_revolv_credit" label="是否循环信用证" required="false" dictname="STD_ZX_YES_NO" readonly="true"/>
			<emp:select id="IqpCreditChangeApp.is_ctrl_gclaim" label="是否可控货权" required="false" dictname="STD_ZX_YES_NO" readonly="true"/>    
			<emp:text id="IqpCreditChangeApp.beneficiar" label="受益人" maxlength="80" required="false" readonly="true"/>
			
			<emp:text id="IqpCreditChangeApp.same_security_amt" label="视同保证金" maxlength="16" hidden="true" required="false" dataType="Currency" readonly="true"/>
			<emp:date id="IqpCreditChangeApp.cont_end_date" label="到期日期" required="false" readonly="true" hidden="true"/>
			<emp:text id="IqpCreditChangeApp.chrg_rate" label="手续费率" maxlength="10" required="false" dataType="Rate" readonly="true" hidden="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="IqpCreditChangeAppGroup" title="信用证修改信息" maxColumn="2">
			<emp:select id="IqpCreditChangeApp.new_cur_type" label="修改后申请币种" required="true" onblur="getHLByCurr()" defvalue="CNY" dictname="STD_ZX_CUR_TYPE" />
		    <emp:text id="IqpCreditChangeApp.new_exchange_rate" label="汇率" maxlength="16" readonly="true" defvalue="1" required="true"/>
			<emp:text id="IqpCreditChangeApp.new_apply_amt" label="修改后信用证金额" maxlength="16" onblur="changeRmbAmt();changeRmbAmt4Security()" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpCreditChangeApp.new_floodact_perc" label="修改后溢装比例" maxlength="10" onblur="changeRmbAmt();changeRmbAmt4Security()" required="true" dataType="Percent" cssElementClass="emp_currency_text_readonly"/> 
			<emp:text id="IqpCreditChangeApp.new_shortact_perc" label="修改后短装比例" maxlength="10" required="true" dataType="Percent" cssElementClass="emp_currency_text_readonly"/>  
			<emp:text id="IqpCreditChangeApp.new_credit_apply_amt" label="修改后最大开证金额" maxlength="16" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpCreditChangeApp.new_rmb_amount" label="修改后折合成人民币金额" maxlength="16" required="true" dataType="Currency" readonly="true" colSpan="2" cssElementClass="emp_currency_text_readonly" />
			
			<emp:select id="IqpCreditChangeApp.new_security_cur_type" label="修改后保证金币种" defvalue='${context.IqpCreditChangeApp.security_cur_type}' onchange="getHLByCurr4Security()" readonly="true" required="true" dictname="STD_ZX_CUR_TYPE" />
		   	<emp:text id="IqpCreditChangeApp.new_security_exchange_rate" label="保证金汇率" defvalue='${context.IqpCreditChangeApp.security_exchange_rate}' maxlength="16" readonly="true" required="true" />
		   	<emp:text id="IqpCreditChangeApp.new_security_rate" label="修改后保证金比例" maxlength="16" readonly="false"  onfocus="_doKeypressDown()" onchange="changeSecRate();changeRmbAmt4SecurityChange()" required="true" dataType="Rate" cssElementClass="emp_currency_text_readonly"/>
		   	<emp:text id="IqpCreditChangeApp.new_security_amt" label="修改后保证金金额" maxlength="18" defvalue="0"  required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
		    <emp:text id="IqpCreditChangeApp.new_security_rmb_rate" label="保证金折算人民币金额" maxlength="18" readonly="true" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		    <emp:text id="IqpCreditChangeApp.new_add_security_rmb_rate" label="修改后追加保证金金额" maxlength="18" readonly="true" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		    <emp:text id="IqpCreditChangeApp.new_risk_open_amt" label="风险敞口金额（元）" maxlength="18" onchange="riskOpenAmtChange()" readonly="true" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
		    <emp:text id="IqpCreditChangeApp.new_risk_open_rate" label="敞口比率" maxlength="10" readonly="true" required="true" dataType="Percent" cssElementClass="emp_currency_text_readonly"/>
		    
			<emp:select id="IqpCreditChangeApp.new_assure_main" label="修改后担保方式" required="true" onchange="assure_mainChange()" dictname="STD_ZB_ASSURE_MEANS"/>
			<emp:select id="IqpCreditChangeApp.new_assure_main_details" label="修改后担保方式细分" required="true" dictname="STD_ZB_ASSUREDET_TYPE"/>   
			
			<emp:select id="IqpCreditChangeApp.new_credit_term_type" label="修改后信用证期限类型" required="true" onchange="changeTermType();" dictname="STD_ZB_CREDIT_DATETYPE"/>
			<emp:select id="IqpCreditChangeApp.new_credit_type" label="修改后信用证类型" required="true" dictname="STD_ZB_CREDIT_TYPE"/>
			<emp:text id="IqpCreditChangeApp.new_fast_day" label="修改后远期天数" maxlength="38" required="false" dataType="Int" hidden="true"/>   
			<emp:date id="IqpCreditChangeApp.new_end_date" label="修改后信用证效期" required="true" />    
			<emp:textarea id="IqpCreditChangeApp.remarks" label="备注" maxlength="250" required="false" colSpan="2" />
		</emp:gridLayout>  
		<emp:gridLayout id="IqpCreditChangeAppGroup" title="登记信息" maxColumn="3">      
			<emp:pop id="IqpCreditChangeApp.manager_br_id_displayname" label="管理机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID"/>
			<emp:text id="IqpCreditChangeApp.input_id_displayname" label="登记人" required="true" readonly="true"/>   
			<emp:text id="IqpCreditChangeApp.input_br_id_displayname" label="登记机构" required="true" readonly="true"/>   
			<emp:text id="IqpCreditChangeApp.input_date" label="登记日期" maxlength="10" required="true" readonly="true"/>     
			<emp:select id="IqpCreditChangeApp.approve_status" label="申请状态" required="false" dictname="WF_APP_STATUS" defvalue="000" hidden="true"/>     
			
			<emp:text id="IqpCreditChangeApp.manager_br_id" label="管理机构" maxlength="20" required="false" hidden="true"/>
			<emp:text id="IqpCreditChangeApp.input_id" label="登记人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="IqpCreditChangeApp.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true"/> 
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
