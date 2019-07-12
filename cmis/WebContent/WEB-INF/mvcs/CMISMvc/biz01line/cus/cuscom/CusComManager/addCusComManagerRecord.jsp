<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<meta http-equiv="Content-Type" content="text/HTML; charset=utf-8" /> 
<emp:page>
<html>
<head>
<title>新增页面</title>
<%
	String editFlag = request.getParameter("EditFlag");
%>
<jsp:include page="/include.jsp" flush="true" />
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/cus/cusbase/CusBase/cusInfo.jsp" flush="true" />
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 450px;
}
</style>
	<script type="text/javascript">
	function doAddCusComManager() {
		var form = document.getElementById("submitForm");
		var result = CusComManager._checkAll();
		if (result) {
			CusComManager._toForm(form);
			toSubmitForm(form);
		}
	};
	
	function toSubmitForm(form) {
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e);
					return;
				}
				var flag = jsonstr.flag;
				if (flag == "新增成功") {
					alert("新增成功！");
					doReturn();
				} else {
					alert(flag);
					return;
				}
			}
		};
		var handleFailure = function(o) {};
		var callback = {
			success : handleSuccess,
			failure : handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', form.action,callback, postData);
	}
	
	/*--user code begin--*/
	function doReturn() {
		var paramStr = "CusComManager.cus_id="+ CusComManager.cus_id._obj.element.value+"&EditFlag=<%=editFlag%>";
		var stockURL = '<emp:url action="queryCusComManagerList.do"/>&' + paramStr;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	}

	//通用异步返回时调用本地js函数，传递参数cusObj为json串格式: cusObj.column ，当证件类型为对私时cusObj存放CusIndiv模型，反之为CusCom。
	function returnCusInd(cusObj){
		//性别
		var indiv_sex = cusObj.indiv_sex;
		if (indiv_sex != null) {
			CusComManager.com_mrg_sex._setValue(indiv_sex);
		}
		//出生日期
		var birth_dt = cusObj.indiv_dt_of_birth;
		if (birth_dt != null) {
			CusComManager.com_mrg_bday._setValue(birth_dt);
		}
		//职业
		var indiv_occ = cusObj.indiv_occ;
		if (indiv_occ != null) {
			CusComManager.com_mrg_occ._setValue(indiv_occ);
		}
		//职务
		var indiv_com_job_ttl = cusObj.indiv_com_job_ttl;
		if (indiv_com_job_ttl != null) {
			CusComManager.com_mrg_duty._setValue(indiv_com_job_ttl);
		}
		//职称
		var indiv_crtfctn = cusObj.indiv_crtfctn;
		if (indiv_crtfctn != null) {
			CusComManager.com_mrg_crtf._setValue(indiv_crtfctn);
		}
		//最高学历
		var indiv_edt = cusObj.indiv_edt;
		if (indiv_edt != null) {
			CusComManager.com_mrg_edt._setValue(indiv_edt);
		}
		//最高学位
		var indiv_dgr = cusObj.indiv_dgr;
		if (indiv_dgr != null) {
			CusComManager.com_mrg_dgr._setValue(indiv_dgr);
		}
		//移动电话
		var mobile = cusObj.mobile;
		if (mobile != null) {
			CusComManager.com_relate_phone._obj.element.value = mobile;
		}

		
	}

	//校验任职日期
	function checkSignDate(){
	//	var openDay = '${context.OPENDAY}';
		var initDate = CusComManager.sign_init_date._getValue();
		var endDate = CusComManager.sign_end_date._getValue();
	/*	if(initDate!=null&&initDate!=''){
			if(initDate>openDay){
				alert('任职开始日期不能晚于当前日期！');
				CusComManager.sign_init_date._setValue('');
			}
		}
		if(endDate!=null&&endDate!=''){
			if(openDay>endDate){
				alert('任职到期日不能小于当前日期！');
				CusComManager.sign_end_date._setValue('');
			}
		}*/
		if(initDate!=null&&initDate!=''&&endDate!=null&&endDate!=''){
			if(initDate>endDate){
				alert('任职开始日期不能大于任职到期日期！');
				CusComManager.sign_init_date._setValue('');
				CusComManager.sign_end_date._setValue('');
			}
		}
	}

	function addBtn(){
		CusComManager.com_mrg_cert_code._obj.addOneButton('uniquCheck','获 取', setCusInfo);
		//移除对公证件类型
		var options = CusComManager.com_mrg_cert_typ._obj.element.options;
	    for ( var i = options.length - 1; i >= 0; i--) {
	   	    if(options[i].value=='a'||options[i].value=='b'||options[i].value=='c'||options[i].value=='X'){
	  			options.remove(i);
	   	    }
	    }
	}

	function setCusInfo(){
		var certTyp = CusComManager.com_mrg_cert_typ._obj.element.value;
		var certCode = CusComManager.com_mrg_cert_code._obj.element.value;
        if(certTyp==null||certTyp==''||certCode==null||certCode=='') {
            alert("证件类型或证件号码不能为空！");
            return ;
        }
		if(certTyp=='0' || certTyp=='7'){//身份证或临时身份证时验证
			var flg = CheckIdValue(certCode);
			if(flg){
				if(certCode.length=='15'){
					if(confirm("15位身份证不能开户，点击 [确定] 系统自动转换成18位新身份证！")){
						CusComManager.com_mrg_cert_code._obj.element.value=oldCardToNewCard(certCode);
					}else{
						CusComManager.com_mrg_cert_code._obj.element.value="";
					}
				}
			}else{
				CusComManager.com_mrg_cert_code._obj.element.value="";
				CusComManager.com_mrg_cert_code._obj.element.focus();
				return;
			}
		}
		//调用公用js函数getCusInfo， 参数(本页面回调赋值js方法名,证件类型,证件号码,本页面此js方法名) 
		getCusInfo('returnCus',certTyp,certCode,'setCusInfo');
	}

	//通用异步返回时调用本地js函数，传递参数cusObj为json串格式: cusObj.column
	function returnCus(cusObj){
		
		if(cusObj==null||cusObj=='undefined'){
			CusComManager.cus_id_rel._setValue('');
			return;
		}
		//高管姓名
		var cus_name=cusObj.cus_name;
		if (cus_name != null) {
			CusComManager.com_mrg_name._setValue(cus_name);
		}
		//高管客户码
		var cus_id_rel = cusObj.cus_id;
		if (cus_id_rel != null) {
			CusComManager.cus_id_rel._setValue(cus_id_rel);
		}
		//国籍
		var cus_country = cusObj.cus_country;
		if (cus_country != null) {
			CusComManager.cus_country._setValue(cus_country);
		}
		
		//证件类型，证件号码
		var com_mrg_cert_typ = cusObj.cert_type;
		CusComManager.com_mrg_cert_typ._setValue(com_mrg_cert_typ);
		
		var com_mrg_cert_code =cusObj.cert_code;
		CusComManager.com_mrg_cert_code._setValue(com_mrg_cert_code);

		//证件签发日期
		var indiv_id_start_dt = cusObj.indiv_id_start_dt;
		if (indiv_id_start_dt !=null){
		    CusComManager.crt_date._setValue(indiv_id_start_dt);
		}
		//签发到期日期
		var indiv_id_exp_dt=cusObj.indiv_id_exp_dt;
		if (indiv_id_exp_dt != null){
			CusComManager.crt_end_date._setValue(indiv_id_exp_dt);
		}
		//证件类型1
		var cert_typ_other=cusObj.cert_typ_other;
		if (cert_typ_other != null){
			CusComManager.com_mrg_cert_typ_other._setValue(cert_typ_other);
		}
		//证件号码1
		var cert_code_other=cusObj.cert_code_other;
		if (cert_code_other != null){
			CusComManager.com_mrg_cert_code_other._setValue(cert_code_other);
		}
		//签发日期1
		var crt_date_other=cusObj.crt_date_other;
		if (crt_date_other != null){ 
		CusComManager.crt_date_other._setValue(crt_date_other);
		}
		//签发到期日期1
		var crt_end_date_other=cusObj.crt_end_date_other;
		if (crt_end_date_other != null){
		CusComManager.crt_end_date_other._setValue(crt_end_date_other);
		}

		getCusDetails();
	}

	function getCusDetails() {
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
					var cusList=jsonstr.cusList;
					var cusObj;
					if (cusList != null && cusList.length > 0) {
						cusObj=cusList[0];
						returnCusInd(cusObj);
					} else {
						alert("记录为空！");
					}
				} catch (e) {
					alert("Parse jsonstr define error!" + e);
					return;
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success : handleSuccess,
			failure : handleFailure
		};
		var cus_id_rel = CusComManager.cus_id_rel._getValue();
		var url = '<emp:url action="getCusIndiv4manager.do"/>?cus_id_rel='+cus_id_rel;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
	}

	function oldCardToNewCard(certCode){
		var vs = "10X98765432";
		var v = new Array();
		v.push(2, 4, 8, 5, 10, 9, 7, 3, 6, 1, 2, 4, 8, 5, 10, 9, 7);
		var cardID17 = certCode.substring(0,6)+"19"+certCode.substring(6);
		var N = 0;
		var R = -1;
		var T = '0';//储存最后一个数字
		var j = 0;
		var cardID18="";
		//计数出第18位数字
		for (var i = 16; i >= 0; i--){
	        N += parseInt(cardID17.substring(i, i + 1)) * v[j];
	        j++;
		}
		R = N % 11;
		T = vs.charAt(R);
		cardID18 = cardID17 + T;
		return cardID18;
	}
