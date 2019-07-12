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

		convert_dzyxthzgzk();
		convert_dzybxnlpj();
		convert_djjgsfyslcd();
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
	
	function convert_dzyxthzgzk(){
		if(PspSitecheckComGr.dzyxthzgzk._getValue()=="2"){
			PspSitecheckComGr.dzyxthzgzk_ycyy._obj._renderHidden(false);
			PspSitecheckComGr.dzyxthzgzk_ycyy._obj._renderRequired(true);
			PspSitecheckComGr.dzyxthzgzk_ycyy._setValue("");
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
			PspSitecheckComGr.dzybxnlpj_ycyy._setValue("");
		}else{
			PspSitecheckComGr.dzybxnlpj_ycyy._obj._renderHidden(true);
			PspSitecheckComGr.dzybxnlpj_ycyy._obj._renderRequired(false);
			PspSitecheckComGr.dzybxnlpj_ycyy._setValue("");
		}
	}

	function convert_djjgsfyslcd(){
		if(PspSitecheckComGr.djjgsfyslcd._getValue()=="1" ){
			PspSitecheckComGr.cdsj._obj._renderHidden(false);
			PspSitecheckComGr.cdsj._obj._renderRequired(true);
			PspSitecheckComGr.cdyy._obj._renderHidden(false);
			PspSitecheckComGr.cdyy._obj._renderRequired(true);
			PspSitecheckComGr.cdsqr._obj._renderHidden(false);
			PspSitecheckComGr.cdsqr._obj._renderRequired(true);
			
		}else{
			PspSitecheckComGr.cdsj._obj._renderHidden(true);
			PspSitecheckComGr.cdsj._obj._renderRequired(false);
			PspSitecheckComGr.cdsj._setValue("");
			PspSitecheckComGr.cdyy._obj._renderHidden(true);
			PspSitecheckComGr.cdyy._obj._renderRequired(false);
			PspSitecheckComGr.cdyy._setValue("");
			PspSitecheckComGr.cdsqr._obj._renderHidden(true);
			PspSitecheckComGr.cdsqr._obj._renderRequired(false);
			PspSitecheckComGr.cdsqr._setValue("");
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	
	<emp:form id="submitForm" action="updatePspSitecheckComGrRecordFortc.do" method="POST">
			<emp:text id="PspSitecheckComGr.cus_id" label="客户号" hidden="true" defvalue="${context.cus_id}" readonly="true"/>
			<emp:text id="PspSitecheckComGr.pk_id" label="主键" required="false" hidden="true" readonly="true"/>
			<emp:text id="PspSitecheckComGr.task_id" label="任务号" hidden="true" defvalue="${context.task_id}" readonly="true"/>
		<emp:gridLayout id="PspSitecheckComGrGroup" title="现场检查" maxColumn="2">
			<emp:date id="PspSitecheckComGr.check_time" label="检查时间"  required="true" readonly="true"/>
			<emp:text id="PspSitecheckComGr.check_addr" label="检查地点" maxlength="100"   required="true" readonly="true"/>
			<emp:text id="PspSitecheckComGr.yjry" label="约见人员" maxlength="40" required="true" readonly="true"/>
			<emp:select id="PspSitecheckComGr.visit_type" label="现场检查类型"  required="true"  dictname="STD_ZB_VISITGR_TYPE" defvalue="04" readonly="true"/>	
		</emp:gridLayout>
		
		<emp:gridLayout id="PspSitecheckComGrGroup" title="抵（质）押物现场检查明细：" maxColumn="2">
			
			<emp:radio id="PspSitecheckComGr.dzyxthzgzk" label="抵（质）押物形态和占管状况：" required="true" dictname="STD_PSP_STATUS_TYPE" layout="false" colSpan="2"  cssLabelClass="emp_field_label1" disabled="true"/>
			<emp:textarea id="PspSitecheckComGr.dzyxthzgzk_ycyy" label="异常请说明原因：" required="false" colSpan="2" hidden="true"  readonly="true"/>
			<emp:radio id="PspSitecheckComGr.dzybxnlpj" label="抵（质）押物变现能力评价：" required="true" dictname="STD_PSP_STATUS_TYPE" layout="false" colSpan="2" cssLabelClass="emp_field_label1" disabled="true"/>
			<emp:textarea id="PspSitecheckComGr.dzybxnlpj_ycyy" label="异常请说明原因：" required="false" colSpan="2" hidden="true" readonly="true"/>
			<emp:radio id="PspSitecheckComGr.dzysdsfcd" label="抵（质）押物实地（物）是否被查封、冻结：" required="true" dictname="STD_ZX_YES_NO" layout="false" colSpan="2" cssLabelClass="emp_field_label1" disabled="true"/>
			<emp:radio id="PspSitecheckComGr.djjgsfyslcd" label="登记机关是否已受理查封、冻结：" required="true" dictname="STD_ZX_YES_NO" layout="false" colSpan="2" cssLabelClass="emp_field_label1" disabled="true"/>
			<emp:date id="PspSitecheckComGr.cdsj" label="查封、冻结时间"  required="true" hidden="true" readonly="true"/>
			<emp:textarea id="PspSitecheckComGr.cdyy" label="原因：" required="true" colSpan="2" hidden="true" readonly="true"/>
			<emp:text id="PspSitecheckComGr.cdsqr" label="查封、冻结申请人" maxlength="40" required="true" hidden="true" readonly="true"/>
			<emp:radio id="PspSitecheckComGr.dzydbyysfbh" label="抵（质）押人的担保意愿是否发生变化：" required="true" dictname="STD_ZX_YES_NO" layout="false" colSpan="2" cssLabelClass="emp_field_label1" disabled="true"/>
			<emp:radio id="PspSitecheckComGr.dywsfcz" label="抵押物是否出租：" required="true" dictname="STD_ZX_YES_NO" layout="false" colSpan="2" cssLabelClass="emp_field_label1" disabled="true"/>
			<emp:radio id="PspSitecheckComGr.dywczyxzqsxys" label="是否存在影响我行债权的顺利实现的风险因素：" required="true" dictname="STD_ZX_YES_NO" layout="false" colSpan="2" cssLabelClass="emp_field_label1" disabled="true"/>
			<emp:radio id="PspSitecheckComGr.dzyszbhyxdb" label="抵（质）押物市场价值是否发生不利变化，影响担保效力：" required="true" dictname="STD_ZX_YES_NO" layout="false" colSpan="2" cssLabelClass="emp_field_label1" disabled="true"/>
			<emp:radio id="PspSitecheckComGr.bgdzydjsfqzsf" label="保管的抵质押登记证明文件是否齐全，账实是否相符：" required="true" dictname="STD_ZX_YES_NO" layout="false" colSpan="2" cssLabelClass="emp_field_label1" disabled="true"/>
			<emp:textarea id="PspSitecheckComGr.dzyczdqtzk" label="抵质押物存在的其他状况：" required="false" colSpan="2" readonly="true"/>
			
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

