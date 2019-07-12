<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<%@ page import="com.ecc.emp.core.EMPConstance"%>
<%@ page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.data.KeyedCollection"%>
<%@page import="com.ecc.emp.data.IndexedCollection"%>
<%@page import="com.ecc.emp.data.DataField"%>
<%
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);	
	IndexedCollection icoll = (IndexedCollection)context.getDataElement("subjectAnalyList");
	
	int i=0,j=0,rows=0;;
	int icoll_Length = icoll.size() ;
	String stat_prd = context.getDataValue("stat_prd").toString();
	String[] types = (context.getDataValue("types").toString()).split(","); //类别
	String[] col = (context.getDataValue("cols").toString()).split(","); //类别行数
	String[] titles = new String[icoll_Length]; //指标
	String[] values = new String[icoll_Length]; //值
	String[] memos = new String[icoll_Length]; //备注
	int[] cols = new int[col.length]; //类别行数	
	
	for(i=0;i<col.length;i++){
		cols[i] = Integer.parseInt(col[i]);
	}
	
		
	for(j=0;j<icoll_Length;j++){
		KeyedCollection kcoll = (KeyedCollection)icoll.getElementAt(j);
		titles[j] = kcoll.getDataValue("titles").toString();
		values[j] = kcoll.getDataValue("values").toString();
		memos[j] = kcoll.getDataValue("memos").toString();
	}
	j=0;
%>

<emp:page>
	<html>
	<head>
	<title>指标分析结果页面</title>
	<jsp:include page="/include.jsp" />
	<link href="<emp:file fileName='styles/emp/rpt.css'/>" rel="stylesheet"	type="text/css" />
	<script>
function formatCurrency(num) {
	if(num.indexOf("%") < 0){
		num = num.toString().replace(/\$|\,/g,'');
	    if(isNaN(num))
	    num = "0";
	    sign = (num == (num = Math.abs(num)));
	    num = Math.floor(num*100+0.50000000001);
	    cents = num%100;
	    num = Math.floor(num/100).toString();
	    if(cents<10)
	    cents = "0" + cents;
	    for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
	    num = num.substring(0,num.length-(4*i+3))+','+
	    num.substring(num.length-(4*i+3));
	    return (((sign)?'':'-') + num + '.' + cents);
	}else{
		return num;
	}
};
</script>
	</head>
	<body>
	<table width="95%" border="0" align="center">
		<tr>
			<td>
			<table align="center">
				<tr>
					<td class="emp_gridlayout_title">第<%=stat_prd%>期财务指标分析结果</td>
				</tr>
			</table>
			<br>
			<table class='emp_rpt_whole_table'>
				<tr>
					<td valign='top' align='center' width='50%'>
					<table width='100%'>
						<tr class='emp_rpt_head' style="color: white">
							<td align="center" width="5%" nowrap="nowrap">序号</td>
							<td width="20%" align="center" nowrap="nowrap">类别</td>
							<td width="20%" align="center" nowrap="nowrap">指标</td>
							<td width="10%" align="center" nowrap="nowrap">值</td>
							<td width="45%" align="center" nowrap="nowrap">备注</td>
						</tr>
						
						<%	for (i = 0; i < icoll_Length; i++) {%>
							<tr class='emp_rpt_tr' >
								<td class='emp_rpt_label' style="text-align: center;" ><%=i+1%></td>
								<%if(i == cols[j]){
									rows = cols[j+1]-cols[j];
									j++;
								%>
								<td valign="middle" style="text-align: center;"  rowspan="<%=rows%>" ><%=types[j-1]%></td>
								<%}%>
								<td class='emp_rpt_label' style="text-align: left;" ><%=titles[i]%></td>
								<td class='emp_rpt_label' style="text-align: right;" >
								<script language="javascript">document.write(formatCurrency('<%=values[i]%>'))</script></td>
								<td class='emp_rpt_label' style="text-align: left;" ><%=memos[i]%></td>
							</tr>							
						<%} %>
					</table>
					</td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
	</body>
	</html>
</emp:page>