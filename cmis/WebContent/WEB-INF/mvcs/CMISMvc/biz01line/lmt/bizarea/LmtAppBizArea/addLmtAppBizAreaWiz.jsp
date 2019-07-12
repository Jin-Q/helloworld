<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function doGetNext(){
		var biz_area_type = LmtAppBizArea.biz_area_type._getValue();
		//var serno = LmtAppBizArea.serno._getValue();
		//var supmk_serno = LmtAppBizArea.supmk_serno._getValue();
		if( biz_area_type == '' )
			alert('请选择圈商类型!');
		else{
			var url = '<emp:url action="getLmtAppBizAreaAddPage.do"/>&biz_area_type=' + biz_area_type ;
				//+ "&serno=" + serno + "&supmk_serno=" + supmk_serno;
			url = EMPTools.encodeURI(url);
			window.location = url;
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="addLmtAppBizAreaRecord.do" method="POST">
		<div>
		<br></br>
	</div>
		<emp:gridLayout id="LmtAppBizAreaGroup" title="圈商准入" maxColumn="2">
			<emp:select id="LmtAppBizArea.biz_area_type" label="圈商类型" required="false" dictname="STD_LMT_BIZ_AREA_TYPE" />
			<emp:text id="LmtAppBizArea.serno" label="申请编号" maxlength="40" required="false" hidden="true" />
			<emp:text id="LmtAppBizArea.supmk_serno" label="参数" maxlength="40" required="false" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="getNext" label="下一步"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

