<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
	.confirm {
		color: red;
	}
</style>
<script type="text/javascript">

	function doGetAddCusGrpInfoPage(){
		var url = '<emp:url action="getCusGrpInfoAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

	function doDeleteCusGrpInfo(){
		var paramStr = CusGrpInfoList._obj.getParamStr(['grp_no','parent_cus_id','manager_id','manager_br_id']);
		if (paramStr != null) {
			var grp_no = CusGrpInfoList._obj.getParamValue(['grp_no']);
			var url = '<emp:url action = "checkLmtApplyAndLmtModApp.do"/>&grp_no=' + grp_no;;
			var handleSuccess = function(o){
				if(o.responseText != undefined){
					try{
						var jsonstr = eval( "(" + o.responseText + ")" );
					}catch (e) {
		 				alert("删除集团成员校验失败!");
		 			}
					var Msg = jsonstr.backMsg;
					if(Msg == "0"){
						alert("该集团有正在做的关联授信业务，请先完成该集团的关联授信业务!");
						return ;
					}
					if(Msg == "1"){
						alert("该集团有正在做的关联变更授信业务，请先完成该集团的关联变更授信业务!");
						return ;
					}
					if(Msg == "3"){
						alert("该集团有正在做的关联集团客户变更，请先完成该集团的关联集团客户变更!");
						return ;
					}
					/**add by lisj 2015-5-20 需求编号：XD150504034 贷后管理常规检查任务改造,判断关联集团是否存在贷后任务 begin**/
					if(Msg == "4"){
						alert("该集团存在贷后任务，不允许做解散操作!");
						return ;
					}
					/**add by lisj 2015-5-20 需求编号：XD150504034 贷后管理常规检查任务改造,判断关联集团是否存在贷后任务 end**/
					if(confirm("是否确认要删除？")){
						if(confirm("将会删除该集团和集团向下的所有集团成员！\n集团的成员将会重新生成各自的独立授信协议！\n请谨慎！！！")){
							var url = '<emp:url action="deleteCusGrpInfoRecord.do"/>?'+paramStr;
							url = EMPTools.encodeURI(url);
							window.location = url;
						}
					}
				}else{
					alert("删除集团成员校验失败!");
				}
			};
			var handleFailure = function(o){
		
				alert("删除集团成员校验失败!");
			};
				
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			}
			YAHOO.util.Connect.asyncRequest('GET',url,callback);
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doGetUpdateCusGrpInfoPage(){
		var paramStr = CusGrpInfoList._obj.getParamStr(['grp_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusGrpInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doViewCusGrpInfo(){
		var paramStr = CusGrpInfoList._obj.getParamStr(['grp_no']);
		if (paramStr != null) {
			var url = '<emp:url action="queryCusGrpInfoDetail.do"/>?'+paramStr+"&op=view";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doQuery(){
		var form = document.getElementById('queryForm');
		CusGrpInfo._toForm(form);
		CusGrpInfoList._obj.ajaxQuery(null,form);
	};

	function doReset(){
		page.dataGroups.CusGrpInfoGroup.reset();
	};
	
	//主管客户经理
	function setconId(data){
		CusGrpInfo.manager_id._setValue(data.actorno._getValue());
	}
	/*--user code begin--*/

	/*--user code end--*/

</script>
</head>

<body class="page_content">
	<form action="#" id="queryForm">
	</form>
	<emp:gridLayout id="CusGrpInfoGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CusGrpInfo.grp_no" label="关联(集团)编号" />
			<emp:text id="CusGrpInfo.grp_name" label="关联(集团)名称" />
			<emp:text id="CusGrpInfo.grp_member_name" label="关联(集团)成员名称" />
			<emp:text id="CusGrpInfo.parent_cus_id" label="主(集团)客户码" />
			<emp:pop id="CusGrpInfo.manager_id" label="主管客户经理编号" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
	</emp:gridLayout>

	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
<!--		<emp:button id="getAddCusGrpInfoPage" label="新增" op="add"/>-->
<!--		<emp:button id="getUpdateCusGrpInfoPage" label="修改" op="update"/>-->
		<emp:button id="deleteCusGrpInfo" label="解散" op="remove"/>
		<emp:button id="viewCusGrpInfo" label="查看" op="view"/>
	</div>
	<emp:table icollName="CusGrpInfoList" pageMode="true" url="pageCusGrpInfoQueryforMain.do">
		<emp:text id="grp_no" label="关联(集团)编号" />
		<emp:text id="grp_name" label="关联(集团)名称" />
		<emp:text id="parent_cus_id" label="主(集团)客户码" />
		<emp:text id="parent_cus_id_displayname" label="主(集团)名称" />
		<emp:text id="manager_id_displayname" label="主管客户经理" />
		<emp:text id="manager_br_id_displayname" label="主管机构" />
		<emp:text id="manager_id" label="主管客户经理" hidden="true"/>
		<emp:text id="manager_br_id" label="主管机构" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>