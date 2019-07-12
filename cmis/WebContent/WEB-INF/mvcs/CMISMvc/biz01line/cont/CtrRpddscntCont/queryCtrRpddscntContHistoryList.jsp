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
		CtrRpddscntCont._toForm(form);
		CtrRpddscntContList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCtrRpddscntContPage() {
		var paramStr = CtrRpddscntContList._obj.getParamStr(['serno','cont_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getCtrRpddscntContUpdatePage.do"/>?'+paramStr+"&op=view";
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCtrRpddscntCont() {
		var paramStr = CtrRpddscntContList._obj.getParamStr(['serno','cont_no']);
		if (paramStr != null) {
		    /** modified by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 begin **/
			var url = '<emp:url action="getCtrRpddscntContViewPage.do"/>?'+paramStr+"&op=view&viewtype=out";
			url = EMPTools.encodeURI(url);
			//window.location = url;
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow_CtrRpddscntContHis',param); 
			/** modified by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 end **/
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCtrRpddscntContPage() {
		var url = '<emp:url action="getCtrRpddscntContAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doReset(){
		page.dataGroups.CtrRpddscntContGroup.reset();
	};
	
	/*--user code begin--*/
	function getOrgNo(data){
		CtrRpddscntCont.toorg_no._setValue(data.same_org_no._getValue());
		CtrRpddscntCont.toorg_name._setValue(data.same_org_cnname._getValue());
    };
	function doImageScan(){
		var data = CtrRpddscntContList._obj.getSelectedData();
		if (data != null && data !=0) {
			//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start
    		var currentUserId = '${context.currentUserId}';
    		var rolesList = '${context.roleNoList}';
    		var manager_id = CtrRpddscntContList._obj.getParamValue('manager_id');
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
		var data = CtrRpddscntContList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View25');	//业务资料查看
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function doImageCheck(){
		var data = CtrRpddscntContList._obj.getSelectedData();
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
		data['serno'] = CtrRpddscntContList._obj.getParamValue(['serno']);	//业务编号
		data['cus_id'] = CtrRpddscntContList._obj.getParamValue(['toorg_no']);	//客户码
		data['prd_id'] = CtrRpddscntContList._obj.getParamValue(['prd_id']);	//业务品种
		data['prd_stage'] = 'YWSQ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};

	//注销
    function doDestroyCtrRpddscntCont(){
    	var paramStr = CtrRpddscntContList._obj.getParamStr(['cont_no']);
    	if (paramStr != null) {
    		//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start
    		var currentUserId = '${context.currentUserId}';
    		var manager_id = CtrRpddscntContList._obj.getParamValue('manager_id');
    		if(manager_id!=null && manager_id!='' && currentUserId != manager_id){
    			alert('非当前客户主管客户经理，操作失败！');
    			return;
    		}
    		//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 end	
    		var approve_status = CtrRpddscntContList._obj.getSelectedData()[0].cont_status._getValue();
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
    			var url = '<emp:url action="destroyCtrRpddscntCont.do"/>?'+paramStr;	
    			url = EMPTools.encodeURI(url);
    			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
    		}
    		}else{
    			alert("只有状态为【生效】的合同才可以注销！");
    		}
    	} else {
    		alert('请先选择一条记录！');
    	}
    };
  //撤销
    function doDeleteCtrRpddscntCont(){
    	var paramStr = CtrRpddscntContList._obj.getParamStr(['cont_no']);
    	if (paramStr != null) {
    		//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start
    		var currentUserId = '${context.currentUserId}';
    		var manager_id = CtrRpddscntContList._obj.getParamValue('manager_id');
    		if(manager_id!=null && manager_id!='' && currentUserId != manager_id){
    			alert('非当前客户主管客户经理，操作失败！');
    			return;
    		}
    		//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 end	
    		var approve_status = CtrRpddscntContList._obj.getSelectedData()[0].cont_status._getValue();
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
    						window.location.reload();
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
    			var url = '<emp:url action="deleteCtrRpddscntCont.do"/>?'+paramStr;	
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

    /**modified by lisj 2015-7-24  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更 begin**/
    /**add by lisj  2015-3-10  需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能 begin**/
	function doPrintln(){	
		var paramStr = CtrRpddscntContList._obj.getParamStr(['cont_no']);
		var prd_id = CtrRpddscntContList._obj.getParamValue(['prd_id']);
		var serno =  CtrRpddscntContList._obj.getParamValue(['serno']);
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
						var url = '<emp:url action="getPrintPage.do"/>?'+paramStr+"&print_type=05&prd_id="+prd_id+"&serno="+serno;
						url = EMPTools.encodeURI(url);
						var param = 'height=356, width=365, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
						window.open(url,'newWindow4CRC',param);
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
			var resetUrl ='<emp:url action="resetRcInfo.do"/>?'+paramStr+"&print_type=05";
			resetUrl = EMPTools.encodeURI(resetUrl);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',resetUrl, callback);
		} else {
			alert('请先选择一条记录！');
		}
	};
	/**add by lisj  2015-3-10  需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能 end**/
	/**modified by lisj 2015-7-24  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更 end**/
	
	/*****2019-03-01 jiangcuihua 附件上传  start******/
	function doUpload(){
		var paramStr = CtrRpddscntContList._obj.getParamValue(['serno']);
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

	<emp:gridLayout id="CtrRpddscntContGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CtrRpddscntCont.cont_no" label="合同编号" />
			<emp:pop id="CtrRpddscntCont.toorg_name" label="交易对手行名" url="queryCusSameOrgForPopList.do?restrictUsed=false" returnMethod="getOrgNo" required="false" buttonLabel="选择" />
			<emp:select id="CtrRpddscntCont.rpddscnt_type" label="转贴现方式" dictname="STD_ZB_RPDDSCNT_MODE"/>
			<emp:select id="CtrRpddscntCont.cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE"/>
			<emp:text id="CtrRpddscntCont.toorg_no" label="交易对手行号" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewCtrRpddscntCont" label="查看" op="view"/>
		<emp:button id="deleteCtrRpddscntCont" label="撤销" op="update" />
		<emp:button id="destroyCtrRpddscntCont" label="注销" op="remove" />
		<emp:button id="ImageScan" label="影像扫描" op="ImageScan"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="ImageCheck" label="影像核对" op="ImageCheck"/>
		<!-- add by lisj 2015-3-10  需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能  begin-->
		<emp:button id="println" label="封面打印" op="println"/>
		<!-- add by lisj 2015-3-10  需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能  end-->
		<emp:button id="upload" label="附件"/>
	</div>

	<emp:table icollName="CtrRpddscntContList" pageMode="true" url="pageCtrRpddscntContHistoryQuery.do">
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="batch_no" label="批次号" hidden="true"/>
		<emp:text id="prd_id" label="产品编号" hidden="true"/>
		<emp:text id="toorg_no" label="交易对手行号" />
		<emp:text id="toorg_name" label="交易对手行名" />
		<emp:text id="prd_id_displayname" label="产品名称" />
		<emp:text id="bill_type" label="票据种类" dictname="STD_DRFT_TYPE" />
		<emp:text id="rpddscnt_type" label="转贴现方式" dictname="STD_ZB_BUSI_TYPE" />
		<emp:text id="bill_total_amt" label="票据总金额" />
		<emp:text id="bill_qnt" label="票据数量" />
		<emp:text id="ser_date" label="合同签订日期" />
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="input_id_displayname" label="登记人" />	
		<emp:text id="manager_br_id" label="管理机构" hidden="true"/>
		<emp:text id="cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE" />
		<!-- modified by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start -->
		<emp:text id="manager_id" label="责任人" hidden="true"/>
		<!-- modified by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 end -->
	</emp:table>
	
</body>
</html>
</emp:page>
    