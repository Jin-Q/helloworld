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

	function doGetAddCusGrpInfoHisPage(){
		var url = '<emp:url action="getCusGrpInfoHisAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

	function doDeleteCusGrpInfoHis(){
		
		var paramStr = CusGrpInfoHisList._obj.getParamStr(['grp_no','parent_cus_id','manager_id','manager_br_id']);
		if (paramStr != null) {
			var grp_no = CusGrpInfoHisList._obj.getParamValue(['grp_no']);
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
					if(confirm("是否确认要删除？")){
						if(confirm("将会删除该集团和集团向下的所有集团成员！\n集团的成员将会重新生成各自的独立授信协议！\n请谨慎！！！")){
							var url = '<emp:url action="deleteCusGrpInfoHisRecord.do"/>?'+paramStr;
							url = EMPTools.encodeURI(url);
							window.location = url;
						}
					}
				}else{
					alert("删除集团成员校验失败!");
				}
			};
			var handleFailure = function(o){
		
				alert("担保品分析校验失败!");
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

	function doGetUpdateCusGrpInfoHisPage(){
		
		var paramStr = CusGrpInfoHisList._obj.getParamStr(['grp_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusGrpInfoHisUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doViewCusGrpInfoHis(){
		var paramStr = CusGrpInfoHisList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusGrpInfoHisViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doQuery(){
		var form = document.getElementById('queryForm');
		CusGrpInfoHis._toForm(form);
		CusGrpInfoHisList._obj.ajaxQuery(null,form);
	};

	function doReset(){
		page.dataGroups.CusGrpInfoHisGroup.reset();
	};

	/*--user code begin--*/

	/*--user code end--*/

</script>
</head>

<body class="page_content">
	<form action="#" id="queryForm">
	</form>

	<emp:gridLayout id="CusGrpInfoHisGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CusGrpInfoHis.grp_no" label="关联(集团)编号" />
			<emp:text id="CusGrpInfoHis.grp_name" label="关联(集团)名称" />
			<emp:text id="CusGrpInfoHis.parent_cus_id" label="主(集团)客户码" />
			<emp:text id="CusGrpInfoHis.parent_cus_name" label="主(集团)名称" />
			<emp:text id="CusGrpInfoHis.cus_manager" label="主办客户经理编号" />
	</emp:gridLayout>

	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="getAddCusGrpInfoHisPage" label="新增" op="add"/>
		<emp:button id="getUpdateCusGrpInfoHisPage" label="修改" op="update"/>
		<emp:button id="deleteCusGrpInfoHis" label="解散" op="remove"/>
		<emp:button id="viewCusGrpInfoHis" label="查看" op="view"/>
	</div>
	<emp:table icollName="CusGrpInfoHisList" pageMode="true" url="pageCusGrpInfoHisQueryforMain.do">
		<emp:text id="serno" label="流水号" />
		<emp:text id="grp_no" label="关联(集团)编号" />
		<emp:text id="grp_name" label="关联(集团)名称" />
		<emp:text id="parent_cus_id" label="主(集团)客户码" />
		<emp:text id="parent_cus_name" label="主(集团)名称" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任人机构" />
		<emp:text id="manager_id" label="主办客户经理" hidden="true"/>
		<emp:text id="manager_br_id" label="主办行" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>