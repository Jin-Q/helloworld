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
		MortCarPledgeInsurance._toForm(form);
		if(!MortCarPledgeInsurance._checkAll()){
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
		page.dataGroups.MortCarPledgeInsuranceGroup.reset();
	};	
	function checkInsurStartDate(){
		if(MortCarPledgeInsurance.start_date._obj.element.value!=''){
			var e = MortCarPledgeInsurance.effective_date._obj.element.value;
			var s = MortCarPledgeInsurance.start_date._obj.element.value;
			var d = MortCarPledgeInsurance.expire_date._obj.element.value;
			//if(CheckDateAfterToday(s)){
        		//alert('起始日期不能大于当前日期！');
        		//GuarantyInsurInfo.insur_start_date._obj.element.value="";
        		//return;
        	//}
			if(e!=''){
				if(s>e){
            		alert('起始日期必须小于或等于生效日期！');
            		MortCarPledgeInsurance.start_date._obj.element.value="";
            		return;
            	}
			}
			if(d!=''){
				if(s>=d){
            		alert('起始日期必须小于到期日期！');
            		MortCarPledgeInsurance.start_date._obj.element.value="";
            		return;
            	}
			}
		}
	}
	function checkInsurEndDate(){
		if(MortCarPledgeInsurance.expire_date._obj.element.value!=''){
			var e = MortCarPledgeInsurance.effective_date._obj.element.value;
			var s = MortCarPledgeInsurance.start_date._obj.element.value;
			var d = MortCarPledgeInsurance.expire_date._obj.element.value;
			if(e!=''){
				if(d<=e){
					alert('到期日期必须大于生效日期！');
					MortCarPledgeInsurance.expire_date._obj.element.value="";
        			return;
				}
			}
			if(s!=''){
				if(d<=s){
            		alert('到期日期必须大于起始日期！');
            		MortCarPledgeInsurance.expire_date._obj.element.value="";
            		return;
            	}
			}
		}
	}
	function checkEffectiveDate(){
		if(MortCarPledgeInsurance.effective_date._obj.element.value!=''){
			var s = MortCarPledgeInsurance.start_date._obj.element.value;
			var e = MortCarPledgeInsurance.effective_date._obj.element.value;
			var d = MortCarPledgeInsurance.expire_date._obj.element.value;
			var openDay='${context.OPENDAY}';
			if(CheckDate1BeforeDate2(openDay,e)){
        		alert('生效日期不能大于当前日期！');
        		MortCarPledgeInsurance.effective_date._obj.element.value="";
        		return;
        	}
			if(s!=''){
				if(e<s){
					alert('生效日期必须大于或等于起始日期！');
					MortCarPledgeInsurance.effective_date._obj.element.value="";
            		return;
				}
			}
			if(d!=''){
				if(e>=d){
            		alert('生效日期必须小于到期日期！');
            		MortCarPledgeInsurance.effective_date._obj.element.value="";
            		return;
            	}
			}
		}
	}			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addMortCarPledgeInsuranceRecord.do" method="POST">
		
		<emp:gridLayout id="MortCarPledgeInsuranceGroup" title="汽车履约保证保险、出口信用保证保险" maxColumn="2">
			<emp:text id="MortCarPledgeInsurance.car_insure_id" label="汽车保险编号" maxlength="20" required="false" hidden="true"/>
			<emp:text id="MortCarPledgeInsurance.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true" />
			<emp:text id="MortCarPledgeInsurance.insur_no" label="保险单号" maxlength="100" required="true" />
			<emp:text id="MortCarPledgeInsurance.insure_org_name" label="保险机构" maxlength="100" required="true" />
			
			<emp:select id="MortCarPledgeInsurance.insu_ccr" label="投保币种" required="false" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="MortCarPledgeInsurance.insu_amt" label="投保金额（元）" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="MortCarPledgeInsurance.insure_count" label="份数" maxlength="100" required="true" dataType="Int"/>
			<emp:text id="MortCarPledgeInsurance.policy_holder" label="被保险人" maxlength="100" required="true" />
			<emp:text id="MortCarPledgeInsurance.beneficiary_name" label="受益人" maxlength="100" required="true" />
			<emp:date id="MortCarPledgeInsurance.start_date" label="起始日期" required="true" onblur="checkInsurStartDate()"/>
			<emp:date id="MortCarPledgeInsurance.effective_date" label="生效日期" required="false" onblur="checkEffectiveDate()"/>
			<emp:date id="MortCarPledgeInsurance.expire_date" label="到期日期" required="true" onblur="checkInsurEndDate()"/>
			<emp:select id="MortCarPledgeInsurance.insurance_type_cd" label="险种" required="false" dictname="STD_ZB_INSU_TYPE" />
			<emp:textarea id="MortCarPledgeInsurance.memo" label="备注" maxlength="1000" required="false" colSpan="2" />
			<emp:date id="MortCarPledgeInsurance.sys_update_time" label="更新日期" required="false" hidden="true"/>
			<emp:text id="MortCarPledgeInsurance.handling_user" label="更新人" maxlength="40" required="false" hidden="true"/>
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

