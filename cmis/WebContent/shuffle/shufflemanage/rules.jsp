<%@page contentType="text/html; charset=gb2312" %>
<%@ page import="java.util.*" %>
<%@ page import="com.ecc.shuffle.rule.*" %>
<%
Map ruleSetMap=RuleBase.getInstance().ruleSets;
RuleSet ruleSet;//���򼯶���
Rule rule;//�������
String ruleSetId;//����ID
%>
<html>
<head>
<title>eChain���̹���</title>
<style type='text/css'>
A{font-size:12px}
BODY{font-size:12px;font-family:'����';color:#111;}
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
</style>
<script>
var originClassName;
function invertmenu(){
	if (event.srcElement.tagName == 'TD'){
		originClassName=event.srcElement.parentElement.className;
		event.srcElement.parentElement.className = 'trclass3';
	}
	else{
		originClassName=event.srcElement.parentElement.parentElement.className;
		event.srcElement.parentElement.parentElement.className = 'trclass3';
	}
}
function resumemenu(){
	if (event.srcElement.tagName == 'TD')
		event.srcElement.parentElement.className=originClassName;
	else
		event.srcElement.parentElement.parentElement.className =originClassName;
}
</script>
</head>
<body>
<p class="QZ_Ptitle">�����б�>></p>
<table class=tablemain cellspacing=1 cellpadding=0>
<tr class=trtitle>
<td width="10%">����ID</td><td width="20%">��������</td><td width="25%">��������</td><td width="8%">��������</td><td width="7%">���򼶱�</td><td width="7%">����״̬</td><td>����</td>
</tr>
<%
if(ruleSetMap==null||ruleSetMap.isEmpty()){
%>
<tr class=trclass><td colspan='7'>û�й�����Ϣ</td></tr>
<%
}else{
	Iterator it=ruleSetMap.keySet().iterator();
	boolean tr = true;
	while(it.hasNext()){
		ruleSetId=(String)it.next();
		ruleSet=(RuleSet)ruleSetMap.get(ruleSetId);
		out.print("<tr class=trclass4><td colspan=7>&nbsp;&nbsp;���򼯡�"+ruleSet.id+"��"+ruleSet.name+"��</td></tr>");
		tr = true;
		for(int i=0;i<ruleSet.rules.size();i++){
			rule=(Rule)ruleSet.rules.get(i);
			if(tr){
				out.print("<tr class=trclass onmouseout='resumemenu()' onmouseover='invertmenu()'>");
			}else{
				out.print("<tr class=trclass2 onmouseout='resumemenu()' onmouseover='invertmenu()'>");
			}
			out.print("<td>"+rule.id+"</td>");
			out.print("<td>"+rule.name+"</td>");
			out.print("<td>"+rule.desc+"</td>");
			out.print("<td>"+(rule.ruletype==null?"":rule.ruletype.equals("ruleformula")?"���ʽ":rule.ruletype.equals("ruletable")?"���߱�":rule.ruletype.equals("ruletree")?"������":rule.ruletype.equals("rulefree")?"���ɹ���":rule.ruletype.equals("ruleflow")?"������":"")+"</td>");
			out.print("<td>"+rule.levels+"</td>");
			out.print("<td>"+(rule.runStatus==0?"����":rule.runStatus==1?"<font color=red><b>������</b></font>":"<font color=red><b>ͣ��</b></font>")+"</td>");
			out.print("<td align=center><a href='rulevar.jsp?ruleSetId="+ruleSetId+"&ruleId="+rule.id+"'>������</a>");
			out.print("&nbsp;&nbsp;<a href='testRule.jsp?i_rulesetid="+ruleSet.ids+"&i_ruleid="+rule.id+"'>�������</a>");
			if(rule.ruletype!=null&&rule.ruletype.equals("ruletable")){
				out.print("&nbsp;&nbsp;<a href='ruletable.jsp?ruleSetId="+rule.ruleSet.ids+"&ruleId="+rule.id+"'>�鿴</a>");
				out.print("&nbsp;&nbsp;<a href='"+request.getContextPath()+"/ShuffleServlet?method=shufflemanage&actionType=exportRuleTable2Excel&ruleSetId="+rule.ruleSet.ids+"&ruleId="+rule.id+"'>����Excel</a>");
			}
			out.print("</td>");
			out.print("</tr>");
			tr=!tr;
		}
	}
}
%>
</table>
<br>
</body></html>
