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
		IqpAbsBatchMng._toForm(form);
		IqpAbsBatchMngList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpAbsBatchMngPage() {
		var paramStr = IqpAbsBatchMngList._obj.getParamStr(['batch_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAbsBatchMngUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAbsBatchMng() {
		var paramStr = IqpAbsBatchMngList._obj.getParamStr(['batch_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAbsBatchMngViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpAbsBatchMngPage() {
		var url = '<emp:url action="getIqpAbsBatchMngAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpAbsBatchMng() {
		var paramStr = IqpAbsBatchMngList._obj.getParamStr(['batch_no']);
		if (paramStr != null) {

			var acct_status = IqpAbsBatchMngList._obj.getParamValue(['acct_status']);
            if(acct_status != "01"){
            	alert("账务状态为【未封包】的批次信息才允许做删除操作！");
            	return;
            }
			
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o) {
					if (o.responseText !== undefined) {
						try {
							var jsonstr = eval("(" + o.responseText + ")");
						} catch (e) {
							alert("Parse jsonstr define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						if(flag =="success"){
							alert("删除成功!");
						}else{
							alert("删除失败!");
						}
					}
				};
				var handleFailure = function(o) {
					alert("异步请求出错！");	
				};
				var callback = {
					success :handleSuccess,
					failure :handleFailure
				};
				var url = '<emp:url action="deleteIqpAbsBatchMngRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpAbsBatchMngGroup.reset();
	};

	function doCancelIqpAbsBatchMng(){
		var paramStr = IqpAbsBatchMngList._obj.getParamStr(['batch_no']);
		if (paramStr != null) {
			var acct_status = IqpAbsBatchMngList._obj.getParamValue(['acct_status']);
            if(acct_status == "05"){
            	if(confirm("是否确认要注销？")){
    				var handleSuccess = function(o) {
    					if (o.responseText !== undefined) {
    						try {
    							var jsonstr = eval("(" + o.responseText + ")");
    						} catch (e) {
    							alert("Parse jsonstr define error!" + e.message);
    							return;
    						}
    						var flag = jsonstr.flag;
    						if(flag =="success"){
    							alert("注销成功!");
    						}else{
    							alert("注销失败!");
    						}
    					}
    				};
    				var handleFailure = function(o) {
    					alert("异步请求出错！");	
    				};
    				var callback = {
    					success :handleSuccess,
    					failure :handleFailure
    				};
    				var url = '<emp:url action="cancelIqpAbsBatchMngRecord.do"/>?'+paramStr;
    				var url="";
    				url = EMPTools.encodeURI(url);
    				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
    			}
            }else{
			  alert("账务状态为【已记账】的批次信息才允许做注销操作！");
            }
		} else {
			alert('请先选择一条记录！');
		}
	};
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpAbsBatchMngGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpAbsBatchMng.batch_no" label="批次号" />
			<emp:text id="IqpAbsBatchMng.batch_name" label="证券化批次名称"/>
		    <emp:text id="IqpAbsBatchMng.trust_org_no" label="受托机构名称"/>
	        <emp:select id="IqpAbsBatchMng.is_this_org_service" label="是否本机构服务" dictname="STD_ZX_YES_NO"/>
	        <emp:text id="IqpAbsBatchMng.input_id" label="操作人员" />
	        <emp:date id="IqpAbsBatchMng.trust_date" label="信托成立日" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" /> 
	
	<div align="left">
		<emp:button id="getAddIqpAbsBatchMngPage" label="新增" />
		<emp:button id="getUpdateIqpAbsBatchMngPage" label="修改" />
		<emp:button id="deleteIqpAbsBatchMng" label="删除" />
		<emp:button id="viewIqpAbsBatchMng" label="查看" />
        <emp:button id="cancelIqpAbsBatchMng" label="注销" />
	</div>

	<emp:table icollName="IqpAbsBatchMngList" pageMode="true" url="pageIqpAbsBatchMngQuery.do">
		<emp:text id="batch_no" label="批次号" />
		<emp:text id="batch_name" label="证券化批次名称" />
		<emp:text id="trust_org_no" label="受托机构名称" />
		<emp:text id="trust_date" label="信托成立日" />
		<emp:text id="input_id" label="操作人员" />
		<emp:text id="acct_status" label="账务状态" dictname="STD_ABS_ACCOUNT_STATUS"/>
		
		<emp:text id="mark" label="备注" hidden="true"/>
		<emp:text id="cash_date" label="最后兑付日期" hidden="true"/>
		<emp:text id="input_br_id" label="操作机构" hidden="true"/>
		<emp:text id="update_date" label="修改日期" hidden="true"/>
		<emp:text id="manager_br_id" label="所属行社" hidden="true"/>
		<emp:text id="fund_acct_no" label="信托财产资金账号" hidden="true"/>
		<emp:text id="fund_acct_name" label="信托财产资金户名" hidden="true"/>
		<emp:text id="keep_org_no" label="资金保管机构行号" hidden="true"/>
		<emp:text id="keep_org_name" label="资金保管机构行名" hidden="true"/>
		<emp:text id="is_this_org_service" label="是否本机构服务" hidden="true"/>
		<emp:text id="service_org_no" label="贷款服务机构行号" hidden="true"/>
		<emp:text id="service_org_name" label="贷款服务机构行名" hidden="true"/>
		<emp:text id="service_fee_rate" label="服务费率" hidden="true"/>
		<emp:text id="service_fee" label="总服务费" hidden="true"/>
		<emp:text id="trust_org_cert_type" label="受托机构证件类型" hidden="true"/>
		<emp:text id="trust_org_cert_no" label="受托机构证件号码" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    