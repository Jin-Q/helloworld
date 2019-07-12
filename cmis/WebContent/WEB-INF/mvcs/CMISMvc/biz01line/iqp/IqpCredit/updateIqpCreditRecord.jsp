<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	String cont ="";
	String menuId ="";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
		if("view".equals(op)){
			request.setAttribute("canwrite","");
		}    
	}
	if(context.containsKey("cont")){
		cont = (String)context.getDataValue("cont");
		if("cont".equals(cont)){   
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
	function doOnLoad(){
		changeTerm();   
		cdtCertNo(); 
		checkIsCert();
	};

    function checkIsCert(){
        var prd_id = "${context.prd_id}";
    	if("700020" == prd_id){
    		IqpCredit.is_internal_cert._setValue("1");
		}else{
			IqpCredit.is_internal_cert._setValue("2");
		}    
    };
	
	function cdtCertNo(){
        if('${context.menuId}' == "queryCtrLoanContHistoryList"){
        	IqpCredit.cdt_cert_no._obj._renderHidden(false);
        	IqpCredit.cdt_cert_no._obj._renderReadonly(true);
       }else{
    	   IqpCredit.cdt_cert_no._obj._renderHidden(true);
    	   IqpCredit.cdt_cert_no._obj._renderRequired(false);
       }
	};
	
	//---------即期远期判断---------
	function changeTerm(){
		var termType = IqpCredit.credit_term_type._getValue();
		if(termType == '02'){
			IqpCredit.fast_day._obj._renderHidden(false);
			IqpCredit.fast_day._obj._renderRequired(true);
		}else{
			IqpCredit.fast_day._setValue("0");
			IqpCredit.fast_day._obj._renderHidden(true);
			IqpCredit.fast_day._obj._renderRequired(false);
		}
	}
	
	function doSave(){
		if(!IqpCredit._checkAll()){
			alert("信息不完全!");
			return;
		}
		var form = document.getElementById("submitForm");
		IqpCredit._toForm(form);
		//var serno = IqpBksyndic._getValue();
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if(flag == "success"){
					alert("保存成功！");
				}else {
					alert("保存失败！");
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

		var url = '<emp:url action="updateIqpCreditRecord.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	};

	function checkDate(){
        var endDate = IqpCredit.end_date._getValue();
        var openDay='${context.OPENDAY}';
        if(endDate){
             //var flag = CheckDate1BeforeDate2(openDay,endDate);
             if(endDate<openDay){
                 alert("【到期日】必须晚于系统营业日！");  
                 IqpCredit.end_date._setValue("");
             }
        }
	};

	/*--user code end--*/ 
	
</script>
</head>
<body class="page_content" onload="doOnLoad();" >
	
	<emp:form id="submitForm" action="updateIqpCreditRecord.do" method="POST">
		<emp:gridLayout id="IqpCreditGroup" maxColumn="2" title="信用证信息">
			<emp:text id="IqpCredit.serno" label="业务编号" maxlength="40" defvalue="${context.serno}" hidden="true" colSpan="2" required="false" readonly="true" />
			<emp:text id="IqpCredit.cdt_cert_no" label="信用证编号" maxlength="40" required="false" />
			<emp:select id="IqpCredit.credit_type" label="信用证类型" dictname="STD_ZB_CREDIT_TYPE" required="true" />
			<emp:select id="IqpCredit.credit_term_type" label="信用证期限类型" onchange="changeTerm();" required="true" dictname="STD_ZB_CREDIT_DATETYPE"/>
			<emp:text id="IqpCredit.fast_day" label="远期天数" maxlength="38" required="false" dataType="Int"/>    
			<emp:text id="IqpCredit.floodact_perc" label="溢装比例" maxlength="10" required="true" dataType="Percent" />
			<emp:text id="IqpCredit.shortact_perc" label="短装比例" maxlength="10" required="true" dataType="Percent" />
			<emp:date id="IqpCredit.end_date" label="效期" required="true" onblur="checkDate()"/>
			<emp:select id="IqpCredit.is_revolv_credit" label="是否循环信用证" required="true" dictname="STD_ZX_YES_NO" />
			<emp:select id="IqpCredit.is_internal_cert" label="是否国内信用证" required="false" hidden="true" dictname="STD_ZX_YES_NO" />
			<emp:select id="IqpCredit.is_ctrl_gclaim" label="是否可控货权" required="true" dictname="STD_ZX_YES_NO"/>  
			<emp:text id="IqpCredit.beneficiar" label="受益人名称" maxlength="80" required="true" colSpan="2" cssElementClass="emp_field_text_long"/>
			
			
			<emp:text id="IqpCredit.chrg_rate" label="手续费率" required="false" hidden="true" dataType="Rate"/>
			<emp:text id="IqpCredit.busnes_cont" label="贸易合同号" maxlength="40" required="false" hidden="true"/>
			<emp:select id="IqpCredit.beneficiar_country" label="受益人所在国家" required="false" hidden="true" dictname="STD_GB_2659-2000" />
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:actButton id="save" label="保存" op="update"/>
			<emp:actButton id="reset" label="重置" op="cancel"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
