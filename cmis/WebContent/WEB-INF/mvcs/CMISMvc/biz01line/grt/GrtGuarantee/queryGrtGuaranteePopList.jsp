<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		GrtGuarantee._toForm(form);
		GrtGuaranteeList._obj.ajaxQuery(null,form);
	};
	function doReturnMethod(){
		var data = GrtGuaranteeList._obj.getSelectedData();
		if (data != null && data.length !=0) {
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin.${context.returnMethod}(data[0])");
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doCancel(){
		window.close();
	};
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
	  <emp:returnButton id="s1" label="引入"/>
	</div>
 	
	<emp:table icollName="GrtGuaranteeList" pageMode="false" url="pageGrtGuaranteeQuery.do">
		<emp:text id="guar_id" label="保证编码 " />
		<emp:text id="guar_type" label="保证形式" dictname="STD_GUAR_FORM" />
		<emp:text id="guar_amt" label="担保金额" dataType="Currency"/>
		<emp:text id="cus_id" label="保证人客户码" />
		<emp:text id="cus_id_displayname" label="保证人客户名称" />
		<emp:select id="is_spadd" label="是否为追加担保" dictname="STD_ZX_YES_NO" />
		
	</emp:table>
	<div align="left">
			<br>
 			<emp:returnButton id="s2" label="引入"/>
			<emp:button id="cancel" label="关闭" />
	</div>
</body>
</html>
</emp:page>
    