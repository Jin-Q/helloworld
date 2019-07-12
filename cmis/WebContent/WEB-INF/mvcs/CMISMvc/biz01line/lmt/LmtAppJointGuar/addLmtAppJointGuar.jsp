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
	
	<emp:form id="submitForm" action="addLmtAppJointCoopRecord.do" method="POST">
		
		<emp:gridLayout id="LmtAppJointCoopGroup" title="授信联保合作方申请表" maxColumn="2">
			<emp:text id="LmtAppJointCoop.serno" label="业务编号" maxlength="40" required="true" />
			<emp:text id="LmtAppJointCoop.cus_id" label="客户码" maxlength="30" required="false" />
			<emp:select id="LmtAppJointCoop.coop_type" label="合作方类型    联保/合作方/..." required="false" dictname="STD_ZB_COOP_TYPE" />
			<emp:select id="LmtAppJointCoop.share_range" label="共享范围" required="false" dictname="STD_SHARED_SCOPE" />
			<emp:text id="LmtAppJointCoop.belg_org" label="所属机构" maxlength="200" required="false" />
			<emp:date id="LmtAppJointCoop.app_date" label="申请日期" required="false" />
			<emp:date id="LmtAppJointCoop.over_date" label="办结日期" required="false" />
			<emp:select id="LmtAppJointCoop.cur_type" label="币种" required="false" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="LmtAppJointCoop.lmt_totl_amt" label="授信总额" maxlength="16" required="false" />
			<emp:text id="LmtAppJointCoop.single_max_amt" label="单户限额" maxlength="16" required="false" />
			<emp:select id="LmtAppJointCoop.term_type" label="期限类型" required="false" dictname="STD_ZB_TERM_TYPE" />
			<emp:text id="LmtAppJointCoop.term" label="期限" maxlength="3" required="false" />
			<emp:text id="LmtAppJointCoop.manager_id" label="责任人" maxlength="40" required="false" />
			<emp:text id="LmtAppJointCoop.manager_br_id" label="责任人机构" maxlength="40" required="false" />
			<emp:text id="LmtAppJointCoop.input_id" label="登记人" maxlength="40" required="false" />
			<emp:text id="LmtAppJointCoop.input_br_id" label="登记机构" maxlength="40" required="false" />
			<emp:date id="LmtAppJointCoop.input_date" label="登记日期" required="false" />
			<emp:select id="LmtAppJointCoop.approve_status" label="申请状态" required="false" dictname="WF_APP_STATUS" />
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

