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
		MortTrafficShip._toForm(form);
		if(!MortTrafficShip._checkAll()){
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
		page.dataGroups.MortTrafficShipGroup.reset();
	};		
	function doLoad(){
		doChange();
		doUsedYears();
	}	
	function doChange(){
		var certificate_ind = MortTrafficShip.certificate_ind._getValue();
		if("1"==certificate_ind){
			MortTrafficShip.certificate_no._obj._renderHidden(false);
			MortTrafficShip.certificate_no._obj._renderRequired(true);
		}else{
			MortTrafficShip.certificate_no._obj._renderHidden(true);
			MortTrafficShip.certificate_no._obj._renderRequired(false);
		}
	}
	function doUsedYears(){
		var is_used = MortTrafficShip.is_used._getValue();
		if("02"==is_used){
			MortTrafficShip.used_years._obj._renderReadonly(false);
			MortTrafficShip.used_years._obj._renderRequired(true);
		}else{
			MortTrafficShip.used_years._obj._renderReadonly(false);
			MortTrafficShip.used_years._obj._renderRequired(false);
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="addMortTrafficShipRecord.do" method="POST">
		
		<emp:gridLayout id="MortTrafficShipGroup" title="船舶" maxColumn="2">
			<emp:text id="MortTrafficShip.ship_id" label="船舶编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="MortTrafficShip.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true" />
			<emp:text id="MortTrafficShip.count" label="数量" maxlength="20" required="true" dataType="Double"/>
			<emp:select id="MortTrafficShip.unit" label="单位" required="true" dictname="STD_AMOUNT_UNIT" />
			<emp:text id="MortTrafficShip.ship_brand" label="船舶名称" maxlength="200" required="false" />
			<emp:text id="MortTrafficShip.work_license_no" label="交通工具营运执照号" maxlength="200" required="false" />
			<emp:select id="MortTrafficShip.is_ship_join" label="是否入船级社" required="true" dictname="STD_ZX_YES_NO" />
			<emp:text id="MortTrafficShip.nation_license_no" label="船舶国籍证书号码" maxlength="200" required="false" />
			<emp:text id="MortTrafficShip.fuel" label="动力燃料" maxlength="20" required="false" />
			<emp:text id="MortTrafficShip.produce_factory" label="制造厂家" maxlength="200" required="true" />
			<emp:date id="MortTrafficShip.manufacture_year" label="制造年度" required="true" />
			<emp:text id="MortTrafficShip.manufacture_site" label="制造地" maxlength="200" required="true" />
			<emp:select id="MortTrafficShip.use_status" label="使用状态" required="true" dictname="STD_USE_STATUS" />
			<emp:text id="MortTrafficShip.total_ton" label="总吨位" maxlength="16" required="true" dataType="Double" />
			<emp:date id="MortTrafficShip.leave_factory_date" label="出厂日期" required="false" />
			<emp:text id="MortTrafficShip.displacement" label="排水量（吨）" maxlength="16" required="false" dataType="Double" />
			<emp:date id="MortTrafficShip.buy_date" label="购入日期" required="false" />
			<emp:select id="MortTrafficShip.certificate_ind" label="是否有航运证" required="true" dictname="STD_ZX_YES_NO" onchange="doChange()"/>
			<emp:text id="MortTrafficShip.certificate_no" label="航运证件号" maxlength="40" required="true" />
			<emp:select id="MortTrafficShip.buy_curr_cd" label="购入币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY"/>
			<emp:text id="MortTrafficShip.buy_price" label="购入价格（元）" maxlength="18" required="true" dataType="Currency" />
			<emp:date id="MortTrafficShip.certificate_date" label="挂牌日期" required="false" />
			<emp:select id="MortTrafficShip.is_import" label="是否进口船舶" required="true" dictname="STD_ZX_YES_NO" />
			<emp:text id="MortTrafficShip.register_address" label="注册地址" maxlength="100" required="false" />
			<emp:text id="MortTrafficShip.run_km" label="已行驶里程（公里）" maxlength="16" required="false" dataType="Double" />
			<emp:select id="MortTrafficShip.is_used" label="一手/二手" required="true" dictname="STD_SKILL_USED" onchange="doUsedYears()"/>
			<emp:text id="MortTrafficShip.used_years" label="已使用年限" maxlength="8" required="false" dataType="Int" readonly="true"/>
			<emp:text id="MortTrafficShip.heavy_repair_times" label="大修次数" maxlength="8" required="false" dataType="Int" />
			<emp:text id="MortTrafficShip.accident_reg" label="事故记录" maxlength="200" required="false" />
			<emp:textarea id="MortTrafficShip.memo" label="备注" maxlength="1000" required="false" colSpan="2" />
			
			
			
			
			<emp:text id="MortTrafficShip.ship_name" label="交通工具名称//" maxlength="200" required="false" hidden="true"/>
			<emp:text id="MortTrafficShip.is_ship_society" label="//" maxlength="1" required="false" hidden="true" />
			<emp:text id="MortTrafficShip.use_years" label="使用年限" maxlength="8" required="false" dataType="Double" hidden="true"/>
			<emp:text id="MortTrafficShip.ship_info" label="船舶用途//" maxlength="20" required="false" hidden="true"/>
			<emp:text id="MortTrafficShip.ship_type_cd" label="运输交通工具类型//" maxlength="20" required="false" hidden="true" />
			<emp:text id="MortTrafficShip.ship_num" label="船牌号//" maxlength="40" required="false" hidden="true" />
			<emp:text id="MortTrafficShip.fuel_label_no" label="燃料标号//" maxlength="40" required="false" hidden="true" />
			<emp:text id="MortTrafficShip.insurance_date" label="保险缴至日期//" maxlength="10" required="false" hidden="true" />
			<emp:text id="MortTrafficShip.ori_ship_owner" label="原船主名称//" maxlength="100" required="false" hidden="true" />
			<emp:text id="MortTrafficShip.engine_type" label="动力类别//" maxlength="20" required="false" hidden="true" />
			<emp:text id="MortTrafficShip.carrying_capacity" label="承载能力//" maxlength="200" required="false" hidden="true" />
			<emp:text id="MortTrafficShip.carrying_ability" label="载重//" maxlength="16" required="false" hidden="true" />
			<emp:text id="MortTrafficShip.ship_power" label="功率//" maxlength="16" required="false" hidden="true" />
			<emp:text id="MortTrafficShip.power_unit_cd" label="功率单位//" maxlength="20" required="false" hidden="true" />
			<emp:text id="MortTrafficShip.manufacture_factory" label="制造商//" maxlength="200" required="false" hidden="true"  />
			<emp:text id="MortTrafficShip.spec_model" label="规格型号//" maxlength="200" required="false" hidden="true" />
			<emp:date id="MortTrafficShip.sys_update_time" label="系统更新时间" required="false" hidden="true" />
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

