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
		CtrAssetstrsfCont._toForm(form);
		CtrAssetstrsfContList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCtrAssetstrsfContPage() {
		var paramStr = CtrAssetstrsfContList._obj.getParamStr(['serno','cont_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getCtrAssetstrsfContUpdatePage.do"/>?op=update&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCtrAssetstrsfCont() {
		var paramStr = CtrAssetstrsfContList._obj.getParamStr(['serno','cont_no']);
		if (paramStr != null) {
		    /** modified by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 begin **/
			var url = '<emp:url action="getCtrAssetstrsfContViewPage.do"/>?op=view&viewtype=out&'+paramStr;
			url = EMPTools.encodeURI(url);
			//window.location = url;
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow_CtrAssetstrsfContHis',param); 
			/** modified by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 end **/
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCtrAssetstrsfContPage() {
		var url = '<emp:url action="getCtrAssetstrsfContAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	//撤销
    function doDeleteCtrAssetstrsfCont(){
    	var paramStr = CtrAssetstrsfContList._obj.getParamStr(['cont_no']);
    	if (paramStr != null) {
    		var approve_status = CtrAssetstrsfContList._obj.getSelectedData()[0].cont_status._getValue();
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
    			var url = '<emp:url action="deleteCtrAssetstrsfCont.do"/>?'+paramStr;	
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
    function doDestroyCtrAssetstrsfCont(){
    	var paramStr = CtrAssetstrsfContList._obj.getParamStr(['cont_no']);
    	if (paramStr != null) {
    		var approve_status = CtrAssetstrsfContList._obj.getSelectedData()[0].cont_status._getValue();
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
    			var url = '<emp:url action="destroyCtrAssetstrsfCont.do"/>?'+paramStr;	
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
	
	function doReset(){
		page.dataGroups.CtrAssetstrsfContGroup.reset();
	};
	
	/*--user code begin--*/
    function getOrgNo(data){
    	CtrAssetstrsfCont.toorg_no._setValue(data.same_org_no._getValue());
    	CtrAssetstrsfCont.toorg_name._setValue(data.same_org_cnname._getValue());
    };
    function doImageView(){
		var data = CtrAssetstrsfContList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View25');	//业务资料查看
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function doImageScan(){
		var data = CtrAssetstrsfContList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('Scan24');	//业务资料扫描
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doImageCheck(){
		var data = CtrAssetstrsfContList._obj.getSelectedData();
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
		data['serno'] = CtrAssetstrsfContList._obj.getParamValue(['serno']);	//业务编号
		data['cus_id'] = CtrAssetstrsfContList._obj.getParamValue(['toorg_no']);	//客户码
		data['prd_id'] = CtrAssetstrsfContList._obj.getParamValue(['prd_id']);	//业务品种
		data['prd_stage'] = 'YWSQ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*--user code end--*/
	/*****2019-03-01 jiangcuihua 附件上传  start******/
	function doUpload(){
		var paramStr = CtrAssetstrsfContList._obj.getParamValue(['serno']);
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

	<emp:gridLayout id="CtrAssetstrsfContGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="CtrAssetstrsfCont.cont_no" label="合同编号" />
			<emp:pop id="CtrAssetstrsfCont.toorg_name" label="交易对手行名" url="queryCusSameOrgForPopList.do?restrictUsed=false" returnMethod="getOrgNo" required="false" buttonLabel="选择" />
			<emp:select id="CtrAssetstrsfCont.cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE" />
			<emp:text id="CtrAssetstrsfCont.toorg_no" label="交易对手行号" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewCtrAssetstrsfCont" label="查看" op="view"/>
		<emp:button id="deleteCtrAssetstrsfCont" label="撤销" op="update" />
		<emp:button id="destroyCtrAssetstrsfCont" label="注销" op="remove" />
		<emp:button id="ImageScan" label="影像扫描" op="ImageScan"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="ImageCheck" label="影像核对" op="ImageCheck"/>
		<emp:button id="upload" label="附件"/>
	</div>

	<emp:table icollName="CtrAssetstrsfContList" pageMode="true" url="pageCtrAssetstrsfContHisQuery.do">
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="toorg_no" label="交易对手行号" />
		<emp:text id="toorg_name" label="交易对手行名" />
		<emp:text id="prd_id_displayname" label="产品名称" />		
		<emp:text id="takeover_type" label="转让方式" dictname="STD_ZB_TAKEOVER_MODE" />		
		<emp:text id="asset_total_amt" label="资产总额" dataType="Currency"/>
		<emp:text id="takeover_total_amt" label="转让金额" dataType="Currency"/>
		<emp:text id="takeover_date" label="转让日期" />
		<emp:text id="ser_date" label="合同签订日期" />
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="manager_br_id" label="管理机构" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" hidden="true"/>
		<emp:text id="cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE" />
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="prd_id" label="产品编号" hidden="true"/>
		<emp:text id="asset_no" label="资产包编号" hidden="true"/>
		<emp:text id="acct_curr" label="转让币种" dictname="STD_ZX_CUR_TYPE" hidden="true"/>
	</emp:table>
</body>
</html>
</emp:page>
    