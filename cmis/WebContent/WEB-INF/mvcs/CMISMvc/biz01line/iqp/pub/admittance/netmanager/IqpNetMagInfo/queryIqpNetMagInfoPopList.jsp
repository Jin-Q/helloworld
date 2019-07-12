<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面pop框</title>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	/*--user code begin--*/
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpNetMagInfo._toForm(form);
		IqpNetMagInfoList._obj.ajaxQuery(null,form);
	};
	
	
	function doReset(){
		page.dataGroups.IqpNetMagInfoGroup.reset();
	};
	
	function doReturnMethod(){
		var data = IqpNetMagInfoList._obj.getSelectedData();
		if (data != null && data.length !=0) {
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin.${context.returnMethod}(data[0])");
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doSelect(){
		doReturnMethod();
	}
	/*--user code end--*/
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="IqpNetMagInfoGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="IqpNetMagInfo.cus_id" label="客户码" />
		<emp:text id="IqpNetMagInfo.cus_id_displayname" label="客户名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />

	<emp:returnButton id="s1" label="选择返回"/>
	<emp:table icollName="IqpNetMagInfoList" pageMode="true" url="pageIqpNetMagInfoQuery.do?cus_id=${context.cus_id}&biz_type=${context.biz_type}">
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="net_agr_no" label="网络编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="net_agr_no" label="网络协议编号" hidden="true"/>
		<emp:select id="flow_type" label="流程类型" hidden="true"/>
	</emp:table>
	<emp:returnButton id="s2" label="选择返回"/>
</body>
</html>
</emp:page>