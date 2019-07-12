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
		GrtGuarCont._toForm(form);
		GrtGuarContList._obj.ajaxQuery(null,form);
	};

	function doReset(){
		page.dataGroups.GrtGuarContGroup.reset();
	};
	
	function doSelect(){
		var data = GrtGuarContList._obj.getSelectedData();
		if (data != null && data.length !=0) {
			var guar_amt = data[0].guar_amt._getValue();
			if(guar_amt == null || guar_amt == ""){
				alert("此笔担保合同金额为空,请检查！");
				return;
			}
            var guar_cont_no = data[0].guar_cont_no._getValue();
            var url = '<emp:url action="getRLmtGuarContAddForLmtPage.do"/>?serno=${context.serno}&cus_id=${context.cus_id}&limit_code=${context.limit_code}&type=${context.type}&guar_cont_no='+guar_cont_no+"&guar_amt="+guar_amt+"&type=${context.type}";   
            url=EMPTools.encodeURI(url);
          	window.open(url,'newwindow','height=650,width=1024,top=100,left=150,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no'); 
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doCancel(){
		window.close();
	};
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
		<emp:gridLayout id="GrtGuarContGroup" title="输入查询条件" maxColumn="3">
		<emp:text id="GrtGuarCont.guar_cont_no" label="担保合同编号" />
		<emp:text id="GrtGuarCont.guar_cont_cn_no" label="中文担保合同编号" />
		<emp:select id="GrtGuarCont.guar_way" label="担保方式" dictname="STD_GUAR_TYPE"/>
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
		
	</form>
	
	<div align="left">
	     <emp:button id="select" label="引入" />
	</div>

	<emp:table icollName="GrtGuarContList" pageMode="true" url="pageGrtGuarContTabQueryForLmt.do?serno=${context.serno}&cus_id=${context.cus_id}&limit_code=${context.limit_code}&type=${context.type}">
		 <emp:text id="guar_cont_no" label="担保合同编号 " />
		 <emp:text id="guar_cont_cn_no" label="中文担保合同编号 " />
		<emp:text id="guar_cont_type" label="担保合同类型" dictname="STD_GUAR_CONT_TYPE" />
		<emp:text id="guar_way" label="担保方式" dictname="STD_GUAR_TYPE" />		
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="guar_amt" label="担保合同金额" dataType="Currency"/>
		<emp:text id="guar_start_date" label="担保起始日" />
		<emp:text id="guar_end_date" label="担保终止日" />
		<emp:text id="guar_cont_state" label="担保合同状态" dictname="STD_CONT_STATUS" />
	</emp:table>
	<div align="left">
	<br>
		<emp:button id="select" label="引入" />
		<emp:button id="cancel" label="关闭" />
	</div>
</body>
</html>
</emp:page>
    