<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.yucheng.cmis.biz01line.qry.util.ReadAnalyseFile"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%
    String IN_TEMPNO = request.getParameter("IN_TEMPNO");
	String IN_JSP_FILE_NAME = request.getParameter("IN_JSP_FILE_NAME");
	String tempName = request.getParameter("TEMP_NAME");
	
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	if(context.containsKey("TEMP_NAME")){
		tempName = (String)context.getDataValue("TEMP_NAME");
	}
	if(null==tempName || "".equals(tempName)){
		tempName = (String) request.getParameter("temp_name");
		tempName = URLDecoder.decode(tempName,"UTF-8");
		//tempName = new String(tempName.getBytes("ISO8859-1"),"GBK");
	}
	String templetType = (String)request.getParameter("templet_type");
	String link ="";
	if(context.containsKey("link")){
		link = (String)context.getDataValue("link");
	}
	if(null==link || "".equals(link)){
		link = (String) request.getParameter("link");
	}
			
%>

<%@page import="javax.mail.Session"%>

<%@page import="com.ecc.emp.core.Context"%><html>
<head>
<title>查询分析展示页面</title>
<link type="text/css" rel="stylesheet" href="<emp:file fileName='styles/dataTable.css'/>" />
<script type="text/javascript" language="javascript" src="<emp:file fileName='scripts/jquery-1.3.2.js'/>"></script>
<%
	
    ReadAnalyseFile read = new ReadAnalyseFile();
	String s_start = (String) request.getParameter("start");
	String s_end = (String) request.getParameter("end");
	String pageIndex = (String) request.getParameter("pageIndex");

	int start = 1;
	int end = 20;
	int size = 0;
	if (s_start != null && s_end != null) {
		start = Integer.parseInt(s_start);
		end = Integer.parseInt(s_end);
	}

	if (pageIndex == null) {
		pageIndex = "1";
	}

	String fileName = (String) request.getAttribute("fileName");
	if(null==fileName || "".equals(fileName)){
		fileName = (String) request.getParameter("fileName");
	}
	
	if(null==fileName || "".equals(fileName)){
		throw new Exception("查询分析获取查询结果错误，找不到结果对应的文件名！");
	}
	int pageSize = 0;

	if (fileName != null) {
		String sizeStr = fileName.substring(fileName.lastIndexOf("@") + 1);
		size = Integer.parseInt(sizeStr);
		pageSize = (size+1) / 20 + ((size+1) % 20 > 0 ? 1 : 0);
	}
	
	//read.genSum(fileName, start, size,"","","","");
	
	String htmlStr = read.genHtml(fileName, start, end,"","","","",size);
	
	//		"</td><td nowrap>", "</tr><tr><td nowrap>",
	//		"</B></td><td nowrap><B>", "</tr><tr><td nowrap>");
	//int idx = htmlStr.lastIndexOf("<td");
	//htmlStr = htmlStr.substring(0, idx);
	//htmlStr = htmlStr.replaceAll("<td nowrap></tr>", "</tr>");
	//htmlStr = htmlStr.replaceAll("<B></tr>", "</tr>");
	//htmlStr = htmlStr.replaceAll("</B></td><td nowrap></tr>","</B></td></tr>");
	htmlStr = "<table class='emp_table' width='100%' border='0' style='overflow:auto;clear:both;display:block;' cellPadding='9'"
			+ " cellSpacing='0' borderColor='#cccccc' bgColor='#ffffff' "
			+ "id='DataTable'>"
			+ htmlStr + "</table>";
%>
<script type="text/javascript" charset="UTF-8">
        var color;
        $(document).ready(function(){
            $("#DataTable tr").attr("selected","0");
            $("#DataTable tr:even").not(":first").css("background","#EEEEFF");//#EEEEFF
            $("#DataTable tr:odd").css("background","transparent");//#97CAFF
        
            $("#DataTable tr").not(":first").click(function(){
                $("#DataTable tr:even").not(":first").css("background","#EEEEFF");//#EEEEFF
                $("#DataTable tr:odd").css("background","transparent");//#97CAFF
                
                var selected = $(this).attr("selected");
                if(selected == 1){
                    $(this).attr("selected","0");
                    color=$(this).css("background");
                }
                else{
                    $("#DataTable tr").attr("selected","0");
                    $(this).attr("selected","1");
                    $(this).css("background","#97CAFF");
                    color=$(this).css("background");
                }
            }).mouseover(function(){
                color = $(this).css("background");
                $(this).css("background","#97CAFF");
            }).mouseout(function(){
                $(this).css("background",color);
            });
        });
			
