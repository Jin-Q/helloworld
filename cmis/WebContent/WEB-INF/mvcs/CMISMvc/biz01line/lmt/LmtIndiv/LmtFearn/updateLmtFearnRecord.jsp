<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	String cus_id = request.getParameter("cus_id");
%>
<emp:page>
<html>
<head>
<title>修改页面</title>
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 450px;
}
</style>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doReturn(){
		window.close();
	}

	function returnCus(data){
		//客户码,证件类型,证件号码,姓名
    	LmtFearn.cus_id._setValue(data.cus_id._getValue());
    	LmtFearn.cus_id_displayname._setValue(data.cus_name._getValue());
    	LmtFearn.cert_type._setValue(data.cert_type._getValue());
    	LmtFearn.cert_code._setValue(data.cert_code._getValue());
	}

	function changePerc(){
		var sour = LmtFearn.earning_sour._getValue();
		if(sour == '01' || sour == '03' || sour == '04' || sour == '06' || sour == '07' || sour == '09'){
			LmtFearn.identy_perc._setValue("1");
			var identy_perc = LmtFearn.identy_perc._getValue();//认定比例
			var mearn_score = LmtFearn.mearn_score._getValue();//月收入原值
			var yearn_score = LmtFearn.yearn_score._getValue();//年收入原值
			LmtFearn.ibank_mearn._setValue(''+Math.round(identy_perc*mearn_score*100)/100+'');
			LmtFearn.ibank_yearn._setValue(''+Math.round(identy_perc*yearn_score*100)/100+'');
		
		}else{
			LmtFearn.identy_perc._setValue("0.7");
			var identy_perc = LmtFearn.identy_perc._getValue();//认定比例
			var mearn_score = LmtFearn.mearn_score._getValue();//月收入原值
			var yearn_score = LmtFearn.yearn_score._getValue();//年收入原值
			LmtFearn.ibank_mearn._setValue(''+Math.round(identy_perc*mearn_score*100)/100+'');
			LmtFearn.ibank_yearn._setValue(''+Math.round(identy_perc*yearn_score*100)/100+'');
		}
	}

	function countMonth(){
		var perc = LmtFearn.identy_perc._getValue();//认定比例
		var mearn = LmtFearn.mearn_score._getValue();//月收入原值
		var yearn = LmtFearn.yearn_score._getValue();//年收入原值
		var ibank_mearn = LmtFearn.ibank_mearn._getValue();//我行认定月收入
		if(parseFloat(mearn)>parseFloat(yearn)){
			alert("月收入必须大于年收入");
			LmtFearn.mearn_score._setValue("0.00");
			return;
		}
		ibank_mearn = Math.round(perc*mearn*100)/100;
		LmtFearn.ibank_mearn._setValue(ibank_mearn+'');
	}

	function countYear(){
		var perc = LmtFearn.identy_perc._getValue();//认定比例
		var mearn = LmtFearn.mearn_score._getValue();//月收入原值
		var yearn = LmtFearn.yearn_score._getValue();//年收入原值
		var ibank_yearn = LmtFearn.ibank_yearn._getValue();//我行认定年收入
		if(parseFloat(mearn)>parseFloat(yearn)){
			alert("月收入必须小于年收入！");
			LmtFearn.yearn_score._setValue("0.00");
			return;
		}
		ibank_yearn = Math.round(perc*yearn*100)/100;
		LmtFearn.ibank_yearn._setValue(ibank_yearn+'');
		
	}

	function doUpdatee() {
		var form = document.getElementById("submitForm");
		var result = LmtFearn._checkAll();
		if(result){
			LmtFearn._toForm(form)
			toSubmitForm(form);
		}else alert("请输入必填项！");
	}
	
	function toSubmitForm(form){
		  var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr define error!"+e);
						return;
					}
					var flag = jsonstr.flag;
					if(flag=="success"){
						alert("修改成功！");
						window.close();
						window.opener.location.reload();
				     }else {
					   alert(flag);
					   return;
				     }
				}
			};
			var handleFailure = function(o){	
			};
			var callback = {
					success:handleSuccess,
					failure:handleFailure
				};
				var postData = YAHOO.util.Connect.setForm(form);	 
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData) 
	};
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateLmtFearnRecord.do" method="POST">
		<emp:gridLayout id="LmtFearnGroup" maxColumn="2" title="家庭收入">
			<emp:pop id="LmtFearn.cus_id" label="客户码" url="queryAllCusPop.do?cusTypCondition=cus_id in( select cus_id from Cus_Indiv_Soc_Rel where indiv_cus_rel in('1','2','3','9') and cus_id_rel='${context.cus_id}')or cus_id='${context.cus_id}'&returnMethod=returnCus" colSpan="2" popParam="width=700px,height=650px" required="true" readonly="true"/>
			<emp:text id="LmtFearn.cus_id_displayname" label="客户名"   required="true" readonly="true" cssElementClass="emp_field_text_readonly" colSpan="2"/>
			<emp:select id="LmtFearn.cert_type" label="证件类型" required="false" dictname="STD_ZB_CERT_TYP" readonly="true"/>
			<emp:text id="LmtFearn.cert_code" label="证件号码" required="false"  readonly="true"/>
			<emp:select id="LmtFearn.cus_attr" label="客户属性" required="false" dictname="STD_ZB_CUS_ATTR" readonly="true"/>
			<emp:select id="LmtFearn.earning_sour" label="收入来源" required="true" dictname="STD_ZB_EARNING_SOUR" onchange="changePerc()" colSpan="2"  cssElementClass="emp_field_text_input2"/>
			<emp:text id="LmtFearn.identy_perc" label="认定比例" maxlength="4" required="false" readonly="true" colSpan="2"/>
			<emp:select id="LmtFearn.ewrant_type" label="收入凭证类别" required="false" dictname="STD_ZB_EWRANT_TYPE" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:text id="LmtFearn.mearn_score" label="月收入原值" maxlength="18" required="true" dataType="Currency" onchange="countMonth()" />
			<emp:text id="LmtFearn.ibank_mearn" label="我行认定月收入" maxlength="18" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>  
			<emp:text id="LmtFearn.yearn_score" label="年收入原值" maxlength="18" required="true" dataType="Currency" onchange="countYear()" />
			<emp:text id="LmtFearn.ibank_yearn" label="我行认定年收入" maxlength="18" required="false" dataType="Currency"  readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:textarea id="LmtFearn.memo" label="备注" maxlength="60" required="false" colSpan="2" />
			<emp:text id="LmtFearn.serno" label="流水号"  required="false" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="updatee" label="修改"/>
			<emp:button id="return" label="关闭"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
