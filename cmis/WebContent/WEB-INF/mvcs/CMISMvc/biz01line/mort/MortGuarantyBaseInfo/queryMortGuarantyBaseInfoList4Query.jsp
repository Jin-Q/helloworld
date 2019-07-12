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
	
	function getAgrView(data) {
		var paramStr = MortGuarantyBaseInfoList._obj.getParamStr(['guaranty_no']);
		if (paramStr != null) {
			getAgrInfo(data);
			<%if(menuId.equals("hwdj")){%>
			//flag=hwdj控制返回按钮
			var url = '<emp:url action="getMortGuarantyBaseInfoViewPage.do"/>?op=view&menuIdTab=mort_maintain&flag=hwdj&'+agrInfo+'&'+paramStr;
			<%}else if(menuId.equals("hwgl")){%>
			var url = '<emp:url action="getMortGuarantyBaseInfoViewPage.do"/>?op=view&flag=hwgl&'+agrInfo+'&'+paramStr;
			<%}else if("storage".equals(menuId)||"temp_storage".equals(menuId)){ //"已入库"、"临时出库"查看 %>
			var url = '<emp:url action="getMortGuarantyBaseInfoViewPage.do"/>?op=view&'+paramStr+'&menuIdTab=mort_maintain';
			<%}else{%>
			var url = '<emp:url action="getMortGuarantyBaseInfoViewPage.do"/>?op=view&'+paramStr+"&menuIdTab=mort_maintain";
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

	function doReset(){
		page.dataGroups.MortGuarantyBaseInfoGroup.reset();
	};

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
	/*** 影像部分操作按钮end ***/
	
	function returnCus(data){
		MortGuarantyBaseInfo.cus_id._setValue(data.cus_id._getValue());
		MortGuarantyBaseInfo.cus_name._setValue(data.cus_name._getValue());
	};

	/**modified by lisj 2015-7-24  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更 begin**/
	/**add by lisj  2015-3-10  需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能 begin**/
	function doPrintln(){	
		var paramStr = MortGuarantyBaseInfoList._obj.getParamStr(['guaranty_no']);
		if (paramStr != null) {
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("异步调用通讯发生异常！");
						return;
					}
					var flag=jsonstr.flag;
					if(flag =="success"){
						var url = '<emp:url action="getPrintPage.do"/>?'+paramStr+"&print_type=00";
						url = EMPTools.encodeURI(url);
						var param = 'height=356, width=365, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
						window.open(url,'newWindow4MGB',param);
					}else{
						alert("清空原表报数据失败！");
					}
				}	
			};
			var handleFailure = function(o){ 
				alert("清空原表报数据发生异常，请联系管理员");
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var resetUrl ='<emp:url action="resetRcInfo.do"/>?'+paramStr+"&print_type=00";
			resetUrl = EMPTools.encodeURI(resetUrl);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',resetUrl, callback);
		} else {
			alert('请先选择一条记录!');
		}
	};
	/**add by lisj  2015-3-10  需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能 end**/
	/**modified by lisj 2015-7-24  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更 end**/
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
			<emp:pop id="MortGuarantyBaseInfo.cus_id" label="出质人客户码" buttonLabel="选择" url="queryAllCusPop.do?returnMethod=returnCus"/>
			<%-- <emp:pop id="MortGuarantyBaseInfo.cus_name" label="出质人名称" buttonLabel="选择" url="queryAllCusPop.do?returnMethod=returnCus"/> --%>
			<emp:text id="MortGuarantyBaseInfo.cus_name" label="出质人名称" hidden="true" />
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
		<emp:button id="viewMortGuarantyBaseInfo" label="查看" op="view"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="ImageCheck" label="影像核对" op="ImageCheck"/>
		<emp:button id="println" label="封面打印" op="println"/>
		<!-- op="ImageView" op="ImageCheck"
		<emp:button id="ImageScan" label="影像扫描"/>
		<emp:button id="ImagePrint" label="条码打印"/>	-->
		<emp:button id="upload" label="附件"/>
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
    