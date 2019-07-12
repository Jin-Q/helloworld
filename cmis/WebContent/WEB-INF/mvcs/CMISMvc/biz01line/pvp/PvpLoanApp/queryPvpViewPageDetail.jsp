<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>

<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<jsp:include page="/include.jsp" flush="true" />
<%
Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
String prd_id = (String)context.getDataValue("prd_id");
request.setAttribute("canwrite","");
%>
<emp:page>
	<head>
	<title>额度选择POP</title>
	<script type="text/javascript">
		function onLoad(){
			if('<%=prd_id%>' == "300022" || '<%=prd_id%>' == "300023" || '<%=prd_id%>' == "300024"){
				main_tabs.tabs.same_tab._clickLink();
			}else{
				main_tabs.tabs.coop_tab._clickLink();
			}
		}
	</script>
	</head>
	<body >
	<emp:tabGroup id="main_tabs" mainTab="main_tabs">
	    <%if("600020".equals(prd_id)){ %>
	    <emp:tab label="资产转受让合同信息" id="assetContSubTab" url="getCtrAssetstrsfContViewPage.do?cont_no=${context.cont_no}&menuId=queryCtrAssetstrsfContHistoryList&op=view&pvp=pvp" initial="false" needFlush="true"/>
		<%}else if("300022".equals(prd_id) || "300023".equals(prd_id) || "300024".equals(prd_id)){%>
		<emp:tab label="转贴现合同信息" id="rpContsubTab" url="getCtrRpddscntContViewPage.do?cont_no=${context.cont_no}&menuId=queryCtrRpddscntContHistoryList&op=view&pvp=pvp" initial="false" needFlush="true"/>
	    <%}else{ %>
	    <emp:tab label="合同信息" id="contSubTab" url="getCtrLoanContViewPage.do?cont_no=${context.cont_no}&menuId=queryCtrLoanContHistoryList&op=view&pvp=pvp&iqpFlowHis=have" initial="false" needFlush="true"/> 
	    <%} %>
	</emp:tabGroup>
	</body>
</emp:page>
