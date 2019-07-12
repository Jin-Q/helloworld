<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = "";
		var type = '${context.type}';
		if(type=='cusTree'){
			url = '<emp:url action="queryLmtAppIndivListForCusTree.do"/>?cus_id=${context.cus_id}&op=view&type=cusTree';
		}else{
			var menuId = '${context.menuId}';
			if(menuId=='lmtindivapp'){
				url = '<emp:url action="queryLmtAppIndivList.do"/>?type=app&menuId=${context.menuId}';
			}else{
				url = '<emp:url action="queryLmtAppIndivList.do"/>?type=his&menuId=${context.menuId}';
			}
		}
		
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	//控制共同债务人隐藏
	function doOnload(){
		LmtAppIndiv.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
		
		checkOrgOrNot();
		//modified by yangzy 2015/06/18 个人额度流程中影像查看 start
		//var showButton = '${context.showButton}';  //是否显示返回按钮
		//if("N"==showButton || "n"==showButton){
		//	document.getElementById("showbutton").style.display="none";
		//}
		//modified by yangzy 2015/06/18 个人额度流程中影像查看 end
		/**add by lisj 2015-5-7 需求编号：【XD150407025】分支机构授信审批权限配置(社区支行) begin**/
		var is_comm_branch = LmtAppIndiv.is_comm_branch._getValue();
		if(is_comm_branch !=null && is_comm_branch =='1'){
			LmtAppIndiv.comm_branch_name._obj._renderHidden(false);
			LmtAppIndiv.comm_branch_name._obj._renderRequired(true);
		}
		/**add by lisj 2015-5-7 需求编号：【XD150407025】分支机构授信审批权限配置(社区支行) end**/
	}

	//查看客户详情
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+LmtAppIndiv.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}

	//控制变更时字段的显示
	function checkOrgOrNot(){
		var app_type = LmtAppIndiv.app_type._getValue();
		if("01"==app_type){    //如果是授信变更，显示原有额度情况   
			LmtAppIndiv.org_crd_totl_amt._obj._renderHidden(true);
		}else{
			//LmtAppIndiv.is_adj_term_totl._obj._renderRequired(true);
			//LmtAppIndiv.term_type._obj._renderHidden(true);
			//LmtAppIndiv.term._obj._renderHidden(true); 
		}
	} ;

	function doImageView(){	//客户信息影像查看
		var data = new Array();
		data['serno'] = LmtAppIndiv.cus_id._getValue();	//客户资料的业务编号就填cus_id
		data['cus_id'] = LmtAppIndiv.cus_id._getValue();	//客户编号
		data['prd_id'] = 'BASIC';	//业务品种
		data['prd_stage'] = '' ;	//业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = 'View23'	;//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnload()">
	<emp:tabGroup mainTab="main_tabs" id="main_tab">
	<emp:tab label="授信基本信息" id="main_tabs" needFlush="true" initial="true">
		<emp:gridLayout id="LmtAppIndivGroup" title="授信信息" maxColumn="2">
			<emp:text id="LmtAppIndiv.serno" label="业务编号" maxlength="40" required="true" readonly="true" />
			<emp:select id="LmtAppIndiv.biz_type" label="授信业务类型 ：内部授信/公开授信" required="true" dictname="STD_ZB_BIZ_TYPE" defvalue="01" hidden="true"/>
			<emp:text id="LmtAppIndiv.cus_id" label="客户码" required="true" readonly="true"/>
			<emp:text id="LmtAppIndiv.cus_id_displayname" label="客户名称"   required="true" readonly="true"/>
			<emp:select id="LmtAppIndiv.cus_type" label="客户类型" required="true" dictname="STD_ZB_CUS_TYPE" readonly="true"/>
			<emp:select id="LmtAppIndiv.app_type" label="申请类型" defvalue="01" required="true" dictname="STD_ZB_APP_TYPE" readonly="true"/>
			<emp:select id="LmtAppIndiv.cur_type" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true" colSpan="2"/>
			<emp:text id="LmtAppIndiv.org_crd_totl_amt" label="原授信总额" maxlength="18" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAppIndiv.crd_totl_amt" label="授信总额" maxlength="18" dataType="Currency" required="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAppIndiv.totl_amt" label="非自助总额" maxlength="18" dataType="Currency" required="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAppIndiv.self_amt" label="自助总额" maxlength="18" dataType="Currency" required="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAppIndiv.crd_cir_amt" label="非自助循环额度" maxlength="18" dataType="Currency" required="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAppIndiv.crd_one_amt" label="非自助一次性额度" maxlength="18" dataType="Currency" required="true" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="LmtAppIndiv.lrisk_type" label="低风险业务类型" required="true" dictname="STD_ZB_LRISK_TYPE" readonly="true"/>
			<!-- add by lisj 2015-4-14 需求编号：【XD150407025】分支机构授信审批权限配置 begin -->
			<emp:select id="LmtAppIndiv.is_overdue" label="逾期或欠息，垫款" required="true" dictname="STD_ZX_YES_NO" colSpan="2"/>
			<!-- add by lisj 2015-4-14 需求编号：【XD150407025】分支机构授信审批权限配置 end -->
			
			<emp:date id="LmtAppIndiv.totl_start_date" label="授信起始日" readonly="true" hidden="true"/>
			<emp:date id="LmtAppIndiv.totl_end_date" label="授信到期日" readonly="true" hidden="true"/>
			<emp:select id="LmtAppIndiv.is_adj_term_totl" label="是否调整期限" dictname="STD_ZX_YES_NO" onchange="showTerm()" colSpan="2" hidden="true"/>
			<emp:select id="LmtAppIndiv.term_type" label="期限类型" dictname="STD_ZB_TERM_TYPE" defvalue="003" hidden="true"/>
			<emp:text id="LmtAppIndiv.term" label="期限" maxlength="3" dataType="Int" defvalue="0" hidden="true"/>
		</emp:gridLayout>

		<emp:gridLayout id="LmtAppIndivGroup" title="其他" maxColumn="2">
			<emp:textarea id="LmtAppIndiv.inve_rst" label="调查人结论" maxlength="800" required="true" colSpan="2" />
			<emp:textarea id="LmtAppIndiv.memo" label="备注" maxlength="800" colSpan="2" />
			<emp:select id="LmtAppIndiv.belg_line" label="所属条线" required="false" dictname="STD_ZB_BUSILINE" hidden="true"/>
			<emp:select id="LmtAppIndiv.util_mode" label="提用方式" required="false" dictname="STD_ZB_UTIL_MODE" hidden="true"/>
		</emp:gridLayout>
		<emp:gridLayout id="LmtAppIndivGroup" title="机构信息" maxColumn="2">
			<emp:pop id="LmtAppIndiv.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" />
			<emp:pop id="LmtAppIndiv.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" />
			<!-- add by lisj 2015-5-7  需求编号：【XD150407025】分支机构授信审批权限配置 begin -->
			
			<emp:text id="LmtAppIndiv.comm_branch_name" label="社区支行名称" required="false" hidden="true"/>
			<!-- add by lisj 2015-5-7  需求编号：【XD150407025】分支机构授信审批权限配置 end -->
			<emp:text id="LmtAppIndiv.input_id_displayname" label="登记人"  required="true" defvalue="$currentUserName" readonly="true"/>
			<emp:text id="LmtAppIndiv.input_br_id_displayname" label="登记机构"   required="true" defvalue="$organName" readonly="true"/>
			<emp:date id="LmtAppIndiv.input_date" label="登记日期" required="true" defvalue="${context.OPENDAY}" readonly="true"/>
		
			<emp:text id="LmtAppIndiv.manager_id" label="责任人" maxlength="20" required="true" hidden="true"/>
			<emp:text id="LmtAppIndiv.manager_br_id" label="责任机构" maxlength="20" required="true" hidden="true" />
			<!-- add by lisj 2015-5-7  需求编号：【XD150407025】分支机构授信审批权限配置 begin -->
			<emp:text id="LmtAppIndiv.comm_branch_id" label="社区支行机构编码" maxlength="20" required="false" hidden="true" />
			<!-- add by lisj 2015-5-7  需求编号：【XD150407025】分支机构授信审批权限配置 end -->
			<emp:text id="LmtAppIndiv.input_id" label="登记人" maxlength="20" required="true" defvalue="$currentUserId" hidden="true"/>
			<emp:text id="LmtAppIndiv.input_br_id" label="登记机构" maxlength="20" required="true" defvalue="$organNo" hidden="true"/>
			<emp:select id="LmtAppIndiv.approve_status" label="申请状态" required="true" dictname="WF_APP_STATUS" defvalue="000" hidden="true"/>
			<emp:date id="LmtAppIndiv.app_date" label="申请日期" required="true" hidden="true" defvalue="$OPENDAY"/>
			<emp:select id="LmtAppIndiv.flow_type" label="流程类型" required="true" dictname="STD_ZB_FLOW_TYPE" hidden="true" defvalue="01"/>
		</emp:gridLayout>
	
	</emp:tab>
	<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
	<div align="center" id="showbutton" style="display: ''">
		<div align="center">
			<br>
			<!-- modified by yangzy 2015/06/18 个人额度流程中影像查看 begin -->
			<%
				String showButton=request.getParameter("showButton");
				if(showButton==null || (!"N".equals(showButton) && !"n".equals(showButton))){
			%>
			<input type="button" class="button100" onclick="doReturn(this)" value="返回列表页面">
			<%
				}
			%>
			<!-- modified by yangzy 2015/06/18 个人额度流程中影像查看 end -->
			<%-- <emp:button id="ImageView" label="影像查看"/> --%>
		</div>
	</div>
</body>
</html>
</emp:page>
