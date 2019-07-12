<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<% 
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String po_no = context.getDataValue("po_no").toString();
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpExpInfo._toForm(form);
		IqpExpInfoList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.IqpExpInfoGroup.reset();
	};
	
	/*--user code begin--*/
	function doReturnMethod(){
		var data = IqpExpInfoList._obj.getSelectedData();
		if (data != null && data.length !=0) {
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin.${context.returnMethod}(data[0])");
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doSelect()
	{
		doReturnMethod();
	}
	function doCancel(){
		window.close();
	};		
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	
	<emp:returnButton id="s1" label="选择返回"/>

	<emp:table icollName="IqpExpInfoList" pageMode="false" url="pageIqpExpInfoQuery.do">
		<emp:text id="po_no" label="池编号" hidden="true"/>
		<emp:text id="express_no" label="快递单号" />
		<emp:text id="express_cprt" label="快递公司" />
		<emp:text id="start_date" label="快递发出日期" />
		<emp:text id="receive_date" label="快递接收日期" />
		<emp:text id="invc_no" label="发票号" />
		<emp:text id="invc_amt" label="发票金额" dataType="Currency"/>
		<emp:text id="invc_date" label="开票日期" />
		
		
		<emp:text id="memo" label="备注" hidden="true"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" hidden="true"/>
	</emp:table>
	
	<div align="left">
			<br>
 			<emp:returnButton id="s2" label="选择返回"/>
			<emp:button id="cancel" label="关闭" />
	</div>
</body>
</html>
</emp:page>
    