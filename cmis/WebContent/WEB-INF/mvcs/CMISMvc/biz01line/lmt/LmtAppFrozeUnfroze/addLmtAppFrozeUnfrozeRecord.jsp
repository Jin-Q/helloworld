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
		var input_id = LmtAppFrozeUnfroze.input_id._getValue();
		//客户码增加查看按钮
		LmtAppFrozeUnfroze.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
		//授信额度编号增加查看按钮
		LmtAppFrozeUnfroze.limit_code._obj.addOneButton("limit_code","查看",getLimitAccNo);
	}
	//客户信息查
	function viewCusInfo(){
		var url = "<emp:url action='getCusViewPage.do'/>&cusId="+LmtAppFrozeUnfroze.cus_id._getValue();
      	url=encodeURI(url); 
      	window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}
	/*授信台账信息查看*/
	function getLimitAccNo(){
		var limit_code = LmtAppFrozeUnfroze.limit_code._getValue();
		var url = "<emp:url action='viewLmtAgrInfo.do'/>?showButton=N&op=view&agr_no="+limit_code;
		url=EMPTools.encodeURI(url);  
		window.open(url,'newwindow','height=538,width=1024,top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	/*POP获取数据*/
	function returnLmtAgrDetail(data){
		var lmt_status = data.lmt_status._getValue();
        
        	if(lmt_status=='10'){
    			LmtAppFrozeUnfroze.agr_no._setValue(data.agr_no._getValue());
    			LmtAppFrozeUnfroze.limit_code._setValue(data.limit_code._getValue());
    			LmtAppFrozeUnfroze.cus_id._setValue(data.cus_id._getValue());
    			LmtAppFrozeUnfroze.cus_id_displayname._setValue(data.cus_id_displayname._getValue());
    			LmtAppFrozeUnfroze.cur_type._setValue(data.cur_type._getValue());
    			LmtAppFrozeUnfroze.cur_type._setValue(data.cur_type._getValue());
    			LmtAppFrozeUnfroze.crd_amt._setValue(data.crd_amt._getValue());

    			LmtAgrDetails.froze_amt_sum._setValue(data.froze_amt._getValue());
    			LmtAppFrozeUnfroze.start_date._setValue(data.start_date._getValue());
    			LmtAppFrozeUnfroze.end_date._setValue(data.end_date._getValue());
    			LmtAppFrozeUnfroze.limit_code_displayname._setValue(data.limit_name_displayname._getValue());
    			LmtAppFrozeUnfroze.prd_id._setValue(data.prd_id._getValue());
    			LmtAppFrozeUnfroze.prd_id_displayname._setValue(data.prd_id_displayname._getValue());
    			
    			
    		}else{
    			alert('请验证额度状态是否为"正常"!');
    		}
       
		}
	/*异步保存*/
	function doAddLmtAppFrozeUnfroze(){
		var form = document.getElementById("submitForm");
		var froze_unfroze_amt = LmtAppFrozeUnfroze.froze_unfroze_amt._getValue();
		
		if(LmtAppFrozeUnfroze._checkAll()){
			if(froze_unfroze_amt != null || froze_unfroze_amt != '0' ||froze_unfroze_amt != 0){
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
				}else{
					alert('请输入冻结金额！');
					}
		}
	}
	/*判断申请的冻结金额小于授信额度*/
	function onChange(){
		var crd_amt = parseFloat(LmtAppFrozeUnfroze.crd_amt._getValue());
		var froze_unfroze_amt = parseFloat(LmtAppFrozeUnfroze.froze_unfroze_amt._getValue());
		var froze_amt_hq  = LmtAppFrozeUnfroze.froze_amt_hq._getValue();/*已凍結金額*/
		var froze_value = crd_amt - froze_amt_hq;
		if( froze_amt_hq == null){
			if(crd_amt > froze_unfroze_amt){
				alert("申请冻结的金额大于授信金额，请重新输入！！！");
				LmtAppFrozeUnfroze.froze_unfroze_amt._setValue("");
				}
			}else{
				froze_amt_hq = parseFloat(froze_amt_hq);
				if(froze_unfroze_amt > froze_value){
					alert("申请冻结的金额大于可用授信金额，请重新输入！！！");
					LmtAppFrozeUnfroze.froze_unfroze_amt._setValue("");
					}
				} 
				
		
		}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onLoad()">
	
	<emp:form id="submitForm" action="addLmtAppFrozeUnfrozeRecord.do" method="POST">
		
		<emp:gridLayout id="LmtAppFrozeUnfrozeGroup" title="冻结申请信息" maxColumn="2">
			
			<emp:pop id="LmtAppFrozeUnfroze.limit_code" label="授信额度编号" url="queryLmtAppFrozeUnfrozeListPop.do?subConndition=lmt_status='10'" returnMethod="returnLmtAgrDetail" readonly="true" />
			<emp:text id="LmtAppFrozeUnfroze.agr_no" label="授信协议编号"   required="true" readonly="true"/>
			<emp:text id="LmtAppFrozeUnfroze.limit_code_displayname" label="额度品种名称"  readonly="true" />
			<emp:select id="LmtAppFrozeUnfroze.app_type" label="申请类型" dictname="STD_ZB_APP_TYPE" required="true" defvalue="03" readonly="true"/>
			<emp:text id="LmtAppFrozeUnfroze.cus_id" label="客户码" maxlength="30"  colSpan="2" cssElementClass="emp_currency_text_readonly"/>
			
			<emp:text id="LmtAppFrozeUnfroze.cus_id_displayname" label="客户名称"  readonly="true" cssElementClass="emp_field_text_long_readonly"/>
			<emp:text id="LmtAppFrozeUnfroze.prd_id" label="适用产品编号"  required="true" colSpan="2" cssElementClass="emp_field_text_long_readonly" />
			<emp:textarea id="LmtAppFrozeUnfroze.prd_id_displayname" label="适用产品名称" required="true" colSpan="2" cssElementClass="emp_field_textarea_readonly"/>
			<emp:select id="LmtAppFrozeUnfroze.cur_type" label="授信币种" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true"/>
			<emp:text id="LmtAppFrozeUnfroze.crd_amt" label="授信金额" maxlength="16" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAppFrozeUnfroze.froze_amt_hq" label="已冻结金额" maxlength="16" readonly="true" defvalue="0" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAppFrozeUnfroze.froze_unfroze_amt" label="冻结金额" maxlength="16" required="true" onblur="onChange()" dataType="Currency"/>
			<emp:text id="LmtAppFrozeUnfroze.start_date" label="授信起始日" maxlength="16" readonly="true" />
			<emp:text id="LmtAppFrozeUnfroze.end_date" label="授信到期日" maxlength="16" readonly="true"  />
			<emp:textarea id="LmtAppFrozeUnfroze.froze_unfroze_resn" label="冻结原因" colSpan="2" ></emp:textarea>
			
			<emp:select id="LmtAppFrozeUnfroze.flow_type" label="流程类型"  required="true" dictname="STD_ZB_FLOW_TYPE" defvalue="01" readonly="true"/>
			<emp:select id="LmtAppFrozeUnfroze.approve_status" label="申请状态" dictname="WF_APP_STATUS" required="true" readonly="true" defvalue="000"/>
		</emp:gridLayout>	
		<emp:gridLayout id="LmtAppFrozeUnfrozeGroup" title="登记信息" maxColumn="2">
			
			<emp:text id="LmtAppFrozeUnfroze.input_id" label="登记人" maxlength="20"   defvalue="${context.currentUserId}" readonly="true" hidden="true"/>
			<emp:text id="LmtAppFrozeUnfroze.input_br_id" label="登记机构" maxlength="20"   defvalue="${context.organNo}" readonly="true" hidden="true"/>
			<emp:text id="LmtAppFrozeUnfroze.input_id_displayname" label="登记人"   readonly="true" />
			<emp:text id="LmtAppFrozeUnfroze.input_br_id_displayname" label="登记机构"   readonly="true" />
			<emp:text id="LmtAppFrozeUnfroze.input_date" label="登记日期" maxlength="10" required="true" defvalue="$OPENDAY" readonly="true"/>
			<emp:text id="LmtAppFrozeUnfroze.serno" label="申请编号" maxlength="40" hidden="true"/>
			
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="addLmtAppFrozeUnfroze" label="确定" op="add"/>
			<emp:button id="return" label="取消"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

