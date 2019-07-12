<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
	String guarContNo = (String)request.getParameter("guar_cont_no");
	String flag = (String)request.getParameter("flag");//授信模块标识
%>

<script type="text/javascript">
	
	function doReturn() {
		var flag = '<%=flag%>'
		if(flag=='lmt')
		{
            window.close();
            return false;
		}
		var guarContNo = '<%=guarContNo%>';
		var menuIdTab = '${context.menuIdTab}';
		var url = '<emp:url action="queryGrtGuaranteeList.do"/>&menuIdTab='+menuIdTab+'&guar_cont_no='+guarContNo;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function doLoad(){
		var guarContNo = '<%=guarContNo%>';
		GrtGuarantee.guar_cont_no._setValue(guarContNo);
		GrtGuarantee.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
	}
	
	/*--user code begin--*/
	//查看客户信息
	function viewCusInfo(){
		var cus_id = GrtGuarantee.cus_id._getValue();
		if(cus_id==null||cus_id==''){
			alert('客户码为空！');
		}else {
			var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id;
	      	url=encodeURI(url); 
	      	windowName = Math.ceil(Math.random()*50000000);
	      	window.open(url,windowName+'','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:gridLayout id="GrtGuaranteeGroup" title="保证人信息" maxColumn="2">
			<emp:text id="GrtGuarantee.guar_id" label="保证编码 " maxlength="40" required="false" hidden="true"/>
			<emp:text id="GrtGuarantee.cus_id" label="保证人客户码" required="true"/>
			<emp:text id="GrtGuarantee.cus_id_displayname" label="保证人名称" required="true" readonly="true" cssElementClass="emp_field_text_readonly" colSpan="2"/>
			<emp:select id="GrtGuarantee.guar_type" label="保证形式" required="true" dictname="STD_GUAR_FORM" />
			<emp:select id="GrtGuarantee.is_spadd" label="是否为追加担保" required="true" dictname="STD_ZX_YES_NO" />
			<emp:text id="GrtGuarantee.guar_amt" label="担保金额" maxlength="18" required="true" dataType="Currency"/>
			<emp:text id="GrtGuarantee.guar_cont_no" label="合同编号" maxlength="30" required="false" hidden="false"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
