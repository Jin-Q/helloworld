<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.*"%>
<%@page import="com.ecc.emp.data.*"%>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String scheme_id = (String)context.getDataValue("scheme_id");
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	function doSelect(){
		var data = PspCheckCatalogList._obj.getSelectedData();
		if (data != null) {

			var catalog_id = data[0].catalog_id._getValue();
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					if(flag == "success"){
						window.opener["${context.returnMethod}"](data[0]);
						window.close(); 
					}else {
						alert("已存在！"); 
					}
				}
			};
			var handleFailure = function(o){
				alert("异步请求出错！");	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var url = '<emp:url action="checkCataOp.do"/>?catalog_id='+catalog_id+'&scheme_id='+'<%=scheme_id %>';
			url = EMPTools.encodeURI(url);  
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
		} else {
			alert('请先选择一条记录！');
		}
		
	};

	
	function doReset(){
		page.dataGroups.PspCheckCatalogGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PspCheckCatalogGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PspCheckCatalog.catalog_id" label="目录编号" />
			<emp:text id="PspCheckCatalog.catalog_name" label="目录名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="select" label="选择" />
	</div>

	<emp:table icollName="PspCheckCatalogList" pageMode="true" url="pagePspCheckCatalogQuery.do">
		<emp:text id="catalog_id" label="目录编号" />
		<emp:text id="catalog_name" label="目录名称" />
		<emp:text id="memo" label="备注" />
		<emp:text id="input_id" label="登记人" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="input_br_id" label="登记机构" />
	</emp:table>
	
</body>
</html>
</emp:page>
    