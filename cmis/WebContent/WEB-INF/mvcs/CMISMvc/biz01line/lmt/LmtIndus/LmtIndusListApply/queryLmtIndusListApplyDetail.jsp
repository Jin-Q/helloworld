<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 450px;
};
</style>
<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryLmtIndusListApplyList.do"/>?serno=${context.serno}&action=${context.action}';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	//控制div的显示隐藏
	function showOrHiddenDiv(){
		var is_limit_set = LmtIndusListApply.is_do_limit._getValue();
		if(is_limit_set=='1'){
			document.getElementById("appDetails_div").style.display = "";
		}else{
			document.getElementById("appDetails_div").style.display = "none";
		}
	};
	function doLoad(){
		LmtIndusListApply.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
		showOrHiddenDiv();
	};
	//查看客户详情
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+LmtIndusListApply.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	function changeHeight(){
		var iframeid = document.getElementById("rightframe");
		iframeid.height = "80px";
		iframeid.style.height = "80px";
		if(iframeid.contentDocument && iframeid.contentDocument.body.offsetHeight){
			iframeid.height = iframeid.contentDocument.body.offsetHeight;
		}else if(iframeid.Document && iframeid.Document.body.scrollHeight){
			iframeid.height = iframeid.Document.body.scrollHeight;
		}
		if(iframeid.height != "undefined")
			iframeid.style.height = iframeid.height + "px";
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:gridLayout id="LmtIndusListApplyGroup" title="行业名单申请表" maxColumn="2">
			<emp:text id="LmtIndusListApply.serno" label="业务编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="LmtIndusListApply.cus_id" label="客户码" required="true" readonly="true" colSpan="2"/>
			<emp:text id="LmtIndusListApply.cus_id_displayname" label="客户名称" colSpan="2" required="true"
			readonly="true" cssElementClass="emp_field_text_input2"/>
			<emp:select id="LmtIndusListApply.is_do_limit" label="是否进行额度设置" hidden="true" required="true" dictname="STD_ZX_YES_NO" />
			<emp:select id="LmtIndusListApply.status" label="状态" required="true" dictname="STD_ZB_LIST_STATUS" readonly="true" />
			<emp:textarea id="LmtIndusListApply.memo" label="备注" maxlength="250" required="false" colSpan="2" />
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表"/>
	</div>
	<div align="left" id="appDetails_div">
	<iframe id="rightframe" name="rightframe" onload="changeHeight()" 
	src="<emp:url action='getLmtDetailsList.do'/>&cus_id=${context.LmtIndusListApply.cus_id}&hidden_button=true&serno=${context.LmtIndusListApply.serno}" 
	frameborder="0" scrolling="auto"  width="100%">
	</iframe>
	</div>
</body>
</html>
</emp:page>
