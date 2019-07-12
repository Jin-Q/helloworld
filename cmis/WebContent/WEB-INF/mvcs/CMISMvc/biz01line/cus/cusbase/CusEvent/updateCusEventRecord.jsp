<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<emp:page>
<html>
<head>
<title>修改页面</title>
<jsp:include page="/include.jsp" flush="true" />
	<script type="text/javascript">
	function doUpdateCusEvent() {
		var form = document.getElementById("submitForm");
		var result = CusEvent._checkAll();
		if (result) {
			CusEvent._toForm(form)
			toSubmitForm(form);
		}
	};
	
	function toSubmitForm(form) {
		var paramStr = "updateInfo=" + '0';
		var url = '<emp:url action="updateCusEventRecord.do"/>&' + paramStr;
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e);
					return;
				}
				var flag = jsonstr.flag;
				if (flag == "修改成功") {
					alert("修改成功!");
					doReturn();
				} else {
					alert(flag);
					return;
				}
			}
		};
		var handleFailure = function(o) {};
		var callback = {
			success : handleSuccess,
			failure : handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback,postData);
	};
	
	function doReturn() {
		var editFlag = '${context.EditFlag}';
		var cus_id = CusEvent.cus_id._obj.element.value;
		var paramStr = "CusEvent.cus_id=" + cus_id+"&EditFlag="+editFlag;
		var url = '<emp:url action="queryCusEventList.do"/>&' + paramStr;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

	function CheckDate() {
		var event_dt = CusEvent.event_dt._obj.element.value;
		var openDay='${context.OPENDAY}';
		if (event_dt != null && event_dt != "") {
			var flag = CheckDate1BeforeDate2(event_dt,openDay);
			if (!flag) {
				alert("发生时间小于等于当前日期！");
				CusEvent.event_dt._obj.element.value = "";
			}
		}
	};
	
	function cheakAmt(amt) {
		var getAmt = parseFloat(amt._getValue());
		if (getAmt < 0) {
			alert("金额值不能为负数！");
			amt._obj.element.value = "";
		}
	}
	/*--user code begin--*/
	// add by zhoujf 20090608 移出“重大经济纠纷”字典项
	function doOnLoad() {
		var options = CusEvent.event_typ._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
			if (options[i].value == '07') {
				options.remove(i);
			}
		}
		if(CusEvent.event_bank_flg._obj.element.value == 1){
			CusEvent.event_bch_name._obj._renderHidden(true);
		}else if(CusEvent.event_bank_flg._obj.element.value == 2){
			CusEvent.event_bch_name._obj._renderHidden(false);
		}
	}

/*	function getOrgName(data){
		CusEvent.event_bch_name_displayname._setValue(data.organname._getValue());
		CusEvent.event_bch_name._setValue(data.organno._getValue());        
	}*/
	// add end 20090608
	/*--user code end--*/
</script>
	</head>
	<body class="page_content" onload="doOnLoad()">
	<emp:form id="submitForm" action="updateCusEventRecord.do"
		method="POST">
		<emp:gridLayout id="CusEventGroup" maxColumn="2" title="客户重大事件">
			<emp:text id="CusEvent.serno" label="登记流水号" maxlength="40" required="true" readonly="true" />
			<emp:text id="CusEvent.cus_id" label="客户码" maxlength="20" required="true" readonly="true" hidden="true" />
			<emp:date id="CusEvent.event_dt" label="发生日期" required="true" onblur="CheckDate()" />
			<emp:text id="CusEvent.event_name" label="事件名称" maxlength="60" required="true" colSpan="2" cssElementClass="emp_field_text_input2" />
			<emp:select id="CusEvent.event_typ" label="事件类型" required="true" dictname="STD_ZB_EVENT_TYP" colSpan="2"
				cssElementClass="emp_field_text_input2" />
			<emp:select id="CusEvent.event_classify" label="事件分类" required="true" dictname="STD_ZB_EVENT_KIND" colSpan="2" />	
			<emp:select id="CusEvent.event_imp_deg" label="事件影响程度" required="true" dictname="STD_ZB_EVENT_IMP_DEG" />
			<emp:textarea id="CusEvent.event_desc" label="事件描述" maxlength="200" required="true" colSpan="2" />
			<emp:text id="CusEvent.event_amt" label="涉及金额(元)" maxlength="18" required="true" dataType="Currency"
				onblur="cheakAmt(CusEvent.event_amt)" />
			<emp:text id="CusEvent.event_exps_com" label="曝光单位" maxlength="60" required="false" />
			<emp:select id="CusEvent.event_bank_flg" label="是否本行发生" required="true" readonly="true" dictname="STD_ZX_YES_NO" />
			<emp:text id="CusEvent.event_bch_name" label="发生机构" maxlength="60" />
			
			<emp:select id="CusEvent.status" label="状态" required="false" hidden="true" dictname="STD_ZB_COMM_STATUS" />
			<emp:text id="CusEvent.input_id" label="登记人" maxlength="20" required="false" readonly="true" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="CusEvent.input_br_id" label="登记机构" maxlength="20" required="false" readonly="true" hidden="true" defvalue="$organNo"/>
			<emp:date id="CusEvent.input_date" label="登记日期" required="false" readonly="true" hidden="true" defvalue="$OPENDAY"/>
			<emp:text id="CusEvent.last_upd_id" label="更新人" required="false" hidden="true" />
			<emp:date id="CusEvent.last_upd_date" label="更新日期" required="false" hidden="true" />
			<emp:text id="CusEvent.logout_id" label="注销人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="CusEvent.logout_br_id" label="注销机构" maxlength="20" required="false" hidden="true"/>
		</emp:gridLayout>
		<div align="center"><br>
		<emp:button id="updateCusEvent" label="保存" /> 
		<emp:button id="reset" label="重置" />
		<emp:button id="return" label="返回" /></div>
	</emp:form>
	</body>
	</html>
</emp:page>