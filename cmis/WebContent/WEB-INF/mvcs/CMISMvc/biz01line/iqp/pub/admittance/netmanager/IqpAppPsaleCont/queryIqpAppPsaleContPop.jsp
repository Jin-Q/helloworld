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
		IqpAppPsaleCont._toForm(form);
		IqpAppPsaleContList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.IqpPsaleContGroup.reset();
	};
	
	/*--user code begin--*/
	function doSelect(){
		var methodName = '${context.returnMethod}';
		doReturnMethod(methodName);
	}
	function doReturnMethod(methodName){
		if (methodName) {
			var data = IqpAppPsaleContList._obj.getSelectedData();
			if(data!=null&&data!=''){
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin.${context.returnMethod}(data[0])");
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
	<emp:returnButton label="选择返回" id="s1"/> <br>
	</div>
	<emp:table icollName="IqpAppPsaleContList" pageMode="true" url="pageIqpPsaleContQuery.do?serno=${context.serno}&mem_cus_id=${cotnext.mem_cus_id}">
		<emp:text id="psale_cont" label="购销合同编号" />
		<emp:text id="buyer_cus_id" label="买方客户码" hidden="true"/>
		<emp:text id="buyer_cus_id_displayname" label="买方客户名称" />
		<emp:text id="barg_cus_id" label="卖方客户码" hidden="true"/>
		<emp:text id="barg_cus_id_displayname" label="卖方客户名称" /> 
		<emp:text id="cont_amt" label="合同金额" dataType="Currency"/>
		<emp:text id="start_date" label="合同起始日" />
		<emp:text id="end_date" label="合同到期日" />
		<emp:text id="net_agr_no" label="网络协议号" hidden="true"/>
	</emp:table>
	<div align="left"><br>
	<emp:returnButton label="选择返回" id="s2"/> <br>
	</div>
</body>
</html>
</emp:page>
    