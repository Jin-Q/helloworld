<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="../pubAction/arpPubAction.jsp" flush="true"/>
<style type="text/css">
.emp_field_textarea_textarea {
	width: 99%;
};
</style>
<script type="text/javascript">
	
	/*--user code begin--*/
	function doLoad(){
		document.getElementById("main_tabs").href="javascript:reLoad();";
		addCusForm(ArpDbtWriteoffApp);
	};
	function doReturn() {
		var url = '<emp:url action="queryArpDbtWriteoffAppList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function doSubmits(){		
		doPubUpdate(ArpDbtWriteoffApp);
	};

	function reLoad(){
		var url = '<emp:url action="getArpDbtWriteoffAppUpdatePage.do"/>?serno=${context.ArpDbtWriteoffApp.serno}&op=update';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

	/** 登记信息begin **/
	function setconId(data){
		ArpDbtWriteoffApp.manager_id_displayname._setValue(data.actorname._getValue());
		ArpDbtWriteoffApp.manager_id._setValue(data.actorno._getValue());
		ArpDbtWriteoffApp.manager_br_id._setValue(data.orgid._getValue());
		ArpDbtWriteoffApp.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//ArpDbtWriteoffApp.manager_br_id_displayname._obj._renderReadonly(true);
		doOrgCheck();
	};

	function doOrgCheck(){
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/
				var flag = jsonstr.flag;
				if("one" == flag){//客户经理只属于一个机构
					ArpDbtWriteoffApp.manager_br_id._setValue(jsonstr.org);
					ArpDbtWriteoffApp.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag || "belg2team" == flag){//客户经理属于多个机构
					ArpDbtWriteoffApp.manager_br_id._setValue("");
					ArpDbtWriteoffApp.manager_br_id_displayname._setValue("");
					ArpDbtWriteoffApp.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = ArpDbtWriteoffApp.manager_id._getValue();
					ArpDbtWriteoffApp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					ArpDbtWriteoffApp.manager_br_id._setValue("");
					ArpDbtWriteoffApp.manager_br_id_displayname._setValue("");
					ArpDbtWriteoffApp.manager_br_id_displayname._obj._renderReadonly(false);
					ArpDbtWriteoffApp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
				/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = ArpDbtWriteoffApp.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	function getOrgID(data){
		ArpDbtWriteoffApp.manager_br_id._setValue(data.organno._getValue());
		ArpDbtWriteoffApp.manager_br_id_displayname._setValue(data.organname._getValue());
	};
	/** 登记信息end **/
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<emp:tabGroup mainTab="main_tabs" id="main_tabs">
	<emp:tab label="呆账认定申请" id="main_tabs">
	<emp:form id="submitForm" action="updateArpDbtWriteoffAppRecord.do" method="POST">
		<emp:gridLayout id="ArpDbtWriteoffAppGroup" title="核销客户信息" maxColumn="2">
			<emp:text id="ArpDbtWriteoffApp.serno" label="业务编号" maxlength="40" hidden="false" readonly="true" required="true"/>
			<emp:text id="ArpDbtWriteoffApp.cus_id" label="客户码" required="true"  colSpan="2" readonly="true" />
			<emp:text id="ArpDbtWriteoffApp.cus_name" label="客户名称" required="true" 
			colSpan="2" readonly="true" cssElementClass="emp_field_text_cusname" />			
			<%if("${context.ArpDbtWriteoffApp.belg_line}".equals("BL300")){	%>
			<emp:text id="ArpDbtWriteoffApp.indiv_rsd_addr_displayname" label="客户地址" cssElementClass="emp_field_text_cusname" colSpan="2" readonly="true" />
			<emp:text id="ArpDbtWriteoffApp.street3" label="街道" cssElementClass="emp_field_text_cusname" colSpan="2" readonly="true"/>
			<emp:text id="ArpDbtWriteoffApp.mobile" label="联系方式" readonly="true" />
			<%}else{ %>
			<emp:text id="ArpDbtWriteoffApp.acu_addr_displayname" label="客户地址" cssElementClass="emp_field_text_cusname" colSpan="2" readonly="true" />
			<emp:text id="ArpDbtWriteoffApp.street" label="街道" cssElementClass="emp_field_text_cusname" colSpan="2" readonly="true"/>
			<emp:text id="ArpDbtWriteoffApp.legal_phone" label="联系方式" readonly="true" />
			<%}%>
			<emp:text id="ArpDbtWriteoffApp.cust_mgr_displayname" label="主管客户经理" readonly="true" />
		</emp:gridLayout>

		<emp:gridLayout id="ArpDbtWriteoffAppGroup" title="呆账核销申请信息" maxColumn="2">
			<emp:text id="ArpDbtWriteoffApp.nums" label="核销笔数" maxlength="38" dataType="Int" colSpan="2" readonly="true" defvalue="0" />
			<emp:text id="ArpDbtWriteoffApp.loan_amt_sum" label="借据金额" maxlength="16" required="false" dataType="Currency" readonly="true" defvalue="0.00" />
			<emp:text id="ArpDbtWriteoffApp.loan_balance_sum" label="借据余额" maxlength="16" required="false" dataType="Currency" readonly="true" defvalue="0.00" />
			<emp:text id="ArpDbtWriteoffApp.inner_owe_int_sum" label="表内欠息" maxlength="16" required="false" dataType="Currency" readonly="true" defvalue="0.00" />
			<emp:text id="ArpDbtWriteoffApp.out_owe_int_sum" label="表外欠息" maxlength="16" required="false" dataType="Currency" readonly="true" defvalue="0.00" />
			<emp:text id="ArpDbtWriteoffApp.owe_int_sum" label="欠息累计" maxlength="16" required="false" dataType="Currency" colSpan="2" readonly="true" defvalue="0.00" />
			<emp:text id="ArpDbtWriteoffApp.writeoff_cap_sum" label="核销本金" maxlength="16" required="false" dataType="Currency" readonly="true" defvalue="0.00" />
			<emp:text id="ArpDbtWriteoffApp.writeoff_int_sum" label="核销利息" maxlength="16" required="false" dataType="Currency" readonly="true" defvalue="0.00" />
			<emp:text id="ArpDbtWriteoffApp.writeoff_amt_sum" label="核销总金额" maxlength="16" required="false" dataType="Currency" colSpan="2" readonly="true" defvalue="0.00" />		

			<emp:textarea id="ArpDbtWriteoffApp.writeoff_resn" label="核销理由" maxlength="250" required="true" colSpan="2" />
			<emp:select id="ArpDbtWriteoffApp.whether_appx_appeal" label="是否保留追诉权" required="true" dictname="STD_ZX_YES_NO" />
			<emp:textarea id="ArpDbtWriteoffApp.memo" label="备注" maxlength="250" required="false" colSpan="2" />
			<emp:date id="ArpDbtWriteoffApp.app_date" label="申请日期" required="false" hidden="true" />
			<emp:date id="ArpDbtWriteoffApp.over_date" label="办结日期" required="false" hidden="true" />
			<emp:text id="ArpDbtWriteoffApp.writeoff_qnt" label="核销笔数" maxlength="38" hidden="true"/>
			<emp:text id="ArpDbtWriteoffApp.loan_balance" label="贷款余额" maxlength="16" hidden="true"/>
		</emp:gridLayout>
		
		<emp:gridLayout id="ArpDbtWriteoffAppGroup" maxColumn="2" title="登记信息" >
			<emp:pop id="ArpDbtWriteoffApp.manager_id_displayname" label="管理人员" required="true" 
			 url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<!-- modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin -->
			<emp:pop id="ArpDbtWriteoffApp.manager_br_id_displayname" label="管理机构"  required="true" 
			url="querySOrgPop.do?manager_id=${context.manager_id}&restrictUsed=false" returnMethod="getOrgID" cssElementClass="emp_pop_common_org" />
			<!-- modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end -->
			<emp:text id="ArpDbtWriteoffApp.manager_br_id" label="管理机构"  required="true" hidden="true"/>
			<emp:text id="ArpDbtWriteoffApp.manager_id" label="管理人员" required="true" hidden="true"  />
			<emp:text id="ArpDbtWriteoffApp.input_id_displayname" label="登记人" readonly="true" required="true" />
			<emp:text id="ArpDbtWriteoffApp.input_br_id_displayname" label="登记机构" readonly="true" required="true" />
			<emp:text id="ArpDbtWriteoffApp.input_id" label="登记人" required="true" hidden="true"/>
			<emp:text id="ArpDbtWriteoffApp.input_br_id" label="登记机构" required="true" hidden="true" />
			<emp:date id="ArpDbtWriteoffApp.input_date" label="登记日期" required="true"  readonly="true" />
			<emp:select id="ArpDbtWriteoffApp.approve_status" label="审批状态" dictname="WF_APP_STATUS" readonly="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submits" label="保存" />
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	</emp:tab>
	<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
</body>
</html>
</emp:page>