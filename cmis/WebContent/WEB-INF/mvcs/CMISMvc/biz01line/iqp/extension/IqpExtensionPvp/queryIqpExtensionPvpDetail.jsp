<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<%  
	/**add by lisj 2015-9-7  需求编号：【XD150303015】关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
    String wf_flag="";
    if(context.containsKey("wf_flag")){
    	wf_flag = (String)context.getDataValue("wf_flag");
    }
    String modify_rel_serno="";
    if(context.containsKey("modify_rel_serno")){
    	modify_rel_serno = (String)context.getDataValue("modify_rel_serno");
    }
    String modiflag="";
    if(context.containsKey("modiflag")){
    	modiflag = (String)context.getDataValue("modiflag");
    }
    String dutyNoList ="";
    if(context.containsKey("dutyNoList")){
    	dutyNoList = (String)context.getDataValue("dutyNoList");
    }
    /**add by lisj 2015-9-7  需求编号：【XD150303015】关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn1() {
		var url = '<emp:url action="queryIqpExtensionPvpList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	function doReturn() {
		window.close();
	};
	
	/*--user code begin--*/
	/*** 重置主资源menuId方法begin ***/
	function doLoad(){
		hidden_button = "${context.hidden_button}";
		if(hidden_button == 'true'){
			document.getElementById('button_return').style.display = 'none';
		}
		IqpExtensionPvp.cus_id._obj.addOneButton("cus_id","查看",getCusForm);
		IqpExtensionPvp.fount_bill_no._obj.addOneButton('view13','查看',viewAccInfo);//借据信息查看
		document.getElementById("main_tabs").href="javascript:reLoad();";

		var prd_id = IqpExtensionPvp.prd_id._getValue();
		if(prd_id < '2' ){	//贷款类
			IqpExtensionPvp.base_rate._obj._renderHidden(false);
			IqpExtensionPvp.base_rate._obj._renderRequired(true);
		}else{
			IqpExtensionPvp.base_rate._obj._renderHidden(true);
			IqpExtensionPvp.base_rate._obj._renderRequired(false);
		}
	};
	function reLoad(){
		var url = '<emp:url action="getIqpExtensionPvpViewPage.do"/>?menuIdTab=iqp_extension_pvp&serno=${context.IqpExtensionPvp.serno}&restrictUsed=false&op=view&sub_button=true&hidden_button='+hidden_button;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	function getCusForm(){
		var cus_id = IqpExtensionPvp.cus_id._getValue();
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id;
		url=EMPTools.encodeURI(url);  
	    window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	function viewAccInfo(){
		var accNo = IqpExtensionPvp.fount_bill_no._getValue();
		if(accNo==null||accNo==''){
			alert('借据编号为空！');
		}else{
			var url = "<emp:url action='getAccViewPage.do'/>&isHaveButton=not&bill_no="+accNo;
	      	url=encodeURI(url); 
	      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		}
	};
	/*** 重置主资源menuId方法begin ***/
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<emp:tabGroup mainTab="main_tabs" id="main_tabs">
	<emp:tab label="展期出账信息" id="main_tabs">
	<emp:gridLayout id="IqpExtensionPvpGroup" title="原借据信息" maxColumn="2">
			<emp:text id="IqpExtensionPvp.fount_bill_no" label="原借据编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="IqpExtensionPvp.fount_cont_no" label="原合同编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="IqpExtensionPvp.cus_id" label="客户码" maxlength="40" required="true" readonly="true" />
			<emp:text id="IqpExtensionPvp.cus_id_displayname" label="客户名称" colSpan="2"  cssElementClass="emp_field_text_cusname" readonly="true" />
			<emp:select id="IqpExtensionPvp.fount_cur_type" label="原币种" required="true" dictname="STD_ZX_CUR_TYPE" readonly="true" />
			<emp:text id="IqpExtensionPvp.fount_loan_amt" label="原贷款金额" maxlength="18" required="true" dataType="Currency" readonly="true" />
			<emp:text id="IqpExtensionPvp.fount_loan_balance" label="原贷款余额" maxlength="18" required="true" dataType="Currency" readonly="true" />
			<emp:text id="IqpExtensionPvp.fount_rate" label="原执行利率(年)" maxlength="16" required="true" dataType="Rate" readonly="true" />
			<emp:date id="IqpExtensionPvp.fount_start_date" label="原起贷日期" required="true" readonly="true" />
			<emp:date id="IqpExtensionPvp.fount_end_date" label="原止贷日期" required="true" readonly="true" />
		</emp:gridLayout>
		<emp:gridLayout id="IqpExtensionPvpGroup" title="展期出账信息" maxColumn="2">
			<emp:text id="IqpExtensionPvp.serno" label="业务编号" maxlength="40" required="true" readonly="true" />
			<emp:text id="IqpExtensionPvp.agr_no" label="展期协议编号" required="true" readonly="true"  />
			<emp:text id="IqpExtensionPvp.extension_amt" label="展期金额" maxlength="18" required="true" dataType="Currency" readonly="true" />
			<emp:date id="IqpExtensionPvp.extension_date" label="展期到期日期" required="true" readonly="true" />
			<emp:text id="IqpExtensionPvp.base_rate" label="基准利率(年)" maxlength="16" required="true" dataType="Rate" readonly="true" />
			<emp:text id="IqpExtensionPvp.extension_rate" label="展期利率(年)" maxlength="16" required="true" dataType="Rate" readonly="true" />
			<emp:textarea id="IqpExtensionPvp.memo" label="备注" maxlength="250" required="false" colSpan="2" readonly="true" />
		</emp:gridLayout>
		<emp:gridLayout id="IqpExtensionPvpGroup" maxColumn="2" title="登记信息">
			<emp:text id="IqpExtensionPvp.manager_id_displayname" label="责任人" required="true" readonly="false"  />
			<emp:text id="IqpExtensionPvp.manager_br_id_displayname" label="责任机构"  required="true" cssElementClass="emp_pop_common_org" />
			<emp:text id="IqpExtensionPvp.input_id_displayname" label="登记人" readonly="true" required="true" />
			<emp:text id="IqpExtensionPvp.input_br_id_displayname" label="登记机构" readonly="true" required="true"  />
			<emp:date id="IqpExtensionPvp.input_date" label="登记日期" required="true"  readonly="true" />	
			<emp:select id="IqpExtensionPvp.approve_status" label="申请状态" required="true" dictname="WF_APP_STATUS"  readonly="true" />
			<emp:text id="IqpExtensionPvp.manager_br_id" label="责任机构"  required="true" hidden="true"/>
			<emp:text id="IqpExtensionPvp.manager_id" label="责任人" required="true" hidden="true"  />
			<emp:text id="IqpExtensionPvp.input_id" label="登记人" required="true"  hidden="true" />
			<emp:text id="IqpExtensionPvp.input_br_id" label="登记机构" required="true"  hidden="true"  />
			<emp:text id="IqpExtensionPvp.prd_id" label="业务类型" hidden="true"  readonly="true" />
		</emp:gridLayout>	
	<div align="center">
		<br>
		<emp:button id="return" label="返回列表"/>
	</div>
	</emp:tab>
	<emp:ExtActTab></emp:ExtActTab>
	<!-- add by lisj 2015-9-7  需求编号：【XD150303015】关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin -->
    <%if("yes".equals(modiflag) && ((dutyNoList.indexOf("S0118")>=0) || (dutyNoList.indexOf("S0200")>=0) ||(dutyNoList.indexOf("S0226")>=0))){ %>
		<emp:tab id="CompareHis" label="与上一次修改值对比" url="getModifyHisViewPage.do" reqParams="cont_no=${context.IqpExtensionPvp.agr_no}&prd_id=${context.IqpExtensionPvp.prd_id}&modify_rel_serno=${context.modify_rel_serno}&op=his4iea&wf_flag=${context.wf_flag}&modiflag=${context.modiflag}"/>
	<%} %>
	<!-- add by lisj 2015-9-7  需求编号：【XD150303015】关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end -->
	</emp:tabGroup>
</body>
</html>
</emp:page>
