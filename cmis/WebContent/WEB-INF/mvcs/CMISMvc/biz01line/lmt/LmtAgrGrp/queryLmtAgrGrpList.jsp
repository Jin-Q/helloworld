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
		LmtAgrGrp._toForm(form);
		LmtAgrGrpList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateLmtAgrGrpPage() {
		var paramStr = LmtAgrGrpList._obj.getParamStr(['grp_agr_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAgrGrpUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtAgrGrp() {
		var paramStr = LmtAgrGrpList._obj.getParamStr(['grp_agr_no']);
		if (paramStr != null) {
			/**add by yangzy  2015-6-16  需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能 begin**/
			var url = '<emp:url action="getLmtAgrGrpViewPage.do"/>?'+paramStr+"&isShow=unview";
			/**add by yangzy  2015-6-16  需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能 end**/
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddLmtAgrGrpPage() {
		var url = '<emp:url action="getLmtAgrGrpAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteLmtAgrGrp() {
		var paramStr = LmtAgrGrpList._obj.getParamStr(['grp_agr_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteLmtAgrGrpRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.LmtAgrGrpGroup.reset();
	};
	 /**add by yangzy  2015-6-16  需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能 begin**/
	function doPrintln(){
		var paramStr = LmtAgrGrpList._obj.getParamStr(['grp_agr_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAgrGrpViewPage.do"/>?'+paramStr+"&isShow=view";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	 /**add by yangzy  2015-6-16  需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能 end**/
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="LmtAgrGrpGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="LmtAgrGrp.serno" label="业务编号" />
			<emp:text id="LmtAgrGrp.grp_agr_no" label="集团协议编号" />
			<emp:text id="LmtAgrGrp.grp_no" label="集团编号" />
			<!-- add by lisj 2015-3-24 增加通过集团成员查询集团客户的功能-->
			<emp:text id="LmtAgrGrp.grp_name" label="集团名称" />
			<emp:text id="LmtAgrGrp.grp_member_name" label="集团成员名称" />
			<emp:select id="LmtAgrGrp.agr_status" label="协议状态" dictname="STD_ZB_AGR_STATUS"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddLmtAgrGrpPage" label="新增" op="add"/>
		<emp:button id="getUpdateLmtAgrGrpPage" label="修改" op="update"/>
		<emp:button id="deleteLmtAgrGrp" label="删除" op="remove"/>
		<emp:button id="viewLmtAgrGrp" label="查看" op="view"/>
		<!-- add by yangzy 2015-6-16  需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能 start-->
		<emp:button id="println" label="封面打印" op="println"/>
		<!-- add by yangzy 2015-6-16  需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能 end-->
	</div>

	<emp:table icollName="LmtAgrGrpList" pageMode="true" url="pageLmtAgrGrpQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="grp_agr_no" label="集团协议编号" />
		<emp:text id="grp_no" label="集团编号" />
		<emp:text id="grp_no_displayname" label="集团名称" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" hidden="true"/>
		<emp:text id="crd_totl_amt" label="授信总额" dataType="Currency"/>
		<emp:text id="start_date" label="授信起始日" />
		<emp:text id="end_date" label="授信到期日" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="agr_status" label="协议状态" dictname="STD_ZB_AGR_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    