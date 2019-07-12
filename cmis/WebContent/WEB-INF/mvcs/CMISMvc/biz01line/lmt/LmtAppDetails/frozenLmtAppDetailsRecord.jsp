<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>
<html>
<head>
<style type="text/css">
	.emp_field_textarea_readonly {
		border: 1px solid #b7b7b7;
		background-color:#eee;
		text-align: left;
		width: 450px;
		height: 50px;
	};
</style>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String app_type = context.getDataValue("app_type").toString();
%>
<script type="text/javascript">
	
	/*--user code begin--*/
	//保存冻结申请分项信息
	function doSaveDet(){
		var form = document.getElementById("submitForm");
		LmtAppDetails._toForm(form);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if(flag == "success"){
					alert('保存成功！');
					var serno = LmtAppDetails.serno._getValue("");
					var url = '<emp:url action="queryLmtFrozenDetailsList.do"/>&serno='+ serno+'&updflag=update&app_type='+'<%=app_type%>';
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else {
					alert("新增选择产品发生异常！");
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

	//进行保存操作
	function doSave(){
		var app_type = '<%=app_type%>';
		//若为冻结申请则校验冻结金额，若为解冻金额则直接保存
		if(app_type=='03'){
			doCheckCanFrozenAmt();
		}else{
			doSaveDet();
		}
	}
	
	//校验冻结金额是否超过（授信总额-冻结金额）
	function doCheckCanFrozenAmt(){
		var orgLmtCode = LmtAppDetails.org_limit_code._getValue();
		var frozeAmt = LmtAppDetails.froze_amt._getValue();
		var crdAmt = LmtAppDetails.crd_amt._getValue();
		var url = '<emp:url action="checkLmtCanFrozenAmt.do"/>&org_limit_code='+ orgLmtCode;
		url = EMPTools.encodeURI(url);
		if(!LmtAppDetails._checkAll()){
			return false;
		}
		if(frozeAmt-crdAmt>0){
			alert('冻结金额不能大于授信金额！');
			return;
		}
		var form = document.getElementById("submitForm");
		LmtAppDetails._toForm(form);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if(flag == "success"){
					var can_froze_amt = jsonstr.can_froze_amt;
					if(can_froze_amt-frozeAmt>=0){
						doSaveDet();
					}else{
						alert('本笔台账已冻结'+(crdAmt-can_froze_amt)+'，还可冻结'+can_froze_amt);
					}
				}else {
					alert("保存失败！");
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
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}
	
	
	function onload(){
		/**mofified by lisj 2015-6-23 需求编号：XD150123005 小微自助循环贷款改造 begin**/
		/*modify by wangj 2015-05-27  需求编号:XD141222087,法人账户透支需求变更  begin */
		var limitName=LmtAppDetails.limit_name._getValue();//额度品种名称
		if(limitName=="100051" || limitName=="100088"){
			var app_type = '<%=app_type%>';
			var crd_amt = LmtAppDetails.crd_amt._getValue();//授信总额
			if("03"==app_type){
				LmtAppDetails.froze_amt._setValue(crd_amt);//冻结金额
				LmtAppDetails.froze_amt._obj._renderReadonly(true);
			}else{
				LmtAppDetails.unfroze_amt._setValue(crd_amt);//解冻金额
				LmtAppDetails.unfroze_amt._obj._renderReadonly(true);
			}	
		}
		/*modify by wangj 2015-05-27  需求编号:XD141222087,法人账户透支需求变更  end */
	    /**mofified by lisj 2015-6-23 需求编号：XD150123005 小微自助循环贷款改造 end**/
	}

	function checkAmt(){
		var crd_amt = LmtAppDetails.crd_amt._getValue();//授信总额
		var froze_amt = LmtAppDetails.froze_amt._getValue();//冻结金额
		var agr_froze_amt = LmtAgrDetails.froze_amt._getValue();//已冻结金额
		if(agr_froze_amt==''){
			agr_froze_amt=0;
		}
		
		if(crd_amt-froze_amt<0){
			alert("冻结金额必须小于等于授信总额！");
			LmtAppDetails.froze_amt._setValue(crd_amt);
			return false;
		}

		if(crd_amt-agr_froze_amt-froze_amt<0){
			alert("冻结金额必须小于等于剩余的授信金额！");
			LmtAppDetails.froze_amt._setValue('');
			return false;
		}
	}

	function checkUnfrozeAmt(){
		var unfroze_amt = LmtAppDetails.unfroze_amt._getValue();//解冻金额
		var agr_froze_amt = LmtAgrDetails.froze_amt._getValue();//已冻结金额
		if(agr_froze_amt==''){
			agr_froze_amt=0;
		}
		
		if(agr_froze_amt-unfroze_amt<0){
			alert("解冻金额必须小于等于冻结总额！");
			LmtAppDetails.unfroze_amt._setValue(agr_froze_amt);
			return false;
		}
	}

	function doReturn() {
		var serno = LmtAppDetails.serno._getValue();
		var app_type = '<%=app_type%>';
		var url = '<emp:url action="queryLmtFrozenDetailsList.do"/>&serno='+serno+"&updflag=update&app_type="+app_type;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onload()">
	<emp:form id="submitForm" action="updateLmtAppDetailsRecord.do" method="POST">
		<emp:gridLayout id="LmtAppDetailsGroup" maxColumn="2" title="额度冻结解冻信息">
			<emp:select id="LmtAppDetails.sub_type" label="分项类别" required="true" dictname="STD_LMT_PROJ_TYPE" readonly="true"/>
			<emp:select id="LmtAppDetails.limit_type" label="额度类型" required="false" dictname="STD_ZB_LIMIT_TYPE" readonly="true"/>
			<emp:text id="LmtAppDetails.limit_code" label="授信额度编号" maxlength="32" required="true" readonly="true" hidden="true"/>
			<emp:text id="LmtAppDetails.org_limit_code" label="授信额度编号" maxlength="32" required="true" readonly="true" />
			<emp:text id="LmtAppDetails.limit_name_displayname" label="额度品种名称" required="true" readonly="true" cssElementClass="emp_field_text_readonly"/>
			<emp:text id="LmtAppDetails.limit_name" label="额度品种名称" maxlength="60" required="true" hidden="true" cssElementClass="emp_field_text_readonly"/>
			<emp:pop id="LmtAppDetails.prd_id" label="适用产品" url="null" required="true" readonly="true" cssElementClass="emp_field_text_readonly"/>
			<emp:textarea id="LmtAppDetails.prd_id_displayname" label="适用产品名称" required="true" colSpan="2" cssElementClass="emp_field_textarea_readonly"/>
			<emp:select id="LmtAppDetails.cur_type" label="授信币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true"/>
			<emp:text id="LmtAppDetails.crd_amt" label="授信总额" maxlength="18" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAgrDetails.froze_amt" label="已冻结金额" maxlength="18" required="true" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly" defvalue="0"/>
			<%if("03".equals(app_type)){ %>
			<emp:text id="LmtAppDetails.froze_amt" label="冻结金额" maxlength="18" required="true" dataType="Currency" onchange="checkAmt()"/>
			<%}else{ %>
			<emp:text id="LmtAppDetails.unfroze_amt" label="解冻金额" maxlength="18" required="true" dataType="Currency" onchange="checkUnfrozeAmt()"/>
			<%} %>
			<emp:select id="LmtAppDetails.guar_type" label="担保方式" required="true" dictname="STD_ZB_ASSURE_MEANS" readonly="true" colSpan="2"/>
			<emp:text id="LmtAppDetails.start_date" label="起始日期" required="true" readonly="true"/> 
			<emp:text id="LmtAppDetails.end_date" label="到期日期" required="true" readonly="true"/>
			
			<emp:select id="LmtAppDetails.is_pre_crd" label="是否预授信" required="false" dictname="STD_ZX_YES_NO" readonly="true" hidden="true"/>
			<emp:text id="LmtAppDetails.serno" label="业务编号" maxlength="40" required="true" hidden="true"/>
			<emp:select id="LmtAppDetails.is_adj_term" label="是否调整期限" required="false" dictname="STD_ZX_YES_NO" hidden="true"/>
			<emp:pop id="LmtAppDetails.core_corp_cus_id" label="核心企业客户码 " url="null" required="false" hidden="true"/>
			<emp:select id="LmtAppDetails.core_corp_duty" label="核心企业责任" required="false" dictname="STD_ZB_CORP_DUTY" hidden="true"/>
			<emp:text id="LmtAppDetails.ori_crd_amt" label="原有授信金额" maxlength="16" required="false" hidden="true" />
			<emp:text id="LmtAppDetails.org_limit_code" label="台账授信额度编号" maxlength="16" required="false" hidden="true" />
			<emp:text id="LmtAppDetails.app_type" label="申请类型：用于冻结解冻" maxlength="16" defvalue="<%=app_type%>" hidden="true" />
			
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="save" label="修改"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
