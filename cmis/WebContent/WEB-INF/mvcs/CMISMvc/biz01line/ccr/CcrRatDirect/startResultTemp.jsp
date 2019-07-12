<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<emp:page>
<html>
<head>

<title>操作返回页面</title>
<script type="text/javascript">
function doOnLoad() {
	var cusId = '${context.cusId}';
	var serNo = '${context.serNo}';
	var cusNameTemp = '${context.cusName}';
	var comsc = '${context.comScaleCcr}';
	var gra = '${context.grade}';

	

	//var businessTypeV = "0";
    if(confirm("确定提交审批流程?")){
        caseNo._obj.element.value = serNo;
		caseName._obj.element.value = "评级直接认定流程";
	    bizCateCd._obj.element.value = "BIZ222";
	    cusName._obj.element.value = cusNameTemp;
        inCusId._obj.element.value = cusId;
        finalGrade._obj.element.value = gra;
        comScaleCcr._obj.element.value = comsc;
		var formWF = document.getElementById("submitWFForm");

		   
		formWF.action = "wfi/process/BIZ222/new";
		 
		caseNo._toForm(formWF);
		caseName._toForm(formWF);
		bizCateCd._toForm(formWF);
		inCusId._toForm(formWF);
		cusName._toForm(formWF);
		finalGrade._toForm(formWF);
		comScaleCcr._toForm(formWF);
		formWF.submit();
	}else{
		var url = '<emp:url action="queryCcrRatDirectList.do"/>';

		url = EMPTools.encodeURI(url);
		window.location = url;
	}
	
	
	
}


</script>

<jsp:include page="/include.jsp" />

</head>
<body class="page_content" onload="doOnLoad()">

<br>
<br>
<br>
<br>
<br>
<br>
<br>
<div class="page_welcome">
<div class="page_welcome_link">
<div class="clear"></div>
</div></div>
<div class="page_welcome_content">
<div class="page_welcome_content_top"></div>
<div class="page_welcome_succ"><span class="page_welcome_red">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><br>
<TABLE align="center" width="300px">
	<TR>
		<TD><span class="page_welcome_red"><font color="green"><a
			id="returnlink" href=""></a></font></span></TD>
	</TR>
	<TR>
		<TD>&nbsp;</TD>
	</TR>
</TABLE>
	<emp:form id="submitWFForm" action="#" method="POST" >
	<emp:text id="cusName" label="cusName" required="false" maxlength="500" hidden="true"/>
	<emp:text id="caseNo" label="caseNo" required="false" maxlength="500" hidden="true"/>  
	<emp:text id="caseName" label="caseName" required="false" maxlength="500" hidden="true"/>
	<emp:text id="bizCateCd" label="bizCateCd" required="false" maxlength="500" hidden="true"/>
	<emp:text id="inCusId" label="inCusId" required="false" maxlength="500" hidden="true"/>
	<emp:text id="finalGrade" label="finalGrade" required="false" maxlength="500" hidden="true"/>
	<emp:text id="comScaleCcr" label="comScaleCcr" required="false" maxlength="500" hidden="true"/>
	
	
	

    </emp:form>
</div>
</div>
</body>
</html>
</emp:page>
