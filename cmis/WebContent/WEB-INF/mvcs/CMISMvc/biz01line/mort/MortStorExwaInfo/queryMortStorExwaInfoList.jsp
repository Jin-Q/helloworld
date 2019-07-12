<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/>
<script type="text/javascript">
	function doLoad(){
		var options = MortStorExwaInfo.stor_exwa_mode._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
			//判断出入库方式，若为"入库"，去掉选项
			if(options[i].value == "04"){
				options.remove(i);
			}
		}  
	}
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		MortStorExwaInfo._toForm(form);
		MortStorExwaInfoList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateMortStorExwaInfoPage() {
		var paramStr = MortStorExwaInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			//判断审批状态，如果审批中的，不允许修改或删除
			var data = MortStorExwaInfoList._obj.getSelectedData();
			var approve_status = data[0].approve_status._getValue();
			if(approve_status!='000' && approve_status!='992'&& approve_status!='993' ){
				alert("非待发起、退回、追回状态的评级申请无法修改");
				return;
			}
			var url = '<emp:url action="getMortStorExwaInfoUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewMortStorExwaInfo() {
		var paramStr = MortStorExwaInfoList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getMortStorExwaInfoViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddMortStorExwaInfoPage() {
		var url = '<emp:url action="getMortStorExwaInfoAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteMortStorExwaInfo() {
		var paramStr = MortStorExwaInfoList._obj.getParamStr(['serno']);
		var status = MortStorExwaInfoList._obj.getParamStr(['approve_status']);
		if(status=="approve_status=000"){
			if (paramStr != null) {
				if(confirm("是否确认要删除？")){
					var handleSuccess = function(o) {
						if (o.responseText !== undefined) {
							try {
								var jsonstr = eval("(" + o.responseText + ")");
							} catch (e) {
								alert("删除失败！");
								return;
							}
							var flag = jsonstr.flag;
							if(flag=='success'){	
								alert("已成功删除！");
								window.location.reload();
							}else{
								alert("删除失败");
							}   
						}	
					};
					var handleFailure = function(o) {
						alert("删除失败!");
					};
					var callback = {
						success :handleSuccess,
						failure :handleFailure
					};
					var url = '<emp:url action="deleteMortStorExwaInfoRecord.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
				}
			} else {
				alert('请先选择一条记录！');
			}
		}else{
			alert("非登记状态的记录不能被删除！");
		}
	};
	
	function doReset(){
		page.dataGroups.MortStorExwaInfoGroup.reset();
	};
	function doSubmitWfRecord(){
		var paramStr = MortStorExwaInfoList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			var approve_status = MortStorExwaInfoList._obj.getParamValue(['approve_status']);
			WfiJoin.table_name._setValue("MortStorExwaInfo");
			WfiJoin.pk_col._setValue("serno");
			WfiJoin.pk_value._setValue(paramStr);
			WfiJoin.wfi_status._setValue(approve_status);	
			WfiJoin.status_name._setValue("approve_status");
			WfiJoin.appl_type._setValue("810");  //流程申请类型，对应字典项[ZB_BIZ_CATE]，对应流程标识：LMT_APP_FLOW
		//	WfiJoin.cus_id._setValue(cus_id);//客户码
		//	WfiJoin.cus_name._setValue(cus_id_displayname);//客户名称
	    //		WfiJoin.amt._setValue(crd_totl_amt);//金额
			WfiJoin.prd_name._setValue("权证出库申请");//产品名称
			initWFSubmit(false);
		}else {
			alert('请先选择一条记录！');
		}
	}
	/*--user code begin--*/
	function doPrint(){
		var paramStr = MortStorExwaInfoList._obj.getParamValue(['serno']);
		if (paramStr != null) {
			var status = MortStorExwaInfoList._obj.getParamValue(['approve_status']);
			var stor_exwa_mode = MortStorExwaInfoList._obj.getParamValue(['stor_exwa_mode']);//出入库方式
			if(status=='997'){
				var serno = MortStorExwaInfoList._obj.getParamValue(['serno']);
				if(stor_exwa_mode=='01'){//取出还贷
					var url = '<emp:url action="getReportShowPage.do"/>&reportId=MortStor/ypckd2_qchd.raq&serno='+serno;
					url = EMPTools.encodeURI(url);
					window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
				}else if(stor_exwa_mode=='02'){//临时借出
					var url = '<emp:url action="getReportShowPage.do"/>&reportId=MortStor/ypjyspb1.raq&serno='+serno;
					url = EMPTools.encodeURI(url);
					window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
				}else{//注销出库
					var url = '<emp:url action="getReportShowPage.do"/>&reportId=MortStor/ypckd1.raq&serno='+serno;
					url = EMPTools.encodeURI(url);
					window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
				}
			}else{
				alert("只有'通过'状态的记录才能进行【打印】操作！");
			}
		}else{
			alert('请先选择一条记录！');
		}
	}		
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="MortStorExwaInfoGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="MortStorExwaInfo.serno" label="业务编号" />
			<emp:select id="MortStorExwaInfo.stor_exwa_mode" label="出入库方式" dictname="STD_STOR_EXWA_MODEL" />
			<emp:date id="MortStorExwaInfo.back_date" label="归还时间" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<!--<emp:button id="getAddMortStorExwaInfoPage" label="新增" op="add"/>
		--><emp:button id="getUpdateMortStorExwaInfoPage" label="修改" op="update"/>
		<emp:button id="deleteMortStorExwaInfo" label="删除" op="remove"/>
		<emp:button id="viewMortStorExwaInfo" label="查看" op="view"/>
		<emp:button id="submitWfRecord" label="提交" op="submit"/>
		<emp:button id="print" label="打印" op="print"/>
	</div>

	<emp:table icollName="MortStorExwaInfoList" pageMode="true" url="pageMortStorExwaInfoQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="stor_exwa_mode" label="出入库方式" dictname="STD_STOR_EXWA_MODEL" />
		<emp:text id="back_date" label="归还时间" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="approve_status" label="审批状态" dictname="WF_APP_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    