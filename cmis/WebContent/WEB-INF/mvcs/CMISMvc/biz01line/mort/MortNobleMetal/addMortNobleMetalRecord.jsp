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
		MortNobleMetal._toForm(form);
		if(!MortNobleMetal._checkAll()){
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
		page.dataGroups.MortNobleMetalGroup.reset();
	};				
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addMortNobleMetalRecord.do" method="POST">
		
		<emp:gridLayout id="MortNobleMetalGroup" title="贵金属" maxColumn="2">
			<emp:text id="MortNobleMetal.metal_id" label="贵金属编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="MortNobleMetal.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true" />
			<emp:text id="MortNobleMetal.coll_name" label="质押物名称" maxlength="200" required="true" />
			<emp:text id="MortNobleMetal.model_no" label="质物品牌/型号" maxlength="40" required="false" />
			<emp:text id="MortNobleMetal.standard_no" label="质物品种/规格" maxlength="40" required="false"/>
			<emp:text id="MortNobleMetal.storage_place" label="储存场所" maxlength="40" required="true" />
			<emp:text id="MortNobleMetal.storager" label="监管方/仓储方" maxlength="40" required="true" />
			<emp:date id="MortNobleMetal.coll_buy_date" label="质物购置日期" required="true"/>
			<emp:text id="MortNobleMetal.coll_max_years" label="质物最高使用年限" maxlength="10" required="false" />
			<emp:text id="MortNobleMetal.coll_used_years" label="质物已使用年限" maxlength="10" required="false" />
			<emp:text id="MortNobleMetal.coll_weight" label="重量(g)" maxlength="16" required="true" dataType="Double" />
			<emp:text id="MortNobleMetal.cover_pieces" label="内含(件)" maxlength="8" required="false" dataType="Int" />
			<emp:text id="MortNobleMetal.quality" label="成色(%)" maxlength="16" required="true" dataType="Rate" />
			<emp:textarea id="MortNobleMetal.remark" label="备注" maxlength="1000" required="false" colSpan="2" />
			<emp:date id="MortNobleMetal.sys_update_time" label="系统更新时间" required="false" hidden="true" />
			<emp:text id="MortNobleMetal.metal_type" label="金属类型" maxlength="10" required="false" hidden="true"/>
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

