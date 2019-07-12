<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<emp:page> 
<html>
<head>
<title>列表查询页面</title>
<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/EUIInclude.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
</head>
<body class="page_content">
	<!-- 高级搜索区 -->
 
	<form  method="POST" action="#" id="queryForm">
	</form>
			<emp:gridLayout id="RscTaskInfoGroup" title="输入查询条件" maxColumn="2">
 				<emp:pop id="RscTaskInfo.input_br_id" label="机构编号" url="querySOrgPop.do?restrictUsed=false" returnMethod="returnOrg" cssElementClass="emp_pop_common_org" />
				<emp:text id="RscTaskInfo.input_br_id_displayname" readonly="true" label="机构名称" />
			 
			</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />

	<!-- 列表信息 -->
	<emp:table icollName="RscTaskInfoList" url="getClassifyUndoTaskInfoData.do?RscTaskInfo.bch_cde=${context.organNo}"   pageMode="true"  >
 		
 		<emp:link id="input_br_id"   label="机构编号 " operation="view"   cssTDClass="tdCenter"/>
 		<emp:text id="input_br_id_displayname"   label="机构名称" cssTDClass="tdCenter" />
 		<emp:text id="totalqnt" label="总分类数" cssTDClass="tdRight"/>
		<emp:text id="unqnt" label="未完成分类" cssTDClass="tdRight"/>
		<emp:text id="qnt" label="已完成分类" cssTDClass="tdRight"/>
		<emp:text id="rate" label="未提交百分比 "  hidden="true"/>
	</emp:table>
	<script type="text/javascript">
	 
 
	function doQuery(){
		var form = document.getElementById('queryForm');
		RscTaskInfo._toForm(form);
		RscTaskInfoList._obj.ajaxQuery(null,form);
	};
	
	function doReset(){
		page.dataGroups.RscTaskInfoGroup.reset();
	};
		
	function doView(){ 
		var selObj = RscTaskInfoList._obj.getSelectedData()[0];
		var input_br_id=selObj.input_br_id._getValue();
		var url = "<emp:url action='getClassifyUnDoTaskDetailListPage.do'/>"+"&RscTaskInfo.input_br_id="+input_br_id+"&RscTaskInfo.flag=unDo";
		url = EMPTools.encodeURI(url);
		window.location =url;
	}
	function view(value,rowData,index){
		var input_br_id = rowData.input_br_id;
		var url = "<emp:url action='getClassifyUnDoTaskDetailListPage.do'/>"+"&RscTaskInfo.input_br_id="+input_br_id+"&RscTaskInfo.flag=unDo";
		url = EMPTools.encodeURI(url);
		return "<a href=\"javascript:window.location='"+url+"'\">"+value+"</a>";
	}
	
	//新增操作
	function doAddRecord(){
		var url="<emp:url action='getRscTaskInfoAddPage.do'/>?op=add";
		url = EMPTools.encodeURI(url);
		window.location = url;
	}

	//修改操作
	function doUpdateRecord(){ 
		var row = $('#RscTaskInfo').datagrid('getSelected');
		if(row){
			var url="<emp:url action='getRscTaskInfoUpdatePage.do'/>?op=update&"+ 'serno='+row.serno;
			url = EMPTools.encodeURI(url);
	
			params[0] = param;
			alert(message, params);
	  		// .messager;
		}
	}
	/**获取产品经理后、回调函数*/      
	function returnOrg(data) {
	    RscTaskInfo.input_br_id._setValue(data.organno._getValue());   //机构id
	    RscTaskInfo.input_br_id_displayname._setValue(data.organname._getValue());//名称机构id
	}
	</script>
	</body>
	</html>
</emp:page>