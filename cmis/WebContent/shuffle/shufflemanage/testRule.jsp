<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="com.ecc.shuffle.db.DbControl" %>
<%@ page import="com.ecc.shuffleserver.ShuffleServlet" %>
<%@ page import="com.ecc.shufflestudio.editor.RuleSetWrapper" %>
<%@ page import="com.ecc.shufflestudio.editor.param.*" %>
<%@ page import="com.ecc.shuffle.rule.*" %>
<%@ page import="com.ecc.shuffle.upgrade.*" %>
<%
String i_rulesetid=request.getParameter("i_rulesetid");//调用的规则集ID
if(i_rulesetid==null)i_rulesetid="";
String i_ruleid=request.getParameter("i_ruleid");//调用的规则ID
if(i_ruleid==null)i_ruleid="";
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
input[type="text"]{border:1px solid #c2c2c2;}
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
	if(ruleid==""){
		alert("请选择所要调用的规则！");
	}else{
		document.getElementById("invokeSign").value="1";
		document.forms[0].submit();
	}
}
</script>
</head>
<body>
<form id="submitForm" action="./testRule.jsp">
<input type="hidden" id="invokeSign" name="invokeSign" value="1">
要调用的规则集：
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
</select>
&nbsp;&nbsp;&nbsp;&nbsp;规则： <select id="i_ruleid" name="i_ruleid"><option>===请选择===</option>
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
</select><br><br>
<table class=tablemain cellspacing=1 cellpadding=0>
<tr class=trtitle><th width="35%">参数名称</th><th width="20%">参数类型</th><th>参数值</th></tr>
<%
String invokeSign=request.getParameter("invokeSign");
boolean ivs=invokeSign==null?false:invokeSign.equals("1")?true:false;
Map outputMap=new HashMap();
if(i_rulesetid!=null&&i_rulesetid.length()>0){
	try{
		DbControl db=DbControl.getInstance();
		//获取RuleSetWrapper
		RuleSetWrapper ruleSetWra=null;
		String sql = "select * from sf_ruleSetInfo where name='" + i_rulesetid + "'";
		Map rsMap = db.querySingle(sql);
		String checkOut = (String) rsMap.get("checkout");
		String rsfilename=null;
		if(checkOut.equals("checkIn")){
			sql = "select * from sf_ruleSetVersion where name='"+ i_rulesetid + "' order by version desc";
			rsMap = db.querySingle(sql);
			String version = (String) rsMap.get("version");
			rsfilename=i_rulesetid + "." + version+".version";
		}else{
			rsfilename=i_rulesetid + ".ruleset";
		}
		if(rsfilename!=null&&rsfilename.length()>0){
			String path = ShuffleServlet.getWrapperFolderPath();
			File file = new File(path + "/" + rsfilename);
			if (file.exists()) {
				FileInputStream input = new FileInputStream(file);
				ObjectInputStream objInput = new ObjectInputStream(input);
				ruleSetWra = (RuleSetWrapper) objInput.readObject();
				objInput.close();
				input.close();
			}
		}		
		Map inputMap=new LinkedHashMap();//输入参数
		//获取规则集参数
		if(ruleSetWra!=null){
			ParametersWrapper paramWra = ruleSetWra.getParamWrapper();
			int paramListSize = paramWra.getParamListSize();
			Parameter param;
			String pv;
			for(int i=0;i<paramListSize;i++){
				param = paramWra.getParamListValue(i);
				if("输入".equals(param.getParamType())){
					out.print("<tr class=trclass><td>"+param.getName()+"</td><td>"+param.getDataType()+"</td><td>");
					pv=request.getParameter("$p_"+i);
					if(pv==null||!ivs)pv="";
					pv=new String(pv.getBytes("iso-8859-1") ,"GBK");
					if(ivs)
						inputMap.put(param.getName(),pv);
					if(param.getSelectListSize()==0){
						out.print("<input type='text' id='$p_"+i+"' name='$p_"+i+"' value='"+pv+"' style='width:300px'/>");
					}else{
						out.print("<select id='$p_"+i+"' name='$p_"+i+"'>");
						for(int j=0;j<param.getSelectListSize();j++){
							if(param.getSelectValue(j).equals(pv))
								out.print("<option selected>"+param.getSelectValue(j)+"</option>");
							else
								out.print("<option>"+param.getSelectValue(j)+"</option>");
						}
						out.print("</select>");
					}
					out.print("</td></tr>");
				}
			}
		}
		if(!inputMap.isEmpty()){
			//inputMap.put("担保方式", "保证(0.4),质押(0.6)");
			RuleSetInstance rsi=new RuleSetInstance(i_rulesetid,i_ruleid);
			outputMap=rsi.fireTargetRules(inputMap);
		}
	}catch(Exception e){
		e.printStackTrace();
	}
}
%>
</table>
<br><center><input type="button" class="button" value="&nbsp;提&nbsp;交&nbsp;" onclick="doSubmit()"></center>
调用规则后返回结果如下：<br>
<textarea name="outputMap" readonly="true" rows="8" cols="150" style="background-color:e1e1e1;border: 1px solid #b7b7b7;"><%=outputMap %></textarea>
</form>
</body>
</html>
