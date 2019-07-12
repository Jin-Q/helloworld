<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<%
    //request = (HttpServletRequest) pageContext.getRequest();
    Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String psale_cont = request.getParameter("psale_cont");
	String serno = request.getParameter("serno");
    context.put("psale_cont",psale_cont);
    context.put("serno",serno);
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function doClose(){
       window.close();
	};
	function setCatalogPath(data){
		IqpAppPsaleContGood.commo_name._setValue(data.locate);
		IqpAppPsaleContGood.commo_name_displayname._setValue(data.locate_cn);
	};
	//计算商品总价
	function clcTotal(){
		var qnt = IqpAppPsaleContGood.qnt._getValue();
		var price = IqpAppPsaleContGood.unit_price._getValue();
		if( qnt !=null && qnt !="" && price !=null && price !=""){
			var qntp = parseInt(IqpAppPsaleContGood.qnt._getValue());
			var pricep = parseFloat(IqpAppPsaleContGood.unit_price._getValue());
			var total = parseFloat(qnt*price).toFixed(2);
			IqpAppPsaleContGood.total._setValue(total);
		}
		return false;
	 };
	 function doSub(){
			if(IqpAppPsaleContGood._checkAll()){
				var form = document.getElementById("submitForm");
				IqpAppPsaleContGood._toForm(form);
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr1 define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						var message = jsonstr.msg;
						if(flag == "success"){
							alert("新增成功!");
							window.opener.parent.location.reload();         
							window.close();   
						}else {
							alert(message);
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
			}
		};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addIqpAppPsaleContGoodRecord.do" method="POST">
		
		<emp:gridLayout id="IqpAppPsaleContGoodGroup" title="购销合同商品表" maxColumn="2">
		
			<emp:text id="IqpAppPsaleContGood.serno" label="业务流水号" maxlength="40" required="true" defvalue="${context.serno}" />
			<emp:text id="IqpAppPsaleContGood.psale_cont" label="购销合同编号" maxlength="40" required="true" defvalue="${context.psale_cont}"/>
			
			<emp:pop id="IqpAppPsaleContGood.commo_name_displayname" label="商品名称" required="true" url="showCatalogManaTree.do?isMin=Y" returnMethod="setCatalogPath"/>
			<emp:text id="IqpAppPsaleContGood.commo_name" label="商品名称" required="false" hidden="true"/>
			
		    <emp:text id="IqpAppPsaleContGood.qnt" label="购买商品数量" maxlength="16" required="true" onblur="clcTotal()" />
			<emp:select id="IqpAppPsaleContGood.qnt_unit" label="数量单位" required="true" dictname="STD_ZB_UNIT"/> 
			<emp:text id="IqpAppPsaleContGood.unit_price" label="购买商品单价（元）" maxlength="16" required="true" dataType="Currency" onblur="clcTotal()" />
			<emp:text id="IqpAppPsaleContGood.total" label="购买商品总价（元）" maxlength="16" required="true" dataType="Currency" readonly="true" />
		
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="确定"/>
			<emp:button id="close" label="关闭"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

