<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<emp:page>
<html>
<head> 
<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/EUIInclude.jsp" flush="true"/>
	<style type="text/css"></style>
</head>
<body class="page_content" >
	 	<div class='emp_gridlayout_title'>风险分类认定从表历史表列表</div>  
	 
	<!-- 列表信息 -->
	<emp:table icollName="RscTaskInfoSubHisList" url="getRscTaskInfoSubHisData.do"  title="风险分类认定从表历史表列表" >
 		<emp:text id="identy_duty" label="认定岗位" cssTDClass="tdCenter" hidden="true"/>
 		<emp:text id="identy_duty_cname" label="认定岗位"cssTDClass="tdCenter"/>
		<emp:select id="class_adjust_rst" label="分类调整结果 " dictname="STD_ZB_NINE_SORT" cssTDClass="tdCenter"/>
 		<emp:text id="remark" label="调整理由" cssTDClass="tdCenter"/>
		<emp:date id="class_date" label="分类日期" cssTDClass="tdCenter"/>
		<emp:text id="pk_id" label="主键" hidden="true"/>
 		<emp:text id="serno" label="风险分类认定编号" hidden="true"/>
	</emp:table>
	 
	<script type="text/javascript">
	 
	

	
	</script>
	</body>
	</html>
</emp:page>