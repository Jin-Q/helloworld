<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	var serno = "${context.serno}";  //申请编号
	var cus_id_zz = "${context.cus_id}";//组长客户码

	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtAppNameList._toForm(form);
		LmtAppNameListList._obj.ajaxQuery(null,form);
	};
	
	/*--user code begin--*/
	function doOnload(){
		
	}
	//修改
	function doUpdate() {
		var paramStr = LmtAppNameListList._obj.getParamStr(['serno','cus_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtNameList_jointUpdatePage.do"/>?'+paramStr+"&cus_id_zz="+cus_id_zz;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	}
	//查看
	function doView() {
		var paramStr = LmtAppNameListList._obj.getParamStr(['serno','cus_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getLmtNameList_jointViewPage.do"/>?'+paramStr+"&cus_id_zz="+cus_id_zz+
				"&view=${context.view}";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	}
	//新增
	function doAddAppNameList() {
		var url = '<emp:url action="getLmtNameList_jointAddPage.do"/>?serno='+serno+"&cus_id_zz="+cus_id_zz;
		url = EMPTools.encodeURI(url);
		window.location = url;
	}

	//删除
	function doDelete() {
		var paramStr = LmtAppNameListList._obj.getParamStr(['serno','cus_id']);
		if (paramStr != null) {
			var cus_id = LmtAppNameListList._obj.getSelectedData()[0].cus_id._getValue();
			if(cus_id==cus_id_zz){//组长不能删除
				alert('联保小组组长不能删除！');
				return ;
			}
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
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnload()">
	<form  method="POST" action="#" id="queryForm">
	</form>
	
	<div align="left" id=btns1 >
		<emp:actButton id="addAppNameList" label="新增" op="add"/>
		<emp:actButton id="update" label="修改" op="update"/>
		<emp:actButton id="delete" label="删除" op="remove"/>
		<emp:actButton id="view" label="查看" op="view"/>
	</div>

	<emp:table icollName="LmtAppNameListList" pageMode="true" url="pageLmtNameList_jointQuery.do" reqParams="serno=${context.serno}" selectType="1" >
		<emp:text id="serno" label="联保申请编号" hidden="false"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="sub_type" label="分项类别" dictname="STD_LMT_PROJ_TYPE" />
		<emp:text id="bail_rate" label="保证金比例" dataType="Percent"/>
	</emp:table>
</body>
</html>
</emp:page>
    