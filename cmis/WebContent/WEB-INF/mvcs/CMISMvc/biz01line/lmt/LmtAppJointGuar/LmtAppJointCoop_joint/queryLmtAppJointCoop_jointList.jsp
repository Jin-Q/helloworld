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
		LmtAppJointCoop_jointList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.LmtAppJointCoopGroup.reset();
	};
	
	/*--user code begin--*/
	function doUpdate() {
		var paramStr = LmtAppJointCoop_jointList._obj.getParamStr(['serno','cus_id']);
		if (paramStr != null) {
			var _status = LmtAppJointCoop_jointList._obj.getParamValue(['approve_status']);
	        if(_status!=''&&_status!= '000' &&_status!= '991'&&_status!= '992'&&_status!= '993'){
				alert('该申请所处状态不是【待发起】、【追回】、【打回】不能修改');
				return;
			}
			var url = '<emp:url action="getLmtAppJointCoop_jointUpdatePage.do"/>?'+paramStr +'&op=update';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doView() {
		var paramStr = LmtAppJointCoop_jointList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAppJointCoop_jointViewPage.do"/>?'+paramStr+"&op=view";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doAdd(){
		var url = '<emp:url action="getLmtJointGuarWizPage.do"/>&op=add';
		url = EMPTools.encodeURI(url);
		window.location = url;
	}
	
	function doDelete() {
		var paramStr = LmtAppJointCoop_jointList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var _status = LmtAppJointCoop_jointList._obj.getParamValue(['approve_status']);
	        if(_status!=''&&_status!= '000'){
				alert('该申请所处状态不是【待发起】不能删除');
				return;
			}
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteLmtAppJointCoop_jointRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var handSuc = function(o){
					if(o.responseText !== undefined) {
						try { var jsonstr = eval("("+o.responseText+")"); } 
						catch(e) {
						alert("数据库操作失败!");
						return;
						}
						var flag = jsonstr.flag;
						if(flag == "success"){
							alert("删除成功!");
							window.location.reload();
						}
					}
				};
			    var handFail = function(o){
			    };
			    var callback = {
			    	success:handSuc,
			    	failure:handFail
			    };
				var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);				
			}
		} else {
			alert('请先选择一条记录！');
		}
	};

	//提交流程
	function doSubm(){
		var paramStr = LmtAppJointCoop_jointList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			var _status = LmtAppJointCoop_jointList._obj.getParamValue(['approve_status']);
	/*    if(_status!=''&&_status!= '000' &&_status!= '991'&&_status!= '992'&&_status!= '993'){
				alert('该申请所处状态不是【待发起】、【追回】、【打回】不能发起流程申请');
				return;
			}*/
			var cus_id = LmtAppJointCoop_jointList._obj.getParamValue(['cus_id']);//客户码
			var cus_name = LmtAppJointCoop_jointList._obj.getParamValue(['cus_id_displayname']);//客户名称
			var amt = LmtAppJointCoop_jointList._obj.getParamValue(['lmt_totl_amt']);//授信总额
			WfiJoin.table_name._setValue("LmtAppJointCoop");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("3241");  //流程申请类型，对应字典项[ZB_BIZ_CATE]
			WfiJoin.cus_id._setValue(cus_id);
			WfiJoin.cus_name._setValue(cus_name);
			WfiJoin.amt._setValue(amt);
			WfiJoin.prd_name._setValue("联保小组授信申请");
			initWFSubmit(false);
		}else {
			alert('请先选择一条记录！');
		}
	};
	//复议
	function doSubmitRedi(){
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
					var url = '<emp:url action="getLmtAppCoopRediPage.do"/>?'+paramStr+"&menuId=unit_team_crd_query&op=view&isShow=N&showButton=Y";
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else {
					alert(jsonstr.msg);
				}
			}
		};
		var handleFailure = function(o) {
			alert(o.responseText);
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		
		var paramStr = LmtAppJointCoop_jointList._obj.getParamStr(['serno']);
		var approve_status = LmtAppJointCoop_jointList._obj.getParamValue(['approve_status']);
		if (paramStr != null) {
			if("998"==approve_status){
				var url = '<emp:url action="searchRediApply.do"/>?'+paramStr+"&type=coop";
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}else{
				alert('只有审批状态为【否决】的业务才能发起复议！');
			}
		}else {
			alert('请先选择一条记录！');
		}
	}
	//2015-07-16 Edited by FCL
	function doExcelExport(){
		var url = '<emp:url action="lmtAppJointExpBatchToExcel.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	}
	function doImageView(){
		var data = LmtAppJointCoop_jointList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View23');	//2.3.	客户资料查看（客户全视图）接口
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = LmtAppJointCoop_jointList._obj.getParamValue(['cus_id']);	//客户资料的业务编号就填cus_id
		data['cus_id'] = LmtAppJointCoop_jointList._obj.getParamValue(['cus_id']);	//客户编号
		data['prd_id'] = 'BASIC';	//业务品种
		data['prd_stage'] = '' ;	//业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/**add by lisj 2015-7-30 需求编号：XD150629047 授信审批流程生成授信审批表 begin**/
	function doPrintAP(){
		var paramStr = LmtAppJointCoop_jointList._obj.getParamStr(['serno']);
		if(paramStr != null) {
			var approve_status  = LmtAppJointCoop_jointList._obj.getParamValue(['approve_status']);
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
		var paramStr = LmtAppJointCoop_jointList._obj.getParamValue(['serno']);
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

	<emp:gridLayout id="LmtAppJointCoopGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="LmtAppJointCoop.serno" label="业务编号" />
			<emp:text id="LmtAppJointCoop.cus_id" label="组长客户码" />
			<!--/**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 begin */-->
			<emp:text id="LmtAppJointCoop.cus_name" label="组长客户名称" />
			<emp:text id="LmtAppJointCoop.cus_member_name" label="联保成员名称" />
			<!--/**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 end */-->
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="add" label="新增" op="add"/>
		<emp:button id="update" label="修改" op="update"/>
		<emp:button id="delete" label="删除" op="remove"/>
		<emp:button id="view" label="查看" op="view"/>
		<emp:button id="submitRedi" label="复议" op="redi"/>
		<emp:button id="subm" label="提交" op="subm"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="excelExport" label="导出" op="putout"/>
		<!-- add by lisj 2015-7-30 需求编号：XD150629047 授信审批流程生成授信审批表 begin -->
		<emp:button id="printAP" label="审批意见打印" op="printAP"/>
		<!-- add by lisj 2015-7-30 需求编号：XD150629047 授信审批流程生成授信审批表 end -->
		<emp:button id="upload" label="附件"/>
	</div>

	<emp:table icollName="LmtAppJointCoop_jointList" pageMode="true" url="pageLmtAppJointCoop_jointQuery.do" reqParams="process=${context.process}">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="组长客户码" />
		<emp:text id="cus_id_displayname" label="组长客户名称" />
		<emp:text id="coop_type" label="类别" dictname="STD_ZB_COOP_TYPE" />
		<emp:text id="lmt_totl_amt" label="授信总额" dataType="Currency"/>
		<emp:text id="single_max_amt" label="单户限额" dataType="Currency"/>
		<emp:text id="app_date" label="申请日期" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" hidden="true"/>
		<emp:text id="input_br_id_displayname" label="登记机构" hidden="true"/>
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    