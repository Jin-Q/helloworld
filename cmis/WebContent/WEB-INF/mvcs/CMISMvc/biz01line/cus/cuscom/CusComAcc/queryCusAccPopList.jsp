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
		CusComAcc._toForm(form);
		CusComAccList._obj.ajaxQuery(null,form);
	};
	
	/*--user code begin--*/
	function doSelect(){	
	 
		var data = CusComAccList._obj.getSelectedData();
	 
		var optObj="${context.optObj}";
		var nameObj="${context.nameObj}";
		if (data != null) {
		 
			window.opener["${context.returnMethod}"](data[0],optObj,nameObj);
			window.close();
			
		} else {
			alert('请先选择一条记录！');
		}
	};				
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
 
   <button onclick="doSelect()">选取返回</button>
	<emp:table icollName="CusComAccList" pageMode="true" url="pageCusComAccQuery.do" reqParams="CusComAcc.cus_id=$CusComAcc.cus_id">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="acc_no" label="一般结算账户帐号" />
		<emp:text id="acc_name" label="一般结算账户帐号户名" />
		<emp:text id="acc_date" label="一般账户开户日期" />
		<emp:text id="acc_open_org_displayname" label="开户机构" />
		<emp:text id="acc_org_displayname" label="核算机构" />
		<emp:text id="acc_open_org" label="开户机构" hidden="true"/>
		<emp:text id="acc_org" label="核算机构" hidden="true"/>
		
	</emp:table>
	<button onclick="doSelect()">选取返回</button>
</body>
</html>
</emp:page>
    