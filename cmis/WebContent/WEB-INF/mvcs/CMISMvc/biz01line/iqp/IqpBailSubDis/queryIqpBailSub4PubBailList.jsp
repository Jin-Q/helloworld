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
		IqpBailSubDis._toForm(form);
		IqpBailSubDisList._obj.ajaxQuery(null,form);
	};
	
	function doViewIqpBailSubDis() {
		var flag = '${context.flag}';
		var paramStr = IqpBailSubDisList._obj.getParamStr(['serno','cont_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpBailSubDisViewPage.do"/>?flow=wf&'+paramStr+'&flag='+flag+'&op=view';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpBailSubDisGroup.reset();
	};
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<div align="left">
		<emp:button id="viewIqpBailSubDis" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpBailSubDisList" pageMode="true" url="pageIqpBailSubDisQuery4PubBail.do?cont_no=${context.cont_no}">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="ori_bail_amt" label="原保证金金额" dataType="Currency"/>
		<emp:text id="adjust_bail_amt" label="追加保证金金额" dataType="Currency"/>
		<emp:text id="adjusted_bail_amt" label="追加后保证金金额" dataType="Currency"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    