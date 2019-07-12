<%@page contentType="text/html; charset=gb2312" %>
<%@ page import="java.util.*" %>
<%@ page import="com.ecc.shuffle.rule.*" %>
<%@ page import="com.ecc.shuffleserver.ShuffleServlet" %>
<%
String ruleSetId=request.getParameter("ruleSetId");//规则集ID
String ruleId=request.getParameter("ruleId");//规则ID
RuleSet ruleSet=(RuleSet)RuleBase.getInstance().ruleSets.get(ruleSetId);//规则集对象
Rule rule=ruleSet.getRule(ruleId);//规则对象
Map ruleMap=new HashMap();//规则变量定义
Map hm;
String minScore="",maxScore="",slope="",varid;
for(RuleConstant rc:rule.ruleConstants){
	if(rc.id.equals("minScore"))
		minScore=rc.value;
	else if(rc.id.equals("maxScore"))
		maxScore=rc.value;
	else if(rc.id.equals("slope"))
		slope=rc.value;
	else{
		hm=new HashMap();
		hm.put("varid",rc.id);
		hm.put("varname",rc.name);
		hm.put("vartype",rc.type);
		hm.put("varvalue",rc.value);
		hm.put("vardesc",rc.desc);
		ruleMap.put(rc.id, hm);
	}
}
%>
<html>
<head>
<title>eChain流程管理</title>
<META http-equiv=Content-Type content="text/html; charset=gb2312">
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

body{font-size:12px;}
fieldset{border:1px solid #c2c2c2;}
legend{color:#020000;}
.QZ_syTable{font-size:12px;}

</style>
</head>
<body>
<form action="<%=request.getContextPath()%>/ShuffleServlet">
<input type="hidden" name="method" value="shufflemanage">
<input type="hidden" name="varsize" value="<%=ruleMap.size()%>"/>
<input type="hidden" name="ruleSetId" value="<%=ruleSetId%>"/>
<input type="hidden" name="ruleId" value="<%=ruleId%>"/>
<fieldset><legend>规则基础信息</legend>
<table width="90%" border="0" cellspacing="1" cellpadding="3" align="center" class="QZ_syTable">
<tr><td width="40%">规则编码：<input type="text" style="background-color:#fff;border:1px solid #c2c2c2;width:300px;height:22px" readonly="true" value="<%=rule.id%>"></td><td>规则名称：<input type="text" style="background-color:#fff;border:1px solid c2c2c2;width:300px;height:22px" readonly="true" value="<%=rule.name%>"></td></tr>
<tr><td colspan=2 valign="top">
规则描述：<textarea name="policyContent" readonly="true" rows="5" cols="98" style="background-color:#fff;border:1px solid #c2c2c2;"><%=rule.desc%></textarea><br>
</td></tr></table><br>
</fieldset>

<br>
<fieldset><legend>规则常量设置</legend>
<table class=tablemain cellspacing=1 cellpadding=0 align="center" class="QZ_syTable">
<tr class=trtitle><td width="30%">常量编码</td><td width="30%">常量名称</td><td>常量值</td></tr>
<%
if(ruleMap==null||ruleMap.isEmpty()){
	out.print("<tr class=trclass><td colspan=3>该规则没有关连的常量</td></tr>");
}else{
	Iterator it=ruleMap.keySet().iterator();
	int k=0;
	while(it.hasNext()){
		k++;
		varid=(String)it.next();
		if(!(varid.equals("minScore")||varid.equals("maxScore")||varid.equals("slope"))){
		hm=(Map)ruleMap.get(varid);
		out.print("<tr class=trclass>");
		out.print("<td><input type='text' name='varid_"+k+"' readonly='true' style='background-color:#fff;border:1px solid #c2c2c2;width:150px;height:22px' value='"+varid+"'></td>");
		out.print("<td><input type='text' name='varname_"+k+"' readonly='true' style='background-color:#fff;border:1px solid #c2c2c2;width:150px;height:22px' value='"+hm.get("varname")+"'></td>");
		out.print("<td><input type='text' name='varvalue_"+k+"' style='background-color:#fff;border:1px solid #c2c2c2;width:150px;height:22px' value='"+hm.get("varvalue")+"'>");
		out.print("<input type='hidden' name='vartype_"+k+"' value='"+hm.get("vartype")+"'/>");
		out.print("<input type='hidden' name='vardesc_"+k+"' value='"+hm.get("vardesc")+"'/></td>");
		out.print("</tr>");
		}
	}
}
%>
</table><br>
</fieldset>

<br>
<fieldset><legend>风险分值设置</legend>
<table width="90%" border="0" cellspacing="1" cellpadding="3" align="center" class="QZ_syTable">
<tr>
<td width="30%">最高分：<input type="text" name="maxScore" style="background-color:#fff;border:1px solid #c2c2c2;width:200px;height:22px" value="<%=maxScore%>"></td>
<td width="30%">最低分：<input type="text" name="minScore" style="background-color:#fff;border:1px solid #c2c2c2;width:200px;height:22px" value="<%=minScore%>"></td>
<td>浮动梯度：<input type="text" name="slope" style="background-color:#fff;border:1px solid #c2c2c2;width:200px;height:22px" value="<%=slope%>"></td>
</tr>
</table><br>
</fieldset>
<center><br>
<input type="button" class="button" value="  返  回  " onclick="history.back()"/>
</center>
</form>
</body></html>
