<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		window.close();
	};
	
	/*--user code begin--*/
    function getOrgID(data){
        
	}			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:gridLayout id="SUserGroup" title="用户基本信息" maxColumn="2">
			<emp:text id="SUser.actorno" label="用户码" maxlength="8" required="true"/>
			<emp:text id="SUser.actorname" label="姓名" maxlength="20" required="true" />
			<emp:text id="SUser.nickname" label="昵称" maxlength="40" required="false" />
			<emp:select id="SUser.isadmin" label="是否管理员"  required="false" dictname="STD_ZX_YES_NO"/>
			<emp:select id="SUser.state" label="状态" required="true" dictname="STD_ZB_USER_STATE" />
			<emp:date id="SUser.birthday" label="生日" required="false" />
			<emp:text id="SUser.telnum" label="联系电话" maxlength="20" required="false" />
			<emp:text id="SUser.idcardno" label="身份证号码" maxlength="20" required="false" onblur="checkCard(this)" />
			<emp:text id="SUser.usermail" label="邮箱" maxlength="50" required="false" />
			<emp:textarea id="SUser.memo" label="备注" maxlength="200" required="false" colSpan="2" />
			<emp:text id="SUser.password" label="密码" maxlength="32"  hidden="true"/>
		</emp:gridLayout>
		
		<emp:gridLayout id="SUserGroup" title="用户防伪信息" maxColumn="2">
			<emp:text id="SUser.ipmask" label="用户IP掩码" maxlength="40" required="false" />
			<emp:text id="SUser.orderno" label="用户排序字段" maxlength="38" required="false" />
			<emp:text id="SUser.question" label="用户防伪问题" maxlength="200" required="false" />
			<emp:text id="SUser.answer" label="用户防伪答案" maxlength="200" required="false" />
		</emp:gridLayout>
		
		<emp:gridLayout id="SUserGroup" title="用户备注信息" maxColumn="2">
			<emp:date id="SUser.startdate" label="启用日期" required="false" />
			<emp:date id="SUser.passwvalda" label="密码失效日期" required="false" />
			<emp:date id="SUser.firedate" label="解雇日期" required="false" />
			<emp:date id="SUser.lastlogdat" label="最后登陆日期" required="false" />
			<emp:text id="SUser.wrongpinnum" label="密码输入错误次数" maxlength="38" required="false" />
			<emp:text id="SUser.creater" label="创建人" maxlength="40" required="false" />
			<emp:date id="SUser.creattime" label="创建时间" required="false" />
			<emp:pop id="SUser.orgid" label="所属机构" required="false" returnMethod="getOrgID" url="querySOrgPop.do?restrictUsed=false" buttonLabel="选择" hidden="true"/>
			<emp:text id="SUser.allowopersys" label="允许操作的系统" maxlength="20" hidden="true" required="false" />
		</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
