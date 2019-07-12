<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<script type="text/javascript">

	/*** 统一的弹出框配置，以新窗口打开，并统一设定默认高350，宽850 ***/
	function getPubDetailsPop(url,height,width){
		url = EMPTools.encodeURI(url);
		windowName = Math.ceil(Math.random()*50000000);
		var styles = "top=200, left=100, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no";
		if(width == ""){
			styles = "width=850, "+styles;
		}else{
			styles = "width="+width+","+styles;
		}
		if(height == ""){
			styles = "height=350, "+styles;
		}else{
			styles = "height="+height+","+styles;
		}
		EMPTools.openWindow(url,windowName+"",styles);
	};

</script>
</head>
</html>
</emp:page>