<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<html>
<head>
<title>操作返回页面</title>
<script type="text/javascript">
	
	function doOnLoad(){
		var url = '<emp:url action="getUploadInfoPage.do"/>?file_type=${context.file_type}&serno=${context.serno}';
		url = EMPTools.encodeURI(url);
		var link = document.getElementById("returnlink");
		link.href = url;
	};

</script>

<jsp:include page="/include.jsp" />

</head>
<body   class="page_content"  onload="doOnLoad()">

<br><br><br><br><br><br><br>
<div class="page_welcome">
	<div class="page_welcome_link">
		<div class="clear"></div>
	</div>
	<div class="page_welcome_content">
		<div class="page_welcome_content_top"></div>
		<div class="page_welcome_succ">
			<span class="page_welcome_red">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;操作已成功！
			</span><br>
			<TABLE align="center" width="300px">
        		<TR align="center">
          			<TD>
          				<span class="page_welcome_red"><font color="green"><a id="returnlink" href="">回到列表页面</a></font></span>
          			</TD>
        		</TR>
        		<TR><TD>&nbsp;</TD></TR>
      		</TABLE>
		</div>
	</div>
</div>
</body>
</html>
