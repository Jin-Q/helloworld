<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>
<%
	String cusid = (String) request.getParameter("cus_id");
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String flag = context.getDataValue("EditFlag").toString();
%>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusIndivBusiness._toForm(form);
		CusIndivBusinessList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusIndivBusinessPage() {
		var paramStr = CusIndivBusinessList._obj.getParamStr(['serno']) + "&"
			+ CusIndivBusinessList._obj.getParamStr(['cus_id']);
		if (paramStr != "null&null") {
			var url = '<emp:url action="getCusIndivBusinessUpdatePage.do"/>?'+paramStr+"&EditFlag=edit";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusIndivBusiness() {
		var paramStr = CusIndivBusinessList._obj.getParamStr(['serno']) + "&" + CusIndivBusinessList._obj.getParamStr(['cus_id']);
		var flag = '<%=flag%>';
		if (paramStr != "null&null") {
			var url = '<emp:url action="getCusIndivBusinessViewPage.do"/>?'+paramStr+"&EditFlag="+flag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusIndivBusinessPage() {
		var url = '<emp:url action="getCusIndivBusinessAddPage.do"/>?' + "cus_id=<%=cusid%>"+"&EditFlag=edit";
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusIndivBusiness() {
		var paramStr = CusIndivBusinessList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusIndivBusinessRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var handleSuccess = function(o){ 
					EMPTools.unmask();
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("删除失败!");
							return;
						}
						var flag=jsonstr.flag;	
						if(flag=="success"){
							window.location.reload();				
						}
					}	
				};
				var handleFailure = function(o){ 
					alert("删除失败，请联系管理员");
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
		page.dataGroups.CusIndivBusinessGroup.reset();
	};
	
	/*--user code begin--*/
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

<%
   // String flag=(String)request.getSession().getAttribute("buttonFlag");
    if(!(flag!=null&&flag.equals("query"))){
%>
	<div align="left">
		<emp:button id="getAddCusIndivBusinessPage" label="新增" />
		<emp:button id="getUpdateCusIndivBusinessPage" label="修改" />
		<emp:button id="deleteCusIndivBusiness" label="删除" />
		<emp:button id="viewCusIndivBusiness" label="查看" />
	</div>
	<%}else{ %>
		<emp:button id="viewCusIndivBusiness" label="查看" />
	<%} %>
	<emp:table icollName="CusIndivBusinessList" pageMode="true" url="pageCusIndivBusinessQuery.do?cus_id=${context.cus_id}">
		<emp:text id="com_name" label="企业名称" />
		<emp:text id="real_controller" label="实际控制人" />
		<emp:text id="ent_model" label="企业规模" dictname="STD_ZB_ENTERPRISE"/>
		<emp:text id="prop_qlty" label="产权性质" dictname="STD_PROP_QLTY"/>
		<emp:text id="reg_fund" label="注册资金" />
		<emp:text id="serno" label="SERNO" hidden="true"/>
		<emp:text id="cus_id" label="客户码" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    