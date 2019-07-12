<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	/*--user code begin--*/
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusComCont._toForm(form);
		CusComContList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusComContPage() {
		var paramStr = CusComContList._obj.getParamStr(['cus_id','seq']);
		if (paramStr != null) {
			var editFlag = '${context.EditFlag}';
			var url = '<emp:url action="getCusComContUpdatePage.do"/>?'+paramStr+"&EditFlag="+editFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusComCont() {
		var paramStr = CusComContList._obj.getParamStr(['cus_id','seq']);
		if (paramStr != null) {
			var editFlag = '${context.EditFlag}';
			var url = '<emp:url action="getCusComContViewPage.do"/>?'+paramStr+"&EditFlag="+editFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusComContPage() {
		var cus_id  ='${context.CusComCont.cus_id}';
		var editFlag = '${context.EditFlag}';
		var paramStr="CusComCont.cus_id="+cus_id+"&EditFlag="+editFlag;
		var url = '<emp:url action="getCusComContAddPage.do"/>?'+paramStr;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusComCont() {
		var paramStr = CusComContList._obj.getParamStr(['cus_id','seq']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusComContRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr define error!"+e);
							return;
						}
						var flag = jsonstr.flag;
						if(flag=="success"){
							alert("删除成功!");
						    var cus_id  ='${context.CusComCont.cus_id}';
						    var editFlag = '${context.EditFlag}';
							var paramStr="CusComCont.cus_id="+cus_id+"&EditFlag="+editFlag;
							var url = '<emp:url action="queryCusComContList.do"/>&'+paramStr;
							url = EMPTools.encodeURI(url);
							window.location = url;
					   }else {
						 alert("删除失败!");
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
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CusComContGroup.reset();
	};
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	
	<div align="left">

	<%
//	String flag=(String)request.getSession().getAttribute("buttonFlag");
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String flag = context.getDataValue("EditFlag").toString();
	if(!(flag!=null&&flag.equals("query"))){
	%>
		<emp:button id="getAddCusComContPage" label="新增" />
		<emp:button id="viewCusComCont" label="查看" />
		<emp:button id="getUpdateCusComContPage" label="修改" />
		<emp:button id="deleteCusComCont" label="删除" />
	<% }else{ %>
		<emp:button id="viewCusComCont" label="查看" />
	<% } %>

	</div>
	<emp:table icollName="CusComContList" pageMode="true" url="pageCusComContQuery.do" reqParams="CusComCont.cus_id=${context.CusComCont.cus_id}&EditFlag=${context.EditFlag}">
		<emp:text id="seq" label="序号" hidden="true"/>
		<emp:text id="com_addr_typ" label="地址类型" dictname="STD_ZB_COM_ADDR_TYP"/>
		<emp:text id="com_addr" label="通讯地址" hidden="true"/>
		<emp:text id="com_addr_displayname" label="通讯地址" />
		<emp:text id="com_phn_code" label="联系电话" />
		<emp:text id="com_zip_code" label="邮政编码" />
		<emp:text id="cus_id" label="客户码" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    