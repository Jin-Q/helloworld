<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<!-- add by yezm 2015-8-14  需求编号：【XD150303015】 增加对被放款审查岗打回的业务申请信息进行修改流程 begin-->
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<!-- add by yezm 2015-8-14  需求编号：【XD150303015】 增加对被放款审查岗打回的业务申请信息进行修改流程 end-->
<% 
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String PO_TYPE = "";
	if(context.containsKey("PO_TYPE")){
		PO_TYPE = context.getDataValue("PO_TYPE").toString();
	}
%>
<script type="text/javascript">

	function doQuery(){
		var form = document.getElementById('queryForm');
		IqpActrecpoMana._toForm(form);
		IqpActrecpoManaList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateIqpActrecpoManaPage() {
		var paramStr = IqpActrecpoManaList._obj.getParamStr(['po_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpActrecpoManaTabHelp.do"/>?'+paramStr+'&PO_TYPE=<%=PO_TYPE%>&type=add';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewIqpActrecpoMana() {
		var paramStr = IqpActrecpoManaList._obj.getParamStr(['po_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getIqpActrecpoManaTabHelp.do"/>?'+paramStr+'&type=view&PO_TYPE=<%=PO_TYPE%>';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddIqpActrecpoManaPage() {
		var url = '<emp:url action="getIqpActrecpoManaAddPage.do"/>?PO_TYPE=<%=PO_TYPE%>&type=add';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	function doDeleteIqpActrecpoMana(){
		
		var paramStr = IqpActrecpoManaList._obj.getParamStr(['po_no']);
		if (paramStr != null) {
			var paramStr2 = IqpActrecpoManaList._obj.getParamStr(['status']);
			if(paramStr2!='status=1'){
				alert("只有登记状态可以删除！");
				return;
				}
			if(confirm("是否确认要删除？")){
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
							alert("删除成功！");
							window.location.reload();
						}else {
							alert("删除失败！");
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
				
				var url = '<emp:url action="deleteIqpActrecpoManaRecord.do"/>?'+paramStr;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	}


	function doInPO(){
		var paramStr = IqpActrecpoManaList._obj.getParamStr(['po_no']);
		if (paramStr != null) {
			var url = '<emp:url action="iqpActrecBondInPoPage.do"/>?'+paramStr+'&type=add';
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow',param);
		}else{
			alert('请先选择一条记录！');
		}
	}

	function doOutPo(){
		var paramStr = IqpActrecpoManaList._obj.getParamStr(['po_no']);
		if (paramStr != null) {
			var paramStr2 = IqpActrecpoManaList._obj.getParamStr(['status']);
			if(paramStr2!='status=2'){
				alert("只有有效状态才可以出池操作！");
				return;
				}
			var url = '<emp:url action="iqpActrecBondOutPoPage.do"/>?'+paramStr+'&type=add';
			url = EMPTools.encodeURI(url);
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow',param);
		}else{
			alert('请先选择一条记录！');
		}
	}
	
	function doReset(){
		page.dataGroups.IqpActrecpoManaGroup.reset();
	};

	function returnCus(data){
		IqpActrecpoMana.cus_id._setValue(data.cus_id._getValue());
		IqpActrecpoMana.cus_id_displayname._setValue(data.cus_name._getValue());
    };

	/**add by lisj 2015-7-23  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更 begin**/
	function doPrintln(){	
		var paramStr = IqpActrecpoManaList._obj.getParamStr(['po_no']);
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
						var url = '<emp:url action="getPrintPage.do"/>?'+paramStr+"&print_type=14";
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
			var resetUrl ='<emp:url action="resetRcInfo.do"/>?'+paramStr+"&print_type=14";
			resetUrl = EMPTools.encodeURI(resetUrl);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',resetUrl, callback);
		} else {
			alert('请先选择一条记录！');
		}
	};
	/**add by lisj 2015-7-23  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更 end**/
	
	/**add by yezm 2015-8-14  需求编号：【XD150303015】 增加对被放款审查岗打回的业务申请信息进行修改流程 begin**/
	function doImageView(){
		var data = IqpActrecpoManaList._obj.getSelectedData();
		if (data != null && data !=0) {
			var image_guaranty_no = IqpActrecpoManaList._obj.getParamValue(['image_guaranty_no']);
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
		var data = IqpActrecpoManaList._obj.getSelectedData();
		if (data != null && data !=0) {
			var image_guaranty_no = IqpActrecpoManaList._obj.getParamValue(['image_guaranty_no']);
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
			data['serno'] = IqpActrecpoManaList._obj.getParamValue(['po_no']);	//保证编码
			data['cus_id'] = IqpActrecpoManaList._obj.getParamValue(['cus_id']);	//客户编号
			data['prd_id'] = 'PO';	//业务品种
			data['prd_stage'] = IqpActrecpoManaList._obj.getParamValue(['image_guaranty_no']);	//业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
			data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		}else{
			data['serno'] = IqpActrecpoManaList._obj.getParamValue(['po_no']);	//业务编号
			data['cus_id'] = IqpActrecpoManaList._obj.getParamValue(['cus_id']);	//客户码
			data['prd_id'] = 'ASSURE';	//业务品种
			data['prd_stage'] = IqpActrecpoManaList._obj.getParamValue(['image_guaranty_no']);	//业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
			data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		}
		doPubImageAction(data);
	};
	/**add by yezm 2015-8-14  需求编号：【XD150303015】 增加对被放款审查岗打回的业务申请信息进行修改流程 end**/
	
	/*****2019-03-01 jiangcuihua 附件上传  start******/
	function doUpload(){
		var paramStr = IqpActrecpoManaList._obj.getParamValue(['po_no']);
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
	<emp:gridLayout id="IqpActrecpoManaGroup" title="输入查询条件" maxColumn="3">		
		<emp:text id="IqpActrecpoMana.po_no" label="池编号" />
		<emp:pop id="IqpActrecpoMana.cus_id_displayname" label="客户名称" url="queryAllCusPop.do?cusTypCondition=cus_status='20' and BELG_LINE IN('BL100','BL200')&returnMethod=returnCus" />
		<emp:select id="IqpActrecpoMana.status" label="池状态" dictname="STD_ACTRECPO_STATUS"/>
		<emp:text id="IqpActrecpoMana.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="getAddIqpActrecpoManaPage" label="新增" op="add"/>
		<emp:button id="getUpdateIqpActrecpoManaPage" label="修改" op="update"/>
		<emp:button id="deleteIqpActrecpoMana" label="删除" op="remove"/>
		<emp:button id="viewIqpActrecpoMana" label="查看" op="view"/>
		<emp:button id="inPO" label="入池" op="inPO"/>
		<emp:button id="outPo" label="出池" op="outPo"/>
		<!-- add by lisj 2015-7-23  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更 begin-->
		<emp:button id="println" label="封面打印" op="println"/>
		<!-- add by lisj 2015-7-23  需求编号：【XD150710050】信贷业务纸质档案封面的导出与打印功能变更  end-->
		<!-- add by yezm 2015-8-14  需求编号：【XD150303015】 增加对被放款审查岗打回的业务申请信息进行修改流程 begin-->
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="ImageCheck" label="影像核对" op="ImageCheck"/>
		<!-- add by yezm 2015-8-14  需求编号：【XD150303015】 增加对被放款审查岗打回的业务申请信息进行修改流程 end-->
		<emp:button id="upload" label="附件"/>
	</div>
	<emp:table icollName="IqpActrecpoManaList" pageMode="true" url="pageIqpActrecpoManaQuery.do" reqParams="PO_TYPE=${context.PO_TYPE}">
		<emp:text id="po_no" label="池编号" />
		<!-- add by yangzy 2015-8-26  需求编号：【XD150303015】 增加对被放款审查岗打回的业务申请信息进行修改流程-池增加影像 begin -->
		<emp:text id="image_guaranty_no" label="影像押品编号" />
		<!-- add by yangzy 2015-8-26  需求编号：【XD150303015】 增加对被放款审查岗打回的业务申请信息进行修改流程-池增加影像 end -->
		<emp:text id="po_type" label="池类别" dictname="STD_ACTRECPO_TYPE"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="po_mode" label="池模式" hidden="true"/>
		<emp:text id="factor_mode" label="保理方式" hidden="true"/>
		<emp:text id="is_rgt_res" label="是否有追索权" hidden="true"/>
		<emp:text id="invc_quant" label="在池发票数量" />
		<emp:text id="invc_amt" label="在池发票总金额" dataType="Currency"/>
		<emp:text id="crd_rgtchg_amt" label="债权转让总金额"  hidden="true"/>
		<emp:text id="pledge_rate" label="质押率" hidden="true"/>
		<emp:text id="period_grace" label="宽限期" hidden="true"/>
		<emp:text id="memo" label="备注" hidden="true"/>		
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="input_br_id" label="登记机构" hidden="true"/>
		<emp:text id="input_date" label="登记日期" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_br_id_displayname" label="登记机构" />	
		<emp:text id="status" label="池状态" dictname="STD_ACTRECPO_STATUS"/>	
	</emp:table>
	
</body>
</html>
</emp:page>
    