<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String flag = "";
	if(context.containsKey("flag")){
		flag = (String)context.getDataValue("flag");
	} 
%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	background-color:#eee;
	text-align:left;
	width:450px;
}
</style>
<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryAccMortvalueRemindList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	

	function doClose(){
         window.close();
	}
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="">
	 <emp:tabGroup mainTab="base_tab" id="mainTab" >
	   <emp:tab label="基本信息" id="base_tab" needFlush="true" initial="true" >
	   <emp:gridLayout id="AccMortvalueRemindGroup" title="基本信息" maxColumn="2">
			<emp:text id="AccMortvalueRemind.catalog_no" label="押品类型代码" required="false" colSpan="2" readonly="true"/>
			<emp:text id="AccMortvalueRemind.catalog_no_displayname" label="押品类型名称" cssElementClass="emp_field_text_input2" required="true" colSpan="2" readonly="true"/>
			<emp:text id="AccMortvalueRemind.market_value" label="最新核准价格"  required="true" dataType="Currency" readonly="true"/>
			<emp:text id="AccMortvalueRemind.identy_unit_price" label="原库存价格"  required="true" dataType="Currency" readonly="true"/>
			<emp:date id="AccMortvalueRemind.auth_date" label="生成日期" required="true" readonly="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="AccMortvalueRemindGroup1" title="登记信息" maxColumn="2">
			<emp:text id="AccMortvalueRemind.input_id_displayname" label="登记人"  required="true" readonly="true"/>
			<emp:date id="AccMortvalueRemind.auth_date" label="登记日期" required="true" readonly="true"/>
			<emp:text id="AccMortvalueRemind.input_br_id_displayname" label="登记机构"  required="true" readonly="true"/>
			<emp:text id="AccMortvalueRemind.input_id" label="登记人"  required="false" hidden="true"/>
			<emp:text id="AccMortvalueRemind.input_br_id" label="登记机构"  required="false" hidden="true"/>
		</emp:gridLayout>
	 	</emp:tab>
		 <emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回"/>
	</div>
</body>
</html>
</emp:page>
