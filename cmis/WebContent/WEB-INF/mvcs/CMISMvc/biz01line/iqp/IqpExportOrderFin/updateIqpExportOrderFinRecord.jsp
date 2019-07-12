<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
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
    

	function doSub(){
		var form = document.getElementById("submitForm");
		IqpExportOrderFin._checkAll();
		if(IqpExportOrderFin._checkAll()){
			IqpExportOrderFin._toForm(form);
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
						alert("修改成功!");
					}else {
						alert("发生异常!");
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
	/*--user code begin--*/
	function doOnLoad(){
		var options = IqpExportOrderFin.biz_settl_mode._obj.element.options;
	    for(var i=options.length-1;i>=0;i--){
			if(options[i].value=="05" || options[i].value=="06" || options[i].value=="07"){
				options.remove(i);
			}
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad();">
	
	<emp:form id="submitForm" action="updateIqpExportOrderFinRecord.do" method="POST">
		<emp:gridLayout id="IqpExportOrderFinGroup" maxColumn="2" title="订单合同信息">
			<emp:select id="IqpExportOrderFin.order_cont_cur_type" label="订单合同币种" required="true" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="IqpExportOrderFin.order_cont_amt" label="订单合同金额" maxlength="18" required="true" dataType="Currency" />
			<emp:select id="IqpExportOrderFin.biz_settl_mode" label="原业务结算方式" required="true" dictname="STD_BIZ_SETTL_MODE"/>
			<emp:text id="IqpExportOrderFin.serno" label="业务编号" maxlength="40" defvalue="${context.serno}" required="false" hidden="true"/>
		</emp:gridLayout> 
		
		<div align="center">
			<br>
			<emp:actButton id="sub" label="保存" op="update"/>
			<emp:actButton id="reset" label="重置" op="cancel"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
