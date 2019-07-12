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
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/> 
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<script type="text/javascript">
	
function doQuery(){
	var form = document.getElementById('queryForm');
	IqpLoanApp._toForm(form);
	IqpLoanAppList._obj.ajaxQuery(null,form);
};

function doGetUpdatePage__IqpLoanApp() {
	var paramStr = IqpLoanAppList._obj.getParamStr(['serno','cus_id']);
	var prd_id = IqpLoanAppList._obj.getParamValue(['prd_id']);
	var appStatus = IqpLoanAppList._obj.getParamValue(['approve_status']);
	if (paramStr != null) {
		var approve_status = IqpLoanAppList._obj.getSelectedData()[0].approve_status._getValue();
		if(approve_status == "000" || approve_status == "992" || approve_status == "993"){
		   var url;
			if(prd_id == 300021 || prd_id == 300020){
				url = '<emp:url action="getIqpLoanAppForDiscUpdatePage.do"/>?op=update&'+paramStr+'&flg=${context.flg}&biz_type='+'<%=biz_type%>'+'&approve_status='+appStatus;
			
			}else{
				url = '<emp:url action="getIqpLoanAppUpdatePage.do"/>?op=update&'+paramStr+'&flg=${context.flg}&biz_type='+'<%=biz_type %>'+'&approve_status='+appStatus;
			} 
			url = EMPTools.encodeURI(url);
			window.location = url;
		}else{
			alert("非【待发起】、【打回】、【追回】状态的记录不能进行修改操作！");
		}
	} else {
		alert('请先选择一条记录！');
	}
};
function doViewIqpLoanApp() {
	var paramStr = IqpLoanAppList._obj.getParamStr(['serno','cus_id']);
	var prd_id = IqpLoanAppList._obj.getParamValue(['prd_id']);
	var appStatus = IqpLoanAppList._obj.getParamValue(['approve_status']);
	if (paramStr != null) {
		if(prd_id == 300021 || prd_id == 300020){
			var url = '<emp:url action="getIqpLoanAppForDiscViewPage.do"/>?op=view&'+paramStr+'&flag=iqpLoanApp&flg=${context.flg}&biz_type='+'<%=biz_type %>'+'&approve_status='+appStatus;
		}else{
			var url = '<emp:url action="getIqpLoanAppViewPage.do"/>?op=view&'+paramStr+'&flag=iqpLoanApp&flg=${context.flg}&biz_type='+'<%=biz_type %>'+'&approve_status='+appStatus;
		}
		url = EMPTools.encodeURI(url);
		window.location = url;
	} else {
		alert('请先选择一条记录！');
	}
};
function doGetAddPage__IqpLoanApp() {
	var url = '<emp:url action="getIqpLoanAppAddPage.do"/>?flg=${context.flg}&biz_type='+'<%=biz_type %>';
	url = EMPTools.encodeURI(url);
	window.location = url;
};

