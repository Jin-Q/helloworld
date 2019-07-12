<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
 	 	
			function doReturnList(){ 
				var url="<emp:url action='queryIndLibList.do'></emp:url>"; 
				window.location=url; 
				}
			function returnIndexNo(data){
				IndLib.par_index_no._setValue(data.index_no._getValue()); 
		       }
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" >
	
	<emp:form id="submitForm" action="addIndLibRecord.do" method="POST">
		<emp:gridLayout id="IndLibGroup" title="指标库信息" maxColumn="2">
			<emp:text id="IndLib.index_no" label="指标编号" maxlength="12" required="true" />
			<emp:text id="IndLib.index_name" label="指标名称" maxlength="60" required="true" />
			<emp:text id="IndLib.fnc_index_rpt" label="财务指标编号" maxlength="16" />
			
			<emp:pop id="IndLib.par_index_no" label="上级指标编号"  required="false" url="queryIndLibPopList.do" returnMethod="returnIndexNo"/>
			<emp:select id="IndLib.index_property" label="指标性质"  required="true" dictname="STD_ZB_PARA_PROP" defvalue="2"/>
			<emp:select id="IndLib.index_type" label="指标类别"  required="true" dictname="STD_ZB_IND_TYPE" defvalue="1"/>
			<emp:select id="IndLib.input_type" label="指标取值方式"  required="true" dictname="STD_ZB_PARA_VAL_TYP" defvalue="1"/>
			<emp:textarea id="IndLib.input_classpath" label="指标取值实现类" required="false" defvalue="" colSpan="2"/>
			<emp:select id="IndLib.exe_cycle" label="执行周期"  required="false" dictname="STD_ZB_RUN_FREQ"/>
			<emp:select id="IndLib.index_level" label="指标级别"  required="false" dictname="STD_ZB_PARA_LEVEL" defvalue="0"/>
		</emp:gridLayout>

		<div align="center">
			<br>
			<emp:button id="submit" label="保存" op="add"/>
			<emp:button id="returnList" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
