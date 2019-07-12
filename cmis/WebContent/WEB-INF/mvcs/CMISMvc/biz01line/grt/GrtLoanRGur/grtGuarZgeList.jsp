<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<%
Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
%>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	function doQuery(){
		var form = document.getElementById('queryForm');
		GrtGuarCont._toForm(form);
		GrtGuarContList._obj.ajaxQuery(null,form);
	};
	
	function doSelect(){
		var data = GrtGuarContList._obj.getSelectedData();
		if (data != null && data.length !=0) {
			var guar_amt = data[0].guar_amt._getValue();
			if(guar_amt == null || guar_amt == ""){
				alert("此笔担保合同金额为空,请检查!");
				return;   
			}    
            var guar_cont_no = data[0].guar_cont_no._getValue(); 
            var guar_amt = data[0].guar_amt._getValue();
            var url = '<emp:url action="getGrtLoanRGurAddPage.do"/>?isFromLmt=is&serno=${context.serno}&guar_cont_type=${context.guar_cont_type}&limit_acc_no=${context.limit_acc_no}&limit_credit_no=${limit_credit_no}&cus_id=${context.cus_id}&guar_cont_no='+guar_cont_no;   
    		url=EMPTools.encodeURI(url); 
          	window.open(url,'newwindow','height=538,width=1024,top=170,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no'); 
		} else {       
			alert('请先选择一条记录！');
		}
	};
	function doCancel(){
		window.close();
	};
	function doViewGrtLoanRGurZge() { 
		var paramStr = GrtGuarContList._obj.getParamStr(['guar_cont_no']);  
		var flag ="view";   
		if (paramStr != null) {  
			var url = '<emp:url action="getGrtGuarContViewPage.do"/>?menuId=zge&op=view&'+paramStr+'&flag=loan&oper='+flag;
			url = EMPTools.encodeURI(url);  
			window.open(url,'newwindow2','height=600,width=1200,top=200,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no'); 
		} else {
			alert('请先选择一条记录！');     
		}
	};   
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<div align="left">
	     <emp:button id="select" label="引入" />
	     <emp:button id="viewGrtLoanRGurZge" label="查看"/>  
	</div>

	<emp:table icollName="GrtGuarContList" pageMode="false" url="">   
	    <emp:text id="guar_cont_no" label="担保合同编号 " />
	    <emp:text id="guar_cont_cn_no" label="中文合同编号" />
		<emp:text id="guar_cont_type" label="担保合同类型" dictname="STD_GUAR_CONT_TYPE" />
		<emp:text id="guar_way" label="担保方式" dictname="STD_GUAR_TYPE" />		
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="guar_amt" label="担保合同金额" dataType="Currency"/>
		<emp:text id="guar_start_date" label="担保起始日" />
		<emp:text id="guar_end_date" label="担保终止日" />
		<emp:text id="guar_cont_state" label="担保合同状态" dictname="STD_CONT_STATUS" />
	</emp:table>
	<div align="left">
	<br>
 			<emp:button id="select" label="引入" />
			<emp:button id="cancel" label="关闭" />
	</div>
</body>
</html>
</emp:page>
    