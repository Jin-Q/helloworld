<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%
	String cus_id=request.getParameter("CusHoldChrem.cus_id");
%>
<emp:page>
<html>
<head>
<title>持有理财信息</title>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusHoldChrem._toForm(form);
		CusHoldChremList._obj.ajaxQuery(null,form);
	}
	
	function doGetUpdateCusHoldChremPage() {
		var paramStr = CusHoldChremList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var editFlag = '${context.EditFlag}';
			var url = '<emp:url action="getCusHoldChremUpdatePage.do"/>?'+paramStr+"&EditFlag="+editFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请选择一条信息!');
		}
	};
	
	function doViewCusHoldChrem() {
		var paramStr = CusHoldChremList._obj.getParamStr(["serno"]);
		if (paramStr != null) {
			var editFlag = '${context.EditFlag}';
			var url = '<emp:url action="getCusHoldChremViewPage.do"/>?'+paramStr+"&EditFlag="+editFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请选择一条信息!');
		}
	};
	
	function doGetAddCusHoldChremPage() {
		var editFlag = '${context.EditFlag}';
		var url = '<emp:url action="getCusHoldChremAddPage.do"/>&'+"CusHoldChrem.cus_id=<%=cus_id%>&EditFlag="+editFlag;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

	function doDeleteCusHoldChrem() {
		var paramStr = CusHoldChremList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusHoldChremRecord.do"/>?'+paramStr;
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
							doReturn();
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
		page.dataGroups.CusHoldChremGroup.reset();
	};

	//返回列表页面
  	function doReturn(){
  		var url = '<emp:url action="queryCusHoldChremList.do"/>&'+"CusHoldChrem.cus_id=<%=cus_id%>&EditFlag="+editFlag;
		url = EMPTools.encodeURI(url);
		window.location = url;
	}
	/*--user code begin--*/
			
	/*--user code end--*/
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<%
	//    String flag=(String)request.getSession().getAttribute("buttonFlag");
		Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
		String flag = context.getDataValue("EditFlag").toString();
		if(!(flag!=null&&flag.equals("query"))){
	%>
		<div align="left">
			<emp:button id="getAddCusHoldChremPage" label="新增"/>
			<emp:button id="viewCusHoldChrem" label="查看"/>
			<emp:button id="getUpdateCusHoldChremPage" label="修改"/>
			<emp:button id="deleteCusHoldChrem" label="删除"/>
		</div>
	<%}else{ %>
		<div align="left">
			<emp:button id="viewCusHoldChrem" label="查看"/>
		</div>
	<%} %>
	<emp:table icollName="CusHoldChremList" pageMode="true" url="pageCusHoldChremQuery.do?CusHoldChrem.cus_id=${context.CusHoldChrem.cus_id}&EditFlag=${context.EditFlag}">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="prod_name" label="产品名称" />
		<emp:text id="subscr_date" label="认购时间" />
		<emp:text id="subscr_amt" label="认购金额" dataType="Currency"/>
		<emp:text id="start_date" label="开始时间" />
		<emp:text id="end_date" label="到期时间" />
		<emp:text id="expect_income_rate" label="预期收益率" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="serno" label="流水号" hidden="true" />
	</emp:table>
</body>
</html>
</emp:page>