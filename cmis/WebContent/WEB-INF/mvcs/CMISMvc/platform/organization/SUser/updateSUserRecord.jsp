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

	function doReturn() {
		window.close();
	};

	function doSave(){
		var form = document.getElementById("submitForm");
		if(SUser._checkAll()){
			SUser._toForm(form);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					var msg = jsonstr.msg;
					if(flag == "success"){
						alert("保存成功!");
						window.close();
					}else {
						alert(msg);
						return;
					}
				}
			};
			var handleFailure = function(o){
				alert("异步请求出错！");	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
		}else {
			return false;
		}
	};
	function doOnLoad(){
		SUser.password._setValue("");
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	
	<emp:form id="submitForm" action="updateSUserRecord.do" method="POST">
		<emp:gridLayout id="SUserGroup" title="用户基本信息" maxColumn="2">
			<emp:text id="SUser.actorno" label="用户码" maxlength="8" required="true" readonly="true"/>
			<emp:text id="SUser.actorname" label="姓名" maxlength="20" required="true" />
			<emp:text id="SUser.nickname" label="昵称" maxlength="40" required="false" />
			<emp:select id="SUser.isadmin" label="是否管理员"  required="false" dictname="STD_ZX_YES_NO"/>
			<emp:select id="SUser.state" label="状态" required="true" dictname="STD_ZB_USER_STATE" />
			<emp:date id="SUser.birthday" label="生日" required="false" />
			<emp:text id="SUser.telnum" label="联系电话" maxlength="20" required="false" />
			<emp:text id="SUser.idcardno" label="身份证号码" maxlength="20" required="false" onblur="checkCard(this)" />
			<emp:text id="SUser.usermail" label="邮箱" maxlength="50" required="false" />
			<emp:textarea id="SUser.memo" label="备注" maxlength="200" required="false" colSpan="2" />
			<emp:text id="SUser.password" label="密码" maxlength="32"  />
		</emp:gridLayout>
		
		<emp:gridLayout id="SUserGroup" title="用户防伪信息" maxColumn="2">
			<emp:text id="SUser.ipmask" label="用户IP掩码" maxlength="40" required="false" />
			<emp:text id="SUser.orderno" label="用户排序字段" maxlength="38" required="false" dataType="Int"/>
			<emp:text id="SUser.question" label="用户防伪问题" maxlength="200" required="false" />
			<emp:text id="SUser.answer" label="用户防伪答案" maxlength="200" required="false" />
		</emp:gridLayout>
		<emp:gridLayout id="SUserGroup" title="用户备注信息" maxColumn="2">
			<emp:date id="SUser.startdate" label="启用日期" required="false" />
			<emp:date id="SUser.passwvalda" label="密码失效日期" required="false" />
			<emp:date id="SUser.firedate" label="解雇日期" required="false" />
			<emp:date id="SUser.lastlogdat" label="最后登陆日期" required="false" />
			<emp:text id="SUser.wrongpinnum" label="密码输入错误次数" maxlength="38" required="false"  dataType="Int"/>
			<emp:text id="SUser.creater" label="创建人" maxlength="40" required="false" readonly="true"/>
			<emp:date id="SUser.creattime" label="创建时间" required="false" />
			<emp:pop id="SUser.orgid" label="所属机构" required="false" returnMethod="getOrgID" url="querySOrgPop.do?restrictUsed=false" buttonLabel="选择" hidden="true"/>
			<emp:text id="SUser.allowopersys" label="允许操作的系统" maxlength="20" hidden="true" required="false" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="save" label="修改" op="update"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
