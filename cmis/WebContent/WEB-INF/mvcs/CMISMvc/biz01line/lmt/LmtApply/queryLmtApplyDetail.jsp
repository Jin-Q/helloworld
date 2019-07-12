<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<%
	request.setAttribute("canwrite","");
	String type = request.getParameter("type");
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	//获取对公客户管理一键查询标识符
	String one_key = "";
	if(context.containsKey("OneKey")){
		one_key = (String)context.getDataValue("OneKey");
	}
	//我的工作台待办事项标志
	String wf_flag = "";
	if(context.containsKey("wf_flag")){
		wf_flag = (String)context.getDataValue("wf_flag");
	}
%>
<script type="text/javascript">
	/*--user code begin--*/
	function doReturn() {
		var type='${context.type}';
		if(type=='Y'){
		//	window.history.go(-1);
		//	return
			var url = '<emp:url action="queryLmtApplyList.do"/>?type=cusHis&menuId=${context.menuId}&cus_id=${context.cus_id}';
			url = EMPTools.encodeURI(url);
			window.location=url;
			return ;
		}

		var menuId = '${context.menuId}'; 
		var url = "";
		if(menuId.indexOf("apply")>1){
			url = '<emp:url action="queryLmtApplyList.do"/>?type=app&menuId=${context.menuId}';
		}else{
			url = '<emp:url action="queryLmtApplyList.do"/>?type=his&menuId=${context.menuId}&overrule=${context.overrule}&cus_id=${context.cus_id}';
		}
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	function doClose(){
		window.close();
	}
	
	function onLoad(){
		LmtApply.cus_id._obj.addOneButton('view12','查看',viewCusInfo);

		var app_type = '${context.LmtApply.app_type}';
		if("01"==app_type){    //如果是授信变更，显示原有额度情况   
			LmtApply.org_crd_totl_amt._obj._renderHidden(true);
			LmtApply.org_crd_cir_amt._obj._renderHidden(true);
			LmtApply.org_crd_one_amt._obj._renderHidden(true);
		}

		var showButton = '${context.showButton}';  //是否显示返回按钮
		if("N"==showButton || "n"==showButton){
			document.getElementById("showbutton").style.display="none";
		}
		//当我的工作台已办事项标志为空时，校验客户关联关系信息
		var wf_flag ='<%=wf_flag%>';
		var one_key ='<%=one_key%>';
		if(wf_flag=="" || wf_flag==null){
			if(one_key =="" || one_key == null){
			doCheckCusRelInfo();
			}
		}
	};

	//查看客户综合信息
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+LmtApply.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height=600,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};

	function doImageView(){	//客户信息影像查看
		var data = new Array();
		data['serno'] = LmtApply.cus_id._getValue();	//客户资料的业务编号就填cus_id
		data['cus_id'] = LmtApply.cus_id._getValue();	//客户编号
		data['prd_id'] = 'BASIC';	//业务品种
		data['prd_stage'] = '' ;	//业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = 'View23'	;//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/**add by lisj 2014年12月11日 需求编号：【XD141107075】 一键查询改造 begin**/
	function doReturnByOneKey() {
		var cus_id  =LmtApply.cus_id._obj.element.value;
		var url = '<emp:url action="queryCusComByOneKey.do"/>?cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/**add by lisj 2014年12月11日 需求编号：【XD141107075】 一键查询改造 end**/
	//关联关系搜索
	function doViewCusRelInfo(){
		var cusId = LmtApply.cus_id._getValue();
		var url = '<emp:url action="GetCusRelTreeOp.do"/>?cus_id='+LmtApply.cus_id._getValue();
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height=600,width=800,top=30,left=30,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	/**add by lisj 2014年12月11日 需求编号：【XD141107075】 授信流程信息自动提醒改造，校验客户关联关系信息 begin**/
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
	/**add by lisj 2014年12月11日 需求编号：【XD141107075】 授信流程信息自动提醒改造，校验客户关联关系信息 end**/
	/*--user code end--*/
</script>
</head>
<body class="page_content" onload="onLoad()">
	<emp:tabGroup mainTab="main_tabs" id="main_tab">
		<emp:tab label="授信基本信息" id="main_tabs" needFlush="true" initial="true">
			<emp:gridLayout id="LmtApplyGroup" title="单一法人授信申请信息" maxColumn="2">
				<emp:text id="LmtApply.serno" label="业务编号" maxlength="40" required="true" />
				<emp:select id="LmtApply.app_type" label="申请类型" required="true" dictname="STD_ZB_APP_TYPE" />
				<!-- add by lisj 2015-3-23 需求编号：【XD150407025】分支机构授信审批权限配置 begin-->
				<emp:select id="LmtApply.is_overdue" label="逾期或欠息，垫款" required="true" dictname="STD_ZX_YES_NO" colSpan="2"/>
			    <!-- add by lisj 2015-3-23 需求编号：【XD150407025】分支机构授信审批权限配置 end -->
				<emp:text id="LmtApply.cus_id" label="客户码" maxlength="32" required="true" />
				<emp:select id="LmtApply.biz_type" label="授信业务类型 " required="true" dictname="STD_ZB_BIZ_TYPE" />
				<emp:text id="cus_name_displayname" label="客户名称" readonly="true" colSpan="2" cssElementClass="emp_field_text_long_readonly"   defvalue="${context.LmtApply.cus_id_displayname}"/>
				<emp:select id="LmtApply.lrisk_type" label="低风险业务类型" required="true" dictname="STD_ZB_LRISK_TYPE"/>
				<emp:select id="LmtApply.lmt_type" label="授信类别" required="false" dictname="STD_ZX_LMT_PRD" readonly="true" />
				<emp:select id="LmtApply.cur_type" label="授信币种" required="true" dictname="STD_ZX_CUR_TYPE" />
				<emp:text id="LmtApply.org_crd_totl_amt" label="原授信总额" maxlength="18" required="true" dataType="Currency" colSpan="2" readonly="true" defvalue="0.00" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="LmtApply.org_crd_cir_amt" label="原循环授信敞口" maxlength="18" required="true" dataType="Currency" readonly="true" defvalue="0.00" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="LmtApply.org_crd_one_amt" label="原一次性授信敞口" maxlength="18" required="true" dataType="Currency" readonly="true" defvalue="0.00" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="LmtApply.crd_totl_amt" label="授信总额" maxlength="18" required="true" dataType="Currency" colSpan="2" readonly="readonly" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="LmtApply.crd_cir_amt" label="循环授信敞口" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<emp:text id="LmtApply.crd_one_amt" label="一次性授信敞口" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
				<emp:select id="LmtApply.flow_type" label="流程类型" required="true" dictname="STD_ZB_FLOW_TYPE" colSpan="2"/>
				<emp:text id="LmtApply.app_date" label="申请日期" maxlength="10" required="true" />
				<emp:text id="LmtApply.over_date" label="办结日期" maxlength="10" required="false" />
				<emp:textarea id="LmtApply.memo" label="备注" maxlength="200" required="false" colSpan="3" />
				<emp:text id="LmtApply.manager_id_displayname" label="责任人" required="true" />
				<emp:text id="LmtApply.manager_br_id_displayname" label="责任机构" required="true" />
				<emp:text id="LmtApply.input_id_displayname" label="登记人" required="true" />
				<emp:text id="LmtApply.input_br_id_displayname" label="登记机构" required="true" />
				<emp:text id="LmtApply.input_date" label="登记日期" maxlength="10" required="true" />
				<emp:select id="LmtApply.approve_status" label="申请状态" required="true" dictname="WF_APP_STATUS" />
				<emp:text id="LmtApply.grp_serno" label="集团授信编号" maxlength="40" required="false" hidden="true"/>
				<emp:text id="LmtAgrInfo.restrict_tab" label="restrict_tab" defvalue="false" hidden="true"/>
			</emp:gridLayout>
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>
	<div align="center">
	<br>
	<%if(!"".equals(one_key) && one_key != null){ %>
		<emp:button id="returnByOneKey" label="返回" />
	<%}else if("surp".equals(type)){ %>
		<emp:button label="关闭" id="close" />
	<%}else{ %>
		<input type="button" class="button80" onclick="doViewCusRelInfo(this)" value="关联关系搜索">
		<input type="button" id="showbutton" class="button80" onclick="doReturn(this)" value="返回列表">
	<%} %>
	<%-- <emp:button id="ImageView" label="影像查看"/> --%>
	</div>
</body>

</html>
</emp:page>
