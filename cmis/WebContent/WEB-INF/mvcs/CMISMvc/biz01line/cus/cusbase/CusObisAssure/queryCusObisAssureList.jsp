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
		CusObisAssure._toForm(form);
		CusObisAssureList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusObisAssurePage() {
		var paramStr = CusObisAssureList._obj.getParamStr(['cus_id','seq']);
		var EditFlag  ='${context.EditFlag}';
		if (paramStr != null) {
			var url = '<emp:url action="getCusObisAssureUpdatePage.do"/>?'+paramStr+"&EditFlag="+EditFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusObisAssure() {
		var paramStr = CusObisAssureList._obj.getParamStr(['cus_id','seq']);
		var EditFlag  ='${context.EditFlag}';
		if (paramStr != null) {
			var url = '<emp:url action="getCusObisAssureViewPage.do"/>?'+paramStr+"&EditFlag="+EditFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusObisAssurePage() {
		var cus_id  ='${context.CusObisAssure.cus_id}';
		var paramStr="CusObisAssure.cus_id="+cus_id;
		var EditFlag  ='${context.EditFlag}';
		var url = '<emp:url action="getCusObisAssureAddPage.do"/>?'+paramStr+"&EditFlag="+EditFlag;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusObisAssure() {
		var paramStr = CusObisAssureList._obj.getParamStr(['cus_id','seq']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusObisAssureRecord.do"/>?'+paramStr;
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
							    var cus_id  ='${context.CusObisAssure.cus_id}';
								var paramStr="CusObisAssure.cus_id="+cus_id;
								var EditFlag  ='${context.EditFlag}';
								var url = '<emp:url action="queryCusObisAssureList.do"/>&'+paramStr+"&EditFlag="+EditFlag;
								url = EMPTools.encodeURI(url);
								window.location = url;
					   }else {
						 alert(flag);
						 return;
					   }
					}
				};
				var handleFailure = function(o){};
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				}; 
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback) 
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CusObisAssureGroup.reset();
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
		Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
		String flag = context.getDataValue("EditFlag").toString();
		if(!(flag!=null&&flag.equals("query"))){
	%>
		<emp:button id="getAddCusObisAssurePage" label="新增" />
		<emp:button id="viewCusObisAssure" label="查看" />
		<emp:button id="getUpdateCusObisAssurePage" label="修改" />
		<emp:button id="deleteCusObisAssure" label="删除" />
	<%
	}else{
	%>
		<emp:button id="viewCusObisAssure" label="查看" />
	<%}%>
	</div>

	<emp:table icollName="CusObisAssureList" pageMode="true" url="pageCusObisAssureQuery.do" reqParams="CusObisAssure.cus_id=${context.CusObisAssure.cus_id}&EditFlag=${context.EditFlag}">
		<emp:text id="seq" label="序号" hidden="true"/>
		<emp:text id="gty_typ" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" />
		<emp:text id="gty_amt" label="担保金额(元)" dataType="Currency" />
		<emp:text id="gty_blc" label="担保余额(元)" dataType="Currency" />
		<emp:text id="gty_str_dt" label="起始日期" />
		<emp:text id="gty_end_dt" label="到期日期" />
		<emp:text id="gty_bus_bch_dec" label="银行详细名称" />
		<emp:text id="valid_flg" label="有效标志" dictname="STD_ZB_STATUS"/>
		<emp:text id="cus_id" label="客户码" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    