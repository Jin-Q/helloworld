<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<!-- add by yezm 2015-8-14  需求编号：【XD150303015】 增加对被放款审查岗打回的业务申请信息进行修改流程 begin-->
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<!-- add by yezm 2015-8-14  需求编号：【XD150303015】 增加对被放款审查岗打回的业务申请信息进行修改流程end-->

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpDrfpoMana._toForm(form);
		IqpDrfpoManaList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateDpoDrfpoManaPage() {
		var paramStr = IqpDrfpoManaList._obj.getParamStr(['drfpo_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpdpoTabHelp.do"/>?op=update&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewDpoDrfpoMana() {
		var paramStr = IqpDrfpoManaList._obj.getParamStr(['drfpo_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getDpoDrfpoManaViewPage.do"/>?op=view&'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddDpoDrfpoManaPage() {
		var url = '<emp:url action="getDpoDrfpoManaAddPage.do"/>?op=add';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteDpoDrfpoMana() {
		var paramStr = IqpDrfpoManaList._obj.getParamStr(['drfpo_no']);
		
		if (paramStr != null) {
			var status = IqpDrfpoManaList._obj.getParamStr(['status']);
			if(status=="status=00"){
				if(confirm("是否确认要删除？")){
					var handleSuccess = function(o) {
						if (o.responseText !== undefined) {
							try {
								var jsonstr = eval("(" + o.responseText + ")");
							} catch (e) {
								alert("Parse jsonstr define error!" + e.message);
								return;
							}
							var flag = jsonstr.flag;
							if("success" == flag){
								alert("已删除！");
								window.location.reload();
							}else{
								alert("删除失败！");
							}
						}
					};
					var handleFailure = function(o) {
					};
					var callback = {
						success :handleSuccess,
						failure :handleFailure
					};
					var url = '<emp:url action="deleteDpoDrfpoManaRecord.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);
			 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
				}
			}else{
				alert("非登记状态的票据池无法进行删除操作！");
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	//票据入池
	function doGetBillToPool(){
		var paramStr = IqpDrfpoManaList._obj.getParamStr(['drfpo_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getBillToPool.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var param = 'height=1000, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'doGetBillToPool',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	//票据出池
	function doGetBillOutPool(){
		var paramStr = IqpDrfpoManaList._obj.getParamStr(['drfpo_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getBillOutPool.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var param = 'height=1000, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'doGetBillOutPool',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	//票据托收
	function doGetBillCollPool(){
		var paramStr = IqpDrfpoManaList._obj.getParamStr(['drfpo_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getBillCollPool.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var param = 'height=1000, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
			window.open(url,'doGetBillCollPool',param);
		} else {
			alert('请先选择一条记录！');
		}
	};
	function doReset(){
		page.dataGroups.IqpDrfpoManaGroup.reset();
	};
	
	function returnCus(data){
		IqpDrfpoMana.cus_id._setValue(data.cus_id._getValue());
		IqpDrfpoMana.cus_id_displayname._setValue(data.cus_name._getValue());
    };			

    /**add by lisj 2015-7-23  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更 begin**/
	function doPrintln(){	
		var paramStr = IqpDrfpoManaList._obj.getParamStr(['drfpo_no']);
		var drfpo_no = IqpDrfpoManaList._obj.getParamValue(['drfpo_no']);
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
						var url = '<emp:url action="getPrintPage.do"/>?po_no='+drfpo_no+"&print_type=14";
						url = EMPTools.encodeURI(url);
						var param = 'height=500, width=1024, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
						window.open(url,'newWindow4IPM',param);
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
			var resetUrl ='<emp:url action="resetRcInfo.do"/>?po_no='+drfpo_no+"&print_type=14";
			resetUrl = EMPTools.encodeURI(resetUrl);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',resetUrl, callback);
		} else {
			alert('请先选择一条记录！');
		}
	};
	/**add by lisj 2015-7-23  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更 end**/

/** add by yezm 2015-8-14  需求编号：【XD150303015】 增加对被放款审查岗打回的业务申请信息进行修改流程 begin**/
	function doImageView(){
		var data = IqpDrfpoManaList._obj.getSelectedData();
		if (data != null && data !=0) {
			var image_guaranty_no = IqpDrfpoManaList._obj.getParamValue(['image_guaranty_no']);
			if(image_guaranty_no != null && image_guaranty_no !=''){
				ImageAction('View23');	//业务资料查看		
			}else{
				alert('影像押品编号不能为空，请确认!');
			}
		} else {
			alert('请先选择一条记录！');
		}		
	};

	function doImageCheck(){
		var data = IqpDrfpoManaList._obj.getSelectedData();
		if (data != null && data !=0) {
			var image_guaranty_no = IqpDrfpoManaList._obj.getParamValue(['image_guaranty_no']);
			if(image_guaranty_no != null && image_guaranty_no !=''){
				if( confirm("影像信息将直接归档，请确认!") ){
					ImageAction('Check3133');	//影像核对	
				}	
			}else{
				alert('影像押品编号不能为空，请确认!');
			}	
		} else {
			alert('请先选择一条记录！');
		}	
	};
	function ImageAction(image_action){
		var data = new Array();
		if(image_action == "Check3133"){
			data['serno'] = IqpDrfpoManaList._obj.getParamValue(['drfpo_no']);	//保证编码
			data['cus_id'] = IqpDrfpoManaList._obj.getParamValue(['cus_id']);	//客户编号
			data['prd_id'] = 'PJ';	//业务品种
			data['prd_stage'] = '' ;	//业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
			data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		}else{
			data['serno'] = IqpDrfpoManaList._obj.getParamValue(['drfpo_no']);	//业务编号
			data['cus_id'] = IqpDrfpoManaList._obj.getParamValue(['cus_id']);	//客户码
			data['prd_id'] = 'ASSURE';	//业务品种
			data['prd_stage'] = IqpDrfpoManaList._obj.getParamValue(['image_guaranty_no']);	//业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
			data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		}
		doPubImageAction(data);
	};
	/**add by yezm 2015-8-14  需求编号：【XD150303015】 增加对被放款审查岗打回的业务申请信息进行修改流程 end**/

	/*****2019-03-01 jiangcuihua 附件上传  start******/
	function doUpload(){
		var paramStr = IqpDrfpoManaList._obj.getParamValue(['drfpo_no']);
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

	<emp:gridLayout id="IqpDrfpoManaGroup" title="输入查询条件" maxColumn="3">
		<emp:text id="IqpDrfpoMana.drfpo_no" label="池编号" />		
		<emp:pop id="IqpDrfpoMana.cus_id_displayname" label="客户名称" url="queryAllCusPop.do?cusTypCondition=cus_status='20' and BELG_LINE IN('BL100','BL200')&returnMethod=returnCus" />
		<emp:select id="IqpDrfpoMana.status" label="池状态" dictname="STD_DRFPO_STATUS"/>
		<emp:text id="IqpDrfpoMana.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddDpoDrfpoManaPage" label="新增" op="add"/>
		<emp:button id="getUpdateDpoDrfpoManaPage" label="修改" op="update"/>
		<emp:button id="deleteDpoDrfpoMana" label="删除" op="remove"/>
		<emp:button id="viewDpoDrfpoMana" label="查看" op="view"/>
		<emp:button id="getBillToPool" label="票据入池" op="rc"/>
		<emp:button id="getBillOutPool" label="票据出池" op="cc"/>
		<emp:button id="getBillCollPool" label="托收管理" op="ts"/>
		<!-- add by yezm 2015-8-14  需求编号：【XD150303015】 增加对被放款审查岗打回的业务申请信息进行修改流程 begin-->
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="ImageCheck" label="影像核对" op="ImageCheck"/>
		<!-- add by yezm 2015-8-14  需求编号：【XD150303015】 增加对被放款审查岗打回的业务申请信息进行修改流程 end-->
		<!-- add by lisj 2015-7-23  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更 begin-->
		<emp:button id="println" label="封面打印" op="println"/>
		<!-- add by lisj 2015-7-23  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更  end-->
		<emp:button id="upload" label="附件"/>
	</div>

	<emp:table icollName="IqpDrfpoManaList" pageMode="true" url="pageDpoDrfpoManaQuery.do">
		<emp:text id="drfpo_no" label="池编号" />
		<!-- add by yangzy 2015-8-26  需求编号：【XD150303015】 增加对被放款审查岗打回的业务申请信息进行修改流程-池增加影像 begin -->
		<emp:text id="image_guaranty_no" label="影像押品编号" />
		<!-- add by yangzy 2015-8-26  需求编号：【XD150303015】 增加对被放款审查岗打回的业务申请信息进行修改流程-池增加影像 end -->
		<emp:text id="drfpo_type" label="池类型" dictname="STD_DRFPO_TYPE" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />		
		<emp:text id="bill_amt" label="在池票据总金额" dataType="Currency"/>
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />		
		<emp:text id="status" label="状态" dictname="STD_DRFPO_STATUS" />
	</emp:table>
	
</body>
</html>
</emp:page>
    