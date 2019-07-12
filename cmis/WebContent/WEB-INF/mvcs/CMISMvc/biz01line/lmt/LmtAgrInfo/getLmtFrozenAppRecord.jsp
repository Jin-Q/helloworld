<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String app_type = (String)context.getDataValue("app_type");
%>
<script type="text/javascript">
	
	/*--user code begin--*/
	//主管客户经理
	function setconId(data){
		LmtApply.manager_id._setValue(data.actorno._getValue());
		LmtApply.manager_id_displayname._setValue(data.actorname._getValue());
		LmtApply.manager_br_id._setValue(data.orgid._getValue());
		LmtApply.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//LmtApply.manager_br_id_displayname._obj._renderReadonly(true);
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
					LmtApply.manager_br_id._setValue(jsonstr.org);
					LmtApply.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					LmtApply.manager_br_id._setValue("");
					LmtApply.manager_br_id_displayname._setValue("");
					LmtApply.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = LmtApply.manager_id._getValue();
					LmtApply.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					LmtApply.manager_br_id._setValue("");
					LmtApply.manager_br_id_displayname._setValue("");
					LmtApply.manager_br_id_displayname._obj._renderReadonly(false);
					LmtApply.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = LmtApply.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	//主管机构
	function getOrgID(data){
		LmtApply.manager_br_id._setValue(data.organno._getValue());
		LmtApply.manager_br_id_displayname._setValue(data.organname._getValue());
	}

	function doAddLmtFrozen(){
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if(flag=='succ'){
					var serno = LmtApply.serno._getValue();
					var url = '<emp:url action="getLmtFrozenUpdateRecord.do"/>?serno='+serno+"&app_type=<%=app_type%>&updflag=update";
					url = EMPTools.encodeURI(url);
					window.location = url;
				}
			}
		};
		var handleFailure = function(o) {
			alert("保存失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};

		var form = document.getElementById("submitForm");
		var result = LmtApply._checkAll();
		if(result){
			page.dataGroups.dataGroup_in_formsubmitForm.toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}else {
           alert("请输入必填项!");
           return ;
		}
	};

	function onLoad(){
		LmtApply.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
	}
	//客户信息查
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+LmtApply.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onLoad()">
	<emp:form id="submitForm" action="addLmtAgrFrozenApp.do" method="POST">
		<emp:gridLayout id="LmtAgrInfoGroup" maxColumn="2" title="授信协议信息">
			<emp:text id="LmtApply.serno" label="业务编号" maxlength="40" required="true" readonly="true" cssElementClass="emp_field_text_readonly"/>
			<emp:text id="LmtApply.agr_no" label="协议编号" maxlength="40" required="false" readonly="true" />
			<emp:text id="LmtApply.cus_id" label="客户码" maxlength="30" required="true" readonly="true"/>
			<emp:text id="LmtApply.cus_id_displayname" label="客户名称" required="true" readonly="true" cssElementClass="emp_field_text_readonly" colSpan="2"/>
			
			<emp:select id="LmtApply.cur_type" label="授信币种" required="false" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true"/>
			<emp:text id="LmtApply.crd_totl_amt" label="授信总额" maxlength="18" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtApply.totl_amt" label="非低风险授信总额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" colSpan="2"/>
			<emp:text id="LmtApply.crd_cir_amt" label="非低风险循环授信敞口" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtApply.crd_one_amt" label="非低风险一次性授信敞口" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<%if(!"GRP".equalsIgnoreCase((String)context.getDataValue("origin"))){ %>
			<emp:text id="LmtApply.lrisk_total_amt" label="低风险授信总额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" colSpan="2"/>
			<emp:text id="LmtApply.lrisk_cir_amt" label="低风险循环授信敞口" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtApply.lrisk_one_amt" label="低风险一次性授信敞口" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<%} %>
			<emp:text id="LmtApply.agr_froze_amt" label="已冻结金额" maxlength="18" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<%if("03".equals(app_type)){ %>
			<emp:text id="LmtApply.frozen_amt" label="冻结金额" maxlength="18" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly" />
			<%}else{ %>
			<emp:text id="LmtApply.unfroze_amt" label="解冻金额" maxlength="18" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<%} %>
			<emp:date id="LmtApply.start_date" label="授信起始日" required="false" dataType="Date" hidden="true"/>
			<emp:date id="LmtApply.end_date" label="授信到期日" required="false" dataType="Date" hidden="true"/>
			<emp:textarea id="LmtApply.memo" label="操作理由" maxlength="500" required="false" colSpan="2"/>
			<emp:pop id="LmtApply.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" />
			<emp:pop id="LmtApply.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" />
			<emp:text id="LmtApply.input_id_displayname" label="登记人" required="true" readonly="true"/>
			<emp:text id="LmtApply.input_br_id_displayname" label="登记机构" required="true" readonly="true"/>
			<emp:date id="LmtApply.input_date" label="登记日期" required="true" defvalue="$OPENDAY" readonly="true"/>
			
			<emp:select id="LmtApply.biz_type" label="授信业务类型" required="true" dictname="STD_ZB_BIZ_TYPE" readonly="true" hidden="true"/>
			<emp:text id="LmtApply.input_id" label="登记人" maxlength="20" required="true" hidden="true"/>
			<emp:text id="LmtApply.input_br_id" label="登记机构" maxlength="20" required="true" hidden="true"/>
			<emp:text id="LmtApply.manager_id" label="责任人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="LmtApply.manager_br_id" label="管理机构" maxlength="20" required="false" hidden="true"/>
			<emp:text id="LmtApply.app_type" label="申请类型" maxlength="20" required="false" hidden="true" defvalue="<%=app_type %>"/>
			<emp:text id="LmtApply.approve_status" label="审批状态" maxlength="20" required="false" hidden="true" defvalue="000"/>
			<emp:text id="LmtApply.flow_type" label="流程类型" maxlength="20" required="false" hidden="true" defvalue="01"/>
			<emp:date id="LmtApply.app_date" label="授信起始日" required="false" dataType="Date" hidden="true" defvalue="$OPENDAY"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="addLmtFrozen" label="确定" />
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
