<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	String task_id = request.getParameter("task_id"); 
	String dataFrom = request.getParameter("dataFrom");
	String cus_name = request.getParameter("cus_name");
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
		PspSitecheckCom.task_id._setValue(task_id);

		convert_worker();
	
		convert_scxts();
		convert_jqsb();
		convert_chsl();
	
	}

	function doSub(){
		var form = document.getElementById("submitForm");
		if(PspSitecheckCom._checkAll()){
			PspSitecheckCom._toForm(form); 
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

	

	function convert_worker(){
		if(PspSitecheckCom.worker_zjqk._getValue()=="03"){
			PspSitecheckCom.worker_jssm._obj._renderHidden(false);
			PspSitecheckCom.worker_jssm._obj._renderRequired(true);
			
		}else{
			PspSitecheckCom.worker_jssm._obj._renderHidden(true);
			PspSitecheckCom.worker_jssm._obj._renderRequired(false);
			PspSitecheckCom.worker_jssm._setValue("");
		}
	}
	function convert_scxts(){
		if(PspSitecheckCom.scx_zjqk._getValue()=="03"){
			PspSitecheckCom.scx_jsyy._obj._renderHidden(false);
			PspSitecheckCom.scx_jsyy._obj._renderRequired(true);
			
		}else{
			PspSitecheckCom.scx_jsyy._obj._renderHidden(true);
			PspSitecheckCom.scx_jsyy._obj._renderRequired(false);
			PspSitecheckCom.scx_jsyy._setValue("");
		}
	}
	function convert_jqsb(){
		if(PspSitecheckCom.jqsb_zjqk._getValue()=="03"){
			PspSitecheckCom.jqsb_jsyy._obj._renderHidden(false);
			PspSitecheckCom.jqsb_jsyy._obj._renderRequired(true);
			
		}else{
			PspSitecheckCom.jqsb_jsyy._obj._renderHidden(true);
			PspSitecheckCom.jqsb_jsyy._obj._renderRequired(false);
			PspSitecheckCom.jqsb_jsyy._setValue("");
		}
	}
	function convert_chsl(){
		if(PspSitecheckCom.chsl_zjqk._getValue()=="03"){
			PspSitecheckCom.chsl_jsyy._obj._renderHidden(false);
			PspSitecheckCom.chsl_jsyy._obj._renderRequired(true);
			
		}else{
			PspSitecheckCom.chsl_jsyy._obj._renderHidden(true);
			PspSitecheckCom.chsl_jsyy._obj._renderRequired(false);
			PspSitecheckCom.chsl_jsyy._setValue("");
		}
	}

    
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	
	<emp:form id="submitForm" action="updatePspSitecheckComRecordFortc.do" method="POST">
			<emp:text id="PspSitecheckCom.cus_id" label="客户号" hidden="true" defvalue="${context.cus_id}" />
			<emp:text id="PspSitecheckCom.pk_id" label="主键" required="false" hidden="true"/>
			<emp:text id="PspSitecheckCom.task_id" label="任务号" hidden="true" defvalue="${context.task_id}" />
		<emp:gridLayout id="PspSitecheckComGroup" title="现场检查" maxColumn="2">
			<emp:date id="PspSitecheckCom.check_time" label="检查时间"  required="true"/>
			<emp:text id="PspSitecheckCom.check_addr" label="检查地点" maxlength="100"   required="true" />
			<emp:text id="PspSitecheckCom.yjry" label="约见人员" maxlength="40" required="true" />
			<emp:select id="PspSitecheckCom.visit_type" label="现场检查类型"  required="true"  dictname="STD_ZB_VISIT_TYPE" defvalue="02" readonly="true"/>	
			<emp:text id="PspSitecheckCom.cus_id_displayname" label="保证人名称" required="false" readonly="true" />
		</emp:gridLayout>	

		<emp:gridLayout id="PspSitecheckComGroup" title="生产经营状况" maxColumn="2"  >
			<emp:text id="PspSitecheckCom.worker_num" label="工人数量" required="true" dataType="Int" colSpan="2"/>
			<emp:radio id="PspSitecheckCom.worker_zjqk" label="与上期对比" required="true" dictname="STD_ZB_ZJ_TYPE" layout="false" onclick="convert_worker()"/>
			<emp:textarea id="PspSitecheckCom.worker_jssm" label="工人数量减少说明原因" required="false" hidden="true" colSpan="2"/>
			<emp:text id="PspSitecheckCom.scxts" label="生产线条数" required="true" dataType="Int"/>
			<emp:text id="PspSitecheckCom.scxts_kgl" label="实际开工率"  required="true" dataType="Rate"/>
			<emp:radio id="PspSitecheckCom.scx_zjqk" label="与上期对比" required="true" dictname="STD_ZB_ZJ_TYPE" layout="false" onclick="convert_scxts()"/>
			<emp:textarea id="PspSitecheckCom.scx_jsyy" label="生产线减少原因" required="false" hidden="true" colSpan="2"/>
			<emp:text id="PspSitecheckCom.jqsb_sum" label="机器设备总台数" required="true" dataType="Int"/>
			<emp:text id="PspSitecheckCom.jqsb_kgl" label="实际开工率" required="true" dataType="Rate"/>
			<emp:radio id="PspSitecheckCom.jqsb_zjqk" label="与上期对比" required="true" dictname="STD_ZB_ZJ_TYPE" layout="false" onclick="convert_jqsb()"/>
			<emp:textarea id="PspSitecheckCom.jqsb_jsyy" label="机器设备减少原因" required="false" hidden="true" colSpan="2"/>
			<emp:text id="PspSitecheckCom.bgry_sum" label="办公场所：办公人员数量" required="true" dataType="Int" />
			<emp:radio id="PspSitecheckCom.bgcs" label="办公场所" required="true" dictname="STD_ZX_FIELD_OWNER" layout="false" />
		</emp:gridLayout>
		
		
		<emp:gridLayout id="PspSitecheckComGroup" title="存货现场" maxColumn="2">
			<emp:text id="PspSitecheckCom.chsl" label="存货数量" required="true"  dataType="Int" colSpan="2"/>
			<emp:radio id="PspSitecheckCom.chsl_zjqk" label="与上期比" required="true" dictname="STD_ZB_ZJ_TYPE" layout="false" onclick="convert_chsl()"/>
			<emp:textarea id="PspSitecheckCom.chsl_jsyy" label="减少原因" required="false" hidden="true" colSpan="2"/>
			<emp:textarea id="PspSitecheckCom.other_sm" label="其他需说明原因" required="false" colSpan="2"/>
		</emp:gridLayout>
		
		
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

