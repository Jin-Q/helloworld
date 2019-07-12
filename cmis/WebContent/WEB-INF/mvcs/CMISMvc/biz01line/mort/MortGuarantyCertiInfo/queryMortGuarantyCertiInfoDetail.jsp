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
String act = "";
if(context.containsKey("act")){
	act = (String)context.getDataValue("act");
}
String guaranty_no = request.getParameter("guaranty_no");
request.setAttribute("canwrite","");
String exwa = "";
if(context.containsKey("exwa")){
	exwa = (String)context.getDataValue("exwa");
}
%>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function convert(){
		if(MortGuarantyCertiInfo.warrant_cls._getValue()=="1"){
			MortGuarantyCertiInfo.warrant_type_other._obj._renderHidden(true);
			MortGuarantyCertiInfo.warrant_type_other._obj._renderRequired(false);
			MortGuarantyCertiInfo.warrant_type_con._obj._renderHidden(false);
			MortGuarantyCertiInfo.warrant_type_con._obj._renderRequired(true);
		}else{
			MortGuarantyCertiInfo.warrant_type_other._obj._renderHidden(false);
			MortGuarantyCertiInfo.warrant_type_other._obj._renderRequired(true);
			MortGuarantyCertiInfo.warrant_type_con._obj._renderHidden(true);
			MortGuarantyCertiInfo.warrant_type_con._obj._renderRequired(false);
		}
	}
	function doset(){
		MortGuarantyCertiInfo.warrant_type._setValue(MortGuarantyCertiInfo.warrant_type_other._getValue());
	}
	function doset1(){
		MortGuarantyCertiInfo.warrant_type._setValue(MortGuarantyCertiInfo.warrant_type_con._getValue());
	}
	function doLoad(){
		convert();
		<%if(exwa.equals("exwa")){ %>
		var warrant_state = '${context.warrant_state}';
		MortGuarantyCertiInfo.warrant_state._setValue(warrant_state);
		<%}%>
	}	
	//选择保管机构信息返回方法
	function getOrgIDKeep(data){
		MortGuarantyCertiInfo.keep_org_no._setValue(data.organno._getValue());
		MortGuarantyCertiInfo.keep_org_no_displayname._setValue(data.organname._getValue());
	};	
	//选择经办机构信息返回方法
	function getOrgIDHand(data){
		MortGuarantyCertiInfo.hand_org_no._setValue(data.organno._getValue());
		MortGuarantyCertiInfo.hand_org_no_displayname._setValue(data.organname._getValue());
	};		
    function doReturn() {
		var guaranty_no = MortGuarantyCertiInfo.guaranty_no._getValue();
        var menuId='<%=menuId%>';
		var act = '<%=act%>'
		<%if(!"mort_maintain".equals(menuId)&&!"hwdj".equals(menuId)&&!"hwgl".equals(menuId)){%>
		var url = '<emp:url action="queryMortGuarantyCertiInfoList.do"/>?menuId='+menuId+'&act='+act;
		<%}else{%>
		var url = '<emp:url action="queryMortGuarantyCertiInfoList.do"/>?guaranty_no='+guaranty_no;
		<%}%>
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function doClose(){
		window.close();
	}
	/*--user code end--*/
	
</script>
<style type="text/css">

/************************ 下拉框(empext:select)的样式 **************************/
	/************ 下拉框(select)普通状态下的样式 ****************/
.emp_field_input {
	   border: 1px solid #b7b7b7;
	  text-align:left;
	  width:250px;
};
.emp_field_select_longinput { 
	display: inline;
	border-width: 1px;
	width: 250px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}
.emp_field_readonly .emp_field_select_longinput {
	display: none;
}
.emp_field_readonly .emp_field_select_longinput {
	display: inline;
	width: 250px;
	border-color: #b7b7b7;
}
</style>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="addMortGuarantyCertiInfoRecord.do" method="POST">
		
		<emp:gridLayout id="MortGuarantyCertiInfoGroup" title="抵质押物权证信息" maxColumn="2">
			<emp:text id="MortGuarantyCertiInfo.guaranty_no" label="押品编号" maxlength="40" required="true" hidden="true" defvalue="<%=guaranty_no %>"/>
			<emp:radio id="MortGuarantyCertiInfo.warrant_cls" label="权证类别" required="true" dictname="STD_WARRANT_TYPE" layout="false" onclick="convert()" defvalue="1" disabled="true"/>
			<emp:text id="MortGuarantyCertiInfo.warrant_type" label="权证类型" required="true" hidden="true"/>
			<emp:select id="MortGuarantyCertiInfo.warrant_type_other" label="其他权证类型" dictname="STD_OTHER_WARRANT_TYPE" onblur="doset()" colSpan="2" cssFakeInputClass="emp_field_select_longinput"/>
			<emp:select id="MortGuarantyCertiInfo.warrant_type_con" label="权利证明类型" dictname="STD_WRR_PROVE_TYPE" onblur="doset1()" colSpan="2" cssFakeInputClass="emp_field_select_longinput"/>
			<emp:select id="MortGuarantyCertiInfo.is_main_warrant" label="是否主权证" required="true" dictname="STD_ZX_YES_NO" colSpan="2" defvalue="2" readonly="true"/>
			<emp:text id="MortGuarantyCertiInfo.warrant_no" label="权证编号" maxlength="100" required="true" />
			<emp:text id="MortGuarantyCertiInfo.warrant_name" label="权证名称" maxlength="40" required="true" />
			<emp:text id="MortGuarantyCertiInfo.warrant_appro_unit" label="权利凭证核发单位" maxlength="100" required="false" />
			<emp:date id="MortGuarantyCertiInfo.warrant_appro_date" label="权利凭证核发日期" required="false" />
			<emp:text id="MortGuarantyCertiInfo.warrant_trem" label="权利凭证期限" maxlength="10" />
			<emp:select id="MortGuarantyCertiInfo.warrant_state" label="权证状态" required="true" dictname="STD_WARRANT_STATUS" readonly="true" defvalue="1" colSpan="2"/>
			<emp:pop id="MortGuarantyCertiInfo.keep_org_no_displayname" label="保管机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgIDKeep" cssElementClass="emp_pop_common_org" required="true"/>
			<emp:pop id="MortGuarantyCertiInfo.hand_org_no_displayname" label="经办机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgIDHand" cssElementClass="emp_pop_common_org" required="true"/>
			<emp:text id="MortGuarantyCertiInfo.keep_org_no" label="保管机构" maxlength="10" required="true" hidden="true"/>
			<emp:text id="MortGuarantyCertiInfo.hand_org_no" label="保管机构" maxlength="10" required="true" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			
		
		<%if(exwa.equals("exwa")){ %>
		<emp:button id="close" label="关闭"/>
		<%}else{%>
		<emp:button id="return" label="返回"/>
		<%} %>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

