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
		var url = '<emp:url action="queryLmtAgrGrpList.do"/>&menuId=grp_crd_agr';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function onLoad(){
		LmtAgrGrp.grp_no._obj.addOneButton('viewGrpCusInfo','查看',viewGrpCusInfo);  //集团编号加查看按钮
	}

	function viewGrpCusInfo(){
		var url = "<emp:url action='queryCusGrpInfoPopDetial.do'/>&grp_no="+LmtAgrGrp.grp_no._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height='+window.screen.availHeight*0.7+',width='+window.screen.availWidth*0.7+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}		
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onLoad()">
	<emp:tabGroup mainTab="main_tabs" id="main_tab">
		<emp:tab label="协议信息" id="main_tabs">
			<emp:gridLayout id="LmtAgrGrpGroup" title="集团授信协议表" maxColumn="2">
					<emp:text id="LmtAgrGrp.serno" label="业务编号" maxlength="40" required="true" />
					<emp:text id="LmtAgrGrp.grp_agr_no" label="集团协议编号" maxlength="40" required="true" />
					<emp:text id="LmtAgrGrp.grp_no" label="集团编号" maxlength="40" required="true" />
					<emp:text id="LmtAgrGrp.grp_no_displayname" label="集团名称" required="true" />
					<emp:select id="LmtAgrGrp.biz_type" label="授信业务类型 " required="true" dictname="STD_ZB_BIZ_TYPE" />
					<emp:select id="LmtAgrGrp.cur_type" label="授信币种" required="true" dictname="STD_ZX_CUR_TYPE" />
					<emp:text id="LmtAgrGrp.crd_totl_amt" label="授信总额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
					<emp:text id="LmtAgrGrp.crd_bal_amt" label="授信余额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" hidden="true"/>
					<emp:date id="LmtAgrGrp.start_date" label="授信起始日期" required="true" />
					<emp:date id="LmtAgrGrp.end_date" label="授信到期日期" required="false" />
					<emp:textarea id="LmtAgrGrp.memo" label="备注" maxlength="200" required="false" colSpan="2" />
					<emp:text id="LmtAgrGrp.manager_id_displayname" label="责任人" required="true" />
					<emp:text id="LmtAgrGrp.manager_br_id_displayname" label="责任机构" required="true" />
					<emp:text id="LmtAgrGrp.input_id_displayname" label="登记人" required="true" />
					<emp:text id="LmtAgrGrp.input_br_id_displayname" label="登记机构" required="true" />
					<emp:date id="LmtAgrGrp.input_date" label="登记日期" required="true" />
					<emp:select id="LmtAgrGrp.agr_status" label="协议状态" required="true" dictname="STD_ZB_AGR_STATUS"/>
					
					<emp:text id="LmtAgrGrp.manager_id" label="责任人" maxlength="20" required="true" hidden="true" />
					<emp:text id="LmtAgrGrp.manager_br_id" label="责任机构" maxlength="20" required="true" hidden="true" />
					<emp:text id="LmtAgrGrp.input_id" label="登记人" maxlength="20" required="true" hidden="true" />
					<emp:text id="LmtAgrGrp.input_br_id" label="登记机构" maxlength="20" required="true" hidden="true" />
			</emp:gridLayout>
			</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
	<div align="center">
		<br>
		<input type="button" value="返回到列表页面" onclick="doReturn()" class="button100"> 
	</div>
</body>
</html>
</emp:page>
