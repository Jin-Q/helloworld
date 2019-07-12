<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<%@ page import="com.ecc.emp.core.EMPConstance"%>
<%@ page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.data.KeyedCollection"%>
<%@page import="com.ecc.emp.data.IndexedCollection"%>
<%@page import="com.ecc.emp.data.DataField"%>
<%
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);	
	String[] stat_prd = ((String)context.getDataValue("stat_prd")).split(","); //报表期间
	String fncType = (String)context.getDataValue("fncType"); //报表类型
	KeyedCollection[] kcolls = new KeyedCollection[stat_prd.length];	//用kcolls[]数组从后台接收多个结果集
	IndexedCollection icoll = (IndexedCollection)context.getDataElement("FncConfDefFmt"); //取自Fnc_Conf_Def_Fmt表，主要是报表项名称、样式、编号
	
	int i=0,j=0;
	int item_Length = icoll.size() ;
	int record_Length = stat_prd.length;
	String[] fncId = new String[item_Length];	//序号
	String[] fncName = new String[item_Length];	//项目名称
	String[] itemFlag = new String[item_Length];	//单元类型，如标题、合计项之类，用于区别样式
	
	for(i=0; i<stat_prd.length ; i++){
		kcolls[i] = (KeyedCollection)context.getDataElement("StruAnaly"+i);	//接收报表项值
	}
	
	for(j=0;j<item_Length;j++){
		KeyedCollection kcoll = (KeyedCollection)icoll.getElementAt(j);
		fncId[j] = (j+1)+"";
		fncName[j] = kcoll.getDataValue("item_id_displayname").toString();
		itemFlag[j] = kcoll.getDataValue("fnc_item_edit_typ").toString();
	}
%>

<emp:page>
	<html>
	<head>
	<title>列表查询页面</title>
	<jsp:include page="/include.jsp" />
	<link href="<emp:file fileName='styles/emp/rpt.css'/>" rel="stylesheet" type="text/css" />
<style type="text/css">
	.emp_field_td{
		text-align: center;
	};
</style>
<script>
/*** 金额格式转换 ***/
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

/*** 选择事件 ***/
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
/*** 全选方法 ***/
function doSelectAll(){
	selected = ",";
	for(i = 0 ; i < <%=fncId.length%> ; i++){
		id = "text"+i;
		if(document.getElementById(id)){
			doSelect(document.getElementById(id));
		}
	}
};
/*** 重置方法 ***/
function doReset(){
	selected = ",";
	for(i = 0 ; i < <%=fncId.length%> ; i++){
		id = "text"+i;
		if(document.getElementById(id)){
			document.getElementById(id).style.background = 'white';
		}		
	}
};
/*** 柱状图处理 ***/
function doBarGraph(){
	if (selected.length > 1) {
		var cus_id = '${context.cus_id}' ;
		var stat_prd_style = '${context.stat_prd_style}' ;
		var stat_prd = '${context.stat_prd}' ;
		var stat_style = '${context.stat_style}' ;
		var paramStr = '&cus_id='+cus_id+'&stat_prd_style='+stat_prd_style+'&stat_prd='+stat_prd+'&stat_style='+stat_style;
		var url = '<emp:url action="getFlashChart.do"/>&selected='+selected
			+'&chartType=bar&fncType=<%=fncType%>&showType='+document.all.fnc_type.value+paramStr;
		url = EMP.util.Tools.encodeURI(url);
		window.location = url;
	} else {
		alert('请先选择分析字段！');
	}
};
/*** 线状图处理 ***/
function doLinearGraph(){
	if (selected.length > 1) {
		var cus_id = '${context.cus_id}' ;
		var stat_prd_style = '${context.stat_prd_style}' ;
		var stat_prd = '${context.stat_prd}' ;
		var stat_style = '${context.stat_style}' ;
		var paramStr = '&cus_id='+cus_id+'&stat_prd_style='+stat_prd_style+'&stat_prd='+stat_prd+'&stat_style='+stat_style;
		var url = '<emp:url action="getFlashChart.do"/>&selected='+selected
			+'&chartType=line&fncType=<%=fncType%>&showType='+document.all.fnc_type.value+paramStr;
		url = EMP.util.Tools.encodeURI(url);
		window.location = url;
	} else {
		alert('请先选择分析字段！');
	}
};
</script>
</head>
<body >

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
					<td width="30%" align="center" nowrap="nowrap">项目名称</td>
				
				<%
					for (i = 0; i < record_Length; i++) {
				%>
					<td nowrap="nowrap" align="center"><%=stat_prd[i]+"期"%></td>
					<%
					}
				%>
				</tr>
				<%	for (i = 0; i < item_Length; i++) {
						if(!itemFlag[i].equals("3")){
							%>
							<tr class='emp_rpt_tr' id="text<%=i%>" onclick="doSelect(this)">
								<td class='emp_rpt_label' style="text-align: center;" ><%=fncId[i]%></td>
								<% if(itemFlag[i].equals("1")){ %>
								<td class='emp_rpt_label' style="text-align: left;" ><%=fncName[i]%></td>
								<%}else if(itemFlag[i].equals("2")){ %>
								<td class='emp_rpt_label' style="text-align: left;color: red" ><%=fncName[i]%></td>
								<%}									
									for (j = 0; j < record_Length; j++) {
									%>
										<td class='emp_rpt_label' style="text-align: right;" ><script language="javascript">
										document.write(formatCurrency('<%=((DataField)kcolls[j].getDataElement(i)).getValue().toString()%>'))
										</script></td>
									<%
									}									
								%>
							</tr>
							<%
						}else {
							%>
							<tr class='emp_rpt_tr' >
								<td class='emp_rpt_label' style="text-align: center;" ><%=fncId[i]%></td>
								<td class='emp_rpt_label' style="text-align: left;" ><%=fncName[i]%></td>
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
<tr>
		<td>
			<div class='emp_gridlayout_title'>选择分析条件：</div>
			<div id='FncStatBaseGroup' class='emp_group_div'>
			<table width="100%" class='emp_gridLayout_table 2'>
				<tr>
					<td class='emp_field_td' colspan='3'>
						<span class='emp_field_label'>结果显示类型</span>
						<span id='emp_field_fnc_type'  class='emp_field'  >
						<select	name='fnc_type'  class='emp_field_select_select'>
						<option value='01' selected="selected">显示所有的值</option>
						<option value='02'>仅显示分析值</option>
						<option value='03'>仅显示实际值</option>
						</select>
					</span></td>
				</tr>
			</table>
			</div>
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