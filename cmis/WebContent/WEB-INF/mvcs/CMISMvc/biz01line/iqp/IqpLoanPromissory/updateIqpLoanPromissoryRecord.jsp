<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	String cont ="";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
		if(op.equals("view")){
			request.setAttribute("canwrite","");
		}  
	}
	String serno = "";
	String bizType = "";
	if(context.containsKey("serno")){
		serno = (String)context.getDataValue("serno");
	}
	if(context.containsKey("cont")){
		cont = (String)context.getDataValue("cont");
		if(cont.equals("cont")){   
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
	function doSave(){
		if(!IqpLoanPromissory._checkAll()){
			return;
		}
		var form = document.getElementById("submitForm");
		IqpLoanPromissory._toForm(form);
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
					alert("保存成功！");
					window.location.reload();
				}else {
					alert("保存失败！");
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

		var url = '<emp:url action="updateIqpLoanPromissoryRecord.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	};
	//主管客户经理
	function setconId(data){
		IqpLoanPromissory.receiver._setValue(data.actorno._getValue());
		IqpLoanPromissory.receiver_displayname._setValue(data.actorname._getValue());
	}					
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="updateIqpLoanPromissoryRecord.do" method="POST">
		<emp:gridLayout id="IqpLoanPromissoryGroup" maxColumn="2" title="项目背景信息">  
			<emp:text id="IqpLoanPromissory.serno" label="业务编号" maxlength="40" required="false" defvalue="<%=serno %>"  colSpan="2" readonly="true" hidden="true"/>       
			<emp:text id="IqpLoanPromissory.receiver" label="接收方" required="true"/> 
			
			<emp:text id="IqpLoanPromissory.item_name" label="项目名称" maxlength="100" colSpan="2" required="false" />
			<emp:textarea id="IqpLoanPromissory.other_cond_need" label="其他条件和要求" maxlength="100" required="false" colSpan="2" />
			<emp:textarea id="IqpLoanPromissory.item_bground" label="项目背景" maxlength="250" colSpan="2" required="false" />
			<emp:textarea id="IqpLoanPromissory.busnes_bground" label="贸易背景" maxlength="250" required="false" />
		    <emp:text id="IqpLoanPromissory.chrg_rate" label="手续费率" required="false" hidden="true" dataType="Permille"/>
		</emp:gridLayout> 
		
		<div align="center">
			<br>
			<emp:actButton id="save" label="保存" op="update"/>
			<emp:actButton id="reset" label="重置" op="cancel"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
