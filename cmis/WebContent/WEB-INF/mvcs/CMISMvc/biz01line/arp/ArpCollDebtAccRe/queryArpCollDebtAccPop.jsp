<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		ArpCollDebtAccRe._toForm(form);
		ArpCollDebtAccReList._obj.ajaxQuery(null,form);
	};
	function doReset(){
		page.dataGroups.ArpCollDebtAccReGroup.reset();
	};
	function doSelect(){
		var methodName = '${context.popReturnMethod}';	
		doReturnMethod(methodName);
	}
	function doReturnMethod(methodName){
		if (methodName) {
			var data = ArpCollDebtAccReList._obj.getSelectedData();
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
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<div align="left"><br>
	<emp:returnButton label="选择返回"/> <br>
	</div>
	<emp:table icollName="ArpCollDebtAccReList" pageMode="false" url="pageArpCollDebtAccReQuery.do">
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="debt_acc_no" label="抵债台账编号" hidden="true" />
		<emp:text id="guaranty_no" label="押品编号" />
		<emp:text id="guaranty_name" label="押品名称" />
		<emp:text id="guaranty_type_displayname" label="押品类型" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="debt_in_amt" label="抵入金额" dataType="Currency"/>
		<emp:text id="status" label="状态" dictname="STD_ZB_DEBT_RE_STATUS" />
		<emp:text id="guaranty_info_status" label="押品信息状态" dictname="STD_MORT_STATE" hidden="true"/>
	</emp:table>
	<div align="left"><br>
	<emp:returnButton label="选择返回"/>
	<emp:button id="close" label="关闭"/> <br>
	</div>
</body>
</html>
</emp:page>
    