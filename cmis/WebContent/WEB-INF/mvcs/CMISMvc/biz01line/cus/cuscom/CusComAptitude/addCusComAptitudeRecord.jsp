<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>
<%
	String editFlag = request.getParameter("EditFlag");
%>
<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">
	/*--user code begin--*/
	function doAddCusComAptitude(){
		var form = document.getElementById("submitForm");
		var result = CusComAptitude._checkAll();
		if(result){
			CusComAptitude._toForm(form)
			toSubmitForm(form);
		}//else alert("请输入必填项！");
	}
	
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
				if(flag=="新增成功"){
					if(confirm("增加成功！是否继续操作？")){
						var paramStr="CusComAptitude.cus_id="+CusComAptitude.cus_id._obj.element.value+"&EditFlag=<%=editFlag%>";
						var url = '<emp:url action="getCusComAptitudeAddPage.do"/>&'+paramStr;
						url = EMPTools.encodeURI(url);
						window.location = url;
				     }else goback();
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
	}
  
	function goback(){
		var editFlag = '<%=editFlag%>';
		var paramStr="CusComAptitude.cus_id="+CusComAptitude.cus_id._obj.element.value+"&EditFlag="+editFlag;
		var stockURL = '<emp:url action="queryCusComAptitudeList.do"/>&'+paramStr;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	};

	function doReturn(){
		goback();
	}

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
	/*--user code end--*/

</script>
</head>
<body class="page_content" >
	
	<emp:form id="submitForm" action="addCusComAptitudeRecord.do" method="POST">
		
		<emp:gridLayout id="CusComAptitudeGroup" title="对公客户资质信息" maxColumn="2">
			<emp:text id="CusComAptitude.cus_id" label="客户码" maxlength="30" required="true" defvalue="${context.CusComFinaBond.cus_id}" hidden="true"/>
			<emp:select id="CusComAptitude.com_apt_typ" label="资质类型" required="false" dictname="STD_ZB_COM_APT_TYP" hidden="true"/>
			<emp:text id="CusComAptitude.com_apt_code" label="资质证书编号" maxlength="40" required="true"/>
			
			<emp:text id="CusComAptitude.com_apt_name" label="资质名称" maxlength="60" required="true" />
			<emp:text id="CusComAptitude.com_apt_cls" label="资质等级" maxlength="20" required="false" />
			<emp:text id="CusComAptitude.reg_bch_id" label="发证/登记机构" maxlength="80" required="true" colSpan="2" cssElementClass="emp_field_text_input2"/>
			<emp:date id="CusComAptitude.crt_date" label="发证/登记日期" required="true" onblur="checkStrData()"/>
			<emp:date id="CusComAptitude.com_apt_expired" label="资质到期日期"  required="true" onblur="checkStrData()"/>
			<emp:textarea id="CusComAptitude.com_apt_dec" label="资质说明" maxlength="200" required="false" colSpan="2" onkeyup="this.value = this.value.substring(0, 200)"/>
			<emp:textarea id="CusComAptitude.remark" label="备注" maxlength="250" required="false" colSpan="2" onkeyup="this.value = this.value.substring(0, 250)"/>
			<emp:text id="CusComAptitude.input_id" label="登记人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="CusComAptitude.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="$organNo"/>
			<emp:date id="CusComAptitude.input_date" label="登记日期" required="false" hidden="true" defvalue="$OPENDAY"/>
			<emp:text id="CusComAptitude.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true"/>
			<emp:date id="CusComAptitude.last_upd_date" label="更新日期" required="false" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="addCusComAptitude" label="保存" />
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

