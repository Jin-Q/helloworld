<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<style type="text/css">
.required{
	color: red;
};
.emp_field_select_select {
border: 1px solid #b7b7b7;
text-align:left;
width:210px;
}
.emp_field_text_input {
border: 1px solid #b7b7b7;
text-align:left;
width:210px;
}
</style>
<script type="text/javascript">

	/*--user code start--*/
	
	function doAdd(){
		if(!HomepageGadget._checkAll()){
			return false;
		}
		
		var form =  document.getElementById("submitForm");
		HomepageGadget._toForm(form);
		form.action="<emp:url action='registHomePageGadget.do'/>&operator=regist";
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

	//回调方法
	function set_duty_ids(data){
		HomepageGadget.gadget_dutys._setValue(data);
	}

	//返回首页
	function doReturn(){
		var url = '<emp:url action="queryHomePageGadget.do"/>&operator=queryByUser';
		url = EMPTools.encodeURI(url);
		window.location = url;
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="">
	
	<emp:form id="submitForm" action="" method="POST">

		<div align="center">
		<emp:gridLayout id="RegistGadgetGroup" title="Gadget注册页面" maxColumn="1">
				<emp:text id="HomepageGadget.gadget_id" label="工具编号"  hidden="true" />
				<emp:text id="HomepageGadget.gadget_title" label="工具标题" required="true" />
				<emp:select id="HomepageGadget.gadget_color" label="工具框颜色" required="true" dictname="HOMEPAGE_COLOR"/>
				<emp:text id="HomepageGadget.gadget_height" label="工具高度" required="true" />
				<emp:text id="HomepageGadget.gadget_width" label="工具宽度" required="true" defvalue="100%" hidden="false"/>
				<emp:text id="HomepageGadget.gadget_url" label="工具URL" required="true" /> 
				<emp:text id="HomepageGadget.gadget_url_resize" label="工具最大化时URL" required="true" />
				<!-- 所有人查看:ALL或空  可根据岗位设置权限，多个岗位以,分割   应做成POP框选择 -->
				<!--  emp:text id="HomepageGadget.gadget_dutys" label="工具添加权限" required="true" /-->
				<!-- emp:pop id="HomepageGadget.gadget_dutys" label="工具添加权限"  required="false"  colSpan="2" url="astmesQueryMultiSDutyPop.do"   hidden="true" returnMethod="set_duty_ids" cssElementClass="emp_field_text_input_blue" -->
				
				<emp:textarea id="HomepageGadget.gadget_remark" label="描述"/>
				<emp:text id="HomepageGadget.gadget_author" label="工具作者" required="false" />
			
		</emp:gridLayout>
		</div>
		<div align="center" id="01">
			<br>
			<emp:button id="add" label="确定" />&nbsp&nbsp
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

