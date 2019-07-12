<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<!-- /**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 begin */ -->
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<!-- /**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 end */ -->
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	String type = request.getParameter("type");
	String hidBut = request.getParameter("hidBut");//审批中控制按钮隐藏
	request.setAttribute("canwrite","");
	/**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 begin */
	String one_key = "";
	String cus_id="";
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	if(context.containsKey("OneKey")){
		one_key = (String)context.getDataValue("OneKey");
		cus_id = (String)context.getDataValue("cus_id");
	}
	if(context.containsKey("type") && "cusHis".equals(context.getDataValue("type"))){
		cus_id = (String)context.getDataValue("cus_id");
	}
	/**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 end */
%>

<script type="text/javascript">
	
	function doReturn() {
		var type = '<%=type%>';
		var url = null;
		if(type=='frozen'){
			url = '<emp:url action="queryLmtAppJointCoopFrozenList.do"/>';
		/**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 begin */
		}else if(type=='cusHis'){
			url = '<emp:url action="queryLmtAppJointCoop_jointList.do"/>?type=${context.type}&cus_id='+'<%=cus_id%>';
		/**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 end */
		}else{
			url = '<emp:url action="queryLmtAppJointCoop_jointList.do"/>';
		}
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doOnload(){
		controlOrg(LmtAppJointCoop.share_range._getValue());  //初始化机构信息显示
		addOneButton();
		//通过申请类型来确定显示字段
		var app_type = LmtAppJointCoop.app_type._getValue();
		if(app_type=='01'){
			LmtAppJointCoop.start_date._obj._renderHidden(true);
			LmtAppJointCoop.end_date._obj._renderHidden(true);
		}else{
			LmtAppJointCoop.term_type._obj._renderHidden(true);
			LmtAppJointCoop.term._obj._renderHidden(true);
		}
	}
	function addOneButton(){
		LmtAppJointCoop.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
	}

	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+LmtAppJointCoop.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow2','height=538px,width=1024px,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}
	
	//设置共享范围机构
	function getBelgOrg(data){
		LmtAppJointCoop.belg_org._setValue(data[0]);
		LmtAppJointCoop.belg_org_displayname._setValue(data[1]);
	}

	/****** 共享范围与所属机构控制 *******/
	function controlOrg(_value){
		if(_value == 2){
			LmtAppJointCoop.belg_org._obj._renderHidden(false);
			LmtAppJointCoop.belg_org_displayname._obj._renderHidden(false);
		}else{
			LmtAppJointCoop.belg_org._obj._renderHidden(true);
			LmtAppJointCoop.belg_org_displayname._obj._renderHidden(true);
		}
	};
	/**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 begin */
	function doReturnByOneKey() {
		var url = '<emp:url action="queryCusComByOneKey.do"/>?cus_id=<%=cus_id%>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 end */
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnload()">
<emp:tabGroup mainTab="main_tabs" id="main_tabs">
	<emp:tab label="联保额度信息"  id="main_tabs" >
		<emp:form id="submitForm" action="addLmtAppJointCoop_jointRecord.do" method="POST">
			
			<emp:gridLayout id="LmtAppJointCoopGroup" title="联保小组信息" maxColumn="2">
				<emp:text id="LmtAppJointCoop.serno" label="业务编号" maxlength="40" required="true" readonly="true" colSpan="2"/>
				<emp:text id="LmtAppJointCoop.cus_id" label="客户码" readonly="true" required="true"/>
				<emp:text id="LmtAppJointCoop.cus_id_displayname" label="客户名称" required="false" readonly="true"/>
				<emp:select id="LmtAppJointCoop.coop_type" label="类别" required="true" dictname="STD_ZB_COOP_TYPE" colSpan="2"/>
				<emp:select id="LmtAppJointCoop.share_range" label="共享范围" required="false" dictname="STD_SHARED_SCOPE" onchange="controlOrg(this.value)" hidden="true"/>
				<emp:pop id="LmtAppJointCoop.belg_org" label="所属机构码"   url="queryMultiSOrgPop.do" returnMethod="getBelgOrg"  required="false"  hidden="true" colSpan="2"/>
				<emp:textarea id="LmtAppJointCoop.belg_org_displayname" label="所属机构名称" readonly="true" hidden="true" colSpan="2"/>
				<emp:date id="LmtAppJointCoop.app_date" label="申请日期" required="false" defvalue="${context.OPENDAY}" readonly="true"/>
				<emp:select id="LmtAppJointCoop.app_type" label="申请类型" required="false" dictname="STD_ZB_APP_TYPE" readonly="true"/>
				<!-- add by lisj 2015-4-14 需求编号：【XD150407025】分支机构授信审批权限配置 begin -->
			    <emp:select id="LmtAppJointCoop.is_overdue" label="额度是否逾期" required="true" dictname="STD_ZX_YES_NO" colSpan="2" readonly="true"/>
			    <!-- add by lisj 2015-4-14 需求编号：【XD150407025】分支机构授信审批权限配置 end -->
				<emp:date id="LmtAppJointCoop.over_date" label="办结日期" required="false" hidden="true"/>
				
			</emp:gridLayout>
			<emp:gridLayout id="LmtAppJointCoopGroupM" title="小组额度信息" maxColumn="2">
				<emp:select id="LmtAppJointCoop.cur_type" label="币种" required="false" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true" colSpan="2"/>
				<emp:text id="LmtAppJointCoop.lmt_totl_amt" label="授信总额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="LmtAppJointCoop.single_max_amt" label="单户限额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<emp:select id="LmtAppJointCoop.term_type" label="期限类型" required="true" dictname="STD_ZB_TERM_TYPE" />
				<emp:text id="LmtAppJointCoop.term" label="期限" maxlength="3" required="true" dataType="Int" cssElementClass="emp_currency_text_readonly"/>
				
				<emp:date id="LmtAppJointCoop.start_date" label="起始日期" required="true" />
				<emp:date id="LmtAppJointCoop.end_date" label="到期日期" required="true" />
			</emp:gridLayout>
			<emp:gridLayout id="LmtAppJointCoopGroupM" title="机构信息" maxColumn="2">
				<emp:pop id="LmtAppJointCoop.manager_id" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setManagerId" hidden="true" required="true"/>
				<emp:text id="LmtAppJointCoop.manager_br_id" label="责任机构" maxlength="20" required="false" readonly="true" hidden="true"/>
				<emp:text id="LmtAppJointCoop.input_id" label="登记人" maxlength="20" required="false" defvalue="$currentUserId" readonly="true" hidden="true"/>
				<emp:text id="LmtAppJointCoop.input_br_id" label="登记机构" maxlength="20" required="false" defvalue="$organNo" readonly="true" hidden="true"/>
				
				<emp:pop id="LmtAppJointCoop.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setManagerId" required="true"/>
				<emp:pop id="LmtAppJointCoop.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName"/>
				<emp:text id="LmtAppJointCoop.input_id_displayname" label="登记人" required="false" readonly="true"/>
				<emp:text id="LmtAppJointCoop.input_br_id_displayname" label="登记机构" required="false" readonly="true"/>
				
				<emp:date id="LmtAppJointCoop.input_date" label="登记日期" required="false" readonly="true" defvalue="$OPENDAY" hidden="true"/>
				<emp:select id="LmtAppJointCoop.approve_status" label="申请状态" required="false" readonly="true" dictname="WF_APP_STATUS" defvalue="000"/>
			</emp:gridLayout>
	<!-- /**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 begin */ -->
		<%if(!"hid".equals(hidBut)&&(one_key == null||one_key.equals(""))){ %>
			<div align="center">
				<br>
				<emp:button id="return" label="返回"/>
			</div>
		<%} %>
		</emp:form>
	</emp:tab>
	<emp:ExtActTab></emp:ExtActTab>	
	</emp:tabGroup>
	<%if(!"".equals(one_key) && one_key != null){ %>
	<div align="center">
		<emp:button id="returnByOneKey" label="返回" />
	</div>
	<%} %>
	<!-- /**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 end */-->
</body>
</html>
</emp:page>
