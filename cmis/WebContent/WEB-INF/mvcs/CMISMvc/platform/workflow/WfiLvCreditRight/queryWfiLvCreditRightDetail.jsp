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

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryWfiLvCreditRightList.do"/>?right_type=01';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
		
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="WfiLvCreditRightGroup" title="授权等级授信审批权限配置" maxColumn="2">
			<emp:select id="WfiLvCreditRight.org_lvl" label="机构等级" dictname="STD_ZB_ORG_LVL" required="true"/>
			<emp:select id="WfiLvCreditRight.right_type" label="权限类型" dictname="STD_ZB_RIGHT_TYPE" readonly="true" defvalue="01"/>
			<emp:select id="WfiLvCreditRight.belg_line" label="机构条线" dictname="STD_ZB_BUSILINE" required="true" />
			<emp:select id="WfiLvCreditRight.assure_main" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" required="true" />
			<emp:text id="WfiLvCreditRight.new_crd_amt" label="新增授信审批金额（万元）" maxlength="16" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="WfiLvCreditRight.stock_crd_amt" label="存量授信审批金额（万元）" maxlength="16" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="WfiLvCreditRight.pk_id" label="主键" maxlength="36" readonly="true" hidden="true"/>
		</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
