<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
	} 
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_input{
border:1px solid #b7b7b7;
width:160px;
}

.emp_input2{
border:1px solid #b7b7b7;
width:600px;
}
</style>
<script type="text/javascript">
	
	/*--user code begin--*/
	function greenPrdReturn(date){
		IqpGreenDeclInfo.green_indus._obj.element.value=date.id;
		IqpGreenDeclInfo.green_indus_displayname._setValue(date.label);
	};	
	function onload(){
		var green_indus = IqpGreenDeclInfo.green_indus._getValue();
        if(green_indus == "2"){
        	document.getElementById('button').style.display="none";
            
        	IqpGreenDeclInfo.reduc_coal._obj._renderHidden(true);//项目年节约标准煤量
        	IqpGreenDeclInfo.emission_co2._obj._renderHidden(true);//项目年减排二氧化碳量
        	IqpGreenDeclInfo.emission_cod._obj._renderHidden(true);//项目年COD减排量
        	IqpGreenDeclInfo.emission_an._obj._renderHidden(true);//项目年氨氮减排量
        	IqpGreenDeclInfo.emission_so2._obj._renderHidden(true);//项目二氧化硫减排量
        	IqpGreenDeclInfo.emission_no._obj._renderHidden(true);//项目年氮氧化物减排量
        	IqpGreenDeclInfo.reduc_water._obj._renderHidden(true);//项目年节水量
        	IqpGreenDeclInfo.totl_invest._obj._renderHidden(true);//项目总投资额

        	IqpGreenDeclInfo.reduc_coal._obj._renderRequired(false);//项目年节约标准煤量
        	IqpGreenDeclInfo.emission_co2._obj._renderRequired(false);//项目年减排二氧化碳量
        	IqpGreenDeclInfo.emission_cod._obj._renderRequired(false);//项目年COD减排量
        	IqpGreenDeclInfo.emission_an._obj._renderRequired(false);//项目年氨氮减排量
        	IqpGreenDeclInfo.emission_so2._obj._renderRequired(false);//项目二氧化硫减排量
        	IqpGreenDeclInfo.emission_no._obj._renderRequired(false);//项目年氮氧化物减排量
        	IqpGreenDeclInfo.reduc_water._obj._renderRequired(false);//项目年节水量
        	IqpGreenDeclInfo.totl_invest._obj._renderRequired(false);//项目总投资额
        }else if(green_indus == "A7.1" || green_indus == "A7.2" || green_indus == "A7.3" || green_indus == "A7.4" || green_indus == "A11.1"){//太阳能项目,风电项目,生物质能项目,水力发电项目,节能服务
        	IqpGreenDeclInfo.reduc_coal._obj._renderHidden(false);//项目年节约标准煤量
        	IqpGreenDeclInfo.emission_co2._obj._renderHidden(false);//项目年减排二氧化碳量
        	IqpGreenDeclInfo.emission_cod._obj._renderHidden(false);//项目年COD减排量
        	IqpGreenDeclInfo.emission_an._obj._renderHidden(false);//项目年氨氮减排量
        	IqpGreenDeclInfo.emission_so2._obj._renderHidden(false);//项目二氧化硫减排量
        	IqpGreenDeclInfo.emission_no._obj._renderHidden(false);//项目年氮氧化物减排量
        	IqpGreenDeclInfo.reduc_water._obj._renderHidden(false);//项目年节水量
        	IqpGreenDeclInfo.totl_invest._obj._renderHidden(false);//项目总投资额

        	IqpGreenDeclInfo.reduc_coal._obj._renderRequired(true);//项目年节约标准煤量
        	IqpGreenDeclInfo.emission_co2._obj._renderRequired(true);//项目年减排二氧化碳量
        	IqpGreenDeclInfo.emission_cod._obj._renderRequired(false);//项目年COD减排量
        	IqpGreenDeclInfo.emission_an._obj._renderRequired(false);//项目年氨氮减排量
        	IqpGreenDeclInfo.emission_so2._obj._renderRequired(false);//项目二氧化硫减排量
        	IqpGreenDeclInfo.emission_no._obj._renderRequired(false);//项目年氮氧化物减排量
        	IqpGreenDeclInfo.reduc_water._obj._renderRequired(false);//项目年节水量
        	IqpGreenDeclInfo.totl_invest._obj._renderRequired(true);//项目总投资额
        }else if(green_indus == "A8.3" || green_indus == "A11.3"){
        	IqpGreenDeclInfo.reduc_coal._obj._renderHidden(false);//项目年节约标准煤量
        	IqpGreenDeclInfo.emission_co2._obj._renderHidden(false);//项目年减排二氧化碳量
        	IqpGreenDeclInfo.emission_cod._obj._renderHidden(false);//项目年COD减排量
        	IqpGreenDeclInfo.emission_an._obj._renderHidden(false);//项目年氨氮减排量
        	IqpGreenDeclInfo.emission_so2._obj._renderHidden(false);//项目二氧化硫减排量
        	IqpGreenDeclInfo.emission_no._obj._renderHidden(false);//项目年氮氧化物减排量
        	IqpGreenDeclInfo.reduc_water._obj._renderHidden(false);//项目年节水量
        	IqpGreenDeclInfo.totl_invest._obj._renderHidden(false);//项目总投资额

        	IqpGreenDeclInfo.reduc_coal._obj._renderRequired(false);//项目年节约标准煤量
        	IqpGreenDeclInfo.emission_co2._obj._renderRequired(false);//项目年减排二氧化碳量
        	IqpGreenDeclInfo.emission_cod._obj._renderRequired(false);//项目年COD减排量
        	IqpGreenDeclInfo.emission_an._obj._renderRequired(false);//项目年氨氮减排量
        	IqpGreenDeclInfo.emission_so2._obj._renderRequired(false);//项目二氧化硫减排量
        	IqpGreenDeclInfo.emission_no._obj._renderRequired(false);//项目年氮氧化物减排量
        	IqpGreenDeclInfo.reduc_water._obj._renderRequired(true);//项目年节水量
        	IqpGreenDeclInfo.totl_invest._obj._renderRequired(true);//项目总投资额
        }else{
        	IqpGreenDeclInfo.reduc_coal._obj._renderHidden(false);//项目年节约标准煤量
        	IqpGreenDeclInfo.emission_co2._obj._renderHidden(false);//项目年减排二氧化碳量
        	IqpGreenDeclInfo.emission_cod._obj._renderHidden(false);//项目年COD减排量
        	IqpGreenDeclInfo.emission_an._obj._renderHidden(false);//项目年氨氮减排量
        	IqpGreenDeclInfo.emission_so2._obj._renderHidden(false);//项目二氧化硫减排量
        	IqpGreenDeclInfo.emission_no._obj._renderHidden(false);//项目年氮氧化物减排量
        	IqpGreenDeclInfo.reduc_water._obj._renderHidden(false);//项目年节水量
        	IqpGreenDeclInfo.totl_invest._obj._renderHidden(false);//项目总投资额

        	IqpGreenDeclInfo.reduc_coal._obj._renderRequired(false);//项目年节约标准煤量
        	IqpGreenDeclInfo.emission_co2._obj._renderRequired(false);//项目年减排二氧化碳量
        	IqpGreenDeclInfo.emission_cod._obj._renderRequired(false);//项目年COD减排量
        	IqpGreenDeclInfo.emission_an._obj._renderRequired(false);//项目年氨氮减排量
        	IqpGreenDeclInfo.emission_so2._obj._renderRequired(false);//项目二氧化硫减排量
        	IqpGreenDeclInfo.emission_no._obj._renderRequired(false);//项目年氮氧化物减排量
        	IqpGreenDeclInfo.reduc_water._obj._renderRequired(false);//项目年节水量
        	IqpGreenDeclInfo.totl_invest._obj._renderRequired(true);//项目总投资额
        }
	};	
	function doSave(){
		if(!IqpGreenDeclInfo._checkAll()){
			return;
		}
		var green_indus = IqpGreenDeclInfo.green_indus._getValue();
		if(green_indus != "2" && green_indus != "A7.1" && green_indus != "A7.2" && green_indus != "A7.3" && green_indus != "A7.4" && green_indus != "A8.3" && green_indus != "A11.1" && green_indus != "A11.3"){
			var reduc_coal = IqpGreenDeclInfo.reduc_coal._getValue();//项目年节约标准煤量
			var emission_co2 = IqpGreenDeclInfo.emission_co2._getValue();//项目年减排二氧化碳量
			var emission_cod = IqpGreenDeclInfo.emission_cod._getValue();//项目年COD减排量
			var emission_an = IqpGreenDeclInfo.emission_an._getValue();//项目年氨氮减排量
			var emission_so2 = IqpGreenDeclInfo.emission_so2._getValue();//项目二氧化硫减排量
			var emission_no = IqpGreenDeclInfo.emission_no._getValue();//项目年氮氧化物减排量
			var reduc_water = IqpGreenDeclInfo.reduc_water._getValue();//项目年节水量
			if(    (reduc_coal == "" || reduc_coal == null) 
				&& (emission_co2 == "" || emission_co2 == null) 
				&& (emission_cod == "" || emission_cod == null) 
				&& (emission_an == "" || emission_an == null) 
				&& (emission_so2 == "" || emission_so2 == null) 
				&& (emission_no =="" || emission_no ==null) 
				&& (reduc_water == "" || reduc_water == null)){
               alert("【项目年节约标准煤量】【项目年减排二氧化碳量】【项目年COD减排量】【项目年氨氮减排量】【项目二氧化硫减排量】【项目年氮氧化物减排量】【项目年节水量】必须输入一项!");
               return;
   			}
		}
		var form = document.getElementById("submitForm");
		IqpGreenDeclInfo._toForm(form);
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
					alert("保存成功！");
				}else {
					alert("保存失败！");
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
	}	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onload()">
	
	<emp:form id="submitForm" action="updateIqpGreenDeclInfoRecord.do" method="POST">
		<emp:gridLayout id="IqpGreenDeclInfoGroup" maxColumn="2" title="绿色环保产业类型">
			<emp:text id="IqpGreenDeclInfo.serno" label="业务流水号" maxlength="40" defvalue="${context.serno}" hidden="true" required="false" readonly="true" />   
			<emp:pop id="IqpGreenDeclInfo.green_indus_displayname" label="绿色产品" url="showDicTree.do?dicTreeTypeId=STD_ZB_GREEN_INDUS" returnMethod="greenPrdReturn" required="false" buttonLabel="选择" colSpan="2"  cssElementClass="emp_field_text_long_readonly" readonly="true"/>
			<emp:text id="IqpGreenDeclInfo.green_indus" label="绿色产品" hidden="true"/>
			<emp:text id="IqpGreenDeclInfo.reduc_coal" label="项目年节约标准煤量" maxlength="16" required="false" dataType="Double"/>
			<emp:text id="IqpGreenDeclInfo.emission_co2" label="项目年减排二氧化碳量" maxlength="16" required="false" dataType="Double"/>
			<emp:text id="IqpGreenDeclInfo.emission_cod" label="项目年COD减排量" maxlength="16" required="false" dataType="Double"/>
			<emp:text id="IqpGreenDeclInfo.emission_an" label="项目年氨氮减排量" maxlength="16" required="false" dataType="Double"/>
			<emp:text id="IqpGreenDeclInfo.emission_so2" label="项目二氧化硫减排量" maxlength="16" required="false" dataType="Double"/>
			<emp:text id="IqpGreenDeclInfo.emission_no" label="项目年氮氧化物减排量" maxlength="16" required="false" dataType="Double"/>
			<emp:text id="IqpGreenDeclInfo.reduc_water" label="项目年节水量" maxlength="16" required="false" dataType="Double"/>
			<emp:text id="IqpGreenDeclInfo.totl_invest" label="项目总投资额" maxlength="16" required="false" dataType="Currency" />
		</emp:gridLayout>
		
		<div align="center" id="button">
			<br>
			<%if("update".equals(op)){%>
			  <emp:button id="save" label="修改"/>
			  <emp:button id="reset" label="重置"/>
			<%}%>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
