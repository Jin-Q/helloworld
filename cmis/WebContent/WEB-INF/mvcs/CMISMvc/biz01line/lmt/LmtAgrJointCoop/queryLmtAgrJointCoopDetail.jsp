<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	String openType = request.getParameter("openType");
	request.setAttribute("canwrite","");
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String view = "";//用来控制返回列表按钮
	if(context.containsKey("view")){
		view=(String)context.getDataValue("view");
	}
%>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doReturn() {
		var url = '<emp:url action="queryLmtAgrJointCoopList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	function doClose(){
		window.close();
	}

	//查看客户信息
	function doOnload(){
		LmtAgrJointCoop.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
		controlOrg(LmtAgrJointCoop.share_range._getValue());  //初始化机构信息显示
		//给主页签增加重载事件
		//document.getElementById("main_tabs").href="javascript:reLoad()";
	}

	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+LmtAgrJointCoop.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}

	//重加载页面
/*	function reLoad(){
		var agr_no = LmtAgrJointCoop.agr_no._getValue();
		var url = '<emp:url action="getLmtAgrJointCoopViewPage.do"/>?agr_no='+agr_no+'&view=yes&menuId=unit_team_crd_agr';
		url = EMPTools.encodeURI(url);
		window.location.href = url;
	}*/
	
	//设置共享范围机构
	function getBelgOrg(data){
		LmtAgrJointCoop.belg_org._setValue(data[0]);
		LmtAgrJointCoop.belg_org_displayname._setValue(data[1]);
	}

	/****** 共享范围与所属机构控制 *******/
	function controlOrg(_value){
		if(_value == 2){
			LmtAgrJointCoop.belg_org._obj._renderHidden(false);
			LmtAgrJointCoop.belg_org_displayname._obj._renderHidden(false);
		}else{
			LmtAgrJointCoop.belg_org._obj._renderHidden(true);
			LmtAgrJointCoop.belg_org_displayname._obj._renderHidden(true);
		}
	};
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnload()">
<emp:tabGroup mainTab="main_tabs" id="main_tabs">
	<emp:tab label="联保额度信息"  id="main_tabs" >
		<emp:gridLayout id="LmtAgrJointCoopGroup" title="联保小组信息" maxColumn="2">
			<emp:text id="LmtAgrJointCoop.serno" label="业务编号" maxlength="40" required="true" readonly="true" colSpan="2" hidden="true"/>
			<emp:text id="LmtAgrJointCoop.agr_no" label="协议编号" maxlength="40" required="true" readonly="true" colSpan="2"/>
			<emp:text id="LmtAgrJointCoop.cus_id" label="客户码" readonly="true" maxlength="40" required="true"/>
			<emp:text id="LmtAgrJointCoop.cus_id_displayname" label="客户名称" required="false" readonly="true"/>
			<emp:select id="LmtAgrJointCoop.coop_type" label="类别" required="true" dictname="STD_ZB_COOP_TYPE" colSpan="2"/>
			<emp:select id="LmtAgrJointCoop.share_range" label="共享范围" required="true" dictname="STD_SHARED_SCOPE" onchange="controlOrg(this.value)" hidden="true"/>
			<emp:pop id="LmtAgrJointCoop.belg_org" label="所属机构"  cssElementClass="emp_pop_common_org" url="queryMultiSOrgPop.do" returnMethod="getBelgOrg"  required="false"  hidden="true" colSpan="2"/>
			<emp:textarea id="LmtAgrJointCoop.belg_org_displayname" label="所属机构" readonly="true" hidden="true" colSpan="2"/>
			
		</emp:gridLayout>
		<emp:gridLayout id="LmtAgrJointCoopGroupM" title="小组额度信息" maxColumn="2">
			<emp:select id="LmtAgrJointCoop.cur_type" label="币种" required="false" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true" colSpan="2"/>
			<emp:text id="LmtAgrJointCoop.lmt_totl_amt" label="授信总额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAgrJointCoop.single_max_amt" label="单户限额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:date id="LmtAgrJointCoop.start_date" label="起始日期" required="true" cssElementClass="emp_currency_text_readonly" />
			<emp:date id="LmtAgrJointCoop.end_date" label="到期日期" required="true" cssElementClass="emp_currency_text_readonly"/>
		</emp:gridLayout>
		<emp:gridLayout id="LmtAgrJointCoopGroupM" title="登记信息" maxColumn="2">
			<emp:text id="LmtAgrJointCoop.manager_id" label="责任人" maxlength="20" hidden="true" required="true"/>
			<emp:text id="LmtAgrJointCoop.manager_br_id" label="责任机构" maxlength="20" required="true" hidden="true"/>
			<emp:text id="LmtAgrJointCoop.input_id" label="登记人" maxlength="20" required="false" defvalue="$currentUserId" readonly="true" hidden="true"/>
			<emp:text id="LmtAgrJointCoop.input_br_id" label="登记机构" maxlength="20" required="false" defvalue="$organNo" readonly="true" hidden="true"/>
			
			<emp:pop id="LmtAgrJointCoop.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setManagerId" required="true"/>
			<emp:pop id="LmtAgrJointCoop.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName"/>
			<emp:text id="LmtAgrJointCoop.input_id_displayname" label="登记人"  required="false" readonly="true"/>
			<emp:text id="LmtAgrJointCoop.input_br_id_displayname" label="登记机构"  required="false" readonly="true"/>
			
			<emp:date id="LmtAgrJointCoop.input_date" label="登记日期" required="false" readonly="true" defvalue="${context.OPENDAY}"/>
		    <emp:text id="LmtAgrJointCoop.restrict_tab" label="去除记录集权限" defvalue="Y" required="false" hidden="true"/>
		</emp:gridLayout>
			
	</emp:tab>
	<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
	<div align="center">
		<br>
		<%if("pop".equals(openType)){%>
		<emp:button id="close" label="关闭" />
		<%}else if("yes".equals(view)){ %>
		<%}else{ %>
		<input type="button" class="button100" id="btReturn" onclick="doReturn(this)" value="返回列表">
		<%} %>
	</div>
</body>
</html>
</emp:page>
