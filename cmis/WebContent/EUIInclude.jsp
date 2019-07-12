<%@page import="com.yucheng.cmis.platform.userparam.UserparamConstance"%>
<%@page import="com.yucheng.cmis.pub.util.CMISPropertyManager"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.yucheng.cmis.pub.language.LanguageManager"%>
<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp-jquery.tld" prefix="emp"%>
<%

String userSkin = null;
String resolution = null;
String layout = null;
String userLang = null;
try{
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	//皮肤 
	userSkin = (String)context.getDataValue(UserparamConstance.ATTR_USERPARAM_SKIN);
	//分辨率
	resolution = (String)context.getDataValue(UserparamConstance.ATTR_USERPARAM_RESOLUTION);
	//布局
	layout = (String)context.getDataValue(UserparamConstance.ATTR_USERPARAM_LAYOUT);
    //用户语言
    userLang = LanguageManager.getUserLanguage(request);
    request.setAttribute("page_userLang", userLang);
}catch(Exception e){
	userSkin = "default";
	resolution = "auto";
	layout = "default";
}
String sysmsgInterval = null;
try{
	sysmsgInterval = CMISPropertyManager.getInstance().getPropertyValue("sysmsg_interval");
}catch(Exception e){
	
}
String disableRightClickMenu = null;
try{
    disableRightClickMenu = CMISPropertyManager.getInstance().getPropertyValue("disable_right_menu");
}catch(Exception e){
}
%>
<script type="text/javascript">
var contextPath="<%=request.getContextPath()%>";
//皮肤路径全局参数
var _userSkin="<%=userSkin%>";
var empId = '${context.EMP_SID}';
<%if(sysmsgInterval!=null){
	%>
var _sysmsgInterval="<%=sysmsgInterval%>";
	<%
}%>
<%if(disableRightClickMenu!=null){
	%>
var _disableRightClickMenu="<%=disableRightClickMenu%>";
	<%
}%>
</script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script
	src="<emp:file fileName='scripts/eui/jqueryeasyui/jquery-1.8.3.min.js'/>"
	type="text/javascript"></script>
<script
	src="<emp:file fileName='scripts/eui/jqueryeasyui/jquery.easyui.min.js'/>"
	type="text/javascript"></script>
<!-- 修改加载顺序，jquery.ycmis.css需要在easyui.css之前加载，easyui.css内需要设定字体，若在之后设置会被 jquery.ycmis.css 覆盖-->
<link href="<emp:file fileName='styles/eui/common/jquery.ycmis.css'/>"
	rel="stylesheet" type="text/css" />
<script type="text/javascript">
var layout = '<%=layout%>';
var resolution = '<%=resolution %>';
if('auto'==resolution) {
	/* var clientWidth = $(top.window).width();
	if(clientWidth < 1900) {
		resolution = '1280';
	}else if(clientWidth < 2800) {
		resolution = '1920';
    }else {
    	resolution = '2880';
    } */
}
if('metro'==layout) {
	document.write("<link rel='stylesheet' href='styles/eui/skin/metro/"+_userSkin+"/"+resolution+"/easyui.css' />");
}else if('concise'==layout || 'accordionmenu'==layout){
	document.write("<link rel='stylesheet' href='styles/eui/skin/"+layout+"/"+_userSkin+"/easyui.css' />");
	document.write("<link rel='stylesheet' href='styles/mainpage/"+layout+"/"+_userSkin+"/skin.css' />");
}else{
	document.write("<link rel='stylesheet' href='styles/eui/skin/origin/"+_userSkin+"/easyui.css' />");
	document.write("<link rel='stylesheet' href='styles/mainpage/origin/"+_userSkin+"/skin.css' />");
}
/* 加载按钮样式 */
if('concise'==layout){
	document.write("<link rel='stylesheet' href='styles/eui/skin/concise/icons/icons.css' />");
}else{
	document.write("<link rel='stylesheet' href='styles/eui/skin/icons/icons.css' />");
}
</script>
<%-- 根据不同语言添加不同的国际化js文件--%>
<script
	src="<emp:file fileName='scripts/eui/jqueryeasyui/locale/easyui-lang-zh_cn.js'/>"
	type="text/javascript"></script>
<script
	src="<emp:file fileName='scripts/eui/jqueryeasyui/locale/easyui-lang-${page_userLang }.js'/>"
	type="text/javascript"></script>
<script
	src="<emp:file fileName='scripts/eui/cuslocale/cus-lang-zh_cn.js'/>"
	type="text/javascript"></script>
<script
	src="<emp:file fileName='scripts/eui/cuslocale/cus-lang-${page_userLang }.js'/>"
	type="text/javascript"></script>

<script
	src="<emp:file fileName='scripts/eui/jqueryeasyui/jquery.edatagrid.js'/>"
	type="text/javascript"></script>
<script
	src="<emp:file fileName='scripts/eui/jqueryeasyui/datagrid-dnd.js'/>"
	type="text/javascript"></script>
