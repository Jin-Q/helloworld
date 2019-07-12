<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

/*--user code begin--*/
	function getOrganName(data){
		WfiLvOverdrawnRight.org_id._setValue(data.organno._getValue());
		WfiLvOverdrawnRight.org_id_displayname._setValue(data.organname._getValue());
	};

	function doClose(){
		 window.close();
	};
	//加载事件
	function onLoad(){
		var options = WfiLvOverdrawnRight.belg_line._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
		//去除个人业务条线/所有业务条线
			if(options[i].value == "BL300" || options[i].value == "BL_ALL"){
				options.remove(i);
			}
		}
	}
	
	function doAddWfiLvOverdrawnRightInfo() {
		var result = WfiLvOverdrawnRight._checkAll();
		if(result){
			var form = document.getElementById('submitForm');
			WfiLvOverdrawnRight._toForm(form);
	    	form.submit();
		}
	};	
/*--user code end--*/
</script>
</head>
<body class="page_content" style="width:200px" onload="onLoad()">
	<emp:form id="submitForm" action="getWfiLvOverdrawnRightAddPage.do" method="POST">
		<emp:gridLayout id="LmtApplyGroup" title="分/支机构透支额度向导" maxColumn="2">
			<emp:pop id="WfiLvOverdrawnRight.org_id" label="机构码"  url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName"  required="true" colSpan="2" />
			<emp:text id="WfiLvOverdrawnRight.org_id_displayname" label="机构名称" required="true"  colSpan="2" readonly="true" />
			<emp:select id="WfiLvOverdrawnRight.belg_line" label="所属条线"  required="true" dictname="STD_ZB_BUSILINE" colSpan="2"  />
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="addWfiLvOverdrawnRightInfo" label="下一步" op="init" />
			<emp:button id="reset" label="重置"/>
			<emp:button id="close" label="关闭"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

