<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateSUserLoanRecord.do" method="POST">
		<emp:gridLayout id="SUserLoanGroup" maxColumn="2" title="S_USER">
			<emp:text id="SUserLoan.actorno" label="用户码" maxlength="8" required="true" readonly="true" />
			<emp:text id="SUserLoan.actorname" label="姓名" maxlength="20" required="true" />
			<emp:text id="SUserLoan.nickname" label="昵称" maxlength="40" required="false" />
			<emp:text id="SUserLoan.state" label="状态 0-正常  1-解雇  2-封锁" maxlength="1" required="true" />
			<emp:text id="SUserLoan.password" label="密码" maxlength="32" required="true" />
			<emp:text id="SUserLoan.startdate" label="启用日期" maxlength="10" required="false" />
			<emp:text id="SUserLoan.passwvalda" label="密码失效日期" maxlength="10" required="false" />
			<emp:text id="SUserLoan.firedate" label="解雇日期" maxlength="10" required="false" />
			<emp:text id="SUserLoan.birthday" label="员工生日" maxlength="10" required="false" />
			<emp:text id="SUserLoan.telnum" label="联系电话" maxlength="20" required="false" />
			<emp:text id="SUserLoan.idcardno" label="身份证号码" maxlength="20" required="false" />
			<emp:text id="SUserLoan.allowopersys" label="允许操作的系统 第1位 核心业务 0不允许 1允许 第2位 运营管理 0不允许 1允许 第3位 事后监督 0不允许 1允许 第4位 事中监督 0不允许 1允许 第5位 信贷管理 0不允许 1允许 第6位 客户管理 0不允许 1允许 第7位 办公自动化 0不允许 1允许 第8位 报表系统 0不允许 1允许 " maxlength="20" required="false" />
			<emp:text id="SUserLoan.lastlogdat" label="最后登陆日期" maxlength="10" required="false" />
			<emp:text id="SUserLoan.creater" label="创建人" maxlength="40" required="false" />
			<emp:text id="SUserLoan.creattime" label="创建时间" maxlength="10" required="false" />
			<emp:text id="SUserLoan.usermail" label="用户邮箱" maxlength="50" required="false" />
			<emp:text id="SUserLoan.wrongpinnum" label="密码输入错误次数" maxlength="38" required="false" />
			<emp:text id="SUserLoan.isadmin" label="是否管理员 0：否 1：是" maxlength="1" required="false" />
			<emp:text id="SUserLoan.memo" label="备注" maxlength="200" required="false" />
			<emp:text id="SUserLoan.ipmask" label="用户IP掩码" maxlength="40" required="false" />
			<emp:text id="SUserLoan.orderno" label="用户排序字段" maxlength="38" required="false" />
			<emp:text id="SUserLoan.question" label="用户防伪问题" maxlength="200" required="false" />
			<emp:text id="SUserLoan.answer" label="用户防伪答案" maxlength="200" required="false" />
			<emp:text id="SUserLoan.orgid" label="组织号" maxlength="16" required="false" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="修改" op="update"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
