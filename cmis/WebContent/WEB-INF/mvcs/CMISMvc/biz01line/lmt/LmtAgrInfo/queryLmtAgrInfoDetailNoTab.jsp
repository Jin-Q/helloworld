<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	//获取对公客户管理一键查询标识符
	String one_key = "";
	if(context.containsKey("OneKey")){
		one_key = (String)context.getDataValue("OneKey");
	}
%>
<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryLmtAgrInfoList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	function onLoad(){
		if("n"=="${context.showBut}" || "N"=="${context.showBut}"){
			document.getElementById("back").style.display="none";
			document.getElementById("close").style.display="";
		}else{
			document.getElementById("back").style.display="";
			document.getElementById("close").style.display="none";
		}
		LmtAgrInfo.cus_id._obj.addOneButton('viewCusInfo','查看',viewCusInfo);
		LmtAgrInfo.agr_no._obj.addOneButton('viewLmtAgrInfo','查看',viewLmtAgrInfo);
	}
	//客户信息查
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+LmtAgrInfo.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'viewCusInfo','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}

	//协议查看
	function viewLmtAgrInfo(){
		var url = '<emp:url action="getLmtAgrInfoViewPage.do"/>&agr_no='+LmtAgrInfo.agr_no._getValue()+'&menuId=crd_agr&cus_id='+LmtAgrInfo.cus_id._getValue()+'&isShow=N&op=view';
      	url=encodeURI(url); 
      	window.open(url,'viewLmtAgrInfo','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}	
	/**add by lisj 2014年12月11日 需求编号：【XD141107075】 一键查询改造 begin**/
	function doReturnByOneKey() {
		var cus_id  =LmtAgrInfo.cus_id._obj.element.value;
		var url = '<emp:url action="queryCusComByOneKey.do"/>?cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/**add by lisj 2014年12月11日 需求编号：【XD141107075】 一键查询改造 end**/
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onLoad()">
		<emp:gridLayout id="LmtAgrInfoGroup" title="授信协议信息" maxColumn="2">
			<emp:text id="LmtAgrInfo.serno" label="业务编号" maxlength="40" required="true" /> 
			<emp:text id="LmtAgrInfo.agr_no" label="协议编号" maxlength="40" required="false"/>
			<emp:text id="LmtAgrInfo.cus_id" label="客户码" maxlength="30" required="true" />
			<emp:text id="LmtAgrInfo.cus_id_displayname" label="客户名称" required="true" cssElementClass="emp_field_text_readonly"/>
			<emp:text id="LmtAgrInfo.grp_agr_no" label="集团授信编号" maxlength="40" hidden="true" />
			<emp:select id="LmtAgrInfo.biz_type" label="授信业务类型" required="true" dictname="STD_ZB_BIZ_TYPE"/>
			<emp:select id="LmtAgrInfo.cur_type" label="授信币种" required="false" dictname="STD_ZX_CUR_TYPE"/>			
			<emp:text id="LmtAgrInfo.crd_totl_amt" label="授信总额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			
			<emp:text id="LmtAgrInfo.totl_amt" label="非低风险授信总额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" colSpan="2"/>
			<emp:text id="LmtAgrInfo.crd_cir_amt" label="非低风险循环授信敞口" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAgrInfo.crd_one_amt" label="非低风险一次性授信敞口" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<%if(!"GRP".equalsIgnoreCase((String)context.getDataValue("origin"))){ %>
			<emp:text id="LmtAgrInfo.lrisk_total_amt" label="低风险授信总额" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" colSpan="2"/>
			<emp:text id="LmtAgrInfo.lrisk_cir_amt" label="低风险循环授信敞口" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAgrInfo.lrisk_one_amt" label="低风险一次性授信敞口" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<%} %>
			<emp:date id="LmtAgrInfo.start_date" label="授信起始日" required="false" />
			<emp:date id="LmtAgrInfo.end_date" label="授信到期日" required="false" />
			<emp:textarea id="LmtAgrInfo.memo" label="备注" maxlength="500" required="false" colSpan="2"/>
		</emp:gridLayout>
		<emp:gridLayout id="LmtAgrInfoGroup_input" title="登记信息" maxColumn="2">
			<emp:text id="LmtAgrInfo.manager_id_displayname" label="责任人" required="false" />
			<emp:text id="LmtAgrInfo.manager_br_id_displayname" label="责任机构" required="false" />
			<emp:text id="LmtAgrInfo.input_id_displayname" label="登记人" required="true" defvalue="$currentUserName"/>
			<emp:text id="LmtAgrInfo.input_br_id_displayname" label="登记机构" required="true" defvalue="$organName"/>
			<emp:text id="LmtAgrInfo.input_date" label="登记日期" maxlength="10" required="true" defvalue="$OPENDAY"/>
			<emp:text id="LmtAgrInfo.input_id" label="登记人" maxlength="20" required="true" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="LmtAgrInfo.input_br_id" label="登记机构" maxlength="20" required="true" hidden="true" defvalue="$organNo"/>
			<emp:text id="LmtAgrInfo.manager_id" label="责任人" maxlength="20" required="false" hidden="true"/>
			<emp:text id="LmtAgrInfo.manager_br_id" label="责任机构" maxlength="20" required="false" hidden="true"/>
			<emp:text id="LmtAgrInfo.restrict_tab" label="restrict_tab" defvalue="false" hidden="true"/>
		</emp:gridLayout>
	<div align="center">
		<br>
		<%if(!"".equals(one_key) && one_key != null) {%>
			  <emp:button id="returnByOneKey" label="返回" />
		<% }else{%>
			<input id="back" type="button" class="button80" onclick="window.history.go(-1);" value="返回列表">
		<% }%>
		<input id="close" type="button" class="button80" onclick="window.close();" value="关闭">
	</div>
</body>
</html>
</emp:page>
