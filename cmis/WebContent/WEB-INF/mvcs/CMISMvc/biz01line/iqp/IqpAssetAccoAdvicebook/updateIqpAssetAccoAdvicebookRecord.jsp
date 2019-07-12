<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
		if("view".equals(op)){
			request.setAttribute("canwrite","");
		}    
	}
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doSub(){
		var form = document.getElementById("submitForm");
		if(IqpAssetAccoAdvicebook._checkAll()){
			IqpAssetAccoAdvicebook._toForm(form);
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
	
	<emp:form id="submitForm" action="updateIqpAssetAccoAdvicebookRecord.do" method="POST">
		<emp:gridLayout id="IqpAssetAccoAdvicebookGroup" maxColumn="2" title="会计意见书">
			<emp:text id="IqpAssetAccoAdvicebook.serno" label="业务编号" maxlength="40" required="true" readonly="true" defvalue="${context.serno}"/>
			<emp:select id="IqpAssetAccoAdvicebook.acco_advice" label="会计出表意见" required="false" dictname="STD_ZB_ACCO_ADVICE"/>
			<emp:date id="IqpAssetAccoAdvicebook.advice_date" label="意见提供日期" required="false" />
			<emp:textarea id="IqpAssetAccoAdvicebook.advice_memo" label="会计意见摘要" maxlength="200" required="false" colSpan="2"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:actButton id="sub" label="保存" op="update"/>
			<emp:actButton id="reset" label="取消" op="update"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
