<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryPspDunningRecordList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	//关闭
	function doCancel(){
		window.close();
	}

	function doLoad(){
		PspDunningRecord.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
		checkIsReply();
	}

	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+PspDunningRecord.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'cusViewWindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
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
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	
	<emp:gridLayout id="PspDunningRecordGroup" title="催收纪录及回执登记" maxColumn="2">
			<emp:text id="PspDunningRecord.serno" label="催收函编号" maxlength="40" required="true" colSpan="2"/>
			<emp:date id="PspDunningRecord.dunning_date" label="催收日期" required="true" />
			<emp:select id="PspDunningRecord.dunning_obj_type" label="催收对象类型" required="true" dictname="STD_ZB_DUNNING_OBJ_TYPE"/>
			<emp:text id="PspDunningRecord.cus_id" label="催收客户码" maxlength="30" required="true" />
			<emp:text id="PspDunningRecord.cus_id_displayname" label="催收客户名称"  readonly="true"/>
			<emp:select id="PspDunningRecord.dunning_type" label="催收方式" required="true" dictname="STD_ZB_DUNNING_TYPE" />
			<emp:text id="PspDunningRecord.dunning_id" label="催收人" maxlength="20" required="true" />
			<emp:select id="PspDunningRecord.cur_type" label="催收币种" required="true" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="PspDunningRecord.dunning_amt" label="催收金额" maxlength="16" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="PspDunningRecord.is_link" label="是否取得联系" required="true" dictname="STD_ZX_YES_NO" />
			<emp:date id="PspDunningRecord.dunning_time" label="催收时间" required="false" hidden="true"/>
			<emp:textarea id="PspDunningRecord.overdue_resn" label="逾期或欠息原因" maxlength="500" required="false" colSpan="2" />
			<emp:date id="PspDunningRecord.repay_promiss_time" label="还款承诺时间" required="false" />
			<emp:textarea id="PspDunningRecord.dunning_dec" label="催收情况描述" maxlength="500" required="false" colSpan="2" />
			<emp:date id="PspDunningRecord.sign_date" label="签收日期" required="false" />
			<emp:text id="PspDunningRecord.sign_id" label="签收人" maxlength="20" required="false" />
			<emp:select id="PspDunningRecord.is_reply" label="是否回执" required="false" dictname="STD_ZX_YES_NO" />
			<emp:date id="PspDunningRecord.reply_date" label="回执日期" required="false" />
			<emp:textarea id="PspDunningRecord.reply_info" label="回执内容" maxlength="500" required="false" colSpan="2" />
			<emp:textarea id="PspDunningRecord.memo" label="备注" maxlength="500" required="false" colSpan="2" />
			<emp:text id="PspDunningRecord.input_id_displayname" label="登记人"   required="true" />
			<emp:text id="PspDunningRecord.input_br_id_displayname" label="登记机构"   required="true" />
			<emp:text id="PspDunningRecord.input_id" label="登记人" maxlength="20" required="true" hidden="true"/>
			<emp:text id="PspDunningRecord.input_br_id" label="登记机构" maxlength="20" required="true" hidden="true"/>
			<emp:date id="PspDunningRecord.input_date" label="登记日期" required="true"  />
			<emp:text id="PspDunningRecord.task_serno" label="关联借据编号" maxlength="40" required="false" hidden="true"/>
	</emp:gridLayout>
	
	<div align="center">
		<br>
		<emp:button id="cancel" label="关闭"/>
	</div>
</body>
</html>
</emp:page>
