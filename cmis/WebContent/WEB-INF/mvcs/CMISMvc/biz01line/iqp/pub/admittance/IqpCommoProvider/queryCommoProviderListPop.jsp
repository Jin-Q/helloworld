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
		IqpCommoProvider._toForm(form);
		IqpCommoProviderList._obj.ajaxQuery(null,form);
	};

	function doSelect(){
		var methodName = '${context.popReturnMethod}';	
		doReturnMethod(methodName);
	}
	
	function doReturnMethod(methodName){
		if (methodName) {
			var data = IqpCommoProviderList._obj.getSelectedData();
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
<body class="page_content" >
	<form  method="POST" action="#" id="queryForm"> </form>

	<emp:gridLayout id="IqpCommoProviderGroup" title="输入查询条件" maxColumn="3">
		<emp:text id="IqpCommoProvider.provider_no" label="供应商编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<emp:returnButton label="选择返回"/>
	<emp:table icollName="IqpCommoProviderList" pageMode="true" url="pageCommoProviderNoPopList.do" reqParams="mort_catalog_no=${context.mort_catalog_no}" >
	    <emp:text id="mort_catalog_no" label="押品目录编号" />
		<emp:text id="provider_no" label="供应商编号" />
		<emp:text id="provider_no_displayname" label="供应商名称" />
		<emp:text id="linkman" label="联系人" />
		<emp:text id="link_addr" label="联系地址" />
		<emp:text id="link_phone" label="联系电话" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="status" label="状态" dictname="STD_ZB_COMMO_PROVIDER"/>
	</emp:table>
	<div align="left"><br>
	<emp:returnButton label="选择返回"/>
	</div>
</body>
</html>
</emp:page>