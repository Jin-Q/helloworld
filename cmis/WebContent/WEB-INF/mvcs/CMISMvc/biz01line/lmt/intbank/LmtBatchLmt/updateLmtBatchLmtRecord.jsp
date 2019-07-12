<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	background-color:#eee;
	text-align:left;
	width:180px;
}
</style>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script type="text/javascript">
	/*--user code begin--*/
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusSameOrg._toForm(form);
		CusSameOrgList._obj.ajaxQuery(null,form);
	};
	
	function getOrgID(data){
		LmtBatchLmt.manager_br_id._setValue(data.organno._getValue());
    	LmtBatchLmt.manager_br_id_displayname._setValue(data.organname._getValue());      
	}

    function setconId(data){
    	LmtBatchLmt.manager_id_displayname._setValue(data.actorname._getValue());
    	LmtBatchLmt.manager_id._setValue(data.actorno._getValue());
    	LmtBatchLmt.manager_br_id._setValue(data.orgid._getValue());
    	LmtBatchLmt.manager_br_id_displayname._setValue(data.orgid_displayname._getValue()); 
    	//LmtBatchLmt.manager_br_id_displayname._obj._renderReadonly(true);
    	doOrgCheck();
	}

    function doOrgCheck(){
   		var handleSuccess = function(o) {
   			if (o.responseText !== undefined) {
   				try {
   					var jsonstr = eval("(" + o.responseText + ")");
   				} catch (e) {
   					alert("Parse jsonstr define error!" + e.message);
   					return;
   				}
   				var flag = jsonstr.flag;
				if("one" == flag){//客户经理只属于一个机构
					LmtBatchLmt.manager_br_id._setValue(jsonstr.org);
					LmtBatchLmt.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					LmtBatchLmt.manager_br_id._setValue("");
					LmtBatchLmt.manager_br_id_displayname._setValue("");
					LmtBatchLmt.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = LmtBatchLmt.manager_id._getValue();
					LmtBatchLmt.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
   				}else if("yteam"==flag){
   					LmtBatchLmt.manager_br_id._setValue("");
   					LmtBatchLmt.manager_br_id_displayname._setValue("");
   					LmtBatchLmt.manager_br_id_displayname._obj._renderReadonly(false);
   					LmtBatchLmt.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
   				}
   			}
   		};
   		var handleFailure = function(o) {
   		};
   		var callback = {
   			success :handleSuccess,
   			failure :handleFailure
   		};
   		var manager_id = LmtBatchLmt.manager_id._getValue();
   		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
   		url = EMPTools.encodeURI(url);
   		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
   	}

    function comSingle(){
		var amt = LmtBatchLmt.lmt_totl_amt._getValue();
		var value = LmtBatchLmt.single_quota._getValue();
		if(amt== 0){
			alert("先输入授信总额！");
			LmtBatchLmt.single_quota._setValue("");
			return;
		}else{
			if(parseFloat(amt)<parseFloat(value)){
				alert("单户限额要小于授信总额！");
				LmtBatchLmt.single_quota._setValue(amt);
			}
		}
	}
	
	function doSetAmt(){		
		if(LmtBatchLmt._checkAll()){
			var serno = LmtBatchLmt.serno._getValue();
			var limit_type = LmtBatchLmt.limit_type._getValue();
			var single_amt = LmtBatchLmt.single_quota._getValue();
			var manager_id = LmtBatchLmt.manager_id._getValue();
			var manager_br_id = LmtBatchLmt.manager_br_id._getValue();
			var paramStr = CusSameOrgList._obj.getParamStr(['cus_id']);
			if(paramStr != null){
				var url='<emp:url action="getLmtSigLmtUpdatePage4Batch.do"/>&serno='+serno+"&"+paramStr
				                 +"&limit_type="+limit_type+"&lmt_amt="+single_amt+"&manager_id="+manager_id+"&manager_br_id="+manager_br_id;
				url = EMPTools.encodeURI(url);
				var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
				EMPTools.openWindow(url,'newwindow',param);
			}else{
				alert('请先选择一条记录！');
			}
		}
	}
	
	function doReturn(){
		var url = '<emp:url action="queryLmtBatchLmtList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function doSave(){
		if(LmtBatchLmt._checkAll()){
			var form = document.getElementById("submitForm");
			LmtBatchLmt._toForm(form);
			var handleSuccess = function(o){
				if(o.responseText !== undefined){
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					if(flag == "success"){
				       alert("保存成功！");
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
		}
	}
	/*--user code end--*/
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm"></form>
	<emp:form id="submitForm" action="updateLmtBatchLmtRecord.do" method="POST">
		<emp:gridLayout id="LmtBatchLmtGroup" maxColumn="2" title="批量授信">
			<emp:text id="LmtBatchLmt.batch_cus_no" label="批量客户编号" maxlength="32" required="true" readonly="true" cssElementClass="emp_field_text_input2"/>
			<emp:select id="LmtBatchLmt.app_cls" label="申请类别" required="true" dictname="STD_ZB_APP_CLS" readonly="true"/>	
			<emp:text id="LmtBatchLmt.serno" label="业务编号  " maxlength="32" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:select id="LmtBatchLmt.limit_type" label="额度类型" defvalue="01" readonly="true" required="true" dictname="STD_ZB_LIMIT_TYPE" />
			<emp:select id="LmtBatchLmt.cur_type" label="授信币种" required="false" dictname="STD_ZX_CUR_TYPE" readonly="true"/>
			<emp:text id="LmtBatchLmt.lmt_totl_amt" label="授信总额(元)" maxlength="18" required="true" dataType="Currency" onblur="comSingle()"/>
			<emp:text id="LmtBatchLmt.single_quota" label="单户限额(元)" maxlength="18" required="true" dataType="Currency" onblur="comSingle()"/>
			<emp:select id="LmtBatchLmt.term_type" label="期限类型" required="true" dictname="STD_ZB_TERM_TYPE"  defvalue="001"/>
			<emp:text id="LmtBatchLmt.term" label="期限" maxlength="6" required="true" defvalue="1" dataType="Int"/>
			<emp:textarea id="LmtBatchLmt.memo" label="备注" maxlength="200" required="false" colSpan="2" />
			<emp:pop id="LmtBatchLmt.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" required="true" 
			         popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
			<emp:pop id="LmtBatchLmt.manager_br_id_displayname" label="管理机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" required="true" 
			         popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no" />
			<emp:text id="LmtBatchLmt.input_id_displayname" label="登记人" required="true" readonly="true"/>
			<emp:text id="LmtBatchLmt.input_br_id_displayname" label="登记机构" required="true" readonly="true"/>
			<emp:date id="LmtBatchLmt.input_date" label="登记日期" required="false" readonly="true"/>
			<emp:select id="LmtBatchLmt.approve_status" label="审批状态" required="false" dictname="WF_APP_STATUS" readonly="true"/>
			<emp:select id="LmtBatchLmt.app_type" label="申请类型" required="false" dictname="STD_ZB_APP_TYPE" hidden="true"/>
			<emp:date id="LmtBatchLmt.app_date" label="申请日期" required="false" hidden="true" defvalue="$OPENDAY"/>
			<emp:date id="LmtBatchLmt.end_date" label="办结日期" required="false" hidden="true"/>
			<emp:text id="LmtBatchLmt.input_id" label="登记人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="LmtBatchLmt.input_br_id" label="登记机构" maxlength="32" required="false" hidden="true"/>
			<emp:pop id="LmtBatchLmt.manager_id" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" required="false" hidden="true"/>
			<emp:pop id="LmtBatchLmt.manager_br_id" label="管理机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" required="false" hidden="true"/>
			<emp:text id="LmtBatchLmt.flow_type" label="流程类型" required="false"  hidden="true" defvalue="01"/>
		</emp:gridLayout>
		</emp:form>
		<div align="left">
			<emp:button id="setAmt" label="设置额度"/>
		</div>
		<emp:table icollName="CusSameOrgList" pageMode="true" url="pageCusSame4BatchQuery.do" reqParams="serno=${context.serno}" >
		    <emp:text id="cus_id" label="客户码"/>
			<emp:text id="same_org_cnname" label="同业机构(行)名称" />
			<emp:text id="same_org_type" label="同业机构(行)类型" dictname="STD_ZB_INTER_BANK_ORG"/>
			<emp:text id="assets" label="总资产(万元)" dataType="Currency"/>
			<emp:text id="paid_cap_amt" label="实收资本(万元)" dataType="Currency"/>
			<emp:text id="input_date" label="登记日期" />
		</emp:table>
		<div align="center">
			<emp:button id="save" label="保存" op="update"/>
			<emp:button id="return" label="返回"/>
		</div>
</body>
</html>
</emp:page>