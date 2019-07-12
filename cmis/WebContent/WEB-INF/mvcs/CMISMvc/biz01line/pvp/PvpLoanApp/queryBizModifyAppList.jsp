<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/platform/workflow/include4WF.jsp" flush="true"/> 
<script type="text/javascript">
	//新增操作
	function doGetBiztModifyRelAddPage(){
		var url = '<emp:url action="getBizModifyAddPage.do"/>';
		url = EMPTools.encodeURI(url);
		window.location = url;
	};
	
	//修改操作
	function doGetBiztModifyRelUpdatePage() {
		var paramStr = BizModifyAppList._obj.getParamStr(['serno','cont_no','modify_rel_serno']);
		var biz_cate = BizModifyAppList._obj.getParamValue(['biz_cate']);
		if (paramStr != null) {
			var approve_status = BizModifyAppList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status == "000" || approve_status == "992" || approve_status == "993"){
			   var url;
				if(biz_cate == "0011" || biz_cate == "0012"){//普通贷款包括：贷款类/银票（不含电票）/保函
					url = '<emp:url action="getBizModifyUpdatePage.do"/>?'+paramStr;
				}else if(biz_cate =="016"){//贷款展期
					url = '<emp:url action="getBizModifyUpdate4IEAPage.do"/>?'+paramStr;
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

	//删除操作
	function doDeleteBiztModifyRelRecord() {
		var paramStr = BizModifyAppList._obj.getParamStr(['modify_rel_serno','cont_no','serno','biz_cate','prd_id']);
		if (paramStr != null) { 
			var approve_status = BizModifyAppList._obj.getSelectedData()[0].approve_status._getValue();
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
						var flag = jsonstr.del;
						if(flag == "success"){
							alert("删除成功!");
							window.location.reload();
						}else {
							alert("删除操作执行异常!"); 
						}
					}
				};
				var handleFailure = function(o){
					alert("异步调用请求出错！");	
				};
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				};
				var url = '<emp:url action="deleteBizModifyAppRecord.do"/>?'+paramStr;	
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

	function doGetBizModifyRelViewPage() {
		var paramStr = BizModifyAppList._obj.getParamStr(['serno','cus_id','modify_rel_serno']);
		var biz_cate = BizModifyAppList._obj.getParamValue(['biz_cate']);
		if (paramStr != null) {
			if(biz_cate == "0011" || biz_cate == "0012"){//普通贷款包括：贷款类/银票（不含电票）/保函
				var url = '<emp:url action="getBizModifyViewPage.do"/>?'+paramStr+"&op=view";
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else if(biz_cate =="016"){
				var url = '<emp:url action="getBizModifyView4IEAPage.do"/>?'+paramStr+"&op=view";
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};

   //提交流程
	function doSubWF(){ 
	 	var paramStr = BizModifyAppList._obj.getParamStr(['modify_rel_serno']);
		if (paramStr != null) {
			var approve_status = BizModifyAppList._obj.getSelectedData()[0].approve_status._getValue();
			if(approve_status == "000" || approve_status == "992" || approve_status == "993"){
				//校验信息是否有改动
				checkIsModifyInfo();
			}else{
				alert("非【待发起】、【打回】、【追回】状态的记录不能进行修改操作！");
			}
		}else{
			alert('请先选择一条记录！');
		}
	};

	function checkIsModifyInfo(){
		var paramStr = BizModifyAppList._obj.getParamStr(['modify_rel_serno','serno','cont_no','biz_cate','prd_id','approve_status']);
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
					var biz_cate = BizModifyAppList._obj.getSelectedData()[0].biz_cate._getValue();
					if(biz_cate!="016"){
						if(!interRisk()){
							return false;
						}
					}
					doSubmitWF();
				}else if(flag == "limited"){
					alert("该修改信息还没保存,请保存后再进行提交!"); 
				}else if(flag =="riskOANone"){
					alert("该修改信息的风险敞口金额还未保存,请检查!"); 
				}else{
					alert("检验功能异常,请联系管理员!");
				}
			}
		};
		var handleFailure = function(o){
			alert("异步调用请求出错！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var url = '<emp:url action="checkIsModifyInfo.do"/>?'+paramStr;	
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
	};

	//风险拦截(包括：还款计划登记校验/保证金敞口金额校验)
    function interRisk(){
	   var modify_rel_serno = BizModifyAppList._obj.getSelectedData()[0].modify_rel_serno._getValue();
	   var _applType="";
	   var _modelId="PvpBizModifyRel";
	   var _pkVal=modify_rel_serno;
	   var _preventIdLst="FFFA276922B722B7D08CAF5ED42D3BE1";
	   var _urlPrv = "<emp:url action='procRiskInspect.do'/>&appltype="+_applType+"&pkVal=" + _pkVal + "&modelId=" + _modelId + "&pvId=" + _preventIdLst +"&timestamp=" + new Date();
       var _retObj = window.showModalDialog(_urlPrv,"","dialogHeight=500px;dialogWidth=850px;");
       if(!_retObj || _retObj == '2' || _retObj == '5'){
		   if( _retObj == '5'){
			   alert("执行风险拦截有错误，请检查！");
		   } 
		   return false;
	   }else{

		   return true;
	   }
	
   }
	
   function doSubmitWF(){
	   if(confirm("确认保存信息无误，是否提交审批流程？")){
			var modify_rel_serno = BizModifyAppList._obj.getSelectedData()[0].modify_rel_serno._getValue();
			var cus_id = BizModifyAppList._obj.getSelectedData()[0].cus_id._getValue();
			var cus_name = BizModifyAppList._obj.getSelectedData()[0].cus_id_displayname._getValue();
			var approve_status = BizModifyAppList._obj.getSelectedData()[0].approve_status._getValue();
			var prd_id = BizModifyAppList._obj.getSelectedData()[0].prd_id._getValue();
			var biz_cate = BizModifyAppList._obj.getSelectedData()[0].biz_cate._getValue();
			WfiJoin.table_name._setValue("PvpBizModifyRel");
			WfiJoin.pk_col._setValue("modify_rel_serno");
			WfiJoin.pk_value._setValue(modify_rel_serno);
			WfiJoin.cus_id._setValue(cus_id);
			WfiJoin.cus_name._setValue(cus_name);
			WfiJoin.prd_pk._setValue(prd_id);
			WfiJoin.prd_name._setValue(BizModifyAppList._obj.getSelectedData()[0].prd_id_displayname._getValue());
			WfiJoin.amt._setValue(BizModifyAppList._obj.getSelectedData()[0].cont_amt._getValue());
			WfiJoin.wfi_status._setValue(approve_status);
			WfiJoin.status_name._setValue("approve_status");
			if(biz_cate =="016"){
				WfiJoin.appl_type._setValue("801");//出账打回业务修改（展期）
			}else {
				WfiJoin.appl_type._setValue("800");//出账打回业务修改
			}
			initWFSubmit(false);
	   }
	};
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		PvpBizModify._toForm(form);
		BizModifyAppList._obj.ajaxQuery(null,form);
	};

	function doReset(){
		page.dataGroups.PvpBizModifyGroup.reset();
	};

	function returnPrdId(data){
		PvpBizModify.prd_id._setValue(data.id);
		PvpBizModify.prd_id_displayname._setValue(data.label); 
	};

	function returnCus(data){
		PvpBizModify.cus_id._setValue(data.cus_id._getValue());
		PvpBizModify.cus_name._setValue(data.cus_name._getValue());
	};
	
</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PvpBizModifyGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="PvpBizModify.cont_no" label="合同编号" />
			<emp:pop id="PvpBizModify.cus_name" label="客户名称" buttonLabel="选择" url="queryAllCusPop.do?returnMethod=returnCus" />
			<emp:select id="PvpBizModify.approve_status" label="申请状态" dictname="WF_APP_STATUS" /> 
	        <emp:pop id="PvpBizModify.prd_id_displayname" label="产品名称" url="showPrdTreeDetails.do?bizline=BL100,BL200,BL300,BL400,BL500" returnMethod="returnPrdId" />
	        <emp:text id="PvpBizModify.prd_id" label="产品编号"  hidden="true" />
	        <emp:text id="PvpBizModify.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>
	<jsp:include page="/queryInclude.jsp" flush="true" />   
	 
	<div align="left">
		<emp:button id="getBiztModifyRelAddPage" label="新增" op="add"/>
		<emp:button id="getBiztModifyRelUpdatePage" label="修改" op="update"/>
		<emp:button id="deleteBiztModifyRelRecord" label="删除" op="remove"/>
		<emp:button id="getBizModifyRelViewPage" label="查看" op="view"/>
		<emp:button id="subWF" label="提交" op="submit"/>
	</div>

	<emp:table icollName="BizModifyAppList" pageMode="true" url="pageBizModifyAppQuery.do" >
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="cn_cont_no" label="中文合同编号" hidden="true"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="prd_id" label="产品编号" hidden="true"/>
		<emp:text id="prd_id_displayname" label="产品名称" />
		<emp:text id="assure_main" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" />
		<emp:text id="cont_cur_type" label="业务币种" dictname="STD_ZX_CUR_TYPE" hidden="true"/>
		<emp:text id="cont_amt" label="业务金额" dataType="Currency" />
		<emp:text id="cont_balance" label="业务余额" dataType="Currency" hidden="true"/>
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="manager_br_id" label="管理机构" hidden="true"/>
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE" hidden="true"/>
		<emp:text id="biz_cate" label="业务类型" dictname="ZB_BIZ_CATE"/>
		<emp:text id="approve_status" label="审批状态" dictname="WF_APP_STATUS"/>
		<emp:text id="modify_rel_serno" label="打回业务关联表流水号" hidden="true"/>
	</emp:table>  
	
</body>
</html>
</emp:page>
    