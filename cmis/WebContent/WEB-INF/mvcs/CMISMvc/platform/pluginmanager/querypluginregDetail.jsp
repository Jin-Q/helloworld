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
		var url = '<emp:url action="querypluginregList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="pluginregGroup" title="插件注册表" maxColumn="2">
			<emp:text id="pluginreg.plugin_modual_id" label="插件ID" maxlength="32" required="true" />
			<emp:text id="pluginreg.plugin_modual_name" label="插件名称" maxlength="32" required="true" />
			<emp:text id="pluginreg.resource_path" label="工程文件相对路径" maxlength="200" required="false" />
			<emp:text id="pluginreg.db_path" label="数据库文件相对路径" maxlength="200" required="false" />
			<emp:text id="pluginreg.res_java_path" label="JAVA文件相对路径" maxlength="200" required="false" />
			<emp:text id="pluginreg.res_jsp_path" label="JSP文件相对路径" maxlength="200" required="false" />
			<emp:text id="pluginreg.res_table_path" label="TABLE文件相对路径" maxlength="200" required="false" />
			<emp:text id="pluginreg.res_action_path" label="ACTION文件相对路径" maxlength="200" required="false" />
			<emp:text id="pluginreg.db_uninstall_sql" label="数据卸载文件" maxlength="200" required="false" />
			<emp:text id="pluginreg.install_date" label="安装时间" maxlength="10" required="false" />
			<emp:text id="pluginreg.plugin_version" label="插件版本" maxlength="20" required="false" />
			<emp:text id="pluginreg.plugin_status" label="插件状态" dictname="PLUGIN_STATUS" maxlength="3" required="false" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
