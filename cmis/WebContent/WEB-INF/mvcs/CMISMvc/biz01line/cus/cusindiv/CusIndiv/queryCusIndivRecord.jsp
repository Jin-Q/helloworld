<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@ page import="com.ecc.emp.core.Context"%>
<%@ page import="java.util.*"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<html>
<head>
<title>修改页面</title>
<%
	String flag=request.getParameter("flag");
	if ((flag != null && flag.equals("query"))) {
		request.setAttribute("canwrite", "");
	}
%>
<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/cus/cusbase/CusBase/cusInfo.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input2 {
border:1px solid #CEC7BD;
background-color:#eee;
text-align:left;
width:450px;
}

.emp_field_select_select1 {
	width: 450px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
	background-color: #e3e3e3;
}

.emp_field_label {
	vertical-align: top;
	padding-top: 4px;
	text-align: right;
	width:14s0px;
}
</style>
<link href="<emp:file fileName='styles/start/jquery-ui-1.7.1.custom.css'/>" rel="stylesheet" type="text/css" media="screen" />
<script type="text/javascript" src="<emp:file fileName='scripts/jquery-1.3.2.js'/>"></script>
<script type="text/javascript" src="<emp:file fileName='scripts/jquery-ui-1.7.1.custom.min.js'/>"></script>
<script type="text/javascript" src="<emp:file fileName='scripts/jquery.cmisDialogs.js'/>"></script>
<script type="text/javascript">
	/*--user code begin--*/
	//返回列表页面
	function doReturn(){
		var gobackURL = '<emp:url action="queryCusIndivList.do"/>';
		gobackURL = EMPTools.encodeURI(gobackURL);
		window.location = gobackURL;
	};
	
	//页面初始化
	function doOnLoad(){
		showCertTyp(CusBase.cert_type, 'indiv');
		//校验是否为我行关联客户  
		checkIsRelaCust();
		//检查是否有贷款卡
		changeCardFlg();
		checkIsLong();
	}

	//add by lilx 2013-7-3 12:04	
	function checkIsRelaCust(){
		if (CusIndiv.is_rela_cust._obj.element.value == "1") {//是
			//与银行的关联关系
			CusIndiv.cus_bank_rel._obj._renderRequired(true);
			//CusIndiv.cus_bank_rel._obj._renderReadonly(false);
			CusIndiv.cus_bank_rel._obj._renderHidden(false);
			//在我行职务
			CusIndiv.bank_duty._obj._renderRequired(true);
			CusIndiv.bank_duty._obj._renderReadonly(false);
			CusIndiv.bank_duty._obj._renderHidden(false);
			//股权证号码
			CusIndiv.stockhold_code._obj._renderRequired(false);
			CusIndiv.stockhold_code._obj._renderReadonly(false);
			CusIndiv.stockhold_code._obj._renderHidden(false);
			//拥有我行股份金额(元)
			CusIndiv.com_hold_stk_amt._obj._renderRequired(false);
			CusIndiv.com_hold_stk_amt._obj._renderReadonly(false);
			CusIndiv.com_hold_stk_amt._obj._renderHidden(false);
			CusIndiv.cus_bank_rel._obj._renderRequired(true);
			var options = CusIndiv.cus_bank_rel._obj.element.options;
			for ( var i = options.length - 1; i >= 0; i--) {
				if(options[i].value == 'B1'){
					options.remove(i);
				}
			}
		} else if (CusIndiv.is_rela_cust._obj.element.value == "2") {//否
			//银行的关联关系
			CusIndiv.cus_bank_rel._obj.element.value = "";
			CusIndiv.cus_bank_rel._obj._renderRequired(false);
			CusIndiv.cus_bank_rel._obj._renderHidden(true);
			//在我行职务
			CusIndiv.bank_duty._obj.element.value = "";
			CusIndiv.bank_duty._obj._renderRequired(false);
			CusIndiv.bank_duty._obj._renderHidden(true);
			//股权证号码
			CusIndiv.stockhold_code._setValue("");
			CusIndiv.stockhold_code._obj._renderRequired(false);
			CusIndiv.stockhold_code._obj._renderHidden(true);
			//拥有我行股份金额(元)
			CusIndiv.com_hold_stk_amt._setValue("");
			CusIndiv.com_hold_stk_amt._obj._renderRequired(false);
			CusIndiv.com_hold_stk_amt._obj._renderHidden(true);
			CusIndiv.cus_bank_rel._obj._renderRequired(false);
			CusIndiv.cus_bank_rel._obj._renderHidden(false);
			var options = CusIndiv.cus_bank_rel._obj.element.options;
	    	var option1 = new Option('普通客户关系','B1');
	    	options.add(option1);
	    	CusIndiv.cus_bank_rel._setValue("B1");
		}
	}

	//选择是否贷款卡时触发的事件
	function changeCardFlg() {
		if (CusBase.loan_card_flg._obj.element.value == "1") {
			CusBase.loan_card_id._obj._renderRequired(true);
			CusBase.loan_card_id._obj._renderReadonly(true);
			CusBase.loan_card_id._obj._renderHidden(false);
			/* modify by wangj 2015-05-20  中征码修改 begin*/
			/*
			CusBase.loan_card_pwd._obj._renderRequired(false);
			CusBase.loan_card_pwd._obj._renderReadonly(true);
			CusBase.loan_card_pwd._obj._renderHidden(false);
			
			CusBase.loan_card_eff_flg._obj._renderRequired(false);
			CusBase.loan_card_eff_flg._obj._renderReadonly(true);
			CusBase.loan_card_eff_flg._obj._renderHidden(false);
			
			//CusBase.loan_card_audit_dt._obj._renderRequired(true);
			CusBase.loan_card_audit_dt._obj._renderReadonly(true);
			CusBase.loan_card_audit_dt._obj._renderHidden(false);
			*//* modify by wangj 2015-05-20  中征码修改 end*/
		} else if (CusBase.loan_card_flg._obj.element.value == "2") {
			CusBase.loan_card_id._obj.element.value = "";
			CusBase.loan_card_id._obj._renderRequired(false);
			CusBase.loan_card_id._obj._renderHidden(true);
			/* modify by wangj 2015-05-20  中征码修改 begin*/
			/*
			CusBase.loan_card_pwd._obj.element.value = "";
			CusBase.loan_card_pwd._obj._renderRequired(false);
			CusBase.loan_card_pwd._obj._renderHidden(true);
			
			CusBase.loan_card_eff_flg._setValue("");
			CusBase.loan_card_eff_flg._obj._renderRequired(false);
			CusBase.loan_card_eff_flg._obj._renderHidden(true);
			
			CusBase.loan_card_audit_dt._setValue("");
			CusBase.loan_card_audit_dt._obj._renderRequired(false);
			CusBase.loan_card_audit_dt._obj._renderHidden(true);
			*//* modify by wangj 2015-05-20  中征码修改 end*/
		}
	};

	function checkIsLong(){
		var is_long_indiv = CusIndiv.is_long_indiv._getValue();
		if(is_long_indiv == '2'){
			CusIndiv.indiv_id_exp_dt._obj._renderHidden(false);
			CusIndiv.indiv_id_exp_dt._obj._renderRequired(true);
		}else{
			CusIndiv.indiv_id_exp_dt._obj._renderHidden(true);
			CusIndiv.indiv_id_exp_dt._obj._renderRequired(false);
		}
	}
	/*--user code end--*/
	   	 
