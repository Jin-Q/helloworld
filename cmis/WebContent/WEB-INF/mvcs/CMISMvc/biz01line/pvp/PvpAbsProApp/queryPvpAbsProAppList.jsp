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
		PvpAbsProApp._toForm(form);
		PvpAbsProAppList._obj.ajaxQuery(null,form);
	};
	
	function doViewPvpAbsProApp() {
		var paramStr = PvpAbsProAppList._obj.getParamStr(['pre_package_serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getPvpAbsProAppViewPage.do"/>?'+paramStr+"&biz_type=${context.biz_type}";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPvpAbsProAppPage() {
		var url = '<emp:url action="getPvpAbsProAppAddPage.do"/>?biz_type=${context.biz_type}';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePvpAbsProApp() {
		var paramStr = PvpAbsProAppList._obj.getParamStr(['pre_package_serno']);
		if (paramStr != null) {
			var prc_status = PvpAbsProAppList._obj.getParamValue(['prc_status']);
			if(prc_status!='1'){
				alert('该状态下的预封包信息不能删除!');
				return;
			}
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o){
					if(o.responseText !== undefined){
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr1 define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						if(flag == "success"){
							alert("删除成功!");
							window.location.reload();
						}else {
							alert("删除失败!");
						}
					}
				};
				var handleFailure = function(o){
					alert("异步请求出错！");	
				};
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				};
				var url = '<emp:url action="deletePvpAbsProAppRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)	
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PvpAbsProAppGroup.reset();
	};

	function doGetApplyPvpAbsProAppPage() {
		var paramStr = PvpAbsProAppList._obj.getParamStr(['pre_package_serno']);
		if (paramStr != null) {
			var biz_type = '${context.biz_type}';
			var prc_status = PvpAbsProAppList._obj.getParamValue(['prc_status']);
			var pre_package_date = PvpAbsProAppList._obj.getParamValue(['pre_package_date']);
			var openday  ='${context.OPENDAY}';
			if(biz_type =='2' && (prc_status!='2')){
				alert('该状态下的申请不允许做出账复核操作!');
				return;
			}else if (biz_type =='3' && (prc_status!='3' && prc_status!='8')){
				alert('该处理状态不允许做出账处理操作!');
				return;
			}else if(biz_type =='3' && (pre_package_date!= openday)){
				alert('该批次只允许营业日当天做出账处理操作!');
				return;
			}
			var url = '<emp:url action="getPvpAbsProAppUpdatePage.do"/>?'+paramStr+'&biz_type=${context.biz_type}';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
    <emp:gridLayout id="PvpAbsProAppGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PvpAbsProApp.batch_no" label="批次号" />
			<emp:text id="PvpAbsProApp.batch_name" label="批次名称"/>
			<emp:text id="PvpAbsProApp.pre_package_date" label="预封包日期"/>
			<emp:text id="PvpAbsProApp.input_id" label="操作人员" />
			<emp:select id="PvpAbsProApp.prc_status" label="处理状态" dictname="PRC_STATUS" />
			<emp:text id="PvpAbsProApp.update_date" label="修改日期" />
	</emp:gridLayout>
   	<jsp:include page="/queryInclude.jsp" flush="true" />	
	<div align="left">
		<%
			String biz_type=request.getParameter("biz_type");
			if(biz_type!=null && biz_type.equals("1")){
		%>
		<emp:button id="getAddPvpAbsProAppPage" label="新增出账申请" />
		<emp:button id="getApplyPvpAbsProAppPage" label="修改" />
		<emp:button id="deletePvpAbsProApp" label="删除" />
		<%
			}
		%>
		<%
			if(biz_type!=null && biz_type.equals("2")){
		%>
		<emp:button id="getApplyPvpAbsProAppPage" label="出账复核" />
		<%
			}
		%>
		<%
			if(biz_type!=null && biz_type.equals("3")){
		%>
		<emp:button id="getApplyPvpAbsProAppPage" label="出账处理" />
		<%
			}
		%>
		<emp:button id="viewPvpAbsProApp" label="查看" />
	</div>

	<emp:table icollName="PvpAbsProAppList" pageMode="true" url="pagePvpAbsProAppQuery.do">
		<emp:text id="batch_no" label="批次号" />
		<emp:text id="batch_name" label="批次名称" />
		<emp:text id="trust_org_no" label="受托机构名称" />
		<emp:text id="is_this_org_service" label="是否本机构服务"  dictname="STD_ZX_YES_NO"  />
		<emp:text id="pre_package_serno" label="预封包流水号" hidden="true"/>
		<emp:text id="pre_package_name" label="预封包名称" hidden="true" />
		<emp:text id="input_id" label="操作人员" />
		<emp:text id="pre_package_date" label="预封包日期"/>
		<emp:text id="input_br_id" label="操作机构"  hidden="true" />
		<emp:text id="update_date" label="修改日期" />
		<emp:text id="prc_status" label="处理状态" dictname="PRC_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    