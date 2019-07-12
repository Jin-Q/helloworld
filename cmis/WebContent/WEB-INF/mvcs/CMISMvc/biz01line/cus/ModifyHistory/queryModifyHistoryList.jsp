<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<link href="<emp:file fileName='styles/start/jquery-ui-1.7.1.custom.css'/>" rel="stylesheet" type="text/css" media="screen" />
<script type="text/javascript" src="<emp:file fileName='scripts/jquery-1.3.2.js'/>"></script>
<script type="text/javascript" src="<emp:file fileName='scripts/jquery-ui-1.7.1.custom.min.js'/>"></script>
<script type="text/javascript" src="<emp:file fileName='scripts/jquery.cmisDialogs.js'/>"></script>
<script type="text/javascript">
    $(document).ready(function(){
        //滚动条id
        var scrollWindow = $("#relatedtabs_ModifyHistory_tabs_tabs");
        //点击链接对象
        var regDetail = $(".emp_field_link_a[class='emp_field_link_a']");
    });
    
	function doQuery(){
		var form = document.getElementById('queryForm');
		ModifyHistory._toForm(form);
		ModifyHistoryList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateModifyHistoryPage() {
		var paramStr = ModifyHistoryList._obj.getParamStr(['key_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getModifyHistoryUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewModifyHistory() {
		var paramStr = ModifyHistoryList._obj.getParamStr(['key_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getModifyHistoryViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			//window.location = url;
			openBigWindow(url);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddModifyHistoryPage() {
		var url = '<emp:url action="getModifyHistoryAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteModifyHistory() {
		var paramStr = ModifyHistoryList._obj.getParamStr(['key_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteModifyHistoryRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.ModifyHistoryGroup.reset();
	};
	/*--user code begin--*/
	function showDetail(obj) {
	   $("#queryForm").after("<div id='dlg'></div>");
            obj.parentNode.parentNode.click();
            var keyId = ModifyHistoryList._obj.getParamValue('key_id');
            $.post("<emp:url action='getModifyHistoryViewPage.do'/>","key_id=" + keyId,function(data){
                //alert(data);
                $("#dlg").html(data);
                $("#dlg").dialog({
                    width:"600",
                    modal:"true"
                });
                doDetailOnload();
            });
	}
	
	function doDetailOnload() {
        var size=ModifyHistoryDetailList.size;
        var i=0;
        var tableName = ModifyHistoryList._obj.getParamValue('table_name');
        for(i=0;i<size;i++){
            var id = ModifyHistoryDetailList._obj.data[i]['modify_name']._getValue();
            var obj = this.parent.document.getElementById("emp_field_" + tableName + "." + id);
            var name = obj.title;
            ModifyHistoryDetailList._obj.data[i]['modify_name']._setValue(name);
            if(obj.type == "Select") {
                var selectObj = this.parent.document.getElementById(tableName+"."+id);
                var oldValue = ModifyHistoryDetailList._obj.data[i]['modify_old_value']._getValue();
                var newValue = ModifyHistoryDetailList._obj.data[i]['modify_new_value']._getValue();
                var oldValueHtml = "";
                var newValueHtml = "";
                if(oldValue != "") {
                    oldValueHtml = parent.$("select[name='" + tableName + "." + id + "'] option[value=" + oldValue + "]").html();
                }
                if(newValue != "") {
                    newValueHtml = parent.$("select[name='" + tableName + "." + id + "'] option[value=" + newValue + "]").html();
                }
                ModifyHistoryDetailList._obj.data[i]['modify_old_value']._setValue(oldValueHtml);
                ModifyHistoryDetailList._obj.data[i]['modify_new_value']._setValue(newValueHtml);
                
            }
        }
    }

	function showDetail2(obj) {
        var keyId = ModifyHistoryList._obj.getParamValue('key_id');
        var url=   "<emp:url action='getModifyHistoryViewPage.do'/>?key_id=" + keyId;
        url = EMPTools.encodeURI(url);
        openBigWindow(url);
	}
	
	function openBigWindow(url){
		var str = "height=600,width=1000,top=100,left=100,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=yes";
		//EMPTools.mask();
		window.open(url,'newwindowgrt',str);
		//alert("-===");
		//window.focus();
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	   <emp:text id="ModifyHistory.cus_id" label="客户代码" hidden="true" defvalue="${context.cus_id}"/>
	</form>
	<emp:button id="viewModifyHistory" label="查看" op=""/>
	
	<emp:table icollName="ModifyHistoryList" pageMode="true" url="pageModifyHistoryQuery.do" reqParams="cus_id=${context.cus_id}">
		<emp:text id="key_id" label="key_id" hidden="true"/>
		<emp:text id="table_name" label="table_name" hidden="true" />
		<emp:text id="cus_id" label="用户id" hidden="true" />
		<emp:text id="modify_time" label="修改时间" />
		<emp:text id="modify_user_id_displayname" label="修改用户" />
		<emp:text id="modify_user_br_id_displayname" label="修改机构"  hidden="true"/>
		<emp:text id="modify_user_id" label="修改用户" hidden="true"/>
        <emp:text id="modify_user_br_id" label="修改机构" hidden="true" />
		<emp:text id="modify_user_ip" label="修改IP" />
		<emp:text id="modify_status" label="状态" hidden="true" />
		<emp:link id="detail" label="修改内容" opName="点击查看..." onclick="showDetail2(this)" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    