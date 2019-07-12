<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<% 
Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
String op= "";
if(context.containsKey("op")){
	op = (String)context.getDataValue("op");
}
if("view".equals(op)||"to_storage".equals(op)){
	request.setAttribute("canwrite","");
}
String guaranty_no = request.getParameter("guaranty_no");
%>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function doAdd(){
		var form = document.getElementById('submitForm');
		MortInsuranceSlip._toForm(form);
		if(!MortInsuranceSlip._checkAll()){
			alert("请输入必填项！");
		}else{
			var handleSuccess = function(o) {
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("保存失败！");
						document.getElementById("button_add").disabled="";
						document.getElementById("button_reset").disabled="";
						return;
					}
					var flag = jsonstr.flag;
					if(flag=='success'){	
						alert("保存成功");
						var guaranty_no = '${context.guaranty_no}';
						var collateral_type_cd = '${context.collateral_type_cd}';
						var url = '<emp:url action="getDetailInformationPage.do"/>?guaranty_no='+guaranty_no+'&collateral_type_cd='+collateral_type_cd;
						url = EMPTools.encodeURI(url);
						location.href(url);
					}else{
						alert("保存失败");
						document.getElementById("button_add").disabled="";
						document.getElementById("button_reset").disabled="";
					}   
				}	
			};
			var handleFailure = function(o) {
				alert("保存失败!");
				document.getElementById("button_add").disabled="";
				document.getElementById("button_reset").disabled="";
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			var postData = YAHOO.util.Connect.setForm(form);	 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	};
	function doReset(){
		var guaranty_no = '${context.guaranty_no}';
		var url = '<emp:url action="getDetailInformationPage.do"/>?guaranty_no='+guaranty_no;
		url = EMPTools.encodeURI(url);
		location.href(url);
		page.dataGroups.MortInsuranceSlipGroup.reset();
	};	
	//两个日期作比较
	function checkInsurStartDate(){
		if(MortInsuranceSlip.insur_begin_date._obj.element.value!=''){
			var e = MortInsuranceSlip.insur_end_date._obj.element.value;
			var s = MortInsuranceSlip.insur_begin_date._obj.element.value;
			if(e!=''){
				if(s>e){
            		alert('保险开始日期必须小于或等于保险到期日期！');
            		MortInsuranceSlip.insur_begin_date._obj.element.value="";
            		return;
            	}
			}
		}
	}
	function checkInsurEndDate(){
		if(MortInsuranceSlip.insur_end_date._obj.element.value!=''){
			var e = MortInsuranceSlip.insur_end_date._obj.element.value;
			var s = MortInsuranceSlip.insur_begin_date._obj.element.value;
			if(s!=''){
				if(s>e){
            		alert('保险到期日期必须大于或等于保险开始日期！');
            		MortInsuranceSlip.insur_end_date._obj.element.value="";
            		return;
            	}
			}
		}
	}	
	function onChange(){
		var flag = MortInsuranceSlip.pay_type_cd._getValue();
		if(flag=="02"){//分期付清
			MortInsuranceSlip.pay_term._obj._renderHidden(false);  
			MortInsuranceSlip.pay_period_amt._obj._renderHidden(false);  
			MortInsuranceSlip.pay_begin_date._obj._renderHidden(false);  
			MortInsuranceSlip.pay_end_date._obj._renderHidden(false); 
			MortInsuranceSlip.pay_term._obj._renderRequired(true);  
			MortInsuranceSlip.pay_period_amt._obj._renderRequired(true);  
			MortInsuranceSlip.pay_begin_date._obj._renderRequired(true);  
			MortInsuranceSlip.pay_end_date._obj._renderRequired(true);    
		}else{
			MortInsuranceSlip.pay_term._obj._renderHidden(true);  
			MortInsuranceSlip.pay_period_amt._obj._renderHidden(true);  
			MortInsuranceSlip.pay_begin_date._obj._renderHidden(true);  
			MortInsuranceSlip.pay_end_date._obj._renderHidden(true);  
			MortInsuranceSlip.pay_term._obj._renderRequired(false);  
			MortInsuranceSlip.pay_period_amt._obj._renderRequired(false);  
			MortInsuranceSlip.pay_begin_date._obj._renderRequired(false);  
			MortInsuranceSlip.pay_end_date._obj._renderRequired(false);
		}
	}	
	function doLoad(){
		onChange();
	}				
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="addMortInsuranceSlipRecord.do" method="POST">
		
		<emp:gridLayout id="MortInsuranceSlipGroup" title="保单" maxColumn="2">
			<emp:text id="MortInsuranceSlip.insure_id" label="保险编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="MortInsuranceSlip.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="MortInsuranceSlip.insur_no" label="保险单号码" maxlength="100" required="false" hidden="true"/>
			<emp:text id="MortInsuranceSlip.insur_type_cd" label="保险类型//" maxlength="9" required="false" hidden="true"/>
			<emp:text id="MortInsuranceSlip.insur_corp_name" label="保险公司名称" maxlength="200" required="true" />
			<emp:text id="MortInsuranceSlip.policy_holder" label="投保人" maxlength="200" required="true" />
			<emp:text id="MortInsuranceSlip.insurant_name" label="被保险人" maxlength="200" required="true" />
			<emp:text id="MortInsuranceSlip.beneficiary_name" label="受益人" maxlength="200" required="true" />
			<emp:text id="MortInsuranceSlip.cash_value" label="现金价值（元）" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="MortInsuranceSlip.insur_amt" label="保险金额（元）" maxlength="18" required="true" dataType="Currency" />
			<emp:date id="MortInsuranceSlip.insur_begin_date" label="保险开始日期" required="true" onblur="checkInsurStartDate()"/>
			<emp:date id="MortInsuranceSlip.insur_end_date" label="保险到期日期" required="true" onblur="checkInsurEndDate()"/>
			<emp:select id="MortInsuranceSlip.pay_type_cd" label="保险费交缴方式" required="true" dictname="STD_PREMIUM_PAID_TYPE" onchange="onChange()"/>
			
			<emp:text id="MortInsuranceSlip.pay_term" label="保险费交缴期限(月)" maxlength="8" required="false" dataType="Int" hidden="true"/>
			<emp:text id="MortInsuranceSlip.pay_period_amt" label="保险费每期交缴金额" maxlength="18" required="false" dataType="Currency" hidden="true"/>
			<emp:date id="MortInsuranceSlip.pay_begin_date" label="交缴开始日" required="false" hidden="true"/>
			<emp:date id="MortInsuranceSlip.pay_end_date" label="交缴结束日" required="false" hidden="true"/>
			<emp:text id="MortInsuranceSlip.insur_liability_info" label="保险责任情况" maxlength="400" required="false" hidden="true"/>
			<emp:date id="MortInsuranceSlip.sys_update_time" label="系统更新时间" required="false" hidden="true"/>
			
			<emp:textarea id="MortInsuranceSlip.remark" label="备注" maxlength="1000" required="false" colSpan="2" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<%if("view".equals(op)||"to_storage".equals(op)){%>		
			<%}else{%>
			<emp:button id="add" label="确定"/>
			<emp:button id="reset" label="重置"/>
			<% } %>			
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

