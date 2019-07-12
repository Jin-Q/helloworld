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
		MortMarketStore._toForm(form);
		if(!MortMarketStore._checkAll()){
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
		page.dataGroups.MortMarketStoreGroup.reset();
	};	
	function onReturnRegStateCode1(date){
		MortMarketStore.guaranty_addr._obj.element.value=date.id;
		MortMarketStore.guaranty_addr_displayname._obj.element.value=date.label;
	}	
	//两个日期作比较
	function checkInsurStartDate(){
		if(MortMarketStore.land_use_begin_date._obj.element.value!=''){
			var e = MortMarketStore.land_use_end_date._obj.element.value;
			var s = MortMarketStore.land_use_begin_date._obj.element.value;
			if(e!=''){
				if(s>e){
            		alert('土地使用起始日期必须小于或等于土地使用终止日期！');
            		MortMarketStore.land_use_begin_date._obj.element.value="";
            		return;
            	}
			}
		}
	}
	function checkInsurEndDate(){
		if(MortMarketStore.land_use_end_date._obj.element.value!=''){
			var e = MortMarketStore.land_use_end_date._obj.element.value;
			var s = MortMarketStore.land_use_begin_date._obj.element.value;
			if(s!=''){
				if(s>e){
            		alert('土地使用终止日期必须大于或等于土地使用起始日期！');
            		MortMarketStore.land_use_end_date._obj.element.value="";
            		return;
            	}
			}
		}
	}		
	function changeStructure(){
		var structure = MortMarketStore.building_structure_cd._getValue();
		if(structure=="04"){
			MortMarketStore.durable_years._setValue("50");
			MortMarketStore.durable_years._obj._renderHidden(false);
			MortMarketStore.durable_years1._obj._renderHidden(true);
			MortMarketStore.durable_years1._setValue("");
		}
		else if(structure=="02"){
			MortMarketStore.durable_years._setValue("60");
			MortMarketStore.durable_years._obj._renderHidden(false);
			MortMarketStore.durable_years1._obj._renderHidden(true);
			MortMarketStore.durable_years1._setValue("");
		}
		else if(structure=="01"){
			MortMarketStore.durable_years._setValue("70");
			MortMarketStore.durable_years._obj._renderHidden(false);
			MortMarketStore.durable_years1._obj._renderHidden(true);
			MortMarketStore.durable_years1._setValue("");
		}
		else if(structure=="09"){
			MortMarketStore.durable_years._setValue("40");
			MortMarketStore.durable_years._obj._renderHidden(false);
			MortMarketStore.durable_years1._obj._renderHidden(true);
			MortMarketStore.durable_years1._setValue("");
		}else if(structure=="05"){
			MortMarketStore.durable_years._obj._renderHidden(true);
			MortMarketStore.durable_years._setValue("");
			MortMarketStore.durable_years1._obj._renderHidden(false);
			MortMarketStore.durable_years1._setValue("");
		}
	}
	function doSetValue(){
		MortMarketStore.durable_years._setValue(MortMarketStore.durable_years1._getValue());
	}
	function doLoad(){
		var structure = MortMarketStore.building_structure_cd._getValue();
		if(structure=="05"){
			MortMarketStore.durable_years._obj._renderHidden(true);
			MortMarketStore.durable_years1._obj._renderHidden(false);
			MortMarketStore.durable_years1._setValue(MortMarketStore.durable_years._getValue());
		}
	}
	function doChange(){
		var is_notice_reg = MortMarketStore.is_notice_reg._getValue();
		if("1"==is_notice_reg){
			MortMarketStore.notice_reg_no._obj._renderHidden(false);
			MortMarketStore.notice_reg_cert_no._obj._renderHidden(false);
			MortMarketStore.notice_reg_date._obj._renderHidden(false);
			MortMarketStore.reg_begin_date._obj._renderHidden(false);
			MortMarketStore.notice_reg_no._obj._renderRequired(true);
			MortMarketStore.notice_reg_cert_no._obj._renderRequired(true);
			MortMarketStore.notice_reg_date._obj._renderRequired(true);
			MortMarketStore.reg_begin_date._obj._renderRequired(true);
		}else{
			MortMarketStore.notice_reg_no._obj._renderHidden(true);
			MortMarketStore.notice_reg_cert_no._obj._renderHidden(true);
			MortMarketStore.notice_reg_date._obj._renderHidden(true);
			MortMarketStore.reg_begin_date._obj._renderHidden(true);
			MortMarketStore.notice_reg_no._obj._renderRequired(false);
			MortMarketStore.notice_reg_cert_no._obj._renderRequired(false);
			MortMarketStore.notice_reg_date._obj._renderRequired(false);
			MortMarketStore.reg_begin_date._obj._renderRequired(false);
			MortMarketStore.notice_reg_no._setValue("");
			MortMarketStore.notice_reg_cert_no._setValue("");
			MortMarketStore.notice_reg_date._setValue("");
			MortMarketStore.reg_begin_date._setValue("");
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="addMortMarketStoreRecord.do" method="POST">
		<emp:gridLayout id="MortMarketStoreGroup" title="押品所处位置" maxColumn="2">
			<emp:text id="MortMarketStore.guaranty_addr" label="押品座落地址" colSpan="2" hidden="true"/>
			<emp:pop id="MortMarketStore.guaranty_addr_displayname" label="押品座落地址" colSpan="2" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" returnMethod="onReturnRegStateCode1" cssElementClass="emp_field_text_input2" required="true" />	
			<emp:text id="MortMarketStore.guaranty_street" label="街道"  required="true" cssElementClass="emp_field_text_input2" colSpan="2"/>	
		</emp:gridLayout>
		<emp:gridLayout id="MortMarketStoreGroup" title="商场商铺" maxColumn="2">
			<emp:text id="MortMarketStore.market_id" label="商场商铺编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="MortMarketStore.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true" />
			<emp:text id="MortMarketStore.building_num" label="所在楼座" maxlength="20" required="false" />
			<emp:text id="MortMarketStore.floor" label="所在层数" maxlength="30" required="true" />
			<emp:text id="MortMarketStore.floor_count" label="楼座总层数" maxlength="30" required="true" dataType="Int"/>
			<emp:text id="MortMarketStore.floor_height" label="层高（米）" maxlength="8" required="false" dataType="Double" />
			<emp:text id="MortMarketStore.build_area" label="建筑面积(平方米)" maxlength="16" required="true" dataType="Double" />
			<emp:text id="MortMarketStore.land_parcel_area" label="宗地面积(平方米)" maxlength="16" required="false" dataType="Double" />
			<emp:select id="MortMarketStore.keep_status" label="维护状态" required="true" dictname="STD_MAINTAIN_STATUS" />
			<emp:select id="MortMarketStore.direction" label="朝向" required="false" dictname="STD_ZB_ORIENT" />
			<emp:select id="MortMarketStore.building_structure_cd" label="建筑结构" required="true" dictname="STD_ARCH_STR" onchange="changeStructure()"/>
			<emp:select id="MortMarketStore.fitment_degree" label="装修程度" required="true" dictname="STD_FITMENT_DEGREE" />
			<emp:text id="MortMarketStore.durable_years" label="耐用年限" maxlength="8" required="true" dataType="Int" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="MortMarketStore.durable_years1" label="耐用年限" maxlength="8" required="false" dataType="Int" onblur="doSetValue()" hidden="true"/>
			<emp:select id="MortMarketStore.use_status" label="使用状态" required="true" dictname="STD_USE_STATUS" />
			<emp:select id="MortMarketStore.is_samdwich" label="是否有夹层" required="true" dictname="STD_ZX_YES_NO" />
			<emp:date id="MortMarketStore.constructed_date" label="建成日期" required="false" />
			<emp:text id="MortMarketStore.land_share_area" label="分摊土地面积(平方米)" maxlength="16" required="false" dataType="Double" />
			<emp:text id="MortMarketStore.street_width" label="临街宽度" maxlength="20" required="false" dataType="Double" />
			<emp:select id="MortMarketStore.land_use_type" label="土地使用权类型" required="false" dictname="STD_LAND_USE_TYPE" />
			<emp:text id="MortMarketStore.street_deep" label="临街深度" maxlength="20" required="false" dataType="Double" />
			<emp:select id="MortMarketStore.street_situation" label="临街状况" required="true" dictname="STD_FRONTAGE_STATUS" />
			<emp:date id="MortMarketStore.buy_date" label="购买时间" required="false" />
			<emp:text id="MortMarketStore.land_use_cert_no" label="土地使用权证书号" maxlength="40" required="false" />
			<emp:date id="MortMarketStore.land_use_begin_date" label="土地使用起始日期" required="false" onblur="checkInsurStartDate()"/>
			<emp:date id="MortMarketStore.land_use_end_date" label="土地使用终止日期" required="false" onblur="checkInsurEndDate()"/>
			<emp:select id="MortMarketStore.land_character" label="土地所有制性质" required="false" dictname="STD_LAND_OWNERS" />
			<emp:select id="MortMarketStore.is_lift" label="是否带电梯" required="true" dictname="STD_ZX_YES_NO" />
			<emp:text id="MortMarketStore.buy_price" label="购置价(元)" maxlength="18" required="false" dataType="Currency" />
			<emp:select id="MortMarketStore.is_mark" label="是否是圈商" required="true" dictname="STD_ZX_YES_NO" />
			<emp:select id="MortMarketStore.is_four" label="是否独立店面" required="true" dictname="STD_ZX_YES_NO" />
			<emp:select id="MortMarketStore.is_notice_reg" label="是否处于预告登记阶段" required="true" dictname="STD_ZX_YES_NO" onchange="doChange()"/>
			<emp:text id="MortMarketStore.design_use" label="设计用途" maxlength="200" required="true" cssElementClass="emp_field_text_input2" colSpan="2"/>
			<emp:textarea id="MortMarketStore.remark" label="其他情况说明" maxlength="1000" required="false" colSpan="2" />
			
			
			
			
			
			
			
			<emp:text id="MortMarketStore.house_license_owner" label="房产权利人名称" maxlength="100" required="false" hidden="true"/>
			<emp:text id="MortMarketStore.manage_status" label="目前管理状况" maxlength="1000" required="false" hidden="true"/>
			<emp:select id="MortMarketStore.land_license_ind" label="是否有土地证" required="false" dictname="STD_ZX_YES_NO" hidden="true"/>
			<emp:text id="MortMarketStore.public_assort_est_cd" label="公共配套设施" maxlength="20" required="false" hidden="true"/>
			<emp:text id="MortMarketStore.environment_cd" label="环境（质量、景观）" maxlength="20" required="false" hidden="true"/>
			<emp:text id="MortMarketStore.traffic_cd" label="交通条件" maxlength="20" required="false" hidden="true"/>
			<emp:text id="MortMarketStore.prosperous_cd" label="繁华度" maxlength="20" required="false" hidden="true"/>
			<emp:text id="MortMarketStore.notice_reg_no" label="预告登记号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="MortMarketStore.notice_reg_cert_no" label="预告登记权利号" maxlength="40" required="false" hidden="true"/>
			<emp:date id="MortMarketStore.notice_reg_date" label="预告登记日期" required="false" hidden="true"/>
			<emp:date id="MortMarketStore.reg_begin_date" label="可办理所有权登记起始日期" required="false" hidden="true"/>
			<emp:date id="MortMarketStore.sys_update_time" label="系统更新时间" required="false" hidden="true"/>
			<emp:text id="MortMarketStore.mark_name" label="所属圈商名称" maxlength="200" required="false" hidden="true"/>
			<emp:text id="MortMarketStore.locate_addr" label="座落地址" maxlength="20" required="false" hidden="true"/>
			<emp:text id="MortMarketStore.street" label="街道" maxlength="100" required="false" hidden="true"/>
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

