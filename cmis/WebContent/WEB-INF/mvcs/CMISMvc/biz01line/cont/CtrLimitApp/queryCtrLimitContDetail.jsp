<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
function doReturn(){
	var url = '<emp:url action="queryCtrLimitAppList.do"/>';
	url = EMPTools.encodeURI(url);
	window.location = url;
};
</script>
</head>
<body class="page_content">
	<emp:tabGroup mainTab="maintabs" id="maintabs">
		<emp:tab label="额度合同变更申请基本信息" id="maintabs">
			<emp:form id="submitForm" action="updateCtrLimitAppRecord.do" method="POST">
				<emp:gridLayout id="CtrLimitAppGroup" title="额度合同申请表" maxColumn="2">
					<emp:text id="CtrLimitApp.cont_no" label="合同编号" maxlength="40" required="true" readonly="true" />
					<emp:text id="CtrLimitApp.serno" label="业务编号" maxlength="40" required="false" readonly="true" hidden="true" />
					<emp:text id="CtrLimitApp.cont_cn" label="中文合同编号" maxlength="200" required="true" readonly="true"/>
					<emp:select id="CtrLimitApp.app_type" label="申请类型" required="false" dictname="STD_ZB_APP_TYPE" readonly="true"/>
					<emp:text id="CtrLimitApp.cus_id" label="客户码" maxlength="32" required="false" readonly="true"/>
					<emp:select id="CtrLimitApp.cur_type" label="币种"   dictname="STD_ZX_CUR_TYPE" required="true" readonly="true"/>
					<emp:text id="CtrLimitApp.app_amt" label="合同金额" maxlength="16" required="true" readonly="true"/>
					<emp:date id="CtrLimitApp.start_date" label="起始日期" required="true" readonly="true"/>
					<emp:date id="CtrLimitApp.end_date" label="到期日期"  required="true" readonly="true"/>
					<emp:text id="CtrLimitApp.cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE" hidden="true" defvalue="200" required="false" />
					<emp:textarea id="CtrLimitApp.memo" label="备注" maxlength="200" required="false" readonly="true"/>
				</emp:gridLayout>
				
				<emp:gridLayout id="CtrLimitAppGroup" title="机构信息" maxColumn="2">
					<emp:pop id="CtrLimitApp.manager_br_id_displayname" label="管理机构" defvalue="${context.organNo}" required="true" readonly="false" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" />
					<emp:text id="CtrLimitApp.input_id_displayname" label="登记人"  defvalue="${context.currentUserId}" required="false" readonly="true"/>
					<emp:text id="CtrLimitApp.input_br_id_displayname" label="登记机构" defvalue="${context.organNo}"  required="false" readonly="true"/>
					<emp:text id="CtrLimitApp.input_date" label="登记日期" maxlength="10" required="false" defvalue="${context.OPENDAY}" readonly="true"/>
					<emp:text id="CtrLimitApp.manager_br_id" label="管理机构" maxlength="20" hidden="true" required="false" />
					<emp:text id="CtrLimitApp.input_id" label="登记人" maxlength="32" required="false" hidden="true" />
					<emp:text id="CtrLimitApp.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" />
				</emp:gridLayout>
				
				<div align="center">
					<br>
					<emp:button id="return" label="返回列表"/>
				</div>
			</emp:form>
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
</body>
</html>
</emp:page>
