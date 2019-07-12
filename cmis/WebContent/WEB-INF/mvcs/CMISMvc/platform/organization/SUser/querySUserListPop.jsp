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
		SUserList._obj.ajaxQuery(null,form);
	};
	/*function doSelect() {
		var data = SUserList._obj.getSelectedData();
		var methodName = "${context.returnMethod}";
		if (data != null&&data.length!=0) {
			window.opener[methodName](data[0]);
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};*/
	function doSelect(){
		var methodName = '${context.popReturnMethod}';	
		doReturnMethod(methodName);
	}
	function doReturnMethod(methodName){
		if (methodName) {
			var data = SUserList._obj.getSelectedData();
			if(data!=null&&data!=''){
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin."+methodName+"(data[0])");
			window.close();
			}else{
				alert('请先选择一条记录！');
			}
		}else{
			alert("未定义返回的函数，请检查弹出按钮的设置!");
		}
	};	

	//获取管理机构信息
	function getOrgID(data){
		SUser.orgid._setValue(data.organno._getValue());
		SUser.orgid_displayname._setValue(data.organname._getValue());
	}

	function doReset(){
		page.dataGroups.SUserGroup.reset();
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="SUserGroup" title="输入查询条件" maxColumn="3">
		<emp:text id="SUser.actorno" label="用户码" />
		<emp:text id="SUser.actorname" label="姓名" />
		<emp:pop id="SUser.orgid_displayname" label="所属机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" 
			         popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no" hidden="true"/>
		<emp:text id="SUser.orgid" label="所属机构" hidden="true"/>
		
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<emp:table icollName="SUserList" pageMode="true" url="pageSUserPopQuery.do"  reqParams="restrictUsed=${context.restrictUsed}" >
	    <emp:text id="actorno" label="用户码" />
		<emp:text id="actorname" label="姓名" />
		<emp:text id="orgid" label="所属机构码" hidden="true"/>
		<emp:text id="orgid_displayname" label="所属机构名称" hidden="true"/>
		<emp:text id="telnum" label="联系电话" />
		<emp:text id="idcardno" label="身份证号码" />
	</emp:table>
	<div align="left"><br>
	<emp:returnButton label="选择返回"/> <br>
	</div>
</body>
</html>
</emp:page>