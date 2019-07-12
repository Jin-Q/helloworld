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
			.getDataElement("BadRateList");
	String[] item = "贷款月份,贷款余额（万元）,不良贷款（万元）,实际不良贷款率".split(",");
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
	<table class='emp_rpt_whole_table' background="images/default/info_ul_bg.gif" >
	<tr height="55" align="center"><td class="emp_gridlayout_titles"  >不良率</td></tr>
		<tr>
			<td valign='top' align='center' width='50%'>
		<table width='80%' align="center" cellpadding="0" cellspacing="0">
		
		<tr class='emp_rpt_tr' >
		<%
		for (j = 0; j< item.length; j++){%>			
			<td class='emp_rpt_head' nowrap="nowrap"><%=item[j]%></td>
		<%}%>
		</tr>
		
		<%
		for (i = 0; i < iColl.size(); i++) {
			KeyedCollection kColl = (KeyedCollection)iColl.get(i);	
			%>
			<tr class='emp_rpt_tr' >
			<%
			for (j = 0; j< item.length; j++){

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