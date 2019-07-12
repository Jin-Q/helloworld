<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String task_id = request.getParameter("task_id");
	context.put("task_id", task_id);
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

function doDeferPspCheckTaskRecord() {
	 if(!PspCheckTask._checkAll()){
			return false;
	 }else{
		var form = document.getElementById("submitForm");
		PspCheckTask._toForm(form);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert(e.message);
					return;
				}
				var flag=jsonstr.flag;	
				if(flag=="success"){
					alert('修改成功！');
					window.opener.location.reload();
					doClose();
				}else if(flag =="wrongdate"){
					alert("任务要求完成时间不得早于任务生成时间，请修改！");
					window.location.reload();
				}else{
					alert('发生异常!');
				}
			}	
		};
		var handleFailure = function(o){ 
			alert("修改失败，请联系管理员！");
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		}; 
		var postData = YAHOO.util.Connect.setForm(form);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	 }
};

function doClose(){
 window.close();
};
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="deferPspCheckTaskRecord.do" method="POST">	
		<emp:gridLayout id="PspCheckTaskGroup" title="任务展期" maxColumn="2"> 	
			<emp:text id="PspCheckTask.task_id" label="任务号" maxlength="40" readonly="true" defvalue="${context.task_id}" colSpan="2" />
			<emp:date id="PspCheckTask.task_request_time" label="要求完成时间" required="true"/>
    	</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="deferPspCheckTaskRecord" label="确定"/>
			<emp:button id="close" label="关闭"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
    