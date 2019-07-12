<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtApply._toForm(form);
		LmtApplyList._obj.ajaxQuery(null,form);
	};
	/*--user code begin--*/
	function doViewLmtApply() {
		var paramStr = LmtApplyList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			//控制 isShow=N协议中不显示冻结解冻按钮 overrule 授信否决标志
			var isShow = "${context.isShow}";
			if(""==isShow){
				isShow = "N";
			}
			var url = '<emp:url action="getLmtApplyViewPage.do"/>?'+paramStr+"&op=view&isShow="+isShow+"&cus_id=${context.cus_id}";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.LmtApplyGroup.reset();
	};
	
	//复议
	function doSubmitRediLmtApply(){
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
					var url = '<emp:url action="getRediApplyPage.do"/>?'+paramStr+"&menuId=lmtRediApp&op=view&isShow=N&showButton=Y&cus_id=${context.cus_id}";
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
		
		var paramStr = LmtApplyList._obj.getParamStr(['serno']);
		var approve_status = LmtApplyList._obj.getParamValue(['approve_status']);
		if (paramStr != null) {
			if("998"==approve_status){
				var url = '<emp:url action="searchRediApply.do"/>?'+paramStr+"&type=com&menuId=lmtRediApp";
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
				//window.location = url;
			}else{
				alert('只有审批状态为【否决】的业务才能发起复议！');
			}
		}else {
			alert('请先选择一条记录！');
		}
	}

	function onLoadMethod(){
		var overrule = '${context.overrule}';
		if("Y"==overrule){   //否决历史标志为是时隐藏复议按钮
			document.all.button_submitLmtApply.style.display="none";
			LmtApply.approve_status._obj._renderHidden(true);  //隐藏审批状态查询条件
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
    /**add by lisj 2015-7-30 需求编号：XD150629047 授信审批流程生成授信审批表 begin**/
	function doPrintAP(){
		var paramStr = LmtApplyList._obj.getParamStr(['serno']);
		if(paramStr != null) {
			var approve_status  = LmtApplyList._obj.getParamValue(['approve_status']);
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
		var paramStr = LmtApplyList._obj.getParamValue(['serno']);
		if (paramStr!=null) {
			var url = '<emp:url action="getUploadInfoPage.do"/>?file_type=03&serno='+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	}
	/*****2019-03-01 jiangcuihua 附件上传  end******/
</script>
</head>
<body class="page_content" onload="onLoadMethod()">
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
		<emp:button id="viewLmtApply" label="查看" op="view"/>
		<emp:button id="submitRediLmtApply" label="复议" op="redi"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<!-- add by lisj 2015-7-30 需求编号：XD150629047 授信审批流程生成授信审批表 begin -->
		<emp:button id="printAP" label="审批意见打印" op="printAP"/>
		<!-- add by lisj 2015-7-30 需求编号：XD150629047 授信审批流程生成授信审批表 end -->
		<emp:button id="upload" label="附件"/>
	</div>

	<emp:table icollName="LmtApplyList" pageMode="true" url="pageLmtApplyQuery.do?type=his&overrule=${context.overrule}">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="app_type" label="申请类型" dictname="STD_ZB_APP_TYPE" />
		<emp:text id="lrisk_type" label="低风险业务类型" dictname="STD_ZB_LRISK_TYPE" />		
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" hidden="true"/>
		<emp:text id="crd_totl_amt" label="授信总额" dataType="Currency"/>
		<emp:text id="crd_cir_amt" label="循环授信敞口" dataType="Currency"/>
		<emp:text id="crd_one_amt" label="一次性授信敞口" dataType="Currency"/>
		<emp:text id="app_date" label="申请日期" cssTDClass="tdCenter" />
		<emp:text id="manager_id_displayname" label="责任人" cssTDClass="tdRight" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="input_id_displayname" label="登记人" cssTDClass="tdRight" hidden="true"/>
		<emp:text id="input_br_id_displayname" label="登记机构" hidden="true"/>
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" cssTDClass="tdCenter"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    