<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>
<style type="text/css">
.emp_field_cus_addr_input {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 395px;
}

;
.emp_field_label {
	text-align: right;
}

;
.emp_field_textarea_textarea {
	width: 450;
	height: 80;
}

;
.emp_field_long_input {
	text-align: right;
}
;
</style>
<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryLmtIntbankBatchListLmtBatchCorreList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
		window.close();
	}

	function checkCountry(){
		var comCountry = CusSameOrg.country._getValue();
		if(comCountry == 'CHN'){
			CusSameOrg.address_displayname._obj._renderHidden(false);
		}else{
			CusSameOrg.address_displayname._obj._renderHidden(true);
		}
	}

	function doload(){
		checkCountry();
	}
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	
	<emp:gridLayout id="CusSameOrgGroup" title="金融同业客户" maxColumn="2">
			<emp:text id="CusSameOrg.cus_id" label="客户码" required="false" readonly="true" hidden="true"/>
			<emp:text id="CusSameOrg.com_ins_code" label="组织机构代码" maxlength="10" required="true" onchange="CheckComInsCode()"/>
			<emp:text id="CusSameOrg.swift_no" label="SWIFT编号" maxlength="35" required="false" />
			<emp:text id="CusSameOrg.same_org_no" label="同业机构(行)号" maxlength="40" required="true"/>
			<emp:text id="CusSameOrg.same_org_cnname" label="同业机构(行)名称" maxlength="100" required="true" colSpan="2" cssElementClass="emp_field_cus_addr_input"/>
			<emp:text id="CusSameOrg.same_org_enname" label="同业机构(行)英文名称" maxlength="40" required="false" colSpan="2" cssElementClass="emp_field_cus_addr_input"/>
			<emp:text id="CusSameOrg.org_site" label="同业机构(行)网址" maxlength="80" required="false" colSpan="2" cssElementClass="emp_field_cus_addr_input"/>
			<emp:select id="CusSameOrg.same_org_type" label="同业机构类型" required="true" dictname="STD_ZB_INTER_BANK_ORG" />
			<emp:select id="CusSameOrg.country" label="国别"  required="true" dictname="STD_GB_2659-2000" onchange="checkCountry()"/>
			<emp:text id="CusSameOrg.address" label="地址"  required="false" hidden="true"/>
			<emp:pop id="CusSameOrg.address_displayname" label="地址"  required="false" colSpan="2" cssElementClass="emp_field_cus_addr_input"
			url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" returnMethod="onReturnRegStateCode" hidden="true"/>
			<emp:text id="CusSameOrg.STREET" label="街道"  required="false" cssElementClass="emp_field_cus_addr_input" colSpan="2"/>
			<emp:date id="CusSameOrg.same_org_est" label="同业机构(行)成立日" required="true"  onblur="CheckDate(CusSameOrg.same_org_est)"/>
			<emp:text id="CusSameOrg.bank_pro_lic" label="金融业务许可证" maxlength="80" required="true" />
			<emp:text id="CusSameOrg.com_ins_no" label="营业执照号码" maxlength="80" required="true" />
			<emp:pop id="CusSameOrg.up_org_no" label="上级行号" required="false" url=""/>
			<emp:select id="CusSameOrg.reg_cur_type" label="注册/开办资金币种" required="true" dictname="STD_ZX_CUR_TYPE"/>
			<emp:text id="CusSameOrg.reg_cap_amt" label="注册/开办资金(万元)" maxlength="18" required="true" dataType="Currency"/>
			<emp:text id="CusSameOrg.paid_cap_amt" label="实际到位资金(万元)" maxlength="18" required="true" dataType="Currency"/>
			<emp:text id="CusSameOrg.assets" label="总资产" maxlength="18" required="true" dataType="Currency"/>
			<emp:select id="CusSameOrg.crd_grade" label="信用等级" required="true" dictname="STD_ZB_CREDIT_GRADE"/>
			<emp:select id="CusSameOrg.cust_level" label="监管评级" required="true" dictname="STD_ZB_CUSTD_RATE"/>
			<emp:date id="CusSameOrg.eval_maturity" label="评级到期日期" required="true" onblur="CheckExpDate(CusSameOrg.eval_maturity)"/>
			<emp:select id="CusSameOrg.mrk_flag" label="上市标志" required="false" dictname="STD_ZX_YES_NO" onchange="checkMrk()"/>
			<emp:select id="CusSameOrg.mrl_area" label="上市地" required="false" dictname="STD_ZX_LISTED"/>
			<emp:text id="CusSameOrg.stock_no" label="股票代码" maxlength="32" required="false" />
			<emp:text id="CusSameOrg.linkman_name" label="主联系人姓名" maxlength="40" required="true" />
			<emp:select id="CusSameOrg.linkman_duty" label="主联系人职务" required="true" dictname="STD_ZB_MANAGER_TYPE"/>
			<emp:text id="CusSameOrg.linkman_phone" label="主联系人电话" maxlength="20" required="true" dataType="Phone"/>
			<emp:text id="CusSameOrg.linkman_mobile_no" label="主联系人手机号" maxlength="20" required="true" dataType="Phone"/>
			<emp:text id="CusSameOrg.linkman_email" label="电子邮箱" maxlength="80" required="true" dataType="Email"/>
			<emp:text id="CusSameOrg.linkman_fax" label="传真" maxlength="35" required="true" dataType="Phone"/>
			<emp:select id="CusSameOrg.rel_dgr" label="与我行合作关系" required="true" dictname="STD_ZB_CUS_BANK_CO"/>
		</emp:gridLayout>	
		<emp:gridLayout id="CusSameOrgGroup2" title="登记信息" maxColumn="2">	
			<emp:text id="CusSameOrg.input_id_displayname" label="登记人" required="false"  hidden="false" readonly="true"/>
			<emp:text id="CusSameOrg.input_br_id_displayname" label="登记机构" required="false" hidden="false" readonly="true"/>
			<emp:pop id="CusSameOrg.main_mgr_displayname" label="主管客户经理" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="CusSameOrg.main_br_id_displayname" label="主管机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID"/>
			<emp:text id="CusSameOrg.input_date" label="登记日期" maxlength="10" required="true"  readonly="true"/>
			<emp:text id="CusSameOrg.input_id" label="登记人" maxlength="20" required="false"  hidden="true" readonly="true"/>
			<emp:text id="CusSameOrg.input_br_id" label="登记机构" maxlength="20" required="false"  hidden="true"/>
			<emp:pop id="CusSameOrg.main_mgr" label="主管客户经理" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" hidden="true"/>
			<emp:pop id="CusSameOrg.main_br_id" label="主管机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" hidden="true"/>
		</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="关闭"/>
	</div>
</body>
</html>
</emp:page>
