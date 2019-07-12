<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String biz_type = "";
	String menuId = "";
	if(context.containsKey("biz_type")){
		biz_type = (String)context.getDataValue("biz_type");
	} 
	if(context.containsKey("menuId")){
		menuId = (String)context.getDataValue("menuId");
	} 
%> 
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		AccLoan._toForm(form);
		AccLoanList._obj.ajaxQuery(null,form);
	};
	
	function doViewAccLoan() {
		var paramStr = AccLoanList._obj.getParamStr(['bill_no']);
		if (paramStr != null) {
			var url;
			if("${context.flag}" == "overdueAcc"){
				url = '<emp:url action="getAccViewPage.do"/>?'+paramStr+'&isHaveButton=not&op=view&flag=${context.flag}&biz_type='+'<%=biz_type%>';
			}else {
				url = '<emp:url action="getAccLoanViewPage.do"/>?'+paramStr+'&op=view&flag=${context.flag}&biz_type='+'<%=biz_type%>';
			}
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	
	function doReset(){
		page.dataGroups.AccLoanGroup.reset();
	};
	function returnCus(data){
		AccLoan.cus_id._setValue(data.cus_id._getValue());
		AccLoan.cus_name._setValue(data.cus_name._getValue());
	};

	function returnPrdId(data){
		AccLoan.prd_id._setValue(data.id);
		AccLoan.prd_id_displayname._setValue(data.label); 
	};

	function doWriterRepayInfo(){
		var paramStr = AccLoanList._obj.getParamStr(['bill_no','cont_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getAccLoanViewPage.do"/>?'+paramStr+'&op=update&flag=${context.flag}&biz_type='+'<%=biz_type%>';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	/**add by lisj 2015-1-5 需求编号：【XD141204082】关于信托台账改造需求调整  begin**/
	function doWriterRepayInfo4Maker(){
		var paramStr = AccLoanList._obj.getParamStr(['bill_no','cont_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getAccLoanViewPage.do"/>?'+paramStr+'&op=update4m&flag=${context.flag}&biz_type='+'<%=biz_type%>';
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	/**add by lisj 2015-1-5 需求编号：【XD141204082】关于信托台账改造需求调整  end**/
	function doImageView(){
		var data = AccLoanList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View25');	//业务资料查看
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = AccLoanList._obj.getParamValue(['fount_serno']);	//业务编号
		data['cus_id'] = AccLoanList._obj.getParamValue(['cus_id']);	//客户码
		data['prd_id'] = AccLoanList._obj.getParamValue(['prd_id']);	//业务品种
		data['prd_stage'] = 'DHTZ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
    
	function onload(){
		var options = AccLoan.acc_status._obj.element.options;
		//台账状态删除'10':'信用证闭卷', '11':'信用证撤销', '2':'正回购卖出', '3':'逆回购买入', '4':'逆回购到期', '5':'正回购到期'
        if('<%=menuId%>' == "isTrustAccLoanList" || '<%=menuId%>' == "promissoryAccLoanList" || '<%=menuId%>' == "adviceAccLoanList" || '<%=menuId%>' == "proveAccLoanList" || '<%=menuId%>' == "guarantAccLoanList" || '<%=menuId%>' == "AccLoanList"){
        	for(var i=options.length-1;i>=0;i--){
    		    if(options[i].value=="10" || options[i].value=="11" || options[i].value=="2" || options[i].value=="3" || options[i].value=="4" || options[i].value=="5"){
    		    	options.remove(i);
    			}
    		} 
    	//台账状态删除 '2':'正回购卖出', '3':'逆回购买入', '4':'逆回购到期', '5':'正回购到期'
        }else if('<%=menuId%>' == "ctrLimitAccLoanList"){
        	for(var i=options.length-1;i>=0;i--){
    		    if(options[i].value=="2" || options[i].value=="3" || options[i].value=="4" || options[i].value=="5"){
    		    	options.remove(i);
    			}
    		} 
    	//台账状态删除'10':'信用证闭卷', '11':'信用证撤销'
        }else if('<%=menuId%>' == "accAssetstrsfList"){
        	for(var i=options.length-1;i>=0;i--){
    		    if(options[i].value=="10" || options[i].value=="11"){
    		    	options.remove(i);
    			}
    		} 
        }else if('<%=menuId%>' == "csgnLoanAccLoanList"){
        	for(var i=options.length-1;i>=0;i--){
    		    if(options[i].value=="10" || options[i].value=="11" || options[i].value=="2" || options[i].value=="3" || options[i].value=="4" || options[i].value=="5" || options[i].value=="6" || options[i].value=="7"){
    		    	options.remove(i);
    			}
    		} 
        }
	};

	function doWriteOff(){
		var paramStr = AccLoanList._obj.getParamStr(['bill_no']);
		if (paramStr != null) { 
			var acc_status = AccLoanList._obj.getSelectedData()[0].acc_status._getValue();
			if(acc_status == "1"){ //台账状态为正常状态
			if(confirm("是否确认要核销？")){
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr1 define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						var amt = jsonstr.amt;
						if(flag == "success"){
							alert("核销成功!");
							window.location.reload();
						}else {
							alert("贷款本金还有["+amt+"]未归还!");
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
				var url = '<emp:url action="writeOffAccLoan.do"/>?'+paramStr;	
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
			}     
			}else{
				alert("只有状态为【正常】的台账才能核销！");
			}
		} else {
			alert('请先选择一条记录！');
		}
    };

    /** 导出excel **/
	function doExcelSDuty(){
		var form = document.getElementById("queryForm");
		AccLoan._toForm(form);
		form.submit();
	}
	/*--user code end--*/
	/*****2019-03-01 jiangcuihua 附件上传  start******/
	function doUpload(){
		var paramStr = AccLoanList._obj.getParamValue(['bill_no']);
		if (paramStr!=null) {
			var url = '<emp:url action="getUploadInfoPage.do"/>?file_type=08&serno='+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	}
	/*****2019-03-01 jiangcuihua 附件上传  end******/
</script>
</head>
<body class="page_content" onload="onload()">
	<emp:form  method="POST" action="accLoanFlagExpBatchToExcel.do?flag=${context.flag}" id="queryForm">
		<emp:gridLayout id="AccLoanGroup" title="输入查询条件" maxColumn="3">
	        <emp:text id="AccLoan.bill_no" label="借据编号" />	 
	        <emp:text id="AccLoan.cont_no" label="合同编号" />       
			<emp:pop id="AccLoan.cus_name" label="客户名称"  buttonLabel="选择" url="queryAllCusPop.do?returnMethod=returnCus" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
			<%if(!"guarantAccLoanList".equals(menuId) && !"csgnLoanAccLoanList".equals(menuId) && !"InterFactAccLoanList".equals(menuId) && !"proveAccLoanList".equals(menuId) && !"promissoryAccLoanList".equals(menuId) && !"adviceAccLoanList".equals(menuId)){ %>
			<emp:pop id="AccLoan.prd_id_displayname" label="产品名称" url="showPrdTreeDetails.do?bizline=BL100,BL200,BL300,BL400,BL500" returnMethod="returnPrdId" />
	        <%} %>
	        <emp:select id="AccLoan.five_class" label="五级分类" dictname="STD_ZB_FIVE_SORT"/>
	        <emp:select id="AccLoan.acc_status" label="台账状态" dictname="STD_ZB_ACC_TYPE"/>	        
	        <emp:text id="AccLoan.cus_id" label="客户码"  hidden="true" />
	        <emp:text id="AccLoan.prd_id" label="产品编号"  hidden="true" />
		</emp:gridLayout> 
	</emp:form>
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left">
		<emp:button id="viewAccLoan" label="查看" op="view"/>
		<% if("isTrustAccLoanList".equals(menuId)){%>
		<emp:button id="writerRepayInfo" label="登记还款信息" op="payBack" mousedownCss="button100" mouseoutCss="button100" mouseoverCss="button100" mouseupCss="button100"/>
		<emp:button id="writerRepayInfo4Maker" label="登记还款信息" op="payBack4Maker" mousedownCss="button100" mouseoutCss="button100" mouseoverCss="button100" mouseupCss="button100"/>
		<emp:button id="writeOff" label="核销" op="remove"/>
		<%}%>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="excelSDuty" label="导出" op="putout"/>
		<emp:button id="upload" label="附件"/>
	</div>
    
	<emp:table icollName="AccLoanList" pageMode="true" url="pageAccLoanList.do" reqParams="flag=${context.flag}">
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="fount_serno" label="业务申请编号" hidden="true" />
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="belg_line" label="客户条线" dictname="STD_ZB_BUSILINE" hidden="true"/>
		<emp:text id="prd_id" label="产品编号" hidden="true"/>
		<emp:text id="prd_id_displayname" label="产品名称" />
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE"/>
		<emp:text id="loan_amt" label="贷款金额" dataType="Currency"/>
		<emp:text id="loan_balance" label="贷款余额" dataType="Currency"/>		
		<emp:text id="distr_date" label="起始日期" />
		<emp:text id="end_date" label="到期日期" />
		<emp:text id="five_class" label="五级分类" dictname="STD_ZB_FIVE_SORT"/>
		<%if("csgnLoanAccLoanList".equals(menuId)){ %>
		<emp:text id="principal_loan_typ" label="委托贷款种类" dictname="STD_ZB_COMMISS_TYPE"/>
		<%} %>
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="acc_status" label="台账状态" dictname="STD_ZB_ACC_TYPE"/>
	</emp:table>
	
</body>
</html>
</emp:page>
    