<%@page contentType="text/html; charset=gb2312" %>
<%@ page import="java.util.*" %>
<%@ page import="com.ecc.shuffle.rule.*" %>
<%@ page import="com.ecc.shuffle.db.DbControl" %>
<%
String varid=request.getParameter("varid");
DbControl db=DbControl.getInstance();
Map hm=db.querySingle("select * from sf_var where var_id='"+varid+"'");
String varname=(String)hm.get("var_name");
String vartype=(String)hm.get("var_type");
String varvalue=(String)hm.get("var_value");
String vardesc=(String)hm.get("var_desc");
if(vartype==null)vartype="string";
if(vardesc==null)vardesc="";
%>
<html>
<head>
<title>eChain流程管理</title>
<META http-equiv=Content-Type content="text/html; charset=gb2312">
<style type='text/css'>
A{font-size:9pt}
BODY{font-size:9pt}
font{font-size:9pt}
td{font-size:9pt;padding-top:3px;padding-left:5px;padding-right:2px;padding-bottom:2px;}
.button
{
    font-size: 9pt;
    color: #000000;
    padding: 2px 0px 0px 0px;
	background-color:#F4F9FF;    
    height: 19px;
	BORDER: #B7BAC1 1pt solid;
	cursor:hand;
}
</style>
</head>
<body>
<form action="<%=request.getContextPath()%>/ShuffleServlet">
<input type="hidden" name="method" value="shufflemanage">
<input type="hidden" name="actionType" value="saveVar"/>

<fieldset><legend>全局变量信息</legend>
<table width="90%" border="0" cellspacing="1" cellpadding="3" align="center">
<tr><td width="40%">&nbsp;&nbsp;变量ID：<input type="text" name="varid" style="background-color:#fff;border:1px solid #c2c2c2;width:280px;height:22px" readonly="true" value="<%=varid%>"></td>
<td>变量名称：<input type="text" name="varname" style="background-color:#fff;border:1px solid #c2c2c2;width:280px;height:22px" readonly="true" value="<%=varname%>"></td></tr>
<tr><td colspan=2>
变量描述：<textarea name="vardesc" rows="5" cols="98" style="background-color:#fff;border:1px solid #c2c2c2;"><%=vardesc%></textarea><br>
</td></tr>
<tr><td width="40%">变量类型：
<select id="vartype" name="vartype" style="background-color:#fff;border:1px solid #c2c2c2;width:200px;height:22px" >
<%
out.print("<option"+(vartype.equals("string")?" selected":"")+">string</option>");
out.print("<option"+(vartype.equals("int")?" selected":"")+">int</option>");
out.print("<option"+(vartype.equals("float")?" selected":"")+">float</option>");
out.print("<option"+(vartype.equals("decimal")?" selected":"")+">decimal</option>");
out.print("<option"+(vartype.equals("boolean")?" selected":"")+">boolean</option>");
%>
</select>
</td>
<td>变量值：<input type="text" name="varvalue" style="background-color:#fff;border:1px solid #c2c2c2;width:300px;height:22px" value="<%=varvalue%>"></td></tr>
</table><br>
</fieldset>

<center><br>
<input type="submit" class="button" value="  提  交 " />
<input type="button" class="button" value="  返  回  " onclick="history.back()"/>
</center>
</form>
</body></html>
