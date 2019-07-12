<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<!-- 使用原系统中的备忘录，只是不在首页的iframe框内打开 -->

<script type="text/javascript">

	/*--user code begin--*/
	
	function openMemoPage(e){
		window.parent.location = e.href; 
	}
	
	/*--user code end--*/
	
</script>
</head>
<body>
<font size=4,color=blue><b>提示：过期信息将于30日后屏蔽，请及时查看</b></font>
<br>
<br>
<br>
<a href="queryAstSysMemoList.do?level=0&EMP_SID=${context.EMP_SID}" onclick="openMemoPage(this)"><b>您共有&nbsp;${context.kcoll1.jjbz}&nbsp;条紧急未查看备注</b></font></a>，<a href="queryAstSysMemoList.do?level=1&EMP_SID=${context.EMP_SID}"><b>&nbsp;${context.kcoll1.jjgqbz}&nbsp;条紧急过期备注</b>
</font></a>
<br>
<br>
<a href="queryAstSysMemoList.do?level=2&EMP_SID=${context.EMP_SID}" onclick="openMemoPage(this)"><b>您共有&nbsp;${context.kcoll1.zybz}&nbsp;条重要未查看备注</b></font></a>，<a href="queryAstSysMemoList.do?level=3&EMP_SID=${context.EMP_SID}"><b>&nbsp;${context.kcoll1.zygqbz}&nbsp;条重要过期备注</b>
</font></a>
<br>
<br>
<a href="queryAstSysMemoList.do?level=4&EMP_SID=${context.EMP_SID}" onclick="openMemoPage(this)"><b>您共有&nbsp;${context.kcoll1.ybbz}&nbsp;条一般未查看备注</b></font></a>，<a href="queryAstSysMemoList.do?level=5&EMP_SID=${context.EMP_SID}"><b>&nbsp;${context.kcoll1.ybgqbz}&nbsp;条一般过期备注</b>
</font></a>
<br>
<br>
<a href="queryAstSysMemoList.do?level=6&EMP_SID=${context.EMP_SID}" onclick="openMemoPage(this)"><b>您共有&nbsp;${context.kcoll1.cybz}&nbsp;条次未查看要备注</b></font></a>，<a href="queryAstSysMemoList.do?level=7&EMP_SID=${context.EMP_SID}"><b>&nbsp;${context.kcoll1.cygqbz}&nbsp;条次要过期备注</b>
</font></a>
<br>
<br>
<a href="queryAstSysMemoList.do?level=8&EMP_SID=${context.EMP_SID}" onclick="openMemoPage(this)"><b>您共有&nbsp;${context.kcoll1.bz}&nbsp;条未查看备注</b></a>，<a href="queryAstSysMemoList.do?level=9&EMP_SID=${context.EMP_SID}"><b>共有&nbsp;${context.kcoll1.gqbz}&nbsp;条过期备注
</b></a>
</body>
</html>
</emp:page>

