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
		MortMortOthers._toForm(form);
		if(!MortMortOthers._checkAll()){
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
		page.dataGroups.MortMortOthersGroup.reset();
	};		
	function checkDt(){
		var occur_date = MortMortOthers.buy_date._getValue();
		var openDay='${context.OPENDAY}';
		if(occur_date!=''){
			if(CheckDate1BeforeDate2(openDay,occur_date)){
	    		alert('购入日期不能大于当前日期！');
	    		MortMortOthers.buy_date._obj.element.value="";
	    		return;
	    	}
    	}
	}		
	/*--user code end--*/
	
</script>
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 450px;
}
</style>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addMortMortOthersRecord.do" method="POST">
		
		<emp:gridLayout id="MortMortOthersGroup" title="其他抵押类" maxColumn="2">
			<emp:text id="MortMortOthers.mort_other_id" label="其他编号" maxlength="16" required="false" hidden="true"/>
			<emp:text id="MortMortOthers.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true" />
			<emp:text id="MortMortOthers.mort_name" label="抵押物名称" maxlength="100" required="true" />
			<emp:text id="MortMortOthers.mort_brand" label="抵押物品牌" maxlength="100" required="false" />
			<emp:text id="MortMortOthers.mort_spec" label="型号" maxlength="100" required="false" colSpan="2"/>
			<emp:text id="MortMortOthers.mort_quantity" label="数量" maxlength="16" required="false" dataType="Double" />
			<emp:text id="MortMortOthers.mort_unit" label="单位(例如：个)" maxlength="12" required="false" />
			<emp:text id="MortMortOthers.quality_state" label="质量及状况" maxlength="100" required="false" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:text id="MortMortOthers.mort_place" label="存放、保管地址" maxlength="200" required="false" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:text id="MortMortOthers.mort_monad" label="存放、保管单位" maxlength="200" required="false" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:select id="MortMortOthers.use_status" label="使用状态" required="true" dictname="STD_USE_STATUS" />
			<emp:date id="MortMortOthers.buy_date" label="购入日期" required="false" onblur="checkDt()"/>
			<emp:select id="MortMortOthers.buy_curr_cd" label="购入币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY"/>
			<emp:text id="MortMortOthers.buy_sum" label="购入金额（元）" maxlength="18" required="true" dataType="Currency" />
			<emp:textarea id="MortMortOthers.mort_memo" label="备注" maxlength="1000" required="false" colSpan="2" />
			
			
			
			<emp:text id="MortMortOthers.mort_price" label="当前市场价//" maxlength="16" required="false" hidden="true" />
			<emp:text id="MortMortOthers.invoice_no" label="发票号码" maxlength="40" required="false" hidden="true"/>
			<emp:date id="MortMortOthers.sys_update_time" label="系统更新时间" required="false" hidden="true" />
			
			
			
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

