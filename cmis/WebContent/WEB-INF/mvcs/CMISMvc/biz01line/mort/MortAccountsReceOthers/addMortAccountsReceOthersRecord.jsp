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
		MortAccountsReceOthers._toForm(form);
		if(!MortAccountsReceOthers._checkAll()){
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
		page.dataGroups.MortAccountsReceOthersGroup.reset();
	};	
	//两个日期作比较
	function checkInsurStartDate(){
		if(MortAccountsReceOthers.use_begin_date._obj.element.value!=''){
			var e = MortAccountsReceOthers.use_end_date._obj.element.value;
			var s = MortAccountsReceOthers.use_begin_date._obj.element.value;
			var openDay='${context.OPENDAY}';
			if(e!=''){
				if(s>e){
            		alert('可使用起始日必须小于或等于可使用终止日！');
            		MortAccountsReceOthers.use_begin_date._obj.element.value="";
            		return;
            	}
			}
		}
	}
	function checkInsurEndDate(){
		if(MortAccountsReceOthers.use_end_date._obj.element.value!=''){
			var e = MortAccountsReceOthers.use_end_date._obj.element.value;
			var s = MortAccountsReceOthers.use_begin_date._obj.element.value;
			if(s!=''){
				if(s>e){
            		alert('可使用终止日必须大于或等于可使用起始日！');
            		MortAccountsReceOthers.use_end_date._obj.element.value="";
            		return;
            	}
			}
		}
	}			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addMortAccountsReceOthersRecord.do" method="POST">
		
		<emp:gridLayout id="MortAccountsReceOthersGroup" title="应收账款其他可转让的权利" maxColumn="2">
			<emp:text id="MortAccountsReceOthers.debt_other_id" label="应收账款其他可转让的权利编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="MortAccountsReceOthers.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true" />
			<emp:text id="MortAccountsReceOthers.approval_no" label="收费权质押批文号" maxlength="80" required="true" />
			<emp:text id="MortAccountsReceOthers.approval_org" label="收费权质押批文单位" maxlength="100" required="true" />
			<emp:text id="MortAccountsReceOthers.standard_value" label="收费标准" maxlength="16" required="false" />
			<emp:text id="MortAccountsReceOthers.user_name" label="使用人" maxlength="100" required="false" />
			<emp:date id="MortAccountsReceOthers.use_begin_date" label="可使用起始日" required="true" onblur="checkInsurStartDate()"/>
			<emp:date id="MortAccountsReceOthers.use_end_date" label="可使用终止日" required="true" onblur="checkInsurEndDate()"/>
			<emp:select id="MortAccountsReceOthers.currency_cd" label="币种" required="false" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" hidden="true"/>
			<emp:text id="MortAccountsReceOthers.account_value" label="价值（元）" maxlength="18" required="false" dataType="Currency" hidden="true"/>
			
			<emp:text id="MortAccountsReceOthers.right_sort" label="权利类别//" maxlength="20" required="false" hidden="true"/>
			<emp:date id="MortAccountsReceOthers.sys_update_time" label="系统更新时间" required="false" hidden="true" />
			<emp:text id="MortAccountsReceOthers.use_years" label="使用年限" maxlength="8" required="false" hidden="true"/>
			<emp:text id="MortAccountsReceOthers.register_org_name" label="登记机构名称" maxlength="100" required="false" hidden="true"/>
			<emp:textarea id="MortAccountsReceOthers.memo" label="备注" maxlength="1000" required="false" colSpan="2" />
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

