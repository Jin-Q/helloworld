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
		AccAssetTrans._toForm(form);
		AccAssetTransList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateAccAssetTransPage() {
		var paramStr = AccAssetTransList._obj.getParamStr(['bill_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getAccAssetTransUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewAccAssetTrans() {
		var paramStr = AccAssetTransList._obj.getParamStr(['bill_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getAccAssetTransViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddAccAssetTransPage() {
		var url = '<emp:url action="getAccAssetTransAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteAccAssetTrans() {
		var paramStr = AccAssetTransList._obj.getParamStr(['bill_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
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
							alert("删除成功!");
							var url = '<emp:url action="queryAccAssetTransList.do"/>';
							url = EMPTools.encodeURI(url);
							window.location = url;
						}else {
							alert("删除异常!");
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
				var url = '<emp:url action="deleteAccAssetTransRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.AccAssetTransGroup.reset();
	};

	/** 台账导出 **/
	function doExportExcel(){                                                                                             
		var form = document.getElementById("queryForm");
		AccAssetTrans._toForm(form);
		form.submit();
	}
	/**add by lisj 2015-3-11 需求编号:【XD150303017】 信贷资产证券化改造 begin**/
	function doUpdate4SecMS(){
		var paramStr = AccAssetTransList._obj.getParamStr(['bill_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getAccAssetTrans4SecMSPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	}
	
	function returnCus(data){
		AccAssetTrans.cus_id._setValue(data.cus_id._getValue());
		AccAssetTrans.cus_id_displayname._setValue(data.cus_name._getValue());
	};
	/**add by lisj 2015-3-11 需求编号:【XD150303017】 信贷资产证券化改造 end**/
</script>
</head>
<body class="page_content">
	<emp:form  method="POST" action="accAssetTransToExcel.do" id="queryForm" >
	<emp:gridLayout id="AccAssetTransGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="AccAssetTrans.bill_no" label="借据编号" />
	        <emp:text id="AccAssetTrans.cont_no" label="合同编号" />
			<emp:select id="AccAssetTrans.acc_status" label="资产状态" dictname="STD_ZB_ACC_TRANS_STATUS"/>
			<emp:pop id="AccAssetTrans.cus_id_displayname" label="客户名称" buttonLabel="选择" url="queryAllCusPop.do?returnMethod=returnCus" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
			<emp:text id="AccAssetTrans.cus_id" label="客户码"  hidden="true" />
	</emp:gridLayout>
	</emp:form>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewAccAssetTrans" label="查看" op="view"/>
		<emp:button id="exportExcel" label="导出" op="putout"/>
		<emp:button id="update4SecMS" label="台账信息维护" />
	</div>

	<emp:table icollName="AccAssetTransList" pageMode="true" url="pageAccAssetTransQuery.do">
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="loan_amt" label="贷款金额" dataType="Currency"/>
		<emp:text id="pro_short_name" label="项目简称" />
		<emp:text id="asset_type" label="资产类型" dictname="STD_ZB_ASSETTRANS_TYPE"/>
		<emp:text id="rebuy_date" label="赎回日期" />
		<emp:text id="acc_status" label="资产状态" dictname="STD_ZB_ACC_TRANS_STATUS"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    