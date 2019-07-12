<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%><emp:page>
<html>
<head>
<title>修改页面</title>
<style type="text/css">
	.emp_field_text_write {
		border: 1px solid #BCD7E2;
		text-align: left;
		width: 240px;
	};
	.emp_field_mobile_write {
		border: 1px solid #BCD7E2;
		text-align: left;
		width: 130px;
	};
	.emp_field_label_text {
		vertical-align: text-bottom;
		padding-top: 4px;
		text-align: right;
		width: 140px; 
	};
	.emp_field_text_input2 {
	border: 1px solid #BCD7E2;
	text-align: left;
	width: 450px;
	};
	.emp_field_select_select1 {
	width: 450px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
	background-color: #e3e3e3;
	};
</style>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "add"; 
	if(context.containsKey("op")){
		op = context.getDataValue("op").toString();
	}
	if("view".equals(op)){  //如果为查看页面控件只读
		request.setAttribute("canwrite","");
	}
%>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function setGeograPlace(data){
		LmtCoopBuilding.geogra_place._setValue(data.id);
		LmtCoopBuilding.geogra_place_displayname._setValue(data.label);
	}

	//异步提交申请数据
	function doSubmitLmtCoopBuilding(){
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("Y" == flag){
					alert("保存成功！");
					//document.getElementById("button_submitLmtCoopBuilding").disabled = "";
					var form = document.getElementById("submitForm");
					var url = '<emp:url action="updateLmtCoopBuildingRecord.do"/>';
					url = EMPTools.encodeURI(url);
					form.action = url;
				}else{
					alert("保存失败,失败原因："+jsonstr.msg);
				}
			}
		};
		var handleFailure = function(o) {
			alert("保存失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};

		var form = document.getElementById("submitForm");
		var result = LmtCoopBuilding._checkAll();
		if(result){
			page.dataGroups.dataGroup_in_formsubmitForm.toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			//document.getElementById("button_submitLmtCoopBuilding").disabled = "true";
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}else {
           alert("页面信息录入不完整！");
           return ;
		}
	};	

	//检查项目竣工日期、从业日期
	function checkDate(mod){
		var open_date = '${context.OPENDAY}';
		if("01"==mod){  //校验项目竣工日期
			var start_date = LmtCoopBuilding.start_date._getValue();
			var end_date = LmtCoopBuilding.end_date._getValue();
			if(""!=start_date && start_date > open_date){
				alert("项目开始日期应小于等于当前系统日期。");
				LmtCoopBuilding.start_date._setValue('');
				return false;
			}
			if(""!=end_date && end_date <= open_date){
			    /* modified by yangzy 2014/10/22 项目竣工日期校验改为提醒 start */
				//alert("项目竣工日期应大于当前系统日期。");
				//LmtCoopBuilding.end_date._setValue('');
				//return false;
				alert("风险提醒：楼盘开发项目竣工日期小于当前系统日期！！");
				/* modified by yangzy 2014/10/22 项目竣工日期校验改为提醒 end */
			}
		}
		if("02"==mod){  //校验项目从业日期
			var fjob_date = LmtCoopBuilding.fjob_date._getValue();
			var eoccu_date = LmtCoopBuilding.eoccu_date._getValue();
			if(""!=fjob_date && fjob_date > open_date){
				alert("从业时间应小于等于当前系统日期。");
				LmtCoopBuilding.fjob_date._setValue('');
				return false;
			}
			if(""!=eoccu_date && eoccu_date > open_date){
				alert("入企业时间应小于等于当前系统日期。");
				LmtCoopBuilding.eoccu_date._setValue('');
				return false;
			}
			if(""!=eoccu_date && ""!=eoccu_date && fjob_date> eoccu_date){
				alert("入企业时间应大于等于从业时间。");
				LmtCoopBuilding.eoccu_date._setValue('');
				return false;
			}
		}
	}

	//校验证件号码的合法性
	function checkCertCode(){
		var cert_type = LmtCoopBuilding.cert_type._getValue();
		if("100"==cert_type){ //身份证 
			var value = LmtCoopBuilding.cert_code._getValue();
			var length = value.length;
			if (length != 15 && length != 18){
			    alert("身份证号码，长度必须为15位或18位！");
			    return false;
			}
		  	if (length == 15){//15位的身份证号
		    	var reg = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/;
		    	var checkres = reg.test(value);
				if (!checkres) {
					alert("身份证号码格式不正确！");
					return false;
				}
		    }else if (length == 18){
		        var reg = /^[1-9]\d{6}[1-9]\d{2}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9|x|X])$/;
		    	var checkres = reg.test(value);
				if (!checkres) {
					alert("身份证号码格式不正确！");
					return false;
				}
		    }
		}
	}

	function onLoad(){
		//剔除对公客户的证件类型
		var options = LmtCoopBuilding.cert_type._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
			if(options[i].value == "20" || options[i].value == "22" ||options[i].value == "23"){
				options.remove(i);
			}
		}
	}

	//校验联系方式
	function checkLinkMode(){
		var link_mode = LmtCoopBuilding.link_mode._getValue();
		if(link_mode!=null&&link_mode!=''){
		// 	var reg = /^(0?[0-9]{2,3}-?){0,1}[0-9]{7,8}(-[0-9]{1,4}){0,1}$/;
		 	var reg = /(^(\d{3,4}-)?\d{7,8})$|(1[0-9]{10})$/;
	    	var checkres = reg.test(link_mode);
			if (!checkres) {
				alert('联系方式填写不正确！');
				LmtCoopBuilding.link_mode._setValue('');
			}
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onLoad()">
	<emp:form id="submitForm" action="${context.action}" method="POST">
		<emp:gridLayout id="LmtCoopBuildingGroup_info" maxColumn="2" title="楼盘项目信息">
			<emp:text id="LmtCoopBuilding.serno" label="流水号" maxlength="40" required="true" colSpan="2" readonly="true" cssLabelClass="emp_field_label_text" hidden="true"/>
			<emp:text id="LmtCoopBuilding.pro_no" label="项目编号" maxlength="40" required="true" readonly="true" cssLabelClass="emp_field_label_text"/>
			<emp:text id="LmtCoopBuilding.pro_name" label="项目名称" maxlength="60" required="true" cssLabelClass="emp_field_label_text" />
			<emp:text id="LmtCoopBuilding.totl_invest_amt" label="项目总投资(元)" maxlength="18" required="true" dataType="Currency" cssLabelClass="emp_field_label_text"/>
			<emp:text id="LmtCoopBuilding.wrr_name" label="项目权利人名称" maxlength="60" required="true" cssLabelClass="emp_field_label_text"/>
			<emp:text id="LmtCoopBuilding.totl_sale_amt" label="项目总销售收入(元)" maxlength="18" required="true" dataType="Currency" cssLabelClass="emp_field_label_text"/>
			<emp:text id="LmtCoopBuilding.other_sale_amt" label="其他部分销售总价(元)" maxlength="18" required="true" dataType="Currency" cssLabelClass="emp_field_label_text"/>
			<emp:date id="LmtCoopBuilding.start_date" label="项目开工时间" required="true" cssLabelClass="emp_field_label_text" onchange="checkDate('01')"/>
			<emp:date id="LmtCoopBuilding.end_date" label="项目竣工时间" required="true" cssLabelClass="emp_field_label_text" onblur="checkDate('01')"/>
		</emp:gridLayout>
		<emp:gridLayout id="LmtCoopBuildingGroup_details" maxColumn="2" title="楼盘详细信息">
			<emp:pop id="LmtCoopBuilding.geogra_place_displayname" label="地理位置" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" returnMethod="setGeograPlace" required="true"
			colSpan="2"  cssElementClass="emp_field_text_input2"/>
			<emp:text id="LmtCoopBuilding.geogra_place" label="位置" maxlength="100" required="true" colSpan="2" hidden="true"/>
			<emp:text id="LmtCoopBuilding.street" label="街道" maxlength="100" required="true" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:text id="LmtCoopBuilding.occup_squ" label="占地面积(平米)" maxlength="16" required="true" dataType="Double" cssLabelClass="emp_field_label_text"/>
			<emp:text id="LmtCoopBuilding.totl_resi_squ" label="住宅建筑面积(平米)" maxlength="16" required="true" dataType="Double" cssLabelClass="emp_field_label_text"/>
			<emp:text id="LmtCoopBuilding.totl_arch_squ" label="总建筑面积(平米)" maxlength="16" required="true" dataType="Double" cssLabelClass="emp_field_label_text" />
			<emp:text id="LmtCoopBuilding.resi_sale_value" label="住宅销售单价(元/平米)" maxlength="16" required="false" dataType="Double" cssLabelClass="emp_field_label_text" />
			<emp:text id="LmtCoopBuilding.comm_resi_squ" label="商业建筑面积(平米)" maxlength="16" required="false" dataType="Double" cssLabelClass="emp_field_label_text" />
			<emp:text id="LmtCoopBuilding.indt_resi_squ" label="工业建筑面积(平米)" maxlength="16" required="false" dataType="Double" cssLabelClass="emp_field_label_text" />
			<emp:text id="LmtCoopBuilding.comm_sale_value" label="商业销售单价(元/平米)" maxlength="16" required="false" dataType="Double" cssLabelClass="emp_field_label_text" />
			<emp:text id="LmtCoopBuilding.indt_sale_value" label="工业销售单价(元/平米)" maxlength="16" required="false" dataType="Double" cssLabelClass="emp_field_label_text" />
			<emp:text id="LmtCoopBuilding.other_resi_squ" label="其他建筑面积(平米)" maxlength="16" required="false" dataType="Double" cssLabelClass="emp_field_label_text" />
			<emp:select id="LmtCoopBuilding.is_cert_comp" label="是否五证齐全" required="true" dictname="STD_ZX_YES_NO" cssLabelClass="emp_field_label_text" />
			<emp:text id="LmtCoopBuilding.house_totl_num" label="住房总套数" maxlength="16" required="true" cssLabelClass="emp_field_label_text" dataType="Int"/>
			<emp:text id="LmtCoopBuilding.comm_totl_num" label="商用房总套数" maxlength="16" required="true" cssLabelClass="emp_field_label_text" dataType="Int"/>
		</emp:gridLayout>
		<emp:gridLayout id="LmtCoopBuildingGroup_manager" maxColumn="2" title="项目责任人信息">
			<emp:text id="LmtCoopBuilding.chief_name" label="负责人姓名" maxlength="60" required="true" cssLabelClass="emp_field_label_text" colSpan="2"/>
			<emp:select id="LmtCoopBuilding.cert_type" label="证件类型" required="true" dictname="STD_ZB_CERT_TYP" cssLabelClass="emp_field_label_text" defvalue="0"/>
			<emp:text id="LmtCoopBuilding.cert_code" label="证件号码" maxlength="20" required="true" cssLabelClass="emp_field_label_text" onblur="checkCertCode()" colSpan="2"/>
			<%if("update".equals(op)){ %>
			<emp:select id="LmtCoopBuilding.high_edu" label="最高学历" required="false" dictname="STD_ZX_EDU" colSpan="2" cssElementClass="emp_field_text_input2" />
			<%}else {%>
			<emp:select id="LmtCoopBuilding.high_edu" label="最高学历" required="false" dictname="STD_ZX_EDU" colSpan="2" cssFakeInputClass="emp_field_select_select1" />
			<%} %>
			<emp:text id="LmtCoopBuilding.link_mode" label="联系方式" maxlength="15" required="true" onblur="checkLinkMode()" cssElementClass="emp_field_mobile_write"/>
			<emp:date id="LmtCoopBuilding.fjob_date" label="从业时间" required="false" cssLabelClass="emp_field_label_text" onblur="checkDate('02')"/>
			<emp:date id="LmtCoopBuilding.eoccu_date" label="入企业时间" required="false" cssLabelClass="emp_field_label_text" onblur="checkDate('02')"/>
		</emp:gridLayout>
	<% if(!"view".equals(op)){ %>
		<div align="center">
			<br>
			<emp:button id="submitLmtCoopBuilding" label="修改" op="update"/>
			<emp:button id="reset" label="重置" op="update"/>
		</div>
	<%} %>
	</emp:form>
</body>
</html>
</emp:page>
