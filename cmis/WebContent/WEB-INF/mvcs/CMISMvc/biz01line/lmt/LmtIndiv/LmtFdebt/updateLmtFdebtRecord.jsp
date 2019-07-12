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

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doUpdatee() {
		var form = document.getElementById("submitForm");
		var result = LmtFdebt._checkAll();
		if(result){
			LmtFdebt._toForm(form)
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

	function doReturn(){
		window.close();
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
<body class="page_content">
	
	<emp:form id="submitForm" action="updateLmtFdebtRecord.do" method="POST">
		<emp:gridLayout id="LmtFdebtGroup" maxColumn="2" title="家庭负债">
			<emp:pop id="LmtFdebt.cus_id" label="客户码" url="queryAllCusPop.do?cusTypCondition=cus_id in( select cus_id from Cus_Indiv_Soc_Rel where indiv_cus_rel in('1','2','3','9') and cus_id_rel='${context.cus_id}') or cus_id = '${context.cus_id}'&returnMethod=returnCus" colSpan="2" popParam="width=700px,height=650px" readonly="true" required="true"/>
			<emp:text id="LmtFdebt.cus_id_displayname" label="客户名称"  required="true" readonly="true" cssElementClass="emp_field_text_readonly" colSpan="2"/>
			<emp:select id="LmtFdebt.cus_attr" label="客户属性" required="false" dictname="STD_ZB_CUS_ATTR" readonly="true"/>
			<emp:select id="LmtFdebt.debt_type" label="负债类型" required="true" dictname="STD_ZB_DEBT_TYPE" />
			<emp:select id="LmtFdebt.cur_type" label="币种" required="false" dictname="STD_ZX_CUR_TYPE" readonly="true" colSpan="2"/>
			<emp:text id="LmtFdebt.debt_amt" label="负债金额" maxlength="18" required="false" dataType="Currency" />
			<emp:text id="LmtFdebt.debt_bal" label="负债余额" maxlength="18" required="false" dataType="Currency" />
			<emp:date id="LmtFdebt.start_date" label="债务开始日期" required="false" onblur="checkDate()"/>
			<emp:date id="LmtFdebt.over_date" label="债务结束日期" required="false" onblur="checkDate()"/>
			<emp:textarea id="LmtFdebt.memo" label="备注" maxlength="60" required="false" colSpan="2" />
			<emp:text id="LmtFdebt.serno" label="流水号" required="false" hidden="true"/>
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
