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
		CtrLoanCont._toForm(form);
		CtrLoanContList._obj.ajaxQuery(null,form);
	};
	
	
	function doViewCtrLoanCont() {
		var paramStr = CtrLoanContList._obj.getParamStr(['cont_no','iqpFlowHis']);
		var prd_id = CtrLoanContList._obj.getParamValue(['prd_id']);
		if (paramStr != null) {
		    /** modified by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 begin **/
			/**modified by lisj  2014-12-29 需求编号：【XD141204082】关于信托贷款业务需求调整 begin**/
			var url;
			if(prd_id==300021||prd_id==300020){
				url = '<emp:url action="getCtrLoanContForDiscViewPage.do"/>?op=view&viewtype=out&cont=cont&'+paramStr+"&flag=ctrLoanContHistory&biz_type="+'<%=biz_type %>';
			}else if('${context.flg}' =="trust"){
				url = '<emp:url action="getCtrLoanContViewPage.do"/>?op=view&viewtype=out&cont=cont&'+paramStr+"&flag=ctrLoanContHistory&biz_type="+'<%=biz_type %>'+"&flg=trust";
			}else{
				url = '<emp:url action="getCtrLoanContViewPage.do"/>?op=view&viewtype=out&cont=cont&'+paramStr+"&flag=ctrLoanContHistory&biz_type="+'<%=biz_type %>';
			}
			url = EMPTools.encodeURI(url);  
			//window.location = url;
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow_CtrLoanContHis',param); 
			/**modified by lisj  2014-12-29 需求编号：【XD141204082】关于信托贷款业务需求调整 end**/
			/** modified by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 end **/
		} else {
			alert('请先选择一条记录！');
		} 
	};
	
	function doReset(){
		page.dataGroups.CtrLoanContGroup.reset();
	};

	function returnCus(data){
		CtrLoanCont.cus_id._setValue(data.cus_id._getValue());
		CtrLoanCont.cus_name._setValue(data.cus_name._getValue());
	};
	function returnPrdId(data){
		CtrLoanCont.prd_id._setValue(data.id);
		CtrLoanCont.prd_id_displayname._setValue(data.label); 
	};
	//打印功能
	function doPrintCtrLoanCont(){
		var paramStr = CtrLoanContList._obj.getParamStr(['cont_no','iqpFlowHis']);
        if (paramStr != null) {
        	var cont_status = CtrLoanContList._obj.getParamValue(['cont_status']);
            
        	if(cont_status == '200'){
        		checkCusBelong();
        	}else{
                alert("只有合同状态为'生效'的合同才能进行【打印】操作！");
    		}
        }else{
        	alert('请先选择一条记录！');
        }
	};

	function checkCusBelong(){//校验客户条线
		var paramStr = CtrLoanContList._obj.getParamStr(['cus_id']);
		var cont_no = CtrLoanContList._obj.getParamValue(['cont_no']);
		var prd_id = CtrLoanContList._obj.getParamValue(['prd_id']);
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
					if(prd_id == '100028' || prd_id == '100033'){//个人住房及个人商用房
						var url = '<emp:url action="getReportShowPage.do"/>&reportId=GuarCont/grgfjkdbht.raq&cont_no='+cont_no;
	    				url = EMPTools.encodeURI(url);
	    				window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
					}else{
						var url = '<emp:url action="getReportShowPage.do"/>&reportId=ctrLoanCont/gesxjkht.raq&cont_no='+cont_no;
	    				url = EMPTools.encodeURI(url);
	    				window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
					}
				}else{
					if(prd_id == '100058'){//一般固定资产贷款
                    	var url = '<emp:url action="getReportShowPage.do"/>&reportId=ctrLoanCont/gdzcjkht.raq&cont_no='+cont_no;
        				url = EMPTools.encodeURI(url);
        				window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
        			}else if(prd_id == '100046'){//流动资金贷款
        				var url = '<emp:url action="getReportShowPage.do"/>&reportId=ctrLoanCont/ldzjjkht.raq&cont_no='+cont_no;
        				url = EMPTools.encodeURI(url);
        				window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
        			}else if(prd_id == '200024'){//银行承兑汇票
        				var url = '<emp:url action="getReportShowPage.do"/>&reportId=ctrLoanCont/yhcdxy.raq&cont_no='+cont_no;
        				url = EMPTools.encodeURI(url);
        				window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
        			/*modified by wangj 需求编号:XD141222087,法人账户透支需求变更 begin*/
        			}else if(prd_id == '100051'){
           				var url = '<emp:url action="getReportShowPage.do"/>&reportId=ctrLoanCont/xdywsdtzs.raq&cont_no='+cont_no;
        				url = EMPTools.encodeURI(url);
        				window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
             		}else{
             		/*modified by wangj 需求编号:XD141222087,法人账户透支需求变更 end*/
						alert("该业务暂不提供打印功能！");
            		}
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
		var url = '<emp:url action="checkCusBelong4CusId.do"/>?'+paramStr;	
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
	}
	//撤销
    function doDeleteCtrLoanCont(){
    	/**added by wangj  需求编号:XD141222087,法人账户透支需求变更  begin**/
    	var prd_id = CtrLoanContList._obj.getSelectedData()[0].prd_id._getValue();
    	if(prd_id=="100051"){
			alert("该合同是法人账户透支，不能做合同撤销！");
			return ;
		}
    	/**added by wangj  需求编号:XD141222087,法人账户透支需求变更  end**/
    	var paramStr = CtrLoanContList._obj.getParamStr(['cont_no']);
    	if (paramStr != null) {
    		var approve_status = CtrLoanContList._obj.getSelectedData()[0].cont_status._getValue();
    		//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start
    		var currentUserId = '${context.currentUserId}';
    		var manager_id = CtrLoanContList._obj.getParamValue('manager_id');
    		if(manager_id!=null && manager_id!='' && currentUserId != manager_id){
    			alert('非当前客户主管客户经理，操作失败！');
    			return;
    		}
    		//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 end	
    		if(approve_status == "200"){
    		if(confirm("是否确认要撤销 ？")){
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
    						alert("撤销成功!");
    						var url = '<emp:url action="queryCtrLoanContHistoryList.do"/>?biz_type='+'<%=biz_type%>';  
    						url = EMPTools.encodeURI(url);
    						window.location=url;
    					}else if(flag == "error"){
                            alert(msg);
        				}else{
    						alert("发生异常!"); 
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
    			var url = '<emp:url action="deleteCtrLoanContRecord.do"/>?'+paramStr;	
    			url = EMPTools.encodeURI(url);
    			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
    		}     
    		}else{
    			alert("只有状态为【生效】的合同才可以撤销！");
    		}
    	} else {
    		alert('请先选择一条记录！');
    	}
    };

    
	//注销
    function doDestroyCtrLoanCont(){
    	/**added by wangj  需求编号:XD141222087,法人账户透支需求变更  begin**/
    	var prd_id = CtrLoanContList._obj.getSelectedData()[0].prd_id._getValue();
    	if(prd_id=="100051"){
			alert("该合同是法人账户透支，不能做合同注销！");
			return ;
		}
		/**add by wangj 需求编号：【XD150123005】小微自助循环贷款改造 begin*/
    	if(prd_id=="100088"){
			alert("该合同是小微自助循环贷，不能做合同注销！");
			return ;
		}
		/**add by wangj 需求编号：【XD150123005】小微自助循环贷款改造 end*/
    	/**added by wangj  需求编号:XD141222087,法人账户透支需求变更  end**/
    	var paramStr = CtrLoanContList._obj.getParamStr(['cont_no']);
    	if (paramStr != null) {
    		var approve_status = CtrLoanContList._obj.getSelectedData()[0].cont_status._getValue();
    		//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start
    		var currentUserId = '${context.currentUserId}';
    		var manager_id = CtrLoanContList._obj.getParamValue('manager_id');
    		if(manager_id!=null && manager_id!='' && currentUserId != manager_id){
    			alert('非当前客户主管客户经理，操作失败！');
    			return;
    		}
    		//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 end	
    		if(approve_status == "200"){
    		if(confirm("是否确认要注销 ？")){
    			var handleSuccess = function(o){
    				if(o.responseText !== undefined) {
    					try {
    						var jsonstr = eval("("+o.responseText+")");
    					} catch(e) {
    						alert("Parse jsonstr1 define error!" + e.message);
    						return;
    					}
    					var flag = jsonstr.flag;
    					var billNo = jsonstr.billNo;
    					if(flag == "success"){
    						alert("合同注销成功!");
    						window.location.reload();
    					}else if(flag == "stopSuccess"){
    						alert("合同中止成功!");
    						window.location.reload();
    					}else if(flag == "accStatusError"){
                            alert("此合同下有业务未结清,未结清业务借据编号为："+billNo);
        				}else if(flag == "Pvperror"){
    						alert("存在未出账记录,不能注销!"); 
    					}else if(flag == "pvpAuthorizeStatusError"){
    						alert("此合同下有未发送授权,未发送授权业务借据编号为："+billNo);
        				}else{
                            alert("注销失败!");
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
    			var url = '<emp:url action="destroyCtrLoanCont.do"/>?'+paramStr;	
    			url = EMPTools.encodeURI(url);
    			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
    		}
    		}else{
    			alert("只有状态为【生效】的合同才可以注销！");
    		}
    	} else {
    		alert('请先选择一条记录！');
    	}
    };
	
	/*--user code begin--*/
	function doImageScan(){
		var data = CtrLoanContList._obj.getSelectedData();
		if (data != null && data !=0) {
			//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start
    		var currentUserId = '${context.currentUserId}';
    		var rolesList = '${context.roleNoList}';
    		var manager_id = CtrLoanContList._obj.getParamValue('manager_id');
    		if(rolesList.indexOf("3002")<0 && manager_id!=null && manager_id!='' && currentUserId != manager_id){
    			alert('非当前客户主管客户经理，操作失败！');
    			return;
    		}
    		//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 end	
			ImageAction('Scan24');	//业务资料扫描
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doImageView(){
		var data = CtrLoanContList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View25');	//业务资料查看
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doImageCheck(){
		var data = CtrLoanContList._obj.getSelectedData();
		if (data != null && data !=0) {
			if( confirm("影像信息将直接归档，请确认!") ){
				ImageAction('Check3132');	//业务资料核对
			}
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = CtrLoanContList._obj.getParamValue(['serno']);	//业务编号
		data['cus_id'] = CtrLoanContList._obj.getParamValue(['cus_id']);	//客户码
		data['prd_id'] = CtrLoanContList._obj.getParamValue(['prd_id']);	//业务品种
		data['prd_stage'] = 'YWSQ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};



	/* added by zhaoxp 2014/12/11 XD140812040, 银行承兑税票改造  start */
	function doActpMaintain(){
		var paramStr = CtrLoanContList._obj.getParamStr(['cont_no','iqpFlowHis']);
		var prd_id = CtrLoanContList._obj.getParamValue(['prd_id']);
		var cont_status = CtrLoanContList._obj.getParamValue(['cont_status']);
		if (paramStr != null) {
			//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start
    		//var currentUserId = '${context.currentUserId}';
    		//var manager_id = CtrLoanContList._obj.getParamValue('manager_id');
    		//if(manager_id!=null && manager_id!='' && currentUserId != manager_id){
    		//	alert('非当前客户主管客户经理，操作失败！');
    		//	return;
    		//}
    		//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 end	
		    /** modified by zhaoxp 20141125  begin **/
			var url;
			if(prd_id=="200024"){
				url = '<emp:url action="getCtrLoanContViewPageMaintain.do"/>?op=view&viewtype=out&cont=cont&'+paramStr+"&flag=ctrLoanContHistory&biz_type="+'<%=biz_type %>';
				url = EMPTools.encodeURI(url);  
				var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
				window.open(url,'newWindow_CtrLoanContHis',param);
			}else{
				alert('请选择银行承兑汇票进行维护！');
			}
			 
			/** modified by zhaoxp 20141125 end **/
		} else {
			alert('请先选择一条记录！');
		} 
		
	}
	/* added by zhaoxp 2014/12/11 XD140812040, 银行承兑税票改造  end */

	/**modified by lisj 2015-7-14  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更 begin**/
	function doPrintln(){	
		var paramStr = CtrLoanContList._obj.getParamStr(['cont_no']);
		var prd_id = CtrLoanContList._obj.getParamValue(['prd_id']);
		var serno =  CtrLoanContList._obj.getParamValue(['serno']);
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
						var url = '<emp:url action="getPrintPage.do"/>?'+paramStr+"&print_type=05|07|13&prd_id="+prd_id+"&serno="+serno;
						url = EMPTools.encodeURI(url);
						var param = 'height=500, width=1024, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
						window.open(url,'newWindow4CLC',param);
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
			var resetUrl ='<emp:url action="resetRcInfo.do"/>?'+paramStr+"&print_type=02|05|07|13";
			resetUrl = EMPTools.encodeURI(resetUrl);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',resetUrl, callback);
		} else {
			alert('请先选择一条记录！');
		}
	};
	/**modified by lisj 2015-7-14  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更 end**/
	/**added by wangj  需求编号:XD141222087,法人账户透支需求变更  begin**/
	function doStopCtrLoanCont(){
		var prd_id = CtrLoanContList._obj.getSelectedData()[0].prd_id._getValue();
    	/**add by wangj 需求编号：【XD150123005】小微自助循环贷款改造 begin*/
    	if(!(prd_id=="100051"||prd_id=="100088")){
			alert("该合同不是法人账户透支或小微自助循环贷款，不能做合同终止！");
			return ;
		}
		/**add by wangj 需求编号：【XD150123005】小微自助循环贷款改造 end*/
    	var paramStr = CtrLoanContList._obj.getParamStr(['cont_no','cus_id','prd_id']);

        if (paramStr != null) {
    		var approve_status = CtrLoanContList._obj.getSelectedData()[0].cont_status._getValue();
    		var currentUserId = '${context.currentUserId}';
    		var manager_id = CtrLoanContList._obj.getParamValue('manager_id');
    		
    		if(manager_id!=null && manager_id!='' && currentUserId != manager_id){
    			alert('非当前客户主管客户经理，操作失败！');
    			return;
    		}
    	
    		if(approve_status == "200"){
    			if(confirm("是否确认要终止合同 ？")){
    				var handleSuccess = function(o){
        				if(o.responseText !== undefined) {
        					try {
        						var jsonstr = eval("("+o.responseText+")");
        					} catch(e) {
        						alert("异步调用通讯发生异常！");
        						return;
        					}
        					var flag = jsonstr.flag;
        					var msg = jsonstr.msg;
        					if(flag == "success"){
        						alert(msg);
        						window.location.reload();
        					}else{
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
        			var url = '<emp:url action="stopCtrLoanCont.do"/>?'+paramStr;	
        			url = EMPTools.encodeURI(url);
        			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)	
    			}
    		}else{
				alert("只有状态为【生效】的合同才可以终止！");
			}
        } else {
    		alert('请先选择一条记录！');
    	}
    };
    /**added by wangj  需求编号:XD141222087,法人账户透支需求变更  end**/
    
    /*****2019-03-01 jiangcuihua 附件上传  start******/
	function doUpload(){
		var paramStr = CtrLoanContList._obj.getParamValue(['serno']);
		if (paramStr!=null) {
			var url = '<emp:url action="getUploadInfoPage.do"/>?file_type=05&serno='+paramStr;
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

	<emp:gridLayout id="CtrLoanContGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CtrLoanCont.cont_no" label="合同编号" />
			<emp:pop id="CtrLoanCont.cus_name" label="客户名称"  url="queryAllCusPop.do?returnMethod=returnCus" />
			<emp:select id="CtrLoanCont.cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE"/>
			<emp:pop id="CtrLoanCont.prd_id_displayname" label="产品名称" url="showPrdTreeDetails.do?bizline=BL100,BL200,BL300,BL400,BL500" returnMethod="returnPrdId" />
	        <emp:text id="CtrLoanCont.prd_id" label="产品编号"  hidden="true" />
			<emp:select id="CtrLoanCont.biz_type" label="业务模式" dictname="STD_BIZ_TYPE" defvalue="${context.biz_type}" hidden="true" />
			<emp:text id="CtrLoanCont.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
		
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="viewCtrLoanCont" label="查看" op="view"/>
		<emp:button id="deleteCtrLoanCont" label="撤销" op="update" />
		<emp:button id="destroyCtrLoanCont" label="注销" op="remove" />
		<!-- /**added by wangj  需求编号:XD141222087,法人账户透支需求变更  begin**/	 -->
		<emp:button id="stopCtrLoanCont" label="终止" op="update" />
		<!-- /**added by wangj  需求编号:XD141222087,法人账户透支需求变更  end**/	 -->
		<emp:button id="printCtrLoanCont" label="打印" op="print" /> 
		<emp:button id="ImageScan" label="影像扫描" op="ImageScan"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="ImageCheck" label="影像核对" op="ImageCheck"/>
		<!-- added by zhaoxp 2014/12/11 XD140812040, 银行承兑税票改造  start -->
		<emp:button id="ActpMaintain" label="银承税票维护" op="ActpMaintain"/>
		<!-- added by zhaoxp 2014/12/11 XD140812040, 银行承兑税票改造  end -->
		<!-- add by lisj 2015-3-10  需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能  begin-->
		<emp:button id="println" label="封面打印" op="println"/>
		<!-- add by lisj 2015-3-10  需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能  end-->
		<emp:button id="upload" label="附件"/>
	</div>

	<emp:table icollName="CtrLoanContList" pageMode="true" url="pageCtrLoanContQueryHistory.do" reqParams="biz_type=${context.biz_type}">
		<emp:text id="serno" label="申请编号" hidden="true"/>
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="cn_cont_no" label="中文合同编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="prd_id" label="产品编号" hidden="true"/>
		<emp:text id="prd_id_displayname" label="产品名称" />
		<emp:text id="assure_main" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" />
		<emp:text id="cont_cur_type" label="合同币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="cont_amt" label="合同金额" dataType="Currency"/>
		<emp:text id="cont_balance" label="合同余额" dataType="Currency"/>
		<emp:text id="ser_date" label="合同签订日期" />
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE"/>
		<emp:text id="iqpFlowHis" label="业务审批标识" hidden="true" defvalue="have"/>
		<!-- modified by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start -->
		<emp:text id="manager_id" label="责任人" hidden="true"/>
		<!-- modified by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 end -->
	</emp:table>

</body>
</html>
</emp:page>