<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
 <% String net_agr_no=(String)request.getParameter("net_agr_no");
    String mem_cus_id=(String)request.getParameter("mem_cus_id");
    String mem_manuf_type=(String)request.getParameter("mem_manuf_type");
 %>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpPsaleCont._toForm(form);
		IqpPsaleContList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.IqpPsaleContGroup.reset();
	};
	
	/*--user code begin--*/
	function doSelect(){
		var methodName = '${context.popReturnMethod}';	
		doReturnMethod(methodName);
	}
	function doReturnMethod(methodName){
		if (methodName) {
			var data = IqpPsaleContList._obj.getSelectedData();
			if(data!=null&&data!=''){
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin."+methodName+"(data[0])");
			window.close();
			}else{
				alert('请先选择一条记录！');
			}
		}else{
			alert("未定义返回的函数，请检查弹出按钮的设置!");
		}
	};	
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
<div align="left"><br>
	<emp:returnButton label="选择返回"/> <br>
	</div>
	<emp:table icollName="IqpPsaleContList" pageMode="true" url="pageIqpPsaleContQuery.do?net_agr_no=${context.net_agr_no}">
		<emp:text id="psale_cont" label="购销合同编号" />
		<emp:text id="start_date" label="起始到期" />
		<emp:text id="end_date" label="到期日期" />
		<emp:text id="cont_amt" label="合同金额" dataType="Currency"/>
		<emp:text id="net_agr_no" label="网络协议号" hidden="true"/>
	</emp:table>
	<div align="left"><br>
	<emp:returnButton label="选择返回"/> <br>
	</div>
</body>
</html>
</emp:page>
    