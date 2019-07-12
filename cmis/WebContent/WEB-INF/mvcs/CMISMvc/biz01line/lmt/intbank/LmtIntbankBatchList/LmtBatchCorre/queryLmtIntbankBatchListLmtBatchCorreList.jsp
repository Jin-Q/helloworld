<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>

<html>
<head>
<title>子表列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>


<script type="text/javascript">
	
	
	function doImport(){
		var batch_cus_no = window.parent.window.LmtIntbankBatchList.batch_cus_no._getValue();
		var url = '<emp:url action="queryCusSameOrgPop4Batch.do"/>?batch_cus_no='+batch_cus_no;
		url = EMPTools.encodeURI(url);
		EMPTools.openWindow(url,'newwindow');
	};
	
	function doDelete() {
		var paramStr = LmtBatchCorreList._obj.getParamStr(['batch_cus_no','cus_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteLmtIntbankBatchListLmtBatchCorreRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doView() {
		var paramStr = LmtBatchCorreList._obj.getParamStr(['cus_id']);
		if (paramStr!=null) {
			var url = '<emp:url action="getIntbankViewPage.do"/>?cus_id='+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		}else {
			alert('请先选择一条记录！');
		}
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>

<body class="page_content">

	<div align="left">	
		<emp:button id="import" label="引入" />		
		<emp:button id="delete" label="剔除" />
		<emp:button id="intro" label="导入" op=""/>
		<emp:button id="view" label="查看" />
	</div>
							
	<emp:table icollName="LmtBatchCorreList" pageMode="true" url="pageLmtIntbankBatchListLmtBatchCorreQuery.do" reqParams="LmtIntbankBatchList.batch_cus_no=$LmtIntbankBatchList.batch_cus_no;">
		<emp:text id="batch_cus_no" label="批量客户编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="same_org_cnname" label="同业机构(行)名称" />
		<emp:select id="same_org_type" label="同业机构类型" dictname="STD_ZB_INTER_BANK_ORG"/>
		<emp:select id="cust_level" label="监管评级" dictname="STD_ZB_CUSTD_RATE"/>
		<emp:text id="assets" label="总资产" dataType="Currency"/>
		<emp:text id="paid_cap_amt" label="实收资本" dataType="Currency"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_date" label="登记日期" defvalue="$OPENDAY"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		
	</emp:table>
				
</body>
</html>
</emp:page>