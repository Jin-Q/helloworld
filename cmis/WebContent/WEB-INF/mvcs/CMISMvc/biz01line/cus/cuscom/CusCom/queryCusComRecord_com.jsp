<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<%@ page import="com.ecc.emp.core.Context"%>
<%@ page import="java.util.*"%>
<%@page import="com.ecc.emp.core.EMPConstance"%><emp:page>
<%
	String flag = request.getParameter("flag");
	String oper = request.getParameter("oper");
	String btnflag = request.getParameter("btnflag");//控制按钮 (rep:补录完成  due:正式客户 temp:临时、草稿)
	if ((flag != null && flag.equals("query"))) {
		request.setAttribute("canwrite", "");
	}
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	context.addDataField("infotree",oper);
%>
<html>
<head>
<title>客户信息页面</title>
<jsp:include page="/include.jsp" flush="true" />
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/cus/cusbase/CusBase/cusInfo.jsp" flush="true" />
<jsp:include page="jsCusCom.jsp" flush="true" />
<link href="<emp:file fileName='styles/start/jquery-ui-1.7.1.custom.css'/>" rel="stylesheet" type="text/css" media="screen" />
<script type="text/javascript" src="<emp:file fileName='scripts/jquery-1.3.2.js'/>"></script>
<script type="text/javascript" src="<emp:file fileName='scripts/jquery-ui-1.7.1.custom.min.js'/>"></script>
<script type="text/javascript" src="<emp:file fileName='scripts/jquery.cmisDialogs.js'/>"></script>
<script type="text/javascript">
/* modify by yangzy 2015-05-21  中征码修改 begin*/
var tmpLoanCardId;
/* modify by yangzy 2015-05-21  中征码修改 end*/
	function doOnLoad() {
		/* modify by yangzy 2015-05-21  中征码修改 begin*/
		tmpLoanCardId = CusBase.loan_card_id._getValue();
		/* modify by yangzy 2015-05-21  中征码修改 end*/
		CusCom.grp_no._obj.addOneButton('view12','查看',viewGrpCusInfo);  //集团编号加查看按钮
		checkRegCode();//税务关系	
		changeRelaCust();//是否为我行关联客户
		//信用等级（外部）
	    changeGrade();
	    cheakSpBusiness();//检查特种经营标识
	    cheakMrk();
	    //是否有贷款卡
	    changeCardFlg();
		//高环境风险高污染企业
		doChange();
		//联动显示集团客户相关信息
		linkChangeComGrpMode();
		//判断客户类型是否为融资性担保公司
		checkCoopInfo();
		checkIeFlag();
		checkIsLong();//是否为长期证件
	};
	//检查特种经营标识
	function cheakSpBusiness(){
		 if(CusCom.com_sp_business._obj.element.value=="1"){
			   CusCom.com_sp_lic_no._obj._renderRequired(true);
			   CusCom.com_sp_lic_no._obj._renderHidden(false);
	
			   CusCom.com_sp_detail._obj._renderRequired(true);
			   CusCom.com_sp_detail._obj._renderHidden(false);
	
			   CusCom.com_sp_lic_org._obj._renderRequired(false);
			   CusCom.com_sp_lic_org._obj._renderHidden(false);
	
			   CusCom.com_sp_str_date._obj._renderRequired(true);
			   CusCom.com_sp_str_date._obj._renderHidden(false);
	
			   CusCom.com_sp_end_date._obj._renderRequired(true);
			   CusCom.com_sp_end_date._obj._renderHidden(false);
		   }else if(CusCom.com_sp_business._obj.element.value=="2"){
			   CusCom.com_sp_lic_no._obj._renderRequired(false);
			   CusCom.com_sp_lic_no._obj._renderHidden(true);
	
			   CusCom.com_sp_detail._obj._renderRequired(false);
			   CusCom.com_sp_detail._obj._renderHidden(true);
	
			   CusCom.com_sp_lic_org._obj._renderRequired(false);
			   CusCom.com_sp_lic_org._obj._renderHidden(true);
	
			   CusCom.com_sp_str_date._obj._renderRequired(false);
			   CusCom.com_sp_str_date._obj._renderHidden(true);
	
			   CusCom.com_sp_end_date._obj._renderRequired(false);
			   CusCom.com_sp_end_date._obj._renderHidden(true);
		}
	}

	function checkLoanCardAuditDt(){
		var date = CusBase.loan_card_audit_dt._getValue();
		var openDay = '${context.OPENDAY}';
		if(date !=null && date != ""){
			if(date<openDay){
				alert("年检到期日应大于等于当前日期！");
				CusBase.loan_card_audit_dt._setValue("");
			}
		}
	}

	function checkIsLong(){//是否为长期证件
		var is_long_com = CusCom.is_long_com._getValue();
		if(is_long_com == '2'){//否
			CusCom.reg_end_date._obj._renderHidden(false);
			CusCom.reg_end_date._obj._renderRequired(true);
		}else{
			CusCom.reg_end_date._obj._renderHidden(true);
			CusCom.reg_end_date._obj._renderRequired(false);
		}
	}
