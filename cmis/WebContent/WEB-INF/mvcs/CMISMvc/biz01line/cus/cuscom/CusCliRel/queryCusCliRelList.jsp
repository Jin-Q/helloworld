<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusComRelInvest._toForm(form);
		CusComRelInvestList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusCliRelPage() {
		var paramStr = CusComRelInvestList._obj.getParamStr(['cus_id','cus_id_rel']);
		var EditFlag  ='${context.EditFlag}';
		if (paramStr != null) {
			var url = '<emp:url action="getCusCliRelUpdatePage.do"/>?'+paramStr+"&EditFlag="+EditFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusCliRel() {
		var paramStr = CusCliRelList._obj.getParamStr(['cus_id','cus_id_rel']);
		var EditFlag  ='${context.EditFlag}';
		if (paramStr != null) {
			var url = '<emp:url action="getCusCliRelViewPage.do"/>?'+paramStr+"&EditFlag="+EditFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusCliRelPage() {
		var cus_id  ='${context.CusCliRel.cus_id}';
	//	var cert_code  ='${context.cert_code}';
		var EditFlag  ='${context.EditFlag}';
		var paramStr="CusCliRel.cus_id="+cus_id;
		var url = '<emp:url action="getCusCliRelAddPage.do"/>&'+paramStr+"&EditFlag="+EditFlag;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusCliRel() {
		var paramStr = CusCliRelList._obj.getParamStr(['cus_id','cus_id_rel']);

		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusCliRelRecord.do"/>?'+paramStr;
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
						if(flag=="删除成功"){
							alert("删除成功!");
							    var cus_id  ='${context.CusCliRel.cus_id}';
								var paramStr="CusCliRel.cus_id="+cus_id;
								var EditFlag  ='${context.EditFlag}';
								var url = '<emp:url action="queryCusCliRelList.do"/>&'+paramStr+"&EditFlag="+EditFlag;
								url = EMPTools.encodeURI(url);
								window.location = url;
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
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CusComRelInvestGroup.reset();
	};

	function doCusCliRelview(){
		var url;
		var flag = "query";
		var oper = "infotree";
		
		var cus_id_rel = CusCliRelList._obj.getSelectedData()[0].cus_id_rel._getValue();
		var paramStr = "cus_id="+cus_id_rel; 

		url = '<emp:url action="getCusComTree.do"/>?'+paramStr+"&flag="+flag+"&oper="+oper;
		url = EMPTools.encodeURI(url);
		 window.open(url,'','height=480, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=auto, resizable=yes,location=no, status=no');
	   }; 

	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	
	<div align="left">
	<%
		Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
		String flag = context.getDataValue("EditFlag").toString();
		if(!(flag!=null&&flag.equals("query"))){
	%>
		<emp:button id="getAddCusCliRelPage" label="新增" />
<!--		<emp:button id="viewCusCliRel" label="查看" />-->
<!--		<emp:button id="getUpdateCusCliRelPage" label="修改" />-->
		<emp:button id="deleteCusCliRel" label="删除" />
	<%
	}else{
		%>
<!--			<emp:button id="viewCusCliRel" label="查看" />-->
		<%
	}
	%>
	</div>
	<emp:table icollName="CusCliRelList" pageMode="true" url="pageCusCliRelQuery.do" reqParams="CusCliRel.cus_id=${context.CusCliRel.cus_id}&EditFlag=${context.EditFlag}">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cert_type" label="证件类型"  dictname="STD_ZB_CERT_TYP" />
		<emp:text id="cert_code" label="证件号码" />
		<emp:link id="cus_id_rel" label="关联客户客户码" operation="CusCliRelview"/>
		<emp:text id="cus_name_rel" label="关联客户名称"  />
		<emp:text id="cus_type_rel" label="关联客户类型"   dictname="STD_RELA_CUS_TYP" />
	</emp:table>
</body>
</html>
</emp:page>
    