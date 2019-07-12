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
		LmtAgrIndiv._toForm(form);
		LmtAgrIndivList._obj.ajaxQuery(null,form);
	};

	//查看详情
	function doViewLmtAgrIndiv() {
		var paramStr = LmtAgrIndivList._obj.getParamStr(['agr_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAgrIndivViewPage.do"/>?'+paramStr+"&op=view&type=cusTree&cus_id=${context.cus_id}";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.LmtAgrIndivGroup.reset();
	};
	
	/*--user code begin--*/
	//发起冻结申请
	function doFrozLmtAgrIndiv(){
		var paramStr = LmtAgrIndivList._obj.getParamStr(['agr_no']);
		if(paramStr == null){
			alert('请先选择一条记录！');
			return;
		}
		if(confirm("是否确认要进行冻结？")){
			var url = '<emp:url action="checkLmtAgrIndivFrozenApp.do"/>?'+paramStr+"&app_type=03";
			url = EMPTools.encodeURI(url);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("操作失败!");
						return;
					}
					var serno=jsonstr.serno;
					if(serno!=""){
						if(serno=="other"){
							alert("该笔协议存在在途的变更或解冻申请，不可以冻结！");
							return;
						}else{
							var appStatus = jsonstr.appStatus;
							var updflag;
							if(appStatus=='000'){//待发起状态
								updflag = 'update';
							}else{
								updflag = 'query';
							}
							var url = '<emp:url action="getLmtIndivFrozenUpdOrViePage.do"/>?serno='+serno+"&app_type=03&menuId=lmtFrozeIndiv&updflag="+updflag;
							url = EMPTools.encodeURI(url);
							window.location = url;
						}
					}else{
						var url = '<emp:url action="getLmtIndivFrozenUpdateRecord.do"/>?'+paramStr+"&app_type=03&menuId=lmtFrozeIndiv&updflag=update";
						url = EMPTools.encodeURI(url);
						window.location = url;
					}
				}	
			};
			var handleFailure = function(o){ 
				alert("操作失败，请联系管理员");
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			}; 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		}
	}

	//发起解冻申请
	function doUnfrozLmtAgrIndiv(){
		var paramStr = LmtAgrIndivList._obj.getParamStr(['agr_no']);
		if(paramStr == null){
			alert('请先选择一条记录！');
			return;
		}
		if(confirm("是否确认要进行解冻？")){
			var url = '<emp:url action="checkLmtAgrIndivFrozenApp.do"/>?'+paramStr+"&app_type=04";
			url = EMPTools.encodeURI(url);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("操作失败!");
						return;
					}
					var serno=jsonstr.serno;
					var appStatus = jsonstr.appStatus;
					if(serno!=""){
						if(serno=="other"){
							alert("该笔协议存在在途的变更或冻结申请，不可以解冻！");
							return;
						}else{
							var updflag;
							if(appStatus=='000'){//待发起状态
								updflag = 'update';
							}else{
								updflag = 'query';
							}
							var url = '<emp:url action="getLmtIndivFrozenUpdOrViePage.do"/>?serno='+serno+"&app_type=04&menuId=lmtFrozeIndiv&updflag="+updflag;
							url = EMPTools.encodeURI(url);
							window.location = url;
						}
					}else{
						if(appStatus=="nofrozen"){
							alert('冻结金额为0，不能进行解冻操作！');
						}else{
							var url = '<emp:url action="getLmtIndivFrozenUpdateRecord.do"/>?'+paramStr+"&app_type=04&menuId=lmtFrozeIndiv&updflag=update";
							url = EMPTools.encodeURI(url);
							window.location = url;
						}
					}
				}
			};
			var handleFailure = function(o){ 
				alert("操作失败，请联系管理员");
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			}; 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		}
	}

	function returnCus(data){
		LmtAgrIndiv.cus_id._setValue(data.cus_id._getValue());
		LmtAgrIndiv.cus_id_displayname._setValue(data.cus_name._getValue());
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
		<emp:gridLayout id="LmtAgrIndivGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="LmtAgrIndiv.agr_no" label="协议编号" />
			<emp:text id="LmtAgrIndiv.serno" label="业务编号" />
			<emp:pop id="LmtAgrIndiv.cus_id_displayname" label="客户名称" url="queryAllCusPop.do?cusTypCondition=cus_status='20' and BELG_LINE='BL300'&returnMethod=returnCus" />
			<emp:text id="LmtAgrIndiv.cus_id" label="客户码" hidden="true"/>
		</emp:gridLayout>
	</form>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewLmtAgrIndiv" label="查看" op="view"/>
	</div>

	<emp:table icollName="LmtAgrIndivList" pageMode="true" url="pageLmtAgrIndivForCusTreeQuery.do?cus_id=${context.cus_id}">
		<emp:text id="agr_no" label="协议编号" />
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="crd_totl_amt" label="授信总额" dataType="Currency"/>
		<emp:text id="self_amt" label="自助金额" dataType="Currency"/>
		<emp:text id="totl_start_date" label="授信起始日" />
		<emp:text id="totl_end_date" label="授信到期日" />	
		<emp:text id="manager_id_displayname" label="责任人" />	
		<emp:text id="manager_br_id_displayname" label="责任机构" />	
	</emp:table>
	
</body>
</html>
</emp:page>
    