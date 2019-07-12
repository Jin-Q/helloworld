<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%><emp:page>
<html>
<head>
<title>修改页面</title>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String app_type = context.getDataValue("LmtApply.app_type").toString();
	String type = request.getParameter("type");
%>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	/*--user code begin--*/
	/**选择POP框用户后自动赋值客户码及客户名称*/
	function setCusDatas(data){
		LmtApply.cus_id._setValue(data.cus_id._getValue());
		cus_name_displayname._setValue(data.cus_name._getValue());
	}

	/**计算授信额度=循环额度+一次性额度 */
	function computeCrdAmt(){
		var crd_cir_amt = LmtApply.crd_cir_amt._getValue();
		var crd_one_amt = LmtApply.crd_one_amt._getValue();
		var total_amt = 0.00;
		if(null!=crd_cir_amt && ""!=crd_cir_amt){
			total_amt += parseFloat(crd_cir_amt);
		}
		if(null!=crd_one_amt && ""!=crd_one_amt){
			total_amt += parseFloat(crd_one_amt);
		}
		LmtApply.crd_totl_amt._setValue(parseFloat(total_amt)+"");
	}

	//返回主管客户经理	
	function setconId(data){
		LmtApply.manager_id._setValue(data.actorno._getValue());
		LmtApply.manager_id_displayname._setValue(data.actorname._getValue());
		LmtApply.manager_br_id._setValue(data.orgid._getValue());
		LmtApply.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//LmtApply.manager_br_id_displayname._obj._renderReadonly(true);
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
					LmtApply.manager_br_id._setValue(jsonstr.org);
					LmtApply.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					LmtApply.manager_br_id._setValue("");
					LmtApply.manager_br_id_displayname._setValue("");
					LmtApply.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = LmtApply.manager_id._getValue();
					LmtApply.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					LmtApply.manager_br_id._setValue("");
					LmtApply.manager_br_id_displayname._setValue("");
					LmtApply.manager_br_id_displayname._obj._renderReadonly(false);
					LmtApply.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = LmtApply.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	//返回主管机构
	function getOrganName(data){
		LmtApply.manager_br_id._setValue(data.organno._getValue());
		LmtApply.manager_br_id_displayname._setValue(data.organname._getValue());
	}

	function onLoad(){
		var action = '${context.operate}';   //新增跟修改共用同一个页面，从后台绑定action 
		var form = document.getElementById('submitForm');
		form.action = action;
		addOneButton();
		//给主页签增加重载事件
		document.getElementById("main_tabs").href="javascript:reLoad()";

		//若不是‘待发起’状态，主管机构、主管客户经理不允许修改
		var app_status = LmtApply.approve_status._getValue();
		if(app_status!='000'){
			LmtApply.manager_id_displayname._obj._renderReadonly(true);
			LmtApply.manager_br_id_displayname._obj._renderReadonly(true);
		}
		/**modified by lisj 2014-11-26需求编号：【XD141107075】 授信流程关联关系信息改造  begin**/
		//校验客户关联关系信息
		doCheckCusRelInfo();
		/**modified by lisj 2014-11-26 需求编号：【XD141107075】授信流程关联关系信息改造  end**/
	}

	function addOneButton(){
		LmtApply.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
	}

	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+LmtApply.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'viewCusInfo','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}

	//异步提交申请数据
	function doSubmitLmtApply(){
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var message = jsonstr.message;
				if("Y"==message){
					alert("保存成功！");
					document.getElementById("button_submitLmtApply").disabled = "";
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
		var result = LmtApply._checkAll();
		if(result){
			LmtApply._toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			document.getElementById("button_submitLmtApply").disabled = "true";
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
		}else {
           alert("请输入必填项!");
           return ;
		}
	};

	//重加载页面
	function reLoad(){
		var serno = LmtApply.serno._getValue(); 
		var url = '<emp:url action="getLmtApplyUpdatePage.do"/>?serno='+serno+'&op=update&isShow=N&menuId=corp_crd_apply';
		url = EMPTools.encodeURI(url);
		window.location.href = url;
	}

	function doReturn() {
		var url = "";
		//var type = '<%=type%>';
		var grp_serno = LmtApply.grp_serno._getValue();
		if(null != grp_serno && "" != grp_serno){
			url = '<emp:url action="queryLmtMemberAppList.do"/>?serno='+grp_serno+'&menuId=grp_crd_apply&subMenuId=LmtMemberApp&op=${context.op}';
		}else{
			url = '<emp:url action="queryLmtApplyList.do"/>?type=app&menuId=${context.menuId}';
		}
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	//关联关系搜索
	function doViewCusRelInfo(){
		var cusId = LmtApply.cus_id._getValue();
		var url = '<emp:url action="GetCusRelTreeOp.do"/>?cus_id='+LmtApply.cus_id._getValue();
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height=600,width=800,top=30,left=30,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	/**modified by lisj 2014-11-26需求编号：【XD141107075】 授信流程关联关系信息改造  begin**/
	function doCheckCusRelInfo(){
		var cusId  = LmtApply.cus_id._getValue();
		if(cusId != null && cusId != ""){
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					if(flag == "existence"){
						if(confirm("该客户存在我行存在关联信息，请点击【确定】进行查看！")){
							var url = '<emp:url action="GetCusRelTreeOp.do"/>?cus_id='+cusId;
							url = EMPTools.encodeURI(url);
							window.open(url,'newwindow','height=600,width=800,top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
						}
					}
				}
			}; 
			var handleFailure = function(o){
				alert("异步请求出错！");	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var url = "<emp:url action='ckeckCusRelInfoOp.do'/>?cus_id="+cusId;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}	
	};
	/**modified by lisj 2014-11-26需求编号：【XD141107075】 授信流程关联关系信息改造  end**/
	/*--user code end--*/
</script>
</head>
<body class="page_content" onload="onLoad()">
	<emp:tabGroup mainTab="main_tabs" id="main_tab">
		<emp:tab label="授信基本信息" id="main_tabs" needFlush="true" initial="true">
			<emp:form id="submitForm" action="updateLmtApplyRecord.do" method="POST">
				<emp:gridLayout id="LmtApplyGroup" title="单一法人授信申请" maxColumn="2">
					<emp:text id="LmtApply.serno" label="业务编号" maxlength="40" required="false" readonly="true" />
					<emp:select id="LmtApply.app_type" label="申请类型" required="false" dictname="STD_ZB_APP_TYPE" readonly="true" />
					<!-- add by lisj 2015-3-23 需求编号：【XD150407025】分支机构授信审批权限配置 begin-->
					<emp:select id="LmtApply.is_overdue" label="逾期或欠息，垫款" required="true" dictname="STD_ZX_YES_NO" colSpan="2"/>
			    	<!-- add by lisj 2015-3-23 需求编号：【XD150407025】分支机构授信审批权限配置 end -->
					<emp:text id="LmtApply.cus_id" label="客户码" required="false" readonly="true"/>
					<emp:select id="LmtApply.biz_type" label="授信业务类型" required="false" dictname="STD_ZB_BIZ_TYPE" defvalue="01" readonly="true"/>
					<emp:text id="cus_name_displayname" label="客户名称" readonly="false" cssElementClass="emp_field_text_long_readonly" colSpan="2"  defvalue="${context.LmtApply.cus_id_displayname}"/> 
					<emp:select id="LmtApply.lrisk_type" label="低风险业务类型" required="true" dictname="STD_ZB_LRISK_TYPE" readonly="true" defvalue="20"/>
					<emp:select id="LmtApply.lmt_type" label="授信类别" required="false" dictname="STD_ZX_LMT_PRD" readonly="true" />
					<emp:select id="LmtApply.cur_type" label="授信币种" required="false" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true"/>
					<% if("02".equals(app_type)){  //如果是授信变更，显示原有额度情况      %>
					<emp:text id="LmtApply.org_crd_totl_amt" label="原授信总额" maxlength="18" required="false" dataType="Currency" colSpan="2" readonly="true" defvalue="0.00" cssElementClass="emp_currency_text_readonly" />
					<emp:text id="LmtApply.org_crd_cir_amt" label="原循环授信敞口" maxlength="18" required="false" dataType="Currency" readonly="true" defvalue="0.00" cssElementClass="emp_currency_text_readonly" />
					<emp:text id="LmtApply.org_crd_one_amt" label="原一次性授信敞口" maxlength="18" required="false" dataType="Currency" readonly="true" defvalue="0.00" cssElementClass="emp_currency_text_readonly" />
					<% }%>
					<emp:text id="LmtApply.crd_totl_amt" label="授信总额" maxlength="18" required="false" colSpan="2" dataType="Currency" readonly="true" defvalue="0" cssElementClass="emp_currency_text_readonly" />
					<emp:text id="LmtApply.crd_cir_amt" label="循环授信敞口" maxlength="18" required="true" onblur="computeCrdAmt()" dataType="Currency" defvalue="0.00" cssElementClass="emp_currency_text_readonly" readonly="true"/>
					<emp:text id="LmtApply.crd_one_amt" label="一次性授信敞口" maxlength="18" required="true" onblur="computeCrdAmt()" dataType="Currency" defvalue="0.00" cssElementClass="emp_currency_text_readonly" readonly="true"/>
					
					<emp:select id="LmtApply.flow_type" label="流程类型" required="false" dictname="STD_ZB_FLOW_TYPE" defvalue="01" readonly="true"/>
					<emp:date id="LmtApply.app_date" label="申请日期" required="false"  defvalue="${context.OPENDAY}" readonly="true"/>
					<emp:text id="LmtApply.over_date" label="办结日期" maxlength="10" required="false" hidden="true" />
					<emp:textarea id="LmtApply.memo" label="备注" maxlength="200" required="false" colSpan="2" />
					<emp:pop id="LmtApply.manager_id_displayname" label="责任人" required="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
					<emp:pop id="LmtApply.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName" />
					<emp:text id="LmtApply.manager_id" label="责任人" required="true" hidden="true"/>
					<emp:text id="LmtApply.manager_br_id" label="责任机构" required="true" hidden="true"/>
					<emp:text id="LmtApply.input_id" label="登记人" maxlength="20" required="true" defvalue="${context.currentUserName}" hidden="true"/>
					<emp:text id="LmtApply.input_br_id" label="登记机构" maxlength="20" required="true" defvalue="${context.organName}" hidden="true" />
					<emp:text id="LmtApply.input_id_displayname" label="登记人" required="false" readonly="true" defvalue="${context.currentUserName}" />
					<emp:text id="LmtApply.input_br_id_displayname" label="登记机构" required="false" readonly="true"  defvalue="${context.organName}"/>
					<emp:text id="LmtApply.input_date" label="登记日期" maxlength="10" required="false" defvalue="${context.OPENDAY}" readonly="true"/>
					<emp:select id="LmtApply.approve_status" label="申请状态" required="true" dictname="WF_APP_STATUS" hidden="true" defvalue="000"/>
					<emp:text id="LmtApply.agr_no" label="协议编号" hidden="true"/>
					<emp:text id="LmtApply.grp_serno" label="集团授信编号" maxlength="40" required="false" hidden="true"/>
				</emp:gridLayout>
				<div align="center">
					<br>
					<emp:button id="submitLmtApply" label="确定" op="update"/>
					<emp:button id="reset" label="重置"/>
				</div>
			</emp:form>
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
	<div align="center">
		<input type="button" class="button80" onclick="doViewCusRelInfo(this)" value="关联关系搜索">
		<input type="button" class="button80" onclick="doReturn(this)" value="返回列表">
	</div>
</body>
</html>
</emp:page>
