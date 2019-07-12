<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>

<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<script type="text/javascript">
	/*--user code begin--*/
	function doReturnMethod(){
		var data = ImageGuaranteeNoList._obj.getSelectedData();
		if (data != null && data.length !=0) {
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin.${context.returnMethod}(data[0])");
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doSelect(){
		doReturnMethod();
	}
	function doCancel(){
		window.close();
	};

	/*** 影像部分操作按钮begin ***/
	function doImageView(){
		var data = ImageGuaranteeNoList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View23');	//2.3.	客户资料查看（客户全视图）接口
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = ImageGuaranteeNoList._obj.getParamValue(['PLEDGE_NO']);	//影像押品编号
		data['cus_id'] = ImageGuaranteeNoList._obj.getParamValue(['cus_id']);	//客户编号
		data['prd_id'] = 'ASSURE';	//业务品种
		/*** 押品影像操作时，prd_stage传影像押品编号 ***/
		data['prd_stage'] = ImageGuaranteeNoList._obj.getParamValue(['PLEDGE_NO']);	//业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*** 影像部分操作按钮end ***/
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		ImageGuaranteeNo._toForm(form);
		ImageGuaranteeNoList._obj.ajaxQuery_noPage(null,form);
	};
	
	function doReset(){
		page.dataGroups.ImageGuaranteeNoGroup.reset();
	};

	function returnCus(data){
		ImageGuaranteeNo.cus_id._setValue(data.cus_id._getValue());
    };
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" >
	<form  method="POST" action="#" id="queryForm">
	</form>
	
	<emp:gridLayout id="ImageGuaranteeNoGroup" title="输入查询条件" maxColumn="2">
		<emp:pop id="ImageGuaranteeNo.cus_id" label="客户码"  url="queryAllCusPop.do?returnMethod=returnCus" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<emp:returnButton id="s1" label="选择返回"/>
	<%-- <emp:button id="ImageView" label="影像查看"/> --%>
	<emp:table icollName="ImageGuaranteeNoList" pageMode="false" url="pageImageGuaranteeNoAction.do" >
		<emp:text id="cus_id" label="客户码" />
		<%-- <emp:text id="PLEDGE_NO" label="影像押品编号" /> --%>
	</emp:table>
	<div align="left">
 		<emp:returnButton id="s2" label="选择返回"/>
		<emp:button id="cancel" label="关闭" />
	</div>
</body>
</html>
</emp:page>