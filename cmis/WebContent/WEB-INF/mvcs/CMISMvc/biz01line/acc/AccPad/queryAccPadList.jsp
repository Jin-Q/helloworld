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
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<script type="text/javascript">

	/*--user code begin--*/
	function doQuery(){
		var form = document.getElementById('queryForm');
		AccPad._toForm(form);
		AccPadList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateAccPadPage() {
		var paramStr = AccPadList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getAccPadUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewAccPad() {
		var paramStr = AccPadList._obj.getParamStr(['bill_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getAccPadViewPage.do"/>?'+paramStr+'&biz_type='+'<%=biz_type%>';
			url = EMPTools.encodeURI(url); 
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddAccPadPage() {
		var url = '<emp:url action="getAccPadAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteAccPad() {
		var paramStr = AccPadList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteAccPadRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.AccPadGroup.reset();
	};


	function returnCus(data){
		AccPad.cus_id._setValue(data.cus_id._getValue());
		AccPad.cus_name._setValue(data.cus_name._getValue());
	};

    function onload(){
    	var accp_status = AccPad.accp_status._obj.element.options;
    	for(var i=accp_status.length-1;i>=0;i--){	
    		if(accp_status[i].value!="1" && accp_status[i].value!="9" && accp_status[i].value!="" ){
    			accp_status.remove(i);
			}
    	}
    };

	function doImageView(){
		var data = AccPadList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View25');	//业务资料查看
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = AccPadList._obj.getParamValue(['fount_serno']);	//业务编号
		data['cus_id'] = AccPadList._obj.getParamValue(['cus_id']);	//客户码
		data['prd_id'] = AccPadList._obj.getParamValue(['prd_id']);	//业务品种
		data['prd_stage'] = 'DHTZ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};

	/** 导出excel **/
	function doExcelSDuty(){
		var form = document.getElementById("queryForm");
		AccPad._toForm(form);
		form.submit();
	}
	
	/*--user code end--*/
	/*****2019-03-01 jiangcuihua 附件上传  start******/
	function doUpload(){
		var paramStr = AccPadList._obj.getParamValue(['fount_serno']);
		if (paramStr!=null) {
			var url = '<emp:url action="getUploadInfoPage.do"/>?file_type=08&serno='+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	}
	/*****2019-03-01 jiangcuihua 附件上传  end******/
</script>
</head>
<body class="page_content" onload="onload()">
	<emp:form  method="POST" action="accPadExpBatchToExcel.do" id="queryForm">
		<emp:gridLayout id="AccPadGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="AccPad.bill_no" label="借据编号" />
			<emp:text id="AccPad.cont_no" label="合同编号" />	       
			<emp:pop id="AccPad.cus_name" label="客户名称"  buttonLabel="选择" url="queryAllCusPop.do?returnMethod=returnCus" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
			<emp:select id="AccPad.pad_type" label="垫款种类" dictname="STD_ZB_PAD_TYPE"/>
			<emp:select id="AccPad.five_class" label="五级分类" dictname="STD_ZB_FIVE_SORT"/>
		    <emp:select id="AccPad.accp_status" label="台账状态" dictname="STD_ZB_ACC_TYPE"/>	        
		    <emp:text id="AccPad.cus_id" label="客户码"  hidden="true" />
		</emp:gridLayout>
	</emp:form>

	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewAccPad" label="查看" op="view"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="excelSDuty" label="导出" op="putout"/>
		<emp:button id="upload" label="附件"/> 
	</div>

	<emp:table icollName="AccPadList" pageMode="true" url="pageAccPadQuery.do" reqParams="biz_type=${context.biz_type}">
		<emp:text id="fount_serno" label="原业务编号" hidden="true"/>
		<emp:text id="prd_id" label="产品编号" hidden="true"/>
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="belg_line" label="客户条线" dictname="STD_ZB_BUSILINE" hidden="true"/>
		<emp:text id="pad_type" label="垫款种类" dictname="STD_ZB_PAD_TYPE"/>
		<emp:text id="pad_cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="pad_amt" label="垫款金额" dataType="Currency"/>
		<emp:text id="pad_bal" label="垫款余额" dataType="Currency"/>
		<emp:text id="five_class" label="五级分类" dictname="STD_ZB_FIVE_SORT"/>
		<emp:text id="manager_br_id_displayname" label="管理机构" />		
		<emp:text id="accp_status" label="台账状态" dictname="STD_ZB_ACC_TYPE"/>
		<emp:text id="serno" label="业务编号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    