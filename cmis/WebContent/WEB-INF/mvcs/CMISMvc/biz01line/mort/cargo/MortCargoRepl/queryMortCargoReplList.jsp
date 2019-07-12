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
		MortCargoRepl._toForm(form);
		MortCargoReplList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateMortCargoReplPage() {
		var paramStr = MortCargoReplList._obj.getParamStr(['guaranty_no','cus_id','serno']);
		var status = MortCargoReplList._obj.getParamStr(['status']);
		if(paramStr !=null){
			if(status=="status=01"){
				alert("此笔业务已记账置换不能进行修改操作！");
			}else{
				var url = '<emp:url action="getMortCargoReplAddPage.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		}else{
			alert("请选择一条记录！");
		}
	};
	
	function doViewMortCargoRepl() {
		var paramStr = MortCargoReplList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getMortCargoReplViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddMortCargoReplPage() {
		var paramStr = MortCargoReplList._obj.getParamStr(['guaranty_no','cus_id','serno']);
		var status = MortCargoReplList._obj.getParamStr(['status']);
		if(paramStr !=null){
			if(status=="status=01"){
				alert("此笔业务已完成置换记账操作不能进行再次置换记账！");
			}else{
				var url = '<emp:url action="getMortCargoReplAddPage.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		}else{
			alert("请选择一条记录！");
		}
	};
	
	function doDeleteMortCargoRepl() {
		var paramStr = MortCargoReplList._obj.getParamStr(['serno','guaranty_no']);
		var status = MortCargoReplList._obj.getParamStr(['status']);
		if (paramStr != null) {
			if(status=="status=00"){
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
							if("success" == flag){
								alert("记录已删除！")
								window.location.reload();
							}else{
								alert("记录删除失败！");
							}
						}
					};
					var handleFailure = function(o) {
					};
					var callback = {
						success :handleSuccess,
						failure :handleFailure
					};
					var url = '<emp:url action="deleteMortCargoReplRecord.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);
			 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
			     }
				}else{
                    alert("非登记状态的置换记录，不可以对其进行删除操作！");
				}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.MortCargoReplGroup.reset();
	};

</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="MortCargoReplGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="MortCargoRepl.serno" label="业务编号" />
			<emp:text id="MortCargoRepl.cus_id" label="客户码" />
			<emp:text id="MortCargoRepl.guaranty_no" label="押品编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddMortCargoReplPage" label="置换记账" op="add"/>
		<emp:button id="getUpdateMortCargoReplPage" label="修改" op="update"/>
		<emp:button id="deleteMortCargoRepl" label="删除" op="remove"/>
		<emp:button id="viewMortCargoRepl" label="查看" op="view"/>
	</div>

	<emp:table icollName="MortCargoReplList" pageMode="true" url="pageMortCargoReplQuery.do">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="serno" label="业务编号" />
		<emp:text id="guaranty_no" label="押品编号" />
		<emp:text id="oversee_agr_no" label="监管协议编号" />
		<emp:text id="storage_total" label="库存总价值" dataType="Currency"/>
		<emp:text id="status" label="状态" dictname="STD_ZB_TALLY_STATUS" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_date" label="登记日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    