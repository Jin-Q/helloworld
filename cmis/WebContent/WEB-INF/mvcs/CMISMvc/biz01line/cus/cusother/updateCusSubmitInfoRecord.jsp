<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@ page import="com.yucheng.cmis.pub.util.TimeUtil" %>
<emp:page>
<%
	String cus_id = request.getParameter("cus_id");
	boolean backFlag = "back".equals(request.getParameter("back"));
	String twoSub = request.getParameter("twoSub");
%>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doSubmto(){
		if(CusSubmitInfo._checkAll()){
		    var handleSuccess = function(o){ 
				EMPTools.unmask();
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("提交失败!");
						return;
					}
					var flag=jsonstr.flag;	
					if(flag=="submit"){
						alert("提交成功!");	
						window.close();
						window.opener.location.reload();						
					}else {
						alert("提交失败!");
					}
				}
			};
			var handleFailure = function(o){
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			}; 
			//设置form
			var form = document.getElementById("submitForm");
			CusSubmitInfo._toForm(form)
			var postData = YAHOO.util.Connect.setForm(form);	 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	}
	
	function doBack(){
		if(CusSubmitInfo._checkAll()){
			//打回
			CusSubmitInfo.opr_type._obj.element.value = '2';
			//设置form
			var handleSuccess = function(o){ 
				EMPTools.unmask();
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("操作失败!");
						return;
					}
					var flag=jsonstr.flag;	
					if(flag=="submit"){
						alert("打回失败");							
					}else{
						alert("打回成功");	
						window.close();
						window.opener.location.reload();
						} 
					}
			};
			var handleFailure = function(o){};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			}; 
			//设置form
			var form = document.getElementById("submitForm");
			CusSubmitInfo._toForm(form)
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	}

	//重置
	function doCancle(){
		window.close();
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addCusSubmitInfoRecord.do" method="POST">
		<emp:gridLayout id="CusSubmitInfoGroup" maxColumn="2" title="客户影像扫描信息录入操作">
			<emp:text id="CusSubmitInfo.cus_id" label="客户码" maxlength="21" required="true" defvalue="<%=cus_id%>" readonly="true" hidden="true"/>
			<emp:text id="CusSubmitInfo.cus_name" label="客户名称" maxlength="60" required="false" hidden="true"/>
			<emp:text id="CusSubmitInfo.submit_id" label="提交人" maxlength="20" required="true" defvalue="$currentUserId" hidden="true"/>
			<emp:text id="CusSubmitInfo.rcv_id" label="接收人" maxlength="20" hidden="true"  />
			<emp:textarea id="CusSubmitInfo.memo" label="提示信息" maxlength="400" required="true" colSpan="2" />
			<emp:date id="CusSubmitInfo.input_date" label="提交日期" required="true" defvalue="$OPENDAY" readonly="true"/>
			<emp:text id="CusSubmitInfo.serno" label="流水号" maxlength="32" readonly="true" hidden="true"/>
			<emp:text id="CusSubmitInfo.end_flag" label="完成标志(0.完成 1.未完成)" maxlength="1" required="true" defvalue="1" hidden="true" />
			<emp:text id="CusSubmitInfo.opr_time" label="具体时间" maxlength="20" hidden="true" />
			<emp:text id="CusSubmitInfo.opr_type" label="操作类型(1.提交 2.打回 3.移交)" maxlength="10" readonly="false" hidden="true" defvalue="1"/>
			<emp:text id="CusSubmitInfo.twoSubFlag" label="再次提交" maxlength="10" readonly="false" hidden="true" defvalue="<%=twoSub%>"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<% if(backFlag){%>
				<emp:button id="back" label="打回" />
			<%}else{ %>
				<emp:button id="submto" label="提交" />
				<emp:button id="cancle" label="重置"/>
			<%} %>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
