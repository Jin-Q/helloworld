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
		LmtAppNameList._toForm(form);
		LmtAppNameListList._obj.ajaxQuery(null,form);
	};
	
	function doUpdate() {
		var paramStr = LmtAppNameListList._obj.getParamStr(['serno','cus_id']);
		if (paramStr != null) {
			var single_max_amt = "${context.single_max_amt}";
			var end_date = "${context.end_date}";
			var url = '<emp:url action="getLmtAppNameListUpdatePage.do"/>?'+paramStr + '&single_max_amt=' + single_max_amt + '&end_date=' + end_date;
			url = EMPTools.encodeURI(url);
			var openWin = window.open(url,'newwindow','height=600, width=800, top=120, left=200, toolbar=no, menubar=no, scrollbars=auto, resizable=yes,location=no, status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewLmtAppNameList() {
		var paramStr = LmtAppNameListList._obj.getParamStr(['serno','cus_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAppNameListViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height=600, width=800, top=120, left=200, toolbar=no, menubar=no, scrollbars=auto, resizable=yes,location=no, status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};
	//删除
	function doDeleteLmtAppNameList() {
		var paramStr = LmtAppNameListList._obj.getParamStr(['serno','cus_id']);
		if (paramStr != null) {
			//联保小组时进入修改页面
			var single_max_amt = "${context.single_max_amt}";
			var end_date = "${context.end_date}";
			if(confirm("是否确认要删除？")){
				var handSuc = function(o){
					if(o.responseText !== undefined) {
						try { var jsonstr = eval("("+o.responseText+")"); } 
						catch(e) {
						alert("数据库操作失败!");
						return;
						}
						var flag = jsonstr.flag;
						if(flag == "success"){
							alert("删除成功!");
							window.location.reload();
						}
					}
				};
			    var handFail = function(o){};
			    var callback = {
			    	success:handSuc,
			    	failure:handFail
			    };
			    var url = '<emp:url action="deleteLmtAppNameListRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.LmtAppNameListGroup.reset();
	};
	
	/*--user code begin--*/
	function doAdd(){
		var app_flag = "${context.app_flag}";
		var serno = '${context.serno}';
		var agr_no = '${context.agr_no}';
		
		var url = '<emp:url action="getLmtAppNameListAddPage.do"/>?' + '&serno=' + serno + 
			'&app_flag='+app_flag+'&agr_no='+agr_no;
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow','height=600, width=800, top=120, left=220, toolbar=no, menubar=no, scrollbars=auto, resizable=yes,location=no, status=no');
	}
	
	function doReturn() {
		var all = '${context.all}';
		var url = "";
		if( all == "yes" ){
			url = '<emp:url action="queryLmtAgrBizAreaList.do"/>?process=${context.process}';
		}else if( all == "no" ){
			url = '<emp:url action="queryLmtAgrBizAreaList.do"/>?process=${context.process}';
		}else{
			url = '<emp:url action="queryLmtAppJoinBackList.do"/>?process=${context.process}';
		}
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	
	<emp:gridLayout id="LmtAppNameListGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="LmtAppNameList.serno" label="申请编号" hidden="true"/>
		<emp:text id="LmtAppNameList.cus_id" label="客户码" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left" id="divBtns1" >
		<emp:actButton id="add" label="新增" op="add"/>
		<emp:actButton id="update" label="修改" op="update"/>
		<emp:actButton id="deleteLmtAppNameList" label="删除" op="remove"/>
		<emp:actButton id="viewLmtAppNameList" label="查看" op="view"/>
	</div>

	<emp:table icollName="LmtAppNameListList" pageMode="true" url="pageLmtAppNameListQuery.do" reqParams="app_flag=${context.app_flag}&agr_no=${context.agr_no}&serno=${context.serno}&all=${context.all}">
		<emp:text id="serno" label="申请编号" />
		<emp:text id="sub_type" label="分项类别" dictname="STD_LMT_PROJ_TYPE" hidden="true"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="cus_type" label="客户类型" dictname="STD_ZB_CUS_TYPE" />
		<emp:text id="is_limit_set" label="是否进行额度设置" dictname="STD_ZX_YES_NO" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>
    