</script>
</head>
	<body class="page_content" onload="addBtn();">
	<emp:form id="submitForm" action="addCusComManagerRecord.do" method="POST">
		<emp:gridLayout id="CusComManagerGroup" title="高管信息" maxColumn="2">
			<emp:select id="CusComManager.com_mrg_cert_typ" label="证件类型" required="true" dictname="STD_ZB_CERT_TYP" onchange="CusComManager.cus_id_rel._setValue('');" colSpan="2"/>
			<emp:text id="CusComManager.com_mrg_cert_code" label="证件号码" colSpan="2" maxlength="20" required="true" onchange="CusComManager.cus_id_rel._setValue('');"/>
			<emp:date id="CusComManager.crt_date" label="签发日期" required="false" readonly="true" />
			<emp:date id="CusComManager.crt_end_date" label="签发到期日期" required="false" readonly="true" />
			<emp:select id="CusComManager.com_mrg_cert_typ_other" label="证件类型1:" required="false" dictname="STD_ZB_CERT_TYP" readonly="true" />
			<emp:text id="CusComManager.com_mrg_cert_code_other" label="证件号码1:"  maxlength="20" required="false" readonly="true"/>
			<emp:date id="CusComManager.crt_date_other" label="签发日期1:" required="false" readonly="true" />
			<emp:date id="CusComManager.crt_end_date_other" label="签发到期日期1:" required="false" readonly="true" />
			<br/>
			<emp:text id="CusComManager.cus_id_rel" label="高管人客户码" required="true" maxlength="30" readonly="true"/>
			<emp:text id="CusComManager.com_mrg_name" label="高管人姓名" maxlength="35" required="true" readonly="true"/>
			<emp:select id="CusComManager.com_mrg_sex" label="性别" required="true" dictname="STD_ZX_SEX" readonly="true"/>
			<emp:select id="CusComManager.com_mrg_typ" label="高管类别" required="true" dictname="STD_ZB_MANAGER_TYPE"/>
			<emp:select id="CusComManager.power_org" label="权力机构" dictname="STD_POWER_ORG_TYPE" required="true"/>
			<emp:select id="CusComManager.cus_country" label="国籍" dictname="STD_GB_2659-2000" readonly="true"/>
			<emp:text id="CusComManager.com_relate_type" label="联系方式" maxlength="35" required="false"/>
			<emp:text id="CusComManager.com_relate_phone" label="联系手机" maxlength="35" dataType="Mobile" required="true"  readonly="false"/>	
			<emp:date id="CusComManager.com_mrg_bday" label="出生日期" readonly="true"/>
			<emp:select id="CusComManager.com_mrg_occ" label="职业" required="false"  readonly="true" dictname="STD_ZX_EMPLOYMET" />
			<emp:select id="CusComManager.com_mrg_duty" label="职务" required="false" dictname="STD_ZX_DUTY" readonly="true"/>
			<emp:select id="CusComManager.com_mrg_crtf" label="职称" required="false" dictname="STD_ZX_TITLE" readonly="true"/>
			<emp:select id="CusComManager.com_mrg_dgr" label="最高学位" required="false" dictname="STD_ZX_DEGREE" readonly="true"/>
			<emp:select id="CusComManager.com_mrg_edt" label="最高学历" required="false" dictname="STD_ZX_EDU" readonly="true"/>
			<emp:date id="CusComManager.sign_init_date" label="任职开始日期" required="false" onblur="checkSignDate()" />
			<emp:date id="CusComManager.sign_end_date" label="任职到期日期" required="false" onblur="checkSignDate()" />
			<emp:text id="CusComManager.cus_id" label="客户码"  required="true" hidden="true"/>
		</emp:gridLayout>
	<div align="center"><br>
	<emp:button id="addCusComManager" label="保存" /> 
	<emp:button id="return" label="返回" /></div>
	</emp:form>
	</body>
	</html>
</emp:page>