<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<emp:page>
<html>
<head> 
<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/EUIInclude.jsp" flush="true"/>
	<style type="text/css"></style>
</head>
	<body class="page_content" onload="onload()" >
	
	<emp:tabGroup id="rsc_tab"   mainTab ="maintab" >
		<emp:tab id="maintab" label="风险分类调整">  
		<form id="detailForm" method="post">	
			<emp:panel id="p1" title="风险分类调整信息">
			<emp:gridLayout id="detailReadOnlyForm" maxColumn="2"  > 
					<emp:text id="RscTaskInfoHis.bill_no" label="借据编号" maxlength="40" required="false" readonly="true" />
		   			<emp:text id="RscTaskInfoHis.cus_id" label="客户编号" maxlength="40" required="true" readonly="true" />
		   			<emp:text id="RscTaskInfoHis.cus_id_cname" label="客户名称"  required="false"  readonly="true"/>
		   			<emp:text id="RscTaskInfoHis.prd_id" label="产品编号" maxlength="40" required="false"  readonly="true"/>
		   			<emp:text id="RscTaskInfoHis.prd_id_cname" label="产品名称" required="false"  readonly="true"/>
		   			<emp:select id="RscTaskInfoHis.cur_type" label="币种 " dictname="STD_ZB_CUR_TYP" required="true"  readonly="true"/>
		   			<emp:text id="RscTaskInfoHis.loan_amt" label="贷款金额"  dataType="Currency" precision="2"   precision="2" required="true"  readonly="true"/>
		   			<emp:text id="RscTaskInfoHis.loan_balance" label="贷款余额"  dataType="Currency" precision="2"   precision="2" required="false"  readonly="true" />
		   			<emp:date id="RscTaskInfoHis.start_date" label="起始日期" required="true"  readonly="true"/>
		   			<emp:date id="RscTaskInfoHis.end_date" label="到期日期" required="true"  readonly="true"/>
		   			<emp:select id="RscTaskInfoHis.pre_class_rst" label="上期分类结果 " dictname="STD_ZB_NINE_SORT" readonly="true" required="true"  />
		   			<emp:select id="RscTaskInfoHis.model_class_rst" label="机评分类结果 " dictname="STD_ZB_NINE_SORT" required="true"  readonly="true"/>
					<emp:textarea id="RscTaskInfoHis.model_class_rea" label="机评理由"   maxlength="400" required="true"    colSpan="2"/> 
		   			<emp:select id="RscTaskInfoHis.model_eval_mac" label="模型分类结果  " dictname="STD_ZB_NINE_SORT" align="center" required="true"  readonly="true" />
		   			<emp:date id="RscTaskInfoHis.pre_class_date" label="上期分类日期" required="true"  readonly="true" />
		   			<emp:date id="RscTaskInfoHis.class_date" label="分类日期" required="false"  readonly="true" />
		   			<emp:select id="RscTaskInfoHis.class_rst" label="分类认定结果 " dictname="STD_ZB_NINE_SORT" required="true"  />
		   			<emp:textarea id="RscTaskInfoHis.remark" label="认定理由" maxlength="400" required="true"    colSpan="2"/>
		   			<emp:select id="RscTaskInfoHis.risk_cls_status" label="风险分类状态 " dictname="STD_ZB_RISK_CLS_ST" required="false" hidden="true" />
		   			<emp:text id="RscTaskInfoHis.serno" label="主键" maxlength="40" required="false"  hidden="true"/>
				</emp:gridLayout>
			</emp:panel>
			<emp:panel id="p2" title="登记信息">
				<emp:gridLayout id="detailDataForm2" maxColumn="2"  >
		   			<emp:text id="RscTaskInfoHis.input_id_displayname" label="登记人"    readonly="true"/>
		   			<emp:text id="RscTaskInfoHis.input_br_id_displayname" label="登记机构"      readonly="true"/>
					<emp:date id="RscTaskInfoHis.input_date" label="登记日期"     readonly="true"/>
					<emp:text id="RscTaskInfoHis.input_id" label="登记人"    readonly="true" hidden="true"/>
		   			<emp:text id="RscTaskInfoHis.input_br_id" label="登记机构"      readonly="true" hidden="true"/>
		   			<emp:text id="RscTaskInfoHis.upd_id_displayname" label="更新人"  readonly="true" hidden="true"/>
		   			<emp:text id="RscTaskInfoHis.upd_br_id_displayname" label="更新机构"   readonly="true"  hidden="true"/>
		   			<emp:date id="RscTaskInfoHis.upd_date" label="更新日期"  readonly="true"  hidden="true"/>
				</emp:gridLayout>
			</emp:panel>
			<div id="dlg-buttons" align="center">       
				<emp:button label="返回" id="close" ></emp:button>
			</div>
		</form>
		</emp:tab>
		<emp:tab id="RscTaskInfoHis_Tab" label="分类调整历史" url="getRscTaskInfoSubHisListPage.do?RscTaskInfoSubHis.serno=${param.serno}"   ></emp:tab>
	</emp:tabs> 
	<script type="text/javascript"> 
	//多次使用请将jQuery对象缓存进变量，避免执行多次选择
	var fm=$('#detailForm');
	function onload(){
		RscTaskInfoHis.bill_no._obj.addOneButton("bill_no","查看",doLookbill);
	}
 
	//查看操作
	function doLookbill(){ 
		var bill_no = RscTaskInfoHis.bill_no._getValue();
		var url = '<emp:url action="getAccLoanViewPage.do"/>?bill_no='+bill_no+"&isHaveButton=not";
		 url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		 
	}
	
	//关闭
	function doClose(){
		window.parent.EMP.closewin(); 
	}

	</script>
	</body>
	</html>
</emp:page>