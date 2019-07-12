<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<emp:page>

	<html>
	<head>
	<title>列表查询页面</title>

	<jsp:include page="/include.jsp" flush="true" />
<style type="text/css">
	/*************** 输入框(input)普通状态下的样式 ********************/
.emp_field_longtext_input { /****** 长度固定 ******/
	width: 250px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}

/*************** 输入框(input)不可用状态下的样式 ********************/
.emp_field_disabled .emp_field_longtext_input {
	border-color: #b7b7b7;
	color: #CEC7BD;
}

/*************** 输入框(input)只读状态下的样式 ********************/
.emp_field_readonly .emp_field_longtext_input {
	border-color: #b7b7b7;
}

</style>
<script src="/cmis-main/scripts/jquery-1.3.2.min.js" type="text/javascript" language="javascript"></script>

	<script type="text/javascript">
	function doQuery() {
		var form = document.getElementById('queryForm');
		CusSameOrg._toForm(form);
		CusSameOrgList._obj.ajaxQuery(null, form);
	};

	function doReset() {
		page.dataGroups.SameSOrgGroup.reset();
	};

	/*--user code begin--*/
	function doSelect(){
		var methodName="${context.popReturnMethod}";
		doReturnMethod(methodName);
	};
	
	function doReturnMethod(methodName){
		if (methodName) {
			var data = CusSameOrgList._obj.getSelectedData();
			if(data!=null&&data!=''){
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin."+methodName+"(data[0])");
			window.close();
			}else {
				alert('请先选择一条记录！');
			}
		}else{
			alert("未定义返回的函数，请检查弹出按钮的设置!");
		}
	};	

	//双击事件
	//$(function (){ 
	//	$(".emp_field_td").dblclick(function (){ 
	//		doSelect();
	//	}); 
	//  }); 
	
	/*--user code end--*/
</script>
	</head>
	<body class="page_content">
	<form method="POST" action="#" id="queryForm"></form>

	<emp:gridLayout id="SameSOrgGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="CusSameOrg.cus_id" label="客户码" />
		<emp:text id="CusSameOrg.same_org_no" label="同业机构(行)号" />
		<emp:text id="CusSameOrg.same_org_cnname" label="同业机构(行)名称" cssElementClass="emp_field_longtext_input" />
	</emp:gridLayout>

	<jsp:include page="/queryInclude.jsp" flush="true" />

	<emp:table icollName="CusSameOrgList" pageMode="true" url="pageCusSameOrgQueryPop.do">
	    <emp:text id="cus_id" label="客户码" />
		<emp:text id="same_org_no" label="同业机构(行)号" />
		<emp:text id="same_org_cnname" label="同业机构(行)名称" />
		<emp:text id="swift_no" label="SWIFT编号" />
		<emp:text id="same_org_type" label="同业机构类型" dictname="STD_ZB_INTER_BANK_ORG"/>
	</emp:table>

	<div align="center"><br>
	<emp:returnButton label="选择返回"/>
	<br>
	</div>
	</body>
	</html>
</emp:page>
