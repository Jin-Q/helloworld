<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<!-- /**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 begin */ -->
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<!-- /**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 end */ -->

<emp:page>
<%
	String hiddReturn = request.getParameter("hiddReturn");
%>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
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
	/**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 begin */
	function doReturn() {
		var cus_id='<%=cus_id%>';
		var url = '<emp:url action="queryLmtGrpApplyList.do"/>?type=${context.type}&menuId=${context.menuId}';
		if(cus_id!=null&&cus_id!=""){
			url=url+"&cus_id=<%=cus_id%>";
		}
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 end */
	/*--user code begin--*/
	function onLoad(){
		LmtAppGrp.grp_no._obj.addOneButton('view12','查看',viewGrpCusInfo);  //集团编号加查看按钮
	}

	function viewGrpCusInfo(){
		var url = "<emp:url action='queryCusGrpInfoPopDetial.do'/>&grp_no="+LmtAppGrp.grp_no._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height=700,width=1024,top=70,left=70,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no'); 
	}
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
<body class="page_content" onload="onLoad()">
	<emp:tabGroup mainTab="main_tabs" id="main_tabs">
		<emp:tab label="授信基本信息" id="main_tabs">
			<emp:gridLayout id="LmtGrpApplyGroup" title="集团授信申请/变更" maxColumn="2">
				<emp:text id="LmtAppGrp.serno" label="业务编号" maxlength="40" required="true" cssElementClass="emp_field_text_readonly" colSpan="2"/>
				<emp:text id="LmtAppGrp.grp_no" label="集团编号" maxlength="40" required="true" />
				<emp:text id="LmtAppGrp.grp_no_displayname" label="集团名称" required="true" cssElementClass="emp_field_text_readonly"/>
				<emp:select id="LmtAppGrp.app_type" label="申请类型" required="true" dictname="STD_ZB_APP_TYPE" />
				<emp:select id="LmtAppGrp.biz_type" label="授信业务类型 " required="true" dictname="STD_ZB_BIZ_TYPE" />
				<emp:select id="LmtAppGrp.cur_type" label="授信币种" required="true" dictname="STD_ZX_CUR_TYPE" />
				<emp:text id="LmtAppGrp.crd_totl_amt" label="授信总额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<emp:date id="LmtAppGrp.app_date" label="申请日期" required="true" />
				<emp:date id="LmtAppGrp.over_date" label="办结日期" required="false" hidden="true"/>
				<emp:select id="LmtAppGrp.flow_type" label="流程类型" required="true" dictname="STD_ZB_FLOW_TYPE" hidden="true"/>
				<emp:textarea id="LmtAppGrp.memo" label="备注" maxlength="200" required="false" colSpan="2" />
			</emp:gridLayout>
			<emp:gridLayout id="LmtAppGrpGroup" maxColumn="2" title="登记信息">
				<emp:text id="LmtAppGrp.manager_id_displayname" label="责任人" required="true" />
				<emp:text id="LmtAppGrp.manager_br_id_displayname" label="责任机构" required="true" />
				<emp:text id="LmtAppGrp.input_id_displayname" label="登记人" required="true" />
				<emp:text id="LmtAppGrp.input_br_id_displayname" label="登记机构" required="true" />
				<emp:date id="LmtAppGrp.input_date" label="登记日期" required="true" />
				<emp:select id="LmtAppGrp.approve_status" label="申请状态" required="true" dictname="WF_APP_STATUS" hidden="true"/>
				<emp:text id="LmtAppGrp.grp_agr_no" label="集团协议编号" maxlength="40" required="false" hidden="true"/>
				<emp:text id="LmtAppGrp.org_crd_totl_amt" label="变更前授信总额 " maxlength="18" required="false" dataType="Currency" hidden="true"/>
				<emp:text id="LmtAppGrp.manager_id" label="责任人" maxlength="20" required="true" hidden="true"/>
				<emp:text id="LmtAppGrp.manager_br_id" label="责任机构" maxlength="20" required="true" hidden="true" />
				<emp:text id="LmtAppGrp.input_id" label="登记人" maxlength="20" required="true" hidden="true"/>
				<emp:text id="LmtAppGrp.input_br_id" label="登记机构" maxlength="20" required="true" hidden="true"/>
			</emp:gridLayout>
		</emp:tab>
	<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
	<!-- /**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 begin */ -->
	<%if(!"".equals(one_key) && one_key != null){ %>
	<div align="center">
		<emp:button id="returnByOneKey" label="返回" />
	</div>
	<%}else if(!"yes".equals(hiddReturn)){ %>
	<!--/**modified by wangj XD150114003_信贷系统联保和集团关联授信审批意见查询需求修改 end */ -->
	<div align="center">
		<input type="button" value="返回到列表页面" onclick="doReturn()" class="button100"/>
	</div>
	<%} %>
</body>
</html>
</emp:page>
