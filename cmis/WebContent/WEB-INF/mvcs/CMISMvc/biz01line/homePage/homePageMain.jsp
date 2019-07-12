<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<emp:page>
	<html>
	<head>
	<title>我的个性化首页</title>

	<jsp:include page="/include.jsp" flush="true" />
	<link href="styles/homePage/inettuts.css" rel="stylesheet"
		type="text/css" />
	<script type="text/javascript"
		src="scripts/homePage/jquery-1.2.6.min.js"></script>
	<script type="text/javascript"
		src="scripts/homePage/jquery-ui-personalized-1.6rc2.min.js"></script>
	<script type="text/javascript" src="scripts/homePage/inettuts.js"></script>
	<style type="text/css">

.emp_link {
	color: #3366FF;
	font-size: 12px;
}

div,body,h3 {
	padding: 0;
	margin: 0;
}
ul ,li{
	display:block;
}
.widget,.widgetOn {
	clear:both;
	display:block;
	min-width: 200px;
	height: 250px;

}

.xb1,.xb2,.xb3,.xb4,.xb5,.xb6,.xb7 {
	display: block;
	overflow: hidden;
}

.xb1,.xb2,.xb3,.xb5,.xb6 {
	height: 1px;
}

.xb1 {
	margin: 0 5px;
	background: #D4E6FC;
}

.widgetOn .xb1 {
	margin: 0 5px;
	background: #D4E6FC;
}

.xb2 {
	margin: 0 3px;
	border-width: 0 2px;
}

.xb3 {
	margin: 0 2px;
}

.xb4 {
	height: 2px;
	margin: 0 1px;
}

.xb5 {
	margin: 0 3px;
	border-width: 0 2px;
}

.xb6 {
	margin: 0 2px;
}

.xb7 {
	height: 2px;
	margin: 0 1px;
}

.xb2,.xb3,.xb4 {
	background: #D4E6FC;
	border-left: 1px solid #cfe2e5;
	border-right: 1px solid #cfe2e5;
}

.xb5,.xb6,.xb7 {
	background: #ffffff;
	border-left: 1px solid #cfe2e5;
	border-right: 1px solid #cfe2e5;
}

.widgetOn .xb2,.widgetOn .xb3,.widgetOn .xb4 {
	background: #aad9e2;
	border-left: 1px solid #61a4b0;
	border-right: 1px solid #61a4b0;
}

.widgetOn .xb5,.widgetOn .xb6,.widgetOn .xb7 {
	background: #ffffff;
	border-left: 1px solid #61a4b0;
	border-right: 1px solid #61a4b0;
}

.xboxcontent {
	display: block;
	background: #FFFFFF;
	border: 0 solid #D4E6FC;
	border-width: 0 1px;
}

.widgetOn .xboxcontent {
	border: 0 solid #D4E6FC;
	border-width: 0 1px;
}

.xtop {
	display: block;
	background: transparent;
	font-size: 1px;
}

.xbottom {
	display: block;
	background: transparent;
	font-size: 1px;
}

.widget-head, .widget-head h3{
	font-size: 12px;
	background: #D4E6FC;
	color: #3366CC;
}

.widget-content {
	padding-top: 3px;
	padding-left: 5px;
	padding-right: 5px;
	padding-bottom: 3px;
}
</style>


	</head>

	<script type="text/javascript">
	function onLoad(){
		
	}

	/**
	*设用自定义的关闭方法，将该用户与该gadget关系删除
	*@param gadgetId
	*/
	function removeGdget(gadgetId){
		
		var form =  document.getElementById("queryGadgetForm");
		form.action="<emp:url action='moveCustomHomePageGadget.do'/>&operator=delete&gadgetId="+gadgetId;
		form.method = "post"; 
		
		 var handleSuccess = function(o){
				if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr define error!"+e);
							return;
						}
						var flag = jsonstr.flag;
						if(flag=="sucess"){
							
						}else {
							 alert("删除小工具数据库操作失败，暂时将该小工具隐藏");
							 return;
						}
				}
			};
			var handleFailure = function(o){	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var postData = YAHOO.util.Connect.setForm(form);	 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData) 
	}

	/**
	 * 添加工具
	 */
	function doAddGadget(){
		var url = "<emp:url action='queryHomePageGadget.do'/>&operator=queryByUser"
		url = EMPTools.encodeURI(url);
		window.location = url;
	}

</script>

	<body onload="onLoad()">

	<div id="heads" class='head'>
	<div align="right" class="emp_link"><a href="#"
		onclick="doAddGadget()">添加工具集</a></div>
	</div>


	<!-- 工具集DIV -->
	<div id="columns"></div>

	<form method="POST" action="#" id="queryGadgetForm"></form>

	<script type="text/javascript" src="scripts/homePage/homepage.js"></script>

	<script type="text/javascript">
    	var EMP_SID = "${context.EMP_SID}";
    	//动态生成gadget集
    	onloadGadget(EMP_SID);   
    	
    </script>
	</body>
	</html>
</emp:page>