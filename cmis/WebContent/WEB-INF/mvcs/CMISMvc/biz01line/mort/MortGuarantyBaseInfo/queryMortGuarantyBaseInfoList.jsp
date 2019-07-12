<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<% 
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String menuId = "";
	if(context.containsKey("menuId")){
		menuId = (String)context.getDataValue("menuId");
	}
%>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input2 {
		border: 1px solid #BCD7E2;
		text-align: left;
		width: 450px;
	};
</style>
<script type="text/javascript">
   var agrInfo ;//全局变量（用来存储监管协议类型及其编号）
	function doQuery(){
		var form = document.getElementById('queryForm');
		MortGuarantyBaseInfo._toForm(form);
		MortGuarantyBaseInfoList._obj.ajaxQuery(null,form);
	};
	//商链通时，根据押品编号获取协议类型和协议编号
	function getAgrUpdate(data) {
		var paramStr = MortGuarantyBaseInfoList._obj.getParamStr(['guaranty_no']);
		if (paramStr != null) {
			getAgrInfo(data);
			<%if(menuId.equals("hwdj")){%>//货物登记菜单标识参数
			//flag=hwdj控制返回按钮
			var url = '<emp:url action="getMortGuarantyBaseInfoUpdatePage.do"/>?op=update&menuId=hwdj&flag=hwdj&'+agrInfo+'&'+paramStr;
			<%}else if(menuId.equals("hwgl")){%>
			var url = '<emp:url action="getMortGuarantyBaseInfoUpdatePage.do"/>?op=update&flag=hwgl&'+agrInfo+'&'+paramStr;
			<%}else{%>
			var url = '<emp:url action="getMortGuarantyBaseInfoUpdatePage.do"/>?op=update&'+paramStr;
			<%}%>
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function getAgrView(data) {
		var paramStr = MortGuarantyBaseInfoList._obj.getParamStr(['guaranty_no']);
		if (paramStr != null) {
			getAgrInfo(data);
			<%if(menuId.equals("hwdj")){%>
			//flag=hwdj控制返回按钮
			var url = '<emp:url action="getMortGuarantyBaseInfoViewPage.do"/>?op=view&menuId=mort_maintain&flag=hwdj&'+agrInfo+'&'+paramStr;
			<%}else if(menuId.equals("hwgl")){%>
			var url = '<emp:url action="getMortGuarantyBaseInfoViewPage.do"/>?op=view&flag=hwgl&'+agrInfo+'&'+paramStr;
			<%}else if("storage".equals(menuId)||"temp_storage".equals(menuId)){ //"已入库"、"临时出库"查看 %>
			var url = '<emp:url action="getMortGuarantyBaseInfoViewPage.do"/>?op=view&'+paramStr+'&menuIdTab=mort_maintain';
			<%}else{%>
			var url = '<emp:url action="getMortGuarantyBaseInfoViewPage.do"/>?op=view&'+paramStr;
			<%}%>
			url = EMPTools.encodeURI(url);
			window.location = url;  
		} else {
			alert('请先选择一条记录！');
		}
	};
	//获取监管协议信息
	function getAgrInfo(data){
		var agr_type = data.agr_type;
		var agr_no = data.agr_no;
		//为监管协议信息赋值
		agrInfo = "agr_type="+agr_type+"&agr_no="+agr_no;
	}
	//押品修改事件
	function doGetUpdateMortGuarantyBaseInfoPage(){
		var paramStr = MortGuarantyBaseInfoList._obj.getParamStr(['guaranty_no']);
		var handleFailure = function(o) {
			alert("获取协议类型和协议编号失败！");
		};
		var callback = {
			success :"getAgrUpdate",
			isJSON : true,
			failure :handleFailure
		};
		var url = '<emp:url action="getAgrInfoByGuarantyNo.do"/>?&flag='+paramStr;
		url = EMPTools.encodeURI(url);
		EMPTools.ajaxRequest('POST',url,callback);
	}
	//押品查看事件
	function doViewMortGuarantyBaseInfo(){
		var paramStr = MortGuarantyBaseInfoList._obj.getParamStr(['guaranty_no']);
		var handleFailure = function(o) {
			alert("获取协议类型和协议编号失败！");
		};
		var callback = {
			success :"getAgrView",
			isJSON : true,
			failure :handleFailure
		};
		var url = '<emp:url action="getAgrInfoByGuarantyNo.do"/>?'+paramStr;
		url = EMPTools.encodeURI(url);
		EMPTools.ajaxRequest('POST',url,callback);
	}
//押品维护菜单，入库按钮
	function doStorageMortGuarantyBaseInfo() {
		var paramStr = MortGuarantyBaseInfoList._obj.getParamStr(['guaranty_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getMortGuarantyBaseInfoViewPage.do"/>?op=to_storage&menuId=mort_maintain&stay=stay_storage&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;  
		} else {
			alert('请先选择一条记录！');
		}
	};
//商链通菜单，货物管理中的入库按钮
	function doInStorageMortGuarantyBaseInfo() {
		var paramStr = MortGuarantyBaseInfoList._obj.getParamStr(['guaranty_no','cus_id','guaranty_info_status']);
		if (paramStr != null) {
			var handleSuccess = function(o) {
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("Parse jsonstr define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					var msg = jsonstr.msg;
					if("success" == flag){
						var url = '<emp:url action="getMortCargoStorageAddPage.do"/>?op=in_storage&menuId=hwgl&'+paramStr;
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else{
						alert(msg);
					}
				}
			};
			var handleFailure = function(o) {
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			var url = '<emp:url action="checkIsOpAllowed.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
	 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
		} else {
			alert('请先选择一条记录！');
		}
	};
	//商链通菜单，货物管理中的出库按钮
	function doOutStorageMortGuarantyBaseInfo() {
		var paramStr = MortGuarantyBaseInfoList._obj.getParamStr(['guaranty_no','cus_id']);
		if (paramStr != null) {
			var handleSuccess = function(o) {
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("Parse jsonstr define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					var msg = jsonstr.msg;
					if("success" == flag){
						var url = '<emp:url action="getMortCargoExwareAddPage.do"/>?op=out_storage&menuId=hwgl&'+paramStr;
						url = EMPTools.encodeURI(url);
						window.location = url;  
					}else{
						alert(msg);
					}
				}
			};
			var handleFailure = function(o) {
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			var url = '<emp:url action="checkIsOpAllowed.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
	 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
		} else {
			alert('请先选择一条记录！');
		}
	};
	//权证管理菜单的出库按钮
	function doExwaMortGuarantyBaseInfo(){
		var paramStr = MortGuarantyBaseInfoList._obj.getParamStr(['guaranty_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getMortGuarantyBaseInfoViewPage.do"/>?op=exwa&menuId=mort_maintain&stay=stay_storage&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;  
		} else {
			alert('请先选择一条记录！');
		}
	}
	function doGetAddMortGuarantyBaseInfoPage() {
		<%if(menuId.equals("hwdj")){%>//货物登记菜单新增
			var url = '<emp:url action="addIqpCargoOverseeReRecord.do"/>';
			url = EMPTools.encodeURI(url);
			var returns = window.showModalDialog(url,'newwindow','dialogWidth:600px;dialogHeight:300px;dialogLeft:200px;dialogTop:150px;center:yes;help:yes;resizable:yes;status:yes');
			var agr_type=returns[0];
			var agr_no =returns[1];
			if(agr_type!=""&&agr_no!=""){//返回的协议编号和协议类型不为空时，才进行跳转、新增操作
				var url1 = '<emp:url action="getMortGuarantyBaseInfoAddPage.do"/>?flag=hwdj&agr_type='+agr_type+'&agr_no='+agr_no;
				url1 = EMPTools.encodeURI(url1);
				window.location = url1;
			}
		<%}else if(menuId.equals("mort_maintain")){%>//押品维护菜单新增
			var url = '<emp:url action="getMortGuarantyBaseInfoAddPage.do"/>?flag=ypwh';
			url = EMPTools.encodeURI(url);
			window.location = url;
		<%}else if(menuId.equals("hwgl")){%>//货物管理菜单新增
			var url = '<emp:url action="addIqpCargoOverseeReRecord.do"/>';
			url = EMPTools.encodeURI(url);
			var returns = window.showModalDialog(url,'newwindow','dialogWidth:600px;dialogHeight:300px;dialogLeft:200px;dialogTop:150px;center:yes;help:yes;resizable:yes;status:yes');
			var agr_type=returns[0];
			var agr_no =returns[1];
			if(agr_type!=""&&agr_no!=""){//返回的协议编号和协议类型不为空时，才进行跳转、新增操作
				var url1 = '<emp:url action="getMortGuarantyBaseInfoAddPage.do"/>?flag=hwgl&agr_type='+agr_type+'&agr_no='+agr_no;
				url1 = EMPTools.encodeURI(url1);
			    window.location = url1;
			}
		<%}%>
		
	};
	function doDeleteMortGuarantyBaseInfo() {
		var paramStr = MortGuarantyBaseInfoList._obj.getParamStr(['guaranty_no']);
		var guaranty_type = MortGuarantyBaseInfoList._obj.getParamStr(['guaranty_type']);
		var guaranty_info_status = MortGuarantyBaseInfoList._obj.getParamValue(['guaranty_info_status']);
		if (paramStr != null) {
			if(guaranty_info_status=="1"||guaranty_info_status=="2"){
				if(confirm("是否确认要删除？")){
					var handleSuccess = function(o) {
						if (o.responseText !== undefined) {
							try {
								var jsonstr = eval("(" + o.responseText + ")");
							} catch (e) {
								alert("Parse jsonstr define error!" + e.message);
								return;
							}
							var delet = jsonstr.delet;
							if("true" == delet){
								alert("已删除！");
								window.location.reload();
							}else{
								alert("该押品已被担保合同引用不能进行删除操作！");
							}
						}
					};
					var handleFailure = function(o) {
					};
					var callback = {
						success :handleSuccess,
						failure :handleFailure
					};
					<%if(menuId.equals("hwdj")||menuId.equals("hwgl")){%> //货物管理和货物登记删除时，需要级联删除货物与监管协议的关联记录
					var url = '<emp:url action="deleteMortGuarantyBaseInfoRecord.do"/>?'+paramStr+'&'+guaranty_type+'&flag=hwdj';
					url = EMPTools.encodeURI(url);
			 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
					<%}else if(menuId.equals("mort_maintain")){%>//押品维护时的删除。
					var url = '<emp:url action="deleteMortGuarantyBaseInfoRecord.do"/>?'+paramStr+'&'+guaranty_type;
					url = EMPTools.encodeURI(url);
			 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
			 		<%}%>
				}
			}else{
				alert('只有处于登记中或登记完成状态的押品可以进行删除！');
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doReset(){
		page.dataGroups.MortGuarantyBaseInfoGroup.reset();
	};
	//货物置换按钮
	function doCargoRepl(){
		var paramStr = MortGuarantyBaseInfoList._obj.getParamStr(['guaranty_no','cus_id']);
		var status = MortGuarantyBaseInfoList._obj.getParamValue(['guaranty_info_status']);
		if (paramStr != null) {
			if(status!='3'){
				alert("非押品生效状态的货物不能做“货物置换”操作！");
				return;
			}
			var handleSuccess = function(o) {
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("Parse jsonstr define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					var msg = jsonstr.msg;
					if("success" == flag){
				//		var url = '<emp:url action="getMortCargoReplAddPage.do"/>?op=cargo_repl&menuId=hwzh&'+paramStr;
						var url = '<emp:url action="getMortCargoReplAddLeadPage.do"/>?op=cargo_repl&menuId=hwzh&'+paramStr;
						url = EMPTools.encodeURI(url);
						window.location = url;  
					}else{
						alert(msg);
					}
				}
			};
			var handleFailure = function(o) {
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			var url = '<emp:url action="checkIsOpAllowed.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
	 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
		} else {
			alert('请先选择一条记录！');
		}
	}
	//保证金提货
	function doBailLad(){
		var paramStr = MortGuarantyBaseInfoList._obj.getParamStr(['guaranty_no','cus_id']);
		var status = MortGuarantyBaseInfoList._obj.getParamValue(['guaranty_info_status']);
		if (paramStr != null) {
			if(status!='3'){
				alert("非押品生效状态的货物不能做“保证金提货”操作！");
				return;
			}
			var handleSuccess = function(o) {
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("Parse jsonstr define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					var msg = jsonstr.msg;
					if("success" == flag){
						var url = '<emp:url action="getMortBailDelivAddPage.do"/>?op=bail_lad&menuId=bzjth&'+paramStr;
						url = EMPTools.encodeURI(url);
						window.location = url;  
					}else{
						alert(msg);
					}
				}
			};
			var handleFailure = function(o) {
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			var url = '<emp:url action="checkIsOpAllowed.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
	 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
		} else {
			alert('请先选择一条记录！');
		}
	}
	function getReturnValueForGuarantyType(data){
		MortGuarantyBaseInfo.guaranty_type_displayname._setValue(data.label);
		MortGuarantyBaseInfo.guaranty_type._setValue(data.id);
	}
	function getReturnValueForGuarantyType1(data){
		MortGuarantyBaseInfo.guaranty_type_displayname._setValue(data.locate_cn);
		MortGuarantyBaseInfo.guaranty_type._setValue(data.locate);
	}
	

	/*** 影像部分操作按钮begin ***/
	function doImageView(){
		var data = MortGuarantyBaseInfoList._obj.getSelectedData();
		if (data != null && data !=0) {
			var image_guaranty_no = MortGuarantyBaseInfoList._obj.getParamValue(['image_guaranty_no']);
			if(image_guaranty_no != null && image_guaranty_no !=''){
				ImageAction('View23');	//2.3.	客户资料查看（客户全视图）接口
			}else{
				alert('影像押品编号不能为空，请确认!');
			}			
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function doImageScan(){
		var data = MortGuarantyBaseInfoList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('Scan22');	//2.2.	客户资料扫描（抵质押预扫）接口
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function doImageCheck(){
		var data = MortGuarantyBaseInfoList._obj.getSelectedData();
		if (data != null && data !=0) {
			var image_guaranty_no = MortGuarantyBaseInfoList._obj.getParamValue(['image_guaranty_no']);
			if(image_guaranty_no != null && image_guaranty_no !=''){
				if( confirm("影像信息将直接归档，请确认!") ){
					ImageAction('Check3133');	//影像核对
				}
			}else{
				alert('影像押品编号不能为空，请确认!');
			}			
		} else {
			alert('请先选择一条记录！');
		}	
	};
	function doImagePrint(){
		var data = MortGuarantyBaseInfoList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('Print');	//条码打印接口
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = MortGuarantyBaseInfoList._obj.getParamValue(['guaranty_no']);	//客户资料的业务编号就填cus_id
		data['cus_id'] = MortGuarantyBaseInfoList._obj.getParamValue(['cus_id']);	//客户编号
		data['prd_id'] = 'ASSURE';	//业务品种
		data['prd_stage'] = MortGuarantyBaseInfoList._obj.getParamValue(['image_guaranty_no']);	//业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	
	function returnCus(data){
		MortGuarantyBaseInfo.cus_id._setValue(data.cus_id._getValue());
		MortGuarantyBaseInfo.cus_name._setValue(data.cus_name._getValue());
	};
	/*** 影像部分操作按钮end ***/
	/*****2019-03-01 jiangcuihua 附件上传  start******/
	function doUpload(){
		var paramStr = MortGuarantyBaseInfoList._obj.getParamValue(['guaranty_no']);
		if (paramStr!=null) {
			var url = '<emp:url action="getUploadInfoPage.do"/>?file_type=07&serno='+paramStr;
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
	

	<emp:gridLayout id="MortGuarantyBaseInfoGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="MortGuarantyBaseInfo.guaranty_no" label="押品编号" />
			<emp:text id="MortGuarantyBaseInfo.guaranty_name" label="押品名称" />
			<emp:text id="MortGuarantyBaseInfo.image_guaranty_no" label="影像押品编号" />
			<emp:pop id="MortGuarantyBaseInfo.cus_name" label="出质人名称" buttonLabel="选择" url="queryAllCusPop.do?returnMethod=returnCus"/>
			<emp:select id="MortGuarantyBaseInfo.guaranty_cls" label="押品类别" dictname="STD_GUARANTY_TYPE" />
			<emp:select id="MortGuarantyBaseInfo.guaranty_info_status" label="押品信息状态" dictname="STD_MORT_STATE" />
			<%if(menuId.equals("hwdj")||menuId.equals("hwgl")){%>
			<emp:pop id="MortGuarantyBaseInfo.guaranty_type_displayname" label="押品类型"  readonly="false" url="showCatalogManaTree.do?&isMin=N&value=N" returnMethod="getReturnValueForGuarantyType1" required="true" cssElementClass="emp_field_text_long"/>
			<emp:text id="MortGuarantyBaseInfo.guaranty_type" label="押品类型" required="true" hidden="true"/>
			<%}else{%>
			<emp:pop id="MortGuarantyBaseInfo.guaranty_type_displayname" label="押品类型"  readonly="false" url="showDicTree.do?dicTreeTypeId=MORT_TYPE&parentNodeId=Z090100" returnMethod="getReturnValueForGuarantyType" required="true" cssElementClass="emp_field_text_long"/>
			<emp:text id="MortGuarantyBaseInfo.guaranty_type" label="押品类型" required="true" hidden="true"/>
			<%}%>
			
			<emp:text id="MortGuarantyBaseInfo.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddMortGuarantyBaseInfoPage" label="新增" op="add"/>
		<emp:button id="getUpdateMortGuarantyBaseInfoPage" label="修改" op="update"/>
		<emp:button id="deleteMortGuarantyBaseInfo" label="删除" op="remove"/>
		<emp:button id="viewMortGuarantyBaseInfo" label="查看" op="view"/>
		<emp:button id="storageMortGuarantyBaseInfo" label="入库" op="to_storage"/>
		<emp:button id="inStorageMortGuarantyBaseInfo" label="入库" op="in_storage"/>
		<emp:button id="outStorageMortGuarantyBaseInfo" label="出库" op="out_storage"/>
		<emp:button id="exwaMortGuarantyBaseInfo" label="出库" op="exwa"/>
		<emp:button id="cargoRepl" label="货物置换" op="cargo_repl"/>
		<emp:button id="bailLad" label="保证金提货" op="bail_lad"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="ImageCheck" label="影像核对" op="ImageCheck"/>
		<emp:button id="upload" label="附件"/>
		<!-- op="ImageView" op="ImageCheck"
		<emp:button id="ImageScan" label="影像扫描"/>
		<emp:button id="ImagePrint" label="条码打印"/>	-->
	</div>

	<emp:table icollName="MortGuarantyBaseInfoList" pageMode="true" url="pageMortGuarantyBaseInfoQuery.do?menuId=${context.menuId}">
		<emp:text id="guaranty_no" label="押品编号" />
		<emp:text id="image_guaranty_no" label="影像押品编号" />
		<emp:text id="guaranty_name" label="押品名称" />
		<emp:text id="cus_id" label="出质人客户码" />
		<emp:text id="guaranty_cls" label="押品类别" dictname="STD_GUARANTY_TYPE" />
		<emp:text id="guaranty_type" label="押品类型" hidden="true"/>
		<emp:text id="guaranty_type_displayname" label="押品类型"/>
		<emp:text id="guaranty_info_status" label="押品信息状态" dictname="STD_MORT_STATE" />
		<emp:text id="manager_id" label="责任人" hidden="true"/>
		<emp:text id="manager_br_id" label="责任机构" hidden="true"/>
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="input_date" label="登记日期" />
	</emp:table>
	
	</form>
	
</body>
</html>
</emp:page>
    