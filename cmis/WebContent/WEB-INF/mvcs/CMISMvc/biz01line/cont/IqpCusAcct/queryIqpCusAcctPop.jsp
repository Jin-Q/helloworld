<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	String serno="";
	if(context.containsKey("serno")){
		serno =(String)context.getDataValue("serno");
	}  
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>    

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpCusAcct._toForm(form);
		IqpCusAcctList._obj.ajaxQuery(null,form);
	};
	
	
	function doViewIqpCusAcct() {
		var paramStr = IqpCusAcctList._obj.getParamStr(['pk_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpCusAcctViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	
	function doReset(){
		page.dataGroups.IqpCusAcctGroup.reset();
	};

	
	
	/*--user code begin--*/
	function doReturnMethod(){
		var data = IqpCusAcctList._obj.getSelectedData();
		if (data != null && data.length !=0) {
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin.${context.returnMethod}(data[0])");
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doSelect(){
		doReturnMethod();
	}
	function doCancel(){
		window.close();
	};		
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<div align="left">
		<emp:returnButton id="s1" label="选择返回"/>
	</div>

	<emp:table icollName="IqpCusAcctList" pageMode="true" url="pageIqpCusAcctQuery.do" reqParams="serno=${context.serno}">
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="pk_id" label="pkid" hidden="true"/> 
		<emp:text id="acct_no" label="账户账号" />
		<emp:text id="acct_name" label="账户名称" />
		<emp:text id="acct_attr" label="账户属性" dictname="STD_ZB_BR_ID_ATTR" />
		<emp:text id="is_this_org_acct" label="是否本行账户" dictname="STD_ZX_YES_NO" />
		<emp:text id="opac_org_no" label="开户行行号" hidden="true"/>
		<emp:text id="opan_org_name" label="开户行行名" />
		<emp:text id="pay_amt" label="支付金额" hidden="true"/>		
	</emp:table>
	<div align="left">
			<br>
 			<emp:returnButton id="s2" label="选择返回"/>
			<emp:button id="cancel" label="关闭" />
	</div>
</body>
</html>
</emp:page>
    