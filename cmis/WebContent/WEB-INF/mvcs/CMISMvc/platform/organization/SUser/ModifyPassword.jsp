<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function getOrgID(data){
		SUser.orgid._setValue(data.organno._getValue());
    	//PrdOrgApply.org_name._setValue(data.organname._getValue());        
	};
	function checkCard(obj) {
		var val = obj.value;
		if (val == "") {
			return;
		}
		
		var flag = CheckIdValue(val);
		if (!flag) {
			obj.value = "";
			return;
		}
	};	


	//异步方法校验密码
	function doUpdate() {
		var password=SUser.password._obj.element.value;
		var password2nd=SUser.password2nd._obj.element.value;
		var passwordold=SUser.passwordold._obj.element.value;
		var passwordhiddenold=SUser.passwordhiddenold._obj.element.value;
		var cusid=SUser.actorno._obj.element.value;
        //alert("password:"+password);
        //alert("password2nd:"+password2nd);
        //alert("passwordold:"+passwordold);
        //alert("passwordhiddenold:"+passwordhiddenold);
        //alert("cusid:"+cusid);
		//return 
		if(password2nd!=password){
			alert("请您两次输入的密码不一致！");
			return SUser.password._obj.element.focus();
		}	
		
		var url = '<emp:url action="checkPassword.do?password=' + passwordold + '&cusid='+cusid+'&passwordold='+passwordhiddenold+'"/>'
		var handleSuccess = function(o) {
			
			getCheckInfo(o);
		};
		var handleFailure = function(o) {
			alert("校验原密码失败！");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var obj1 = YAHOO.util.Connect.asyncRequest('GET', url, callback);
	}

	

	function getCheckInfo(o) {
		
		if (o.responseText !== undefined) {
			try {
				var jsonstr = eval("(" + o.responseText + ")");
			} catch (e) {
				alert("Parse jsonstr define error!" + e.message);
				return;
			}
			var jsonStr = jsonstr.result; 
			if (jsonStr == "ok" ) {
				var form=document.getElementById("submitForm");
				SUser._toForm(form);
				form.submit();
			}else{
				alert("您输入的旧密码有误，请重新输入！");
				return SUser.passwordold._obj.element.focus();
			}	
		}
	}

	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="ModifyPasswordUpdate.do" method="POST">
		
		<emp:gridLayout id="SUserGroup" maxColumn="1" title="修改密码">
			<emp:password id="SUser.passwordold" label="请输入原密码" maxlength="32" required="true" />
			<emp:password id="SUser.password" label="新密码" maxlength="32" required="true" />
			<emp:password id="SUser.password2nd" label="确认新密码" maxlength="32" required="true" />
			<emp:text id="SUser.passwordhiddenold" label="隐藏密码" maxlength="32" hidden="true"/>
			<emp:text id="SUser.actorno" label="用户码" maxlength="8"  readonly="true" hidden="true"/>
			<emp:text id="SUser.actorname" label="姓名" maxlength="20"  hidden="true"/>
			<emp:text id="SUser.nickname" label="昵称" maxlength="40" required="false" hidden="true"/>
			<emp:select id="SUser.state" label="状态"  dictname="STD_ZB_USER_STATE" hidden="true"/>
			
			<emp:date id="SUser.startdate" label="启用日期" required="false" hidden="true"/>
			<emp:date id="SUser.passwvalda" label="密码失效日期" required="false" hidden="true"/>
			<emp:date id="SUser.firedate" label="解雇日期" required="false" hidden="true"/>
			<emp:date id="SUser.birthday" label="生日" required="false" hidden="true"/>
			<emp:text id="SUser.telnum" label="联系电话" maxlength="20" required="false" hidden="true"/>
			<emp:text id="SUser.idcardno" label="身份证号码" maxlength="20" required="false" onblur="checkCard(this)" hidden="true"/>
			<emp:text id="SUser.allowopersys" label="允许操作的系统" maxlength="20" required="false" hidden="true"/>
			<emp:date id="SUser.lastlogdat" label="最后登陆日期" required="false" hidden="true"/>
			<emp:text id="SUser.creater" label="创建人" maxlength="40" required="false" hidden="true"/>
			<emp:date id="SUser.creattime" label="创建时间" required="false" hidden="true"/>
			<emp:text id="SUser.usermail" label="邮箱" maxlength="50" required="false" hidden="true"/>
			<emp:text id="SUser.wrongpinnum" label="密码输入错误次数" maxlength="38" required="false" hidden="true"/>
			<emp:select id="SUser.isadmin" label="是否管理员"  required="false" dictname="STD_ZX_YES_NO" hidden="true"/>
			<emp:textarea id="SUser.memo" label="备注" maxlength="200" required="false" colSpan="2" hidden="true"/>
			<emp:text id="SUser.ipmask" label="用户IP掩码" maxlength="40" required="false" hidden="true"/>
			<emp:text id="SUser.orderno" label="用户排序字段" maxlength="38" required="false" hidden="true"/>
			<emp:text id="SUser.question" label="用户防伪问题" maxlength="200" required="false" hidden="true"/>
			<emp:text id="SUser.answer" label="用户防伪答案" maxlength="200" required="false" hidden="true"/>
			<emp:pop id="SUser.orgid" label="所属机构"  returnMethod="getOrgID" url="querySOrgPop.do?restrictUsed=false" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="update" label="修改" op="update"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
