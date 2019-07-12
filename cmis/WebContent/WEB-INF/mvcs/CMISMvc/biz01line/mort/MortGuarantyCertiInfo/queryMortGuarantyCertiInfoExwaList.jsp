<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<% 
Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
String act = ""; 
if(context.containsKey("act")){
    act = (String)context.getDataValue("act");
}
%>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		MortGuarantyCertiInfo._toForm(form);
		MortGuarantyCertiInfoList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.MortGuarantyCertiInfoGroup.reset();
	};

	function doReturnMethod(){
		var data = MortGuarantyCertiInfoList._obj.getSelectedData();
		if (data != null && data.length !=0) {
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin.${context.returnMethod}(data)");
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doSelect()
	{
		doReturnMethod();
	}
	function doClose(){
		window.close();
	}
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="MortGuarantyCertiInfoGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="MortGuarantyCertiInfo.guaranty_no" label="押品编号" />
			<emp:text id="MortGuarantyCertiInfo.warrant_no" label="权证编号" />
			<emp:text id="MortGuarantyCertiInfo.warrant_name" label="权证名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />

	
	<div align="left">
		<emp:returnButton id="s1" label="选择返回"/>
	</div>

	<emp:table icollName="MortGuarantyCertiInfoList" pageMode="true" url="pageMortGuarantyCertiInfoQuery.do?act=${context.act}&restrictUsed=${context.restrictUsed}" selectType="2">
		
		<emp:text id="warrant_cls" label="权证类别" dictname="STD_WARRANT_TYPE" />
		<emp:text id="guaranty_no" label="押品编号" />
		<emp:text id="warrant_type" label="权证类型" hidden="true"/>
		<emp:text id="warrant_no" label="权证编号" />
		<emp:text id="warrant_name" label="权证名称" />
		<emp:text id="is_main_warrant" label="是否主权证" dictname="STD_ZX_YES_NO"/>
		<emp:text id="guaranty_type" label="押品类型" hidden="true"/>
		<emp:text id="guaranty_no_displayname" label="押品名称" hidden="true"/>
		<emp:text id="keep_org_no" label="保管机构" hidden="true"/>
		<emp:text id="hand_org_no" label="经办机构" hidden="true"/>
		<emp:text id="keep_org_no_displayname" label="保管机构" />
		<emp:text id="hand_org_no_displayname" label="经办机构" hidden="true"/>
		<emp:text id="warrant_state" label="权证状态" dictname="STD_WARRANT_STATUS" />
		
	</emp:table>
	<div align="left">
		<emp:returnButton id="s2" label="选择返回"/>
		<emp:button id="close" label="关闭"/>
	</div>
</body>
</html>
</emp:page>
    