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
		//var url = '<emp:url action="querySDutyList.do"/>';
		//url = EMPTools.encodeURI(url);
		//window.location=url;
		window.close();
	};
	function doSave(){
		var form = document.getElementById("submitForm");
		if(SDuty._checkAll()){
			SDuty._toForm(form);
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
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateSDutyRecord.do" method="POST">
		<emp:gridLayout id="SDutyGroup" maxColumn="2" title="岗位表">
			<emp:text id="SDuty.dutyno" label="岗位码" maxlength="20" required="true" readonly="true" />
			<emp:text id="SDuty.dutyname" label="岗位名称" maxlength="40" required="true" />
			<emp:text id="SDuty.organno" label="机构码" maxlength="16" required="false" />
			<emp:text id="SDuty.depno" label="部门码" maxlength="16" required="false" />
			<emp:text id="SDuty.orderno" label="排序字段" hidden="true" maxlength="38" required="false" />
			<emp:text id="SDuty.orgid" label="组织号" hidden="true" maxlength="16" required="false" />
			<emp:text id="SDuty.type" label="类型" maxlength="38" required="false" hidden="true"/>
			<emp:textarea id="SDuty.memo" label="备注" maxlength="60" required="false" colSpan="2" />
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
