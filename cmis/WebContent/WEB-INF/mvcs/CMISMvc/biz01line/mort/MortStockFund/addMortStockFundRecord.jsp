<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<% 
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
String op= "";
if(context.containsKey("op")){
	op = (String)context.getDataValue("op");
}
if("view".equals(op)||"to_storage".equals(op)){
	request.setAttribute("canwrite","");
}
String guaranty_no = request.getParameter("guaranty_no");
%>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
			function doAdd(){
		var form = document.getElementById('submitForm');
		MortStockFund._toForm(form);
		if(!MortStockFund._checkAll()){
			alert("请输入必填项！");
		}else{
			var handleSuccess = function(o) {
				if (o.responseText !== undefined) {
					try {
						var jsonstr = eval("(" + o.responseText + ")");
					} catch (e) {
						alert("保存失败！");
						document.getElementById("button_add").disabled="";
						document.getElementById("button_reset").disabled="";
						return;
					}
					var flag = jsonstr.flag;
					if(flag=='success'){	
						alert("保存成功");
						var guaranty_no = '${context.guaranty_no}';
						var collateral_type_cd = '${context.collateral_type_cd}';
						var url = '<emp:url action="getDetailInformationPage.do"/>?guaranty_no='+guaranty_no+'&collateral_type_cd='+collateral_type_cd;
						url = EMPTools.encodeURI(url);
						location.href(url);
					}else{
						alert("保存失败");
						document.getElementById("button_add").disabled="";
						document.getElementById("button_reset").disabled="";
					}   
				}	
			};
			var handleFailure = function(o) {
				alert("保存失败!");
				document.getElementById("button_add").disabled="";
				document.getElementById("button_reset").disabled="";
			};
			var callback = {
				success :handleSuccess,
				failure :handleFailure
			};
			var postData = YAHOO.util.Connect.setForm(form);	 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	};
	function doReset(){
		var guaranty_no = '${context.guaranty_no}';
		var url = '<emp:url action="getDetailInformationPage.do"/>?guaranty_no='+guaranty_no;
		url = EMPTools.encodeURI(url);
		location.href(url);
		page.dataGroups.MortStockFundGroup.reset();
	};			
	function checkDt(){
		var occur_date = MortStockFund.release_date._getValue();
		var openDay='${context.OPENDAY}';
		if(occur_date!=''){
			if(CheckDate1BeforeDate2(openDay,occur_date)){
	    		alert('股票/基金发行日期不能大于当前日期！');
	    		MortStockFund.release_date._obj.element.value="";
	    		return;
	    	}
    	}
	}	
	function checkDt1(){
		var occur_date = MortStockFund.marketing_date._getValue();
		var openDay='${context.OPENDAY}';
		if(occur_date!=''){
			if(CheckDate1BeforeDate2(openDay,occur_date)){
	    		alert('上市日期不能大于当前日期！');
	    		MortStockFund.marketing_date._obj.element.value="";
	    		return;
	    	}
    	}
	}	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addMortStockFundRecord.do" method="POST">
		
		<emp:gridLayout id="MortStockFundGroup" title="股票基金" maxColumn="2">
			<emp:text id="MortStockFund.stock_id" label="股票/基金编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="MortStockFund.guaranty_no" label="抵押品编号" maxlength="40" required="false" hidden="true"/>
			
			<emp:text id="MortStockFund.stock_count" label="数量（股）" maxlength="16" required="true" dataType="Int" />
			<emp:text id="MortStockFund.net_value_per_stock_now" label="当前每股净资产" maxlength="20" required="true" dataType="Double" />
			<emp:text id="MortStockFund.average_price" label="平均市价" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="MortStockFund.stock_name" label="股票/基金名称" maxlength="100" required="true" />
			<emp:date id="MortStockFund.release_date" label="股票/基金发行日期" required="false" onblur="checkDt()"/>
			<emp:select id="MortStockFund.stock_issue_type_cd" label="发行方式代码" required="true" dictname="STD_EMIT_TYPE" />
			<emp:text id="MortStockFund.market_place" label="上市地点" maxlength="100" required="false" />
			<emp:text id="MortStockFund.market_company_name" label="上市公司名称" maxlength="100" required="true" />
			<emp:text id="MortStockFund.market_company_type_cd" label="上市公司类型代码" maxlength="20" required="false" />
			<emp:text id="MortStockFund.average_price_latest_day" label="最近一个交易日市价" maxlength="18" required="false" dataType="Currency"/>
			<emp:select id="MortStockFund.stock_divi_payout_type" label="按股票派息顺序划分后的类型" required="false" dictname="STD_STOCK_DIVIPAY_TYPE" />
			<emp:select id="MortStockFund.stock_investor_type" label="按股票投资主体划分后的类型" required="false" dictname="STD_STOCK_INVEST_TYPE" />
			<emp:date id="MortStockFund.marketing_date" label="上市日期" required="false" onblur="checkDt1()"/>
			<emp:text id="MortStockFund.stock_cd" label="股票代码" maxlength="100" required="true" />
			<emp:text id="MortStockFund.trust_unit" label="未上市股票的托管单位" maxlength="200" required="false" hidden="false"/>
			<emp:select id="MortStockFund.currency_cd" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY"/>
			<emp:textarea id="MortStockFund.memo" label="备注" maxlength="1000" required="false" colSpan="2" />
			
			
			
			<emp:text id="MortStockFund.stock_type" label="股权、股票类型//" maxlength="20" required="false" hidden="true" />
			<emp:text id="MortStockFund.stock_info" label="股票信息" maxlength="200" required="false" hidden="true"/>
			<emp:text id="MortStockFund.net_value_per_stock" label="每股净资产" maxlength="20" required="false" dataType="Double" hidden="true"/>
			<emp:text id="MortStockFund.stock_registration_org" label="股票/基金登记机构" maxlength="100" required="false" hidden="true"/>
			<emp:text id="MortStockFund.stock_listed_ind" label="是，否//" maxlength="1" required="false" hidden="true"/>
			<emp:text id="MortStockFund.stock_character" label="多选：国家股、法人股、流通股、其它//" maxlength="30" required="false" hidden="true"/>
			<emp:text id="MortStockFund.control_percentage" label="CONTROL_PERCENTAGE" maxlength="12" required="false" hidden="true"/>
			<emp:text id="MortStockFund.net_value_currency_cd" label="NET_VALUE_CURRENCY_CD" maxlength="20" required="false" hidden="true" />
			<emp:text id="MortStockFund.market_company_oper_status" label="MARKET_COMPANY_OPER_STATUS" maxlength="256" required="false" hidden="true"/>
			<emp:text id="MortStockFund.average_price_latest_year" label="AVERAGE_PRICE_LATEST_YEAR" maxlength="20" required="false" hidden="true"/>
			<emp:text id="MortStockFund.securities_trader_cd" label="券商席位代码//" maxlength="100" required="false" hidden="true"/>
			<emp:text id="MortStockFund.shareholder_cd" label="股东代码//" maxlength="100" required="false" hidden="true"/>
			<emp:date id="MortStockFund.sys_update_time" label="系统更新时间" required="false" hidden="true" />
			
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<%if("view".equals(op)||"to_storage".equals(op)){%>		
			<%}else{%>
			<emp:button id="add" label="确定"/>
			<emp:button id="reset" label="重置"/>
			<% } %>			
		</div>

	</emp:form>
	
</body>
</html>
</emp:page>

