<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function doLoad(){
		var cus_id = PspDunningTaskDivis.cus_id._getValue();
		PspDunningTaskDivis.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
		//拼借据编号的查询条件
		var urls = "&cusTypCondition=cus_id="+cus_id;
		PspDunningTaskDivis.acc_no._obj.config.url=PspDunningTaskDivis.acc_no._obj.config.url+urls;
	}

	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+PspDunningTaskDivis.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}

	//选择借据POP
	function returnAcc(data){
		PspDunningTaskDivis.acc_no._setValue(data.bill_no._getValue());
		PspDunningTaskDivis.cont_no._setValue(data.cont_no._getValue());
	}

	//任务执行机构
	function setExeBrId(data){
		PspDunningTaskDivis.exe_br_id._setValue(data.organno._getValue());
		PspDunningTaskDivis.exe_br_id_displayname._setValue(data.organname._getValue());
	}

	//任务执行人
	function setExeId(data){
		PspDunningTaskDivis.exe_id._setValue(data.actorno._getValue());
		PspDunningTaskDivis.exe_id_displayname._setValue(data.actorname._getValue());
	}

	//任务分配机构
	function setDivisBrId(data){
		PspDunningTaskDivis.divis_br_id._setValue(data.organno._getValue());
		PspDunningTaskDivis.divis_br_id_displayname._setValue(data.organname._getValue());
	}

	//任务分配人
	function setDivisId(data){
		PspDunningTaskDivis.divis_id._setValue(data.actorno._getValue());
		PspDunningTaskDivis.divis_id_displayname._setValue(data.actorname._getValue());
	}

	//新增催收任务
	function doAddPspDun(){
		var form = document.getElementById("submitForm");
		if(PspDunningTaskDivis._checkAll()){
			PspDunningTaskDivis._toForm(form);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					if(flag == "success"){
						alert("保存成功！");
						var url = '<emp:url action="queryPspDunningTaskDivisList.do"/>';
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else{
						alert("保存失败！");
					}
				}
			};
			var handleFailure = function(o){
				alert("保存失败！");	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	}
	
	//校验任务生成日期
	function checkCreateDate(objDate){
		var openDay = '${context.OPENDAY}';
		var createDate = PspDunningTaskDivis.task_create_date._getValue();
		var endDate = PspDunningTaskDivis.need_end_date._getValue();
		if(createDate!=null&&createDate!=''&&endDate!=null&&endDate!=''){
			if(createDate>endDate){
				alert('任务生成日期不能大于要求完成日期！');
				objDate._setValue('');
			}
			if(openDay>endDate){
				alert('要求完成日期不能小于当前日期！');
				PspDunningTaskDivis.need_end_date._setValue('');
			}
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<emp:form id="submitForm" action="addPspDunningTaskDivisRecord.do" method="POST">
		<emp:gridLayout id="PspDunningTaskDivisGroup" title="催收任务分配" maxColumn="2">
			<emp:text id="PspDunningTaskDivis.serno" label="业务编号" maxlength="40" required="false" hidden="true" />
			<emp:text id="PspDunningTaskDivis.cus_id" label="客户码" readonly="true" maxlength="40" required="true"/>
			<emp:text id="PspDunningTaskDivis.cus_id_displayname" label="客户名称"   required="true" readonly="true" cssElementClass="emp_field_text_readonly" colSpan="2"/>
			<emp:pop id="PspDunningTaskDivis.acc_no" label="借据编号" url="AccViewPop.do?returnMethod=returnAcc" required="true" />
			<emp:text id="PspDunningTaskDivis.cont_no" label="合同编号" maxlength="40" required="true" readonly="true"/>
			<emp:date id="PspDunningTaskDivis.task_create_date" label="任务生成日期" required="true" onblur="checkCreateDate(PspDunningTaskDivis.task_create_date)" defvalue="$OPENDAY"/>
			<emp:date id="PspDunningTaskDivis.need_end_date" label="要求完成日期" required="true" onblur="checkCreateDate(PspDunningTaskDivis.need_end_date)"/>
			
			<emp:pop id="PspDunningTaskDivis.exe_id_displayname" label="任务执行人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setExeId" required="true" />
			<emp:pop id="PspDunningTaskDivis.exe_br_id_displayname" label="任务执行机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="setExeBrId" required="true" />
			<emp:text id="PspDunningTaskDivis.exe_id" label="任务执行人" maxlength="20" required="true" hidden="true"/>
			<emp:text id="PspDunningTaskDivis.exe_br_id" label="任务执行机构" maxlength="20" required="true" hidden="true"/>
			
			<emp:pop id="PspDunningTaskDivis.divis_id_displayname" label="任务分配人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setDivisId" required="true" />
			<emp:pop id="PspDunningTaskDivis.divis_br_id_displayname" label="任务分配机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="setDivisBrId" required="true" />
			<emp:text id="PspDunningTaskDivis.divis_id" label="任务分配人" maxlength="20" required="true" hidden="true"/>
			<emp:text id="PspDunningTaskDivis.divis_br_id" label="任务分配机构" maxlength="20" required="true" hidden="true"/>
			
			<emp:textarea id="PspDunningTaskDivis.memo" label="备注" maxlength="500" required="false" colSpan="2" />
			<emp:text id="PspDunningTaskDivis.input_id_displayname" label="登记人"   required="true" defvalue="$currentUserName" readonly="true"/>
			<emp:text id="PspDunningTaskDivis.input_br_id_displayname" label="登记机构"   required="true" defvalue="$organName" readonly="true"/>
			<emp:text id="PspDunningTaskDivis.input_id" label="登记人" maxlength="20" required="true" defvalue="$currentUserId" hidden="true"/>
			<emp:text id="PspDunningTaskDivis.input_br_id" label="登记机构" maxlength="20" required="true" defvalue="$organNo" hidden="true"/>
			<emp:date id="PspDunningTaskDivis.input_date" label="登记日期" required="true" readonly="true" defvalue="$OPENDAY"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="addPspDun" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

