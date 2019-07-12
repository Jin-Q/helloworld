<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	String task_id = request.getParameter("task_id"); 
	String dataFrom = request.getParameter("dataFrom");
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_label1 {
	vertical-align: top;
	padding-top: 4px;
	text-align: left;
	width: 400px;
}
</style>
<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">

	/*--user code begin--*/
	function doload(){
		var task_id = '<%=task_id%>';
		PspSitecheckComGr.task_id._setValue(task_id);
		convert_jkr_jyxc();
		convert_jkr_chxc();
		convert_jkrjssfbh();
		convert_jkrgzdwbh();
		convert_jkrjtzcbh();
		convert_dbr_jyxc();
		convert_dbr_chxc();
		convert_bzrjssfbh();
		convert_bzrgzdwbh();
		convert_bzrjtzcbh();
		convert_bzrjtsrbh();
		convert_bzrjkzkbh();
		convert_dzyxthzgzk();
		convert_dzybxnlpj();
		changeVisitType();
		convert_jkrjkzkbh();
		convert_jkrjtsrbh();
	}	

	function doReturn(){
		history.go(-1);
	}


	function changeVisitType(){
		var visit_type = PspSitecheckComGr.visit_type._getValue();
		
		if(visit_type=="01"){
			
			document.getElementById('jkrgrjyx').style.display="";
			document.getElementById('jkrgrxfx').style.display="none";
			document.getElementById('dbr').style.display="none";
			document.getElementById('dzyw').style.display="none";
			
			
		}else if (visit_type=="02"){
			document.getElementById('jkrgrjyx').style.display="none";
			document.getElementById('jkrgrxfx').style.display="";
			document.getElementById('dbr').style.display="none";
			document.getElementById('dzyw').style.display="none"; 
			
			
		}else if (visit_type=="03"){
			PspSitecheckComGr.cus_type._obj._renderHidden(false);
			PspSitecheckComGr.cus_type._obj._renderRequired(true);
			changeCusType();
					
		}else if (visit_type=="04"){
			document.getElementById('jkrgrjyx').style.display="none";
			document.getElementById('jkrgrxfx').style.display="none";
			document.getElementById('dbr').style.display="none";
			document.getElementById('dzyw').style.display="";
		}
	}

	function changeCusType(){
		var cust_type = PspSitecheckComGr.cus_type._getValue();
		if (cust_type=="01"){
			document.getElementById('jkrgrjyx').style.display="none";
			document.getElementById('jkrgrxfx').style.display="none";
			document.getElementById('dbr').style.display="";
			document.getElementById('dzyw').style.display="none";
			document.getElementById('dbrgskh').style.display="";
			document.getElementById('dbrgrkh').style.display="none";

		}else if(cust_type=="02"){
			document.getElementById('jkrgrjyx').style.display="none";
			document.getElementById('jkrgrxfx').style.display="none";
			document.getElementById('dbr').style.display="";
			document.getElementById('dzyw').style.display="none";
			document.getElementById('dbrgskh').style.display="none";
			document.getElementById('dbrgrkh').style.display="";
		}
	}
	
	function convert_jkr_jyxc(){
		if(PspSitecheckComGr.jkr_jyxc._getValue()=="02"){
			PspSitecheckComGr.jkr_jyxc_ycyy._obj._renderHidden(false);
			PspSitecheckComGr.jkr_jyxc_ycyy._obj._renderRequired(true);
			
		}else{
			PspSitecheckComGr.jkr_jyxc_ycyy._obj._renderHidden(true);
			PspSitecheckComGr.jkr_jyxc_ycyy._obj._renderRequired(false);
			PspSitecheckComGr.jkr_jyxc_ycyy._setValue("");
		}
	}

	function convert_jkr_chxc(){
		if(PspSitecheckComGr.jkr_chxc._getValue()=="02"){
			PspSitecheckComGr.jkr_chxc_ycyy._obj._renderHidden(false);
			PspSitecheckComGr.jkr_chxc_ycyy._obj._renderRequired(true);
			
		}else{
			PspSitecheckComGr.jkr_chxc_ycyy._obj._renderHidden(true);
			PspSitecheckComGr.jkr_chxc_ycyy._obj._renderRequired(false);
			PspSitecheckComGr.jkr_chxc_ycyy._setValue("");
		}
	}

	function convert_jkrjssfbh(){
		if(PspSitecheckComGr.jkrjssfbh._getValue()=="1"){
			PspSitecheckComGr.jkrjssfbh_xdz._obj._renderHidden(false);
			PspSitecheckComGr.jkrjssfbh_xdz._obj._renderRequired(true);
			
		}else{
			PspSitecheckComGr.jkrjssfbh_xdz._obj._renderHidden(true);
			PspSitecheckComGr.jkrjssfbh_xdz._obj._renderRequired(false);
			PspSitecheckComGr.jkrjssfbh_xdz._setValue("");
		}
	}

	function convert_jkrgzdwbh(){
		if(PspSitecheckComGr.jkrgzdwbh._getValue()=="1"){
			PspSitecheckComGr.jkrgzdwbh_xdwm._obj._renderHidden(false);
			PspSitecheckComGr.jkrgzdwbh_xdwm._obj._renderRequired(true);
			
		}else{
			PspSitecheckComGr.jkrgzdwbh_xdwm._obj._renderHidden(true);
			PspSitecheckComGr.jkrgzdwbh_xdwm._obj._renderRequired(false);
			PspSitecheckComGr.jkrgzdwbh_xdwm._setValue("");
		}
	}

	function convert_jkrjtzcbh(){
		if(PspSitecheckComGr.jkrjtzcbh._getValue()=="1"){
			PspSitecheckComGr.jkrjtzcbh_bhqk._obj._renderHidden(false);
			PspSitecheckComGr.jkrjtzcbh_bhqk._obj._renderRequired(true);
			
		}else{
			PspSitecheckComGr.jkrjtzcbh_bhqk._obj._renderHidden(true);
			PspSitecheckComGr.jkrjtzcbh_bhqk._obj._renderRequired(false);
			PspSitecheckComGr.jkrjtzcbh_bhqk._setValue("");
		}
	}

	function convert_jkrjtsrbh(){
		if(PspSitecheckComGr.jkrjtsrbh._getValue()=="1"){
			PspSitecheckComGr.jkrjtsrbh_bhqk._obj._renderHidden(false);
			PspSitecheckComGr.jkrjtsrbh_bhqk._obj._renderRequired(true);
			
		}else{
			PspSitecheckComGr.jkrjtsrbh_bhqk._obj._renderHidden(true);
			PspSitecheckComGr.jkrjtsrbh_bhqk._obj._renderRequired(false);
			PspSitecheckComGr.jkrjtsrbh_bhqk._setValue("");
		}
	}

	function convert_jkrjkzkbh(){
		if(PspSitecheckComGr.jkrjkzkbh._getValue()=="1"){
			PspSitecheckComGr.jkrjkzkbh_bhqk._obj._renderHidden(false);
			PspSitecheckComGr.jkrjkzkbh_bhqk._obj._renderRequired(true);
			
		}else{
			PspSitecheckComGr.jkrjkzkbh_bhqk._obj._renderHidden(true);
			PspSitecheckComGr.jkrjkzkbh_bhqk._obj._renderRequired(false);
			PspSitecheckComGr.jkrjkzkbh_bhqk._setValue("");
		}
	}

	function convert_dbr_jyxc(){
		if(PspSitecheckComGr.dbr_jyxc._getValue()=="02"){
			PspSitecheckComGr.dbr_jyxc_yysm._obj._renderHidden(false);
			PspSitecheckComGr.dbr_jyxc_yysm._obj._renderRequired(true);
			
		}else{
			PspSitecheckComGr.dbr_jyxc_yysm._obj._renderHidden(true);
			PspSitecheckComGr.dbr_jyxc_yysm._obj._renderRequired(false);
			PspSitecheckComGr.dbr_jyxc_yysm._setValue("");
		}
	}

	function convert_dbr_chxc(){
		if(PspSitecheckComGr.dbr_chxc._getValue()=="02"){
			PspSitecheckComGr.dbr_chxc_yysm._obj._renderHidden(false);
			PspSitecheckComGr.dbr_chxc_yysm._obj._renderRequired(true);
			
		}else{
			PspSitecheckComGr.dbr_chxc_yysm._obj._renderHidden(true);
			PspSitecheckComGr.dbr_chxc_yysm._obj._renderRequired(false);
			PspSitecheckComGr.dbr_chxc_yysm._setValue("");
		}
	}

	function convert_bzrjssfbh(){
		if(PspSitecheckComGr.bzrjssfbh._getValue()=="1"){
			PspSitecheckComGr.bzrjssfbh_xdz._obj._renderHidden(false);
			PspSitecheckComGr.bzrjssfbh_xdz._obj._renderRequired(true);
			
		}else{
			PspSitecheckComGr.bzrjssfbh_xdz._obj._renderHidden(true);
			PspSitecheckComGr.bzrjssfbh_xdz._obj._renderRequired(false);
			PspSitecheckComGr.bzrjssfbh_xdz._setValue("");
		}
	}

	function convert_bzrgzdwbh(){
		if(PspSitecheckComGr.bzrgzdwbh._getValue()=="1"){
			PspSitecheckComGr.bzrgzdwbh_xdw._obj._renderHidden(false);
			PspSitecheckComGr.bzrgzdwbh_xdw._obj._renderRequired(true);
			
		}else{
			PspSitecheckComGr.bzrgzdwbh_xdw._obj._renderHidden(true);
			PspSitecheckComGr.bzrgzdwbh_xdw._obj._renderRequired(false);
			PspSitecheckComGr.bzrgzdwbh_xdw._setValue("");
		}
	}

	function convert_bzrjtzcbh(){
		if(PspSitecheckComGr.bzrjtzcbh._getValue()=="1"){
			PspSitecheckComGr.bzrjtzcbh_bhqk._obj._renderHidden(false);
			PspSitecheckComGr.bzrjtzcbh_bhqk._obj._renderRequired(true);
			
		}else{
			PspSitecheckComGr.bzrjtzcbh_bhqk._obj._renderHidden(true);
			PspSitecheckComGr.bzrjtzcbh_bhqk._obj._renderRequired(false);
			PspSitecheckComGr.bzrjtzcbh_bhqk._setValue("");
		}
	}
	
	function convert_bzrjtsrbh(){
		if(PspSitecheckComGr.bzrjtsrbh._getValue()=="1"){
			PspSitecheckComGr.bzrjtsrbh_bhqk._obj._renderHidden(false);
			PspSitecheckComGr.bzrjtsrbh_bhqk._obj._renderRequired(true);
			
		}else{
			PspSitecheckComGr.bzrjtsrbh_bhqk._obj._renderHidden(true);
			PspSitecheckComGr.bzrjtsrbh_bhqk._obj._renderRequired(false);
			PspSitecheckComGr.bzrjtsrbh_bhqk._setValue("");
		}
	}
	
	function convert_bzrjkzkbh(){
		if(PspSitecheckComGr.bzrjkzkbh._getValue()=="1"){
			PspSitecheckComGr.bzrjkzkbh_bhqk._obj._renderHidden(false);
			PspSitecheckComGr.bzrjkzkbh_bhqk._obj._renderRequired(true);
			
		}else{
			PspSitecheckComGr.bzrjkzkbh_bhqk._obj._renderHidden(true);
			PspSitecheckComGr.bzrjkzkbh_bhqk._obj._renderRequired(false);
			PspSitecheckComGr.bzrjkzkbh_bhqk._setValue("");
		}
	}
	
	function convert_dzyxthzgzk(){
		if(PspSitecheckComGr.dzyxthzgzk._getValue()=="2"){
			PspSitecheckComGr.dzyxthzgzk_ycyy._obj._renderHidden(false);
			PspSitecheckComGr.dzyxthzgzk_ycyy._obj._renderRequired(true);
		
		}else{
			PspSitecheckComGr.dzyxthzgzk_ycyy._obj._renderHidden(true);
			PspSitecheckComGr.dzyxthzgzk_ycyy._obj._renderRequired(false);
			PspSitecheckComGr.dzyxthzgzk_ycyy._setValue("");
		}
	}

	function convert_dzybxnlpj(){
		if(PspSitecheckComGr.dzybxnlpj._getValue()=="2"){
			PspSitecheckComGr.dzybxnlpj_ycyy._obj._renderHidden(false);
			PspSitecheckComGr.dzybxnlpj_ycyy._obj._renderRequired(true);
			
		}else{
			PspSitecheckComGr.dzybxnlpj_ycyy._obj._renderHidden(true);
			PspSitecheckComGr.dzybxnlpj_ycyy._obj._renderRequired(false);
			PspSitecheckComGr.dzybxnlpj_ycyy._setValue("");
		}
	}

	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	
	<emp:form id="submitForm" action="" method="POST">
			<emp:text id="PspSitecheckComGr.task_id" label="任务号" hidden="true" defvalue="${context.task_id}" />
		<emp:gridLayout id="PspSitecheckComGrGroup" title="现场检查" maxColumn="2">
			<emp:date id="PspSitecheckComGr.check_time" label="检查时间"  required="true"/>
			<emp:text id="PspSitecheckComGr.check_addr" label="检查地点" maxlength="100"   required="true" />
			<emp:text id="PspSitecheckComGr.yjry" label="约见人员" maxlength="40" required="true" />
			<emp:select id="PspSitecheckComGr.visit_type" label="现场检查类型"  required="true"  dictname="STD_ZB_VISITGR_TYPE" onchange="changeVisitType();changeVisit()"/>	
			<emp:select id="PspSitecheckComGr.cus_type" label="客户类型"  required="false"  dictname="STD_ZB_GRCUS_TYPE" hidden="true" />	
		</emp:gridLayout>
		
		<div id="jkrgrjyx" style="display:none" >
		<emp:gridLayout id="PspSitecheckComGrGroup" title="借款人（个人经营性）的检查明细：" maxColumn="2"  >
			
			<emp:radio id="PspSitecheckComGr.jkr_jyxc" label="经营现场" disabled="true"  required="true" dictname="STD_OFFICE_WORK_STATUS" layout="false" onclick="convert_jkr_jyxc()"/>
			<emp:textarea id="PspSitecheckComGr.jkr_jyxc_ycyy" label="如异常请说明异常情况及原因说明：" required="false" hidden="true" colSpan="2"/>
			<emp:radio id="PspSitecheckComGr.jkr_chxc" label="存货现场" disabled="true"  required="true" dictname="STD_OFFICE_WORK_STATUS" layout="false" onclick="convert_jkr_chxc()"/>
			<emp:textarea id="PspSitecheckComGr.jkr_chxc_ycyy" label="如异常请说明异常情况及原因说明：" required="false" hidden="true" colSpan="2"/>
			<emp:text id="PspSitecheckComGr.jkr_bgry_sum" label="办公场所：办公人员数量" required="true" dataType="Int" />
			<emp:radio id="PspSitecheckComGr.jkr_bgcs" label="办公场所" disabled="true"  required="true" dictname="STD_ZX_FIELD_OWNER" layout="false" />
		</emp:gridLayout>
		</div>
		
		<div id="jkrgrxfx" style="display:none" >
		<emp:gridLayout id="PspSitecheckComGrGroup" title="借款人（个人消费性）的检查明细：" maxColumn="2">
		
			<emp:radio id="PspSitecheckComGr.jkrjssfbh" label="借款人居所是否发生变化" disabled="true"  required="true" dictname="STD_ZX_YES_NO" layout="false" onclick="convert_jkrjssfbh()"/>
			<emp:textarea id="PspSitecheckComGr.jkrjssfbh_xdz" label="请录入新的居所地址" required="false" hidden="true" colSpan="2"/>
			<emp:radio id="PspSitecheckComGr.jkrgzdwbh" label="借款人工作单位是否发生变化" disabled="true"  required="true" dictname="STD_ZX_YES_NO" layout="false" onclick="convert_jkrgzdwbh()"/>
			<emp:textarea id="PspSitecheckComGr.jkrgzdwbh_xdwm" label="请录入新的工作单位名称" required="false" hidden="true" colSpan="2"/>
			<emp:radio id="PspSitecheckComGr.jkrjtzcbh" label="借款人家庭资产是否变化" disabled="true"  required="true" dictname="STD_ZX_YES_NO" layout="false" onclick="convert_jkrjtzcbh()"/>
			<emp:textarea id="PspSitecheckComGr.jkrjtzcbh_bhqk" label="请录入变化情况" required="false" hidden="true" colSpan="2"/>
			<emp:radio id="PspSitecheckComGr.jkrjtsrbh" label="借款人家庭收入是否变化" disabled="true"  required="true" dictname="STD_ZX_YES_NO" layout="false" onclick="convert_jkrjtsrbh()"/>
			<emp:textarea id="PspSitecheckComGr.jkrjtsrbh_bhqk" label="请录入变化情况" required="false" hidden="true" colSpan="2"/>
			<emp:radio id="PspSitecheckComGr.jkrjkzkbh" label="借款人健康状况是否变化" disabled="true"  required="true" dictname="STD_ZX_YES_NO" layout="false" onclick="convert_jkrjkzkbh()"/>
			<emp:textarea id="PspSitecheckComGr.jkrjkzkbh_bhqk" label="请录入变化情况" required="false" hidden="true" colSpan="2"/>
		    <emp:textarea id="PspSitecheckComGr.grxfx_qtqk" label="其他情况：" required="false" hidden="false" colSpan="2"/>
		</emp:gridLayout>
		</div>
		
		<div id="dbr" style="display:none" >
		<div id="dbrgskh" style="display:none" >
		<emp:gridLayout id="PspSitecheckComGrGroup" title="公司客户" maxColumn="2">
			
			<emp:radio id="PspSitecheckComGr.dbr_jyxc" label="经营现场" disabled="true"  required="true" dictname="STD_OFFICE_WORK_STATUS" layout="false" onclick="convert_dbr_jyxc()"/>
			<emp:textarea id="PspSitecheckComGr.dbr_jyxc_yysm" label="异常情况及原因说明：" required="false" hidden="true" colSpan="2" />
			<emp:radio id="PspSitecheckComGr.dbr_chxc" label="存货现场" disabled="true"  required="true" dictname="STD_OFFICE_WORK_STATUS" layout="false" onclick="convert_dbr_chxc()"/>
			<emp:textarea id="PspSitecheckComGr.dbr_chxc_yysm" label="异常情况及原因说明：" required="false" hidden="true" colSpan="2"/>
			<emp:text id="PspSitecheckComGr.dbr_bgry" label="办公场所：办公人员数量" required="true" dataType="Int" />
			<emp:radio id="PspSitecheckComGr.dbr_bgcs" label="办公场所" disabled="true"  required="true" dictname="STD_ZX_FIELD_OWNER" layout="false" />	
		</emp:gridLayout>
		</div>
		
		<div id="dbrgrkh" style="display:none" >
		<emp:gridLayout id="PspSitecheckComGrGroup" title="个人客户" maxColumn="2">
			<emp:radio id="PspSitecheckComGr.bzrjssfbh" label="保证人居所是否发生变化" disabled="true"  required="true" dictname="STD_ZX_YES_NO" layout="false" onclick="convert_bzrjssfbh()"/>
			<emp:textarea id="PspSitecheckComGr.bzrjssfbh_xdz" label="请录入新居所地址" required="false" hidden="true" colSpan="2" />
			<emp:radio id="PspSitecheckComGr.bzrgzdwbh" label="保证人工作单位是否变化" disabled="true"  required="true" dictname="STD_ZX_YES_NO" layout="false" onclick="convert_bzrgzdwbh()"/>
			<emp:textarea id="PspSitecheckComGr.bzrgzdwbh_xdw" label="请录入新的工作单位名称" required="false" hidden="true" colSpan="2"/>
			<emp:radio id="PspSitecheckComGr.bzrjtzcbh" label="保证人家庭资产是否变化" disabled="true"  required="true" dictname="STD_ZX_YES_NO" layout="false" onclick="convert_bzrjtzcbh()"/>
			<emp:textarea id="PspSitecheckComGr.bzrjtzcbh_bhqk" label="请录入变化情况" required="false" hidden="true" colSpan="2"/>
			<emp:radio id="PspSitecheckComGr.bzrjtsrbh" label="保证人家庭收入是否变化" disabled="true"  required="true" dictname="STD_ZX_YES_NO" layout="false" onclick="convert_bzrjtsrbh()"/>
			<emp:textarea id="PspSitecheckComGr.bzrjtsrbh_bhqk" label="请录入变化情况" required="false" hidden="true" colSpan="2"/>
			<emp:radio id="PspSitecheckComGr.bzrjkzkbh" label="保证人健康状况是否变化" disabled="true"  required="true" dictname="STD_ZX_YES_NO" layout="false" onclick="convert_bzrjkzkbh()"/>
			<emp:textarea id="PspSitecheckComGr.bzrjkzkbh_bhqk" label="请录入变化情况" required="false" hidden="true" colSpan="2"/>
			<emp:textarea id="PspSitecheckComGr.dbr_qtqk" label="其他情况：" required="false" hidden="false" colSpan="2"/>
		</emp:gridLayout>
		</div>
		</div>
		
		<div id="dzyw" style="display:none" >
		<emp:gridLayout id="PspSitecheckComGrGroup" title="抵（质）押物现场检查明细：" maxColumn="2">
			
			<emp:radio id="PspSitecheckComGr.dzyxthzgzk" label="抵（质）押物形态和占管状况：" disabled="true"  required="true" dictname="STD_PSP_STATUS_TYPE" layout="false" colSpan="2" onclick="convert_dzyxthzgzk()" cssLabelClass="emp_field_label1"/>
			<emp:textarea id="PspSitecheckComGr.dzyxthzgzk_ycyy" label="异常请说明原因：" required="false" colSpan="2" hidden="true" />
			<emp:radio id="PspSitecheckComGr.dzybxnlpj" label="抵（质）押物变现能力评价：" disabled="true"  required="true" dictname="STD_PSP_STATUS_TYPE" layout="false" colSpan="2" onclick="convert_dzybxnlpj()" cssLabelClass="emp_field_label1"/>
			<emp:textarea id="PspSitecheckComGr.dzybxnlpj_ycyy" label="异常请说明原因：" required="false" colSpan="2" hidden="true" />
			<emp:radio id="PspSitecheckComGr.dzysdsfcd" label="抵（质）押物实地（物）是否被查封、冻结：" disabled="true"  required="true" dictname="STD_ZX_YES_NO" layout="false" colSpan="2" cssLabelClass="emp_field_label1" />
			<emp:radio id="PspSitecheckComGr.djjgsfyslcd" label="登记机关是否已受理查封、冻结：" disabled="true"  required="true" dictname="STD_ZX_YES_NO" layout="false" colSpan="2" cssLabelClass="emp_field_label1"/>
			<emp:date id="PspSitecheckComGr.cdsj" label="查封、冻结时间"  required="true" />
			<emp:textarea id="PspSitecheckComGr.cdyy" label="原因：" required="true" colSpan="2"/>
			<emp:text id="PspSitecheckComGr.cdsqr" label="查封、冻结申请人" maxlength="40" required="true" />
			<emp:radio id="PspSitecheckComGr.dzydbyysfbh" label="抵（质）押人的担保意愿是否发生变化：" disabled="true"  required="true" dictname="STD_ZX_YES_NO" layout="false" colSpan="2" cssLabelClass="emp_field_label1" />
			<emp:radio id="PspSitecheckComGr.dywsfcz" label="抵押物是否出租：" disabled="true"  required="true" dictname="STD_ZX_YES_NO" layout="false" colSpan="2" cssLabelClass="emp_field_label1"/>
			<emp:radio id="PspSitecheckComGr.dywczyxzqsxys" label="是否存在影响我行债权的顺利实现的风险因素：" disabled="true"  required="true" dictname="STD_ZX_YES_NO" layout="false" colSpan="2" cssLabelClass="emp_field_label1"/>
			<emp:radio id="PspSitecheckComGr.dzyszbhyxdb" label="抵（质）押物市场价值是否发生不利变化，影响担保效力：" disabled="true"  required="true" dictname="STD_ZX_YES_NO" layout="false" colSpan="2" cssLabelClass="emp_field_label1"/>
			<emp:radio id="PspSitecheckComGr.bgdzydjsfqzsf" label="保管的抵质押登记证明文件是否齐全，账实是否相符：" disabled="true"  required="true" dictname="STD_ZX_YES_NO" layout="false" colSpan="2" cssLabelClass="emp_field_label1"/>
			<emp:textarea id="PspSitecheckComGr.dzyczdqtzk" label="抵质押物存在的其他状况：" required="false" colSpan="2"/>
			
		</emp:gridLayout>
		</div>
		
		<div align="center">
			<br>
			<emp:button id="return" label="返回到列表页面" />
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

