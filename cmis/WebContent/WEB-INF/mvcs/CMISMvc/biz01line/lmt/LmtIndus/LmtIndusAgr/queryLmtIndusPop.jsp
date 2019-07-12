<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表POP页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtIndusAgr._toForm(form);
		LmtIndusAgrList._obj.ajaxQuery(null,form);
	};
	
	
	function doReset(){
		page.dataGroups.LmtIndusAgrGroup.reset();
	};
	
	/*--user code begin--*/
	function doReturnMethod(methodName){
		var data = LmtIndusAgrList._obj.getSelectedData();
		if (data != null && data.length !=0) {
			if (methodName) {
				var parentWin = EMPTools.getWindowOpener();
				eval("parentWin."+methodName+"(data[0])");
				window.close();
			}else{
				alert("未定义返回的函数，请检查弹出按钮的设置!");
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doCancel(){
		window.close();
	};

	function doSelect(){
		var methodName = '${context.popReturnMethod}';	
		doReturnMethod(methodName);
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	<emp:gridLayout id="LmtIndusAgrGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="LmtIndusAgr.serno" label="业务编号" />
		<emp:text id="LmtIndusAgr.agr_no" label="协议编号" />
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	 <emp:returnButton id="s1" label="选择返回"/>
	<emp:table icollName="LmtIndusAgrList" pageMode="true" url="pageLmtIndusQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="agr_no" label="协议编号" />
		<emp:select id="indus_type" label="行业分类" dictname="STD_ZB_INDUS_TYPE" />
		<emp:text id="manager_id" label="责任人" hidden="true"/>
		<emp:text id="main_br_id" label="主管机构" hidden="true"/>
		<emp:text id="is_list_mana" label="是否名单制管理" hidden="true"/>
	</emp:table>
	<div align="left">
			<br>
 <emp:returnButton id="s2" label="选择返回"/>
			<emp:button id="cancel" label="关闭" />
	</div>
</body>
</html>
</emp:page>