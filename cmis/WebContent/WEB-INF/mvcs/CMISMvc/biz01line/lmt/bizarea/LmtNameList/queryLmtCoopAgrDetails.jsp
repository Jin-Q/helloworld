<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>授信台账查看页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>
<style type="text/css">
	.emp_field_textarea_readonly {
		border: 1px solid #b7b7b7;
		background-color:#eee;
		text-align: left;
		width: 450px;
		height: 50px;
	};
</style>
<script type="text/javascript">
	
	function doReturn() {
		var serno = LmtAgrDetails.serno._getValue();
		var type = '${context.type}';
		var url = "";
		if("app"==type){
			url = "<emp:url action='queryLmtAppDetailsList.do'/>&serno=${context.serno}&app_type=${context.app_type}&cus_id=${context.cus_id}&op=${context.op}&subButtonId=${context.subButtonId}";
		}else if("indiv"==type){
			url = "<emp:url action='queryLmtAgrIndivDetailsList.do'/>&agr_no=${context.LmtAgrDetails.agr_no}";
		}else{
			url = "<emp:url action='queryLmtAgrDetailsList.do'/>&subConndition=${context.subConndition}&restrict_tab=false";
		}
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doLoad(){
		var menuId = "${context.menuId}";
		if(menuId == 'indus_crd_change'||menuId == 'indus_crd_query' || menuId == "IqpMemMana"){
			document.all.btReturn.style.display = 'none';
			document.all.btClose.style.display = '';
		}else{
			document.all.btReturn.style.display = '';
			document.all.btClose.style.display = 'none';
		}

		if("N"=='${context.showButton}'){   //是否显示按钮为N
			document.all.btReturn.style.display = 'none'; 
			document.all.btClose.style.display = ''; 
		}
		
		//根据授信分项类别区分是否需要展示 核心企业责任
		var sub_type = LmtAgrDetails.sub_type._getValue();
		if("05"==sub_type){  //供应链授信
			LmtAgrDetails.core_corp_cus_id._obj._renderHidden(false);
			LmtAgrDetails.core_corp_cus_id_displayname._obj._renderHidden(false);

			LmtAgrDetails.core_corp_cus_id._obj._renderRequired(true);
			LmtAgrDetails.core_corp_cus_id_displayname._obj._renderRequired(true);
			LmtAgrDetails.core_corp_duty._obj._renderRequired(true);
			
			LmtAgrDetails.core_corp_duty._obj._renderHidden(false);
		}
		//给客户、核心企业加查看按钮
		LmtAgrDetails.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
		LmtAgrDetails.core_corp_cus_id._obj.addOneButton('viewCoreCus','查看',viewCoreCusInfo);
	}
	
	//客户信息查
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+LmtAgrDetails.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'viewCusInfo','height=600,width=1024,top=70,left=60,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}

	//核心企业客户信息查
	function viewCoreCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+LmtAgrDetails.core_corp_cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'viewCoreCusInfo','height=600,width=1024,top=70,left=60,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<emp:tabGroup mainTab="main_tabs" id="main_tab">
		<emp:tab label="授信基本信息" id="main_tabs" needFlush="true" initial="true">
		<emp:gridLayout id="LmtAgrDetailsGroup" title="授信额度台账" maxColumn="2">
			<emp:text id="LmtAgrDetails.agr_no" label="授信协议编号" maxlength="40" required="true" colSpan="2"/>
			<emp:text id="LmtAgrDetails.cus_id" label="客户码" maxlength="32" required="true" />
			<emp:text id="LmtAgrDetails.cus_id_displayname" label="客户名称" cssElementClass="emp_field_text_readonly" readonly="true"/>
			<emp:select id="LmtAgrDetails.limit_type" label="额度类型" required="true" dictname="STD_ZB_LIMIT_TYPE" />
			<emp:select id="LmtAgrDetails.sub_type" label="分项类别" readonly="true" required="true" dictname="STD_LMT_PROJ_TYPE"/>
			
			<emp:text id="LmtAgrDetails.core_corp_cus_id" label="核心企业客户码 " required="false"  hidden="true"/>
			<emp:text id="LmtAgrDetails.core_corp_cus_id_displayname" label="核心企业客户名称" required="false" cssElementClass="emp_field_text_readonly" hidden="true"/>
			<emp:select id="LmtAgrDetails.core_corp_duty" label="核心企业责任" required="false" dictname="STD_ZB_CORP_DUTY" colSpan="2" hidden="true" />
			
			<emp:text id="LmtAgrDetails.limit_code" label="授信额度编号"  required="true" readonly="true"/>
			<emp:text id="LmtAgrDetails.limit_name" label="额度品种名称" required="true" maxlength="40" cssElementClass="emp_field_text_readonly"/>
			<emp:text id="LmtAgrDetails.prd_id" label="适用产品" maxlength="200" required="true" cssElementClass="emp_field_text_readonly" />
			<emp:textarea id="LmtAgrDetails.prd_id_displayname" label="适用产品名称" required="true" colSpan="2" cssElementClass="emp_field_textarea_readonly"/>
			<emp:select id="LmtAgrDetails.cur_type" label="授信币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true"/>
			<emp:text id="LmtAgrDetails.crd_amt" label="授信金额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAgrDetails.bal_amt" label="授信余额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAgrDetails.enable_amt" label="启用金额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="LmtAgrDetails.guar_type" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" required="true" />
			<emp:select id="LmtAgrDetails.is_adj_term" label="是否调整期限" dictname="STD_ZX_YES_NO" required="true"/>
			<emp:select id="LmtAgrDetails.is_pre_crd" label="是否预授信" dictname="STD_ZX_YES_NO" required="true"/>
			<emp:select id="LmtAgrDetails.term_type" label="授信期限类型" dictname="STD_ZB_TERM_TYPE" required="true" />
			<emp:text id="LmtAgrDetails.term" label="授信期限" maxlength="2" required="true" />
			<emp:date id="LmtAgrDetails.start_date" label="授信起始日" required="true" />
			<emp:date id="LmtAgrDetails.end_date" label="授信到期日" required="true" /> 
			
			<emp:text id="LmtAgrDetails.lmt_status" label="额度状态" maxlength="2" required="false" hidden="true"/>
			<emp:text id="LmtAgrDetails.cus_type" label="客户类别" maxlength="3" required="false" hidden="true"/>
			<emp:text id="LmtAgrDetails.serno" label="申请流水号" maxlength="40" required="true" hidden="true"/>
			<emp:text id="LmtAgrDetails.bail_rate" label="保证金缴存比例" maxlength="16" required="false" hidden="true"/>
		</emp:gridLayout>
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
	<div align="center">
		<br>
		<input type="button" class="button100" id="btReturn" onclick="doReturn(this)" value="返回列表页面">
		<input type="button" class="button80" id="btClose" onclick="window.close()" value="关闭">
	</div>
</body>
</html>
</emp:page>
