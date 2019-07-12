<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>查询详情页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>
<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryCusScaleApplyList.do"/>';
		var menuId = "${context.menuId}";
		if(menuId=='scaleCognizHis'){
			url = '<emp:url action="queryCusScaleApplyHisList.do"/>';
		}
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doload(){
		CusScaleApply.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
		menuId = "${context.menuId}";
		if(menuId == 'scaleCognizHis' || menuId == 'scaleCognizApp'){
			document.getElementById('button_return').style.display = '';
		}else{
			document.getElementById('button_return').style.display = 'none';
		}
	};
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+CusScaleApply.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	<emp:tabGroup mainTab="main_tabs" id="main_tabs">
		<emp:tab label="基本信息" id="main_tabs">
		<emp:gridLayout id="CusScaleApplyGroup" maxColumn="2" title="条线认定申请">
			<emp:text id="CusScaleApply.serno" label="申请流水号" maxlength="40" required="true" colSpan="2" readonly="true" />
			<emp:text id="CusScaleApply.cus_id" label="客户码" required="true" />
			<emp:text id="CusScaleApply.cus_id_displayname" label="客户名称"  required="true" colSpan="2" readonly="true" cssElementClass="emp_field_text_input2"/>
			<emp:select id="CusScaleApply.scale_type" label="认定类别" required="true" dictname="STD_ZB_COGNIZ_TYPE"  />
			<emp:textarea id="CusScaleApply.scale_detail" label="认定说明" maxlength="250" required="false" colSpan="2" onblur="this.value = this.value.substring(0, 250)" />
		</emp:gridLayout>
		<emp:gridLayout id="CusScaleApplyGroup" maxColumn="2" title="登记信息">
			<emp:pop id="CusScaleApply.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="CusScaleApply.manager_br_id_displayname" label="管理机构"  required="true"  url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" />
			
			<emp:text id="CusScaleApply.input_id_displayname" label="登记人"  readonly="true" required="true"  defvalue="$currentUserId" />
			<emp:text id="CusScaleApply.input_br_id_displayname" label="登记机构" readonly="true" required="true"  defvalue="$organNo" />
			<emp:text id="CusScaleApply.input_id" label="登记人" maxlength="20" readonly="true" required="true" hidden="true"/>
			<emp:text id="CusScaleApply.input_br_id" label="登记机构"  maxlength="20" readonly="true" required="true" hidden="true"/>
			<emp:text id="CusScaleApply.approve_status" label="审批状态" maxlength="3" required="false"  hidden="true" defvalue="000"/>
			<emp:text id="CusScaleApply.manager_id" label="责任人" maxlength="20" required="true"  readonly="true" hidden="true"/>
			<emp:text id="CusScaleApply.manager_br_id" label="管理机构"  required="true" hidden="true"/>
			<emp:text id="CusScaleApply.input_date" label="登记日期" required="true" readonly="true" defvalue="$OPENDAY" colSpan="2"/>
		</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表"/>
	</div>
	</emp:tab>
	<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup >
</body>
</html>
</emp:page>
