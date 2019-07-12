<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
 	function doQuery(){
		var form = document.getElementById('queryForm');
		PspRejectBatchLog._toForm(form);
		PspRejectBatchLogList._obj.ajaxQuery(null,form);
};
	
	function doReset(){
		page.dataGroups.PspRejectBatchLogGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">

	<div align="left">
	</div>
	<emp:table icollName="PspRejectBatchLogList" pageMode="true" url="pagePspRejectBatchLogQuery.do">
		<emp:text id="task_log_id" label="log编码" hidden="true"/>
		<emp:text id="task_id" label="任务编号" />
		<emp:text id="input_id" label="操作人" />
		<emp:select id="approve_status_ori" label="审批原状态" dictname="WF_APP_STATUS"/>
		<emp:text id="update_type" label="操作类型（否决、启用）" />
		<emp:text id="input_time" label="修改时间" /> 
	</emp:table>
	
</body>
</html>
</emp:page>
    