<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	background-color:#eee;
	text-align:left;
	width:180px;
}
</style>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		window.history.go(-1);
	}
	
	function doView(){
		var paramStr = CusSameOrgList._obj.getParamStr(['cus_id']);
		if (paramStr != null){   
			var handleSuccess = function(o){ 		
			if(o.responseText != undefined){
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr define error!"+e.message);
					return;
				}
				var flag = jsonstr.flag;
				if(flag == "success" ){
					alert("客户在批量包还未授信！");
				}else{					
					var url = '<emp:url action="queryLmtSigLmtDetail.do"/>?'+paramStr+'&flag=view';
					url = EMPTools.encodeURI(url);
					var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
					EMPTools.openWindow(url,'newwindow',param);
				}
			}
		};
		var handleFailure = function(o){
			alert("异步回调失败！");	
		};
		var url = '<emp:url action="checkCusIsLmt.do"/>?'+paramStr;
		var callback = {
				success:handleSuccess,
				failure:handleFailure
		};
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url,callback);
		} else {
			alert('请先选择一条记录！');
		}
	}
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:tabGroup id="LmtBatchLmt_tabs" mainTab="tab1">
    	<emp:tab id="tab1" label="基本信息" needFlush="true" initial="true">
			<emp:form id="submitForm" action=".do" method="POST">
				<emp:gridLayout id="LmtBatchLmtGroup" title="批量授信" maxColumn="2">
					<emp:text id="LmtBatchLmt.serno" label="业务编号  " maxlength="32" required="true" colSpan="2" cssElementClass="emp_field_text_input2"/>
					<emp:text id="LmtBatchLmt.batch_cus_no" label="批量客户编号" maxlength="32" required="true" cssElementClass="emp_field_text_input2"/>
					<emp:select id="LmtBatchLmt.app_cls" label="申请类别" required="false" dictname="STD_ZB_APP_CLS"/>
					<emp:select id="LmtBatchLmt.limit_type" label="额度类型" required="true" dictname="STD_ZB_LIMIT_TYPE" />
					<emp:select id="LmtBatchLmt.cur_type" label="授信币种" required="false" dictname="STD_ZX_CUR_TYPE" />
					<emp:text id="LmtBatchLmt.lmt_totl_amt" label="授信总额(元)" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
					<emp:text id="LmtBatchLmt.single_quota" label="单户限额(元)" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
					<emp:select id="LmtBatchLmt.term_type" label="期限类型" required="false" dictname="STD_ZB_TERM_TYPE" />
					<emp:text id="LmtBatchLmt.term" label="期限" maxlength="6" required="false" />
					<emp:textarea id="LmtBatchLmt.memo" label="备注" maxlength="200" required="false" colSpan="2" />
					<emp:pop id="LmtBatchLmt.manager_id_displayname" label="责任人" url="null" required="false" />
					<emp:pop id="LmtBatchLmt.manager_br_id_displayname" label="管理机构" url="null" required="false" />
					<emp:text id="LmtBatchLmt.input_id_displayname" label="登记人" required="false" />
					<emp:text id="LmtBatchLmt.input_br_id_displayname" label="登记机构" required="false" />
					<emp:date id="LmtBatchLmt.input_date" label="登记日期" required="false" />
					<emp:select id="LmtBatchLmt.approve_status" label="审批状态" required="false" dictname="WF_APP_STATUS" />
					<emp:select id="LmtBatchLmt.app_type" label="申请类型" required="false" dictname="STD_ZB_APP_TYPE" hidden="true"/>
					<emp:date id="LmtBatchLmt.app_date" label="申请日期" required="false" hidden="true"/>
					<emp:date id="LmtBatchLmt.end_date" label="办结日期" required="false" hidden="true"/>
					<emp:pop id="LmtBatchLmt.manager_id" label="责任人" url="null" required="false" hidden="true"/>
					<emp:pop id="LmtBatchLmt.manager_br_id" label="管理机构" url="null" required="false" hidden="true"/>
					<emp:text id="LmtBatchLmt.input_id" label="登记人" maxlength="20" required="false" hidden="true"/>
					<emp:text id="LmtBatchLmt.input_br_id" label="登记机构" maxlength="32" required="false" hidden="true"/>
					<emp:text id="LmtBatchLmt.flow_type" label="流程类型" maxlength="10" required="false" hidden="true"/>  			
			</emp:gridLayout>
		</emp:form>
			<div align="left">
				<br>
				<emp:button id="view" label="查看"/>
			</div>
		  		<emp:table icollName="CusSameOrgList" pageMode="true" url="pageCusSame4BatchQuery.do" reqParams="serno=${context.serno}">
				    <emp:text id="cus_id" label="客户码"/>
					<emp:text id="same_org_cnname" label="同业机构(行)名称" />
					<emp:text id="same_org_type" label="同业机构(行)类型" dictname="STD_ZB_INTER_BANK_ORG"/>
					<emp:text id="assets" label="总资产(万元)" dataType="Currency"/>
					<emp:text id="paid_cap_amt" label="实收资本(万元)" dataType="Currency"/>
					<emp:text id="lmt_amt" label="授信金额" dataType="Currency"/>
					<emp:text id="input_date" label="登记日期" />
				</emp:table>
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
	
	<div align="center">
		<br>
		<emp:button id="return" label="返回到列表页面"/>
	</div>
</body>
</html>
</emp:page>
