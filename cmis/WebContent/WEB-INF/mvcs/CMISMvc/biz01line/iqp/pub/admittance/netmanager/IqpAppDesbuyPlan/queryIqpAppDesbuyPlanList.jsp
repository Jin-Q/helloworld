<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	var modifyWindow;
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpAppDesbuyPlan._toForm(form);
		IqpAppDesbuyPlanList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpAppDesbuyPlanPage() {
		var paramStr = IqpAppDesbuyPlanList._obj.getParamStr(['desgoods_plan_no']);
		if (paramStr != null) {
			var status = IqpAppDesbuyPlanList._obj.getSelectedData()[0].status._getValue();
			if(status == "0"){
                alert("失效状态不能修改 ！");
                return;
			}
			var url = '<emp:url action="getIqpAppDesbuyPlanUpdatePage.do"/>?'+paramStr+'&mem_cus_id=${context.mem_cus_id}'
			+"&mem_manuf_type=${context.mem_manuf_type}"
			+"&cus_id=${context.cus_id}"
		    +"&serno=${context.serno}";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpAppDesbuyPlan() {
		if(modifyWindow != null){
			modifyWindow.close();
		}
		var paramStr = IqpAppDesbuyPlanList._obj.getParamStr(['desgoods_plan_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpAppDesbuyPlanViewPage.do"/>?'+paramStr+'&mem_cus_id=${context.mem_cus_id}'
			+"&mem_manuf_type=${context.mem_manuf_type}"
			+"&cus_id=${context.cus_id}"
		    +"&serno=${context.serno}"; 
			url = EMPTools.encodeURI(url);
			var param = 'height=500, width=1000, top=180, left=150, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			modifyWindow = window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpAppDesbuyPlanPage() {
		var url = '<emp:url action="getIqpAppDesbuyPlanAddPage.do"/>?mem_cus_id=${context.mem_cus_id}'
			+"&mem_manuf_type=${context.mem_manuf_type}"
			+"&cus_id=${context.cus_id}"
		    +"&serno=${context.serno}";
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpAppDesbuyPlan() {
		var paramStr = IqpAppDesbuyPlanList._obj.getParamStr(['desgoods_plan_no','serno']);
		if (paramStr != null) {
			var cont_status = IqpAppDesbuyPlanList._obj.getSelectedData()[0].cont_status._getValue();
			if(cont_status == "0"){
                alert("原有协议不能删除!");
                return;
			}
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
							window.location.reload();
						}else {
							alert("删除失败!");
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
				var url = '<emp:url action="deleteIqpAppDesbuyPlanRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doRemoveIqpAppDesbuyPlan() {
		var paramStr = IqpAppDesbuyPlanList._obj.getParamStr(['desgoods_plan_no','serno']);
		var msg;
		if (paramStr != null) {
			var cont_status = IqpAppDesbuyPlanList._obj.getSelectedData()[0].cont_status._getValue();
			var status = IqpAppDesbuyPlanList._obj.getSelectedData()[0].status._getValue();
			if(cont_status == "0" && status == "1"){
				msg="是否确认要做失效操作？";
			}else if(cont_status == "0" && status == "0"){
				msg="是否确认失效撤销操作？";
			}else{
                alert("非原有协议不能做失效/撤销操作!");
                return;
			}
			if(confirm(msg)){
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
							if(msg == "success"){
								alert("协议撤销失效操作成功!");
							}else{
								alert("协议失效操作成功!");
							}
							window.location.reload();
						}else {
							alert("操作失败!");
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
				var url = '<emp:url action="removeIqpAppDesbuyPlanRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpAppDesbuyPlanGroup.reset();
	};
	function closeMdyWin(){
		if(modifyWindow != null){
			modifyWindow.close();
		}
	}
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onunload="closeMdyWin();">
	<form  method="POST" action="#" id="queryForm">
	</form>
	
	<div align="left">
		<emp:actButton id="getAddIqpAppDesbuyPlanPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateIqpAppDesbuyPlanPage" label="修改" op="update"/>
		<emp:actButton id="deleteIqpAppDesbuyPlan" label="删除" op="remove"/>
		<emp:actButton id="removeIqpAppDesbuyPlan" label="失效/撤销" op="remove"/>
		<emp:actButton id="viewIqpAppDesbuyPlan" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpAppDesbuyPlanList" pageMode="true" url="pageIqpAppDesbuyPlanQuery.do" reqParams="serno=${context.serno}&mem_cus_id=${context.mem_cus_id}&cus_id=${context.cus_id}&mem_manuf_type=${context.mem_manuf_type}">
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="desgoods_plan_no" label="订货计划流水号" hidden="false"/>
		<emp:text id="for_manuf_displayname" label="供货厂商名称" />
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="commo_name_displayname" label="商品名称" />
		<emp:text id="desbuy_qnt" label="订购数量" />
		<emp:text id="desbuy_qnt_unit" label="单位" dictname="STD_ZB_UNIT" />
		<emp:text id="desbuy_unit_price" label="订购单价（元）" dataType="Currency" />
		<emp:text id="desbuy_total" label="订购总价（元）" dataType="Currency" />
		<emp:text id="fore_disp_date" label="预计发货日期" />
		<emp:text id="status" label="生效状态" dictname="STD_ZB_STATUS"/>
		<emp:text id="cont_status" label="协议状态" dictname="STD_ZB_NET_STATUS"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    