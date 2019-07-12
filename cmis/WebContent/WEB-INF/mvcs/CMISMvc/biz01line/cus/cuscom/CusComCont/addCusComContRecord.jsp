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
<style type="text/css">
.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	text-align:left;
	width:450px;
}
</style>
<script type="text/javascript">
	function doAddCusComCont(){
		var form = document.getElementById("submitForm");
		var result = CusComCont._checkAll();
		if(result){
			CusComCont._toForm(form)
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
				if(flag=="success"){
					if(confirm("增加成功！是否继续操作？")){
						var paramStr="CusComCont.cus_id="+CusComCont.cus_id._obj.element.value+"&EditFlag=<%=editFlag%>";
						var url = '<emp:url action="getCusComContAddPage.do"/>&'+paramStr;
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
		var paramStr="CusComCont.cus_id="+CusComCont.cus_id._obj.element.value+"&EditFlag=<%=editFlag%>";
		var stockURL = '<emp:url action="queryCusComContList.do"/>&'+paramStr;
		stockURL = EMPTools.encodeURI(stockURL);
		window.location = stockURL;
	}
	/*--user code begin--*/
	function doReturn(){
		goback();
	}
	
	function onReturnComAddr(date){
		CusComCont.com_addr._obj.element.value=date.id;
		CusComCont.com_addr_displayname._obj.element.value=date.label;
		searchComZipCode(date.id);
	}

	//根据地址获取相对应的邮政编码
	function searchComZipCode(date){
		var handleSuccess = function(o){
            if(o.responseText !== undefined) {
               try {
                 var jsonstr = eval("("+o.responseText+")");
               } catch(e) {
                 alert("获取失败！");
                 return;
               }
               var flag = jsonstr.flag;
               if(flag=="success"){
                   var post_code = jsonstr.post_code;
                   CusComCont.com_zip_code._setValue(post_code);
               }
            }
        };
        var handleFailure = function(o){
        };
        var callback = {
            success:handleSuccess,
            failure:handleFailure
        };
        var post_code = date;
        var url = '<emp:url action="getComAddr2Pcode.do"/>?post_addr='+post_code;
  	  	url = EMPTools.encodeURI(url);
  	  	var postData = YAHOO.util.Connect.setForm();
        var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
	}
	/*--user code end--*/
</script>
</head>
<body class="page_content">
	<emp:form id="submitForm" action="addCusComContRecord.do" method="POST">
		<emp:gridLayout id="CusComContGroup" title="联系信息" maxColumn="2">
			<emp:text id="CusComCont.cus_id" label="客户码" maxlength="30" required="true" hidden="true"/>
			<emp:text id="CusComCont.seq" label="序号" maxlength="38" readonly="true" required="false" hidden="true"/>
			<emp:select id="CusComCont.com_addr_typ" label="地址类型" required="true" dictname="STD_ZB_COM_ADDR_TYP"/>
			<emp:text id="CusComCont.com_addr" label="通讯地址" colSpan="2" hidden="true" />
			<emp:pop id="CusComCont.com_addr_displayname" label="通讯地址" colSpan="2" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL"
				returnMethod="onReturnComAddr" cssElementClass="emp_field_text_input2" required="true" />
			<emp:text id="CusComCont.street_addr" label="街道" required="false" cssElementClass="emp_field_text_input2" maxlength="100" colSpan="2" />
			<emp:text id="CusComCont.com_zip_code" label="邮政编码" maxlength="6" required="false" dataType="Postcode" />
			<emp:text id="CusComCont.com_phn_code" label="联系电话" maxlength="35" required="true" dataType="Phone" />
			<emp:text id="CusComCont.com_fax_code" label="传真电话" maxlength="35" required="false" dataType="Phone" />
			<emp:textarea id="CusComCont.remark" label="备注" maxlength="250" required="false" colSpan="2" onkeyup="this.value = this.value.substring(0, 250)"/>
			<emp:text id="CusComCont.input_id" label="登记人" maxlength="20" required="false" hidden="true" defvalue="$currentUserId"/>
			<emp:text id="CusComCont.input_br_id" label="登记机构" maxlength="20" required="false" hidden="true" defvalue="$organNo"/>
			<emp:date id="CusComCont.input_date" label="登记日期" required="false" hidden="true" defvalue="$OPENDAY"/>
			<emp:text id="CusComCont.last_upd_id" label="更新人" maxlength="20" required="false" hidden="true"/>
			<emp:date id="CusComCont.last_upd_date" label="更新日期" required="false" hidden="true"/>
		</emp:gridLayout>
		<div align="center">
			<emp:button id="addCusComCont" label="保存" />
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>