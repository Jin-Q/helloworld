<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	String cus_id = request.getParameter("cus_id");
%>
<emp:page>
<html>
<head>
<title>修改页面</title>
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 350px;
}
</style>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function countAmt(){
		var identy_perc=document.getElementById("identy_perc").value;//认定比例
		var asset_seval = LmtFasset.asset_seval._getValue();//资产原估值
		var asset_ivalue = LmtFasset.asset_ivalue._getValue();//资产认定值
		if(identy_perc<0 || asset_seval<0){
			alert("请输入合法数据！");
			LmtFasset.identy_perc._setValue("");
			LmtFasset.asset_seval._setValue("");
			return;
		}else{
			asset_ivalue = identy_perc*asset_seval;   
			LmtFasset.asset_ivalue._setValue(asset_ivalue+'');
		}
	}

	function doReturn(){
		window.close();
	}

	function doUpdatee() {
		var form = document.getElementById("submitForm");
		var result = LmtFasset._checkAll();
		if(result){
			LmtFasset._toForm(form)
			toSubmitForm(form);
		}else alert("请输入必填项！");
	}
	
	function toSubmitForm(form){
		  var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr define error!"+e);
						return;
					}
					var flag = jsonstr.flag;
					if(flag=="success"){
						alert("修改成功！");
						window.close();
						window.opener.location.reload();
				     }else {
					   alert(flag);
					   return;
				     }
				}
			};
			var handleFailure = function(o){	
			};
			var callback = {
					success:handleSuccess,
					failure:handleFailure
				};
				var postData = YAHOO.util.Connect.setForm(form);	 
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData) 
	};

	function countAmt(){
		var identy_perc = LmtFasset.identy_perc._obj.element.value;//认定比例
		var asset_seval = LmtFasset.asset_seval._getValue();//资产原估值
		var asset_ivalue = LmtFasset.asset_ivalue._getValue();//资产认定值
		if(identy_perc == null || identy_perc == ""){
			identy_perc = 0;
		}
			var asset_ivalue = Math.round(parseFloat(asset_seval)*parseFloat(identy_perc))/100;
			LmtFasset.asset_ivalue._setValue(''+asset_ivalue+'');
	};

	function changePerc(){
		var fasset_type = LmtFasset.fasset_type._getValue();
		if(fasset_type == '01' || fasset_type == '02' || fasset_type == '03'){
			LmtFasset.identy_perc._setValue("1");
		}else if(fasset_type == '04' || fasset_type == '07' || fasset_type == '08'  || fasset_type == '13' || fasset_type == '14' ){
			LmtFasset.identy_perc._setValue("0.5");
		}else if(fasset_type == '05' || fasset_type == '10' || fasset_type == '11' ){
			LmtFasset.identy_perc._setValue("0.7");
		}else if(fasset_type == '06' || fasset_type == '09' ){
			LmtFasset.identy_perc._setValue("0.5");
		}else if(fasset_type == '12' ){
			LmtFasset.identy_perc._setValue("0.6");
		}
		countAmt();
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateLmtFassetRecord.do" method="POST">
		<emp:gridLayout id="LmtFassetGroup" maxColumn="2" title="家庭资产">
			<emp:pop id="LmtFasset.cus_id" label="客户码" url="queryAllCusPop.do?cusTypCondition=cus_id in( select cus_id from Cus_Indiv_Soc_Rel where indiv_cus_rel in('1','2','3','9') and cus_id_rel='${context.cus_id}') or cus_id = '${context.cus_id}'&returnMethod=returnCus" colSpan="2" popParam="width=700px,height=650px" readonly="true" required="true"/>
			<emp:text id="LmtFasset.cus_id_displayname" label="客户名称"  required="true" readonly="true" cssElementClass="emp_field_text_readonly" colSpan="2"/>
			<emp:select id="LmtFasset.cus_attr" label="客户属性" required="false" dictname="STD_ZB_CUS_ATTR" readonly="true" colSpan="2"/>
			<emp:select id="LmtFasset.fasset_type" label="家庭资产类型" required="true" dictname="STD_ZB_FASSET_TYPE" onchange="changePerc()" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:text id="LmtFasset.autho_name" label="权属人名称" maxlength="10" required="false" />
			<emp:text id="LmtFasset.identy_perc" label="认定比例" maxlength="18" required="true" readonly="true" />
			<emp:text id="LmtFasset.asset_seval" label="资产原估值" maxlength="18" defvalue="0.00" required="true" dataType="Currency" onchange="countAmt()"/>
			<emp:text id="LmtFasset.asset_ivalue" label="资产认定值" maxlength="18" defvalue="0.00" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" readonly="true"/>
			<emp:textarea id="LmtFasset.memo" label="备注" maxlength="60" required="false" colSpan="2" />
			<emp:text id="LmtFasset.serno" label="流水号" required="false" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="updatee" label="修改"/>
			<emp:button id="return" label="关闭"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
