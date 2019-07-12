<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	//设置购销合同
	function setPsale(data){
		IqpAppDepotAgr.psale_cont._setValue(data.psale_cont._getValue());
	};
	//设置订货计划信息
	function setDesgood(data){
		IqpAppDepotAgr.desgoods_plan_no._setValue(data.desgoods_plan_no._getValue());
	};

	function doSub(){
		var form = document.getElementById("submitForm");
		if(IqpAppDepotAgr._checkAll()){
			IqpAppDepotAgr._toForm(form);
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
						alert("保存成功!");
						var url = '<emp:url action="queryIqpAppDepotAgrList.do"/>'+"&serno=${context.serno}"
		                          +"&mem_cus_id=${context.mem_cus_id}"
		                          +"&cus_id=${context.cus_id}"
		                          +"&mem_manuf_type=${context.mem_manuf_type}";
		                url = EMPTools.encodeURI(url);
		                window.location = url;
					}else {
						alert("保存失败!");
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
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
		}else {
			return;
		}
	};
	function firstChange(){
		var fst_deliv_agreed = IqpAppDepotAgr.fst_deliv_agreed._getValue();
        if("02" == fst_deliv_agreed){
        	IqpAppDepotAgr.agreed_rate._setValue("");
        	IqpAppDepotAgr.agreed_rate._obj._renderHidden(false);
        	IqpAppDepotAgr.agreed_rate._obj._renderRequired(true);
        }else{
        	IqpAppDepotAgr.agreed_rate._setValue("0");
        	IqpAppDepotAgr.agreed_rate._obj._renderHidden(true);
        	IqpAppDepotAgr.agreed_rate._obj._renderRequired(false);
        	
        }
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addIqpAppDepotAgrRecord.do" method="POST">
		<emp:gridLayout id="IqpAppDepotAgrGroup" title="保兑仓协议信息" maxColumn="2">
			<emp:text id="IqpAppDepotAgr.cus_id" label="借款人客户码" maxlength="30" required="true" readonly="true" />
			<emp:text id="IqpAppDepotAgr.cus_id_displayname" label="借款人客户名称"   required="true" readonly="true"/>
			<emp:pop id="IqpAppDepotAgr.psale_cont" label="购销合同" url="queryIqpAppPsaleContPop.do?serno=${context.serno}&mem_cus_id=${context.mem_cus_id}&returnMethod=setPsale" required="true" />
			<emp:text id="IqpAppDepotAgr.fst_bail_perc" label="首次保证金比例" maxlength="16" required="true" dataType="Rate" />
			<emp:select id="IqpAppDepotAgr.fst_deliv_agreed" label="首次提货约定" dictname="STD_ZB_FST_AGREED" onchange="firstChange()" required="false" cssElementClass="emp_field_text_input2" colSpan="2"/>
			<emp:text id="IqpAppDepotAgr.agreed_rate" label="约定比率" maxlength="10" required="false" hidden="true" dataType="Percent" colSpan="2"/>
			<emp:text id="IqpAppDepotAgr.contacc_freq" label="对账频率"  required="true" dataType="Int"/>
			<emp:select id="IqpAppDepotAgr.contacc_freq_unit" label="对账频率单位" required="true" dictname="STD_ZB_CONTACC_FREP"/>
			
			<emp:pop id="IqpAppDepotAgr.desgoods_plan_no" label="订货计划" url="queryIqpAppDesbuyPlanPop.do?serno=${context.serno}&mem_cus_id=${context.mem_cus_id}&returnMethod=setDesgood" required="true" />	
			
			<emp:select id="IqpAppDepotAgr.contacc_mode" label="对账方式"  required="false" dictname="STD_ZB_CONTACC_MODE" />

			<emp:textarea id="IqpAppDepotAgr.memo" label="备注" maxlength="500" required="false" colSpan="2"/>
			
			<emp:text id="IqpAppDepotAgr.input_id_displayname" label="登记人"   required="true" readonly="true"/>
			<emp:text id="IqpAppDepotAgr.input_br_id_displayname" label="登记机构"   required="true" readonly="true"/>
			<emp:text id="IqpAppDepotAgr.input_date" label="登记日期" maxlength="10" required="false" defvalue="$OPENDAY" readonly="true"/>
			
			<emp:text id="IqpAppDepotAgr.serno" label="业务编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="IqpAppDepotAgr.depot_agr_no" label="协议编号" maxlength="40" required="false" hidden="true" />
			<emp:text id="IqpAppDepotAgr.start_date" label="协议起始日期" maxlength="10" required="false" hidden="true" />
			<emp:text id="IqpAppDepotAgr.end_date" label="协议到期日期" maxlength="10" required="false" hidden="true" />
			<emp:text id="IqpAppDepotAgr.input_id" label="登记人" maxlength="20" required="false" hidden="true" />
			<emp:text id="IqpAppDepotAgr.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" /> 
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

