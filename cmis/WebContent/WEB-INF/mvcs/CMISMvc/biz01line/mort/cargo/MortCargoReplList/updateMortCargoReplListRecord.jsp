<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doCalcu(){
		var amount = MortCargoReplList.qnt._getValue();
		var unit_price = MortCargoReplList.identy_unit_price._getValue();
		var total_price = parseFloat(unit_price)*parseFloat(amount);
		MortCargoReplList.identy_total._setValue(total_price.toString());
	}
	
	function doNext(){
		if(!MortCargoReplList._checkAll()){
			alert("请输入必填项！");
		}else{
			var handleSuccess = function(o) {
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("Parse jsonstr define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					if("success" == flag){
						alert("货物信息已修改成功!");
						window.opener.location.reload();
						window.close();
					}else{
						alert("更新失败!");
					}
				}
			};
			var handleFailure = function(o) {
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			var form = document.getElementById('submitForm');
			MortCargoReplList._toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);	
	 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action,callback,postData);
		}
	}

	function doClose(){
        window.close();
	}
	//押品所属目录返回方法
	function getCatalog(data){
		MortCargoReplList.value_no._setValue(data.value_no._getValue());
		MortCargoReplList.value_unit._setValue(data.unit._getValue());
		MortCargoReplList.guaranty_catalog._setValue(data.catalog_no._getValue());
		MortCargoReplList.guaranty_catalog_displayname._setValue(data.catalog_no_displayname._getValue());
		MortCargoReplList.produce_vender._setValue(data.produce_vender._getValue());
		MortCargoReplList.produce_area._setValue(data.produce_area._getValue());
		MortCargoReplList.produce_area_displayname._setValue(data.produce_area_displayname._getValue());
		MortCargoReplList.sale_area._setValue(data.sale_area._getValue());
		MortCargoReplList.sale_area_displayname._setValue(data.sale_area_displayname._getValue());
		MortCargoReplList.market_value._setValue(data.market_value._getValue());
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("fail" == flag){
					alert("此货物的押品所属目录非抵质押物押品类型项下目录类型，不能进行新增!");
					MortCargoReplList.value_no._setValue("");
					MortCargoReplList.value_unit._setValue("");
					MortCargoReplList.guaranty_catalog._setValue("");
					MortCargoReplList.guaranty_catalog_displayname._setValue("");
					MortCargoReplList.produce_vender._setValue("");
					MortCargoReplList.produce_area._setValue("");
					MortCargoReplList.produce_area_displayname._setValue("");
					MortCargoReplList.sale_area._setValue("");
					MortCargoReplList.sale_area_displayname._setValue("");
					MortCargoReplList.market_value._setValue("");
					return;
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var guaranty_no = MortCargoReplList.guaranty_no._getValue();
		var guaranty_catalog = data.catalog_no._getValue();
		var url = '<emp:url action="checkGuarantyCatalog.do"/>?guaranty_no='+guaranty_no+'&guaranty_catalog='+guaranty_catalog;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateMortCargoReplListRecord.do" method="POST">
		<emp:gridLayout id="MortCargoReplListGroup" maxColumn="2" title="货物质押清单">
			<emp:text id="MortCargoReplList.cargo_id" label="货物编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="MortCargoReplList.cargo_name" label="货物名称" maxlength="60" required="false" />
			<emp:text id="MortCargoReplList.value_no" label="价格编号" maxlength="40" required="false" hidden="true" />
			<emp:text id="MortCargoReplList.guaranty_no" label="押品编号" maxlength="40" required="true" hidden="true" />
			<emp:pop id="MortCargoReplList.guaranty_catalog_displayname" label="押品所属目录" url="queryIqpMortValueManaPopList.do?returnMethod=getCatalog" required="true" colSpan="2" readonly="true" cssElementClass="emp_field_text_long_readonly"/>
			<emp:text id="MortCargoReplList.guaranty_catalog" label="押品所属目录ID" maxlength="60" required="false" hidden="true"/>
			<emp:text id="MortCargoReplList.produce_vender" label="生产厂家" maxlength="200" required="true" readonly="true" cssElementClass="emp_field_text_long_readonly"/>
			<emp:text id="MortCargoReplList.produce_area" label="产地代码" maxlength="200" required="false" readonly="true" hidden="true"/>
			<emp:text id="MortCargoReplList.produce_area_displayname" label="产地" colSpan="2" readonly="true" cssElementClass="emp_field_text_long_readonly" />
			<emp:text id="MortCargoReplList.sale_area" label="销售区域ID" required="false" readonly="true" hidden="true"/>
			<emp:text id="MortCargoReplList.sale_area_displayname" label="销售区域" required="true" readonly="true" cssElementClass="emp_field_text_long_readonly"/>
			<emp:text id="MortCargoReplList.at_store" label="所属仓库" maxlength="20" required="false" />
			<emp:text id="MortCargoReplList.disp_bill_no" label="发货单号" maxlength="40" required="false" />
			
			<emp:select id="MortCargoReplList.value_unit" label="计价单位" required="true" dictname="STD_ZB_UNIT" readonly="true"/>
			<emp:text id="MortCargoReplList.market_value" label="市场价（元）" maxlength="18" required="true" dataType="Currency" onblur="doCalcu()" defvalue="0" cssElementClass="emp_currency_text_readonly" readonly="true"/>
			<emp:text id="MortCargoReplList.qnt" label="数量" maxlength="18" required="true" dataType="Currency" onblur="doCalcu()" defvalue="0" colSpan="2"/>
			<emp:text id="MortCargoReplList.identy_unit_price" label="银行认定单价（元）" maxlength="18" required="true" dataType="Currency" onblur="doCalcu()" defvalue="0"/>
			<emp:text id="MortCargoReplList.identy_total" label="银行认定总价（元）" maxlength="18" required="true" dataType="Currency" readonly="true" defvalue="0" cssElementClass="emp_currency_text_readonly"/>
			<emp:date id="MortCargoReplList.storage_date" label="入库日期" required="false" readonly="true"/>
			<emp:date id="MortCargoReplList.exware_date" label="出库日期" required="false" readonly="true"/>
			<emp:select id="MortCargoReplList.cargo_status" label="状态" required="false" dictname="STD_CARGO_STATUS" readonly="true" defvalue="01" colSpan="2"/>
			<emp:textarea id="MortCargoReplList.memo" label="备注" maxlength="600" required="false" colSpan="2" />
			<emp:text id="MortCargoReplList.model" label="型号" maxlength="40" required="false" hidden="true"/>
			
		</emp:gridLayout>
		<emp:gridLayout id="RegGroup" maxColumn="2" title="登记信息">
			<emp:text id="MortCargoReplList.input_id_displayname" label="登记人" required="false" defvalue="$currentUserName" readonly="true"/>
			<emp:text id="MortCargoReplList.input_br_id_displayname" label="登记机构" required="false" defvalue="$organName" readonly="true"/>
			<emp:text id="MortCargoReplList.input_id" label="登记人" maxlength="40" defvalue="$currentUserId" hidden="true"/>
			<emp:text id="MortCargoReplList.input_br_id" label="登记机构" maxlength="40" defvalue="$organNo" hidden="true"/>
			<emp:text id="MortCargoReplList.reg_date" label="登记日期" maxlength="10" required="false" defvalue="$OPENDAY" readonly="true"/>
			
			<emp:text id="MortCargoReplList.serno" label="关联业务流水号" maxlength="40" required="true" readonly="true" />
			<emp:text id="MortCargoReplList.oper" label="操作类型（1--初次入库，2--补货，3--置出，4--置入，5--提货）" maxlength="2" hidden="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="next" label="修改" />
			<emp:button id="close" label="关闭"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
