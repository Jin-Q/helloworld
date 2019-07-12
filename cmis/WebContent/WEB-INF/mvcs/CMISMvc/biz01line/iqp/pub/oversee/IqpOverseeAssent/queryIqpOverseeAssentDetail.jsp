<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<% String type=(String)request.getParameter("type"); %>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		window.close();
	};
	
	/*--user code begin--*/
	function doLoad(){
		var type='<%=type%>'
		if(type=='main'){
			IqpOverseeAssent.util_term._obj._renderHidden(false);
			IqpOverseeAssent.util_term._obj._renderRequired(true);
			IqpOverseeAssent.util_case._obj._renderHidden(false);	
			IqpOverseeAssent.util_case._obj._renderRequired(true);
		}else{
			IqpOverseeAssent.util_term._obj._renderHidden(true);
			IqpOverseeAssent.util_term._obj._renderRequired(false);
			IqpOverseeAssent.util_case._obj._renderHidden(true);	
			IqpOverseeAssent.util_case._obj._renderRequired(false);	
		}
	}			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:gridLayout id="IqpOverseeAssentGroup" title="资产明细" maxColumn="2">
			<emp:text id="IqpOverseeAssent.assent_name" label="资产名称" maxlength="100" required="true" />
			<emp:text id="IqpOverseeAssent.assent_qnt" label="资产数量" maxlength="10" required="true" dataType="Int" cssElementClass="emp_currency_text_readonly"/>	
			<emp:text id="IqpOverseeAssent.fore_value" label="账面净值" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpOverseeAssent.reckon_value" label="估计现值" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>	
			<emp:select id="IqpOverseeAssent.util_case" label="目前使用情况" required="true" dictname="STD_ZX_FIELD_OWNER" />				
			<emp:text id="IqpOverseeAssent.util_term" label="已使用年限(年)" maxlength="10" required="false" hidden="true"/>
			<emp:text id="IqpOverseeAssent.wrr_proof" label="权利凭证" maxlength="40" required="true" />
			<emp:select id="IqpOverseeAssent.is_pldimn" label="是否已抵质押" required="true" dictname="STD_ZX_YES_NO" />					
			<emp:select id="IqpOverseeAssent.assent_type" label="资产类别" required="false" dictname="STD_ZB_ASSENT_TYPE" hidden="true"/>
			<emp:text id="IqpOverseeAssent.assent_id" label="资产编号" maxlength="32" required="false" hidden="true"/>
			<emp:text id="IqpOverseeAssent.serno" label="业务流水号" maxlength="32" required="false" hidden="true"/>
			<emp:textarea id="IqpOverseeAssent.memo" label="备注" maxlength="500" required="false" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="关闭"/>
	</div>
</body>
</html>
</emp:page>
