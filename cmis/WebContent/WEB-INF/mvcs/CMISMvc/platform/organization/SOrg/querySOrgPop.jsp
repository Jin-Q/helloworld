<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<emp:page>

	<html>
	<head>
	<title>列表查询页面</title>

	<jsp:include page="/include.jsp" flush="true" />
	<style type="text/css">
		.emp_field_longtext_input { /****** 长度固定 ******/
			width: 250px;
			border-width: 1px;
			border-color: #b7b7b7;
			border-style: solid;
			text-align: left;
		}
	</style>
	<script type="text/javascript">
	function doQuery() {
		var form = document.getElementById('queryForm');
		SOrg._toForm(form);
		SOrgList._obj.ajaxQuery(null, form);
	};

	function doReset() {
		page.dataGroups.SOrgGroup.reset();
	};

	/*--user code begin--*/
	function doSelect(){
		var data = SOrgList._obj.getSelectedData();	
		if (data != null && data.length !=0) {
			var parentWin = EMPTools.getWindowOpener();
			eval("parentWin.${context.popReturnMethod}(data[0])");
			window.close();
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	/*--user code end--*/
</script>
	</head>
	<body class="page_content">
	<form method="POST" action="#" id="queryForm"></form>

	<emp:gridLayout id="SOrgGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="SOrg.organno" label="机构码" />
		<emp:text id="SOrg.organname" label="机构名称" />
		<emp:text id="SOrg.distno" label="地区编号" hidden="true"/>
		<emp:text id="SOrg.fincode" label="金融代码" hidden="true" />
		<emp:text id="SOrg.arti_organno" label="所属法人机构码" hidden="true"/>
		<emp:text id="SOrg.suporganno" label="上级机构码" hidden="true"/>
	</emp:gridLayout>

	<jsp:include page="/queryInclude.jsp" flush="true" />
	<!--/** modified by wangj 2015-10-10 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/-->
	<emp:table icollName="SOrgList" pageMode="true" url="pageSOrgQuery.do" reqParams="restrictUsed=${context.restrictUsed}&team=${context.team}&manager_id=${context.manager_id}&yewu=${context.yewu}&opt=${context.opt}">
	<!--/** modified by wangj 2015-10-10 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/-->
		<emp:text id="organno" label="机构码" />
		<emp:text id="suporganno" label="上级机构码" hidden="true"/>
		<emp:text id="organname" label="机构名称" />
		<emp:text id="distno" label="地区编号" />
		<emp:text id="fincode" label="金融代码" />
		<emp:text id="arti_organno" label="所属法人机构码" />
		<!-- modified by lisj 2015-5-6 需求编号：【XD150407025】分支机构授信审批权限配置 begin -->
		<emp:text id="org_lvl" label="机构等级" dictname="STD_ZB_ORG_LVL"/>
		<!-- modified by lisj 2015-5-6 需求编号：【XD150407025】分支机构授信审批权限配置 end -->
	</emp:table>

	<div ><br>
	<button onclick="doSelect()">选取返回</button>
	<br>
	</div>
	</body>
	</html>
</emp:page>
