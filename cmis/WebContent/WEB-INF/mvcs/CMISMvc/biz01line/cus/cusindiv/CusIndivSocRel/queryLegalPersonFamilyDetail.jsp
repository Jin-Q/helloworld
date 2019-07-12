<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>
<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/cus/cusbase/CusBase/cusInfo.jsp" flush="true" />
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String com_id="";
	if(context.containsKey("com_id")){
		com_id = context.getDataValue("com_id").toString();
	}
	request.setAttribute("canwrite","");
%>
<script type="text/javascript">
	
	/*--user code begin--*/
	function doOnLoad() {
		setCusInfo();
	};

	function setCusInfo(){
		var certTyp = CusIndivSocRel.indiv_rel_cert_typ._obj.element.value;
		var certCode = CusIndivSocRel.indiv_rl_cert_code._obj.element.value;
        if(certTyp==null||certTyp==''||certCode==null||certCode=='') {
            return ;
        }
		if(certTyp=='0' || certTyp=='7'){//身份证或临时身份证时验证
			var flg = CheckIdValue(certCode);
			
			if(flg){
				if(certCode.length=='15'){
				
					if(confirm("15位身份证不能开户，点击 [确定] 系统自动转换成18位新身份证！")){
						CusIndivSocRel.indiv_rl_cert_code._obj.element.value=oldCardToNewCard(certCode);
					}else{
						CusIndivSocRel.indiv_rl_cert_code._obj.element.value="";
					}
				}
			}else{
				
				CusIndivSocRel.indiv_rl_cert_code._obj.element.value="";
				CusIndivSocRel.indiv_rl_cert_code._obj.element.focus();
				return;
			}
			
		}
		
		//调用公用js函数getCusInfo， 参数(本页面回调赋值js方法名,证件类型,证件号码,本页面此js方法名) 
		getCusInfo('returnCus',certTyp,certCode,'setCusInfo');
	
	}

	//通用异步返回时调用本地js函数，传递参数cusObj为json串格式: cusObj.column
	function returnCus(cusObj){
	
		if(cusObj==null||cusObj=='undefined'){
			CusIndivSocRel.cus_id_rel._setValue('');
			return;
		}

		//家庭成员客户码
		var cus_id_rel = cusObj.cus_id;
		if (cus_id_rel != null) {
			CusIndivSocRel.cus_id_rel._setValue(cus_id_rel);
		}
		//证件签发日期
		var indiv_id_start_dt = cusObj.indiv_id_start_dt;
		if (indiv_id_start_dt != null){
			CusIndivSocRel.crt_date._setValue(indiv_id_start_dt);
		}
		//签发到期日期
		var indiv_id_exp_dt=cusObj.indiv_id_exp_dt;
		if (indiv_id_exp_dt != null){
			CusIndivSocRel.crt_end_date._setValue(indiv_id_exp_dt);
		}
		//证件类型1
		var cert_typ_other=cusObj.cert_typ_other;
		if (cert_typ_other != null){
			CusIndivSocRel.com_mrg_cert_typ_other._setValue(cert_typ_other);
		}
		//证件号码1
		var cert_code_other=cusObj.cert_code_other;
		if (cert_code_other != null){
			CusIndivSocRel.com_mrg_cert_code_other._setValue(cert_code_other);
		}
		//签发日期1
		var crt_date_other=cusObj.crt_date_other;
		if (crt_date_other != null){ 
			CusIndivSocRel.crt_date_other._setValue(crt_date_other);
		}
		//签发到期日期1
		var crt_end_date_other=cusObj.crt_end_date_other;
		if (crt_end_date_other != null){
			CusIndivSocRel.crt_end_date_other._setValue(crt_end_date_other);
		}
	}
	
	
	function doReturnCusIndivSocRel(){
		goback();
	}
		
	function goback(){
		var com_id = "<%=com_id%>";
		var paramStr = "";
		var stockURL = "";
		var editFlag = '${context.EditFlag}';
		if(com_id!=null &&com_id!=""){
			paramStr="cus_id="+com_id+"&EditFlag="+editFlag;
			stockURL = '<emp:url action="queryLegalPersonFamilyList.do"/>&'+paramStr;
		}else{
			paramStr="CusIndivSocRel.cus_id="+CusIndivSocRel.cus_id._obj.element.value+"&EditFlag="+editFlag;
			stockURL = '<emp:url action="queryCusIndivSocRelList.do"/>&'+paramStr;
		}
	//	var stockURL = '<emp:url action="queryLegalPersonFamilyList.do"/>&'+paramStr;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	}			
	/*--user code end--*/
</script>
</head>
<body class="page_content"  onload="doOnLoad()">
	<emp:gridLayout id="CusIndivSocRelGroup" title="法人家庭成员" maxColumn="2">
		<emp:select id="CusIndivSocRel.indiv_rel_cert_typ" label="证件类型" required="true" dictname="STD_ZB_CERT_TYP" />
		<emp:text id="CusIndivSocRel.indiv_rl_cert_code" label="证件号码" maxlength="20" required="true" />
		<emp:date id="CusIndivSocRel.crt_date" label="签发日期" required="false" readonly="true" />
		<emp:date id="CusIndivSocRel.crt_end_date" label="签发到期日期" required="false" readonly="true" />
		<emp:select id="CusIndivSocRel.com_mrg_cert_typ_other" label="证件类型1:" required="false" dictname="STD_ZB_CERT_TYP" readonly="true" />
		<emp:text id="CusIndivSocRel.com_mrg_cert_code_other" label="证件号码1:"  maxlength="20" required="false" readonly="true"/>
		<emp:date id="CusIndivSocRel.crt_date_other" label="签发日期1:" required="false" readonly="true" />
		<emp:date id="CusIndivSocRel.crt_end_date_other" label="签发到期日期1:" required="false" readonly="true" />
		<br/>
		<emp:select id="CusIndivSocRel.indiv_family_flg" label="是否家庭成员" required="true" dictname="STD_ZX_YES_NO" />
		<emp:text id="CusIndivSocRel.cus_id" label="法人客户码" required="true"  colSpan="2" hidden="true" />
		 <emp:text id="CusIndivSocRel.cus_id_rel" label="法人家庭成员客户码" required="true"  readonly="true"/>
			<emp:text id="CusIndivSocRel.indiv_rel_cus_name" label="姓名" required="true" maxlength="30" readonly="true" />
			<emp:select id="CusIndivSocRel.indiv_sex" label="性别" required="false" dictname="STD_ZX_SEX"  readonly="true" />
			<emp:select id="CusIndivSocRel.indiv_cus_rel" label="与客户关系" required="true" dictname="STD_ZB_INDIV_CUS"  />
			<emp:text id="CusIndivSocRel.indiv_rl_y_incm" label="年收入（元）" maxlength="18" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="CusIndivSocRel.indiv_rel_job" label="职业" required="false" dictname="STD_ZX_EMPLOYMET" readonly="true" />
			<emp:text id="CusIndivSocRel.indiv_rel_com_name" label="单位名称" maxlength="60" required="false" readonly="true" hidden="true"/>
			<emp:select id="CusIndivSocRel.indiv_rel_duty" label="职务" required="false" dictname="STD_ZX_DUTY" readonly="true" />
			<emp:textarea id="CusIndivSocRel.remark" label="备注" maxlength="250" required="false" colSpan="2" onkeyup="this.value = this.value.substring(0, 250)" />
			<emp:text id="CusIndivSocRel.input_id" label="登记人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId" />
			<emp:text id="CusIndivSocRel.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="$organNo" />
			<emp:date id="CusIndivSocRel.input_date" label="登记日期" required="false" hidden="true" defvalue="$OPENDAY" />
			<emp:text id="CusIndivSocRel.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true" />
			<emp:date id="CusIndivSocRel.last_upd_date" label="更新日期" required="false" hidden="true" />

	</emp:gridLayout>
	<div align="center">
		<br>
		<emp:button id="returnCusIndivSocRel" label="返回"/>
	</div>
</body>
</html>
</emp:page>