<%@page contentType="text/html; charset=gb2312" %>
<%@ page import="java.util.*" %>
<%@ page import="com.ecc.shuffle.rule.*" %>
<%@ page import="com.ecc.shuffleserver.factory.ServerFactory" %>
<%
Map varMap=(Map)new ServerFactory().getVariable().getObj();
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
<p class="QZ_Ptitle">ȫ�ֱ���>></p>
<table class=tablemain cellspacing=1 cellpadding=0>
<tr class=trtitle>
<td width="20%">����ID</td><td width="20%">��������</td><td width="15%">��������</td><td>����ֵ</td><td width="10%">����</td>
</tr>
<%
if(varMap==null||varMap.isEmpty()){
%>
<tr class=trclass><td colspan='5'>û��ȫ�ֱ�����Ϣ</td></tr>
<%
}else{
	Iterator it=varMap.keySet().iterator();
	boolean tr = true;
	Map hm;
	while(it.hasNext()){
		hm=(Map)varMap.get(it.next());
		if(tr){
			out.print("<tr class=trclass onmouseout='resumemenu()' onmouseover='invertmenu()'>");
		}else{
			out.print("<tr class=trclass2 onmouseout='resumemenu()' onmouseover='invertmenu()'>");
		}
		out.print("<td>"+(String)hm.get("varid")+"</td>");
		out.print("<td>"+(String)hm.get("varname")+"</td>");
		out.print("<td>"+(String)hm.get("vartype")+"</td>");
		out.print("<td>"+(String)hm.get("varvalue")+"</td>");
		out.print("<td align=center><a href='"+request.getContextPath()+"/shuffle/shufflemanage/varEdit.jsp?varid="+(String)hm.get("varid")+"'>�޸�</a></td>");
		out.print("</tr>");
		tr=!tr;
	}
}
%>
</table>
<br>
</body></html>
