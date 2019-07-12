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
		CusObisLoan._toForm(form);
		CusObisLoanList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusObisLoanPage() {
		var paramStr = CusObisLoanList._obj.getParamStr(['seq','cus_id']);
		var EditFlag  ='${context.EditFlag}';
		if (paramStr != null) {
			var url = '<emp:url action="getCusObisLoanUpdatePage.do"/>?'+paramStr+"&oper=update&EditFlag="+EditFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusObisLoan() {
		var paramStr = CusObisLoanList._obj.getParamStr(['seq','cus_id']);
		var EditFlag  ='${context.EditFlag}';
		if (paramStr != null) {
			var url = '<emp:url action="getCusObisLoanViewPage.do"/>?'+paramStr+"&oper=view&EditFlag="+EditFlag;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusObisLoanPage() {
		var cus_id  ='${context.CusObisLoan.cus_id}';
		var paramStr="CusObisLoan.cus_id="+cus_id;
		var EditFlag  ='${context.EditFlag}';
		var url = '<emp:url action="getCusObisLoanAddPage.do"/>?'+paramStr+"&oper=add&EditFlag="+EditFlag;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteCusObisLoan() {
		var paramStr = CusObisLoanList._obj.getParamStr(['seq','cus_id']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deleteCusObisLoanRecord.do"/>?'+paramStr;
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
							    var cus_id  ='${context.CusObisLoan.cus_id}';
								var paramStr="CusObisLoan.cus_id="+cus_id;
								var EditFlag  ='${context.EditFlag}';
								var url = '<emp:url action="queryCusObisLoanList.do"/>&'+paramStr+"&EditFlag="+EditFlag;
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
		page.dataGroups.CusObisLoanGroup.reset();
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
		<emp:button id="getAddCusObisLoanPage" label="新增" />
		<emp:button id="viewCusObisLoan" label="查看" />
		<emp:button id="getUpdateCusObisLoanPage" label="修改" />
		<emp:button id="deleteCusObisLoan" label="删除" />
	<%
	}else{
	%>
		<emp:button id="viewCusObisLoan" label="查看" />
	<%}%>
	</div>

	<emp:table icollName="CusObisLoanList" pageMode="true" url="pageCusObisLoanQuery.do" reqParams="CusObisLoan.cus_id=${context.CusObisLoan.cus_id}&EditFlag=${context.EditFlag}">
		<emp:text id="seq" label="序号" hidden="true"/>
		<emp:text id="cus_typ" label="客户类型" dictname="STD_ZB_INVESTOR2" hidden="true"/>
		<emp:text id="loan_typ" label="业务品种" dictname="STD_ZB_OTHERPRO_TYPE" />
		<emp:text id="cont_no" label="合同号" />
		<emp:text id="cont_cur_typ" label="币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="cont_amt" label="合同金额(元)" dataType="Currency"/>
		<emp:text id="org_name" label="开户机构名称" />
		<emp:text id="cus_id" label="客户码" hidden="true"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    