</script>
	</head>
	<body class="page_content" onload="doOnLoad()">
	<emp:form id="submitForm" action="updateCusComRecord.do" method="POST">
		<emp:tabGroup id="CusCom_tabs" mainTab="base_tab">
			<emp:tab id="base_tab" label="基本信息" initial="true" >
				<div>
					<emp:gridLayout id="CusComGroup" title="对公客户基本信息" maxColumn="2">
					<emp:select id="CusBase.cert_type" label="证件类型" required="true" readonly="true" dictname="STD_ZB_CERT_TYP" /> 
					<emp:text id="CusBase.cert_code" label="证件号码" maxlength="20" required="true" readonly="true" />
					<emp:text id="CusBase.cus_id" label="客户码" maxlength="30" required="true" readonly="true" />
					<emp:text id="CusBase.cus_name" label="客户名称" maxlength="80" onchange="CusBase.cus_short_name._setValue(this.value);" required="true" readonly="true" />
					<emp:text id="CusBase.cus_short_name" label="客户简称" maxlength="46" required="false" />
					<emp:text id="CusCom.cus_en_name" label="外文名称" maxlength="80" required="false" />
					<emp:select id="CusBase.cus_type" label="客户类型" required="true" dictname="STD_ZB_CUS_TYPE" readonly="true"/>
					<emp:select id="CusBase.cus_country" label="国别" required="true" dictname="STD_GB_2659-2000" defvalue="CHN" readonly="true" />
					<emp:select id="CusCom.com_city_flg" label="城乡类型" required="true" dictname="STD_ZB_URBAN_RURAL" />
					<emp:select id="CusCom.invest_type" label="投资主体" required="true" dictname="STD_ZB_INVESTOR" />
					<emp:select id="CusCom.com_sub_typ" label="隶属关系" required="false" dictname="STD_ZB_SUBJECTION" />
					<emp:select id="CusCom.com_hold_type" label="控股类型" required="true" dictname="STD_ZB_HOLD_TYPE" />
					<emp:pop id="CusCom.corp_qlty" label="企业性质编号" url="showDicTree.do?dicTreeTypeId=STD_GB_CORP_QLTY" required="true" returnMethod="onReturnCorpQlty"/>
					<emp:text id="CusCom.corp_qlty_displayname" label="企业性质" colSpan="2" cssElementClass="emp_field_text_input2" readonly="true"/>
					<emp:pop id="CusCom.econ_dep" label="国民经济部门编号" required="true" url="showDicTree.do?dicTreeTypeId=STD_GB_ECON_DEPT" returnMethod="onReturnEconDept"/>
					<emp:text id="CusCom.econ_dep_displayname" label="国民经济部门" colSpan="2" cssElementClass="emp_field_text_input2" readonly="true" />
					<emp:pop id="CusCom.com_cll_type" label="行业编号(国标)" required="true" url="showDicTree.do?dicTreeTypeId=STD_GB_4754-2011" returnMethod="onReturnComCllName"/>
					<emp:text id="CusCom.com_cll_type_displayname" label="行业名称" colSpan="2" cssElementClass="emp_field_text_input2" readonly="true" />
					<emp:date id="CusCom.com_str_date" label="成立日期" required="true" />
					<emp:text id="CusCom.com_employee" label="从业人数" maxlength="38" required="true" />
					<emp:select id="CusCom.com_scale" label="企业规模" required="true" dictname="STD_ZB_ENTERPRISE" defvalue="31" readonly="true" colSpan="2"/>
					<emp:pop id="CusCom.reg_state_code" label="注册地行政区划" required="true" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" returnMethod="onReturnRegStateCode" colSpan="2" />
					<emp:text id="CusCom.reg_state_code_displayname" label="行政区划名称" readonly="true" colSpan="2" cssElementClass="emp_field_text_input2" />
				</emp:gridLayout> 
				<%-- <emp:gridLayout id="" title="组织机构代码信息" maxColumn="2">
					<emp:text id="CusBase.cert_code1" label="组织机构代码" maxlength="20" required="true" colSpan="2" readonly="true" defvalue="$CusBase.cert_code"/>
					<emp:date id="CusCom.com_ins_reg_date" label="组织机构登记日期" required="true" />
					<emp:date id="CusCom.com_ins_exp_date" label="组织机构登记有效期" required="true" />
					<emp:text id="CusCom.com_ins_org" label="组织机构代码证颁发机关" maxlength="60" required="true"/>
					<emp:date id="CusCom.com_ins_ann_date" label="组织机构代码证年检到期日" required="false" />
				</emp:gridLayout> --%> 
				<emp:gridLayout id="" title="证件信息" maxColumn="2">
					<emp:text id="CusBase.cert_code1" label="证件代码" maxlength="20" required="true" colSpan="2" readonly="true" defvalue="$CusBase.cert_code"/>
					<emp:date id="CusCom.com_ins_reg_date" label="证件登记日期" required="true" onblur="CheckRegDate(CusCom.com_ins_reg_date,CusCom.com_ins_exp_date);" />
					<emp:date id="CusCom.com_ins_exp_date" label="证件登记有效期" required="true" readonly="false"
						onblur="CheckExpDate(CusCom.com_ins_reg_date,CusCom.com_ins_exp_date);CheckDate4End(CusCom.com_ins_exp_date,CusCom.com_ins_ann_date)" />
					<emp:text id="CusCom.com_ins_org" label="证件颁发机关" maxlength="60" required="false"/>
					<emp:date id="CusCom.com_ins_ann_date" label="证件年检到期日" required="false" />
				</emp:gridLayout>
				<emp:gridLayout id="" title="登记注册信息" maxColumn="2">
					<emp:text id="CusCom.reg_code" label="登记注册号" maxlength="30" required="true" colSpan="2" />
					<emp:select id="CusCom.reg_type" label="登记注册类型" required="true" readonly="true" cssFakeInputClass="emp_field_select_select1" dictname="STD_ZB_REG_TYPE" />
					<emp:text id="CusCom.admin_org" label="主管单位" maxlength="100" required="false" colSpan="2" cssElementClass="emp_field_text_input2" hidden="false"/>
					<emp:text id="CusCom.appr_org" label="登记机关" maxlength="100" required="true" colSpan="2" cssElementClass="emp_field_text_input2" hidden="false"/>
					<emp:text id="CusCom.appr_doc_no" label="批准文号" maxlength="100" required="false" colSpan="2" cssElementClass="emp_field_text_input2" hidden="false"/>
					<emp:text id="CusCom.reg_addr" label="注册登记地址" colSpan="2" hidden="true"/>
					<emp:pop id="CusCom.reg_addr_displayname" label="注册登记地址" colSpan="2" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" 
						returnMethod="onReturnRegStateCode1" cssElementClass="emp_field_text_input2" required="true" />	
					<emp:text id="CusCom.reg_street" label="街道"  required="true" cssElementClass="emp_field_text_input2" colSpan="2"/>	
					<emp:text id="CusCom.en_reg_addr" label="外文注册登记地址" maxlength="80" required="false" colSpan="2" cssElementClass="emp_field_text_input2" />
					<emp:text id="CusCom.acu_addr" label="实际经营地址" required="false" colSpan="2" hidden="true"/>
					<emp:pop id="CusCom.acu_addr_displayname" label="实际经营地址" colSpan="2" cssElementClass="emp_field_text_input2" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" 
						returnMethod="onReturnRegStateCode2" required="true"/>	
					<emp:text id="CusCom.street" label="街道(住所)"  required="true" cssElementClass="emp_field_text_input2" colSpan="2"/>	
					<emp:textarea id="CusCom.com_part_opt_scp" label="一般经营项目" maxlength="250" required="true" colSpan="2" />
					<emp:textarea id="CusCom.com_main_opt_scp" label="许可经营项目" maxlength="500" required="false" colSpan="2" />
					<emp:select id="CusCom.reg_cur_type" label="注册资本币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" />
					<emp:text id="CusCom.reg_cap_amt" label="注册资金(万元)" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
					<emp:select id="CusCom.paid_cap_cur_type" label="实收资本币种" dictname="STD_ZX_CUR_TYPE" required="true" defvalue="CNY"/>
					<emp:text id="CusCom.paid_cap_amt" label="实收注册资金(万元)" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>	
					<emp:date id="CusCom.reg_start_date" label="注册登记日期" required="true" />
					<emp:date id="CusCom.reg_audit_end_date" label="注册登记年检到期日" required="false" />
					<emp:select id="CusCom.is_long_com" label="是否为长期" dictname="STD_ZX_YES_NO" required="true" onchange="checkIsLong()" defvalue="1"/>	
					<emp:date id="CusCom.reg_end_date" label="注册登记有效期" required="false" hidden="true"/>	
					<emp:text id="CusCom.reg_area_code" label="注册地区域编码" required="true" colSpan="2" dataType="Postcode"/>
					<emp:date id="CusCom.reg_audit_date" label="注册登记年审日期" required="false" colSpan="2"/>	
					<emp:textarea id="CusCom.reg_audit" label="注册登记年审结论" maxlength="200" required="false" colSpan="2" />	
				</emp:gridLayout> 
				<emp:gridLayout id="" title="税务信息" maxColumn="2">
					<emp:text id="CusCom.loc_tax_reg_code" label="地税税务登记代码" maxlength="20" colSpan="2" onblur="checkRegCode()"/>
					<emp:date id="CusCom.loc_tax_reg_dt" label="地税税务登记日期" />
					<emp:date id="CusCom.loc_tax_reg_end_dt" label="地税税务登记有效期" />
					<emp:date id="CusCom.loc_tax_ann_date" label="地税年检到期日" colSpan="2" />
					<emp:text id="CusCom.loc_tax_reg_org" label="地税登记机关" maxlength="80" onblur="checkRegCode()"/>
					<emp:text id="CusCom.nat_tax_reg_code" label="国税税务登记代码" maxlength="20" colSpan="2" onblur="checkRegCode()"/>
					<emp:date id="CusCom.nat_tax_reg_dt" label="国税税务登记日期" />
					<emp:date id="CusCom.nat_tax_reg_end_dt" label="国税税务登记有效期" />
					<emp:date id="CusCom.nat_tax_ann_date" label="国税年检到期日" colSpan="2" />
					<emp:text id="CusCom.nat_tax_reg_org" label="国税登记机关" maxlength="80" onblur="checkRegCode()"/>
				</emp:gridLayout> 
				<emp:gridLayout id="" title="特种经营信息" maxColumn="2">
					<emp:select id="CusCom.com_sp_business" label="特种经营标识" required="true" dictname="STD_ZX_YES_NO" colSpan="2" defvalue="2" />
					<emp:text id="CusCom.com_sp_lic_no" label="特种经营许可证编号" maxlength="80" required="false" colSpan="2" />
					<emp:textarea id="CusCom.com_sp_detail" label="特种经营情况" maxlength="80" required="false" colSpan="2" />
					<emp:text id="CusCom.com_sp_lic_org" label="特种许可证颁发机关" maxlength="80" required="false" colSpan="2" cssElementClass="emp_field_text_input2" />
					<emp:date id="CusCom.com_sp_str_date" label="特种经营登记日期" required="true" />
					<emp:date id="CusCom.com_sp_end_date" label="特种经营到期日期" required="true" />
				</emp:gridLayout>
				<emp:gridLayout id="" title="与我行关系" maxColumn="2">
					<emp:select id="CusCom.is_ours_rela_cust" label="是否为我行关联客户" dictname="STD_ZB_OURS_RECUST" required="true" onchange="changeRelaCust()" colSpan="2" defvalue="03"/>
					<emp:select id="CusCom.cus_bank_rel" label="与我行关联关系" dictname="STD_ZB_CUS_BANK" hidden="true" readonly="true" cssFakeInputClass="emp_field_select_select1" colSpan="2"/>
					<emp:text id="CusCom.cus_bank_rel_fake" label="与我行关联关系" defvalue="普通客户关系" readonly="true" cssElementClass="emp_field_select_select1" colSpan="2"/>
					<emp:select id="CusCom.bank_duty" label="在我行职务" required="false" dictname="STD_ZB_BANK_DUTY" hidden="true"/>
					<emp:text id="CusCom.equi_no" label="股权证号码" maxlength="30" required="false" hidden="true"/>
					<emp:text id="CusCom.bank_equi_amt" label="拥有我行股份(元)" maxlength="18" dataType="Currency" hidden="true" cssElementClass="emp_currency_text_readonly"/>
				</emp:gridLayout>
				<!-- /* modify by wangj 2015-05-20  中征码修改 begin*/ -->
				<emp:gridLayout id="" title="中征码信息" maxColumn="2">
					<emp:select id="CusBase.loan_card_flg" label="是否有中征码" required="true" dictname="STD_ZX_YES_NO" colSpan="2" onchange="changeCardFlg()" defvalue="2"/>
					<emp:text id="CusBase.loan_card_id" label="中征码" maxlength="16" required="false" hidden="true"/>
				<!-- /* modify by wangj 2015-05-20  中征码修改 end*/ -->
					<emp:text id="CusBase.loan_card_pwd" label="贷款卡密码" hidden="true" maxlength="6" required="false" />
					<emp:select id="CusBase.loan_card_eff_flg" label="贷款卡状态" hidden="true" required="false" dictname="STD_ZB_LOAN_CARD_FLG" />
					<emp:date id="CusBase.loan_card_audit_dt" label="贷款卡年检到期日" hidden="true" required="false" onblur="checkLoanCardAuditDt()"/>
				</emp:gridLayout>
				
				<emp:gridLayout id="" title="银行账户信息" maxColumn="2">
					<emp:select id="CusCom.bas_acc_flg" label="基本存款账户是否在本行" required="false" hidden="false" 
						colSpan="2" dictname="STD_ZX_YES_NO" onchange="licenceRequired('2');"/>
					<emp:text id="CusCom.bas_acc_licence" label="基本账户开户许可证核准号" hidden="false" required="false" />
					<emp:text id="CusCom.bas_acc_no" label="基本存款账户帐号" maxlength="32" required="false" hidden="false" />
					<emp:date id="CusCom.bas_acc_date" label="基本账户开户日期 " required="false" hidden="false" />
					<emp:pop id="CusCom.bas_acc_bank_displayname" label="基本存款账户开户行" url="getPrdBankInfoPopList.do" returnMethod="getAaorgNo" required="false" hidden="false" buttonLabel="选择"/>
					<emp:text id="CusCom.bas_acc_bank" label="基本存款账户开户行" maxlength="80" required="false" hidden="true"/>
				</emp:gridLayout>
				</div>
			</emp:tab>
			<emp:tab id="cert_tab" label="联系信息" initial="true">
				<div>
				<emp:gridLayout id="" title="财务部信息" maxColumn="2">
					<emp:text id="CusCom.fna_mgr" label="财务部负责人" maxlength="35" required="true" />
					<emp:text id="CusCom.com_operator" label="财务部联系人" maxlength="35" required="true" />
					<emp:text id="CusCom.fina_per_tel" label="财务部联系人电话" maxlength="35" required="false" />
					<emp:text id="CusCom.fina_per_phone" label="财务部联系人手机" maxlength="35" required="true" />
				</emp:gridLayout>		
				<emp:gridLayout id="" title="地址信息" maxColumn="2">	
					<emp:text id="CusCom.post_addr" label="省/直辖市/自治区" required="true" colSpan="2" hidden="true" />
					<emp:pop id="CusCom.post_addr_displayname" label="省/直辖市/自治区" colSpan="2" cssElementClass="emp_field_text_input2" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" 
						returnMethod="onReturnStreetPost" required="true"/>	
					<emp:text id="CusCom.street_post" label="街道" cssElementClass="emp_field_text_input2" colSpan="2"/>
					<emp:text id="CusCom.post_code" label="邮政编码" maxlength="6" required="true" colSpan="2" />
					<emp:text id="CusCom.legal_phone" label="联系电话" maxlength="35" required="true" colSpan="2" />
					<emp:text id="CusCom.fax_code" label="传真" maxlength="35" />
					<emp:text id="CusCom.email" label="Email" maxlength="80" dataType="Email" colSpan="2" cssElementClass="emp_field_text_input2" />
					<emp:text id="CusCom.web_url" label="网址" maxlength="80" colSpan="2" cssElementClass="emp_field_text_input2" />
				</emp:gridLayout></div>
			</emp:tab>
			<emp:tab id="Cont_tab" label="概况信息" initial="true">
				<div>
				<emp:gridLayout id="CusComGroup" title="对公客户概况信息" maxColumn="2">
					<emp:select id="CusCom.com_mrk_flg" label="上市公司标志" required="true" dictname="STD_ZX_YES_NO" colSpan="2" defvalue="2" />						
					<emp:select id="CusCom.com_mrk_area" label="上市地" required="true" dictname="STD_ZX_LISTED" />
					<emp:text id="CusCom.com_stock_id" label="股票代码" maxlength="32" required="true" />
					<emp:select id="CusCom.gover_fin_ter" readonly="true" label="政府融资平台" required="true" dictname="STD_ZX_YES_NO" colSpan="2" defvalue="2" />
					<emp:select id="CusBase.belg_line" readonly="true" label="所属条线" required="true" dictname="STD_ZB_BUSILINE" colSpan="2" defvalue="01"/>	
					<emp:select id="CusCom.com_grp_mode" label="集团客户类型" required="true" defvalue="9" dictname="STD_ZB_GROUP_TYPE" colSpan="2" onchange="linkChangeComGrpMode();" />
					<emp:text id="CusCom.grp_no" label="所属集团号" maxlength="40"/>
					<emp:text id="CusCom.parent_cus_name" label="集团客户母公司名称" maxlength="80" hidden="true"/>
					<emp:text id="CusCom.parent_cert_code" label="母公司组织机构代码" maxlength="20" hidden="true" />
					<emp:text id="CusCom.parent_loan_card" label="母公司贷款卡编码" maxlength="16" hidden="true" />
					
					<emp:select id="CusCom.str_cus" readonly="true" label="战略性客户" required="true" dictname="STD_ZX_YES_NO" colSpan="2" defvalue="2" onchange="changeLine()"/>	
					<emp:date id="CusCom.str_cus_end_dt" readonly="true" label="战略性客户到期日" required="false" hidden="true"/>	
					<emp:select id="CusCom.hou_exp" readonly="true" label="房地产开发商" required="true" dictname="STD_ZX_YES_NO" colSpan="2" defvalue="2" />	
					<emp:select id="CusCom.ie_flag" label="进出口权标志" required="true" dictname="STD_ZX_YES_NO" onchange="checkIeFlag()"/>
					<emp:select id="CusCom.rel_flag" label="是否存在主要关联企业" required="true" dictname="STD_ZX_YES_NO" colSpan="2" defvalue="2"/>
					<emp:text id="CusCom.ie_con_code" label="进出口企业代码" required="flase" hidden="true"/>	
					<emp:textarea id="CusCom.com_main_product" label="主要产品情况" maxlength="250" required="false" colSpan="2" />
					<emp:textarea id="CusCom.com_prod_equip" label="主要生产设备" maxlength="250" required="false" colSpan="2" hidden="false" />
					<emp:textarea id="CusCom.com_fact_prod" label="实际生产能力" maxlength="250" required="false" colSpan="2" hidden="false" />
					<emp:text id="CusCom.com_opt_aera" label="经营场地面积(平方米)" maxlength="16" required="true" />
					<emp:select id="CusCom.com_opt_owner" label="经营场地所有权" required="true" dictname="STD_ZX_FIELD_OWNER" />
					<emp:select id="CusCom.com_opt_st" label="经营状况" required="true" dictname="STD_ZB_BUSI_STATUS"/>
					<emp:select id="CusCom.com_imptt_flg" label="地区重点企业" required="true" dictname="STD_ZX_YES_NO" />
					<emp:select id="CusCom.com_prep_flg" label="优势企业" required="true" hidden="false" dictname="STD_ZX_YES_NO" />
					<emp:select id="CusCom.com_dhgh_flg" label="高环境风险高污染企业" onchange="doChange()" required="true" dictname="STD_ZX_YES_NO" />
					<emp:select id="CusCom.com_cl_entp" label="国家宏观调控限控行业" required="true" dictname="STD_ZX_YES_NO" defvalue="2" colSpan="2"/>	
					<emp:text id="CusCom.row_lice" label="排污许可证(编号)" maxlength="25" required="true"/>	
					<emp:date id="CusCom.row_lice_end_dt" label="排污许可证到期日" required="true" hidden="false" />			
					<emp:select id="CusCom.com_hd_enterprise" label="龙头企业" required="true" dictname="STD_ZB_COM_HD_ENTER" colSpan="2" />	
					<emp:text id="CusCom.com_mng_org" label="上级主管单位" maxlength="80" required="false" colSpan="2" cssElementClass="emp_field_text_input2" />
					<emp:select id="CusCom.com_fin_rep_type" label="财务报表类型" required="true" colSpan="2" dictname="STD_ZB_FIN_REP_TYPE"  hidden="true" defvalue="PB0001"/>	
				</emp:gridLayout></div>
			</emp:tab>
			<emp:tab id="pro_tab" label="合作信息" initial="true">
				<div>
				<emp:gridLayout id="CusComGroup" title="合作信息" maxColumn="2">
					<emp:date id="CusCom.com_init_loan_date" label="建立信贷关系时间" required="true" defvalue="${context.OPENDAY}" colSpan="2"/>
					<emp:select id="CusBase.cus_crd_grade" label="信用等级(内部)" required="false" dictname="STD_ZB_CREDIT_GRADE" defvalue="00" readonly="true" colSpan="2"/>
					<emp:select id="CusBase.guar_crd_grade" label="担保信用等级(内部)" required="false" dictname="STD_ZB_FINA_GRADE" defvalue="Z" hidden="true" readonly="true"/>		
					<emp:date id="CusBase.cus_crd_dt" label="信用评定到期日期" required="false" readonly="true" />
					<emp:select id="CusCom.com_out_crd_grade" label="信用等级(外部)" required="true" dictname="STD_ZB_CREDIT_GRADE" colSpan="2" onchange="changeGrade()"/>
					<emp:select id="CusCom.guar_crd_grade2" label="担保信用等级(外部)" required="false" dictname="STD_ZB_FINA_GRADE" hidden="true"/>
					<emp:date id="CusCom.com_out_crd_dt" label="评定日期(外部)" />
					<emp:date id="CusCom.com_coop_exp" label="合作有效期" required="false" hidden="true"/>	
					<emp:select id="CusCom.guar_cls" label="担保类别" required="false" dictname="STD_ZB_GUAR_TYPE" hidden="true" readonly="true" />	
					<emp:text id="CusCom.guar_bail_multiple" label="担保放大倍数" required="false" hidden="true" dataType="Int" readonly="true" cssElementClass="emp_currency_text_readonly"/>		
					<emp:text id="CusCom.com_out_crd_org" label="评定机构" maxlength="60" colSpan="2" />
				</emp:gridLayout>
				<emp:gridLayout id="cusManagerInfo" title="管户人">
					<emp:pop id="CusBase.cust_mgr_displayname" label="主管客户经理" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
					<emp:pop id="CusBase.main_br_id_displayname" label="主管机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName"/>
					<emp:text id="CusBase.input_id_displayname" label="登记人" required="false" readonly="true" />
					<emp:text id="CusBase.input_br_id_displayname" label="登记机构" required="false" readonly="true" />
					<emp:text id="CusBase.input_date" label="登记日期" maxlength="10" required="false" readonly="true" defvalue="$OPENDAY"/>
					<emp:text id="CusCom.cust_mgr_phone" label="主管客户经理手机" maxlength="20" required="true" />
					<emp:select id="CusBase.cus_status" label="客户状态" required="false" dictname="STD_ZB_CUS_STATUS" readonly="true"/>
					<emp:text id="CusBase.main_br_id" label="主管机构" required="false" hidden="true"/>
					<emp:text id="CusBase.cust_mgr" label="主管客户经理" required="false" hidden="true"/>
					<emp:text id="CusBase.input_id" label="登记人" maxlength="20" required="false" readonly="true" hidden="true" defvalue="$currentUserId"/> 
					<emp:text id="CusBase.input_br_id" label="登记机构" maxlength="20" required="false" readonly="true" hidden="true" defvalue="$organNo"/>
				</emp:gridLayout>
				</div>
			</emp:tab>
        <emp:tab label="修改历史记录" id="modifyHistory" url="queryModifyHistoryList.do" initial="true" needFlush="true" reqParams="cus_id=${context.CusBase.cus_id}"></emp:tab>
		</emp:tabGroup>
	</emp:form>
	</body>
	</html>
</emp:page>