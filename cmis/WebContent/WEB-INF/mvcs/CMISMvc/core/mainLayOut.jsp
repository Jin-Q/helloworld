<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<html>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String skinId = (String)context.getDataValue("SkinId");
%>
<head>
<title>信贷管理信息系统(CMIS)</title>
<script type="text/javascript" language="JavaScript" src="<emp:file fileName='scripts/yui/yahoo/yahoo-min.js'/>"/> </script>
<script type="text/javascript" language="JavaScript" src="<emp:file fileName='scripts/yui/event/event-min.js'/>"/> </script>
<script type="text/javascript" language="JavaScript" src="<emp:file fileName='scripts/yui/connection/connection.js'/>"/> </script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<!-- ECC IDE required comment, please don't delete it! -->
<!-- toBeLayoutContent="true" mvcFile="designFiles/mvcs/CreditMvc/signOn.mvc" -->
<%if("01".equals(skinId)){ %>
<link href="<emp:file fileName='styles/default.css'/>" rel="stylesheet" type="text/css" />
<link href="<emp:file fileName='styles/dtree.css'/>" rel="stylesheet" type="text/css" />
<%}else if("02".equals(skinId)){ %>
<link href="<emp:file fileName='stylesGreen/default.css'/>" rel="stylesheet" type="text/css" />
<link href="<emp:file fileName='stylesGreen/dtree.css'/>" rel="stylesheet" type="text/css" />
<%}else if("03".equals(skinId)){ %>
<link href="<emp:file fileName='stylesPink/default.css'/>" rel="stylesheet" type="text/css" />
<link href="<emp:file fileName='stylesPink/dtree.css'/>" rel="stylesheet" type="text/css" />
<%}else if("04".equals(skinId)){ %>
<link href="<emp:file fileName='stylesRed/default.css'/>" rel="stylesheet" type="text/css" />
<link href="<emp:file fileName='stylesRed/dtree.css'/>" rel="stylesheet" type="text/css" />
<%}%>
<jsp:include page="/include.jsp" flush="true"/>
<script src="<emp:file fileName='scripts/dtree.js'/>" type="text/javascript" language="javascript"></script>
<script src="<emp:file fileName='scripts/creditMenu.js'/>" type="text/javascript" language="javascript"></script>

