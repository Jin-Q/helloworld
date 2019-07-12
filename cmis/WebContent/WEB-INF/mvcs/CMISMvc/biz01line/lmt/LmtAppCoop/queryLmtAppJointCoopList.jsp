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
		LmtAppJointCoop._toForm(form);
		LmtAppJointCoopList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateLmtAppJointCoopPage() {
		var paramStr = LmtAppJointCoopList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var approve_status = LmtAppJointCoopList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status == "000" || approve_status == "992" || approve_status == "993"){
				var url = '<emp:url action="getLmtAppJointCoopUpdatePage.do"/>?'+paramStr+"&op=update";
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else{
				alert("非【待发起】、【打回】、【追回】状态的记录不能进行修改操作！");
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtAppJointCoop() {
		var paramStr = LmtAppJointCoopList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAppJointCoopViewPage.do"/>?'+paramStr+"&op=view&type=${context.type}&menuId=${context.menuId}";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddLmtAppJointCoopPage() {
		var url = '<emp:url action="getLmtJointCoopLeadPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

	function doReset(){
		page.dataGroups.LmtApplyGroup.reset();
	};
	
	/*--user code begin--*/
	//异步删除合作方授信申请 
	function doDeleteLmtAppJointCoop() {
		var paramStr = LmtAppJointCoopList._obj.getParamStr(['serno','coop_type']);
		if (paramStr != null) {
			var approve_status = LmtAppJointCoopList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status != "000"){
				alert("只有状态为'待发起'的申请才可以进行删除！");
				return ;
			}
			if(confirm("该操作会关联删除合作方信息，是否确认要删除？")){
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
				var url = '<emp:url action="deleteLmtAppJointCoopRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};

	//提交流程
	//var url = '<emp:url action="submitLmtCoopApply.do"/>?'+paramStr;
	function doStartLmtAppJointCoop(){
		var paramStr = LmtAppJointCoopList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			var cus_id = LmtAppJointCoopList._obj.getSelectedData()[0].cus_id._getValue();
			var cus_id_displayname = LmtAppJointCoopList._obj.getSelectedData()[0].cus_id_displayname._getValue();
			var lmt_totl_amt = LmtAppJointCoopList._obj.getSelectedData()[0].lmt_totl_amt._getValue();
			var approve_status = LmtAppJointCoopList._obj.getParamValue(['approve_status']);
			WfiJoin.table_name._setValue("LmtAppJointCoop");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(approve_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("3231");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：LMT_APP_FLOW
			WfiJoin.cus_id._setValue(cus_id);//客户码
			WfiJoin.cus_name._setValue(cus_id_displayname);//客户名称
			WfiJoin.amt._setValue(lmt_totl_amt);//金额
			WfiJoin.prd_name._setValue("合作方额度申请");//产品名称
			initWFSubmit(false);
		}else {
			alert('请先选择一条记录！');
		}
	}

	function doImageView(){
		var data = LmtAppJointCoopList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View23');	//2.3.	客户资料查看（客户全视图）接口
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = LmtAppJointCoopList._obj.getParamValue(['cus_id']);	//客户资料的业务编号就填cus_id
		data['cus_id'] = LmtAppJointCoopList._obj.getParamValue(['cus_id']);	//客户编号
		data['prd_id'] = 'BASIC';	//业务品种
		data['prd_stage'] = '' ;	//业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};

	/**add by lisj 2015-7-30 需求编号：XD150629047 授信审批流程生成授信审批表 begin**/
	function doPrintAP(){
		var paramStr = LmtAppJointCoopList._obj.getParamStr(['serno']);
		if(paramStr != null) {
			var approve_status  = LmtAppJointCoopList._obj.getParamValue(['approve_status']);
			if(approve_status == "997"){
				var url = '<emp:url action="getReportShowPage.do"/>&reportId=lmt/rcapprovalopinion.raq&'+paramStr;
				url = EMPTools.encodeURI(url);
				window.open(url,'newwindow','height='+window.screen.availHeight*0.7+',width='+window.screen.availWidth*0.6+',top=50,left=80,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
			}else{
				alert('仅有审批状态为【通过】的授信审批才允许打印！');
			}
		}else{
			alert('请先选择一条记录！');
		}
	}
	/**add by lisj 2015-7-30 需求编号：XD150629047 授信审批流程生成授信审批表 end**/
	
	/*****2019-03-01 jiangcuihua 附件上传  start******/
	function doUpload(){
		var paramStr = LmtAppJointCoopList._obj.getParamValue(['serno']);
		if (paramStr!=null) {
			var url = '<emp:url action="getUploadInfoPage.do"/>?file_type=04&serno='+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	}
	/*****2019-03-01 jiangcuihua 附件上传  end******/
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="LmtApplyGroup" title="输入查询条件" maxColumn="3">
		<emp:text id="LmtAppJointCoop.serno" label="业务编号" />
		<emp:text id="LmtAppJointCoop.cus_id" label="合作方客户码" />
		<emp:select id="LmtAppJointCoop.approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="getAddLmtAppJointCoopPage" label="新增" op="add"/>
		<emp:button id="getUpdateLmtAppJointCoopPage" label="修改" op="update"/>
		<emp:button id="deleteLmtAppJointCoop" label="删除" op="remove"/>
		<emp:button id="viewLmtAppJointCoop" label="查看" op="view"/>
		<emp:button id="startLmtAppJointCoop" label="提交" op="startFlow"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<!-- add by lisj 2015-7-30 需求编号：XD150629047 授信审批流程生成授信审批表 begin -->
		<emp:button id="printAP" label="审批意见打印" op="printAP"/>
		<!-- add by lisj 2015-7-30 需求编号：XD150629047 授信审批流程生成授信审批表 end -->
		<emp:button id="upload" label="附件"/>
	</div>
	<emp:table icollName="LmtAppJointCoopList" url="pageLmtAppJointCoopQuery.do" reqParams="type=${context.type}">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="合作方客户码" />
		<emp:text id="cus_id_displayname" label="合作方客户名称" />
		<emp:text id="coop_type" label="合作方类型" dictname="STD_ZB_COOP_TYPE" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" hidden="true"/>
		<emp:text id="lmt_totl_amt" label="授信总额" dataType="Currency"/>
		<emp:text id="single_max_amt" label="单户限额" dataType="Currency"/>
		<emp:text id="app_date" label="申请日期" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="input_id_displayname" label="登记人" hidden="true"/>
		<emp:text id="input_br_id_displayname" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" hidden="true"/>
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" />
		<emp:text id="flow_type" label="流程类型" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>