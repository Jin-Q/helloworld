<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doReturn() {
		window.close();
		window.parent.colseWinExt();  //从资源定义 打开的页面
	};

	function doSave(){
		var form = document.getElementById("submitForm");
		if(SRole._checkAll()){
			SRole._toForm(form);
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
						try{
							window.opener.location.reload();
						}catch(e){}
						try{
							window.parent.colseWinExt();  //从资源定义 打开的页面
							window.parent.location.reload();
						}catch(e){}
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
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateSRoleRecord.do" method="POST">
		<emp:gridLayout id="SRoleGroup" maxColumn="2" title="角色表">
			<emp:text id="SRole.roleno" label="角色码" maxlength="4" required="true" readonly="true" />
			<emp:text id="SRole.rolename" label="角色名称" maxlength="40" required="true" />
			<emp:text id="SRole.orderno" label="排序字段" maxlength="38" required="false" dataType="Int" />
			<emp:text id="SRole.orgid" label="组织号" maxlength="16" required="false" />
			<emp:select id="SRole.type" label="类型"  required="false" hidden="false" dictname="STD_ZB_SROLE_TYPE" defvalue="2"/>
			<emp:textarea id="SRole.memo" label="备注" maxlength="60" required="false" colSpan="2" />
			
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="save" label="修改"/>
			<emp:button id="return" label="关闭"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
