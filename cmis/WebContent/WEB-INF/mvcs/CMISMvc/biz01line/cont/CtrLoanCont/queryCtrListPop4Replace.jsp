<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
%>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
function doSelect(){
	var methodName="${context.popReturnMethod}";
	doReturnMethod(methodName);
};

function doReturnMethod(methodName){
	if (methodName) {
		var data = AccViewList._obj.getSelectedData();
		if(data!=null&&data!=''){
		var parentWin = EMPTools.getWindowOpener();
		eval("parentWin."+methodName+"(data[0])");
		window.close();
		}else {
			alert('请先选择一条记录！');
		}
	}else{
		alert("未定义返回的函数，请检查弹出按钮的设置!");
	}
};	
	
</script>
</head>
<body class="page_content">
	<div align="left">
		<emp:returnButton label="选择返回"/>
	</div>

	<emp:table icollName="AccViewList" pageMode="true" url="pageQueryCtrListPop4Replace.do" reqParams="prd_id=${context.prd_id}&cus_id=${context.cus_id}">
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />  
		<emp:text id="bill_amt" label="借据金额" dataType="Currency"/>  
		<emp:text id="bill_bal" label="借据余额" dataType="Currency"/>  
		<emp:text id="prd_id" label="产品编号" hidden="true"/>
		<emp:text id="prd_id_displayname" label="产品名称" />
	</emp:table>
	<br>
     <emp:returnButton label="选择返回"/>
</body>
</html>
</emp:page>