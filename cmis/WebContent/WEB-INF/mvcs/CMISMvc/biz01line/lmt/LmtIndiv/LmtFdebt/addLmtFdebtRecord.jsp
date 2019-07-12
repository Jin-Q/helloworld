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
	function returnCus(data){
		LmtFdebt.cus_id._setValue(data.cus_id._getValue());
		LmtFdebt.cus_id_displayname._setValue(data.cus_name._getValue());
		LmtFdebt.cus_attr._setValue(data.cus_attr._getValue());//客户属性
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
					LmtFdebt.cus_id._setValue("");
					LmtFdebt.cus_id_displayname._setValue("");
					LmtFdebt.cus_attr._setValue("");
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
			var cus_id = LmtFdebt.cus_id._getValue();
			var serno = LmtFdebt.serno._getValue();
			var url = '<emp:url action="checkCusIdAdd4Debt.do"/>&cus_id='+cus_id+"&serno="+serno;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
		
	}

	function doReturn(){
		window.close();
	}

	function doload(){
		//var urls = '&lmt_serno=<%=lmt_serno%>';
		var cus_id = '<%=cus_id %>';
		//var param = "cusTypCondition=cus_id in( select cus_id from Cus_Indiv_Soc_Rel where indiv_cus_rel in('1','2','3','9') and cus_id_rel='"+cus_id+"') or cus_id ='"+cus_id+"'&returnMethod=returnCus";
		var urls = "&lmt_serno=<%=lmt_serno%>";
		//var url = '<emp:url action="queryAllCusPop.do"/>?'+param;
		//url = EMPTools.encodeURI(url);
		LmtFdebt.cus_id._obj.config.url=LmtFdebt.cus_id._obj.config.url+urls;
		
	}

	function doAddLmtFdebt(){
		var form = document.getElementById("submitForm");
		LmtFdebt._checkAll();
		if(LmtFdebt._checkAll()){
			LmtFdebt._toForm(form);
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

	function checkDate(){
		start = LmtFdebt.start_date._getValue();
		over = LmtFdebt.over_date._getValue();
		if (start!=null && start!="" &&over!=null && over!=""){
			var flag = CheckDate1BeforeDate2(start,over);
			if(!flag){
				alert("负债结束日期要大于负债开始日期！！");
				LmtFdebt.over_date._setValue("");
				return false;
			}			
		} 
	}
	/*--user code end--*/
	
</script>  
</head>
<body class="page_content" onload="doload()">
	
	<emp:form id="submitForm" action="addLmtFdebtRecord.do" method="POST">
		
		<emp:gridLayout id="LmtFdebtGroup" title="家庭负债" maxColumn="2">
			<emp:pop id="LmtFdebt.cus_id" label="客户码" url="queryRelaCusByLmtSernoPop.do?returnMethod=returnCus" colSpan="2" popParam="width=700px,height=650px" required="true"/>
			<emp:text id="LmtFdebt.cus_id_displayname" label="客户名称"  required="true" readonly="true" cssElementClass="emp_field_text_readonly" colSpan="2"/>
			<emp:select id="LmtFdebt.cus_attr" label="客户属性" required="false" dictname="STD_ZB_CUS_ATTR" readonly="true"/>
			<emp:select id="LmtFdebt.debt_type" label="负债类型" required="true" dictname="STD_ZB_DEBT_TYPE" />
			<emp:select id="LmtFdebt.cur_type" label="币种" required="false" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" colSpan="2" readonly="true"/>
			<emp:text id="LmtFdebt.debt_amt" label="负债金额" maxlength="18" required="true" dataType="Currency" />
			<emp:text id="LmtFdebt.debt_bal" label="负债余额" maxlength="18" required="true" dataType="Currency" />
			<emp:date id="LmtFdebt.start_date" label="债务开始日期" required="true" onblur="checkDate()"/>
			<emp:date id="LmtFdebt.over_date" label="债务结束日期" required="true" onblur="checkDate()"/>
			<emp:textarea id="LmtFdebt.memo" label="备注" maxlength="60" required="false" colSpan="2" />
			<emp:text id="LmtFdebt.serno" label="流水号" maxlength="40" required="false" defvalue="<%=lmt_serno%>" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="addLmtFdebt" label="确定" />
			<emp:button id="return" label="关闭"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

