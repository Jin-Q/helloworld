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
</head>
<body class="page_content" onload="getBulletinList()">

<div id="left">


<span id="bulletinShowList" >
信息读取中....
  
 
 
</span>
<br>

</div>

<script>
	function getBulletinList() {
		 var innerHtmlStr="今天是："+getCurrentDate1()+"</br>欢迎您：${context.loginuserid}-${context.loginusername}"+
			"</br>责任人：${context.currentUserId}-${context.currentUserName}</br>机构：${context.organNo}-${context.organName}</br>岗位：${context.dutyNameList}</br>角色：${context.roleNameList}";
         document.getElementById("bulletinShowList").innerHTML = innerHtmlStr;
	}

	function getCurrentDate1(){
		var date_new = '${context.OPENDAY}';
		var currentDateStr;
		var sdate = date_new.split('-');
		var y=new Date(sdate[0],(sdate[1]-1),(sdate[2]-0));
		var gy=y.getYear();
		var dName=new Array("星期天","星期一","星期二","星期三","星期四","星期五","星期六");
		var mName=new Array("1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月");
		currentDateStr= y.getYear()+"年" + mName[y.getMonth()] + y.getDate() + "日   " + dName[y.getDay()];
		return currentDateStr;
}

</script>

</body>
</html>
</emp:page>
