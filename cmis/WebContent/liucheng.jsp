<%@page language="java" contentType="text/html; charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程意见</title>
<script type="text/javascript">
	function show(){
		var a = window.dialogArguments;
		document.getElementById("mys").innerHTML=a;
	}
</script>
</head>
<body onload="show()">
<br>
<h4 align="center" ><font face=黑体>流程意见</font></h4>
<div align="center"><textarea rows="20" cols="80" id="mys" readonly="readonly"></textarea></div>
<!--<fieldset  ><legend>流程意见 </legend>-->
<!--	<div id="mys" >-->
<!--	-->
<!--	<iframe id="123" frameborder="0" width="100%" >-->
<!--				</iframe>-->
<!--	</div>-->
<!--	</fieldset>-->
</body>
</html>