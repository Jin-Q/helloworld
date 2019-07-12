<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<% 
Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
String guaranty_no = (String)context.getDataValue("guaranty_no");
%>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		MortGuarantyInsurInfo._toForm(form);
		MortGuarantyInsurInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateMortGuarantyInsurInfoPage() {
		var paramStr = MortGuarantyInsurInfoList._obj.getParamStr(['insuarance_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getMortGuarantyInsurInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewMortGuarantyInsurInfo() {
		var paramStr = MortGuarantyInsurInfoList._obj.getParamStr(['insuarance_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getMortGuarantyInsurInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddMortGuarantyInsurInfoPage() {
		var url = '<emp:url action="getMortGuarantyInsurInfoAddPage.do"/>?guaranty_no='+'<%=guaranty_no%>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteMortGuarantyInsurInfo() {
		var paramStr = MortGuarantyInsurInfoList._obj.getParamStr(['insuarance_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o) {
					EMPTools.unmask();
					if (o.responseText !== undefined) {
						try {
							var jsonstr = eval("(" + o.responseText + ")");
						} catch (e) {
							alert("Parse jsonstr define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						if("success" == flag){
							alert("已删除！");
							window.location.reload();
						}else{
							alert("删除失败！");
						}
					}
				};
				var handleFailure = function(o) {
					alert("删除失败!");
				};
				var callback = {
					success :handleSuccess,
					failure :handleFailure
				};
				var url = '<emp:url action="deleteMortGuarantyInsurInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.MortGuarantyInsurInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:actButton id="getAddMortGuarantyInsurInfoPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateMortGuarantyInsurInfoPage" label="修改" op="update"/>
		<emp:actButton id="deleteMortGuarantyInsurInfo" label="删除" op="remove"/>
		<emp:actButton id="viewMortGuarantyInsurInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="MortGuarantyInsurInfoList" pageMode="true" url="pageMortGuarantyInsurInfoQuery.do?guaranty_no=${context.guaranty_no}">
		<emp:text id="guaranty_no" label="押品编号" hidden="true"/>
		<emp:text id="insuarance_no" label="保险单编号" />
		<emp:text id="insu_org_name" label="保险机构" />
		<emp:text id="insu_type" label="保险险种" dictname="STD_ZB_INSU_TYPE" />
		<emp:text id="insurant" label="被保险人" />
		<emp:text id="beneficiar" label="受益人" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="insure_amt" label="投保金额" />
		<emp:text id="insur_start_date" label="起始日期" />
		<emp:text id="insur_end_date" label="到期日期" />
		<emp:text id="inure_date" label="生效日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    