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
		var url = '<emp:url action="queryPrdPolcySchemeList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:tabGroup mainTab="base_tab" id="main_tabs">
			<emp:tab label="政策资料方案配置" id="base_tab" initial="true" >
				<emp:gridLayout id="PrdPolcySchemeGroup" maxColumn="2" title="政策资料设置方案">
					<emp:text id="PrdPolcyScheme.schemeid" label="方案编号" maxlength="30" required="true" readonly="true" />
					<emp:text id="PrdPolcyScheme.schemename" label="方案名称" maxlength="40" required="false" readonly="true" />
					<emp:select id="PrdPolcyScheme.effectived" label="是否启用" dictname="STD_ZX_YES_NO" required="true" />
					<emp:textarea id="PrdPolcyScheme.comments" label="备注" colSpan="2"  maxlength="200" required="false" />
					<emp:text id="PrdPolcyScheme.inputid" label="登记人员" maxlength="20" required="false" readonly="true" />
					<emp:text id="PrdPolcyScheme.inputdate" label="登记日期" maxlength="10" required="false" readonly="true" />
					<emp:text id="PrdPolcyScheme.orgid" label="登记机构" maxlength="20" required="false" readonly="true" />
				</emp:gridLayout>
			</emp:tab>
			<emp:tab label="政策资料方案关联场景" id="space_tab" url="getSpaceApplyListBySchemeId.do?schemeId=$PrdPolcyScheme.schemeid;" needFlush="true" initial="false">
			
			</emp:tab>
		</emp:tabGroup>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
