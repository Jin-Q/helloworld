<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpChkMarketTask._toForm(form);
		IqpChkMarketTaskList._obj.ajaxQuery(null,form);
	};

	//价格调整
	function doAdjVlaueMana(){
		var paramStr = IqpChkMarketTaskList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var status = IqpChkMarketTaskList._obj.getParamValue(['status']);
			if(status=='2'){
				alert('任务已处理完成！');
				return;
			}
			var url = '<emp:url action="getIqpChkMarketTaskUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	}
	
	function doReset(){
		page.dataGroups.IqpChkMarketTaskGroup.reset();
	};

	function doViewIqpChkMarketTask() {
		var paramStr = IqpChkMarketTaskList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpChkMarketTaskViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doExportReport(){
		var form = document.getElementById("queryForm");
		IqpChkMarketTask._toForm(form);
		form.submit();
		/*
		var url = '<emp:url action="IqpChkMarketTaskExcel.do"/>&pageInfo.maxLine=' + document.all('emp_pq_maxLine').value + '&pageInfo.targetPage=' + document.all('emp_pq_jumpInput').value;
		window.open(url);
		*/
	}

	function doImportReport(){
		var url = '<emp:url action="queryIqpChkMarketImport.do"/>';
		url = EMPTools.encodeURI(url);
		EMPTools.openWindow(url,'newwindow');
	}

	//异步确认数据
	function doConfirm(){
		var paramStr = IqpChkMarketTaskList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var status = IqpChkMarketTaskList._obj.getParamValue(['status']);
			if(status=='2'){
				alert('任务已处理完成！');
				return;
			}else{
				var change_valve = IqpChkMarketTaskList._obj.getParamValue(['change_valve']);
				var org_valve = IqpChkMarketTaskList._obj.getParamValue(['org_valve']);
				if(change_valve==""||change_valve==null){
					alert("【此次商品核准单价】为空，不能进行【确认】操作！");
					return;
				}else if(org_valve != change_valve){
					alert("【此次商品核准单价】与【原有商品单价】不符，不能进行【确认】操作！");
					return;
				}else{
					//if(confirm("是否确认成功？")){
						var handleSuccess = function(o) {
							EMPTools.unmask();
							if (o.responseText !== undefined) {
								try {
									var jsonstr = eval("(" + o.responseText + ")");
								} catch (e) {
									alert("Parse jsonstr define error!" + e.message);
									return;
								}
								var flag = jsonstr.msg;
								if("Y" == flag){
									//生成完成后直接提交流程
									//alert("确认成功！");
									//window.location.reload();
									var serno = jsonstr.serno;
									doSubmitConfirm(serno,jsonstr.approve_status);
								}else{
									alert("确认失败,失败原因："+jsonstr.msg);
								}
							}
						};
						var handleFailure = function(o) {
							alert("确认失败!");
						};
						var callback = {
							success :handleSuccess,
							failure :handleFailure
						};
						//盯市任务需要提交流程    2014-09-29   唐顺岩
						//var url = '<emp:url action="updateIqpChkMarketTaskRecord.do"/>?isConfirm=1&'+paramStr;
						var adj_pk = IqpChkMarketTaskList._obj.getParamValue(['pk_id']);
						var value_no = IqpChkMarketTaskList._obj.getParamValue(['value_no']);
						var url = '<emp:url action="initIqpChkMarketTaskApp.do"/>&adj_pk='+adj_pk+"&value_no="+value_no;
						
						url = EMPTools.encodeURI(url);
						var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null) 
					//}
				}
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	/*--user code begin--*/
	//提交流程
	function doSubmitConfirm(serno,approve_status){
		var catalog_name = IqpChkMarketTaskList._obj.getSelectedData()[0].catalog_name._getValue();
		var change_valve = IqpChkMarketTaskList._obj.getSelectedData()[0].change_valve._getValue();
		//var approve_status = IqpChkMarketTaskList._obj.getParamValue(['approve_status']);
		
		WfiJoin.table_name._setValue("IqpChkMarketTaskApp");
		WfiJoin.pk_col._setValue("serno");
		WfiJoin.pk_value._setValue(serno);
		WfiJoin.wfi_status._setValue(approve_status);	
		WfiJoin.status_name._setValue("approve_status");
		WfiJoin.appl_type._setValue("515");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：IqpChkMarketTask
		WfiJoin.cus_id._setValue("");//客户码
		WfiJoin.cus_name._setValue(catalog_name);//客户名称
		WfiJoin.amt._setValue(change_valve);//金额
		WfiJoin.prd_name._setValue("盯市任务管理");//产品名称
		initWFSubmit(false);
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:form method="POST" action="IqpChkMarketTaskExcel.do" id="queryForm">
	<emp:gridLayout id="IqpChkMarketTaskGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="IqpChkMarketTask.catalog_name" label="目录名称" />
		<emp:select id="IqpChkMarketTask.attr_type" label="类型属性 " dictname="STD_ZB_ATTR_TYPE" />
		<emp:select id="IqpChkMarketTask.status" label="状态 " dictname="STD_CHKMARKET_TASK_STATUS" />
	</emp:gridLayout>
	</emp:form>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="exportReport" label="导出" op="exportReport"/>
		<emp:button id="importReport" label="导入" op="importReport"/>
		<emp:button id="adjVlaueMana" label="价格调整" op="update"/>
		<emp:button id="confirm" label="确认" op="confirm"/>
		<emp:button id="viewIqpChkMarketTask" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpChkMarketTaskList" pageMode="true" url="pageIqpChkMarketTaskQuery.do">
		<emp:text id="pk_id" label="价格流水物理主键" hidden="true"/>
		<emp:text id="value_no" label="价格编号" hidden="true"/>
		<emp:text id="catalog_no" label="目录编号" hidden="true"/>
		<emp:text id="catalog_name" label="目录名称" hidden="false"/>
		<emp:text id="catalog_lvl" label="押品目录层级" hidden="false"/>
		<emp:text id="attr_type" label="类型属性" hidden="false" dictname="STD_ZB_ATTR_TYPE"/>
		<emp:text id="unit" label="计价单位" dictname="STD_ZB_UNIT"/>
		<emp:text id="org_valve" label="原有商品单价" dataType="Currency"/>
		<emp:text id="change_valve" label="此次商品核准单价" dataType="Currency"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="status" label="状态" dictname="STD_CHKMARKET_TASK_STATUS"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    