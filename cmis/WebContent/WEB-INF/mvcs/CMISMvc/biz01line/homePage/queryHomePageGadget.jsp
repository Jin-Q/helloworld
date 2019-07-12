<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code start --*/

	function doAddGadgetForUser(){
		//取当前选中行数据
		var data = null
		var gadgetId = null;

		try{
			data = HomepageGadgetList._obj.getSelectedData();
			gadgetId = data[0].gadget_id._getValue();

	        var organNo = "${context.organNo}";
	        //添加审批报备小工具时，判断登录机构是否为授信审批管理部
	        if(gadgetId == "FFFA274300AB12F12B005937DF6EE879"){
               if(organNo != "9350500012"){
                  alert("本机构不能添加该项小工具!");
                  return;
               }
		    }
		    var dutyNoList = "${context.dutyNoList}";
		    if(gadgetId == "FFFA27870189FF97ED1E2B4E2B815291"){
		    	var res = dutyNoList.indexOf("S0107"); 
	               if(res<0){
	                  alert("本岗位不能添加该项小工具!");
	                  return;
	               }
			    }
		}catch(e){
			alert("请选中一条记录");
			return ;
		}
        
		var form =  document.getElementById("gadgetForm");
		form.action="<emp:url action='addCustomHomePageGadget.do'/>&operator=add&gadgetId="+gadgetId;
		form.method = "post"; 
		
		 var handleSuccess = function(o){
				if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr define error!"+e);
							return;
						}
						var flag = jsonstr.flag;
						if(flag=="sucess"){
							alert("该小工具添加成功");
							window.location.reload();
							/**
							var url = '<emp:url action="queryHomePageGadget.do"/>&operator=queryByUser';
							url = EMPTools.encodeURI(url);
							window.location = url;
							*/
						}else {
							 alert("添加失败");
							 return;
						}
				}
			};
			var handleFailure = function(o){	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var postData = YAHOO.util.Connect.setForm(form);	 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData) 
	}

	//返回首页
	function doReturn(){
		var url = '<emp:url action="queryCustomHomePage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	}


	//注册Gadget
	function doRegistGadget(){
		var url = '<emp:url action="addHomePageGadget.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	}

	//编辑Gadget
	function doEditGadget(){
		var data = null
		var gadgetId = null;

		try{
			data = HomepageGadgetList._obj.getSelectedData();
			gadgetId = data[0].gadget_id._getValue();
		}catch(e){
			alert("请中一条记录");
			return ;
		}
		
		
		var url = '<emp:url action="updateHomePageGadgetPage.do"/>?operator=queryDetail&gadget_id='+gadgetId;
		url = EMPTools.encodeURI(url);
		window.location = url;
		
	}
	
	//删除Gadget
	function doDeleteGadget(){
		//取当前选中行数据
		var data =HomepageGadgetList._obj.getSelectedData()
		if(data == null || data == ""){
			alert("请先选择一条工具记录！");
			return;
		}
		var gadgetId = data[0].gadget_id._getValue();
		
		if(!confirm("您确定要删除该小工具")){
			return ;
		}
		
		var form =  document.getElementById("gadgetForm");
		form.action="<emp:url action='deleteHomePageGadget.do'/>&operator=delete&gadget_id="+gadgetId;
		form.method = "post"; 
		
		 var handleSuccess = function(o){
				if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr define error!"+e);
							return;
						}
						var flag = jsonstr.flag;
						if(flag=="sucess"){
							alert("该小工具删除成功");
							
							var url = '<emp:url action="queryHomePageGadget.do"/>&operator=queryByUser';
							url = EMPTools.encodeURI(url);
							window.location = url;
						}else {
							 alert("添加失败");
							 return;
						}
				}
			};
			var handleFailure = function(o){	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var postData = YAHOO.util.Connect.setForm(form);	 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData) 
	}
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<div align="left">
		<emp:button id="addGadgetForUser" label="添加小工具" op="add"/>  <!-- 一般用户 -->
		<emp:button id="registGadget" label="注册gadget" op="update"/>  <!-- 管理员 --> 
		<emp:button id="editGadget" label="编辑gadget" op="view"/>  <!-- 管理员 -->
		<emp:button id="deleteGadget" label="删除gadget" op="remove"/>  <!-- 管理员 -->
	</div>
	
	<emp:table icollName="HomepageGadgetList" pageMode="true" url="PageHomePageGadget"> 
		<emp:text id="gadget_id" label="工具编号" hidden="true"/>
		<emp:text id="gadget_title" label="工具标题" />
		<emp:text id="gadget_color" label="工具框颜色" dictname="HOMEPAGE_COLOR"/>
		<emp:text id="gadget_height" label="工具高度" />
		<emp:text id="gadget_width" label="工具宽度"  hidden="true"/>
		<emp:text id="gadget_url" label="工具URL" />
		<emp:text id="gadget_url_resize" label="最大化时工具URL" />
		<emp:text id="gadget_remark" label="描述" />
		<emp:text id="gadget_author" label="作者" hidden="true"/>
	</emp:table>
	
	<form  method="POST" action="#" id="gadgetForm"> 
	</form>
	
	<span id="btnSpan">
		<button onclick="doReturn();">返回</button>
		</span>
</body>
</html>
</emp:page>
    