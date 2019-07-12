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
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">

	/*--user code begin--*/
	/** 登记信息begin **/
	function setconId(data){
		IqpExtensionPvp.manager_id_displayname._setValue(data.actorname._getValue());
		IqpExtensionPvp.manager_id._setValue(data.actorno._getValue());
		IqpExtensionPvp.manager_br_id._setValue(data.orgid._getValue());
		IqpExtensionPvp.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		IqpExtensionPvp.manager_br_id_displayname._obj._renderReadonly(true);
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
					IqpExtensionPvp.manager_br_id._setValue(jsonstr.org);
					IqpExtensionPvp.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag || "belg2team" == flag){//客户经理属于多个机构
					IqpExtensionPvp.manager_br_id._setValue("");
					IqpExtensionPvp.manager_br_id_displayname._setValue("");
					IqpExtensionPvp.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = IqpExtensionPvp.manager_id._getValue();
					IqpExtensionPvp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					IqpExtensionPvp.manager_br_id._setValue("");
					IqpExtensionPvp.manager_br_id_displayname._setValue("");
					IqpExtensionPvp.manager_br_id_displayname._obj._renderReadonly(false);
					IqpExtensionPvp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
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
		var manager_id = IqpExtensionPvp.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	function getOrgID(data){
		IqpExtensionPvp.manager_br_id._setValue(data.organno._getValue());
		IqpExtensionPvp.manager_br_id_displayname._setValue(data.organname._getValue());
	};
	/** 登记信息end **/
	function selCusId(data){
		IqpExtensionPvp.agr_no._setValue(data.agr_no._getValue());
		IqpExtensionPvp.fount_bill_no._setValue(data.fount_bill_no._getValue());
		IqpExtensionPvp.fount_cont_no._setValue(data.fount_cont_no._getValue());
		IqpExtensionPvp.cus_id._setValue(data.cus_id._getValue());
		IqpExtensionPvp.cus_id_displayname._setValue(data.cus_id_displayname._getValue());
		IqpExtensionPvp.fount_cur_type._setValue(data.fount_cur_type._getValue());
		IqpExtensionPvp.fount_loan_amt._setValue(data.fount_loan_amt._getValue());
		IqpExtensionPvp.fount_loan_balance._setValue(data.fount_loan_balance._getValue());
		IqpExtensionPvp.fount_rate._setValue(data.fount_rate._getValue());
		IqpExtensionPvp.fount_start_date._setValue(data.fount_start_date._getValue());
		IqpExtensionPvp.fount_end_date._setValue(data.fount_end_date._getValue());
		IqpExtensionPvp.extension_amt._setValue(data.extension_amt._getValue());
		IqpExtensionPvp.extension_date._setValue(data.extension_date._getValue());
		IqpExtensionPvp.base_rate._setValue(data.base_rate._getValue());
		IqpExtensionPvp.extension_rate._setValue(data.extension_rate._getValue());
		IqpExtensionPvp.memo._setValue(data.memo._getValue());		
		IqpExtensionPvp.manager_id_displayname._setValue(data.manager_id_displayname._getValue());
		IqpExtensionPvp.manager_br_id_displayname._setValue(data.manager_br_id_displayname._getValue());
		IqpExtensionPvp.manager_br_id._setValue(data.manager_br_id._getValue());
		IqpExtensionPvp.manager_id._setValue(data.manager_id._getValue());
		
		var prd_id = data.prd_id._getValue();		
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
					if(prd_id < '2' ){	//贷款类
						IqpExtensionPvp.base_rate._obj._renderHidden(false);
						IqpExtensionPvp.base_rate._obj._renderRequired(true);
					}else{
						IqpExtensionPvp.base_rate._obj._renderHidden(true);
						IqpExtensionPvp.base_rate._obj._renderRequired(false);
					}
				}else {
					alert("此协议已有在途出账!");
					IqpExtensionPvp.agr_no._setValue("");
					IqpExtensionPvp.fount_bill_no._setValue("");
					IqpExtensionPvp.fount_cont_no._setValue("");
					IqpExtensionPvp.cus_id._setValue("");
					IqpExtensionPvp.cus_id_displayname._setValue("");
					IqpExtensionPvp.fount_cur_type._setValue("");
					IqpExtensionPvp.fount_loan_amt._setValue("");
					IqpExtensionPvp.fount_loan_balance._setValue("");
					IqpExtensionPvp.fount_rate._setValue("");
					IqpExtensionPvp.fount_start_date._setValue("");
					IqpExtensionPvp.fount_end_date._setValue("");
					IqpExtensionPvp.extension_amt._setValue("");
					IqpExtensionPvp.extension_date._setValue("");
					IqpExtensionPvp.base_rate._setValue("");
					IqpExtensionPvp.extension_rate._setValue("");
					IqpExtensionPvp.memo._setValue("");		
					IqpExtensionPvp.manager_id_displayname._setValue("");
					IqpExtensionPvp.manager_br_id_displayname._setValue("");
					IqpExtensionPvp.manager_br_id._setValue("");
					IqpExtensionPvp.manager_id._setValue("");
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
		var url="<emp:url action='checkUnique.do'/>&type=ExtensionAgrCheck&value="+data.agr_no._getValue();
		var postData = YAHOO.util.Connect.setForm();	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
	};
	function doAddIqpExtensionPvp(){
		//added by yagnzy 20140829 放款改造  begin
		var buttonObjWait = document.getElementById('button_addIqpExtensionPvp');
		buttonObjWait.disabled=true;
		buttonObjWait.innerHTML='请稍等..';
		//added by yagnzy 20140829 放款改造  end		
		var form = document.getElementById("submitForm");
		if(IqpExtensionPvp._checkAll()){
			IqpExtensionPvp._toForm(form);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						//added by yangzy 20140829 放款改造  begin
						buttonObjWait.disabled=false;
						buttonObjWait.innerHTML='确定';
						//added by yangzy 20140829 放款改造  end 
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					var serno = jsonstr.serno;
					if(flag == "success"){
						alert("保存成功！");
						var url = '<emp:url action="getIqpExtensionPvpUpdatePage.do"/>?op=update&sub_button=true&hidden_button=true&restrictUsed=false&serno='+serno;
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else{
						//added by yangzy 20140829 放款改造  begin
						buttonObjWait.disabled=false;
						buttonObjWait.innerHTML='确定';
						//added by yangzy 20140829 放款改造  end 
						alert("保存失败！");
					}
				}
			};
			var handleFailure = function(o){
				//added by yangzy 20140829 放款改造  begin
				buttonObjWait.disabled=false;
				buttonObjWait.innerHTML='确定';
				//added by yangzy 20140829 放款改造  end 
				alert("异步请求出错！");	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};

			var url = '<emp:url action="addIqpExtensionPvpRecord.do"/>';
			url = EMPTools.encodeURI(url);
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData)
		}else {
			//added by yangzy 20140829 放款改造  begin
			buttonObjWait.disabled=false;
			buttonObjWait.innerHTML='确定';
			//added by yangzy 20140829 放款改造  end 
			return false;
		}
	};
	function doReturn() {
		var url = '<emp:url action="queryIqpExtensionPvpList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addIqpExtensionPvpRecord.do" method="POST">
		
		<emp:gridLayout id="IqpExtensionPvpGroup" title="待出账展期协议" maxColumn="2">
			<emp:text id="IqpExtensionPvp.serno" label="业务编号" maxlength="40" required="false" hidden="true" />
			<emp:pop id="IqpExtensionPvp.agr_no" label="展期协议编号"
			 url="extensionAgrNoPop.do?orgId=${context.organNo}&returnMethod=selCusId" required="true" />
		</emp:gridLayout>
		<emp:gridLayout id="IqpExtensionPvpGroup" title="原借据信息" maxColumn="2">
			<emp:text id="IqpExtensionPvp.fount_bill_no" label="原借据编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="IqpExtensionPvp.fount_cont_no" label="原合同编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="IqpExtensionPvp.cus_id" label="客户码" maxlength="40" required="true" readonly="true" />
			<emp:text id="IqpExtensionPvp.cus_id_displayname" label="客户名称" colSpan="2"
			cssElementClass="emp_field_text_cusname" readonly="true" />
			<emp:select id="IqpExtensionPvp.fount_cur_type" label="原币种" required="true" dictname="STD_ZX_CUR_TYPE" readonly="true" />
			<emp:text id="IqpExtensionPvp.fount_loan_amt" label="原贷款金额" maxlength="18" required="true" dataType="Currency" readonly="true" />
			<emp:text id="IqpExtensionPvp.fount_loan_balance" label="原贷款余额" maxlength="18" required="true" dataType="Currency" readonly="true" />
			<emp:text id="IqpExtensionPvp.fount_rate" label="原执行利率(年)" maxlength="16" required="true" dataType="Rate" readonly="true" />
			<emp:date id="IqpExtensionPvp.fount_start_date" label="原起贷日期" required="true" readonly="true" />
			<emp:date id="IqpExtensionPvp.fount_end_date" label="原止贷日期" required="true" readonly="true" />
		</emp:gridLayout>
		<emp:gridLayout id="IqpExtensionPvpGroup" title="展期出账信息" maxColumn="2">
			<emp:text id="IqpExtensionPvp.extension_amt" label="展期金额" maxlength="18" required="true" dataType="Currency" readonly="true" />
			<emp:date id="IqpExtensionPvp.extension_date" label="展期到期日期" required="true" readonly="true" />
			<emp:text id="IqpExtensionPvp.base_rate" label="基准利率(年)" maxlength="16" required="true" dataType="Rate" readonly="true" />
			<emp:text id="IqpExtensionPvp.extension_rate" label="展期利率(年)" maxlength="16" required="true" dataType="Rate" readonly="true" />
			<emp:textarea id="IqpExtensionPvp.memo" label="备注" maxlength="250" required="false" colSpan="2" readonly="true" />
		</emp:gridLayout>
		<emp:gridLayout id="IqpExtensionPvpGroup" maxColumn="2" title="登记信息">
			<!-- modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin -->
			<emp:pop id="IqpExtensionPvp.manager_id_displayname" label="责任人" required="true" readonly="false" 
			 url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="IqpExtensionPvp.manager_br_id_displayname" label="责任机构"  required="true" url="querySOrgPop.do?restrictUsed=false" 
			 returnMethod="getOrgID" cssElementClass="emp_pop_common_org" readonly="false"/>
			 <!-- modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end -->
			<emp:text id="IqpExtensionPvp.input_id_displayname" label="登记人" readonly="true" required="true" defvalue="$currentUserName"/>
			<emp:text id="IqpExtensionPvp.input_br_id_displayname" label="登记机构" readonly="true" required="true" defvalue="$organName" />
			<emp:date id="IqpExtensionPvp.input_date" label="登记日期" required="true" defvalue="$OPENDAY" readonly="true" />	
			<emp:select id="IqpExtensionPvp.approve_status" label="申请状态" required="true"
			dictname="WF_APP_STATUS" defvalue="000" readonly="true" />
			<emp:text id="IqpExtensionPvp.manager_br_id" label="责任机构"  required="true" hidden="true"/>
			<emp:text id="IqpExtensionPvp.manager_id" label="责任人" required="true" hidden="true"  />
			<emp:text id="IqpExtensionPvp.input_id" label="登记人" required="true"  hidden="true" defvalue="$currentUserId"/>
			<emp:text id="IqpExtensionPvp.input_br_id" label="登记机构" required="true" defvalue="$organNo" hidden="true"  />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="addIqpExtensionPvp" label="确定" />
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>