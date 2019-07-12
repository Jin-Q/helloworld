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

	/*--user code begin--*/
	function setCatalogPath(data){
		IqpAppDesbuyPlan.commo_name._setValue(data.locate);
		IqpAppDesbuyPlan.commo_name_displayname._setValue(data.locate_cn);
	};

	//计算货物总价
	function clcTotal(){
	    var desbuy_qnt=IqpAppDesbuyPlan.desbuy_qnt._getValue();
	    var price=IqpAppDesbuyPlan.desbuy_unit_price._getValue();
	    if(desbuy_qnt!=null && desbuy_qnt!="" &&price!=null &&price!=""){
		   	var desbuy_qntp=parseInt(IqpAppDesbuyPlan.desbuy_qnt._getValue());
		   	var pricep=parseFloat(IqpAppDesbuyPlan.desbuy_unit_price._getValue());
		   	var total=parseFloat(desbuy_qntp*pricep).toFixed(2);
		   	IqpAppDesbuyPlan.desbuy_total._setValue(total);
	    }
	};	

	//校验发货时间不能大于当前时间
	function checkDt(){
		var openDay='${context.OPENDAY}'
		var date2=IqpAppDesbuyPlan.fore_disp_date._getValue();
		if(date2 == null || date2 ==""){
            return;
		}
	 	var flag=CheckDate1BeforeDate2(openDay,date2);
	 	if(!flag){
	 	 	alert("发货时间不能早于当前时间！");
	 	 	IqpAppDesbuyPlan.fore_disp_date._setValue("");
	 	}
	};	

	function doSub(){
		if(IqpAppDesbuyPlan._checkAll()){
			var form = document.getElementById("submitForm");
			IqpAppDesbuyPlan._toForm(form);
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
						var url = '<emp:url action="queryIqpAppDesbuyPlanList.do"/>'+"&mem_cus_id=${context.mem_cus_id}"
                                   +"&serno=${context.serno}"
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

	function doBack(){
		var url = '<emp:url action="queryIqpAppDesbuyPlanList.do"/>?serno=${context.serno}'
                                                                +"&mem_cus_id=${context.mem_cus_id}"
                                                                +"&cus_id=${context.cus_id}"
			                                                    +"&mem_manuf_type=${context.mem_manuf_type}";
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	/*--user code end--*/
	
</script>
</head>
<body class="page_content" >
	
	<emp:form id="submitForm" action="updateIqpAppDesbuyPlanRecord.do" method="POST">
		<emp:gridLayout id="IqpAppDesbuyPlanGroup" maxColumn="2" title="订货计划信息">
			<emp:text id="IqpAppDesbuyPlan.for_manuf" label="供货厂商客户码" maxlength="30" required="true" readonly="true" />
			<emp:text id="IqpAppDesbuyPlan.for_manuf_displayname" label="供货厂商名称"  required="true" readonly="true"/>
			<emp:pop id="IqpAppDesbuyPlan.commo_name_displayname" label="商品名称" required="true" url="showCatalogManaTree.do?isMin=Y" returnMethod="setCatalogPath"/>
			<emp:text id="IqpAppDesbuyPlan.commo_name" label="商品名称" maxlength="100" required="false" hidden="true"/>
			<emp:text id="IqpAppDesbuyPlan.desbuy_qnt" label="订购数量" maxlength="16" required="true" onblur="clcTotal()" />
			<emp:select id="IqpAppDesbuyPlan.desbuy_qnt_unit" label="订购数量单位" required="true"  dictname="STD_ZB_UNIT" />
			<emp:text id="IqpAppDesbuyPlan.desbuy_unit_price" label="订购单价（元）" maxlength="16" required="true" dataType="Currency" onblur="clcTotal()" />
			<emp:text id="IqpAppDesbuyPlan.desbuy_total" label="订购总价（元）" maxlength="16" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly" />
			<emp:date id="IqpAppDesbuyPlan.fore_disp_date" label="预计发货日期"  required="true" onblur="checkDt()" />
			<emp:textarea id="IqpAppDesbuyPlan.memo" label="备注" maxlength="500" required="false" colSpan="2" />
			<emp:text id="IqpAppDesbuyPlan.input_id_displayname" label="登记人"  required="true" readonly="true"/>
			<emp:text id="IqpAppDesbuyPlan.input_br_id_displayname" label="登记机构"  required="true" readonly="true"/>
			<emp:text id="IqpAppDesbuyPlan.input_date" label="登记日期" maxlength="10" required="true" defvalue="$OPENDAY" readonly="true"/>
			<emp:text id="IqpAppDesbuyPlan.input_id" label="登记人" maxlength="20" required="false" hidden="true" />
			<emp:text id="IqpAppDesbuyPlan.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" />
			<emp:text id="IqpAppDesbuyPlan.start_date" label="起始日期" maxlength="10" required="false" hidden="true"/>
			<emp:text id="IqpAppDesbuyPlan.end_date" label="到期日期" maxlength="10" required="false" hidden="true"/>
			<emp:text id="IqpAppDesbuyPlan.serno" label="业务编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="IqpAppDesbuyPlan.desgoods_plan_no" label="订货流水号" maxlength="40" required="false" hidden="true"/>
		    <emp:text id="IqpAppDesbuyPlan.cus_id" label="客户码" maxlength="30" required="false" hidden="true" />
			<emp:text id="IqpAppDesbuyPlan.cus_id_displayname" label="客户名称" required="false" hidden="true"  />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="修改" op="update"/>
			<emp:button id="back" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