function doDeleteRecord__IqpLoanApp() {
	var paramStr = IqpLoanAppList._obj.getParamStr(['serno']);
	if (paramStr != null) { 
		var approve_status = IqpLoanAppList._obj.getSelectedData()[0].approve_status._getValue();
		if(approve_status == "000"){ 
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
						alert("删除成功!");
						var url = '<emp:url action="queryIqpLoanAppList.do"/>?biz_type='+'<%=biz_type%>'+'&flg=${context.flg}';  
						url = EMPTools.encodeURI(url);
						window.location=url;
					}else {
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
			var url = '<emp:url action="deleteIqpLoanAppRecord.do"/>?'+paramStr;	
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
		}     
		}else{
			alert("只有状态为【待发起】的申请才可以进行删除！");
		}
	} else {
		alert('请先选择一条记录！');
	}
};

   function doReset(){
	  page.dataGroups.IqpLoanAppGroup.reset();
   };
	function returnPrdId(data){
		IqpLoanApp.prd_id._setValue(data.id);
		IqpLoanApp.prd_id_displayname._setValue(data.label); 
	};
   function returnCus(data){
	   IqpLoanApp.cus_id._setValue(data.cus_id._getValue());
	   IqpLoanApp.cus_name._setValue(data.cus_name._getValue());
   };

   //提交流程
   function doSubWF(){ 
	   var paramStr = IqpLoanAppList._obj.getParamStr(['serno']);
		if (paramStr != null) { 
		      var prd_id = IqpLoanAppList._obj.getSelectedData()[0].prd_id._getValue();
		      if(prd_id=='200024'){
			     checkAccpBillInfo();
		      }else if(prd_id=='300021'||prd_id=='300020'){
		    	 checkBillInfo();
			  }else{
			     getApplyTypeByPrdId();
		      }
		}else{
			alert('请先选择一条记录！');
		}
   };
		
   function doSubmitWF(apply_type){
		var serno = IqpLoanAppList._obj.getSelectedData()[0].serno._getValue();
		var cus_id = IqpLoanAppList._obj.getSelectedData()[0].cus_id._getValue();
		var cus_name = IqpLoanAppList._obj.getSelectedData()[0].cus_id_displayname._getValue();
		var approve_status = IqpLoanAppList._obj.getSelectedData()[0].approve_status._getValue();
		var prd_id = IqpLoanAppList._obj.getSelectedData()[0].prd_id._getValue();
		WfiJoin.table_name._setValue("IqpLoanApp");
		WfiJoin.pk_col._setValue("serno");
		WfiJoin.pk_value._setValue(serno);
		WfiJoin.cus_id._setValue(cus_id);
		WfiJoin.cus_name._setValue(cus_name);
		WfiJoin.prd_pk._setValue(prd_id);
		WfiJoin.prd_name._setValue(IqpLoanAppList._obj.getSelectedData()[0].prd_id_displayname._getValue());
		WfiJoin.amt._setValue(IqpLoanAppList._obj.getSelectedData()[0].apply_amount._getValue());
		WfiJoin.wfi_status._setValue(approve_status);
		WfiJoin.status_name._setValue("approve_status");
		if('${context.menuId}'=="csgnClaimInvestqueryIqpLoanApp"){
			if(prd_id == "100065"){//个人委托贷款
				WfiJoin.appl_type._setValue("0017");
			}else if(prd_id == "100063"){//企业委托贷款
				WfiJoin.appl_type._setValue("0016");
			}
		}else if('${context.menuId}'=="csgnqueryIqpLoanApp"){	
			WfiJoin.appl_type._setValue("0028");
		}else if('${context.menuId}'=="xphhqueryIqpLoanApp" || '${context.menuId}'=="jdxdbyhqueryIqpLoanApp" || '${context.menuId}'=="bdcqueryIqpLoanApp"){
			WfiJoin.appl_type._setValue("0021");//公司/小微业务申请(预付款类融资)  三种业务模式下 先票后货，阶段性担保+货押,保兑仓
		}else if('${context.menuId}'=="yztqueryIqpLoanApp"){
			WfiJoin.appl_type._setValue("0022");//公司/小微业务申请(银租通)
		}else{
			WfiJoin.appl_type._setValue(apply_type);
		}
		initWFSubmit(false);
	};


	//放入流程前先校验银票信息是否完整
	function checkAccpBillInfo(){
		var serno = IqpLoanAppList._obj.getSelectedData()[0].serno._getValue();
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
					getApplyTypeByPrdId();
				}else {
					alert(msg);
					return;
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

		var url="<emp:url action='checkAccpBillInfo.do'/>?serno="+serno;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null) 
	};


	function getApplyTypeByPrdId(){
		var prd_id = IqpLoanAppList._obj.getSelectedData()[0].prd_id._getValue();
		var url = '<emp:url action="getIqpApplyTypeByPrdId.do"/>?prdid='+prd_id;
		url = EMPTools.encodeURI(url);   
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
				var apply_type = jsonstr.apply_type;
				if(flag == "success"){
					doSubmitWF(apply_type);
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
		//var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
	};

	//放入流程前先校验票据信息是否完整
	function checkBillInfo(){
		var serno = IqpLoanAppList._obj.getSelectedData()[0].serno._getValue();
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
					getApplyTypeByPrdId();
				}else {
					alert(msg);
					return;
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

		var url="<emp:url action='checkBillInfo.do'/>?serno="+serno;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null) 
	}
	/*--user code begin--*/
	function doImageScan(){
		var data = IqpLoanAppList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('Scan24');	//业务资料扫描
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function doImageView(){
		var data = IqpLoanAppList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View25');	//业务资料查看
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function doImagePrint(){
		var data = IqpLoanAppList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('Print');	//业务资料条码打印
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = IqpLoanAppList._obj.getParamValue(['serno']);	//业务编号
		data['cus_id'] = IqpLoanAppList._obj.getParamValue(['cus_id']);	//客户码
		data['prd_id'] = IqpLoanAppList._obj.getParamValue(['prd_id']);	//业务品种
		data['prd_stage'] = 'YWSQ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};

	function doOnload(){
		//var size = IqpLoanAppList.size;
		//alert(size);
		//var serno = IqpLoanAppList[0].serno._getValue();
		//alert(serno);
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnload()">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="IqpLoanAppGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="IqpLoanApp.serno" label="业务流水号" />
			<emp:pop id="IqpLoanApp.cus_name" label="客户名称" buttonLabel="选择" url="queryAllCusPop.do?returnMethod=returnCus" />
			<emp:select id="IqpLoanApp.approve_status" label="申请状态" dictname="WF_APP_STATUS" /> 
	        <emp:pop id="IqpLoanApp.prd_id_displayname" label="产品名称" url="showPrdTreeDetails.do?bizline=BL100,BL200,BL300,BL400,BL500" returnMethod="returnPrdId" />
	        <emp:text id="IqpLoanApp.prd_id" label="产品编号"  hidden="true" />
	        <emp:select id="IqpLoanApp.biz_type" label="业务模式" dictname="STD_BIZ_TYPE" defvalue="${context.biz_type}" hidden="true" />
	        <emp:text id="IqpLoanApp.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />   
	 
	<div align="left">
		<emp:button id="getAddPage__IqpLoanApp" label="新增" op="add"/>
		<emp:button id="getUpdatePage__IqpLoanApp" label="修改" op="update"/>
		<emp:button id="deleteRecord__IqpLoanApp" label="删除" op="remove"/>
		<emp:button id="viewIqpLoanApp" label="查看" op="view"/>
		<emp:button id="subWF" label="提交" op="submit"/>
		<emp:button id="ImageScan" label="影像扫描" op="ImageScan"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="ImagePrint" label="条码打印" op="ImagePrint"/>
	</div>

	<emp:table icollName="IqpLoanAppList" pageMode="true" url="pageIqpLoanAppQuery.do" reqParams="biz_type=${context.biz_type}">
		<emp:text id="serno" label="业务流水号" />  		
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="prd_id_displayname" label="产品名称" />
		<emp:text id="assure_main" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" />
		<emp:text id="apply_cur_type" label="币种" dictname="STD_ZX_CUR_TYPE"/>
		<emp:text id="apply_amount" label="申请金额" dataType="Currency" />
		<emp:text id="apply_date" label="申请日期" />
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="input_id_displayname" label="登记人" />		
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:select id="approve_status" label="申请状态" dictname="WF_APP_STATUS"/>
		<emp:select id="prd_id" label="产品编号" hidden="true"/>
	</emp:table>   
	
</body>
</html>
</emp:page>
    