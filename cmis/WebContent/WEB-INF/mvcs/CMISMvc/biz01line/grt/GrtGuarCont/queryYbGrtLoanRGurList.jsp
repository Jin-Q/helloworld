<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String serno="";
	if(context.containsKey("serno")){
		serno =(String)context.getDataValue("serno");
	}  
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript"> 
	/*一般担保合同修改 
	@flag=loan 业务担保合同关联页面标识，可以查看到业务担保合同关联信息Tab页
	@oper=view 查看按钮标识
	@menuId=ybCount  当Tab页页面在别处调用时，需加上原有的menuId
	*/
	function doGetUpdateGrtLoanRGurPage() {
		var paramStr = GrtLoanRGurListYb._obj.getParamStr(['pk_id','guar_cont_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getGrtLoanRGurUpdatePage.do"/>?op=update&flag=loan&oper=update&menuId=ybCount&'+paramStr; 
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height=538,width=1024,top=170,left=200,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		} else {  
			alert('请先选择一条记录！'); 
		}
	};
	/*一般担保合同查看
	@flag=loan 业务担保合同关联页面标识，可以查看到业务担保合同关联信息Tab页
	@oper=view 查看按钮标识
	@menuId=ybCount  当Tab页页面在别处调用时，需加上原有的menuId
	 */
	function doViewGrtLoanRGur() {
		var paramStr = GrtLoanRGurListYb._obj.getParamStr(['pk_id','guar_cont_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getGrtLoanRGurViewPage.do"/>?op=view&flag=loan&oper=view&menuId=ybCount&'+paramStr;    
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height=538,width=1024,top=170,left=200,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');      
		}
	};

	
	/*一般担保合同增加 */
	function doGetAddGrtYBGuarPage(){
		var url = '<emp:url action="getGrtYBContAddPage.do"/>?serno='+'<%=serno%>';
		url=EMPTools.encodeURI(url);  
      	window.open(url,'newwindow','height=538,width=1024,top=170,left=200,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	/*一般担保合同删除 */
	function doDeleteGrtLoanRGur() {
		var paramStr = GrtLoanRGurListYb._obj.getParamStr(['pk_id','guar_cont_no']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
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
								alert("删除成功!");
								var url = '<emp:url action="queryGrtLoanRGurList.do"/>?serno='+'<%=serno%>';
								url = EMPTools.encodeURI(url);
								window.location = url; 
							}else {
								alert("发生异常!");
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
					var url = '<emp:url action="deleteGrtLoanRGurRecord.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback)
				}
			 
		} else {
			alert('请先选择一条记录！');
		}
	};
    /*最高担保额合同引入*/
	function doGetAddGrtLoanRGurZge() {
		var url = '<emp:url action="getGrtLoanRGurAddPage.do"/>?serno='+'<%=serno%>';
		url=EMPTools.encodeURI(url);  
      	window.open(url,'newwindow','height=538,width=1024,top=170,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	};
	/*最高额担保合同删除 */
	function doDeleteGrtLoanRGurZge() {
		var paramStr = GrtLoanRGurListZge._obj.getParamStr(['pk_id']);  
		if (paramStr != null) {   
			if(confirm("是否确认要删除？")){ 
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
								alert("删除成功!");
								var url = '<emp:url action="queryGrtLoanRGurList.do"/>?serno='+'<%=serno%>';
								url = EMPTools.encodeURI(url);
								window.location = url; 
							}else {
								alert("发生异常!");
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
					var url = '<emp:url action="deleteGrtLoanRGurZGE.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback)
				}
			 
		} else {
			alert('请先选择一条记录！');
		}
	};
	   
	/*最高额担保合同查看
	 @flag=loan 业务担保合同关联页面标识，可以查看到业务担保合同关联信息Tab页
	 @op=view 查看按钮标识
	 @menuId=ybCount  当Tab页页面在别处调用时，需加上原有的menuId
	*/
	function doViewGrtLoanRGurZge() {
		var paramStr = GrtLoanRGurListZge._obj.getParamStr(['pk_id','guar_cont_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getGrtLoanRGurViewPage.do"/>&op=view&flag=loan&oper=view&menuId=zge&'+paramStr;     
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height=538,width=1024,top=170,left=200,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');   
		} 
	};      
	
	function doReset(){
		page.dataGroups.GrtLoanRGurGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
    
	<b>一般担保合同</b>
	<div align="left">
		<emp:actButton id="getAddGrtYBGuarPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateGrtLoanRGurPage" label="修改" op="update"/>
		<emp:actButton id="deleteGrtLoanRGur" label="删除" op="remove"/>
		<emp:actButton id="viewGrtLoanRGur" label="查看" op="view"/>  
	</div> 
	
	<emp:table icollName="GrtLoanRGurListYb" pageMode="true" url="pageGrtLoanRGurQueryYB.do">
	    <emp:text id="pk_id" label="pk_id" hidden="true"/>
		<emp:text id="guar_cont_no" label="担保合同编号" />
		<emp:text id="guar_cont_type" label="担保合同类型" dictname="STD_GUAR_CONT_TYPE"/>
		<emp:select id="guar_way" label="担保方式"  dictname="STD_GUAR_TYPE" />
		<emp:text id="guar_amt" label="担保合同金额" />
		<emp:text id="this_guar_amt" label="本次担保合同金额" />
		<emp:text id="is_per_gur" label="是否阶段性担保" dictname="STD_ZX_YES_NO"/>
		<emp:select id="guar_cont_state" label="担保状态"  dictname="STD_CONT_STATUS"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    