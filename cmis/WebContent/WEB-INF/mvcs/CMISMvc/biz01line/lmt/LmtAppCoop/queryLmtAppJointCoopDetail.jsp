<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>合作方授信详情查询页面</title>
<jsp:include page="/include.jsp" flush="true"/>

<%
	String type = request.getParameter("type");
	String hiddReturn = request.getParameter("hiddReturn");//流程中隐藏返回按钮
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var menuId = '${context.menuId}';
		var type = '<%=type%>';
		var url = "";
		if(type!=null&&type!=""&&type!="app"&&type!="his"){
			url = '<emp:url action="queryLmtCoopFrozenList.do"/>?menuId=${context.menuId}';
		}else{
			if(menuId.indexOf("apply")>1){
				url = '<emp:url action="queryLmtAppJointCoopList.do"/>?type=app&menuId=${context.menuId}';
			}else{
				url = '<emp:url action="queryLmtAppJointCoopList.do"/>?type=his&menuId=${context.menuId}';
			}
		}
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function onLoad(){
		//根据共享范围 设置所属机构的显示跟隐藏 
		var share_range = LmtAppJointCoop.share_range._getValue();
		if(share_range == 2){
			LmtAppJointCoop.belg_org._obj._renderHidden(false);
			LmtAppJointCoop.belg_org_displayname._obj._renderHidden(false);
			LmtAppJointCoop.belg_org._obj._renderRequired(true);
			LmtAppJointCoop.belg_org_displayname._obj._renderRequired(true);
		}else{
			LmtAppJointCoop.belg_org._obj._renderHidden(true);
			LmtAppJointCoop.belg_org_displayname._obj._renderHidden(true);
			LmtAppJointCoop.belg_org._obj._renderRequired(false);
			LmtAppJointCoop.belg_org_displayname._obj._renderRequired(false);
		}
		
		LmtAppJointCoop.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
		setFrozeHiddenOrNot();
	}

	//冻结解冻字段显示隐藏
	function setFrozeHiddenOrNot(){
		var app_type = LmtAppJointCoop.app_type._getValue();
		if(app_type=="03"){
			LmtAppJointCoop.unfroze_amt._obj._renderHidden(true);

			LmtAppJointCoop.term_type._obj._renderHidden(true);
			LmtAppJointCoop.term._obj._renderHidden(true);
		}else if(app_type=="04"){
			LmtAppJointCoop.froze_amt._obj._renderHidden(true);
			LmtAppJointCoop.unfroze_amt._obj._renderRequired(true);

			LmtAppJointCoop.term_type._obj._renderHidden(true);
			LmtAppJointCoop.term._obj._renderHidden(true);
		}else{
			LmtAppJointCoop.app_type._obj._renderHidden(true);
			LmtAppJointCoop.agr_no._obj._renderHidden(true);
			LmtAppJointCoop.al_froze_amt._obj._renderHidden(true);
			LmtAppJointCoop.froze_amt._obj._renderHidden(true);
			LmtAppJointCoop.unfroze_amt._obj._renderHidden(true);
			LmtAppJointCoop.start_date._obj._renderHidden(true);
			LmtAppJointCoop.end_date._obj._renderHidden(true);
		}
	}
	
	//客户信息查
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+LmtAppJointCoop.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onLoad()">
	<emp:tabGroup mainTab="main_tabs" id="main_tab">
		<emp:tab label="合作方额度申请" id="main_tabs" needFlush="true" initial="true">
			<emp:gridLayout id="LmtAppJointCoopGroup" title="合作方授信申请" maxColumn="2">
				<emp:text id="LmtAppJointCoop.serno" label="业务编号" maxlength="40" required="true" readonly="true" />
				<emp:text id="LmtAppJointCoop.agr_no" label="协议编号" maxlength="40" required="true" readonly="true" />
				<emp:select id="LmtAppJointCoop.app_type" label="申请类型 " required="true" dictname="STD_ZB_APP_TYPE" readonly="true"/>
				<emp:text id="LmtAppJointCoop.cus_id" label="客户码" maxlength="30" required="true" readonly="true" colSpan="2"/>
				<emp:text id="LmtAppJointCoop.cus_id_displayname" label="客户名称"  required="true" readonly="true" cssElementClass="emp_field_text_long_readonly" colSpan="2"/>
				<emp:select id="LmtAppJointCoop.coop_type" label="合作方类型 " required="true" dictname="STD_ZB_COOP_TYPE" readonly="true"/>
				<emp:select id="LmtAppJointCoop.share_range" label="共享范围" required="true" dictname="STD_SHARED_SCOPE" readonly="true"/>
				<emp:text id="LmtAppJointCoop.belg_org" label="所属机构" cssElementClass="emp_field_text_readonly" required="false" hidden="true" colSpan="2"/>
				<emp:textarea id="LmtAppJointCoop.belg_org_displayname" label="所属机构"  required="false" readonly="true" hidden="true" colSpan="2"/> 
				<emp:select id="LmtAppJointCoop.cur_type" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true" colSpan="2"/>
				<emp:text id="LmtAppJointCoop.lmt_totl_amt" label="授信总额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="LmtAppJointCoop.single_max_amt" label="单户限额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				
				<emp:text id="LmtAppJointCoop.al_froze_amt" label="已冻结金额" maxlength="18" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="LmtAppJointCoop.froze_amt" label="冻结金额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="LmtAppJointCoop.unfroze_amt" label="解冻金额" maxlength="18" dataType="Currency" cssElementClass="emp_currency_text_readonly" colSpan="2"/>
				
				<emp:select id="LmtAppJointCoop.term_type" label="期限类型" required="true" dictname="STD_ZB_TERM_TYPE" />
				<emp:text id="LmtAppJointCoop.term" label="期限" maxlength="3" required="true" />
				
				<emp:date id="LmtAppJointCoop.start_date" label="起始日期" readonly="true"/>
				<emp:date id="LmtAppJointCoop.end_date" label="到期日期" readonly="true"/>
				
			</emp:gridLayout>
			<emp:gridLayout id="LmtAppJointCoopGroup" maxColumn="2" title="登记信息">
				<emp:pop id="LmtAppJointCoop.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
				<emp:pop id="LmtAppJointCoop.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName"/>
				<emp:text id="LmtAppJointCoop.manager_id" label="责任人" required="true" hidden="true"/>
				<emp:text id="LmtAppJointCoop.manager_br_id" label="责任机构" required="true" hidden="true"/>
				
				<emp:text id="LmtAppJointCoop.input_id" label="登记人" maxlength="20" required="true" defvalue="${context.currentUserId}" hidden="true"/>
				<emp:text id="LmtAppJointCoop.input_br_id" label="登记机构" maxlength="20" required="true" defvalue="${context.organNo}" hidden="true" />
				<emp:text id="LmtAppJointCoop.input_id_displayname" label="登记人" required="true" readonly="true"/>
				<emp:text id="LmtAppJointCoop.input_br_id_displayname" label="登记机构" required="true" readonly="true" />
				<emp:date id="LmtAppJointCoop.input_date" label="登记日期" required="false" defvalue="${context.OPENDAY}" readonly="true"/>
				<emp:select id="LmtAppJointCoop.approve_status" label="申请状态" required="false" dictname="WF_APP_STATUS" readonly="true"/>
				<emp:date id="LmtAppJointCoop.app_date" label="申请日期" required="true" readonly="true" defvalue="${context.OPENDAY}" hidden="true"/>
				<emp:date id="LmtAppJointCoop.over_date" label="办结日期" required="false" hidden="true"/>
				<emp:text id="LmtAppJointCoop.flow_type" label="流程类型" required="false" hidden="true" maxlength="20"/>
			</emp:gridLayout>
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
	<div align="center">
	<%if(hiddReturn==null||"".equals(hiddReturn)){ %>
		<input type="button" class="button100" onclick="doReturn(this)" value="返回列表页面">
	<%} %>
	</div>
</body>
</html>
</emp:page>
