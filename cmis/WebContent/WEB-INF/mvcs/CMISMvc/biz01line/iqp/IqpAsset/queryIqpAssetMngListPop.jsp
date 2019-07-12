<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpAsset._toForm(form);
		IqpAssetList._obj.ajaxQuery(null,form);
	};
	function doReset(){
		page.dataGroups.IqpAssetGroup.reset();
	};
	/*--user code begin--*/
	function doSelect(){
		var data = IqpAssetList._obj.getSelectedData();
		if(data!=null&&data!=''){
			var status = data[0].status._getValue();
			//alert(status);
			if(status!='01'){
				alert('该资产包已被业务引用！');
				return;
			}
			window.opener["${context.returnMethod}"](data[0]);
			window.close();
		}else{
			alert('请先选择一条记录！');
		}
	};	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpAssetGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpAsset.asset_no" label="资产包编号" />
			<emp:text id="IqpAsset.asset_name" label="资产包名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="select" label="选取返回" op="add"/>
	</div>

	<emp:table icollName="IqpAssetList" pageMode="true" url="pageIqpAssetMngListPop.do">
	    <emp:text id="asset_no" label="资产包编号" maxlength="40" required="false" readonly="true" />
		<emp:text id="asset_name" label="资产包名称" maxlength="100" required="false" readonly="true"/>
		<emp:text id="asset_type" label="资产类型" required="false" dictname="STD_ZB_ASSET_TYPE"/>
		<emp:select id="takeover_type" label="转让方式" required="false" dictname="STD_ZB_TAKEOVER_MODE"/>
		<emp:text id="asset_qnt" label="资产数量" maxlength="38" required="false" dataType="Int"/>   		
		<emp:text id="asset_total_amt" label="资产总额" maxlength="18" required="false" dataType="Currency" />
		<emp:text id="takeover_total_amt" label="转让总额" maxlength="18" required="false" dataType="Currency" />
		<emp:text id="takeover_total_int" label="转让利息" maxlength="18" required="false" dataType="Currency" />
		<emp:text id="takeover_date" label="转让日期"  />
		<emp:select id="status" label="资产包状态" required="false" dictname="STD_ZB_ASSET_STATUS" readonly="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    