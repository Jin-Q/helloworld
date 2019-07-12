<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>子表列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doGetUpdateCusGrpMemberApplyPage() {
		
		var paramStr = CusGrpMemberApplyList._obj.getParamStr(['grp_no','cus_id','serno']);
		if (paramStr!=null) {
			var url = '<emp:url action="getCusGrpInfoApplyCusGrpMemberApplyUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		}else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusGrpMemberApplyPage(){
		var grp_no = window.parent.window.CusGrpInfoApply.grp_no._getValue();
		var serno = window.parent.window.CusGrpInfoApply.serno._getValue();
		var url = '<emp:url action="getCusGrpInfoApplyCusGrpMemberApplyAddPage.do"/>?CusGrpMemberApply.grp_no='+grp_no+'&CusGrpMemberApply.serno='+serno;
		url = EMPTools.encodeURI(url);
		EMPTools.openWindow(url,'newwindow');
	};
	
	function doDeleteCusGrpMemberApply() {
		
		var data = CusGrpMemberApplyList._obj.getSelectedData();
		if (data[0]) {
			var cus_id = data[0].cus_id._getValue();
			if(parent_cus_id==cus_id){
				alert("主申请关联(集团)客户不能删除");
			}else {
				
				var paramStr = CusGrpMemberApplyList._obj.getParamStr(['grp_no','cus_id','serno']);
				if (paramStr != null) {
					if(confirm("是否确认要删除？")){
						var url = '<emp:url action="deleteCusGrpInfoApplyCusGrpMemberApplyRecord.do"/>?'+paramStr;
						url = EMPTools.encodeURI(url);
						window.location = url;
					}
				} 
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusGrpMemberApply() {
		var paramStr = CusGrpMemberApplyList._obj.getParamStr(['grp_no','cus_id','serno']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryCusGrpInfoApplyCusGrpMemberApplyDetail.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		}else {
			alert('请先选择一条记录！');
		}
	};

	function doGetCusRelation(){
		var pCusId = window.parent.window.CusGrpInfoApply.parent_cus_id._getValue();
		var pCusName = window.parent.window.CusGrpInfoApply.parent_cus_name._getValue();
		var url = '<emp:url action="GetCusRelTreeOp.do"/>?cus_id='+pCusId+'&cus_name='+pCusName;
		url = EMPTools.encodeURI(url);
		EMPTools.openWindow(url,'newwindow');
	}
	function doViewCusGrpRe(){
		var paramStr = CusGrpMemberApplyList._obj.getParamStr(['cus_id']);
		if (paramStr!=null) {
			var url = '<emp:url action="GetCusRelTreeOp.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			EMPTools.openWindow(url,'newwindow');
		} else {
			alert('请先选择一条记录！');
		}
	}
	/*--user code begin--*/
	function doLoad(){
		hidden_button = "${context.hidden_button}";
		if(hidden_button == 'true'){
			document.getElementById('button_getAddCusGrpMemberApplyPage').style.display = 'none';
			document.getElementById('button_getUpdateCusGrpMemberApplyPage').style.display = 'none';
			document.getElementById('button_deleteCusGrpMemberApply').style.display = 'none';
		}
		parent_cus_id = "${context.CusGrpInfoApply.parent_cus_id}";
	};
	function doSel(){
		var selObj = CusGrpMemberApplyList._obj.getSelectedData()[0];
		var cus_id=selObj.cus_id._getValue();
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+cus_id;
		url=EMPTools.encodeURI(url);
		windowName = Math.ceil(Math.random()*50000000);
		EMPTools.openWindow(url,windowName+"",'height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	/*--user code end--*/
	
</script>
</head>

<body class="page_content" onload="doLoad()">

	<div align="left">
		<emp:button id="getAddCusGrpMemberApplyPage" label="新增" />
		<emp:button id="getUpdateCusGrpMemberApplyPage" label="修改" />
		<emp:button id="deleteCusGrpMemberApply" label="删除" />
		<emp:button id="viewCusGrpMemberApply" label="查看" />
		<emp:button id="viewCusGrpRe" label="客户关联信息查看" mouseoutCss="button100" mousedownCss="button100" mouseoverCss="button100" mouseupCss="button100"/>
	</div>
							
	<emp:table icollName="CusGrpMemberApplyList" pageMode="true" url="pageCusGrpInfoApplyCusGrpMemberApplyQuery.do" reqParams="CusGrpInfoApply.grp_no=${context.CusGrpInfoApply.grp_no}&CusGrpInfoApply.parent_cus_id=${context.CusGrpInfoApply.parent_cus_id}&CusGrpInfoApply.serno=${context.CusGrpInfoApply.serno}">

		<emp:text id="serno" label="申请编号" />
		<emp:link id="cus_id" label="成员客户码" operation="sel"/>
		<emp:link id="cus_name" label="成员客户名称" operation="sel"/>
		<emp:text id="grp_corre_type" label="关联(集团)关联关系类型" dictname="STD_ZB_GROUP_TYPE"/>
		<emp:text id="manager_id_displayname" label="主管客户经理" />
		<emp:text id="manager_br_id_displayname" label="主管机构" />
		<emp:text id="cus_type" label="客户类型" dictname="STD_ZB_CUS_TYPE"  hidden="true"/>
		<emp:text id="grp_no" label="关联(集团)编号" hidden="true"/>
		<emp:text id="gen_type" label="生成类型" dictname="STD_ZB_GEN_TYPE" hidden="true"/>
	</emp:table>

</body>
</html>
</emp:page>