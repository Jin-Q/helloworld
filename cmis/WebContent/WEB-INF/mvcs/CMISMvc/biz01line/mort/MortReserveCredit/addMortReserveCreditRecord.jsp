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
		MortReserveCredit._toForm(form);
		if(!MortReserveCredit._checkAll()){
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
		page.dataGroups.MortReserveCreditGroup.reset();
	};		

	//两个日期作比较
	function checkInsurStartDate(){
		if(MortReserveCredit.issue_date._obj.element.value!=''){
			var e = MortReserveCredit.expire_date._obj.element.value;
			var s = MortReserveCredit.issue_date._obj.element.value;
			var openDay='${context.OPENDAY}';
			if(s>openDay){
        		alert('开证日期必须小于或等于当前日期！');
        		MortReserveCredit.issue_date._obj.element.value="";
        		return;
        	}
			if(e!=''){
				if(s>e){
            		alert('开证日期必须小于或等于到期日期！');
            		MortReserveCredit.issue_date._obj.element.value="";
            		return;
            	}
			}
		}
	}
	function checkInsurEndDate(){
		if(MortReserveCredit.expire_date._obj.element.value!=''){
			var e = MortReserveCredit.expire_date._obj.element.value;
			var s = MortReserveCredit.issue_date._obj.element.value;
			if(s!=''){
				if(s>e){
            		alert('到期日期必须大于或等于开证日期！');
            		MortReserveCredit.expire_date._obj.element.value="";
            		return;
            	}
			}
		}
	}	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addMortReserveCreditRecord.do" method="POST">
		
		<emp:gridLayout id="MortReserveCreditGroup" title="备用信用证" maxColumn="2">
			<emp:text id="MortReserveCredit.credit_id" label="信用证编号" maxlength="40" required="false" hidden="true" />
			<emp:text id="MortReserveCredit.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true" />
			<emp:text id="MortReserveCredit.apply_user" label="开证申请人名称" maxlength="100" required="true" />
			<emp:text id="MortReserveCredit.apply_area" label="开证申请人国家或地区" maxlength="100" required="false" />
			<emp:text id="MortReserveCredit.beneficiary_bank" label="受益人行名" maxlength="100" required="true" />
			<emp:text id="MortReserveCredit.issue_amt" label="开证金额（元）" maxlength="18" required="true" dataType="Currency" />
			<emp:date id="MortReserveCredit.issue_date" label="开证日期" required="true" onblur="checkInsurStartDate()"/>
			<emp:date id="MortReserveCredit.expire_date" label="到期日期" required="true" onblur="checkInsurEndDate()"/>
			<emp:text id="MortReserveCredit.issue_bank" label="开证行" maxlength="100" required="true" />
			<emp:text id="MortReserveCredit.issue_area" label="开证行所在国家或地区" maxlength="100" required="false" />
			<emp:textarea id="MortReserveCredit.remark" label="备注" maxlength="1000" required="false" colSpan="2" />
			
			
			
			<emp:select id="MortReserveCredit.credit_currency_cd" label="信用证币种" required="false" dictname="STD_ZX_CUR_TYPE" hidden="true"/>
			<emp:text id="MortReserveCredit.credit_value" label="信用证金额" maxlength="18" required="false" dataType="Currency" hidden="true"/>
			<emp:date id="MortReserveCredit.sys_update_date" label="更新日期" required="false" hidden="true"/>
			<emp:text id="MortReserveCredit.handling_user" label="更新人" maxlength="40" required="false" hidden="true"/>
			
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

