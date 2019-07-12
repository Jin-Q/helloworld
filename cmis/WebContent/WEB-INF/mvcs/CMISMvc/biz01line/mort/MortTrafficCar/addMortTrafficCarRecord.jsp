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
		MortTrafficCar._toForm(form);
		if(!MortTrafficCar._checkAll()){
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
		page.dataGroups.MortTrafficCarGroup.reset();
	};		
	function doUsedYears(){
		var is_used = MortTrafficCar.is_used._getValue();
		if("02"==is_used){
			MortTrafficCar.used_years._obj._renderReadonly(false);
			MortTrafficCar.used_years._obj._renderRequired(true);
		}else{
			MortTrafficCar.used_years._obj._renderReadonly(false);
			MortTrafficCar.used_years._obj._renderRequired(false);
		}
	}	
	function doLoad(){
		doUsedYears();
	}	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="addMortTrafficCarRecord.do" method="POST">
		
		<emp:gridLayout id="MortTrafficCarGroup" title="机动车辆" maxColumn="2">
			<emp:text id="MortTrafficCar.car_id" label="机动车辆编号" maxlength="30" required="false" hidden="true"/>
			<emp:text id="MortTrafficCar.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true" />
			<emp:text id="MortTrafficCar.vehicle_name" label="专用车辆名称" maxlength="40" required="true" />
			<emp:text id="MortTrafficCar.vehicle_brand" label="车辆品牌" maxlength="200" required="true" />
			<emp:text id="MortTrafficCar.spec_model" label="型号" maxlength="200" required="true" />
			<emp:text id="MortTrafficCar.displacement" label="排气量（升）" maxlength="20" required="false" dataType="Double"/>
			<emp:select id="MortTrafficCar.is_import" label="是否进口车" required="true" dictname="STD_ZX_YES_NO" />
			<emp:text id="MortTrafficCar.obtain_way" label="运输工具获得方式" maxlength="20" required="true" />
			<emp:text id="MortTrafficCar.use_nature" label="使用性质" maxlength="20" required="true" />
			<emp:text id="MortTrafficCar.color" label="颜色" maxlength="20" required="false" />
			<emp:select id="MortTrafficCar.buy_curr_cd" label="购置币种" required="true" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="MortTrafficCar.buy_price" label="购置价（元）" maxlength="18" required="true" dataType="Double" />
			<emp:select id="MortTrafficCar.is_used" label="一手/二手" required="true" dictname="STD_SKILL_USED" onchange="doUsedYears()"/>
			<emp:select id="MortTrafficCar.use_status" label="使用状态" required="true" dictname="STD_USE_STATUS" />
			<emp:date id="MortTrafficCar.leave_factory_date" label="出厂时间" required="true" />
			<emp:text id="MortTrafficCar.used_years" label="已使用年限" maxlength="8" required="false" dataType="Int" hidden="false" readonly="true"/>
			<emp:date id="MortTrafficCar.buy_date" label="购入日期" required="false" />
			<emp:text id="MortTrafficCar.engine_num" label="发动机号" maxlength="40" required="true" />
			<emp:text id="MortTrafficCar.car_card_num" label="车牌号" maxlength="40" required="false" />
			<emp:text id="MortTrafficCar.vehicle_flag_cd" label="车辆识别代号/车架号" maxlength="40" required="true" />
			<emp:text id="MortTrafficCar.location" label="处所" maxlength="100" required="false" />
			<emp:text id="MortTrafficCar.use_years" label="使用年限（年）" maxlength="8" required="false" dataType="Int" />
			<emp:text id="MortTrafficCar.drive_license_no" label="行驶证号码" maxlength="200" required="false" />
			<emp:text id="MortTrafficCar.run_km" label="行驶里程（公里）" maxlength="16" required="false" dataType="Double"/>
			<emp:text id="MortTrafficCar.user_name" label="使用人" maxlength="100" required="false" />
			<emp:textarea id="MortTrafficCar.memo" label="备注" maxlength="1000" required="false" colSpan="2" />
			
			
			
			
			<emp:text id="MortTrafficCar.vehicle_type_cd" label="机动车辆类型" maxlength="20" required="false" hidden="true"/>
			<emp:text id="MortTrafficCar.vehicle_reg_no" label="机动车登记证号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="MortTrafficCar.invoice_no" label="发票号码" maxlength="40" required="false" hidden="true"/>
			<emp:text id="MortTrafficCar.buy_tax_voucher_no" label="购置税缴交凭证号码" maxlength="40" required="false" hidden="true"/>
			<emp:date id="MortTrafficCar.sys_update_time" label="系统更新时间" required="false" hidden="true" />
			
			
			
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

