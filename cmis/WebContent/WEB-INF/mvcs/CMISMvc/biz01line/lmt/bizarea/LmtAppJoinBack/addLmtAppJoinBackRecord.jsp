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
	
	<emp:form id="submitForm" action="addLmtAppJoinBackRecord.do" method="POST">
		
		<emp:gridLayout id="LmtAppJoinBackGroup" title="入圈/退圈申请表" maxColumn="2">
			<emp:text id="LmtAppJoinBack.serno" label="业务编号" maxlength="40" required="true" />
			<emp:text id="LmtAppJoinBack.biz_area_no" label="圈商编号" maxlength="40" required="true" />
			<emp:select id="LmtAppJoinBack.app_flag" label="申请标识" required="false" dictname="STD_LMT_APP_FLAG" />
			<emp:text id="LmtAppJoinBack.biz_area_name" label="圈商名称" maxlength="40" required="false" />
			<emp:select id="LmtAppJoinBack.biz_area_type" label="圈商类型" required="false" dictname="STD_LMT_BIZ_AREA_TYPE" />
			<emp:select id="LmtAppJoinBack.share_range" label="共享范围" required="false" dictname="STD_SHARED_SCOPE" />
			<emp:pop id="LmtAppJoinBack.belg_org" label="所属机构" url="null" required="false" />
			<emp:text id="LmtAppJoinBack.guar_type" label="授信合作担保方式" maxlength="10" required="false" />
			<emp:select id="LmtAppJoinBack.cur_type" label="币种" required="false" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="LmtAppJoinBack.lmt_totl_amt" label="授信总额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="LmtAppJoinBack.single_max_amt" label="单户限额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="LmtAppJoinBack.start_date" label="授信起始日期" maxlength="10" required="false" dataType="Date" />
			<emp:text id="LmtAppJoinBack.end_date" label="授信到期日期" maxlength="10" required="false" dataType="Date" />
			<emp:text id="LmtAppJoinBack.manager_id" label="责任人" maxlength="20" required="false" />
			<emp:text id="LmtAppJoinBack.manager_br_id" label="责任机构" maxlength="20" required="false" />
			<emp:text id="LmtAppJoinBack.input_id" label="登记人" maxlength="20" required="false" />
			<emp:text id="LmtAppJoinBack.input_br_id" label="登记机构" maxlength="20" required="false" />
			<emp:text id="LmtAppJoinBack.input_date" label="登记日期" maxlength="10" required="false" dataType="Date" />
			<emp:select id="LmtAppJoinBack.approve_status" label="申请状态" required="false" dictname="WF_APP_STATUS" />
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

