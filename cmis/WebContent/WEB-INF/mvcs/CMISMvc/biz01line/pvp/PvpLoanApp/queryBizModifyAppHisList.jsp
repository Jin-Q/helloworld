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
	function doGetBizModifyRelViewPage() {
		var paramStr = BizModifyAppHisList._obj.getParamStr(['serno','cus_id','modify_rel_serno']);
		var biz_cate = BizModifyAppHisList._obj.getParamValue(['biz_cate']);
		if (paramStr != null) {
			if(biz_cate == "0011" || biz_cate == "0012"){//普通贷款包括：贷款类/银票（不含电票）/保函
				var url = '<emp:url action="getBizModifyViewPage.do"/>?'+paramStr+"&op=his";
				url = EMPTools.encodeURI(url);
				window.location = url;
			}else if(biz_cate =="016"){
				var url = '<emp:url action="getBizModifyView4IEAPage.do"/>?'+paramStr+"&op=his";
				url = EMPTools.encodeURI(url);
				window.location = url;
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		PvpBizModify._toForm(form);
		BizModifyAppHisList._obj.ajaxQuery(null,form);
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

	function doCheckIsModifyInfo(){
		var paramStr = BizModifyAppHisList._obj.getParamStr(['modify_rel_serno','serno','cont_no','biz_cate','approve_status','prd_id']);
		if (paramStr != null) {
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
						doGetBizModifyHisQueryPage();
					}else if(flag == "limited"){
						alert("该笔申请信息不存在修改历史信息!"); 
					}else if(flag =="forbidden"){
						alert("审批状态为【否决】的申请信息不存在修改历史信息!"); 
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
		}else{
			alert('请先选择一条记录！');
		}
	};
	
	function doGetBizModifyHisQueryPage(){
		var paramStr = BizModifyAppHisList._obj.getParamStr(['serno','cont_no','prd_id','modify_rel_serno']);
		var biz_cate = BizModifyAppHisList._obj.getParamValue(['biz_cate']);
		if (paramStr != null) {
			if(biz_cate == "0011" || biz_cate == "0012"){//普通贷款包括：贷款类/银票（不含电票）/保函
				var url = '<emp:url action="getModifyHisViewPage.do"/>?'+paramStr+"&op=his";
				url = EMPTools.encodeURI(url);
				window.open(url,'newwindow','height=538,width=1024,top=70,left=50,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=yes');
			}else if(biz_cate =="016"){
				var url = '<emp:url action="getModifyHisViewPage.do"/>?'+paramStr+"&op=his4iea";
				url = EMPTools.encodeURI(url);
				window.open(url,'newwindow','height=538,width=1080,top=70,left=50,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
			}
		} else {
			alert('请先选择一条记录！');
		}
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
		<emp:button id="getBizModifyRelViewPage" label="查看" op="view"/>
		<emp:button id="checkIsModifyInfo" label="修改历史查询" op="viewHis"/>
	</div>

	<emp:table icollName="BizModifyAppHisList" pageMode="true" url="pageBizModifyAppHisQuery.do">
		<emp:text id="serno" label="业务流水号" hidden="true"/>
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
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE" hidden="true"/>
		<emp:text id="biz_cate" label="业务类型" dictname="ZB_BIZ_CATE"/>
		<emp:text id="approve_status" label="审批状态" dictname="WF_APP_STATUS"/>
		<emp:text id="update_time"	label="修改发起时间" />
		<emp:text id="modify_rel_serno" label="打回业务关联表流水号" hidden="true"/>
	</emp:table>  
	
</body>
</html>
</emp:page>