<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	String cus_id = request.getParameter("cus_id");
%>
<emp:page>
<html>
<head>
<title>新增页面</title>
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 350px;
}
</style>
<jsp:include page="/include.jsp" flush="true"/>
<%
	String lmt_serno = request.getParameter("lmt_serno");
%>
<script type="text/javascript">

	/*--user code begin--*/
	function doReturn(){
		window.close();
	}

	function doload(){
		//var urls = '&lmt_serno=<%=lmt_serno%>';
		var cus_id = '<%=cus_id %>';
		//var param = "cusTypCondition=cus_id in( select cus_id from Cus_Indiv_Soc_Rel where indiv_cus_rel in('1','2','3','9') and cus_id_rel='"+cus_id+"')or cus_id = '"+cus_id+"'&returnMethod=returnCus";
		var urls = "&lmt_serno=<%=lmt_serno%>";
		//var url = '<emp:url action="queryAllCusPop.do"/>?'+param;
		//url = EMPTools.encodeURI(url);
		LmtFasset.cus_id._obj.config.url=LmtFasset.cus_id._obj.config.url+urls;
		
	}

	function doAddLmtFasset(){
		var form = document.getElementById("submitForm");
		LmtFasset._checkAll();
		if(LmtFasset._checkAll()){
			LmtFasset._toForm(form);
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
						alert("新增成功！");
						window.close();
						window.opener.location.reload();
					}else {
						alert("新增失败！");
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
	}

	function returnCus(data){
		//客户码,证件类型,证件号码,姓名
    	LmtFasset.cus_id._setValue(data.cus_id._getValue());
    	LmtFasset.cus_id_displayname._setValue(data.cus_name._getValue());
    	LmtFasset.cus_attr._setValue(data.cus_attr._getValue());//客户属性
    	checkCusId();
	}

	function checkCusId(){
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
					
				}else{
					alert("此客户存在申请！");
					LmtFasset.cus_id._setValue("");
			    	LmtFasset.cus_id_displayname._setValue("");
			    	LmtFasset.cus_attr._setValue("");;//客户属性
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
			var cus_id = LmtFasset.cus_id._getValue();
			var serno = LmtFasset.serno._getValue();
			var url = '<emp:url action="checkCusIdAdd4Asset.do"/>&cus_id='+cus_id+"&serno="+serno;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
		
	}

	function countAmt(){
		var identy_perc = LmtFasset.identy_perc._obj.element.value;//认定比例
		var asset_seval = LmtFasset.asset_seval._getValue();//资产原估值
		var asset_ivalue = LmtFasset.asset_ivalue._getValue();//资产认定值
		if(identy_perc == null || identy_perc == ""){
			identy_perc = 0;
		}
			var asset_ivalue = Math.round(parseFloat(asset_seval)*parseFloat(identy_perc));
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
<body class="page_content" onload="doload()">
	
	<emp:form id="submitForm" action="addLmtFassetRecord.do" method="POST">
		
		<emp:gridLayout id="LmtFassetGroup" title="家庭资产" maxColumn="2">
			<emp:pop id="LmtFasset.cus_id" label="客户码" url="queryRelaCusByLmtSernoPop.do?returnMethod=returnCus" colSpan="2" popParam="width=700px,height=650px" required="true"/>
			<emp:text id="LmtFasset.cus_id_displayname" label="客户名称"  required="true" readonly="true" cssElementClass="emp_field_text_readonly" colSpan="2"/>
			<emp:select id="LmtFasset.cus_attr" label="客户属性" required="false" dictname="STD_ZB_CUS_ATTR" readonly="true" colSpan="2"/>
			<emp:select id="LmtFasset.fasset_type" label="家庭资产类型" required="true" dictname="STD_ZB_FASSET_TYPE" onchange="changePerc()" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:text id="LmtFasset.autho_name" label="权属人名称" maxlength="10" required="false" />
			<emp:text id="LmtFasset.identy_perc" label="认定比例" maxlength="18" required="true" readonly="true" />
			<emp:text id="LmtFasset.asset_seval" label="资产原估值" maxlength="18" defvalue="0.00" required="true" dataType="Currency" onblur="countAmt()"/>
			<emp:text id="LmtFasset.asset_ivalue" label="资产认定值" maxlength="18" defvalue="0.00" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" readonly="true"/>
			<emp:textarea id="LmtFasset.memo" label="备注" maxlength="60" required="false" colSpan="2" />
			<emp:text id="LmtFasset.serno" label="流水号" maxlength="40" required="false" defvalue="<%=lmt_serno%>" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="addLmtFasset" label="确定"/>
			<emp:button id="return" label="关闭"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

