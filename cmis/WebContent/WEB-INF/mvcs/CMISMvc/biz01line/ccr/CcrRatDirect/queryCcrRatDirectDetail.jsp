<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%> <%@page import="com.ecc.emp.core.EMPConstance"%>
<%
    Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	request.setAttribute("canwrite","");	
	String smallFlag = (String)request.getAttribute("smallFlag");
	String flow = "";//流程查看标识
	if(context.containsKey("flow")){
		flow = (String)context.getDataValue("flow");
	}
%>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="/WEB-INF/mvcs/CMISMvc/biz01line/image/pubAction/imagePubAction.jsp" flush="true"/>
<script type="text/javascript">
	
	/*--user code begin--*/
	function returnCus(data){
		CcrAppInfo.cus_id._setValue(data.cus_id._getValue());
		CcrAppInfo.cus_name._setValue(data.cus_name._getValue());
		CcrAppInfo.cus_type._setValue(data.cus_type._getValue()); 
		//checkCusExist();
	}

	function doload(){ 
	//	CcrRatDirect.cus_id._obj.config.url=CcrRatDirect.cus_id._obj.config.url+"&returnMethod=returnCus";
		var flag = CcrAppInfo.approve_status._getValue();
		if(flag=="997"){
			//CusCom.cus_crd_grade._obj._renderHidden(true);  
		}
	}
	
	function CheckDt(){
		var start = CcrAppInfo.app_begin_date._getValue();
		var expiring = CcrAppDetail.congniz_fn_dt._getValue();
		if (start!=null && start!="" &&expiring!=null && expiring!=""){
			if(start>expiring){
				alert("到期日期要>认定日期！！");
				CcrAppDetail.congniz_fn_dt._setValue("");
				return false;
			}		
		} 
		return true;
	}
	
	function doReturn() {
		var url = '<emp:url action="queryCcrRatDirectList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	}

	function toSubmitForm(form){
	    var handleSuccess = function(o){
	        if(o.responseText !== undefined) {
	            try {
	              var jsonstr = eval("("+o.responseText+")");
	              var flag = jsonstr.flag;
	              if(flag == 'success'){
						alert("保存成功");
		          }
	            } catch(e) {
	              alert("保存失败！");
	              return;
	            }
	        }
	     };
	     var handleFailure = function(o){
	     };
	     var callback = {
	         success:handleSuccess,
	         failure:handleFailure
	     };
	     var postData = YAHOO.util.Connect.setForm(form);
	     var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
	  };

	function doSubmitt(){
		var form = document.getElementById("submitForm");
	 	var result = CcrAppInfo._checkAll();
	 	var result1 = CcrAppDetail._checkAll();
	    if(result&&result1){
	    	CcrAppInfo._toForm(form);
	    	CcrAppDetail._toForm(form);
	        toSubmitForm(form);
	    }else {
		    alert("保存失败！\n请检查各标签页面中的必填信息是否遗漏！");
		}
	}

    function doStartCcrDirectApp(){
  	  doSubmitt();
         var _status = CcrRatDirect.approve_status._getValue();
       if(_status!=''&&_status!= '000' &&_status!= '991'&&_status!= '992'){
			alert('该申请所处状态不是【待发起】、【追回】、【打回】不能发起流程申请');
			return;
		}
    	//680:信用评级直接认定申请
	    var _applType = "680";
		var _wfSign  = "";
		var _modelId = "CcrRatDirect";
		var _pkCol   = "serno";
		var _pkVal   = 	CcrRatDirect.serno._getValue();
		//alert(_pkVal);
		//跳转返回页面
		var _rurl=EMPTools.encodeURI('<emp:url action="queryCcrRatDirectList.do"/>');
		
		WfiJoin.cus_id._setValue(CcrRatDirect.cus_id._getValue());
		WfiJoin.cus_name._setValue(CcrRatDirect.cus_name._getValue());
		WfiJoin.amt._setValue("");
	 	var _noteMessageContent = "客户【"+CcrRatDirect.cus_name._getValue() + "】流水号为【"+ _pkVal +"】的信用评级直接认定";
	 	WfiJoin.note_message_content._setValue(_noteMessageContent);
		//WfiJoin.cus_type._setValue(CcrRatDirectList._obj.getParamValue(['cus_type']))
		//load wf variable
		var _variable = "" ;
	    initWFSubmit(_applType, _wfSign, _modelId, _pkCol, _pkVal,_variable,"",_status,_rurl);
	};

	function doImageView(){	//客户信息影像查看
		var data = new Array();
		data['serno'] = CcrAppInfo.cus_id._getValue();	//客户资料的业务编号就填cus_id
		data['cus_id'] = CcrAppInfo.cus_id._getValue();	//客户编号
		data['prd_id'] = 'BASIC';	//业务品种
		data['prd_stage'] = '' ;	//业务阶段：YWSQ业务申请，YWSP业务审批，CZSQ出账申请，CZSH出账审核，DHTZ贷后台账，其他填空
		data['image_action'] = 'View23'	;//影像接口调用类型:影像扫描、影像查看、影像核对。后面数字取接口文档编号
		doPubImageAction(data);
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doload()">
	<emp:tabGroup mainTab="congniz" id="ccrAppTab">
	<emp:tab id="congniz" label="评级直接认定" initial="true" needFlush="true">
	<emp:form id="submitForm" action="updateCcrRatDirectRecord.do" method="POST">
		<emp:gridLayout id="CcrAppInfoGroup" title="评级直接认定" maxColumn="2">
			<emp:text id="CcrAppInfo.serno" label="业务申请编号" maxlength="40" required="false" colSpan="2" readonly="true" hidden="true"/>
			<emp:pop id="CcrAppInfo.cus_id" label="客户码" url="queryAllCusPop.do?cusTypCondition=Com" returnMethod="returnCus" required="true"/>
			<emp:text id="CcrAppInfo.cus_name" label="客户名称 " maxlength="60" required="true" readonly="true" cssElementClass="emp_field_text_input2" colSpan="2"  defvalue="${context.cus_name}"/>	
			<emp:select id="CcrAppInfo.cus_type" label="客户类型" required="false" dictname="STD_ZB_CUS_TYPE" readonly="true" defvalue="${context.cus_type}"/>
		    <emp:select id="CcrAppDetail.adjusted_grade" label="信用等级" required="true" dictname="STD_ZB_CREDIT_GRADE"/> 
		   	<emp:checkbox id="CcrAppDetail.reason_show" label=" " required="false" disabled="true" dictname="STD_ZB_COGNIZ" checkValue="1"/>
		   	<emp:text id="CcrAppDetail.reason_show0" label=" " required="false" onchange="doChange()" hidden="true"/>
			<emp:text id="CcrAppDetail.reason_show1" label=" " required="false" onchange="doChange()" hidden="true"/>
			<emp:text id="CcrAppDetail.reason_show2" label=" " required="false" onchange="doChange()" hidden="true"/>
			<emp:textarea id="CcrAppDetail.congniz_reason" label="认定理由" maxlength="1000" required="false" colSpan="2"/>
			<emp:text id="CcrAppInfo.app_begin_date" label="认定日期" maxlength="10" required="false" readonly="true" defvalue=""/>
			<emp:date id="CcrAppDetail.congniz_fn_dt" label="到期日期" required="true" onblur="CheckDt()" />
			<emp:text id="CcrAppInfo.reportId" label="报表名" hidden="true" />
		    <emp:text id="CcrAppInfo.check_value" label="检查值" hidden="true"/>
		</emp:gridLayout>	
		<emp:gridLayout id="RegGroup" maxColumn="2" title="登记信息">
			<emp:text id="CcrAppInfo.manager_id_displayname" label="主管客户经理" required="true" readonly="true" hidden="false"/>
			<emp:text id="CcrAppInfo.manager_br_id_displayname" label="主管机构" required="true" readonly="true"/>
			<emp:text id="CcrAppInfo.input_id_displayname" label="登记人"  required="false" readonly="true" hidden="false" />
			<emp:text id="CcrAppInfo.input_br_id_displayname" label="登记机构"  required="false" readonly="true" hidden="false" />
			<emp:text id="CcrAppInfo.reg_date" label="登记日期" maxlength="20" required="false" readonly="true" hidden="false" defvalue="$OPENDAY"/>
			<emp:select id="CcrAppInfo.approve_status" label="申请状态" required="false" dictname="WF_APP_STATUS" defvalue="000" readonly="true" hidden="false"/>
			<emp:text id="CcrAppInfo.manager_id" label="主管客户经理" required="false" readonly="false" hidden="true" />
			<emp:text id="CcrAppInfo.manager_br_id" label="主管机构" readonly="false" hidden="true"/>
			<emp:text id="CcrAppInfo.input_id" label="登记人" maxlength="20" required="false" readonly="true" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="CcrAppInfo.input_br_id" label="登记机构" maxlength="20" required="false" readonly="true" hidden="true" defvalue="$organNo"/>
			<emp:text id="CcrAppInfo.restrict_tab" label="restrict_tab" defvalue="false" hidden="true"/>
		</emp:gridLayout>
		</emp:form>
	</emp:tab>
    	<emp:ExtActTab></emp:ExtActTab>
	</emp:tabGroup>	
		<div align="center">
			<br>
			<%if(flow.equals("wf")){ %>
			<%}else{ %>
			<emp:button id="return" label="返回到列表页面"/>
			<%} %>
			<%-- <emp:button id="ImageView" label="影像查看"/> --%>
		</div>

</body>
</html>
</emp:page>
