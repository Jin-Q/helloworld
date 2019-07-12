<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>
<style type="text/css">
.emp_field_text_input2 {
border: 1px solid #b7b7b7;
background-color:#eee;
text-align:left;
width:450px;
}
</style>
<jsp:include page="/include.jsp" flush="true"/>

<%
	String hidBut = request.getParameter("hidBut");
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		//var url = '<emp:url action="queryPspAppCusVisitList.do"/>';
		//url = EMPTools.encodeURI(url);
		//window.location=url;
		history.go(-1);
	};
	
	/*--user code begin--*/
	function onLoad(){
		PspAppCusVisit.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
	}

	//调用时需传入客户Id
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+PspAppCusVisit.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onLoad()">
	<emp:tabGroup mainTab="main_tabs" id="main_tabs">
		<emp:tab label="基本信息" id="main_tabs">
		<emp:gridLayout id="PspAppCusVisitGroup" title="客户走访登记" maxColumn="2">
			<emp:text id="PspAppCusVisit.serno" label="业务编号" maxlength="40" required="true" hidden="true"/>
			<emp:text id="PspAppCusVisit.cus_id" label="受访客户码" required="true" readonly="true"/>
			<emp:text id="PspAppCusVisit.cus_id_displayname" label="受访客户名称" required="true" cssElementClass="emp_field_text_input2" colSpan="2" readonly="true"/>
			<emp:date id="PspAppCusVisit.visit_time" label="访客时间" required="true" />
			<emp:text id="PspAppCusVisit.visit_addr" label="访客地点" required="false" maxlength="80" hidden="true"/>
			<emp:pop id="PspAppCusVisit.visit_addr_displayname" label="访客地点" colSpan="2" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" 
						returnMethod="onReturnAddr" cssElementClass="emp_field_text_input2"/>
			<emp:text id="PspAppCusVisit.visit_street" label="街道" maxlength="80" required="false" cssElementClass="emp_field_text_input2" colSpan="2"/>
			<emp:text id="PspAppCusVisit.visit_obj" label="访谈对象" maxlength="80" required="true" />
			<emp:text id="PspAppCusVisit.visit_obj_phone" label="访谈对象电话" maxlength="80" required="false" dataType="Phone" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="PspAppCusVisit.visit_obj_duty" label="访谈对象职务" cssFakeInputClass="emp_field_text_input2" dictname="STD_ZX_DUTY" required="true" colSpan="2"/>
			<emp:select id="PspAppCusVisit.is_cret_need" label="是否存在信贷需求" required="true" dictname="STD_ZX_YES_NO" />
			<emp:select id="PspAppCusVisit.is_advice_sale" label="是否建议再次营销" required="false" dictname="STD_ZX_YES_NO" />
			<emp:textarea id="PspAppCusVisit.visit_dest" label="本次访客目的" maxlength="500" required="false" colSpan="2" />
			<emp:textarea id="PspAppCusVisit.visit_record" label="本次访谈记录" maxlength="500" required="false" colSpan="2" />
			<emp:textarea id="PspAppCusVisit.visit_res" label="本次访谈结论" maxlength="500" required="false" colSpan="2" />
			<emp:textarea id="PspAppCusVisit.memo" label="备注" maxlength="500" required="false" colSpan="2" />
			<emp:text id="PspAppCusVisit.input_id_displayname" label="登记人" required="true" defvalue="$currentUserName" readonly="true"/>
			<emp:text id="PspAppCusVisit.input_br_id_displayname" label="登记机构" required="true" defvalue="$organName"  readonly="true"/>
			<emp:text id="PspAppCusVisit.input_id" label="登记人" maxlength="20" required="true" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="PspAppCusVisit.input_br_id" label="登记机构" maxlength="20" required="true" hidden="true" defvalue="$organNo"/>
			<emp:text id="PspAppCusVisit.input_date" label="登记日期" maxlength="10" required="true" defvalue="${context.OPENDAY}" readonly="true"/>
			<emp:select id="PspAppCusVisit.approve_status" label="申请状态" required="true" dictname="WF_APP_STATUS" defvalue="000" hidden="true"/>
		</emp:gridLayout>
	
		<div align="center">
			<br>
			<%if(!"hid".equals(hidBut)){ %>
			<emp:button id="return" label="返回到列表页面"/>
			<%} %>
		</div>
	</emp:tab>
	<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup >
</body>
</html>
</emp:page>