</script>
</head>
<form id="postForm" action="" method="post" target="_blank"  >
 <input type='hidden' name="EMP_SID" value='<%=request.getParameter("EMP_SID") %>' />
</form>
<body class="page_content" onload="doOnLoad();">
	<emp:form id="submitForm" action="updateCusIndivRecord.do" method="POST"  >
	<emp:tabGroup id="CusIndiv_tabs" mainTab="base_tab">
	<emp:tab id="base_tab" label="基本信息" initial="true" needFlush="true">
		<div>
			<emp:gridLayout id="CusIndivGroup" title="基本信息" maxColumn="2">
				<emp:text id="CusBase.cus_id" label="客户码" maxlength="30" required="true" readonly="true"/>
				<emp:select id="CusBase.cus_type" label="客户类型" required="true" dictname="STD_ZB_CUS_TYPE" readonly="true"/>
				<emp:text id="CusBase.cus_name" label="姓名" maxlength="80" required="true" readonly="true"/>
				<emp:select id="CusIndiv.indiv_sex" label="性别" required="true" dictname="STD_ZX_SEX" readonly="true"/>
				<emp:select id="CusBase.cert_type" label="证件类型" required="true" dictname="STD_ZB_CERT_TYP" defvalue="10"  readonly="true"/>
				<emp:text id="CusBase.cert_code" label="证件号码" maxlength="20" required="true"  readonly="true"/>
				<emp:select id="CusIndiv.is_long_indiv" label="是否为长期证件" dictname="STD_ZX_YES_NO" required="true" onchange="checkIsLong()" defvalue="1"/>
				<emp:date id="CusIndiv.indiv_id_exp_dt" label="证件到期日" required="false" readonly="true" hidden="true"/>								
				<emp:select id="CusIndiv.agri_flg" label="是否为农户" required="true" dictname="STD_ZX_YES_NO" colSpan="2"/>
				<emp:select id="CusBase.cus_country" label="国籍" required="true" dictname="STD_GB_2659-2000" defvalue="CHN" readonly="true"/>
				<emp:select id="CusIndiv.indiv_ntn" label="民族" required="true" dictname="STD_ZB_NATION" defvalue="01"/>
				<emp:text id="CusIndiv.indiv_brt_place" label="籍贯" required="true" hidden="true"/>
				<emp:pop id="CusIndiv.indiv_brt_place_displayname" label="籍贯" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" 
				returnMethod="onReturnBrtPlace" colSpan="2"  cssElementClass="emp_field_text_input2" required="true"/>
                <emp:text id="CusIndiv.indiv_houh_reg_add" label="户籍地址" required="true" hidden="true"/>
				<emp:pop id="CusIndiv.indiv_houh_reg_add_displayname" label="户籍地址" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" 
					returnMethod="onReturnHouhReg" colSpan="2"  cssElementClass="emp_field_text_input2" required="true"/>	
				<emp:text id="CusIndiv.street" label="街道"  required="false" cssElementClass="emp_field_text_input2" colSpan="2"/>
				<emp:date id="CusIndiv.indiv_dt_of_birth" label="出生日期" required="true" readonly="true"/>
				<emp:select id="CusIndiv.indiv_pol_st" label="政治面貌"  required="false" dictname="STD_ZB_POLITICAL"/>
				<emp:select id="CusIndiv.indiv_edt" label="最高学历" required="true" dictname="STD_ZX_EDU" />
				<emp:select id="CusIndiv.indiv_dgr" label="最高学位" required="true" dictname="STD_ZX_DEGREE"/>
				<emp:textarea id="CusIndiv.indiv_hobby" label="爱好" maxlength="200" required="false" colSpan="2"  rows="3"/>
			</emp:gridLayout>
			
			<emp:gridLayout id="" title="与我行合作关系" maxColumn="2">
				<emp:date id="CusIndiv.com_init_loan_date" label="建立信贷关系时间" required="true" />	
				<%-- <emp:checkbox id="CusIndiv.indiv_hld_acnt" label="在我行开立账户情况" colSpan="2" dictname="STD_ZB_INV_HL_ACN" disabled="true"/> --%>
				<emp:select id="CusIndiv.hold_card" label="持卡情况" required="true" dictname="STD_ZB_HOLD_CARD_YZ" />
				<emp:select id="CusIndiv.passport_flg" label="是否拥有外国护照或居住权" required="true" dictname="STD_ZX_YES_NO"/>			
				<emp:select id="CusBase.cus_crd_grade" label="信用等级" required="true"  dictname="STD_ZB_CREDIT_GRADE" defvalue="00" readonly="true" />
				<emp:date id="CusBase.cus_crd_dt" label="信用评级到期日期" required="false" readonly="true"/>
				<emp:select id="CusBase.cus_status" label="客户状态" required="false" colSpan="2" dictname="STD_ZB_CUS_STATUS"  hidden="false" readonly="true"/>
				<emp:textarea id="CusIndiv.remark" label="备注" maxlength="250" required="false" colSpan="2" hidden="true" />
			</emp:gridLayout>
			<emp:gridLayout id="" title="与我行关系" maxColumn="2">
				<emp:select id="CusIndiv.is_rela_cust" label="是否为我行关联客户" dictname="STD_ZX_YES_NO" required="true" onchange="checkIsRelaCust()"/>
				<emp:select id="CusIndiv.cus_bank_rel" label="与我行关联关系" dictname="STD_ZB_CUS_BANK" readonly="true" cssFakeInputClass="emp_field_select_select1" colSpan="2"/>
				<emp:select id="CusIndiv.bank_duty" label="在我行职务" dictname="STD_ZB_BANK_DUTY" />
				<emp:text id="CusIndiv.stockhold_code" label="股权证号码" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="CusIndiv.com_hold_stk_amt" label="拥有我行股份金额(元)" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				
			</emp:gridLayout>
			<!-- /* modify by wangj 2015-05-20  中征码修改 begin*/ -->
			<emp:gridLayout id="" title="中征码信息" maxColumn="2" >
				<emp:select id="CusBase.loan_card_flg" label="是否有中征码" required="true" colSpan="2" hidden="false" dictname="STD_ZX_YES_NO" />
				<emp:text id="CusBase.loan_card_id" label="中征码" maxlength="16" required="true" hidden="true" />
				<emp:text id="CusBase.loan_card_pwd" label="贷款卡密码" maxlength="6" required="false" hidden="true" />
				<emp:select id="CusBase.loan_card_eff_flg" label="贷款卡状态" required="false" dictname="STD_ZB_LOAN_CARD_FLG"  hidden="true" />
				<emp:date id="CusBase.loan_card_audit_dt" label="贷款卡年检到期日" required="false"  hidden="true" />
			</emp:gridLayout>
			<!-- /* modify by wangj 2015-05-20  中征码修改 end*/ -->	
			<emp:gridLayout id="" title="登记信息" maxColumn="2" >
				<emp:pop id="CusBase.cust_mgr_displayname" label="主管客户经理" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" required="true" />
				<emp:pop id="CusBase.main_br_id_displayname" label="主管机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName"/>
				<emp:text id="CusBase.input_id_displayname" label="登记人"  required="true" readonly="true" defvalue="$currentUserName" />
				<emp:text id="CusBase.input_br_id_displayname" label="登记机构" required="true" readonly="true" defvalue="$organName" />
				<emp:date id="CusBase.input_date" label="登记日期" required="true" readonly="true" defvalue="$OPENDAY"/>
				<emp:select id="CusBase.belg_line" label="所属条线" dictname="STD_ZB_BUSILINE" readonly="true" defvalue="BL300"/>
				
				<emp:text id="CusBase.cust_mgr" label="主管客户经理" required="true" hidden="true"/>
				<emp:text id="CusBase.main_br_id" label="主管机构" required="true" hidden="true"/>
				<emp:text id="CusBase.input_id" label="登记人" maxlength="20" required="true" readonly="true" defvalue="$currentUserId" hidden="true"/>
				<emp:text id="CusBase.input_br_id" label="登记机构" maxlength="20" required="true" readonly="true" defvalue="$organNo" hidden="true"/>
			</emp:gridLayout>
		</div>
	</emp:tab>
	<emp:tab id="cont_tab" label="联系信息" initial="true" needFlush="true"><div>
		<emp:gridLayout id="CusIndivGroup" title="个人客户联系信息" maxColumn="2">
			<emp:text id="CusIndiv.post_addr" label="身份证地址" required="true" hidden="true"/>
			<emp:pop id="CusIndiv.post_addr_displayname" label="身份证地址" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" 
				returnMethod="onReturnPostAddr" colSpan="2"  cssElementClass="emp_field_text_input2" required="true"/>	
			<emp:text id="CusIndiv.street2" label="街道"  required="false" cssElementClass="emp_field_text_input2" colSpan="2"/>
			<emp:text id="CusIndiv.post_code" label="邮政编码" maxlength="6" required="true" dataType="Postcode" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusIndiv.indiv_rsd_addr" label="常住居住地址" required="true" hidden="true"/>
			<emp:pop id="CusIndiv.indiv_rsd_addr_displayname" label="常住居住地址" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" 
				returnMethod="onReturnRsdAddr" colSpan="2"  cssElementClass="emp_field_text_input2" required="true"/>	
			<emp:text id="CusIndiv.street3" label="常住居住地址街道/路"  required="true" cssElementClass="emp_field_text_input2" colSpan="2"/>
			<emp:text id="CusIndiv.indiv_zip_code" label="常住居住地邮政编码" maxlength="6" required="true" dataType="Postcode" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="CusIndiv.indiv_rsd_st" label="常住居住状况" required="true" dictname="STD_ZB_RESIDE_STATUS"/>
			<emp:pop id="CusIndiv.area_code" label="区域编号" required="true" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" returnMethod="onReturnRegStateCode" colSpan="2" />
			<emp:text id="CusIndiv.area_code_displayname" label="区域名称" required="true" readonly="true" cssElementClass="emp_field_text_input2" colSpan="2"/>
			<emp:text id="CusIndiv.fphone" label="住宅电话" maxlength="35" required="false"  dataType="Phone" defvalue="0573" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusIndiv.mobile" label="手机号码" maxlength="35" required="true" dataType="Mobile" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusIndiv.phone" label="第二联系方式(手机)" maxlength="35" required="false" dataType="Mobile" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusIndiv.fax_code" label="传真" maxlength="35" required="false" dataType="Phone" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusIndiv.email" label="Email地址" maxlength="80" required="false" dataType="Email" colSpan="2" cssElementClass="emp_currency_text_readonly"/>
		</emp:gridLayout>
	</div>
	</emp:tab>
		<emp:tab id="work_tab" label="单位信息" initial="true" needFlush="true"><div>
			<emp:gridLayout id="CusIndivGroup" title="个人客户单位信息" maxColumn="2">
				<emp:select id="CusIndiv.indiv_occ" label="职业" dictname="STD_ZX_EMPLOYMET" readonly="true" cssFakeInputClass="emp_field_select_select1" colSpan="2"/>
			    <emp:text id="CusIndiv.indiv_com_name" label="工作单位" maxlength="60" required="true" colSpan="2" />
				<emp:select id="CusIndiv.indiv_com_typ" label="单位性质" dictname="STD_ZB_UNIT_TYPE" readonly="true" cssFakeInputClass="emp_field_select_select1" colSpan="2"/>
				<emp:pop id="CusIndiv.indiv_com_fld" label="单位所属行业" required="true" url="showDicTree.do?dicTreeTypeId=STD_GB_4754-2011" returnMethod="onReturn" colSpan="2" />
				<emp:text id="CusIndiv.indiv_com_fld_displayname" label="单位所属行业名称"  required="true" colSpan="2" cssElementClass="emp_field_text_input2" readonly="true" />
				<emp:text id="CusIndiv.indiv_com_addr" label="单位地址" required="true" hidden="true"/>
				<emp:pop id="CusIndiv.indiv_com_addr_displayname" label="单位地址" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL"
					returnMethod="onReturnIndivAddr" colSpan="2" cssElementClass="emp_field_text_input2" required="true" />
				<emp:text id="CusIndiv.street_unit" label="单位地址街道/路"  required="true" cssElementClass="emp_field_text_input2" colSpan="2"/>
				<emp:text id="CusIndiv.indiv_com_zip_code" label="单位邮编" maxlength="6" required="false" />
				<emp:text id="CusIndiv.indiv_com_phn" label="单位电话" maxlength="25" required="false" dataType="Phone" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="CusIndiv.indiv_com_fax" label="单位传真" maxlength="25" required="false" dataType="Phone" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="CusIndiv.indiv_com_cnt_name" label="单位联系人" maxlength="30" required="false" />
				<emp:text id="CusIndiv.indiv_work_job_y" label="单位工作起始年" required="true" />
				<emp:select id="CusIndiv.indiv_com_job_ttl" label="职务" dictname="STD_ZX_DUTY" readonly="true" cssFakeInputClass="emp_field_select_select1" colSpan="2"/>
				<emp:select id="CusIndiv.indiv_crtfctn" label="职称" required="true" dictname="STD_ZX_TITLE"/>
				<emp:text id="CusIndiv.indiv_ann_incm" label="年收入(元)" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="CusIndiv.indiv_sal_acc_bank" label="工资账户开户行" maxlength="80" required="false" colSpan="2" />
				<emp:text id="CusIndiv.indiv_sal_acc_no" label="工资账号" maxlength="32" required="false" />
				<emp:textarea id="CusIndiv.work_resume" label="个人简历" maxlength="125" colSpan="2" required="false"></emp:textarea>
			</emp:gridLayout></div>
		</emp:tab>
	 	<emp:tab label="配偶信息" id="querySpouseInfo" url="querySpouseInfo.do" initial="false" reqParams="cus_id=${context.CusIndiv.cus_id}" needFlush="true"></emp:tab>
		<emp:tab label="经营信息" id="managerBusInfo" url="queryCusIndivBusinessList.do" initial="false" reqParams="cus_id=${context.CusIndiv.cus_id}&EditFlag=${context.flag}" needFlush="true"></emp:tab>
		<emp:tab label="修改痕迹" id="modifyHistory" url="queryModifyHistoryList.do" initial="false" reqParams="cus_id=${context.CusIndiv.cus_id}" needFlush="true"></emp:tab>
	</emp:tabGroup>
	</emp:form>
</body>
</html>
</emp:page>