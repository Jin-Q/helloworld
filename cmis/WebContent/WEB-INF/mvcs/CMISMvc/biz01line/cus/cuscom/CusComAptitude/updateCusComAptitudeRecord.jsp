<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>修改页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	function doUpdateCusComAptitude() {
		var form = document.getElementById("submitForm");
		var result = CusComAptitude._checkAll();
		if(result){
			CusComAptitude._toForm(form)
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
				if(flag=="修改成功"){
					alert("修改成功!");
					doReturn();
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
		var cus_id  =CusComAptitude.cus_id._obj.element.value;
		var editFlag = '${context.EditFlag}';
		var paramStr="CusComAptitude.cus_id="+cus_id+"&EditFlag="+editFlag;
		var url = '<emp:url action="queryCusComAptitudeList.do"/>&'+paramStr;
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
	
	function checkStrData(){
		var crt_date = CusComAptitude.crt_date._getValue();//发证/登记日期
		var com_apt_expired = CusComAptitude.com_apt_expired._getValue();//资质到期日期
		var openDay = '${context.OPENDAY}';
		if(crt_date!=null && crt_date!="" ){
			var flag = CheckDate1BeforeDate2(crt_date,openDay);
			if(!flag){
				alert("发证/登记日期要小于等于当前日期！");
				CusComAptitude.crt_date._setValue("");
			}else{
				if(com_apt_expired!=null && com_apt_expired!=""){
					var ff = CheckDate1BeforeDate2(crt_date,com_apt_expired);
					if(!ff){
						alert("发证/登记日期要小于等于资质到期日期！");
						CusComAptitude.com_apt_expired._setValue("");
					}
				}
		    }
		}
	}
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="updateCusComAptitudeRecord.do" method="POST">
		<emp:gridLayout id="CusComAptitudeGroup" maxColumn="2" title="对公客户资质信息表">
			<emp:text id="CusComAptitude.cus_id" label="客户码" maxlength="30" required="true" readonly="true" hidden="true"/>
			<emp:select id="CusComAptitude.com_apt_typ" label="资质类型" required="false" dictname="STD_ZB_COM_APT_TYP" hidden="true"/>
			<emp:text id="CusComAptitude.com_apt_code" label="资质证书编号" maxlength="40" required="true" readonly="true" hidden="false"/>
			<emp:text id="CusComAptitude.com_apt_name" label="资质名称" maxlength="60" required="true" />
			<emp:text id="CusComAptitude.com_apt_cls" label="资质等级" maxlength="20" required="false" />
			<emp:text id="CusComAptitude.reg_bch_id" label="发证/登记机构" maxlength="80" required="true" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:date id="CusComAptitude.crt_date" label="发证/登记日期" required="true" onblur="checkStrData()"/>
			<emp:date id="CusComAptitude.com_apt_expired" label="资质到期日期"  required="true" onblur="checkStrData()"/>
			<emp:textarea id="CusComAptitude.com_apt_dec" label="资质说明" maxlength="200" required="false" colSpan="2" onkeyup="this.value = this.value.substring(0, 200)"/>
			<emp:textarea id="CusComAptitude.remark" label="备注" maxlength="250" required="false" colSpan="2" onkeyup="this.value = this.value.substring(0, 250)"/>
			<emp:text id="CusComAptitude.input_id" label="登记人" maxlength="20" required="false" readonly="true" hidden="true"/>
			<emp:text id="CusComAptitude.input_br_id" label="登记机构" maxlength="20" required="false" readonly="true" hidden="true"/>
			<emp:date id="CusComAptitude.input_date" label="登记日期" required="false" readonly="true" hidden="true"/>
			<emp:text id="CusComAptitude.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId"/>
			<emp:date id="CusComAptitude.last_upd_date" label="更新日期" required="false" hidden="true" defvalue="$OPENDAY"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="updateCusComAptitude" label="保存" />
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
