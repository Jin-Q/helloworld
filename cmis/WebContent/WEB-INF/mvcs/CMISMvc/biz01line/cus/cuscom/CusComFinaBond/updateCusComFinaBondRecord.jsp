<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">

	/*--user code begin--*/
	function doUpdateCusComFinaBond() {
		var form = document.getElementById("submitForm");
		var result = CusComFinaBond._checkAll();
		if(result){
			CusComFinaBond._toForm(form)
			toSubmitForm(form);
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
					alert("修改成功!");
					doReturn();
				}else if(flag=="exists"){
					alert('该债券编号已经存在，请检查！');
				}else {
					alert(flag);
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
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	};
	
	function doReturn() {
		var editFlag = '${context.EditFlag}';
		var cus_id  =CusComFinaBond.cus_id._obj.element.value;
		var paramStr="CusComFinaBond.cus_id="+cus_id+"&EditFlag="+editFlag;
		var url = '<emp:url action="queryCusComFinaBondList.do"/>&'+paramStr;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	//起始日期不能大于到期日期
	function CheckDate(){
		var openDay = '${context.OPENDAY}';
		var str_date = CusComFinaBond.com_bond_pub_dt._getValue();
		var end_date = CusComFinaBond.com_bond_end_dt._getValue();
		if(str_date!=null&&str_date!=''){
			if(str_date>openDay){
				alert('债券发行日期不能大于当前日期！');
				CusComFinaBond.com_bond_pub_dt._setValue('');
			}
		}
		if(str_date!=null&&str_date!=''&&end_date!=null&&end_date!=''){
			if(str_date>end_date){
				alert('债券发行日期不能大于债券到期日期！');
				CusComFinaBond.com_bond_end_dt._setValue('');
			}
		}
	}
	
	function cheakAmt(amt){
	  var getAmt = parseFloat(amt._getValue());
	  if(getAmt<0){
		  alert("金额值不能为负数！");
		  amt._obj.element.value="";
	   }
	}
			
	function ChangeFlg(){
       var flg = CusComFinaBond.com_bond_mrk_flg._obj.element.value;
       if(flg=='2'){
    	   CusComFinaBond.com_bond_mrk_place._obj._renderReadonly(true);
    	   CusComFinaBond.com_bond_mrk_place._obj._renderRequired(false);
    	   CusComFinaBond.com_bond_mrk_place._setValue("");
    	   
    	   CusComFinaBond.com_bond_mrk_brs._obj._renderReadonly(true);
    	   CusComFinaBond.com_bond_mrk_brs._obj._renderRequired(false);
    	   CusComFinaBond.com_bond_mrk_brs._obj.element.value="";
           
        }else if(flg=='1'){
    	   CusComFinaBond.com_bond_mrk_place._obj._renderReadonly(false);
    	   CusComFinaBond.com_bond_mrk_place._obj._renderRequired(true);
    	   CusComFinaBond.com_bond_mrk_brs._obj._renderReadonly(false);
    	   CusComFinaBond.com_bond_mrk_brs._obj._renderRequired(true);
        }
	}
	
	function doOnload(){
		ChangeFlg();
	}
	/*--user code end--*/
</script>
</head>
<body class="page_content" onload="doOnload()">
	
	<emp:form id="submitForm" action="updateCusComFinaBondRecord.do" method="POST">
		<emp:gridLayout id="CusComFinaBondGroup" maxColumn="2" title="发行债券信息">
			<emp:text id="CusComFinaBond.cus_id" label="客户码" maxlength="30" required="true" readonly="true" hidden="true"/>
			<emp:text id="CusComFinaBond.seq" label="序号" maxlength="38"  readonly="true" hidden="true"/>
			<emp:text id="CusComFinaBond.com_bond_no" label="债券编号" maxlength="60" required="true" colSpan="2"/>
			<emp:text id="CusComFinaBond.com_bond_name" label="债券名称" maxlength="60" required="true" />
			<emp:text id="CusComFinaBond.com_bond_trm" label="债券期限(月)" maxlength="38" required="true" dataType="Long" />
			<emp:date id="CusComFinaBond.com_bond_pub_dt" label="债券发行日期" required="true" onblur="CheckDate()" />
			<emp:date id="CusComFinaBond.com_bond_end_dt" label="债券到期日期" required="true" onblur="CheckDate()" />
			<emp:select id="CusComFinaBond.com_bond_typ" label="债券类型" required="true" dictname="STD_ZB_COM_BOND_TYP"/>
			<emp:select id="CusComFinaBond.com_bond_cls" label="债券分类" required="true" dictname="STD_ZB_COM_BOND_CLS"/>
			<emp:select id="CusComFinaBond.com_bond_cur_typ" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" />
			<emp:text id="CusComFinaBond.com_bond_amt" label="总金额(元)" maxlength="18" required="true" dataType="Currency" onblur="cheakAmt(CusComFinaBond.com_bond_amt)"/>
			<emp:text id="CusComFinaBond.com_bond_rate" label="年利率" maxlength="16" required="true" dataType="Rate" colSpan="2" />
			<emp:select id="CusComFinaBond.com_bond_mrk_flg" label="是否上市" required="true" dictname="STD_ZX_YES_NO" onblur="ChangeFlg()"/>
			<emp:select id="CusComFinaBond.com_bond_mrk_place" label="上市地" required="true" dictname="STD_ZX_LISTED" />
			<emp:text id="CusComFinaBond.com_bond_mrk_brs" label="交易所名称" maxlength="60" required="false" colSpan="2"/>
			<emp:select id="CusComFinaBond.com_acct_typ" label="账户类型" required="true" colSpan="2" dictname="STD_ZB_COM_ACC_TYP" />
			<emp:text id="CusComFinaBond.com_bond_crt_info" label="债券等级" maxlength="60" required="false" colSpan="2"/>
			<emp:text id="CusComFinaBond.com_bond_crt_org" label="评级机构" maxlength="80" required="false" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:text id="CusComFinaBond.com_bond_eva_amt" label="债券评估价(元)" maxlength="18" required="false" dataType="Currency" onblur="cheakAmt(CusComFinaBond.com_bond_eva_amt)"/>
			<emp:select id="CusComFinaBond.com_bond_assure_means" label="债券的担保方式" required="false" dictname="STD_ZB_ASSURE_MEANS"/>
			<emp:textarea id="CusComFinaBond.remark" label="备注" maxlength="250" required="false" colSpan="2" onkeyup="this.value = this.value.substring(0, 250)"/>
			<emp:text id="CusComFinaBond.input_id" label="登记人" maxlength="20" required="false" readonly="true" hidden="true"/>
			<emp:text id="CusComFinaBond.input_br_id" label="登记机构" maxlength="20" required="false" readonly="true" hidden="true"/>
			<emp:date id="CusComFinaBond.input_date" label="登记日期" required="false" readonly="true" hidden="true"/>
			<emp:text id="CusComFinaBond.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId"/>
			<emp:date id="CusComFinaBond.last_upd_date" label="更新日期" required="false" hidden="true" defvalue="$OPENDAY"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="updateCusComFinaBond" label="保存" />
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
