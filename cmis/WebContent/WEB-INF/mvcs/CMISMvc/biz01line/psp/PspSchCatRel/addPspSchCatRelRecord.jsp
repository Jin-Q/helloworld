<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.*"%>
<%@page import="com.ecc.emp.data.*"%>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
    String scheme_id =(String)context.getDataValue("schemeId");
	// String scheme_id = request.getParameter("schemeId");
%> 
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

  function doSave(){  
	if(!PspSchCatRel._checkAll()){
		return;
	}
	var form = document.getElementById("submitForm");
	PspSchCatRel._toForm(form);
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
				window.opener.location.reload();
				window.close(); 
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

	  var url = '<emp:url action="addPspSchCatRelRecord.do"/>';
	  url = EMPTools.encodeURI(url);
	  var postData = YAHOO.util.Connect.setForm(form);	
	  var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
  }; 

	function getCatalog(data){
		PspSchCatRel.catalog_id._setValue(data.catalog_id._getValue());
	}   
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addPspSchCatRelRecord.do" method="POST">
		
		<emp:gridLayout id="PspSchCatRelGroup" title="方案与目录关系表" maxColumn="2">
			<emp:text id="PspSchCatRel.scheme_id" label="方案编号" defvalue="<%=scheme_id %>" maxlength="40" required="true" colSpan="2"/> 
			<emp:pop id="PspSchCatRel.catalog_id" label="目录编号" url="PspCheckCatalogPopList.do?returnMethod=getCatalog" reqParams="scheme_id=${context.schemeId}" required="true" />    
			<emp:text id="PspSchCatRel.seq" label="排序" maxlength="38" required="false" />  
		</emp:gridLayout>    
		
		<div align="center">
			<br>
			<emp:button id="save" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

