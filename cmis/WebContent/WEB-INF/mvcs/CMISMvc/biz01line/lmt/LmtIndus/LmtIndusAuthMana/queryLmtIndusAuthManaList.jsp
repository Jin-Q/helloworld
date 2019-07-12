<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtIndusAuthMana._toForm(form);
		LmtIndusAuthManaList._obj.ajaxQuery(null,form);
	};

	function doViewLmtIndusAuthMana() {
		var paramStr = LmtIndusAuthManaList._obj.getParamStr(['agr_no','input_br_id','guar_type']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtIndusAuthManaViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
		
	function doReset(){
		page.dataGroups.LmtIndusAuthManaGroup.reset();
	};
	
	/*--user code begin--*/
	function getOrgID(data){
		LmtIndusAuthMana.input_br_id._setValue(data.organno._getValue());
		document.all.input_br_id_displayname.value = data.organname._getValue();
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="LmtIndusAuthManaGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="LmtIndusAuthMana.agr_no" label="协议编号" hidden="true" defvalue="${context.agr_no}"/>
			<emp:text id="LmtIndusAuthMana.input_br_id" label="申请机构" hidden="true" />
			<emp:pop id="input_br_id_displayname" label="申请机构"  url="querySOrgPop.do?restrictUsed=false" 
			returnMethod="getOrgID" cssElementClass="emp_pop_common_org" />
			<emp:select id="LmtIndusAuthMana.guar_type" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddLmtIndusAuthManaPage" label="新增" op="add"/>
		<emp:button id="getUpdateLmtIndusAuthManaPage" label="修改" op="update"/>
		<emp:button id="deleteLmtIndusAuthMana" label="删除" op="remove"/>
		<emp:button id="viewLmtIndusAuthMana" label="查看" op="view"/>
	</div>

	<emp:table icollName="LmtIndusAuthManaList" pageMode="true" url="pageLmtIndusAuthManaQuery.do">
		<emp:text id="agr_no" label="协议编号" />
		<emp:text id="input_br_id_displayname" label="申请机构" />
		<emp:text id="input_br_id" label="申请机构" hidden="true"/>
		<emp:text id="guar_type" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" />
		<emp:text id="single_auth_amt" label="单户授权金额(元)" dataType="Currency"/>
		<emp:text id="status" label="状态" dictname="STD_DRFPO_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    