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
		var url = '<emp:url action="queryWfiCallBackDiscList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="WfiCallBackDiscGroup" title="打回标识配置信息" maxColumn="2">
			<emp:text id="WfiCallBackDisc.pk_id" label="主键" maxlength="36" colSpan="2" required="true" hidden="true" />
			<emp:text id="WfiCallBackDisc.cb_enname" label="打回标识号" required="true"  readonly="true"  />
			<emp:text id="WfiCallBackDisc.cb_cnname" label="打回标识"  required="true" />
			<emp:textarea id="WfiCallBackDisc.cb_memo" label="内容说明" maxlength="200" colSpan="2" required="false" />
			<emp:text id="WfiCallBackDisc.attr1" label="attr1" maxlength="50" required="false" hidden="true" colSpan="2" />
			<emp:text id="WfiCallBackDisc.attr2" label="attr2" maxlength="50" required="false" hidden="true" colSpan="2" />
			<emp:text id="WfiCallBackDisc.attr3" label="attr3" maxlength="50" required="false" hidden="true" colSpan="2" />
			<emp:text id="WfiCallBackDisc.or_no" label="排序号" maxlength="50" required="false" />
			<emp:select id="WfiCallBackDisc.is_inuse" label="是否有效"  required="true" dictname="STD_ZX_YES_NO" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
