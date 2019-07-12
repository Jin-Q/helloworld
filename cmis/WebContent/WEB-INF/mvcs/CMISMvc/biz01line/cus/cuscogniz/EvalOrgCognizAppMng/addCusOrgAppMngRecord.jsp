<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addCusOrgAppMngRecord.do" method="POST">
		
		<emp:gridLayout id="CusOrgAppMngGroup" title="评估机构申请管理" maxColumn="2">
			<emp:text id="CusOrgAppMng.cus_id" label="客户码" maxlength="30" required="true" />
			<emp:text id="CusOrgAppMng.cus_name" label="客户名称" maxlength="60" required="true" />
			<emp:text id="CusOrgAppMng.extr_eval_org" label="评估机构组织机构代码" maxlength="30" required="false" />
			<emp:select id="CusOrgAppMng.extr_eval_quali" label="评估机构资质等级" required="false" dictname="STD_ZB_EXTR_EVAL_QUALI" />
			<emp:text id="CusOrgAppMng.extr_eval_addr" label="评估机构地址" maxlength="80" required="false" />
			<emp:text id="CusOrgAppMng.extr_eval_rng" label="评估范围" maxlength="80" required="false" />
			<emp:text id="CusOrgAppMng.fic_per" label="法定代表人" maxlength="60" required="false" />
			<emp:text id="CusOrgAppMng.real_oper_per" label="实际经营人" maxlength="60" required="false" />
			<emp:date id="CusOrgAppMng.founded_date" label="成立日期" required="false" />
			<emp:text id="CusOrgAppMng.reg_cap_amt" label="注册资金" maxlength="16" required="false" />
			<emp:text id="CusOrgAppMng.tel" label="联系电话" maxlength="35" required="false" />
			<emp:select id="CusOrgAppMng.extr_eval_exp_type" label="评估机构有效期类型" required="false" dictname="STD_ZB_TERM_TYPE" />
			<emp:text id="CusOrgAppMng.extr_eval_exp_term" label="评估机构有效期期限" maxlength="3" required="false" />
			<emp:text id="CusOrgAppMng.realty_validation_lice_no" label="房地产价格评估资质证书号" maxlength="40" required="false" />
			<emp:text id="CusOrgAppMng.realty_validation_lice_name" label="房地产价格评估资质证书名称" maxlength="60" required="false" />
			<emp:text id="CusOrgAppMng.land_extr_eval_reg_lice_no" label="土地评估中介机构注册证书号" maxlength="40" required="false" />
			<emp:text id="CusOrgAppMng.land_extr_eval_reg_lice_name" label="土地评估中介机构注册证书名称" maxlength="60" required="false" />
			<emp:text id="CusOrgAppMng.cap_eval_quali_lice_no" label="资产评估资格证书号" maxlength="40" required="false" />
			<emp:text id="CusOrgAppMng.cap_eval_quali_lice_name" label="资产评估资格证书名称" maxlength="60" required="false" />
			<emp:text id="CusOrgAppMng.reg_realty_estimater" label="注册房地产估价师" maxlength="60" required="false" />
			<emp:text id="CusOrgAppMng.reg_land_estimater" label="注册土地估价师" maxlength="60" required="false" />
			<emp:text id="CusOrgAppMng.cap_realty_estimater" label="注册资产评估师" maxlength="60" required="false" />
			<emp:text id="CusOrgAppMng.remarks" label="备注" maxlength="250" required="false" />
			<emp:text id="CusOrgAppMng.manager_br_id" label="管理机构" maxlength="20" required="true" />
			<emp:text id="CusOrgAppMng.manager_id" label="责任人" maxlength="20" required="true" />
			<emp:text id="CusOrgAppMng.input_id" label="登记人" maxlength="20" required="true" />
			<emp:text id="CusOrgAppMng.input_date" label="登记日期" maxlength="10" required="true" />
			<emp:text id="CusOrgAppMng.input_br_id" label="登记机构" maxlength="20" required="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

