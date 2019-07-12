<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryIndGroupList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function doViewIndGroupIndex() {
		var paramStr = IndGroup.IndGroupIndex._obj.getParamStr(['group_no','index_no']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryIndGroupIndGroupIndexDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		} else {
			alert('请先选择一条记录！');
		}
	};

	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:gridLayout id="IndGroupGroup" title="指标组别信息" maxColumn="2">
			<emp:text id="IndGroup.group_no" label="组别编号" maxlength="12" required="true" readonly="true" />
			<emp:text id="IndGroup.group_name" label="组别名称" maxlength="60" required="false" />
			<emp:select id="IndGroup.group_kind" label="组性质" hidden="true" dictname="STD_ZB_GROUP_PROP" />
			<emp:textarea id="IndGroup.rating_rules" label="组评分规则" maxlength="100" required="false" />
			<emp:textarea id="IndGroup.rating_rules_displayname" label="组评分规则描述" readonly="true" colSpan="2"/>
			<emp:text id="IndGroup.sup_group_no" label="上级组编号" maxlength="8" hidden="true" />
			<emp:text id="IndGroup.trans_id" label="规则交易" maxlength="32" required="true" />
			<emp:text id="IndGroup.trans_id_displayname" label="规则交易名称" required="true" />
			<emp:textarea id="IndGroup.memo" label="备注" maxlength="100" required="false" colSpan="2"/>
	</emp:gridLayout>

	<div align=center>
		<emp:button id="return" label="返回到列表页面"/>
	</div>

</body>
</html>
</emp:page>
