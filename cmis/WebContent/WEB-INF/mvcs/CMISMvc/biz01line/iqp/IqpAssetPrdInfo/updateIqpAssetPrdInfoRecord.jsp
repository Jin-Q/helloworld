<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript"><!--
	
	/*--user code begin--*/
	/**页面加载时显示资产规模的值*/
	function onload(){
		var asset_perc = IqpAssetPrdInfo.asset_perc._getValue();
		//alert(asset_perc);
		var pro_amt = IqpAssetProApp.pro_amt._getValue();
		//alert(asset_perc);
		var asset_scale=Math.round((parseFloat(asset_perc))*(parseFloat(pro_amt))*100)/100;
		IqpAssetPrdInfo.asset_scale._setValue(''+asset_scale+'');
	};
	
	function doSub(){
		var form = document.getElementById("submitForm");
		if(IqpAssetPrdInfo._checkAll()){
			IqpAssetPrdInfo._toForm(form);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					//alert(o.responseText);
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					if(flag == "success"){
						alert("修改成功!");
						var url = '<emp:url action="queryIqpAssetPrdInfoList.do"/>?serno='+IqpAssetPrdInfo.serno._getValue();
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else {
						alert("修改异常!");
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
			return false;
		}
	};

	/**校验付息产品日期不超过1-31的范围  2014-08-04 邓亚辉*/
	function onChange(){
			var int_pint_date = IqpAssetPrdInfo.int_pint_date._getValue();
			if(int_pint_date>31||int_pint_date<1){
					alert("超出1-31的范围，请重新输入");
					IqpAssetPrdInfo.int_pint_date._setValue("");
				}
		}

	/**计算"资产规模"(资产规模=资产占比*项目金额)   2014-08-04   邓亚辉 */
	/*function  caculate(){
		var asset_perc=IqpAssetPrdInfo.asset_perc._getValue();
		var asset_scale;
		asset_scale=asset_perc*${context.pro_amt};
		IqpAssetPrdInfo.asset_scale._setValue(asset_scale);
		alert("计算成功");
		}*/
		function change(){
			IqpAssetPrdInfo.asset_scale._setValue("0");
			var asset_perc = IqpAssetPrdInfo.asset_perc._getValue();
			var pro_amt = IqpAssetProApp.pro_amt._getValue();
			//alert(pro_amt+"ccc"+asset_perc);
			var asset_scale=Math.round((parseFloat(asset_perc))*(parseFloat(pro_amt)))/100;
			//alert("000"+asset_scale);
			IqpAssetPrdInfo.asset_scale._setValue(''+asset_scale+'');
			};
	/*--user code end--*/
	/**
	新增"资产规模"，"附息产品付息日"的数据类型改成"Short"，"选择项赎回条款"、"强制性赎回条款" 、"终止条款"改为文本域
	2014-08-04
	邓亚辉
	*/
--></script>
</head>
<body class="page_content" onload="onload()">
	
	<emp:form id="submitForm" action="updateIqpAssetPrdInfoRecord.do" method="POST">
		<emp:gridLayout id="IqpAssetPrdInfoGroup" title="产品信息" maxColumn="2">
			<emp:text id="IqpAssetPrdInfo.serno" label="业务编号" maxlength="40" required="true" readonly="true"/>
			<emp:text id="IqpAssetPrdInfo.prd_id" label="产品代码" maxlength="40" required="true" readonly="true"/>
			<emp:text id="IqpAssetPrdInfo.prd_type" label="产品档次" maxlength="5" required="true" />
			<emp:text id="IqpAssetPrdInfo.prd_short_name" label="产品简称" maxlength="80" required="true" />
			<emp:text id="IqpAssetPrdInfo.prd_all_name" label="产品全称" maxlength="80" required="true" />
			<emp:select id="IqpAssetPrdInfo.prd_qlty" label="产品性质" required="true" dictname="STD_ZB_PRD_QLTY"/>
			<emp:select id="IqpAssetPrdInfo.prd_cur_type" label="产品币种" required="true" dictname="STD_ZX_CUR_TYPE"/>
			<emp:date id="IqpAssetPrdInfo.fore_end_date" label="预期到期日期" required="true" dataType="Date" />
			<emp:select id="IqpAssetPrdInfo.int_bill_type" label="息票品种" required="false" dictname="STD_ZB_INT_BILL_TYPE"/>
			<emp:text id="IqpAssetPrdInfo.int_pint_qnt" label="附息产品的每年付息次数" maxlength="38" required="false" dataType="Int" />
			<emp:text id="IqpAssetPrdInfo.int_pint_date" label="附息产品付息日" required="false" dataType="Short" onchange="onChange()"/>
			<emp:select id="IqpAssetPrdInfo.bill_ir_type" label="票面利率类型" required="false" dictname="STD_ZB_BILL_IR_TYPE"/>
			<emp:text id="IqpAssetPrdInfo.int_cal_mode" label="利息计算方式" maxlength="60" required="false" />
			<emp:text id="IqpAssetPrdInfo.base_ir" label="基础利率" maxlength="16" required="false" dataType="Rate"/>
			<emp:textarea id="IqpAssetPrdInfo.base_ir_mome" label="基础利率选择说明" maxlength="200" required="false" colSpan="2"/>
			<emp:text id="IqpAssetPrdInfo.rate_sprd" label="基本利差" maxlength="16" required="false" dataType="Rate"/>
			<emp:text id="IqpAssetPrdInfo.bill_ir" label="票面利率" maxlength="16" required="false" dataType="Rate"/>
			<emp:text id="IqpAssetPrdInfo.asset_perc" label="资产占比" maxlength="16" required="false" dataType="Percent" onchange="change()"/>	
			<emp:text id="IqpAssetPrdInfo.asset_scale" label="资产规模" maxlength="16" readonly="true" defvalue="0" dataType="Currency" required="false"  />
			<emp:text id="IqpAssetPrdInfo.lvlup_mode" label="信用增级方式" maxlength="16" required="false" />
			<emp:text id="IqpAssetPrdInfo.guarantee" label="担保人" maxlength="60" required="false" cssElementClass="emp_field_text_long" colSpan="2"/>
			<emp:select id="IqpAssetPrdInfo.assure_main" label="担保方式" required="false" dictname="STD_ZB_ASSURE_MEANS"/>
			<emp:text id="IqpAssetPrdInfo.choose_rebuy_item" label="选择项赎回条款" maxlength="600" required="false" cssElementClass="emp_field_text_long" colSpan="2"/>
			<emp:text id="IqpAssetPrdInfo.force_rebuy_item" label="强制性赎回条款" maxlength="600" required="false" cssElementClass="emp_field_text_long" colSpan="2"/>
			<emp:text id="IqpAssetPrdInfo.end_item" label="终止条款" maxlength="600" required="false" cssElementClass="emp_field_text_long" colSpan="2"/>
			<emp:textarea id="IqpAssetPrdInfo.remarks" label="备注" maxlength="600" required="false" colSpan="2"/>
			<emp:text id="IqpAssetProApp.pro_amt" label="项目金额" maxlength="16" hidden="true" dataType="Currency" defvalue="${context.pro_amt}" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="sub" label="修改" op="update"/>
			<emp:button id="reset" label="取消"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
