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
		MortImpawnOthers._toForm(form);
		if(!MortImpawnOthers._checkAll()){
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
		page.dataGroups.MortImpawnOthersGroup.reset();
	};		
	//两个日期作比较
	function checkInsurStartDate(){
		if(MortImpawnOthers.buy_date._obj.element.value!=''){
			var e = MortImpawnOthers.expire_date._obj.element.value;
			var s = MortImpawnOthers.buy_date._obj.element.value;
			var openDay='${context.OPENDAY}';
			if(s>openDay){
        		alert('购入/起始日期必须小于或等于当前日期！');
        		MortImpawnOthers.buy_date._obj.element.value="";
        		return;
        	}
			if(e!=''){
				if(s>e){
            		alert('购入/起始日期必须小于或等于到期日期！');
            		MortImpawnOthers.buy_date._obj.element.value="";
            		return;
            	}
			}
		}
	}
	function checkInsurEndDate(){
		if(MortImpawnOthers.expire_date._obj.element.value!=''){
			var e = MortImpawnOthers.expire_date._obj.element.value;
			var s = MortImpawnOthers.buy_date._obj.element.value;
			if(s!=''){
				if(s>e){
            		alert('到期日期必须大于或等于购入/起始日期！');
            		MortImpawnOthers.expire_date._obj.element.value="";
            		return;
            	}
			}
		}
	}					
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addMortImpawnOthersRecord.do" method="POST">
		
		<emp:gridLayout id="MortImpawnOthersGroup" title="其他质押品" maxColumn="2">
			<emp:text id="MortImpawnOthers.impawn_other_id" label="质押品编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="MortImpawnOthers.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true" />
			<emp:text id="MortImpawnOthers.guaranty_name" label="质押物名称" maxlength="100" required="true" />
			<emp:text id="MortImpawnOthers.impawn_quantity" label="数量及单位" maxlength="16" required="false" />
			<emp:date id="MortImpawnOthers.buy_date" label="购入/起始日期" required="false" onblur="checkInsurStartDate()"/>
			<emp:date id="MortImpawnOthers.expire_date" label="到期日期" required="false" onblur="checkInsurEndDate()"/>
			<emp:select id="MortImpawnOthers.buy_curr_cd" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY"/>
			<emp:text id="MortImpawnOthers.buy_sum" label="购入金额（元）" maxlength="18" required="true" dataType="Currency" />
			<emp:textarea id="MortImpawnOthers.memo" label="备注" maxlength="1000" required="false" colSpan="2" />
			<emp:date id="MortImpawnOthers.sys_update_time" label="系统更新时间" required="false" hidden="true" />
			
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

