<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>
<%@ page import="com.ecc.emp.core.Context"%>
<%@ page import="com.ecc.emp.core.EMPConstance" %>
<%@page import="com.ecc.emp.data.KeyedCollection"%>
<%@page import="com.ecc.emp.data.DataField"%>
<%
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);	
	String[] stat_prd = ((String)context.getDataValue("stat_prd")).split(",");
	KeyedCollection[] kcolls = new KeyedCollection[stat_prd.length];
	int i=0,j=0 ;
	
	for(i=0; i<stat_prd.length ; i++){
		kcolls[i] = (KeyedCollection)context.getDataElement("DupontAnaly"+i);
	}
%>

<emp:page>
	<html>
	<head>
	<title>列表查询页面</title>
	<jsp:include page="/include.jsp" />
	<link href="<emp:file fileName='styles/emp/rpt.css'/>" rel="stylesheet" type="text/css" />
<script>
/*
 * 将数值四舍五入(保留2位小数)后格式化成金额形式
 *
 * @param num 数值(Number或者String)
 * @return 金额格式的字符串,如'1,234,567.45'
 * @type String
 */
function formatCurrency(num) {
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
};
var selected = ",";
function doSelect(obj){
	text = obj.id;
	str_begin = selected.indexOf(text+",");
	if(str_begin>0){
		obj.style.background = 'white';
		selected = selected.substr(0, str_begin) + selected.substr(str_begin+text.length+1, selected.length);
	}else{
		selected = selected+text+",";
		obj.style.background = '#BBDAFD';
	}
};

function doBarGraph(){
	if (selected.length > 1) {
		var cus_id = '${context.cus_id}' ;
		var stat_prd_style = '${context.stat_prd_style}' ;
		var stat_prd = '${context.stat_prd}' ;
		var stat_style = '${context.stat_style}' ;
		var paramStr = '&cus_id='+cus_id+'&stat_prd_style='+stat_prd_style+'&stat_prd='+stat_prd+'&stat_style='+stat_style;
		var url = '<emp:url action="getFlashChart.do"/>&selected='+selected+'&display_type='+document.all.display_type.value
			+'&chartType=bar&fncType=dupont&showType='+document.all.fnc_type.value+paramStr;
		url = EMP.util.Tools.encodeURI(url);
		window.location = url;
	} else {
		alert('请先选择分析字段！');
	}
};

function doLinearGraph(){
	if (selected.length > 1) {
		var cus_id = '${context.cus_id}' ;
		var stat_prd_style = '${context.stat_prd_style}' ;
		var stat_prd = '${context.stat_prd}' ;
		var stat_style = '${context.stat_style}' ;
		var paramStr = '&cus_id='+cus_id+'&stat_prd_style='+stat_prd_style+'&stat_prd='+stat_prd+'&stat_style='+stat_style;
		var url = '<emp:url action="getFlashChart.do"/>&selected='+selected+'&display_type='+document.all.display_type.value
			+'&chartType=line&fncType=dupont&showType='+document.all.fnc_type.value+paramStr;
		url = EMP.util.Tools.encodeURI(url);
		window.location = url;
	} else {
		alert('请先选择分析字段！');
	}
};

function doSelectAll(){
	selected = ",";
	for(i = 0 ; i < 28 ; i++){
		id = "text"+i;
		doSelect(document.getElementById(id));
	}
};
function doReset(){
	selected = ",";
	for(i = 0 ; i < 28 ; i++){
		id = "text"+i;
		document.getElementById(id).style.background = 'white';
	}
};
function setDisplayType(){
	if(document.getElementById("fnc_type").value == '01'){		
		document.getElementById("display_type").value = '2';
		document.getElementById("display_type").disabled = true;
	}else{
		document.getElementById("display_type").disabled = false;
	}
	
};
</script>
</head>
<body onload="setDisplayType()">

<table width="95%" border="0" align="center">
<tr> 
<td> 
	<table align="center">
	<tr>
        <td class="emp_gridlayout_title">点击表格选择分析项</td>
    </tr>
	</table>
	<div align="right">		
		<emp:button id="selectAll" label="全选"/>
		<emp:button id="reset" label="重置"/>
	</div>

	<table class='emp_rpt_whole_table'>
		<tr>
			<td valign='top' align='center' width='50%'>
			<table width='100%'>
				<tr class='emp_rpt_head' style="color: white">
					<td align="center" width="5%" nowrap="nowrap">序号</td>
					<td width="15%" align="center" nowrap="nowrap">项目名称</td>
				
				<%
					String[] fncName = ("主营业务收入,净利润,资产总计,财务费用,管理费用,营业费用,主营业务成本,其他业务利润,所得税," +
						"流动资产合计,长期投资,固定资产,无形资产,货币资金,短期投资,应收账款,存货,其他流动资产,其他长期资产," +
						"负债合计,长期资产,全部成本,资产总额,总资产周转率,主营业务利润率,总资产收益率,权益乘数,净资产收益率").split(",");
					String[] fncId = ("0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28").split(",");
					for (i = 0; i < stat_prd.length; i++) {
				%>
					<td nowrap="nowrap" align="center"><%=stat_prd[i]+"期"%></td>
					<%
					}
				%>
				</tr>
				<%
					for (i = 0; i < fncName.length; i++) {
				%>
				<tr class='emp_rpt_tr' id="text<%=fncId[i]%>" onclick="doSelect(this)">
					<td class='emp_rpt_label' style="text-align: center;" ><%=fncId[i+1]%></td>
					<td class='emp_rpt_label' style="text-align: left;" ><%=fncName[i]%></td>
					<%
						for (j = 0; j < stat_prd.length; j++) {
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
				%>
			</table>
			</td>
		</tr>
	</table>
</td>
</tr>
<tr>
		<td>
		<emp:gridLayout id="FncStatBaseGroup" maxColumn="2" title="选择分析条件： " >
			<emp:select id="fnc_type"  label="结果显示类型"  dictname="STD_ZB_FNC_CHART" defvalue="01"  onchange="setDisplayType()"/>
			<emp:select id="display_type"  label="是否仅显示比较"  dictname="STD_ZX_YES_NO"  />
		</emp:gridLayout>
		<div align="center">
			<emp:button id="barGraph" label="柱状图" />
			<emp:button	id="linearGraph" label="线状图" />
		</div>
		</td>
		</tr>
</table>	           
</body>
</html>
</emp:page>