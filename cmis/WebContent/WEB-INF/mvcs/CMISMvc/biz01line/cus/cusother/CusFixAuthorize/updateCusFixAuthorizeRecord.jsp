<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	//选择授权人
	function setconId(data){
		CusFixAuthorize.auth_id._setValue(data.actorno._getValue());
		CusFixAuthorize.auth_id_displayname._setValue(data.actorname._getValue());
	}

	//主管机构
	function getOrgID(data){
		CusFixAuthorize.manager_br_id._setValue(data.organno._getValue());
		CusFixAuthorize.manager_br_id_displayname._setValue(data.organname._getValue());
	}

	//校验起始日
	function checkStartDate(){
		var startDate = CusFixAuthorize.startdate._getValue();
		var endDate = CusFixAuthorize.enddate._getValue();
		if(startDate!=null&&startDate!=''&&endDate!=null&&endDate!=''){
			if(startDate>=endDate){
				alert('结束日期要大于开始日期！');
				CusFixAuthorize.startdate._setValue('');
				return;
			}
		}
	}
	
	//校验到期日
	function checkEndDate(){
		var openDay = '${context.OPENDAY}';
		var startDate = CusFixAuthorize.startdate._getValue();
		var endDate = CusFixAuthorize.enddate._getValue();
		if(endDate!=null&&endDate!=''){
			if(openDay>endDate){
				alert('结束日期不能小于当前日期！');
				CusFixAuthorize.enddate._setValue('');
				return;
			}
			if(startDate!=null&&startDate!=''){
				if(startDate>=endDate){
					alert('结束日期要大于开始日期！');
					CusFixAuthorize.enddate._setValue('');
					return;
				}
			}
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="updateCusFixAuthorizeRecord.do" method="POST">
		<emp:gridLayout id="CusFixAuthorizeGroup" maxColumn="2" title="客户修改授权">
			<emp:pop id="CusFixAuthorize.auth_id_displayname" label="授权人" required="true" url="getAllSUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="CusFixAuthorize.manager_br_id_displayname" label="管理机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID"/>
			<emp:password id="CusFixAuthorize.checkcode" label="授权码" maxlength="32" required="true" readonly="true"/>
			<emp:password id="CusFixAuthorize.checkcodechk" label="确认授权码" maxlength="32" required="true" defvalue="$CusFixAuthorize.checkcode" readonly="true"/>
			<emp:date id="CusFixAuthorize.startdate" label="开始日期" required="true" onblur="checkStartDate()"/>
			<emp:date id="CusFixAuthorize.enddate" label="结束日期" required="true" onblur="checkEndDate()"/>
			<emp:text id="CusFixAuthorize.input_id_displayname" label="登记人"  required="true" readonly="true"/>
			<emp:text id="CusFixAuthorize.input_br_id_displayname" label="登记机构"  required="true" readonly="true"/>
			<emp:text id="CusFixAuthorize.serno" label="流水号" maxlength="32" required="true" readonly="true" hidden="true"/>
			<emp:text id="CusFixAuthorize.input_id" label="登记人" maxlength="20" required="true" readonly="true" hidden="true"/>
			<emp:text id="CusFixAuthorize.input_br_id" label="登记机构" maxlength="20" required="true" readonly="true" hidden="true"/>
			<emp:text id="CusFixAuthorize.auth_id" label="授权人" maxlength="20" required="true" hidden="true"/>
			<emp:text id="CusFixAuthorize.manager_br_id" label="管理机构" maxlength="20" required="true" hidden="true"/>
			<emp:select id="CusFixAuthorize.status" label="状态" required="true" readonly="true" dictname="STD_ZB_STATUS"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="修改" op="update"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
