<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>首页Resizable</title>

<jsp:include page="/include.jsp" flush="true"/>
<link href="styles/homePage/inettuts.js.css" rel="stylesheet" type="text/css" /> 
<style type="text/css">
.required{
	color: red;
};
.emp_field_select_select {
border: 1px solid #b7b7b7;
text-align:left;
width:210px;
}
.emp_field_text_input {
border: 1px solid #b7b7b7;
text-align:left;
width:210px;
}

.color-blue   {background:#e1c299;}

.widget {
    margin: 10px 5px 0 5px;
    padding: 2px;
    -moz-border-radius: 4px;
    -webkit-border-radius: 4px;
}

.widget-head {
    color: #000093;
}

.titleClass {
	width: 100%;
    color: #551f0a;
    float: left;
    font-size: 18px;
    font-weight: bold;
    
}

</style>

<% 
	String title = request.getParameter("gadgetTitle");
%>
<script type="text/javascript">

	/*--user code start--*/
		function loadGadget(){
			//设置标题
			var titleObj = document.getElementById("gadgetTitle");
			var title = "${context.gadgetTitle}";
			title = decodeURI(title);
			titleObj.innerHTML=title;
			
			//设置iframe URL
			var gadgetUrl = "${context.gadgetUrl}"+"?EMP_SID="+"${context.EMP_SID}";
			var iframeObj = document.getElementById("gadgetId");
			iframeObj.src = gadgetUrl;
		}

	
	function doReturn(){
		var url='<emp:url action="queryCustomHomePage.do" />';
		url = EMPTools.encodeURI(url);
		window.location = url;
		
	}

	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="loadGadget()"> 
	<ul id="column" class="column">
           <li class="widget color-blue">  
               <div class="widget-head">
					<table>
						<tr>
							<td id="gadgetTitle" class="titleClass"></td>
							<td id="gadgetResize" class="resize">
								<img src="images/homePage/minimize.jpg" style="vertical-align:bottom;CURSOR:hand" alt="还原" onclick="doReturn()"/></td>
						</tr>
					</table>
                   
                   
               </div>
               <div class="widget-content">
                   <iframe id="gadgetId" name="" src="" height="100%"  width="100%"  frameborder="0" scrolling="Auto" ></iframe>
               </div>
           </li>
       </ul>
</body>
</html>
</emp:page>

