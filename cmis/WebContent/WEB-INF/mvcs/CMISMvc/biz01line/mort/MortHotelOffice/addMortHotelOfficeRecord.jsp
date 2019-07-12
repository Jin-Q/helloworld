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
		MortHotelOffice._toForm(form);
		if(!MortHotelOffice._checkAll()){
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
		page.dataGroups.MortHotelOfficeGroup.reset();
	};	
	function onReturnRegStateCode1(date){
		MortHotelOffice.guaranty_addr._obj.element.value=date.id;
		MortHotelOffice.guaranty_addr_displayname._obj.element.value=date.label;
	}	
	//两个日期作比较
	function checkInsurStartDate(){
		if(MortHotelOffice.land_use_begin_date._obj.element.value!=''){
			var e = MortHotelOffice.land_use_end_date._obj.element.value;
			var s = MortHotelOffice.land_use_begin_date._obj.element.value;
			if(e!=''){
				if(s>e){
            		alert('土地使用起始日期必须小于或等于土地使用终止日期！');
            		MortHotelOffice.land_use_begin_date._obj.element.value="";
            		return;
            	}
			}
		}
	}
	function checkInsurEndDate(){
		if(MortHotelOffice.land_use_end_date._obj.element.value!=''){
			var e = MortHotelOffice.land_use_end_date._obj.element.value;
			var s = MortHotelOffice.land_use_begin_date._obj.element.value;
			if(s!=''){
				if(s>e){
            		alert('土地使用终止日期必须大于或等于土地使用起始日期！');
            		MortHotelOffice.land_use_end_date._obj.element.value="";
            		return;
            	}
			}
		}
	}			
	function doChange(){
		var is_notice_reg = MortHotelOffice.is_notice_reg._getValue();
		if("1"==is_notice_reg){
			MortHotelOffice.notice_reg_no._obj._renderHidden(false);
			MortHotelOffice.notice_reg_cert_no._obj._renderHidden(false);
			MortHotelOffice.notice_reg_date._obj._renderHidden(false);
			MortHotelOffice.reg_begin_date._obj._renderHidden(false);
			MortHotelOffice.notice_reg_no._obj._renderRequired(true);
			MortHotelOffice.notice_reg_cert_no._obj._renderRequired(true);
			MortHotelOffice.notice_reg_date._obj._renderRequired(true);
			MortHotelOffice.reg_begin_date._obj._renderRequired(true);
		}else{
			MortHotelOffice.notice_reg_no._obj._renderHidden(true);
			MortHotelOffice.notice_reg_cert_no._obj._renderHidden(true);
			MortHotelOffice.notice_reg_date._obj._renderHidden(true);
			MortHotelOffice.reg_begin_date._obj._renderHidden(true);
			MortHotelOffice.notice_reg_no._obj._renderRequired(false);
			MortHotelOffice.notice_reg_cert_no._obj._renderRequired(false);
			MortHotelOffice.notice_reg_date._obj._renderRequired(false);
			MortHotelOffice.reg_begin_date._obj._renderRequired(false);
			MortHotelOffice.notice_reg_no._setValue("");
			MortHotelOffice.notice_reg_cert_no._setValue("");
			MortHotelOffice.notice_reg_date._setValue("");
			MortHotelOffice.reg_begin_date._setValue("");
		}
	}
	function doLoad(){
		 doChange();
		 var structure = MortHotelOffice.building_structure_cd._getValue();
		 if(structure=="05"){
			 MortHotelOffice.durable_years._obj._renderHidden(true);
			 MortHotelOffice.durable_years1._obj._renderHidden(false);
			 MortHotelOffice.durable_years1._setValue(MortHotelOffice.durable_years._getValue());
		 }
	}
	function changeStructure(){
		var structure = MortHotelOffice.building_structure_cd._getValue();
		if(structure=="04"){
			MortHotelOffice.durable_years._setValue("50");
			MortHotelOffice.durable_years._obj._renderHidden(false);
			MortHotelOffice.durable_years1._obj._renderHidden(true);
			MortHotelOffice.durable_years1._setValue("");
		}
		else if(structure=="02"){
			MortHotelOffice.durable_years._setValue("60");
			MortHotelOffice.durable_years._obj._renderHidden(false);
			MortHotelOffice.durable_years1._obj._renderHidden(true);
			MortHotelOffice.durable_years1._setValue("");
		}
		else if(structure=="01"){
			MortHotelOffice.durable_years._setValue("70");
			MortHotelOffice.durable_years._obj._renderHidden(false);
			MortHotelOffice.durable_years1._obj._renderHidden(true);
			MortHotelOffice.durable_years1._setValue("");
		}
		else if(structure=="09"){
			MortHotelOffice.durable_years._setValue("40");
			MortHotelOffice.durable_years._obj._renderHidden(false);
			MortHotelOffice.durable_years1._obj._renderHidden(true);
			MortHotelOffice.durable_years1._setValue("");
		}else if(structure=="05"){
			MortHotelOffice.durable_years._obj._renderHidden(true);
			MortHotelOffice.durable_years._setValue("");
			MortHotelOffice.durable_years1._obj._renderHidden(false);
			MortHotelOffice.durable_years1._setValue("");
		}
	}
	function doSetValue(){
		MortHotelOffice.durable_years._setValue(MortHotelOffice.durable_years1._getValue());
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="addMortHotelOfficeRecord.do" method="POST">
		<emp:gridLayout id="MortHotelOfficeGroup" title="押品所处位置" maxColumn="2">
			<emp:text id="MortHotelOffice.guaranty_addr" label="押品座落地址" colSpan="2" hidden="true"/>
			<emp:pop id="MortHotelOffice.guaranty_addr_displayname" label="押品座落地址" colSpan="2" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" returnMethod="onReturnRegStateCode1" cssElementClass="emp_field_text_input2" required="true" />	
			<emp:text id="MortHotelOffice.guaranty_street" label="街道"  required="true" cssElementClass="emp_field_text_input2" colSpan="2"/>	
		</emp:gridLayout>
		<emp:gridLayout id="MortHotelOfficeGroup" title="酒店、办公" maxColumn="2">
			<emp:text id="MortHotelOffice.hotel_office_id" label="酒店/办公楼编码" maxlength="40" required="false" hidden="true"/>
			<emp:text id="MortHotelOffice.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true" />
			<emp:text id="MortHotelOffice.room_num" label="具体房号" maxlength="20" required="false" />
			<emp:text id="MortHotelOffice.building_num" label="所在楼座" maxlength="20" required="false" />
			<emp:text id="MortHotelOffice.floor_count" label="楼座总层数" maxlength="30" required="true" dataType="Int"/>
			<emp:text id="MortHotelOffice.floor" label="所在层数" maxlength="30" required="true" />
			<emp:text id="MortHotelOffice.floor_height" label="层高（米）" maxlength="8" required="false" dataType="Double" />
			<emp:text id="MortHotelOffice.build_area" label="建筑面积（平方米）" maxlength="16" required="true" dataType="Double" />
			<emp:text id="MortHotelOffice.land_use_area" label="用地面积（平方米）" maxlength="16" required="false" dataType="Double" />
			<emp:select id="MortHotelOffice.keep_status" label="维护状态" required="true" dictname="STD_MAINTAIN_STATUS" />
			<emp:select id="MortHotelOffice.building_structure_cd" label="建筑结构" required="true" dictname="STD_ARCH_STR" onchange="changeStructure()"/>
			<emp:text id="MortHotelOffice.durable_years" label="耐用年限" maxlength="8" required="false" dataType="Int" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="MortHotelOffice.durable_years" label="耐用年限" maxlength="8" required="false" dataType="Int" hidden="true" onblur="doSetValue()"/>
			<emp:select id="MortHotelOffice.fitment_degree" label="装修程度" required="true" dictname="STD_FITMENT_DEGREE" />
			<emp:select id="MortHotelOffice.direction" label="朝向" required="false" dictname="STD_ZB_ORIENT" />
			<emp:date id="MortHotelOffice.constructed_date" label="建成日期" required="false" />
			<emp:text id="MortHotelOffice.land_area" label="分摊土地面积（平方米）" maxlength="16" required="false" dataType="Double" />
			<emp:select id="MortHotelOffice.land_use_type" label="土地使用权类型" required="false" dictname="STD_LAND_USE_TYPE" />
			<emp:select id="MortHotelOffice.street_situation" label="临街状况" required="true" dictname="STD_FRONTAGE_STATUS" />
			<emp:date id="MortHotelOffice.land_use_begin_date" label="土地使用起始日期" required="false" onblur="checkInsurStartDate()"/>
			<emp:date id="MortHotelOffice.land_use_end_date" label="土地使用终止日期" required="false" onblur="checkInsurEndDate()"/>
			<emp:select id="MortHotelOffice.land_character" label="土地所有制性质" required="false" dictname="STD_LAND_OWNERS" />
			<emp:select id="MortHotelOffice.is_lift" label="是否带电梯" required="true" dictname="STD_ZX_YES_NO" />
			<emp:text id="MortHotelOffice.buy_price" label="购置价（元）" maxlength="18" required="false" dataType="Currency" />
			<emp:select id="MortHotelOffice.use_status" label="使用状态" required="true" dictname="STD_USE_STATUS" />
			<emp:select id="MortHotelOffice.is_notice_reg" label="是否处于预告登记阶段" required="true" dictname="STD_ZX_YES_NO" onchange="doChange()"/>
			<emp:text id="MortHotelOffice.notice_reg_no" label="预告登记号" maxlength="40" required="false"/>
			<emp:text id="MortHotelOffice.notice_reg_cert_no" label="预告登记权利号" maxlength="40" required="false"/>
			<emp:date id="MortHotelOffice.notice_reg_date" label="预告登记日期" required="false"/>
			<emp:date id="MortHotelOffice.reg_begin_date" label="可办理所有权登记起始日期" required="false"/>
			<emp:text id="MortHotelOffice.land_use_cert_no" label="土地使用权证书号" maxlength="40" required="false" />
			<emp:text id="MortHotelOffice.design_use" label="设计用途" maxlength="200" required="true" cssElementClass="emp_field_text_input2" colSpan="2"/>
		    <emp:textarea id="MortHotelOffice.remark" label="其他情况说明" maxlength="1000" required="false" colSpan="2" />
			
			
			
			<emp:select id="MortHotelOffice.land_license_ind" label="是否有土地证" required="false" dictname="STD_ZX_YES_NO" hidden="true"/>
			<emp:select id="MortHotelOffice.public_assort_est_cd" label="公共配套设施" required="false" dictname="STD_EQUIP_STATUS" hidden="true"/>
			<emp:text id="MortHotelOffice.environment_cd" label="环境（质量、景观）" maxlength="20" required="false" hidden="true"/>
			<emp:text id="MortHotelOffice.traffic_cd" label="交通条件" maxlength="20" required="false" hidden="true"/>
			<emp:text id="MortHotelOffice.prosperous_cd" label="繁华度" maxlength="20" required="false" hidden="true"/>
			<emp:date id="MortHotelOffice.buy_date" label="购买时间" required="false" hidden="true"/>
			
			<emp:date id="MortHotelOffice.plan_complete_date" label="预计竣工日期" required="false" hidden="true"/>
			<emp:date id="MortHotelOffice.sys_update_time" label="系统更新时间" required="false" hidden="true"/>
			<emp:text id="MortHotelOffice.locate_addr" label="座落地址" maxlength="20" required="false" hidden="true"/>
			<emp:text id="MortHotelOffice.street" label="街道" maxlength="100" required="false" hidden="true"/>
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

