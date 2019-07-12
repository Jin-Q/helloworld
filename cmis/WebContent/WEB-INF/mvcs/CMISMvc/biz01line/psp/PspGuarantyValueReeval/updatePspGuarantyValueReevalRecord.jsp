<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
	}
	if(op.equals("view")){
		request.setAttribute("canwrite","");
	}
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doReturn(){
		window.close();
	}

	function doSub(){
    	var form = document.getElementById("submitForm");
		PspGuarantyValueReeval._checkAll();
		if(PspGuarantyValueReeval._checkAll()){
			PspGuarantyValueReeval._toForm(form); 
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					if(flag == "success"){
						alert("保存成功!");
						window.close();
					}else {
						alert("保存异常!"); 
					}
				}
			};
			var handleFailure = function(o){
				alert("异步请求出错！");	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
		}else {
			return false;
		}
		
	};	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updatePspGuarantyValueReevalRecord.do" method="POST">
		<emp:gridLayout id="PspGuarantyValueReevalGroup" maxColumn="2" title="担保品价值重估">
			<emp:text id="PspGuarantyValueReeval.guaranty_no" label="担保品编号" maxlength="40" required="true" defvalue="${context.guaranty_no}" readonly="true"/>
			<emp:text id="PspGuarantyValueReeval.guaranty_name" label="担保品名称" maxlength="100" required="true" readonly="true"/>
			<emp:text id="PspGuarantyValueReeval.last_reeval_value" label="上期认定价值" maxlength="16" required="false" dataType="Currency" colSpan="2" readonly="true"/>
			<emp:text id="PspGuarantyValueReeval.batch_reeval_value" label="本期批量重估押品价值" maxlength="16" required="true" dataType="Currency"/>
			<emp:text id="PspGuarantyValueReeval.reeval_value" label="本期建议押品价值" maxlength="16" required="true" dataType="Currency"/>
			
			<emp:text id="PspGuarantyValueReeval.input_id" label="登记人" maxlength="40" required="false" hidden="true" defvalue="${context.currentUserId}"/>
			<emp:text id="PspGuarantyValueReeval.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="${context.organNo}"/>
			<emp:text id="PspGuarantyValueReeval.input_date" label="登记日期" maxlength="10" required="false" hidden="true" defvalue="${context.OPENDAY}"/>
			
			<emp:text id="PspGuarantyValueReeval.pk_id" label="主键" maxlength="32" required="false" readonly="true" hidden="true"/>
			<emp:text id="PspGuarantyValueReeval.task_id" label="任务编号" required="true" hidden="true" defvalue="${context.task_id}"/>
			<emp:text id="PspGuarantyValueReeval.cus_id" label="客户码" maxlength="40" required="true" hidden="true" defvalue="${context.cus_id}"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<%if(!op.equals("view")){ %>
			<emp:button id="sub" label="确认"/>
			<%} %>
			<emp:button id="return" label="关闭"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
