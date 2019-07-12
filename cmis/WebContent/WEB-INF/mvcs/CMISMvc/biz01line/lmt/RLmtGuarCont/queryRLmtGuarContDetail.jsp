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
	
	function doReturn() {
		var url = '<emp:url action="queryRLmtGuarContList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="RLmtGuarContGroup" title="授信和担保合同关系表" maxColumn="2">
			<emp:text id="RLmtGuarCont.limit_code" label="授信额度编号" maxlength="40" required="false" />
			<emp:text id="RLmtGuarCont.agr_no" label="授信协议编号" maxlength="40" required="false" />
			<emp:text id="RLmtGuarCont.guar_cont_no" label="担保合同编号" maxlength="40" required="false" />
			<emp:select id="RLmtGuarCont.is_per_gur" label="是否阶段性担保" required="false" dictname="STD_ZX_YES_NO" />
			<emp:text id="RLmtGuarCont.guar_amt" label="本次担保金额" maxlength="16" required="false" dataType="Currency" />
			<emp:select id="RLmtGuarCont.corre_rel" label="关联关系" required="false" dictname="STD_BIZ_CORRE_REL" />
			<emp:select id="RLmtGuarCont.is_add_guar" label="是否追加担保" required="false" dictname="STD_ZX_YES_NO" />
			<emp:text id="RLmtGuarCont.guar_lvl" label="担保等级" maxlength="2" required="false" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
