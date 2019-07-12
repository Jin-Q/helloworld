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
width:450px;
border-color: #b7b7b7;
background-color: #e3e3e3;
}
</style>
<script type="text/javascript">
			function doReturn()
		    {
				var url = '<emp:url action="queryIqpDesbuyPlanList.do"/>?net_agr_no=${context.net_agr_no}'
                                                                        +"&mem_cus_id=${context.mem_cus_id}"
                                                                        +"&cus_id=${context.cus_id}"
					                                                    +"&mem_manuf_type=${context.mem_manuf_type}";
				url = EMPTools.encodeURI(url);
				window.location=url;
			};
	
	/*--user code begin--*/
			function getTotal()
			{
				var qnt=IqpDesbuyPlan.desbuy_qnt._getValue();
				var unit = IqpDesbuyPlan.desbuy_unit_price._getValue();
				var total = qnt*unit;
				IqpDesbuyPlan.desbuy_total._obj.element.value=total;
			}
			 function doUpdate(){
			   		if(IqpDesbuyPlan._checkAll()){
			   			var form = document.getElementById("submitForm");
			   			IqpDesbuyPlan._toForm(form);
			   			var handleSuccess = function(o){
			   				if(o.responseText !== undefined) {
			   					var jsonstr = eval("("+o.responseText+")");
			   					var flag = jsonstr.flag;
			   					if(flag == "success"){
			   	   					alert("保存成功！");
			   	   				var url = '<emp:url action="queryIqpDesbuyPlanList.do"/>?net_agr_no=${context.net_agr_no}'
                                                                                        +"&mem_cus_id=${context.mem_cus_id}"
                                                                                        +"&cus_id=${context.cus_id}"
									                                                    +"&mem_manuf_type=${context.mem_manuf_type}";
									url = EMPTools.encodeURI(url);
									window.location=url;
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
			  //校验发货时间不能大于当前时间
	 			function checkDt(){
	 	 			var openDay='${context.OPENDAY}'
	 	 			var date2=IqpDesbuyPlan.fore_disp_date._getValue();
	 	 	 		var flag=CheckDate1BeforeDate2(openDay,date2);
	 	 	 		if(!flag){
	 	 	 	 		alert("发货时间不能早于当前时间！");
	 	 	 	 	    IqpDesbuyPlan.fore_disp_date._setValue("");
	 	 	 	 	}
	 	 		}	

	 			function setCatalogPath(data){
	 				IqpDesbuyPlan.commo_name._setValue(data.id);
	 				IqpDesbuyPlan.commo_name_displayname._setValue(data.label);
				}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateIqpDesbuyPlanRecord.do" method="POST">
		<emp:gridLayout id="IqpDesbuyPlanGroup" maxColumn="2" title="年度订货计划">
			<emp:text id="IqpDesbuyPlan.desgoods_plan_no" label="订货流水号" maxlength="32" required="true" colSpan="2" readonly="true"/>
			<emp:text id="IqpDesbuyPlan.cus_id" label="客户码" readonly="true" required="true" />
			<emp:text id="IqpDesbuyPlan.cus_id_displayname" label="客户名称"   required="true" readonly="true"/>
			<emp:text id="IqpDesbuyPlan.commo_name" label="购买商品名称" required="true" hidden="true"/>
			<emp:pop id="IqpDesbuyPlan.commo_name_displayname" label="购买商品名称" required="true" url="showCatalogManaTree.do?isMin=Y" returnMethod="setCatalogPath"/>
			<emp:text id="IqpDesbuyPlan.desbuy_qnt" label="订购数量" maxlength="10" required="true" onblur="getTotal()"/>
			<emp:text id="IqpDesbuyPlan.desbuy_unit_price" label="订购单价" maxlength="18" required="true" dataType="Currency" onblur="getTotal()"/>
			<emp:text id="IqpDesbuyPlan.desbuy_total" label="订购总价" maxlength="18" required="true" dataType="Currency"  readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:date id="IqpDesbuyPlan.fore_disp_date" label="预计发货时间" required="true" onblur="checkDt()"/>	
			<emp:textarea id="IqpDesbuyPlan.memo" label="备注" maxlength="100" required="false" colSpan="2" />			
			<emp:text id="IqpDesbuyPlan.input_id_displayname" label="登记人"   required="true" readonly="true"/>
			<emp:text id="IqpDesbuyPlan.input_br_id_displayname" label="登记机构"   required="true" readonly="true"/>
			<emp:date id="IqpDesbuyPlan.input_date" label="登记日期" required="true" defvalue="$OPENDAY" readonly="true"/>
			<emp:text id="IqpDesbuyPlan.net_agr_no" label="网络编号" maxlength="32" required="false" hidden="true"/>
			<emp:date id="IqpDesbuyPlan.start_date" label="起始日期" required="false" hidden="true"/>
			<emp:date id="IqpDesbuyPlan.end_date" label="到期日期" required="false" hidden="true"/>
			<emp:select id="IqpDesbuyPlan.status" label="状态" required="false" dictname="STD_ZB_STATUS" hidden="true"/>
			<emp:text id="IqpDesbuyPlan.input_id" label="登记人" maxlength="32" required="false" hidden="true"/>
			<emp:text id="IqpDesbuyPlan.input_br_id" label="登记机构" maxlength="32" required="false" hidden="true"/>
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
