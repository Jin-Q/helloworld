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
	<form id="addDetailForm" method="post">
	<emp:panel id="p1" title="风险分类认定">
		<emp:gridLayout id="detailForm" maxColumn="2" >
   			<emp:text id="RscTaskInfo.serno" label="主键" maxLength="40" required="true"  />
   			<emp:text id="RscTaskInfo.bill_no" label="借据编号" maxLength="40" required="true"  />
   			<emp:text id="RscTaskInfo.cus_id" label="客户编号" maxLength="40" required="true"  />
   			<emp:text id="RscTaskInfo.prd_id" label="产品编号" maxLength="40" required="false"  />
   			<emp:select id="RscTaskInfo.cur_type" label="币种 STD_ZB_CUR_TYP" dictname="STD_ZX_CUR_TYPE" required="false"  />
   			<emp:text id="RscTaskInfo.loan_amt" label="贷款金额" maxLength="22" required="false"  />
   			<emp:text id="RscTaskInfo.loan_balance" label="贷款余额" maxLength="22" required="false"  />
   			<emp:date id="RscTaskInfo.start_date" label="起始日期" required="false"  />
   			<emp:date id="RscTaskInfo.end_date" label="到期日期" required="false"  />
   			<emp:select id="RscTaskInfo.pre_class_rst" label="上期分类结果 STD_ZB_NINE_SORT" dictname="STD_ZB_NINE_SORT" required="false"  />
   			<emp:select id="RscTaskInfo.model_class_rst" label="模型分类结果 STD_ZB_NINE_SORT" dictname="STD_ZB_NINE_SORT" required="false"  />
   			<emp:date id="RscTaskInfo.class_date" label="分类日期" required="false"  />
   			<emp:select id="RscTaskInfo.class_rst" label="认定结果 STD_ZB_NINE_SORT" dictname="STD_ZB_NINE_SORT" required="false"  />
   			<emp:text id="RscTaskInfo.remark" label="认定理由" maxLength="400" required="false"  />
   			<emp:text id="RscTaskInfo.manager_id" label="主办人" maxLength="20" required="true"  />
   			<emp:text id="RscTaskInfo.manager_br_id" label="主办机构" maxLength="20" required="true"  />
   			<emp:text id="RscTaskInfo.input_id" label="登记人" maxLength="20" required="true"  />
   			<emp:text id="RscTaskInfo.input_br_id" label="登记机构" maxLength="20" required="true"  />
   			<emp:date id="RscTaskInfo.input_date" label="登记日期" required="true"  />
   			<emp:text id="RscTaskInfo.upd_id" label="更新人" maxLength="20" required="false"  />
   			<emp:text id="RscTaskInfo.upd_br_id" label="更新机构" maxLength="20" required="false"  />
   			<emp:date id="RscTaskInfo.upd_date" label="更新日期" required="false"  />
   			<emp:select id="RscTaskInfo.risk_cls_status" label="风险分类状态 STD_ZB_RISK_CLS_ST" dictname="STD_ZB_RISK_CLS_ST" required="false"  />
		</emp:gridLayout>
	</emp:panel>
	</form>	
	<div id="dlg-buttons" align="center">        
		<emp:button text="保存" id="save" iconCls="icon-ok" op="add"></emp:button>
		<emp:button text="取消" id="close" iconCls="icon-cancel"></emp:button>
	</div>
	<script type="text/javascript"> 
	//多次使用请将jQuery对象缓存进变量，避免执行多次选择
	var addDetailForm = $('#addDetailForm');

	//保存
	function doSave(){
		var result=$('#detailForm').checkAll();
		if(!result) {
			return false;
		} else {
			var formData = $('#addDetailForm').toJsonData();
			$.ajax({ 
				type: "POST", 
				dataType : 'html',
				url: "<emp:url action='addRscTaskInfoData4kcoll.do'/>",
				data: formData, 
				success: function(data) { 
					try {
						var jsonstr = eval("("+data+")");
					} catch(e) {
						EMP.alertException(data);
						return;
					}
					var flag = jsonstr.flag;
					if(flag == "success"){
						alert("保存成功！");
						 
					}else{
						alert("保存失败！");
					}
				}
			});
		}
	}

	//关闭
	function doClose(){
		window.close();
	}

	</script>
</body>
</html>
</emp:page>