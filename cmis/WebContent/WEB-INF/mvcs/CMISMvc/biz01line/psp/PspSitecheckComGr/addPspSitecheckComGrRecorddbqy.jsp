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
<script type="text/javascript">

	/*--user code begin--*/
	function doload(){
		var task_id = '<%=task_id%>';
		PspSitecheckComGr.task_id._setValue(task_id);

		changeCusType();
		convert_dbr_jyxc();
		convert_dbr_chxc();
		convert_bzrjssfbh();
		convert_bzrgzdwbh();
		convert_bzrjtzcbh();
		
		convert_bzrjtsrbh();
		convert_bzrjkzkbh();
		
	}

	function doSub(){
		var form = document.getElementById("submitForm");
		if(PspSitecheckComGr._checkAll()){
			PspSitecheckComGr._toForm(form); 
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
						alert("保存成功!");
						window.location.reload();
						doReturn();
					}else {
						alert("保存异常!"); 
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
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	};	

	function doReturn(){
		window.close();
	}

	function changeCusType(){
		var cust_type = PspSitecheckComGr.cus_type._getValue();
		if (cust_type=="01"){
			document.getElementById('dbrgskh').style.display="";
			document.getElementById('dbrgrkh').style.display="none";

			PspSitecheckComGr.task_id._obj._renderRequired(true);
			PspSitecheckComGr.check_time._obj._renderRequired(true);
			PspSitecheckComGr.check_addr._obj._renderRequired(true);
			PspSitecheckComGr.yjry._obj._renderRequired(true);
			PspSitecheckComGr.visit_type._obj._renderRequired(true);
			
			PspSitecheckComGr.dbr_jyxc._obj._renderRequired(true);
			PspSitecheckComGr.dbr_jyxc_yysm._obj._renderRequired(true);
			PspSitecheckComGr.dbr_chxc._obj._renderRequired(true);
			PspSitecheckComGr.dbr_chxc_yysm._obj._renderRequired(true);
			PspSitecheckComGr.dbr_bgry._obj._renderRequired(true);
			PspSitecheckComGr.dbr_bgcs._obj._renderRequired(true);
			PspSitecheckComGr.bzrjssfbh._obj._renderRequired(false);
			PspSitecheckComGr.bzrjssfbh_xdz._obj._renderRequired(false);
			PspSitecheckComGr.bzrgzdwbh._obj._renderRequired(false);
			PspSitecheckComGr.bzrgzdwbh_xdw._obj._renderRequired(false);
			PspSitecheckComGr.bzrjtzcbh._obj._renderRequired(false);
			PspSitecheckComGr.bzrjtzcbh_bhqk._obj._renderRequired(false);
			PspSitecheckComGr.bzrjtsrbh._obj._renderRequired(false);
			PspSitecheckComGr.bzrjtsrbh_bhqk._obj._renderRequired(false);
			PspSitecheckComGr.bzrjkzkbh._obj._renderRequired(false);
			PspSitecheckComGr.bzrjkzkbh_bhqk._obj._renderRequired(false);
			PspSitecheckComGr.dbr_qtqk._obj._renderRequired(false);
			
			PspSitecheckComGr.cus_type._obj._renderRequired(true);
			
		}else if(cust_type=="02"){
			document.getElementById('dbrgskh').style.display="none";
			document.getElementById('dbrgrkh').style.display="";
			
			PspSitecheckComGr.task_id._obj._renderRequired(true);
			PspSitecheckComGr.check_time._obj._renderRequired(true);
			PspSitecheckComGr.check_addr._obj._renderRequired(true);
			PspSitecheckComGr.yjry._obj._renderRequired(true);
			PspSitecheckComGr.visit_type._obj._renderRequired(true);
			
			PspSitecheckComGr.dbr_jyxc._obj._renderRequired(false);
			PspSitecheckComGr.dbr_jyxc_yysm._obj._renderRequired(false);
			PspSitecheckComGr.dbr_chxc._obj._renderRequired(false);
			PspSitecheckComGr.dbr_chxc_yysm._obj._renderRequired(false);
			PspSitecheckComGr.dbr_bgry._obj._renderRequired(false);
			PspSitecheckComGr.dbr_bgcs._obj._renderRequired(false);
			PspSitecheckComGr.bzrjssfbh._obj._renderRequired(true);
			PspSitecheckComGr.bzrjssfbh_xdz._obj._renderRequired(true);
			PspSitecheckComGr.bzrgzdwbh._obj._renderRequired(true);
			PspSitecheckComGr.bzrgzdwbh_xdw._obj._renderRequired(true);
			PspSitecheckComGr.bzrjtzcbh._obj._renderRequired(true);
			PspSitecheckComGr.bzrjtzcbh_bhqk._obj._renderRequired(true);
			PspSitecheckComGr.bzrjtsrbh._obj._renderRequired(true);
			PspSitecheckComGr.bzrjtsrbh_bhqk._obj._renderRequired(true);
			PspSitecheckComGr.bzrjkzkbh._obj._renderRequired(true);
			PspSitecheckComGr.bzrjkzkbh_bhqk._obj._renderRequired(true);
			PspSitecheckComGr.dbr_qtqk._obj._renderRequired(false);
			
			PspSitecheckComGr.cus_type._obj._renderRequired(true);
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

	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	
	<emp:form id="submitForm" action="updatePspSitecheckComGrRecordFortc.do" method="POST">
			<emp:text id="PspSitecheckComGr.cus_id" label="客户号" hidden="true" defvalue="${context.cus_id}" />
			<emp:text id="PspSitecheckComGr.pk_id" label="主键" required="false" hidden="true"/>
			<emp:text id="PspSitecheckComGr.task_id" label="任务号" hidden="true" defvalue="${context.task_id}" />
		<emp:gridLayout id="PspSitecheckComGrGroup" title="现场检查" maxColumn="2">
			<emp:date id="PspSitecheckComGr.check_time" label="检查时间"  required="true"/>
			<emp:text id="PspSitecheckComGr.check_addr" label="检查地点" maxlength="100"   required="true" />
			<emp:text id="PspSitecheckComGr.yjry" label="约见人员" maxlength="40" required="true" />
			<emp:select id="PspSitecheckComGr.visit_type" label="现场检查类型"  required="true"  dictname="STD_ZB_VISITGR_TYPE" readonly="true" defvalue="03"/>	
			<emp:select id="PspSitecheckComGr.cus_type" label="客户类型"  required="true"  dictname="STD_ZB_GRCUS_TYPE"  onchange="changeCusType()"/>	
			<emp:text id="PspSitecheckComGr.cus_id_displayname" label="保证人名称" required="false" readonly="true" />
		</emp:gridLayout>
		
		<div id="dbrgskh" style="display:none" >
		<emp:gridLayout id="PspSitecheckComGrGroup" title="公司客户" maxColumn="2">
			
			<emp:radio id="PspSitecheckComGr.dbr_jyxc" label="经营现场" required="true" dictname="STD_OFFICE_WORK_STATUS" layout="false" onclick="convert_dbr_jyxc()"/>
			<emp:textarea id="PspSitecheckComGr.dbr_jyxc_yysm" label="异常情况及原因说明：" required="false" hidden="true" colSpan="2" />
			<emp:radio id="PspSitecheckComGr.dbr_chxc" label="存货现场" required="true" dictname="STD_OFFICE_WORK_STATUS" layout="false" onclick="convert_dbr_chxc()"/>
			<emp:textarea id="PspSitecheckComGr.dbr_chxc_yysm" label="异常情况及原因说明：" required="false" hidden="true" colSpan="2"/>
			<emp:text id="PspSitecheckComGr.dbr_bgry" label="办公场所：办公人员数量" required="true" dataType="Int" />
			<emp:radio id="PspSitecheckComGr.dbr_bgcs" label="办公场所" required="true" dictname="STD_ZX_FIELD_OWNER" layout="false" />	
		</emp:gridLayout>
		</div>
		
		<div id="dbrgrkh" style="display:none" >
		<emp:gridLayout id="PspSitecheckComGrGroup" title="个人客户" maxColumn="2">
			<emp:radio id="PspSitecheckComGr.bzrjssfbh" label="保证人居所是否发生变化" required="true" dictname="STD_ZX_YES_NO" layout="false" onclick="convert_bzrjssfbh()"/>
			<emp:textarea id="PspSitecheckComGr.bzrjssfbh_xdz" label="请录入新居所地址" required="false" hidden="true" colSpan="2" />
			<emp:radio id="PspSitecheckComGr.bzrgzdwbh" label="保证人工作单位是否变化" required="true" dictname="STD_ZX_YES_NO" layout="false" onclick="convert_bzrgzdwbh()"/>
			<emp:textarea id="PspSitecheckComGr.bzrgzdwbh_xdw" label="请录入新的工作单位名称" required="false" hidden="true" colSpan="2"/>
			<emp:radio id="PspSitecheckComGr.bzrjtzcbh" label="保证人家庭资产是否变化" required="true" dictname="STD_ZX_YES_NO" layout="false" onclick="convert_bzrjtzcbh()"/>
			<emp:textarea id="PspSitecheckComGr.bzrjtzcbh_bhqk" label="请录入变化情况" required="false" hidden="true" colSpan="2"/>
			<emp:radio id="PspSitecheckComGr.bzrjtsrbh" label="保证人家庭收入是否变化" required="true" dictname="STD_ZX_YES_NO" layout="false" onclick="convert_bzrjtsrbh()"/>
			<emp:textarea id="PspSitecheckComGr.bzrjtsrbh_bhqk" label="请录入变化情况" required="false" hidden="true" colSpan="2"/>
			<emp:radio id="PspSitecheckComGr.bzrjkzkbh" label="保证人健康状况是否变化" required="true" dictname="STD_ZX_YES_NO" layout="false" onclick="convert_bzrjkzkbh()"/>
			<emp:textarea id="PspSitecheckComGr.bzrjkzkbh_bhqk" label="请录入变化情况" required="false" hidden="true" colSpan="2"/>
			<emp:textarea id="PspSitecheckComGr.dbr_qtqk" label="其他情况：" required="false" hidden="false" colSpan="2"/>
		</emp:gridLayout>
		</div>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="确定"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回列表" />
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

