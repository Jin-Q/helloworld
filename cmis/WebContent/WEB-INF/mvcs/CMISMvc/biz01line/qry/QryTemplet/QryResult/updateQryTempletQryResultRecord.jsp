<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function remove(){
		QryResult.cnname._setValue(QryResult.cnname._getValue().replace(/\|/g, ""));
	}

	//选择字典返回方法
	function returnDicNo(data){
		QryResult.result_title._setValue(data[0]);
		QryResult.result_title_displayname._setValue(data[1]);
	}

	//内部链接POP
	function doSelectLink(data){
		QryResult.link_temp_no._setValue(data.temp_no._getValue());
		QryResult.link_temp_no_displayname._setValue(data.temp_name._getValue());
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="updateQryTempletQryResultRecord.do" method="POST">
		<emp:gridLayout id="QryResultGroup" title="查询返回值配置表" maxColumn="2">
			<emp:text id="QryResult.temp_no" label="查询模板编号" maxlength="20" required="true" readonly="true" />
			<emp:text id="QryResult.result_no" label="返回值编号" maxlength="20" required="true" readonly="true" />
			<emp:text id="QryResult.cnname" label="返回值标题名称" maxlength="60" required="true" onblur="remove()"/>
			<emp:text id="QryResult.enname" label="列名称" maxlength="60" required="true" />
			<emp:text id="QryResult.enname2" label="别名" maxlength="80" required="true" />
			<emp:select id="QryResult.result_type" label="返回值类型" required="true" dictname="STD_ZB_QRYREST_TYPE" />
			<emp:pop id="QryResult.result_title_displayname" label="参数选项字典编号" required="false" url="getQryParamPopPage.do" returnMethod="returnDicNo"/>
			<emp:pop id="QryResult.link_temp_no_displayname" label="内部链接" required="false" url="queryQryTempletListPop.do" returnMethod="doSelectLink"/>
			<emp:text id="QryResult.orderid" label="排序字段" maxlength="20" required="false" dataType="Int"/>
			<emp:text id="QryResult.result_title" label="参数选项字典编号" required="false" hidden="true"/>
			<emp:text id="QryResult.link_temp_no" label="内部链接" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="修改" op="update"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
