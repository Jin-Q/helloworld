<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>子表添加记录页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
		function doload(){
			 //IndModelGroup.group_no._obj.config.url=IndModelGroup.group_no._obj.config.url+"&returnMethod=returnGroupNo";
		 }
		 function returnGroupNo(data){
			 IndModelGroup.group_no._setValue(data.group_no._getValue()); 
			 IndModelGroup.group_name._setValue(data.group_name._getValue()); 
	       }	
		 function doReturnList(){ 
			   window.close();
			}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="addIndModelIndModelGroupRecord.do" method="POST">
		<emp:gridLayout id="IndModelGroupGroup" title="模型指标组关联设置" maxColumn="2">
			<emp:text id="IndModelGroup.model_no" label="模型编号" maxlength="12" required="true" readonly="true" />
			<emp:pop id="IndModelGroup.group_no" label="组别编号"  required="true" url="queryIndGroupPopList.do" returnMethod="returnGroupNo"/>
			<emp:text id="IndModelGroup.group_name" label="组别名称" maxlength="60" required="true" readonly="true"/>
			<emp:text id="IndModelGroup.weight" label="权重" maxlength="9" defvalue="1" hidden="true" required="false" />
			<emp:text id="IndModelGroup.seqno" label="顺序号" maxlength="38" required="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="保存" />
			<emp:button id="returnList" label="关闭"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
