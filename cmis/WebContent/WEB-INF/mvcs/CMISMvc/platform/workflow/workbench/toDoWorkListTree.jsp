<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="java.util.*"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<%
Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
Map applTypeMap = (Map)context.getDataValue("applTypeMap");
StringBuffer dicStrBuf = new StringBuffer() ;
if(applTypeMap!=null&&!applTypeMap.isEmpty()){
	Iterator it = applTypeMap.keySet().iterator();
	String enname, cnname;
	dicStrBuf.append("[");
	while(it.hasNext()){
		enname = (String)it.next();
		cnname=(String)applTypeMap.get(enname);
		dicStrBuf.append("{enname:\"").append(enname).append("\",cnname:\"").append(cnname).append("\"},");
	}
	dicStrBuf.replace(dicStrBuf.length()-1, dicStrBuf.length(), "]");
}
String dicStr = dicStrBuf.toString();
%>
<emp:page>
<html>
<head>
<title>待办事项流程申请类型</title>
<jsp:include page="/include.jsp" />
<script src="<emp:file fileName='scripts/dtree.js'/>" type="text/javascript" language="javascript"></script>
<!-- <link href="<emp:file fileName='styles/dtree.css'/>" rel="stylesheet" type="text/css" /> -->>

<link href="<emp:file fileName='styles/default/common.css'/>"
	rel="stylesheet" type="text/css" />
<link href="<emp:file fileName='styles/default/dataField.css'/>"
	rel="stylesheet" type="text/css" />
<link href="<emp:file fileName='styles/default/lianav3.css'/>"
	rel="stylesheet" type="text/css" />
<link href="<emp:file fileName='styles/default/lianav3_menuframe.css'/>"
	rel="stylesheet" type="text/css" />
<link href="<emp:file fileName='styles/default/moneystyle.css'/>"
	rel="stylesheet" type="text/css" />

<script src="<emp:file fileName='scripts/jquery-1.3.2.js'/>" type="text/javascript" language="javascript"></script>

<script type="text/javascript">
var menuOpTree;
var page = new EMP.util.Page();

function createTree(){
	var dicjsondata = <%=dicStr%> ;
	var baseUrl = '<emp:url action="getToDoWorkListByType.do" />';
	var treename = "menuOpTree";
	var d;
	try {
		d = new dTree(treename);
        d.add(0,-1,'流程申请类型');
        for(var i=0; i<dicjsondata.length; i++) {
        	var dic = dicjsondata[i];
        	d.add(i+1, 0, dic.cnname, baseUrl+'&WfiWorklistTodo.appl_type='+dic.enname, '', 'rightframe');
        }
		//d.add(1,0,'测试申请类型', baseUrl+'&applType=000', '', 'rightframe');
		//d.add(2,0,'测试申请类型02', baseUrl+'&applType=111', '', 'rightframe');
		
	} catch (e) {alert(e)};
	document.getElementById("Page_left_detail_page").innerHTML = d.toString();
	menuOpTree=d;
 };
 
 
 function moveLeftFrame(cssleft,cssright,yqti,slide,alt,obj) {
		
		var yq = document.getElementById(yqti);
		var sl = document.getElementById(slide);
		var alrt = document.getElementById(alt);
		
		if(yq.style.display=='none'){
			yq.style.display='inline';
			alrt.alt='点击隐藏';
			sl.className=cssleft;		
			obj.style.width="85%";		
		} else {
			obj.style.width="98.7%";
			yq.style.display='none';
			alrt.alt='点击显示';
			sl.className=cssright;
		}
	
	};
 </script>
 
</head>
<body onload="createTree()" bgcolor="#F8F7F7">

<div id="Page_left_detail_page"></div>
	<div id="image" style="display: none;"></div>
	<div id="Page_middle">
	<table width="2px" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td height="100%" valign="top">
			<div id="slide" class="slide_l"
				onclick="moveLeftFrame('slide_l','slide_r','Page_left_detail_page','slide','alt_slide',getElementById('Page_right'))">
			<img id="alt_slide" src="images/default/space.gif" width="10"
				height="100" alt="点击隐藏" /></div>
			</td>
		</tr>
	</table>
	</div>
	<div id="Page_right" style="width: 85%;" >
	<iframe id="rightframe" 
		name="rightframe" 
		src='<emp:url action="getToDoWorkListByType.do"/>&WfiWorklistTodo.appl_type=000' frameborder="0" scrolling="auto" height="100%" width="100%" >
	</iframe>
	</div>
<emp:select id="WfiWorklistTodo.appl_type" label="申请类型" dictname="ZB_BIZ_CATE" hidden="true"/>
</body>

</html>
</emp:page>
