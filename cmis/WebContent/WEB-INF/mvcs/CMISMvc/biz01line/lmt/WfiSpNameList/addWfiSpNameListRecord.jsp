<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function doOnLoad(){
		WfiSpNameList.cus_id._obj.addOneButton("cus","选择",getCus);
		WfiSpNameList.cus_id._obj._renderRequired(true); 
	}
	function getCus(){  
		   var main_br_id = '${context.organNo}';
		   var url = '<emp:url action="queryAllCusPop.do"/>?cusTypCondition=&returnMethod=returnCus';
			url = EMPTools.encodeURI(url);  
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow',param); 
	};
	function returnCus(data){
		WfiSpNameList.cus_id._setValue(data.cus_id._getValue());
		WfiSpNameList.manager_br_id._setValue(data.main_br_id._getValue());
		cus_id_displayname._setValue(data.cus_name._getValue());
		manager_br_id_displayname._setValue(data.main_br_id_displayname._getValue());
		WfiSpNameList.cus_id._obj._renderReadonly(true); 
		cus_id_displayname._obj._renderReadonly(true);
	};		
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	
	<emp:form id="submitForm" action="addWfiSpNameListRecord.do" method="POST">
		
		<emp:gridLayout id="WfiSpNameListGroup" title="授信特别权限授权名单表" maxColumn="1">
			<emp:text id="WfiSpNameList.pk_id" label="主键" maxlength="36" required="false" hidden="true"/>
			<emp:text id="WfiSpNameList.cus_id" label="客户号" maxlength="30" required="true" />
			<emp:text id="cus_id_displayname" label="客户名称" maxlength="100" required="true" />
			<emp:select id="WfiSpNameList.sp_right_type" label="特殊权限类型" dictname="STD_SP_RIGHT_TYPE" required="true" />
			<emp:textarea id="WfiSpNameList.memo" label="描述" maxlength="100" required="false" />
			<emp:text id="WfiSpNameList.app_date" label="申请日期" maxlength="10"  defvalue="${context.OPENDAY}" readonly="true"/>
			<emp:text id="WfiSpNameList.manager_br_id" label="责任机构码" maxlength="20" readonly="true" hidden="false"/>
			<emp:text id="manager_br_id_displayname" label="责任机构" maxlength="20" readonly="true" />
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="submit" label="确定" op="add"/>
			<emp:button id="reset" label="取消"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

