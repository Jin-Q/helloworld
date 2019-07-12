<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%><emp:page>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<html>
<head>
<title>授信复议</title>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String app_type = context.getDataValue("LmtRediApply.app_type").toString();
	String showButton = (String)context.getDataValue("showButton");
	String isShow = (String)context.getDataValue("isShow");
	
	request.setAttribute("canwrite","");
%>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	/*--user code begin--*/
	/**选择POP框用户后自动赋值客户码及客户名称*/
	function setCusDatas(data){
		LmtRediApply.cus_id._setValue(data.cus_id._getValue());
		cus_name_displayname._setValue(data.cus_name._getValue());
	}

	/**计算授信额度=循环额度+一次性额度 */
	function computeCrdAmt(){
		var crd_cir_amt = LmtRediApply.crd_cir_amt._getValue();
		var crd_one_amt = LmtRediApply.crd_one_amt._getValue();
		var total_amt = 0.00;
		if(null!=crd_cir_amt && ""!=crd_cir_amt){
			total_amt += parseFloat(crd_cir_amt);
		}
		if(null!=crd_one_amt && ""!=crd_one_amt){
			total_amt += parseFloat(crd_one_amt);
		}
		LmtRediApply.crd_totl_amt._setValue(parseFloat(total_amt)+"");
	}

	//返回主管客户经理	
	function setconId(data){
		LmtRediApply.manager_id._setValue(data.actorno._getValue());
		LmtRediApply.manager_id_displayname._setValue(data.actorname._getValue());
		LmtRediApply.manager_br_id._setValue(data.orgid._getValue());
		LmtRediApply.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		LmtRediApply.manager_br_id_displayname._obj._renderReadonly(true);
		doOrgCheck();
	}

	function doOrgCheck(){
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("one" == flag){//客户经理只属于一个机构
					LmtRediApply.manager_br_id._setValue(jsonstr.org);
					LmtRediApply.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					LmtRediApply.manager_br_id._setValue("");
					LmtRediApply.manager_br_id_displayname._setValue("");
					LmtRediApply.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = LmtRediApply.manager_id._getValue();
					LmtRediApply.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					LmtRediApply.manager_br_id._setValue("");
					LmtRediApply.manager_br_id_displayname._setValue("");
					LmtRediApply.manager_br_id_displayname._obj._renderReadonly(false);
					LmtRediApply.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = LmtRediApply.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	//返回主管机构
	function getOrganName(data){
		LmtRediApply.manager_br_id._setValue(data.organno._getValue());
		LmtRediApply.manager_br_id_displayname._setValue(data.organname._getValue());
	}

	function onLoad(){
		var action = '${context.operate}';   //新增跟修改共用同一个页面，从后台绑定action 
		var form = document.getElementById('submitForm');
		form.action = action;
		addOneButton();
		//给主页签增加重载事件
		document.getElementById("main_tabs").href="javascript:reLoad()";
	}

	function addOneButton(){
		LmtRediApply.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
	}

	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+LmtRediApply.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'viewCusInfo','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}

	//提交流程
	function doSubmitLmtRediApply(){
		var serno = LmtRediApply.serno._getValue();
		var approve_status = LmtRediApply.approve_status._getValue();
		WfiJoin.table_name._setValue("LmtRediApply");
		WfiJoin.pk_col._setValue("serno");
		WfiJoin.pk_value._setValue(serno);
		WfiJoin.wfi_status._setValue(approve_status);
		WfiJoin.status_name._setValue("approve_status");
		WfiJoin.appl_type._setValue("0061");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：LMT_APP_FLOW

		WfiJoin.cus_id._setValue(LmtRediApply.cus_id._getValue());//客户码
		WfiJoin.cus_name._setValue(LmtRediApply.cus_id_displayname._getValue());//客户名称
		WfiJoin.amt._setValue(LmtRediApply.crd_totl_amt._getValue());  //金额
		WfiJoin.prd_name._setValue("对公授信复议申请");//产品名称
		initWFSubmit(false);
	};

	//重加载页面
	function reLoad(){
		window.location.reload();
	}

	function doReturn() {
		var	url = '<emp:url action="queryLmtApplyList.do"/>?menuId=corp_crd_query&type=his';
		//复议列表过来
		if('COM'=='${context.lx}' || 'com'=='${context.lx}'){
			url = '<emp:url action="queryLmtRediApplyList.do"/>?menuId=LmtRediApplyList&type=com';
		}
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/*--user code end--*/
</script>
</head>
<body class="page_content" onload="onLoad()">
	<emp:tabGroup mainTab="main_tabs" id="main_tab">
		<emp:tab label="授信基本信息" id="main_tabs" needFlush="true" initial="true">
			<emp:form id="submitForm" action="updateLmtRediApplyRecord.do" method="POST">
				<emp:gridLayout id="LmtRediApplyGroup" title="单一法人授信申请" maxColumn="2">
					<emp:text id="LmtRediApply.serno" label="业务编号" maxlength="40" required="true" readonly="true" />
					<emp:text id="LmtRediApply.grp_serno" label="集团授信编号" maxlength="40" required="false" hidden="true"/>
					<emp:select id="LmtRediApply.app_type" label="申请类型" required="true" dictname="STD_ZB_APP_TYPE" colSpan="2" readonly="true" />
					<emp:text id="LmtRediApply.cus_id" label="客户码" required="true" readonly="true"/>
					<emp:text id="LmtRediApply.cus_id_displayname" label="客户名称" readonly="true" cssElementClass="emp_field_text_readonly"   defvalue="${context.LmtRediApply.cus_id_displayname}"/> 
					<emp:select id="LmtRediApply.biz_type" label="授信业务类型" required="true" dictname="STD_ZB_BIZ_TYPE" defvalue="01" readonly="true"/>
					<emp:select id="LmtRediApply.cur_type" label="授信币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true"/>
					<emp:select id="LmtRediApply.lrisk_type" label="低风险业务类型" required="true" dictname="STD_ZB_LRISK_TYPE"/>
					<% if("06".equals(app_type)){  //如果是授信变更，显示原有额度情况      %>
					<emp:text id="LmtRediApply.org_crd_totl_amt" label="原授信总额" maxlength="18" required="true" dataType="Currency" colSpan="2" readonly="true" defvalue="0.00" cssElementClass="emp_currency_text_readonly"/>
					<emp:text id="LmtRediApply.org_crd_cir_amt" label="原循环授信敞口" maxlength="18" required="true" dataType="Currency" readonly="true" defvalue="0.00" cssElementClass="emp_currency_text_readonly"/>
					<emp:text id="LmtRediApply.org_crd_one_amt" label="原一次性授信敞口" maxlength="18" required="true" dataType="Currency" readonly="true" defvalue="0.00" cssElementClass="emp_currency_text_readonly"/>
					<% }%>
					<emp:text id="LmtRediApply.crd_totl_amt" label="授信总额" maxlength="18" required="true" colSpan="2" dataType="Currency" readonly="true" defvalue="0" cssElementClass="emp_currency_text_readonly"/>
					<emp:text id="LmtRediApply.crd_cir_amt" label="循环授信敞口" maxlength="18" required="true" onblur="computeCrdAmt()" dataType="Currency" defvalue="0.00" cssElementClass="emp_currency_text_readonly"/>
					<emp:text id="LmtRediApply.crd_one_amt" label="一次性授信敞口" maxlength="18" required="true" onblur="computeCrdAmt()" dataType="Currency" defvalue="0.00" cssElementClass="emp_currency_text_readonly"/>
					<emp:select id="LmtRediApply.flow_type" label="流程类型" required="true" dictname="STD_ZB_FLOW_TYPE" defvalue="01" readonly="true"/>
					<emp:date id="LmtRediApply.app_date" label="申请日期" required="false"  defvalue="${context.OPENDAY}" readonly="true"/>
					<emp:text id="LmtRediApply.over_date" label="办结日期" maxlength="10" required="false" hidden="true" />
					<emp:textarea id="LmtRediApply.memo" label="备注" maxlength="200" required="false" colSpan="3" />
					<emp:pop id="LmtRediApply.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
					<emp:pop id="LmtRediApply.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName" readonly="true"/>
					<emp:text id="LmtRediApply.manager_id" label="责任人" required="true" hidden="true"/>
					<emp:text id="LmtRediApply.manager_br_id" label="责任机构" required="true" hidden="true"/>
					<emp:text id="LmtRediApply.input_id" label="登记人" maxlength="20" required="true" defvalue="${context.currentUserName}" hidden="true"/>
					<emp:text id="LmtRediApply.input_br_id" label="登记机构" maxlength="20" required="true" defvalue="${context.organName}" hidden="true" />
					<emp:text id="LmtRediApply.input_id_displayname" label="登记人" required="true" readonly="true" defvalue="${context.currentUserName}" />
					<emp:text id="LmtRediApply.input_br_id_displayname" label="登记机构" required="true" readonly="true"  defvalue="${context.organName}"/>
					<emp:text id="LmtRediApply.input_date" label="登记日期" maxlength="10" required="true" defvalue="${context.OPENDAY}" readonly="true"/>
					<emp:select id="LmtRediApply.approve_status" label="申请状态" required="true" dictname="WF_APP_STATUS" hidden="true" defvalue="000"/>
					<emp:text id="LmtRediApply.arg_no" label="协议编号" hidden="true"/>
				</emp:gridLayout>
			</emp:form>
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
	<div align="center">
		<%if(!"N".equalsIgnoreCase(showButton)){ %>
		<emp:button id="submitLmtRediApply" label="提交审批" />
		<%} %>
		<%if(!"N".equalsIgnoreCase(isShow)){ %>
		<emp:button id="return" label="返回列表" />
		<%} %>
	</div>
</body>
</html>
</emp:page>
