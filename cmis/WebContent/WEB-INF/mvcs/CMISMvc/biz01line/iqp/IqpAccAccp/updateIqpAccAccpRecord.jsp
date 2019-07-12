<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	String cont="";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
		if(op.equals("view")){   
			request.setAttribute("canwrite","");
		}
	}
	/**modified by lisj 2015-8-13 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
	if(context.containsKey("cont")){
		cont = (String)context.getDataValue("cont");
		if(cont.equals("cont") || cont.equals("modify")){   
			request.setAttribute("canwrite","");
		}
	}
	String modify_rel_serno ="";
	if(context.containsKey("modify_rel_serno")){
		modify_rel_serno = (String)context.getDataValue("modify_rel_serno");
	}
	/**modified by lisj 2015-8-13 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
    function load(){
    	var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var bill_qnt = jsonstr.bill_qnt;
				if(flag == "success"){
					IqpAccAccp.bill_qnt._setValue(bill_qnt); 
					var is_elec_bill = IqpAccAccp.is_elec_bill._getValue();
    				if(is_elec_bill == '1'){
    					IqpAccAccp.actp_org_no._obj._renderReadonly(true);
        			}  
				}else {
					alert("保存失败！"+jsonstr.msg);
					//alert("保存失败！");
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
		var url = '<emp:url action="updateBillQntRecord.do"/>?serno=${context.serno}&cont_no=${context.cont_no}';
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
    };


	function refreshIqpAccpDetail() {
		IqpAccAccp_tabs.tabs.IqpAccpDetail_tab.refresh();
	};
	/*--user code begin--*/
	//-------异步保存主表单页面信息-------
	function doSave(){
		if(!IqpAccAccp._checkAll()){
			return;
		}
		var form = document.getElementById("submitForm");
		IqpAccAccp._toForm(form);
		//var serno = IqpBksyndic._getValue();
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
					alert("保存成功！");
					//getIqpAccAccpUpdatePage
					window.location.reload();
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

		var url = '<emp:url action="updateIqpAccAccpRecord.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	}

	function getOrgNo(data){
    	IqpAccAccp.actp_org_no._setValue(data.bank_no._getValue());
    	IqpAccAccp.actp_org_name._setValue(data.bank_name._getValue());
    };	
    
	function getOpacNo(data){
		IqpAccAccp.opac_org._setValue(data.bank_no._getValue());
		IqpAccAccp.opac_org_name._setValue(data.bank_name._getValue());
    };	
    
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="load();">
	
	<emp:form id="submitForm" action="updateIqpAccAccpRecord.do" method="POST">
		<emp:gridLayout id="IqpAccAccpGroup" title="银行承兑汇票" maxColumn="2">
			<emp:text id="IqpAccAccp.serno" label="业务编号" defvalue="${context.serno}" colSpan="2" hidden="true" maxlength="40" required="false" readonly="true" />
			<emp:select id="IqpAccAccp.opac_type" label="签发类型" required="true" dictname="STD_ZB_SIGN_TYPE" /> 
			<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  begin-->
			<emp:pop id="IqpAccAccp.opac_org" label="签发行行号"  url="getPrdBankInfoPopList.do?restrictUsed=false&status=1" returnMethod="getOpacNo" required="true" />
			<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  end-->
			<emp:text id="IqpAccAccp.opac_org_name" label="签发行行名" maxlength="100" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
			<emp:select id="IqpAccAccp.is_elec_bill" label="是否电子票据" required="true" readonly="true" defvalue="2" dictname="STD_ZX_YES_NO" /> 
			<emp:text id="IqpAccAccp.adv_rate" label="垫款利率" maxlength="10" required="false" dataType="Rate" hidden="true"/> 
			<emp:text id="IqpAccAccp.bill_qty" label="汇票数量" maxlength="38" dataType="Int" required="false" readonly="true"/>
			<emp:text id="IqpAccAccp.bill_qnt" label="汇票金额" maxlength="18" dataType="Currency" required="false" readonly="true"/>
			<emp:select id="IqpAccAccp.acpt_org_type" label="承兑行类型" required="true" dictname="STD_AORG_ACCTSVCR_TYPE" colSpan="2"/>
			<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  begin-->
			<emp:pop id="IqpAccAccp.actp_org_no" label="承兑行行号" url="getPrdBankInfoPopList.do?restrictUsed=false&status=1" returnMethod="getOrgNo" required="true" buttonLabel="选择" />
			<!--modified by wangj 需求编号：ED150612003 ODS系统取数需求  end-->
			<emp:text id="IqpAccAccp.actp_org_name" label="承兑行名称" maxlength="100" required="true" readonly="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"/>
			<emp:textarea id="IqpAccAccp.use" label="用途" maxlength="250" required="false" colSpan="2" /> 
		    <emp:text id="IqpAccAccp.chrg_rate" label="手续费率" required="false" hidden="true" dataType="Rate" />
		</emp:gridLayout>

		<div align="center">
			<emp:actButton id="save" label="保存" op="update"/>
			<emp:actButton id="reset" label="重置" op="cancel"/> 
		</div>
	</emp:form>
	
	<emp:tabGroup id="IqpAccAccp_tabs" mainTab="IqpAccpDetail_tab">
		<emp:tab id="IqpAccpDetail_tab" label="承兑汇票申请明细" url="queryIqpAccAccpIqpAccpDetailList.do" reqParams="op=${context.op}&cont=${context.cont}&IqpAccAccp.serno=${context.serno}&is_elec_bill=${context.IqpAccAccp.is_elec_bill}&modify_rel_serno=${context.modify_rel_serno}" initial="true"/>
				
	</emp:tabGroup>
</body>
</html>
</emp:page>
