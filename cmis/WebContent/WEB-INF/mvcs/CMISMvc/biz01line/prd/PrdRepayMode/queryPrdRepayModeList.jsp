<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String showPrdBut = "";
	if(context.containsKey("showPrdBut")){
		showPrdBut = (String)context.getDataValue("showPrdBut");
	} 
%>
<emp:page>

<html>
<head>
<title>还款方式列表页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		PrdRepayMode._toForm(form);
		PrdRepayModeList._obj.ajaxQuery(null,form);
	};
	
	
	function doReset(){
		page.dataGroups.PrdRepayModeGroup.reset();
	};
	
	function doSelect() {
		var data = PrdRepayModeList._obj.getSelectedData();
		var methodName = "${context.returnMethod}";
		if (data != null&&data.length!=0) {
			window.opener[methodName](data[0]);
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};

	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PrdRepayModeGroup" title="输入查询条件" maxColumn="2">
			<emp:select id="PrdRepayMode.repay_mode_type" dictname="STD_ZB_REPAY_MODE" label="还款方式种类" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
	    <emp:button id="select" label="引入" op="add"/>
	</div>

	<emp:table icollName="PrdRepayModeList" pageMode="true" url="pagePrdRepayModeQuery.do" reqParams="prd_id=${context.prd_id}">
		<emp:text id="repay_mode_id" label="还款方式代码" />
		<emp:text id="repay_mode_type" label="还款方式种类" dictname="STD_ZB_REPAY_MODE" />  
		<emp:text id="min_term" label="支持最小期限(月)" />
		<emp:text id="max_term" label="支持最大期限(月)" />
		<emp:text id="repay_interval" label="还款间隔" hidden="true" />
		<emp:text id="firstpay_perc" label="首付比例" />
		<emp:text id="lastpay_perc" label="尾付比例" />
		<emp:text id="is_instm" label="是否期供类" dictname="STD_ZX_YES_NO" />
		<emp:text id="repay_mode_dec" label="还款方式描述" />
	</emp:table>
</body>
</html>
</emp:page>
    