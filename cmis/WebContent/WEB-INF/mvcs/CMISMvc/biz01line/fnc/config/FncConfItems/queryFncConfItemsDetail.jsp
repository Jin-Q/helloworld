<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详细信息页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryFncConfItemsList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="FncConfItemsGroup" title="报表配置项目列表" maxColumn="2">
			<emp:text id="FncConfItems.item_id" label="项目编号" maxlength="9" required="true" />
			<emp:text id="FncConfItems.item_name" label="项目名称" maxlength="200" required="false" />
			<emp:select id="FncConfItems.fnc_conf_typ" label="所属报表种类" required="false" dictname="STD_ZB_FNC_TYP"/>
			<emp:select id="FncConfItems.fnc_no_flg" label="新旧报表标志" required="false" dictname="STD_ZB_FNC_ON_TYP"/>
			<emp:text id="FncConfItems.item_unit" label="单位" maxlength="60" colSpan="2"/>
			<emp:select id="FncConfItems.def_item_id" label="映射财报id" dictname="STD_ZB_FNC_CHG" colSpan="2" hidden="true" help="一套财报一个id这个id是对应的公共财报条目id"/>
			<emp:textarea id="FncConfItems.formula" label="指标公式" maxlength="500" colSpan="2"/>
			<emp:textarea id="FncConfItems.remark" label="备注"></emp:textarea>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回"/>
	</div>
</body>
</html>
</emp:page>
