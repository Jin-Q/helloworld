<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String cus_id = (String)request.getParameter("CusGrpInfo.cus_id");
	String returnflag = "";
	returnflag = (String)request.getParameter("returnflag");
	//获取对公客户管理一键查询标识符
	String one_key = "";
	if(context.containsKey("OneKey")){
		one_key = (String)context.getDataValue("OneKey");
	}
%>

<script type="text/javascript">
	
	function doReturn() {
		var cus_id = '<%=cus_id%>';
		var url = '';
		if(null!=cus_id && cus_id!='null'){
			url = '<emp:url action="queryCusGrpInfoListForRel.do"/>?CusGrpInfo.cus_id='+cus_id;
		}else{
			url = '<emp:url action="queryCusGrpInfoList.do"/>';
		}
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function doViewCusGrpMember() {
		var paramStr = CusGrpMember._obj.getParamStr(['grp_no','cus_id']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryCusGrpInfoCusGrpMemberDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		}else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusGrpRe(){
		var paramStr = CusGrpMember._obj.getParamStr(['cus_id']);
		if (paramStr!=null) {
			var url = '<emp:url action="GetCusRelTreeOp.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		} else {
			alert('请先选择一条记录！');
		}
	}
	/*--user code begin--*/
	//主管机构
	function getOrgID(data){
		CusGrpInfo.manager_br_id._setValue(data.organno._getValue());
		CusGrpInfo.manager_br_id_displayname._setValue(data.organname._getValue());
	}
	function doSel(){
		var selObj = CusGrpMember._obj.getSelectedData()[0];
		var cus_id=selObj.cus_id._getValue();
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id;
		url=EMPTools.encodeURI(url);
		windowName = Math.ceil(Math.random()*50000000);
		window.open(url,windowName+"",'height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	/**add by lisj 2014年12月11日 需求编号：【XD141107075】 一键查询改造 begin**/
	function doReturnByOneKey() {
		var url = '<emp:url action="queryCusComByOneKey.do"/>?cus_id='+"<%=cus_id%>";
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/**add by lisj 2014年12月11日 需求编号：【XD141107075】 一键查询改造 end**/
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:gridLayout id="CusGrpInfoGroup" title="关联(集团)客户基本信息" maxColumn="2">
			<emp:text id="CusGrpInfo.grp_no" label="关联(集团)编号" maxlength="30" required="true" readonly="true" />
			<emp:text id="CusGrpInfo.grp_name" label="关联(集团)名称" maxlength="60" required="true" />
			<emp:pop id="CusGrpInfo.parent_cus_id" label="主关联(集团)客户码" url="null" required="true" />
			<emp:text id="CusGrpInfo.parent_cus_name" label="主关联(集团)客户名称" maxlength="60" required="true" />
			<emp:text id="CusGrpInfo.parent_org_code" label="主关联(集团)组织机构代码" maxlength="10" required="true" />
			<emp:text id="CusGrpInfo.parent_loan_card" label="主关联(集团)贷款卡编码" maxlength="16" required="false" />
			<emp:select id="CusGrpInfo.grp_finance_type" label="关联(集团)融资形式" required="false" dictname="STD_ZB_GRP_FIN_TYPE"/>
			<emp:select id="CusGrpInfo.grp_cus_type" label="集团客户类型"  required="true" dictname="STD_ZB_GRP_CUS_TYPE"  />
			<emp:textarea id="CusGrpInfo.grp_detail" label="关联(集团)情况说明" maxlength="250" required="false" colSpan="2" />
			
	</emp:gridLayout>
	<emp:gridLayout id="CusGrpInfoGroup" title="登记信息" maxColumn="2">	
			<emp:text id="CusGrpInfo.manager_id_displayname" label="责任人"  />
            <emp:pop id="CusGrpInfo.manager_br_id_displayname" label="管理机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID"  required="true" readonly="true"/>
			<emp:text id="CusGrpInfo.input_id_displayname" label="登记人"   />
            <emp:text id="CusGrpInfo.input_br_id_displayname" label="登记机构" />
            <emp:text id="CusGrpInfo.input_date" label="登记日期" maxlength="10" required="true" />
            
            <emp:text id="CusGrpInfo.manager_id" label="责任人" maxlength="20" hidden="true" />
            <emp:text id="CusGrpInfo.manager_br_id" label="管理机构" maxlength="20" required="true"  hidden="true"/>
			<emp:text id="CusGrpInfo.input_id" label="登记人" maxlength="20"  hidden="true" />
            <emp:text id="CusGrpInfo.input_br_id" label="登记机构" maxlength="20"  hidden="true"/>
	</emp:gridLayout>
	<br>
	<div align="center">
	<%if(!"".equals(one_key) && one_key != null) {%>
		<emp:button id="returnByOneKey" label="返回" />
		<%}else if(null == returnflag || !"1".equals(returnflag.trim())) {%>
		<emp:button id="return" label="返回"/>
	<%}%>
	</div>
	<br>
		<div class='emp_gridlayout_title'>关联(集团)客户成员&nbsp;</div>
		<div  id='CusGrpInfoGroupMember' class='QZ_menberBox'>
		<div align="left">	
			<emp:button id="viewCusGrpMember" label="查看"/>
			<emp:button label="客户关联信息查看" id="viewCusGrpRe" mousedownCss="button120_2" mouseoverCss="button120_2" mouseoutCss="button120" mouseupCss="button120"></emp:button>
		</div>
		
		<emp:table icollName="CusGrpMember" pageMode="false" url="">
			<emp:link id="cus_id" label="成员客户码" operation="sel"/>
			<emp:link id="cus_name" label="成员客户名称" operation="sel"/>
			<emp:text id="grp_corre_type" label="关联(集团)关联关系类型" dictname='STD_ZB_GROUP_TYPE'/>
			<emp:text id="manager_id_displayname" label="主管客户经理" />
			<emp:text id="manager_br_id_displayname" label="主管机构" />			
			<emp:text id="cus_type" label="客户类型" dictname="STD_ZB_CUS_TYPE"  hidden="true"/>
			<emp:text id="manager_br_id" label="责任机构" hidden="true"/>
			<emp:text id="grp_no" label="关联(集团)编号" hidden="true"/>
		</emp:table>
		</div>
</body>
</html>
</emp:page>
