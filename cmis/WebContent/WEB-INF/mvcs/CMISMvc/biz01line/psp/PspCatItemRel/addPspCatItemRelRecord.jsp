<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.*"%>
<%@page import="com.ecc.emp.data.*"%>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String catalog_id = (String)context.getDataValue("catalog_id");
%>   
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

function doSave(){
	if(!PspCatItemRel._checkAll()){
		return;
	}
	var form = document.getElementById("submitForm");
	PspCatItemRel._toForm(form);
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

	var url = '<emp:url action="addPspCatItemRelRecord.do"/>';
	url = EMPTools.encodeURI(url);
	var postData = YAHOO.util.Connect.setForm(form);	
	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
};

function getItem(data){
	PspCatItemRel.item_id._setValue(data.item_id._getValue());
};
	
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="addPspCatItemRelRecord.do" method="POST">
		<emp:gridLayout id="PspCatItemRelGroup" title="检查目录和项目关系表" maxColumn="2">  
			<emp:text id="PspCatItemRel.catalog_id" label="目录编号" defvalue="<%=catalog_id %>" maxlength="40" readonly="true" required="true" colSpan="2" /> 
			<emp:pop id="PspCatItemRel.item_id" label="项目编号" url="queryPspCheckItemPopList.do?returnMethod=getItem" reqParams="catalog_id=${context.catalog_id}"  required="true" colSpan="2" />
			<emp:text id="PspCatItemRel.seq" label="排序" maxlength="38" required="false" colSpan="2"/>         
			
			 
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

