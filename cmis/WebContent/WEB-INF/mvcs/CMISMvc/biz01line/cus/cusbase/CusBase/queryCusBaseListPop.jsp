<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表POP页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusBase._toForm(form);
		CusBaseList._obj.ajaxQuery(null,form);
	};
	
	
	function doReset(){
		page.dataGroups.CusBaseGroup.reset();
	};
	
	/*--user code begin--*/
	function doSelectreturn(){
		var data = CusBaseList._obj.getSelectedData();
		methodName = "${context.returnMethod}";
		if (data==null||data.length==0) {
			alert('请先选择一条记录！');
			return;
		}
		top.opener[methodName](data[0]);
		window.close();
	};
	function doReturnMethod(methodName){
		var data = CusBaseList._obj.getSelectedData();
		if (data != null && data.length !=0) {
			if (methodName) {
				var parentWin = EMPTools.getWindowOpener();
				eval("parentWin."+methodName+"(data[0])");
				window.close();
			}else{
				alert("未定义返回的函数，请检查弹出按钮的设置!");
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doCancel(){
		window.close();
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="CusBaseGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="CusBase.cus_id" label="客户代码" />
		<emp:text id="CusBase.cus_name" label="客户名称" />
		<emp:select id="CusBase.cert_type" label="证件类型" dictname="STD_ZB_CERT_TYP" />
		<emp:text id="CusBase.cert_code" label="证件号码" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	 <emp:returnButton id="s1" label="选择返回"/>
	<emp:table icollName="CusBaseList" pageMode="true" url="pageCusBaseQuery.do">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="cus_short_name" label="客户简称" />
		<emp:text id="cus_type" label="客户类型" dictname="STD_ZB_CUS_TYPE"  />
		<emp:select id="cert_type" label="证件类型" dictname="STD_ZB_CERT_TYP" />
		<emp:text id="cert_code" label="证件号码" />
	</emp:table>
	<div align="left">
			<br>
 <emp:returnButton id="s2" label="选择返回"/>
			<emp:button id="cancel" label="关闭" />
	</div>
</body>
</html>
</emp:page>
    