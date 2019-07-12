<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op="";
	if(context.containsKey("op")){
		op =(String)context.getDataValue("op");
	}  
	//added by yangzy 2015/04/29 需求：XD150325024，集中作业扫描岗权限改造 start
	String roles = "";
	if(context.containsKey("roles")){
		roles = (String)context.getDataValue("roles");
	} 
	String flagDA = "";
	if(roles.contains("1022") ){
		flagDA = "1";
	}
	//added by yangzy 2015/04/29 需求：XD150325024，集中作业扫描岗权限改造 end
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
		GrtGuarantee._toForm(form);
		GrtGuaranteeList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateGrtGuaranteePage() {
		var paramStr = GrtGuaranteeList._obj.getParamStr(['guar_id']);
		var cont_no = "${context.guar_cont_no}";
		if (paramStr != null) {
			var url = '<emp:url action="getGrtGuaranteeUpdatePage.do"/>?'+paramStr+'&guar_cont_no='+cont_no;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewGrtGuarantee() {
		var paramStr = GrtGuaranteeList._obj.getParamStr(['guar_id']);
		var cont_no = "${context.guar_cont_no}";
		var menuIdTab = "${context.menuIdTab}";
		var flag = "view";
		if (paramStr != null) {
			var url = '<emp:url action="getGrtGuaranteeViewPage.do"/>?menuIdTab='+menuIdTab+'&'+paramStr+'&guar_cont_no='+cont_no;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddGrtGuaranteePage() {
		var cont_no = "${context.guar_cont_no}";
		var url = '<emp:url action="getGrtGuaranteeAddPage.do"/>&guar_cont_no='+cont_no;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteGrtGuarantee() {
		var paramStr = GrtGuaranteeList._obj.getParamStr(['guar_id']);
		var cont_no = "${context.guar_cont_no}";
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
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
							alert("已删除所选择的保证人记录以及其与担保合同的关联信息！");
							window.location.reload();
						}else {
							alert("修改失败！");
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
				var url = '<emp:url action="deleteGrtGuaranteeRecord.do"/>?'+paramStr+'&guar_cont_no='+cont_no;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.GrtGuaranteeGroup.reset();
	};
	function doLoad(){
		
	}
	/*--user code begin--*/
	function doImageView(){
		var data = GrtGuaranteeList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View23');	//2.3.	客户资料查看（客户全视图）接口
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		if(image_action == "Check3133"){
			data['serno'] = GrtGuaranteeList._obj.getParamValue(['guar_id']);	//保证编码
			data['cus_id'] = GrtGuaranteeList._obj.getParamValue(['cus_id']);	//客户编号
			data['prd_id'] = 'BZRYXHD';	//业务品种
			data['prd_stage'] = '' ;	//业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
			data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		}else{
			data['serno'] = GrtGuaranteeList._obj.getParamValue(['cus_id']);	//客户资料的业务编号就填cus_id
			data['cus_id'] = GrtGuaranteeList._obj.getParamValue(['cus_id']);	//客户编号
			data['prd_id'] = 'BASIC';	//业务品种
			data['prd_stage'] = 'BZR' ;	//业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
			data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		}
		doPubImageAction(data);
	};
	function doImageCheck(){
		var data = GrtGuaranteeList._obj.getSelectedData();
		if (data != null && data !=0) {
			if( confirm("影像信息将直接归档，请确认!") ){
				ImageAction('Check3133');	//影像核对
			}
		} else {
			alert('请先选择一条记录！');
		}	
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<div align="left">
		<emp:actButton id="getAddGrtGuaranteePage" label="新增" op="add"/>
		<emp:actButton id="getUpdateGrtGuaranteePage" label="修改" op="update"/>
		<emp:actButton id="deleteGrtGuarantee" label="删除" op="remove"/>
		<emp:actButton id="viewGrtGuarantee" label="查看" op="view"/>
		<%-- <emp:actButton id="ImageView" label="影像查看" op="ImageView"/> --%>
		<!--added by yangzy 2015/04/29 需求：XD150325024，集中作业扫描岗权限改造 start-->
		<%if(flagDA.equals("1")){ //是否档案岗标志%>
		<%-- <emp:actButton id="ImageCheck" label="影像核对" op="ImageCheck"/> --%>
		<%}%>
		<!--added by yangzy 2015/04/29 需求：XD150325024，集中作业扫描岗权限改造 end-->
	</div>

	<emp:table icollName="GrtGuaranteeList" pageMode="false" url="pageGrtGuaranteeQuery.do">
		<emp:text id="guar_id" label="保证编码 " />
		<emp:text id="guar_type" label="保证形式" dictname="STD_GUAR_FORM" />
		<emp:text id="guar_amt" label="担保金额" dataType="Currency"/>
		<emp:text id="cus_id" label="保证人客户码" />
		<emp:text id="cus_id_displayname" label="保证人客户名称" />
		<emp:select id="is_spadd" label="是否为追加担保" dictname="STD_ZX_YES_NO" />
		
	</emp:table>
	
</body>
</html>
</emp:page>
    