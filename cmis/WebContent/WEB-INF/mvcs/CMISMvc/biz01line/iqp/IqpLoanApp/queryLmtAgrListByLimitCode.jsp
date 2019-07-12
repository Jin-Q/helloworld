<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%
Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	function doSelect(){
		var data = LmtAgrList._obj.getSelectedData();
		if(data == null || data.length == 0){
			alert('请先选择一条记录！');
			return;
		}
		window.opener["${context.returnMethod}"](data[0]);
		window.close();
	};	

	function doQuery(){
		var form = document.getElementById('queryForm');
		LmtAgr.lmt_code_no._toForm(form);
		LmtAgrList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.LmtAgrDetailsGroup.reset();
	};
	function doViewAgrDetail(){
		var data = LmtAgrList._obj.getSelectedData();  //得到选中记录行
		if (data != null && data.length !=0) {
			var limit_code = LmtAgrList._obj.getSelectedData()[0].limit_code._getValue();
			var url = '<emp:url action="viewLmtAgrInfo.do"/>&agr_no='+limit_code+'&type=surp&menuId=crd_ledger&op=view&type=surp&showButton=N';
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow',param);
		}else{
			alert('请先选择一条记录！');
			return;
		}
	}
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm"></form>

	<emp:gridLayout id="LmtAgrDetailsGroup" title="输入查询条件" maxColumn="2">
		<emp:text id="LmtAgr.lmt_code_no" label="授信协议编号" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		本次应占用授信额度：<font color="red">${context.risk_open_amt}</font>
	</div> 
	<div align="left">
		<emp:button id="select" label="选取返回"></emp:button> 
		<emp:button id="viewAgrDetail" label="查看"/>
	</div>

	<emp:table icollName="LmtAgrList" pageMode="false" url="pageLmtAgrListByLimitCodeOp.do?limit_cont_no=${context.limit_cont_no}&guar_type=${context.guar_type}&limit_type=${context.limit_type}&risk_open_amt=${context.risk_open_amt}">
		<emp:text id="agr_no" label="授信协议编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="limit_code" label="额度编码" />
		<emp:text id="limit_name_displayname" label="授信名称" />
		<emp:text id="limit_type" label="额度类型" dictname="STD_ZB_LIMIT_TYPE"/>
		<emp:text id="crd_amt" label="授信金额" dataType="Currency"/>
		<emp:text id="enable_amt" label="可用金额" dataType="Currency"/>
		<emp:text id="guar_type" label="担保方式" dictname="STD_ZB_ASSURE_MEANS"/>
		<emp:text id="start_date" label="开始日期" />
		<emp:text id="end_date" label="到期日期"  />
	</emp:table>
	
</body>
</html>
</emp:page>
    