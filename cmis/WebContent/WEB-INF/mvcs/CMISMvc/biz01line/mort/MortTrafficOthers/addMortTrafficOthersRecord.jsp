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
		MortTrafficOthers._toForm(form);
		if(!MortTrafficOthers._checkAll()){
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
		page.dataGroups.MortTrafficOthersGroup.reset();
	};	
	function checkDt(){
		var occur_date = MortTrafficOthers.produce_date._getValue();
		var openDay='${context.OPENDAY}';
		if(occur_date!=''){
			if(CheckDate1BeforeDate2(openDay,occur_date)){
	    		alert('制造年度不能大于当前日期！');
	    		MortTrafficOthers.produce_date._obj.element.value="";
	    		return;
	    	}
    	}
	}	
	function doLoad(){
		doUsedYears();
	}	
	function doUsedYears(){
		var is_used = MortTrafficOthers.is_used._getValue();
		if("02"==is_used){
			MortTrafficOthers.used_years._obj._renderReadonly(false);
			MortTrafficOthers.used_years._obj._renderRequired(true);
		}else{
			MortTrafficOthers.used_years._obj._renderReadonly(false);
			MortTrafficOthers.used_years._obj._renderRequired(false);
		}
	}			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="addMortTrafficOthersRecord.do" method="POST">
		
		<emp:gridLayout id="MortTrafficOthersGroup" title="其他交通运输设备" maxColumn="2">
			<emp:text id="MortTrafficOthers.traffic_other_id" label="其他运输工具编号" maxlength="40" required="false" hidden="true" />
			<emp:text id="MortTrafficOthers.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true" />
			<emp:text id="MortTrafficOthers.vehicle_license_no" label="运输工具营运执照号" maxlength="40" required="false" />
			<emp:text id="MortTrafficOthers.count" label="数量" maxlength="20" required="true" dataType="Double" />
			<emp:text id="MortTrafficOthers.unit" label="单位" maxlength="20" required="true" />
			<emp:text id="MortTrafficOthers.load" label="载重(吨)" maxlength="16" required="true" dataType="Double" />
			<emp:text id="MortTrafficOthers.power" label="功率" maxlength="16" required="true" dataType="Double" />
			<emp:select id="MortTrafficOthers.power_unit" label="功率单位" required="true" dictname="STD_POWER_UNIT" />
			<emp:date id="MortTrafficOthers.produce_date" label="制造年度" required="true" onblur="checkDt()"/>
			<emp:text id="MortTrafficOthers.produce_factory" label="制造商" maxlength="100" required="true" />
			<emp:text id="MortTrafficOthers.produce_site" label="制造地" maxlength="100" required="true" />
			<emp:text id="MortTrafficOthers.model_no" label="规格型号" maxlength="40" required="true" />
			<emp:text id="MortTrafficOthers.repair_count" label="大修次数" maxlength="8" required="false" dataType="Int" />
			<emp:select id="MortTrafficOthers.use_status" label="使用状态" required="true" dictname="STD_USE_STATUS" />
			<emp:text id="MortTrafficOthers.accident_record" label="事故记录" maxlength="1000" required="false" />
			<emp:select id="MortTrafficOthers.is_used" label="一手/二手" required="true" dictname="STD_SKILL_USED" onchange="doUsedYears()"/>
			<emp:text id="MortTrafficOthers.used_years" label="已使用年限" maxlength="8" required="false" dataType="Int" />
			<emp:text id="MortTrafficOthers.bearing_capacity" label="承载能力" maxlength="20" required="false" />
			<emp:text id="MortTrafficOthers.buy_price" label="购入金额（元）" maxlength="18" required="true" dataType="Currency" />
			<emp:select id="MortTrafficOthers.buy_curr_cd" label="购入币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY"/>
			<emp:textarea id="MortTrafficOthers.memo" label="备注" maxlength="1000" required="false" colSpan="2" />
			
			
			<emp:date id="MortTrafficOthers.sys_update_time" label="系统更新时间" required="false" hidden="true" />
			<emp:text id="MortTrafficOthers.vehicle_type" label="运输工具类型//" maxlength="20" required="false" hidden="true" />
			
			
			
			
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

