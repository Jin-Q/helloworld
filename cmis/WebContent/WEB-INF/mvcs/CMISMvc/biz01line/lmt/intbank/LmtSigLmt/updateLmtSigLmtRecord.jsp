<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>
<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	text-align:left;
	width:180px;
}
<%
String cus_id = request.getParameter("cus_id");
String limit_type = request.getParameter("limit_type");
%>
</style>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script type="text/javascript">
	function refreshLmtSubApp(){
		LmtSigLmt_tabs.tabs.LmtSubApp_tab.refresh();
	}
	
	function doReturn(){
		var url = '<emp:url action="queryLmtSigLmtList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url
	};

	function doLoad(){
		LmtSigLmt.app_cls._setValue("01");
	}
	
    function getOrgID(data){
    	LmtSigLmt.manager_br_id._setValue(data.organno._getValue());
    	LmtSigLmt.manager_br_id_displayname._setValue(data.organname._getValue());
    	//PrdOrgApply.org_name._setValue(data.organname._getValue());        
	}

    function setconId(data){
    	LmtSigLmt.manager_id_displayname._setValue(data.actorname._getValue());
    	LmtSigLmt.manager_id._setValue(data.actorno._getValue());
    	LmtSigLmt.manager_br_id._setValue(data.orgid._getValue());
    	LmtSigLmt.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
    	//LmtSigLmt.manager_br_id_displayname._obj._renderReadonly(true);
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
					LmtSigLmt.manager_br_id._setValue(jsonstr.org);
					LmtSigLmt.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					LmtSigLmt.manager_br_id._setValue("");
					LmtSigLmt.manager_br_id_displayname._setValue("");
					LmtSigLmt.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = LmtSigLmt.manager_id._getValue();
					LmtSigLmt.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
   				}else if("yteam"==flag){
   					LmtSigLmt.manager_br_id._setValue("");
   					LmtSigLmt.manager_br_id_displayname._setValue("");
   					LmtSigLmt.manager_br_id_displayname._obj._renderReadonly(false);
   					LmtSigLmt.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
   				}
   			}
   		};
   		var handleFailure = function(o) {
   		};
   		var callback = {
   			success :handleSuccess,
   			failure :handleFailure
   		};
   		var manager_id = LmtSigLmt.manager_id._getValue();
   		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
   		url = EMPTools.encodeURI(url);
   		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
   	}
	
	//申请日期大于当前日期
	function CheckExpDate(date1){
		var end = date1._obj.element.value;
		var openDay='${context.OPENDAY}';
		if(end!=null && end!="" ){
			var flag = CheckDate1BeforeDate2(openDay,end);
			if(!flag){
				alert("您输入的日期应大于当前日期！");
				date1._obj.element.value="";
			}
		}
	}
	
	function doAdd(){
		if(LmtSigLmt._checkAll()){
			var form = document.getElementById("submitForm");
			LmtSigLmt._toForm(form);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					}catch(e){
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					if( flag == "success"){
						var serno = jsonstr.serno;
						alert("保存成功！");
						var url = '<emp:url action="getLmtSigLmtUpdatePage.do"/>?serno='+serno;
						url = EMPTools.encodeURI(url);
						window.location=url
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
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	};	 	
</script>
</head>
<body class="page_content" onload="doLoad()">
    <emp:tabGroup id="LmtSigLmt_tabs" mainTab="tab1">
		<emp:tab id="tab1" label="基本信息" needFlush="true" initial="true">
			<emp:form id="submitForm" action="updateLmtSigLmtRecord.do" method="POST">
				<emp:gridLayout id="LmtSigLmtGroup" title="申請信息" maxColumn="2">		
					<emp:text id="LmtSigLmt.serno" label="业务编号" maxlength="32" hidden="true" required="false" readonly="true" colSpan="2" cssElementClass="emp_field_text_readonly"/>
					<emp:text id="LmtSigLmt.cus_id" label="客户码" maxlength="32" readonly="true" defvalue="<%=cus_id%>"/>
					<emp:text id="LmtSigLmt.same_org_cnname" label="同业机构(行)名称" readonly="true" />
					<emp:select id="LmtSigLmt.same_org_type" label="同业机构类型"  readonly="true" dictname="STD_ZB_INTER_BANK_ORG"/>
					<emp:select id="LmtSigLmt.app_cls" label="申请类别"  readonly="true" required="false" dictname="STD_ZB_APP_CLS"/>		
					<emp:select id="LmtSigLmt.limit_type" label="额度类型" required="true" defvalue="<%=limit_type%>" readonly="true" dictname="STD_ZB_LIMIT_TYPE" />
					<emp:select id="LmtSigLmt.crd_grade" label="我行评级"  readonly="true" dictname="STD_ZB_FINA_GRADE" hidden="true"/>					
					<emp:text id="LmtSigLmt.owner_wrr" label="所有者权益(元)" maxlength="18" required="false" dataType="Currency" readonly="false"/>
					<emp:text id="LmtSigLmt.asserts" label="总资产(万元)" maxlength="18" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
					<emp:select id="LmtSigLmt.cur_type" label="授信币种"  required="false" dictname="STD_ZX_CUR_TYPE" readonly="true" defvalue="CNY"/>
					<emp:text id="LmtSigLmt.risk_quota" label="风险限额(元)" maxlength="18" required="false" dataType="Currency" />
					<emp:text id="LmtSigLmt.lmt_amt" label="授信金额(元)" maxlength="18" required="true" colSpan="2" dataType="Currency" />
					<emp:select id="LmtSigLmt.term_type" label="期限类型" required="true" dictname="STD_ZB_TERM_TYPE" />
					<emp:text id="LmtSigLmt.term" label="期限" maxlength="6" required="true" dataType="Int"/>
					<emp:textarea id="LmtSigLmt.memo" label="备注" maxlength="200" required="false" colSpan="2" />
				</emp:gridLayout>
				<emp:gridLayout id="LmtSigLmtGroup" title="登记信息" maxColumn="2">
					<emp:pop id="LmtSigLmt.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" required="true"/>
					<emp:pop id="LmtSigLmt.manager_br_id_displayname" label="管理机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" required="true" />
					<emp:text id="LmtSigLmt.input_id_displayname" label="登记人"   defvalue="$currentUserName" readonly="true"/>
					<emp:text id="LmtSigLmt.input_br_id_displayname" label="登记机构" defvalue="$organName"  required="false" readonly="true"/>
					<emp:date id="LmtSigLmt.input_date" label="登记日期" required="false" readonly="true" defvalue="$OPENDAY"/>
					<emp:date id="LmtSigLmt.app_date" label="申请日期" required="true"  readonly="true" defvalue="$OPENDAY"/>			
					<emp:pop id="LmtSigLmt.manager_id" label="责任人" url="" hidden="true" />
					<emp:pop id="LmtSigLmt.manager_br_id" label="管理机构" url="null" hidden="true" />
					<emp:text id="LmtSigLmt.input_id" label="登记人" maxlength="20" hidden="true"  defvalue="$currentUserId"/>
					<emp:text id="LmtSigLmt.input_br_id" label="登记机构" maxlength="32" required="false" hidden="true"  defvalue="$organNo"/>
					<emp:select id="LmtSigLmt.approve_status" label="审批状态" required="false" dictname="WF_APP_STATUS" defvalue="000" hidden="true"/>
					<emp:date id="LmtSigLmt.over_date" label="办结日期" hidden="true" required="false"/>
				    <emp:select id="LmtSigLmt.app_type" label="申请类型" dictname="STD_ZB_APP_TYPE" hidden="true" required="false"/>
				    <emp:text id="LmtSigLmt.flow_type" label="流程类型" maxlength="6" required="false" hidden="true" defvalue="01"/>
				</emp:gridLayout>
				<div align="center">
					<emp:button id="add" label="保存" op="update"/>
				</div>
			</emp:form>
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
</body>
	<div align="center">
		<emp:button id="return" label="返回"/>
	</div> 
</html>
</emp:page>
