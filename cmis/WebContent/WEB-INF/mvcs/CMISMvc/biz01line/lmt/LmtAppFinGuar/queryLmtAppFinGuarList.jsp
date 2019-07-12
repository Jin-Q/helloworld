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
<%
	String type = request.getParameter("type");
%>
	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtAppFinGuar._toForm(form);
		LmtAppFinGuarList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateLmtAppFinGuarPage() {
		var type = '<%=type%>';
		var paramStr = LmtAppFinGuarList._obj.getParamStr(['serno']);
		var approve_status = LmtAppFinGuarList._obj.getParamStr(['approve_status']);
		var meunId = '${context.menuId}';
		if (paramStr != null) {
			if(approve_status!='approve_status=000' && approve_status!='approve_status=992'&& approve_status!='approve_status=993' ){
				alert("非待发起、退回、追回状态的申请无法修改");
				return;
			}else{
				var url = '<emp:url action="getLmtAppFinGuarUpdatePage.do"/>?'+paramStr+'&type='+type+'&meunId='+meunId;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtAppFinGuar() {
		var type = '<%=type%>';
		var meunId = '${context.menuId}';
		var paramStr = LmtAppFinGuarList._obj.getParamStr(['serno']);
		if (paramStr != null) {
				var url = '<emp:url action="getLmtAppFinGuarViewPage.do"/>?'+paramStr+'&type='+type+'&meunId='+meunId;
				url = EMPTools.encodeURI(url);
				window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddLmtAppFinGuarPage() {
		var type = '<%=type%>';
		var url = '<emp:url action="getLmtAppFinGuarAddPage.do"/>?type='+type;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

	function doDeleteLmtAppFinGuar() {
		var paramStr = LmtAppFinGuarList._obj.getParamStr(['serno']);
		var approve_status = LmtAppFinGuarList._obj.getParamValue(['approve_status']);
		if (paramStr != null){
			if(approve_status=="000"){
				if(confirm("是否确认要删除？")){
					var url = '<emp:url action="deleteLmtAppFinGuarRecord.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);
					var handleSuccess = function(o){
						if(o.responseText !== undefined) {
							try {
								var jsonstr = eval("("+o.responseText+")");
							} catch(e) {
								alert("Parse jsonstr define error!"+e);
								return;
							}
							var flag = jsonstr.flag;
							if(flag=="success"){
								alert("删除成功!");
									var type = '<%=type%>';
									var url = '<emp:url action="queryLmtAppFinGuarList.do"/>&type='+type;
									url = EMPTools.encodeURI(url);
									window.location = url;
						   }else {
							 alert(flag);
							 return;
						   }
						}
					};
					var handleFailure = function(o){	
					};
					var callback = {
						success:handleSuccess,
						failure:handleFailure
					}
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback) 
				}
			}else{
				alert("非[待发起]状态的记录不能进行删除！");
			}
		}else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.LmtAppFinGuarGroup.reset();
	};

	function checkLmtQuotaAdjustApp() {
		var serno = LmtAppFinGuarList._obj.getParamValue(['serno']);
		var url = '<emp:url action="checkLmtQuotaAdjustAppRecord.do"/>?&serno='+serno;
		url = EMPTools.encodeURI(url);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr define error!"+e);
					return;
				}
				var flag = jsonstr.flag;
				if(flag=="success"){
					var paramStr = LmtAppFinGuarList._obj.getParamValue(['serno']);
					var cus_id = LmtAppFinGuarList._obj.getParamValue(['cus_id']);
					var cus_id_displayname = LmtAppFinGuarList._obj.getParamValue(['cus_id_displayname']);
					var fin_totl_limit = LmtAppFinGuarList._obj.getParamValue(['fin_totl_limit']);
					var approve_status = LmtAppFinGuarList._obj.getParamValue(['approve_status']);
					WfiJoin.table_name._setValue("LmtAppFinGuar");
					WfiJoin.pk_col._setValue("serno");
					WfiJoin.pk_value._setValue(paramStr);
					WfiJoin.wfi_status._setValue(approve_status);
					WfiJoin.status_name._setValue("approve_status");
					WfiJoin.appl_type._setValue("375");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：LMT_APP_FLOW
					WfiJoin.cus_id._setValue(cus_id);//客户码
					WfiJoin.cus_name._setValue(cus_id_displayname);//客户名称
					WfiJoin.amt._setValue(fin_totl_limit);//金额
					WfiJoin.prd_name._setValue("融资担保公司用信限额调整申请");//产品名称
					initWFSubmit(false);
			   }else {
				 alert(flag);
				 return;
			   }
			}
		};
		var handleFailure = function(o){	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		}
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback) 
	};
	/*--user code begin--*/
	function doSubmitLmtAppFinGuar(){
		var paramStr = LmtAppFinGuarList._obj.getParamValue(['serno']);
		var cus_id = LmtAppFinGuarList._obj.getParamValue(['cus_id']);
		var cus_id_displayname = LmtAppFinGuarList._obj.getParamValue(['cus_id_displayname']);
		var fin_totl_limit = LmtAppFinGuarList._obj.getParamValue(['fin_totl_limit']);
		var approve_status = LmtAppFinGuarList._obj.getParamValue(['approve_status']);
		var app_type = LmtAppFinGuarList._obj.getParamValue(['app_type']);
		if (paramStr != null) {
			if(app_type == '01'){//融资性担保公司用信限额调整申请
				checkLmtQuotaAdjustApp();
			}else{
				WfiJoin.table_name._setValue("LmtAppFinGuar");
				WfiJoin.pk_col._setValue("serno");
				WfiJoin.pk_value._setValue(paramStr);
				WfiJoin.wfi_status._setValue(approve_status);
				WfiJoin.status_name._setValue("approve_status");
				WfiJoin.appl_type._setValue("372");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：LMT_APP_FLOW
				WfiJoin.cus_id._setValue(cus_id);//客户码
				WfiJoin.cus_name._setValue(cus_id_displayname);//客户名称
				WfiJoin.amt._setValue(fin_totl_limit);//金额
				WfiJoin.prd_name._setValue("融资性担保公司授信申请");//产品名称
				initWFSubmit(false);
			}
			
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
		LmtAppFinGuar.cus_id._setValue(data.cus_id._getValue());//客户码
		LmtAppFinGuar.cus_id_displayname._setValue(data.cus_name._getValue());//客户名称
	}

	function setconId(data){
		LmtAppFinGuar.manager_id_displayname._setValue(data.actorname._getValue());
		LmtAppFinGuar.manager_id._setValue(data.actorno._getValue());
	}

	function doImageView(){
		var data = LmtAppFinGuarList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View23');	//2.3.	客户资料查看（客户全视图）接口
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = LmtAppFinGuarList._obj.getParamValue(['cus_id']);	//客户资料的业务编号就填cus_id
		data['cus_id'] = LmtAppFinGuarList._obj.getParamValue(['cus_id']);	//客户编号
		data['prd_id'] = 'BASIC';	//业务品种
		data['prd_stage'] = '' ;	//业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*--user code end--*/
	
	/*****2019-03-01 jiangcuihua 附件上传  start******/
	function doUpload(){
		var paramStr = LmtAppFinGuarList._obj.getParamValue(['serno']);
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
		<emp:gridLayout id="LmtAppFinGuarGroup" title="输入查询条件" maxColumn="2">
			<emp:pop id="LmtAppFinGuar.cus_id_displayname" label="客户名称" url="queryAllCusPop.do?cusTypCondition=cus_type='A2' and cus_status='20'&returnMethod=returnCus"/>
			<emp:pop id="LmtAppFinGuar.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" />
			<emp:datespace id="LmtAppFinGuar.app_date" label="申请日期" />
			<emp:text id="LmtAppFinGuar.cus_id" label="客户码" hidden="true"/>
			<emp:text id="LmtAppFinGuar.manager_id" label="责任人" maxlength="20" required="false" hidden="true"/>
		</emp:gridLayout>
	</form>

	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddLmtAppFinGuarPage" label="新增" op="add"/>
		<emp:button id="getUpdateLmtAppFinGuarPage" label="修改" op="update"/>
		<emp:button id="deleteLmtAppFinGuar" label="删除" op="remove"/>
		<emp:button id="viewLmtAppFinGuar" label="查看" op="view"/>
		<emp:button id="submitLmtAppFinGuar" label="提交" op="startFlow"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="upload" label="附件"/>
	</div>

	<emp:table icollName="LmtAppFinGuarList" pageMode="true" url="pageLmtAppFinGuarQuery.do" reqParams="type=${context.type}">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="fin_cls" label="融资类别" />
		<emp:text id="app_date" label="申请日期" />
		<emp:text id="fin_totl_limit" label="融资总额" dataType="Currency"/>
		<emp:text id="single_quota" label="单户限额" dataType="Currency"/>
		<emp:text id="guar_bail_multiple" label="担保放大倍数" dataType="Int"/>
		<emp:text id="eval_rst" label="评级结果" dictname="STD_ZB_FINA_GRADE"/>
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS"/>
		<emp:text id="app_type" label="申请类型" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    