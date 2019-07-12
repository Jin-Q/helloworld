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
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusEvent._toForm(form);
		CusEventList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusEventPage() {
		var editFlag = '${context.EditFlag}';
		var paramStr = CusEventList._obj.getParamStr(['serno','cus_id']);
		if (paramStr != null) {
			var status = CusEventList._obj.getSelectedData()[0].status._getValue();
			if(status!='0'){
				alert('请选择预登记状态下的记录进行操作！');
			}else{
				var paramStr = CusEventList._obj.getParamStr(['serno','cus_id']);
				var url = '<emp:url action="getCusEventUpdatePage.do"/>?'+paramStr+"&EditFlag="+editFlag;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		}else{
			alert('请先选择一条记录！');
		}
	}
	
	function doViewCusEvent() {
		var editFlag = '${context.EditFlag}';
		var paramStr = CusEventList._obj.getParamStr(['serno','cus_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusEventViewPage.do"/>?'+paramStr+"&EditFlag="+editFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusEventPage() {
		var cus_id  ='${context.CusEvent.cus_id}';
		var EditFlag  ='${context.EditFlag}';
		var paramStr="CusEvent.cus_id="+cus_id;
		var url = '<emp:url action="getCusEventAddPage.do"/>?'+paramStr+"&EditFlag="+EditFlag;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusEvent() {
		var paramStr = CusEventList._obj.getParamStr(['serno','cus_id']);
		if (paramStr != null){
			var status = CusEventList._obj.getSelectedData()[0].status._getValue();
			if(status!='0'){
				alert('请选择预登记状态下的记录进行操作！');
			}else{
				var paramStr = CusEventList._obj.getParamStr(['serno','cus_id']);
				if(confirm("是否确认要删除？")){
					var url = '<emp:url action="deleteCusEventRecord.do"/>?'+paramStr;
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
							if(flag=="删除成功"){
								alert("删除成功!");
							    var cus_id  ='${context.CusEvent.cus_id}';
							    var editFlag = '${context.EditFlag}';
								var paramStr="CusEvent.cus_id="+cus_id;
								var url = '<emp:url action="queryCusEventList.do"/>&'+paramStr+"&EditFlag="+editFlag;
								url = EMPTools.encodeURI(url);
								window.location = url;
						   }else {
							 alert(flag);
							 return;
						   }
						}
					};
					var handleFailure = function(o){	
					};
					var callback = {
						success:handleSuccess,
						failure:handleFailure
					}
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
				}
			}
		}else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CusEventGroup.reset();
	};
	
	function doInure(){
		var editFlag = '${context.EditFlag}';
		var data = CusEventList._obj.getParamStr(['serno','cus_id']);
		if (data != null) {
			var status = CusEventList._obj.getSelectedData()[0].status._getValue();
			if(status!='0'){
				alert('请选择预登记状态下的记录进行操作！');
			}else{
				var paramStr = CusEventList._obj.getParamStr(['serno','cus_id']);
				var url = '<emp:url action="getCusEventViewPageInure.do"/>?'+paramStr+"&EditFlag="+editFlag;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doLogout(){
		var editFlag = '${context.EditFlag}';
		var data = CusEventList._obj.getParamStr(['serno','cus_id']);
		if (data != null) {
			var status = CusEventList._obj.getSelectedData()[0].status._getValue();
			if(status!='1'){
				alert('请选择[生效]状态下的记录进行操作！');
			}else{
				var paramStr = CusEventList._obj.getParamStr(['serno','cus_id']);
				var url = '<emp:url action="getCusEventViewPageLogout.do"/>?'+paramStr+"&EditFlag="+editFlag;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	/*--user code begin--*/
			
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
		<emp:button id="getAddCusEventPage" label="新增" />
		<emp:button id="viewCusEvent" label="查看" />
		<emp:button id="getUpdateCusEventPage" label="修改" />
		<emp:button id="deleteCusEvent" label="删除" />
		<emp:button id="inure" label="生效" />
		<emp:button id="logout" label="注销" />
	<%}else{%>
		<emp:button id="viewCusEvent" label="查看" />
	<%}%>

	</div>
	<emp:table icollName="CusEventList" pageMode="true" url="pageCusEventQuery.do" reqParams="CusEvent.cus_id=${context.CusEvent.cus_id}&EditFlag=${context.EditFlag}">
		<emp:text id="serno" label="登记流水号" hidden="false"/>
		<emp:text id="cus_id" label="客户码" hidden="false"/>
		<emp:text id="event_dt" label="发生日期" />
		<emp:text id="event_typ" label="事件类型" dictname="STD_ZB_EVENT_TYP" />
		<emp:text id="event_imp_deg" label="事件影响程度" dictname="STD_ZB_EVENT_IMP_DEG" />
		<emp:text id="event_bank_flg" label="是否本行发生" dictname="STD_ZX_YES_NO"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="status" label="状态" dictname="STD_ZB_COMM_STATUS"/>
		<emp:text id="event_classify" label="事件分类" dictname="STD_ZB_EVENT_KIND" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>