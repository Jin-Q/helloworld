<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>
<% 
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String type = "";
	if(context.containsKey("type")){
		type = context.getDataValue("type").toString();
	}
%>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtAppFinSubpay._toForm(form);
		LmtAppFinSubpayList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateLmtAppFinSubpayPage() {
		var paramStr = LmtAppFinSubpayList._obj.getParamStr(['serno','cus_id']);
		var type = '<%=type%>';
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAppFinSubpayUpdatePage.do"/>?'+paramStr+'&dcpa=yes'+'&type='+type+'&op=update';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtAppFinSubpay() {
		var paramStr = LmtAppFinSubpayList._obj.getParamStr(['serno','cus_id']);
		var type = '<%=type%>';
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAppFinSubpayViewPage.do"/>?'+paramStr+'&dcpa=yes'+'&type='+type+'&op=view&act=view';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddLmtAppFinSubpayPage() {
		var type = '<%=type%>';
		var url = '<emp:url action="getLmtAppFinSubpayAddBasePage.do"/>?dcpa=yes'+'&type='+type+'&op=add';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteLmtAppFinSubpay() {
		var paramStr = LmtAppFinSubpayList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteLmtAppFinSubpayRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				doDelete(url);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doDelete(url){
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
				if(flag=='success'){
					alert("删除成功！");
		            window.location.reload();
				}else{
					alert('操作失败!');
				}
			}
		}
		var handleFailure = function(o) {
			alert("操作失败!");
		}
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		}
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
	}
	
	function doReset(){
		page.dataGroups.LmtAppFinSubpayGroup.reset();
	};
	
	/*--user code begin--*/
	function doSubmitLmtAppFinSubpay(){
		var paramStr = LmtAppFinSubpayList._obj.getParamValue(['serno']);
		var cus_id = LmtAppFinSubpayList._obj.getParamValue(['cus_id']);
		var cus_id_displayname = LmtAppFinSubpayList._obj.getParamValue(['cus_id_displayname']);
		var subpay_totl_limit = LmtAppFinSubpayList._obj.getParamValue(['subpay_totl_limit']);
		var approve_status = LmtAppFinSubpayList._obj.getParamValue(['approve_status']);
		if (paramStr != null) {
			WfiJoin.table_name._setValue("LmtAppFinSubpay");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(approve_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("373");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：Lmt_App_Fin_Subpay
			WfiJoin.cus_id._setValue(cus_id);//客户码
			WfiJoin.cus_name._setValue(cus_id_displayname);//客户名称
			WfiJoin.amt._setValue(subpay_totl_limit);//代偿总额
			WfiJoin.prd_name._setValue("融资性担保公司代偿申请");//产品名称
			initWFSubmit(false);
		}else {
			alert('请先选择一条记录！');
		}
	}

	function doSubmitRecord(url){
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
				if(flag=='success'){
					alert("提交成功！");
		            window.location.reload();
				}else{
					alert('操作失败!');
				}
			}
		}
		var handleFailure = function(o) {
			alert("操作失败!");
		}
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		}
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
	}

	function returnCus(data){
		LmtAppFinSubpay.cus_id._setValue(data.cus_id._getValue());//客户名称
		LmtAppFinGuar.cus_id_displayname._setValue(data.cus_name._getValue());//客户名称
	}

	function doImageView(){
		var data = LmtAppFinSubpayList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View23');	//2.3.	客户资料查看（客户全视图）接口
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = LmtAppFinSubpayList._obj.getParamValue(['cus_id']);	//客户资料的业务编号就填cus_id
		data['cus_id'] = LmtAppFinSubpayList._obj.getParamValue(['cus_id']);	//客户编号
		data['prd_id'] = 'BASIC';	//业务品种
		data['prd_stage'] = '' ;	//业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*--user code end--*/
	/*****2019-03-01 jiangcuihua 附件上传  start******/
	function doUpload(){
		var paramStr = LmtAppFinSubpayList._obj.getParamValue(['serno']);
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

	<emp:gridLayout id="LmtAppFinSubpayGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="LmtAppFinSubpay.cus_id" label="客户码" />
			<emp:pop id="LmtAppFinGuar.cus_id_displayname" label="客户名称" url="queryAllCusPop.do?cusTypCondition=cus_type='A2' and cus_status='20'&returnMethod=returnCus"/>
			<emp:select id="LmtAppFinSubpay.approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddLmtAppFinSubpayPage" label="新增" op="add"/>
		<emp:button id="getUpdateLmtAppFinSubpayPage" label="修改" op="update"/>
		<emp:button id="deleteLmtAppFinSubpay" label="删除" op="remove"/>
		<emp:button id="viewLmtAppFinSubpay" label="查看" op="view"/>
		<emp:button id="submitLmtAppFinSubpay" label="提交" op="startFlow"/>
		<emp:button id="print" label="打印" op="print"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="upload" label="附件"/>
	</div>

	<emp:table icollName="LmtAppFinSubpayList" pageMode="true" url="pageLmtAppFinSubpayQuery.do" reqParams="type=${context.type}&rt=${context.rt}&cus_id=${context.cus_id}">
		<emp:text id="serno" label="业务编号"/>
		<emp:text id="cus_id" label="客户码"/>
		<emp:text id="cus_id_displayname" label="客户名称"/>
		<emp:text id="subpay_times" label="代偿笔数" dataType="Int"/>
		<emp:text id="subpay_totl_limit" label="代偿总额" dataType="Currency"/>
		<emp:text id="subpay_app_date" label="代偿申请日期"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人"/>
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    