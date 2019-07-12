<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">

	function refreshLmtSubApp(){
		LmtSigLmt_tabs.tabs.LmtSubApp_tab.refresh();
	}

	function doReturn(){
           window.close();
	}

    function getOrgID(data){
    	LmtSigLmt.manager_br_id._setValue(data.organno._getValue());
    	LmtSigLmt.manager_br_id_displayname._setValue(data.organname._getValue());
    	//PrdOrgApply.org_name._setValue(data.organname._getValue());        
	}

    function setconId(data){
		LmtSigLmt.manager_id_displayname._setValue(data.actorname._getValue());
    	LmtSigLmt.manager_id._setValue(data.actorno._getValue());
	}
	//申请日期大于当前日期
	/*function CheckExpDate(date1){
		var end = date1._obj.element.value;
		if(end!=null && end!="" ){
			var flag = CheckDateAfterToday(end);
			if(!flag){
				alert("您输入的日期应大于当前日期！");
				date1._obj.element.value="";
			}
		}
	}*/
	function doSave(){
		if(LmtSigLmt._checkAll()){
			var form = document.getElementById("submitForm");
			LmtSigLmt._toForm(form);
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
						alert("保存成功！");
						window.location.reload();
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
	}
	
	function check(){
		var amt = LmtSigLmt.lmt_amt._getValue();
		var single_amt ='${context.lmt_amt}';
		if(eval(amt)>eval(single_amt)){
		    alert("授信金额不能大于单户限额！");
		    LmtSigLmt.lmt_amt._setValue(single_amt);
		}
		return;
	}

</script>
</head>
<body class="page_content">
    <emp:tabGroup id="LmtSigLmt_tabs" mainTab="tab1">
    <emp:tab id="tab1" label="基本信息" needFlush="true" initial="true">
	<emp:form id="submitForm" action="saveSignCusLmt.do" method="POST">
		<emp:gridLayout id="LmtSigLmtGroup" title="申請信息" maxColumn="2">		
			<emp:text id="LmtSigLmt.serno" label="业务编号" maxlength="32" required="true" readonly="true" colSpan="2"/>
			<emp:text id="LmtSigLmt.cus_id" label="客户码" maxlength="32" readonly="true" />
			<emp:text id="LmtSigLmt.same_org_cnname" label="同业机构(行)名称" readonly="true"/>
			<emp:select id="LmtSigLmt.same_org_type" label="同业机构类型"  readonly="true" dictname="STD_ZB_INTER_BANK_ORG"/>
			<emp:select id="LmtSigLmt.app_cls" label="申请类别"  readonly="true" required="false" dictname="STD_ZB_APP_CLS" defvalue="02"/>		
			<emp:select id="LmtSigLmt.limit_type" label="额度类型" required="true" dictname="STD_ZB_LIMIT_TYPE" />
			<emp:select id="crd_grade" label="我行评级"  readonly="true" dictname="STD_ZB_FINA_GRADE" hidden="true"/>					
			<emp:text id="LmtSigLmt.owner_wrr" label="所有者权益(元)" maxlength="18" required="false" dataType="Currency" readonly="false" />
			<emp:text id="LmtSigLmt.asserts" label="总资产(万元)" maxlength="18" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="LmtSigLmt.cur_type" label="授信币种"  required="false" dictname="STD_ZX_CUR_TYPE"  defvalue="CNY" readonly="true"/>
			<emp:text id="LmtSigLmt.risk_quota" label="风险限额(元)" maxlength="18" required="false" dataType="Currency"/>
			<emp:text id="LmtSigLmt.lmt_amt" label="授信金额(元)" maxlength="18" required="true" colSpan="2" dataType="Currency" onblur="check()" defvalue="${context.lmt_amt}"/>
			<emp:select id="LmtSigLmt.term_type" label="期限类型" required="true" dictname="STD_ZB_TERM_TYPE" defvalue="001"/>
			<emp:text id="LmtSigLmt.term" label="期限" maxlength="6" required="true" defvalue="1"/>
			<emp:textarea id="LmtSigLmt.memo" label="备注" maxlength="200" required="false" colSpan="2" />
			<emp:text id="LmtSigLmt.batch_serno" label="批量包流水号"  required="false" colSpan="2" hidden="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="LmtSigLmtGroup" title="登记信息" maxColumn="2">
			<emp:text id="LmtSigLmt.manager_id_displayname" label="责任人" required="true" readonly="true"/>
			<emp:text id="LmtSigLmt.manager_br_id_displayname" label="管理机构" required="true" readonly="true"/>
			<emp:text id="LmtSigLmt.input_id_displayname" label="登记人" readonly="true"/>
			<emp:text id="LmtSigLmt.input_br_id_displayname" label="登记机构" required="false" readonly="true"/>
			<emp:date id="LmtSigLmt.input_date" label="登记日期" required="false" defvalue="$OPENDAY" readonly="true"/>
			<emp:date id="LmtSigLmt.app_date" label="申请日期" required="true" readonly="true"/>			
			<emp:text id="LmtSigLmt.manager_id" label="责任人" hidden="true" defvalue="${context.manager_id}"/>
			<emp:text id="LmtSigLmt.manager_br_id" label="管理机构" hidden="true" defvalue="${context.manager_br_id}"/>
			<emp:text id="LmtSigLmt.input_id" label="登记人" maxlength="20" hidden="true" />
			<emp:text id="LmtSigLmt.input_br_id" label="登记机构" maxlength="32" required="false" hidden="true"/>
			<emp:select id="LmtSigLmt.approve_status" label="审批状态" required="false" dictname="WF_APP_STATUS" defvalue="000" hidden="true"/>
			<emp:date id="LmtSigLmt.over_date" label="办结日期" hidden="true" required="false"/>
		    <emp:select id="LmtSigLmt.app_type" label="申请类型" dictname="STD_ZB_APP_TYPE" hidden="true" required="false" defvalue="01"/>
		    <emp:text id="LmtSigLmt.flow_type" label="流程类型" maxlength="6" required="false" hidden="true" defvalue="01"/>
		</emp:gridLayout>
		<div align="center">
			<emp:button id="save" label="保存" op="update"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	</emp:tab>
		<emp:tab id="LmtSubApp_tab" label="授信分项" url="queryLmtSigLmtLmtSubAppList.do?lmt_amt=${context.LmtSigLmt.lmt_amt}&serno=${context.LmtSigLmt.serno}"  needFlush="true" initial="false">
	</emp:tab>
	</emp:tabGroup>

</body>
</html>
</emp:page>
