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
			<%}else{%>
			var url = '<emp:url action="getMortGuarantyBaseInfoViewPage.do"/>?op=view&menuIdTab=mort_maintain&flag=hwdj&tab=tab&'+paramStr;
			<%}%>
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindowAgrView','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
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
	};
	function doReset(){
		page.dataGroups.MortGuarantyBaseInfoGroup.reset();
	};
	//选择押品类型
	function getReturnValueForGuarantyType(data){
		MortGuarantyBaseInfo.guaranty_type_displayname._setValue(data.label);
		MortGuarantyBaseInfo.guaranty_type._setValue(data.id);
	}
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
<emp:tabGroup mainTab="base_tab" id="mainTab" >
  <emp:tab label="抵质押信息" id="base_tab" needFlush="true" initial="true" >  
	<emp:gridLayout id="MortGuarantyBaseInfoGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="MortGuarantyBaseInfo.guaranty_no" label="押品编号" />
			<emp:text id="MortGuarantyBaseInfo.guaranty_name" label="押品名称" />
			<emp:select id="MortGuarantyBaseInfo.guaranty_cls" label="押品类别" dictname="STD_GUARANTY_TYPE" />
			<emp:select id="MortGuarantyBaseInfo.guaranty_info_status" label="押品信息状态" dictname="STD_MORT_STATE" />
			<emp:pop id="MortGuarantyBaseInfo.guaranty_type_displayname" label="押品类型"  readonly="false" url="showDicTree.do?dicTreeTypeId=MORT_TYPE&parentNodeId=Z090100" returnMethod="getReturnValueForGuarantyType" required="true" cssElementClass="emp_field_text_long"/>
			<emp:text id="MortGuarantyBaseInfo.guaranty_type" label="押品类型" required="true" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewMortGuarantyBaseInfo" label="查看" op="view"/>
	</div>

	<emp:table icollName="MortGuarantyBaseInfoList" pageMode="true" url="pageMortGuarantyBaseByCusId.do?menuId=${context.menuId}&cus_id=${context.cus_id}">
		<emp:text id="guaranty_no" label="押品编号" />
		<emp:text id="guaranty_name" label="押品名称" />
		<emp:text id="cus_id" label="出质人客户码" />
		<emp:text id="guaranty_cls" label="押品类别" dictname="STD_GUARANTY_TYPE" />
		<emp:text id="guaranty_type" label="押品类型" hidden="true"/>
		<emp:text id="guaranty_type_displayname" label="押品类型"/>
		<emp:text id="guaranty_info_status" label="押品信息状态" dictname="STD_MORT_STATE" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="input_date" label="登记日期" />
	</emp:table>
   </emp:tab>
   <emp:tab label="保证人信息" id="subTab" url="getGrtGuaranteeByCusId.do?cus_id=${context.cus_id}" initial="false" needFlush="true"/>   
  </emp:tabGroup>
</body>
</html>
</emp:page>
    