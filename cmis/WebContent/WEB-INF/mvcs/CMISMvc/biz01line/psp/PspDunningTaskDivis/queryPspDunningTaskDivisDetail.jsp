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
		var url = '<emp:url action="queryPspDunningTaskDivisList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doLoad(){
		PspDunningTaskDivis.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
	}

	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+PspDunningTaskDivis.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
		<emp:gridLayout id="PspDunningTaskDivisGroup" title="催收任务分配" maxColumn="2">
			<emp:text id="PspDunningTaskDivis.serno" label="业务编号" maxlength="40" required="false" hidden="true" />
			<emp:text id="PspDunningTaskDivis.cus_id" label="客户码" maxlength="40" readonly="true" required="true"/>
			<emp:text id="PspDunningTaskDivis.cus_id_displayname" label="客户名称"  required="true" readonly="true" cssElementClass="emp_field_text_readonly" colSpan="2"/>
			<emp:text id="PspDunningTaskDivis.acc_no" label="借据编号" maxlength="40" required="true" />
			<emp:text id="PspDunningTaskDivis.cont_no" label="合同编号" maxlength="40" required="true" />
			<emp:date id="PspDunningTaskDivis.task_create_date" label="任务生成日期" required="true" />
			<emp:date id="PspDunningTaskDivis.need_end_date" label="要求完成日期" required="true" />
			
			<emp:pop id="PspDunningTaskDivis.exe_id_displayname" label="任务执行人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setExeId" required="true" />
			<emp:pop id="PspDunningTaskDivis.exe_br_id_displayname" label="任务执行机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="setExeBrId" required="true" />
			<emp:text id="PspDunningTaskDivis.exe_id" label="任务执行人" maxlength="20" required="true" hidden="true"/>
			<emp:text id="PspDunningTaskDivis.exe_br_id" label="任务执行机构" maxlength="20" required="true" hidden="true"/>
			
			<emp:pop id="PspDunningTaskDivis.divis_id_displayname" label="任务分配人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setDivisId" required="true" />
			<emp:pop id="PspDunningTaskDivis.divis_br_id_displayname" label="任务分配机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="setDivisBrId" required="true" />
			<emp:text id="PspDunningTaskDivis.divis_id" label="任务分配人" maxlength="20" required="true" hidden="true"/>
			<emp:text id="PspDunningTaskDivis.divis_br_id" label="任务分配机构" maxlength="20" required="true" hidden="true"/>
			
			<emp:textarea id="PspDunningTaskDivis.memo" label="备注" maxlength="500" required="false" colSpan="2" />
			<emp:text id="PspDunningTaskDivis.input_id_displayname" label="登记人" required="true" defvalue="$currentUserName" readonly="true"/>
			<emp:text id="PspDunningTaskDivis.input_br_id_displayname" label="登记机构"  required="true" defvalue="$organName" readonly="true"/>
			<emp:text id="PspDunningTaskDivis.input_id" label="登记人" maxlength="20" required="true" defvalue="$currentUserId" hidden="true"/>
			<emp:text id="PspDunningTaskDivis.input_br_id" label="登记机构" maxlength="20" required="true" defvalue="$organNo" hidden="true"/>
			<emp:date id="PspDunningTaskDivis.input_date" label="登记日期" required="true" readonly="true" defvalue="$OPENDAY"/>
		</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
