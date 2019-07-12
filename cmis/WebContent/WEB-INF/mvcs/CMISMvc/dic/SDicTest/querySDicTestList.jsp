<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>鍒楄〃鏌ヨ椤甸潰</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		SDicTest._toForm(form);
		SDicTestList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateSDicTestPage() {
		var paramStr = SDicTestList._obj.getParamStr(['enname','opttype']);
		if (paramStr != null) {
			var url = '<emp:url action="getSDicTestUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('璇峰厛閫夋嫨涓�鏉¤褰曪紒');
		}
	};
	
	function doViewSDicTest() {
		var paramStr = SDicTestList._obj.getParamStr(['enname','opttype']);
		if (paramStr != null) {
			var url = '<emp:url action="getSDicTestViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('璇峰厛閫夋嫨涓�鏉¤褰曪紒');
		}
	};
	
	function doGetAddSDicTestPage() {
		var url = '<emp:url action="getSDicTestAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteSDicTest() {
		var paramStr = SDicTestList._obj.getParamStr(['enname','opttype']);
		if (paramStr != null) {
			if(confirm("鏄惁纭瑕佸垹闄わ紵")){
				var url = '<emp:url action="deleteSDicTestRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('璇峰厛閫夋嫨涓�鏉¤褰曪紒');
		}
	};
	
	function doReset(){
		page.dataGroups.SDicTestGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="SDicTestGroup" title="杈撳叆鏌ヨ鏉′欢" maxColumn="2">
			<emp:text id="SDicTest.cnname" label="CNNAME" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddSDicTestPage" label="鏂板" op="add"/>
		<emp:button id="getUpdateSDicTestPage" label="淇敼" op="update"/>
		<emp:button id="deleteSDicTest" label="鍒犻櫎" op="remove"/>
		<emp:button id="viewSDicTest" label="鏌ョ湅" op="view"/>
	</div>

	<emp:table icollName="SDicTestList" pageMode="true" url="pageSDicTestQuery.do">
		<emp:text id="enname" label="ENNAME" />
		<emp:text id="cnname" label="CNNAME" />
		<emp:text id="opttype" label="OPTTYPE" />
		<emp:text id="memo" label="MEMO" />
		<emp:text id="flag" label="FLAG" />
		<emp:text id="levels" label="LEVELS" />
		<emp:text id="orderid" label="ORDERID" />
	</emp:table>
	
</body>
</html>
</emp:page>
    