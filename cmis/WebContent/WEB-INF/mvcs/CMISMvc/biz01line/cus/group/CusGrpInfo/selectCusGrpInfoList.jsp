<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<% 
	String type = request.getParameter("type");
%>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusGrpInfo._toForm(form);
		CusGrpInfoList._obj.ajaxQuery(null,form);
	};

	function doSelect(){	
		var data = CusGrpInfoList._obj.getSelectedData();
		//alert("+++++++++====-------=======");
		if (data != null && data != "") {
		//	alert("+++++++++===========------");
			window.opener["${context.returnMethod}"](data[0]);
			//alert("+++++++++=======222====");
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	/*--user code begin--*/
	function doReset(){
		page.dataGroups.CusGrpInfoGroup.reset();
	};
	
	//主管客户经理
	function setconId(data){
		CusGrpInfo.manager_id._setValue(data.actorno._getValue());
		CusGrpInfo.manager_id_displayname._setValue(data.actorname._getValue());
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" >
	<form  method="POST" action="#" id="queryForm">
	  <input type="hidden" name="flag" value="2"/>  
	</form>

	<emp:gridLayout id="CusGrpInfoGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="CusGrpInfo.grp_no" label="关联(集团)编号" />
		<emp:text id="CusGrpInfo.grp_name" label="关联(集团)名称" />
		<emp:pop id="CusGrpInfo.manager_id_displayname" label="主管客户经理" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
		<emp:text id="CusGrpInfo.manager_id" label="主管客户经理编号" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<button onclick="doSelect()">选取返回</button>

	<emp:table icollName="CusGrpInfoList" pageMode="true" url="pageCusGrpInfoQueryforMainForMod.do" >
	   <emp:text id="grp_no" label="关联(集团)编号" />
		<emp:text id="grp_name" label="关联(集团)名称" />
		<emp:text id="parent_cus_id" label="主(集团)客户码" />
		<emp:text id="parent_cus_name" label="主(集团)名称" />
		<emp:text id="manager_id_displayname" label="责任人" />  
		<emp:text id="parent_org_code" label="母公司组织机构代码" hidden="true"/>   
		<emp:text id="parent_loan_card" label="母公司贷款卡编码" hidden="true"/>  
		<emp:text id="grp_finance_type" label="集团融资形式" hidden="true"/>
		<emp:text id="grp_cus_type" label="集团客户类型" hidden="true"/>
		<emp:text id="grp_detail" label="集团情况说明" hidden="true"/>
		<emp:text id="input_user_id" label="登记人" hidden="true"/>
		<emp:text id="input_date" label="登记日期" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="manager_br_id_displayname" label="责任机构" hidden="true"/>
		<emp:text id="manager_id" label="主办客户经理" hidden="true"/>
		<emp:text id="manager_br_id" label="主办行" hidden="true"/>		
	</emp:table>
	<button onclick="doSelect()">选取返回</button>
</body>
</html>
</emp:page>
  
    