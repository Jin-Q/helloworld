<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
	String flag = request.getParameter("flag");
%>
<emp:page>
<html>
<head>
<title>新增页面</title>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">

	/*--user code begin--*/
	//保存按钮操作事件
	function doNext(){
		var result = IqpBailSubDis._checkAll();
		if(result){
			var form = document.getElementById('submitForm');
			IqpBailSubDis._toForm(form);
			var flag =<%=flag%>;
			var cont_no = IqpBailSubDis.cont_no._getValue();
			var cus_id = IqpBailSubDis.cus_id._getValue();
			var cus_id_displayname = IqpBailSubDis.cus_id_displayname._getValue();
			var url = '<emp:url action="getIqpBailSubDisAddPage.do"/>?flag='+flag+'&cont_no='+cont_no+'&cus_id='+cus_id+'&cus_id_displayname='+cus_id_displayname;
			url = EMPTools.encodeURI(url);
			window.location=url;
		}else{
			 alert("保存失败！\n请检查各标签页面中的必填信息是否遗漏！");
		}
	}
	//返回按钮操作事件
	function doReturn(){
		var flag =<%=flag%>
		var url = '<emp:url action="queryIqpBailSubDisList.do"/>?flag='+flag;
		url = EMPTools.encodeURI(url);
		window.location=url;
	}
	//合同编号pop返回事件
	function returnContNo(data){
		IqpBailSubDis.cont_no._setValue(data.cont_no._getValue());
		IqpBailSubDis.cus_id._setValue(data.cus_id._getValue());
		IqpBailSubDis.cus_id_displayname._setValue(data.cus_id_displayname._getValue());
		checkExist();
	}
	//检验此合同是否有在途
	function checkExist(){
		var flag =<%=flag%>;
		var cont_no = IqpBailSubDis.cont_no._getValue();
		var url = "<emp:url  action='checkBailExist.do'/>?cont_no="+cont_no+"&flag="+flag;	
		url = EMPTools.encodeURI(url);
		var callback = {
			success : "doReturnMethod",
			isJSON : true
		};
		EMPTools.ajaxRequest('GET', url, callback);
	}
	function doReturnMethod(json, callback){

		if('false'==json.result){
			//未发现重名客户
		}else if('canNot'==json.result){
			alert("该笔合同不存在保证金，不能做提取操作！");
			IqpBailSubDis.cont_no._setValue("");
			IqpBailSubDis.cus_id._setValue("");
			IqpBailSubDis.cus_id_displayname._setValue("");
		}else if('true'==json.result){
			alert("该笔合同有在途的保证金追加或提取操作，在操作未结束之前不允许重复！");
			IqpBailSubDis.cont_no._setValue("");
			IqpBailSubDis.cus_id._setValue("");
			IqpBailSubDis.cus_id_displayname._setValue("");
		}else{
			alert('查询保证金追加/提取表出错！');
		}

	}
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="" method="POST">
		<emp:gridLayout id="IqpBailSubDisGroup" title="请选择合同编号：" maxColumn="1">
			<emp:pop id="IqpBailSubDis.cont_no" label="合同编号" url="queryIqpBailSubDisPopList.do?returnMethod=returnContNo" required="true" returnMethod="returnContNo"/>
			<emp:text id="IqpBailSubDis.cus_id" label="客户码" maxlength="60" required="false" hidden="true"/>
			<emp:text id="IqpBailSubDis.cus_id_displayname" label="客户名称" required="false" hidden="true"/>
		</emp:gridLayout>
	</emp:form>
		<div align="center">
			<br>
			<emp:button id="next" label="保存" op="add"/>
			<emp:button id="return" label="返回"/>
		</div>
</body>
</html>
</emp:page>

