<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="java.io.File"%>
<%@page import="java.net.URL"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="com.yucheng.cmis.biz01line.qry.component.QryGenPageComponent"%>
<%
	 String jspFileName = (String)request.getParameter("jsp_file_name");
     String tempNo = (String)request.getParameter("temp_no");
     String pageurl=jspFileName+"/"+tempNo+".jsp";
     String toSelecturl=jspFileName+"/"+tempNo+"toSelect.jsp";
     String toOrderurl=jspFileName+"/"+tempNo+"toOrder.jsp";
     String jsurl="scripts/qry/"+jspFileName+"/"+tempNo+".js";
     URL url=QryGenPageComponent.class.getResource("");
     String path = url.getPath();
	 path=path.replaceAll("classes/com/yucheng/cmis/biz01line/qry/component/", ""); 
	 ResourceBundle res = ResourceBundle.getBundle("cmis");
	 String dir = res.getString("qry.jsp.path"); 
	 path = path.substring(0,path.length() - 1 );
	 //String allPath = path+"/"+dir+"/"+pageurl;
	 //String allPath = path+"/"+dir+"/"+toSelecturl;//生成目录换成共享盘下文件夹不需要获取根路径
	 String allPath = dir+"/"+toSelecturl;
	 String sys_name = System.getProperty("os.name");
	if("Windows XP".equalsIgnoreCase(sys_name)){   //WINDOWS
		allPath = allPath.substring(1);
	}
     File file = new File(allPath);
     if(file.exists()){
    	 
     
%>
<emp:page>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>查询</title>
<jsp:include page="/include.jsp" flush="true"/>
<link href="<emp:file fileName='styles/search.css'/>" rel="stylesheet" type="text/css" />
<script src="<emp:file fileName='scripts/qry/analyseQuery.js'/>" type="text/javascript" language="javascript"></script>
<script src="<emp:file fileName='<%=jsurl%>'/>" type="text/javascript" language="javascript"></script>
<script>
	var tabobj;
	function doOnLoad() {
	}
	function getDefaltUrl(){
	return '<emp:url action="xxxxxxx"/>';
	}
	function returnPage(){
		var url = '<emp:url action="queryQryTempletList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	}
	
</script>
</head>
<body onload="doOnLoad()">


	<form id="submitForm" action="<emp:url action = "doQuery.do"/>" method="POST"  >
<emp:tabGroup id="Qrytabs" mainTab="query_main" >
		<emp:tab id="query_main" label="查询条件">			
				<div class="formContainer">
				<jsp:include page="<%=pageurl%>"></jsp:include>
				<p class='col'><span class='titleCol'>查询条数</span><span class='inputCol'><input type=text id='INT_COUNT' name='INT_COUNT' maxLength=5 value='1000'></span></p>
			</div>
		<input type="hidden" name="ShowColumns" id="ShowColumns"/>
		<input type="hidden" name="OrderByColumns" id="OrderByColumns"/>
		</emp:tab>
		<emp:tab id="queryResult" label="查询结果显示">		
			<jsp:include page="<%=toSelecturl%>" flush="true" />
		</emp:tab>
		<emp:tab id="queryOrder" label="排序字段">
			<jsp:include page="<%=toOrderurl%>" flush="true" />
		</emp:tab>
		
</emp:tabGroup>
<div id ="buttonDiv">
<button onclick='doSubmit2()'>查 询</button>
<button onclick='returnPage()'>返 回</button>
</div>
</form>
</body>
</html>
</emp:page>
<%
     }else{%>
<body   class="page_content">

<br><br><br><br><br><br><br>
<div class="page_welcome">
	<div class="page_welcome_link">
		<div class="clear"></div>
	</div>
	<div class="page_welcome_content">
		<div class="page_welcome_content_top"></div>
		<div class="page_welcome_succ">
			<span class="page_welcome_red">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${context.message}
			</span><br>
			<TABLE align="center" width="300px">
        		<TR align="center">
          			<TD>
          				<span class="page_welcome_red"><font color="green">未生成查询页面，请联系管理员</font></span>
          			</TD>
        		</TR>
        		<TR><TD>&nbsp;</TD></TR>
      		</TABLE>
		</div>
	</div>
</body>
     <%}
%>