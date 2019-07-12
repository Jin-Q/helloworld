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
		MortUsufructForest._toForm(form);
		if(!MortUsufructForest._checkAll()){
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
		page.dataGroups.MortUsufructForestGroup.reset();
	};			
	function checkInsurStartDate(){
		if(MortUsufructForest.forest_accessed_date._obj.element.value!=''){
			var e = MortUsufructForest.use_end_date._obj.element.value;
			var s = MortUsufructForest.forest_accessed_date._obj.element.value;
			if(e!=''){
				if(s>e){
            		alert('林权取得日期必须小于或等于林地使用终止日期！');
            		MortUsufructForest.forest_accessed_date._obj.element.value="";
            		return;
            	}
			}
		}
	}
	function checkInsurEndDate(){
		if(MortUsufructForest.use_end_date._obj.element.value!=''){
			var e = MortUsufructForest.use_end_date._obj.element.value;
			var s = MortUsufructForest.forest_accessed_date._obj.element.value;
			if(s!=''){
				if(s>e){
            		alert('林地使用终止日期必须大于或等于林权取得日期！');
            		MortUsufructForest.use_end_date._obj.element.value="";
            		return;
            	}
			}
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addMortUsufructForestRecord.do" method="POST">
		
		<emp:gridLayout id="MortUsufructForestGroup" title="林权" maxColumn="2">
			<emp:text id="MortUsufructForest.forest_id" label="林地编号" maxlength="16" required="false" hidden="true"/>
			<emp:text id="MortUsufructForest.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true" />
			<emp:text id="MortUsufructForest.forest_name" label="林地名称" maxlength="200" required="true" />
			<emp:text id="MortUsufructForest.land_no" label="宗地号" maxlength="100" required="true" />
			<emp:text id="MortUsufructForest.forest_addr" label="座落地址" maxlength="200" required="true" />
			<emp:text id="MortUsufructForest.forest_alias" label="小地名" maxlength="200" required="false" />
			<emp:text id="MortUsufructForest.location" label="林班" maxlength="200" required="true" />
			<emp:text id="MortUsufructForest.minute_location" label="小班" maxlength="200" required="true" />
			<emp:select id="MortUsufructForest.tree_category_type_cd" label="林种" required="false" dictname="STD_FOREST_CATE" />
			<emp:text id="MortUsufructForest.major_tree_kind" label="主要树种" maxlength="100" required="false" />
			<emp:text id="MortUsufructForest.tree_count" label="株数" maxlength="8" required="false" dataType="Int" />
			<emp:text id="MortUsufructForest.tree_age" label="树龄" maxlength="16" required="false" dataType="Double" />
			<emp:select id="MortUsufructForest.access_type_cd" label="林权取得方式" required="false" dictname="STD_FOREST_ACQU_TYPE" />
			<emp:date id="MortUsufructForest.forest_accessed_date" label="林权取得日期" required="false" onblur="checkInsurStartDate()"/>
			<emp:text id="MortUsufructForest.land_obligee" label="林地使用权权利人" maxlength="200" required="true" />
			<emp:text id="MortUsufructForest.forest_obligee" label="森林或林木使用权利人" maxlength="200" required="false" />
			<emp:text id="MortUsufructForest.use_amt" label="承包或购入金额（元）" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="MortUsufructForest.use_years" label="林地使用期(年)" maxlength="16" required="false" dataType="Double" />
			<emp:date id="MortUsufructForest.use_end_date" label="林地使用终止日期" required="false" onblur="checkInsurEndDate()"/>
			<emp:text id="MortUsufructForest.renew_cession_amt" label="应补出让金金额" maxlength="18" required="false" dataType="Currency" />
			<emp:textarea id="MortUsufructForest.memo" label="备注" maxlength="1000" required="false" colSpan="2" />
			<emp:date id="MortUsufructForest.sys_update_date" label="系统更新时间" required="false" hidden="true" />
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

