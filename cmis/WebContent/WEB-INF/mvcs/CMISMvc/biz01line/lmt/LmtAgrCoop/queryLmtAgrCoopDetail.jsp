<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>合作方授信详情查询页面</title>
<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
	String type = request.getParameter("type");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryLmtAgrCoopList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function onLoad(){
		//根据共享范围 设置所属机构的显示跟隐藏 
		var share_range = LmtAgrJointCoop.share_range._getValue();
		if(share_range == 2){
			LmtAgrJointCoop.belg_org._obj._renderHidden(false);
			LmtAgrJointCoop.belg_org_displayname._obj._renderHidden(false);

			LmtAgrJointCoop.belg_org._obj._renderRequired(true);
			LmtAgrJointCoop.belg_org_displayname._obj._renderRequired(true);
		}else{
			LmtAgrJointCoop.belg_org._obj._renderHidden(true);
			LmtAgrJointCoop.belg_org_displayname._obj._renderHidden(true);
			LmtAgrJointCoop.belg_org._obj._renderRequired(false);
			LmtAgrJointCoop.belg_org_displayname._obj._renderRequired(false);
		}
		LmtAgrJointCoop.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
	}
	
	//客户信息查
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+LmtAgrJointCoop.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}

	function doClose(){
		window.close();
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onLoad()">
	<emp:tabGroup mainTab="main_tabs" id="main_tab">
		<emp:tab label="合作方额度" id="main_tabs" needFlush="true" initial="true">
			<emp:gridLayout id="LmtAgrJointCoopGroup" title="合作方授信申请" maxColumn="2">
				<emp:text id="LmtAgrJointCoop.serno" label="业务编号" maxlength="40" required="true" readonly="true" cssElementClass="emp_field_text_readonly"/>
				<emp:text id="LmtAgrJointCoop.agr_no" label="协议编号" maxlength="40" required="true" readonly="true"/>
				<emp:text id="LmtAgrJointCoop.cus_id" label="客户码" maxlength="30" required="true" readonly="true" colSpan="2"/>
				<emp:text id="LmtAgrJointCoop.cus_id_displayname" label="客户名称" required="true" readonly="true" cssElementClass="emp_field_text_long_readonly" colSpan="2"/>
				<emp:select id="LmtAgrJointCoop.coop_type" label="合作方类型 " required="true" dictname="STD_ZB_COOP_TYPE" readonly="true"/>
				<emp:select id="LmtAgrJointCoop.share_range" label="共享范围" required="true" dictname="STD_SHARED_SCOPE" readonly="true"/>
				<emp:text id="LmtAgrJointCoop.belg_org" label="所属机构" cssElementClass="emp_field_text_readonly" required="false" hidden="true" colSpan="2"/>
				<emp:textarea id="LmtAgrJointCoop.belg_org_displayname" label="所属机构"  required="false" readonly="true" hidden="true" colSpan="2"/> 
				<emp:select id="LmtAgrJointCoop.cur_type" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true" colSpan="2"/>
				<emp:text id="LmtAgrJointCoop.lmt_totl_amt" label="授信总额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="LmtAgrJointCoop.single_max_amt" label="单户限额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="LmtAgrJointCoop.lmt_bal_amt" label="授信余额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" />
				<emp:text id="LmtAgrJointCoop.froze_amt" label="冻结金额" maxlength="18" defvalue="0" dataType="Currency" cssElementClass="emp_currency_text_readonly" />
				<emp:text id="LmtAgrJointCoop.start_date" label="起始日期" required="true"  />
				<emp:text id="LmtAgrJointCoop.end_date" label="到期日期" maxlength="3" required="true" />
			</emp:gridLayout>
			<emp:gridLayout id="LmtAgrJointCoopGroup" title="登记申请" maxColumn="2">
				<emp:pop id="LmtAgrJointCoop.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
				<emp:pop id="LmtAgrJointCoop.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName"/>
				<emp:text id="LmtAgrJointCoop.input_id_displayname" label="登记人" required="true" readonly="true"/>
				<emp:text id="LmtAgrJointCoop.input_br_id_displayname" label="登记机构" required="true" readonly="true" />
				<emp:date id="LmtAgrJointCoop.input_date" label="登记日期" required="false" readonly="true"/>
				<emp:select id="LmtAgrJointCoop.agr_status" label="协议状态" required="false" dictname="STD_ZB_AGR_STATUS" readonly="true"/>
			</emp:gridLayout>
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
	<div align="center">
		<%if("surp".equals(type)){%>
			<emp:button id="close" label="关闭"/>
		<%}else{%>
			<input type="button" class="button100" onclick="doReturn(this)" value="返回到列表页面">
		<%}%>
	</div>
</body>
</html>
</emp:page>
