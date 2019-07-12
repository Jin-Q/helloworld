<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String query = "";
	if(context.containsKey("query")){
		query = (String)context.getDataValue("query");
	}
%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>
<style type="text/css">
.emp_field_textarea_textarea1 { /****** 长度、高度固定 ******/
	width: 600px;
	height: 100px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}

.emp_input{
	width: 600px;
	height: 50px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}
</style>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
%>

<script type="text/javascript">

	function doReturn() {
		var url = '<emp:url action="queryPrdBasicinfoList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	function getPrdRepayModeList4Prd(){
		var prdid = PrdBasicinfo.prdid._getValue();
		if(prdid==""){
			return false;
		}
		var url = '<emp:url action="getPrdRepayModeList4Prd.do"/>?prdid='+prdid;
		url = EMPTools.encodeURI(url);
		PLoanTypMtdList._obj.ajaxQuery(url,null);
	};
	function doOnLoad(){
		getPrdRepayModeList4Prd();
	};
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()" >
		<emp:tabGroup mainTab="base_tab" id="mainTab" >
			<emp:tab label="产品基本信息" id="base_tab" needFlush="true" initial="true" >
				<emp:gridLayout id="" maxColumn="2" title="产品基本信息">
					<emp:text id="PrdBasicinfo.prdname" label="产品名称" maxlength="40" required="false" readonly="true" />
					<emp:text id="PrdBasicinfo.prdid" label="产品编号" maxlength="6" required="true" readonly="true" />
					<emp:text id="PrdBasicinfo.supcatalog_displayname" label="上级目录"  required="false"/>
					
					<emp:text id="PrdBasicinfo.prdmanager" label="产品经理" maxlength="30" required="false" readonly="true" />
					<emp:select id="PrdBasicinfo.prdstatus" label="产品状态" dictname="STD_PRD_STATE" readonly="true" />
					<emp:date id="PrdBasicinfo.startdate" label="生效日期" required="false" />
					<emp:date id="PrdBasicinfo.enddate" label="到期日期" required="false" />
					<emp:text id="PrdBasicinfo.prdversion" label="产品版本号"  maxlength="10"  required="false" />
					<emp:text id="PrdBasicinfo.inputid_displayname" label="登记人员"  readonly="true" required="false" />
					<emp:text id="PrdBasicinfo.inputdate" label="登记日期" maxlength="10" required="false" readonly="true" />
					<emp:text id="PrdBasicinfo.orgid_displayname" label="登记机构"  required="false" readonly="true"/>
					<emp:checkbox id="PrdBasicinfo.prdowner" label="产品归属" required="false" dictname="STD_ZB_PRDLINE" layout="false" colSpan="2" />
					<emp:textarea id="PrdBasicinfo.prddescribe" label="产品描述" maxlength="200" required="false" />
				    
				    <emp:text id="PrdBasicinfo.supcatalog" label="上级目录" maxlength="100" required="false" readonly="true" hidden="true"/>
				    <emp:text id="PrdBasicinfo.inputid" label="登记人员" maxlength="30" readonly="true" required="false" hidden="true"/>
				    <emp:text id="PrdBasicinfo.orgid" label="登记机构" maxlength="20" required="false" readonly="true" hidden="true"/>
				</emp:gridLayout>
				<emp:gridLayout id="" maxColumn="2" title="产品配置表">
					<emp:checkbox id="PrdBasicinfo.currency" label="可用币种" dictname="STD_ZX_CUR_TYPE" layout="false" colSpan="2" required="false" />
					<emp:checkbox id="PrdBasicinfo.guarway" label="可用担保方式" dictname="STD_ZB_ASSURE_MEANS" layout="false" colSpan="2" required="false" />
					<emp:checkbox id="PrdBasicinfo.canFeeCode" label="可用费用代码" dictname="STD_ZB_FEE_CODE" layout="false" colSpan="2" required="false" />
					<emp:checkbox id="PrdBasicinfo.needFeeCode" label="必需费用代码" dictname="STD_ZB_FEE_CODE" layout="false" colSpan="2" required="false" />
					<emp:pop id="PrdBasicinfo.preventtactics" label="拦截策略" url=""  required="false" />
					<emp:text id="PrdBasicinfo.loanform" label="申请表单"  required="false" />
					<emp:text id="PrdBasicinfo.contform" label="合同表单" required="false" />
					<emp:text id="PrdBasicinfo.pvpform" label="出账表单" required="false" />
					<emp:select id="PrdBasicinfo.loanflow" label="申请流程" dictname="ZB_BIZ_CATE"  required="false" />
					<emp:select id="PrdBasicinfo.pvpway" label="出账方式" required="false" dictname="STD_ZB_PUTOUT_TYPE" />
					<emp:select id="PrdBasicinfo.payflow" label="放款流程" dictname="ZB_BIZ_CATE" required="false" />
					<emp:pop id="PrdBasicinfo.policytactics" label="政策策略" flat="flase" url="#" />
					<emp:text id="PrdBasicinfo.businessrule" label="业务规则" maxlength="40" required="false" />
					<emp:text id="PrdBasicinfo.costset" label="费用设置" maxlength="20" required="false" />
					<emp:text id="PrdBasicinfo.repayway" label="还款方式设置" hidden="true" maxlength="20" required="false" />
					<emp:text id="PrdBasicinfo.datacollection" label="资料收集" maxlength="40" required="false" />
					<emp:textarea id="PrdBasicinfo.contmapping" label="合同映射"  required="false" colSpan="2" cssElementClass="emp_field_textarea_textarea1"/>
					<emp:textarea id="PrdBasicinfo.pvpmapping" label="出账映射" required="false" colSpan="2" cssElementClass="emp_field_textarea_textarea1"/>
					<emp:textarea id="PrdBasicinfo.comments" label="备注" maxlength="200" required="false" cssElementClass="emp_input"/>
				</emp:gridLayout>
				
				<div  class='emp_gridlayout_title'>还款方式设置</div>
				<div id="tempButton" style="display:${param.optype}" >
				</div>
				<emp:table icollName="PLoanTypMtdList" pageMode="true" url="getPrdRepayModeList4Prd.do" reqParams="prdid=${context.PrdBasicinfo.prdid}">
					<emp:text id="repay_mode_id" label="还款方式代码" />
		            <emp:text id="repay_mode_type" label="还款方式种类" dictname="STD_ZB_REPAY_MODE" />  
					<emp:text id="min_term" label="支持最小期限(月)" />
					<emp:text id="max_term" label="支持最大期限(月)" />
					<emp:text id="repay_interval" label="还款间隔" hidden="true" />
					<emp:text id="firstpay_perc" label="首付比例" dataType="Rate"/>
					<emp:text id="lastpay_perc" label="尾付比例" dataType="Rate"/>
					<emp:text id="is_instm" label="是否期供类" dictname="STD_ZX_YES_NO" />
					<emp:text id="repay_mode_dec" label="还款方式描述" />
				</emp:table>
		
			</emp:tab>
			<emp:tab label="产品适用机构配置" url="getPrdOrgApplyListByPrdPk.do?prdId=${context.PrdBasicinfo.prdid}&query=${context.query}" id="org_tab" initial="false" needFlush="true">
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
