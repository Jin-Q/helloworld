<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	function doReturn() {
		var url = '<emp:url action="queryLmtAppFrozeUnfrozeList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	function onLoad(){
		
		//客户码增加查看按钮
		LmtAppFrozeUnfroze.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
		//window.location.reload();//重载当前页面
	}
	//客户信息查
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+LmtAppFrozeUnfroze.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}
	
	/*POP获取数据*/
	function returnLmtAgrDetail(data){
		var lmt_status = data.lmt_status._getValue();
        
        	if(lmt_status=='10'){
    			LmtAppFrozeUnfroze.agr_no._setValue(data.agr_no._getValue());
    			LmtAppFrozeUnfroze.limit_code._setValue(data.limit_code._getValue());
    			LmtAppFrozeUnfroze.limit_code_displayname._setValue(data.limit_name_displayname._getValue());
    			LmtAppFrozeUnfroze.cus_id._setValue(data.cus_id._getValue());
    			LmtAppFrozeUnfroze.cus_id_displayname._setValue(data.cus_id_displayname._getValue());
    			LmtAppFrozeUnfroze.crd_amt._setValue(data.crd_amt._getValue());

    			LmtAppFrozeUnfroze.start_date._setValue(data.start_date._getValue());
    			LmtAppFrozeUnfroze.end_date._setValue(data.end_date._getValue());
    			LmtAppFrozeUnfroze.prd_id._setValue(data.prd_id._getValue());
    			LmtAppFrozeUnfroze.froze_amt_hq._setValue(data.froze_amt_hq._getValue());
    			
    			
    		}else{
    			alert('请验证额度状态是否为"正常"!');
    		}
       
		}
	/*异步保存*/
	function doAddLmtAppFrozeUnfroze(){
		var form = document.getElementById("submitForm");
		if(LmtAppFrozeUnfroze._checkAll()){
			LmtAppFrozeUnfroze._toForm(form);
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
						alert("保存成功！");
						var url = '<emp:url action="queryLmtAppFrozeUnfrozeList.do"/>';
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else{
						alert("保存失败！");
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
			var serno = LmtAppFrozeUnfroze.serno._getValue();
			var url = '<emp:url action="addLmtAppFrozeUnfrozeRecord.do"/>?serno='+serno;
			url = EMPTools.encodeURI(url);
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
		}
	}

	//下一步方法
	function doNext(){
		var form = document.getElementById('submitForm');
		var result = LmtAppFrozeUnfroze._checkAll();
		var froze_amt_hq = LmtAppFrozeUnfroze.froze_amt_hq._getValue();
		if(froze_amt_hq != null && froze_amt_hq != "" && froze_amt_hq != 0.0){
			if(!interRisk()){
				return false;
			}
			//var approve_status  = LmtAppFrozeUnfroze.approve_status._getValue();
			//alert(approve_status);
		    if(result){
			    	LmtAppFrozeUnfroze._toForm(form);
			    	form.submit();
			}
	    }else {
		    alert("冻结金额为零，无需申请解冻！");
		}
	}
	//风险拦截
    function interRisk(){
	   var serno = LmtAppFrozeUnfroze.limit_code._getValue();
	   var _applType="";
	   var _modelId="LmtAppFrozeUnfroze";
	   var _pkVal=serno;
	   var _preventIdLst="FFFA278701EDEE60A0011B0C806F5137";
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
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onLoad()">
	
	<emp:form id="submitForm" action="getLmtAppUnfrozeNextAddPage.do" method="POST">
		
		<emp:gridLayout id="LmtAppFrozeUnfrozeGroup" title="解冻申请信息" maxColumn="2">
			
			<emp:pop id="LmtAppFrozeUnfroze.limit_code" label="授信额度编号" url="queryLmtAppFrozeUnfrozeListPop.do?subConndition=lmt_status='10'" returnMethod="returnLmtAgrDetail"  />
			<emp:text id="LmtAppFrozeUnfroze.agr_no" label="授信协议编号"   required="true" readonly="true"/>
			<emp:text id="LmtAppFrozeUnfroze.limit_code_displayname" label="额度品种名称"  readonly="true" />
			<emp:text id="LmtAppFrozeUnfroze.cus_id" label="客户码" maxlength="30"  cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAppFrozeUnfroze.cus_id_displayname" label="客户名称"  readonly="true" cssElementClass="emp_field_text_long_readonly"/>
			
			
			<emp:select id="LmtAppFrozeUnfroze.app_type" label="申请类型" dictname="STD_ZB_APP_TYPE" required="true" defvalue="04" readonly="true" hidden="true"/>
			<emp:text id="LmtAppFrozeUnfroze.prd_id" label="适用产品编号"   colSpan="2" cssElementClass="emp_field_text_long_readonly" hidden="true"/>
			<emp:text id="LmtAppFrozeUnfroze.crd_amt" label="授信金额" maxlength="16" readonly="true" dataType="Currency"  hidden="true"/>
			<emp:text id="LmtAppFrozeUnfroze.froze_amt_hq" label="已冻结金额" maxlength="16"  defvalue="0" dataType="Currency" hidden="true"/>
			<emp:text id="LmtAppFrozeUnfroze.start_date" label="授信起始日" maxlength="16" readonly="true" hidden="true"/>
			<emp:text id="LmtAppFrozeUnfroze.end_date" label="授信到期日" maxlength="16" readonly="true"  hidden="true"/>
			<emp:select id="LmtAppFrozeUnfroze.approve_status" label="申请状态" dictname="WF_APP_STATUS" required="true" readonly="true" defvalue="000" hidden="true"/>
			
			<emp:text id="LmtAppFrozeUnfroze.input_id" label="登记人" maxlength="20"   defvalue="${context.currentUserId}" readonly="true" hidden="true"/>
			<emp:text id="LmtAppFrozeUnfroze.input_br_id" label="登记机构" maxlength="20"   defvalue="${context.organNo}" readonly="true" hidden="true"/>
		</emp:gridLayout>
		
		
		
		<div align="center">
			<br>
			<emp:button id="next" label="下一步" />
			<emp:button id="return" label="取消"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

