<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%
	String cus_id=request.getParameter("CusIndivAss.cus_id");
%>
<emp:page>
<html>
<head>
<title>列表查询页面</title>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusIndivAss._toForm(form);
		CusIndivAssList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusIndivAssPage() {
		var paramStr = CusIndivAssList._obj.getParamStr(['cus_id','indiv_ass_id']);
		var EditFlag  ='${context.EditFlag}';
		if (paramStr != null) {
			var url = '<emp:url action="getCusIndivAssUpdatePage.do"/>?'+paramStr+"&EditFlag="+EditFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusIndivAss() {
		var paramStr = CusIndivAssList._obj.getParamStr(['cus_id','indiv_ass_id']);
		var EditFlag  ='${context.EditFlag}';
		if (paramStr != null) {
			var url = '<emp:url action="getCusIndivAssViewPage.do"/>?'+paramStr+"&EditFlag="+EditFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusIndivAssPage() {
		var EditFlag  ='${context.EditFlag}';
		var url = '<emp:url action="getCusIndivAssAddPage.do"/>&' + "CusIndivAss.cus_id=<%=cus_id%>&EditFlag="+EditFlag;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusIndivAss() {
		var paramStr = CusIndivAssList._obj.getParamStr(['cus_id','indiv_ass_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var EditFlag  ='${context.EditFlag}';
				var url = '<emp:url action="deleteCusIndivAssRecord.do"/>?'+paramStr+"&EditFlag="+EditFlag;
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
							goback();
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
				}; 
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CusIndivAssGroup.reset();
	};
	
	/*--user code begin--*/
	function goback(){
		var EditFlag  ='${context.EditFlag}';
		var stockURL = '<emp:url action="queryCusIndivAssList.do"/>&' + "CusIndivAss.cus_id=<%=cus_id%>&EditFlag="+EditFlag;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	}			
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
		<emp:button id="getAddCusIndivAssPage" label="新增"/>
        <emp:button id="viewCusIndivAss" label="查看"/>
		<emp:button id="getUpdateCusIndivAssPage" label="修改"/>
		<emp:button id="deleteCusIndivAss" label="删除"/>
	<%}else{%>
		<emp:button id="viewCusIndivAss" label="查看"/>
	<%}%>
	</div>
	<emp:table icollName="CusIndivAssList" pageMode="true" url="pageCusIndivAssQuery.do" reqParams="CusIndivAss.cus_id=${context.CusIndivAss.cus_id}&EditFlag=${context.EditFlag}">
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="indiv_ass_id" label="资产编号" hidden="true"/>
		<emp:text id="indiv_ass_type" label="资产类别" dictname="STD_ZB_INV_ASS_TPY"/>
		<emp:text id="indiv_ass_name" label="资产名称" />
		<emp:text id="indiv_ass_plr" label="资产单位" />
		<emp:text id="indiv_ass_num" label="资产数量" />
		<emp:text id="indiv_ass" label="资产估价(元)" dataType="Currency"/>
	</emp:table>
</body>
</html>
</emp:page>