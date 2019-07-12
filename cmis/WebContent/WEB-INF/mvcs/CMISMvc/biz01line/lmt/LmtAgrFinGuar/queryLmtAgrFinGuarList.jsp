<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String guar = "";
	if(context.containsKey("guar")){
		guar = (String)context.getDataValue("guar");
	}
%>
<emp:page>

	<html>
	<head>
	<title>列表查询页面</title>

	<jsp:include page="/include.jsp" flush="true" />

	<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtAgrFinGuar._toForm(form);
		LmtAgrFinGuarList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateLmtAgrFinGuarPage() {
		var paramStr = LmtAgrFinGuarList._obj.getParamStr(['agr_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAgrFinGuarUpdatePage.do"/>?'+paramStr+"&rt=yes&op=update";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtAgrFinGuar() {
		var paramStr = LmtAgrFinGuarList._obj.getParamStr(['agr_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAgrFinGuarViewPage.do"/>?'+paramStr+"&op=view";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddLmtAgrFinGuarPage() {
		var url = '<emp:url action="getLmtAgrFinGuarAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteLmtAgrFinGuar() {
		var paramStr = LmtAgrFinGuarList._obj.getParamStr(['agr_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteLmtAgrFinGuarRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.LmtAgrFinGuarGroup.reset();
	};

	function returnCus(data){
		LmtAgrFinGuar.cus_id._setValue(data.cus_id._getValue());//客户码
		LmtAgrFinGuar.cus_id_displayname._setValue(data.cus_name._getValue());//客户名称
	}
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
	</head>
	<body class="page_content">
	<form method="POST" action="#" id="queryForm">
		<emp:gridLayout id="LmtAgrFinGuarGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="LmtAgrFinGuar.serno" label="业务编号" />
			<emp:text id="LmtAgrFinGuar.agr_no" label="协议编号" />
			<emp:pop id="LmtAgrFinGuar.cus_id_displayname" label="客户名称" url="queryAllCusPop.do?cusTypCondition=cus_type='A2' and cus_status='20'&returnMethod=returnCus"/>
			<emp:text id="LmtAgrFinGuar.cus_id" label="客户码" hidden="true"/>
		</emp:gridLayout>
	</form>
	<jsp:include page="/queryInclude.jsp" flush="true" />

	<div align="left">
		<emp:button id="viewLmtAgrFinGuar" label="查看" op="view" />
		<%if(!guar.equals("guar")){ %> 
		<emp:button id="getUpdateLmtAgrFinGuarPage" label="授信维护" op="update" />
		<%} %>
	</div>

	<emp:table icollName="LmtAgrFinGuarList" pageMode="true" url="pageLmtAgrFinGuarQuery.do">
		<emp:text id="agr_no" label="协议编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="fin_cls" label="融资类别" />
		<emp:text id="fin_totl_limit" label="融资总额" dataType="Currency" />
		<emp:text id="single_quota" label="单户限额" dataType="Currency" />
		<emp:text id="guar_bail_multiple" label="担保放大倍数" dataType="Int" />
		<emp:text id="eval_rst" label="评级结果" />
		<emp:text id="lmt_start_date" label="授信起始日期" />
		<emp:text id="lmt_end_date" label="授信到期日期" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
	</emp:table>

	</body>
	</html>
</emp:page>
