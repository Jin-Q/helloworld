<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<emp:page> 
<html>
<head> 
<jsp:include page="/include.jsp" flush="true"/> 
<jsp:include page="/EUIInclude.jsp" flush="true"/>
</head>
<body class="page_content" >

		<form  method="POST" action="#" id="queryForm">
	</form>
			<emp:gridLayout id="RscTaskInfoGroup" title="输入查询条件" maxColumn="2">
			    <emp:text id="RscTaskInfo.bill_no" label="借据编号 " />
 				<emp:pop id="RscTaskInfo.input_id_cname"     label="客户经理名称"   
 				 url="getSUsrPopList.do" returnMethod="returnRegUser"  />
			 	<emp:date id="RscTaskInfo.class_date" label="分类日期 " />
			 	<emp:text id="RscTaskInfo.input_id" label="客户编号"  hidden="true" /> 
			</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	
	<!-- 列表信息 -->
	<emp:table icollName="RscTaskInfoList" url="getRscTaskInfoData.do?RscTaskInfo.input_br_id=${context.input_br_id}&RscTaskInfo.flag=unDo"    >
 		<emp:text id="serno" label="主键" hidden="true"/>
 		<emp:text id="bill_no" label="借据编号"/>
 		<emp:text id="input_id" label="客户经理编号"/>
 		<emp:text id="input_id_displayname"    label="客户经理名称"/>		
		<emp:date id="pre_class_date" label="上期分类日期" />
		<emp:date id="class_date" label="分类日期" />
		<emp:select id="class_rst" label="分类认定结果 " dictname="STD_ZB_NINE_SORT" hidden="true"/>
	</emp:table>
	<div style="margin-top:50px;"></div>
				
	<div  align="center">
			<emp:button label="返回" id="close" ></emp:button>
	</div> 
	<script type="text/javascript">
	function doQuery(){
		var form = document.getElementById('queryForm');
		RscTaskInfo._toForm(form);
		RscTaskInfoList._obj.ajaxQuery(null,form);
	};
	function doReset(){
		page.dataGroups.RscTaskInfoGroup.reset();
	};
	//关闭
	function doClose(){
		history.go(-1);
	}
	/**POP反显主办人,主办机构**/
	function returnRegUser(data){
		$('#equals-input_id_cname').setValue(data.usr_name);
		$('#equals-input_id').setValue(data.usr_cde);
	}
	</script>
	</body>
	</html>
</emp:page>