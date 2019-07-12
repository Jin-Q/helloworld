<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>
<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input2 {
border: 1px solid #b7b7b7;
text-align:left;
width:350px;
border-color: #b7b7b7;
background-color: #e3e3e3;
}
</style>
<%
	String serno = request.getParameter("serno");
%>
<script type="text/javascript">

	/*--user code begin--*/
	function doSub(){
		if(IqpOverseeManager._checkAll()){
			var form = document.getElementById("submitForm");
			IqpOverseeManager._toForm(form);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					var jsonstr = eval("("+o.responseText+")");
					var flag = jsonstr.flag;
					if(flag == "success"){
                            alert("保存成功！");
                            window.close();
                            window.opener.location.reload();
					}else {
						alert("发生异常！");
					}
				}
			};
			var callback = {
				success:handleSuccess,
				failure:null
			};
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
		}
	};

	function doClose(){
		window.close();
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addIqpOverseeManagerRecord.do" method="POST">		
	<div align="center">
		<emp:gridLayout id="IqpOverseeManagerGroup" title="主要管理人员" maxColumn="1">		
			<emp:text id="IqpOverseeManager.name" label="姓名" maxlength="32" required="true" />
			<emp:select id="IqpOverseeManager.edu" label="学历" required="true" dictname="STD_ZX_EDU"  cssElementClass="emp_field_text_input2"/>
			<emp:select id="IqpOverseeManager.duty" label="职务" required="true" dictname="STD_ZX_DUTY" cssElementClass="emp_field_text_input2"/>		
			<emp:text id="IqpOverseeManager.term" label="本公司工作（从业）年限" maxlength="10" required="true" dataType="Int"/>
			<emp:text id="IqpOverseeManager.serno" label="业务流水号" maxlength="32" hidden="true" defvalue="<%=serno%>"/>
			<emp:text id="IqpOverseeManager.manager_id" label="主要管理人员信息编号" maxlength="32" required="false" hidden="true" />
		</emp:gridLayout>			
			<br>
			<emp:button id="sub" label="保存" op="add"/>
			<emp:button id="close" label="关闭"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

