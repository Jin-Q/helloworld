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
		PrdRate._toForm(form);
		PrdRateList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePrdRatePage() {
		var paramStr = PrdRateList._obj.getParamStr(['pk1']);
		if (paramStr != null) {
			var url = '<emp:url action="getPrdRateUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPrdRate() {
		var paramStr = PrdRateList._obj.getParamStr(['pk1']);
		if (paramStr != null) {
			var url = '<emp:url action="getPrdRateViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPrdRatePage() {
		var url = '<emp:url action="getPrdRateAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePrdRate() {
		var paramStr = PrdRateList._obj.getParamStr(['pk1']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deletePrdRateRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PrdRateGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PrdRateGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PrdRate.biz_type" label="业务品种" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddPrdRatePage" label="新增" op="add"/>
		<emp:button id="getUpdatePrdRatePage" label="修改" op="update"/>
		<emp:button id="deletePrdRate" label="删除" op="remove"/>
		<emp:button id="viewPrdRate" label="查看" op="view"/>
	</div>

	<emp:table icollName="PrdRateList" pageMode="true" url="pagePrdRateQuery.do">
		<emp:text id="pk1" label="主键" hidden="true"/>
		<emp:text id="biz_type" label="业务品种" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="base_remit_type" label="基准利率项目类型" />
		<emp:text id="base_rate_m" label="基准利率(年)" dataType="Rate"/>
		<emp:text id="rate_float_max" label="利率浮动上限" dataType="Percent"/>
		<emp:text id="rate_float_min" label="利率浮动下限" dataType="Percent"/>
		<emp:text id="term_max" label="期限上限（月）" />
		<emp:text id="term_min" label="期限下限（月）" />
		<emp:text id="term_memo" label="期限说明" />
		<emp:text id="manager_id_displayname" label="责任人" />  
		<emp:text id="inure_date" label="生效日期" />
		<emp:text id="regi_date" label="登记日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    