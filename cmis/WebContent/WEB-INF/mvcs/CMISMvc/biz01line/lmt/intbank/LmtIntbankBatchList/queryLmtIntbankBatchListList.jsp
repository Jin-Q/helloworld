<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doGetAddLmtIntbankBatchListPage(){
		var url = '<emp:url action="getLmtIntbankBatchListAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteLmtIntbankBatchList(){		
		var paramStr = LmtIntbankBatchListList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = LmtIntbankBatchListList._obj.getSelectedData()[0].status._getValue();
			if(status =='02'){
				alert("该批量包处于引用状态，不能删除！");
			    return ;
			}else if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteLmtIntbankBatchListRecord.do"/>?'+paramStr;
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
						var flagInfo=jsonstr.flagInfo;
						if(flag=="success"){
							alert('删除成功！');
							window.location.reload();								
						}else{
							alert(flag);
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
	
	function doGetUpdateLmtIntbankBatchListPage(){
		var paramStr = LmtIntbankBatchListList._obj.getParamStr(['serno']);	
		if (paramStr != null) {
			var status = LmtIntbankBatchListList._obj.getSelectedData()[0].status._getValue();
			if(status =='02'){
				alert("该批量包处于引用状态，不能修改！");
				}else{
					var url = '<emp:url action="getLmtIntbankBatchListUpdatePage.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);
					window.location = url;
				}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtIntbankBatchList(){
		var paramStr = LmtIntbankBatchListList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtIntbankBatchListUpdatePage.do"/>?'+paramStr+'&flag=view';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	//查询方法
	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtIntbankBatchList._toForm(form);
		LmtIntbankBatchListList._obj.ajaxQuery(null,form);
	};
	//重置功能按钮
	function doReset(){
		page.dataGroups.LmtIntbankBatchListGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>

<body class="page_content">
	<form action="#" id="queryForm">
	</form>

	<emp:gridLayout id="LmtIntbankBatchListGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="LmtIntbankBatchList.batch_cus_no" label="批量客户编号" />
			<emp:select id="LmtIntbankBatchList.cdt_lvl" label="信用等级" dictname="STD_ZB_FINA_GRADE" />
			<emp:select id="LmtIntbankBatchList.status" label="状态" dictname="STD_ZB_INTBANK_STATE" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="getAddLmtIntbankBatchListPage" label="新增" op="add"/>
		<emp:button id="getUpdateLmtIntbankBatchListPage" label="修改" op="update"/>
		<emp:button id="deleteLmtIntbankBatchList" label="删除" op="remove"/>
		<emp:button id="viewLmtIntbankBatchList" label="查看" op="view"/>
	</div>
	<emp:table icollName="LmtIntbankBatchListList" pageMode="true" url="pageLmtIntbankBatchListListQuery.do">
		<emp:text id="batch_cus_no" label="批量客户编号" />
		<emp:text id="batch_cus_type" label="批量客户类型" dictname="STD_ZB_BATCH_CUS_TYPE"/>
		<emp:text id="cdt_lvl" label="信用等级" dictname="STD_ZB_FINA_GRADE" />		
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>		
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="status" label="状态" dictname="STD_ZB_INTBANK_STATE"/>		
	</emp:table>
</body>
</html>
</emp:page>