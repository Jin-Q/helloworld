<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script src="<emp:file fileName='scripts/grt/pub/grtAddPageUtil.js'/>"
        type="text/javascript" language="javascript"></script>
<style type="text/css">
.emp_field_text_input {
border: 1px solid #b7b7b7;
text-align:left;
width:300px;
}
</style>

<script type="text/javascript">
	
/*--user code begin--*/
	function returnCusId(data){
		LmtSigLmt.cus_id._setValue(data.cus_id._getValue());
		LmtSigLmt.cus_id_displayname._setValue(data.same_org_cnname._getValue());
	}
	
	function doReturn(){
		var url = '<emp:url action="queryLmtSigLmtList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	
	}
	
	function doNext(){
		var result = LmtSigLmt._checkAll();
		var crd_grade = LmtSigLmt.crd_grade._getValue();
		if(crd_grade=='E'||crd_grade=='Z'){
			alert("信用等级为E不能授信！");
			return false;
		}					
	    if(result){
	    	var cus_id = LmtSigLmt.cus_id._getValue();
			var limit_type = LmtSigLmt.limit_type._getValue();
			var url = '<emp:url action="getNextLmtSigPage.do"/>?cus_id='+cus_id+"&limit_type="+limit_type+"&op=add";
			url = EMPTools.encodeURI(url);
			window.location = url;
	    	//saveSigLmt();
	    }else {
		    alert("保存失败！\n请检查各标签页面中的必填信息是否遗漏！");
		}
	}
	//校验该客户是否存在授信
	function saveSigLmt(){
		var form = document.getElementById("submitForm");				
		LmtSigLmt._toForm(form);
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
					var cus_id = LmtSigLmt.cus_id._getValue();
					var limit_type = LmtSigLmt.limit_type._getValue();
					var url = '<emp:url action="getNextLmtSigPage.do"/>?cus_id='+cus_id+"&limit_type="+limit_type;
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else if(flag=="fail"){
					alert("该客户已有正在进行的授信，不能重复发起！");
					window.location.reload();
				}else{
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
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	}
/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="addLmtSigLmtRecord.do" method="POST">
		<emp:gridLayout id="LmtSigLmtGroup" title="单笔授信 " maxColumn="2">
			<emp:select id="LmtSigLmt.limit_type" label="额度类型" required="true" dictname="STD_ZB_LIMIT_TYPE" colSpan="2" defvalue="01" readonly="true"/>
			<emp:pop id="LmtSigLmt.cus_id" label="客户码"  required="true" colSpan="2" url="queryCusSameOrgForPopList.do?restrictUsed=false" returnMethod="returnCusId"/>
			<emp:text id="LmtSigLmt.cus_id_displayname" label="客户名称" readonly="true" cssElementClass="emp_field_text_input"/>
			<emp:select id="LmtSigLmt.crd_grade" label="信用等级" dictname="STD_ZB_FINA_GRADE" hidden="true"/>
			<emp:text id="LmtSigLmt.serno" label="业务编号" maxlength="32"  hidden="true" />
			<emp:date id="LmtSigLmt.app_date" label="申请日期" required="false" hidden="true" defvalue="$OPENDAY"/>
			<emp:date id="LmtSigLmt.over_date" label="办结日期" required="false" hidden="true"/>			
			<emp:select id="LmtSigLmt.app_type" label="申请类型" required="false" dictname="STD_ZB_APP_TYPE" hidden="true" />
			<emp:select id="LmtSigLmt.app_cls" label="申请类别" required="false" hidden="true" dictname="STD_ZB_APP_CLS" defvalue="01"/>
			<emp:text id="LmtSigLmt.owner_wrr" label="所有者权益" maxlength="16" required="false" hidden="true"/>
			<emp:text id="LmtSigLmt.asserts" label="总资产" maxlength="16" required="false" hidden="true"/>
			<emp:text id="LmtSigLmt.risk_quota" label="风险限额" maxlength="16" required="false" hidden="true"/>
			<emp:text id="LmtSigLmt.cur_type" label="授信币种" maxlength="3" required="false" hidden="true" defvalue="CNY"/>
			<emp:text id="LmtSigLmt.lmt_amt" label="授信金额" maxlength="16" required="false" hidden="true"/>
			<emp:select id="LmtSigLmt.term_type" label="期限类型" required="false" dictname="STD_ZB_TERM_TYPE" hidden="true" defvalue="001"/>
			<emp:text id="LmtSigLmt.term" label="期限" maxlength="6" required="false" hidden="true" defvalue="1"/>
			<emp:textarea id="LmtSigLmt.memo" label="备注" maxlength="200" required="false" colSpan="2" hidden="true"/>
			<emp:pop id="LmtSigLmt.manager_id" label="责任人" url="null" required="false" hidden="true"/>
			<emp:pop id="LmtSigLmt.manager_br_id" label="管理机构" url="null" required="false" hidden="true"/>
			<emp:text id="LmtSigLmt.input_id" label="登记人" maxlength="20" required="false" defvalue="$currentUserName" hidden="true"/>
			<emp:text id="LmtSigLmt.input_br_id" label="登记机构" maxlength="32" required="false" defvalue="$organName" hidden="true"/>
			<emp:date id="LmtSigLmt.input_date" label="登记日期" required="false" defvalue="$OPENDAY" hidden="true"/>
			<emp:select id="LmtSigLmt.approve_status" label="审批状态" required="false" dictname="WF_APP_STATUS" hidden="true" defvalue="000"/>
			<emp:text id="LmtSigLmt.flow_type" label="流程类型" maxlength="6" required="false" hidden="true" defvalue="01"/>
		</emp:gridLayout>
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
