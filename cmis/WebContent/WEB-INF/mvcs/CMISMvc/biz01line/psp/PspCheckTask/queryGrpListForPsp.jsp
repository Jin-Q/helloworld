<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
	}
	// added by yangzy 2014/05/04  扫描岗增加影像扫描功能 start
	String viewfrom = "";
	if(context.containsKey("viewfrom")){
		viewfrom = (String)context.getDataValue("viewfrom");
	} 
	// added by yangzy 2014/05/04  扫描岗增加影像扫描功能 end
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<script type="text/javascript">
function doViewcus() {
	var paramStr = GrpList._obj.getParamStr(['task_id']);
	if (paramStr != null) {
		var cus_id = GrpList._obj.getParamValue(['cus_id']);
		var url = '<emp:url action="getCusViewPage.do"/>&cusId='+cus_id;
		url=EMPTools.encodeURI(url);  
      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	} else {
		alert('请先选择一条记录！');
	}
};
/*function doCheckgrp(){
	var paramStr = GrpList._obj.getParamStr(['task_id']);
	if (paramStr != null) {
		var cus_id = GrpList._obj.getParamValue(['cus_id']);
		var url = '<emp:url action="getGrpCheckTabHelp.do"/>?'+paramStr+'&cus_id='+cus_id+'&op=update';
		url=EMPTools.encodeURI(url);  
      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	} else {
		alert('请先选择一条记录！');
	}
}*/
function doCheckgrp(){
	var paramStr = GrpList._obj.getParamStr(['task_id']);
	if (paramStr != null) {
		var approve_status = GrpList._obj.getParamValue(['approve_status']);
		if(approve_status!='000'){
			alert('该笔检查申请已经提交流程不能修改！');
			return;
		}
		var url = '<emp:url action="getPspCheckTaskUpdatePage.do"/>?'+paramStr+'&op=update';
		url=EMPTools.encodeURI(url);  
      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	} else {
		alert('请先选择一条记录！');
	}
}

function doViewgrp(){
	// modified by yangzy 2014/05/04  扫描岗增加影像扫描功能 start
	var paramStr = GrpList._obj.getParamStr(['task_id']);
	var viewfrom = '<%=viewfrom%>';
	if (paramStr != null) {
		var url = '<emp:url action="getPspCheckTaskViewPage.do"/>?'+paramStr+'&op=view&openType=grp&viewfrom='+viewfrom;
		url=EMPTools.encodeURI(url);  
      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	} else {
		alert('请先选择一条记录！');
	}
	// modified by yangzy 2014/05/04  扫描岗增加影像扫描功能 end
}

/*function doViewgrp(){
	var paramStr = GrpList._obj.getParamStr(['task_id']);
	if (paramStr != null) {
		var cus_id = GrpList._obj.getParamValue(['cus_id']);
		var url = '<emp:url action="getGrpCheckTabHelp.do"/>?'+paramStr+'&cus_id='+cus_id+'&op=view';
		url=EMPTools.encodeURI(url);  
      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	} else {
		alert('请先选择一条记录！');
	}
}*/
	
	/*** 影像部分操作按钮begin ***/
	function doImageScan(){
		var data = GrpList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('Scan26');	//贷后资料扫描
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function doImageView(){
		var data = GrpList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View27');	//贷后资料查看
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function doImageCheck(){
		var data = GrpList._obj.getSelectedData();
		if (data != null && data !=0) {
			if( confirm("影像信息将直接归档，请确认!") ){
				ImageAction('Check3134');	//贷后资料归档
			}
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function doImagePrint(){
		var data = GrpList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('Print');	//条码打印
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = GrpList._obj.getParamValue(['task_id']);	//客户资料的业务编号就填cus_id
		data['cus_id'] = GrpList._obj.getParamValue(['cus_id']);	//客户编号
		data['prd_id'] = 'POSTLOAN';	//业务品种
		data['prd_stage'] = 'DHTZ' ;	//业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*** 影像部分操作按钮end ***/
</script>
</head>
<body class="page_content" >
	<emp:button id="viewcus" label="查看成员信息" op="view" mousedownCss="button80" mouseoutCss="button80" mouseoverCss="button80" mouseupCss="button80"/>
	<%if(!op.equals("view")){ %>
	<emp:button id="checkgrp" label="检查成员信息" op="update" mousedownCss="button80" mouseoutCss="button80" mouseoverCss="button80" mouseupCss="button80"/>
	<%} %>
	<emp:button id="viewgrp" label="查看成员检查信息" op="view" mousedownCss="button100" mouseoutCss="button100" mouseoverCss="button100" mouseupCss="button100"/>
	
		<emp:button id="ImageScan" label="影像扫描" op="ImageScan"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="ImageCheck" label="影像核对" op="ImageCheck"/>
		<emp:button id="ImagePrint" label="条码打印" op="ImagePrint"/>
	<br>
	<emp:table icollName="GrpList" pageMode="false" url="">
		<emp:text id="task_id" label="成员检查任务号" />
		<emp:text id="cus_id" label="成员客户码" />
		<emp:text id="cus_id_displayname" label="成员客户名称" />
		<emp:text id="grp_corre_type" label="关联(集团)关联关系类型" dictname="STD_ZB_GROUP_TYPE"/>
		<emp:text id="grp_no" label="集团编号" />
		<emp:text id="grp_name" label="集团名称" />
		<emp:text id="approve_status" label="申请状态" hidden="true"/>
	</emp:table>
	<br>
</body>
</html>
</emp:page>
    