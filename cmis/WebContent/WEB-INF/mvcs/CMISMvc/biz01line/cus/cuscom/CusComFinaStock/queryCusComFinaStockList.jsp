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
		CusComFinaStock._toForm(form);
		CusComFinaStockList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusComFinaStockPage() {
		var paramStr = CusComFinaStockList._obj.getParamStr(['cus_id','com_stk_code']);
		if (paramStr != null) {
			var editFlag = '${context.EditFlag}';
			var url = '<emp:url action="getCusComFinaStockUpdatePage.do"/>?'+paramStr+"&EditFlag="+editFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusComFinaStock() {
		var paramStr = CusComFinaStockList._obj.getParamStr(['cus_id','com_stk_code']);
		if (paramStr != null) {
			var editFlag = '${context.EditFlag}';
			var url = '<emp:url action="getCusComFinaStockViewPage.do"/>?'+paramStr+"&EditFlag="+editFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusComFinaStockPage() {
		var editFlag = '${context.EditFlag}';
		var cus_id  ='${context.CusComFinaStock.cus_id}';
		var paramStr="CusComFinaStock.cus_id="+cus_id+"&EditFlag="+editFlag;
		var url = '<emp:url action="getCusComFinaStockAddPage.do"/>&'+paramStr;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusComFinaStock() {
		var paramStr = CusComFinaStockList._obj.getParamStr(['cus_id','com_stk_code']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusComFinaStockRecord.do"/>?'+paramStr;
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
						    var cus_id  ='${context.CusComFinaStock.cus_id}';
						    var editFlag = '${context.EditFlag}';
							var paramStr="CusComFinaStock.cus_id="+cus_id+"&EditFlag="+editFlag;
							var url = '<emp:url action="queryCusComFinaStockList.do"/>&'+paramStr;
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
		page.dataGroups.CusComFinaStockGroup.reset();
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
//	String flag=(String)request.getSession().getAttribute("buttonFlag");
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String flag = context.getDataValue("EditFlag").toString();
	if(!(flag!=null&&flag.equals("query"))){
	%>
		<emp:button id="getAddCusComFinaStockPage" label="新增" />
		<emp:button id="viewCusComFinaStock" label="查看" />
		<emp:button id="getUpdateCusComFinaStockPage" label="修改" />
		<emp:button id="deleteCusComFinaStock" label="删除" />
	<% }else{ %>
		<emp:button id="viewCusComFinaStock" label="查看" />
	<%}%>
	
	</div>
	<emp:table icollName="CusComFinaStockList" pageMode="true" url="pageCusComFinaStockQuery.do" reqParams="CusComFinaStock.cus_id=${context.CusComFinaStock.cus_id}&EditFlag=${context.EditFlag}">
		<emp:text id="com_stk_code" label="股票代码" />
		<emp:text id="com_stk_name" label="股票名称" />
		<emp:text id="com_stk_mrk_dt" label="上市日期" />
		<emp:text id="com_stk_mrk_place" label="上市地" dictname="STD_ZX_LISTED" />
		<emp:text id="com_stk_cap_qnt" label="当前股本总量(万股)" />
		<emp:text id="cus_id" label="客户码" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    