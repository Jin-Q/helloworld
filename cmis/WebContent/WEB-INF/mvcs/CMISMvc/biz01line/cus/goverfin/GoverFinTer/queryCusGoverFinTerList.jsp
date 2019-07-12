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
		CusGoverFinTer._toForm(form);
		CusGoverFinTerList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusGoverFinTerPage() {
		//modified by yangzy 2015/04/17 需求：XD150325024，集中作业扫描岗权限改造 start
		var paramStr = CusGoverFinTerList._obj.getParamStr(['serno']);
		var currentUserId = '${context.currentUserId}';
		if (paramStr != null) {
			var manager_id = CusGoverFinTerList._obj.getParamValue('manager_id');
			if(manager_id==null || manager_id=='' || currentUserId == manager_id){
				var url = '<emp:url action="getCusGoverFinTerUpdatePage.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else{
				alert('非当前业务主管客户经理，操作失败！');
			}
		} else {
			alert('请先选择一条记录！');
		}
		//modified by yangzy 2015/04/17 需求：XD150325024，集中作业扫描岗权限改造 end
	};
	
	function doViewCusGoverFinTer() {
		var paramStr = CusGoverFinTerList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusGoverFinTerViewPage.do"/>?ops=view&op=view&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusGoverFinTerPage() {
		var url = '<emp:url action="getCusGoverFinTerAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusGoverFinTer() {
		//modified by yangzy 2015/04/17 需求：XD150325024，集中作业扫描岗权限改造 start
		var paramStr = CusGoverFinTerList._obj.getParamStr(['serno','cus_id']);
		var currentUserId = '${context.currentUserId}';
		if (paramStr != null) {
			var manager_id = CusGoverFinTerList._obj.getParamValue('manager_id');
			if(1==1){
				if(confirm("是否确认要删除？")){
					var url = '<emp:url action="deleteCusGoverFinTerRecord.do"/>?'+paramStr;
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
			}else{
				alert('非当前业务主管客户经理，删除失败！');
			}
		} else {
			alert('请先选择一条记录！');
		}
		//modified by yangzy 2015/04/17 需求：XD150325024，集中作业扫描岗权限改造 end
	};
	
	function doReset(){
		page.dataGroups.CusGoverFinTerGroup.reset();
	};
	
	/*--user code begin--*/
	function returnCus(data){
		CusGoverFinTer.cus_id._setValue(data.cus_id._getValue());
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="CusGoverFinTerGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CusGoverFinTer.serno" label="申请流水号" />
			<emp:pop id="CusGoverFinTer.cus_id" label="客户码"  
			buttonLabel="选择" url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" />				
			<emp:select id="CusGoverFinTer.gover_fin_loan_type" label="政府融资贷款类型" dictname="STD_ZB_GOVER_FIN_TYPE" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddCusGoverFinTerPage" label="新增" op="add"/>
		<emp:button id="getUpdateCusGoverFinTerPage" label="修改" op="update"/>
		<emp:button id="deleteCusGoverFinTer" label="删除" op="remove"/>
		<emp:button id="viewCusGoverFinTer" label="查看" op="view"/>
	</div>

	<emp:table icollName="CusGoverFinTerList" pageMode="true" url="pageCusGoverFinTerQuery.do">
		<emp:text id="serno" label="申请流水号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="gover_fin_loan_type" label="政府融资贷款类型" dictname="STD_ZB_GOVER_FIN_TYPE" />
		<emp:text id="cash_cover_rate" label="现金流覆盖率" dataType="Rate" />
		<emp:text id="input_id_displayname" label="登记人" cssTDClass="tdRight" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_date" label="登记日期" />
		<!-- added by yangzy 2015/04/17 需求：XD150325024，集中作业扫描岗权限改造 start -->
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="manager_id" label="客户经理" hidden="true"/>
		<!-- added by yangzy 2015/04/17 需求：XD150325024，集中作业扫描岗权限改造 end -->
	</emp:table>
	
</body>
</html>
</emp:page>
    