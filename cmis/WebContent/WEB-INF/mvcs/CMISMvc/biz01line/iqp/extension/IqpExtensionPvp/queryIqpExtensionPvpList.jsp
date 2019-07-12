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
		IqpExtensionPvp._toForm(form);
		IqpExtensionPvpList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpExtensionPvpPage() {
		var paramStr = IqpExtensionPvpList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpExtensionPvpUpdatePage.do"/>?'+paramStr+"&op=update&sub_button=true&hidden_button=false&restrictUsed=false";
			url = EMPTools.encodeURI(url);
			//window.location = url;
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpExtensionPvp() {
		var paramStr = IqpExtensionPvpList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpExtensionPvpViewPage.do"/>?'+paramStr+"&op=view&sub_button=true&hidden_button=false&restrictUsed=false";
			url = EMPTools.encodeURI(url);
			//window.location = url;
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpExtensionPvpPage() {
		var url = '<emp:url action="getIqpExtensionPvpAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
    function doDeleteIqpExtensionPvp() {
		var paramStr = IqpExtensionPvpList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = IqpExtensionPvpList._obj.getParamValue('approve_status');
			if(status != '000' ){
			    alert("该记录已提交审批！");
			    return ;
			}
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteIqpExtensionPvpRecord.do"/>?'+paramStr;
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
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doReset(){
		page.dataGroups.IqpExtensionPvpGroup.reset();
	};
	
	/*--user code begin--*/
	function returnCus(data){
		IqpExtensionPvp.cus_id._setValue(data.cus_id._getValue());
		IqpExtensionPvp.cus_id_displayname._setValue(data.cus_name._getValue());
    };
	function doHisIqpExtensionPvp() {
		var paramStr = IqpExtensionPvpList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpExtensionPvpViewHis.do"/>?'+paramStr+"&op=view&sub_button=true&hidden_button=false&restrictUsed=false";
			url = EMPTools.encodeURI(url);
			//window.location = url;
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doSubmitIqpExtensionPvp(){
		var paramStr = IqpExtensionPvpList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			var _status = IqpExtensionPvpList._obj.getParamValue(['approve_status']);
			cus_id = IqpExtensionPvpList._obj.getParamValue(['cus_id']);//客户码
			cus_name = IqpExtensionPvpList._obj.getParamValue(['cus_id_displayname']);//客户名称
			amt = IqpExtensionPvpList._obj.getParamValue(['extension_amt']);//展期金额
			WfiJoin.table_name._setValue("IqpExtensionPvp");					
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("017");  //流程申请类型，对应字典项[ZB_BIZ_CATE]
			WfiJoin.cus_id._setValue(cus_id);
			WfiJoin.cus_name._setValue(cus_name);
			WfiJoin.amt._setValue(amt);
			WfiJoin.prd_name._setValue("展期出账申请流程");
			initWFSubmit(false);
		}else {
			alert('请先选择一条记录！');
		}
	};

	function doPrint(){
		var paramStr = IqpExtensionPvpList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			var approve_status  = IqpExtensionPvpList._obj.getParamValue(['approve_status']);
			var serno  = IqpExtensionPvpList._obj.getParamValue(['serno']);
			if(approve_status=='997'){
				var url = '<emp:url action="getReportShowPage.do"/>&reportId=printFktzs/PRINT_ZQCZD.raq&serno='+serno;
				url = EMPTools.encodeURI(url);
				window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
			}else{
				alert("只有状态为'通过'的才能被【打印】！");
			}
		}else {
			alert('请先选择一条记录！');
		}
	}

	function doImageScan(){
		var data = IqpExtensionPvpList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('Scan24');	//业务资料扫描
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function doImageView(){
		var data = IqpExtensionPvpList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View25');	//业务资料查看
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function doImagePrint(){
		var data = IqpExtensionPvpList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('Print');	//业务资料条码打印
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = IqpExtensionPvpList._obj.getParamValue(['fount_serno']);	//业务编号
		data['cus_id'] = IqpExtensionPvpList._obj.getParamValue(['cus_id']);	//客户码
		/**add by lisj 2014年11月10日 修改展期prd_id字段为'zqyw' begin**/
		data['prd_id'] = 'zqyw';	//业务品种
		/**add by lisj 2014年11月10日 修改展期prd_id字段为'zqyw' end**/
		data['prd_stage'] = 'CZSQ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};

	/**add by lisj 2015-8-6  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
	function doGetModifyBizInfoPage(){
		var data = IqpExtensionPvpList._obj.getSelectedData();
		if (data != null && data !=0) {
			var approve_status = IqpExtensionPvpList._obj.getParamValue(['approve_status']); //申请状态
			var fount_cur_type = IqpExtensionPvpList._obj.getParamValue(['fount_cur_type']);
			//申请状态为【打回】状态
			if(approve_status == "992"){
				var serno = IqpExtensionPvpList._obj.getParamValue(['serno']);
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr1 define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						if(flag == "allow"){
							doModifyBizInfo();
						}else if(flag == "forbidden"){
							alert("打回修改业务审批状态仅为【待发起】才允许进行业务信息维护操作！");
						}else if(flag == "exist"){
							alert("存在打回修改业务【待发起】信息,该入口不可用！");
						}else if(flag =="curTypeErr"){
							alert("币种为【人民币】的出账信息才允许信息维护操作！");
						}else if(flag == "limited"){
							alert("该笔出账信息无权进行业务信息维护操作！");
						}else{
							alert("校验类发生异常，请检查！");
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
				var url = '<emp:url action="checkBizModifyRight.do"/>?serno='+serno+"&biz_mode=loanExt"+"&cur_type="+fount_cur_type;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
			}else{
				alert("申请状态仅为【打回】状态才允许发起业务信息维护操作！");
			}
		}else{
			alert('请先选择一条记录！');
		}
	}

	function doModifyBizInfo(){
		var cont_no = IqpExtensionPvpList._obj.getParamValue(['agr_no']);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var modify_rel_serno = jsonstr.modify_rel_serno;
				if(flag == "update"){
				 	var url = '<emp:url action="getBizModifyUpdate4IEAPage.do"/>?modify_rel_serno='+modify_rel_serno;
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else if(flag == "add"){
					doGetModifyBizInfo();
				}else{
					alert("获取业务修改信息发生异常，请联系管理员！");
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
		var url = '<emp:url action="getModifyBizInfo.do"/>?cont_no='+cont_no;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
	};

	function doGetModifyBizInfo(){
		var cont_no  = IqpExtensionPvpList._obj.getParamValue(['agr_no']);
		var serno = IqpExtensionPvpList._obj.getParamValue(['serno']);//出账流水号
		var biz_cate ="016";
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var modify_rel_serno = jsonstr.modify_rel_serno;
				if(flag == "success"){
					var url = '<emp:url action="getBizModifyUpdate4IEAPage.do"/>?modify_rel_serno='+modify_rel_serno;
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else{
					alert("获取业务修改信息发生异常，请联系管理员！");
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
		var url = '<emp:url action="addBizModifyRecord.do"/>?serno='+serno+"&cont_no="+cont_no+"&biz_cate="+biz_cate;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
	};

	function doCheckBizModifyProcess(){
		var paramStr = IqpExtensionPvpList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			var serno = IqpExtensionPvpList._obj.getSelectedData()[0].serno._getValue();
			var agr_no = IqpExtensionPvpList._obj.getSelectedData()[0].agr_no._getValue();
			var url = '<emp:url action="checkBizModifyProcess.do"/>?serno='+serno+"&cont_no="+agr_no;
			url = EMPTools.encodeURI(url);
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
						doSubmitIqpExtensionPvp();
					}else if(flag == "forbidden"){
						alert("该笔出账信息存在打回业务修改的操作信息，不允许提交！");
					}else{
						alert("检查发生异常，请联系管理员！");
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
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);		
		}else{
			alert('请先选择一条记录！');
		}
	};
	
	/**add by lisj 2015-8-6  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
	
	/*****2019-03-01 jiangcuihua 附件上传  start******/
	function doUpload(){
		var paramStr = IqpExtensionPvpList._obj.getParamValue(['serno']);
		if (paramStr!=null) {
			var url = '<emp:url action="getUploadInfoPage.do"/>?file_type=06&serno='+paramStr;
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
	<form  method="POST" action="#" id="queryForm" style="width: 1500">

	<emp:gridLayout id="IqpExtensionPvpGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="IqpExtensionPvp.serno" label="出账流水号" />
			<emp:text id="IqpExtensionPvp.agr_no" label="展期协议编号" />
			<emp:text id="IqpExtensionPvp.fount_bill_no" label="原借据编号" />
			<emp:pop id="IqpExtensionPvp.cus_id_displayname" label="客户名称" buttonLabel="选择" url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" />	
			<emp:select id="IqpExtensionPvp.approve_status" label="申请状态" dictname="WF_APP_STATUS" />
			<emp:text id="IqpExtensionPvp.cus_id" label="客户码"  hidden="true"/>		
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpExtensionPvpPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpExtensionPvpPage" label="修改" op="update"/>
		<emp:button id="deleteIqpExtensionPvp" label="删除" op="remove"/>
		<emp:button id="viewIqpExtensionPvp" label="查看" op="view"/>
		<emp:button id="hisIqpExtensionPvp" label="查看" op="his"/>
		<emp:button id="print" label="打印" op="print"/>
		<!-- modified by lisj 2015-8-25 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin -->
		<emp:button id="checkBizModifyProcess" label="提交" op="startFlow"/>
		<!-- modified by lisj 2015-8-25 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end -->
		<emp:button id="ImageScan" label="影像扫描" op="ImageScan"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="ImagePrint" label="条码打印" op="ImagePrint"/>
		<!--add by lisj 2015-8-6  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin -->
		<emp:button id="getModifyBizInfoPage" label="业务信息维护" op="modifyBizInfo"/>
		<!--add by lisj 2015-8-6  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end -->
		<emp:button id="upload" label="附件"/>
	</div>

	<emp:table icollName="IqpExtensionPvpList" pageMode="true" url="pageIqpExtensionPvpQuery.do">
		<emp:text id="serno" label="出账流水号" />
		<emp:text id="fount_serno" label="原业务编号" hidden="true"/>
		<emp:text id="prd_id" label="产品编号" hidden="true"/>
		<emp:text id="agr_no" label="展期协议编号" />
		<emp:text id="fount_bill_no" label="原借据编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="prd_name" label="产品名称"/>
		<emp:text id="fount_cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="extension_amt" label="展期金额" dataType="Currency"/>		
		<emp:text id="extension_rate" label="展期利率(年)" dataType="Rate"/>
		<emp:text id="extension_date" label="展期到期日期" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />	
		<!-- modified by lisj 2015-8-25 需求编号：【XD150303015】 放款审查岗打回的业务申请信息修改需求 begin -->	
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" hidden="true"/>
		<emp:text id="status_display" label="申请状态" dictname="WF_APP_STATUS" />
		<!-- modified by lisj 2015-8-25 需求编号：【XD150303015】 放款审查岗打回的业务申请信息修改需求 end -->
		<emp:text id="input_date" label="出账申请日期" />
	</emp:table>
	</form>
</body>
</html>
</emp:page>