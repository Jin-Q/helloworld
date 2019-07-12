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
		CtrLimitCont._toForm(form);
		CtrLimitContList._obj.ajaxQuery(null,form);
	};
	
	function doOrderCtrLimitCont() {
		var paramStr = CtrLimitContList._obj.getParamStr(['cont_no']);
		var contStatus = CtrLimitContList._obj.getParamStr(['cont_status']);
		if (paramStr != null) {
			if(contStatus == '100'){
				var url = '<emp:url action="getCtrLimitContUpdatePage.do"/>?iqpFlowHis=have&op=view&'+paramStr;
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else {
				alert("只有未生效的合同才允许签订操作！");
				return;
			}
			
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCtrLimitCont() {
		var paramStr = CtrLimitContList._obj.getParamStr(['cont_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getCtrLimitContViewPage.do"/>?iqpFlowHis=have&op=view&his=is&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};

	
	function doReset(){
		page.dataGroups.CtrLimitContGroup.reset();
	};

	function returnCus(data){
		CtrLimitCont.cus_id._setValue(data.cus_id._getValue());
		CtrLimitCont.cus_name._setValue(data.cus_name._getValue());
	};

	function doImageView(){
		var data = CtrLimitContList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View23');	//2.3.	客户资料查看（客户全视图）接口
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = CtrLimitContList._obj.getParamValue(['cus_id']);	//客户资料的业务编号就填cus_id
		data['cus_id'] = CtrLimitContList._obj.getParamValue(['cus_id']);	//客户编号
		data['prd_id'] = 'BASIC';	//业务品种
		data['prd_stage'] = '' ;	//业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	//注销
    function doDestroyGrtGuarCont(){
    	var paramStr = CtrLimitContList._obj.getParamStr(['cont_no']);
    	if (paramStr != null) {
    		var approve_status = CtrLimitContList._obj.getSelectedData()[0].cont_status._getValue();
    		//added by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start
    		var currentUserId = '${context.currentUserId}';
    		var manager_id = CtrLimitContList._obj.getParamValue('manager_id');
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
    					}else if(flag == "iqpError"){
    						alert("此合同下存在在途业务,业务流水号为："+billNo);
        				}else if(flag == "ctrError"){
        					alert("此合同下存在未签订合同,合同编号为："+billNo);
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
    			var url = '<emp:url action="destroyCtrLimitCont.do"/>?'+paramStr;	
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
	/*****2019-03-01 jiangcuihua 附件上传  start******/
	function doUpload(){
		var paramStr = CtrLimitContList._obj.getParamValue(['serno']);
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

	<emp:gridLayout id="CtrLimitContGroup" title="输入查询条件" maxColumn="3">
			<emp:text id="CtrLimitCont.cont_no" label="合同编号" />
			<emp:pop id="CtrLimitCont.cus_name" label="客户名称" buttonLabel="选择" url="queryAllCusPop.do?cusTypCondition=main_br_id='${context.organNo}'&returnMethod=returnCus" />
			<emp:select id="CtrLimitCont.cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE"/>
			<emp:text id="CtrLimitCont.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewCtrLimitCont" label="查看" op="view"/>
		<emp:button id="destroyGrtGuarCont" label="注销" op="remove" />
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="upload" label="附件"/>
	</div>


	<emp:table icollName="CtrLimitContList" pageMode="true" url="pageCtrLimitContQuery.do">
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="cont_cn" label="中文合同编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE"/>
		<emp:text id="cont_amt" label="合同金额" dataType="Currency"/>
		<emp:text id="start_date" label="起始日期" />
		<emp:text id="end_date" label="到期日期" />		
		<emp:text id="memo" label="备注"  hidden="true"/>
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" hidden="true"/>
		<emp:text id="cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE"/>
		<emp:text id="manager_br_id" label="管理机构" hidden="true"/>
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<!-- modified by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 start -->
		<emp:text id="manager_id" label="责任人" hidden="true"/>
		<!-- modified by yangzy 2015/04/21 需求：XD150325024，集中作业扫描岗权限改造 end -->
	</emp:table>
	
</body>
</html>
</emp:page>
    