<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doReturn(){
		window.location= '<emp:url action="queryQryParamDicList.do"/>';
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateQryParamDicRecord.do" method="POST">
		<emp:gridLayout id="QryParamDicGroup" maxColumn="2" title="查询条件字典配置表">
			<emp:text id="QryParamDic.param_dic_no" label="参数选项字典编号" maxlength="20" required="true" readonly="true" />
			<emp:text id="QryParamDic.name" label="字典名称" maxlength="30" required="true" />
			<emp:select id="QryParamDic.par_dic_type" label="参数类型" required="true" dictname="STD_ZB_PAR_DIC_TYPE" />
			<emp:text id="QryParamDic.opttype" label="参数选项字典" maxlength="20" required="false" />
			<emp:textarea id="QryParamDic.query_sql" label="查询SQL语句" maxlength="250" required="false" colSpan="2" hidden="true"/>
			<emp:textarea id="QryParamDic.popname" label="POP框信息" maxlength="250" required="false" colSpan="2"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="修改" op="update"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
