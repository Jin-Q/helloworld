<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>个人贷款额度出账队列导出页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">	
</script>
</head>
<body class="page_content">
<emp:tabGroup mainTab="iqpGroupPvpTab" id="mainTab" >
	<emp:tab label="零售贷款明细清单" id="iqpGroupPvpTab" needFlush="true" url="getReportShowPage.do?reportId=iqp/iqpGroupPvpReport.raq&needPrint=no&needSaveAsWord=no" />
	<emp:tab label="额度安排表" id="lmtScheduleTab" needFlush="true" url="getReportShowPage.do?reportId=iqp/lmtScheduleReport.raq&needPrint=no&needSaveAsWord=no" />
</emp:tabGroup>
</body>
</html>
</emp:page>