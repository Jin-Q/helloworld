<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>
<%
String task_serno = request.getParameter("task_serno");
String cus_id = request.getParameter("cus_id");
%>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	//关闭
	function doCancel(){
		window.close();
	}

	//保存
	function doAddRecord(){
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if(flag=='success'){
		            alert('保存成功!');
		            window.opener.location.reload();
					window.close();
				}
			}
		};
		var handleFailure = function(o) {
			alert("保存失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};

		var form = document.getElementById("submitForm");
		var result = PspDunningRecord._checkAll();
		if(result){
			page.dataGroups.dataGroup_in_formsubmitForm.toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}
	};

	//校验日期
	function checkDate(obj){
		var openDay = '${context.OPENDAY}';
		var dunDate = obj._getValue();
		if(dunDate>openDay){
			alert('日期不能大于当前日期！');
			obj._setValue('');
		}
	}

	//是否回执
	function checkIsReply(){
		var isReply = PspDunningRecord.is_reply._getValue();
		if(isReply=='1'){
			PspDunningRecord.reply_date._obj._renderHidden(false);
			PspDunningRecord.reply_info._obj._renderHidden(false);
			PspDunningRecord.reply_date._obj._renderRequired(true);
			PspDunningRecord.reply_info._obj._renderRequired(true);
		}else if(isReply=='2'){
			PspDunningRecord.reply_date._obj._renderHidden(true);
			PspDunningRecord.reply_info._obj._renderHidden(true);
			PspDunningRecord.reply_date._obj._renderRequired(false);
			PspDunningRecord.reply_info._obj._renderRequired(false);
		}
	}

	function doLoad(){
	//	PspDunningRecord.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
	}

	//选择客户POP框返回方法
	function returnCus(data){
		PspDunningRecord.cus_id._setValue(data.cus_id._getValue());
		cus_name_displayname._setValue(data.cus_name._getValue());
		/*	LmtApply.main_br_id._setValue(data.main_br_id._getValue());
		LmtApply.manager_id._setValue(data.cust_mgr._getValue());
		LmtApply.manager_br_id._setValue(data.main_br_id._getValue());*/
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:form id="submitForm" action="addPspDunningRecordRecord.do" method="POST">
		
		<emp:gridLayout id="PspDunningRecordGroup" title="催收纪录及回执登记" maxColumn="2">
			<emp:text id="PspDunningRecord.serno" label="催收函编号" maxlength="40" hidden="true" colSpan="2"/>
			<emp:date id="PspDunningRecord.dunning_date" label="催收日期" required="true" onblur="checkDate(PspDunningRecord.dunning_date)"/>
			<emp:select id="PspDunningRecord.dunning_obj_type" label="催收对象类型" required="true" dictname="STD_ZB_DUNNING_OBJ_TYPE"/>
		<%//	<emp:text id="PspDunningRecord.cus_id" label="催收客户码" maxlength="30" required="true" defvalue="" readonly="true"/> %>
			<emp:pop id="PspDunningRecord.cus_id" label="催收客户码" url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" required="true"/>
			<emp:text id="cus_name_displayname" label="催收客户名称"  readonly="true"/>
			<emp:select id="PspDunningRecord.dunning_type" label="催收方式" required="true" dictname="STD_ZB_DUNNING_TYPE" />
			<emp:text id="PspDunningRecord.dunning_id" label="催收人" maxlength="20" required="true" />
			<emp:select id="PspDunningRecord.cur_type" label="催收币种" required="true" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="PspDunningRecord.dunning_amt" label="催收金额" maxlength="16" required="true" dataType="Currency" />
			<emp:select id="PspDunningRecord.is_link" label="是否取得联系" required="true" dictname="STD_ZX_YES_NO" />
			<emp:date id="PspDunningRecord.dunning_time" label="催收时间" required="false" hidden="true"/>
			<emp:textarea id="PspDunningRecord.overdue_resn" label="逾期或欠息原因" maxlength="500" required="false" colSpan="2" />
			<emp:date id="PspDunningRecord.repay_promiss_time" label="还款承诺时间" required="false" />
			<emp:textarea id="PspDunningRecord.dunning_dec" label="催收情况描述" maxlength="500" required="false" colSpan="2" />
			<emp:date id="PspDunningRecord.sign_date" label="签收日期" required="false" onblur="checkDate(PspDunningRecord.sign_date)"/>
			<emp:text id="PspDunningRecord.sign_id" label="签收人" maxlength="20" required="false" />
			<emp:select id="PspDunningRecord.is_reply" label="是否回执" required="true" dictname="STD_ZX_YES_NO" onchange="checkIsReply()"/>
			<emp:date id="PspDunningRecord.reply_date" label="回执日期" required="false" />
			<emp:textarea id="PspDunningRecord.reply_info" label="回执内容" maxlength="500" required="false" colSpan="2" />
			<emp:textarea id="PspDunningRecord.memo" label="备注" maxlength="500" required="false" colSpan="2" />
			<emp:text id="PspDunningRecord.input_id_displayname" label="登记人"   required="true" defvalue="$currentUserName" readonly="true"/>
			<emp:text id="PspDunningRecord.input_br_id_displayname" label="登记机构"   required="true" defvalue="$organName" readonly="true"/>
			<emp:text id="PspDunningRecord.input_id" label="登记人" maxlength="20" required="true" defvalue="$currentUserId" hidden="true"/>
			<emp:text id="PspDunningRecord.input_br_id" label="登记机构" maxlength="20" required="true" defvalue="$organNo" hidden="true"/>
			<emp:date id="PspDunningRecord.input_date" label="登记日期" required="true" defvalue="$OPENDAY" readonly="true"/>
			<emp:text id="PspDunningRecord.task_serno" label="关联借据编号" maxlength="40" required="false" hidden="true" defvalue="<%=task_serno%>"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="addRecord" label="确定" />
			<emp:button id="cancel" label="关闭"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

