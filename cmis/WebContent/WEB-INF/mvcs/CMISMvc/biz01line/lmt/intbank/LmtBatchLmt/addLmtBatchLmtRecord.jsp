<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	/*--user code begin--*/
	function returnData(data){
		LmtBatchLmt.batch_cus_no._setValue(data.batch_cus_no._getValue());
		LmtBatchLmt.batch_cus_type._setValue(data.batch_cus_type._getValue());
		LmtBatchLmt.cdt_lvl._setValue(data.cdt_lvl._getValue());
	}
	
	function doReturn(){		
		var url = '<emp:url action="queryLmtBatchLmtList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;	
	};
	//进行异步保存
	function doAdd(){
		var form = document.getElementById("submitForm");
		LmtBatchLmt._toForm(form);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var serno = jsonstr.serno;
				if(flag == "success"){		
					var url = '<emp:url action="getLmtBatchLmtUpdatePage.do"/>?serno='+serno;			
					url = EMPTools.encodeURI(url);
					 window.location = url;
				}else {
					alert("发生异常！");
				}
			}
		};
		var handleFailure = function(o){
			alert("异步请求出错！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
	};
	
	function doNext(){
		if(LmtBatchLmt._checkAll()){
    		var handleSuccess = function(o){ 		
				if(o.responseText != undefined){
					var jsonstr = eval("("+o.responseText+")");
					var flag = jsonstr.flag;
					var message = jsonstr.message;
					if(flag == "success" ){
						alert(message);
					}else if(flag=="fail"){
						alert(message);
					}else{
						doAdd();
					}
				}
			};
			var handleFailure = function(o){
				alert("异步回调失败！");	
			};
			var batch_no_value= LmtBatchLmt.batch_cus_no._getValue();
		    var url = '<emp:url action="CheckBatchInLmt.do"/>?batch_cus_no='+batch_no_value;
			var callback = {
					success:handleSuccess,
					failure:handleFailure
			};
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST', url,callback);
			}
	}
	/*--user code end--*/
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="addLmtBatchLmtRecord.do" method="POST">
		<div align="center" id="openDiv">
		<emp:gridLayout id="LmtBatchLmtGroup" title="批量授信" maxColumn="2">
		    <emp:select id="LmtBatchLmt.app_cls" label="申请类别" required="true" readonly="true" dictname="STD_ZB_APP_CLS" defvalue="02" />		
			<emp:pop id="LmtBatchLmt.batch_cus_no" label="批量客户编号" url="LmtIntbankBatchList4PopList.do?returnMethod=returnData" required="true" colSpan="2"/>			
			<emp:select id="LmtBatchLmt.batch_cus_type" label="批量客户类型 " required="true" colSpan="2" readonly="true" dictname="STD_ZB_BATCH_CUS_TYPE"/>
			<emp:text id="LmtBatchLmt.cdt_lvl" label="信用等级 " maxlength="32" required="true" colSpan="2" readonly="true"/>
			<emp:text id="LmtBatchLmt.serno" label="业务编号  " maxlength="32" required="false" colSpan="2" hidden="true"/>
			<emp:select id="LmtBatchLmt.app_type" label="申请类型" required="false" dictname="STD_ZB_APP_TYPE" hidden="true" defvalue="01"/>
			<emp:select id="LmtBatchLmt.limit_type" label="额度类型"  defvalue="01" readonly="true" hidden="true" dictname="STD_ZB_LIMIT_TYPE" colSpan="2"/>
			<emp:select id="LmtBatchLmt.cur_type" label="授信币种" required="false" dictname="STD_ZX_CUR_TYPE" hidden="true" defvalue="CNY"/>
			<emp:text id="LmtBatchLmt.lmt_totl_amt" label="授信总额(元)" maxlength="16" required="false" hidden="true"/>
			<emp:text id="LmtBatchLmt.single_quota" label="单户限额(元)" maxlength="16" required="false" hidden="true"/>
			<emp:select id="LmtBatchLmt.term_type" label="期限类型" required="false" dictname="STD_ZB_TERM_TYPE" hidden="true" />
			<emp:text id="LmtBatchLmt.term" label="期限" maxlength="6" required="false" hidden="true"/>
			<emp:textarea id="LmtBatchLmt.memo" label="备注" maxlength="200" required="false" colSpan="2" hidden="true"/>
			<emp:pop id="LmtBatchLmt.manager_id" label="责任人" url="null" required="false" hidden="true"/>
			<emp:pop id="LmtBatchLmt.manager_br_id" label="管理机构" url="null" required="false" hidden="true"/>
			<emp:text id="LmtBatchLmt.input_id" label="登记人" maxlength="20" required="false" hidden="true"  defvalue="$currentUserId"/>
			<emp:text id="LmtBatchLmt.input_br_id" label="登记机构" maxlength="32" required="false" hidden="true" defvalue="$organNo"/>
			<emp:date id="LmtBatchLmt.input_date" label="登记日期" required="false" hidden="true" defvalue="$OPENDAY"/>			
			<emp:date id="LmtBatchLmt.app_date" label="申请日期" required="false" hidden="true"/>
			<emp:date id="LmtBatchLmt.end_date" label="办结日期" required="false" hidden="true"/>
			<emp:select id="LmtBatchLmt.approve_status" label="审批状态" required="false" dictname="WF_APP_STATUS" defvalue="000" hidden="true"/>
			<emp:text id="LmtBatchLmt.flow_type" label="流程类型" required="false" hidden="true" defvalue="01"/>
		</emp:gridLayout></div>
		<div align="center">
			<br>
			<emp:button id="next" label="下一步"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回" />
		</div>
	</emp:form>
</body>
</html>
</emp:page>