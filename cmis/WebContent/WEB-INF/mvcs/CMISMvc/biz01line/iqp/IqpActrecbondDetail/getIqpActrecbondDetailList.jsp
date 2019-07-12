<%@page language="java" contentType="text/html; charset=UTF-8"%>
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
		IqpActrecbondDetail._toForm(form);
		IqpActrecbondDetailList._obj.ajaxQuery(null,form);
	};
	
	function doReturnMethod(){
		var data = IqpActrecbondDetailList._obj.getSelectedData();
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
	
	function doReset(){
		page.dataGroups.IqpActrecbondDetailGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="IqpActrecbondDetailGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="IqpActrecbondDetail.invc_no" label="发票号" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />

	
	<emp:returnButton id="s1" label="选择返回"/>
	
	<emp:table icollName="IqpActrecbondDetailList" pageMode="false" url="pageIqpActrecbondDetailQuery.do?po_no=${context.po_no}">
		<emp:text id="po_no" label="池编号" hidden="true"/>
		<emp:text id="buy_cus_name" label="买方客户名称" />
		<emp:text id="sel_cus_name" label="卖方客户名称" />
		<emp:text id="cont_no" label="贸易合同编号" />
		<emp:text id="invc_no" label="发票号" />
		<emp:text id="invc_amt" label="发票金额" dataType="Currency"/>
		<emp:text id="bond_amt" label="债权金额" dataType="Currency"/>
		<emp:text id="invc_date" label="开票日期" />
		<emp:text id="bond_pay_date" label="付款到期日" />
		<emp:select id="status" label="状态" hidden="false" dictname="STD_ACTREC_INVC_STATUS"/>
		
		<emp:text id="bond_mode" label="债权类型" hidden="true"/>
		<emp:text id="invc_ccy" label="发票币种" hidden="true"/>
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
    