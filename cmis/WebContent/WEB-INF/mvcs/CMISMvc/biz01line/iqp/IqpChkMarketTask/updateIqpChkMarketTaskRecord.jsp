<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>详情查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script type="text/javascript">
	
	function doReturn() {
		var url = '<emp:url action="queryIqpChkMarketTaskList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	/*--user code begin--*/
	//异步提交申请数据
	function doSubmitValueAdj(data){
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("Y" == flag){
					//如果是点击提交需要 真正提交到流程中  2014-09-30
					if(data=='wf'){  //如果是点击流程提交
						doConfirm();
					}else{
						alert("保存成功！");
						
						var url = '<emp:url action="queryIqpChkMarketTaskList.do"/>';
						url = EMPTools.encodeURI(url);
						window.location=url;
					}
				}else{
					alert("保存失败,失败原因："+jsonstr.msg);
				}
			}
		};
		var handleFailure = function(o) {
			alert("保存失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};

		var form = document.getElementById("submitForm");
		var result = IqpMortValueAdj._checkAll();
		if(result){
			page.dataGroups.dataGroup_in_formsubmitForm.toForm(form);
			var postData = YAHOO.util.Connect.setForm(form);
			var url;
			//提交时需要走流程，此处只单纯的保存     2014-09-30 
			//if(data=='wf'){
			//	url = form.action + "?isWf=1";
			//}else{
				url = form.action;
			//}
			
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
		}else {
           alert("请输入必填项!");
           return ;
		}
	};

	function doSubmitWf(){
		if(confirm("提交后修改价格将生效，是否确认提交？")){
			doSubmitValueAdj('wf');
		}
	}

	function doSubmitSave(){
		doSubmitValueAdj('save');
	}

	//异步初始化流程中数据   2014-09-30
	function doConfirm(){
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.msg;
				if("Y" == flag){
					//生成完成后直接提交流程
					var serno = jsonstr.serno;
					doSubmitConfirm(serno,jsonstr.approve_status);
				}else{
					alert("确认失败,失败原因："+jsonstr.msg);
				}
			}
		};
		var handleFailure = function(o) {
			alert("初始化流程失败！");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		//盯市任务需要提交流程    2014-09-29   唐顺岩
		var adj_pk = IqpMortValueAdj.pk_id._getValue();
		var value_no = IqpMortValueAdj.value_no._getValue();
		var url = '<emp:url action="initIqpChkMarketTaskApp.do"/>&adj_pk='+adj_pk+"&value_no="+value_no;
		
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null) 
	};
	
	//提交流程   2014-09-30
	function doSubmitConfirm(serno,approve_status){
		var catalog_name = IqpMortValueMana.catalog_no_displayname._getValue();
		var change_valve = IqpMortValueAdj.change_valve._getValue();
		
		WfiJoin.table_name._setValue("IqpChkMarketTaskApp");
		WfiJoin.pk_col._setValue("serno");
		WfiJoin.pk_value._setValue(serno);
		WfiJoin.wfi_status._setValue(approve_status);	
		WfiJoin.status_name._setValue("approve_status");
		WfiJoin.appl_type._setValue("515");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：IqpChkMarketTask
		WfiJoin.cus_id._setValue("");//客户码
		WfiJoin.cus_name._setValue(catalog_name);//客户名称
		WfiJoin.amt._setValue(change_valve);//金额
		WfiJoin.prd_name._setValue("盯市任务管理");//产品名称
		initWFSubmit(false);
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<emp:tabGroup mainTab="base_tab" id="mainTab" >
	  <emp:tab label="盯市任务基本信息" id="base_tab" needFlush="true" initial="true" >
	  <emp:form id="submitForm" action="updateIqpChkMarketTaskRecord.do" method="POST">
		<emp:gridLayout id="IqpChkMarketTaskGroup" title="盯市任务信息" maxColumn="2">
			<emp:text id="IqpMortValueAdj.value_no" label="价格编号" maxlength="40" required="true" readonly="true" />
			<emp:select id="IqpMortValueAdj.status" label="状态" required="false" dictname="STD_CHKMARKET_TASK_STATUS" readonly="true" />
			<emp:pop id="IqpMortValueMana.catalog_no_displayname" label="目录编号" url="showCatalogManaTree.do" returnMethod="setCatalogPath" required="true" cssElementClass="emp_field_text_readonly" colSpan="2" readonly="true"/>
			<emp:pop id="IqpMortValueMana.produce_area_displayname" label="产地" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" required="false" returnMethod="setProduceArea" colSpan="2" cssElementClass="emp_field_text_readonly" readonly="true"/>
			<emp:pop id="IqpMortValueMana.produce_vender_displayname" label="生产厂家" url="" required="true" cssElementClass="emp_field_text_readonly" defvalue="11" colSpan="2" readonly="true"/>
			<emp:pop id="IqpMortValueMana.sale_area_displayname" label="销售区域" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" returnMethod="setSaleArea" required="false" cssElementClass="emp_field_text_readonly" colSpan="2" readonly="true"/>
			<emp:select id="IqpMortValueMana.freq_unit" label="盯市频率单位" required="true" dictname="STD_ZX_FREQ_UNIT" readonly="true" />
			<emp:text id="IqpMortValueMana.freq" label="盯市频率" maxlength="1" required="true" dataType="Int" cssElementClass="emp_currency_text_readonly" readonly="true" />
			<emp:date id="IqpMortValueMana.auth_date" label="价格核准时间" required="true" readonly="true" />
			<emp:text id="IqpMortValueMana.change_valve" label="价格变动阀值" maxlength="16" required="true" dataType="Percent" cssElementClass="emp_currency_text_readonly" />
			<emp:select id="IqpMortValueMana.is_qual_judge" label="是否需要品质鉴定" required="true" dictname="STD_ZX_YES_NO" readonly="true"/>
			<emp:select id="IqpMortValueAdj.info_sour" label="价格信息来源" required="true" dictname="STD_ZB_INFO_SOUR" />
			<emp:text id="IqpMortValueAdj.org_valve" label="上次商品核准价格" maxlength="18" required="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" />
			<emp:text id="IqpMortValueAdj.change_valve" label="此次商品核准价格" maxlength="18" required="true" dataType="Currency" />
			<emp:textarea id="IqpMortValueAdj.change_resn" label="价格变动原因" maxlength="200" required="true" colSpan="2" />
			<emp:textarea id="IqpMortValueAdj.memo" label="备注" maxlength="200" required="false" colSpan="2" readonly="false"/>
			<emp:text id="IqpMortValueAdj.input_id_displayname" label="登记人" required="true" readonly="true" hidden="true"/>
			<emp:text id="IqpMortValueAdj.input_br_id_displayname" label="登记机构" required="true" readonly="true" hidden="true"/> 
			<emp:date id="IqpMortValueAdj.input_date" label="登记日期" required="true" readonly="true" hidden="true"/>
			
			<emp:text id="IqpMortValueAdj.inure_date" label="变动生效时间" required="false" readonly="true" hidden="true"/>
			<emp:text id="IqpMortValueAdj.pk_id" label="物理主键" hidden="true" readonly="true" />
			<emp:text id="IqpMortValueMana.market_value" label="市场价" maxlength="18" hidden="true" dataType="Currency" />
		</emp:gridLayout>
		<div align="center">
			<emp:button id="submitSave" label="保存" op="update"/>
			<emp:button id="submitWf" label="提交" op="update"/>
			<emp:button id="return" label="返回"/>
		</div>
		</emp:form>
	
		<div class='emp_gridlayout_title'>价格变动历史&nbsp;</div>
		<emp:table icollName="IqpMortValueAdjList" pageMode="true" url="pageIqpMortValueAdjQuery.do?value_no=${context.IqpMortValueAdj.value_no}&pk_id=${context.IqpMortValueAdj.pk_id}">
			<emp:text id="org_valve" label="原有押品单价" dataType="Currency"/>
			<emp:text id="change_valve" label="此次核准单价" dataType="Currency"/>
			<emp:text id="info_sour" label="信息来源" dictname="STD_ZB_INFO_SOUR" />
			<emp:text id="inure_date" label="生效时间" />
		</emp:table>
		</emp:tab>
		<emp:ExtActTab></emp:ExtActTab>
		</emp:tabGroup>
		
	
</body>
</html>
</emp:page>
