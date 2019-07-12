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
	
function doQuery(){
	var form = document.getElementById('queryForm');
	IqpLoanApp._toForm(form);
	IqpLoanAppList._obj.ajaxQuery(null,form);
};

function doViewIqpLoanApp() {
	var paramStr = IqpLoanAppList._obj.getParamStr(['serno','iqpFlowHis']); 
	var prd_id = IqpLoanAppList._obj.getParamValue(['prd_id']);
	var appStatus = IqpLoanAppList._obj.getParamValue(['approve_status']);
	if (paramStr != null) {
		if(prd_id == 300021 || prd_id == 300020){//银行承兑贴现，商业承兑贴现
			var url = '<emp:url action="getIqpLoanAppForDiscViewPage.do"/>?op=view&'+paramStr+'&flg=${context.flg}&biz_type='+'<%=biz_type %>'+'&approve_status='+appStatus;
		}else{
			var url = '<emp:url action="getIqpLoanAppViewPage.do"/>?op=view&'+paramStr+'&flg=${context.flg}&biz_type='+'<%=biz_type %>'+'&approve_status='+appStatus;
		}
		url = EMPTools.encodeURI(url);
		window.location = url;
	} else {
		alert('请先选择一条记录！');
	}
};

   function doReset(){
	  page.dataGroups.IqpLoanAppGroup.reset();
   };
   function returnPrdId(data){
		IqpLoanApp.prd_id._setValue(data.id);
		IqpLoanApp.prd_id_displayname._setValue(data.label); 
	};
   function returnCus(data){
	   IqpLoanApp.cus_id._setValue(data.cus_id._getValue());
	   IqpLoanApp.cus_name._setValue(data.cus_name._getValue());
   };
	
	/*--user code begin--*/
	function doImageView(){
		var data = IqpLoanAppList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View25');	//业务资料查看
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = IqpLoanAppList._obj.getParamValue(['serno']);	//业务编号
		data['cus_id'] = IqpLoanAppList._obj.getParamValue(['cus_id']);	//客户码
		data['prd_id'] = IqpLoanAppList._obj.getParamValue(['prd_id']);	//业务品种
		data['prd_stage'] = 'YWSQ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*--user code end--*/
	/*****2019-03-01 jiangcuihua 附件上传  start******/
	function doUpload(){
		var paramStr = IqpLoanAppList._obj.getParamValue(['serno']);
		if (paramStr!=null) {
			var url = '<emp:url action="getUploadInfoPage.do"/>?file_type=05&serno='+paramStr;
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

	<emp:gridLayout id="IqpLoanAppGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpLoanApp.serno" label="业务流水号" />
			<emp:pop id="IqpLoanApp.cus_name" label="客户名称" buttonLabel="选择" url="queryAllCusPop.do?returnMethod=returnCus" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no" /> 
			<emp:select id="IqpLoanApp.approve_status" label="申请状态" dictname="WF_APP_STATUS" /> 
	        <emp:pop id="IqpLoanApp.prd_id_displayname" label="产品名称" url="showPrdTreeDetails.do?bizline=BL100,BL200,BL300,BL400,BL500" returnMethod="returnPrdId" />
	        <emp:text id="IqpLoanApp.prd_id" label="产品编号"  hidden="true" />
	        <emp:select id="IqpLoanApp.biz_type" label="业务模式" dictname="STD_BIZ_TYPE" defvalue="${context.biz_type}" hidden="true" />
	        <emp:text id="IqpLoanApp.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewIqpLoanApp" label="查看" op="view"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="upload" label="附件"/>
	</div>

	<emp:table icollName="IqpLoanAppList" pageMode="true" url="pageIqpLoanAppHistoryQuery.do" reqParams="biz_type=${context.biz_type}"> 
		<emp:text id="serno" label="业务流水号" />
		<emp:text id="cus_id" label="客户码" />		
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="prd_id_displayname" label="产品名称" />
		<emp:text id="assure_main" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" />
		<emp:text id="apply_cur_type" label="币种" dictname="STD_ZX_CUR_TYPE"/> 
		<emp:text id="apply_amount" label="申请金额" dataType="Currency"/>
		<emp:text id="apply_date" label="申请日期" />
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:select id="approve_status" label="申请状态" dictname="WF_APP_STATUS"/>
		<emp:select id="prd_id" label="产品编号" hidden="true"/>
		<emp:text id="iqpFlowHis" label="业务审批标识" hidden="true" defvalue="have"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    