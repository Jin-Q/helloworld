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
		IqpNetMagInfo._toForm(form);
		IqpNetMagInfoList._obj.ajaxQuery(null,form);
	};
	
	function doSelect(){	
		var data = IqpNetMagInfoList._obj.getSelectedData();
		if (data != null) {
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin.${context.popReturnMethod}(data[0])");	 
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doReset(){
		page.dataGroups.IqpNetMagInfoGroup.reset();
	};
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
	
<button onclick="doSelect()">选取返回</button>
	<emp:table icollName="IqpNetMagInfoList" pageMode="true" url="pagesearchCoreConQuery.do">	
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="status" label="状态" dictname="STD_ZB_STATUS"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="net_agr_no" label="网络协议编号" hidden="true"/>
		<emp:text id="serno" label="业务编号" hidden="true"/>
	</emp:table>
<button onclick="doSelect()">选取返回</button>
</body>
</html>
</emp:page>
    