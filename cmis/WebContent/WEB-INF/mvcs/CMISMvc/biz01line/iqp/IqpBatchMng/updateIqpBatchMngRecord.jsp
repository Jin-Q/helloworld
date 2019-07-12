<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<jsp:include page="iqpBatchMngComm.jsp" flush="true" /> 
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String bizType = "";
	String rate = "";
	String updateflag = "";
	if(context.containsKey("bizType")){
		bizType = (String)context.getDataValue("bizType");
	}
	if(context.containsKey("rate")){
		rate = (String)context.getDataValue("rate");
	}
	if(context.containsKey("updateflag")){
		updateflag = (String)context.getDataValue("updateflag");
	}
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doOnLoad(){
		chageBizType();
		var updateflag ="<%=updateflag%>";
		if(updateflag=='ebill'){
			IqpBatchMng.fore_disc_date._obj._renderReadonly(true);
			IqpBatchMng.opp_org_no._obj._renderReadonly(true);
		}
	}
	
	//-------异步保存主表单页面信息,如果贴现日期有变动要同步更新批次下所有票据的利息-------
	function doSave(){
		var bizType = IqpBatchMng.biz_type._getValue();
		if(!IqpBatchMng._checkAll()){
			return;
		}
		if(!discAndRebuyDateCheck()){
            return false;
		}
		if(bizType==04 || bizType==02){
			IqpBatchMng.rebuy_rate._setValue(IqpBatchMng.rate._getValue());
		}
		var form = document.getElementById("submitForm");
		IqpBatchMng._toForm(form);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var msg = jsonstr.msg;
				if(flag == "success"){
					alert("保存成功！");
					window.location.reload();
				}else {
					alert(msg);
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

		var url = '<emp:url action="updateIqpBatchMngRecord.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	};

	//-------------对手行行号pop框选择返回函数-----------
    function getOrgNo(data){
    	IqpBatchMng.opp_org_no._setValue(data.bank_no._getValue());
    	IqpBatchMng.opp_org_name._setValue(data.bank_name._getValue());
    };

  //POP框返回方法
	function setconId(data){
		IqpBatchMng.manager_id._setValue(data.actorno._getValue());
		IqpBatchMng.manager_id_displayname._setValue(data.actorname._getValue());
		IqpBatchMng.manager_br_id._setValue(data.orgid._getValue());
		IqpBatchMng.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		IqpBatchMng.manager_br_id_displayname._obj._renderReadonly(true);
		doOrgCheck();
	};

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
					IqpBatchMng.manager_br_id._setValue(jsonstr.org);
					IqpBatchMng.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					IqpBatchMng.manager_br_id._setValue("");
					IqpBatchMng.manager_br_id_displayname._setValue("");
					IqpBatchMng.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = IqpBatchMng.manager_id._getValue();
					IqpBatchMng.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					IqpBatchMng.manager_br_id._setValue("");
					IqpBatchMng.manager_br_id_displayname._setValue("");
					IqpBatchMng.manager_br_id_displayname._obj._renderReadonly(false);
					IqpBatchMng.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = IqpBatchMng.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	function getOrgID(data){
		IqpBatchMng.manager_br_id._setValue(data.organno._getValue());
		IqpBatchMng.manager_br_id_displayname._setValue(data.organname._getValue());
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad();">
	<emp:form id="submitForm" action="updateIqpBatchMngRecord.do" method="POST">
			<emp:gridLayout id="IqpBatchMngGroup" title="票据批次信息" maxColumn="2">
				<emp:text id="IqpBatchMng.batch_no" label="批次号"  readonly="true" maxlength="40" colSpan="2" required="false" hidden="false"/>
				<emp:select id="IqpBatchMng.bill_type" label="票据种类" readonly="true" required="true" dictname="STD_DRFT_TYPE"/>
				<emp:select id="IqpBatchMng.biz_type" onblur="chageBizType();" label="暂存用途" readonly="true" required="false" dictname="STD_ZB_BUSI_TYPE"/>
				<emp:date id="IqpBatchMng.fore_disc_date" label="转/贴现日期"  onblur="discDateCheck()" required="true" />
				<emp:text id="IqpBatchMng.rate" label="转/再贴现利率" maxlength="10" required="false" dataType="Rate" />
				<emp:date id="IqpBatchMng.rebuy_date" label="回购日期" onblur="rebuyDateCheck()" required="false" />
				<emp:text id="IqpBatchMng.rebuy_rate" label="回购利率" maxlength="10" required="false" dataType="Rate" />
				<emp:text id="IqpBatchMng.rebuy_int" label="回购利息" maxlength="18" required="false" readonly="true" dataType="Currency" />
				<emp:text id="IqpBatchMng.due_rebuy_rate" label="逾期回购利率" maxlength="10" required="false" dataType="Rate" />
				<emp:select id="IqpBatchMng.opp_org_type" label="对手行类型" required="false" dictname="STD_AORG_ACCTSVCR_TYPE" colSpan="2"/>
				<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  begin-->
				<emp:pop id="IqpBatchMng.opp_org_no" label="对手行行号" url="getPrdBankInfoPopList.do?status=1" returnMethod="getOrgNo" required="false" buttonLabel="选择" />
				<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  end-->
				<emp:text id="IqpBatchMng.opp_org_name" label="对手行行名" maxlength="100" readonly="true" required="false" colSpan="2"  cssElementClass="emp_field_text_long_readonly"/>
				<emp:text id="IqpBatchMng.bill_qnt" label="票据数量" maxlength="38" readonly="true" required="false" dataType="Int" />
				<emp:text id="IqpBatchMng.bill_total_amt" label="票据总金额" maxlength="18" readonly="true" required="false" dataType="Currency" />
				<emp:text id="IqpBatchMng.int_amt" label="利息金额" maxlength="18" required="false" readonly="true" dataType="Currency" />
				<emp:text id="IqpBatchMng.rpay_amt" label="实付金额" maxlength="18" required="false" readonly="true" dataType="Currency" />
				
				<emp:text id="IqpBatchMng.input_id" label="登记人" maxlength="40" hidden="true"  required="false" />
				<emp:text id="IqpBatchMng.input_br_id" label="登记机构" maxlength="20" hidden="true" required="false" />
				<emp:date id="IqpBatchMng.input_date" label="登记日期" hidden="true"  required="false" />
				<emp:select id="IqpBatchMng.status" label="状态" hidden="true" required="false" dictname="STD_BATCH_NUM_STATUS" />
				
				<emp:pop id="IqpBatchMng.manager_id_displayname" label="责任人" hidden="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" required="false" buttonLabel="选择"/>
				<emp:pop id="IqpBatchMng.manager_br_id_displayname" label="责任机构" hidden="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" required="false" buttonLabel="选择" readonly="true"/>
				<emp:text id="IqpBatchMng.manager_br_id" label="责任机构" hidden="true"/>
				<emp:text id="IqpBatchMng.manager_id" label="责任人" hidden="true"/>
			</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="save" label="确定" />
			<emp:button id="reset" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
