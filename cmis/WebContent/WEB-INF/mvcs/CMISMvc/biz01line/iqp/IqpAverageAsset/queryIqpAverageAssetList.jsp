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
		IqpAverageAsset._toForm(form);
		IqpAverageAssetList._obj.ajaxQuery(null,form);
	};
	
	function doViewIqpAverageAsset() {
		var paramStr = IqpAverageAssetList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAverageAssetViewPage.do"/>?'+paramStr+'&op=view';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doRemove(){
		var paramStr = IqpAverageAssetList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start
    		var currentUserId = '${context.currentUserId}';
    		var manager_id = IqpAverageAssetList._obj.getParamValue('input_id');
    		if(manager_id!=null && manager_id!='' && currentUserId != manager_id){
    			alert('非当前业务登记人，操作失败！');
    			return;
    		}
    		//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 end
			var approve_status = IqpAverageAssetList._obj.getSelectedData()[0].average_status._getValue();
			if(approve_status == "1"){ 
				if(confirm("是否确认要作废？")){
					var handleSuccess = function(o){
						if(o.responseText !== undefined) {
							try {
								var jsonstr = eval("("+o.responseText+")");
							} catch(e) {
								alert("Parse jsonstr1 define error!" + e.message);
								return;
							}
							var flag = jsonstr.flag;
							var msg = jsonstr.msg;
							if(flag == "success"){
								alert("作废成功!");
								window.location.reload();
							}else {
								alert(msg);
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
					var url = '<emp:url action="deleteIqpAverageAssetRecord.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);
				    var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
				}
			}else{
				alert("只有状态为【正常】状态的卖出资产才可以进行作废！");
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpAverageAssetGroup.reset();
	};
	function returnCus(data){
	   IqpAverageAsset.cus_id._setValue(data.cus_id._getValue());
	   IqpAverageAsset.cus_name._setValue(data.cus_name._getValue());
    };
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpAverageAssetGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpAverageAsset.bill_no" label="借据编号" />
			<emp:pop id="IqpAverageAsset.cus_name" label="客户名称" buttonLabel="选择" url="queryAllCusPop.do?returnMethod=returnCus" />
			<emp:text id="IqpAverageAsset.cont_no" label="合同编号" />
			<emp:select id="IqpAverageAsset.average_status" label="资产状态" dictname="STD_ZB_AVERGER_STATUS"/>
			<emp:text id="IqpAverageAsset.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewIqpAverageAsset" label="查看" op="view"/>
		<emp:button id="remove" label="作废" op="cancel"/>
	</div>

	<emp:table icollName="IqpAverageAssetList" pageMode="true" url="pageIqpAverageAssetQuery.do">
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="average_status" label="资产状态 " dictname="STD_ZB_AVERGER_STATUS" />
		<emp:text id="asset_no" label="资产包编号" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="serno" label="业务流水号" hidden="true"/>
		<!-- modified by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start -->
		<emp:text id="input_id" label="责任人" hidden="true"/>
		<!-- modified by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 end -->
	</emp:table>
	
</body>
</html>
</emp:page>
    