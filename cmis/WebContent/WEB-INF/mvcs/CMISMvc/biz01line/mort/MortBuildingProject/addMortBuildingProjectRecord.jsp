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
		MortBuildingProject._toForm(form);
		if(!MortBuildingProject._checkAll()){
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
		page.dataGroups.MortBondPledgeGroup.reset();
	};			
	function onReturnRegStateCode1(date){
		MortBuildingProject.guaranty_addr._obj.element.value=date.id;
		MortBuildingProject.guaranty_addr_displayname._obj.element.value=date.label;
	}		
	//两个日期作比较
	function checkInsurStartDate(){
		if(MortBuildingProject.land_use_begin_date._obj.element.value!=''){
			var e = MortBuildingProject.land_use_end_date._obj.element.value;
			var s = MortBuildingProject.land_use_begin_date._obj.element.value;
			if(e!=''){
				if(s>e){
            		alert('土地使用开始日期必须小于或等于土地使用终止日期！');
            		MortBuildingProject.land_use_begin_date._obj.element.value="";
            		return;
            	}
			}
		}
	}
	function checkInsurEndDate(){
		if(MortBuildingProject.land_use_end_date._obj.element.value!=''){
			var e = MortBuildingProject.land_use_end_date._obj.element.value;
			var s = MortBuildingProject.land_use_begin_date._obj.element.value;
			if(s!=''){
				if(s>e){
            		alert('土地使用终止日期必须大于或等于土地使用开始日期！');
            		MortBuildingProject.land_use_end_date._obj.element.value="";
            		return;
            	}
			}
		}
	}
	function doChange(){
		var is_notice_reg = MortBuildingProject.is_notice_reg._getValue();
		if("1"==is_notice_reg){
			MortBuildingProject.notice_reg_no._obj._renderHidden(false);
			MortBuildingProject.notice_reg_cert_no._obj._renderHidden(false);
			MortBuildingProject.notice_reg_date._obj._renderHidden(false);
			MortBuildingProject.reg_begin_date._obj._renderHidden(false);
			MortBuildingProject.notice_reg_no._obj._renderRequired(true);
			MortBuildingProject.notice_reg_cert_no._obj._renderRequired(true);
			MortBuildingProject.notice_reg_date._obj._renderRequired(true);
			MortBuildingProject.reg_begin_date._obj._renderRequired(true);
		}else{
			MortBuildingProject.notice_reg_no._obj._renderHidden(true);
			MortBuildingProject.notice_reg_cert_no._obj._renderHidden(true);
			MortBuildingProject.notice_reg_date._obj._renderHidden(true);
			MortBuildingProject.reg_begin_date._obj._renderHidden(true);
			MortBuildingProject.notice_reg_no._obj._renderRequired(false);
			MortBuildingProject.notice_reg_cert_no._obj._renderRequired(false);
			MortBuildingProject.notice_reg_date._obj._renderRequired(false);
			MortBuildingProject.reg_begin_date._obj._renderRequired(false);
			MortBuildingProject.notice_reg_no._setValue("");
			MortBuildingProject.notice_reg_cert_no._setValue("");
			MortBuildingProject.notice_reg_date._setValue("");
			MortBuildingProject.reg_begin_date._setValue("");
		}
	}
	function doLoad(){
		 doChange();
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="addMortBuildingProjectRecord.do" method="POST">
		<emp:gridLayout id="MortUsufructLandGroup" title="押品所处位置" maxColumn="2">
			<emp:text id="MortBuildingProject.guaranty_addr" label="押品座落地址" colSpan="2" hidden="true"/>
			<emp:pop id="MortBuildingProject.guaranty_addr_displayname" label="押品座落地址" colSpan="2" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" returnMethod="onReturnRegStateCode1" cssElementClass="emp_field_text_input2" required="true" />	
			<emp:text id="MortBuildingProject.guaranty_street" label="街道"  required="true" cssElementClass="emp_field_text_input2" colSpan="2"/>	
		</emp:gridLayout>
		<emp:gridLayout id="MortBuildingProjectGroup" title="在建工程" maxColumn="2">
			<emp:text id="MortBuildingProject.project_id" label="工程编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="MortBuildingProject.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true" />
			<emp:select id="MortBuildingProject.project_purpose" label="项目用途" required="true" dictname="STD_PRO_USE" />
			<emp:select id="MortBuildingProject.building_structure_cd" label="房屋结构" required="true" dictname="STD_ARCH_STR" />
			<emp:text id="MortBuildingProject.guarnaty_fina" label="抵押财产状况" maxlength="200" required="true" />
			<emp:select id="MortBuildingProject.land_obtain_way" label="土地获得方式" required="true" dictname="STD_ZX_LAND_WAY" />
			<emp:text id="MortBuildingProject.land_scheme_unit" label="建设用地规划许可证核发单位" maxlength="100" required="true" />
			<emp:text id="MortBuildingProject.project_scheme_unit" label="建设工程规划许可证核发单位" maxlength="100" required="true" />
			<emp:text id="MortBuildingProject.project_unit" label="施工许可证核发单位" maxlength="100" required="true" />
			<emp:text id="MortBuildingProject.plan_build_area" label="规划建筑面积(平方米)" maxlength="16" required="false" dataType="Double" />
			<emp:date id="MortBuildingProject.start_date" label="开工日期" required="false" />
			<emp:date id="MortBuildingProject.end_date" label="工程竣工日期" required="false" />
			<emp:text id="MortBuildingProject.land_area" label="土地面积（平方米）" maxlength="16" required="true" dataType="Double" />
			<emp:select id="MortBuildingProject.land_develop_level" label="土地开发程度" required="true" dictname="STD_LAND_DEVE_DEGREE" />
			<emp:select id="MortBuildingProject.land_use_type_cd" label="土地使用权类型" required="true" dictname="STD_LAND_USE_TYPE" />
			<emp:select id="MortBuildingProject.street_situation" label="临街状况" required="false" dictname="STD_FRONTAGE_STATUS" />
			<emp:date id="MortBuildingProject.land_use_begin_date" label="土地使用开始日期" required="true" onblur="checkInsurStartDate()"/>
			<emp:date id="MortBuildingProject.land_use_end_date" label="土地使用终止日期" required="true" onblur="checkInsurEndDate()"/>
			<emp:date id="MortBuildingProject.land_access_date" label="土地取得日期" required="false" />
			<emp:text id="MortBuildingProject.land_four" label="宗地四至" maxlength="100" required="false" />
			<emp:select id="MortBuildingProject.area_type_cd" label="所处区域" required="true" dictname="STD_AT_AREA" />
			<emp:select id="MortBuildingProject.land_character" label="土地所有制性质" required="true" dictname="STD_LAND_OWNERS" />
			<emp:text id="MortBuildingProject.builded_area" label="已完工建筑面积" maxlength="16" required="true" dataType="Double" />
			<emp:text id="MortBuildingProject.proj_plan_accnt" label="工程预算值" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="MortBuildingProject.pro_process" label="工程进度完成百分比" maxlength="256" required="false" dataType="Rate"/>
			<emp:text id="MortBuildingProject.finished_money" label="累计完成投资额(元)" maxlength="18" required="true" dataType="Currency" />
			<emp:select id="MortBuildingProject.ind_lack_proj_fund" label="是否欠工程款" required="true" dictname="STD_ZX_YES_NO" />
			<emp:text id="MortBuildingProject.lack_proj_fund" label="欠工程款额(元)" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="MortBuildingProject.build_unit" label="建设单位" maxlength="100" required="false" />
			<emp:text id="MortBuildingProject.land_layout_licence" label="建设用地规划许可证号" maxlength="200" required="false" />
			<emp:text id="MortBuildingProject.remise_price" label="出让价格(元)" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="MortBuildingProject.layout_licence" label="建设工程规划许可证号" maxlength="200" required="false" />
			<emp:select id="MortBuildingProject.land_fund_pay_cd" label="出让缴付情况" required="true" dictname="STD_SELL_AMT_CASE" />
			<emp:text id="MortBuildingProject.construction_licence" label="建筑工程施工许可证号" maxlength="200" required="false" />
			<emp:text id="MortBuildingProject.patch_land_money" label="应补出让金金额" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="MortBuildingProject.presell_licence" label="预售许可证" maxlength="100" required="false" />
			<emp:text id="MortBuildingProject.plot_ratio" label="容积率" maxlength="16" required="false" dataType="Rate" />
			<emp:select id="MortBuildingProject.is_notice_reg" label="是否处于预告登记阶段" required="true" dictname="STD_ZX_YES_NO" onblur="doChange()" />
			<emp:text id="MortBuildingProject.notice_reg_no" label="预告登记号" maxlength="40" required="false"/>
			<emp:text id="MortBuildingProject.notice_reg_cert_no" label="在建工程抵押权登记证明书编号" maxlength="40" required="false"/>
			<emp:date id="MortBuildingProject.notice_reg_date" label="预告登记日期" required="false"/>
			<emp:date id="MortBuildingProject.reg_begin_date" label="预可办理所有权登记起始日期" required="false"/>
			<emp:textarea id="MortBuildingProject.memo" label="备注" maxlength="1000" required="false" colSpan="2" />
			
			
			<emp:text id="MortBuildingProject.land_license_owner" label="土地权证人名称" maxlength="100" required="false" hidden="true"/>
			<emp:text id="MortBuildingProject.land_use_cert_no" label="土地使用权证书号" maxlength="100" required="false" hidden="true"/>
			
			<emp:text id="MortBuildingProject.circle_status" label="周边环境状况" maxlength="100" required="false" hidden="true"/>
			<emp:date id="MortBuildingProject.finish_date" label="预计竣工日期" required="false" hidden="true"/>
			<emp:select id="MortBuildingProject.land_license_ind" label="是否有土地证" required="false" hidden="true"/>
			<emp:text id="MortBuildingProject.land_license_type" label="土地权证类型" maxlength="100" required="false" hidden="true"/>
			<emp:text id="MortBuildingProject.project_info" label="PROJECT_INFO" maxlength="100" required="false" hidden="true"/>
			<emp:text id="MortBuildingProject.proj_final_accnt" label="工程决算值" maxlength="18" required="false" dataType="Currency" hidden="true"/>
			<emp:text id="MortBuildingProject.project_present_status" label="项目现状" maxlength="100" required="false" hidden="true"/>
			<emp:text id="MortBuildingProject.visible_schedule" label="工程形象进度" maxlength="16" required="false" hidden="true"/>
			<emp:date id="MortBuildingProject.sys_update_time" label="系统更新时间" required="false" hidden="true" />
			<emp:select id="MortBuildingProject.use_status" label="使用状态" required="false" dictname="STD_USE_STATUS" hidden="true"/>
			<emp:text id="MortBuildingProject.guaranty_fina" label="GUARANTY_FINA" maxlength="200" required="false" hidden="true"/>
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

