<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
	String hidBut = request.getParameter("hidBut");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryPspAppRavelSignalList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doLoad(){
		PspAppRavelSignal.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
	}

	//查看客户信息
	function viewCusInfo(){
		var cus_id = PspAppRavelSignal.cus_id._getValue();
		if(cus_id==null||cus_id==''){
			alert('客户码为空！');
		}else {
			var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id;
	      	url=encodeURI(url); 
	      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		}
	}
	/****************预警信号*****************************/
	function doViewPspRavelSignalList() {
		var paramStr = PspRavelSignalListList._obj.getParamStr(['serno','pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getPspRavelSignalListViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height='+window.screen.availHeight*0.6+',width='+window.screen.availWidth*0.6+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:gridLayout id="PspAppRavelSignalGroup" title="预警信号解除申请" maxColumn="2">
			<emp:text id="PspAppRavelSignal.serno" label="业务编号" maxlength="32" required="true" colSpan="2"/>
			<emp:text id="PspAppRavelSignal.cus_id" label="客户编码" maxlength="40" required="true" />
			<emp:text id="PspAppRavelSignal.cus_id_displayname" label="客户名称"  required="true" />
			<emp:select id="PspAppRavelSignal.signal_type" label="类型" required="false" dictname="STD_ZB_ALT_SIGNAL_TYPE" />
			<emp:textarea id="PspAppRavelSignal.memo" label="解除原因" maxlength="200" required="false" colSpan="2" />
			<emp:text id="PspAppRavelSignal.approve_status" label="申请类型" maxlength="3" required="false" hidden="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="PspAppRavelSignalGroup" title="登记信息" maxColumn="2">
			<emp:text id="PspAppRavelSignal.manager_id_displayname" label="责任人" required="false" />
			<emp:text id="PspAppRavelSignal.manager_br_id_displayname" label="责任机构" required="false" />
			<emp:text id="PspAppRavelSignal.input_id_displayname" label="登记人" required="false" />
			<emp:text id="PspAppRavelSignal.input_br_id_displayname" label="登记机构" required="false" />
			<emp:date id="PspAppRavelSignal.input_date" label="登记日期" required="false" />
			
			<emp:text id="PspAppRavelSignal.manager_id" label="责任人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="PspAppRavelSignal.manager_br_id" label="责任机构" maxlength="20" required="false" hidden="true"/>
			<emp:text id="PspAppRavelSignal.input_id" label="登记人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="PspAppRavelSignal.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true"/>
	</emp:gridLayout>
	<%if(hidBut==null||"".equals(hidBut)){ %>
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
	<%} %>
	<div align="left" id="appDetails_div">
	<div class='emp_gridlayout_title'>预警信号信息&nbsp;</div>
	<div align="left">
		<emp:button id="viewPspRavelSignalList" label="查看" />
	</div>

	<emp:table icollName="PspRavelSignalListList" pageMode="false" url="pagePspRavelSignalListQuery.do">
		<emp:text id="serno" label="申请编号" hidden="true"/>
		<emp:text id="pk_id" label="预警信号ID" />
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="signal_info" label="风险预警信息内容及影响" />
		<emp:text id="signal_type" label="类型" dictname="STD_ZB_ALT_SIGNAL_TYPE" />
		<emp:text id="last_date" label="预计持续时间（天）" />
		<emp:text id="disp_mode" label="处置措施及进展情况" />
	</emp:table>
	</div>
</body>
</html>
</emp:page>
