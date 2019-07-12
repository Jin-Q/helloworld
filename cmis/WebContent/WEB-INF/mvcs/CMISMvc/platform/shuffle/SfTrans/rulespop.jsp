<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="com.ecc.shuffle.db.DbControl" %>
<%@ page import="com.ecc.shuffleserver.ShuffleServlet" %>
<%@ page import="com.ecc.shufflestudio.editor.RuleSetWrapper" %>
<%@ page import="com.ecc.shufflestudio.editor.param.*" %>
<%@ page import="com.ecc.shuffle.rule.*" %>
<%@ page import="com.ecc.shuffle.upgrade.*" %>
<jsp:include page="/include.jsp" flush="true"/>
<%
String i_rulesetid=request.getParameter("i_rulesetid");//调用的规则集ID
if(i_rulesetid==null)i_rulesetid="";
String i_ruleid=request.getParameter("i_ruleid");//调用的规则ID
if(i_ruleid==null)i_ruleid="";
String id=request.getParameter("id");//复制对象id
%>
<html>
<head>
<style type='text/css'>
A{font-size:12px}
BODY{font-size:12px;font-family:'宋体';color:#111;}
.QZ_Ptitle{width:100%;height:20px;line-height:20px;text-indent:10px;text-align:left;font-size:13px;}
.tablemain {background-color: #fff;width:100%;border-collapse:collapse;border: 1px solid #b7b7b7;}
.tablemain tr,.tablemain td{border-right:1px solid #b7b7b7;border-bottom:1px solid #b7b7b7;padding:2px 5px;}
.trtitle{FONT-WEIGHT: bold;FONT-SIZE: 12px;LINE-HEIGHT: 28px;FONT-FAMILY:;BACKGROUND-COLOR: #d7d7d7;text-align: center;}
.trclass {background-color: #ffffff;FONT-WEIGHT: 500;FONT-SIZE: 12px;COLOR: #252525;height:20px}
.trclass2 {background-color: #e3e4e3;FONT-WEIGHT: 500;FONT-SIZE: 12px;COLOR: #252525;height:20px}
.trclass3 {background-color: #d7d7d7;FONT-WEIGHT: 500;FONT-SIZE: 12px;COLOR: #252525;height:20px}
.trclass4 {background-color: #ccc;FONT-WEIGHT: 700;FONT-SIZE: 12px;COLOR: #252525;height:20px}
.button
{
    font-size: 12px;
    color: #000000;
    padding: 2px 10px;
	background-color:#d7d7d7;    
    height: 26px;
    line-height:23px;
    text-align:center;
	BORDER: #bdbdbd 1px solid;
	cursor:hand;
	border-radius:4px;
}
.button:hover{background-color:#e1e1e1;}
.sy_selectTab{margin-top:10px;}
</style>
<script>
function changeruleset(){
	var rsid=document.getElementById("i_rulesetid").value;
	if(rsid!=""){
		document.getElementById("invokeSign").value="0";
		document.forms[0].submit();
	}
}
function doSubmit(){	
	var ruleid=document.getElementById("i_ruleid").value;
	var ruleOptions=document.getElementById("i_ruleid").options;
	var ruleSetId=	document.getElementById("i_rulesetid").value;
	var ruleSetOptions=document.getElementById("i_rulesetid").options;
	var ruleName;
	var ruleSetName;
	for ( var i = ruleOptions.length - 1; i >= 0; i--){
		if(ruleOptions[i].value==ruleid){
			ruleName=ruleOptions[i].text;
			break;
		}
	}

	for ( var i = ruleSetOptions.length - 1; i >= 0; i--){
		if(ruleSetOptions[i].value==ruleSetId){
			ruleSetName=ruleSetOptions[i].text;
			break;
		}
	}
	
	var id=document.getElementById("id").value;
	var name=id+'_displayname';
	var rule=window.opener.document.getElementById(id);
	var ruleNameElement=window.opener.document.getElementById(name);
	rule.value=ruleSetId+'$'+ruleid;
    //如果要返回文字描述，则需定义_displayname字段
	try{
	ruleNameElement.value=ruleSetName+ruleName;
	}catch(e){}
	window.close();
}
</script>
</head>
<body>
<form id="submitForm" action="rulespop.do">
<input type="hidden" id="invokeSign" name="invokeSign" value="0">
<input type="hidden" id="id" name="id" value="<%=id %>">
<input type="hidden" id="EMP_SID" name="EMP_SID" value="${context.EMP_SID}">
<table class="sy_selectTab"  cellpadding="0" cellspacing="0">
<tr>
<td style="padding-left:30px;">
要调用的规则集：</td>
<td>
<select id="i_rulesetid" name="i_rulesetid" onchange="changeruleset()">
<option>===请选择===</option>
<%
Map ruleSetMap=RuleBase.getInstance().ruleSets;
RuleSet ruleSet;//规则集对象
Rule rule;//规则对象
String rulesetid;//规则集ID,原始大小写格式
Iterator it=ruleSetMap.keySet().iterator();
while(it.hasNext()){
	ruleSet=(RuleSet)ruleSetMap.get((String)it.next());
	rulesetid=ruleSet.ids;
	if(rulesetid.equals(i_rulesetid))
		out.print("<option value='"+rulesetid+"' selected>"+rulesetid+" 【"+ruleSet.name+"】</option>");
	else
		out.print("<option value='"+rulesetid+"'>"+rulesetid+" 【"+ruleSet.name+"】</option>");
}
%>
</select></td>

<td style="padding-left:30px;">规则：</td>
<td>
 <select id="i_ruleid" name="i_ruleid"><option>===请选择===</option>
<%
if(i_rulesetid!=null&&i_rulesetid.length()>0){
	ruleSet=(RuleSet)RuleBase.getInstance().ruleSets.get(i_rulesetid.toUpperCase());
	for(int i=0;i<ruleSet.rules.size();i++){
		rule=(Rule)ruleSet.rules.get(i);
		if(rule.id.equals(i_ruleid))
			out.print("<option value='"+rule.id+"' selected>"+rule.id+" 【"+rule.name+"】</option>");
		else
			out.print("<option value='"+rule.id+"'>"+rule.id+" 【"+rule.name+"】</option>");
	}
}
%>
</select></td>
</tr>
<tr><td colspan="4" align="center" style="padding-top:10px;"><input type="button" class="button" value="&nbsp;提&nbsp;交&nbsp;" onclick="doSubmit()"></td></tr>
</table>
</form>
</body>
</html>
