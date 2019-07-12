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
	/*--user code begin--*/
	function doReturn() {
		var url = '<emp:url action="queryLmtRediApplyList.do"/>?type=app&menuId=${context.menuId}';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function onLoad(){
		LmtRediApply.cus_id._obj.addOneButton('viewCusInfo','查看',viewCusInfo);

		var app_type = '${context.LmtRediApply.app_type}';
		if("05"==app_type){    //如果是授信变更，显示原有额度情况   
			LmtRediApply.org_crd_totl_amt._obj._renderHidden(true);
			LmtRediApply.org_crd_cir_amt._obj._renderHidden(true);
			LmtRediApply.org_crd_one_amt._obj._renderHidden(true);
		}
	}

	//查看客户综合信息
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+LmtRediApply.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height=600,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}
	/*--user code end--*/
</script>
</head>
<body class="page_content" onload="onLoad()">
	<emp:tabGroup mainTab="main_tabs" id="main_tab">
		<emp:tab label="授信基本信息" id="main_tabs" needFlush="true" initial="true">
			<emp:gridLayout id="LmtRediApplyGroup" title="授信申请信息" maxColumn="2">
				<emp:text id="LmtRediApply.serno" label="业务编号" maxlength="40" required="true" />
				<emp:text id="LmtRediApply.grp_serno" label="集团授信编号" maxlength="40" required="false" />
				<emp:select id="LmtRediApply.app_type" label="申请类型" required="true" dictname="STD_ZB_APP_TYPE" colSpan="2"/>
				<emp:text id="LmtRediApply.cus_id" label="客户码" maxlength="32" required="true" />
				<emp:text id="cus_name_displayname" label="客户名称" readonly="true" cssElementClass="emp_field_text_readonly" defvalue="${context.LmtRediApply.cus_id_displayname}"/>
				<emp:select id="LmtRediApply.biz_type" label="授信业务类型 " required="true" dictname="STD_ZB_BIZ_TYPE" />
				<emp:select id="LmtRediApply.cur_type" label="授信币种" required="true" dictname="STD_ZX_CUR_TYPE" />
				<emp:text id="LmtRediApply.org_crd_totl_amt" label="原授信总额" maxlength="18" required="true" dataType="Currency" colSpan="2" readonly="true" defvalue="0.00" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="LmtRediApply.org_crd_cir_amt" label="原循环授信敞口" maxlength="18" required="true" dataType="Currency" readonly="true" defvalue="0.00" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="LmtRediApply.org_crd_one_amt" label="原一次性授信敞口" maxlength="18" required="true" dataType="Currency" readonly="true" defvalue="0.00" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="LmtRediApply.crd_totl_amt" label="授信总额" maxlength="18" required="true" dataType="Currency" colSpan="2" readonly="readonly" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="LmtRediApply.crd_cir_amt" label="循环授信敞口" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="LmtRediApply.crd_one_amt" label="一次性授信敞口" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<emp:select id="LmtRediApply.flow_type" label="流程类型" required="true" dictname="STD_ZB_FLOW_TYPE" colSpan="2"/>
				<emp:text id="LmtRediApply.app_date" label="申请日期" maxlength="10" required="true" />
				<emp:text id="LmtRediApply.over_date" label="办结日期" maxlength="10" required="false" />
				<emp:textarea id="LmtRediApply.memo" label="备注" maxlength="200" required="false" colSpan="3" />
				<emp:text id="LmtRediApply.manager_id_displayname" label="责任人"  required="true" />
				<emp:text id="LmtRediApply.manager_br_id_displayname" label="责任机构"  required="true" />
				<emp:text id="LmtRediApply.input_id_displayname" label="登记人"  required="true" />
				<emp:text id="LmtRediApply.input_br_id_displayname" label="登记机构"  required="true" />
				<emp:text id="LmtRediApply.input_date" label="登记日期" maxlength="10" required="true" />
				<emp:select id="LmtRediApply.approve_status" label="申请状态" required="true" dictname="WF_APP_STATUS" />
				<emp:text id="LmtRediApply.restrict_tab" label="restrict_tab" defvalue="false" hidden="true"/>
			</emp:gridLayout>
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
	<div align="center" id="showbutton" >
		<input type="button" class="button100" onclick="doReturn(this)" value="返回列表页面">
	</div>
</body>

</html>
</emp:page>
