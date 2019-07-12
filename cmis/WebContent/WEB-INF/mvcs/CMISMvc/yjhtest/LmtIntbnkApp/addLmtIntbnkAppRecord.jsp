<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>鏂板椤甸潰</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addLmtIntbnkAppRecord.do" method="POST">
		<emp:gridLayout id="LmtIntbnkAppGroup" title="信息1" maxColumn="2">
			<emp:text id="LmtIntbnkApp.serno" label="流水号" maxlength="40" hidden="true"/>
			<emp:text id="LmtIntbnkApp.bank_no" label="同业机构行号" maxlength="30" required="false" />
			<emp:text id="LmtIntbnkApp.bank_name" label="同业机构名称" maxlength="60" required="false" />
			<emp:text id="LmtIntbnkApp.bank_type" label="同业机构类型" maxlength="1" required="false" />
			<emp:text id="LmtIntbnkApp.crd_biz_type" label="授信业务类型" maxlength="2" required="false" />
			<emp:text id="LmtIntbnkApp.crd_lmt_type" label="授信额度类型" maxlength="2" required="false" />
		</emp:gridLayout>
		<emp:gridLayout id="LmtIntbnkAppGroup" title="信息2" maxColumn="2">
			<emp:text id="LmtIntbnkApp.auto_score" label="机评得分" maxlength="3" required="false" />
			<emp:text id="LmtIntbnkApp.auto_grade" label="机评等级" maxlength="10" required="false" />
			<emp:text id="LmtIntbnkApp.crd_lmt_amt" label="授信限额" maxlength="16" required="false" />
			<emp:text id="LmtIntbnkApp.crd_totl_amt" label="授信总额" maxlength="16" required="false" />
			<emp:text id="LmtIntbnkApp.approve_status" label="审批状态" maxlength="3" required="false" />
		</emp:gridLayout>
		<emp:gridLayout id="LmtIntbnkAppGroup" title="信息3" maxColumn="2">
			<emp:text id="LmtIntbnkApp.cur_type" label="币种" maxlength="3" required="false" />
			<emp:text id="LmtIntbnkApp.term_type" label="期限类型" maxlength="3" required="false" />
			<emp:text id="LmtIntbnkApp.term" label="期限" maxlength="38" required="false" />
			<emp:text id="LmtIntbnkApp.apply_date" label="申请日期" maxlength="10" required="false" />
			<emp:text id="LmtIntbnkApp.input_date" label="登记日期" maxlength="10" required="false" />
			<emp:text id="LmtIntbnkApp.update_date" label="修改日期" maxlength="10" required="false" />
			<emp:text id="LmtIntbnkApp.input_id" label="申请人" maxlength="20" required="false" />
		</emp:gridLayout>
		<emp:gridLayout id="LmtIntbnkAppGroup" title="信息4" maxColumn="2">
			<emp:text id="LmtIntbnkApp.cus_manager" label="客户经理" maxlength="20"  hidden="true" required="false" defvalue="${context.loginuserid}"/>
			<emp:text id="LmtIntbnkApp.cus_manager_displayname" label="客户经理" maxlength="20" readonly="true" required="false" defvalue="${context.loginusername}"/>
			<emp:text id="LmtIntbnkApp.input_br_id" label="申请机构" maxlength="20" readonly="true" required="false" defvalue="${context.organNo}"/>
			<emp:text id="LmtIntbnkApp.mng_br_id" label="主管机构" maxlength="20" required="false" />
			<emp:text id="LmtIntbnkApp.remarks" label="备注" maxlength="250" required="false" />
			<emp:text id="LmtIntbnkApp.lmt_serno" label="授信协议编号" maxlength="40" required="false" />
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
