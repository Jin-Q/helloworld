<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>测算页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input2 {
	vertical-align: text-bottom;
	padding-top: 4px;
	text-align: left;
	width: 450px; 
}
</style>

<script type="text/javascript">
	/*--user code begin--*/
	/***等于(公司经营性现金流+自身拥有土地使用权证的土地处置收入+自身拥有的已办理过户手续
	的上市公司股权分红和股权转让收入+自身拥有的其他资产处置收入+已明确归属借款人的专项规费收入)
	/公司在所有银行业金融机构贷款（包括信托等）总余额***/
	function doGetCalCashCoverRate(){
		if(CalCashCoverRate._checkAll()){
			var rate ;
			acsh_cover = CalCashCoverRate.acsh_cover._getValue()*1; //公司经营性现金流
			land_income = CalCashCoverRate.land_income._getValue()*1; //自身拥有土地使用权证的土地处置收入
			stk_income = CalCashCoverRate.stk_income._getValue()*1; //自身拥有的已办理过户手续的上市公司股权分红和股权转让收入
			asset_income = CalCashCoverRate.asset_income._getValue()*1; //自身拥有的其他资产处置收入
			special_income = CalCashCoverRate.special_income._getValue()*1; //已明确归属借款人的专项规费收入
			acsh_sum = CalCashCoverRate.acsh_sum._getValue()*1; //公司在所有银行业金融机构贷款（包括信托等）总余额
			rate = (acsh_cover+land_income+stk_income+asset_income+special_income)/acsh_sum;
			if(acsh_sum == 0 || acsh_sum == null){
				alert("公司在所有银行业金融机构贷款（包括信托等）总余额应该大于0");
				return false;
			}else{
				CalCashCoverRate.acsh_rate._setValue(rate+"");
			}
		}else {
			alert("请检查必输项！");
		}
	};
	function doSelect(){
		var data = CalCashCoverRate.acsh_rate._getValue();
		if(data == null || data.length == 0){
			alert('请测算出结果！');
			return;
		}
		window.opener["${context.returnMethod}"](data);
		window.close();
	};
	/*--user code end--*/
</script>
</head>
<body class="page_content" >
	<emp:form id="submitForm" action="" method="POST">
		<emp:gridLayout id="CalCashCoverRateGroup" title="融资平台贷款现金流信息" maxColumn="2">
			<emp:text id="CalCashCoverRate.cus_id" label="客户码"  readonly="true" defvalue="${context.cus_id}"
			colSpan="2" cssLabelClass="emp_field_text_input2" hidden="true"/>
			<emp:text id="CalCashCoverRate.acsh_cover" label="公司经营性现金流(元)"  maxlength="18" required="true" dataType="Currency"
			colSpan="2" cssLabelClass="emp_field_text_input2" />
			<emp:text id="CalCashCoverRate.land_income" label="自身拥有土地使用权证的土地处置收入(元)"  dataType="Currency" maxlength="18"
			required="true" colSpan="2" cssLabelClass="emp_field_text_input2"/>
			<emp:text id="CalCashCoverRate.stk_income" label="自身拥有的已办理过户手续的上市公司股权分红和股权转让收入(元)" 
			cssLabelClass="emp_field_text_input2" required="true" colSpan="2" dataType="Currency" maxlength="18"/>
			<emp:text id="CalCashCoverRate.asset_income" label="自身拥有的其他资产处置收入(元)"  dataType="Currency" maxlength="18"
			required="true" colSpan="2" cssLabelClass="emp_field_text_input2"/>
			<emp:text id="CalCashCoverRate.special_income" label="已明确归属借款人的专项规费收入(元)"  dataType="Currency" maxlength="18"
			required="true" colSpan="2" cssLabelClass="emp_field_text_input2"/>
			<emp:text id="CalCashCoverRate.acsh_sum" label="公司在所有银行业金融机构贷款（包括信托等）总余额(元)"  
			required="true" colSpan="2" cssLabelClass="emp_field_text_input2" dataType="Currency" maxlength="18"/>
			<emp:text id="CalCashCoverRate.acsh_rate" label="现金流覆盖率"  required="false" 
			colSpan="2" cssLabelClass="emp_field_text_input2" readonly="true" dataType = "Rate"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="getCalCashCoverRate" label="测算覆盖率"/>
			<emp:button id="select" label="确定" />
		</div>
	</emp:form>		
</body>
</html>
</emp:page>
