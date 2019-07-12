<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>
<% 
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String PO_TYPE = "";
	String cus_id = "";
	if(context.containsKey("PO_TYPE")){
		PO_TYPE = context.getDataValue("PO_TYPE").toString();
	}
	if(context.containsKey("cus_id")){
		cus_id = context.getDataValue("cus_id").toString();
	}
%>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">

	/*--user code begin--*/
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpActrecpoMana._toForm(form);
		IqpActrecpoManaList._obj.ajaxQuery(null,form);
	};
	function doReturnMethod(){
		var data = IqpActrecpoManaList._obj.getSelectedData();
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
		
	function returnCus(data){
		IqpActrecpoMana.cus_id._setValue(data.cus_id._getValue());
		IqpActrecpoMana.cus_id_displayname._setValue(data.cus_name._getValue());
    };
	/*--user code end--*/
	/**add by lisj 2015-1-30 需求编号【HS141110017】保理业务改造 begin**/
	function doViewIqpActrecpoMana() {
		var paramStr = IqpActrecpoManaList._obj.getParamStr(['po_no']);
		if (paramStr != null ) {
			var url = '<emp:url action="getIqpActrecpoManaTabHelp.do"/>?'+paramStr+'&type=view&PO_TYPE=2&ggcbl=y';
			url = EMPTools.encodeURI(url);
			window.open(url,'newWindow','height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no');
		} else {
			alert('请选择一条记录！');
		}
	};
/**add by lisj 2015-1-30 需求编号【HS141110017】保理业务改造 end**/
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="IqpActrecpoManaGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="IqpActrecpoMana.po_no" label="池编号" />
		<emp:pop id="IqpActrecpoMana.cus_id_displayname" label="客户名称" url="queryAllCusPop.do?cusTypCondition=cus_status='20' and BELG_LINE IN('BL100','BL200')&returnMethod=returnCus" />
		<emp:text id="IqpActrecpoMana.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewIqpActrecpoMana" label="查看" />
		<emp:returnButton id="s1" label="选择返回"/>
	</div>
	<emp:table icollName="IqpActrecpoManaList" pageMode="true" url="pageIqpActrecpoManaPopQuery.do" reqParams="PO_TYPE=${context.PO_TYPE}&cus_id=${context.cus_id}">
		<emp:text id="po_no" label="池编号" />
		<emp:text id="po_type" label="池类别" dictname="STD_ACTRECPO_TYPE"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="invc_quant" label="在池发票数量" />
		<emp:text id="invc_amt" label="在池发票总金额" dataType="Currency"/>
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />		
		<emp:text id="status" label="池状态" dictname="STD_ACTRECPO_STATUS"/>
	</emp:table>
	<div align="left">
			<br>
 			<emp:returnButton id="s2" label="选择返回"/>
			<emp:button id="cancel" label="关闭" />
	</div>
</body>
</html>
</emp:page>
    