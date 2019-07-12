<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%
	String agr_no= (String)request.getParameter("agr_no");
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function returnCus(data){
		LmtIndusListMana.cus_id._setValue(data.cus_id._getValue());
		LmtIndusListMana.cus_name._setValue(data.cus_name._getValue());
		checkIndusType(data.cus_id._getValue());
	};
	//行业名单唯一校验
	function checkIndusType(cus_id){
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

				}else {
					alert("此客户已存在于行业名单中!");
					LmtIndusListMana.cus_id._setValue('');
					LmtIndusListMana.cus_name._setValue('');
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
		var url="<emp:url action='checkUniqueType.do'/>&type=indusList&value="+cus_id;
		var postData = YAHOO.util.Connect.setForm();	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData)
	};
	function doSubmits(){
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var serno = jsonstr.serno;
				if(flag=='success'){
		            alert('新增成功!');
		            doReturn();
				}
			}
		};
		var handleFailure = function(o) {
			alert("新增失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var form = document.getElementById("submitForm");
		var result = LmtIndusListMana._checkAll();
		if(result){
			LmtIndusListMana._toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}else {
           return ;
		}
	};
	function doReturn() {
		var url = '<emp:url action="queryLmtIndusListManaList.do"/>?agr_no='+'<%=agr_no%>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addLmtIndusListManaRecord.do" method="POST">
		<emp:gridLayout id="LmtIndusListManaGroup" title="行业名单申请" maxColumn="2">
			<emp:text id="LmtIndusListMana.agr_no" label="协议编号" maxlength="40" required="true" 
			defvalue="<%=agr_no%>" colSpan="2" readonly="true" />
			<emp:pop id="LmtIndusListMana.cus_id" label="客户码" required="true"
			url="queryAllCusPop.do?cusTypCondition=BELG_LINE in ('BL100','BL200')and cus_status='20'&returnMethod=returnCus"  />
			<emp:text id="LmtIndusListMana.cus_name" label="客户名称" colSpan="2"
			 readonly="true" cssElementClass="emp_field_text_cusname"/>
			<emp:select id="LmtIndusListMana.is_do_limit" label="是否进行额度设置" 
			defvalue="2" required="true" dictname="STD_ZX_YES_NO" hidden="true"/>
			<emp:select id="LmtIndusListMana.status" label="状态" required="true" 
			readonly="true" dictname="STD_ZB_LIST_STATUS" defvalue="003" />
			<emp:textarea id="LmtIndusListMana.memo" label="备注" maxlength="250" required="false" colSpan="2" />			
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submits" label="确定"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>