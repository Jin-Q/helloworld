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
	text-align: left;
	width: 300px;
}
</style>
<script type="text/javascript">
	
	/*--user code begin--*/
			function doReturn()
			{
				var url = '<emp:url action="queryIqpDepotAgrList.do"/>'+"&net_agr_no=${context.net_agr_no}"
                                                                       +"&mem_cus_id=${context.mem_cus_id}"
                                                                       +"&cus_id=${context.net_agr_no}"
															           +"&mem_manuf_type=${context.mem_manuf_type}";
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
			//异步修改数据
			function doUpdate(){
				if(IqpDepotAgr._checkAll()){
					var form = document.getElementById("submitForm");
					IqpDepotAgr._toForm(form);
					var handleSuccess = function(o){
					if(o.responseText !== undefined) {									
							var jsonstr = eval("("+o.responseText+")");
							var flag = jsonstr.flag;
							if(flag == 'success'){
								alert("修改成功！");
								var url = '<emp:url action="queryIqpDepotAgrList.do"/>'+"&net_agr_no=${context.net_agr_no}"
											                                +"&mem_cus_id=${context.mem_cus_id}"
											                                +"&cus_id=${context.net_agr_no}"
																            +"&mem_manuf_type=${context.mem_manuf_type}";
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
			};
			//设置购销合同
			function setPsale(data){
				IqpDepotAgr.psale_cont_no._setValue(data.psale_cont._getValue());
			}
			//设置订货计划信息
			function setDesgood(data){
				IqpDepotAgr.desgoods_plan_no._setValue(data.desgoods_plan_no._getValue());
			}
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateIqpDepotAgrRecord.do" method="POST">
		<emp:gridLayout id="IqpDepotAgrGroup" maxColumn="2" title="保兑仓协议">
			<emp:text id="IqpDepotAgr.net_agr_no" label="网络编号" maxlength="32" required="false" hidden="true"/>
			<emp:text id="IqpDepotAgr.depot_agr_no" label="保兑仓协议号" maxlength="32" hidden="true" />
			<emp:text id="IqpDepotAgr.cus_id" label="借款人客户码" maxlength="32" required="true" readonly="true"/>
			<emp:text id="IqpDepotAgr.cus_id_displayname" label="借款人客户名称" required="true" readonly="true"/>
			<emp:pop id="IqpDepotAgr.psale_cont_no" label="购销合同" url="queryIqpPsaleContPop.do" returnMethod="setPsale" required="true" />
			<emp:text id="IqpDepotAgr.fst_bail_perc" label="首次保证金比例" maxlength="16" required="false" dataType="Rate" />
			<emp:select id="IqpDepotAgr.fst_deliv_agreed" label="首次提货约定" dictname="STD_ZB_FST_AGREED" required="false" cssElementClass="emp_field_text_input2" hidden="true"/>
			<emp:text id="IqpDepotAgr.agreed_rate" label="约定比率" maxlength="10" required="false" dataType="Rate" hidden="true" colSpan="2"/>
			<emp:select id="IqpDepotAgr.contacc_freq" label="对账频率" dictname="STD_ZB_CONTACC_FREP" required="false" />
			<emp:pop id="IqpDepotAgr.desgoods_plan_no" label="订货计划" url="queryIqpDesbuyPlanPop.do" returnMethod="setDesgood" required="true" />	
			<emp:textarea id="IqpDepotAgr.memo" label="备注" maxlength="100" required="false" colSpan="2" />
			<emp:text id="IqpDepotAgr.input_id_displayname" label="登记人"   required="true" readonly="true"/>
			<emp:text id="IqpDepotAgr.input_br_id_displayname" label="登记机构"   required="true" readonly="true"/>
			<emp:date id="IqpDepotAgr.input_date" label="登记日期" required="true" defvalue="$OPENDAY" readonly="true"/>
			<emp:select id="IqpDepotAgr.status" label="状态" required="false" dictname="STD_ZB_STATUS" defvalue="1" readonly="true"/>
			<emp:text id="IqpDepotAgr.contacc_freq_unit" label="对账频率单位" maxlength="32" required="false" hidden="true"/>
			<emp:select id="IqpDepotAgr.contacc_mode" label="对账方式" required="false" dictname="STD_ZB_CONTACC_MODE" hidden="true"/>
			<emp:date id="IqpDepotAgr.start_date" label="协议起始日期" required="false" hidden="true"/>
			<emp:date id="IqpDepotAgr.end_date" label="协议到期日" required="false" hidden="true"/>
			<emp:text id="IqpDepotAgr.input_id" label="登记人" maxlength="32" required="false" hidden="true"/>
			<emp:text id="IqpDepotAgr.input_br_id" label="登记机构" maxlength="32" required="false" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="update" label="修改" op="update"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
