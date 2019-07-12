<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String updflag = context.getDataValue("updflag").toString();
	String app_type = context.getDataValue("LmtApply.app_type").toString();
	if(!"update".equals(updflag)){
		request.setAttribute("canwrite","");
	}
%>
<script type="text/javascript">
	
	/*--user code begin--*/
	function doReturn() {
		var url = '<emp:url action="queryLmtFrozenApplyList.do"/>?menuId=query_frozen';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
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

	function checkAmt(){
		var frozen_amt = LmtApply.frozen_amt._getValue();//冻结金额
		var unfroze_amt = LmtApply.unfroze_amt._getValue();//解冻金额
		if(frozen_amt>unfroze_amt){
			alert("冻结金额大于解冻金额！");
			return false;
		}
	}
	
	function onLoad(){
		LmtApply.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
		LmtApply.agr_no._obj.addOneButton('view12','查看',viewAgrInfo);
	}
	//客户信息查
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+LmtApply.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}	

	//协议信息查询
	function viewAgrInfo(){
		var url = "<emp:url action='getLmtAgrInfoViewPageNoTab.do'/>&agr_no="+LmtApply.agr_no._getValue()+"&showBut=N";
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height=538,width=1024,top=80,left=80,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
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
					alert("保存成功！");
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

	//提交流程
	function searchAmt4Wfi(Amt){
		var serno = LmtApply.serno._getValue();
		var cus_id = LmtApply.cus_id._getValue();
		var cus_id_displayname = LmtApply.cus_id_displayname._getValue();
		var approve_status = LmtApply.approve_status._getValue();
		WfiJoin.table_name._setValue("LmtApply");
		WfiJoin.pk_col._setValue("serno");
		WfiJoin.pk_value._setValue(serno);
		WfiJoin.wfi_status._setValue(approve_status);
		WfiJoin.status_name._setValue("approve_status");
		WfiJoin.appl_type._setValue("049");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：LMT_APP_FLOW
		WfiJoin.cus_id._setValue(cus_id);//客户码
		WfiJoin.cus_name._setValue(cus_id_displayname);//客户名称
		WfiJoin.amt._setValue(Amt);//金额
		WfiJoin.prd_name._setValue("授信额度冻结/解冻申请");//产品名称
		initWFSubmit(false);
	}

	function doSubmitLmtApply(){
		var result = LmtApply._checkAll();
		if(result){
			/*modify by wangj 2015-06-01  需求编号:XD141222087,法人账户透支需求变更  begin */
			//var app_type='<%=app_type%>';
			//if("03"==app_type&&!interRisk()){
			//	return ;
			//}
			/*modify by wangj 2015-06-01  需求编号:XD141222087,法人账户透支需求变更  end */
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
			var app_type = LmtApply.app_type._getValue();
			var serno = LmtApply.serno._getValue();
			var url = '<emp:url action="searchAmt4wfi.do"/>?serno='+serno+"&app_type="+app_type;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		}else {
           alert("请输入必填项!");
           return ;
		}
	}

	/*modify by wangj 2015-06-01  需求编号:XD141222087,法人账户透支需求变更  begin */	
	//风险拦截
	function interRisk(){
		var serno = LmtApply.serno._getValue();
		var _applType="";
		var _modelId="LmtApply";
		var _pkVal=serno;
		var _preventIdLst="FFFA2769264E264E984576B2CAFFE9F6";//单一法人额度冻结风险拦截方案（非流程） 
		var _urlPrv = "<emp:url action='procRiskInspect.do'/>&appltype="+_applType+"&pkVal=" + _pkVal + "&modelId=" + _modelId + "&pvId=" + _preventIdLst +"&timestamp=" + new Date();
	    var _retObj = window.showModalDialog(_urlPrv,"","dialogHeight=500px;dialogWidth=850px;");
	    if(!_retObj || _retObj == '2' || _retObj == '5'){
			if( _retObj == '5'){
				alert("执行风险拦截有错误，请检查！");
			}
			return false;
		}else{
			return true;
		}
	}
	/*modify by wangj 2015-06-01  需求编号:XD141222087,法人账户透支需求变更  end */
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onLoad()">
	<emp:tabGroup mainTab="main_tabs" id="main_tabs">
	<emp:tab label="额度冻结/解冻申请" id="main_tabs">
		<emp:form id="submitForm" action="updateLmtAgrFrozenApp.do" method="POST">
			<emp:gridLayout id="LmtAgrInfoGroup" maxColumn="2" title="额度冻结/解冻申请信息">
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
				<emp:date id="LmtApply.input_date" label="登记日期" required="true" readonly="true"/>
				
				<emp:select id="LmtApply.biz_type" label="授信业务类型" required="true" dictname="STD_ZB_BIZ_TYPE" readonly="true" colSpan="2" hidden="true"/>
				<emp:text id="LmtApply.input_id" label="登记人" maxlength="20" required="true" hidden="true"/>
				<emp:text id="LmtApply.input_br_id" label="登记机构" maxlength="20" required="true" hidden="true"/>
				<emp:text id="LmtApply.manager_id" label="责任人" maxlength="20" required="false" hidden="true"/>
				<emp:text id="LmtApply.manager_br_id" label="管理机构" maxlength="20" required="false" hidden="true"/>
				<emp:text id="LmtApply.updflag" label="修改标志" maxlength="20" required="false" defvalue="<%=updflag%>" hidden="true"/>
				<emp:select id="LmtApply.app_type" label="申请类型" required="false" dictname="STD_ZB_APP_TYPE" defvalue="<%=app_type%>" hidden="true" />
				<emp:select id="LmtApply.approve_status" label="审批状态" required="false" dictname="WF_APP_STATUS" hidden="true" />
			</emp:gridLayout>
			<%
				if("update".equals(updflag)){
			%>
			<div align="center">
				<br>
				<emp:button id="updateLmtFrozen" label="确定" />
				<emp:button id="return" label="返回"/>
				<emp:button id="submitLmtApply" label="提交" />
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
		</emp:form>
	</emp:tab>
	<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
</body>
</html>
</emp:page>
