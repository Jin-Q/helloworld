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
	.emp_field_text_input2 {
		border: 1px solid #BCD7E2;
		text-align: left;
		width: 450px;
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
	//异步提交申请数据
	function doSubmitLmtCoopCar(){
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
					//document.getElementById("button_submitLmtCoopCar").disabled = "";
					var form = document.getElementById("submitForm");
					var url = '<emp:url action="updateLmtCoopCarRecord.do"/>';
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
		var result = LmtCoopCar._checkAll();
		if(result){
			page.dataGroups.dataGroup_in_formsubmitForm.toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			//document.getElementById("button_submitLmtCoopCar").disabled = "true";
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}else {
           alert("页面信息录入不完整！");
           return ;
		}
	};	
	
	//校验证件号码的合法性
	function checkCertCode(){
		var cert_type = LmtCoopCar.cert_type._getValue();
		if("100"==cert_type){ //身份证 
			var value = LmtCoopCar.cert_code._getValue();
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
		var options = LmtCoopCar.cert_type._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
			if(options[i].value == "20" || options[i].value == "22" ||options[i].value == "23"){
				options.remove(i);
			}
		}
	}

	//设置专卖店地址1
	function setAgencyAddr(data){
		LmtCoopCar.agency_addr1._setValue(data.id);
		LmtCoopCar.agency_addr1_displayname._setValue(data.label);
	}
	//设置专卖店地址2
	function setAgencyAddr2(data){
		LmtCoopCar.agency_addr2._setValue(data.id);
		LmtCoopCar.agency_addr2_displayname._setValue(data.label);
	}
	//设置专卖店地址13
	function setAgencyAddr3(data){
		LmtCoopCar.agency_addr3._setValue(data.id);
		LmtCoopCar.agency_addr3_displayname._setValue(data.label);
	}
	//校验联系方式
	function checkLinkMode(){
		var link_mode = LmtCoopCar.link_mode._getValue();
		if(link_mode!=null&&link_mode!=''){
		// 	var reg = /^(0?[0-9]{2,3}-?){0,1}[0-9]{7,8}(-[0-9]{1,4}){0,1}$/;
		 	var reg = /(^(\d{3,4}-)?\d{7,8})$|(1[0-9]{10})$/;
	    	var checkres = reg.test(link_mode);
			if (!checkres) {
				alert('联系方式填写不正确！');
				LmtCoopCar.link_mode._setValue('');
			}
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onLoad()">
	<emp:form id="submitForm" action="${context.action}" method="POST">
		<emp:gridLayout id="LmtCoopCarGroup" maxColumn="2" title="汽车项目信息">
			<emp:text id="LmtCoopCar.serno" label="流水号" maxlength="40" required="true" colSpan="2" hidden="true" cssElementClass="emp_field_text_readonly" />
			<emp:text id="LmtCoopCar.pro_no" label="项目编号" maxlength="40" required="true" readonly="true" colSpan="2"/>
			<emp:text id="LmtCoopCar.pro_name" label="项目名称" maxlength="60" required="true" cssElementClass="emp_field_text_write"/>
			<emp:text id="LmtCoopCar.sale_cus_id" label="销售商编号" maxlength="30" required="false" hidden="true"/>
			<emp:text id="LmtCoopCar.sale_brh_num" label="销售网点数量" maxlength="4" required="true" dataType="Int"/>
			<emp:pop id="LmtCoopCar.agency_addr1_displayname" label="专卖店地址1" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" returnMethod="setAgencyAddr" required="true" colSpan="2" cssElementClass="emp_field_text_input2" />
			<emp:text id="LmtCoopCar.agency_addr1" label="专卖店地址1" maxlength="8" required="true" hidden="true"/>
			<emp:text id="LmtCoopCar.street1" label="街道1" maxlength="100" required="true" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:pop id="LmtCoopCar.agency_addr2_displayname" label="专卖店地址2" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" returnMethod="setAgencyAddr2" required="false" colSpan="2" cssElementClass="emp_field_text_input2" />
			<emp:text id="LmtCoopCar.agency_addr2" label="专卖店地址2" maxlength="8" required="false" hidden="true"/>
			<emp:text id="LmtCoopCar.street2" label="街道2" maxlength="100" required="false" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:pop id="LmtCoopCar.agency_addr3_displayname" label="专卖店地址3" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" returnMethod="setAgencyAddr3" required="false" colSpan="2" cssElementClass="emp_field_text_input2" />
			<emp:text id="LmtCoopCar.agency_addr3" label="专卖店地址3" maxlength="8" required="false" hidden="true"/>
			<emp:text id="LmtCoopCar.street3" label="街道3" maxlength="100" required="false" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:text id="LmtCoopCar.coop_brand" label="合作品牌" maxlength="80" required="false" />
			<emp:select id="LmtCoopCar.util_cls" label="汽车使用类别" required="true" dictname="STD_ZB_UTIL_CLS"/>
			<emp:text id="LmtCoopCar.chief_name" label="负责人姓名" maxlength="60" required="true" />
			<emp:select id="LmtCoopCar.cert_type" label="证件类型" required="true" dictname="STD_ZB_CERT_TYP" defvalue="0"/>
			<emp:text id="LmtCoopCar.cert_code" label="证件号码" maxlength="20" required="true" onblur="checkCertCode()" />
			<emp:text id="LmtCoopCar.link_mode" label="联系方式" maxlength="15" required="true" onblur="checkLinkMode()" cssElementClass="emp_field_mobile_write"/>
			<emp:textarea id="LmtCoopCar.remark" label="备注" maxlength="1000" required="false" colSpan="2" />
		</emp:gridLayout>
	<% if(!"view".equals(op)){ %>
		<div align="center">
			<br>
			<emp:button id="submitLmtCoopCar" label="修改" op="update"/>
			<emp:button id="reset" label="重置" op="update"/>
		</div>
	<%} %>
	</emp:form>
</body>
</html>
</emp:page>
