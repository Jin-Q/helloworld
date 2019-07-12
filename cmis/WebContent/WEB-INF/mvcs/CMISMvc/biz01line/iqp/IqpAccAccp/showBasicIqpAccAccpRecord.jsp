<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	String cont="";
	String prd_id = "";
	if(context.containsKey("prd_id")){
	    	prd_id = (String)context.getDataValue("prd_id");
	    }
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
		if(op.equals("view")){   
			request.setAttribute("canwrite","");
		}
	}
	if(context.containsKey("cont")){
		cont = (String)context.getDataValue("cont");
		if(cont.equals("cont")){   
			request.setAttribute("canwrite","");
		}
	}
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	/*--user code begin--*/
      function load(){
    	var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var bill_qnt = jsonstr.bill_qnt;
				var prd_id = jsonstr.prd_id;
				if(flag == "success"){
					if(prd_id == "200024"){
						IqpAccAccp.bill_qnt._setValue(bill_qnt); 
						var is_elec_bill = IqpAccAccp.is_elec_bill._getValue();
	    				if(is_elec_bill == '1'){
	    					IqpAccAccp.actp_org_no._obj._renderReadonly(true);
	        			}  
					}
				}else {
					alert("保存失败！"+jsonstr.msg);
					//alert("保存失败！");
				}
			}
		};
		var handleFailure = function(o){
			alert("异步请求出错！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var url = '<emp:url action="updateBillQntRecord.do"/>?serno=${context.serno}&cont_no=${context.cont_no}';
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content"  onload="load();">
	
	<emp:form id="submitForm" action="updateIqpAccAccpRecord.do" method="POST">
	<%if("200024".equals(prd_id)){%>
		<emp:gridLayout id="CtrLoanContGroup" title="银承业务基本信息" maxColumn="2">
			<emp:text id="CtrLoanCont.cus_id" label="客户码" colSpan="2" readonly="true" required="true" />
			<emp:text id="CtrLoanCont.cus_id_displayname" label="客户名称 " colSpan="2" readonly="true" cssElementClass="emp_field_text_long_readonly"/>
			<emp:select id="CtrLoanCont.assure_main" label="担保方式"  dictname="STD_ZB_ASSURE_MEANS" readonly="true" required="true" /> 
			<emp:select id="IqpAccAccp.is_elec_bill" label="是否电子票据"  readonly="true"  dictname="STD_ZX_YES_NO" required="true" /> 
			<emp:text id="CtrLoanCont.security_rate" label="保证金比例" readonly="true" dataType="Rate" required="true" />
			<emp:text id="IqpAccAccp.bill_qnt" label="汇票金额" readonly="true" dataType="Currency" required="true" />
		</emp:gridLayout>
	<%}else if("400020".equals(prd_id) || "400021".equals(prd_id)){ %>
		<emp:gridLayout id="CtrLoanContGroup" title="保函业务基本信息" maxColumn="2">
			<emp:text id="CtrLoanCont.cus_id" label="客户码" colSpan="2" readonly="true" required="true" />
			<emp:text id="CtrLoanCont.cus_id_displayname" label="客户名称 " colSpan="2" readonly="true" cssElementClass="emp_field_text_long_readonly"/>
			<emp:text id="IqpGuarantInfo.ben_name" label="受益人 " required="true" colSpan="2" readonly="true" cssElementClass="emp_field_text_long_readonly"/>
			<emp:select id="CtrLoanCont.assure_main" label="担保方式" required="true" dictname="STD_ZB_ASSURE_MEANS" />
			<emp:text id="CtrLoanCont.security_rate" label="保证金比例" readonly="true" dataType="Rate" required="true" />
			 <emp:text id="CtrLoanContSub.cont_term" label="期限" required="true"  readonly="true"/>					
		    <emp:select id="CtrLoanContSub.term_type" label="类型" required="true"  readonly="true" dictname="STD_ZB_TERM_TYPE" />	 
			<emp:text id="PvpLoanApp.pvp_amt" label="申请金额" maxlength="100" required="true" readonly="true" colSpan="2" dataType="Currency"/>
		</emp:gridLayout>
	<%}else if("300020".equals(prd_id) || "300021".equals(prd_id) || "300022".equals(prd_id) || "300023".equals(prd_id) || "300024".equals(prd_id)){ %>
		<emp:gridLayout id="CtrDiscContGroup" title="贴现业务基本信息" maxColumn="2">
			<emp:text id="CtrLoanCont.cus_id" label="客户码" colSpan="2" readonly="true" required="true"/>
			<emp:text id="CtrLoanCont.cus_id_displayname" label="客户名称 " colSpan="2" readonly="true" cssElementClass="emp_field_text_long_readonly"/>
			<emp:select id="CtrDiscCont.bill_type" label="票据种类" readonly ="true" dictname="STD_DRFT_TYPE" required="true"/>
			<emp:select id="CtrDiscCont.is_elec_bill" label="是否电子票据" readonly="true" dictname="STD_ZX_YES_NO" required="true"/> 
			<emp:select id="CtrDiscCont.dscnt_int_pay_mode" label="付息方式" readonly="true" dictname="STD_ZB_DSCNT_DEFRAY_MODE" /> 
			<emp:text id="PvpLoanApp.pvp_amt" label="票面总金额" maxlength="100"  readonly="true" dataType="Currency" required="true"/>
		</emp:gridLayout>
	<%}else{ %>
		<emp:gridLayout id="CtrLoanContGroup" title="贷款业务基本信息" maxColumn="2">
			<emp:text id="CtrLoanCont.cus_id" label="客户码" colSpan="2" readonly="true" required="true" />
			<emp:text id="CtrLoanCont.cus_id_displayname" label="客户名称 " colSpan="2" readonly="true" cssElementClass="emp_field_text_long_readonly"/>
			<emp:text id="CtrLoanCont.cont_amt" label="合同金额" maxlength="18" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="CtrLoanContSub.loan_nature" label="贷款性质" dictname="STD_LOAN_NATYRE" readonly="true" required="true" />
			<emp:select id="CtrLoanCont.assure_main" label="担保方式"  dictname="STD_ZB_ASSURE_MEANS" readonly="true" required="true" />
			<emp:select id="CtrLoanContSub.is_close_loan" label="是否无间贷" hidden="true" dictname="STD_ZX_YES_NO" readonly="true" required="false" />
			<emp:select id="CtrLoanContSub.pay_type" label="支付方式" dictname="STD_IQP_PAY_TYPE" readonly="true" required="true" />
			<emp:text id="CtrLoanContSub.repay_type_displayname" label="还款方式" readonly="true" required="true" />
			<emp:text id="CtrLoanContSub.cont_term" label="合同期限" readonly="true" required="true" />					
		    <emp:select id="CtrLoanContSub.term_type" label="期限类型" readonly="true" dictname="STD_ZB_TERM_TYPE" required="true" />
			<emp:text id="CtrLoanContSub.ir_float_rate" label="利率浮动比(年)" dataType="Rate"/>  
			<!-- added by yangzy 2015/09/15  贷款基本信息增加展示  begin -->
			<emp:text id="CtrLoanContSub.reality_ir_y" cssElementClass="emp_currency_text_readonly" label="执行利率（年）" readonly="true" maxlength="16" required="true" dataType="Rate"/>
			<emp:select id="CtrLoanContSub.limit_useed_type" label="额度占用来源" required="true" dictname="STD_POSITION_ENGROSS_ORIGIN" readonly="true"/>
			<!-- added by yangzy 2015/09/15  贷款基本信息增加展示  end -->
		</emp:gridLayout>
	<%}%>
	</emp:form>
</body>
</html>
</emp:page>
