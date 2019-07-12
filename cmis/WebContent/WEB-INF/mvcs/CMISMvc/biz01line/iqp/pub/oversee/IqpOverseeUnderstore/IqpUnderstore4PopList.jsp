<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	/*--user code begin--*/
	function doSelect(){
		var methodName = '${context.popReturnMethod}';	
		doReturnMethod(methodName);
	}
	function doReturnMethod(methodName){
		if (methodName) {
			var data = IqpOverseeUnderstoreList._obj.getSelectedData();
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

	function doCancel(){
		window.close();
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
	<emp:table icollName="IqpOverseeUnderstoreList" pageMode="true" url="getIqpOverseeUnderstorePage.do">		
		<emp:text id="store_id" label="仓库编号" />
		<emp:text id="store_name" label="仓库名称" />
		<emp:text id="store_addr_displayname" label="仓库地址" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="store_addr" label="仓库地址" hidden="true"/>
		<emp:text id="oversee_org_id" label="监管机构编号" hidden="true"/>
	</emp:table>
	<div align="left"><br>
		<emp:returnButton label="选择返回"/>
		<emp:button id="cancel" label="关闭" />
	</div>
</body>
</html>
</emp:page>
    