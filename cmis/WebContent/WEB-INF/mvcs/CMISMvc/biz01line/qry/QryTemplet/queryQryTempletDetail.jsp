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
		var url = '<emp:url action="queryQryTempletList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function doViewQryParam() {
		var paramStr = QryTemplet.QryParam._obj.getParamStr(['temp_no','param_no']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryQryTempletQryParamDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doViewQryResult() {
		var paramStr = QryTemplet.QryResult._obj.getParamStr(['temp_no','result_no']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryQryTempletQryResultDetail.do"/>?'+paramStr;
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
	<emp:gridLayout id="QryTempletGroup" title="查询信息配置模板" maxColumn="2">
			<emp:text id="QryTemplet.temp_no" label="查询模板编号" maxlength="20" required="true" readonly="true"/>
			<emp:text id="QryTemplet.temp_name" label="查询名称" maxlength="60" required="false" />
			<emp:pop id="QryTemplet.organlevel" label="适用机构等级" required="false" url="popOrgLevel.do" popParam="height=300, width=600, top=0, left=50, toolbar=yes, menubar=yes, scrollbars=yes, resizable=no, location=no, status=yes"/>
			<emp:select id="QryTemplet.templet_type" label="查询类型" required="false" dictname="STD_ZB_TEMPLET_TYPE" readonly="true"/>
			<emp:select id="QryTemplet.temp_pattern" label="查询模式" required="false" dictname="STD_ZB_TEMP_PATTERN"/>
			<emp:text id="QryTemplet.classpath" label="扩展类路径" maxlength="250" required="false" />
			<emp:select id="QryTemplet.temp_enable" label="是否启用" required="false" dictname="STD_ZX_YES_NO"/>
			<emp:textarea id="QryTemplet.query_sql" label="查询SQL语句" maxlength="4000" required="false" colSpan="2" />
			<emp:text id="QryTemplet.jsp_file_name" label="查询页面文件夹名" maxlength="250" required="false" />
			<emp:text id="QryTemplet.order_id" label="排序字段" maxlength="20" required="false" dataType="Long" />
			</emp:gridLayout>
	
	<br>

	<emp:tabGroup id="QryTemplet_tabs" mainTab="QryParam_tab">
		<emp:tab id="QryParam_tab" label="查询条件参数配置表">
			<div align="left">
				<emp:button id="viewQryParam" label="查看" op="view_QryParam"/>
			</div>
			<emp:table icollName="QryTemplet.QryParam" pageMode="false" url="">
		<emp:text id="temp_no" label="查询模板编号" />
		<emp:text id="param_no" label="条件参数编号" />
		<emp:text id="cnname" label="参数中文名称" />
		<emp:text id="enname" label="参数英文名称" />
		<emp:text id="param_type" label="条件参数类型" dictname="STD_ZB_PARAM_TYPE" />
		<emp:text id="param_dic_no" label="参数选项字典编号" />
		<emp:text id="orderid" label="排序字段" />
			</emp:table>
		</emp:tab>
		<emp:tab id="QryResult_tab" label="查询返回值配置表">
			<div align="left">
				<emp:button id="viewQryResult" label="查看" op="view_QryResult"/>
			</div>
			<emp:table icollName="QryTemplet.QryResult" pageMode="false" url="">
		<emp:text id="temp_no" label="查询模板编号" />
		<emp:text id="result_no" label="返回值编号" />
		<emp:text id="cnname" label="返回值标题名称" />
		<emp:text id="enname" label="列名称" />
		<emp:text id="enname2" label="别名" />
		<emp:text id="result_type" label="返回值类型" dictname="STD_ZB_QRYREST_TYPE" />
		<emp:text id="result_title" label="返回值数据字典号" />
		<emp:text id="orderid" label="排序字段" />
			</emp:table>
		</emp:tab>
	</emp:tabGroup>

	<div align=center>
		<emp:button id="return" label="返回到列表页面"/>
	</div>

</body>
</html>
</emp:page>
