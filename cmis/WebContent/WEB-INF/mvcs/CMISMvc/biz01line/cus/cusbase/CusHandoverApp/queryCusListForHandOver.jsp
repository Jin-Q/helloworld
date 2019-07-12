<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusBase._toForm(form);
		CusBaseList._obj.ajaxQuery(null,form);
	};
	function doReset(){
		page.dataGroups.CusBaseGroup.reset();
	};
	function doReturn(){
		window.close();
	}
	function doReturn1(){
		window.close();
	}
	
	function doSelect(){
		var form = document.getElementById('queryForm');
		var paramStr = CusBaseList._obj.getParamStr(['cus_id']);
		//paramStr += "&" + CusBaseList._obj.getParamStr(['cus_name']);
		if (paramStr != null && paramStr != "null&null") {
		
			paramStr=paramStr+"&handover_br_id="+'${context.handover_br_id}'+"&handover_id="+'${context.handover_id}'
                  +"&receiver_br_id="+'${context.receiver_br_id}'+"&receiver_id="+'${context.receiver_id}'+"&serno="+'${context.serno}'
                  +"&handover_mode="+'${context.handover_mode}'
                  +"&handover_scope="+'${context.handover_scope}';
                  var url = '<emp:url action="addCusHandoverLstRecord.do"/>?'+paramStr;
                  url = EMPTools.encodeURI(url);
                  var form = document.getElementById('queryForm');
                  form.action=url;
                  iCollSel2Form(CusBaseList,form,['cus_id','cus_name']);
                  form.submit();
				
		}else{
			alert('请您先至少先选择一条记录！');
		}
	}
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>
	
	<emp:gridLayout id="CusBaseGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CusBase.cus_id" label="客户码" />
			<emp:text id="CusBase.cus_name" label="客户名称" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
<button onclick="doSelect()">选取返回</button>
<emp:button id="return" label="关闭"/>
	<emp:table icollName="CusBaseList" pageMode="true" url="getCusBaseList4HandOver.do"  reqParams="scope=${context.scope}&handover_id=${context.handover_id}&handover_mode=${context.handover_mode}">
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="cus_type" label="客户类型" dictname="STD_ZB_CUS_TYPE"/>
		<emp:text id="cert_type" label="证件类型" dictname="STD_ZB_CERT_TYP"/>
		<emp:text id="cert_code" label="证件号码" />
		<emp:text id="cust_mgr_displayname" label="主管客户经理" />
		<emp:text id="main_br_id_displayname" label="主管机构" />
		
		<emp:text id="cust_mgr" label="主管客户经理" hidden="true"/>
		<emp:text id="main_br_id" label="主管机构" hidden="true"/>
	</emp:table>
<button onclick="doSelect()">选取返回</button>
<emp:button id="return" label="关闭"/>
</body>
</html>
</emp:page>
    