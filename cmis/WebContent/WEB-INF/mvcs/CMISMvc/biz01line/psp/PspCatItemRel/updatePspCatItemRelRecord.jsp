<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

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
				alert("修改成功！");
				window.opener.location.reload();
				window.close();  
			}else {
				alert("修改失败！");
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

	var url = '<emp:url action="updatePspCatItemRelRecord.do"/>';
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
	
	<emp:form id="submitForm" action="updatePspCatItemRelRecord.do" method="POST">
		<emp:gridLayout id="PspCatItemRelGroup" maxColumn="2" title="检查目录和项目关系表">
			<emp:text id="PspCatItemRel.catalog_id" label="目录编号" maxlength="40" required="true" readonly="true" />
				<emp:pop id="PspCatItemRel.item_id" label="项目编号" url="queryPspCheckItemPopList.do?returnMethod=getItem" reqParams="catalog_id=${context.catalog_id}"  required="true" colSpan="2" />
			<emp:text id="PspCatItemRel.seq" label="排序" maxlength="38" required="false" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="save" label="修改" op="update"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
