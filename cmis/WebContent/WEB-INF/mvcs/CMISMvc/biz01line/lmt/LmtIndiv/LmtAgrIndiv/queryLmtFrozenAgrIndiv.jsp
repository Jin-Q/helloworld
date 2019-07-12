<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String updflag = context.getDataValue("updflag").toString();
	String app_type = context.getDataValue("LmtAppIndiv.app_type").toString();
	String lst = "";
	if(context.containsKey("lst")){
		lst = (String)context.getDataValue("lst");
	}
	if(!"update".equals(updflag)){
		request.setAttribute("canwrite","");
	}
%>

<script type="text/javascript">
	
	function doReturn() {
		var lst = '<%=lst%>';
		var url = '';
		if(lst=="list"){
			url = '<emp:url action="queryLmtFrozenAppIndivList.do"/>';
		}else{
			url = '<emp:url action="queryLmtAgrIndivList.do"/>&menuId=lmtAgrIndiv';
		}
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/

	function doOnload(){
		LmtAppIndiv.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
	}
	//查看客户详情
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+LmtAppIndiv.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}

	function doUpdateLmtFrozen(){
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
					var serno = LmtAppIndiv.serno._getValue();
					var url = '<emp:url action="getLmtIndivFrozenUpdOrViePage.do"/>?serno='+serno+"&app_type=<%=app_type%>&updflag=update";
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
		var result = LmtAppIndiv._checkAll();
		if(result){
			page.dataGroups.dataGroup_in_formsubmitForm.toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	};

	//提交流程
	function doSubmitLmtAppIndiv(){
		var result = LmtAppIndiv._checkAll();
		if(result){
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr define error!"+e);
						return;
					}
					var flag = jsonstr.flag;
					var Amt = jsonstr.Amt;
					if(flag == "success"){
						searchAmt4Wfi(Amt);
				   	}else{
				   		alert("异步获取失败！");
					}
				}
			};
			var handleFailure = function(o){	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			}; 
			var app_type = '<%=app_type%>';
			var serno = LmtAppIndiv.serno._getValue();
			var url = '<emp:url action="searchAmt4wfi.do"/>?serno='+serno+"&app_type="+app_type;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		}
	}
	
	function searchAmt4Wfi(Amt){
		var serno = LmtAppIndiv.serno._getValue();
		var cus_id = LmtAppIndiv.cus_id._getValue();
		var cus_id_displayname = LmtAppIndiv.cus_id_displayname._getValue();
		var crd_totl_amt = LmtAppIndiv.crd_totl_amt._getValue();
		var approve_status = LmtAppIndiv.approve_status._getValue();
		WfiJoin.table_name._setValue("LmtAppIndiv");
		WfiJoin.pk_col._setValue("serno");
		WfiJoin.pk_value._setValue(serno);
		WfiJoin.wfi_status._setValue(approve_status);
		WfiJoin.status_name._setValue("approve_status");
		WfiJoin.appl_type._setValue("050");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：LMT_APP_FLOW
		WfiJoin.cus_id._setValue(cus_id);//客户码
		WfiJoin.cus_name._setValue(cus_id_displayname);//客户名称
		WfiJoin.amt._setValue(Amt);//金额
		WfiJoin.prd_name._setValue("授信额度冻结/解冻申请");//产品名称
		initWFSubmit(false);
	}

	//主管客户经理
	function setconId(data){
		LmtAppIndiv.manager_id._setValue(data.actorno._getValue());
		LmtAppIndiv.manager_id_displayname._setValue(data.actorname._getValue());
		LmtAppIndiv.manager_br_id._setValue(data.orgid._getValue());
		LmtAppIndiv.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//LmtAppIndiv.manager_br_id_displayname._obj._renderReadonly(true);
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
					LmtAppIndiv.manager_br_id._setValue(jsonstr.org);
					LmtAppIndiv.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					LmtAppIndiv.manager_br_id._setValue("");
					LmtAppIndiv.manager_br_id_displayname._setValue("");
					LmtAppIndiv.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = LmtAppIndiv.manager_id._getValue();
					LmtAppIndiv.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					LmtAppIndiv.manager_br_id._setValue("");
					LmtAppIndiv.manager_br_id_displayname._setValue("");
					LmtAppIndiv.manager_br_id_displayname._obj._renderReadonly(false);
					LmtAppIndiv.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = LmtAppIndiv.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	//主管机构
	function getOrgID(data){
		LmtAppIndiv.manager_br_id._setValue(data.organno._getValue());
		LmtAppIndiv.manager_br_id_displayname._setValue(data.organname._getValue());
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnload()">
	<emp:tabGroup mainTab="main_tabs" id="main_tab">
	<emp:tab label="授信基本信息" id="main_tabs" needFlush="true" initial="true">
	<emp:form id="submitForm" action="updateLmtAgrFrozenIndivApp.do" method="POST">
		<emp:gridLayout id="LmtAppIndivGroup" title="总额度" maxColumn="2">
			<emp:text id="LmtAppIndiv.agr_no" label="协议编号" maxlength="40" required="true" readonly="true"/>
			<emp:text id="LmtAppIndiv.serno" label="业务编号" maxlength="40" required="true" readonly="true"/>
			<emp:text id="LmtAppIndiv.cus_id" label="客户码" maxlength="30" required="true" readonly="true"/>
			<emp:text id="LmtAppIndiv.cus_id_displayname" label="客户名称"  required="true" readonly="true"/>
			<emp:select id="LmtAppIndiv.cur_type" label="授信币种" required="true" dictname="STD_ZX_CUR_TYPE" readonly="true"/>
			
			<emp:text id="LmtAppIndiv.crd_totl_amt" label="授信总额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			
			<emp:text id="LmtAppIndiv.totl_amt" label="非低风险非自助总额(元)" maxlength="18" dataType="Currency" required="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAppIndiv.self_amt" label="自助总额(元)" maxlength="18" dataType="Currency" required="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAppIndiv.crd_cir_amt" label="非低风险非自助循环额度(元)" maxlength="18" dataType="Currency" required="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAppIndiv.crd_one_amt" label="非低风险非自助一次性额度(元)" maxlength="18" dataType="Currency" required="true" cssElementClass="emp_currency_text_readonly"/>
			
			<emp:text id="LmtAppIndiv.lrisk_total_amt" label="低风险非自助总额(元)" maxlength="18" dataType="Currency" required="true" cssElementClass="emp_currency_text_readonly" colSpan="2"/>
			<emp:text id="LmtAppIndiv.lrisk_cir_amt" label="低风险非自助循环额度(元)" maxlength="18" dataType="Currency" required="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAppIndiv.lrisk_one_amt" label="低风险非自助一次性额度(元)" maxlength="18" dataType="Currency" required="true" cssElementClass="emp_currency_text_readonly"/>
			
			<emp:text id="LmtAppIndiv.agr_froze_amt" label="已冻结金额" maxlength="18" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<%if("03".equals(app_type)){ %>
			<emp:text id="LmtAppIndiv.frozen_amt" label="冻结金额" maxlength="18" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<%} %>
			<%if("04".equals(app_type)){ %>
			<emp:text id="LmtAppIndiv.unfroze_amt" label="解冻金额" maxlength="18" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<%} %>
			<emp:date id="LmtAppIndiv.totl_start_date" label="授信起始日" required="false" readonly="true"/>
			<emp:date id="LmtAppIndiv.totl_end_date" label="授信到期日" required="false" readonly="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="LmtAppIndivGroup" title="其他" maxColumn="2">
			<emp:textarea id="LmtAppIndiv.memo" label="备注" maxlength="800" required="false" colSpan="2" />
		</emp:gridLayout>
		<emp:gridLayout id="LmtAppIndivGroup" title="机构信息" maxColumn="2">
			<emp:pop id="LmtAppIndiv.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" />
			<emp:pop id="LmtAppIndiv.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" />
			<emp:text id="LmtAppIndiv.input_id_displayname" label="登记人"   required="true" readonly="true"/>
			<emp:text id="LmtAppIndiv.input_br_id_displayname" label="登记机构"  required="true" readonly="true"/>
			<emp:date id="LmtAppIndiv.input_date" label="登记日期" required="true" readonly="true"/>
			
			<emp:text id="LmtAppIndiv.manager_id" label="责任人" maxlength="20" required="true" hidden="true"/>
			<emp:text id="LmtAppIndiv.manager_br_id" label="责任机构" maxlength="20" required="true" hidden="true"/>
			<emp:text id="LmtAppIndiv.input_id" label="登记人" maxlength="20" required="true" hidden="true"/>
			<emp:text id="LmtAppIndiv.input_br_id" label="登记机构" maxlength="20" required="true" hidden="true"/>
			<emp:select id="LmtAppIndiv.approve_status" label="审批状态" dictname="WF_APP_STATUS" required="true" hidden="true"/>
		</emp:gridLayout>
	</emp:form>
			<%
				if("update".equals(updflag)){
			%>
			<div align="center">
				<br>
				<emp:button id="updateLmtFrozen" label="确定" />
				<emp:button id="return" label="返回"/>
				<emp:button id="submitLmtAppIndiv" label="提交" />
			</div>
			<%
				}else if("query".equals(updflag)){
			%>
			<div align="center">
				<br>
				<emp:button id="return" label="返回"/>
			</div>
			<%	
				}
			%>
	</emp:tab>
	<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
</body>
</html>
</emp:page>
