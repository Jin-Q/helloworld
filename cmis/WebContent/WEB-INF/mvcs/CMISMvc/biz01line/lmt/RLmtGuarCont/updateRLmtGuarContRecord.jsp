<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>
<html>
<head>
<title>修改页面</title>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String guar_model = "";
	String rel = "";
	if(context.containsKey("guar_model")){
		guar_model = (String)context.getDataValue("guar_model");
	}
	if(context.containsKey("rel")){
		rel = (String)context.getDataValue("rel");
	}
%>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	
	/*--user code begin--*/
	function doSub(){
	    var handleSuccess = function(o){
	    	if(o.responseText !== undefined) {
	            try {
					var jsonstr = eval("("+o.responseText+")");
	            } catch(e) {
					alert("保存失败！"); 
	              	return;  
	            }
				var flag = jsonstr.flag;
				if(flag=="success"){
					alert("保存成功！");
	            }else {
					alert("保存失败！");
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
	    if(RLmtGuarCont._checkAll()){
	    	RLmtGuarCont._toForm(form);   
		    var postData = YAHOO.util.Connect.setForm(form);
		    var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	};


    function doReturn(){
    	var guar_cont_no = RLmtGuarCont.guar_cont_no._getValue();
    	var guar_model = '<%=guar_model%>';
		var rel = '<%=rel%>';
    	var url = '<emp:url action="queryReListByGuarContNo.do"/>?guar_cont_no='+guar_cont_no+"&guar_model="+guar_model+"&rel="+rel;
	    url = EMPTools.encodeURI(url); 
	    window.location=url;
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateRLmtGuarContRecord.do" method="POST">
		<emp:gridLayout id="RLmtGuarContGroup" maxColumn="2" title="授信和担保合同关系表">
			<emp:text id="RLmtGuarCont.agr_no" label="授信协议编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="RLmtGuarCont.limit_code" label="授信额度编号" maxlength="40" required="true" readonly="true"/>
			<emp:text id="RLmtGuarCont.guar_cont_no" label="担保合同编号" maxlength="40" required="true" readonly="true"/>
			<emp:text id="RLmtGuarCont.guar_amt" label="本次担保金额" maxlength="16" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="RLmtGuarCont.is_per_gur" label="是否阶段性担保" required="true" dictname="STD_ZX_YES_NO" />
			<emp:select id="RLmtGuarCont.is_add_guar" label="是否追加担保" required="true" dictname="STD_ZX_YES_NO" />
			<emp:select id="RLmtGuarCont.corre_rel" label="关联关系" required="false" dictname="STD_BIZ_CORRE_REL" hidden="true" />
			<emp:text id="RLmtGuarCont.guar_lvl" label="担保等级" maxlength="2" required="false" hidden="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="修改"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
