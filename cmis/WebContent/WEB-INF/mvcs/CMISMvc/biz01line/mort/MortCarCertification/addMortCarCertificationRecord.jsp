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
		MortCarCertification._toForm(form);
		if(!MortCarCertification._checkAll()){
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
		page.dataGroups.MortCarCertificationGroup.reset();
	};			
	function checkDt(){
		var occur_date = MortCarCertification.leave_factory_date._getValue();
		var openDay='${context.OPENDAY}';
		if(occur_date!=''){
			if(CheckDate1BeforeDate2(openDay,occur_date)){
	    		alert('出厂时间不能大于当前日期！');
	    		MortCarCertification.leave_factory_date._obj.element.value="";
	    		return;
	    	}
    	}
	}	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addMortCarCertificationRecord.do" method="POST">
		
		<emp:gridLayout id="MortCarCertificationGroup" title="汽车合格证" maxColumn="2">
			<emp:text id="MortCarCertification.car_cert_id" label="汽车合格证编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="MortCarCertification.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true" />
			<emp:select id="MortCarCertification.is_import" label="是否进口车" required="true" dictname="STD_ZX_YES_NO" />
			<emp:text id="MortCarCertification.car_cert_no" label="汽车合格证编号" maxlength="40" required="true" />
			<emp:select id="MortCarCertification.cur_type" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY"/>
			<emp:text id="MortCarCertification.amt" label="金额（元）" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="MortCarCertification.count" label="张数" maxlength="10" required="true" dataType="Int" />
			<emp:text id="MortCarCertification.car_brand" label="汽车品牌" maxlength="100" required="true" />
			<emp:text id="MortCarCertification.car_model" label="汽车型号" maxlength="100" required="true" />
			<emp:text id="MortCarCertification.car_flag_no" label="车架号" maxlength="100" required="true" />
			<emp:date id="MortCarCertification.leave_factory_date" label="出厂时间" required="true" onblur="checkDt()"/>
			<emp:textarea id="MortCarCertification.memo" label="备注" maxlength="1000" required="false" colSpan="2" />
			<emp:date id="MortCarCertification.sys_update_time" label="更新时间" required="false" hidden="true"/>
			<emp:text id="MortCarCertification.handling_user" label="更新人" maxlength="40" required="false" hidden="true"/>
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

