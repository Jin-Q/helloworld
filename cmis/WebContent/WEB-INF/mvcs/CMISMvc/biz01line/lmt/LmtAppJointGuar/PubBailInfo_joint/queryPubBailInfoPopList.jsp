<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<% 
	String serno = request.getParameter("serno");
	//String cus_id = request.getParameter("cus_id");
%>
<script type="text/javascript">
	/*--user code begin--*/
	function doQuery(){
		var form = document.getElementById('queryForm');
		PubBailInfo._toForm(form);
		PubBailInfoList._obj.ajaxQuery(null,form);
	};
	function doReset(){
		page.dataGroups.PubBailInfoGroup.reset();
	};
	function doSelect(){
		var methodName = '${context.popReturnMethod}';	
		doReturnMethod(methodName);
	}
	function doReturnMethod(methodName){
		if (methodName) {
			var data = PubBailInfoList._obj.getSelectedData();
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
	function doClose(){
		window.close();
	}
	/*--user code end--*/
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<div align="left"><br>
		<emp:returnButton label="选择返回"/> <br>
	</div>

	<emp:table icollName="PubBailInfoList" pageMode="true" url="pagePubBailInfoQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="bail_acct_no" label="保证金账号" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="rate" label="利率" />
		<emp:text id="dep_term" label="存期" />
		<emp:text id="open_org" label="开户机构" />
		<emp:text id="bail_status" label="状态" dictname="STD_BAIL_INFO_STATUS"/>
	</emp:table>
	<div align="left"><br>
		<emp:returnButton label="选择返回"/>
		<emp:button id="close" label="关闭"/> 
	</div>
</body>
</html>
</emp:page>
    