<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/cus/cusbase/CusBase/cusInfo.jsp" flush="true" />
<%
	request.setAttribute("canwrite","");
	String cus_state = (String)request.getParameter("cus_state");
%>
<style type="text/css">

.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	text-align:left;
	width:450px;
}
</style>
<script type="text/javascript">

	function doOnLoad() {
		setCusInfo();
	};

	function setCusInfo(){
		var certTyp = CusComManager.com_mrg_cert_typ._obj.element.value;
		var certCode = CusComManager.com_mrg_cert_code._obj.element.value;
		
        if(certTyp==null||certTyp==''||certCode==null||certCode=='') {
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
		//高管客户码
		var cus_id_rel = cusObj.cus_id;
		if (cus_id_rel != null) {
			CusComManager.cus_id_rel._setValue(cus_id_rel);
		}
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
	}
	
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+CusComManager.cus_id_rel._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindowDivtemp','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}

	function doReturn() {
		var editFlag = '${context.EditFlag}';
		var paramStr = "CusComManager.cus_id="+ CusComManager.cus_id._obj.element.value+"&EditFlag="+editFlag;
		var stockURL = '<emp:url action="queryCusComManagerList.do"/>&' + paramStr;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	};

	function doViewCusIndivResume() {
		var paramStr = CusComManager.CusIndivResume._obj.getParamStr(['cus_id','seq']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryCusComManagerCusIndivResumeDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		} else {
			alert('请先选择一条记录！');
		}
	};
	/*--user code begin--*/

	/*--user code end--*/
</script>
</head>
<body class="page_content" onload="doOnLoad()">
		<emp:gridLayout id="CusComManagerGroup" title="高管信息" maxColumn="2">
			<emp:text id="CusComManager.cus_id_rel" label="高管人客户码" required="true" />
			<emp:text id="CusComManager.com_mrg_name" label="高管人姓名" maxlength="35" required="true" readonly="true"/>
			<emp:select id="CusComManager.com_mrg_cert_typ" label="证件类型" required="true" dictname="STD_ZB_CERT_TYP" readonly="true"/>
			<emp:text id="CusComManager.com_mrg_cert_code" label="证件号码" readonly="true" maxlength="20" required="true" />
			<emp:date id="CusComManager.crt_date" label="签发日期" required="false" readonly="true" />
			<emp:date id="CusComManager.crt_end_date" label="签发到期日期" required="false" readonly="true" />
			<emp:select id="CusComManager.com_mrg_cert_typ_other" label="证件类型1:" required="false" dictname="STD_ZB_CERT_TYP" readonly="true" />
			<emp:text id="CusComManager.com_mrg_cert_code_other" label="证件号码1:"  maxlength="20" required="false" readonly="true"/>
			<emp:date id="CusComManager.crt_date_other" label="签发日期1:" required="false" readonly="true" />
			<emp:date id="CusComManager.crt_end_date_other" label="签发到期日期1:" required="false" readonly="true" />
			<br/>
			<emp:select id="CusComManager.com_mrg_sex" label="性别" required="true" dictname="STD_ZX_SEX" disabled="true"/>
			<emp:select id="CusComManager.com_mrg_typ" label="高管类别" required="true" dictname="STD_ZB_MANAGER_TYPE"/>
			<emp:select id="CusComManager.power_org" label="权力机构" dictname="STD_POWER_ORG_TYPE" required="true"/>
			<emp:select id="CusComManager.cus_country" label="国别" dictname="STD_GB_2659-2000" required="true"/>
			<emp:text id="CusComManager.com_relate_type" label="联系方式" maxlength="35" required="false"/>
			<emp:text id="CusComManager.com_relate_phone" label="联系手机" maxlength="35" required="true"  />	
			<emp:date id="CusComManager.com_mrg_bday" label="出生日期" required="true" disabled="true"/>
			<emp:select id="CusComManager.com_mrg_occ" label="职业" required="false" dictname="STD_ZX_EMPLOYMET"/>
			<emp:select id="CusComManager.com_mrg_duty" label="职务" required="false" dictname="STD_ZX_DUTY" disabled="true"/>
			<emp:select id="CusComManager.com_mrg_crtf" label="职称" required="false" dictname="STD_ZX_TITLE" disabled="true"/>
			<emp:select id="CusComManager.com_mrg_dgr" label="最高学位" required="false" dictname="STD_ZX_DEGREE" disabled="true"/>
			<emp:select id="CusComManager.com_mrg_edt" label="最高学历" required="false" dictname="STD_ZX_EDU" disabled="true"/>
			<emp:date id="CusComManager.sign_init_date" label="任职开始日期" required="false" />
			<emp:date id="CusComManager.sign_end_date" label="任职到期日期" required="false" />
			
			<emp:text id="CusComManager.cus_id" label="客户码"  required="true" hidden="true"/>
		</emp:gridLayout>
	<div align="center">
		<emp:button id="return" label="返回"/>
	</div>
	<br>
</body>
</html>
</emp:page>
