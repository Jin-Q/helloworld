<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	function doReset(){
		page.dataGroups.MortGuarantyBaseInfoGroup.reset();
	};
	function doGetUpdateMortGuarantyBaseInfoPage() {
		var paramStr = MortGuarantyBaseInfoList._obj.getParamStr(['guaranty_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getMortGuarantyBaseInfoUpdatePage.do"/>?op=update&cus_id=${context.cus_id}&'+paramStr+'&menuId=mort_maintain&tab=tab';
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=yes, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewMortGuarantyBaseInfo() {
		var paramStr = MortGuarantyBaseInfoList._obj.getParamStr(['guaranty_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getMortGuarantyBaseInfoViewPage.do"/>?op=view&'+paramStr+'&menuId=mort_maintain&tab=tab';
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddMortGuarantyBaseInfoPage() {
		//tab=tab用来控制关闭按钮
		var url = '<emp:url action="getMortGuarantyBaseInfoAddPage.do"/>?cus_id=${context.cus_id}&guar_cont_no=${context.guar_cont_no}&tab=tab';
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',param);
	};
	
	function doDeleteMortGuarantyBaseInfo() {
		var paramStr = MortGuarantyBaseInfoList._obj.getParamStr(['guaranty_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o) {
					if (o.responseText !== undefined) {
						try {
							var jsonstr = eval("(" + o.responseText + ")");
						} catch (e) {
							alert("Parse jsonstr define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						if("success" == flag){
							alert("记录已删除！");
							window.location.reload();
						}else{
							alert("删除失败！");
						}
					}
				};
				var handleFailure = function(o) {
				};
				var callback = {
					success :handleSuccess,
					failure :handleFailure
				};
				var url = '<emp:url action="deleteGrtGuarantyReRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
		 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
		} else {
			alert('请先选择一条记录！');
		}
	  }
	};
	function doQuery(){
		var form = document.getElementById('queryForm');
		MortGuarantyBaseInfo._toForm(form);
		MortGuarantyBaseInfoList._obj.ajaxQuery(null,form);
	};
	function doReturnMethod(){
		var data = MortGuarantyBaseInfoList._obj.getSelectedData();
		if (data != null && data.length !=0) {
			var status = data[0].guaranty_info_status._getValue();
			if(status=="1"){
				alert("此押品未登记完成，不能进行引入操作，请录入其押品评估信息后，再进行引入操作！");
			}else{
				var parentWin = EMPTools.getWindowOpener();
				eval("parentWin.${context.returnMethod}(data[0])");
				window.close();
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doSelect(){
		doReturnMethod();
	}
	function doCancel(){
		window.close();
	};
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="MortGuarantyBaseInfoGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="MortGuarantyBaseInfo.guaranty_no" label="押品编号" />
			<emp:text id="MortGuarantyBaseInfo.guaranty_name" label="押品名称" />
			<emp:select id="MortGuarantyBaseInfo.guaranty_cls" label="押品类别" dictname="STD_GUARANTY_TYPE" />
			<emp:select id="MortGuarantyBaseInfo.guaranty_info_status" label="押品信息状态" dictname="STD_MORT_STATE" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
		<emp:returnButton id="s1" label="引入"/>
		<!-- 
		<emp:actButton id="getAddMortGuarantyBaseInfoPage" label="新增" op="add" />
		<emp:actButton id="getUpdateMortGuarantyBaseInfoPage" label="修改" op="update"/>
		<emp:actButton id="deleteMortGuarantyBaseInfo" label="删除" op="remove"/>
		<emp:actButton id="viewMortGuarantyBaseInfo" label="查看" op="view"/>
		-->
	<emp:table icollName="MortGuarantyBaseInfoList" pageMode="true" url="pageMortGuarantyBaseInfoQuery.do?Intro=${context.Intro}&arpCD=${context.arpCD}&rel=${context.rel}&serno=${context.serno}">
	 
		<emp:text id="guaranty_no" label="押品编号" />
		<emp:text id="guaranty_name" label="押品名称" />
		<emp:text id="cus_id" label="抵质押人客户码" />
		<emp:text id="cus_id_displayname" label="抵质押人客户名称" />
		<emp:text id="guaranty_cls" label="押品类别" dictname="STD_GUARANTY_TYPE" />
		<emp:text id="guaranty_info_status" label="押品信息状态" dictname="STD_MORT_STATE" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:text id="input_date" label="登记日期" />
	</emp:table>
	<div align="left">
			<br>
 			<emp:returnButton id="s2" label="引入"/>
			<emp:button id="cancel" label="关闭" />
	</div>
</body>
</html>
</emp:page>
    