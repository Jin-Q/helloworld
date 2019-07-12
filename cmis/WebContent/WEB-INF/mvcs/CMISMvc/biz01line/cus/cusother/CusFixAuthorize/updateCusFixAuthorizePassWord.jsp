<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改密码页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	//确认授权码
	function checkCheckCode(){
		var checkCode = CusFixAuthorize.checkcodenew._getValue();
		var checkCodeChk = CusFixAuthorize.checkcodechk._getValue();
		if(checkCode!=null&&checkCode!=''&&checkCodeChk!=null&&checkCodeChk!=''){
			if(checkCode!=checkCodeChk){
				alert('两次密码不符，请重新输入！');
				CusFixAuthorize.checkcodenew._setValue('');
				CusFixAuthorize.checkcodechk._setValue('');
				return;
			}
		}
	}

	//修改密码
	function doChangePassWord(){
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var operMsg = jsonstr.flag;
				if(operMsg=='success'){
		            alert('修改成功!');
		            window.close();
				}else if(operMsg=='fail'){
					alert('原授权码不正确，请确认!');
				}
			}
		};
		var handleFailure = function(o) {
			alert("修改失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};

		var form = document.getElementById("submitForm");
		var result = CusFixAuthorize._checkAll();
		if(result){
			page.dataGroups.dataGroup_in_formsubmitForm.toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}else {
           alert("请输入必填项!");
           return ;
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateCusFixAuthorizePass.do" method="POST">
		<emp:gridLayout id="CusFixAuthorizeGroup" maxColumn="2" title="授权码修改">
		
			<emp:password id="CusFixAuthorize.checkcode" label="原授权码" required="true" colSpan="2"/>
			<emp:password id="CusFixAuthorize.checkcodenew" label="新授权码" maxlength="32" required="true" onblur="checkCheckCode()"/>
			<emp:password id="CusFixAuthorize.checkcodechk" label="确认新授权码" maxlength="32" required="true" onblur="checkCheckCode()"/>
			<emp:text id="CusFixAuthorize.serno" label="流水号" maxlength="32" readonly="true" required="true" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="changePassWord" label="修改" op="update"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
