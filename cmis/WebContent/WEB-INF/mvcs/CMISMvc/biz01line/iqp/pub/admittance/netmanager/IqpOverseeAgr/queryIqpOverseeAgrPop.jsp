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
		IqpOverseeAgr._toForm(form);
		IqpOverseeAgrList._obj.ajaxQuery(null,form);
	};
	/*--user code begin--*/
	function doSelect(){
		var methodName = '${context.popReturnMethod}';	
		doReturnMethod(methodName);
	}
	
	function doReturnMethod(methodName){
		if (methodName) {
			var data = IqpOverseeAgrList._obj.getSelectedData();
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

	function doReset(){
		page.dataGroups.IqpOverseeAgrGroup.reset();
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
		<emp:gridLayout id="IqpOverseeAgrGroup" title="输入查询条件" maxColumn="2">			
			<emp:text id="IqpOverseeAgr.mortgagor_id_displayname" label="客户名称" hidden="true"/>
			<emp:text id="IqpOverseeAgr.oversee_con_id_displayname" label="监管企业名称" hidden="true"/>
			<emp:text id="IqpOverseeAgr.mortgagor_id" label="客户码" />
			<emp:text id="IqpOverseeAgr.oversee_con_id" label="监管企业编号" />
		</emp:gridLayout>
	</form>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left"><br>
	<emp:returnButton label="选择返回"/> <br>
	</div>
	<emp:table icollName="IqpOverseeAgrList" pageMode="true" url="pageIqpOverseeAgrQuery4Pop.do">
		<emp:text id="oversee_agr_no" label="监管协议号" />
		<emp:text id="mortgagor_id" label="客户码" />
		<emp:text id="mortgagor_id_displayname" label="客户名称" />
		<emp:text id="oversee_con_id" label="监管企业编号" />
		<emp:text id="oversee_con_id_displayname" label="监管企业名称" />
		<emp:text id="status" label="协议状态" dictname="STD_ZB_STATUS" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" />
	</emp:table>
	<div align="left"><br>
	<emp:returnButton label="选择返回"/> <br>
	</div>
</body>
</html>
</emp:page>
    