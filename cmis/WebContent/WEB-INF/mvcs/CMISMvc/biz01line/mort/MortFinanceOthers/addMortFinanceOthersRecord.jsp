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
		MortFinanceOthers._toForm(form);
		if(!MortFinanceOthers._checkAll()){
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
		page.dataGroups.MortFinanceOthersGroup.reset();
	};			
	//两个日期作比较
	function checkInsurStartDate(){
		if(MortFinanceOthers.buy_date._obj.element.value!=''){
			var e = MortFinanceOthers.mature_date._obj.element.value;
			var s = MortFinanceOthers.buy_date._obj.element.value;
			var openDay='${context.OPENDAY}';
			if(s>openDay){
        		alert('购入日期必须小于或等于当前日期！');
        		MortFinanceOthers.buy_date._obj.element.value="";
        		return;
        	}
			if(e!=''){
				if(s>e){
            		alert('购入日期必须小于或等于到期日期！');
            		MortFinanceOthers.buy_date._obj.element.value="";
            		return;
            	}
			}
		}
	}
	function checkInsurEndDate(){
		if(MortFinanceOthers.mature_date._obj.element.value!=''){
			var e = MortFinanceOthers.mature_date._obj.element.value;
			var s = MortFinanceOthers.buy_date._obj.element.value;
			if(s!=''){
				if(s>e){
            		alert('到期日期必须大于或等于购入日期！');
            		MortFinanceOthers.mature_date._obj.element.value="";
            		return;
            	}
			}
		}
	}				
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addMortFinanceOthersRecord.do" method="POST">
		
		<emp:gridLayout id="MortFinanceOthersGroup" title="其他金融产品质押" maxColumn="2">
			<emp:text id="MortFinanceOthers.finance_other_id" label="其他金融产品质押编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="MortFinanceOthers.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true" />
			<emp:text id="MortFinanceOthers.usufruct_type" label="//" maxlength="20" required="false" hidden="true" />
			<emp:text id="MortFinanceOthers.usufruct_cert_no" label="受益权权证编号" maxlength="40" required="false" hidden="true" />
			<emp:select id="MortFinanceOthers.currency_cd" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY"/>
			<emp:text id="MortFinanceOthers.usufruct_amt" label="票面金额（元）" maxlength="18" required="true" dataType="Currency" />
			<emp:date id="MortFinanceOthers.buy_date" label="购入日期" required="true" onblur="checkInsurStartDate()"/>
			<emp:date id="MortFinanceOthers.mature_date" label="到期日期" required="true" onblur="checkInsurEndDate()"/>
			<emp:text id="MortFinanceOthers.entrust_user" label="委托人" maxlength="100" required="false" />
			<emp:text id="MortFinanceOthers.beneficiary_name" label="受益人" maxlength="100" required="false" />
			<emp:textarea id="MortFinanceOthers.memo" label="备注" maxlength="1000" required="false" colSpan="2" />
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

