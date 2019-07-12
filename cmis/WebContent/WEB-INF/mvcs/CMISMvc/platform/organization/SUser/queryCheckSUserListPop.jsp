<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		SUser._toForm(form);
		var getSOrg=SUser.orgid._getValue();
		var url = '<emp:url action="getValueCheckQuerySUserPopListOp.do"/>&SUser.orgid='+getSOrg;
		url = EMPTools.encodeURI(url);
		form.action=url;
		form.submit();
	};
	function doSelect() {
		var data = SUserList._obj.getSelectedData();
		var methodName = "${context.returnMethod}";
		if (data != null&&data.length!=0) {
			top.opener[methodName](data[0]);
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doReturnMethod(methodName){
		if (methodName) {
			var data = SUserList._obj.getSelectedData();
			if(data!=null&&data!=''){
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin."+methodName+"(data[0])");
			window.close();
			}
			else{
				alert('请先选择一条记录！');
			}
		}else{
			alert("未定义返回的函数，请检查弹出按钮的设置!");
		}
	};	
	function doReset(){
		SUser.actorno._setValue('');
		SUser.actorname._setValue('');
		}
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="SUserGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="SUser.actorno" label="用户码" />
			<emp:text id="SUser.actorname" label="姓名" />
			<emp:text id="SUser.orgid" label="姓名" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />

	<emp:table icollName="SUserList" pageMode="true" url="pageCheckSUserQuery.do"  reqParams="SUser.orgid=${context.SUser.orgid}" >
	    <emp:text id="actorno" label="用户码" />
		<emp:text id="actorname" label="姓名" />
		<emp:text id="telnum" label="联系电话" />
		<emp:text id="idcardno" label="身份证号码" />
	</emp:table>
	<div align="left"><br>
	<emp:returnButton label="选择返回"/> <br>
	</div>
</body>
</html>
</emp:page>