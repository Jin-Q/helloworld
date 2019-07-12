<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<% 
	String cusid = (String)request.getParameter("CusSocialProtection.cus_id");
%>
<emp:page>

<html>
<head>
<title>社会保障信息</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusSocialProtection._toForm(form);
		CusSocialProtectionList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusSocialProtectionPage() {
		var paramStr = CusSocialProtectionList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var editFlag = '${context.EditFlag}';
			var url = '<emp:url action="getCusSocialProtectionUpdatePage.do"/>?'+paramStr+"&EditFlag="+editFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请选择一条数据!');
		}
	};
	
	function doViewCusSocialProtection() {
		var paramStr = CusSocialProtectionList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var editFlag = '${context.EditFlag}';
			var url = '<emp:url action="getCusSocialProtectionViewPage.do"/>?'+paramStr+"&EditFlag="+editFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请选择一条数据!');
		}
	};
	
	function doGetAddCusSocialProtectionPage() {
		var editFlag = '${context.EditFlag}';
		var url = '<emp:url action="getCusSocialProtectionAddPage.do"/>?' + "CusSocialProtection.cus_id=<%=cusid%>&EditFlag="+editFlag;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusSocialProtection() {
		var paramStr = CusSocialProtectionList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusSocialProtectionRecord.do"/>?'+paramStr+ "&CusSocialProtection.cus_id=<%=cusid%>";
				url = EMPTools.encodeURI(url);
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr define error!"+e);
							return;
						}
						var flag = jsonstr.flag;
						if(flag=="success"){
							alert("删除成功!");
							window.location.reload();
					   }else {
						 alert(flag);
						 return;
					   }
					}
				};
				var handleFailure = function(o){	
				};
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				}; 
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback) 
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CusSocialProtectionGroup.reset();
	};

	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<%
	//    String flag=(String)request.getSession().getAttribute("buttonFlag");
		Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
		String flag = context.getDataValue("EditFlag").toString();
		if(!(flag!=null&&flag.equals("query"))){
	%>
	<div align="left">
		<emp:button id="getAddCusSocialProtectionPage" label="新增"/>
		<emp:button id="viewCusSocialProtection" label="查看"/>
		<emp:button id="getUpdateCusSocialProtectionPage" label="修改"/>
		<emp:button id="deleteCusSocialProtection" label="删除"/>
	</div>
	<%}else{ %>
		<div align="left">
			<emp:button id="viewCusSocialProtection" label="查看"/>
		</div>
	<%} %>
	<emp:table icollName="CusSocialProtectionList" pageMode="true" url="pageCusSocialProtectionQuery.do" reqParams="CusSocialProtection.cus_id=${context.CusSocialProtection.cus_id}&EditFlag=${context.EditFlag}">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="social_prot_id" label="社会保障号码" />
		<emp:text id="provid_fund_bal" label="公积金余额" dataType="Currency"/>
		<emp:text id="family_pay_monthly_total" label="家庭月缴合计" dataType="Currency"/>
		<emp:text id="with_cust_rela" label="与客户关系" dictname="STD_ZB_INDIV_CUS"/>
		<emp:text id="serno" label="流水号" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>