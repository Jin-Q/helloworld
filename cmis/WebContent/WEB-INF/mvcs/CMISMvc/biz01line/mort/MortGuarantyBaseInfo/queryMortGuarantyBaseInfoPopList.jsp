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
		MortGuarantyBaseInfo._toForm(form);
		MortGuarantyBaseInfoList._obj.ajaxQuery(null,form);
	};
	
	
	function doReset(){
		page.dataGroups.MortGuarantyBaseInfoGroup.reset();
	};
	
	/*--user code begin--*/
	function doReturnMethod(){
		var data = MortGuarantyBaseInfoList._obj.getSelectedData();
		if (data != null && data.length !=0) {
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin.${context.returnMethod}(data[0])");
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doSelect()
	{
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

	<emp:gridLayout id="MortGuarantyBaseInfoGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="MortGuarantyBaseInfo.guaranty_no" label="押品编号" />
			<emp:select id="MortGuarantyBaseInfo.guaranty_cls" label="押品类别" dictname="STD_GUARANTY_TYPE" />
			<emp:select id="MortGuarantyBaseInfo.guaranty_info_status" label="押品信息状态" dictname="STD_MORT_STATE" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
	    <emp:returnButton id="s1" label="选择返回"/>
	</div>

	<emp:table icollName="MortGuarantyBaseInfoList" pageMode="true" url="pageMortGuarantyBaseInfoQuery1.do">
		<emp:text id="guaranty_no" label="押品编号" />
		<emp:text id="guaranty_name" label="押品名称" />
		<emp:text id="agr_no" label="监管协议编号" />
		<emp:text id="cus_id" label="出质人客户码" />
		<emp:text id="cus_id_displayname" label="出质人客户名称" />
		<emp:text id="guaranty_cls" label="押品类别" dictname="STD_GUARANTY_TYPE" />
		<emp:text id="guaranty_type" label="押品类型" hidden="true"/>
		<emp:text id="guaranty_type_displayname" label="押品类型"/>
		<emp:text id="guaranty_info_status" label="押品信息状态" dictname="STD_MORT_STATE" />
		<emp:text id="input_id" label="责任人" />
		<emp:text id="input_br_id" label="责任机构" />
		<emp:text id="input_date" label="登记日期" />
	</emp:table>
	<div align="left">
		<br>
		<emp:returnButton id="s2" label="选择返回"/>
		<emp:button id="cancel" label="关闭" />
	</div>
</body>
</html>
</emp:page>
    