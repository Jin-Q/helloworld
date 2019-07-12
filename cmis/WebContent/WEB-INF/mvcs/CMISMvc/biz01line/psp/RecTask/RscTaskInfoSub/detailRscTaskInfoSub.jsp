<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<emp:page>
<html>
<head> 
<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/EUIInclude.jsp" flush="true"/>
	<style type="text/css"></style>
</head>
	<body class="page_content">
	<form id="detailForm" method="post" novalidate></form>
	<emp:panel id="p1" title="风险分类认定从表">
		<emp:gridLayout id="detailReadOnlyForm" maxColumn="2" hiddenBorder="true">
   			<emp:text id="RscTaskInfoSub-pk_id" label="主键" maxLength="40"   readonly="true" />
   			<emp:text id="RscTaskInfoSub-serno" label="风险分类认定编号" maxLength="40"   readonly="true" />
   			<emp:text id="RscTaskInfoSub-identy_duty" label="认定岗位" maxLength="80"   readonly="true" />
   			<emp:select id="RscTaskInfoSub-class_adjust_rst" label="分类调整结果  STD_ZB_NINE_SORT" dictname="STD_ZB_NINE_SORT"   readonly="true"/>
   			<emp:text id="RscTaskInfoSub-remark" label="调整理由" maxLength="400"   readonly="true" />
			<emp:date id="RscTaskInfoSub-class_date" label="分类日期"   readonly="true" />
		</emp:gridLayout>
	</emp:panel>
		
	<div id="dlg-buttons" align="center">        
		<emp:button text="取消" id="close" iconCls="icon-cancel"></emp:button>
	</div> 
	<script type="text/javascript"> 
	//多次使用请将jQuery对象缓存进变量，避免执行多次选择
	var fm=$('#detailForm');
	
	
	//初始化
	$(function(){
			var loadurl="<emp:url action='getRscTaskInfoSubUpdateData.do'/>?"+ 'RscTaskInfoSub.pk_id='+'${param.pk_id}'
;
			loadurl=EMPTools.encodeURI(loadurl);
			fm.form('iload',loadurl);	// load from URL
	});
	
	//关闭
	function doClose(){
		window.parent.EMP.closewin(); 
	}

	</script>
	</body>
	</html>
</emp:page>