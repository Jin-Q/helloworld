<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	/**add by lisj 2015-8-27 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin **/
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String cont="";
	if(context.containsKey("cont")){
		cont = (String)context.getDataValue("cont");		
	}
	/**add by lisj 2015-8-27 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end **/
%>
<emp:page>
<html>
<head>
<title>承兑汇票导入页面</title>
<jsp:include page="/include.jsp" />
<script>	
	var page = new EMP.util.Page();
	function doImportReport() {
	    var address = document.getElementsByName("pFile")[0].value;
		if(address == "") {
			alert("请选择要导入的EXCEL文件");
		}
		if(address != "") {
			var form = document.getElementById('improtForm');
			form.submit();
		}
	};
	
	function doReturn(){
		window.close();
	};
	
    //下载模板
    function doDownLoadTemp(){
    	var url = '<emp:url action="downLoadCusBlkTmplate.do"/>?templateName=IqpAccpDetail.xls';
		url = EMPTools.encodeURI(url);
		window.location = url;
    }
</script>
</head>
<body class="page_content" >
	<div  class='emp_gridlayout_title'>承兑汇票导入</div>
	<form class='emp_group_div' id="improtForm" action="<emp:url action='importIqpAccpDetailOp.do'/>&serno=${context.serno}&cont=${context.cont}" method="POST" enctype="multipart/form-data">
	 <table align="center">
		<tr>
			<td>请选择要导入的EXCEL表的路径：</td>
			<td><input type="file" name="pFile"  ></td>
		</tr>
 	</table>
 	<table align="center">
		<tr>
			<td>目前只支持03版EXCEL表，请按照模板格式操作</td>
		</tr>
	</table>
	</form>
	
	<br>
 <div align="center">
 	<emp:button id="downLoadTemp" label="下载模板"/>&nbsp;
	<emp:button id="importReport" label="导入" />&nbsp;
	<emp:button id="return" label="关闭"/>
	<br>
  <br>
  </div>

</body>
</html>
</emp:page>