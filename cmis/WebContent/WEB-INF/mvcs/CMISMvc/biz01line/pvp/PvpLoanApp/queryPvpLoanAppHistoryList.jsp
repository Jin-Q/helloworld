<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String biz_type = "";
	if(context.containsKey("biz_type")){
		biz_type = (String)context.getDataValue("biz_type");
		
	} 
%>  
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
	
	function doReset(){
		  page.dataGroups.PvpLoanAppGroup.reset(); 
	};
	
	function doViewPvpLoanApp() {
		var paramStr = PvpLoanAppList._obj.getParamStr(['serno','cont_no']);

		if (paramStr != null) {
			var url = '<emp:url action="getPvpLoanAppViewPage.do"/>?'+paramStr+"&flag=pvpLoanHistory&biz_type="+'<%=biz_type%>';
			url = EMPTools.encodeURI(url);
			//window.location = url;
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'newWindow',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function returnCus(data){
		PvpLoanApp.cus_id._setValue(data.cus_id._getValue());
		PvpLoanApp.cus_name._setValue(data.cus_name._getValue());
	};

	function returnPrdId(data){
		PvpLoanApp.prd_id._setValue(data.id);
		PvpLoanApp.prd_id_displayname._setValue(data.label); 
	};

	/*--user code begin--*/
	function doPrint(){
		var paramStr = PvpLoanAppList._obj.getParamStr(['serno','cont_no']);
		if (paramStr != null) {
			var approve_status  = PvpLoanAppList._obj.getParamValue(['approve_status']);
			var serno  = PvpLoanAppList._obj.getParamValue(['serno']);
			if(approve_status=='997'){
				var prd_id = PvpLoanAppList._obj.getParamValue(['prd_id']);//产品编号
				if(prd_id=='500024'||prd_id=='500025'||prd_id=='500026'){//出口商业发票融资\出口托收\信用证项下出口押汇
					searchIsReplace4Gyxd();
				}else if(prd_id=='500029'){//福费廷
					searchIsReplace4FFT();
				}else if(prd_id=='500029'){//延期信用证项下应收款买入
					searchIsReplace4YSKMR();
				}else if(prd_id=='500027'){//远期信用证项下汇票贴现
					searchIsReplace4HPTX();
				}else if(prd_id=='500020'){//同业代付
					searchIsReplace4TYDF();
				}else if(prd_id=='500021'){//信托收据贷款
					searchIsReplace4XTSJ();
				}else if(prd_id=='500032'){//提货担保
					searchIsReplace4THDB();
				}else if(prd_id=='500022'||prd_id=='500031'||prd_id=='800020'||prd_id=='500023'){//打包贷款\短期信保融资\国际保理\出口订单融资
					var url = '<emp:url action="getReportShowPage.do"/>&reportId=printFktzs/PRINT_FKTZS_GYXD_FZH.raq&serno='+serno;
					url = EMPTools.encodeURI(url);
					window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
				}else if(prd_id=='400021'){//境内保函
					var url = '<emp:url action="getReportShowPage.do"/>&reportId=printFktzs/PRINT_YHBHQRS.raq&serno='+serno;
					url = EMPTools.encodeURI(url);
					window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
				}else if(prd_id=='700020' || prd_id=='700021'){//国内信用证(开立信用证)
					var url = '<emp:url action="getReportShowPage.do"/>&reportId=printFktzs/PRINT_FKTZS_GYXD_KLXYZ.raq&serno='+serno;
					url = EMPTools.encodeURI(url);
					window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
				}else if(prd_id=='400020'){//外汇保函(开立保函)
					var url = '<emp:url action="getReportShowPage.do"/>&reportId=printFktzs/PRINT_FKTZS_GYXD_KLBH.raq&serno='+serno;
					url = EMPTools.encodeURI(url);
					window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
				}else if(prd_id=='300020' || prd_id=='300021'){//贴现
					var url = '<emp:url action="getReportShowPage.do"/>&reportId=printFktzs/PRINT_TXXYQRS.raq&serno='+serno;
					url = EMPTools.encodeURI(url);
					window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
				}else if(prd_id=='200024'){//银行承兑汇票
					var url = '<emp:url action="getReportShowPage.do"/>&reportId=printFktzs/PRINT_YHCDHPQRS.raq&serno='+serno;
					url = EMPTools.encodeURI(url);
					window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
				}else if(prd_id=='400024'){//贷款意向不需要打印
					alert("无需打印通知单！");
				/*add by wangj 需求编号：XD150123005小微自助循环贷款改造   begin*/
				}else if(prd_id == '100088'){
					var cont_no  = PvpLoanAppList._obj.getParamValue(['cont_no']);
       				var url = '<emp:url action="getReportShowPage.do"/>&reportId=ctrLoanCont/xdywsdtzs.raq&cont_no='+cont_no;
    				url = EMPTools.encodeURI(url);
    				window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
             	}else{//普通贷款
             	/*add by wangj 需求编号：XD150123005小微自助循环贷款改造   end*/
					var url = '<emp:url action="getReportShowPage.do"/>&reportId=printFktzs/PRINT_FKTZS.raq&serno='+serno;
					url = EMPTools.encodeURI(url);
					window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
				}
			}else{
				alert("只有状态为'通过'的才能被【打印】！");
			}
		}else{
			alert('请先选择一条记录！');
		}
	};

	//贸易融资的置换
	function searchIsReplace4Gyxd(){
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr define error!"+e);
					return;
				}
				var Is_Replace = jsonstr.Is_Replace;
				var serno = PvpLoanAppList._obj.getParamValue(['serno']);
				if(Is_Replace=='1'){//置换
					var url = '<emp:url action="getReportShowPage.do"/>&reportId=printFktzs/PRINT_FKTZS_GYXD.raq&serno='+serno;
					url = EMPTools.encodeURI(url);
					window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
				}else{//非置换
					var url = '<emp:url action="getReportShowPage.do"/>&reportId=printFktzs/PRINT_FKTZS_GYXD_FZH.raq&serno='+serno;
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
		var cont_no = PvpLoanAppList._obj.getParamValue(['cont_no']);
		var url = '<emp:url action="searchIsReplace4CollectCont.do"/>?cont_no='+cont_no;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}

	//同业代付
	function searchIsReplace4TYDF(){
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr define error!"+e);
					return;
				}
				var Is_Replace = jsonstr.Is_Replace;
				var serno = PvpLoanAppList._obj.getParamValue(['serno']);
				if(Is_Replace=='1'){//置换
					var url = '<emp:url action="getReportShowPage.do"/>&reportId=printFktzs/PRINT_FKTZS_GYXD_TYDF.raq&serno='+serno;
					url = EMPTools.encodeURI(url);
					window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
				}else{//非置换
					var url = '<emp:url action="getReportShowPage.do"/>&reportId=printFktzs/PRINT_FKTZS_GYXD_TYDF_FZH.raq&serno='+serno;
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
		var cont_no = PvpLoanAppList._obj.getParamValue(['cont_no']);
		var url = '<emp:url action="searchIsReplace4CollectCont.do"/>?cont_no='+cont_no;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}

	//信托收据
	function searchIsReplace4XTSJ(){
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr define error!"+e);
					return;
				}
				var Is_Replace = jsonstr.Is_Replace;
				var serno = PvpLoanAppList._obj.getParamValue(['serno']);
				if(Is_Replace=='1'){//置换
					var url = '<emp:url action="getReportShowPage.do"/>&reportId=printFktzs/PRINT_FKTZS_GYXD_XTSJ.raq&serno='+serno;
					url = EMPTools.encodeURI(url);
					window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
				}else{//非置换
					var url = '<emp:url action="getReportShowPage.do"/>&reportId=printFktzs/PRINT_FKTZS_GYXD_XTSJ_FZH.raq&serno='+serno;
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
		var cont_no = PvpLoanAppList._obj.getParamValue(['cont_no']);
		var url = '<emp:url action="searchIsReplace4CollectCont.do"/>?cont_no='+cont_no;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}

	//福费廷置换
	function searchIsReplace4FFT(){
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr define error!"+e);
					return;
				}
				var Is_Replace = jsonstr.Is_Replace;
				var serno = PvpLoanAppList._obj.getParamValue(['serno']);
				if(Is_Replace=='1'){//置换
					var url = '<emp:url action="getReportShowPage.do"/>&reportId=printFktzs/PRINT_FKTZS_GYXD_FFT.raq&serno='+serno;
					url = EMPTools.encodeURI(url);
					window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
				}else{//非置换
					var url = '<emp:url action="getReportShowPage.do"/>&reportId=printFktzs/PRINT_FKTZS_GYXD_FFT_FZH.raq&serno='+serno;
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
		var cont_no = PvpLoanAppList._obj.getParamValue(['cont_no']);
		var url = '<emp:url action="searchIsReplace4CollectCont.do"/>?cont_no='+cont_no;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}

	//提货担保置换
	function searchIsReplace4THDB(){
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr define error!"+e);
					return;
				}
				var Is_Replace = jsonstr.Is_Replace;
				var serno = PvpLoanAppList._obj.getParamValue(['serno']);
				if(Is_Replace=='1'){//置换
					var url = '<emp:url action="getReportShowPage.do"/>&reportId=printFktzs/PRINT_FKTZS_GYXD_THDB.raq&serno='+serno;
					url = EMPTools.encodeURI(url);
					window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
				}else{//非置换
					var url = '<emp:url action="getReportShowPage.do"/>&reportId=printFktzs/PRINT_FKTZS_GYXD_THDB_FZH.raq&serno='+serno;
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
		var cont_no = PvpLoanAppList._obj.getParamValue(['cont_no']);
		var url = '<emp:url action="searchIsReplace4CollectCont.do"/>?cont_no='+cont_no;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}

	//延期信用证项下应收款买入
	function searchIsReplace4YSKMR(){
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr define error!"+e);
					return;
				}
				var Is_Replace = jsonstr.Is_Replace;
				var serno = PvpLoanAppList._obj.getParamValue(['serno']);
				if(Is_Replace=='1'){//置换
					var url = '<emp:url action="getReportShowPage.do"/>&reportId=printFktzs/PRINT_FKTZS_GYXD_YSKMR.raq&serno='+serno;
					url = EMPTools.encodeURI(url);
					window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
				}else{//非置换
					var url = '<emp:url action="getReportShowPage.do"/>&reportId=printFktzs/PRINT_FKTZS_GYXD_YSKMR_FZH.raq&serno='+serno;
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
		var cont_no = PvpLoanAppList._obj.getParamValue(['cont_no']);
		var url = '<emp:url action="searchIsReplace4CollectCont.do"/>?cont_no='+cont_no;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}

	//远期信用证项下汇票贴现
	function searchIsReplace4HPTX(){
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr define error!"+e);
					return;
				}
				var Is_Replace = jsonstr.Is_Replace;
				var serno = PvpLoanAppList._obj.getParamValue(['serno']);
				if(Is_Replace=='1'){//置换
					var url = '<emp:url action="getReportShowPage.do"/>&reportId=printFktzs/PRINT_FKTZS_GYXD_FFT_HPTX.raq&serno='+serno;
					url = EMPTools.encodeURI(url);
					window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
				}else{//非置换
					var url = '<emp:url action="getReportShowPage.do"/>&reportId=printFktzs/PRINT_FKTZS_GYXD_FFT_HPTX_FZH.raq&serno='+serno;
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
		var cont_no = PvpLoanAppList._obj.getParamValue(['cont_no']);
		var url = '<emp:url action="searchIsReplace4CollectCont.do"/>?cont_no='+cont_no;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
	}

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
		<emp:pop id="PvpLoanApp.cus_name" label="客户名称" url="queryAllCusPop.do?returnMethod=returnCus" buttonLabel="选择" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
	    <emp:select id="PvpLoanApp.approve_status" label="申请状态" dictname="WF_APP_STATUS" />
	    <emp:text id="PvpLoanApp.cont_no" label="合同编号" />	    
	    <emp:pop id="PvpLoanApp.prd_id_displayname" label="产品名称" url="showPrdTreeDetails.do?bizline=BL100,BL200,BL300,BL400,BL500" returnMethod="returnPrdId" />
	    <emp:text id="PvpLoanApp.prd_id" label="产品编号"  hidden="true" />
	    <emp:text id="PvpLoanApp.cus_id" label="客户码"  hidden="true" />
	</emp:gridLayout> 
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewPvpLoanApp" label="查看" op="view"/>
		<emp:button id="print" label="打印" op="print"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="upload" label="附件"/>
	</div>

	<emp:table icollName="PvpLoanAppList" pageMode="true" url="pagePvpLoanAppHistoryQuery.do" reqParams="biz_type=${context.biz_type}&menuId=${context.menuId}">
		<emp:text id="serno" label="出账流水号" />
		<emp:text id="fount_serno" label="原业务编号" hidden="true" />
		<emp:text id="cont_no" label="合同编号" />		
		<emp:text id="cus_id" label="客户码" hidden="true"/>
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="prd_id_displayname" label="产品名称" /> 
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE"/>
		<emp:text id="pvp_amt" label="出账金额" dataType="Currency"/>
		<emp:text id="manager_br_id_displayname" label="管理机构" />	
		<emp:text id="in_acct_br_id_displayname" label="入账机构" />	
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="approve_status" label="申请状态" dictname="WF_APP_STATUS" />
		<emp:text id="prd_id" label="产品编号" hidden="true"/>
		<emp:text id="input_date" label="出账申请日期" /> 
	</emp:table>
	
</body>
</html>
</emp:page>
    