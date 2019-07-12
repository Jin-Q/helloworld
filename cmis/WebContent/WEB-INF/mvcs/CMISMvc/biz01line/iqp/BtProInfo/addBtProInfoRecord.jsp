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
	
	<emp:form id="submitForm" action="addBtProInfoRecord.do" method="POST">
		
		<emp:gridLayout id="BtProInfoGroup" title="项目信息" maxColumn="2">
			<emp:text id="BtProInfo.serno" label="业务编号" maxlength="40" required="true" />
			<emp:select id="BtProInfo.pro_cls" label="项目类别" required="true" />
			<emp:text id="BtProInfo.pro_name" label="项目名称" maxlength="80" required="false" />
			<emp:text id="BtProInfo.pro_addr" label="项目地点" maxlength="100" required="false" />
			<emp:text id="BtProInfo.pro_occup_squ" label="项目占地面积" maxlength="100" required="false" />
			<emp:text id="BtProInfo.pro_arch_squ" label="项目建筑面积" maxlength="100" required="false" />
			<emp:text id="BtProInfo.approve_gover_dept" label="批项政府部门" maxlength="80" required="false" />
			<emp:text id="BtProInfo.pro_approve_no" label="项目批准文号" maxlength="80" required="false" />
			<emp:text id="BtProInfo.govlanduser_no" label="国有土地使用证编号" maxlength="80" required="false" />
			<emp:text id="BtProInfo.landuser_lic" label="建设用地规划许可证号" maxlength="80" required="false" />
			<emp:text id="BtProInfo.pro_tolinves" label="项目总投资" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="BtProInfo.get_capital" label="到位资本金" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="BtProInfo.get_capital_rate" label="到位资本金比例" maxlength="10" required="false" dataType="Percent" />
			<emp:text id="BtProInfo.construct_lic" label="施工许可证" maxlength="80" required="false" />
			<emp:text id="BtProInfo.pro_main_term" label="项目经营期限" maxlength="38" required="false" />
			<emp:text id="BtProInfo.construct_name" label="施工企业名称" maxlength="80" required="false" />
			<emp:textarea id="BtProInfo.pro_memo" label="项目情况说明" maxlength="250" required="false" colSpan="2" />
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