</script>
<jsp:include page="/include.jsp" flush="true" />

<style type="text/css">
	#pageDiv {
		text-align: right;
	}
	#textInfo{
		font-size: 13px;
		text-align：right;
		vertical-align: middle;
	} 
	.trHover {
		cursor: default;
	}
	
	#buttonDiv {
		text-align: center;
	}
	.redf{
		color:red;
	}
	.clear{
		clear:both;
	}
	
	/** 2014-03-29 唐顺岩 修改超链接样式  **/
	a:link {font-size: 12px;color: #2233FF;	text-decoration: underline;}
	a:visited {font-size: 12px;	color: #2233FF;	text-decoration: underline;}
	a:hover {font-size: 12px; color: red; text-decoration: underline;}
	a:active {font-size: 12px; color: #2233FF; text-decoration: underline;}
</style>
</head>
<body >

<form id="form1" action="<cmis:url url='turnAnalysePage.do'/>" method="post">
<fieldset><b><legend style="font-size:15px;color:#04477c;"><%=tempName%></legend></b>
<div id="box" style="width:100%;padding:2px 2px 0px 2px">
 <%
     out.print(htmlStr);
 %>
 
 <div class="clear"></div>
<div id="pageDiv">

<span id="textInfo">
查询结果为:   当前为第<span class="redf"> <%=pageIndex%></span>/<span class="redf"><%=pageSize%> </span>页，共<span class="redf"> <%=size%> </span>条记录  每页显示<span class="redf"> 20 </span>条   </span>
<!--  查询结果为:   总共<span class="redf"> <%=size%> </span>条记录  <span class="redf"> <%=pageSize%> </span>页 每页显示<span class="redf"> 20 </span>条  当前为第<span class="redf"> <%=pageIndex%> </span>页 </span> -->
<button id="first" onclick="firstPage()">首 页</button>
<button id="previous" onclick="upPage()">上一页</button>

<button id="next" onclick="nextPage()">下一页</button>
<button id="last" onclick="lastPage()">尾 页</button>

<input style="width:50px" id="directtoinput" onblur="directChange()" />
&nbsp;<button id="sayHello" onclick="goToNew()">跳转</button>
<button id="loadExcel" onclick="toLoadExcel()" class="button80">导出为Excel</button>

<button id="loadCsv" onclick="toLoadCsv()" class="button100">导出为Csv(推荐)</button>
</div>
 </div>
</fieldset>
<%if(null!=link && "Y".equals(link)){  %>
<% }else{ %> 
<div id="buttonDiv">
 <button onclick="doClose()" id="back">返回</button>   
</div>
<%} %>

	<input type="hidden" name="title" value='<%=tempName%>' /> 
	<input type="hidden" name="fileName" value='<%=fileName%>' /> 
	<input type="hidden" name="start" value='<%=start%>' /> 
	<input type="hidden" name="end" value='<%=end%>' /> 
	<input type="hidden" name="pageIndex" value='<%=pageIndex%>' /> 
	<input type="hidden" name="targetPage" value='' />
	</form>
<script>

function sendRedirect(){
   	var url = "<emp:url action='turnAnalysePage.do'/>&start="+document.all.start.value+
    "&end="+document.all.end.value+"&pageIndex="+document.all.pageIndex.value+ "&title="+document.all.title.value+"&fileName="+document.all.fileName.value
    +"&IN_TEMPNO=<%=IN_TEMPNO%>&IN_JSP_FILE_NAME=<%=IN_JSP_FILE_NAME%>&temp_name=<%=tempName%>&templet_type=<%=templetType%>&link=<%=link%>";
   url = encodeURI(encodeURI(url));
   window.location = url;
}


function nextPage(){
	var pagesize=<%=pageSize%>;
    var pageIndex=<%=pageIndex%>;
    if(pageIndex==pagesize){
    	 alert('已经是最后一页');
         return;
    }
    document.all.start.value = Math.floor(document.all.start.value) + 20;
    document.all.end.value = Math.floor(document.all.end.value) + 20;
    document.all.pageIndex.value = Math.floor(document.all.pageIndex.value) + 1;   
    sendRedirect();
}

function upPage(){
    if( document.all.start.value == '1' ){
         alert('已经是首页');
         return;
    }
    document.all.start.value = Math.floor(document.all.start.value) - 20;
    document.all.end.value = Math.floor(document.all.end.value) - 20;
    document.all.pageIndex.value = Math.floor(document.all.pageIndex.value) -1;
    
    sendRedirect();
}

function firstPage(){
	if( document.all.start.value == '1' ){
        alert('已经是首页');
        return;
   }
    document.all.start.value = "1";
    document.all.end.value = "20";
    document.all.pageIndex.value = "1";
    
    sendRedirect();
}
function lastPage(){
    var pagesize=<%=pageSize%>;
    var pageIndex=<%=pageIndex%>;
    if(pageIndex==pagesize){
    	 alert('已经是最后一页');
         return;
    }
    document.all.targetPage.value = pagesize;
    document.all.start.value = (Math.floor(document.all.targetPage.value)-1)*20 + 1;
    document.all.end.value = Math.floor(document.all.targetPage.value)*20;
    document.all.pageIndex.value = Math.floor(document.all.targetPage.value); 
    sendRedirect();
}

function goToNew(){
    if( document.all.directtoinput.value == '' ){
        alert("请输入要跳转的页面！");
        return;
    }

    document.all.targetPage.value = Math.floor(document.all.directtoinput.value);
    document.all.start.value = (Math.floor(document.all.targetPage.value)-1)*20 + 1;
    document.all.end.value = Math.floor(document.all.targetPage.value)*20;
    document.all.pageIndex.value = Math.floor(document.all.targetPage.value);
    
    sendRedirect();
}

function directChange(){
   var value = document.all.directtoinput.value;
   if( value == "" ){
       return;
   }
   if( isNaN(value) ){
      alert("输入必须为数字");
      document.all.directtoinput.value = "";
      return;
   }
   value = Math.floor(value);
   var pageSize=<%=pageSize%>;
   if(value>pageSize){
	   alert("没有该页数据");
		 value=pageSize;
	}
   if( value < 1 || value > 5000 ){
      document.all.directtoinput.value = "";
   }else{
      document.all.directtoinput.value = value;
   }
}

 function toLoadExcel() {

	var QueryID = document.all.fileName.value;

	var url = "<emp:url action='loadExcelOrCsv.do'/>&fileName="+QueryID+"&loadType=EXCEL";
	
	url = encodeURI(url);

    window.open(url,'','width=550,height=400,top=100,left=200,scrollbars=yes');

}

 function toLoadCsv() {

	var QueryID = document.all.fileName.value;

	var url = "<emp:url action='loadExcelOrCsv.do'/>&fileName="+QueryID+"&loadType=CSV";

    url = encodeURI(url);

    window.open(url,'','width=550,height=400,top=100,left=200,scrollbars=yes');

}

function doReturn(){
//	var paramStr = "&temp_no=<%=IN_TEMPNO%>&jsp_file_name=<%=IN_JSP_FILE_NAME%>&temp_name=<%=tempName%>&templet_type=<%=templetType%>";
	//var url = '<emp:url action="getQueryPage.do"/>'+ paramStr;
		//	url = encodeURI(url);
			//window.location = url;
}

function doClose(){
   history.go(-1);
}

/**内部链接执行方法 **/
function showLink(_tempno,param){
	var url = '<emp:url action="doQuery.do"/>?IN_TEMPNO='+_tempno+"&param="+param+"&link=Y&ShowColumns=&OrderByColumns=&INT_COUNT=50000";
	url = EMPTools.encodeURI(url);
	//window.showModalDialog(url,"","dialogHeight:700px;dialogWidth:1200px;center:1");
	window.open(url,"showLink","");
}
</script>

</body>
</html>
