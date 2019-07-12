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

<script type="text/javascript">
    function onload(){
    	PvpAuthorize.status._setValue('02');
	    doQuery();
    };

	function doQuery(){
		var form = document.getElementById('queryForm');
		PvpAuthorize._toForm(form);
		PvpAuthorizeList._obj.ajaxQuery(null,form);
	};
	
	function doViewPvpAuthorize() {
		var paramStr = PvpAuthorizeList._obj.getParamStr(['tran_serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getPvpAuthorizeViewPage.do"/>?'+paramStr+'&flag=authorizeHistory&biz_type='+'<%=biz_type %>';		
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');  
		}
	}; 
	
	function doReset(){
		page.dataGroups.PvpAuthorizeGroup.reset();
	};

	function returnCus(data){
		PvpAuthorize.cus_id._setValue(data.cus_id._getValue());
		PvpAuthorize.cus_name._setValue(data.cus_name._getValue());
	};

	function returnPrdId(data){
		PvpAuthorize.prd_id._setValue(data.id);
		PvpAuthorize.prd_id_displayname._setValue(data.label); 
	};

	function doCancelAuthorize(){
		var paramStr = PvpAuthorizeList._obj.getParamStr(['tran_serno']);
		if (paramStr != null) {
			//added by yangzy 2015/04/23 需求：XD150325024，集中作业扫描岗权限改造 start
    		//modified by yangzy 2015/06/24 出账授权权限改造 start
    		var currentUserId = '${context.currentUserId}';
    		var rolesList = '${context.roleNoList}';
    		var manager_id = PvpAuthorizeList._obj.getParamValue('manager_id');
    		if(rolesList.indexOf("3003")>0 && manager_id!=null && manager_id!='' && currentUserId != manager_id){
    			alert('非当前客户主管客户经理，操作失败！');
    			return;
    		}
    		//modified by yangzy 2015/06/24 出账授权权限改造 end
    		//added by yangzy 2015/04/23 需求：XD150325024，集中作业扫描岗权限改造 end	
			if(confirm("是否确认要撤销？")){
				var status = PvpAuthorizeList._obj.getParamValue(['status']);
				//'00':'未授权', '01':'授权失败', '02':'已授权', '03':'授权已撤销', '04':'授权已确认', '05':'等待通知'
				if(status!='02' &&  status!='01' &&  status!='00'){
					//alert('非发送成功的授权不能撤销！');
					alert('状态为【授权已撤销】【授权已确认】【等待通知】的授权不能撤销！');
					return;
				}else{
					var tran_date = PvpAuthorizeList._obj.getParamValue(['tran_date']);
					var openday = '${context.OPENDAY}';
					if(tran_date!=openday){
						alert('授权撤销只能操作当天发生的交易！');
						return;
					}
					
					var prd_id = PvpAuthorizeList._obj.getParamValue(['prd_id']);
					if(prd_id=='700020'||prd_id=='700021'||prd_id=='400020'||prd_id=='500032'){
						alert('国内信用证、进口开证、外汇保函、提货担保不能进行撤销操作！');
						return;
					}else{
						if(prd_id=='200024'||prd_id=='300020'||prd_id=='300021'||prd_id=='300022'||prd_id=='300023'||prd_id=='300024'){
							if(confirm("票据类授权撤销，会将所有相同授权编号下的所有交易全部撤销，是否继续撤销？")){
								cancelAuthorize();
							}
						}else if(prd_id=='600020'||prd_id=='600021'||prd_id=='600022'){
							if(confirm("资产类授权撤销，会将所有相同授权编号下的所有交易全部撤销，是否继续撤销？")){
								cancelAuthorize();
							}
						}else{
							cancelAuthorize();
						}
					}
				}
			}
		} else {
			alert('请先选择一条记录！');
		}
	}

	function cancelAuthorize(){
		var paramStr = PvpAuthorizeList._obj.getParamStr(['tran_serno']);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var retMsg = jsonstr.retMsg;
				if(flag == "success"){
					alert("撤销成功！");
				}else {
					alert(retMsg);
				}
				window.location.reload();
			}
		};
		var handleFailure = function(o){
			alert("异步请求出错！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var url = '<emp:url action="cancelPvpAuthorize.do"/>?'+paramStr;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
	}
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onload()">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<emp:gridLayout id="PvpAuthorizeGroup" title="输入查询条件" maxColumn="3">
	        <emp:text id="PvpAuthorize.serno" label="出账流水号" />			
			<emp:text id="PvpAuthorize.cus_name" label="客户名称"  />
			<emp:select id="PvpAuthorize.status" label="授权状态" dictname="STD_AUTHORIZE_STATUS"/>
			<emp:text id="PvpAuthorize.cont_no" label="合同编号" />
			<emp:pop id="PvpAuthorize.prd_id_displayname" label="产品名称" url="showPrdTreeDetails.do?bizline=BL100,BL200,BL300,BL400,BL500" returnMethod="returnPrdId" />
	        <emp:date id="PvpAuthorize.tran_date" label="交易日期"  />
	        <emp:text id="PvpAuthorize.cus_id" label="客户码"  hidden="true" />	        
	        <emp:text id="PvpAuthorize.prd_id" label="产品编号"  hidden="true" />
	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="cancelAuthorize" label="撤销" op="update"/>
		<emp:button id="viewPvpAuthorize" label="查看" op="view"/>
	</div>

	<emp:table icollName="PvpAuthorizeList" pageMode="true" url="pagePvpAuthorizeHistoryQuery.do" reqParams="biz_type=${context.biz_type}">
	  	<emp:text id="tran_serno" hidden="true" label="交易流水号" />
	    <emp:text id="authorize_no" hidden="true" label="授权编号" />	    
		<emp:text id="bill_no" hidden="true"  label="借据编码" />
	    <emp:text id="serno" label="出账流水号" />
	    <emp:text id="cont_no" label="合同编号" />
		<emp:text id="cus_name" label="客户名称/项目名称" />
		<emp:text id="prd_id" label="产品编号" hidden="true"/>
		<emp:text id="prd_id_displayname" label="产品名称" />
		<emp:text id="tran_id" label="交易码" hidden="true"/>
		<emp:text id="cur_type" label="币种"  dictname="STD_ZX_CUR_TYPE"/>
		<emp:text id="tran_amt" label="交易金额" dataType="Currency"/>		
		<emp:text id="tran_date" label="交易日期" hidden="false"/>
		<emp:text id="send_times" label="发送次数" hidden="false"/>		
		<emp:text id="return_code" label="返回编码" hidden="true"/>
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="in_acct_br_id_displayname" label="入账机构" /> 
		<emp:text id="status" label="授权状态" dictname="STD_AUTHORIZE_STATUS"/>
		<!-- modified by yangzy 2015/04/23 需求：XD150325024，集中作业扫描岗权限改造 start -->
		<emp:text id="manager_id" label="责任人" hidden="true"/>
		<!-- modified by yangzy 2015/04/23 需求：XD150325024，集中作业扫描岗权限改造 end -->
	</emp:table>
	
</body>
</html>
</emp:page>
    