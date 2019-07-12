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
		MortManagedProductOthers._toForm(form);
		if(!MortManagedProductOthers._checkAll()){
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
		page.dataGroups.MortManagedProductOthersGroup.reset();
	};				
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addMortManagedProductOthersRecord.do" method="POST">
		
		<emp:gridLayout id="MortManagedProductOthersGroup" title="其他托管品" maxColumn="2">
			<emp:text id="MortManagedProductOthers.mp_other_id" label="其他托管品编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="MortManagedProductOthers.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true" />
			<emp:select id="MortManagedProductOthers.cur_type" label="币种" required="false" dictname="STD_ZX_CUR_TYPE" hidden="false" defvalue="CNY"/>
			<emp:text id="MortManagedProductOthers.handling_user" label="价值（元）" maxlength="40" required="false" hidden="false" dataType="Currency"/>
			<emp:text id="MortManagedProductOthers.managed_product_name" label="托管品名称" maxlength="100" required="true" colSpan="2"/>
			<emp:text id="MortManagedProductOthers.owner" label="抵质押人" maxlength="100" required="true" />
			<emp:text id="MortManagedProductOthers.co_owner" label="共有人" maxlength="100" required="false" />
			<emp:textarea id="MortManagedProductOthers.remark" label="备注" maxlength="1000" required="false" colSpan="2" />
			<emp:date id="MortManagedProductOthers.sys_update_time" label="更新日期" required="false" hidden="true"/>
			
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

