<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="../SHolidayRegister/SPubAction.jsp" flush="true"/>
<script type="text/javascript">

	/*--user code begin--*/
	function doSubmits(){
		url = 'doReturn';
		doPubAdd(url,SHolidayRegister);
	};

	function doReturn() {
		var url = '<emp:url action="querySHolidayRegisterList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	function checkDate(obj){
		if(obj.value){
			var begin_date = SHolidayRegister.begin_date._getValue();
			var plan_end_date = SHolidayRegister.plan_end_date._getValue();
			var input_date = SHolidayRegister.input_date._getValue();

			if(obj.name == 'SHolidayRegister.begin_date'){
				if( ( plan_end_date != '' && plan_end_date <= begin_date )|| ( begin_date < input_date )){
					obj.value = '';
					alert("[开始日期]应该小于[预计到期日期]，并大于等于[当前日期]");
		   		}
			}
			if(obj.name == 'SHolidayRegister.plan_end_date'){
				if( ( plan_end_date != '' && plan_end_date <= begin_date )|| ( plan_end_date <= input_date )){
					obj.value = '';
					alert("[预计到期日期]应该大于[开始日期]与[当前日期]");
		   		}
			}
		}		
	};

	function setconId(data){
		actorno = data.actorno._getValue()
		actorname = data.actorname._getValue()
		SHolidayRegister.actorno._setValue(actorno);
		SHolidayRegister.actorno_displayname._setValue(actorname);
	};

	/*--user code end--*/

</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addSHolidayRegisterRecord.do" method="POST">
		
		<emp:gridLayout id="SHolidayRegisterGroup" title="用户休假登记" maxColumn="2">
			<emp:pop id="SHolidayRegister.actorno" label="用户码" required="true" 
			 url="getAllSUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:text id="SHolidayRegister.actorno_displayname" label="用户名称"  required="true" readonly="true" />
			<emp:date id="SHolidayRegister.begin_date" label="开始日期" required="true" onblur="checkDate(this)" />
			<emp:date id="SHolidayRegister.plan_end_date" label="预计到期日期" required="true"  onblur="checkDate(this)" />
			<emp:date id="SHolidayRegister.real_end_date" label="实际到期日期" required="false" hidden="true" />
			<emp:textarea id="SHolidayRegister.memo" label="备注" maxlength="250" required="false" colSpan="2" />
			<emp:select id="SHolidayRegister.status" label="状态" readonly="true" dictname="STD_DRFPO_STATUS" defvalue="00" />
		</emp:gridLayout>
		
		<emp:gridLayout id="SHolidayRegisterGroup" maxColumn="2" title="登记信息">
			<emp:text id="SHolidayRegister.input_id_displayname" label="登记人" readonly="true" required="true" defvalue="$currentUserName" />
			<emp:text id="SHolidayRegister.input_br_id_displayname" label="登记机构" readonly="true" required="true" defvalue="$organName" />
			<emp:text id="SHolidayRegister.input_id" label="登记人" required="true"  defvalue="$currentUserId" hidden="true"/>
			<emp:text id="SHolidayRegister.input_br_id" label="登记机构" required="true" defvalue="$organNo" hidden="true" />
			<emp:date id="SHolidayRegister.input_date" label="登记日期" required="true"  defvalue="$OPENDAY" readonly="true" />
			<emp:text id="SHolidayRegister.serno" label="流水号" maxlength="40" required="false" hidden="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submits" label="保存"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>