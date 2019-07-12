<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@taglib uri="/WEB-INF/c-rt.tld" prefix="c"%>
<emp:page>
<html>
<head>
<title>风险预警</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_label{
	color:#228B22;
	font-family: "宋体","Times New Roman";
	font-size: 13px;
	font-weight: bold;
}

#left{
	
	float:left;
	
}

#right{
	margin:16px;
	float:right;
	 
	width:400px;
	height:200px;
}

}


</style>
<script type="text/javascript">
	function toThePage(e){
		window.parent.location = e.href;
	}
	
</script>
</head>
<body class="page_content">

<div id="left">
<c:forTokens items="${context.scheme_type.typeStr}" delims="," var="type">
<c:set var="tp" value="${type}"></c:set>
<c:set var="stype" value="iColl${type}"></c:set>
<c:forEach items="${context.typeDic}" var="dic">
<c:set var="enname" value="${dic.enname}"></c:set>

	<c:if test="${enname==tp}">
	<br>
	<emp:label text="${dic.cnname}" CSSClass="emp_label" />
		<br>
	</c:if>
</c:forEach>
<br>
<c:forEach items="${context[stype]}" var="scheme">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<a href="${scheme.scheme_action}?readedInd=2&EMP_SID=${context.EMP_SID}&scheme_id=${scheme.scheme_id}" onClick="toThePage(this)">您有${scheme.scheme_name}信息&nbsp;&nbsp;${scheme.scheme_count}&nbsp;&nbsp;条</a><br>
</c:forEach>
</c:forTokens>
</div>

</body>
</html>
</emp:page>