<script
	src="<emp:file fileName='scripts/eui/jqueryeasyui/datagrid-groupview.js'/>"
	type="text/javascript"></script>
<script
	src="<emp:file fileName='scripts/eui/jqueryeasyui/datagrid-bufferview.js'/>"
	type="text/javascript"></script>
<script
	src="<emp:file fileName='scripts/eui/jqueryeasyui/datagrid-defaultview.js'/>"
	type="text/javascript"></script>
<script
	src="<emp:file fileName='scripts/eui/jqueryeasyui/datagrid-detailview.js'/>"
	type="text/javascript"></script>
<script
	src="<emp:file fileName='scripts/eui/jqueryeasyui/datagrid-scrollview.js'/>"
	type="text/javascript"></script>
<script src="<emp:file fileName='scripts/eui/utils/jquery.ycmis.js'/>"
	type="text/javascript"></script>
<script src="<emp:file fileName='scripts/eui/utils/EMPTools.js'/>"
	type="text/javascript"></script>
<script
	src="<emp:file fileName='scripts/eui/easyuiplugins/ycmis.popbox.js'/>"
	type="text/javascript"></script>
<script
	src="<emp:file fileName='scripts/eui/easyuiplugins/ycmis.checkbox.js'/>"
	type="text/javascript"></script>
<script
	src="<emp:file fileName='scripts/eui/easyuiplugins/ycmis.dateymbox.js'/>"
	type="text/javascript"></script>
<script src="<emp:file fileName='scripts/eui/ext/ycmis.easyui.ext.js'/>"
	type="text/javascript"></script>
<script
	src="<emp:file fileName='scripts/eui/easyuiplugins/ycmis.numtx.js'/>"
	type="text/javascript"></script>
<script
	src="<emp:file fileName='scripts/eui/easyuiplugins/ycmis.checkbox.js'/>"
	type="text/javascript"></script>
<!--引入weboffice的js  -->
<script
	src="<emp:file fileName='scripts/platform/report/weboffice/weboffice.js'/>"
	type="text/javascript"></script>
<!--win8 metro风格下根据不同分辨率加载不同的js文件  ycmis.content.js放在ycmis.easyui.ext.js后，以覆盖设置表头及表格高度的js方法-->
<script>
if('metro'==layout){
	if(resolution == '1280') {
		document.write("<script src='scripts/mainpage/metro/1280/ycmis.content.js' type='text/javascript'></sc" + "ript>");
	}else if(resolution == '1920'){
		document.write("<script src='scripts/mainpage/metro/1920/ycmis.content.js' type='text/javascript'></sc" + "ript>");
	}
}
</script>
<!-- icon按钮鼠标悬浮 -->
<script
	src="<emp:file fileName='scripts/eui/easyuiplugins/ycmis.button.js'/>"
	type="text/javascript"></script>
<!-- 下拉列表框联动 -->
<link href="<emp:file fileName='styles/eui/common/ycmis.linkage.css'/>"
	rel="stylesheet" type="text/css" />
<script
	src="<emp:file fileName='scripts/eui/easyuiplugins/ycmis.linkage.js'/>"
	type="text/javascript"></script>
<!-- 扩展下拉列表框 -->
<script
	src="<emp:file fileName='scripts/eui/ext/ycmis.combogrid.ext.js'/>"
	type="text/javascript"></script>
<!-- 扩展数字微调 -->
<script
	src="<emp:file fileName='scripts/eui/ext/ycmis.numberspinner.ext.js'/>"
	type="text/javascript"></script>

<!-- 用于高度显示代码，项目中可以删除  START-->
<link
	href="<emp:file fileName='scripts/plugins/snippet/jquery.snippet.css'/>"
	rel="stylesheet" type="text/css" />
<script
	src="<emp:file fileName='scripts/plugins/snippet/jquery.snippet.js'/>"
	type="text/javascript"></script>
<!-- 用于高度显示代码，项目中可以删除  END-->

<!-- 用于表格显示列的功能扩展 update by WangYang 2016-06-28 -->
<script type="text/javascript"
	src="<emp:file fileName='scripts/eui/ext/ycmis.datagrid.ext.js'/>"></script>

<!-- win8 metro风格磁贴 -->
<link href="scripts/plugins/gridster/skin/jquery.gridster.min.css"
	rel="stylesheet" type="text/css" />
<link href="scripts/plugins/gridster/skin/gridster.css" rel="stylesheet"
	type="text/css" />
<script
	src="<emp:file fileName='scripts/plugins/gridster/jquery.gridster.min.js'/>"
	type="text/javascript"></script>
<script
	src="<emp:file fileName='scripts/plugins/gridster/jquery.gridster.with-extras.min.js'/>"
	type="text/javascript"></script>

<!-- 海峡信贷项目  by chenxh -->
<script src="<emp:file fileName='scripts/common/dataCheck.js'/>"
	type="text/javascript"></script>
