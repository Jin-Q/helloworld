<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>
<html>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String biz_area_type = (String)context.getDataValue("LmtAgrBizArea.biz_area_type");
%>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doOnload(){
		changeSharedScope();
	}
	function changeSharedScope(){
		var share_range = LmtAgrBizArea.share_range._getValue();
		if( share_range == '' || share_range == '1' ){
			LmtAgrBizArea.belg_org._obj._renderHidden(true);
			LmtAgrBizArea.belg_org_displayname._obj._renderHidden(true);
			LmtAgrBizArea.belg_org._obj._renderRequired(false);
			LmtAgrBizArea.belg_org_displayname._obj._renderRequired(false);
		}else{
			LmtAgrBizArea.belg_org_displayname._obj._renderHidden(false);
			LmtAgrBizArea.belg_org_displayname._obj._renderRequired(true);

			LmtAgrBizArea.belg_org._obj._renderHidden(false);
			LmtAgrBizArea.belg_org._obj._renderRequired(true);
		}
	}
	//责任人
	function setManagerId(data){
		LmtAppJoinBack.manager_id._setValue(data.actorno._getValue());
		LmtAppJoinBack.manager_id_displayname._setValue(data.actorname._getValue());
		LmtAppJoinBack.manager_br_id._setValue(data.orgid._getValue());
		LmtAppJoinBack.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//LmtAppJoinBack.manager_br_id_displayname._obj._renderReadonly(true);
		doOrgCheck();
	}

	function doOrgCheck(){
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("one" == flag){//客户经理只属于一个机构
					LmtAppJoinBack.manager_br_id._setValue(jsonstr.org);
					LmtAppJoinBack.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					LmtAppJoinBack.manager_br_id._setValue("");
					LmtAppJoinBack.manager_br_id_displayname._setValue("");
					LmtAppJoinBack.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = LmtAppJoinBack.manager_id._getValue();
					LmtAppJoinBack.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					LmtAppJoinBack.manager_br_id._setValue("");
					LmtAppJoinBack.manager_br_id_displayname._setValue("");
					LmtAppJoinBack.manager_br_id_displayname._obj._renderReadonly(false);
					LmtAppJoinBack.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = LmtAppJoinBack.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	//责任机构
	function getOrgID(data){
		LmtAppJoinBack.manager_br_id._setValue(data.organno._getValue());
		LmtAppJoinBack.manager_br_id_displayname._setValue(data.organname._getValue());
	}
	//返回
	function doReturn() {
		var url = '<emp:url action="queryLmtAppJoinBackList.do"/>';//?process=${context.process}';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	//修改
	function doUpdAppJoinBack(){
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("success"==flag){
					alert("保存成功！");
				}
			}
		};
		var handleFailure = function(o) {
			alert("保存失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var form = document.getElementById("submitForm");		
		var result = LmtAppJoinBack._checkAll();
		if(result){
			LmtAppJoinBack._toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}else {
           alert("请输入必填项!");
           return ;
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnload()">
	<emp:tabGroup mainTab="main_tabs" id="main_tabs">
		<emp:tab label="圈商额度信息" id="main_tabs">
		<emp:form id="submitForm" action="updateLmtAppJoinBackRecord.do" method="POST">
		<emp:gridLayout id="LmtAppJoinBackGroup" title="入圈/退圈申请表" maxColumn="2">
			<emp:text id="LmtAppJoinBack.serno" label="业务编号" maxlength="40" required="true" readonly="true"/>
			<emp:text id="LmtAppJoinBack.agr_no" label="圈商编号" maxlength="40" required="true" readonly="true"/>
			<emp:select id="LmtAppJoinBack.app_flag" label="申请标识" required="false" dictname="STD_LMT_APP_FLAG" readonly="true"/>
			<emp:select id="LmtAgrBizArea.share_range" label="共享范围" required="true" dictname="STD_SHARED_SCOPE" onchange="changeSharedScope()" readonly="true"/>
			<emp:pop id="LmtAgrBizArea.belg_org"  label="所属机构" colSpan="2" url="queryMultiSOrgPop.do" returnMethod="getOrganName" required="true" readonly="true"/>
			<emp:textarea id="LmtAgrBizArea.belg_org_displayname"  label=" " colSpan="2" readonly="true" />
			<emp:text id="LmtAgrBizArea.biz_area_name" label="圈商名称" maxlength="40" required="true" readonly="true"/>
			<emp:select id="LmtAgrBizArea.biz_area_type" label="圈商类型" required="false" dictname="STD_LMT_BIZ_AREA_TYPE" readonly="true"/>
			<emp:select id="LmtAgrBizArea.cur_type" label="授信币种" readonly="true" dictname="STD_ZX_CUR_TYPE" colSpan="2" defvalue="CNY"/>
			<emp:text id="LmtAgrBizArea.lmt_totl_amt" label="授信总额度(元)" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAgrBizArea.already_used" label="已使用额度" maxlength="18" dataType="Currency" cssElementClass="emp_currency_text_readonly" hidden="true"/>
			<emp:text id="LmtAgrBizArea.single_max_amt" label="单户限额(元)" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAgrBizArea.totl_cus" label="总户数" maxlength="16" cssElementClass="emp_currency_text_readonly"/>
			<emp:checkbox id="LmtAgrBizArea.guar_type" disabled="true" label="授信合作担保方式" dictname="STD_LMT_CRD_COOP_LN_TYPE" required="true" colSpan="2" layout="false"/>
			<emp:text id="LmtAgrBizArea.start_date" label="授信起始日期" maxlength="10" required="false" readonly="true"/>
			<emp:text id="LmtAgrBizArea.end_date" label="授信到期日期" maxlength="10" required="false" readonly="true"/>
		</emp:gridLayout>
		<%if("0".equals(biz_area_type)){ %>
		<emp:gridLayout id="LmtAgrBizAreaComnGroup" maxColumn="2" title="目标客户群（一般圈商）">
			<emp:text id="LmtAgrBizAreaComn.serno" label="业务编号" maxlength="40" required="true"  hidden="true"/>
			<emp:select id="LmtAgrBizAreaComn.shop_type" label="商户类型" required="true" dictname="STD_LMT_BIZ_TYPE" readonly="true"/>
			<emp:textarea id="LmtAgrBizAreaComn.main_prd" label="主要产品" maxlength="200" required="false" colSpan="2" readonly="true"/>
			<emp:select id="LmtAgrBizAreaComn.oper_model" label="经营规模" required="false" dictname="STD_LMT_BIZ_SIZE" readonly="true"/>
			<emp:textarea id="LmtAgrBizAreaComn.other_cond" label="其他准入条件" maxlength="200" required="false" colSpan="2" readonly="true"/>
		</emp:gridLayout>
		<%}else if("1".equals(biz_area_type)){ %>
		<emp:gridLayout id="LmtAgrBizAreaCoreGroup" maxColumn="2" title="目标客户群（核心企业）">
			<emp:text id="LmtAgrBizAreaCore.serno" label="业务编号" maxlength="40" required="true" readonly="true" hidden="true"/>
			<emp:select id="LmtAgrBizAreaCore.core_con_type" label="核心企业类型" readonly="true" dictname="STD_LMT_BIZ_TYPE" />
			<emp:text id="LmtAgrBizAreaCore.year_sale_amt" label="年供货销售额" maxlength="18" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAgrBizAreaCore.fore_debt_bal" label="平均应收(付)账款余额" maxlength="18" readonly="true" cssElementClass="emp_currency_text_readonly" dataType="Currency" />
			<emp:text id="LmtAgrBizAreaCore.coop_year" label="合作年限" maxlength="6" readonly="true" />
			<emp:textarea id="LmtAgrBizAreaCore.other_cond" label="其他准入条件" maxlength="200" readonly="true" colSpan="2" />
		</emp:gridLayout>
		<%}else{ %>
		<div  class='emp_gridlayout_title'>目标客户群（超市百货类）</div>
		<emp:table icollName="LmtAgrBizAreaSupmkList" editable="false" pageMode="false" url="">
			<emp:text id="supmk_serno" label="流水号" readonly="true" hidden="true"/>
			<emp:text id="serno" label="业务编号" hidden="true"/>
			<emp:select id="oper_trade" label="经营行业" dictname="STD_LMT_BIZ_INDUS" />
			<emp:select id="oper_model" label="经营规模" dictname="STD_LMT_BIZ_SIZE" />
			<emp:text id="trade_rank" label="行业排名" dataType="Int"/>
			<emp:text id="provid_year" label="供货年限" dataType="Int"/>
			<emp:text id="net_asset" label=" 净资产" dataType="Currency" />
			<emp:text id="other_cond" label="其他准入条件" />
		</emp:table>
		<%} %>
		<emp:gridLayout id="LmtAppJoinBackGroup" title="机构信息" maxColumn="2">
			<emp:text id="LmtAppJoinBack.manager_id" label="责任人" maxlength="20" hidden="true" required="true"/>
			<emp:text id="LmtAppJoinBack.manager_br_id" label="责任机构" maxlength="20" required="false" readonly="true" hidden="true"/>
			<emp:text id="LmtAppJoinBack.input_id" label="登记人" maxlength="20" required="false" defvalue="$currentUserId" readonly="true" hidden="true"/>
			<emp:text id="LmtAppJoinBack.input_br_id" label="登记机构" maxlength="20" required="false" defvalue="$organNo" readonly="true" hidden="true"/>
			
			<emp:pop id="LmtAppJoinBack.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setManagerId" required="true"/>
			<emp:pop id="LmtAppJoinBack.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" />
			<emp:text id="LmtAppJoinBack.input_id_displayname" label="登记人" readonly="true" defvalue="$currentUserName"/>
			<emp:text id="LmtAppJoinBack.input_br_id_displayname" label="登记机构" readonly="true" defvalue="$organName"/>
			
			<emp:text id="LmtAppJoinBack.input_date" label="登记日期" maxlength="10" readonly="true" defvalue="$OPENDAY" />
			<emp:select id="LmtAppJoinBack.approve_status" label="申请状态" required="false" dictname="WF_APP_STATUS" hidden="true" />
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="updAppJoinBack" label="修改" op="update"/>
			<emp:button id="return" label="返回列表"/>
		</div>
		</emp:form>
		</emp:tab>
	<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
		
</body>
</html>
</emp:page>
