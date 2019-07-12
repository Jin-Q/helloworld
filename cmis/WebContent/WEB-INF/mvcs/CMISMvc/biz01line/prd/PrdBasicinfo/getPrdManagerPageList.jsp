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
	
	function doReset(){
		page.dataGroups.PrdBasicinfoGroup.reset();
	};

	function doSelect(){
		var data = SUserList._obj.getSelectedData();
		if(data != null){
			window.opener["${context.returnMethod}"](data[0]);
			window.close();
		}else{
			alert('请先选择一条记录！');
		}
	}
	/*--user code begin--*/
	//双击事件
	$(function (){ 
		$(".emp_field_td").dblclick(function (){ 
			doSelect();
		}); 
	  });		
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PrdBasicinfoGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="SUser.actorname" label="产品经理名称" />
			<emp:text id="SUser.actorno" label="产品经理编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<emp:table icollName="SUserList" pageMode="true" url="getPrdManagerPop.do">
		<emp:text id="actorname" label="产品经理名称" />
		<emp:text id="actorno" label="产品经理编号" />
		<emp:text id="orgid" label="机构号" />
		<emp:text id="state" dictname="STD_ZB_USER_STATE" label="客户状态" />
	</emp:table>
	<button id="but" onclick="doSelect()" >选择返回</button>
</body>
</html>
</emp:page>
    