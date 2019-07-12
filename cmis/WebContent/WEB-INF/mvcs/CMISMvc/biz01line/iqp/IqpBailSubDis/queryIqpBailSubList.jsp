<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpBailSubDis._toForm(form);
		IqpBailSubDisList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpBailSubDisPage() {
		var flag = '${context.flag}';
		var paramStr = IqpBailSubDisList._obj.getParamStr(['serno','cont_no']);
		var approve_status = IqpBailSubDisList._obj.getParamValue(['approve_status']);
		if (paramStr != null) {
			if(approve_status=="000"||approve_status=="991"||approve_status=="992"||approve_status=="993"){
				var url = '<emp:url action="getIqpBailSubDisUpdatePage.do"/>?'+paramStr+'&flag='+flag+'&op=update';
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else{
				alert("只有【待发起】、【打回】、【重办】状态的记录可以进行修改操作！")
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpBailSubDis() {
		var flag = '${context.flag}';
		var paramStr = IqpBailSubDisList._obj.getParamStr(['serno','cont_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpBailSubDisViewPage.do"/>?'+paramStr+'&flag='+flag+'&op=view';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpBailSubDisPage() {
		var flag = '${context.flag}';
		var url = '<emp:url action="getIqpBailSubDisAddLeadPage.do"/>?flag='+flag;
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpBailSubDis() {
		var paramStr = IqpBailSubDisList._obj.getParamStr(['serno']);
		var approve_status = IqpBailSubDisList._obj.getParamValue(['approve_status']);
		if (paramStr != null) {
			if(approve_status=="000"){
				if(confirm("是否确认要删除？")){
					var handleSuccess = function(o){
						if(o.responseText !== undefined) {
							try {
								var jsonstr = eval("("+o.responseText+")");
							} catch(e) {
								alert("Parse jsonstr1 define error!" + e.message);
								return;
							}
							var flag = jsonstr.flag;
							if(flag == "success"){
								alert("已成功删除!");
								window.location.reload();
							}else {
								alert("删除失败!"); 
							}
						}
					};
					var handleFailure = function(o){
						alert("异步请求出错！");	
					};
					var callback = {
						success:handleSuccess,
						failure:handleFailure
					};
					var url = '<emp:url action="deleteIqpBailSubDisRecord.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);	
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
				}
		     }else{
				alert("非待发起状态的记录不能进行删除操作！");
			 }
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.IqpBailSubDisGroup.reset();
	};
	//流程提交信息
	function submitFlow(){
		var paramStr = IqpBailSubDisList._obj.getParamStr(['serno']);
		if (paramStr != null) { 
			var approve_status = IqpBailSubDisList._obj.getSelectedData()[0].approve_status._getValue();
			var serno = IqpBailSubDisList._obj.getSelectedData()[0].serno._getValue();
			var cus_id = IqpBailSubDisList._obj.getSelectedData()[0].cus_id._getValue();
			var cus_name = IqpBailSubDisList._obj.getSelectedData()[0].cus_id_displayname._getValue();
			var adjust_bail_amt = IqpBailSubDisList._obj.getSelectedData()[0].adjust_bail_amt._getValue();
			WfiJoin.table_name._setValue("IqpBailSubDis");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(serno);
			WfiJoin.cus_id._setValue(cus_id);
			WfiJoin.cus_name._setValue(cus_name);
			WfiJoin.prd_name._setValue("保证金追加");
			WfiJoin.amt._setValue(adjust_bail_amt);
			WfiJoin.wfi_status._setValue(approve_status);
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("1001");
			initWFSubmit(false);
		}else{
            alert("请选择一条记录!");
		}
	}

	/** 保证金追加通知书打印 **/
	function doPrintIqpBailSubDis1(){
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var checked = jsonstr.checked;
				if("1" == checked){//追加保证金
					var approve_status = IqpBailSubDisList._obj.getParamValue(['approve_status']);
					if(approve_status == '997'){
						var paramStr = IqpBailSubDisList._obj.getParamStr(['serno']);
						var url = '<emp:url action="getReportShowPage.do"/>&reportId=psp/bzjzjtzs.raq&'+paramStr;
						url = EMPTools.encodeURI(url);
						window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
					}else{
						alert("只有状态为'通过'的才能被【打印】");
					}
				}else {//客户经理属于多个机构
					alert("新增的保证金暂不提供打印");	
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var paramStr = IqpBailSubDisList._obj.getParamStr(['serno']); 
		var url = '<emp:url action="checkIqpBailSubDisRecord.do"/>?'+paramStr;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	function doPrintIqpBailSubDis(){
		var paramStr = IqpBailSubDisList._obj.getParamStr(['serno']);
		if (paramStr != null) { 
			var approve_status = IqpBailSubDisList._obj.getParamValue(['approve_status']);
			if(approve_status == '997'){
				var paramStr = IqpBailSubDisList._obj.getParamStr(['serno']);
				var url = '<emp:url action="getReportShowPage.do"/>&reportId=psp/bzjzjtzs.raq&'+paramStr;
				url = EMPTools.encodeURI(url);
				window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
			}else{
				alert("只有状态为'通过'的才能被【打印】");
			}
		}else{
			alert("请选择一条记录!");
		}
		
	}
	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpBailSubDisGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpBailSubDis.serno" label="业务编号" />
			<emp:text id="IqpBailSubDis.cont_no" label="合同编号" />
			<emp:text id="IqpBailSubDis.cus_id" label="客户码" />
			<emp:select id="IqpBailSubDis.approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpBailSubDisPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpBailSubDisPage" label="修改" op="update"/>
		<emp:button id="deleteIqpBailSubDis" label="删除" op="remove"/>
		<emp:button id="viewIqpBailSubDis" label="查看" op="view"/>
		<emp:button id="printIqpBailSubDis" label="打印" op="printout"/>
	</div>

	<emp:table icollName="IqpBailSubDisList" pageMode="true" url="pageIqpBailSubDisQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="ori_bail_amt" label="原保证金金额" dataType="Currency"/>
		<emp:text id="adjust_bail_amt" label="追加保证金金额" dataType="Currency"/>
		<emp:text id="adjusted_bail_amt" label="追加后保证金金额" dataType="Currency"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" />
		<emp:text id="input_date" label="追加日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    