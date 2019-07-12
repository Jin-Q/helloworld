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
	IqpAbsProApp._toForm(form);
	IqpAbsProAppList._obj.ajaxQuery(null,form);
};


function doReset(){
	page.dataGroups.IqpAbsProAppGroup.reset();
};

function doReturnMethod(){
	var data = IqpAbsProAppList._obj.getSelectedData();
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
function doCancel(){
	window.close();
};

</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="IqpAbsProAppGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpAbsProApp.batch_no" label="批次号" />
			<emp:text id="IqpAbsProApp.batch_name" label="批次名称"/>
			<emp:date id="IqpAbsProApp.pre_package_date" label="预封包日期"/>
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" /> 
	<emp:table icollName="IqpAbsProAppList" pageMode="true" url="queryAllIqpAbsProAppPopQuery.do">
		<emp:text id="batch_no" label="批次号" />
		<emp:text id="pre_package_serno" label="流水号" />
		<emp:text id="batch_name" label="批次名称"/>
		<emp:text id="trust_org_no" label="受托机构名称"/>
	    <emp:select id="is_this_org_service" label="是否本机构服务" dictname="STD_ZX_YES_NO"/>
		<emp:text id="pre_package_date" label="预封包日期" />
		<emp:text id="input_id" label="操作人员" hidden="true"/>
		<emp:text id="prc_status" label="处理状态" dictname="STD_ABS_PRC_STATUS"/>
		<emp:text id="input_br_id" label="操作机构" hidden="true"/>
		<emp:text id="update_date" label="修改日期" hidden="true"/>
		<emp:text id="pre_package_name" label="预封包名称" hidden="true"/>
		
	</emp:table>
	<div align="left">
			<br>
 			<emp:returnButton id="s2" label="选择返回"/>
			<emp:button id="cancel" label="关闭" />
	</div>
</body>
</html>
</emp:page>
    