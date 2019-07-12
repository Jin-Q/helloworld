<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doClose(){
       window.close();
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
							alert("修改成功!");
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
	
	<emp:form id="submitForm" action="updateIqpAppPsaleContGoodRecord.do" method="POST">
		<emp:gridLayout id="IqpAppPsaleContGoodGroup" maxColumn="2" title="购销合同商品表">
			<emp:text id="IqpAppPsaleContGood.serno" label="业务流水号" maxlength="40" required="true" readonly="true"/>
			<emp:text id="IqpAppPsaleContGood.psale_cont" label="购销合同编号" maxlength="40" required="true" readonly="true"/>
			<emp:text id="IqpAppPsaleContGood.commo_name" label="商品名称" maxlength="100" hidden="true"/>
			<emp:text id="IqpAppPsaleContGood.commo_name_displayname" label="商品名称"  required="true" readonly="true" colSpan="2"/>
			
			<emp:text id="IqpAppPsaleContGood.qnt" label="购买商品数量" maxlength="16" required="true" onblur="clcTotal()" />
			<emp:select id="IqpAppPsaleContGood.qnt_unit" label="数量单位" required="true" dictname="STD_ZB_UNIT"/> 
			<emp:text id="IqpAppPsaleContGood.unit_price" label="购买商品单价（元）" maxlength="16" required="true" dataType="Currency" onblur="clcTotal()" />
			<emp:text id="IqpAppPsaleContGood.total" label="购买商品总价（元）" maxlength="16" required="true" dataType="Currency" readonly="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="修改" op="update"/>
			<emp:button id="close" label="关闭"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