<script src="<emp:file fileName='scripts/jquery/jquery-1.4.4.min.js'/>" type="text/javascript" language="javascript"></script>
<script language="JavaScript">

	function moveLeftFrame(cssleft,cssright,yqti,slide_l,slide_r,alt,obj,obj_right) {
	
		var yq = document.getElementById(yqti);//左侧 二级菜单
		var sl = document.getElementById(slide_l);//左分侧
		var sr = document.getElementById(slide_r);//右分侧
		var alrt = document.getElementById(alt);//图片
 
		if(yq.style.display=='none'){
			yq.style.display='inline';
			alrt.alt='点击隐藏';
			sl.className=cssleft;	

			//判断右侧 分栏是否打开
			//if(sr.style.display == "none"){
			//	obj.style.width="90%";	
			//}else{
			//	obj.style.width="70%";	
				//obj_right.style.width="30%";
			//}
		} else {
			yq.style.display='none';
			alrt.alt='点击显示';
			sl.className=cssright;

			//判断右侧 分栏是否打开
			//if(sr.style.display == "none"){
			//	obj.style.width="90%";	
			//}else{
			//	obj.style.width="70%";	
				//obj_right.style.width="30%";
			//}
		}
	
	}

	function moveRightFrame(cssleft,cssright,yqti,slide_l,slide_r,alt,obj_left,obj_right){
		
		var yq = document.getElementById(yqti);//左侧 二级菜单
		var sl = document.getElementById(slide_l);//左分侧
		var sr = document.getElementById(slide_r);//右分侧
		var alrt = document.getElementById(alt);//右图片

		if(obj_right.style.display=='none'){
			alrt.alt='点击隐藏';
			sr.className=cssleft;	 
			obj_right.style.display="inline";
			obj_left.style.width="70%";    
			obj_right.style.width="30%";  	
		} else {

			//判断左侧 分栏是否打开
			if(yq.style.display=="none"){
				obj_left.style.width="100%";
			}
			else{
				obj_left.style.width="90%";
			}
			
			obj_right.style.display="none";
			alrt.alt='点击显示';
			sr.className=cssright;
		}
	}
		

	var timerID=null;
	var timerRunning=false;
	var selectValue="";
	var selectContent="";
	var menuElement;

	function stopClock()
	{
		if(timerRunning)
			clearTimeout (timerID);
		timerRunning=false;
	};
	
	function showTime()
	{
		var now=new Date();
		var year=now.getFullYear();
		var month=now.getMonth()+1;
		var date=now.getDate();
		var hours=now.getHours();
		var mins=now.getMinutes();
		var secs=now.getSeconds();
		var timeVal="";
		
		timeVal += year+'年';
		timeVal += ((month < 10) ? "0" : "") + month + "月";
		timeVal += date + "日";
		timeVal += ((hours < 12) ? "上午" : "下午");
		timeVal += ((hours<=12) ? hours : hours-12);
		timeVal += ((mins<10)? ":0" : ":")+ mins;
//		timeVal += ((secs<10)? ":0" : ":")+ secs;
		
		document.getElementById('time').innerHTML = timeVal;
		timerID = setTimeout("showTime();",1000);
		timerRunning = true;
	};
	
	function startClock()
	{
		stopClock();
		showTime();
	};

	//退出登录
	function exit(){
		var flag = window.confirm('您确定要退出么? ');
		if(flag)
			window.location='signOut.do';
		return;
	};
		
	function encodeURL(url) {
		if (url) {
			if(url.indexOf("http://") != -1)
				return url;
			var sample = "<emp:url action='[sample]'/>";
			var newurl = sample.replace("[sample]",url);
			var b = 0;
			var reg = new RegExp("(.*)\\?(.*)\\?(.*)");
			return newurl.replace(reg, "$1?$2&$3");
		}
	};
		
	function loadContent(){
		getMenuData("<emp:url action='taskInfo.do'/>");
		//startClock();
		changeTableSize();
		logInfo="营业日期：${context.OPENDAY}  登录机构：${context.organNo}-${context.organName}  登录人员：${context.loginusername}  登录角色：${context.loginRoleNameList}     ★★★ 版权所有(C) 裕民银行★★★";
		window.status =logInfo;
		//异步方法调用iframe里的公告栏，等所有页面加载完成后再加载公告栏页面
        var handleFailure = function(o){
            document.getElementById("infoFrame").src = "<emp:url action='queryCustomHomePage.do?menuId=customHomePage'/>";
            
        };
        var callback = {
            failure:handleFailure
        };
        //aa.do是随便写的一个action，为了跳转到handleFailure方法
        var obj1 = YAHOO.util.Connect.asyncRequest('POST',"aa.do", callback); 


        //皮肤默认选中
        var skin_itms = document.all.skin.options;
        var skin_id = '${context.SkinId}';
        for(k = skin_itms.length - 1 ; k > 0 ; k--){
    		if(skin_itms[k].value == skin_id){
    			skin_itms[k].selected=true; 
    		}
        }
		
	};	
	

	function doChange(){
		var skin = document.getElementById("skin").value;
		var actorno = '${context.currentUserId}';
		var url = '<emp:url action="AddOrUpdSkin.do"/>&actorno='+actorno+'&skinIdNew='+skin;
		url = EMPTools.encodeURI(url);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("切换失败!");
					return;
				}
				var flag=jsonstr.flag;	
				if(flag=="success"){
					window.location.reload();
				}else{
					alert("切换失败！");
				}
			}	
		};
		var handleFailure = function(o){
			alert("切换失败，请联系管理员");
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		}; 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}

	//帮助文档下载
	 function downloanPage(){
		var url = "<emp:url action='queryPubDocumentInfoList.do'/>&menuId=document&help=Y"; 
      	url=encodeURI(url); 
      	window.open(url,'PubDocumentInfoList','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	 }

	 window.onresize=function(){
		 changeTableSize();
	 }
	
	
	
	
</script>

<!--  <script language="JavaScript">
$(function(){
	$("#mainMenu_Box").hover(function(){$("#menu_lv1").show();},function(){$("#menu_lv1").hide();});
});
</script> -->

</head>

<%
  String systemid = "clpm";     
%>

<body onload="loadContent()"  style="overflow:auto">
<!-- container start -->
<div id="Container">
<div id="Header">
	<div class="QZ_topBox">
		<div id="logo">
			<span style="margin:25px 0px 0px 260px;font-size:24px;display:inline-block;width:300px;font-family:Microsoft Yahei;height:52px">|综合信贷管理系统</span>
		</div><!-- logo -->	
		<div id="infotopr">
			<a>皮肤</a>
			<select id='skin' onchange="doChange()" title='<%=skinId%>'>
				<option value=''>-----请选择-----</option>
				<option value='01'>蓝色</option>
				<option value='02'>绿色</option>
				<option value='03'>粉色</option>
				<option value='04'>红色</option>
			</select>
			<a href="#" onclick="downloanPage()" class="QZ_help">帮助</a><!-- help -->
			<a href="#" onclick="exit()">退出</a>
			<input type="hidden" id="emp_sid" value="${context.EMP_SID }"><!--  记录登录SESSION用于我的工作台、行长驾驶舱一级菜单跳转    不要覆盖！！！  -->
		</div>
	</div>
	<!--  <div id="mainMenu_Box">  -->
		<div id="menu_lv1" ><!-- 一级菜单 -->
			<div id="systemid" style="display:none" title="<%=systemid %>" ></div>
		</div>
		<!--  <div id="mainMenu_btn"></div>
	</div> -->
</div>
<!-- header end -->
<div id="clear" style="clear:both "></div>
<!-- center start -->
<div id="center">
  <table id="mainTable" width="100%" border="0" cellspacing="0" cellpadding="0" style="width:100%">
  <tr>
    <td valign="top" id="Page_left" class="Page_left">
    <div id="userinfo">欢迎您：${context.loginusername} </div>
    
    <div id="menu_lv2"> </div><!-- 二级菜单 -->
    
    <div id="Page_left_tree"></div>
    </td>
    <td width="10px" valign="middle"><div id="Page_middle">
		<table width="10px" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td width="10" height="100%" valign="top">
					<div id="slide_left" style='display:' class="slide_l" onclick="moveLeftFrame('slide_l','slide_r','Page_left','slide_left','slide_right','alt_slide',document.all('mainframe'),document.all('rightTd'));">
						<img id="alt_slide" src="images/space.gif" width="10" height="103">
					</div>
				</td>
       		</tr>
		</table>
	</div></td>
    <td id='mainframe' valign="top" style='display:'>
    	<iframe id="infoFrame" name="infoframe" src="welcome.htm" frameborder="0" scrolling="Auto" height="100%" width="100%"> </iframe>
    </td>
    
  </tr>
</table>
</div>
 <!--  <div id="footer">版权所有(C) 宇信易诚</div> -->
</div>
<!-- container end -->
</body>
</html>
<script type="text/javascript">

<!--
function changeTableSize(){
	var width = this.screen.width>1000?this.screen.width:1000;
	var table = document.getElementById("mainTable");
	table.width = width-28;

	//var winHeight = this.screen.availHeight;
	//alert(winHeight);
	//获得网页高度
	var winHeight = $(document).height();
	//alert(winHeight);
	
	//获得头部高度
	var header_height = $("#Header").height();
	
	//var memu_lv1 = $("#memu_lv1").height();
	var memu_lv1 = document.getElementById("menu_lv1").scrollHeight;
	//alert(memu_lv1);
	//获得版权信息页面高度
	//var footer_height = $("#footer").height();
	//alert("winHeight:"+winHeight +"  header_height:"+header_height + " footer_height:"+footer_height);
	//winHeight = winHeight - header_height - footer_height - 5;
	winHeight = winHeight - header_height - memu_lv1-3;
	//设置主内容页面高度
	//table.height= winHeight;
	
	$("#mainTable").height(winHeight);
	
	
	//if(winHeight>799){
	//	winHeight=winHeight -290;
	//}else{
	//	winHeight=768 -260;
	//}

	//var h=$(document).height();
	//$("#menu").height(h-10);
	//$("#rightpart").height(h-10);
}
//-->
</script>
