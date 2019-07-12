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
		MortLadingBill._toForm(form);
		if(!MortLadingBill._checkAll()){
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
		page.dataGroups.MortLadingBillGroup.reset();
	};		
	//两个日期作比较（有当前日期）
	function checkInsurStartDate(){
		if(MortLadingBill.cargo_sign_date._obj.element.value!=''){
			var e = MortLadingBill.avail_expiration_date._obj.element.value;
			var s = MortLadingBill.cargo_sign_date._obj.element.value;
			var openDay='${context.OPENDAY}';
			if(s>openDay){
        		alert('提单签发日期必须小于或等于当前日期！');
        		MortLadingBill.cargo_sign_date._obj.element.value="";
        		return;
        	}
			if(e!=''){
				if(s>e){
            		alert('提单签发日期必须小于或等于有效截止日期！');
            		MortLadingBill.cargo_sign_date._obj.element.value="";
            		return;
            	}
			}
		}
	}
	function checkInsurEndDate(){
		if(MortLadingBill.avail_expiration_date._obj.element.value!=''){
			var e = MortLadingBill.avail_expiration_date._obj.element.value;
			var s = MortLadingBill.cargo_sign_date._obj.element.value;
			if(s!=''){
				if(s>e){
            		alert('有效截止日期必须大于或等于提单签发日期！');
            		MortLadingBill.avail_expiration_date._obj.element.value="";
            		return;
            	}
			}
		}
	}	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addMortLadingBillRecord.do" method="POST">
		
		<emp:gridLayout id="MortLadingBillGroup" title="提单" maxColumn="2">
			<emp:text id="MortLadingBill.lading_id" label="提单编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="MortLadingBill.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true" />
			<emp:text id="MortLadingBill.lading_bill_no" label="提单编号" maxlength="40" required="true" />
			<emp:select id="MortLadingBill.bill_type" label="提单类型" required="true" dictname="STD_ZX_BILL_TYPE" />
			<emp:text id="MortLadingBill.cargo_model_no" label="提单项下货物品牌/型号" maxlength="40" required="true" />
			<emp:text id="MortLadingBill.cargo_standard_no" label="提单项下货物品种/规格" maxlength="40" required="true" />
			<emp:text id="MortLadingBill.sell_consult" label="提单项下货物销售商名称" maxlength="40" required="true" />
			<emp:text id="MortLadingBill.cargo_product_consult" label="提单项下货物生产商名称" maxlength="40" required="true" />
			<emp:text id="MortLadingBill.cargo_deposit_place" label="提单项下货物存放地点" maxlength="100" required="true" />
			<emp:text id="MortLadingBill.cargo_checker" label="提单托运人" maxlength="40" required="true" />
			<emp:text id="MortLadingBill.cargo_consignee" label="收货人" maxlength="40" required="true" />
			<emp:select id="MortLadingBill.is_import_permit" label="是否有进口许可证" required="true" dictname="STD_ZX_YES_NO" />
			
			<emp:text id="MortLadingBill.permit_unit" label="进口许可单位" maxlength="40" required="true" />
			<emp:date id="MortLadingBill.permit_date" label="进口许可日期" required="true" />
			<emp:text id="MortLadingBill.permit_no" label="进口许可证号" maxlength="40" required="true" colSpan="2"/>
		
			<emp:text id="MortLadingBill.amount" label="数量及重量" maxlength="8" required="true" dataType="Double"/>
			<emp:select id="MortLadingBill.cargo_unit" label="货物计量单位" required="true" dictname="STD_AMOUNT_UNIT" />
			<emp:date id="MortLadingBill.cargo_sign_date" label="提单签发日期" required="true" onblur="checkInsurStartDate()"/>
			<emp:date id="MortLadingBill.avail_expiration_date" label="有效截止日期" required="true" onblur="checkInsurEndDate()"/>
			<emp:text id="MortLadingBill.carrier" label="承运人" maxlength="20" required="false" />
			<emp:text id="MortLadingBill.cargo_name" label="货物名称" maxlength="100" required="true" />
			<emp:textarea id="MortLadingBill.memo" label="备注" maxlength="1000" required="false" colSpan="2" />
			<emp:date id="MortLadingBill.sys_update_time" label="系统更新时间" required="false" hidden="true" />
			
			
			
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

