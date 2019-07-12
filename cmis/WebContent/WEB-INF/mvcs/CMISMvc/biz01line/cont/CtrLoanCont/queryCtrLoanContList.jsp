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
		CtrLoanCont._toForm(form);
		CtrLoanContList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdatePage__CtrLoanCont() {
		var paramStr = CtrLoanContList._obj.getParamStr(['cont_no','iqpFlowHis']);
		if (paramStr != null){
		   var data = CtrLoanContList._obj.getSelectedData();
		   var cont_status = data[0].cont_status._getValue();
		   var prd_id = CtrLoanContList._obj.getParamValue(['prd_id']);
		    if(cont_status =='100'){
		    	var url;
			  	if(prd_id==300021||prd_id==300020){
			  		url = '<emp:url action="getCtrLoanContForDiscUpdatePage.do"/>?menuId=${context.menuId}&op=update&cont=cont&flg=${context.flg}&'+paramStr+'&biz_type='+'<%=biz_type %>&op=update';
			  	}else{
			  		url = '<emp:url action="getCtrLoanContUpdatePage.do"/>?menuId=${context.menuId}&op=update&cont=cont&flg=${context.flg}&'+paramStr+'&biz_type='+'<%=biz_type %>&op=update';  
				}
			  	url = EMPTools.encodeURI(url);
			  	/** modified by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 begin **/
			  	//window.location = url;
				var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
				window.open(url,'newWindow_CtrLoanCont',param); 
				/** modified by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 end **/
		   } else {
			 alert('合同已签订');
		   }
		}else{
           alert("请先选择一条记录！");
		}
	};
	/** added by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 begin **/
	function refresh(){
		var direct = document.getElementById("emp_pq_jumpButton");
		    direct.click();
	};
	/** added by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 end **/
	function doViewCtrLoanCont() {
		var paramStr = CtrLoanContList._obj.getParamStr(['cont_no','iqpFlowHis']);
		var prd_id = CtrLoanContList._obj.getParamValue(['prd_id']); 
		if (paramStr != null) {
		    /** modified by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 begin **/
			var url;
			if(prd_id==300021||prd_id==300020){
				url = '<emp:url action="getCtrLoanContForDiscViewPage.do"/>?menuId=${context.menuId}&viewtype=out&op=view&cont=cont&flg=${context.flg}&'+paramStr+"&flag=ctrLoanCont&biz_type="+'<%=biz_type %>';
			}else{
				url = '<emp:url action="getCtrLoanContViewPage.do"/>?menuId=${context.menuId}&viewtype=out&op=view&cont=cont&flg=${context.flg}&'+paramStr+"&flag=ctrLoanCont&biz_type="+'<%=biz_type %>';  
			} 
			url = EMPTools.encodeURI(url);  
			//window.location = url;
			var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
			window.open(url,'newWindow_CtrLoanCont',param); 
			/** modified by yangzy 20140928 合同查询模块查看及签订功能改为弹出框查看模式 end **/
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doReset(){
		page.dataGroups.CtrLoanContGroup.reset();
	};

	function returnCus(data){
		CtrLoanCont.cus_id._setValue(data.cus_id._getValue());
		CtrLoanCont.cus_name._setValue(data.cus_name._getValue());
	};

	function returnPrdId(data){
		CtrLoanCont.prd_id._setValue(data.id);
		CtrLoanCont.prd_id_displayname._setValue(data.label); 
	};

	//作废
    function doRemoveCtrLoanCont(){
    	var paramStr = CtrLoanContList._obj.getParamStr(['cont_no']);
    	if (paramStr != null) {
    		var approve_status = CtrLoanContList._obj.getSelectedData()[0].cont_status._getValue();
    		if(approve_status == "100"){
    		if(confirm("是否确认要作废？")){
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
    						alert("作废成功!");
    						window.location.reload();
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
    			var url = '<emp:url action="removeCtrLoanContRecord.do"/>?'+paramStr;	
    			url = EMPTools.encodeURI(url);
    			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
    		}     
    		}else{
    			alert("只有状态为【未生效】的合同才可以作废！");
    		}
    	} else {
    		alert('请先选择一条记录！');
    	}
    };
	
	/*--user code begin--*/
	function doImageView(){
		var data = CtrLoanContList._obj.getSelectedData();
		if (data != null && data !=0) {
			ImageAction('View25');	//业务资料查看
		} else {
			alert('请先选择一条记录！');
		}		
	};
	function ImageAction(image_action){
		var data = new Array();
		data['serno'] = CtrLoanContList._obj.getParamValue(['serno']);	//业务编号
		data['cus_id'] = CtrLoanContList._obj.getParamValue(['cus_id']);	//客户码
		data['prd_id'] = CtrLoanContList._obj.getParamValue(['prd_id']);	//业务品种
		data['prd_stage'] = 'YWSQ'; //业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = image_action	//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*--user code end--*/
	/*****2019-03-01 jiangcuihua 附件上传  start******/
	function doUpload(){
		var paramStr = CtrLoanContList._obj.getParamValue(['serno']);
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

	<emp:gridLayout id="CtrLoanContGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="CtrLoanCont.cont_no" label="合同编号" />
			<emp:pop id="CtrLoanCont.cus_name" label="客户名称"  buttonLabel="选择" url="queryAllCusPop.do?returnMethod=returnCus" popParam="height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"/>
			<emp:select id="CtrLoanCont.cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE"/>
			<emp:pop id="CtrLoanCont.prd_id_displayname" label="产品名称" url="showPrdTreeDetails.do?bizline=BL100,BL200,BL300,BL400,BL500" returnMethod="returnPrdId" />
	        <emp:text id="CtrLoanCont.prd_id" label="产品编号"  hidden="true" />
			<emp:select id="CtrLoanCont.biz_type" label="业务模式" dictname="STD_BIZ_TYPE" defvalue="${context.biz_type}" hidden="true" />
			<emp:text id="CtrLoanCont.cus_id" label="客户码" hidden="true"/>
	</emp:gridLayout>  
		
	<jsp:include page="/queryInclude.jsp" flush="true" />
	<div align="left">
		<emp:button id="getUpdatePage__CtrLoanCont" label="签订" op="update" />
		<emp:button id="viewCtrLoanCont" label="查看" op="view"/>
		<emp:button id="removeCtrLoanCont" label="作废" op="remove"/>
		<emp:button id="ImageView" label="影像查看" op="ImageView"/>
		<emp:button id="upload" label="附件"/>
	</div>

	<emp:table icollName="CtrLoanContList" pageMode="true" url="pageCtrLoanContQuery.do" reqParams="biz_type=${context.biz_type}">
		<emp:text id="serno" label="业务编号" hidden="true"/>
		<emp:text id="cont_no" label="合同编号" />
		<emp:text id="cn_cont_no" label="中文合同编号" hidden="true"/>
		<emp:text id="cus_id" label="客户码" />
		<emp:text id="cus_id_displayname" label="客户名称" />
		<emp:text id="prd_id" label="产品编号" hidden="true"/>
		<emp:text id="prd_id_displayname" label="产品名称" />
		<emp:text id="assure_main" label="担保方式" dictname="STD_ZB_ASSURE_MEANS" />
		<emp:text id="cont_cur_type" label="合同币种" dictname="STD_ZX_CUR_TYPE" />
		<emp:text id="cont_amt" label="合同金额" dataType="Currency"/>
		<emp:text id="cont_balance" label="合同余额" dataType="Currency"/>
		<emp:text id="manager_br_id_displayname" label="管理机构" />
		<emp:text id="input_id_displayname" label="登记人" />
		<emp:text id="input_id" label="登记人" hidden="true"/>
		<emp:text id="cont_status" label="合同状态" dictname="STD_ZB_CTRLOANCONT_TYPE"/>
		<emp:text id="iqpFlowHis" label="业务审批标识" hidden="true" defvalue="have"/>
	</emp:table>

</body>
</html>
</emp:page>