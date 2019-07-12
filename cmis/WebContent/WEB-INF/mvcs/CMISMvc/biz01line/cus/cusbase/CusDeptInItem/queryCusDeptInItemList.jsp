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
		CusDeptInItem._toForm(form);
		CusDeptInItemList._obj.ajaxQuery(null,form);
	};
	
	function doViewCusDeptInItem() {
		var paramStr = CusDeptInItemList._obj.getParamStr(['acc_no']);
		var editFlag = '${context.EditFlag}';
		if (paramStr != null) {
			var url = '<emp:url action="getCusDeptInItemViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
	  		window.open(url,'newwindow1','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.7+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CusDeptInItemGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<div align="left">
		<emp:button id="viewCusDeptInItem" label="查看"/>
	</div>
	<emp:table icollName="CusDeptInItemList" pageMode="true" url="pageCusDeptInItemQuery.do" reqParams="CusDeptInItem.cus_id=${context.CusDeptInItem.cus_id}">
		<emp:text id="seq" label="序号" hidden="true"/>
		<emp:text id="cus_bch_id" label="开户机构代码" />
		<emp:text id="acc_no" label="账号" />
		<emp:text id="cus_acc_cur_typ" label="币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="cus_open_dt" label="开户日期" />
		<emp:text id="cus_acc_blc" label="存款余额" />
		<emp:text id="cus_id" label="客户码" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>