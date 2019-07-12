<%@page contentType="text/html; charset=gb2312" %>
<%@ page import="java.util.*" %>
<%@ page import="com.ecc.shuffle.rule.*" %>
<%@ page import="com.ecc.shuffle.db.DbControl" %>
<%
String varid=request.getParameter("varid");
DbControl db=DbControl.getInstance();
Map hm=db.querySingle("select * from sf_constant where constant_id='"+varid+"'");
String varname=(String)hm.get("constant_name");
String vartype=(String)hm.get("constant_type");
String varvalue=(String)hm.get("constant_value");
String vardesc=(String)hm.get("constant_desc");
if(vartype==null)vartype="string";
if(vardesc==null)vardesc="";
%>
<html>
<head>
<title>eChain流程管理</title>
<META http-equiv=Content-Type content="text/html; charset=gb2312">
<style type="text/css">
body{font-size:12px;}
fieldset{border:1px solid #c2c2c2;}
legend{color:#020000;}
.QZ_syTable{font-size:12px;}
.button{padding:3px 5px;border:1px solid #c2c2c2;background-color:#e1e1e1;cursor:pointer;}
</style>
</head>
<body>
<form action="<%=request.getContextPath()%>/ShuffleServlet">
<input type="hidden" name="method" value="shufflemanage">
<input type="hidden" name="actionType" value="saveConstant"/>

<fieldset><legend>全局常量信息</legend>
<table width="90%" border="0" cellspacing="1" cellpadding="3" align="center" class="QZ_syTable">
<tr><td width="40%">&nbsp;&nbsp;常量ID：<input type="text" name="varid" style="background-color:#fff;border:1px solid #c2c2c2;width:280px;height:22px" readonly="true" value="<%=varid%>"></td>
<td>常量名称：<input type="text" name="varname" style="background-color:#fff;border:1px solid #c2c2c2;width:280px;height:22px" readonly="true" value="<%=varname%>"></td></tr>
<tr><td colspan=2 valign="top">
常量描述：<textarea name="vardesc" rows="5" cols="98" style="background-color:#fff;border:1px solid #c2c2c2;"><%=vardesc%></textarea><br>
</td></tr>
<tr><td width="40%">常量类型：
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
<td>常量值：<input type="text" name="varvalue" style="background-color:#fff;border:1px solid #c2c2c2;width:300px;height:22px" value="<%=varvalue%>"></td></tr>
</table><br>
</fieldset>

<center><br>
<input type="submit" class="button" value="  提  交 " />
<input type="button" class="button" value="  返  回  " onclick="history.back()"/>
</center>
</form>
</body></html>
