<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.yucheng.cmis.platform.riskmanage.RISKPUBConstant"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@ page import="com.ecc.emp.core.Context"%>
<%@ page import="java.util.*"%>
<%@ page import="com.ecc.emp.core.EMPConstance"%>
<%@ page import="com.ecc.emp.data.IndexedCollection"%>
<%@ page import="com.ecc.emp.data.KeyedCollection"%>
<%@ page import="com.yucheng.cmis.pub.util.Tools"%>
<%@ page import="java.sql.Connection"%>

<%
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	IndexedCollection icoll = (IndexedCollection)context.get("riskInspectList");
%>


<emp:page>
<html>
<head>
<title>风险拦截</title>
<jsp:include page="/include.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript">
	var resultI=0;
	var time ;
	function doOnLoad() {
		var _result = "${context.result}";

		if(_result == "<%=RISKPUBConstant.WFI_RISKINSPECT_RESULT_CANCEL%>"){
		   this.returnValue = "<%=RISKPUBConstant.WFI_RISKINSPECT_RESULT_CANCEL%>";
		   window.close();
		}
		if(_result == "<%=RISKPUBConstant.WFI_RISKINSPECT_RESULT_EXCEPTION%>"){
			   this.returnValue = "<%=RISKPUBConstant.WFI_RISKINSPECT_RESULT_EXCEPTION%>";
			   window.close();
		}
		<%
		if(icoll == null || icoll.size() == 0){
		%>	
			this.returnValue = "<%=RISKPUBConstant.WFI_RISKINSPECT_RESULT_CANCEL%>";
			window.close();
		<%
		}
		%>
		time = setInterval("showResult()",400);
	};
	function retSuc() {
	    this.returnValue = "<%=RISKPUBConstant.WFI_RISKINSPECT_RESULT_PASS%>";
	    window.close();
	}
	function doCloseMe() {
		var _result = "${context.result}";
		this.returnValue = _result;
		window.close();
	}
	function showResult(){
		
	<%
		if(icoll != null && icoll.size()>0){
	%>	
			var iMax = "<%=icoll.size()%>";
			while(resultI<iMax){		
				document.getElementById("result"+resultI).style.display = "block";
				var str = "img"+resultI;
				var time = 300;
				var name = document.getElementById(str).name;
				if("u" == name.substr(0,1)){
					setTimeout("unPass('"+str+"')", time);
					
				}else if("p" == name.substr(0,1)){
					setTimeout("pass('"+str+"')", time);
				}else{
					setTimeout("warn('"+str+"')", time);
				}
				
				resultI++;
				break;
			}
			if(iMax<=resultI)
				clearInterval(time);
	<%	
		}
		
	%>
	}
	function unPass(str){
		document.getElementById(str).src = "images/platform/riskmanage/action_delete.gif";
	}
	function pass(str){
		document.getElementById(str).src = "images/platform/riskmanage/action_check.gif";
	}
	function warn(str){
		document.getElementById(str).src = "images/platform/riskmanage/action_warn.gif";
	}

	//执行外部链接
	function goto(url){
		if(url==null)
			return;
		var EMP_SID='${context.EMP_SID}';
		url = EMPTools.encodeURI(url);
		url=url+'&EMP_SID='+EMP_SID;
		EMPTools.openWindow(url,'newwindow');
	}
</script>
</head>
<body class="page_content" onload="doOnLoad()">

<%
	if(icoll != null && icoll.size() != 0) {
		out.println("<br/>");
		out.println("<table id='riskTab' class='emp_table'>");
		out.println("<tr class='emp_table_title'>");
		out.print("<td style='display:none'>风险拦截编号</td><td>风险拦截项目说明</td><td>风险拦截结论</td><td>是否通过</td><td style='display:none'>风险等级</td>");
		out.println("</tr>");
		for(int i=0;i<icoll.size();i++) {
			KeyedCollection	kcoll = (KeyedCollection)icoll.get(i);
			String passState = (String)kcoll.get("pass_state");
			String riskLevel = (String)kcoll.get("risk_level");
			System.out.println("TETTTTTTTTT:"+passState+":"+riskLevel);
			String linkUrl=(String)kcoll.get("link_url");
			out.println("<tr style='display:none' id='result"+i+"' class='"+ (i%2==0?"row1":"row2") +"'>");
			//out.println("<tr>");
			out.println("<td style='display:none'>"+kcoll.get("item_id")+"</td>");	//风险拦截编号
			out.println("<td >"+kcoll.get("item_name")+"</td>");


			//是否通过2是不通过，显示一个红叉，非2是通过显示一个绿勾   
			if("2".equals(passState)&&"2".equals(riskLevel)){
				out.println("<td ><A href=\"javascript:goto(\'"+linkUrl+"\')\"><span style=\"color: red;\">"+kcoll.get("item_desc")+"</span></A></td>");
				String id = "img"+i;
				String name = "unPass"+i;
				//out.println("<td><img alt='没通过' src='images/workflow/action_delete.gif'></td>");
				out.println("<td ><img id = '"+id+"' alt='拦截' src='images/wait.gif' name='"+name+"'></td>");
			}
			else if("1".equals(passState)||"1".equals(riskLevel)){
				out.println("<td >"+kcoll.get("item_desc")+"</td>");
				String id = "img"+i;
				String name = "pass"+i;
				//out.println("<td><img alt='通过' src='images/workflow/action_check.gif'></td>");
				out.println("<td ><img id = '"+id+"' alt='不拦截' src='images/wait.gif' name = '"+name+"'></td>");
			}	
			else{
				out.println("<td ><A href=\"javascript:goto(\'"+linkUrl+"\')\"><span style=\"color: #A6A600;\">"+kcoll.get("item_desc")+"</span></A></td>");
				String id = "img"+i;
				String name = "warn"+i;
				out.println("<td ><img id = '"+id+"' alt='不拦截但警告' src='images/wait.gif' name = '"+name+"'></td>");
			}	
			
			out.println("<td style='display:none'>"+Tools.getComCdeCnName(context,"STD_ZB_RISK_LEVEL",riskLevel)+"</td>");	//风除等级的字典项转换
			out.println("</tr>");
		}
		out.println("</table>");
	}
%>
    <br><br>
   <div align="center">
		<br>
		<emp:button id="closeMe" label="关闭" />
	</div>
 </body>
</html>
</emp:page>