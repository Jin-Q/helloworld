<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%><emp:page>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<html>
<head>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String showButton = (String)context.getDataValue("showButton");
	String isShow = (String)context.getDataValue("isShow");
	
	request.setAttribute("canwrite","");
%>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryLmtAppJointCoopRediList.do"/>?menuId=LmtAppJointCoopRediList';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doOnload(){
		controlOrg(LmtAppJointCoopRedi.share_range._getValue());  //初始化机构信息显示
		addOneButton();
		//给主页签增加重载事件
		document.getElementById("main_tabs").href="javascript:reLoad()";
	}
	//重加载页面
	function reLoad(){
		window.location.reload();
	}
	function addOneButton(){
		LmtAppJointCoopRedi.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
	}

	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+LmtAppJointCoopRedi.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow2','height=538px,width=1024px,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}
	//选择客户POP框返回方法
	function returnCus(data){
		LmtAppJointCoopRedi.cus_id._setValue(data.cus_id._getValue());
		LmtAppJointCoopRedi.cus_id_displayname._setValue(data.cus_name._getValue());
	}
	
	//设置共享范围机构
	function getBelgOrg(data){
		LmtAppJointCoopRedi.belg_org._setValue(data[0]);
		LmtAppJointCoopRedi.belg_org_displayname._setValue(data[1]);
	}

	/****** 共享范围与所属机构控制 *******/
	function controlOrg(_value){
		if(_value == 2){
			LmtAppJointCoopRedi.belg_org._obj._renderHidden(false);
			LmtAppJointCoopRedi.belg_org_displayname._obj._renderHidden(false);
		}else{
			LmtAppJointCoopRedi.belg_org._obj._renderHidden(true);
			LmtAppJointCoopRedi.belg_org_displayname._obj._renderHidden(true);
		}
	};
	//提交流程
	function doSubmitLmtAppJointCoopRedi(){
		
		var serno = LmtAppJointCoopRedi.serno._getValue();
		var approve_status = LmtAppJointCoopRedi.approve_status._getValue();

		WfiJoin.table_name._setValue("LmtAppJointCoopRedi");
		WfiJoin.pk_col._setValue("serno");
		WfiJoin.pk_value._setValue(serno);
		WfiJoin.wfi_status._setValue(approve_status);
		WfiJoin.status_name._setValue("approve_status");
		WfiJoin.appl_type._setValue("3241");  //流程申请类型，对应字典项[ZB_BIZ_CATE]
		WfiJoin.cus_id._setValue(LmtAppJointCoopRedi.cus_id._getValue());//客户码
		WfiJoin.cus_name._setValue(LmtAppJointCoopRedi.cus_id_displayname._getValue());//客户名称
		WfiJoin.amt._setValue(LmtAppJointCoopRedi.lmt_totl_amt._getValue());  //金额
		WfiJoin.prd_name._setValue("联保小组授信复议申请");

		initWFSubmit(false);
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnload()">
<emp:tabGroup mainTab="main_tabs" id="main_tabs">
	<emp:tab label="联保额度信息"  id="main_tabs" >
		<emp:form id="submitForm" action="addLmtAppJointCoop_jointRecord.do" method="POST">
			
			<emp:gridLayout id="LmtAppJointCoopRediGroup" title="联保小组信息" maxColumn="2">
				<emp:text id="LmtAppJointCoopRedi.serno" label="业务编号" maxlength="40" required="true" readonly="true" colSpan="2"/>
				<emp:text id="LmtAppJointCoopRedi.cus_id" label="客户码" readonly="true" required="true"/>
				<emp:text id="LmtAppJointCoopRedi.cus_id_displayname" label="客户名称" required="false" readonly="true"/>
				<emp:select id="LmtAppJointCoopRedi.coop_type" label="类别" required="true" dictname="STD_ZB_COOP_TYPE" colSpan="2"/>
				<emp:select id="LmtAppJointCoopRedi.share_range" label="共享范围" required="false" dictname="STD_SHARED_SCOPE" onchange="controlOrg(this.value)" hidden="true"/>
				<emp:pop id="LmtAppJointCoopRedi.belg_org" label="所属机构码"   url="queryMultiSOrgPop.do" returnMethod="getBelgOrg"  required="false"  hidden="true" colSpan="2"/>
				<emp:textarea id="LmtAppJointCoopRedi.belg_org_displayname" label="所属机构名称" readonly="true" hidden="true" colSpan="2"/>
				<emp:date id="LmtAppJointCoopRedi.app_date" label="申请日期" required="false" defvalue="${context.OPENDAY}" readonly="true"/>
				<emp:date id="LmtAppJointCoopRedi.over_date" label="办结日期" required="false" hidden="true"/>
				
			</emp:gridLayout>
			<emp:gridLayout id="LmtAppJointCoopRediGroupM" title="小组额度信息" maxColumn="2">
				<emp:select id="LmtAppJointCoopRedi.cur_type" label="币种" required="false" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true" colSpan="2"/>
				<emp:text id="LmtAppJointCoopRedi.lmt_totl_amt" label="授信总额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="LmtAppJointCoopRedi.single_max_amt" label="单户限额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<emp:select id="LmtAppJointCoopRedi.term_type" label="期限类型" required="true" dictname="STD_ZB_TERM_TYPE" />
				<emp:text id="LmtAppJointCoopRedi.term" label="期限" maxlength="3" required="true" dataType="Int" cssElementClass="emp_currency_text_readonly"/>
			</emp:gridLayout>
			<emp:gridLayout id="LmtAppJointCoopRediGroupM" title="机构信息" maxColumn="2">
				<emp:pop id="LmtAppJointCoopRedi.manager_id" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setManagerId" hidden="true" required="true"/>
				<emp:text id="LmtAppJointCoopRedi.manager_br_id" label="责任机构" maxlength="20" required="false" readonly="true" hidden="true"/>
				<emp:text id="LmtAppJointCoopRedi.input_id" label="登记人" maxlength="20" required="false" defvalue="$currentUserId" readonly="true" hidden="true"/>
				<emp:text id="LmtAppJointCoopRedi.input_br_id" label="登记机构" maxlength="20" required="false" defvalue="$organNo" readonly="true" hidden="true"/>
				
				<emp:pop id="LmtAppJointCoopRedi.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setManagerId" required="true"/>
				<emp:pop id="LmtAppJointCoopRedi.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName" readonly="true"/>
				<emp:text id="LmtAppJointCoopRedi.input_id_displayname" label="登记人" required="false" readonly="true"/>
				<emp:text id="LmtAppJointCoopRedi.input_br_id_displayname" label="登记机构" required="false" readonly="true"/>
				
				<emp:date id="LmtAppJointCoopRedi.input_date" label="登记日期" required="false" readonly="true" defvalue="$OPENDAY" hidden="true"/>
				<emp:select id="LmtAppJointCoopRedi.approve_status" label="申请状态" required="false" readonly="true" dictname="WF_APP_STATUS" defvalue="000"/>
			</emp:gridLayout>

		</emp:form>
	</emp:tab>
	<emp:ExtActTab></emp:ExtActTab>	
	</emp:tabGroup>
	<div align="center">
		<%if(!"N".equalsIgnoreCase(showButton)){ %>
		<emp:button id="submitLmtAppJointCoopRedi" label="提交审批" />
		<%} %>
		<%if(!"N".equalsIgnoreCase(isShow)){ %>
		<emp:button id="return" label="返回列表" />
		<%} %>
	</div>
</body>
</html>
</emp:page>
