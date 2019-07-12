<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
	}
	if("view".equals(op)){
		request.setAttribute("canwrite","");
	}
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	
	function doNext(){
		if(!ArpAssetRentInfo._checkAll()){
			return;
		}
		var form = document.getElementById("submitForm");
		ArpAssetRentInfo._toForm(form);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if(flag == "success"){
					alert("修改成功!");
					window.location.reload();
				}else {
					alert("新增失败!"); 
				}
			}
		};
		var handleFailure = function(o){
			alert("异步请求出错！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	};	
	//两个日期作比较
	function checkInsurStartDate(){
		if(ArpAssetRentInfo.lease_start_date._obj.element.value!=''){
			var e = ArpAssetRentInfo.lease_end_date._obj.element.value;
			var s = ArpAssetRentInfo.lease_start_date._obj.element.value;
			if(e!=''){
				if(s>e){
            		alert('租约起始日期必须小于或等于租约到期日期！');
            		ArpAssetRentInfo.lease_start_date._obj.element.value="";
            		return;
            	}
			}
		}
	}
	function checkInsurEndDate(){
		if(ArpAssetRentInfo.lease_end_date._obj.element.value!=''){
			var e = ArpAssetRentInfo.lease_end_date._obj.element.value;
			var s = ArpAssetRentInfo.lease_start_date._obj.element.value;
			if(s!=''){
				if(s>e){
            		alert('租约到期日期必须大于或等于租约起始日期！');
            		ArpAssetRentInfo.lease_end_date._obj.element.value="";
            		return;
            	}
			}
		}
	}

	//校验证件号码的合法性
	function checkCertCode(obj){
		var cert_type = ArpAssetRentInfo.lessee_cert_type._getValue();
		if("0"==cert_type){ //身份证 
			var value = ArpAssetRentInfo.lessee_cert_no._getValue();
			var length = value.length;
			if (length != 15 && length != 18){
			    alert("身份证号码，长度必须为15位或18位！");
			    obj.value = '';
			    return false;
			}
		  	if (length == 15){//15位的身份证号
		    	var reg = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/;
		    	var checkres = reg.test(value);
				if (!checkres) {
					alert("身份证号码格式不正确！");
					obj.value = '';
					return false;
				}
		    }else if (length == 18){
		        var reg = /^[1-9]\d{6}[1-9]\d{2}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9|x|X])$/;
		    	var checkres = reg.test(value);
				if (!checkres) {
					alert("身份证号码格式不正确！");
					obj.value = '';
					return false;
				}
		    }
		}
	}
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateArpAssetRentInfoRecord.do" method="POST">
		<emp:gridLayout id="ArpAssetRentInfoGroup" maxColumn="2" title="资产出租信息">
			<emp:text id="ArpAssetRentInfo.serno" label="业务编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="ArpAssetRentInfo.asset_disp_no" label="资产处置编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="ArpAssetRentInfo.guaranty_no" label="抵债资产编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="ArpAssetRentInfo.guaranty_name" label="抵债资产名称" maxlength="40" required="true" readonly="true" />
			<emp:text id="ArpAssetRentInfo.guaranty_type_displayname" label="抵债资产类型" required="true" readonly="true"  cssElementClass="emp_field_text_cusname" colSpan="2" />
			<emp:text id="ArpAssetRentInfo.debt_in_amt" label="抵入金额" maxlength="16" required="false" dataType="Currency" defvalue="${context.debt_in_amt}" readonly="true" colSpan="2"/>
			<emp:text id="ArpAssetRentInfo.renta_paid_mode" label="租金收缴方式" maxlength="5" required="true" colSpan="2"/>
			<emp:text id="ArpAssetRentInfo.rent_amt" label="租金" maxlength="16" required="true" dataType="Currency" />
			<emp:text id="ArpAssetRentInfo.mort_amt" label="押金" maxlength="16" required="true" dataType="Currency" />
			<emp:text id="ArpAssetRentInfo.lessee" label="承租人" maxlength="40" required="true" />
			<emp:text id="ArpAssetRentInfo.lessee_addr" label="承租人地址" maxlength="40" required="true" cssElementClass="emp_field_text_input2" colSpan="2"/>
			<emp:text id="ArpAssetRentInfo.lessee_phone" label="承租人电话" maxlength="40" required="true" dataType="Phone" colSpan="2"/>
			<emp:select id="ArpAssetRentInfo.lessee_cert_type" label="承租人证件类型" required="true" dictname="STD_ZB_CERT_TYP" defvalue="0"/>
			<emp:text id="ArpAssetRentInfo.lessee_cert_no" label="承租人证件号码" maxlength="40" required="true" onblur="checkCertCode(this)"/>
			<emp:date id="ArpAssetRentInfo.lease_start_date" label="租约起始日期" required="true" onblur="checkInsurStartDate()"/>
			<emp:date id="ArpAssetRentInfo.lease_end_date" label="租约到期日期" required="true" onblur="checkInsurEndDate()"/>
			<emp:textarea id="ArpAssetRentInfo.memo" label="备注" maxlength="200" required="false" colSpan="2" />
			<emp:select id="ArpAssetRentInfo.status" label="状态" required="false" dictname="STD_ZX_ASSET_STATUS" defvalue="00" readonly="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<%if(!"view".equals(op)){ %>
			<emp:button id="next" label="保存" op="update"/>
			<%} %>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
