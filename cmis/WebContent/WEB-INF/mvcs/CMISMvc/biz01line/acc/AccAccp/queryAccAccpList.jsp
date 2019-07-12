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
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		AccAccp._toForm(form);
		AccAccpList._obj.ajaxQuery(null,form);
	};
	
	function doViewAccAccp() {   
		var paramStr = AccAccpList._obj.getParamStr(['bill_no']);
		if (paramStr != null) {
			var url = '<emp:url action="getAccAccpViewPage.do"/>?'+paramStr+'&biz_type='+'<%=biz_type%>';
			url = EMPTools.encodeURI(url); 
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}         
	};
	
	
	function doReset(){
		page.dataGroups.AccAccpGroup.reset();
	};
	function returnCus(data){
		AccAccp.daorg_cusid._setValue(data.cus_id._getValue());
		AccAccp.daorg_cusid_displayname._setValue(data.cus_name._getValue());
	};
	/*--user code begin--*/
	function doImageView(){
		var data = AccAccpList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View25');	//业务资料查看
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = AccAccpList._obj.getParamValue(['fount_serno']);	//业务编号
		data['cus_id'] = AccAccpList._obj.getParamValue(['daorg_cusid']);	//客户码
		data['prd_id'] = AccAccpList._obj.getParamValue(['prd_id']);	//业务品种
		data['prd_stage'] = 'DHTZ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
   //台账状态删除'10':'信用证闭卷', '11':'信用证撤销', '2':'正回购卖出', '3':'逆回购买入', '4':'逆回购到期', '5':'正回购到期'
	function onload(){
		var options = AccAccp.accp_status._obj.element.options;
	    for(var i=options.length-1;i>=0;i--){
		    if(options[i].value=="10" || options[i].value=="11" || options[i].value=="2" || options[i].value=="3" || options[i].value=="4" || options[i].value=="5"){
		    	options.remove(i);
			}
		}
    };

    /**导出excel**/
    function doExcelSDuty(){
		var form = document.getElementById("queryForm");
		AccAccp._toForm(form);
		form.submit();
	}
    
	/*--user code end--*/
	/*****2019-03-01 jiangcuihua 附件上传  start******/
	function doUpload(){
		var paramStr = AccAccpList._obj.getParamValue(['fount_serno']);
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
	<emp:form  method="POST" action="accAccpExpBatchToExcel.do" id="queryForm">	
	<emp:gridLayout id="AccAccpGroup" title="输入查询条件" maxColumn="3">
		<emp:text id="AccAccp.bill_no" label="借据编号" />
		<emp:text id="AccAccp.cont_no" label="合同编号" />
	    <emp:select id="AccAccp.accp_status" label="台账状态" dictname="STD_ZB_ACC_TYPE"/>
		<emp:pop id="AccAccp.daorg_cusid_displayname" label="客户名称"  buttonLabel="选择" url="queryAllCusPop.do?returnMethod=returnCus" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
		<emp:select id="AccAccp.five_class" label="五级分类" dictname="STD_ZB_FIVE_SORT"/>
		<emp:text id="AccAccp.daorg_cusid" label="客户码"  hidden="true" />
	</emp:gridLayout>  
	
	</emp:form>
	
	
	
	<jsp:include page="/queryInclude.jsp" flush="true" />
	
	<div align="left" style="padding-top:10px;border-top:1px solid #c2c2c2;">
		<emp:button id="viewAccAccp" label="查看" op="view"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="excelSDuty" label="导出" op="putout"/>
		<emp:button id="upload" label="附件"/> 
	</div>

	<emp:table icollName="AccAccpList" pageMode="true" url="pageAccAccpQuery.do" reqParams="biz_type=${context.biz_type}">
		<emp:text id="bill_no" label="借据编号" />
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="fount_serno" label="业务编号" hidden="true"/>
		<emp:text id="prd_id" label="产品编号" hidden="true"/>
		<emp:text id="daorg_cusid" label="客户码" />
		<emp:text id="daorg_cusid_displayname" label="客户名称" />
		<emp:text id="belg_line" label="客户条线" dictname="STD_ZB_BUSILINE" hidden="true"/>
		<emp:text id="cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="drft_amt" label="票面金额" dataType="Currency" />
		<emp:text id="isse_date" label="出票日期" />
		<emp:text id="porder_end_date" label="到期日期" />
		<emp:text id="five_class" label="五级分类" dictname="STD_ZB_FIVE_SORT"/>
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="accp_status" label="台帐状态" dictname="STD_ZB_ACC_TYPE"/>    
	</emp:table>
	
</body>
</html>
</emp:page>
    