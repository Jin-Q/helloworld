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
		MortMachineEquipment._toForm(form);
		if(!MortMachineEquipment._checkAll()){
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
		page.dataGroups.MortMachineEquipmentGroup.reset();
	};	
	function checkDt(){
		var occur_date = MortMachineEquipment.leave_factory_date._getValue();
		var openDay='${context.OPENDAY}';
		if(occur_date!=''){
			if(CheckDate1BeforeDate2(openDay,occur_date)){
	    		alert('出厂日期不能大于当前日期！');
	    		MortMachineEquipment.leave_factory_date._obj.element.value="";
	    		return;
	    	}
    	}
	}			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addMortMachineEquipmentRecord.do" method="POST">
		
		<emp:gridLayout id="MortMachineEquipmentGroup" title="机械设备" maxColumn="2">
			<emp:text id="MortMachineEquipment.machine_id" label="机器设备编号" maxlength="30" required="false" hidden="true"/>
			<emp:text id="MortMachineEquipment.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true" />
			<emp:text id="MortMachineEquipment.spec_model" label="型号" maxlength="100" required="true" />
			<emp:text id="MortMachineEquipment.location" label="处所" maxlength="100" required="false" />
			<emp:select id="MortMachineEquipment.is_used" label="一手/二手" required="true" dictname="STD_SKILL_USED" />
			<emp:select id="MortMachineEquipment.equipment_status" label="设备状况（是否正常生产)" required="true" dictname="STD_ZX_YES_NO" />
			<emp:text id="MortMachineEquipment.equipment_amount" label="数量" maxlength="38" required="false" dataType="Double"/>
			<emp:text id="MortMachineEquipment.amount_unit" label="数量单位" maxlength="8" required="false" />
			<emp:text id="MortMachineEquipment.equipment_weigh" label="重量" maxlength="16" required="false" dataType="Double"/>
			<emp:text id="MortMachineEquipment.weigh_unit" label="重量单位" maxlength="8" required="false" />
			<emp:text id="MortMachineEquipment.used_years" label="已使用年限" maxlength="16" required="false" dataType="Double" readonly="true"/>
			<emp:text id="MortMachineEquipment.produce_factory" label="制造厂家" maxlength="100" required="false" />
			<emp:text id="MortMachineEquipment.producing_area" label="产地" maxlength="100" required="true" />
			<emp:text id="MortMachineEquipment.equip_power" label="功率" maxlength="16" required="false" dataType="Double"/>
			<emp:select id="MortMachineEquipment.power_unit_cd" label="功率单位" required="false" dictname="STD_POWER_UNIT" />
			<emp:select id="MortMachineEquipment.buy_curr_cd" label="购置币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY"/>
			<emp:text id="MortMachineEquipment.buy_price" label="购置价" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="MortMachineEquipment.user_name" label="使用人" maxlength="100" required="false" />
			<emp:date id="MortMachineEquipment.leave_factory_date" label="出厂日期" required="true" onblur="checkDt()"/>
			<emp:select id="MortMachineEquipment.use_status" label="使用状态" required="true" dictname="STD_USE_STATUS" />
			<emp:select id="MortMachineEquipment.is_import" label="是否进口设备" required="true" dictname="STD_ZX_YES_NO" />
			<emp:select id="MortMachineEquipment.is_supervise" label="是否为海关监管物" required="true" dictname="STD_ZX_YES_NO" />
			<emp:select id="MortMachineEquipment.machine_type" label="机器设备类型" required="false" dictname="STD_MACHINE_TYPE" />
			<emp:textarea id="MortMachineEquipment.memo" label="备注" maxlength="1000" required="false" colSpan="2" />
			
			
			
			
			
			
			<emp:text id="MortMachineEquipment.invoice_no" label="发票号" maxlength="40" required="false" hidden="true"/>
			<emp:date id="MortMachineEquipment.supervise_mature_date" label="监管到期日" required="false" hidden="true"/>
			<emp:text id="MortMachineEquipment.equip_no" label="设备编号//" maxlength="100" required="false" hidden="true" />
			<emp:text id="MortMachineEquipment.equip_name" label="设备名称" maxlength="200" required="false" hidden="true" />
			<emp:text id="MortMachineEquipment.new_old_cd" label="新旧情况//" maxlength="20" required="false" hidden="true" />
			<emp:text id="MortMachineEquipment.fuel_cd" label="动力燃料//" maxlength="20" required="false" hidden="true" />
			<emp:text id="MortMachineEquipment.buy_date" label="购入日期//" maxlength="10" required="false" hidden="true" />
			<emp:text id="MortMachineEquipment.metric_unit_cd" label="计量单位//" maxlength="20" required="false" hidden="true" />
			<emp:text id="MortMachineEquipment.leasehold_cd" label="租赁情况//" maxlength="20" required="false" hidden="true" />
			<emp:text id="MortMachineEquipment.leasehold_years" label="租赁年限//" maxlength="16" required="false" hidden="true" />
			<emp:text id="MortMachineEquipment.ind_eligible_certi" label="有无产品合格证//" maxlength="1" required="false" hidden="true" />
			<emp:text id="MortMachineEquipment.ind_safe_certi" label="有无安全检查证明//" maxlength="1" required="false" hidden="true" />
			<emp:text id="MortMachineEquipment.year_rent" label="年租金//" maxlength="16" required="false" hidden="true" />
			<emp:text id="MortMachineEquipment.detail_purpose" label="具体用途//" maxlength="100" required="false" hidden="true" />
			<emp:text id="MortMachineEquipment.ind_fire_certi" label="有无消防检查证明//" maxlength="1" required="false" hidden="true" />
			<emp:text id="MortMachineEquipment.use_life" label="设备使用寿命//" maxlength="16" required="false" hidden="true" />
			<emp:text id="MortMachineEquipment.heavy_repair_times" label="大修次数//" maxlength="8" required="false" hidden="true" />
			<emp:text id="MortMachineEquipment.owner_name" label="抵质押人" maxlength="100" required="false" hidden="true"/>
			<emp:date id="MortMachineEquipment.sys_update_time" label="系统更新时间" required="false" hidden="true" />
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

