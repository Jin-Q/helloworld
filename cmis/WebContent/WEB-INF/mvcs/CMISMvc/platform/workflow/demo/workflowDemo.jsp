<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.yucheng.cmis.base.CMISConstance"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
Date date = new Date();
SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
String serno = format.format(date);
%>
<emp:page>
<html>
<head>
<title>发起流程示例</title>
<jsp:include page="/include.jsp" flush="true"/>
<!-- 包含流程接入的公用jsp页面 -->
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script type="text/javascript">

	/*--user code begin--*/
	
	window.onload = function() {
		
	}
	
	function doSubmitWF() {
		doSave(startWorkFlow());
	}
	
	function startWorkFlow() {
		WfiJoin.table_name._setValue('WfiDemo');
		WfiJoin.pk_col._setValue('serno');
		WfiJoin.pk_value._setValue(WfiDemo.serno._getValue());
		WfiJoin.wfi_status._setValue(WfiDemo.approve_status._getValue());  //提交流程时，业务当前的审批状态
		WfiJoin.status_name._setValue('approve_status'); //缺省为approve_status
		WfiJoin.appl_type._setValue('111');   //通过申请类型发起流程
		//其他的表单元素可根据需求设置
		WfiJoin.cus_id._setValue(WfiDemo.cus_id._getValue());
		WfiJoin.cus_name._setValue(WfiDemo.cus_name._getValue());
		initWFSubmit(true);
	}
	
	function doSave(callFnc) {
		var form = document.getElementById('submitForm');
		WfiDemo._toForm(form);
		var url = form.action;
		url = EMPTools.encodeURI(url);
		var handleSuccess = function(o){
			if(o.responseText != undefined){
				try {
					var jsonstr = eval("("+o.responseText+")");
					var msg = jsonstr.msg;
					if(msg == '1') {
						alert('保存成功！');
					}
					if(callFnc!=undefined && callFnc!=null && callFnc!='') {
						eval(callFnc);
					}
				} catch(e) {
					alert(o.responseText);
					return;
				}
			}
		};
		var handleFailure = function(o){
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);	 
		var obj1 = YAHOO.util.Connect.asyncRequest('post',url, callback, postData);
	}
	
	/*--user code end--*/

</script>
</head>
<body class="page_content">

	<emp:form id="submitForm" action="addWfiDemo.do" method="POST">
		<emp:gridLayout id="WfiDemoGroup" title="发起流程示例" maxColumn="1">
			<emp:text id="WfiDemo.serno" label="申请流水号" required="true" defvalue="<%=serno %>" />
			<emp:text id="WfiDemo.cus_id" label="客户ID" required="true" defvalue="示例客户ID001"/>
			<emp:text id="WfiDemo.cus_name" label="客户名称" required="true" defvalue="示例客户名称001"/>
			<emp:text id="WfiDemo.approve_status" label="审批状态" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="save" label="保存"/>
			<emp:button id="submitWF" label="提交流程"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>