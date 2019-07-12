<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<html>
<head>
<title>信贷管理系统</title>
<jsp:include page="/include.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<link  href="<emp:file fileName='scripts/ext/resources/css/ext-all-notheme.css'/>" rel="stylesheet" type="text/css" />
	<link href="<emp:file fileName='styles/ext/base.css'/>" rel="stylesheet" type="text/css" />
	<script type="text/javascript">
		baseUrl="<emp:file fileName=''/>";
	</script>
	<script type="text/javascript"	src="<emp:file fileName='scripts/ext/ext-base.js'/>"></script>
	<script type="text/javascript"	src="<emp:file fileName='scripts/ext/ext-all.js'/>"></script>
	<script type="text/javascript"	src="<emp:file fileName='scripts/ext/ext-lang-zh_CN.js'/>"></script>
	<script type="text/javascript"	src="<emp:file fileName='scripts/ext/treeFilter.js'/>"></script>
	<script type="text/javascript"	src="<emp:file fileName='scripts/ext/cmis-util.js'/>"></script>
	<script language="JavaScript">
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
	Ext.BLANK_IMAGE_URL ="<emp:file fileName='scripts/ext/resources/images/default/s.gif'/>";
	/*
		20120426周凤雷
		bizlineList不再由页面传入，改为直接从context中获取
	*/
	//curbizLineList = "${context.bizlineList}";
	//bizLinecfgUrl = "<emp:url action='queryBizLineCfg.do'/>&bizLine=" + curbizLineList; 
	bizLinecfgUrl = "<emp:url action='queryBizLineCfg.do'/>";
	resTreeUrl="<emp:url action='resourceTree.do'/>";
	actTreeUrl = "<emp:url action='actionTree.do'/>"
	rightTreeUrl = "<emp:url action='rightTree.do'/>"
	roleTreeUrl = "<emp:url action='roleTree.do'/>&lineShow=true" ;//增加一个参数表明是否显示 条线
	grantRightUrl="<emp:url action='grantRight.do'/>" ;

	//新增模块对应的url
	addResUrl = "<emp:url action='getAddResourcePage.do' />";
	//修改模块对应的url
	updResUrl = "<emp:url action='resourceDetails.do' />";
	//删除模块对应的url
	delResUrl = "<emp:url action='delResource.do' />";
	//记录集权限配置对应的url
	restrictUrl = "<emp:url action='getRestrictupdatePage.do' />";

    addActionUrl = "<emp:url action='getAddActionPage.do' />";
    upActionUrl = "<emp:url action='getEditActPage.do' />";
    delActionUrl = "<emp:url action='delAction.do' />";

    //增加：角色增删改url 20120426周凤雷
    addRoleUrl="<emp:url action='getSRoleAddPage.do'/>"; 
    updateRoleUrl="<emp:url action='getSRoleUpdatePage.do'/>";
    deleteRoleUrl="<emp:url action='deleteSRoleRecord.do'/>";
	
</script>
<script type="text/javascript"	src="<emp:file fileName='scripts/ext/app/resConfigApp.js'/>"></script>
</head>
<body>
</body>
</html>
