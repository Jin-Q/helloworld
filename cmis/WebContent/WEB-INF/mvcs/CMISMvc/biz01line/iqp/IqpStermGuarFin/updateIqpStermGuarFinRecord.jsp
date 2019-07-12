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
		if(!IqpStermGuarFin._checkAll()){
			return;
		}
		var form = document.getElementById("submitForm");
		IqpStermGuarFin._toForm(form);
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

		var url = '<emp:url action="updateIqpStermGuarFinRecord.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	}
	function doOnLoad(){
		var options = IqpStermGuarFin.biz_settl_mode._obj.element.options;
	    for(var i=options.length-1;i>=0;i--){
			if(options[i].value=="07"){
				options.remove(i);
			}
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<emp:form id="submitForm" action="updateIqpStermGuarFinRecord.do" method="POST">
		<emp:gridLayout id="IqpStermGuarFinGroup" maxColumn="2" title="短期信保融资信息">
			<emp:text id="IqpStermGuarFin.serno" label="业务编号" defvalue="${context.serno}" maxlength="40" hidden="true" colSpan="2" required="true" readonly="true" />
			<emp:select id="IqpStermGuarFin.biz_settl_mode" label="原业务结算方式" required="true" dictname="STD_BIZ_SETTL_MODE"/>
			<emp:select id="IqpStermGuarFin.invc_cur_type" label="发票币种" required="true" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpStermGuarFin.invc_amt" label="发票金额" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="IqpStermGuarFin.insur_no" label="保单号" colSpan="2" maxlength="40" required="true" />						
			<emp:select id="IqpStermGuarFin.cdt_amt_cur_type" label="信用限额币种" required="true" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpStermGuarFin.buyer_cdt_limit" label="信保公司核定的买方有效信用限额余额" maxlength="18" required="true" dataType="Currency"/> 
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:actButton id="save" label="保存" op="update" /> 
			<emp:actButton id="reset" label="重置" op="cancel"/> 
		</div>
	</emp:form>
</body>
</html>
</emp:page>
