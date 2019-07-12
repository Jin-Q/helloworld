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

<jsp:include page="/include.jsp" flush="true"/>
<%
	String lmt_serno = request.getParameter("lmt_serno");
%>
<script type="text/javascript">

	/*--user code begin--*/
	function doReturn(){
		window.close();
	}

	function returnCus(data){
		//客户码,证件类型,证件号码,姓名
    	LmtFpayout.cus_id._setValue(data.cus_id._getValue());
    	LmtFpayout.cus_id_displayname._setValue(data.cus_name._getValue());
    	LmtFpayout.cert_type._setValue(data.cert_type._getValue());
    	LmtFpayout.cert_code._setValue(data.cert_code._getValue());
    	LmtFpayout.cus_attr._setValue(data.cus_attr._getValue());//客户属性
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
					LmtFpayout.cus_id._setValue("");
					LmtFpayout.cus_id_displayname._setValue("");
					LmtFpayout.cert_type._setValue("");
					LmtFpayout.cert_code._setValue("");
					LmtFpayout.cus_attr._setValue("");
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
			var cus_id = LmtFpayout.cus_id._getValue();
			var serno = LmtFpayout.serno._getValue();
			var url = '<emp:url action="checkCusIdAdd4payout.do"/>&cus_id='+cus_id+"&serno="+serno;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
		
	}

	function doAddFpayout(){
		var form = document.getElementById("submitForm");
		LmtFpayout._checkAll();
		if(LmtFpayout._checkAll()){
			LmtFpayout._toForm(form);
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

	function checkPayout(){
		var mpayout = LmtFpayout.mpayout._getValue();
		var ypayout = LmtFpayout.ypayout._getValue();
		if(parseFloat(mpayout)>parseFloat(ypayout)){
			alert("月收入必须小于年收入！");
			LmtFpayout.mpayout._setValue("");
			return;
		}
	}
	
	function doload(){
		//var urls = '&lmt_serno=<%=lmt_serno%>';
		var cus_id = '<%=cus_id %>';
		//var param = "cusTypCondition=cus_id in( select cus_id from Cus_Indiv_Soc_Rel where indiv_cus_rel in('1','2','3','9') and cus_id_rel='"+cus_id+"') or cus_id = '"+cus_id+"'&returnMethod=returnCus";
		var urls = "&lmt_serno=<%=lmt_serno%>";
		//var url = '<emp:url action="queryAllCusPop.do"/>?'+param;
		//url = EMPTools.encodeURI(url);
		LmtFpayout.cus_id._obj.config.url=LmtFpayout.cus_id._obj.config.url+urls;
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	
	<emp:form id="submitForm" action="addLmtFpayoutRecord.do" method="POST"> 
		
		<emp:gridLayout id="LmtFpayoutGroup" title="家庭支出" maxColumn="2">
			<emp:pop id="LmtFpayout.cus_id" label="客户码" url="queryRelaCusByLmtSernoPop.do?returnMethod=returnCus" colSpan="2" popParam="width=700px,height=650px" required="true"/>
			<emp:text id="LmtFpayout.cus_id_displayname" label="客户名称"  required="true" readonly="true" cssElementClass="emp_field_text_readonly" colSpan="2"/>
			<emp:select id="LmtFpayout.cert_type" label="证件类型" required="false" dictname="STD_ZB_CERT_TYP" readonly="true"/>
			<emp:text id="LmtFpayout.cert_code" label="证件号码" required="false"  readonly="true"/>
			<emp:select id="LmtFpayout.cus_attr" label="客户属性" required="false" dictname="STD_ZB_CUS_ATTR" readonly="true" colSpan="2"/>
			<emp:select id="LmtFpayout.fpayout_type" label="家庭支出类型" required="true" dictname="STD_ZB_FPAYOUT_TYPE"  colSpan="2"/>
			<emp:text id="LmtFpayout.mpayout" label="月支出" maxlength="18" required="true" dataType="Currency" onchange="checkPayout()"/>
			<emp:text id="LmtFpayout.ypayout" label="年支出" maxlength="18" required="true" dataType="Currency" onchange="checkPayout()"/>
			<emp:textarea id="LmtFpayout.memo" label="备注" maxlength="60" required="false" colSpan="2" />
			<emp:text id="LmtFpayout.serno" label="流水号" maxlength="40" required="false" defvalue="<%=lmt_serno%>" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="AddFpayout" label="确定"/>
			<emp:button id="return" label="关闭"/>
		</div>
	</emp:form> 
	
</body>
</html>
</emp:page>

