<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
String guaranty_no = request.getParameter("guaranty_no");
//request = (HttpServletRequest) pageContext.getRequest();
Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
%>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
  	function doNext(){
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
				if("success" == flag){
					alert("保存成功！");
					doReturn();
				}else{
					alert("保存失败！");
				}
			}
		};
		var handleFailure = function(o) {
			alert("保存失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var form = document.getElementById('submitForm');
		var result = MortGuarantySuddenInfo._checkAll();
	    if(result){
	    	MortGuarantySuddenInfo._toForm(form);
			page.dataGroups.dataGroup_in_formsubmitForm.toForm(form);
	    	var postData = YAHOO.util.Connect.setForm(form);
	 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	    }else {
		    alert("保存失败！\n请检查各标签页面中的必填信息是否遗漏！");
		}
	};			
	function checkDt(){
		var occur_date = MortGuarantySuddenInfo.occur_date._getValue();
		var openDay='${context.OPENDAY}';
		if(occur_date!=''){
			if(CheckDate1BeforeDate2(openDay,occur_date)){
	    		alert('发生日期不能大于当前日期！');
	    		MortGuarantySuddenInfo.occur_date._obj.element.value="";
	    		return;
	    	}
    	}
	}
	function doReturn(){
		var guaranty_no = MortGuarantySuddenInfo.guaranty_no._getValue();
		var url = '<emp:url action="queryMortGuarantySuddenInfoList.do"/>?menuIdTab=mort_maintain&guaranty_no='+guaranty_no;
		url = EMPTools.encodeURI(url);
		window.location=url;
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addMortGuarantySuddenInfoRecord.do" method="POST">
		
		<emp:gridLayout id="MortGuarantySuddenInfoGroup" title="记录抵质押品意外情况" maxColumn="2">
			<emp:text id="MortGuarantySuddenInfo.accident_no" label="意外情况编码" maxlength="40" required="false" hidden="true"/>
			<emp:text id="MortGuarantySuddenInfo.guaranty_no" label="押品编号" maxlength="40" required="false" hidden="true" defvalue="<%=guaranty_no %>"/>
			<emp:select id="MortGuarantySuddenInfo.accident_type" label="意外情况类型" required="true" dictname="STD_ACCIDENT_INSU_TYPE" />
			<emp:date id="MortGuarantySuddenInfo.occur_date" label="发生日期" required="true" onblur="checkDt()" />
			<emp:textarea id="MortGuarantySuddenInfo.accident_resn" label="意外原因" maxlength="2000" required="true" colSpan="2" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="next" label="保存" />
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

