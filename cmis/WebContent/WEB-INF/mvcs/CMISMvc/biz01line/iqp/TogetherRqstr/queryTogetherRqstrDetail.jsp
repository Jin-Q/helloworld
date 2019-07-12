<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
function onload(){
	IqpTogetherRqstr.cus_id._obj.addOneButton("cus_id","查看",getCusForm);
};
function getCusForm(){
	var cus_id = IqpTogetherRqstr.cus_id._getValue();
	if(cus_id == "" || cus_id == null){
       alert("请先选择客户!");
       return; 
	}
	var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id;
	url=EMPTools.encodeURI(url);  
  	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
};
	function doReturn() {
		var serno = IqpTogetherRqstr.serno._getValue();
		var url = '<emp:url action="queryTogetherRqstrList.do"/>?serno='+serno; 
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onload()">
	
	<emp:gridLayout id="TogetherRqstrGroup" title="共同申请人" maxColumn="2">
			<emp:text id="IqpTogetherRqstr.serno" label="业务编号" maxlength="40" required="true" />
			<emp:text id="IqpTogetherRqstr.cus_id" label="客户码" maxlength="40" required="false" />
			<emp:text id="IqpTogetherRqstr.cus_id_displayname" label="客户名称" />   
			<emp:text id="IqpTogetherRqstr.cont_no" label="合同编号" maxlength="40" required="false" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
