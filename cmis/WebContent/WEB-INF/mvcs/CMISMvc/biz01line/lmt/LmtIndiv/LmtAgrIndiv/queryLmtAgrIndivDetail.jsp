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
	
	function doReturn() {
		var url = "";
		var type = '${context.type}';
		if(type=='cusTree'){
			url = '<emp:url action="queryLmtAgrIndivListForCusTree.do"/>?cus_id=${context.cus_id}&op=view&type=cusTree';
		}else{
			url = '<emp:url action="queryLmtAgrIndivList.do"/>?menuId=lmtAgrIndiv';
		}
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function doOnload(){
		LmtAgrIndiv.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
	}

	//查看客户详情
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+LmtAgrIndiv.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnload()">
	<emp:tabGroup mainTab="main_tabs" id="main_tab">
	<emp:tab label="授信基本信息" id="main_tabs" needFlush="true" initial="true">
		<emp:gridLayout id="LmtAgrIndivGroup" title="总额度" maxColumn="2">
			<emp:text id="LmtAgrIndiv.agr_no" label="协议编号" maxlength="40" required="true" />
			<emp:text id="LmtAgrIndiv.serno" label="业务编号" maxlength="40" required="true" />
			<emp:text id="LmtAgrIndiv.cus_id" label="客户码" maxlength="30" required="true" />
			<emp:text id="LmtAgrIndiv.cus_id_displayname" label="客户名称" required="true" />
			<emp:select id="LmtAgrIndiv.cur_type" label="授信币种" required="true" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="LmtAgrIndiv.crd_totl_amt" label="授信总额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAgrIndiv.crd_bal_amt" label="授信余额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" hidden="true" colSpan="2"/>
			
			<emp:text id="LmtAgrIndiv.totl_amt" label="非低风险非自助总额" maxlength="18" dataType="Currency" required="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAgrIndiv.self_amt" label="自助总额" maxlength="18" dataType="Currency" required="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAgrIndiv.crd_cir_amt" label="非低风险非自助循环额度" maxlength="18" dataType="Currency" required="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAgrIndiv.crd_one_amt" label="非低风险非自助一次性额度" maxlength="18" dataType="Currency" required="true" cssElementClass="emp_currency_text_readonly"/>
			
			<emp:text id="LmtAgrIndiv.lrisk_total_amt" label="低风险非自助总额" maxlength="18" dataType="Currency" required="true" cssElementClass="emp_currency_text_readonly" colSpan="2"/>
			<emp:text id="LmtAgrIndiv.lrisk_cir_amt" label="低风险非自助循环额度" maxlength="18" dataType="Currency" required="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAgrIndiv.lrisk_one_amt" label="低风险非自助一次性额度" maxlength="18" dataType="Currency" required="true" cssElementClass="emp_currency_text_readonly"/>
			
			<emp:date id="LmtAgrIndiv.totl_start_date" label="授信起始日" required="false" />
			<emp:date id="LmtAgrIndiv.totl_end_date" label="授信到期日" required="false" />
		</emp:gridLayout>
		
		<emp:gridLayout id="LmtAppIndivGroup" title="其他" maxColumn="2">
			<emp:textarea id="LmtAgrIndiv.inve_rst" label="调查人结论" maxlength="800" required="false" colSpan="2" />
			<emp:textarea id="LmtAgrIndiv.memo" label="备注" maxlength="800" required="false" colSpan="2" />
			<emp:select id="LmtAgrIndiv.util_mode" label="提用方式" required="false" dictname="STD_ZB_UTIL_MODE" hidden="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="LmtAppIndivGroup" title="机构信息" maxColumn="2">
			<emp:text id="LmtAgrIndiv.manager_id_displayname" label="责任人" required="true" />
			<emp:text id="LmtAgrIndiv.manager_br_id_displayname" label="责任机构"  required="true" />
			<emp:text id="LmtAgrIndiv.input_id_displayname" label="登记人"   required="true" />
			<emp:text id="LmtAgrIndiv.input_br_id_displayname" label="登记机构" required="true" />
			<emp:date id="LmtAgrIndiv.input_date" label="登记日期" required="true" />
			
			<emp:text id="LmtAgrIndiv.manager_id" label="责任人" maxlength="20" required="true" hidden="true"/>
			<emp:text id="LmtAgrIndiv.manager_br_id" label="责任机构" maxlength="20" required="true" hidden="true"/>
			<emp:text id="LmtAgrIndiv.input_id" label="登记人" maxlength="20" required="true" hidden="true"/>
			<emp:text id="LmtAgrIndiv.input_br_id" label="登记机构" maxlength="20" required="true" hidden="true"/>
			<emp:text id="LmtAgrInfo.restrict_tab" label="restrict_tab" defvalue="false" hidden="true"/>
		</emp:gridLayout>
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
