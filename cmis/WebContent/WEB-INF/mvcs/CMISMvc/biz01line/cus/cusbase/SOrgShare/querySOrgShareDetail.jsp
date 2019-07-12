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
		var url = '<emp:url action="querySOrgShareList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:gridLayout id="SOrgShareGroup" title="S_ORG" maxColumn="2">
			<emp:text id="SOrgShare.organno" label="机构码" maxlength="20" required="true" />
			<emp:text id="SOrgShare.suporganno" label="上级机构码" maxlength="20" required="true" />
			<emp:text id="SOrgShare.locate" label="机构编号" maxlength="100" required="true" />
			<emp:text id="SOrgShare.organname" label="机构名称" maxlength="40" required="true" />
			<emp:text id="SOrgShare.organshortform" label="机构简称" maxlength="40" required="false" />
			<emp:text id="SOrgShare.enname" label="英文名" maxlength="40" required="false" />
			<emp:text id="SOrgShare.orderno" label="序号" maxlength="38" required="false" />
			<emp:text id="SOrgShare.distno" label="地区编号" maxlength="12" required="false" />
			<emp:text id="SOrgShare.launchdate" label="开办日期" maxlength="10" required="false" />
			<emp:text id="SOrgShare.organlevel" label="机构级别" maxlength="38" required="false" />
			<emp:text id="SOrgShare.fincode" label="金融代码" maxlength="21" required="false" />
			<emp:select id="SOrgShare.state" label="状态" required="true" dictname="STD_ZB_ORG_STATUS" />
			<emp:text id="SOrgShare.organchief" label="机构负责人" maxlength="32" required="false" />
			<emp:text id="SOrgShare.telnum" label="联系电话" maxlength="20" required="false" />
			<emp:text id="SOrgShare.address" label="地址" maxlength="200" required="false" />
			<emp:text id="SOrgShare.postcode" label="邮编" maxlength="10" required="false" />
			<emp:text id="SOrgShare.control" label="备注" maxlength="10" required="false" />
			<emp:text id="SOrgShare.arti_organno" label="ARTI_ORGANNO" maxlength="20" required="true" />
			<emp:text id="SOrgShare.distname" label="DISTNAME" maxlength="100" required="false" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
