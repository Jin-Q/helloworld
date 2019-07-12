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
		/*--user code begin--*/
		function doAdd(){
		var form = document.getElementById('submitForm');
		MortRealHouseDetail._toForm(form);
		if(!MortRealHouseDetail._checkAll()){
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
		page.dataGroups.MortNobleMetalGroup.reset();
	};	
	function onReturnRegStateCode1(date){
		MortRealHouseDetail.guaranty_addr._obj.element.value=date.id;
		MortRealHouseDetail.guaranty_addr_displayname._obj.element.value=date.label;
	}			
	function doChange(){
		var structure = MortRealHouseDetail.building_struct_cd._getValue();
		if(structure=="04"){
			MortRealHouseDetail.durable_years._setValue("50");
			MortRealHouseDetail.durable_years._obj._renderHidden(false);
			MortRealHouseDetail.durable_years1._obj._renderHidden(true);
			MortRealHouseDetail.durable_years1._setValue("");
		}
		else if(structure=="02"){
			MortRealHouseDetail.durable_years._setValue("60");
			MortRealHouseDetail.durable_years._obj._renderHidden(false);
			MortRealHouseDetail.durable_years1._obj._renderHidden(true);
			MortRealHouseDetail.durable_years1._setValue("");
		}
		else if(structure=="01"){
			MortRealHouseDetail.durable_years._setValue("70");
			MortRealHouseDetail.durable_years._obj._renderHidden(false);
			MortRealHouseDetail.durable_years1._obj._renderHidden(true);
			MortRealHouseDetail.durable_years1._setValue("");
		}
		else if(structure=="09"){
			MortRealHouseDetail.durable_years._setValue("40");
			MortRealHouseDetail.durable_years._obj._renderHidden(false);
			MortRealHouseDetail.durable_years1._obj._renderHidden(true);
			MortRealHouseDetail.durable_years1._setValue("");
		}else if(structure=="05"){
			MortRealHouseDetail.durable_years._obj._renderHidden(true);
			MortRealHouseDetail.durable_years._setValue("");
			MortRealHouseDetail.durable_years1._obj._renderHidden(false);
			MortRealHouseDetail.durable_years1._setValue("");
		}
	}
	function doSetValue(){
		MortRealHouseDetail.durable_years._setValue(MortRealHouseDetail.durable_years1._getValue());
	}
	function doChange1(){
		var is_notice_reg = MortRealHouseDetail.is_notice_reg._getValue();
		if("1"==is_notice_reg){
			MortRealHouseDetail.notice_reg_no._obj._renderHidden(false);
			MortRealHouseDetail.notice_reg_cert_no._obj._renderHidden(false);
			MortRealHouseDetail.notice_reg_date._obj._renderHidden(false);
			MortRealHouseDetail.reg_begin_date._obj._renderHidden(false);
			MortRealHouseDetail.notice_reg_no._obj._renderRequired(true);
			MortRealHouseDetail.notice_reg_cert_no._obj._renderRequired(true);
			MortRealHouseDetail.notice_reg_date._obj._renderRequired(true);
			MortRealHouseDetail.reg_begin_date._obj._renderRequired(true);
		}else{
			MortRealHouseDetail.notice_reg_no._obj._renderHidden(true);
			MortRealHouseDetail.notice_reg_cert_no._obj._renderHidden(true);
			MortRealHouseDetail.notice_reg_date._obj._renderHidden(true);
			MortRealHouseDetail.reg_begin_date._obj._renderHidden(true);
			MortRealHouseDetail.notice_reg_no._obj._renderRequired(false);
			MortRealHouseDetail.notice_reg_cert_no._obj._renderRequired(false);
			MortRealHouseDetail.notice_reg_date._obj._renderRequired(false);
			MortRealHouseDetail.reg_begin_date._obj._renderRequired(false);
			MortRealHouseDetail.notice_reg_no._setValue("");
			MortRealHouseDetail.notice_reg_cert_no._setValue("");
			MortRealHouseDetail.notice_reg_date._setValue("");
			MortRealHouseDetail.reg_begin_date._setValue("");
		}
	}
	function doLoad(){
		doChange1();
		var structure = MortRealHouseDetail.building_struct_cd._getValue();
		if(structure=="05"){
			MortRealHouseDetail.durable_years._obj._renderHidden(true);
			MortRealHouseDetail.durable_years1._obj._renderHidden(false);
			MortRealHouseDetail.durable_years1._setValue(MortRealHouseDetail.durable_years._getValue());
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="addMortRealHouseDetailRecord.do" method="POST">
		<emp:gridLayout id="MortRealHouseDetailGroup" title="押品所处位置" maxColumn="2">
			<emp:text id="MortRealHouseDetail.guaranty_addr" label="押品座落地址" colSpan="2" hidden="true"/>
			<emp:pop id="MortRealHouseDetail.guaranty_addr_displayname" label="押品座落地址" colSpan="2" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" returnMethod="onReturnRegStateCode1" cssElementClass="emp_field_text_input2" required="true" />	
			<emp:text id="MortRealHouseDetail.guaranty_street" label="街道"  required="true" cssElementClass="emp_field_text_input2" colSpan="2"/>	
		</emp:gridLayout>
		<emp:gridLayout id="MortRealHouseDetailGroup" title="房产明细信息" maxColumn="2">
			<emp:text id="MortRealHouseDetail.house_id" label="房屋编号" maxlength="30" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.guaranty_no" label="押品编号" maxlength="40" required="true" hidden="true"/>
			<emp:text id="MortRealHouseDetail.inhabit_no" label="所处小区" maxlength="100" required="false" />
			<emp:select id="MortRealHouseDetail.house_type_cd" label="房产类型" required="true" dictname="STD_GUAR_HOUSE_TYPE"/>
			<emp:text id="MortRealHouseDetail.house_man_name" label="房产商名称" maxlength="80" required="false" />
			<emp:select id="MortRealHouseDetail.house_land_type" label="房产分类" dictname="STD_ZB_REAL_ONE" required="false" />
			<emp:select id="MortRealHouseDetail.area_type" label="地域分类" dictname="STD_ZB_LAND_AREA" required="true" />
			<emp:text id="MortRealHouseDetail.house_cont_num" label="房产买卖合同号" maxlength="80" required="false" />
			<emp:text id="MortRealHouseDetail.land_price" label="应补出让金金额" maxlength="16" required="false" dataType="Currency"/>
			<emp:text id="MortRealHouseDetail.leasehold_years" label="租赁年限" maxlength="8" required="false" dataType="Double"/>
			<emp:text id="MortRealHouseDetail.tenancy_amt" label="年租金（元）" maxlength="16" required="false" dataType="Currency" />
			<emp:select id="MortRealHouseDetail.manage_status" label="目前管理状况" dictname="STD_MANA_STATUS" required="false" />
			<emp:text id="MortRealHouseDetail.building_num" label="所在楼座" maxlength="20" required="false" />
			<emp:text id="MortRealHouseDetail.floor" label="所在楼层" maxlength="30" required="true" />
			<emp:text id="MortRealHouseDetail.floor_count" label="楼层总层数" maxlength="30" required="true" dataType="Int" />
			<emp:text id="MortRealHouseDetail.floor_height" label="层高(米)" maxlength="8" required="false" dataType="Double" />
			<emp:text id="MortRealHouseDetail.build_area" label="建筑面积（㎡）" maxlength="16" required="true" dataType="Double" />
			<emp:text id="MortRealHouseDetail.land_parcel_area" label="宗地面积(㎡)" maxlength="16" required="false" dataType="Double" />
			<emp:text id="MortRealHouseDetail.court_area" label="庭院面积(㎡)" maxlength="16" required="false" dataType="Double" />
			<emp:select id="MortRealHouseDetail.keep_status" label="维护状态" required="false" dictname="STD_MAINTAIN_STATUS" />
			<emp:select id="MortRealHouseDetail.direction" label="朝向" required="false" dictname="STD_ZB_ORIENT" />
			<emp:select id="MortRealHouseDetail.house_model" label="户型" required="false" dictname="STD_HOUSE_MODEL" />
			<emp:select id="MortRealHouseDetail.building_struct_cd" label="建筑结构" required="true" dictname="STD_ARCH_STR" onchange="doChange()" />
			<emp:text id="MortRealHouseDetail.durable_years" label="耐用年限" maxlength="8" required="false" dataType="Int" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="MortRealHouseDetail.durable_years1" label="耐用年限" maxlength="8" required="false" dataType="Int" hidden="true" onblur="doSetValue()"/>
			<emp:select id="MortRealHouseDetail.fitment_degree" label="装修程度" required="false" dictname="STD_FITMENT_DEGREE" />
			<emp:select id="MortRealHouseDetail.use_status" label="使用状态" required="true" dictname="STD_USE_STATUS" />
			<emp:select id="MortRealHouseDetail.plane_layout_cd" label="平面布置优劣度" required="false" dictname="STD_FLAT_DEC_DEGREE" />
			<emp:select id="MortRealHouseDetail.equipment_facil_cd" label="设备设施" required="false" dictname="STD_EQUIP_STATUS" />
			<emp:date id="MortRealHouseDetail.constructed_date" label="建成日期" required="false" />
			<emp:text id="MortRealHouseDetail.land_share_area" label="分摊土地面积(㎡)" maxlength="16" required="false" dataType="Double" />
			<emp:select id="MortRealHouseDetail.land_use_type" label="土地使用权类型" required="false" dictname="STD_LAND_USE_TYPE" />
			<emp:text id="MortRealHouseDetail.land_use_cert_no" label="土地证号" maxlength="40" required="false" />
			<emp:date id="MortRealHouseDetail.build_date" label="首次购建日期" required="false" />
			<emp:date id="MortRealHouseDetail.over_work_date" label="竣工日期" required="false" />
			<emp:text id="MortRealHouseDetail.real_price" label="原单价(元)" maxlength="80" required="false" dataType="Currency"/>
			<emp:text id="MortRealHouseDetail.land_aera" label="土地使用权面积" maxlength="16" required="false" dataType="Double"/>
			<emp:date id="MortRealHouseDetail.land_use_begin_date" label="土地使用起始日期" required="false" />
			<emp:date id="MortRealHouseDetail.land_use_end_date" label="土地使用终止日期" required="false" />
			<emp:text id="MortRealHouseDetail.land_use_year" label="土地使用年限(年)" maxlength="16" required="false" dataType="Double" />
			<emp:text id="MortRealHouseDetail.land_remain_use_y" label="土地剩余使用年限（年）" maxlength="16" required="false" dataType="Double" />
			<emp:select id="MortRealHouseDetail.land_character" label="土地所有制性质" required="false" dictname="STD_LAND_OWNERS" />
			<emp:text id="MortRealHouseDetail.land_price_remark" label="土地出让金缴付情况" maxlength="1000" required="false"/>
			<emp:select id="MortRealHouseDetail.self_carbarn_ind" label="是否有私家车库" required="false" dictname="STD_ZX_YES_NO" />
			<emp:select id="MortRealHouseDetail.street_situation" label="临街状况" required="false" dictname="STD_FRONTAGE_STATUS"  />
			<emp:select id="MortRealHouseDetail.is_lift" label="是否带电梯" required="false" dictname="STD_ZX_YES_NO" />
			<emp:text id="MortRealHouseDetail.buy_price" label="购置价（元）" maxlength="20" required="false" dataType="Currency" />
			<emp:date id="MortRealHouseDetail.buy_date" label="购买日期" required="false" />
			<emp:select id="MortRealHouseDetail.is_notice_reg" label="是否处于预登记阶段" required="true" dictname="STD_ZX_YES_NO" onchange="doChange1()"/>
			<emp:text id="MortRealHouseDetail.notice_reg_no" label="预告登记号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.notice_reg_cert_no" label="商品房预告登记证明书编号" maxlength="40" required="false" hidden="true"/>
			<emp:date id="MortRealHouseDetail.notice_reg_date" label="预告登记日期" required="false" hidden="true"/>
			<emp:date id="MortRealHouseDetail.reg_begin_date" label="可办理所有权登记起始日期" required="false" hidden="true"/>
			<emp:textarea id="MortRealHouseDetail.remark" label="其他情况说明" maxlength="1000" required="false" colSpan="2"/>
			
			
			
			
			<emp:text id="MortRealHouseDetail.house_license_owner" label="房产权利人名称" maxlength="100" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.room_num" label="具体房号" maxlength="20" required="false" hidden="true"/>
			<emp:select id="MortRealHouseDetail.land_license_ind" label="是否有土地证" required="false" dictname="STD_ZX_YES_NO" hidden="true"/>
			<emp:text id="MortRealHouseDetail.public_assort_cd" label="公共配套设施" maxlength="20" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.environment_cd" label="环境（质量、景观）" maxlength="20" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.traffic_cd" label="交通条件" maxlength="20" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.prosperous_cd" label="繁华度" maxlength="20" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.ownership_cd" label="权属情况" maxlength="20" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.room_user" label="使用者" maxlength="100" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.building_type_cd" label="建筑形式" maxlength="20" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.eval_unit_price" label="评估单价" maxlength="20" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.ind_inhabit" label="居住与否" maxlength="1" required="false" hidden="true"/>
			<emp:select id="MortRealHouseDetail.city_location_ind" label="是否位于城市地带" required="false" dictname="STD_ZX_YES_NO" hidden="true"/>
			<emp:text id="MortRealHouseDetail.build_all_area" label="土地面积（平方米）" maxlength="16" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.circle_status" label="周边环境状况" maxlength="100" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.street_deep" label="邻街深度" maxlength="20" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.street_width" label="邻街宽度" maxlength="20" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.other_status" label="附属设施_服务设施_物业管理情况" maxlength="1000" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.finish_date" label="竣工日期" maxlength="10" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.asset_life" label="使用年限" maxlength="8" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.house_license_ind" label="有无房产权证" maxlength="1" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.house_license_type" label="房产权证类型" maxlength="100" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.house_license_num" label="房产权证号" maxlength="80" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.land_license_date" label="核发日期" maxlength="10" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.land_license_type" label="土地权证类型" maxlength="100" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.land_license_num" label="土地权证书号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.land_license_owner" label="土地权利人名称" maxlength="100" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.floor_area" label="基建面积" maxlength="16" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.land_usufruct_ki_cd" label="土地使用权性质" maxlength="20" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.proj_start_date" label="开工日期" maxlength="10" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.land_access_date" label="土地取得日期" maxlength="10" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.is_used" label="一手/二手" maxlength="2" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.plan_complete_date" label="预计竣工日期" maxlength="10" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.sys_update_time" label="系统更新时间" maxlength="10" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.land_acquiring_cd" label="土地取得方式" maxlength="2" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.land_use_area" label="用地面积" maxlength="16" required="false" hidden="true"/>
			<emp:text id="MortRealHouseDetail.tenancy_circe" label="租赁情况" maxlength="1000" required="false" hidden="true"/>
			
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

