<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtAppIndiv._toForm(form);
		LmtAppIndivList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateLmtAppIndivPage() {
		var paramStr = LmtAppIndivList._obj.getParamStr(['serno']);
		var cus_id = LmtAppIndivList._obj.getParamValue('cus_id');
		if (paramStr != null) {
			var approve_status = LmtAppIndivList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status == "000" || approve_status == "992" || approve_status == "993"){
				var url = '<emp:url action="getLmtAppIndivUpdatePage.do"/>?'+paramStr+'&op=update&cus_id='+cus_id;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else{
				alert("非【待发起】、【打回】、【追回】状态的记录不能进行修改操作！");
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtAppIndiv() {
		var paramStr = LmtAppIndivList._obj.getParamStr(['serno']);
		var cus_id = LmtAppIndivList._obj.getParamValue('cus_id');
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAppIndivViewPage.do"/>?'+paramStr+'&op=view&cus_id='+cus_id+'&type=cusTree';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddLmtAppIndivPage() {
	//	var url = '<emp:url action="getLmtAppIndivAddPage.do"/>';
		var url = '<emp:url action="getLmtIndivLeadPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	//异步删除
	function doDeleteLmtAppIndiv(){
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("success" == flag){
					window.location.reload();
				}
			}
		};
		var handleFailure = function(o) {
			alert(o.responseText);
			alert("保存失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};

		var paramStr = LmtAppIndivList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var approve_status = LmtAppIndivList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status == "000"){
				if(confirm("该操作会关联删除新增的授信分项明细，是否确认要删除？")){
					var url = '<emp:url action="deleteLmtAppIndivRecord.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
				}
			}else{
				alert("只有状态为【待发起】的申请才可以进行删除！");
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.LmtAppIndivGroup.reset();
	};

	/*--user code begin--*/
	//提交流程
	function doSubLmtAppIndiv(){
		var paramStr = LmtAppIndivList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			var cus_id = LmtAppIndivList._obj.getSelectedData()[0].cus_id._getValue();
			var cus_id_displayname = LmtAppIndivList._obj.getSelectedData()[0].cus_id_displayname._getValue();
			var crd_totl_amt = LmtAppIndivList._obj.getSelectedData()[0].crd_totl_amt._getValue();
			var approve_status = LmtAppIndivList._obj.getParamValue(['approve_status']);
			
			WfiJoin.table_name._setValue("LmtAppIndiv");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(approve_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("3281");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：LMT_APP_FLOW
			WfiJoin.cus_id._setValue(cus_id);//客户码
			WfiJoin.cus_name._setValue(cus_id_displayname);//客户名称
			WfiJoin.amt._setValue(crd_totl_amt);//金额
			WfiJoin.prd_name._setValue("个人额度申请");//产品名称
			initWFSubmit(false);
		}else {
			alert('请先选择一条记录！');
		}
	}

	function returnCus(data){
		LmtAppIndiv.cus_id._setValue(data.cus_id._getValue());
		LmtAppIndiv.cus_id_displayname._setValue(data.cus_name._getValue());
    };

	function doImageView(){
		var data = LmtAppIndivList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View23');	//2.3.	客户资料查看（客户全视图）接口
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = LmtAppIndivList._obj.getParamValue(['cus_id']);	//客户资料的业务编号就填cus_id
		data['cus_id'] = LmtAppIndivList._obj.getParamValue(['cus_id']);	//客户编号
		data['prd_id'] = 'BASIC';	//业务品种
		data['prd_stage'] = '' ;	//业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="LmtAppIndivGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="LmtAppIndiv.serno" label="业务编号" />
		<emp:pop id="LmtAppIndiv.cus_id_displayname" label="客户名称" url="queryAllCusPop.do?cusTypCondition=cus_status='20' and BELG_LINE='BL300'&returnMethod=returnCus" />
		<emp:select id="LmtAppIndiv.lrisk_type" label="低风险业务类型" dictname="STD_ZB_LRISK_TYPE"/>
		<emp:select id="LmtAppIndiv.approve_status" label="申请状态" dictname="WF_APP_STATUS" />
		<emp:text id="LmtAppIndiv.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewLmtAppIndiv" label="查看" op="view"/>
	</div>

	<emp:table icollName="LmtAppIndivList" pageMode="true" url="pageLmtAppIndivForCusTreeQuery.do?cus_id=${context.cus_id}">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="lrisk_type" label="低风险业务类型" dictname="STD_ZB_LRISK_TYPE" />
		<emp:text id="crd_totl_amt" label="授信总额" dataType="Currency"/>
		<emp:text id="app_type" label="申请类型" dictname="STD_ZB_APP_TYPE" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="input_id_displayname" label="登记人" hidden="true"/>
		<emp:text id="input_br_id_displayname" label="登记机构" hidden="true"/>
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    