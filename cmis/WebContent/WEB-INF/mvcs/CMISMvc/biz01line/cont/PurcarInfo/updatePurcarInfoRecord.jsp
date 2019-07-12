<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	String cont="";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
		if(op.equals("view")){
			request.setAttribute("canwrite","");
		} 
	}
	if(context.containsKey("cont")){
		cont = (String)context.getDataValue("cont");
		if(cont.equals("cont")){   
			request.setAttribute("canwrite","");
		}
	} 
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
    /*--user code begin--*/
	function doSave(){
		if(!PurcarInfo._checkAll()){
			return;
		}
		var form = document.getElementById("submitForm");
		PurcarInfo._toForm(form);
		//var serno = IqpBksyndic._getValue();
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

		var url = '<emp:url action="updatePurcarInfoRecord.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	}		
			
	/*--user code end--*/  
	
</script> 
</head>
<body class="page_content" >   
	<emp:form id="submitForm" action="updatePurcarInfoRecord.do" method="POST">
		<emp:gridLayout id="PurcarInfoGroup" maxColumn="2" title="机构法人购车信息">
			<emp:text id="PurcarInfo.serno" label="业务编号" maxlength="40" defvalue="${context.serno}" colSpan="2" hidden="true" required="false" readonly="true"/>
			<emp:text id="PurcarInfo.cont_no" label="合同编号" maxlength="40" required="false" hidden="true"/>
			<emp:text id="PurcarInfo.pur_amt" label="购买金额" maxlength="18" required="true" dataType="Currency" colSpan="2"/> 
			<emp:text id="PurcarInfo.loan_perc" label="贷款比例" maxlength="10" required="true" dataType="Percent" />
			<emp:text id="PurcarInfo.fst_pyr_perc" label="首付款比率" maxlength="10" required="true" dataType="Percent" />
			<emp:select id="PurcarInfo.car_collect_type" label="汽车取得方式" required="true" dictname="STD_ZB_CAR_GET"/>
			<emp:text id="PurcarInfo.car_name" label="汽车名称" maxlength="100" required="false" />
			<emp:text id="PurcarInfo.car_sale" label="汽车销售商" maxlength="100" required="false" /> 
			<emp:text id="PurcarInfo.car_brand" label="汽车品牌" maxlength="60" required="false" />
			<emp:text id="PurcarInfo.car_model" label="汽车型号" maxlength="20" required="false" />
			<emp:text id="PurcarInfo.car_shelf_no" label="车架号" maxlength="40" required="false" /> 
			<emp:select id="PurcarInfo.car_type" label="汽车种类" required="true" dictname="STD_ZB_CAR_TYPE"/> 
			<emp:select id="PurcarInfo.car_use" label="汽车用途" required="true" dictname="STD_ZB_CAR_USER"/>   
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:actButton id="save" label="保存" op="update"/>
			<emp:actButton id="reset" label="重置" op="cancel"/> 
 
		</div>
	</emp:form>
</body>
</html>
</emp:page>
