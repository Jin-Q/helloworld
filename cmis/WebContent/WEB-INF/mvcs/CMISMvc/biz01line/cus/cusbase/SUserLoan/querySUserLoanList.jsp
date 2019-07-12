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
		SUserLoan._toForm(form);
		SUserLoanList._obj.ajaxQuery(null,form);
	};


	function doSelect(){	
		var data = CusComLoanShareList._obj.getSelectedData();

		if (data != null) {
			window.opener["${context.returnMethod}"](data[0]);
			window.close();
			
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doReset(){
		page.dataGroups.SUserLoanGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:button id="getAddSUserLoanPage" label="新增" op="add"/>
		<emp:button id="getUpdateSUserLoanPage" label="修改" op="update"/>
		<emp:button id="deleteSUserLoan" label="删除" op="remove"/>
		<emp:button id="viewSUserLoan" label="查看" op="view"/>
	</div>

	<emp:table icollName="SUserLoanList" pageMode="true" url="pageSUserLoanQuery.do">
		<emp:text id="actorno" label="用户码" />
		<emp:text id="actorname" label="姓名" />
		<emp:text id="telnum" label="联系电话" />
		<emp:text id="idcardno" label="身份证号码" />
		<emp:text id="orgid" label="组织机构" />
	</emp:table>
	<button onclick="doSelect()">选取返回</button>
</body>
</html>
</emp:page>
    