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
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 450px;
}
</style>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">

	/*--user code begin--*/
		function doAdd(){
		var form = document.getElementById('submitForm');
		MortCarBarnDetail._toForm(form);
		if(!MortCarBarnDetail._checkAll()){
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
		page.dataGroups.MortCarBarnDetailGroup.reset();
	};	
	function onReturnRegStateCode1(date){
		MortCarBarnDetail.guaranty_addr._obj.element.value=date.id;
		MortCarBarnDetail.guaranty_addr_displayname._obj.element.value=date.label;
	}			
	function doChange(){
		var is_notice_reg = MortCarBarnDetail.is_notice_reg._getValue();
		if("1"==is_notice_reg){
			MortCarBarnDetail.notice_reg_no._obj._renderHidden(false);
			MortCarBarnDetail.notice_reg_cert_no._obj._renderHidden(false);
			MortCarBarnDetail.notice_reg_date._obj._renderHidden(false);
			MortCarBarnDetail.reg_begin_date._obj._renderHidden(false);
			MortCarBarnDetail.notice_reg_no._obj._renderRequired(true);
			MortCarBarnDetail.notice_reg_cert_no._obj._renderRequired(true);
			MortCarBarnDetail.notice_reg_date._obj._renderRequired(true);
			MortCarBarnDetail.reg_begin_date._obj._renderRequired(true);
		}else{
			MortCarBarnDetail.notice_reg_no._obj._renderHidden(true);
			MortCarBarnDetail.notice_reg_cert_no._obj._renderHidden(true);
			MortCarBarnDetail.notice_reg_date._obj._renderHidden(true);
			MortCarBarnDetail.reg_begin_date._obj._renderHidden(true);
			MortCarBarnDetail.notice_reg_no._obj._renderRequired(false);
			MortCarBarnDetail.notice_reg_cert_no._obj._renderRequired(false);
			MortCarBarnDetail.notice_reg_date._obj._renderRequired(false);
			MortCarBarnDetail.reg_begin_date._obj._renderRequired(false);
			MortCarBarnDetail.notice_reg_no._setValue("");
			MortCarBarnDetail.notice_reg_cert_no._setValue("");
			MortCarBarnDetail.notice_reg_date._setValue("");
			MortCarBarnDetail.reg_begin_date._setValue("");
		}
	}
	function doLoad(){
		 doChange();
		 var structure = MortCarBarnDetail.building_structure_cd._getValue();
		 if(structure=="05"){
				MortCarBarnDetail.durable_years._obj._renderHidden(true);
				MortCarBarnDetail.durable_years1._obj._renderHidden(false);
				MortCarBarnDetail.durable_years1._setValue(MortCarBarnDetail.durable_years._getValue());
		 }
	}
	//校验建成年代
	function checkConstructedDate(){
		var ConstructedDate = MortCarBarnDetail.constructed_date._getValue();
		//var curr_time='${context.OPENDAY}';
		//var year=curr_time.substr(0,4);
		if(ConstructedDate!='')
		{
			if(ConstructedDate<1900||ConstructedDate>2049){
				alert("建成年代必须在1900年和2049年之间！");
				MortCarBarnDetail.constructed_date._obj.element.value="";
			}
		}
		//校验建成年代与土地使用起始日期
		setDateValue();
		var land_use_begin_date = MortCarBarnDetail.land_use_begin_date._obj.element.value;
		var constructed_date = MortCarBarnDetail.constructed_date_hidden._obj.element.value;
		if(land_use_begin_date!=''&&MortCarBarnDetail.constructed_date._obj.element.value!=''){
			if(land_use_begin_date>constructed_date){
				alert('建成年代不能早于土地起始日期！');
				MortCarBarnDetail.constructed_date._obj.element.value = '';
				return;
			}
		}
	}
	//给隐藏域建成年代设置日期，用于校验
	function setDateValue(){
		var constructed_date = MortCarBarnDetail.constructed_date._obj.element.value;
		constructed_date = constructed_date + '-12-31';
		MortCarBarnDetail.constructed_date_hidden._setValue(constructed_date);
	}
	function checkLandUseBeginDate(){
		setDateValue();
		if(MortCarBarnDetail.land_use_begin_date._obj.element.value!=''&&MortCarBarnDetail.constructed_date._obj.element.value!=''){
			var e = MortCarBarnDetail.land_use_begin_date._obj.element.value;
			var s = MortCarBarnDetail.land_use_end_date._obj.element.value;
			if(e!=''){
				if(s>e){
            		alert('土地使用起始日期不能大于建成年代！');
            		MortCarBarnDetail.constructed_date_hidden._obj.element.value="";
            		return;
            	}
			}
		}
		if(MortCarBarnDetail.land_use_begin_date._obj.element.value!=''&&MortCarBarnDetail.land_use_end_date._obj.element.value!=''){
			var e = MortCarBarnDetail.land_use_end_date._obj.element.value;
			var s = MortCarBarnDetail.land_use_begin_date._obj.element.value;
			if(e!=''){
				if(s>e){
            		alert('起始日期不能大于终止日期！');
            		MortCarBarnDetail.land_use_begin_date._obj.element.value="";
            		return;
            	}
			}
		}
	}
	function checkLandUseEndDate(){
		if(MortCarBarnDetail.land_use_end_date._obj.element.value!=''){
			var occur_date = MortCarBarnDetail.land_use_end_date._getValue();
			var openDay='${context.OPENDAY}';
			if(occur_date!=''){
				if(CheckDate1BeforeDate2(occur_date,openDay)){
		    		alert('终止日期不能小于当前日期！');
		    		MortCarBarnDetail.land_use_end_date._obj.element.value="";
		    		return;
		    	}
	    	}
		}
		if(MortCarBarnDetail.land_use_begin_date._obj.element.value!=''&&MortCarBarnDetail.land_use_end_date._obj.element.value!=''){
			if(MortCarBarnDetail.land_use_begin_date._obj.element.value!=''&&MortCarBarnDetail.land_use_end_date._obj.element.value!=''){
				var e = MortCarBarnDetail.land_use_end_date._obj.element.value;
				var s = MortCarBarnDetail.land_use_begin_date._obj.element.value;
				if(e!=''){
					if(s>e){
	            		alert('起始日期不能大于终止日期！');
	            		MortCarBarnDetail.land_use_end_date._obj.element.value="";
	            		return;
	            	}
				}
			}
		}
	}
	function changeStructure(){
		var structure = MortCarBarnDetail.building_structure_cd._getValue();
		if(structure=="04"){
			MortCarBarnDetail.durable_years._setValue("50");
			MortCarBarnDetail.durable_years._obj._renderHidden(false);
			MortCarBarnDetail.durable_years1._obj._renderHidden(true);
			MortCarBarnDetail.durable_years1._setValue("");
		}
		else if(structure=="02"){
			MortCarBarnDetail.durable_years._setValue("60");
			MortCarBarnDetail.durable_years._obj._renderHidden(false);
			MortCarBarnDetail.durable_years1._obj._renderHidden(true);
			MortCarBarnDetail.durable_years1._setValue("");
		}
		else if(structure=="01"){
			MortCarBarnDetail.durable_years._setValue("70");
			MortCarBarnDetail.durable_years._obj._renderHidden(false);
			MortCarBarnDetail.durable_years1._obj._renderHidden(true);
			MortCarBarnDetail.durable_years1._setValue("");
		}
		else if(structure=="09"){
			MortCarBarnDetail.durable_years._setValue("40");
			MortCarBarnDetail.durable_years._obj._renderHidden(false);
			MortCarBarnDetail.durable_years1._obj._renderHidden(true);
			MortCarBarnDetail.durable_years1._setValue("");
		}else if(structure=="05"){
			MortCarBarnDetail.durable_years._obj._renderHidden(true);
			MortCarBarnDetail.durable_years._setValue("");
			MortCarBarnDetail.durable_years1._obj._renderHidden(false);
			MortCarBarnDetail.durable_years1._setValue("");
		}
	}
	function doSetValue(){
		MortCarBarnDetail.durable_years._setValue(MortCarBarnDetail.durable_years1._getValue());
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="addMortCarBarnDetailRecord.do" method="POST">
		<emp:gridLayout id="MortCarBarnDetailGroup" title="押品所处位置" maxColumn="2">
			<emp:text id="MortCarBarnDetail.guaranty_addr" label="押品座落地址" colSpan="2" hidden="true"/>
			<emp:pop id="MortCarBarnDetail.guaranty_addr_displayname" label="押品座落地址" colSpan="2" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" returnMethod="onReturnRegStateCode1" cssElementClass="emp_field_text_input2" required="true" />	
			<emp:text id="MortCarBarnDetail.guaranty_street" label="街道"  required="true" cssElementClass="emp_field_text_input2" colSpan="2"/>	
		</emp:gridLayout>
		<emp:gridLayout id="MortCarBarnDetailGroup" title="车位/车库明细信息" maxColumn="2">
			<emp:text id="MortCarBarnDetail.barn_detail_id" label="车位/车库编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="MortCarBarnDetail.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true"/>
			<emp:select id="MortCarBarnDetail.is_barn" label="是否车库" required="true" dictname="STD_ZX_YES_NO" />
			<emp:text id="MortCarBarnDetail.design_use" label="设计用途" maxlength="200" required="true" cssElementClass="emp_field_text_input2" colSpan="2"/>
			<emp:text id="MortCarBarnDetail.inhabit_no" label="所处小区" maxlength="100" required="true" cssElementClass="emp_field_text_input2" colSpan="2"/>
			<emp:text id="MortCarBarnDetail.build_area" label="建筑面积(平方米)" maxlength="16" required="true" dataType="Double" />
			<emp:text id="MortCarBarnDetail.building_num" label="所在楼座" maxlength="20" required="false" />
			<emp:text id="MortCarBarnDetail.floor" label="所在楼层" maxlength="8" required="true" />
			<emp:select id="MortCarBarnDetail.building_structure_cd" label="建筑结构" required="true" dictname="STD_ARCH_STR" onchange="changeStructure()"/>
			<emp:text id="MortCarBarnDetail.durable_years" label="耐用年限" maxlength="8" required="false" dataType="Int" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="MortCarBarnDetail.durable_years1" label="耐用年限" maxlength="8" required="false" dataType="Int" hidden="true" onblur="doSetValue()"/>
			<emp:select id="MortCarBarnDetail.use_status" label="使用状态" required="true" dictname="STD_USE_STATUS" />
			<emp:text id="MortCarBarnDetail.constructed_date" label="建成年代（年）" maxlength="10" required="false"/>
			<emp:select id="MortCarBarnDetail.land_use_type" label="土地使用权类型" required="false" dictname="STD_LAND_USE_TYPE" />
			<emp:date id="MortCarBarnDetail.land_use_begin_date" label="土地使用起始日期" required="false" onblur="checkLandUseBeginDate()"/>
			<emp:date id="MortCarBarnDetail.land_use_end_date" label="土地使用终止日期" required="false" onblur="checkLandUseEndDate()"/>
			<emp:select id="MortCarBarnDetail.land_character" label="土地所有制性质" required="false" dictname="STD_LAND_OWNERS" />
			<emp:text id="MortCarBarnDetail.land_use_cert_no" label="土地证号" maxlength="40" required="false" />
			<emp:text id="MortCarBarnDetail.buy_price" label="购置价（元）" maxlength="18" required="true" dataType="Currency" />
			<emp:date id="MortCarBarnDetail.buy_date" label="购买时间" required="false" />
			<emp:select id="MortCarBarnDetail.is_notice_reg" label="是否处于预告登记阶段" required="true" dictname="STD_ZX_YES_NO" onchange="doChange()"/>
			<emp:text id="MortCarBarnDetail.notice_reg_no" label="预告登记号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="MortCarBarnDetail.notice_reg_cert_no" label="预告登记权利号" maxlength="40" required="false" hidden="true"/>
			<emp:date id="MortCarBarnDetail.notice_reg_date" label="预告登记日期" required="false" hidden="true"/>
			<emp:date id="MortCarBarnDetail.reg_begin_date" label="可办理所有权登记起始日期" required="false" hidden="true"/>
			<emp:textarea id="MortCarBarnDetail.memo" label="备注" maxlength="1000" required="false" colSpan="2" />
			
			<emp:date id="MortCarBarnDetail.constructed_date_hidden" label="建成年代(年)" hidden="true"/>
			<emp:date id="MortCarBarnDetail.sys_update_time" label="系统更新时间" required="false" hidden="true"/>
			<emp:text id="MortCarBarnDetail.locate_addr" label="座落地址" maxlength="20" required="false" hidden="true"/>
			<emp:text id="MortCarBarnDetail.street" label="街道" maxlength="100" required="false" hidden="true"/>
			
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

