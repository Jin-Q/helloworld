<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>
<jsp:include page="/include.jsp" />

<script type="text/javascript">

	var page = new EMP.util.Page();
	function doOnLoad() {
		dealAudit();
		dealAdjt();
		page.renderEmpObjects();

		FncStatBase.input_date._setValue("${context.OPENDAY}");

		FncStatBase.last_upd_id._setValue("${context.currentUserId}");
		FncStatBase.last_upd_id_displayname._setValue("${context.currentUserName}");
	}
	
	function doUpdateFncStatBase() {
		var form = document.getElementById('submitForm');
		var result = FncStatBase._checkAll();
		if(result){
			FncStatBase._toForm(form)
			form.submit();
		}
	};


	function dealAudit(){
		//是否经过调整
		//var adjt = FncStatBase.stat_is_adjt._obj.element.value;
		var audit = FncStatBase.stat_is_audit._getValue();
		
		if(audit=="1"){//经过审计
			FncStatBase.stat_adt_entr._obj.config.hidden=false;
			FncStatBase.stat_adt_entr._obj._renderStatus();
			
			FncStatBase.stat_adt_conc._obj.config.hidden=false;
			FncStatBase.stat_adt_conc._obj._renderStatus();
		}else{
			FncStatBase.stat_adt_entr._obj.config.hidden=true;
			FncStatBase.stat_adt_entr._obj._renderStatus();

			FncStatBase.stat_adt_conc._obj.config.hidden=true;
			FncStatBase.stat_adt_conc._obj._renderStatus();
		}		
	}
	
	function dealAdjt(){
		//是否经过调整
		//var adjt = FncStatBase.stat_is_adjt._obj.element.value;
		var adjt = FncStatBase.stat_is_adjt._getValue();
		
		if(adjt=="1"){//经过调整
			FncStatBase.stat_adj_rsn._obj.config.hidden=false;
			FncStatBase.stat_adj_rsn._obj._renderStatus();
		}else{
			FncStatBase.stat_adj_rsn._obj.config.hidden=true;
			FncStatBase.stat_adj_rsn._obj._renderStatus();
		}		
	}
	
	function doReset(){
		page.dataGroups.FncStatBaseGroup.reset();
	};
	
	function doCheckStatFlg(){
		var statFlg = state_flg._getValue();
		var sbFlg = statFlg.substring(0,1);
		var ibFlg = statFlg.substring(1,2);
		var cfsFlg = statFlg.substring(2,3);
		var fiFlg = statFlg.substring(3,4);
		var soeFlg = statFlg.substring(4,5);
		var slFlg = statFlg.substring(5,6);
		
		var flgStr = statFlg.substring(8);//得到财报状态的标志位，2 表示这一套报表都已经提交
	
		if(flgStr == "2"){
			if(FncStatBase._checkAll()){
				doUpdateFncStatBase();
			}
		}else{
			if(slFlg != "9" && slFlg != "2"){
				alert("在维护财务报表审计信息之前，请先完成财务简表信息");
			}else {
				if(sbFlg != "9" && sbFlg != "2"){
					alert("在维护财务报表审计信息之前，请先完成资产负债表信息");
				}
				if(ibFlg != "9" && ibFlg != "2"){
					alert("在维护财务报表审计信息之前，请先完成损益表信息");
				}
				if(cfsFlg != "9" && cfsFlg != "2"){
					alert("在维护财务报表审计信息之前，请先完成现金流量表信息");
				}
				if(fiFlg != "9" && fiFlg != "2"){
					alert("在维护财务报表审计信息之前，请先完成财务指标表信息");
				}
				if(soeFlg != "9" && soeFlg != "2"){
					alert("在维护财务报表审计信息之前，请先完成所有者权益变动表信息");
				}
			}
		}
	};
</script>
<style type="text/css">
.emp_field_text_input_0 {
width:300px;
}
</style>
</head>
<body class="page_content" onload="doOnLoad()">
	<form id="submitForm" action="<emp:url action='updateFncStatBaseAdt.do'/>" method="POST">
		<emp:text id="cus_id" label="客户代码" hidden="true"/>
		<emp:text id="stat_prd_style" label="报表周期类型" hidden="true"/>
		<emp:text id="stat_style" label="报表口径" hidden="true"/>
		<emp:text id="stat_prd" label="报表期间" hidden="true"/>
		<emp:text id="fnc_conf_data_col" label="数据列数" hidden="true"/>
		<emp:text id="fnc_name" label="报表名称"  hidden="true"/>
		<emp:text id="style_id" label="报表样式编号"  hidden="true"/>
		<emp:text id="state_flg" label="状态"  hidden="true"/>
		<emp:text id="fnc_type" label="报表类型"  hidden="true"/>
	</form>
	<div id="FncStatBaseGroup" class="emp_group_div">
		<emp:gridLayout id="FncStatBaseGroup" maxColumn="1" title="公司客户报表">
			<emp:text id="FncStatBase.cus_id" label="客户码" maxlength="20" required="true" readonly="true" colSpan="2"/>
			<emp:select id="FncStatBase.stat_prd_style" label="报表周期类型" required="true" readonly="true" dictname="STD_ZB_FNC_STAT"/>
			<emp:text id="FncStatBase.stat_prd" label="报表期间" maxlength="6" required="true" readonly="true" />
			<emp:select id="FncStatBase.stat_style" label="报表口径"  required="true" dictname="STD_ZB_FNC_STYLE" colSpan="2" readonly="true"/>
			<emp:select id="FncStatBase.stat_is_audit" label="是否经过审计" required="true" dictname="STD_ZX_YES_NO" onchange="dealAudit()"/>
			<emp:select id="FncStatBase.stat_is_adjt" label="是否经过调整" colSpan="2" required="true" dictname="STD_ZX_YES_NO" onchange="dealAdjt()"/>
			<emp:text id="FncStatBase.fnc_type" label="报表类型" required="true" hidden="true" />
			<emp:textarea id="FncStatBase.stat_adt_conc" label="审计结论" colSpan="2" maxlength="200" required="false" />
			<emp:textarea id="FncStatBase.stat_adj_rsn" label="财务报表调整原因"  colSpan="2" maxlength="200" required="false" />
			<emp:text id="FncStatBase.stat_adt_entr" label="审计单位" colSpan="2" maxlength="60" required="false" cssElementClass="emp_field_text_input_0"/>
			<emp:text id="FncStatBase.last_upd_id" label="编辑人" maxlength="60" hidden="true" readonly="true" required="false"/>
			<emp:text id="FncStatBase.last_upd_id_displayname" label="编辑人"  readonly="true" required="true"/>
			<emp:text id="FncStatBase.input_id" label="登记人"  maxlength="60" hidden="true" readonly="true" required="false" />
			<emp:text id="FncStatBase.input_id_displayname" label="登记人"  readonly="true" required="false" />
			<emp:date id="FncStatBase.input_date" label="登记日期" readonly="true" required="false" />
		</emp:gridLayout>
		</div>
		
	<div align="center">
		<br>
		<emp:button id="checkStatFlg" label="完成"/>
	</div>
</body>
</html>
</emp:page>
