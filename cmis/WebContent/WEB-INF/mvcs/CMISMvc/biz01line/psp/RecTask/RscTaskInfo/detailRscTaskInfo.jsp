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
	<form id="detailForm" method="post" novalidate></form>
	<emp:panel name="p1" title="风险分类认定">
		<emp:gridLayout id="detailReadOnlyForm" maxColumn="2"  >
   			<emp:text id="RscTaskInfo.serno" label="主键" maxlength="40"   readonly="true" />
   			<emp:text id="RscTaskInfo.bill_no" label="借据编号" maxlength="40"   readonly="true" />
   			<emp:text id="RscTaskInfo.cus_id" label="客户编号" maxlength="40"   readonly="true" />
   			<emp:text id="RscTaskInfo.prd_id" label="产品编号" maxlength="40"   readonly="true" />
   			<emp:select id="RscTaskInfo.cur_type" label="币种 STD_ZB_CUR_TYP" dictname="STD_ZX_CUR_TYPE"   readonly="true"/>
   			<emp:text id="RscTaskInfo.loan_amt" label="贷款金额" maxlength="22"   readonly="true" />
   			<emp:text id="RscTaskInfo.loan_balance" label="贷款余额" maxlength="22"   readonly="true" />
			<emp:date id="RscTaskInfo.start_date" label="起始日期"   readonly="true" />
			<emp:date id="RscTaskInfo.end_date" label="到期日期"   readonly="true" />
   			<emp:select id="RscTaskInfo.pre_class_rst" label="上期分类结果 STD_ZB_NINE_SORT" dictname="STD_ZB_NINE_SORT"   readonly="true"/>
   			<emp:select id="RscTaskInfo.model_class_rst" label="模型分类结果 STD_ZB_NINE_SORT" dictname="STD_ZB_NINE_SORT"   readonly="true"/>
			<emp:date id="RscTaskInfo.class_date" label="分类日期"   readonly="true" />
   			<emp:select id="RscTaskInfo.class_rst" label="认定结果 STD_ZB_NINE_SORT" dictname="STD_ZB_NINE_SORT"   readonly="true"/>
   			<emp:text id="RscTaskInfo.remark" label="认定理由" maxlength="400"   readonly="true" />
   			<emp:text id="RscTaskInfo.manager_id" label="主办人" maxlength="20"   readonly="true" />
   			<emp:text id="RscTaskInfo.manager_br_id" label="主办机构" maxlength="20"   readonly="true" />
   			<emp:text id="RscTaskInfo.input_id" label="登记人" maxlength="20"   readonly="true" />
   			<emp:text id="RscTaskInfo.input_br_id" label="登记机构" maxlength="20"   readonly="true" />
			<emp:date id="RscTaskInfo.input_date" label="登记日期"   readonly="true" />
   			<emp:text id="RscTaskInfo.upd_id" label="更新人" maxlength="20"   readonly="true" />
   			<emp:text id="RscTaskInfo.upd_br_id" label="更新机构" maxlength="20"   readonly="true" />
			<emp:date id="RscTaskInfo.upd_date" label="更新日期"   readonly="true" />
   			<emp:select id="RscTaskInfo.risk_cls_status" label="风险分类状态 STD_ZB_RISK_CLS_ST" dictname="STD_ZB_RISK_CLS_ST"   readonly="true"/>
		</emp:gridLayout>
	</emp:panel>
		
	<div id="dlg-buttons" align="center">        
		<emp:button label="取消" id="close"  ></emp:button>
	</div> 
	<script type="text/javascript"> 
	//多次使用请将jQuery对象缓存进变量，避免执行多次选择
	var fm=$('#detailForm');
	
	
	//初始化
//	$(function(){
//			var loadurl="<emp:url action='getRscTaskInfoUpdateData.do'/>?"+ 'RscTaskInfo.serno='+'${param.serno}';
//			loadurl=EMPTools.encodeURI(loadurl);
//			fm.form('iload',loadurl);	// load from URL
//	});
	
	//关闭
	function doClose(){
		window.close();
	}

	</script>
	</body>
	</html>
</emp:page>