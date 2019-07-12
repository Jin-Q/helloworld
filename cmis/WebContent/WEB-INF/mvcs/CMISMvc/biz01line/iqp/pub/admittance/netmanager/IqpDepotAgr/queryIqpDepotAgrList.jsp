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
		IqpDepotAgr._toForm(form);
		IqpDepotAgrList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpDepotAgrPage() {
		var paramStr = IqpDepotAgrList._obj.getParamStr(['depot_agr_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpDepotAgrUpdatePage.do"/>?'+paramStr
			                                                           +"&net_agr_no=${context.net_agr_no}"
                                                                       +"&mem_cus_id=${context.mem_cus_id}"
                                                                       +"&cus_id=${context.cus_id}"
															           +"&mem_manuf_type=${context.mem_manuf_type}";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpDepotAgr() {
		var paramStr = IqpDepotAgrList._obj.getParamStr(['depot_agr_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpDepotAgrViewPage.do"/>?'+paramStr
																	+"&net_agr_no=${context.net_agr_no}"
															        +"&mem_cus_id=${context.mem_cus_id}"
															        +"&net_agr_no=${context.net_agr_no}"
														            +"&mem_manuf_type=${context.mem_manuf_type}"
														            +"&flag=haveButton";
			url = EMPTools.encodeURI(url);
			var param = 'height=500, width=1000, top=180, left=150, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpDepotAgrPage() {
		var url = '<emp:url action="getIqpDepotAgrAddPage.do"/>'+"&net_agr_no=${context.net_agr_no}"
		                                                        +"&mem_cus_id=${context.mem_cus_id}"
		                                                        +"&cus_id=${context.cus_id}"
													            +"&mem_manuf_type=${context.mem_manuf_type}";
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	//异步删除操作
	function doDeleteIqpDepotAgr() {
		var paramStr = IqpDepotAgrList._obj.getParamStr(['depot_agr_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o){ 		
                    var jsonstr = eval("(" + o.responseText + ")");
					var flag = jsonstr.flag;
					if(flag == "success" ){
						alert("删除成功！");
						window.location.reload();
					}else{
					}
				}
				var handleFailure = function(o){
				alert("异步回调失败！");	
				};
				var url = '<emp:url action="deleteIqpDepotAgrRecord.do"/>?'+paramStr;
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				};
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST', url,callback);
	      }
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpDepotAgrGroup.reset();
	};
	
	/*--user code begin--*/
	function doViewIqpDesbuyPlan() {
		var paramStr = IqpDepotAgrList._obj.getParamStr(['desgoods_plan_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpDesbuyPlanViewPage.do"/>?'+paramStr
			                                                           +"&mem_cus_id=${context.mem_cus_id}"
			                                                           +"&net_agr_no=${context.net_agr_no}"
			                                                           +"&cus_id=${context.cus_id}"
																	   +"&mem_manuf_type=${context.mem_manuf_type}";
			url = EMPTools.encodeURI(url);
			var param = 'height=500, width=1000, top=180, left=150, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};


	function doViewIqpPsaleCont() {
		var paramStr = IqpDepotAgrList._obj.getParamStr(['psale_cont']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpPsaleContViewPage.do"/>?'+paramStr
																	   +"&mem_cus_id=${context.mem_cus_id}"
															           +"&cus_id=${context.cus_id}"
															           +"&mem_manuf_type=${context.mem_manuf_type}"
															           +"&net_agr_no=${context.net_agr_no}";       
			url = EMPTools.encodeURI(url);
			var param = 'height=500, width=1000, top=180, left=150, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};		
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:actButton id="getAddIqpDepotAgrPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateIqpDepotAgrPage" label="修改" op="update"/>
		<emp:actButton id="deleteIqpDepotAgr" label="删除" op="remove"/>
		<emp:actButton id="viewIqpDepotAgr" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpDepotAgrList" pageMode="true" url="pageIqpDepotAgrQuery.do?net_agr_no=${context.net_agr_no}&mem_manuf_type=${context.mem_manuf_type}&mem_cus_id=${context.mem_cus_id}&cus_id=${context.cus_id}">
		<emp:text id="depot_agr_no" label="保兑仓协议编号" />
		<emp:text id="cus_id" label="借款人客户码" />
		<emp:text id="cus_id_displayname" label="借款人客户名称" />
		<emp:link id="psale_cont" label="年度购销合同编号" operation="viewIqpPsaleCont" />
		<emp:link id="desgoods_plan_no" label="订货计划" operation="viewIqpDesbuyPlan"/>
		<emp:text id="fst_bail_perc" label="首次保证金比例" dataType="Percent"/>		
		<emp:text id="status" label="状态" dictname="STD_ZB_STATUS" />
		
	</emp:table>
	
</body>
</html>
</emp:page>
    