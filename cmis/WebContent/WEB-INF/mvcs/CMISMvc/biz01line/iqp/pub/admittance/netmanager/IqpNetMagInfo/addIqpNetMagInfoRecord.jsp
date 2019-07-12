<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>
<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	background-color:#eee;
	text-align:left;
	width:450px;
}
</style>
<script type="text/javascript">

	/*--user code begin--*/
	function getCusInfo(data){
		IqpNetMagInfo.cus_id._setValue(data.cus_id._getValue());
		IqpNetMagInfo.cus_id_displayname._setValue(data.cus_name._getValue());
		IqpNetMagInfo.cdt_lvl._setValue(data.guar_crd_grade._getValue());
	}
       
	function getOrgID(data){
		IqpNetMagInfo.manager_br_id._setValue(data.organno._getValue());
		IqpNetMagInfo.manager_br_id_displayname._setValue(data.organname._getValue());
	}

	function setconId(data){
		IqpNetMagInfo.manager_id_displayname._setValue(data.actorname._getValue());
		IqpNetMagInfo.manager_id._setValue(data.actorno._getValue());
		IqpNetMagInfo.manager_br_id._setValue(data.orgid._getValue());
		IqpNetMagInfo.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		IqpNetMagInfo.manager_br_id_displayname._obj._renderReadonly(true);
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
					IqpNetMagInfo.manager_br_id._setValue(jsonstr.org);
					IqpNetMagInfo.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					IqpNetMagInfo.manager_br_id._setValue("");
					IqpNetMagInfo.manager_br_id_displayname._setValue("");
					IqpNetMagInfo.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = IqpNetMagInfo.manager_id._getValue();
					IqpNetMagInfo.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					IqpNetMagInfo.manager_br_id._setValue("");
					IqpNetMagInfo.manager_br_id_displayname._setValue("");
					IqpNetMagInfo.manager_br_id_displayname._obj._renderReadonly(false);
					IqpNetMagInfo.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = IqpNetMagInfo.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	function doSub(){
		if(IqpNetMagInfo._checkAll()){
   			var form = document.getElementById("submitForm");
   			IqpNetMagInfo._toForm(form);
   			var net_agr_no=IqpNetMagInfo.net_agr_no._getValue();
   			var app_type=IqpNetMagInfo.app_type._getValue();
   			alert(app_type);
   			var handleSuccess = function(o){
   				if(o.responseText !== undefined) {
   					var jsonstr = eval("("+o.responseText+")");
   					var flag = jsonstr.flag;
   					if(flag == "success"){
   	   					alert("保存成功！");
   	   					if(app_type=='02'){
   	   						var url = '<emp:url action="getIqpNetMagInfoUpdatePage.do"/>?net_agr_no='+net_agr_no+"&app_type="+app_type+"&op=add";
   	   	   				}else{
   	   	   			   		var url = '<emp:url action="getIqpNetMagInfoUpdatePage.do"/>?net_agr_no='+net_agr_no+"&app_type="+app_type+"&op=remove";
   	   	   	   			}
   	   	   	   			url = EMPTools.encodeURI(url);
		   	 			window.location = url;
   					}else {
   						alert("发生异常！");
   					}
   				}
   			};
   			var callback = {
   				success:handleSuccess,
   				failure:null
   			};
   			var postData = YAHOO.util.Connect.setForm(form);	
   			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
   		}
	}
	
	function doReturn(){
		var url = '<emp:url action="queryIqpNetMagInfoList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addIqpNetMagInfoRecord.do" method="POST">		
		<emp:gridLayout id="IqpNetMagInfoGroup" title="客户概况分析" maxColumn="2">
	        <emp:text id="IqpNetMagInfo.serno" label="业务流水号" maxlength="32" hidden="true" />
			<emp:pop id="IqpNetMagInfo.cus_id" label="客户码" url="queryAllCusPop.do?cusTypCondition=IqpNet&returnMethod=getCusInfo" required="true" readonly="true"/>
			<emp:text id="IqpNetMagInfo.cus_id_displayname" label="客户名称"  required="true" readonly="true"/>
			<emp:select id="IqpNetMagInfo.cdt_lvl" label="信用等级" dictname="STD_ZB_CREDIT_GRADE" required="true" readonly="true"/>
			<emp:textarea id="IqpNetMagInfo.main_prd" label="主要产品" maxlength="20" required="false" colSpan="2" />
			<emp:textarea id="IqpNetMagInfo.devlfore" label="发展前景" maxlength="100" required="false" colSpan="2" />
			<emp:textarea id="IqpNetMagInfo.app_resn" label="申请理由" maxlength="100" required="false" colSpan="2" />
			<emp:textarea id="IqpNetMagInfo.inte_income" label="综合收益概述" maxlength="100" required="false" colSpan="2" />
		</emp:gridLayout>
		<emp:gridLayout id="tradeGtoup" title="行业前景分析" maxColumn="2">
			<emp:text id="IqpNetMagInfo.trade_type_displayname" label="行业分类"   readonly="true"  required="true" cssElementClass="emp_field_text_input2"/>
			<emp:text id="IqpNetMagInfo.trade_type" label="行业分类" required="false" hidden="true"/>
			<emp:textarea id="IqpNetMagInfo.trade_devlfore" label="行业发展前景" maxlength="100" required="false" colSpan="2" />
		</emp:gridLayout>
		<emp:gridLayout id="LmtGroup" title="授信业务情况" maxColumn="2">
		    <emp:text id="IqpNetMagInfo.lmt_amt" label="直接授信额度" maxlength="18" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>  
			<emp:text id="IqpNetMagInfo.batair_lmt_amt" label="间接授信额度" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="IqpNetMagInfo.dealer_rebuyamt" label="经销商回购担保额度" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="IqpNetMagInfo.buyer_rebuy_amt" label="买方回购担保额度" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="IqpNetMagInfo.fin_bail_perc" label="融资保证金比例" maxlength="16" required="true" dataType="Rate" />
			</emp:gridLayout>
		<emp:gridLayout id="CoopGroup" title="业务合作方案" maxColumn="2">
			<emp:text id="IqpNetMagInfo.disp_term" label="发货期限" maxlength="10" required="true" />
			<emp:date id="IqpNetMagInfo.net_build_date" label="网络建立时间" required="true" readonly="true"/>
			<emp:select id="IqpNetMagInfo.coop_term_type" label="合作期限类型" required="true" dictname="STD_ZB_TERM_TYPE" />
			<emp:text id="IqpNetMagInfo.coop_term" label="合作期限" maxlength="3" required="true" />
			<emp:text id="IqpNetMagInfo.sale_reliterm" label="销售调剂期" maxlength="5" required="true" />
			<emp:text id="IqpNetMagInfo.rebuy_cond" label="回购条件" maxlength="32" required="true" />
			<emp:text id="IqpNetMagInfo.rebuy_perc" label="回购比例" maxlength="10" required="true" dataType="Percent" />
			<emp:checkbox id="IqpNetMagInfo.respond_mode" label="承担责任方式" dictname="STD_ZB_RESPOND_MODE" required="true" layout="false" colSpan="2"/>
			<emp:checkbox id="IqpNetMagInfo.dealer_lmt_type" label="经销商授信业务种类" required="true" dictname="STD_DEALER_BIZ_TYPE" layout="false"/>
			<emp:checkbox id="IqpNetMagInfo.provider_lmt_type" label="供应商授信业务种类" required="true" dictname="STD_PROVIDER_BIZ_TYPE" layout="false"/>
			<emp:checkbox id="IqpNetMagInfo.oversee_mode" label="监管方式" required="true" colSpan="2" dictname="STD_ZB_OVERSEE_MODE" layout="false"/>
			<emp:textarea id="IqpNetMagInfo.memo" label="备注" maxlength="100" required="false" colSpan="2" />
			<emp:pop id="IqpNetMagInfo.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" required="true" 
			           popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
			<emp:pop id="IqpNetMagInfo.manager_br_id_displayname" label="管理机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID"  required="true" 
			          popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no" readonly="true"/>
			<emp:text id="IqpNetMagInfo.input_id_displayname" label="登记人"  required="true" readonly="true"/>
			<emp:text id="IqpNetMagInfo.input_br_id_displayname" label="登记机构"   required="true" readonly="true"/>
			<emp:text id="IqpNetMagInfo.manager_id" label="责任人" maxlength="32" required="false" hidden="true"/>
			<emp:text id="IqpNetMagInfo.manager_br_id" label="管理机构" maxlength="32" required="false" hidden="true"/>
			<emp:text id="IqpNetMagInfo.input_id" label="登记人" maxlength="32" required="false" hidden="true"/>
			<emp:text id="IqpNetMagInfo.input_br_id" label="登记机构" maxlength="32" required="false" hidden="true"/>
			<emp:date id="IqpNetMagInfo.input_date" label="登记日期" required="true" readonly="true"/>
			<emp:select id="IqpNetMagInfo.status" label="状态" required="false" dictname="STD_ZB_STATUS" readonly="true"/>
			<emp:text id="IqpNetMagInfo.net_agr_no" label="网络协议编号" maxlength="32" required="false" hidden="true"/>
			<emp:select id="IqpNetMagInfo.flow_type" label="流程类型" hidden="true" defvalue="01"/>
			<emp:text id="IqpNetMagInfo.app_type" label="申请类型" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="保存" op="add"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

