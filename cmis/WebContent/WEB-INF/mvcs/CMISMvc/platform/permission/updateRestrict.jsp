<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>
<jsp:include page="/include.jsp" />
<style type="text/css">
.emp_field_textarea_textarea1 { /****** 长度、高度固定 ******/
	width: 600px;
	height: 100px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}
.emp_field_textarea_textarea2 { /****** 长度、高度固定 ******/
	width: 600px;
	height: 50px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
}
.labelCss {
	color:#000000;
	size: 10px;
}
</style>
<script type="text/javascript">
	function doAdd(){
		if(!SRowright._checkAll()){
			return;
		}
		var resourceid = SRowright.resourceid._getValue();
		var form = document.getElementById("submitForm");
		SRowright._toForm(form);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if(flag == "success"){
					alert("保存成功！");
					var url = '<emp:url action="getRestrictupdatePage.do"/>?resourceid='+resourceid;
					url = EMPTools.encodeURI(url);
					window.location = url;
				}else {
					alert("保存失败！");
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

		var url = '<emp:url action="updateRestrict.do"/>';
		url = EMPTools.encodeURI(url);
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData) 
	};
	//--------------删除记录集权限调用函数-------------
	function doDeleteRestrict(){
		if(confirm("是否确认要重置权限？重置权限会删除该资源配置的权限...")){
			var resourceid = SRowright.resourceid._getValue();
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					
					if(flag == "success"){
						alert("重置权限成功!");
						
						var url = '<emp:url action="getRestrictupdatePage.do"/>?resourceid='+resourceid;
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else {
						alert("重置权限失败！");
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
			var url = '<emp:url action="deleteRestrict.do"/>?resourceid='+resourceid;
			
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null) 
		}
	};	
</script>
</head>
<body  class="page_content">
	<emp:form id="submitForm" action="updateRestrict.do" method="POST">
		<emp:gridLayout id="SRowrightGroup" maxColumn="1" title="记录集权限配置信息">
			<emp:text id="SRowright.pk1" label="主键" maxlength="32" hidden="true" required="false" readonly="true" />
			<emp:text id="SRowright.resourceid" label="资源ID" maxlength="32" required="true" readonly="true" />
			<emp:text id="SRowright.cnname" label="资源名称" maxlength="60" required="false" />
			<emp:textarea id="SRowright.readtemp" label="读权限配置" maxlength="4000" required="false" cssElementClass="emp_field_textarea_textarea1"/>
			<emp:textarea id="SRowright.writetemp" label="写权限配置" maxlength="4000" required="false" cssElementClass="emp_field_textarea_textarea1"/>
			<emp:textarea id="SRowright.memo" label="描述" maxlength="200" required="false" cssElementClass="emp_field_textarea_textarea2"/>
		</emp:gridLayout>
		<emp:gridLayout id="SRowrightGroup" maxColumn="1" title="参数说明">
		<div class="labelCss">
			上述记录集权限中的读权限、写权限都是用拼装SQL来实现控制，对于SQL中的参数，替换方式为：'$变量名称$'
			1-6参数为单个值，7-8参数为多值</br>
			示例如下：读权限控制，当前登录人员可读：登录人员字段(input_id)='$currentUserName$'</br>
			当前登录角色可读：角色字段 in ($dutys$)
		</div>
		<emp:textarea id="SRowright.desc" label="参数说明" cssElementClass="emp_field_textarea_textarea1" defvalue="1.	当前登录人员：currentUserId
2.	当前登录人员名称：currentUserName
3.	当前登录机构：organNo
4.	当前登录机构名称：organName
5.	系统当前登录机构法人ID：artiOrganNo
6.	系统当前登录日期：OPENDAY
7.	系统当前登录人岗位：dutys(可能存在多个岗位信息)
8.	系统当前登录人角色ID：roles（可能存在多个角色信息）
" required="false"/>
		</emp:gridLayout>
		<div align="center">
				<br>
			<emp:button id="add" label="确定"/>
			<emp:button id="deleteRestrict" label="重置"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>

