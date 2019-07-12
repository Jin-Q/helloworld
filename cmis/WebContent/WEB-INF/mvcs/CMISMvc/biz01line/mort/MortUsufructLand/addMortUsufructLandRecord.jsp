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
		MortUsufructLand._toForm(form);
		if(!MortUsufructLand._checkAll()){
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
		page.dataGroups.MortUsufructLandGroup.reset();
	};			
	function onReturnRegStateCode1(date){
		MortUsufructLand.guaranty_addr._obj.element.value=date.id;
		MortUsufructLand.guaranty_addr_displayname._obj.element.value=date.label;
	}	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addMortUsufructLandRecord.do" method="POST">
		<emp:gridLayout id="MortUsufructLandGroup" title="押品所处位置" maxColumn="2">
			<emp:text id="MortUsufructLand.guaranty_addr" label="押品座落地址" colSpan="2" hidden="true"/>
			<emp:pop id="MortUsufructLand.guaranty_addr_displayname" label="押品座落地址" colSpan="2" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" returnMethod="onReturnRegStateCode1" cssElementClass="emp_field_text_input2" required="true" />	
			<emp:text id="MortUsufructLand.guaranty_street" label="街道"  required="true" cssElementClass="emp_field_text_input2" colSpan="2"/>	
		</emp:gridLayout>
		<emp:gridLayout id="MortUsufructLandGroup" title="土地使用权" maxColumn="2">
			<emp:text id="MortUsufructLand.land_id" label="土地使用权编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="MortUsufructLand.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true" />
			<emp:text id="MortUsufructLand.land_area" label="土地面积(平方米)" maxlength="16" required="true" dataType="Double" />
			<emp:text id="MortUsufructLand.share_area" label="分摊面积(平方米)" maxlength="16" required="true" dataType="Double" />
			<emp:select id="MortUsufructLand.land_user_type" label="土地使用权类型" required="true" dictname="STD_LAND_USE_TYPE" />
			<emp:select id="MortUsufructLand.land_dev_level" label="土地开发程度" required="false" dictname="STD_LAND_DEVE_DEGREE" />
			<emp:select id="MortUsufructLand.use_purpose_type_cd" label="土地用途" required="true" dictname="STD_ZB_USE" />
			<emp:select id="MortUsufructLand.is_building" label="是否有地上建筑物" required="true" dictname="STD_ZX_YES_NO" />
			<emp:text id="MortUsufructLand.sign_desc" label="地上情况说明" maxlength="256" required="false" />
			<emp:date id="MortUsufructLand.land_get_date" label="土地取得日期" required="false" />
			<emp:select id="MortUsufructLand.land_character" label="土地所有制性质" required="true" dictname="STD_LAND_OWNERS" />
			<emp:text id="MortUsufructLand.land_num" label="宗地号" maxlength="80" required="false" />
			<emp:text id="MortUsufructLand.land_map_num" label="宗地图图幅编号" maxlength="40" required="false" />
			<emp:select id="MortUsufructLand.project_plan_license_ind" label="是否取得建设用地规划许可证" required="false" dictname="STD_ZX_YES_NO" />
			<emp:select id="MortUsufructLand.area_type_cd" label="所处区域" required="true" dictname="STD_AT_AREA" />
			<emp:text id="MortUsufructLand.land_price" label="土地价款(元)" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="MortUsufructLand.land_four" label="宗地四至" maxlength="100" required="false" />
			<emp:text id="MortUsufructLand.patch_land_money" label="应补出让金金额（元）" maxlength="18" required="false" dataType="Currency" />
			<emp:select id="MortUsufructLand.remise_pay_info" label="出让金交付情况" required="true" dictname="STD_SELL_AMT_CASE"/>
			<emp:text id="MortUsufructLand.plot_ratio" label="容积率" maxlength="16" required="false" dataType="Percent" />
			<emp:textarea id="MortUsufructLand.remark" label="其他情况说明" maxlength="1000" required="false" colSpan="2" />
			
			
			<emp:date id="MortUsufructLand.land_use_begin_date" label="土地使用起始日期" required="false" hidden="true"/>
			<emp:date id="MortUsufructLand.land_use_end_date" label="土地使用终止日期" required="false" hidden="true"/>
			<emp:text id="MortUsufructLand.use_years" label="使用年限" maxlength="8" required="false" dataType="Double" hidden="true"/>
			<emp:text id="MortUsufructLand.land_license_num" label="土地权证号" maxlength="80" required="false" hidden="true"/>
			<emp:text id="MortUsufructLand.circle_status" label="周边环境状况" maxlength="100" required="false" hidden="true"/>
			<emp:select id="MortUsufructLand.land_license_ind" label="是否有土地证" required="false" dictname="STD_ZX_YES_NO" hidden="true"/>
			<emp:text id="MortUsufructLand.building_density" label="BUILDING_DENSITY" maxlength="16" required="false" hidden="true" />
			<emp:date id="MortUsufructLand.sys_update_time" label="系统更新时间" required="false" hidden="true" />
			<emp:select id="MortUsufructLand.use_status" label="使用状态" required="false" dictname="STD_USE_STATUS" hidden="true"/>
			<emp:text id="MortUsufructLand.land_license_owner" label="土地权证人" maxlength="100" required="false" hidden="true"/>
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

