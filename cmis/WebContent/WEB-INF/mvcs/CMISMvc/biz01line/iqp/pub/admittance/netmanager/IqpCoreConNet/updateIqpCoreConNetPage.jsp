<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String flag = "";
	if(context.containsKey("flag")){
		flag = (String)context.getDataValue("flag");
	}
%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>
<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	background-color:#eee;
	text-align:left;
	width:450px;
}
</style>
<script type="text/javascript">
	/**add by lisj 2015-2-3 需求编号【HS141110017】保理业务改造  begin**/
	function doOnLoad(){
		var provider_lmt_type = IqpCoreConNet.provider_lmt_type._getValue();
		//国内保理
		if(provider_lmt_type =="4"){
			IqpCoreConNet.dealer_lmt_type._obj._renderRequired(false);
			IqpCoreConNet.oversee_mode._obj._renderRequired(false);
		}
	};
	/**add by lisj 2015-2-3 需求编号【HS141110017】保理业务改造  end**/
	
	function doReturn() {
		var flag='${context.flag}';//历史查询时，查看详情。
		if(flag=='ls'){
			var url = '<emp:url action="queryIqpCoreConNetLs.do"/>';
			url = EMPTools.encodeURI(url);
			window.location=url;
			return false;
		}else if(flag=='net'){//网络变更历史查询时
				var url = '<emp:url action="queryIqpNetAppLs.do"/>';
				url = EMPTools.encodeURI(url);
				window.location=url;
				return false;
			}
			var url = '<emp:url action="queryIqpCoreConNetList.do"/>';
			url = EMPTools.encodeURI(url);
			window.location=url;
	};
	
	function doUpdateIqpCoreConNet(){
		var form = document.getElementById("submitForm");
		var result = IqpCoreConNet._checkAll();
		if(result){
			IqpCoreConNet._toForm(form)
			toSubmitForm(form);
		}else alert("请输入必填项！");
	};
	
	function toSubmitForm(form){
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try{
					var jsonstr = eval("("+o.responseText+")");
				} catch(e){
					alert("Parse jsonstr define error!"+e);
					return;
				}
				var flag = jsonstr.flag;
				if(flag=="success"){
					alert("修改成功！");
					doReturn();
				}else{
					alert("修改失败！");
				}
			}
		};
		var handleFailure = function(o){	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);	 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData) 
	};

	function doReturn(){
		var url = '<emp:url action="queryIqpCoreConNetList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	}
	
	function getCusInfo(data){
		IqpCoreConNet.cus_id._setValue(data.cus_id._getValue());
  	   	IqpCoreConNet.cus_id_displayname._setValue(data.cus_name._getValue());
  	   	IqpCoreConNet.cdt_lvl._setValue(data.cus_crd_grade._getValue());
		var cus_id = data.cus_id._getValue();
		checkCusExistNet(cus_id);//判断客户是否已经在本行有网络
	}
    
	//判断客户是否已经在本行有网络
	function checkCusExistNet(cus_id){
		var handleSuccess = function(o){ 		
		var jsonstr = eval("(" + o.responseText + ")");
		var flag = jsonstr.flag;
			if(flag == "success" ){
				alert("该核心企业已有一个网络！");
				IqpCoreConNet.cus_id._setValue("");
				IqpCoreConNet.cus_id_displayname._setValue("");
				IqpCoreConNet.cdt_lvl._setValue("");
				IqpCoreConNet.lmt_amt._setValue("");
				IqpCoreConNet.trade_type_displayname._setValue("");
			}else{
				getRelatevalue(cus_id);
		  	}
		}
		var handleFailure = function(o){
		        alert("异步回调失败！");	
		};
		var url = '<emp:url action="checkCusExistNetRecord.do"/>?cus_id='+cus_id;
		var callback = {
				success:handleSuccess,
				failure:handleFailure
		};
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url,callback);
	}

	function getOrgID(data){
    	IqpCoreConNet.manager_br_id._setValue(data.organno._getValue());
    	IqpCoreConNet.manager_br_id_displayname._setValue(data.organname._getValue());
	}

    function setconId(data){
 	   IqpCoreConNet.manager_id_displayname._setValue(data.actorname._getValue());
 	   IqpCoreConNet.manager_id._setValue(data.actorno._getValue());
 	   IqpCoreConNet.manager_br_id._setValue(data.orgid._getValue());
 	   IqpCoreConNet.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
 	   //IqpCoreConNet.manager_br_id_displayname._obj._renderReadonly(true);
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
					IqpCoreConNet.manager_br_id._setValue(jsonstr.org);
					IqpCoreConNet.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					IqpCoreConNet.manager_br_id._setValue("");
					IqpCoreConNet.manager_br_id_displayname._setValue("");
					IqpCoreConNet.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = IqpCoreConNet.manager_id._getValue();
					IqpCoreConNet.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
   				}else if("yteam"==flag){
   					IqpCoreConNet.manager_br_id._setValue("");
   					IqpCoreConNet.manager_br_id_displayname._setValue("");
   					IqpCoreConNet.manager_br_id_displayname._obj._renderReadonly(false);
   					IqpCoreConNet.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
   				}
   			}
   		};
   		var handleFailure = function(o) {
   		};
   		var callback = {
   			success :handleSuccess,
   			failure :handleFailure
   		};
   		var manager_id = IqpCoreConNet.manager_id._getValue();
   		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
   		url = EMPTools.encodeURI(url);
   		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
   	}
	   
	function getRelatevalue(cus_id){
		var handleSuccess = function(o){
			var jsonstr = eval("(" + o.responseText + ")");
			var amt = jsonstr.amt;
			var type = jsonstr.type;
			IqpCoreConNet.lmt_amt._setValue(amt);
			IqpCoreConNet.trade_type_displayname._setValue(type);
		 }
			var handleFailure = function(o){
				alert("异步回调失败！");	
			};
			var url = '<emp:url action="getValuefromCus.do"/>?cus_id='+cus_id;
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST', url,callback);
	}
	   
	function CheckDt(){
		var NetDate = IqpCoreConNet.net_build_date._getValue();//网络建立时间
		var openDay='${context.OPENDAY}';
		if(NetDate !=null || NetDate !=""){
			var flag= CheckDate1BeforeDate2(NetDate,openDay);
			if(!flag){
				alert("网络建立日期不能大于当前日期！");
				IqpCoreConNet.net_build_date._setValue("");
				return false;
			}
		}
	}

	/**add by lisj 2015-2-3 需求编号【HS141110017】保理业务改造  begin**/
	function doChangeLTandOM(){
		var provider_lmt_type = IqpCoreConNet.provider_lmt_type._getValue();
		//国内保理
		if(provider_lmt_type =="4"){
			IqpCoreConNet.dealer_lmt_type._obj._renderRequired(false);
			IqpCoreConNet.oversee_mode._obj._renderRequired(false);
		}else{
			IqpCoreConNet.dealer_lmt_type._obj._renderRequired(true);
			IqpCoreConNet.oversee_mode._obj._renderRequired(true);
			}
	};
	/**add by lisj 2015-2-3 需求编号【HS141110017】保理业务改造  end**/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<emp:tabGroup mainTab="base_tab" id="mainTab" >
		<emp:tab label="申请基本信息" id="base_tab" needFlush="true" initial="true" >
			<emp:form id="submitForm" action="updateIqpCoreConNetRecord.do" method="POST">
				<emp:gridLayout id="IqpCoreConNetGroup" title="客户概况分析" maxColumn="2">
					<emp:text id="IqpCoreConNet.serno" label="业务编号" maxlength="32" required="true" />
						<emp:select id="IqpCoreConNet.app_type" label="申请类型" required="false" dictname="STD_ZB_NET_APP_TYPE" hidden="true"/>
						<emp:pop id="IqpCoreConNet.cus_id" label="核心企业客户码" url="queryAllCusPop.do?cusTypCondition=BELG_LINE IN('BL100','BL200') and cus_status='20'&returnMethod=getCusInfo" required="true" />
						<emp:text id="IqpCoreConNet.cus_id_displayname" label="核心企业客户名称"  required="true" readonly="true"/>
						<emp:select id="IqpCoreConNet.cdt_lvl" label="信用等级" dictname="STD_ZB_CREDIT_GRADE" required="true" readonly="true"/>
						<emp:textarea id="IqpCoreConNet.main_prd" label="主要产品" maxlength="20" required="false" colSpan="2" />
						<emp:textarea id="IqpCoreConNet.devlfore" label="发展前景" maxlength="100" required="false" colSpan="2" />
						<emp:textarea id="IqpCoreConNet.app_resn" label="申请理由" maxlength="100" required="false" colSpan="2" />
						<emp:textarea id="IqpCoreConNet.inte_income" label="综合收益概述" maxlength="100" required="false" colSpan="2" />
					</emp:gridLayout>
					<emp:gridLayout id="tradeGtoup" title="行业前景分析" maxColumn="2">
						 <emp:text id="IqpCoreConNet.trade_type_displayname" label="行业分类"   readonly="true"  required="true" cssElementClass="emp_field_text_input2"/>
						<emp:text id="IqpCoreConNet.trade_type" label="行业分类" required="false" hidden="true"/>
						<emp:textarea id="IqpCoreConNet.trade_devlfore" label="行业发展前景" maxlength="100" required="false" colSpan="2" />
					</emp:gridLayout>
					<emp:gridLayout id="LmtGroup" title="授信业务情况" maxColumn="2">
					    <emp:text id="IqpCoreConNet.lmt_amt" label="直接授信额度" maxlength="18" required="false" dataType="Currency" readonly="true" cssElementClass="emp_currency_text_readonly"/>  
						<emp:text id="IqpCoreConNet.batair_lmt_amt" label="间接授信额度" maxlength="18" required="true" dataType="Currency"  />
						<emp:text id="IqpCoreConNet.dealer_rebuyamt" label="经销商回购担保额度" maxlength="18" required="true" dataType="Currency"  />
						<emp:text id="IqpCoreConNet.buyer_rebuy_amt" label="供应商回购担保额度" maxlength="18" required="true" dataType="Currency"  />
						<emp:text id="IqpCoreConNet.fin_bail_perc" label="融资保证金比例" maxlength="16" required="true" dataType="Rate" />
						</emp:gridLayout>
					<emp:gridLayout id="CoopGroup" title="业务合作方案" maxColumn="2">
						<emp:text id="IqpCoreConNet.disp_term" label="发货期限（天）" maxlength="10" required="true" />
						<emp:date id="IqpCoreConNet.net_build_date" label="网络建立时间" required="true" />
						<emp:select id="IqpCoreConNet.coop_term_type" label="合作期限类型" required="true" dictname="STD_ZB_TERM_TYPE" />
						<emp:text id="IqpCoreConNet.coop_term" label="合作期限" maxlength="3" required="true" />
						<emp:text id="IqpCoreConNet.sale_reliterm" label="销售调剂期" maxlength="5" required="true" />
						<emp:text id="IqpCoreConNet.rebuy_cond" label="回购条件" maxlength="32" required="true" />
						<emp:text id="IqpCoreConNet.rebuy_perc" label="回购比例" maxlength="10" required="true" dataType="Percent" />
						<emp:checkbox id="IqpCoreConNet.respond_mode" label="承担责任方式" dictname="STD_ZB_RESPOND_MODE" required="true" layout="false" colSpan="2"/>
						<!-- modified by lisj 2015-2-3 需求编号【HS141110017】保理业务改造  begin -->
						<emp:checkbox id="IqpCoreConNet.provider_lmt_type" label="供应商授信业务种类" required="true" dictname="STD_PROVIDER_BIZ_TYPE" layout="false" colSpan="2" onchange="doChangeLTandOM()"/>
						<emp:checkbox id="IqpCoreConNet.dealer_lmt_type" label="经销商授信业务种类" required="true" dictname="STD_DEALER_BIZ_TYPE" layout="false" />
						<emp:checkbox id="IqpCoreConNet.oversee_mode" label="监管方式" required="true" colSpan="2" dictname="STD_ZB_OVERSEE_MODE" layout="false" />
						<!-- modified by lisj 2015-2-3 需求编号【HS141110017】保理业务改造  end -->	
						<emp:textarea id="IqpCoreConNet.memo" label="备注" maxlength="100" required="false" colSpan="2" />
						<emp:pop id="IqpCoreConNet.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" required="true" />
						<emp:pop id="IqpCoreConNet.manager_br_id_displayname" label="管理机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID"  required="true" />
						<emp:text id="IqpCoreConNet.input_id_displayname" label="登记人"  required="true" readonly="true"/>
						<emp:text id="IqpCoreConNet.input_br_id_displayname" label="登记机构"  required="true" readonly="true"/>
						<emp:text id="IqpCoreConNet.manager_id" label="责任人" maxlength="32" required="false" hidden="true"/>
						<emp:text id="IqpCoreConNet.manager_br_id" label="管理机构" maxlength="32" required="false" hidden="true"/>
						<emp:text id="IqpCoreConNet.input_id" label="登记人" maxlength="32" required="false" hidden="true"/>
						<emp:text id="IqpCoreConNet.input_br_id" label="登记机构" maxlength="32" required="false" hidden="true"/>
						<emp:date id="IqpCoreConNet.input_date" label="登记日期" required="true" defvalue="$OPENDAY" readonly="true"/>
						<emp:select id="IqpCoreConNet.approve_status" label="申请状态" required="false" dictname="WF_APP_STATUS" hidden="true"/>
						<emp:select id="IqpCoreConNet.flow_type" label="流程类型" hidden="true" defvalue="01"/>
					</emp:gridLayout>
					<div align="center">
						<br>
						<emp:button id="updateIqpCoreConNet" label="修改" op="update"/>
						<emp:button id="return" label="返回"/>
					</div>
			</emp:form>	
		</emp:tab>
	  <emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>  
</body>
</html>
</emp:page>
