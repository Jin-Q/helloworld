<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@ page import="com.ecc.emp.core.EMPConstance" %>
<%@ page import="com.ecc.emp.core.Context"%>
<%@ page import="com.ecc.emp.core.Context"%>
<%@ page import="com.ecc.emp.core.EMPConstance" %>
<%@page import="com.ecc.emp.data.KeyedCollection"%>
<%@page import="com.ecc.emp.data.DataField"%>
<%
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);	
	String[] stat_prd = ((String)context.getDataValue("stat_prd")).split(",");
	String selects = (String)context.getDataValue("selected");
	String chartType = request.getParameter("chartType");
	String showType = request.getParameter("showType");
	String fncType = request.getParameter("fncType");
	String display_type = request.getParameter("display_type");
	
	int i=0,j=0,num=0 ;
	int records = stat_prd.length;

	if(showType.equals("02")||showType.equals("03")||!fncType.equals("dupont")){
		records = (Integer)context.getDataValue("records");
		String yName = (String)context.getDataValue("yName");
		yName = yName.substring(1,yName.length());
		stat_prd = yName.split(",");
	}
	KeyedCollection[] kcolls = new KeyedCollection[records];	//接收分析结果的值
	KeyedCollection kColl_item = new KeyedCollection("item") ;	//接收财报项名称
	if(fncType.equals("dupont")){
		for(i=0; i<records ; i++){
			kcolls[i] = (KeyedCollection)context.getDataElement("DupontAnaly"+i);
		}
	}else{
		for(i=0; i<records ; i++){
			kcolls[i] = (KeyedCollection)context.getDataElement("StruAnaly"+i);
		}
		kColl_item = (KeyedCollection)context.getDataElement("item");
	}
	
%>
<emp:page>
<html>
<head>
<title>财务报表分析</title>

<jsp:include page="/include.jsp" flush="true"/>
<link href="<emp:file fileName='styles/emp/rpt.css'/>" rel="stylesheet" type="text/css" />
<script src="<emp:file fileName='scripts/flashcharts/js/FusionCharts.js'/>" type="text/javascript" language="javascript"></script> 
<style type="text/css">
	/*************** 输入框(input)普通状态下的样式 ********************/
.emp_field_longtext_input { /****** 长度固定 ******/
	width: 400px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}

/*************** 输入框(input)不可用状态下的样式 ********************/
.emp_field_disabled .emp_field_longtext_input {
	border-color: #b7b7b7;
	color: #CEC7BD;
}

/*************** 输入框(input)只读状态下的样式 ********************/
.emp_field_readonly .emp_field_longtext_input {
	border-color: #b7b7b7;
}

</style>

<script>
/*
 * 将数值四舍五入(保留2位小数)后格式化成金额形式
 *
 * @param num 数值(Number或者String)
 * @return 金额格式的字符串,如'1,234,567.45'
 * @type String
 */
function formatCurrency(num) {
	if(num.indexOf('%')<0){
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
window.onresize = function(){
	myChart.render("chartdiv");
};
</script>
</head>
<body class="page_content">

	<div id="chartdiv"></div>

	<script type="text/javascript">
		var xml = "${context.multiXML}" ;
		var swf = "<%=chartType%>";
		swf = (swf == "bar") ? "scripts/flashcharts/swfs/FCF_MSColumn3D.swf":"scripts/flashcharts/swfs/FCF_MSLine.swf";
		myChart = new FusionCharts(swf,"myChartid","100%","500");
		myChart.setDataXML(xml); 
		myChart.render("chartdiv");
	</script>	
<br>
<table width="90%" border="0" align="center">
<tr> 
<td> 
	<table align="center">
	<tr>
        <td class="emp_gridlayout_title">财报分析结果</td>
    </tr>
	</table>
	<table class='emp_rpt_whole_table'>
		<tr>
			<td valign='top' align='center' width='50%'>
			<table width='100%'>
				<%if(fncType.equals("dupont")){
					String[] fncName = ("主营业务收入,净利润,资产总计,财务费用,管理费用,营业费用,主营业务成本,其他业务利润,所得税," +
							"流动资产合计,长期投资,固定资产,无形资产,货币资金,短期投资,应收账款,存货,其他流动资产,其他长期资产," +
							"负债合计,长期资产,全部成本,资产总额,总资产周转率,主营业务利润率,总资产收益率,权益乘数,净资产收益率").split(",");
						String[] fncId = ("0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28").split(",");
					%>
						<tr class='emp_rpt_head' style="color: white">
						<td align="center" width="5%" nowrap="nowrap">序号</td>
						<td width="15%" align="center" nowrap="nowrap">项目名称</td>
					<%						
						for (i = 0; i < records; i++) {
							if(!(display_type.equals("1")&& (i%2 != 0 || i == 0  ))){
								%>								
								<td  align="center"  nowrap="nowrap"><%=stat_prd[i]+"期"%></td>
								<%
							}					
						}
					%>
					</tr>
					<%
						for (i = 0; i < fncId.length; i++) {
							
							if(selects.indexOf(",text"+fncId[i]+",") > -1){
								num++;
					%>
					<tr class='emp_rpt_tr' id="text<%=fncId[i]%>">
						<td class='emp_rpt_label' style="text-align: center;" ><%=fncId[num]%></td>
						<td class='emp_rpt_label' style="text-align: left;" ><%=fncName[i]%></td>
						<%
							for (j = 0; j < records; j++) {
								if(!(display_type.equals("1")&& (j%2 != 0 || j == 0  ))){
									%>
									<td class='emp_rpt_label' style="text-align: right;" ><script language="javascript">	
									document.write(formatCurrency('<%=((DataField)kcolls[j].getDataElement(i)).getValue().toString()%>'))
									</script></td>
								<%
								}						
							}
						%>
					</tr>
					<%
							}
						}
					}else{
					%>
						<tr class='emp_rpt_head' style="color: white">
						<td align="center" width="5%" nowrap="nowrap">序号</td>
						<td width="15%" align="center" nowrap="nowrap">项目名称</td>
					<%	
					for (i = 0; i < records; i++) {
						%>
							<td  align="center"  nowrap="nowrap"><%=stat_prd[i]+"期"%></td>
							<%
							}
					%>
					</tr>
					<%
					for (i = 0; i < kcolls[0].size(); i++) {
				%>
				<tr class='emp_rpt_tr' id="text<%=i%>">
					<td class='emp_rpt_label' style="text-align: center;" ><%=i+1%></td>
					<td class='emp_rpt_label' style="text-align: left;" ><%=kColl_item.getDataValue(kcolls[0].getDataElement(i).getName()).toString()%></td>
					<%
						for (j = 0; j < records; j++) {
					%>
						<td class='emp_rpt_label' style="text-align: right;" ><script language="javascript">	
						document.write(formatCurrency('<%=((DataField)kcolls[j].getDataElement(i)).getValue().toString()%>'))
						</script></td>
					<%
						}
					%>
				</tr>
				<%
						}
				}
				%>
				
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