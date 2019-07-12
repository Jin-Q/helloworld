<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String lmt_flag = "";
	lmt_flag = (String)context.getDataValue("CusSameOrg.lmt_flag");
%>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
	String type = request.getParameter("type");
%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryCusSameOrgList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	}

	//地址街道隐藏显示
	function checkCountry(){
		var comCountry = CusSameOrg.country._getValue();
		if(comCountry == 'CHN'){
			CusSameOrg.address_displayname._obj._renderRequired(true);
			CusSameOrg.street._obj._renderRequired(true);
			CusSameOrg.address_displayname._obj._renderHidden(false);
			CusSameOrg.street._obj._renderHidden(false);
		}else{
			CusSameOrg.address_displayname._obj._renderHidden(true);
			CusSameOrg.street._obj._renderHidden(true);
			CusSameOrg.address_displayname._obj._renderRequired(false);
			CusSameOrg.street._obj._renderRequired(false);
		}
	}

	//上市标志事件
	function cheakMrk(){
		var mrkFlag = CusSameOrg.mrk_flag._obj.element.value;
		if(mrkFlag=='1'){
			//当选择为上市时，上市地和股票代码必输
			//上市地
			CusSameOrg.mrl_area._obj._renderRequired(true);
			CusSameOrg.mrl_area._obj._renderHidden(false);
			//股票代码
			CusSameOrg.stock_no._obj._renderRequired(true);
			CusSameOrg.stock_no._obj._renderHidden(false);
		}else if(mrkFlag=='2'){
			//当选择为未上市时，上市地和股票代码非必输且隐藏
			CusSameOrg.mrl_area._setValue("");
			CusSameOrg.mrl_area._obj._renderRequired(false);
			CusSameOrg.mrl_area._obj._renderHidden(true);

			CusSameOrg.stock_no._obj.element.value="";
			CusSameOrg.stock_no._obj._renderRequired(false);
			CusSameOrg.stock_no._obj._renderHidden(true);
		}
	}
	
	function doload(){
		checkCountry();
		cheakMrk();
		CusSameOrg.head_org_no._obj.addOneButton("cus_id","查看",getCusForm);
	};
	function getCusForm(){
		var head_org_no = CusSameOrg.head_org_no._getValue();
		var url = "<emp:url action='getCusSameOrgView4Head.do'/>&type=cusSame&head_org_no="+head_org_no;
		url=EMPTools.encodeURI(url);  
      	window.open(url,'cus_window','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
  <emp:tabGroup mainTab="base_tab" id="mainTab" >
   <emp:tab label="基本信息" id="base_tab" needFlush="true" initial="true" >
	<emp:gridLayout id="CusSameOrgGroup" title="金融同业客户" maxColumn="2">
		<%//	<emp:select id="CusSameOrg.same_org_type" label="同业机构类型" required="true" dictname="STD_ZB_INTER_BANK_ORG" /> %>
			<emp:pop id="CusSameOrg.same_org_type_displayname" label="同业机构类型" required="true" colSpan="2" cssElementClass="emp_field_text_input2" url="showDicTree.do?dicTreeTypeId=STD_ZB_INTER_BANK_ORG" returnMethod="onReturnOrgType"/>
		    <emp:text id="CusSameOrg.same_org_type" label="同业机构类型" colSpan="2" hidden="true"/>
			<emp:select id="CusSameOrg.country" label="国别"  required="true" dictname="STD_GB_2659-2000" onchange="checkCountry()"/>
			<emp:text id="CusSameOrg.cus_id" label="客户码" required="false" readonly="true" hidden="false"/>
			<emp:text id="CusSameOrg.com_ins_code" label="组织机构代码" maxlength="10" required="true" onchange="CheckComInsCode()"/>
			<emp:text id="CusSameOrg.swift_no" label="SWIFT编号" maxlength="35" required="false" />
			<emp:text id="CusSameOrg.same_org_no" label="同业机构(行)号" maxlength="40" required="true"/>
			<emp:text id="CusSameOrg.same_org_cnname" label="同业机构(行)名称" maxlength="100" required="true" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:text id="CusSameOrg.same_org_enname" label="同业机构(行)英文名称" maxlength="40" required="false" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:text id="CusSameOrg.org_site" label="同业机构(行)网址" maxlength="80" required="false" colSpan="2" cssElementClass="emp_field_text_input2"/>
			
			<emp:text id="CusSameOrg.address" label="地址"  required="false" hidden="true"/>
			<emp:pop id="CusSameOrg.address_displayname" label="地址"  required="false" colSpan="2" cssElementClass="emp_field_text_input2"
			url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" returnMethod="onReturnRegStateCode" />
			<emp:text id="CusSameOrg.street" label="街道"  required="false" cssElementClass="emp_field_text_input2" colSpan="2"/>
			<emp:date id="CusSameOrg.same_org_est" label="同业机构(行)成立日" required="false" />
			<emp:text id="CusSameOrg.bank_pro_lic" label="金融业务许可证" maxlength="80" required="true" />
			<emp:text id="CusSameOrg.com_ins_no" label="营业执照号码" maxlength="80" required="true" />
			<emp:pop id="CusSameOrg.up_org_no" label="上级行号" required="false" url="getPrdBankInfoPopList.do?restrictUsed=false" returnMethod="setUpOrgEst" />
			<emp:text id="CusSameOrg.head_org_no" label="总行行号" required="false" />
			<emp:select id="CusSameOrg.reg_cur_type" label="注册/开办资金币种" required="false" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="CusSameOrg.reg_cap_amt" label="注册/开办资金(万元)" maxlength="18" required="false" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusSameOrg.paid_cap_amt" label="实际到位资金(万元)" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusSameOrg.assets" label="总资产(万元)" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="CusSameOrg.crd_grade" label="信用等级" required="true" dictname="STD_ZB_FINA_GRADE" defvalue="Z"/>
			<emp:select id="CusSameOrg.cust_level" label="监管评级" required="false" dictname="STD_ZB_CUSTD_RATE"/>
			<emp:date id="CusSameOrg.eval_maturity" label="评级到期日期" required="false"/>
			<emp:select id="CusSameOrg.mrk_flag" label="上市标志" required="true" dictname="STD_ZX_YES_NO" onchange="checkMrk()"/>
			<emp:select id="CusSameOrg.mrl_area" label="上市地" required="false" dictname="STD_ZX_LISTED"/>
			<emp:text id="CusSameOrg.stock_no" label="股票代码" maxlength="32" required="false" />
			<emp:text id="CusSameOrg.linkman_name" label="主联系人姓名" maxlength="40" required="true" />
			<emp:select id="CusSameOrg.linkman_duty" label="主联系人职务" required="true" dictname="STD_ZB_MANAGER_TYPE"/>
			<emp:text id="CusSameOrg.linkman_phone" label="主联系人电话" maxlength="20" required="true" dataType="Phone" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusSameOrg.linkman_mobile_no" label="主联系人手机号" maxlength="20" required="true" dataType="Mobile" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusSameOrg.linkman_email" label="电子邮箱" maxlength="80" required="true" dataType="Email" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="CusSameOrg.linkman_fax" label="传真" maxlength="35" required="true" dataType="Phone" cssElementClass="emp_currency_text_readonly"/>
			<emp:select id="CusSameOrg.rel_dgr" label="与我行合作关系" required="true" dictname="STD_ZB_CUS_BANK_CO"/>
			<emp:select id="CusSameOrg.lmt_flag" label="是否授信" readonly="true" dictname="STD_ZX_YES_NO"/>
		</emp:gridLayout>	
		<emp:gridLayout id="CusSameOrgGroup2" title="登记信息" maxColumn="2">	
			<emp:pop id="CusSameOrg.manager_id_displayname" label="主管客户经理" required="false" hidden="true" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="CusSameOrg.manager_br_id_displayname" label="主管机构" required="false" hidden="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID"/>
			<emp:text id="CusSameOrg.input_id_displayname" label="登记人"  required="false"  hidden="false" readonly="true"/>
			<emp:text id="CusSameOrg.input_br_id_displayname" label="登记机构"  required="false"  hidden="false" readonly="true"/>
			<emp:text id="CusSameOrg.input_id" label="登记人" maxlength="20" required="false"  hidden="true" readonly="true"/>
			<emp:text id="CusSameOrg.input_br_id" label="登记机构" maxlength="20" required="false"  hidden="true" readonly="true"/>
			<emp:text id="CusSameOrg.manager_id" label="主管客户经理" required="false" hidden="true"/>
			<emp:text id="CusSameOrg.manager_br_id" label="主管机构" required="false" hidden="true"/>
			<emp:text id="CusSameOrg.input_date" label="登记日期" maxlength="10" required="false" hidden="false" readonly="true"/>
		</emp:gridLayout>
	</emp:tab>
	 <%if("1".equals(lmt_flag)){%>
	    <emp:tab label="授信信息" id="LmtSubTab" url="queryLmtIntbankPop4Cus.do?cus_id=${context.CusSameOrg.cus_id}&op=view" initial="false" needFlush="true"/>
	    <%}%>
	</emp:tabGroup>
	<div align="center">
		<br>
		<%if("cusSame".equals(type)) {%>
		<%}else{ %>
			<emp:button id="return" label="返回到列表页面"/>
		<%} %>
	</div>
</body>
</html>
</emp:page>
