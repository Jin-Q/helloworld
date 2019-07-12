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
		MortBill._toForm(form);
		if(!MortBill._checkAll()){
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
		page.dataGroups.MortBillGroup.reset();
	};		
	//两个日期作比较
	function checkInsurStartDate(){
		if(MortBill.draw_bill_date._obj.element.value!=''){
			var e = MortBill.bill_expire_date._obj.element.value;
			var s = MortBill.draw_bill_date._obj.element.value;
			var openDay='${context.OPENDAY}';
			if(s>openDay){
        		alert('出票日期必须小于或等于当前日期！');
        		MortBill.draw_bill_date._obj.element.value="";
        		return;
        	}
			if(e!=''){
				if(s>e){
            		alert('出票日期必须小于或等于到期日期！');
            		MortBill.draw_bill_date._obj.element.value="";
            		return;
            	}
			}
		}
	}
	function checkInsurEndDate(){
		if(MortBill.bill_expire_date._obj.element.value!=''){
			var e = MortBill.bill_expire_date._obj.element.value;
			var s = MortBill.draw_bill_date._obj.element.value;
			if(s!=''){
				if(s>e){
            		alert('到期日期必须大于或等于出票日期！');
            		MortBill.bill_expire_date._obj.element.value="";
            		return;
            	}
			}
		}
	}				
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addMortBillRecord.do" method="POST">
		
		<emp:gridLayout id="MortBillGroup" title="票据" maxColumn="2">
			<emp:text id="MortBill.bill_id" label="票据编号" maxlength="16" required="false" hidden="true"/>
			<emp:text id="MortBill.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true" />
			
			
			<emp:select id="MortBill.bill_currency_cd" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY"/>
			<emp:text id="MortBill.bill_amt" label="票面金额（元）" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="MortBill.ticketer" label="出票人" maxlength="100" required="true" />
			<emp:date id="MortBill.draw_bill_date" label="出票日期" required="true" onblur="checkInsurStartDate()"/>
			<emp:date id="MortBill.bill_expire_date" label="到期日期" required="true" onblur="checkInsurEndDate()"/>
			<emp:text id="MortBill.acceptor_name" label="承兑人名称" maxlength="100" required="true" />
			<emp:text id="MortBill.remitter_name" label="持有人" maxlength="200" required="true" />
			<emp:date id="MortBill.accept_date" label="承兑日期" required="true" />
			<emp:text id="MortBill.collector" label="收款人" maxlength="100" required="true" />
			<emp:text id="MortBill.endorsement" label="背书次数" maxlength="100" required="true" dataType="Int"/>
			<emp:text id="MortBill.endorsement_name" label="最后背书人" maxlength="40" required="true" colSpan="2"/>
			<emp:text id="MortBill.bill_no" label="票据号码" maxlength="100" required="true" />
			<emp:select id="MortBill.bill_type_cd" label="票据类型" required="true" dictname="STD_DRFT_TYPE" />


			<emp:text id="MortBill.remitter_bank" label="出票人开户行号" maxlength="100" required="false" hidden="true"/>
			<emp:text id="MortBill.remitter_num" label="出票人账号" maxlength="100" required="false" hidden="true"/>
			<emp:text id="MortBill.payee_name" label="收款人名称" maxlength="200" required="false" hidden="true"/>
			<emp:text id="MortBill.payee_bank" label="收款人开户行号" maxlength="100" required="false" hidden="true"/>
			<emp:text id="MortBill.payee_num" label="收款人账号" maxlength="100" required="false" hidden="true"/>
			<emp:text id="MortBill.pawnee_name" label="质权人名称" maxlength="200" required="false" hidden="true"/>
			<emp:text id="MortBill.pawnee_bank" label="质权人开户行号" maxlength="100" required="false" hidden="true"/>
			<emp:text id="MortBill.acceptor_bank_type" label="承兑人行类别" maxlength="20" required="false" hidden="true"/>
			<emp:text id="MortBill.acceptor_bank" label="承兑人行号" maxlength="100" required="false" hidden="true"/>
			<emp:text id="MortBill.query_info" label="查询查复情况" maxlength="20" required="false" hidden="true"/>
			<emp:text id="MortBill.encash_company_type" label="承兑公司类别" maxlength="20" required="false" hidden="true"/>
			<emp:text id="MortBill.encash_com_oper_status" label="承兑公司经营情况" maxlength="1000" required="false" hidden="true"/>
			<emp:date id="MortBill.bill_signup_date" label="签发日期" required="false" hidden="true"/>
			<emp:date id="MortBill.sys_update_time" label="系统更新时间" required="false" hidden="true" />
			<emp:text id="MortBill.endorsement_num" label="背书次数" maxlength="10" required="false" dataType="Int" hidden="true"/>
			<emp:text id="MortBill.memo" label="备注" maxlength="1000" required="false" hidden="true"/>
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

