<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String biz_type = "";
	if(context.containsKey("biz_type")){
		biz_type = (String)context.getDataValue("biz_type");
		
	} 
%>
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
		PvpLoanApp._toForm(form);
		PvpLoanAppList._obj.ajaxQuery(null,form);
	};
	function doGetUpdatePvpLoanAppPage() {
		var biz_type='<%=biz_type %>';
		var paramStr = PvpLoanAppList._obj.getParamStr(['serno']);
		var cont_no = PvpLoanAppList._obj.getParamStr(['cont_no']);
		if (paramStr != null) {
			var approve_status = PvpLoanAppList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status == "000" || approve_status == "992" || approve_status == "993"){
				var url = '<emp:url action="getPvpLoanAppUpdatePage.do"/>?'+paramStr+'&'+cont_no+'&biz_type='+biz_type;
				url = EMPTools.encodeURI(url);
				//window.location = url;
				var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
				window.open(url,'newWindow',param);
			}else{
				alert("非【待发起】、【打回】、【追回】状态的记录不能进行修改操作！");
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPvpLoanApp() {
		var paramStr = PvpLoanAppList._obj.getParamStr(['serno','cont_no']);

		if (paramStr != null) {
			var url = '<emp:url action="getPvpLoanAppViewPage.do"/>?'+paramStr+"&flag=pvpLoan&biz_type="+'<%=biz_type%>';
			url = EMPTools.encodeURI(url);
			//window.location = url;
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！'); 
		}
	};
	
	function doGetAddPvpLoanAppPage() {
		var biz_type='<%=biz_type %>';
		if(biz_type == '7'){
			if('${context.menuId}'=='queryPvpLoanAppList'){
				var url = '<emp:url action="getPvpLoanAppAddPage.do"/>?menuId=getPvpLoanAppAddPage&biz_type='+biz_type;
			}else if('${context.menuId}'=='cfirmCsgnqueryPvpLoanAppList'){
				var url = '<emp:url action="getPvpLoanAppAddPage.do"/>?menuId=cfirmCsgngetPvpLoanAppAdd&biz_type='+biz_type;
			}else if('${context.menuId}'=='csgnClaimInvestqueryPvpLoanApp'){
				var url = '<emp:url action="getPvpLoanAppAddPage.do"/>?menuId=csgnClaimInvestgetPvpLoanAppAdd&biz_type='+biz_type;
			}
	    }else if(biz_type == '8'){
	    	var url = '<emp:url action="getPvpLoanAppAddPage.do"/>?menuId=yztgetPvpLoanAppAddPage&biz_type='+biz_type;
	    }else{
	    	var url = '<emp:url action="getPvpLoanAppAddPage.do"/>?menuId=sltgetPvpLoanAppAddPage&biz_type='+biz_type;
		}
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePvpLoanApp() {
		var paramStr = PvpLoanAppList._obj.getParamStr(['serno','pvp_amt','cont_no']); 
		if (paramStr != null) {
			var approve_status = PvpLoanAppList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status == "000"){ 
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr1 define error!" + e.message);
							return;
						}
						var del = jsonstr.del; 
						if(del == "success"){
							alert("删除成功!");
							var url = '<emp:url action="queryPvpLoanAppList.do"/>?biz_type='+'<%=biz_type%>';  
							url = EMPTools.encodeURI(url);
							window.location=url; 
						}else {
							alert("发生异常!"); 
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
				var url = '<emp:url action="deletePvpLoanAppRecord.do"/>?'+paramStr;	
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
			}else{
				alert("只有状态为【待发起】的申请才可以进行删除！");
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PvpLoanAppGroup.reset();
	};
//------生成授权信息------
	function doGetAuthorize(){
		return false;//暂不可用
		var paramStr = PvpLoanAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
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
						alert("生成授权信息成功！");
						window.location.reload();
					}else {
						alert("生成授权信息失败！");
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
			var url = '<emp:url action="getAuthorizeRecord.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
		} else {
			alert('请先选择一条记录！');
		}
	};
	function returnCus(data){
		PvpLoanApp.cus_id._setValue(data.cus_id._getValue());
		PvpLoanApp.cus_name._setValue(data.cus_name._getValue());
	};
	function returnPrdId(data){
		PvpLoanApp.prd_id._setValue(data.id);
		PvpLoanApp.prd_id_displayname._setValue(data.label); 
	};


	function doFlow(){
		var paramStr = PvpLoanAppList._obj.getParamStr(['serno']);
		    if (paramStr != null) {
		    getApplyTypeByPrdId();
		}else{
			alert('请先选择一条记录！');
		}
	};
	//-----------通过产品编号查询产品配置中使用流程类型----------
	function getApplyTypeByPrdId(){
		var prdId = PvpLoanAppList._obj.getSelectedData()[0].prd_id._getValue();
		var url = '<emp:url action="getPvpApplyTypeByPrdId.do"/>?prdid='+prdId;
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
				var msg = jsonstr.msg;
				var apply_type = jsonstr.apply_type;
				if(flag == "success"){
					/**add by lisj 2015-8-24  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
					checkBizModifyProcess(apply_type);
					/**add by lisj 2015-8-24  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
				}else {
					alert(msg);
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
	};

	//-----------提交流程----------
	function doSubmitWF(apply_type){
		var serno = PvpLoanAppList._obj.getSelectedData()[0].serno._getValue();
		var cus_id = PvpLoanAppList._obj.getSelectedData()[0].cus_id._getValue();
		var cus_name = PvpLoanAppList._obj.getSelectedData()[0].cus_id_displayname._getValue();
		var approve_status = PvpLoanAppList._obj.getSelectedData()[0].approve_status._getValue();
		WfiJoin.cus_id._setValue(cus_id);
		WfiJoin.cus_name._setValue(cus_name);
		WfiJoin.prd_pk._setValue(PvpLoanAppList._obj.getSelectedData()[0].prd_id._getValue());
		WfiJoin.prd_name._setValue(PvpLoanAppList._obj.getSelectedData()[0].prd_id_displayname._getValue());
		WfiJoin.amt._setValue(PvpLoanAppList._obj.getSelectedData()[0].pvp_amt._getValue());
		WfiJoin.table_name._setValue("PvpLoanApp");
		WfiJoin.pk_col._setValue("serno");
		WfiJoin.pk_value._setValue(serno);
		WfiJoin.wfi_status._setValue(approve_status);
		WfiJoin.status_name._setValue("approve_status");
		WfiJoin.appl_type._setValue(apply_type);
		initWFSubmit(false);
	};
	/*--user code begin--*/
	function doImageScan(){
		var data = PvpLoanAppList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('Scan24');	//业务资料扫描
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function doImageView(){
		var data = PvpLoanAppList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View25');	//业务资料查看
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function doImagePrint(){
		var data = PvpLoanAppList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('Print');	//业务资料条码打印
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = PvpLoanAppList._obj.getParamValue(['fount_serno']);	//业务编号
		data['cus_id'] = PvpLoanAppList._obj.getParamValue(['cus_id']);	//客户码
		data['prd_id'] = PvpLoanAppList._obj.getParamValue(['prd_id']);	//业务品种
		data['prd_stage'] = 'CZSQ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};

	/**add by lisj 2015-8-6  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
	function doGetModifyBizInfoPage(){
		var data = PvpLoanAppList._obj.getSelectedData();
		if (data != null && data !=0) {
			var cur_type = PvpLoanAppList._obj.getParamValue(['cur_type']);
			var approve_status = PvpLoanAppList._obj.getParamValue(['approve_status']); //申请状态
			//申请状态为【打回】状态
			if(approve_status == "992"){
				var serno = PvpLoanAppList._obj.getParamValue(['serno']);
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
							doCheckModifyBizInfo();
						}else if(flag == "limited"){
							alert("该笔出账信息无权进行业务信息维护操作！");
						}else if(flag == "exist"){
							alert("存在打回修改业务【待发起】信息,该入口不可用！");
						}else if(flag == "forbidden"){
							alert("打回修改业务审批状态仅为【待发起】才允许进行业务信息维护操作！");
						}else if(flag =="curTypeErr"){
							alert("币种为【人民币】的出账信息才允许信息维护操作！");
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
				var url = '<emp:url action="checkBizModifyRight.do"/>?serno='+serno+"&biz_mode=loan"+"&cur_type="+cur_type;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
			}else{
				alert("申请状态仅为【打回】状态才允许发起业务信息维护操作！");
			}
		}else{
			alert('请先选择一条记录！');
		}
	}

	function doCheckModifyBizInfo(){
		var cont_no = PvpLoanAppList._obj.getParamValue(['cont_no']);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if(flag == "update"){
					var modify_rel_serno = jsonstr.modify_rel_serno;
				 	var url = '<emp:url action="getBizModifyUpdatePage.do"/>?modify_rel_serno='+modify_rel_serno;
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else if(flag == "add"){
					doAddModifyBizInfo();
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

	function doAddModifyBizInfo(){
		var cont_no  = PvpLoanAppList._obj.getParamValue(['cont_no']);
		var serno = PvpLoanAppList._obj.getParamValue(['serno']);//出账流水号
		var fount_serno = PvpLoanAppList._obj.getParamValue(['fount_serno']);
		var cus_id =PvpLoanAppList._obj.getParamValue(['cus_id']);
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
					var url = '<emp:url action="getBizModifyUpdatePage.do"/>?modify_rel_serno='+modify_rel_serno;
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
		var url = '<emp:url action="addBizModifyRecord.do"/>?serno='+serno+"&cont_no="+cont_no+"&cus_id="+cus_id+"&fount_serno="+fount_serno;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
	};

	function checkBizModifyProcess(apply_type){
		var serno = PvpLoanAppList._obj.getSelectedData()[0].serno._getValue();
		var cont_no = PvpLoanAppList._obj.getSelectedData()[0].cont_no._getValue();
		var url = '<emp:url action="checkBizModifyProcess.do"/>?serno='+serno+"&cont_no="+cont_no;
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
					doSubmitWF(apply_type);
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
	};
	/**add by lisj 2015-8-6  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
	/*****2019-03-01 jiangcuihua 附件上传  start******/
	function doUpload(){
		var paramStr = PvpLoanAppList._obj.getParamValue(['serno']);
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
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PvpLoanAppGroup" title="输入查询条件" maxColumn="3">
		<emp:text id="PvpLoanApp.serno" label="出账流水号" />
		<emp:pop id="PvpLoanApp.cus_name" label="客户名称" url="queryAllCusPop.do?returnMethod=returnCus" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
	    <emp:select id="PvpLoanApp.approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	    <emp:text id="PvpLoanApp.cont_no" label="合同编号" />	    
	    <emp:pop id="PvpLoanApp.prd_id_displayname" label="产品名称" url="showPrdTreeDetails.do?bizline=BL100,BL200,BL300,BL400,BL500" returnMethod="returnPrdId" />
	    <emp:text id="PvpLoanApp.prd_id" label="产品编号"  hidden="true" />
	    <emp:text id="PvpLoanApp.cus_id" label="客户码"  hidden="true" />
	</emp:gridLayout> 
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddPvpLoanAppPage" label="新增" op="add"/>
		<emp:button id="getUpdatePvpLoanAppPage" label="修改" op="update"/>
		<emp:button id="deletePvpLoanApp" label="删除" op="remove"/>
		<emp:button id="viewPvpLoanApp" label="查看" op="view"/>
		<emp:button id="flow" label="提交" op="submit"/>
		<emp:button id="ImageScan" label="影像扫描" op="ImageScan"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="ImagePrint" label="条码打印" op="ImagePrint"/>
		<!-- emp:button id="getAuthorize" label="提交" -->
		<!--add by lisj 2015-8-6  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin -->
		<emp:button id="getModifyBizInfoPage" label="业务信息维护" op="modifyBizInfo"/>
		<!--add by lisj 2015-8-6  需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end -->
		<emp:button id="upload" label="附件"/>
	</div>

	<emp:table icollName="PvpLoanAppList" pageMode="true" url="pagePvpLoanAppQuery.do" reqParams="biz_type=${context.biz_type}">
		<emp:text id="serno" label="出账流水号" />
		<emp:text id="fount_serno" label="原业务编号" hidden="true" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="prd_id" label="产品编号" hidden="true"/> 
		<emp:text id="prd_id_displayname" label="产品名称" /> 
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE"/>
		<emp:text id="pvp_amt" label="出账金额" dataType="Currency"/>	
		<emp:text id="manager_br_id_displayname" label="管理机构" />	
		<emp:text id="in_acct_br_id_displayname" label="入账机构" />	
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<!-- modified by lisj 2015-8-25 需求编号：【XD150303015】 放款审查岗打回的业务申请信息修改需求 begin -->	
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" hidden="true"/>
		<emp:text id="status_display" label="申请状态" dictname="WF_APP_STATUS" />
		<!-- modified by lisj 2015-8-25 需求编号：【XD150303015】 放款审查岗打回的业务申请信息修改需求 end -->	
		<emp:text id="input_date" label="出账申请日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    