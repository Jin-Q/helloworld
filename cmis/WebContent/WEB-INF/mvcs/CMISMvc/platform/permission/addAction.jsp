<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>
<jsp:include page="/include.jsp" />

<script type="text/javascript">

	function doSubmit(){
		var url = "<emp:url action='addActionSubmit.do'/>";
		EMPTools.doAjaxUpdateAndBack('submitForm',s_resourceaction,url,null,addBackFun);
	}
	/*
	*添加模块成功之后 调用的函数
	*/
	function addBackFun(){
	  //
	   window.parent.actionTree.getRootNode().reload();
	   doClose();
	}
		
	function doClose(){
		//page.dataGroups.s_resourceGroup.reset();
		if('${param.closeFun}' != ''){
              eval('${param.closeFun}');
              
		}else{
			window.close();
	    }
	};
</script>
</head>
<body  class="page_content">
	<form id="submitForm" action="<emp:url action='addActionSubmit.do'/>" method="POST">
	</form>
		<emp:gridLayout id="s_resourceactionGroup" maxColumn="1" title="资源操作表">
			<emp:text id="s_resourceaction.resourceid" label="资源ID" maxlength="32" required="true" readonly="true" />
			<emp:text id="s_resourceaction.actid" label="操作ID" maxlength="32" required="true" />
			<emp:text id="s_resourceaction.descr" label="描述" maxlength="20" required="false" />
			<emp:text id="s_resourceaction.flag" label="FLAG" maxlength="50" required="false" />
			<emp:text id="s_resourceaction.confirmmsg" label="提示确认信息" maxlength="100" required="false" />
		</emp:gridLayout>
	
	<div align="center">
			<br>
		<emp:button id="submit" label="确定"/>
		<emp:button id="close" label="关闭"/>
	</div>
</body>
</html>
</emp:page>

