<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">

function doOnLoad(){
	var appType = CtrLimitApp.app_type._obj.element.options;
	for(var i=appType.length-1;i>=0;i--){
		if((appType[i].value != '01') && (appType[i].value != '02')){
			appType.remove(i);
		}
	}
}
function doSave(){
	if(CtrLimitApp._checkAll()){
		var appType = CtrLimitApp.app_type._getValue();
		if(appType == 01){
			var form = document.getElementById("submitForm");
			CtrLimitApp._toForm(form);
			
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
					var message = jsonstr.message;
					if(flag == "success" && serno != null){
						var	url = '<emp:url action="getCtrLimitAppUpdatePage.do"/>?op=update&serno='+serno;
						url = EMPTools.encodeURI(url);  
						window.location = url;
					}else {
						alert(message);
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
		}else if(appType == 02){//变更
			/** 通过合同号查询对应的业务流水号，将信息显示出来 */
			var cus_id = CtrLimitApp.cus_id._getValue();
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
					var message = jsonstr.message;
					if(flag == "success"){
						var cont_no = CtrLimitApp.cont_no._getValue();
						var	url = '<emp:url action="getCtrLimitAppChangePage.do"/>?op=update&cont_no='+cont_no;
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else {
						alert(message);
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
			var url1 = '<emp:url action="checkHaveCtrLimitApp.do"/>?cus_id='+cus_id;
			url1 = EMPTools.encodeURI(url1);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url1, callback);
		}
	}
};
/** 选取客户返回的pop框 */
function getCusMsg(data){
	CtrLimitApp.cus_id._setValue(data.cus_id._getValue());
	CtrLimitApp.cus_name._setValue(data.cus_name._getValue());
	chooseAppType(data.cus_id._getValue());
};

function chooseAppType(cus_id){
	var handleSuccess = function(o){
		if(o.responseText !== undefined) {
			try {
				var jsonstr = eval("("+o.responseText+")");
			} catch(e) {
				alert("Parse jsonstr1 define error!" + e.message);
				return;
			}
			var flag = jsonstr.flag;
			var cont_no = jsonstr.cont_no;
			if(flag == "yes"){
				CtrLimitApp.app_type._setValue("02");
				CtrLimitApp.cont_no._obj._renderHidden(false);
				CtrLimitApp.cont_no._obj._renderRequired(true);
				CtrLimitApp.cont_no._obj._renderReadonly(true);
				CtrLimitApp.cont_no._setValue(cont_no);
			}else {
				CtrLimitApp.app_type._setValue("01");
				CtrLimitApp.cont_no._obj._renderHidden(true);
				CtrLimitApp.cont_no._obj._renderRequired(false);
				CtrLimitApp.cont_no._setValue("");
			}
			changeAppType();
		}
	};
	var handleFailure = function(o){
		alert("异步请求出错！");	
	};
	var callback = {
		success:handleSuccess,
		failure:handleFailure
	};
	var url = '<emp:url action="getChooseAppTypeByCusId.do"/>?cus_id='+cus_id;
	url = EMPTools.encodeURI(url);
	var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
};
 

function getContMsg(data){
	CtrLimitApp.cont_no._setValue(data.cont_no._getValue());
};
function changeAppType(){
	var appType = CtrLimitApp.app_type._getValue();
	if(appType == 01){//新增
		CtrLimitApp.cus_id._obj._renderRequired(true);
		CtrLimitApp.cus_id._obj._renderHidden(false);
		CtrLimitApp.cus_name._obj._renderRequired(true);
		CtrLimitApp.cus_name._obj._renderHidden(false);
		CtrLimitApp.cont_no._obj._renderRequired(false);
		CtrLimitApp.cont_no._obj._renderHidden(true);
		CtrLimitApp.cur_type._obj._renderRequired(true);
		CtrLimitApp.cur_type._obj._renderHidden(false);
		CtrLimitApp.app_amt._obj._renderRequired(true);
		CtrLimitApp.app_amt._obj._renderHidden(false);
		CtrLimitApp.start_date._obj._renderRequired(true);
		CtrLimitApp.start_date._obj._renderHidden(false);
		CtrLimitApp.end_date._obj._renderRequired(true);
		CtrLimitApp.end_date._obj._renderHidden(false);
		CtrLimitApp.memo._obj._renderRequired(false);
		CtrLimitApp.memo._obj._renderHidden(false);
	}else if(appType == 02){//变更
		CtrLimitApp.cus_id._obj._renderRequired(false);
		CtrLimitApp.cus_id._obj._renderHidden(true);
		CtrLimitApp.cus_name._obj._renderRequired(false);
		CtrLimitApp.cus_name._obj._renderHidden(true);
		CtrLimitApp.cont_no._obj._renderRequired(true);
		CtrLimitApp.cont_no._obj._renderHidden(false);
		CtrLimitApp.cur_type._obj._renderRequired(false);
		CtrLimitApp.cur_type._obj._renderHidden(true);
		CtrLimitApp.app_amt._obj._renderRequired(false);
		CtrLimitApp.app_amt._obj._renderHidden(true);
		CtrLimitApp.start_date._obj._renderRequired(false);
		CtrLimitApp.start_date._obj._renderHidden(true);
		CtrLimitApp.end_date._obj._renderRequired(false);
		CtrLimitApp.end_date._obj._renderHidden(true);
		CtrLimitApp.memo._obj._renderRequired(false);
		CtrLimitApp.memo._obj._renderHidden(true);
	}
};


//两个日期作比较
function checkInsurStartDate(){
	if(CtrLimitApp.start_date._obj.element.value!=''){
		var e = CtrLimitApp.end_date._obj.element.value;
		var s = CtrLimitApp.start_date._obj.element.value;
		if(e!=''){
			if(s>e){
        		alert('起始日期必须小于或等于到期日期！');
        		CtrLimitApp.start_date._obj.element.value="";
        		return;
        	}
		}
	}
};
function checkInsurEndDate(){
	if(CtrLimitApp.end_date._obj.element.value!=''){
		var e = CtrLimitApp.end_date._obj.element.value;
		var s = CtrLimitApp.start_date._obj.element.value;
		if(s!=''){
			if(s>e){
        		alert('起始日期必须大于或等于到期日期！');
        		CtrLimitApp.end_date._obj.element.value="";
        		return;
        	}
		}
	}
};
</script>
</head>
<body class="page_content" onload="doOnLoad();">
	<emp:form id="submitForm" action="addCtrLimitAppRecord.do" method="POST">
		<emp:gridLayout id="CtrLimitAppGroup" title="额度合同申请表" maxColumn="2">
		
			<emp:text id="CtrLimitApp.serno" label="业务编号" maxlength="40" hidden="true" required="false" colSpan="2"/>
			<!-- modified by yangzy 2015/04/20 需求：XD150325024，集中作业扫描岗权限改造 start -->
			<!--/*modified by wangj XD150918069  丰泽鲤城区域团队业务流程改造 begin*/-->
			<emp:pop id="CtrLimitApp.cus_id" label="客户码" url="queryAllCusPop.do?cusTypCondition=cus_status='20' and main_br_id='${context.organNo}' and cust_mgr = '${context.currentUserId}' and cert_code not in (select cert_code from cus_blk_list where status='002' and black_level='3')&opt=team&cusTypCondition2=cus_status='20' and cust_mgr = '${context.currentUserId}' and cert_code not in (select cert_code from cus_blk_list where status='002' and black_level='3')&returnMethod=getCusMsg" required="true" />
			<!--/*modified by wangj XD150918069  丰泽鲤城区域团队业务流程改造 end*/-->
			<!-- modified by yangzy 2015/04/20 需求：XD150325024，集中作业扫描岗权限改造 end -->
			<emp:text id="CtrLimitApp.cus_name" label="客户名称" readonly="true"/>
			<emp:select id="CtrLimitApp.app_type" label="申请类型"  required="true" readonly="true"  defvalue="01" dictname="STD_ZB_APP_TYPE" onchange="changeAppType();"/>
			<emp:pop id="CtrLimitApp.cont_no" label="合同编号"  readonly="true"  url="queryCtrLimitContListPop.do?returnMethod=getContMsg" required="false" hidden="true"/>
			<emp:select id="CtrLimitApp.cur_type" label="币种"  required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY"/>
			<emp:text id="CtrLimitApp.app_amt" label="申请金额" maxlength="16" required="true" dataType="Currency"/>
			<emp:date id="CtrLimitApp.start_date" label="起始日期" onblur="checkInsurStartDate()" required="true" defvalue="${context.OPENDAY}"/>
			
			<emp:date id="CtrLimitApp.end_date" label="到期日期" onblur="checkInsurEndDate()" required="true" />
			<emp:select id="CtrLimitApp.approve_status" label="申请状态" required="false" hidden="true" dictname="WF_APP_STATUS" defvalue="000" />
			<emp:textarea id="CtrLimitApp.memo" label="备注" maxlength="200" required="false" />
			<emp:text id="CtrLimitApp.input_date" label="登记日期" maxlength="10" required="false" defvalue="${context.OPENDAY}" hidden="true"/>
			<!-- add by lisj 2015-10-15 XD150918069 丰泽鲤城区域团队业务流程改造 begin-->
			<emp:text id="CtrLimitApp.manager_br_id" label="管理机构" maxlength="20" hidden="true" required="false" />
			<!-- add by lisj 2015-10-15 XD150918069 丰泽鲤城区域团队业务流程改造  end-->
			<emp:text id="CtrLimitApp.input_id" label="登记人" maxlength="32" required="false" hidden="true" defvalue="${context.currentUserId}"/>
			<emp:text id="CtrLimitApp.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="${context.organNo}"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="save" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

