<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>子表添加记录页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function returnDicNo(data){
		QryParam.param_dic_no._setValue(data[0]);
		QryParam.param_dic_no_displayname._setValue(data[1]);
	}					
	function changeCast(){
		QryParam.enname._setValue(QryParam.enname._getValue().toLowerCase());
	}

	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="addQryTempletQryParamRecord.do" method="POST">
		<emp:gridLayout id="QryParamGroup" title="查询条件参数配置表" maxColumn="2">
			<emp:text id="QryParam.temp_no" label="查询模板编号" maxlength="20" required="true" readonly="true" />
			<emp:text id="QryParam.param_no" label="条件参数编号" maxlength="20" required="true"  hidden="true" defvalue="系统自动生成"/>
			<emp:text id="QryParam.cnname" label="参数中文名称" maxlength="40" required="true" />
			<emp:text id="QryParam.enname" label="参数英文名称" maxlength="40" required="true" onblur="changeCast()"/>
			<emp:select id="QryParam.param_type" label="条件参数类型" required="true" dictname="STD_ZB_PARAM_TYPE" />			
			<emp:pop id="QryParam.param_dic_no_displayname" label="参数选项字典编号" required="false" url="getQryParamPopPage.do" returnMethod="returnDicNo"/>
			<emp:text id="QryParam.orderid" label="排序字段" maxlength="20" required="false"  dataType="Int"/>
			<emp:text id="QryParam.param_dic_no" label="参数选项字典编号" required="false" hidden="true"/>
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
