<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

	/*--user code begin--*/
	
	function onLoad(){
		
		//客户码增加查看按钮
		LmtAppFrozeUnfroze.cus_id._obj.addOneButton('view12','查看',viewCusInfo);
		//window.location.reload();//重载当前页面
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
	function doReturn() {
		var url = '<emp:url action="queryLmtAppUnfrozeList.do"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};


  
   	/*异步修改*/
	function doUpdateLmtAppFrozeUnfroze() {
		var form = document.getElementById("submitForm");
		var result = LmtAppFrozeUnfroze._checkAll();
		var approve_status = LmtAppFrozeUnfroze.approve_status._getValue();
		
		if(result){
			if(approve_status == "997"){
				alert('此条记录已经提交通过，不可修改！');
				window.location.reload();
				}else if(approve_status == "000"){
					LmtAppFrozeUnfroze._toForm(form)
					toSubmitForm(form);
					}
		}//else alert("请输入必填项！");
	};
	
	function toSubmitForm(form){
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr define error!"+e);
					return;
				}
				var flag = jsonstr.flag;
				if(flag=="success"){
					alert("修改成功！");
					doReturn();
			     }else{
				     alert("修改失败！")
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
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	};

	/*判断申请的解冻金额小于已冻结金额*/
	function onChange(){
		var froze_unfroze_amt = LmtAppFrozeUnfroze.froze_unfroze_amt._getValue();/*获取当前解冻金额*/
		var froze_amt_hq  = LmtAgrDetails.froze_amt_hq._getValue();/*已冻结金额*/
		if( froze_amt_hq == null){
				alert("冻结金额为零，无可解冻金额！！！");
				LmtAppFrozeUnfroze.froze_unfroze_amt._setValue("");
				
			}else if(froze_unfroze_amt == null){
				alert('请输入本次解冻金额！');
				LmtAppFrozeUnfroze.froze_unfroze_amt._setValue("");
				}else {
					froze_amt_hq = parseFloat(froze_amt_hq);
					froze_unfroze_amt = parseFloat(froze_unfroze_amt);
					if(froze_amt_hq < froze_unfroze_amt){
						alert("申请解冻的金额大于已冻结金额，请重新输入！！！");
						LmtAppFrozeUnfroze.froze_unfroze_amt._setValue("");
					} 
		}
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="onLoad()">
	
	<emp:form id="submitForm" action="updateLmtAppFrozeUnfrozeRecord.do" method="POST">
		
		<emp:gridLayout id="LmtAppFrozeUnfrozeGroup" title="解冻申请信息" maxColumn="2">
			
			<emp:text id="LmtAppFrozeUnfroze.limit_code" label="授信额度编号"  readonly="true" />
			<emp:text id="LmtAppFrozeUnfroze.agr_no" label="授信协议编号"   required="true" readonly="true"/>
			<emp:text id="LmtAgrDetails.limit_name_displayname" label="额度品种名称"  readonly="true" />
			<emp:select id="LmtAppFrozeUnfroze.app_type" label="申请类型" dictname="STD_ZB_APP_TYPE" required="true" defvalue="04" readonly="true"/>
			<emp:text id="LmtAppFrozeUnfroze.cus_id" label="客户码" maxlength="30" colSpan="2" cssElementClass="emp_currency_text_readonly"/>
			
			<emp:text id="LmtAppFrozeUnfroze.cus_id_displayname" label="客户名称"  readonly="true" cssElementClass="emp_field_text_long_readonly"/>
			<emp:text id="LmtAgrDetails.prd_id" label="适用产品编号" maxlength="200"  colSpan="2" cssElementClass="emp_field_text_long_readonly" />
			<emp:textarea id="LmtAgrDetails.prd_id_displayname" label="适用产品名称"  colSpan="2" cssElementClass="emp_field_textarea_readonly"/>
			<emp:select id="LmtAppFrozeUnfroze.cur_type" label="授信币种" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true"/>
			<emp:text id="LmtAppFrozeUnfroze.crd_amt" label="授信金额" maxlength="16" readonly="true" defvalue="0" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAgrDetails.froze_amt_hq" label="已冻结金额" maxlength="16" readonly="true" defvalue="0" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAppFrozeUnfroze.froze_unfroze_amt" label="解冻金额" maxlength="16" required="true" onchange="onChange()" dataType="Currency"/>
			<emp:text id="LmtAgrDetails.start_date" label="授信起始日" maxlength="16" readonly="true" />
			<emp:text id="LmtAgrDetails.end_date" label="授信到期日" maxlength="16" readonly="true"  />
			<emp:textarea id="LmtAppFrozeUnfroze.froze_unfroze_resn" label="解冻原因" colSpan="2" ></emp:textarea>
			
			<emp:select id="LmtAppFrozeUnfroze.flow_type" label="流程类型"  required="true" dictname="STD_ZB_FLOW_TYPE" defvalue="01" readonly="true"/>
			<emp:select id="LmtAppFrozeUnfroze.approve_status" label="申请状态" dictname="WF_APP_STATUS" required="true" readonly="true" defvalue="000"/>
		</emp:gridLayout>	
		<emp:gridLayout id="LmtAppFrozeUnfrozeGroup" title="登记信息" maxColumn="2">
			<emp:text id="LmtAppFrozeUnfroze.input_id_displayname" label="登记人"   readonly="true" />
			<emp:text id="LmtAppFrozeUnfroze.input_br_id_displayname" label="登记机构"   readonly="true" />
			<emp:text id="LmtAppFrozeUnfroze.input_date" label="登记日期" maxlength="10" required="true"  readonly="true"/>
			
			<emp:text id="LmtAppFrozeUnfroze.serno" label="申请编号" colSpan="1" maxlength="40" hidden="true"/>
			
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="updateLmtAppFrozeUnfroze" label="确定" op="update"/>
			<emp:button id="return" label="取消"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

