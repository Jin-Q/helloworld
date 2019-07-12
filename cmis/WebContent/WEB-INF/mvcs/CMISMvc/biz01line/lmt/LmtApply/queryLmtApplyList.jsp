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
		LmtApply._toForm(form);
		LmtApplyList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateLmtApplyPage() {
		var paramStr = LmtApplyList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var approve_status = LmtApplyList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status == "000" || approve_status == "992" || approve_status == "993"){
				var url = '<emp:url action="getLmtApplyUpdatePage.do"/>?'+paramStr+"&op=update&isShow=N";
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else{
				alert("非【待发起】、【打回】、【追回】状态的记录不能进行修改操作！");
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtApply() {
		var paramStr = LmtApplyList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtApplyViewPage.do"/>?'+paramStr+"&op=view&isShow=N";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	//异步删除
	function doDeleteLmtApply(){
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
					alert("删除成功！");
					window.location.reload();
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

		var paramStr = LmtApplyList._obj.getParamStr(['serno','app_type']);
		if (paramStr != null) {
			var approve_status = LmtApplyList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status == "000"){
				if(confirm("该操作会关联删除关联的授信分项明细，是否确认要删除？")){
					var url = '<emp:url action="deleteLmtApplyRecord.do"/>?'+paramStr;
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
		page.dataGroups.LmtApplyGroup.reset();
	};
	
	/*--user code begin--*/
	//跳转到新增引导页
	function doGetAddLmtApplyPage() {
		var url = '<emp:url action="getLmtLeadPage.do"/>?op=add';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
    //单一法人企业客户主动授信改造   modefied by zhaoxp 2015-02-08 start
	//从lmt_ent_self_cus_info 表中查找是否有对应的数据
	function doQueryLmtEntCusInfo() {
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
				if("failed" == flag){
					initWFSubmit(false);
					
				}else {
					initWFSubmitSub(false);
					//initWFSubmit(false);
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
		var cus_id = LmtApplyList._obj.getSelectedData()[0].cus_id._getValue();
		var paramStr = LmtApplyList._obj.getParamStr(['cus_id']);
		var url = '<emp:url action="QueryLmtEntSelfCusInfoRecord.do"/>?'+paramStr;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		};
		
	//单一法人企业客户主动授信改造   modefied by zhaoxp 2015-02-08 end
	//提交流程
	function doSubmitLmtApply(){
		var paramStr = LmtApplyList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			var cus_id = LmtApplyList._obj.getSelectedData()[0].cus_id._getValue();
			var cus_id_displayname = LmtApplyList._obj.getSelectedData()[0].cus_id_displayname._getValue();
			var crd_totl_amt = LmtApplyList._obj.getSelectedData()[0].crd_totl_amt._getValue();
			var approve_status = LmtApplyList._obj.getParamValue(['approve_status']);
			
			WfiJoin.table_name._setValue("LmtApply");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(approve_status);	
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("003");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：LMT_APP_FLOW
			WfiJoin.cus_id._setValue(cus_id);//客户码+
			WfiJoin.cus_name._setValue(cus_id_displayname);//客户名称
			WfiJoin.amt._setValue(crd_totl_amt);//金额
			WfiJoin.prd_name._setValue("单一法人授信申请");//产品名称
    //单一法人企业客户主动授信改造   modefied by zhaoxp 2015-02-08 start
			doQueryLmtEntCusInfo();
	//单一法人企业客户主动授信改造   modefied by zhaoxp 2015-02-08 end
		}else {
			alert('请先选择一条记录！');
		}
	}

	function returnCus(data){
		LmtApply.cus_id._setValue(data.cus_id._getValue());
		LmtApply.cus_id_displayname._setValue(data.cus_name._getValue());
    };
	function doImageView(){
		var data = LmtApplyList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View23');	//2.3.	客户资料查看（客户全视图）接口
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = LmtApplyList._obj.getParamValue(['cus_id']);	//客户资料的业务编号就填cus_id
		data['cus_id'] = LmtApplyList._obj.getParamValue(['cus_id']);	//客户编号
		data['prd_id'] = 'BASIC';	//业务品种
		data['prd_stage'] = '' ;	//业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*--user code end--*/
	/*****2019-03-01 jiangcuihua 附件上传  start******/
	function doUpload(){
		var paramStr = LmtApplyList._obj.getParamValue(['serno']);
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
		<emp:text id="LmtApply.serno" label="业务编号" />
		<!-- modified by lisj 2014年11月27日--单一法人查询环节， 支持输入客户名称可模糊查询-->
		<emp:text id="LmtApply.cus_name" label="客户名称" />
		<emp:select id="LmtApply.approve_status" label="申请状态" dictname="WF_APP_STATUS" />
		<emp:text id="LmtApply.cus_id" label="客户码" hidden="true"/>
		<emp:pop id="LmtApply.cus_id_displayname" label="客户名称" url="queryAllCusPop.do?cusTypCondition=cus_status='20' and BELG_LINE IN('BL100','BL200')&returnMethod=returnCus" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddLmtApplyPage" label="新增" op="add"/>
		<emp:button id="getUpdateLmtApplyPage" label="修改" op="update"/>
		<emp:button id="deleteLmtApply" label="删除" op="remove"/>
		<emp:button id="viewLmtApply" label="查看" op="view"/>
		<emp:button id="submitLmtApply" label="提交" op="update"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="upload" label="附件"/>
	</div>

	<emp:table icollName="LmtApplyList" pageMode="true" url="pageLmtApplyQuery.do?type=app" statisticType="" >
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="lrisk_type" label="低风险业务类型" dictname="STD_ZB_LRISK_TYPE" />
		<emp:text id="app_type" label="申请类型" dictname="STD_ZB_APP_TYPE" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" hidden="true"/>
		<emp:text id="crd_totl_amt" label="授信总额" dataType="Currency"/>
		<emp:text id="crd_cir_amt" label="循环授信敞口" dataType="Currency"/>
		<emp:text id="crd_one_amt" label="一次性授信敞口" dataType="Currency"/>
		<emp:text id="app_date" label="申请日期" cssTDClass="tdCenter" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="input_id_displayname" label="登记人" cssTDClass="tdRight" hidden="true"/>
		<emp:text id="input_br_id_displayname" label="登记机构" hidden="true"/>
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" cssTDClass="tdCenter"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    