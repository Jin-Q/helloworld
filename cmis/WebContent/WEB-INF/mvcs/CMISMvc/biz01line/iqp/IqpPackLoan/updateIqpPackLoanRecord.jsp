<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	String cont = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
		if(op.equals("view")){
			request.setAttribute("canwrite","");
		}   
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
		if(!IqpPackLoan._checkAll()){
			return;
		}
		var form = document.getElementById("submitForm");
		IqpPackLoan._toForm(form);
		//var serno = IqpBksyndic._getValue();
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

		var url = '<emp:url action="updateIqpPackLoanRecord.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	}
							
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" >
	<emp:form id="submitForm" action="updateIqpPackLoanRecord.do" method="POST">
		<emp:gridLayout id="IqpPackLoanGroup" maxColumn="2" title="信用证信息">
			<emp:text id="IqpPackLoan.serno" label="业务编号" maxlength="40" defvalue="${context.serno}" colSpan="2" hidden="true" required="false" readonly="true" />
			<emp:text id="IqpPackLoan.cdt_cert_no" label="信用证编号" maxlength="40" required="true" />
			<emp:select id="IqpPackLoan.cdt_cert_cur_type" label="信用证币种" required="true" defvalue="CNY" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpPackLoan.cdt_cert_amt" label="信用证金额" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="IqpPackLoan.cdt_cert_bal" label="信用证余额" maxlength="18" required="true" dataType="Currency" />
			<emp:select id="IqpPackLoan.is_internal_cert" label="是否国内证" required="true" dictname="STD_ZX_YES_NO" />
			<emp:textarea id="IqpPackLoan.cdt_cert_app_advice" label="国业部信用证审查意见" hidden="true" colSpan="2" maxlength="250" required="false" />
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
