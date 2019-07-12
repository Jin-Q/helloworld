<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<%@ page import="com.ecc.emp.core.EMPConstance"%>
<%@ page import="com.ecc.emp.core.Context"%>
<%@ page import="com.ecc.emp.data.KeyedCollection"%>
<%@ page import="com.ecc.emp.data.IndexedCollection"%>
<%@ page import="com.ecc.emp.data.DataField"%>
<%
	Context context = (Context) request
			.getAttribute(EMPConstance.ATTR_CONTEXT);
	IndexedCollection iColl = (IndexedCollection) context
			.getDataElement("OrgBadAnalyList");
	String[] item = "机构,贷款余额（万元）,不良贷款（万元）,不良贷款率,次级（万元）,可疑（万元）,损失（万元）".split(",");
	int i=0, j=0;
%>
<emp:page>

<html>
<head>
<title>贷款指标分析</title>
<jsp:include page="/include.jsp" flush="true" />
<link href="<emp:file fileName='styles/emp/rpt.css'/>" rel="stylesheet"	type="text/css" />
<script type="text/javascript"></script>
</head>

<body class="page_content">
	<table class='emp_rpt_whole_table' cellpadding="0" cellspacing="0" >
	<tr height="55" align="center"><td class="emp_gridlayout_titles"  >机构不良贷款分析</td></tr>
		<tr>
			<td valign='top' align='center' width='50%'>
		<table width='90%' align="center" cellpadding="0" cellspacing="0">
		<tr class='emp_rpt_tr' >
		<%
		for (i = 0; i < item.length; i++) {
		%>
			<td class='emp_rpt_head' nowrap="nowrap"><%=item[i]%></td>
		<%}%>
		</tr>
	<%
		for (i = 0; i < iColl.size(); i++) {
	%>
		<tr class='emp_rpt_tr' >
	<%
			for (j = 0; j< item.length ; j++){
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				
				if(j==0){
			%>
					<td class='emp_rpt_labelb' nowrap="nowrap"><%=kColl.getDataValue(kColl.getDataElement(j).getName())%></td>
			<%
				}else{
			%>
					<td class='emp_rpt_label' style="text-align: right;" nowrap="nowrap"><%=kColl.getDataValue(kColl.getDataElement(j).getName())%></td>
			<%
				}
			}
	%>
		</tr>
	<%
		}
	%>
		</table>
			</td>
		</tr>
	</table>
</body>
</html>
</emp:page>