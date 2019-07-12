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
		IqpExtensionAgr._toForm(form);
		IqpExtensionAgrList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpExtensionAgrPage() {
		var paramStr = IqpExtensionAgrList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var status = IqpExtensionAgrList._obj.getParamValue('status');
			if(status != "100"){
				alert("只能签订未生效的协议!");
				return;
			}
			var url = '<emp:url action="getIqpExtensionAgrUpdatePage.do"/>?'+paramStr+"&op=view";
			url = EMPTools.encodeURI(url);
			/** modified by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 begin **/
			//window.location = url;
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow_IqpExtensionAgr',param); 
			/** modified by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 end **/
		} else {
			alert('请先选择一条记录！');
		}
	};

	/** added by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 begin **/
	function refresh(){
		var direct = document.getElementById("emp_pq_jumpButton");
		    direct.click();
	};
	/** added by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 end **/
	
	function doViewIqpExtensionAgr() {
		var paramStr = IqpExtensionAgrList._obj.getParamStr(['serno']);
		if (paramStr != null) {
		    /** modified by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 begin **/
			var url = '<emp:url action="getIqpExtensionAgrViewPage.do"/>?'+paramStr+"&op=view&viewtype=out";
			url = EMPTools.encodeURI(url);
			//window.location = url;
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow_IqpExtensionAgr',param); 
		    /** modified by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 end **/
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doDeleteIqpExtensionAgr() {//作废操作
		var paramStr = IqpExtensionAgrList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			if(confirm("是否确认要作废？")){
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
    						alert("作废成功!");
    						window.location.reload();
        				}else{
    						alert("作废失败!"); 
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
    			var url = '<emp:url action="updateIqpExtensionAgrRecord.do"/>?'+paramStr+"&action=800";
    			url = EMPTools.encodeURI(url);
    			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
    		}
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doMoveIqpExtensionAgr() {//撤销操作
		var paramStr = IqpExtensionAgrList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start
    		var currentUserId = '${context.currentUserId}';
    		var manager_id = IqpExtensionAgrList._obj.getParamValue('manager_id');
    		if(manager_id!=null && manager_id!='' && currentUserId != manager_id){
    			alert('非当前客户主管客户经理，操作失败！');
    			return;
    		}
    		//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 end	
    		var status = IqpExtensionAgrList._obj.getParamValue(['status']);
    		if(status == "200"){
    			if(confirm("是否确认要作废？")){
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
        						alert("撤销成功!");
        						window.location.reload();
            				}else{
            					alert("已出账，不能撤销!");
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
        			var url = '<emp:url action="updateIqpExtensionAgrRecord.do"/>?'+paramStr+"&action=700";
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
			
	function doReset(){
		page.dataGroups.IqpExtensionAgrGroup.reset();
	};
	
	/*--user code begin--*/
	function returnCus(data){
		IqpExtensionAgr.cus_id._setValue(data.cus_id._getValue());
		IqpExtensionAgr.cus_id_displayname._setValue(data.cus_name._getValue());
    };
    function doImageScan(){
		var data = IqpExtensionAgrList._obj.getSelectedData();
		if (data != null && data !=0) {
    		var currentUserId = '${context.currentUserId}';
    		var rolesList = '${context.roleNoList}';
    		var manager_id = IqpExtensionAgrList._obj.getParamValue('manager_id');
    		if(rolesList.indexOf("3002")<0 && manager_id!=null && manager_id!='' && currentUserId != manager_id){
    			alert('非当前客户主管客户经理，操作失败！');
    			return;
    		}
			ImageAction('Scan24');	//展期业务资料扫描
		} else {
			alert('请先选择一条记录！');
		}
	};
    function doImageView(){
		var data = IqpExtensionAgrList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View25');	//业务资料查看
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function doImageCheck(){
		var data = IqpExtensionAgrList._obj.getSelectedData();
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
		/* modified by yangzy 2014/11/07 展期协议影像查看传新流水改造 start */
		data['serno'] = IqpExtensionAgrList._obj.getParamValue(['serno'])+","
			+IqpExtensionAgrList._obj.getParamValue(['prd_id']);	//展期同时取原业务编号和展期编号
		/* modified by yangzy 2014/11/07 展期协议影像查看传新流水改造 end */
		data['cus_id'] = IqpExtensionAgrList._obj.getParamValue(['cus_id']);	//客户码
		/**add by lisj 2014年11月10日 修改展期prd_id字段为'zqyw' begin**/
		data['prd_id'] = 'zqyw';	//业务品种
		/**add by lisj 2014年11月10日 修改展期prd_id字段为'zqyw' end**/
		data['prd_stage'] = 'YWSQ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	
	/**modified by lisj 2015-7-24  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更 begin**/
	/**add by lisj  2015-3-10  需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能 begin**/
	function doPrintln(){	
		var paramStr = IqpExtensionAgrList._obj.getParamStr(['agr_no']);
		var cont_no  = IqpExtensionAgrList._obj.getParamValue(['agr_no']);
		var prd_id = IqpExtensionAgrList._obj.getParamValue(['prd_id']);
		var serno =  IqpExtensionAgrList._obj.getParamValue(['serno']);
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
						var url = '<emp:url action="getPrintPage.do"/>?cont_no='+cont_no+"&print_type=05&prd_id="+prd_id+"&serno="+serno;
						url = EMPTools.encodeURI(url);
						var param = 'height=356, width=365, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
						window.open(url,'newWindow4IEA',param);
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
			var resetUrl ='<emp:url action="resetRcInfo.do"/>?cont_no='+cont_no+"&print_type=05";
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
		var paramStr = IqpExtensionAgrList._obj.getParamValue(['serno']);
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
	<form  method="POST" action="#" id="queryForm" style="width: 1600">
	
	<emp:gridLayout id="IqpExtensionAgrGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="IqpExtensionAgr.serno" label="业务编号" />
			<emp:text id="IqpExtensionAgr.agr_no" label="展期协议编号" />
			<emp:text id="IqpExtensionAgr.fount_bill_no" label="原借据编号" />			
			<emp:pop id="IqpExtensionAgr.cus_id_displayname" label="客户名称" buttonLabel="选择" url="queryAllCusPop.do?cusTypCondition=cus_status='20'&returnMethod=returnCus" />
			<emp:text id="IqpExtensionAgr.cus_id" label="客户码" hidden="true"/>
			<emp:select id="IqpExtensionAgr.status" label="协议状态" dictname="STD_ZB_CTRLOANCONT_TYPE"/>
			<emp:text id="IqpExtensionAgr.fount_cont_no" label="原合同编号" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewIqpExtensionAgr" label="查看" op="view"/>
		<emp:button id="getUpdateIqpExtensionAgrPage" label="签订" op="update"/>		
		<emp:button id="deleteIqpExtensionAgr" label="作废" op="moves"/>
		<emp:button id="moveIqpExtensionAgr" label="撤销" op="move"/>
		<emp:button id="ImageScan" label="影像扫描" op="ImageScan"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="ImageCheck" label="影像核对" op="ImageCheck"/>
		<!-- add by lisj 2015-3-10  需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能  begin-->
		<emp:button id="println" label="封面打印" op="println"/>
		<!-- add by lisj 2015-3-10  需求编号：【XD150107002】信贷业务纸质档案封面的导出与打印功能  end-->
		<emp:button id="upload" label="附件"/>
	</div>

	<emp:table icollName="IqpExtensionAgrList" pageMode="true" url="pageIqpExtensionAgrQuery.do">
		<emp:text id="serno" label="业务编号" />
		<emp:text id="fount_serno" label="原业务编号" hidden="true"/>
		<emp:text id="agr_no" label="展期协议编号" />
		<emp:text id="fount_bill_no" label="原借据编号" />
		<emp:text id="fount_cont_no" label="原合同编号" hidden="true"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="prd_name" label="产品名称"/>
		<emp:text id="prd_id" label="产品编号" hidden="true"/>
		<emp:text id="fount_cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="extension_amt" label="展期金额" dataType="Currency"/>		
		<emp:text id="extension_rate" label="展期利率(年)" dataType="Rate"/>
		<emp:text id="extension_date" label="展期到期日期" />
		<emp:text id="manager_id_displayname" label="责任人" />
		<emp:text id="manager_br_id_displayname" label="责任机构" />
		<emp:date id="input_date" label="登记日期" hidden="true"/>
		<emp:text id="status" label="协议状态" dictname="STD_ZB_CTRLOANCONT_TYPE" />
		<!-- modified by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start -->
		<emp:text id="manager_id" label="责任人" hidden="true"/>
		<!-- modified by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 end -->
	</emp:table>
	</form>
</body>
</html>
</emp:page>
    