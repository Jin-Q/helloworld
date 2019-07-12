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
		MortStandardDepotBill._toForm(form);
		if(!MortStandardDepotBill._checkAll()){
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
		page.dataGroups.MortStandardDepotBillGroup.reset();
	};	
	function setOrg(data){
		MortStandardDepotBill.bill_manage_name._setValue(data.oversee_org_id_displayname._getValue());
		MortStandardDepotBill.bill_manage_no._setValue(data.oversee_org_id._getValue());
	}
	function setStore(data){
		MortStandardDepotBill.bill_storage_name._setValue(data.store_name._getValue());
		MortStandardDepotBill.bill_storage_no._setValue(data.store_id._getValue());
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addMortStandardDepotBillRecord.do" method="POST">
		
		<emp:gridLayout id="MortStandardDepotBillGroup" title="标准仓单质押" maxColumn="2">
			<emp:text id="MortStandardDepotBill.depot_id" label="标准仓单编号" maxlength="40" required="false" hidden="true" />
			<emp:text id="MortStandardDepotBill.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true"/>
			<emp:pop id="MortStandardDepotBill.bill_manage_name" label="监管方" required="true" colSpan="2" url="IqpOverseeOrg4PopList.do?restrictUsed=false&returnMethod=setOrg"/>
			<emp:text id="MortStandardDepotBill.bill_manage_no" label="监管方" maxlength="30" required="false" hidden="true"/>
			<emp:pop id="MortStandardDepotBill.bill_storage_name" label="仓储方" required="true" colSpan="2" url="IqpUnderstore4PopList.do?restrictUsed=false" returnMethod="setStore"/>
			<emp:text id="MortStandardDepotBill.bill_storage_no" label="仓储方" maxlength="30" required="false" hidden="true"/>
			<emp:text id="MortStandardDepotBill.depot_bill_no" label="仓单编号" maxlength="40" required="true" />
			<emp:text id="MortStandardDepotBill.cargo_model_no" label="存储物品牌/型号" maxlength="40" required="true" />
			<emp:text id="MortStandardDepotBill.cargo_standard_no" label="存储物品种/规格" maxlength="100" required="true" />
			<emp:text id="MortStandardDepotBill.bourse" label="商品（期货）交易所" maxlength="100" required="false" colSpan="2"/>
			<emp:text id="MortStandardDepotBill.amount" label="数量" maxlength="12" required="false" dataType="Double"/>
			<emp:text id="MortStandardDepotBill.cargo_unit" label="数量单位(例如：个)" maxlength="20" required="false" />
			<emp:text id="MortStandardDepotBill.weigh" label="重量" maxlength="16" required="false" dataType="Double"/>
			<emp:text id="MortStandardDepotBill.weigh_unit" label="重量单位 (例如：克)" maxlength="10" required="false" />
			<emp:text id="MortStandardDepotBill.rank" label="等级" maxlength="100" required="true" />
			<emp:date id="MortStandardDepotBill.avail_expiration_date" label="有效截止日" required="true" />
			<emp:text id="MortStandardDepotBill.logo" label="商标" maxlength="100" required="false" />
			<emp:text id="MortStandardDepotBill.bill_storage_place" label="存储场所" maxlength="100" required="false" />
			<emp:date id="MortStandardDepotBill.last_goods_date" label="最迟提货时间" required="true" />
			<emp:text id="MortStandardDepotBill.cargo_name" label="货物名称" maxlength="100" required="true" />
			<emp:text id="MortStandardDepotBill.hold_num" label="持有手数" maxlength="12" required="false" />
			<emp:date id="MortStandardDepotBill.sys_update_time" label="系统更新时间" required="false" hidden="true"/>
			<emp:textarea id="MortStandardDepotBill.memo" label="备注" maxlength="1000" required="false" colSpan="2" />
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

