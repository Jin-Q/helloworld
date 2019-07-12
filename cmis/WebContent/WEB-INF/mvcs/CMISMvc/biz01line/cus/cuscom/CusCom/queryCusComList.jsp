<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<script type="text/javascript" src="<emp:file fileName='scripts/jquery-1.3.2.js'/>"></script>

<script type="text/javascript">

	function doOnLoad() {
	    //showCertTyp(CusBase.cert_type, 'com');
	
		var optionJosn = "00,04,20";
		var options =CusBase.cus_status._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
			if(optionJosn.indexOf(options[i].value)<0){
				options.remove(i);
			}
		}
		//隐藏对个人户类型
    	var options = CusBase.cus_type._obj.element.options;
    	for( var i = options.length - 1; i >= 0; i--){
	   	    if(options[i].value=='Z'||options[i].value=='Z1'||options[i].value=='Z2'||options[i].value=='Z3'){
				options.remove(i);
			}
    	}
	};
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusBase._toForm(form);
		CusBaseList._obj.ajaxQuery(null,form);
	};

	function checkTmpCus(){
		 var cusStatus=CusBaseList._obj.getSelectedData()[0];
		 if (cusStatus != null && cusStatus!='' && (cusStatus.cus_status._getValue()=='00' || cusStatus.cus_status._getValue()=='04')) return true;
		 return false;
	}

	//2015-04-16 Edited by FCL 
	//记录集权限已经放开对作业岗待处理客户记录的过滤，故需新增对当前记录的编辑权限校验
	function checkRestrict(act){
		var paramCusId = CusBaseList._obj.getParamStr(['cus_id']);
		if(paramCusId!=null){
			var url = '<emp:url action="queryCusSubmitInfoRecord.do"/>?'+paramCusId+"&checkRestrict=Y";
			url = EMPTools.encodeURI(url);
			EMPTools.mask();
			var handleSuccess = function(o){
				EMPTools.unmask();
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("提交失败!"+e.message);
						return ;
					}
					var flag=jsonstr.flag;
					if(flag=="0"){
						alert("该客户处于其他作业人员的待办事项中，您无权修改!");
					}else{
						if(act=='XG'){
							notOfficialCus();
						}else if(act=='BL'){
							var flag = "edit";
							var oper = "infotree";
							var url = '<emp:url action="getCusComUpdatePage.do"/>?'+paramCusId+"&flag="+flag+"&oper="+oper+"&btnflag=rep";
							url = EMPTools.encodeURI(url);
							window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.6+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');	
						}
					}
				}	
			};
			var handleFailure = function(o){};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			}; 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		}else{
			alert("请选择一条记录");
		}
	}
	
	function doViewCusCom() {
		var data = CusBaseList._obj.getSelectedData();
		if (data != null && data !=0) {
			var flag = "query";
			var oper = "infotree";
			var paramStr = CusBaseList._obj.getParamStr(['cus_id']);
			if (paramStr != null) {
				var url = '<emp:url action="getCusComTree.do"/>?'+paramStr+"&flag="+flag+"&oper="+oper;
				url = EMPTools.encodeURI(url);debugger;
				window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no');
			}
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doGetAddCusComPage() {
		var url = '<emp:url action="newCusComAddPage.do"/>?isTrans=false';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};

	function doGetCusComTree() {
		var data = CusBaseList._obj.getSelectedData();
		if (data != null && data !=0) {
			var cusStatus = CusBaseList._obj.getSelectedData()[0].cus_status._getValue();
			if(cusStatus=='04'||cusStatus=='00'){//临时客户或者是正是客户（草稿）
				checkRestrict('XG');
			}else if(cusStatus=='20'){//正式客户
				//增加授权校验
				var data = CusBaseList._obj.getSelectedData();
				if (data == null) {
					alert('请先选择一条记录！');
					return ;
				}
				var flag = 'edit';
				var paramStrs = CusBaseList._obj.getParamStr(['cus_id']);
				//chenBQ 20180311 裕民银行渠道授权码校验
			    /* var url = '<emp:url action="getCusFixHistoryUpdatePage.do"/>?'+paramStrs;
			    url = EMPTools.encodeURI(url);
			    var returnValue = window.showModalDialog(url,'newwindow10','dialogWidth:850px;dialogHeight:450px;dialogLeft:200px;dialogTop:150px;center:yes;help:yes;resizable:yes;status:yes');
			    if("suc"==returnValue){
			    	var url = '<emp:url action="getCusComTree.do"/>?'+paramStrs+'&flag='+flag + "&btnflag=due";
					url = EMPTools.encodeURI(url);
					window.open(url,'newwindow11','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
			    } */
			    var url = '<emp:url action="getCusComTree.do"/>?'+paramStrs+'&flag='+flag + "&btnflag=due";
				url = EMPTools.encodeURI(url);
				window.open(url,'newwindow11','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		   
			}else{//正式客户（草稿）
				var flag = 'edit';
				var paramStrs = CusBaseList._obj.getParamStr(['cus_id']);
				var url = '<emp:url action="getCusComTree.do"/>?'+paramStrs+'&flag='+flag + "&tempCus=no";
				url = EMPTools.encodeURI(url);
				window.open(url,'newwindow11','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no');
		    }
		} else {
			alert('请先选择一条记录！');
		}
	};

	function notOfficialCus(){//临时客户
		var flag = "edit";
		var oper = "infotree";
		var paramStr = CusBaseList._obj.getParamStr(['cus_id','cus_name','cert_type','cert_code','main_br_id']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusComTree.do"/>?'+paramStr+"&flag="+flag+"&oper="+oper+"&btnflag=temp";
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow11','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no');
		}
	}
	/*--user code begin--*/
	//提交
	function doCheckRecord(){
		var paramCusId = CusBaseList._obj.getParamStr(['cus_id']);
	/*	var cusstatus = CusBaseList._obj.getSelectedData()[0].cus_status._getValue();
		if(cusstatus == '20'){
			alert("正式客户不能提交!");
			return ;
		}*/
		//modified by yangzy 2015/04/17 需求：XD150325024，集中作业扫描岗权限改造 start
		var currentUserId = '${context.currentUserId}';
		if(paramCusId!=null){
			var manager_id = CusBaseList._obj.getParamValue('cust_mgr');
			if(manager_id==null || manager_id=='' || currentUserId == manager_id){
				var url = '<emp:url action="queryCusSubmitInfoRecord.do"/>?'+paramCusId ;
				url = EMPTools.encodeURI(url);
				EMPTools.mask();
				var handleSuccess = function(o){
					EMPTools.unmask();
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("提交失败!");
							return;
						}
						var flag=jsonstr.flag;	
						if(flag=="2"){
							doSubmt(2);
						}else if(flag=="0"){
							alert("此客户已经提交过，待集中作业岗人员处理完！");
						}else if(flag=="1"){ //不是首次提交
							doSubmt(1);
						}
					}	
				};
				var handleFailure = function(o){};
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				}; 
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
			}else{
				alert('非当前客户主管客户经理，操作失败！');
			}
		}else {
			alert('请选择一条记录!');
		}
		//modified by yangzy 2015/04/17 需求：XD150325024，集中作业扫描岗权限改造 end
	}
	
	function doSubmt(v){
		var paramStr = CusBaseList._obj.getParamStr(['cus_id','cus_name']);
		if (paramStr!=null) {
			var url = "";
			if(v == 2){
				url = '<emp:url action="getCusSubmitInfoUpdatePage.do"/>?'+paramStr;
			}else{
				url = '<emp:url action="getCusSubmitInfoUpdatePage.do"/>?'+paramStr + "&twoSub=y";
			}
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height=400,width=800,top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		} else {
			alert('请选择一条记录!');
		}
	}
	
	function goback(){
		var gobackURL = '<emp:url action="queryCusComList.do"/>';
		gobackURL = EMPTools.encodeURI(gobackURL);
		window.location = gobackURL;
	};
	
	function doViewCusGrpRe(){
		var paramStr = CusBaseList._obj.getParamStr(['cus_id']);
		if (paramStr!=null) {
			var url = '<emp:url action="GetCusRelTreeOp.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height=600,width=800,top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	}
	function getOrgID(data){
		CusBase.main_br_id._setValue(data.organno._getValue());
		CusBase.main_br_id_displayname._setValue(data.organname._getValue());
	};

	//主管客户经理
	function setconId(data){
		CusBase.cust_mgr._setValue(data.actorno._getValue());
		CusBase.cust_mgr_displayname._setValue(data.actorname._getValue());
	}
	
	function onReturnComCllName(date){
		CusBase.com_cll_type._obj.element.value=date.One+date.id;
		CusBase.com_cll_name._obj.element.value=date.label;
	}

	//对公客户一键查询功能
	function doQueryByOneKey(){
		var paramStr = CusBaseList._obj.getParamStr(['cus_id']);
		if (paramStr!=null) {
			var url = '<emp:url action="queryCusComByOneKey.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow','height=700,width=1200,top=50,left=20,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！');
		}
	};
	/*****2019-02-22 jiangcuihua 附件上传  start******/
	function doUpload(){
		var paramStr = CusBaseList._obj.getParamValue(['cus_id']);
		if (paramStr!=null) {
			var url = '<emp:url action="getUploadInfoPage.do"/>?file_type=01&serno='+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	}
	/*****2019-02-22 jiangcuihua 附件上传  end******/
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	<form  method="POST" action="#" id="queryForm" >
		<emp:gridLayout id="CusBaseGroup" title="输入查询条件" maxColumn="2">
		    <emp:text id="CusBase.cus_id" label="客户码" />
		    <emp:text id="CusBase.cus_name" label="客户名称" />
			<emp:select id="CusBase.cus_type" label="客户类型" dictname="STD_ZB_CUS_TYPE" />
			<emp:text id="CusBase.cert_code" label="证件号码" />
			<emp:pop id="CusBase.cust_mgr_displayname" label="主管客户经理" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId"/>
			<emp:pop id="CusBase.main_br_id_displayname" label="主管机构" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID"/>
			<emp:select id="CusBase.cus_status" label="客户状态" dictname="STD_ZB_CUS_STATUS" />
			<emp:text id="CusBase.cust_mgr" label="主管客户经理编号" hidden="true"/>
			<emp:text id="CusBase.main_br_id" label="主管机构编号" hidden="true"/>
		</emp:gridLayout>
	</form>

	<script>
	function doUpdateCusBase(){
		var paramStr = CusBaseList._obj.getParamStr(['cus_id']);
		//var oper = "infotree";
		var url = '<emp:url action="updateCusCom.do"/>?'+paramStr;
		url = EMPTools.encodeURI(url);
		//window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no');
	    window.location = url;
	}

	function doReset(){
		page.dataGroups.CusBaseGroup.reset();
	};

	function doReplenish(){
		var data = CusBaseList._obj.getSelectedData();
		if (data != null && data !=0) {
			var flag = "edit";
			var oper = "infotree";
			var paramStr = CusBaseList._obj.getParamStr(['cus_id']);
			if (paramStr != null) {
				if(checkComCus()){
					var cert_type = CusBaseList._obj.getSelectedData()[0].cert_type._getValue();
					if(cert_type != 'a'&&cert_type != '20'){
						alert("证件类型只有为'组织机构代码或统一社会信用代码'时才能进行【转正】操作！");
					}else{
						checkRestrict('BL');//2015-04-16 Edited by FCL 
					}
				}else{
					alert('正式客户不需要进行补录！')
					return;
				}
			}
		} else {
			alert('请先选择一条记录！');
		}
	}
	
	function checkComCus(){
		var cusStatus=CusBaseList._obj.getSelectedData()[0];
		if (cusStatus != null && cusStatus!='' && (cusStatus.cus_status._getValue()=='00' || cusStatus.cus_status._getValue()=='04')) return true;
		return false;
	}
	
	//打回
	function doGetBack(){
		var sobj = CusBaseList._obj.getSelectedData();
		if(sobj != null && sobj != ""){
		/*	var cusstatus = sobj[0].cus_status._getValue();
			if( cusstatus == "20" ){
				alert('正式客户不能打回!');
				return ;
			}*/
			//提交操作
			var cusid = sobj[0].cus_id._getValue();
			var url = '<emp:url action="queryCusSubmitInfoRecord.do"/>?cus_id='+cusid + '&back=back';
			url = EMPTools.encodeURI(url);
			EMPTools.mask();
			var handleSuccess = function(o){
				EMPTools.unmask();
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("打回失败!");
						return;
					}
					var flag=jsonstr.flag;	
					if(flag=="0"){
						alert("没有该客户提交信息！");							
					}else if(flag=="1"){
						var paramStr = CusBaseList._obj.getParamStr(['cus_id','cus_name']);
						var url = '<emp:url action="getCusSubmitInfoUpdatePage.do"/>?'+paramStr + '&back=back';
						url = EMPTools.encodeURI(url);
						window.open(url,'newwindow','height=400,width=800,top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
					}else if(flag=="2"){
						alert("没有该客户提交信息！");
					}
				}
			};
			var handleFailure = function(o){};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			}; 
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback);
		}else{
			alert('请选择一条记录!');
		}
	}

	/*** 影像部分操作按钮begin ***/
	function doImageScan(){
		//modified by yangzy 2015/04/17 需求：XD150325024，集中作业扫描岗权限改造 start
		var currentUserId = '${context.currentUserId}';
		var rolesList = '${context.roleNoList}';
		var data = CusBaseList._obj.getSelectedData();
		if (data != null && data !=0) {
			var manager_id = CusBaseList._obj.getParamValue('cust_mgr');
			if(rolesList.indexOf("3002")>=0 ||manager_id==null || manager_id=='' || currentUserId == manager_id){
				ImageAction('Scan22');	//2.2.	客户资料扫描（抵质押预扫）接口
			}else{
				alert('非当前客户主管客户经理，操作失败！');
			}		
		} else {
			alert('请先选择一条记录！');
		}	
		//modified by yangzy 2015/04/17 需求：XD150325024，集中作业扫描岗权限改造 end	
	};
	function doImageView(){
		var data = CusBaseList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View23');	//2.3.	客户资料查看（客户全视图）接口
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function doImageView1(){
		var data = CusBaseList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View24');	//2.3.	客户资料查看接口
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function doImageCheck(){
		var data = CusBaseList._obj.getSelectedData();
		if (data != null && data !=0) {
			if( confirm("影像信息将直接归档，请确认!") ){
				ImageAction('Check3131');	//3.1.3.1 客户资料归档接口
			}
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function doImagePrint(){
		var data = CusBaseList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('Print');	//条码打印接口			
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = CusBaseList._obj.getParamValue(['cus_id']);	//客户资料的业务编号就填cus_id
		data['cus_id'] = CusBaseList._obj.getParamValue(['cus_id']);	//客户编号
		data['prd_id'] = 'BASIC';	//业务品种
		data['prd_stage'] = '' ;	//业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*** 影像部分操作按钮end ***/
	
	//强制变更正式客户
	function doCoerceChangeCus(){
		var sobj = CusBaseList._obj.getSelectedData();
		if(sobj != null && sobj != ""){
			var cus_status = sobj[0].cus_status._getValue();
			if(cus_status != "20"){
				var cus_id = sobj[0].cus_id._getValue();
				if(confirm("是否确认要强制变更正式客户？")){
					var handleSuccess = function(o){
						if(o.responseText !== undefined) {
							try {
								var jsonstr = eval("("+o.responseText+")");
							} catch(e) {
								alert("Parse jsonstr1 define error!" + e.message);
								return;
							}
							var flag = jsonstr.flag;
							var msg = jsonstr.msg;
							if(flag == "success"){
								alert(msg);
								window.location.reload();
							}else {
								alert(msg); 
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
					var url = '<emp:url action="coerceChangeCus.do"/>?cus_id='+cus_id;	
					url = EMPTools.encodeURI(url);
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
				}
			}else{
				alert('该客户已是正式客户！');
			}
		}else{
			alert('请选择一条记录!');
		}
	};

	/**modified by lisj 2015-7-24  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更 begin**/
	/**add by lisj  2015-3-10  需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能 begin**/
	function doPrintln(){	
		var paramStr = CusBaseList._obj.getParamStr(['cus_id']);
		if (paramStr != null) {
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("异步调用通讯发生异常！");
						return;
					}
					var flag=jsonstr.flag;
					if(flag =="success"){
						var url = '<emp:url action="getPrintPage.do"/>?'+paramStr+"&print_type=03";
						url = EMPTools.encodeURI(url);
						var param = 'height=356, width=365, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
						window.open(url,'newWindow4CC',param);
					}else{
						alert("清空原表报数据失败！");
					}
				}	
			};
			var handleFailure = function(o){ 
				alert("清空原表报数据发生异常，请联系管理员");
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var resetUrl ='<emp:url action="resetRcInfo.do"/>?'+paramStr+"&print_type=03";
			resetUrl = EMPTools.encodeURI(resetUrl);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',resetUrl, callback);
		} else {
			alert('请先选择一条记录！');
		}
	};
	/**add by lisj  2015-3-10  需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能 end**/
	/**modified by lisj 2015-7-24  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更 end**/
	
	</script>
	<jsp:include page="/queryInclude.jsp" flush="true" />

	<div align="left">
		<emp:button id="getAddCusComPage" label="开户" op="add"/>
		<emp:button id="viewCusCom" label="查看" op="view"/>
		<emp:button id="getCusComTree" label="修改" op="update" />
		<emp:button id="viewCusGrpRe" label="智能关联搜索" op="serch"/>
		<emp:button id="replenish" label="补录完成" op="replenish"/>
		<emp:button id="checkRecord" label="提交" op="subm"/>
		<emp:button id="getBack" label="打回" op="getBack"/>
		<emp:button id="ImageScan" label="影像扫描" op="ImageScan" />
		<emp:button id="ImageView" label="影像全视图" op="ImageView" />
		<emp:button id="ImageView1" label="基础资料查看" op="ImageView1" />
		<emp:button id="ImageCheck" label="影像核对" op="ImageCheck" />
		<emp:button id="ImagePrint" label="条码打印" op="ImagePrint" />
		<emp:button id="coerceChangeCus" label="强制变更正式客户" op="coerceChangeCus" mousedownCss="button100" mouseoutCss="button100" mouseoverCss="button100" mouseupCss="button100"/>
		<!-- add by lisj 2014年12月11日 需求编号：【XD141107075】 一键查询改造  -->
		<emp:button id="queryByOneKey" label="一键查询" op="queryByOneKey"/>
		<!--<emp:button id="updateCusCom" label="变更客户名称" op="change"/>-->
		<emp:button id="println" label="封面打印" op="println"/>
		<emp:button id="upload" label="附件"/>
	</div>
	<emp:table icollName="CusBaseList" pageMode="true" url="pageCusComQuery.do">
	    <emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_name" label="客户名称" />
		<emp:text id="cert_type" label="开户证件类型" dictname="STD_ZB_CERT_TYP"/>
		<emp:text id="cert_code" label="开户证件号码" />
		<emp:text id="cus_type" label="客户类型" dictname="STD_ZB_CUS_TYPE" />
		<emp:text id="cust_mgr_displayname" label="主管客户经理" />
        <emp:text id="main_br_id_displayname" label="主管机构" />
		<emp:text id="cust_mgr" label="主管客户经理" hidden="true"/>
        <emp:text id="main_br_id" label="主管机构" hidden="true"/>
		<emp:text id="cus_status" label="客户状态" dictname="STD_ZB_CUS_STATUS" hidden="false" />
	</emp:table>

</body>
</html>
</emp:page>