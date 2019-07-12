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
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 450px;
}
</style>
<script type="text/javascript">

	/*--user code begin--*/
		function doAdd(){
		var form = document.getElementById('submitForm');
		MortOwnershipOthers._toForm(form);
		if(!MortOwnershipOthers._checkAll()){
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
		page.dataGroups.MortOwnershipOthersGroup.reset();
	};			


	//两个日期作比较
	function checkInsurStartDate(){
		if(MortOwnershipOthers.buy_date._obj.element.value!=''){
			var e = MortOwnershipOthers.mature_date._obj.element.value;
			var s = MortOwnershipOthers.buy_date._obj.element.value;
			var openDay='${context.OPENDAY}';
			if(s>openDay){
        		alert('购入日期必须小于或等于当前日期！');
        		MortOwnershipOthers.buy_date._obj.element.value="";
        		return;
        	}
			if(e!=''){
				if(s>e){
            		alert('购入日期必须小于或等于到期日期！');
            		MortOwnershipOthers.buy_date._obj.element.value="";
            		return;
            	}
			}
		}
	}
	function checkInsurEndDate(){
		if(MortOwnershipOthers.mature_date._obj.element.value!=''){
			var e = MortOwnershipOthers.mature_date._obj.element.value;
			var s = MortOwnershipOthers.buy_date._obj.element.value;
			if(s!=''){
				if(s>e){
            		alert('到期日期必须大于或等于购入日期！');
            		MortOwnershipOthers.mature_date._obj.element.value="";
            		return;
            	}
			}
		}
	}	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addMortOwnershipOthersRecord.do" method="POST">
		
		<emp:gridLayout id="MortOwnershipOthersGroup" title="其他所有权" maxColumn="2">
			<emp:text id="MortOwnershipOthers.own_other_id" label="其他所有权编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="MortOwnershipOthers.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true" />
			<emp:select id="MortOwnershipOthers.use_state" label="是否已出租" required="false" dictname="STD_ZX_YES_NO" colSpan="2"/>
			<emp:text id="MortOwnershipOthers.ownership_user" label="所有权授予人" maxlength="100" required="false" />
			<emp:text id="MortOwnershipOthers.gage_name" label="抵/质押物名称" maxlength="80" required="true" />
			<emp:text id="MortOwnershipOthers.amount" label="数量" maxlength="38" required="true" dataType="Double" />
			<emp:text id="MortOwnershipOthers.amount_unit" label="数量单位(例如：个)" maxlength="8" required="true" />
			<emp:text id="MortOwnershipOthers.weigh" label="重量" maxlength="16" required="false" dataType="Double" />
			<emp:text id="MortOwnershipOthers.weigh_unit" label="重量单位（例如：克）" maxlength="8" required="false" />
			<emp:text id="MortOwnershipOthers.address" label="住所（地址）" maxlength="200" required="false"  cssElementClass="emp_field_text_input2" colSpan="2"/>
			<emp:date id="MortOwnershipOthers.buy_date" label="购入日期" required="false" onblur="checkInsurStartDate()"/>
			<emp:date id="MortOwnershipOthers.mature_date" label="到期日期" required="false" onblur="checkInsurEndDate()"/>
			<emp:textarea id="MortOwnershipOthers.memo" label="备注" maxlength="1000" required="false" colSpan="2" />
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

