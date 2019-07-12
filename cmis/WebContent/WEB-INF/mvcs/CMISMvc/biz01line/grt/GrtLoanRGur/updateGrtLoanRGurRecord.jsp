<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
  Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
  String oper ="";
  if(context.containsKey("oper")){
	  oper = (String)context.getDataValue("oper");
	  if(oper.equals("view")){
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
	//修改事件
function doSub(){
	if(!GrtLoanRGur._checkAll()){
		return false;
	}
    var handleSuccess = function(o){
    	if(o.responseText !== undefined) {
            try {
				var jsonstr = eval("("+o.responseText+")");
            } catch(e) {
				alert("修改失败！");
              return;
            }
			var flag = jsonstr.flag;
			if(flag=="success"){
				alert("修改成功！");	
				window.parent.opener.location.reload();  
            }else {
				alert("修改失败！");
		    }
         }
	}
	var handleFailure = function(o){
	}
	var callback = {
        success:handleSuccess,
        failure:handleFailure
    }
    var form = document.getElementById("submitForm");
	GrtLoanRGur._toForm(form);
    var postData = YAHOO.util.Connect.setForm(form);
    var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
}		
	/*--user code end--*/
	
</script>
</head>
<body class="page_content"> 
	<emp:form id="submitForm" action="updateGrtLoanRGurRecord.do" method="POST">
		<emp:gridLayout id="GrtLoanRGurGroup" maxColumn="2" title="本次担保信息">
			<emp:text id="GrtLoanRGur.serno" label="业务编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="GrtLoanRGur.pk_id" label="pk_id" maxlength="40" required="false" hidden="true"/>
			<emp:text id="GrtLoanRGur.cont_no" label="合同编号" maxlength="40" required="false" hidden="true" />
			<emp:text id="GrtLoanRGur.guar_cont_no" label="担保合同编号" maxlength="40" required="true" readonly="true" />
			<emp:select id="GrtLoanRGur.is_per_gur" label="是否阶段性担保" required="true" dictname="STD_ZX_YES_NO"/>
			<emp:text id="GrtLoanRGur.guar_amt" label="本次担保金额" maxlength="18" required="true" dataType="Currency"/>
			<emp:select id="GrtLoanRGur.is_add_guar" label="是否追加担保" dictname="STD_ZX_YES_NO" />
		    <emp:text id="GrtLoanRGur.guar_lvl" label="担保等级 " maxlength="2" />
		</emp:gridLayout>  
		<div align="center">   
			<br>
			<%if(oper.equals("update")){%>
			<emp:button id="sub" label="修改" op="update"/>
			<emp:button id="reset" label="重置" />
			<%} %>  
		</div> 
	</emp:form>
</body>
</html>
</emp:page>
