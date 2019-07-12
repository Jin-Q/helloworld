<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>子表列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doGetUpdateCusGrpMemberPage() {
		var paramStr = CusGrpMemberList._obj.getParamStr(['grp_no','cus_id']);
		if (paramStr!=null) {
			var url = '<emp:url action="getCusGrpInfoCusGrpMemberUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		}
	};
	
	function doGetAddCusGrpMemberPage(){
		var grp_no = window.parent.window.CusGrpInfo.grp_no._getValue();
		var br_id = window.parent.window.CusGrpInfo.main_br_id._getValue();
		var manager = window.parent.window.CusGrpInfo.cus_manager._getValue();
		var url = '<emp:url action="getCusGrpInfoCusGrpMemberAddPage.do"/>?CusGrpMember.grp_no='+grp_no+'&CusGrpMember.cus_manager='+manager+'&CusGrpMember.main_br_id='+br_id;
		url = EMPTools.encodeURI(url);
		EMPTools.openWindow(url,'newwindow');
	};
	
	function doDeleteCusGrpMember() {
		var paramStr = CusGrpMemberList._obj.getParamStr(['grp_no','cus_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusGrpInfoCusGrpMemberRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusGrpMember() {
		var paramStr = CusGrpMemberList._obj.getParamStr(['grp_no','cus_id']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryCusGrpInfoCusGrpMemberDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		}else {
			alert('请先选择一条记录！');
		}
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>

<body class="page_content">

	<!--<div align="left">
		<emp:button id="getAddCusGrpMemberPage" label="新增" op="add_CusGrpMember"/>
		<emp:button id="getUpdateCusGrpMemberPage" label="修改" op="update_CusGrpMember"/>
		<emp:button id="deleteCusGrpMember" label="删除" op="remove_CusGrpMember"/>
		<emp:button id="viewCusGrpMember" label="查看" op="view_CusGrpMember"/>
	</div>-->
	<div align="left">
		
		<emp:button id="viewCusGrpMember" label="查看"/>
		
	</div>
	<emp:table icollName="CusGrpMemberList" pageMode="true" url="pageCusGrpInfoCusGrpMemberQuery.do" reqParams="CusGrpInfo.grp_no=$CusGrpInfo.grp_no;">

		<emp:text id="cus_id" label="成员客户码" />
		<emp:text id="cus_name" label="成员客户名称" />
		<emp:text id="grp_corre_type" label="关联(集团)关联关系类型" dictname="STD_ZB_GRP_CO_TYPE"/>
		<emp:text id="cus_manager" label="客户经理" />
		<emp:text id="main_br_id" label="主管机构" />
		<emp:text id="grp_no" label="关联(集团)编号" hidden="true"/>
	</emp:table>
				
</body>
</html>
</emp:page>