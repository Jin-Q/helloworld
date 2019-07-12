<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.yucheng.cmis.pub.PUBConstant"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<html>
<head>
<title>跨域访问链接页面（系统通用跨域访问方法）</title>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	function doOnLoad(){
		
		var query_type = '${context.query_type}';//跨域查询类型
		var EMP_SID ='${context.EMP_SID}';
		var url =""
		//查询新信贷客户基本信息
		if(query_type=="CUSINFO"){
			var cus_id = '${context.cus_id}';//所查询的客户编号
			var cus_bline = '${context.cus_bline}';//客户所属条线
			var currentuserid = '${context.currentUserId}';//当前登录人员ID
			if(cus_bline!="BL300"){
				url="<%=PUBConstant.CMISURL%>/getCusComTree.do?EMP_SID="+EMP_SID+"&cus_id="+cus_id+"&oper=infotree&flag=query";
			}else{
				url="<%=PUBConstant.CMISURL%>/getCusIndivTree.do?EMP_SID="+EMP_SID+"&cus_id="+cus_id+"&oper=infotree&flag=query";
			}
		}
		url = window.encodeURI(url);
		window.location=url;
	};
</script>
</head>
<body class="page_content" onload="doOnLoad()"> 
</body>
</html>