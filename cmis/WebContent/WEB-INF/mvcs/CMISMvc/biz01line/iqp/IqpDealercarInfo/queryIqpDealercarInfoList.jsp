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
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpDealercarInfo._toForm(form);
		IqpDealercarInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpDealercarInfoPage() {
		var paramStr = IqpDealercarInfoList._obj.getParamStr(['serno','car_serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpDealercarInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpDealercarInfo() {
		var paramStr = IqpDealercarInfoList._obj.getParamStr(['serno','car_serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpDealercarInfoViewPage.do"/>?'+paramStr+'&restrictUsed=false';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpDealercarInfoPage() {
		var url = '<emp:url action="getIqpDealercarInfoAddPage.do"/>?serno='+'<%=serno%>';
		url = EMPTools.encodeURI(url);
		window.location = url; 
	};
	
	function doDeleteIqpDealercarInfo() {
		var paramStr = IqpDealercarInfoList._obj.getParamStr(['serno','car_serno']);
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
							var url = '<emp:url action="queryIqpDealercarInfoList.do"/>?serno='+'<%=serno%>';
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
				var url = '<emp:url action="deleteIqpDealercarInfoRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpDealercarInfoGroup.reset();
	};
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<div align="left"> 
		<emp:actButton id="getAddIqpDealercarInfoPage" label="新增" op="add"/>
		<emp:actButton id="getUpdateIqpDealercarInfoPage" label="修改" op="update"/>
		<emp:actButton id="deleteIqpDealercarInfo" label="删除" op="remove"/>
		<emp:actButton id="viewIqpDealercarInfo" label="查看" op="view"/>
	</div>  

	<emp:table icollName="IqpDealercarInfoList" pageMode="true" url="pageIqpDealercarInfoQuery.do">
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="car_serno" label="车辆信息编号" hidden="true"/>  
		<emp:text id="car_type" label="车辆类型" dictname="STD_ZB_CAR_TYPE" />
		<emp:text id="car_name" label="车辆名称" /> 
		<emp:text id="car_sign" label="车辆品牌" />
		<emp:text id="car_num" label="数量（辆）" />
		<emp:text id="car_amt" label="单价" dataType="Currency"/>
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" />
	</emp:table>
	
</body>
</html>
</emp:page>
    