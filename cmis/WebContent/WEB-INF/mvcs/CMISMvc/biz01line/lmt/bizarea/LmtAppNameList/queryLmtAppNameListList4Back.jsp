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

	//查看退圈申请名单
	function doViewLmtAppNameList() {
		var paramStr = LmtAppNameListList._obj.getParamStr(['serno','cus_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtAppNameListViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height='+window.screen.availHeight*0.6+',width='+window.screen.availWidth*0.6+', top=120, left=200, toolbar=no, menubar=no, scrollbars=auto, resizable=yes,location=no, status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};

	//查看原圈商名单详细信息
	function doViewLmtAgrDetails(){
		var paramStr = LmtNameListList._obj.getParamStr(['cus_id','agr_no']);
		if(paramStr != null){
			var url = '<emp:url action="getLmtNameListViewPage.do"/>?'+paramStr+"&type=bizArea";
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height='+window.screen.availHeight*0.6+',width='+window.screen.availWidth*0.6+', top=120, left=200, toolbar=no, menubar=no, scrollbars=auto, resizable=yes,location=no, status=no');
		}else {
			alert('请先选择一条记录！');
		}
	}
	
	/*--user code begin--*/

	function doReturn() {
		var all = '${context.all}';
		var url = "";
		if( all == "yes" ){
			url = '<emp:url action="queryLmtAgrBizAreaList.do"/>?process=${context.process}&menuId=joinBackBizArea';
		}else if( all == "no" ){
			url = '<emp:url action="queryLmtAgrBizAreaList.do"/>?process=${context.process}&menuId=joinBackBizArea';
		}else{
			url = '<emp:url action="queryLmtAppJoinBackList.do"/>?process=${context.process}&menuId=joinBackBizArea';
		}
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	/*--- tangzf ---*/
	//剔除
	function doAddNameToAppList(){
		var serno = '${context.serno}';
		var paramStr = LmtNameListList._obj.getParamStr(['cus_id','agr_no']);
		if (paramStr != null) {
			var url = '<emp:url action="addNameToAppList.do"/>?'+paramStr+'&serno='+serno;
			url = EMPTools.encodeURI(url);
			var handleSuccess = function(o){
				EMPTools.unmask();
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("剔除失败!");
						return;
					}
					var flag=jsonstr.flag;	
					if(flag=="success"){
						window.location.reload();								
					}else{
						alert(flag);
					}
				}
			};
			var handleFailure = function(o){
				alert("剔除失败，请联系管理员!");
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			}; 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		} else {
			alert('请先选择一条记录！');
		}
	}

	//删除
	function doDeleteLmtAppNameList() {
		var paramStr = LmtAppNameListList._obj.getParamStr(['serno','cus_id']);
		if (paramStr != null) {
			//联保小组时进入修改页面
			if(confirm("是否确认要删除？")){
				var handSuc = function(o){
					if(o.responseText !== undefined) {
						try { var jsonstr = eval("("+o.responseText+")"); } 
						catch(e) {
							alert("删除失败!");
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
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm"></form>
	<div class='emp_gridlayout_title'>圈商名单&nbsp;</div>
	<div align="left">
		<emp:actButton id="addNameToAppList" label="剔除" op="update"/>
		<emp:actButton id="viewLmtAgrDetails" label="查看" op="view"/>
	</div>
	<emp:table icollName="LmtNameListList" pageMode="false" url="">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="agr_no" label="协议编号" />
	</emp:table>
	<br>
	
	<div class='emp_gridlayout_title'>退圈名单&nbsp;</div>
	<div align="left" id="divBtns1" >
		<emp:actButton id="deleteLmtAppNameList" label="删除" op="remove"/>
		<emp:actButton id="viewLmtAppNameList" label="查看" op="view"/>
	</div>
	<emp:table icollName="LmtAppNameListList" pageMode="false" url="pageLmtAppNameListQuery.do" reqParams="biz_area_no=${context.biz_area_no}&serno=${context.serno}&all=${context.all}">
		<emp:text id="serno" label="申请编号" hidden="true"/>
		<emp:text id="sub_type" label="分项类别" dictname="STD_LMT_PROJ_TYPE" hidden="true"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="cus_type" label="客户类型" dictname="STD_ZB_CUS_TYPE" />
	</emp:table>
	<div align="center">
	</div>
</body>
</html>
</emp:page>
    