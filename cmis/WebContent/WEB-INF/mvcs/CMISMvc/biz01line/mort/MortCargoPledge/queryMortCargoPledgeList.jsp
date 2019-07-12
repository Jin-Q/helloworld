<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<% 
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
	}
%>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		MortCargoPledge._toForm(form);
		MortCargoPledgeList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateMortCargoPledgePage() {
		var paramStr = MortCargoPledgeList._obj.getParamStr(['cargo_id']);
		if (paramStr != null) {
			var status = MortCargoPledgeList._obj.getParamValue(['cargo_status']);
			if("01"!=status){
				alert("非登记状态的货物记录，不能进行“修改”操作！");
				return;
			}
			var url = '<emp:url action="getMortCargoPledgeUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewMortCargoPledge() {
		var paramStr = MortCargoPledgeList._obj.getParamStr(['cargo_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getMortCargoPledgeViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddMortCargoPledgePage() {
		var guaranty_no = '${context.guaranty_no}';
		var url = '<emp:url action="getMortCargoPledgeAddPage.do"/>?guaranty_no='+guaranty_no;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteMortCargoPledge() {
		var paramStr = MortCargoPledgeList._obj.getParamStr(['cargo_id']);
		if (paramStr != null) {
			var status = MortCargoPledgeList._obj.getParamValue(['cargo_status']);
			if("01"!=status){
				alert("非登记状态的货物记录，不能进行“删除”操作！");
				return;
			}
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o) {
					if (o.responseText !== undefined) {
						try {
							var jsonstr = eval("(" + o.responseText + ")");
						} catch (e) {
							alert("Parse jsonstr define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						if("success" == flag){
							alert("已删除!");
							window.location.reload();
						}else{
							alert("删除失败!");
						}
					}
				};
				var handleFailure = function(o) {
				};
				var callback = {
					success :handleSuccess,
					failure :handleFailure
				};
				var url = '<emp:url action="deleteMortCargoPledgeRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
		 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback,null);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.MortCargoPledgeGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="MortCargoPledgeGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="MortCargoPledge.cargo_id" label="货物质押编号" />
		<emp:text id="MortCargoPledge.cargo_name" label="货物名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<%if(op.equals("update")||op.equals("add")) {%>
		<emp:button id="getAddMortCargoPledgePage" label="新增" op="add"/>
		<emp:button id="getUpdateMortCargoPledgePage" label="修改" op="update"/>
		<emp:button id="deleteMortCargoPledge" label="删除" op="remove"/>
		<%} %>
		<emp:button id="viewMortCargoPledge" label="查看" op="view"/>
	</div>

	<emp:table icollName="MortCargoPledgeList" pageMode="true" url="pageMortCargoPledgeQuery.do?guaranty_no=${context.guaranty_no}">
		<emp:text id="cargo_id" label="货物编号" />
		<emp:text id="cargo_name" label="货物名称" />
		<emp:text id="guaranty_catalog" label="押品所处目录" hidden="true" />
		<emp:text id="guaranty_catalog_displayname" label="押品所处目录" />
		<emp:text id="identy_total" label="银行认定总价" dataType="Currency"/>
		<emp:text id="storage_date" label="入库日期" />
		<emp:text id="exware_date" label="出库日期" />
		<emp:text id="cargo_status" label="状态" dictname="STD_CARGO_STATUS" />
		<emp:text id="reg_date" label="登记日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    