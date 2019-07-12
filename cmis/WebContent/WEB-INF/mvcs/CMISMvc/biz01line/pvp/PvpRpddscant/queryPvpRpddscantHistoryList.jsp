<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		PvpLoanApp._toForm(form);
		PvpLoanAppList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePvpLoanAppPage() {
		var paramStr = PvpLoanAppList._obj.getParamStr(['serno','cont_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getPvpRpddscantUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewPvpLoanApp() {
		var paramStr = PvpLoanAppList._obj.getParamStr(['serno','cont_no']);

		if (paramStr != null) {
			var url = '<emp:url action="getPvpRpddscantViewPage.do"/>?'+paramStr+"&flag=pvpLoan&showButton=Y";
			url = EMPTools.encodeURI(url);
			//window.location = url;
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {  
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddPvpLoanAppPage() {
		var url = '<emp:url action="getPvpRpddscantAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeletePvpLoanApp() {
		var paramStr = PvpLoanAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要删除？")){
				var url = '<emp:url action="deletePvpRpddscantRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url); 
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.PvpLoanAppGroup.reset();
	};
//------生成授权信息------
	function doGetAuthorize(){
		return false;//暂不可用
		var paramStr = PvpLoanAppList._obj.getParamStr(['serno']);
		if (paramStr != null) {
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
						alert("生成授权信息成功！");
						window.location.reload();
					}else {
						alert("生成授权信息失败！");
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
			var url = '<emp:url action="getAuthorizeRecord.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
		} else {
			alert('请先选择一条记录！');
		}
	};
	function returnCus(data){
		PvpLoanApp.cus_id._setValue(data.cus_id._getValue());
	};
	/*--user code begin--*/
	function doPrint(){
		var paramStr = PvpLoanAppList._obj.getParamStr(['serno','cont_no']);
		if (paramStr != null) {
			var approve_status  = PvpLoanAppList._obj.getParamValue(['approve_status']);
			var serno  = PvpLoanAppList._obj.getParamValue(['serno']);
			var cont_no  = PvpLoanAppList._obj.getParamValue(['cont_no']);
			if(approve_status=='997'){
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr define error!"+e);
							return;
						}
						var Is_Replace = jsonstr.Is_Replace;
						if(Is_Replace=='0'){//内部转贴现
							var url = '<emp:url action="getReportShowPage.do"/>&reportId=printFktzs/PRITNT_NBZTXYWCZQRS.raq&serno='+serno;
							url = EMPTools.encodeURI(url);
							window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
						}else if(Is_Replace=='1'){//买断式
							var url = '<emp:url action="getReportShowPage.do"/>&reportId=printFktzs/PRITNT_ZTXMDYWCZQRS.raq&serno='+serno;
							url = EMPTools.encodeURI(url);
							window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
						}else{//回购式
							var url = '<emp:url action="getReportShowPage.do"/>&reportId=printFktzs/PRITNT_ZTXHGYWCZQRS.raq&serno='+serno;
							url = EMPTools.encodeURI(url);
							window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
						}
					}
				};
				var handleFailure = function(o){	
				};
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				}; 
				var url = '<emp:url action="searchIsReplace4CollectCont.do"/>?cont_no='+cont_no;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}else{
				alert("只有状态为'通过'的才能被【打印】！");
			}
		}else{
			alert('请先选择一条记录！');
		}
	};	

	function getOrgNo(data){
		PvpLoanApp.cus_id._setValue(data.bank_no._getValue());
    	PvpLoanApp.cus_id_displayname._setValue(data.bank_name._getValue());
    };		

	function returnPrdId(data){
		PvpLoanApp.prd_id._setValue(data.id);
		PvpLoanApp.prd_id_displayname._setValue(data.label); 
	};

	function doImageView(){
		var data = PvpLoanAppList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View25');	//业务资料查看
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = PvpLoanAppList._obj.getParamValue(['fount_serno']);	//业务编号
		data['cus_id'] = PvpLoanAppList._obj.getParamValue(['cus_id']);	//客户码
		data['prd_id'] = PvpLoanAppList._obj.getParamValue(['prd_id']);	//业务品种
		data['prd_stage'] = 'CZSQ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*--user code end--*/
	/*****2019-03-01 jiangcuihua 附件上传  start******/
	function doUpload(){
		var paramStr = PvpLoanAppList._obj.getParamValue(['serno']);
		if (paramStr!=null) {
			var url = '<emp:url action="getUploadInfoPage.do"/>?file_type=06&serno='+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	}
	/*****2019-03-01 jiangcuihua 附件上传  end******/
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PvpLoanAppGroup" title="输入查询条件" maxColumn="3">
		<emp:text id="PvpLoanApp.serno" label="出账流水号" />
		<emp:pop id="PvpLoanApp.cus_id_displayname" label="交易对手行名" url="getPrdBankInfoPopList.do" returnMethod="getOrgNo" required="false" buttonLabel="选择" />
		<emp:select id="PvpLoanApp.approve_status" label="申请状态" dictname="WF_APP_STATUS"/>	
		<emp:text id="PvpLoanApp.cont_no" label="合同编号" />		
		<emp:pop id="PvpLoanApp.prd_id_displayname" label="产品名称" url="showPrdTreeDetails.do?bizline=BL400" returnMethod="returnPrdId" />
	    <emp:text id="PvpLoanApp.prd_id" label="产品编号"  hidden="true" />
		<emp:text id="PvpLoanApp.cus_id" label="对手行行号" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewPvpLoanApp" label="查看" op="view"/>
		<emp:button id="print" label="打印" op="print"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="upload" label="附件"/>
	</div>

	<emp:table icollName="PvpLoanAppList" pageMode="true" url="pagePvpRpddscantHistoryQuery.do">
		<emp:text id="serno" label="出账流水号" />
		<emp:text id="fount_serno" label="业务编号" hidden="true"/>
		<emp:text id="cont_no" label="合同编号" /> 
		<emp:text id="cus_id" label="交易对手行号" hidden="true"/>
		<emp:text id="toorg_name" label="交易对手行名" />
		<emp:text id="prd_id" label="产品编号" hidden="true"/>  
		<emp:text id="prd_id_displayname" label="产品名称" />  
		<emp:text id="pvp_amt" label="出账金额" />   
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="in_acct_br_id_displayname" label="入账机构" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" />
		<emp:text id="input_date" label="出账申请日期" />
	</emp:table>
	
</body>
</html>
</emp:page>
    