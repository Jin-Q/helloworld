<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

function returnCus(data){
	FncProject.cus_id._setValue(data.cus_id._getValue());
	FncProject.cus_name._setValue(data.cus_name._getValue());
}
function checkZero(data){
	var x=data.length;
	if(x !=6){
 		alert("年月的位数必须为6位,格式为YYYYMM！");
 		FncProject.fnc_ym._setValue("");
	}
	else{
		var nn;
		nn=new Date();
		var m = data.substring(4,6);
		var y = Number(data.substring(0,4));
		var ny = nn.getFullYear();
		if(y<=ny){
				if(m>'12'||m=='00'){
				alert("月份应该在1-12月之间！");
				FncProject.fnc_ym._setValue("");
				}
		}else {		
			alert("年份不应该大于当前年份");
			FncProject.fnc_ym._setValue("");
		}
		
	}
}
function doReturn() {
	var url = '<emp:url action="queryFncProjectList.do"/>';
	url = EMPTools.encodeURI(url);
	window.location=url;
};	
function checkM(data){
	var val= FncProject.fnc_invt_amt._getValue();
	var changeValue = data._getValue();
	if(changeValue == null || changeValue == ''){
         return;
	}	
	var a = parseFloat(val);
	var b = parseFloat(changeValue);
	if(b>a){
		alert("已完成投资额不能大于投入金额");
		data._setValue("");
	}
}

</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addFncProjectRecord.do" method="POST">
		
		<emp:gridLayout id="FncProjectGroup" title="在建工程明细表" maxColumn="2">
			<emp:pop id="FncProject.cus_id" label="客户码" url="selectCusInfoPop.do?type=com" returnMethod="returnCus" required="true" />
			<emp:text id="FncProject.cus_name" label="客户名称" maxlength="80" required="false" />			
			<emp:text id="FncProject.fnc_ym" label="年月" maxlength="6" required="true" onchange="checkZero(this.value)"/>
			<emp:text id="FncProject.fnc_prj_name" label="项目名称" maxlength="200" required="true" />
			<emp:text id="FncProject.fnc_const_loc" label="施工地点" maxlength="200" required="true" />
			<emp:text id="FncProject.fnc_const_depnt" label="施工单位" maxlength="200" required="true" />
			<emp:select id="FncProject.guar_st" label="抵质押情况" dictname="STD_ZB_GUAR_ST" required="false"/>			
			<emp:text id="FncProject.fnc_invt_amt" label="投入金额" maxlength="18" required="true" dataType="Currency"/>
			<emp:text id="FncProject.fnc_invt_amted" label="已完成投资额" onchange="checkM(FncProject.fnc_invt_amted)" maxlength="18" required="true" dataType="Currency"/>
			<emp:text id="FncProject.fnc_prg" label="施工进度" maxlength="200" required="false" />
			<emp:textarea id="FncProject.remark" label="备注" maxlength="250" required="false" colSpan="2"/>
			<emp:text id="FncProject.input_id_displayname" label="登记人" required="true" readonly="true" defvalue="${context.actorname}" />
			<emp:text id="FncProject.input_br_id_displayname" label="登记机构" required="true" readonly="true" defvalue="${context.organName}" />
			<emp:text id="FncProject.input_date" label="登记日期" required="true" readonly="true" defvalue="${context.OPENDAY}"/>
			<emp:text id="FncProject.input_id" label="登记人" hidden="true" defvalue="${context.currentUserId}"/>
			<emp:text id="FncProject.input_br_id" label="登记机构" hidden="true" defvalue="${context.organNo}"/>
			<emp:text id="FncProject.last_upd_id" label="更新人" hidden="true" defvalue="${context.currentUserId}"/>
			<emp:text id="FncProject.last_upd_date" label="更新日期" hidden="true" defvalue="${context.OPENDAY}"  />
			<emp:text id="FncProject.pk_id" label="主键" hidden="true" />
			
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="保存" op="add"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

