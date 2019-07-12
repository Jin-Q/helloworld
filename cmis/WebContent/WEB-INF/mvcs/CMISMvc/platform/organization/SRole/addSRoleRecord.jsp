<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	
	function doAddRoleRecord() {
		var form = document.getElementById("submitForm");
		var result = SRole._checkAll();
		if(result){
			SRole._toForm(form);
			toSubmitForm(form);
		}else alert("请输入必填项！");
	};
	function toSubmitForm(form){
	
		  var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr define error!"+e);
						return;
					}
					var flag = jsonstr.flag;
					var roleno = jsonstr.roleno;
					if(flag == "exist"){
						alert("该角色【"+roleno+"】已存在，不能重复添加");
						return;
					}else if(flag == "sucess") {
						alert("保存成功!");
						window.close();
						try{
							window.opener.location.reload();
						}catch(e){}
						try{
							window.parent.colseWinExt();  //从资源定义 打开的页面
							window.parent.location.reload();
						}catch(e){}
                        return;
                     }else{
						alert("操作失败");
						return ;
                    }
		                        
				}
			};
			var handleFailure = function(o){	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var postData = YAHOO.util.Connect.setForm(form);	 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData) 
		  }
	
	
	function doReturn() {
		window.close();
		try{
			window.parent.colseWinExt();  //从资源定义 打开的页面
		}catch(e){}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addSRoleRecord.do" method="POST">
		
		<emp:gridLayout id="SRoleGroup" title="角色表" maxColumn="2">
			<emp:text id="SRole.roleno" label="角色码" maxlength="4" required="true" dataType="CodeNo" cssElementClass="emp_field_text_input"/>
			<emp:text id="SRole.rolename" label="角色名称" maxlength="40" required="true" />
			<emp:text id="SRole.orderno" label="排序字段" maxlength="38" required="false" dataType="Int"/>
			<emp:text id="SRole.orgid" label="组织号" maxlength="16" required="false" />
			<emp:select id="SRole.type" label="类型"  required="false" hidden="false" dictname="STD_ZB_SROLE_TYPE" defvalue="2"/>
			<emp:textarea id="SRole.memo" label="备注" maxlength="60" required="false" colSpan="2" />
			
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="addRoleRecord" label="确定"/>
			<emp:button id="return" label="关闭"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

