<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<%
	request.setAttribute("canwrite","");
	String cus_id = (String)request.getParameter("CusGrpInfoHis.cus_id");
	String returnflag = "";
	returnflag = (String)request.getParameter("returnflag");

%>

<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryCusGrpInfoHisList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function doViewCusGrpMemberHis() {
		//var paramStr = CusGrpMember._obj.getParamStr(['grp_no','cus_id']);
		var data = CusGrpMemberHis._obj.getSelectedData();
		//var type = data[0].grp_corre_type._getValue();
		var cusId = data[0].cus_id._getValue();
		var cusType = data[0].cus_type._getValue();
		if (cusId!=null) {
			
			if(cusType.substring(0,1)==1){
				var url = '<emp:url action="getCusIndivViewPage.do"/>?cus_id='+cusId+'&flag=query';
				url = EMPTools.encodeURI(url);
				EMPTools.openWindow(url,'newwindow','height=600,width=1024,top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
			}else{
				//var url = '<emp:url action="queryCusGrpInfoHisCusGrpMemberDetail.do"/>?'+paramStr;
				var url = '<emp:url action="getCusComViewPage.do"/>?cus_id='+cusId+'&flag=query';
				url = EMPTools.encodeURI(url);
				EMPTools.openWindow(url,'newwindow','height=600,width=1024,top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
			}	
		}else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusGrpRe(){
		var paramStr = CusGrpMemberHis._obj.getParamStr(['cus_id']);
		if (paramStr!=null) {
			var url = '<emp:url action="GetCusRelTreeOp.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		} else {
			alert('请先选择一条记录！');
		}
	}
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:gridLayout id="CusGrpInfoHisGroup" title="关联(集团)客户基本信息" maxColumn="2">
			<emp:text id="serno" label="流水号"/>
	
			<emp:text id="CusGrpInfoHis.grp_no" label="关联(集团)编号" maxlength="30" required="true" readonly="true" />
			<emp:text id="CusGrpInfoHis.grp_name" label="关联(集团)名称" maxlength="60" required="true" />
			<emp:pop id="CusGrpInfoHis.parent_cus_id" label="主关联(集团)客户码" url="null" required="true" />
			<emp:text id="CusGrpInfoHis.parent_cus_name" label="主关联(集团)客户名称" maxlength="60" required="true" />
			<emp:text id="CusGrpInfoHis.parent_org_code" label="主关联(集团)组织机构代码" maxlength="10" required="true" />
			<emp:text id="CusGrpInfoHis.parent_loan_card" label="主关联(集团)贷款卡编码" maxlength="16" required="false" />
			<emp:select id="CusGrpInfoHis.grp_finance_type" label="关联(集团)融资形式" required="false" hidden="true" dictname="STD_ZB_GRP_FIN_TYPE"/>
			<emp:textarea id="CusGrpInfoHis.grp_detail" label="关联(集团)情况说明" maxlength="250" required="false" colSpan="2" />
			<emp:pop id="CusGrpInfoHis.manager_br_id" label="主办行" url=""  hidden="true" />
			<emp:pop id="CusGrpInfoHis.manager_id" label="主办客户经理" url="null" hidden="true" />
			<emp:text id="CusGrpInfoHis.input_id" label="登记人" maxlength="20" hidden="true" />
			<emp:text id="CusGrpInfoHis.input_br_id" label="登记机构" maxlength="20" hidden="true" />
			 <emp:text id="CusGrpInfoHis.input_id_displayname" label="登记人"  required="true" colSpan="2"/>
			 <emp:pop id="CusGrpInfoHis.manager_id_displayname" label="责任人" url="null" required="true" />
			
			<emp:pop id="CusGrpInfoHis.manager_br_id_displayname" label="责任机构" url=""  required="true" />
            <emp:text id="CusGrpInfoHis.input_br_id_displayname" label="登记机构"  required="true" hidden="true"/>
            <emp:text id="CusGrpInfoHis.input_date" label="登记日期" maxlength="10" required="true" />
	</emp:gridLayout>
	<br>
	<div align="center">
	<%if(null != returnflag && "1".equals(returnflag.trim())) {%>
	
	<%}else { %>
		<emp:button id="return" label="返回"/>
	<%} %>
	</div>
	<br>

	<emp:tabGroup id="CusGrpInfoHis_tabs" mainTab="CusGrpMemberHis_tab">
		<emp:tab id="CusGrpMemberHis_tab" label="关联(集团)客户成员">
			<div align="left">
				<emp:button id="viewCusGrpMemberHis" label="查看"/>
				<emp:button id="viewCusGrpRe" label="客户关联信息查看"/>
			</div>
			<emp:table icollName="CusGrpMemberHis" pageMode="false" url="">
		<emp:text id="cus_id" label="成员客户码" />
		<emp:text id="cus_name" label="成员客户名称" />
		<emp:text id="grp_corre_type" label="关联(集团)关联关系类型" dictname='STD_ZB_GROUP_TYPE'/>
		<emp:text id="cus_manager" label="主办客户经理" hidden="true" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id" label="责任机构" hidden="true"/>
		
		<emp:text id="cus_type" label="客户类型" dictname="STD_ZB_CUS_TYPE"  hidden="true"/>
        <emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="grp_no" label="关联(集团)编号" hidden="true"/>
		<emp:text id="serno" label="流水号" hidden="true"/>
		
			</emp:table>
		</emp:tab>
	</emp:tabGroup>

	

</body>
</html>
</emp:page>
