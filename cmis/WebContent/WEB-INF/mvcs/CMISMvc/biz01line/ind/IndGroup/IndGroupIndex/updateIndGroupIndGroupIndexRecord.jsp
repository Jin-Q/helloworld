<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	  
		 function doload(){
			 IndGroupIndex.index_no._obj.config.url=IndGroupIndex.index_no._obj.config.url+"&returnMethod=returnIndexNo";
	
			 }
		 function returnIndexNo(data){ 
			 IndGroupIndex.index_no._setValue(data.index_no._getValue());  
			 IndGroupIndex.index_name._setValue(data.index_name._getValue());
	       }
		 function doReturnList(){ 
				window.close();
				}		 
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="updateIndGroupIndGroupIndexRecord.do" method="POST">
		<emp:gridLayout id="IndGroupIndexGroup" title="设置指标组与指标关联" maxColumn="2">
			<emp:text id="IndGroupIndex.group_no" label="组别编号" maxlength="12" required="true" readonly="true" />
			<emp:pop id="IndGroupIndex.index_no" label="指标编号" required="true"  url="queryIndLibPopList.do" returnMethod="returnIndexNo"/>
			<emp:text id="IndGroupIndex.index_name" label="指标名称" maxlength="60" required="false" readonly="true"/>
			<emp:text id="IndGroupIndex.ind_std_score" label="满分值" maxlength="16" required="false" />
			<emp:text id="IndGroupIndex.full_score" label="参照值" maxlength="20" required="false" dataType="number"/> 
			<emp:text id="IndGroupIndex.weight" label="权重" maxlength="9" required="false" defvalue="1"/>
			<emp:select id="IndGroupIndex.score_way" label="评分方式" required="false" dictname="STD_ZB_SCORE_WAY"/>
			<emp:textarea id="IndGroupIndex.rule_classpath" label="指标评分规则" required="false" colSpan="2"/>
			<emp:text id="IndGroupIndex.category" label="指标大类" maxlength="1" required="false"  hidden="true"/>
			<emp:text id="IndGroupIndex.sub_category" label="指标小类" maxlength="1" required="false"  hidden="true"/>
			<emp:select id="IndGroupIndex.dis_property" label="显示属性"   required="false" dictname="STD_ZB_DISPLAY_PROP"/>
			<emp:select id="IndGroupIndex.ind_dis_type" label="指标显示类型"  required="false" dictname="STD_ZB_PARA_DISP_TYP"/>
			<emp:text id="IndGroupIndex.seq_no" label="顺序号" maxlength="38" required="false" />
			
			<emp:text id="IndGroupIndex.reference_value" label="参照值" maxlength="38" required="false" hidden="true"/>
			<emp:text id="IndGroupIndex.limit_value" label="极限值" maxlength="38" required="false" />
			
			<emp:select id="IndGroupIndex.limit_flag" label="极限值符号" required="false" dictname="STD_LIMIT_FLAG"/>
			<emp:textarea id="IndGroupIndex.memo_cal" label="评分计算公式" required="false" colSpan="2"/>
			<emp:textarea id="IndGroupIndex.memo" label="评分标准" required="false" colSpan="2"/>
		</emp:gridLayout> 
		<div align="center">
			<br>
			<emp:button id="submit" label="保存" op="update_IndGroupIndex"/>
			<emp:button id="returnList" label="关闭"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
