<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>

<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<jsp:include page="/include.jsp" flush="true" />
<%
Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
String prd_id = (String)context.getDataValue("prd_id");
String OUT_COOP = (String)context.getDataValue("OUT_COOP");
String OUT_BIZAREA = (String)context.getDataValue("OUT_BIZAREA");
String OUT_BUSDRFT = (String)context.getDataValue("OUT_BUSDRFT");
String OUT_SAMEORG = (String)context.getDataValue("OUT_SAMEORG");
String OUT_INDUS = (String)context.getDataValue("OUT_INDUS");
%>
<emp:page>
	<head>
	<title>额度选择POP</title>
	<script type="text/javascript">

		function onLoad(){
			var OUT_COOP = '<%=OUT_COOP%>';
			var OUT_BIZAREA = '<%=OUT_BIZAREA%>';
			var OUT_BUSDRFT = '<%=OUT_BUSDRFT%>';
			var OUT_SAMEORG = '<%=OUT_SAMEORG%>';
			var OUT_INDUS = '<%=OUT_INDUS%>';
			if(OUT_COOP=="1"){
				main_tabs.tabs.coop_tab._clickLink();
			}else if(OUT_BIZAREA=='1'){
				main_tabs.tabs.bizArea_tab._clickLink();
			}else if(OUT_INDUS=='1'){
				main_tabs.tabs.indus_tab._clickLink();
			}else if(OUT_SAMEORG=='1'){
				main_tabs.tabs.same_tab._clickLink();
			}else {
				main_tabs.tabs.intback_tab._clickLink();
			}
		}
	</script>
	</head>
	<body onload="onLoad()">
	<emp:tabGroup id="main_tabs" mainTab="main_tabs">
	  <%if("1".equals(OUT_COOP)){ %>
		<emp:tab id="coop_tab" label="合作方授信" url="selectLmtAgrDetails.do" 
			reqParams="lmt_type=${context.lmt_type}&cus_id=${context.cus_id}&outstnd_amt=${context.outstnd_amt}&guar_type=${context.guar_type}&prd_id=${context.prd_id}&selectType=1&returnMethod=${context.returnMethod}" needFlush="true" initial="false" />
		<%}
	  	if("1".equals(OUT_BIZAREA)){%>
		<emp:tab id="bizArea_tab" label="圈商授信" url="selectLmtAgrDetails.do" 
			reqParams="lmt_type=${context.lmt_type}&cus_id=${context.cus_id}&outstnd_amt=${context.outstnd_amt}&guar_type=${context.guar_type}&prd_id=${context.prd_id}&selectType=2&returnMethod=${context.returnMethod}" needFlush="true" initial="false" />
		<%} 
		if("1".equals(OUT_INDUS)){%>
		<emp:tab id="indus_tab" label="行业授信" url="selectLmtAgrDetails.do" 
			reqParams="lmt_type=${context.lmt_type}&cus_id=${context.cus_id}&outstnd_amt=${context.outstnd_amt}&guar_type=${context.guar_type}&prd_id=${context.prd_id}&selectType=3&returnMethod=${context.returnMethod}" needFlush="true" initial="false" />
		<%} 
		if("1".equals(OUT_SAMEORG)){%>
		<emp:tab id="same_tab" label="同业授信" url="selectLmtAgrDetails.do" 
			reqParams="lmt_type=${context.lmt_type}&cus_id=${context.cus_id}&outstnd_amt=${context.outstnd_amt}&guar_type=${context.guar_type}&prd_id=${context.prd_id}&selectType=5&returnMethod=${context.returnMethod}" needFlush="true" initial="false" />
		<%} 
		if("1".equals(OUT_BUSDRFT)){%>
		<emp:tab id="intback_tab" label="商票贴现" url="selectLmtAgrDetails.do" 
			reqParams="lmt_type=${context.lmt_type}&cus_id=${context.cus_id}&outstnd_amt=${context.outstnd_amt}&guar_type=${context.guar_type}&prd_id=${context.prd_id}&selectType=4&returnMethod=${context.returnMethod}" needFlush="true" initial="false" />
		<%} %>
	</emp:tabGroup>
	</body>
</emp:page>
