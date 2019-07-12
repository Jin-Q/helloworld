<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
 <% String net_agr_no=(String)request.getParameter("net_agr_no");
    String mem_cus_id=(String)request.getParameter("mem_cus_id");
    String mem_manuf_type=(String)request.getParameter("mem_manuf_type");
 %>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpPsaleCont._toForm(form);
		IqpPsaleContList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpPsaleContPage() {
		var paramStr = IqpPsaleContList._obj.getParamStr(['psale_cont','net_agr_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpPsaleContUpdatePage.do"/>?'+paramStr
			                                                             +"&mem_cus_id=${context.mem_cus_id}"
			                                                             +"&cus_id=${context.cus_id}"
			                                                             +"&mem_manuf_type=${context.mem_manuf_type}"
			                                                             +"&net_agr_no=${context.net_agr_no}";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpPsaleCont() {
		var paramStr = IqpPsaleContList._obj.getParamStr(['psale_cont']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpPsaleContViewPage.do"/>?'+paramStr
																	   +"&mem_cus_id=${context.mem_cus_id}"
															           +"&cus_id=${context.cus_id}"
															           +"&mem_manuf_type=${context.mem_manuf_type}"
															           +"&net_agr_no=${context.net_agr_no}";       
			url = EMPTools.encodeURI(url);
			var param = 'height=500, width=1000, top=180, left=150, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpPsaleContPage() {
		var url = '<emp:url action="getIqpPsaleContAddPage.do"/>?net_agr_no=${context.net_agr_no}'+
		                                                         "&mem_cus_id=${context.mem_cus_id}"+
		                                                         "&mem_manuf_type=${context.mem_manuf_type}"
		                                                         +"&cus_id=${context.cus_id}";
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpPsaleCont() {
		var paramStr = IqpPsaleContList._obj.getParamStr(['psale_cont']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var handleSuccess = function(o){ 		
                    var jsonstr = eval("(" + o.responseText + ")");
					var flag = jsonstr.flag;
					if(flag == "success" ){
						alert("删除客户成功！");
						window.location.reload();
					}else{
					}
				}
				var handleFailure = function(o){
				alert("异步回调失败！");	
				};
				var url = '<emp:url action="deleteIqpPsaleContRecord.do"/>?'+paramStr;
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				};
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST', url,callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpPsaleContGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	
	<div align="left">
		<emp:actButton id="getAddIqpPsaleContPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateIqpPsaleContPage" label="修改" op="update"/>
		<emp:actButton id="deleteIqpPsaleCont" label="删除" op="remove"/>
		<emp:actButton id="viewIqpPsaleCont" label="查看" op="view"/>
	</div>

	<emp:table icollName="IqpPsaleContList" pageMode="true" url="pageIqpPsaleContQuery.do?net_agr_no=${context.net_agr_no}&mem_manuf_type=${context.mem_manuf_type}&mem_cus_id=${context.mem_cus_id}">
		<emp:text id="psale_cont" label="购销合同编号" />
		<emp:text id="buyer_cus_id" label="买方客户码" hidden="true"/>
		<emp:text id="buyer_cus_id_displayname" label="买方客户名称" />
		<emp:text id="barg_cus_id" label="卖方客户码" hidden="true"/>
		<emp:text id="barg_cus_id_displayname" label="卖方客户名称" /> 
		<emp:text id="cont_amt" label="合同金额" dataType="Currency" />
		<emp:text id="start_date" label="合同起始日" />
		<emp:text id="end_date" label="合同到期日" />
		<emp:text id="status" label="状态" dictname="STD_ZB_STATUS" />
		<emp:text id="net_agr_no" label="网络协议号" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    